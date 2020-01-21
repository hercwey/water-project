package com.learnbind.ai.ccb.processor;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.learnbind.ai.base.common.DateUtil;
import com.learnbind.ai.ccb.base64.Base64Util;
import com.learnbind.ai.ccb.business.BodyParams;
import com.learnbind.ai.ccb.business.HeaderParams;
import com.learnbind.ai.ccb.business.QueryAccountBalanceParams;
import com.learnbind.ai.ccb.business.QueryAccountDetailParams;
import com.learnbind.ai.ccb.business.TransferParams;
import com.learnbind.ai.ccb.business.TransferResultParams;
import com.learnbind.ai.ccb.des.DESede;
import com.learnbind.ai.ccb.md5.MD5withRSA;
import com.learnbind.ai.ccb.requestpacket.AccountForDetail;
import com.learnbind.ai.ccb.requestpacket.AccountForDetailList;
import com.learnbind.ai.ccb.requestpacket.AccountInfoForBalance;
import com.learnbind.ai.ccb.requestpacket.AccountInfoForBalanceList;
import com.learnbind.ai.ccb.requestpacket.CCBTransaction;
import com.learnbind.ai.ccb.requestpacket.FileInfo;
import com.learnbind.ai.ccb.requestpacket.FileInfoList;
import com.learnbind.ai.ccb.requestpacket.RequestBodyBatchWithhold;
import com.learnbind.ai.ccb.requestpacket.RequestBodyQueryAccountBalance;
import com.learnbind.ai.ccb.requestpacket.RequestBodyQueryAccountDetail;
import com.learnbind.ai.ccb.requestpacket.RequestBodyQueryTransferResult;
import com.learnbind.ai.ccb.requestpacket.RequestBodyQueryCertificateDetail;
import com.learnbind.ai.ccb.requestpacket.RequestBodyQueryCertificateState;
import com.learnbind.ai.ccb.requestpacket.RequestBodyTransfer;
import com.learnbind.ai.ccb.requestpacket.TransactionRequestHeader;
import com.learnbind.ai.ccb.sdk.rsa.GenKey;
import com.learnbind.ai.common.util.EntityUtils;

/**
 * 下行报文处理器 生成请求报文
 * 
 * @author lenovo
 *
 */
public class DownPacketProcessor {

	/**
	 * 功能:生成签到报文
	 * @param chanl_cust_no  电子银行合约编号
	 * @Param txn_stff_id    交易人员编号
	 * @Param txn_itt_ip_adr 交易发起方的IP地址
	 * @return 签到报文(XML格式)
	 */
	public static String genP1OPME00X(String transactionCode,HeaderParams headerParams) {
		String returnXML = "";
		TransactionRequestHeader transactionHeader = createTransactionHeaderRequest(transactionCode);
		
		//设置报文中字段信息
		setRequestHeaderParams(transactionHeader,headerParams);

		// 创建一个xml文档
		Document doc = DocumentHelper.createDocument();
		// doc.addComment("这里是注释"); //向xml文件中添加注释
		// (1)创建一个名为Transaction的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = doc.addElement("Transaction");

		// (2)在root节点下创建两个子节点,分别是 Transaction_Header Transaction_Body
		Element header = root.addElement("Transaction_Header");

		// 采用MAP将请求参数加入到DOM中
		Element msgLenElem = null;
		List<Map<String, Object>> list = EntityUtils.entityToListMap(transactionHeader);
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				Element elem = header.addElement(key);
				elem.addCDATA((String) map.get(key));
				// 得到消息长度元素引用.
				if (key.equalsIgnoreCase("SYS_MSG_LEN")) {
					msgLenElem = elem;
				}
			}
		}

		// (3)在body子节点下加入request子节点
		Element body = root.addElement("Transaction_Body");
		body.addElement("request");

		// 先生成一次XML,计算XML的长度
		String tempXML = createXMLStr(doc);
		// 重新置报文长度 10位固定长度.不足补0
		int msgLen = tempXML.length();
		String msgLenStr = String.format("%010d", msgLen);
		msgLenElem.setText("");
		msgLenElem.addCDATA(msgLenStr);

		returnXML = createXMLStr(doc);
		return returnXML;
	}

	
	/**
	 * 批量代扣制单
	 * @param headerParams   请求头参数
	 * @param listPack       文件列表
	 * @param entrst_prj_id  委托项目编号
	 * @Param prj_use_id     项目用途编号
	 * @Param etrunt_accno	  委托单位帐号
	 * @return
	 */	
	
	/**
	 * @param headerParams	head通用参数
	 * @param bodyParams	body通用参数
	 * @param listPack		fileList 已经上传的某个文件(为了扩展性,采用List容器保存文件)
	 * @return
	 */
	public static String genP1CLP1051(HeaderParams headerParams,BodyParams bodyParams,FileInfoList listPack) {
		String returnXML = "";

		//生成请求报文头
		TransactionRequestHeader transactionHeader = createTransactionHeaderRequest(
				CCBTransaction.TRANSACTION_CODE_BATCH_WITHHOLD);
		
		//设置报文中字段信息
		setRequestHeaderParams(transactionHeader,headerParams);
//		transactionHeader.setCHNL_CUST_NO(headerParams.getChanl_cust_no());
//		transactionHeader.setTXN_STFF_ID(headerParams.getTxn_stff_id());
//		transactionHeader.setTxn_Itt_IP_Adr(headerParams.getTxn_itt_ip_adr());

		// 创建一个xml文档
		Document doc = DocumentHelper.createDocument();
		// doc.addComment("这里是注释"); //向xml文件中添加注释
		// (1)创建一个名为Transaction的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = doc.addElement("Transaction");

		// (2)在root节点下创建两个子节点,分别是 Transaction_Header Transaction_Body
		Element header = root.addElement("Transaction_Header");

		// 采用MAP将请求参数加入到DOM中
		Element msgLenElem = null; // 报文长度
		List<Map<String, Object>> list = EntityUtils.entityToListMap(transactionHeader);
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				Element elem = header.addElement(key);
				elem.addCDATA((String) map.get(key));
				// 得到消息长度元素引用.
				if (key.equalsIgnoreCase("SYS_MSG_LEN")) {
					msgLenElem = elem;
				}
			}
		}

		// (3)在body子节点下加入request子节点
		Element body = root.addElement("Transaction_Body");

		// (4)增加body下的内容
		Element requestElement = body.addElement("request");

		// 生成文件列表结点
		Element fileListPackElement = requestElement.addElement("FILE_LIST_PACK");
		Element fileNumElement = fileListPackElement.addElement("FILE_NUM");
		fileNumElement.addCDATA(Integer.toString(listPack.getFileNum()));

		List<FileInfo> tempFileInfoList = listPack.getFileList();
		for (FileInfo file : tempFileInfoList) {
			Element tempElem = null;
			Element fileInfoElement = fileListPackElement.addElement("FILE_INFO");
			tempElem = fileInfoElement.addElement("FILE_NAME");
			tempElem.addCDATA(file.getFILE_NAME());

			tempElem = fileInfoElement.addElement("Msg_Smy");
			tempElem.addCDATA(file.getMsg_Smy());
		}

		//批量代扣请求BODY
		RequestBodyBatchWithhold requestBody = new RequestBodyBatchWithhold();
		//set request body attribute
		FileInfo fileInfo=tempFileInfoList.get(0);
		requestBody.setTxn_SN(fileInfo.getTxn_SN());  					//(1)set field1  凭证号		
		requestBody.setEBnk_SvAr_ID(headerParams.getChanl_cust_no());  	//(2)set field2  合约号
		requestBody.setEntrst_Prj_ID(bodyParams.getEntrst_prj_id());  					//(3)set field3  委托项目号
		requestBody.setPrj_Use_ID(bodyParams.getPrj_use_id());							//(4)set field4  项目用途号
		requestBody.setEtrUnt_AccNo(bodyParams.getEtrunt_accno());						//(5)set field5	 委托单位帐号
		requestBody.setTDP_ID(headerParams.getTxn_stff_id());							//(6)set field6  制单员编号
		requestBody.setOrig_File_Nm(fileInfo.getFILE_NAME());							//(7)set field7  批量时必须输入，用于记录客户上传批量文件的原始文件名称
		requestBody.setVCHR_TP_CODE(CCBTransaction.TRANSACTION_VCHR_TP_CODE_BATCH);  //(8)批量处理  凭证类型  0:单笔 1:批量
		
		//TODO 
		//请求金额及笔数暂时未设置.
		
		
		List<Map<String, Object>> list1 = EntityUtils.entityToListMap(requestBody);
		for (Map<String, Object> map : list1) {
			for (String key : map.keySet()) {
				Element elem = requestElement.addElement(key);
				elem.addCDATA((String) map.get(key));
			}
		}

		// 先生成一次XML,计算XML的长度
		String tempXML = createXMLStr(doc);
		// 重新置报文长度 10位固定长度.不足补0
		int msgLen = tempXML.length();
		String msgLenStr = String.format("%010d", msgLen);
		msgLenElem.setText("");
		msgLenElem.addCDATA(msgLenStr);

		returnXML = createXMLStr(doc);
		return returnXML;
	}

	/**
	 * 用于直连客户根据查询条件分页查询凭证状态信息
	 * 代发代扣直联单据查询 生成请求报文
	 * @param bus_url  CCB端提供的URL
	 * @Param txn_sn	凭证编号(批量文件所对应的编号)   凭证编号(客户端生成)----->上传的批量代扣文件(凭证)
	 * @param headerParams	报文头参数
	 * @param bodyParams   报文BODY参数
	 * @return  生成的请求报文
	 */
	public static String genP1CLP1054(String txn_sn,HeaderParams headerParams,BodyParams bodyParams) {
		String returnXML = "";
		TransactionRequestHeader transactionHeader = createTransactionHeaderRequest(
				CCBTransaction.TRANSACTION_CODE_QUERY_WITHHOLD_RECEIPT);
		
				
		//设置报文中字段信息
		setRequestHeaderParams(transactionHeader,headerParams);

		// 创建一个xml文档
		Document doc = DocumentHelper.createDocument();
		// doc.addComment("这里是注释"); //向xml文件中添加注释
		// (1)创建一个名为Transaction的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = doc.addElement("Transaction");

		// (2)在root节点下创建两个子节点,分别是 Transaction_Header Transaction_Body
		Element header = root.addElement("Transaction_Header");

		// 采用MAP将请求参数加入到DOM中
		Element msgLenElem = null; // 报文长度
		List<Map<String, Object>> list = EntityUtils.entityToListMap(transactionHeader);
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				Element elem = header.addElement(key);
				elem.addCDATA((String) map.get(key));
				// 得到消息长度元素引用.
				if (key.equalsIgnoreCase("SYS_MSG_LEN")) {
					msgLenElem = elem;
				}
			}
		}

		// (3)在body子节点下加入request子节点
		Element body = root.addElement("Transaction_Body");

		// (4)增加body下的内容
		Element requestElement = body.addElement("request");

		// (5)加入请求
		RequestBodyQueryCertificateState requestBody = new RequestBodyQueryCertificateState();		
		//需要根据项目的实际情况进行调整.
		requestBody.setTxn_SN(txn_sn);  					//(1)set field1  凭证号(客户端生成)--->上传文件(凭证)		
		requestBody.setEBnk_SvAr_ID(headerParams.getChanl_cust_no());  	//(2)set field2  合约号
		requestBody.setEntrst_Prj_ID(bodyParams.getEntrst_prj_id());  					//(3)set field3  委托项目号		
		requestBody.setEtrUnt_AccNo(bodyParams.getEtrunt_accno());						//(5)set field5	 委托单位帐号
		//加入XML相应结点中
		List<Map<String, Object>> list1 = EntityUtils.entityToListMap(requestBody);
		for (Map<String, Object> map : list1) {
			for (String key : map.keySet()) {
				Element elem = requestElement.addElement(key);
				elem.addCDATA((String) map.get(key));
			}
		}

		// 先生成一次XML,计算XML的长度
		String tempXML = createXMLStr(doc);
		// 重新置报文长度 10位固定长度.不足补0
		int msgLen = tempXML.length();
		String msgLenStr = String.format("%010d", msgLen);
		msgLenElem.setText("");
		;
		msgLenElem.addCDATA(msgLenStr);

		returnXML = createXMLStr(doc);
		return returnXML;
	}

	/**
	 * 查询凭证明细
	 * 
	 * @param txn_sn  凭证序列号
	 * @param retResultCode 
	 * 		TRANSACTION_RETURN_RESULT_CODE_PACKET="1";   			//明细返回方式_报文
			TRANSACTION_RETURN_RESULT_CODE_FILE="2";   				//明细返回方式_文件	 
	 * @param headerParams   请求报文HEAD参数
	 * @param bodyParamsString   请求报文BODY参数
	 *      
	 * @return  查询
	 */
	public static String genP1CLP1055(String txn_sn,
										String retResultCode,										
										HeaderParams headerParams) {
		String returnXML = "";
		TransactionRequestHeader transactionHeader = createTransactionHeaderRequest(
				CCBTransaction.TRANSACTION_CODE_QUERY_VOUCHER);

		//设置报文中字段信息
		setRequestHeaderParams(transactionHeader,headerParams);
		// 创建一个xml文档
		Document doc = DocumentHelper.createDocument();
		// doc.addComment("这里是注释"); //向xml文件中添加注释
		// (1)创建一个名为Transaction的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = doc.addElement("Transaction");

		// (2)在root节点下创建两个子节点,分别是 Transaction_Header Transaction_Body
		Element header = root.addElement("Transaction_Header");

		// 采用MAP将请求参数加入到DOM中
		Element msgLenElem = null; // 报文长度
		List<Map<String, Object>> list = EntityUtils.entityToListMap(transactionHeader);
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				Element elem = header.addElement(key);
				elem.addCDATA((String) map.get(key));
				// 得到消息长度元素引用.
				if (key.equalsIgnoreCase("SYS_MSG_LEN")) {
					msgLenElem = elem;
				}
			}
		}

		// (3)在body子节点下加入request子节点
		Element body = root.addElement("Transaction_Body");

		// (4)增加body下的内容
		Element requestElement = body.addElement("request");

		// (5)加入请求报文BODY段
		RequestBodyQueryCertificateDetail requestBody = new RequestBodyQueryCertificateDetail();
		//需要根据项目的实际情况进行调整.
		requestBody.setEBnk_SvAr_ID(headerParams.getChanl_cust_no());
		requestBody.setTxn_SN(txn_sn);   //凭证序列号		
		requestBody.setRet_Rslt_Cd(retResultCode);  //返回结果类型
		
		//如果文件方式时则设置返回的文件类型

		if(retResultCode.equalsIgnoreCase(CCBTransaction.TRANSACTION_RETURN_RESULT_CODE_FILE)) {
			//			 @param formatFileTypeCode  格式文件类型代码
			//	 		参见CCBTransaction常量定义
			//	 		TRANSACTION_FORMAT_FILE_TYPE_CODE_SUMMARY_FILE="0";     //汇总文件
			//			TRANSACTION_FORMAT_FILE_TYPE_CODE_SUCCESS_DETAIL="1";   //成功明细
			//			TRANSACTION_FORMAT_FILE_TYPE_CODE_FAIL_DETAIL="2";      //失败明细
			requestBody.setFmt_File_TpCd(CCBTransaction.TRANSACTION_FORMAT_FILE_TYPE_CODE_SUMMARY_FILE);
		}
		
		List<Map<String, Object>> list1 = EntityUtils.entityToListMap(requestBody);
		for (Map<String, Object> map : list1) {
			for (String key : map.keySet()) {
				Element elem = requestElement.addElement(key);
				elem.addCDATA((String) map.get(key));
			}
		}

		// 先生成一次XML,计算XML的长度
		String tempXML = createXMLStr(doc);
		// 重新置报文长度 10位固定长度.不足补0
		int msgLen = tempXML.length();
		String msgLenStr = String.format("%010d", msgLen);
		msgLenElem.setText("");
		;
		msgLenElem.addCDATA(msgLenStr);

		returnXML = createXMLStr(doc);
		return returnXML;
	}

	/**
	 * 功能:生成转账报文 
	 * 业务代码:P1CMSET35
	 * @param headerParams   报文头参数
	 * @param transferParams 报文BODY参数
	 * @return
	 */
	public static String genP1CMSET35(HeaderParams headerParams,TransferParams transferParams) {
		String returnXML = "";
		TransactionRequestHeader transactionHeader = createTransactionHeaderRequest(CCBTransaction.TRANSACTION_CODE_TRANSFER);
		//设置报文中字段信息
		setRequestHeaderParams(transactionHeader,headerParams);
		
	
		// 创建一个xml文档
		Document doc = DocumentHelper.createDocument();
		// doc.addComment("这里是注释"); //向xml文件中添加注释
		// (1)创建一个名为Transaction的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = doc.addElement("Transaction");
	
		// (2)在root节点下创建两个子节点,分别是 Transaction_Header Transaction_Body
		Element header = root.addElement("Transaction_Header");
	
		// 采用MAP将请求参数加入到DOM中
		Element msgLenElem = null; // 报文长度
		List<Map<String, Object>> list = EntityUtils.entityToListMap(transactionHeader);
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				Element elem = header.addElement(key);
				elem.addCDATA((String) map.get(key));
				// 得到消息长度元素引用.
				if (key.equalsIgnoreCase("SYS_MSG_LEN")) {
					msgLenElem = elem;
				}
			}
		}
	
		// (3)在body子节点下加入request子节点
		Element body = root.addElement("Transaction_Body");
		
		// (4)增加body下的内容
		Element requestElement = body.addElement("request");
	
		// (5)加入请求段,将配置参数置于请求报文BODY中
		RequestBodyTransfer requestBody = new RequestBodyTransfer();
		Map<String,Object> requestBodyMap=EntityUtils.entityToMap(requestBody);
		Map<String,Object> transferParamsMap=EntityUtils.entityToMap(transferParams);
		for(String key:transferParamsMap.keySet()) {
			requestBodyMap.put(key, transferParamsMap.get(key));
		}
		requestBody=EntityUtils.mapToEntity(requestBodyMap, RequestBodyTransfer.class);	
		
		//生成请求报文BODY内字段
		List<Map<String, Object>> list1 = EntityUtils.entityToListMap(requestBody);
		for (Map<String, Object> map : list1) {
			for (String key : map.keySet()) {
				Element elem = requestElement.addElement(key);
				elem.addCDATA((String) map.get(key));
			}
		}
	
		// 先生成一次XML,计算XML的长度
		String tempXML = createXMLStr(doc);
		// 重新置报文长度 10位固定长度.不足补0
		int msgLen = tempXML.length();
		String msgLenStr = String.format("%010d", msgLen);
		msgLenElem.setText("");
		;
		msgLenElem.addCDATA(msgLenStr);
	
		returnXML = createXMLStr(doc);
		return returnXML;
	}


	/**
	 * 生成查询转账结果报文 P1CMSET36
	 * @param headerParams			请求报文HEAD参数对象
	 * @param transferResultParams	请求报文BODY参数对象
	 * @return 查询转换结果请求报文
	 */
	public static String genP1CMSET36(HeaderParams headerParams,TransferResultParams transferResultParams) {
		String returnXML = "";
		TransactionRequestHeader transactionHeader = createTransactionHeaderRequest(CCBTransaction.TRANSACTION_CODE_QUERY_TRANSFER_RESULT);
		
		//设置报文中字段信息
		setRequestHeaderParams(transactionHeader,headerParams);
	
		// 创建一个xml文档
		Document doc = DocumentHelper.createDocument();
		// doc.addComment("这里是注释"); //向xml文件中添加注释
		// (1)创建一个名为Transaction的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = doc.addElement("Transaction");
	
		// (2)在root节点下创建两个子节点,分别是 Transaction_Header Transaction_Body
		Element header = root.addElement("Transaction_Header");
	
		// 采用MAP将请求参数加入到DOM中
		Element msgLenElem = null; // 报文长度
		List<Map<String, Object>> list = EntityUtils.entityToListMap(transactionHeader);
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				Element elem = header.addElement(key);
				elem.addCDATA((String) map.get(key));
				// 得到消息长度元素引用.
				if (key.equalsIgnoreCase("SYS_MSG_LEN")) {
					msgLenElem = elem;
				}
			}
		}
	
		// (3)在body子节点下加入request子节点
		Element body = root.addElement("Transaction_Body");
	
		// (4)增加body下的内容
		Element requestElement = body.addElement("request");
	
		// (5)加入请求段
		//需要根据项目的实际情况进行调整.
		RequestBodyQueryTransferResult requestBody = new RequestBodyQueryTransferResult();
		//将参数对象中相应字段的值置到对象中.
		Map<String,Object> requestBodyMap=EntityUtils.entityToMap(requestBody);																		   	
		Map<String,Object> transferResultParamsMap=EntityUtils.entityToMap(transferResultParams);
		for(String key:transferResultParamsMap.keySet()) {
			requestBodyMap.put(key, transferResultParamsMap.get(key));
		}
		requestBody=EntityUtils.mapToEntity(requestBodyMap, RequestBodyQueryTransferResult.class);
		//设置请求报文BODY
		List<Map<String, Object>> list1 = EntityUtils.entityToListMap(requestBody);
		for (Map<String, Object> map : list1) {
			for (String key : map.keySet()) {
				Element elem = requestElement.addElement(key);
				elem.addCDATA((String) map.get(key));
			}
		}
	
		// 先生成一次XML,计算XML的长度
		String tempXML = createXMLStr(doc);
		// 重新置报文长度 10位固定长度.不足补0
		int msgLen = tempXML.length();
		String msgLenStr = String.format("%010d", msgLen);
		msgLenElem.setText("");
		msgLenElem.addCDATA(msgLenStr);
	
		returnXML = createXMLStr(doc);
		return returnXML;
	}

	/**
	 * 功能:生成查询账户余额报文 
	 * @param headerParams				请求报文HEAD参数
	 * @param queryAccountBalanceParams请求报文BODY参数
	 * @param accountInfoList			帐户列表(只填写一个即可)
	 * @return 报文
	 */
	public static String genP1CMSER18(HeaderParams headerParams,
									QueryAccountBalanceParams queryAccountBalanceParams,
									AccountInfoForBalanceList accountInfoList) {
		String returnXML = "";

		TransactionRequestHeader transactionHeader = createTransactionHeaderRequest(CCBTransaction.TRANSACTION_CODE_QUERY_ACCOUNT_BALANCE);
		
		//设置报文中字段信息
		setRequestHeaderParams(transactionHeader,headerParams);

		// 创建一个xml文档
		Document doc = DocumentHelper.createDocument();
		// doc.addComment("这里是注释"); //向xml文件中添加注释
		// (1)创建一个名为Transaction的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = doc.addElement("Transaction");

		// (2)在root节点下创建两个子节点,分别是 Transaction_Header Transaction_Body
		Element header = root.addElement("Transaction_Header");

		// 采用MAP将请求参数加入到DOM中
		Element msgLenElem = null; // 报文长度
		List<Map<String, Object>> list = EntityUtils.entityToListMap(transactionHeader);
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				Element elem = header.addElement(key);
				elem.addCDATA((String) map.get(key));
				// 得到消息长度元素引用.
				if (key.equalsIgnoreCase("SYS_MSG_LEN")) {
					msgLenElem = elem;
				}
			}
		}

		// (3)在body子节点下加入request子节点
		Element body = root.addElement("Transaction_Body");

		// (4)增加body下的内容
		Element requestElement = body.addElement("request");

		//循环记录个数(帐户个数)
		RequestBodyQueryAccountBalance requestBody = new RequestBodyQueryAccountBalance();
		requestBody.setRvl_Rcrd_Num(Integer.toString(accountInfoList.getAccountNum()));
		//将参数对象中相应字段的值置到对象中.
		Map<String,Object> requestBodyMap=EntityUtils.entityToMap(requestBody);																		   	
		Map<String,Object> queryAccountBalanceParamsMap=EntityUtils.entityToMap(queryAccountBalanceParams);
		for(String key:queryAccountBalanceParamsMap.keySet()) {
			requestBodyMap.put(key, queryAccountBalanceParamsMap.get(key));
		}
		requestBody=EntityUtils.mapToEntity(requestBodyMap, RequestBodyQueryAccountBalance.class);	
		// 加入到BODY
		List<Map<String, Object>> list1 = EntityUtils.entityToListMap(requestBody);
		for (Map<String, Object> map : list1) {
			for (String key : map.keySet()) {
				Element elem = requestElement.addElement(key);
				elem.addCDATA((String) map.get(key));
			}
		}

		// 加入<request>
		// <LIST1>...</LIST1>
		// <LIST1>...</LIST1>
		List<AccountInfoForBalance> tempAccountInfoList = accountInfoList.getAccountList();
		for (AccountInfoForBalance accountInfo : tempAccountInfoList) {
			Element accountInfoElement = requestElement.addElement("LIST1");
			accountInfoElement.addAttribute("p_type", "G");

			List<Map<String, Object>> list2 = EntityUtils.entityToListMap(accountInfo);
			for (Map<String, Object> map : list2) {
				for (String key : map.keySet()) {
					Element elem = accountInfoElement.addElement(key);
					elem.addCDATA((String) map.get(key));
				}
			}
		}

		// 先生成一次XML,计算XML的长度
		String tempXML = createXMLStr(doc);
		// 重新置报文长度 10位固定长度.不足补0
		int msgLen = tempXML.length();
		String msgLenStr = String.format("%010d", msgLen);
		msgLenElem.setText("");
		msgLenElem.addCDATA(msgLenStr);

		returnXML = createXMLStr(doc);
		return returnXML;
	}

	/**
	 * 功能:生成请求查询账户明细报文
	 * 业务代码:P1CMSER65
	 * @param headerParams
	 * @param queryAccountDetailParams
	 * @param accountList 活期帐户列表
	 * @return
	 */
	public static String genP1CMSER65(HeaderParams headerParams,
			QueryAccountDetailParams queryAccountDetailParams,
			AccountForDetailList accountList) {
		String returnXML = "";

		TransactionRequestHeader transactionHeader = createTransactionHeaderRequest(CCBTransaction.TRANSACTION_CODE_QUERY_ACCOUNT_DETAIL);
		//设置报文中字段信息
		setRequestHeaderParams(transactionHeader,headerParams);

		// 创建一个xml文档
		Document doc = DocumentHelper.createDocument();
		// doc.addComment("这里是注释"); //向xml文件中添加注释
		// (1)创建一个名为Transaction的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = doc.addElement("Transaction");

		// (2)在root节点下创建两个子节点,分别是 Transaction_Header Transaction_Body
		Element header = root.addElement("Transaction_Header");

		// 采用MAP将请求参数加入到DOM中
		Element msgLenElem = null; // 报文长度
		List<Map<String, Object>> list = EntityUtils.entityToListMap(transactionHeader);
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				Element elem = header.addElement(key);
				elem.addCDATA((String) map.get(key));
				// 得到消息长度元素引用.
				if (key.equalsIgnoreCase("SYS_MSG_LEN")) {
					msgLenElem = elem;
				}
			}
		}

		// (3)在body子节点下加入request子节点
		Element body = root.addElement("Transaction_Body");
		// (4)增加body下的内容
		Element requestElement = body.addElement("request");

		// (5)加入请求段
		RequestBodyQueryAccountDetail requestBody = new RequestBodyQueryAccountDetail();
		//需要根据项目的实际情况进行调整.
		//将参数对象中相应字段的值置到对象中.
		Map<String,Object> requestBodyMap=EntityUtils.entityToMap(requestBody);																		   	
		Map<String,Object> queryAccountDetailParamsMap=EntityUtils.entityToMap(queryAccountDetailParams);
		for(String key:queryAccountDetailParamsMap.keySet()) {
			requestBodyMap.put(key, queryAccountDetailParamsMap.get(key));
		}
		requestBody=EntityUtils.mapToEntity(requestBodyMap, RequestBodyQueryAccountDetail.class);
		//加入到BODY
		List<Map<String, Object>> list1 = EntityUtils.entityToListMap(requestBody);
		for (Map<String, Object> map : list1) {
			for (String key : map.keySet()) {
				Element elem = requestElement.addElement(key);
				elem.addCDATA((String) map.get(key));
			}
		}
		

		// 加入<request>
		// <LIST1>...</LIST1>
		// <LIST1>...</LIST1>
		List<AccountForDetail> tempAccountInfoList = accountList.getAccountList();
		for (AccountForDetail accountInfo : tempAccountInfoList) {
			Element accountInfoElement = requestElement.addElement("LIST1");
			accountInfoElement.addAttribute("p_type", "G");

			List<Map<String, Object>> list2 = EntityUtils.entityToListMap(accountInfo);
			for (Map<String, Object> map : list2) {
				for (String key : map.keySet()) {
					Element elem = accountInfoElement.addElement(key);
					elem.addCDATA((String) map.get(key));
				}
			}
		}

		// 先生成一次XML,计算XML的长度
		String tempXML = createXMLStr(doc);
		// 重新置报文长度 10位固定长度.不足补0
		int msgLen = tempXML.length();
		String msgLenStr = String.format("%010d", msgLen);
		msgLenElem.setText("");
		msgLenElem.addCDATA(msgLenStr);

		returnXML = createXMLStr(doc);
		return returnXML;

	}

	/**
	 * 设置请求报文头中的参数
	 * @param transactionHeader  请求报文头对象
	 * @param headerParams		  报文头参数对象
	 */
	private static void setRequestHeaderParams(TransactionRequestHeader transactionHeader,HeaderParams headerParams) {
		//设置报文中字段信息
		transactionHeader.setCHNL_CUST_NO(headerParams.getChanl_cust_no());
		transactionHeader.setTXN_STFF_ID(headerParams.getTxn_stff_id());
		transactionHeader.setTxn_Itt_IP_Adr(headerParams.getTxn_itt_ip_adr());
	}

	/**
	 * 创建请求报文头
	 * 
	 * @param transactionCode
	 * @return
	 */
	private static TransactionRequestHeader createTransactionHeaderRequest(String transactionCode) {
		TransactionRequestHeader transationHeader = new TransactionRequestHeader();
		// 设置业务代码
		transationHeader.setSYS_TX_CODE(transactionCode);

		Date nowTime = new Date(); // 获取当前日期

		// 发起方交易时间
		String reqTime = DateUtil.getMillisecondDate(nowTime);
		transationHeader.setSYS_REQ_TIME(reqTime);

		// 交易日期
		String txnDt = DateUtil.getYYYYMMDDDate(nowTime);
		transationHeader.setTXN_DT(txnDt);

		// 交易时间
		String txnTm = DateUtil.getHHmmssDate(nowTime);
		transationHeader.setTXN_TM(txnTm);

		// TODO 需要根据项目的实际情况设置其值 交易人员编号
		// 测试时暂时采用一个常量
		// String operatorId="";
		// transationHeader.setTXN_STFF_ID(operatorId);

		return transationHeader;
	}

	/**
	 * 生成XML字符串
	 * 
	 * @param doc Document对象(DOM4J)
	 * @return
	 */
	private static String createXMLStr(Document doc) {

		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8"); //设置xml文档的编码为utf-8

		StringWriter out; // 输出流对象
		String resultXML = "";
		try {

			out = new StringWriter(); // 创建一个输出流对象
			XMLWriter writer = new XMLWriter(out, format); // 创建一个dom4j创建xml的对象
			// 调用write方法将doc文档写到输出流中
			writer.write(doc);
			writer.close();

			resultXML = out.toString();
			out.close();

			//System.out.println("结果XML1:" + resultXML);
			//System.out.println("报文长度1:" + resultXML.length());

		} catch (IOException e) {
			System.out.print("生成XML失败");
			e.printStackTrace();
		}

		return resultXML;
	}

	/**
	 * TODO 报文加密时使用的函数(企业端) 采用3DES加密+BASE64编码
	 * 
	 * @param packet 报文(正常报文)
	 * @param desKey DES key
	 * @return String类型加密报文(BASE64编码)
	 */
	private static String encryptPacket(String packet, String key) {
		String resultStr = "";
		try {
			byte[] packetArr = packet.getBytes("UTF-8");
			byte[] secretArr = DESede.encryptMode(DESede.Algorithm_Desede, key, packetArr);
			resultStr = Base64Util.encode2Base64Str(secretArr);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultStr;
	}

	/**
	 * 模块建行服务器端 对接收到的packet进行解密处理
	 * 
	 * @param packet 接收到的报文BASE64编码 (3DES加密+BASE64处理)
	 * @param key    3DES密钥
	 * @return 解密后的内容
	 */
	private static String decryptPacket(String packet, String key) {
		String resultStr = "";
		try {
			byte[] packetArr = Base64Util.decode2Byte(packet);
			byte[] decryptArr = DESede.decryptMode(DESede.Algorithm_Desede, key, packetArr);
			resultStr = new String(decryptArr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultStr;
	}

	/**
	 * TODO 项目上使用此方式进行解密 解密报文:
	 * 
	 * @param packet byte[]格式报文(BASE64编码) 3DES加密+BASE64编码
	 * @param key    DES key 自银行端获取得到的DES KEY
	 * @return
	 */
	private static String decryptPacket(byte[] packet, String key) {
		String resultStr = "";
		try {

			byte[] packetArr = Base64Util.decode2Byte(packet);
			byte[] decryptArr = DESede.decryptMode(DESede.Algorithm_Desede, key, packetArr);
			resultStr = new String(decryptArr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultStr;
	}

	/**
	 * TODO 实际项目中可用的代码. 生成签名(企业端使用自己的私钥生成签名)
	 * 
	 * @param packet 需要签名的报文(正常报文)
	 * @param key    RSA私钥(企业端私钥) BASE64编码
	 * @return 签名(BASE64编码)
	 * 
	 *         注:银行端采用企业端的公钥对签名进行验证
	 * @throws Exception
	 */
	private static String createSign(String packet, String privateKey) throws Exception {
		// 获取钥匙对

		/*
		 * KeyPair keyPair=null;
		 * 
		 * keyPair = MD5withRSA.getKeyPair();
		 * 
		 * //获取公钥 PublicKey publicKey = MD5withRSA.getPublicKey(keyPair); //获取私钥
		 * PrivateKey privateKey = MD5withRSA.getPrivateKey(keyPair);
		 * 
		 * 
		 * String publicKeyBase64=Base64Util.encode(publicKey.getEncoded());
		 * //BASE64编码公钥
		 * System.out.println("BASE64 ENCODE PUBLIC KEY IS:"+publicKeyBase64);
		 * 
		 * String privateKeyBase64=Base64Util.encode(privateKey.getEncoded());
		 * //BASE64编码私钥
		 * System.out.println("BASE64 ENCODE private KEY IS:"+privateKeyBase64);
		 */
		// PublicKey genedPublicKey=GenKey.getRSAPublidKeyBybase64(publicKeyBase64);
		// //生成公钥

		PrivateKey genedPrivateKey = GenKey.getRSAPrivateKeyBybase64(privateKey); // 生成私钥

		// 采用私钥进行签名 MD5withRSA签名
		byte[] sign = MD5withRSA.signMethod(packet, genedPrivateKey);
		String signBase64 = Base64Util.encode2Base64Str(sign);

		// 采用公钥进行校验签名是否一致
		// boolean verifyMethod = MD5withRSA.verifyMethod(packet, signMethod,
		// publicKey);
		// boolean verifyMethod = MD5withRSA.verifyMethod(packet, sign, genedPublicKey);
		// System.out.println("使用SignatureAPI 数字签名是否一致：" + verifyMethod);

		return signBase64;
	}

	/**
	 * 测试报文签名
	 * 验证签名.
	 * @throws Exception 
	 */
	private static void testCreateSign() throws Exception {
		// 获取钥匙对

		KeyPair keyPair = null;

		keyPair = MD5withRSA.getKeyPair();
		 
		PublicKey publicKey = MD5withRSA.getPublicKey(keyPair); 	// 获取公钥
		PrivateKey privateKey = MD5withRSA.getPrivateKey(keyPair);   //获取私钥

		//BASE64编码公钥
		String publicKeyBase64 = Base64Util.encode2Base64Str(publicKey.getEncoded());		
		System.out.println("BASE64 ENCODE PUBLIC KEY IS:" + publicKeyBase64);

		// BASE64编码私钥
		String privateKeyBase64 = Base64Util.encode2Base64Str(privateKey.getEncoded());
		System.out.println("BASE64 ENCODE private KEY IS:" + privateKeyBase64);

		//生成公钥
		PublicKey genedPublicKey=GenKey.getRSAPublidKeyBybase64(publicKeyBase64);
		
		
		String packet="createSign";
		String signBase64Str=createSign(packet,privateKeyBase64);
		
		// 采用公钥进行校验签名是否一致
		//boolean verifyMethod = MD5withRSA.verifyMethod(packet, signMethod, publicKey);
		byte[] signArr=Base64Util.decode2Byte(signBase64Str);
		boolean verify = MD5withRSA.verifyMethod("createSign", signArr, genedPublicKey);
		System.out.println("使用SignatureAPI 数字签名是否一致：" + verify);
	}

	/**
	 * 测试报文加密,解密 测试通过
	 */
	private static void testEncryptAndDecrypt() {
		String str = "1234567abcdefg;";
		String key = "1234567890";
		System.out.println("加密前字符串:" + str);

		String encryptStr = DownPacketProcessor.encryptPacket(str, key);
		System.out.println("加密后字符串:" + encryptStr);

		String decryptStr = DownPacketProcessor.decryptPacket(encryptStr, key);
		System.out.println("加密后字符串:" + decryptStr);

	}

	/**
	 * TODO 实际项目中使用此种方式进行报文解密 测试报文加密,解密 测试通过
	 */
	private static void testEncryptAndDecrypt1() {
		String str = "1234567abcdefg;";
		String key = "1234567890";
		System.out.println("加密前字符串:" + str);

		String encryptStr = DownPacketProcessor.encryptPacket(str, key);
		System.out.println("加密后字符串:" + encryptStr);

		String decryptStr;
		try {
			decryptStr = DownPacketProcessor.decryptPacket(encryptStr.getBytes("UTF-8"), key);
			System.out.println("加密后字符串:" + decryptStr);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 测试代码
	 * 
	 * @param args
	 * @throws Exception
	 */
	private static void main(String[] args) throws Exception {

		// DownProcessor.genP1OPME001(); //(1)签到
		// DownProcessor.genP1CLP1051(); //(2)批量代扣制单
		// DownProcessor.genP1CLP1054(); //(3)代发代扣直联单据查询
		// DownProcessor.genP1CLP1055(CCBTransaction.TRANSACTION_Ret_Rslt_Cd_PACKET);
		// //(4.1)查询凭证明细结果 报文方式
		// DownProcessor.genP1CLP1055(CCBTransaction.TRANSACTION_Ret_Rslt_Cd_FILE);
		// //(4.2)查询凭证明细结果 文件方式

		// DownProcessor.genP1CMSER18(); //(5)查询帐户余额

		// DownProcessor.genP1CMSET35(); //(6)转账
		// DownProcessor.genP1CMSET36(); //(7)查询转账结果

		// DownProcessor.genP1CMSER65(); //(8)查询账户明细

		// DownProcessor.testEncryptAndDecrypt();

		// DownProcessor.testEncryptAndDecrypt1();

		// DownProcessor.createSign("test",null);
		
		DownPacketProcessor.testCreateSign();  //测试报文签名

	}

}
