package com.learnbind.ai.ccb.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.learnbind.ai.ccb.requestpacket.TransactionResponseBody;
import com.learnbind.ai.ccb.responsepacket.ResponseAccountBalance;
import com.learnbind.ai.ccb.responsepacket.ResponseAccountBalanceList;
import com.learnbind.ai.ccb.responsepacket.ResponseAccountDetail;
import com.learnbind.ai.ccb.responsepacket.ResponseAccountDetailList;
import com.learnbind.ai.ccb.responsepacket.ResponseBatchWithhold;
import com.learnbind.ai.ccb.responsepacket.ResponseCertDetailFileMode;
import com.learnbind.ai.ccb.responsepacket.ResponseCertDetailFileModeList;
import com.learnbind.ai.ccb.responsepacket.ResponseCertDetailPacketMode;
import com.learnbind.ai.ccb.responsepacket.ResponseCertDetailPacketModeList;
import com.learnbind.ai.ccb.responsepacket.ResponseCertState;
import com.learnbind.ai.ccb.responsepacket.ResponseCertStateList;
import com.learnbind.ai.ccb.responsepacket.ResponseTransfer;
import com.learnbind.ai.ccb.responsepacket.ResponseTransferResult;
import com.learnbind.ai.ccb.responsepacket.ResponseTransferResultList;
import com.learnbind.ai.common.util.EntityUtils;

/**
 * 上行报文处理器 用于处理响应报文
 * 
 * @author lenovo
 */
public class UpPacketProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(UpPacketProcessor.class);
	
	public static final String KYE_SYS_PKG_STS_TYPE="SYS_PKG_STS_TYPE";  //包响应类型
	public static final String KEY_SYS_TX_STATUS="SYS_TX_STATUS";			//服务状态
	public static final String KEY_TRAN_RESPONSE_STATUS="TRAN_RESPONSE_STATUS"; //交易响应状态
	public static final String KEY_SYS_RESPONSE_CODE="SYS_RESP_CODE";  //交易错误码
	public static final String KEY_TRAN_RESPONSE_DESC="TRAN_RESPONSE_DESC";  //交易错误描述
	
	
	//private void 
	
	/**
	 * 判定所返回的报文是否正常
	 * @param map 初步分析数据包状态结果map
	 * @return	如果正常返回True,否则返回False;
	 */
	public static boolean isSuccessResponse(String xml) {		
		boolean result=true;
		
		Map<String,String> map =getRespState(xml);
		
		if(map.get(KYE_SYS_PKG_STS_TYPE)!=null) {		
			boolean b1 = map.get(KYE_SYS_PKG_STS_TYPE)
					.equalsIgnoreCase(TransactionResponseBody.TRANSACTION_SYS_PKG_STS_TYPE_RESP);
			if (!b1) {
				result = false;
				return result;
			}
		}
		
		if(map.get(KEY_SYS_TX_STATUS)!=null) {
		boolean b2 = map.get(KEY_SYS_TX_STATUS)
				.equalsIgnoreCase(TransactionResponseBody.TRANSACTION_SYS_TX_STATUS_SUCCESS);
		if (!b2) {
			result = false;
			return result;
		}
		}
		
		if(map.get(KEY_TRAN_RESPONSE_STATUS)!=null) {
		boolean b3 = map.get(KEY_TRAN_RESPONSE_STATUS)
				.equalsIgnoreCase(TransactionResponseBody.TRANSACTION_TRAN_RESPONSE_STATUS_COMPLETE);
		if (!b3) {
			result = false;
			return result;
		}
		}
		
		if(map.get(KEY_SYS_RESPONSE_CODE)!=null) {
			boolean b4 = map.get(KEY_SYS_RESPONSE_CODE)
					.equalsIgnoreCase(TransactionResponseBody.TRANSACTION_SYS_RESP_CODE_SUCCESS);
			if (!b4) {
				result = false;
				return result;
			}
		}
		
		
		return result;		
	}
	
	/**
	 * 功能:解析查询账户余额报文
	 * @param xml  查询账户余额-响应报文
	 * @return 账户余额对象列表,如果余额对象列表数为null时,则表解析报文时出现错误
	 */
	public static ResponseAccountBalanceList parseQueryAccountBalance(String xml) {
		
		//需要返回的帐户余额对象列表
		ResponseAccountBalanceList accountBalanceList=new ResponseAccountBalanceList();
		
		try {
			Document doc = DocumentHelper.parseText(xml);  //将字符串转为XML
			Element rootElem = doc.getRootElement();  // 获取根节点
			
			//获取BODY结点
			Element bodyElem = rootElem.element("Transaction_Body");

			Element responseElem = bodyElem.element("response");
			Iterator iterList1 = responseElem.elementIterator("LIST1");
			while (iterList1.hasNext()) {
				Element list1Elem = (Element) iterList1.next();
				Iterator iterField=list1Elem.elementIterator();  //获取LIST1节点的所有子节点
				
				//迭代LIST1下的子节点
				//(1.1)生成账户余额对象并转换成MAP对象   object--->map
				ResponseAccountBalance accountBalance=new  ResponseAccountBalance();
				Map<String,Object> accountBalanceMap=EntityUtils.entityToMap(accountBalance);
				//(1.2)迭代某个LIST1下的子节点,并置map的值
				while(iterField.hasNext()) {
					Element fieldElem=(Element)iterField.next();
					String name= fieldElem.getName();					
					String value=fieldElem.getTextTrim();
					accountBalanceMap.put(name, value);					
				}
				//(1.3)map--->object
				accountBalance=EntityUtils.mapToEntity(accountBalanceMap, ResponseAccountBalance.class);
				accountBalanceList.add(accountBalance);
			}
		} catch (Exception e) {
			accountBalanceList=null;
		}
		
		
		return accountBalanceList;
	}
	
	
	/**
	 * 功能:转账-parse reponse packet
	 * @param xml  转账-响应报文
	 * @return  如果解析正确,则返回 ResponseTransfer对象,否则返回null;
	 */
	
	public static ResponseTransfer parseTransferAccount(String xml) {
		ResponseTransfer responseTransfer=new ResponseTransfer();
		
		try {
			Document doc = DocumentHelper.parseText(xml);  //将字符串转为XML
			Element rootElem = doc.getRootElement();  // 获取根节点
			
			//获取BODY结点
			Element bodyElem = rootElem.element("Transaction_Body");

			//(1.1)生成转账响应对象
			Map<String,Object> responseTransferMap=EntityUtils.entityToMap(responseTransfer);
			
			//(1.2)迭代response结点下的子节点,将值写入map
			Element responseElem = bodyElem.element("response");
			Iterator iterField = responseElem.elementIterator();
			while (iterField.hasNext()) {
				Element fieldElem = (Element) iterField.next();
				String name= fieldElem.getName();					
				String value=fieldElem.getTextTrim();
				logger.info(name+"------>"+value);
				responseTransferMap.put(name, value);	
			}
			//(1.3)map--->object
			responseTransfer=EntityUtils.mapToEntity(responseTransferMap, ResponseTransfer.class);
		} catch (Exception e) {
			responseTransfer=null;
		}
		
		return responseTransfer;
	}
	
	/**
	 * 响应报文-查询转账结果
	 * @param xml
	 * @return  如果解析成功,返回对象;否则返回null;
	 */
	public static ResponseTransferResultList parseQueryTransferAccountResult(String xml) {
		// 需要返回的转账结果对象列表
		ResponseTransferResultList transferResultList = new ResponseTransferResultList();

		try {
			Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElem = doc.getRootElement(); // 获取根节点

			// 获取BODY结点
			Element bodyElem = rootElem.element("Transaction_Body");

			Element responseElem = bodyElem.element("response");
			Iterator iterList1 = responseElem.elementIterator("LIST1");
			while (iterList1.hasNext()) {
				Element list1Elem = (Element) iterList1.next();
				Iterator iterField = list1Elem.elementIterator(); // 获取LIST1节点的所有子节点

				// 迭代LIST1下的子节点
				// (1.1)生成账户余额对象并转换成MAP对象 object--->map
				ResponseTransferResult transferResult = new ResponseTransferResult();
				Map<String, Object> transferResultMap = EntityUtils.entityToMap(transferResult);
				// (1.2)迭代某个LIST1下的子节点,并置map的值
				while (iterField.hasNext()) {
					Element fieldElem = (Element) iterField.next();
					String name = fieldElem.getName();
					String value = fieldElem.getTextTrim();
					transferResultMap.put(name, value);
				}
				// (1.3)map--->object
				transferResult = EntityUtils.mapToEntity(transferResultMap, ResponseTransferResult.class);
				transferResultList.add(transferResult);
			}
		} catch (Exception e) {
			transferResultList = null;
		}

		return transferResultList;
	}
	
	/**
	 * 响应报文解析-查询账户明细
	 * @param xml  响应报文
	 * @return  如果正确解析则返回ResponseAccountDetailList,否则返回null
	 */
	public static ResponseAccountDetailList parseQueryAccountDetail(String xml) {
		ResponseAccountDetailList accountDetailList = new ResponseAccountDetailList();

		try {
			Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElem = doc.getRootElement(); // 获取根节点

			// 获取BODY结点
			Element bodyElem = rootElem.element("Transaction_Body");

			Element responseElem = bodyElem.element("response");
			Iterator iterList1 = responseElem.elementIterator("LIST1");
			while (iterList1.hasNext()) {
				Element list1Elem = (Element) iterList1.next();
				Iterator iterField = list1Elem.elementIterator(); // 获取LIST1节点的所有子节点

				// 迭代LIST1下的子节点
				// (1.1)生成账户余额对象并转换成MAP对象 object--->map
				ResponseAccountDetail accountDetail = new ResponseAccountDetail();
				Map<String, Object> accountDetailMap = EntityUtils.entityToMap(accountDetail);
				// (1.2)迭代某个LIST1下的子节点,并置map的值
				while (iterField.hasNext()) {
					Element fieldElem = (Element) iterField.next();
					String name = fieldElem.getName();
					String value = fieldElem.getTextTrim();
					accountDetailMap.put(name, value);
				}
				// (1.3)map--->object
				accountDetail = EntityUtils.mapToEntity(accountDetailMap, ResponseAccountDetail.class);
				accountDetailList.add(accountDetail);
			}
		} catch (Exception e) {
			accountDetailList = null;
		}

		return accountDetailList;
	}
	
	/**
	 * 解析:查询凭证状态-响应报文
	 * @param xml  响应报文
	 * @return  如果解析正确则返回凭证状态列表对象,否则返回null;
	 */
	public static ResponseCertStateList parseQueryCertState(String xml) {
		ResponseCertStateList certificateList = new ResponseCertStateList();

		try {
			Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElem = doc.getRootElement(); // 获取根节点

			// 获取BODY结点
			Element bodyElem = rootElem.element("Transaction_Body");

			Element responseElem = bodyElem.element("response");
			Iterator iterList1 = responseElem.elementIterator("CERT_LIST");
			while (iterList1.hasNext()) {
				Element list1Elem = (Element) iterList1.next();
				Iterator iterField = list1Elem.elementIterator(); // 获取LIST1节点的所有子节点

				// 迭代LIST1下的子节点
				// (1.1)生成账户余额对象并转换成MAP对象 object--->map
				ResponseCertState certificateState = new ResponseCertState();
				Map<String, Object> certificateStateMap = EntityUtils.entityToMap(certificateState);
				// (1.2)迭代某个LIST1下的子节点,并置map的值
				while (iterField.hasNext()) {
					Element fieldElem = (Element) iterField.next();
					String name = fieldElem.getName();
					String value = fieldElem.getTextTrim();
					certificateStateMap.put(name, value);
				}
				// (1.3)map--->object
				certificateState = EntityUtils.mapToEntity(certificateStateMap, ResponseCertState.class);
				certificateList.add(certificateState);
			}
		} catch (Exception e) {
			certificateList = null;
		}

		return certificateList;
	}
	
	/**
	 * 解析:凭证明细-响应报文(报文形式)
	 * @param xml
	 * @return
	 */
	public static ResponseCertDetailPacketModeList parseCertDetailPacketMode(String xml) {		
		// 需要返回的凭证明细对象列表
		ResponseCertDetailPacketModeList certificateDetailList = new ResponseCertDetailPacketModeList();

		try {
			Document doc = DocumentHelper.parseText(xml); 	// 将字符串转为XML
			Element rootElem = doc.getRootElement(); 		// 获取根节点

			// 获取BODY结点
			Element bodyElem = rootElem.element("Transaction_Body");

			Element responseElem = bodyElem.element("response");
			Iterator iterList1 = responseElem.elementIterator("INST_GRP");
			while (iterList1.hasNext()) {
				Element list1Elem = (Element) iterList1.next();
				Iterator iterField = list1Elem.elementIterator(); // 获取LIST1节点的所有子节点

				// 迭代LIST1下的子节点
				// (1.1)生成账户余额对象并转换成MAP对象 object--->map
				ResponseCertDetailPacketMode certificateDetail = new ResponseCertDetailPacketMode();
				Map<String, Object> certificateDetailMap = EntityUtils.entityToMap(certificateDetail);
				// (1.2)迭代某个LIST1下的子节点,并置map的值
				while (iterField.hasNext()) {
					Element fieldElem = (Element) iterField.next();
					String name = fieldElem.getName();
					String value = fieldElem.getTextTrim();
					certificateDetailMap.put(name, value);
				}
				// (1.3)map--->object
				certificateDetail = EntityUtils.mapToEntity(certificateDetailMap, ResponseCertDetailPacketMode.class);
				certificateDetailList.add(certificateDetail);
			}
		} catch (Exception e) {
			certificateDetailList = null;
		}

		return certificateDetailList;
	}
	
	/**
	 * 解析:凭证明细-响应报文(文件形式)
	 * @param xml
	 * @return
	 */
	public static ResponseCertDetailFileModeList parseCertDetailFileMode(String xml) {		
		// 需要返回的凭证明细对象列表
		ResponseCertDetailFileModeList certDetailList = new ResponseCertDetailFileModeList();

		try {
			Document doc = DocumentHelper.parseText(xml); 	// 将字符串转为XML
			Element rootElem = doc.getRootElement(); 		// 获取根节点

			// 获取BODY结点
			Element bodyElem = rootElem.element("Transaction_Body");

			Element responseElem = bodyElem.element("response");
			Iterator iterFileInfo = responseElem.element("FILE_LIST_PACK").elementIterator("FILE_INFO");
			//迭代 FILE_INFO 结点列表
			while (iterFileInfo.hasNext()) {				
				Element fileInfoElem = (Element) iterFileInfo.next();  //获取某个FIEL_INFO NODE
				Iterator iterField = fileInfoElem.elementIterator();   //获取 FILE_INFO 节点下子节点迭代器

				
				// (1.1)生成对象并转换成MAP对象 object--->map
				ResponseCertDetailFileMode certFileDetail = new ResponseCertDetailFileMode();
				Map<String, Object> certFileDetailMap = EntityUtils.entityToMap(certFileDetail);
				
				// (1.2)迭代某个LIST1下的子节点,并置map的值
				while (iterField.hasNext()) {
					Element fieldElem = (Element) iterField.next();
					String name = fieldElem.getName();
					String value = fieldElem.getTextTrim();
					certFileDetailMap.put(name, value);
				}
				// (1.3)map--->object
				certFileDetail = EntityUtils.mapToEntity(certFileDetailMap, ResponseCertDetailFileMode.class);
				certDetailList.add(certFileDetail);
			}
		} catch (Exception e) {
			certDetailList = null;
		}

		return certDetailList;
	}
	
	/**
	 * 解析:响应报文-请求批量代扣
	 * @param xml 请求批量代扣的响应报文
	 * @return 如果正确解析则返回ResponseBatchWithhold对象,否则返回null;
	 */
	public static ResponseBatchWithhold parseBatchWithhold(String xml) {
		ResponseBatchWithhold batchWithhold = new ResponseBatchWithhold();

		try {
			Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElem = doc.getRootElement(); // 获取根节点

			// 获取BODY结点
			Element bodyElem = rootElem.element("Transaction_Body");

			// (1.1)生成对象
			Map<String, Object> batchWithholdMap = EntityUtils.entityToMap(batchWithhold);

			// (1.2)迭代response结点下的子节点,将值写入map
			//TODO 处理response结果
			Element responseElem = bodyElem.element("response");
			Iterator iterField = responseElem.elementIterator();
			while (iterField.hasNext()) {
				Element fieldElem = (Element) iterField.next();
				String name = fieldElem.getName();
				String value = fieldElem.getTextTrim();
				logger.info(name + "------>" + value);
				batchWithholdMap.put(name, value);
			}
			// (1.3)map--->object
			batchWithhold = EntityUtils.mapToEntity(batchWithholdMap, ResponseBatchWithhold.class);
		} catch (Exception e) {
			batchWithhold = null;
		}

		return batchWithhold;
	}
	
	
	/**
		 * 对响应报文进行初步的分析,返回业务代码及响应状态
		 * 	SYS_PKG_STS_TYPE--->响应包类型    public static final String TRANSACTION_SYS_PKG_STS_TYPE_REQ="00";  //请求报文
											public static final String TRANSACTION_SYS_PKG_STS_TYPE_RESP="01";   //响应报文
											
			SYS_TX_STATUS--->服务状态
										public static final String TRANSACTION_SYS_TX_STATUS_SUCCESS="00";   	//成功
										public static final String TRANSACTION_SYS_TX_STATUS_FAIL="01";   		//失败
										public static final String TRANSACTION_SYS_TX_STATUS_UNCERTAIN="02";   	//不确定
			TRAN_RESPONSE_STATUS--->交易状态							
		       	交易成功时的应答报文，报文头中tran_response 节点下的 
		       									status 为 “COMPLETE”。
	 			交易错误时的应答报文，状态、错误码和错误信息分别填写在报文头中 
					tran_response 
						节点下的status为 “FAIL”、
						code （错误码）和
						desc （错误信息）域中；
						
				报文体（Transaction_Body）节点为空。
			
			TRAN_RESPONSE_CODE--->交易-错误码  (当发生错误时)
			TRAN_RESPONSE_DESC--->交易-错误信息  (当发生错误时)
			
		 * @param xml
		 * @return  以Map形式返回
		 */
		private static Map<String, String> getRespState(String xml) {
			
			Map<String, String> map = new HashMap<>();
			try {
	
				Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
				Element root = doc.getRootElement(); // 获取根节点
				Element header = root.element("Transaction_Header");
				Element body = root.element("Transaction_Body");
				
				Element element;
				
				//响应包类型
				element = header.element(KYE_SYS_PKG_STS_TYPE);
				map.put(KYE_SYS_PKG_STS_TYPE, element.getTextTrim());
				
				//SYS_TX_STATUS  服务状态
				element = header.element(KEY_SYS_TX_STATUS);
				map.put(KEY_SYS_TX_STATUS, element.getTextTrim());
				
				//SYS_RESP_CODE 服务响应码,12个0表示成功
				/*
				 * element = header.element(KEY_SYS_RESPONSE_CODE);
				 * map.put(KEY_SYS_RESPONSE_CODE, element.getTextTrim());
				 */		
				
				//TRAN_RESPONSE_STATUS  交易响应状态
				element = header.element("tran_response").element("status");
				String status=element.getTextTrim();
				map.put(KEY_TRAN_RESPONSE_STATUS, status);
				
				//TRAN_RESPONSE_CODE and  TRAN_RESPONSE_DESC
				//当错误发生时
				if(status.equalsIgnoreCase(TransactionResponseBody.TRANSACTION_TRAN_RESPONSE_STATUS_FAIL)) {
					element = header.element("tran_response").element("code");				
					map.put(KEY_SYS_RESPONSE_CODE, element.getTextTrim());
					
					element = header.element("tran_response").element("desc");				
					map.put(KEY_TRAN_RESPONSE_DESC, element.getTextTrim());
				}
				
	
			} catch (DocumentException e) {
				System.out.println(e.getMessage());
			}
			return map;		
		}


	//------------------------test case-------------------------------
		
	//测试解析批量代扣
	private static void test_parseBatchWithhold() {
		String batchWithholdXML="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<Transaction>\r\n" + 
				"	<Transaction_Header>\r\n" + 
				"		<SYS_TX_VRSN><![CDATA[01]]></SYS_TX_VRSN>\r\n" + 
				"		<SYS_MSG_LEN><![CDATA[]]></SYS_MSG_LEN>\r\n" + 
				"		<SYS_RECV_TIME><![CDATA[20181011114524942]]></SYS_RECV_TIME>\r\n" + 
				"		<SYS_RESP_TIME><![CDATA[20181011114529079]]></SYS_RESP_TIME>\r\n" + 
				"		<SYS_PKG_STS_TYPE><![CDATA[01]]></SYS_PKG_STS_TYPE>\r\n" + 
				"		<SYS_TX_STATUS><![CDATA[00]]></SYS_TX_STATUS>\r\n" + 
				"		<SYS_RESP_CODE><![CDATA[000000000000]]></SYS_RESP_CODE>\r\n" + 
				"		<SYS_RESP_DESC_LEN><![CDATA[]]></SYS_RESP_DESC_LEN>\r\n" + 
				"		<SYS_RESP_DESC><![CDATA[]]></SYS_RESP_DESC>\r\n" + 
				"		<SYS_EVT_TRACE_ID><![CDATA[1010010011539229524167010]]></SYS_EVT_TRACE_ID>\r\n" + 
				"		<SYS_SND_SERIAL_NO><![CDATA[]]></SYS_SND_SERIAL_NO>\r\n" + 
				"		<SYS_TX_CODE><![CDATA[P1CLP1051]]></SYS_TX_CODE>\r\n" + 
				"		<CHNL_CUST_NO><![CDATA[HE13000009021440001]]></CHNL_CUST_NO>\r\n" + 
				"		<tran_response>\r\n" + 
				"			<status><![CDATA[COMPLETE]]></status>\r\n" + 
				"		</tran_response>\r\n" + 
				"	</Transaction_Header>\r\n" + 
				"	<Transaction_Body>\r\n" + 
				"		<response>\r\n" + 
				"			<Txn_SN><![CDATA[113810458305358140]]></Txn_SN>\r\n" + 
				"			<EBnk_SvAr_ID><![CDATA[HE13000009021440001]]></EBnk_SvAr_ID>\r\n" + 
				"			<VchID><![CDATA[VS201810110000093989]]></VchID>\r\n" + 
				"			<PROCESS_FLAG><![CDATA[0]]></PROCESS_FLAG>\r\n" + 
				"			<TAmt>11.00</TAmt>\r\n" + 
				"			<TDnum><![CDATA[5]]></TDnum>\r\n" + 
				"			<RDsc><![CDATA[委托项目编号：130130070,实时批量交易中]]></RDsc>\r\n" + 
				"			<Vchr_St><![CDATA[400]]></Vchr_St>\r\n" + 
				"			<SRP_Err_Cd><![CDATA[]]></SRP_Err_Cd>\r\n" + 
				"			<HJNo><![CDATA[]]></HJNo>\r\n" + 
				"		</response>\r\n" + 
				"	</Transaction_Body>\r\n" + 
				"</Transaction>";
		ResponseBatchWithhold batchWithhold=parseBatchWithhold(batchWithholdXML);
		logger.info("RDsc 的值为:"+batchWithhold.getRDsc());
		
		
	}
		
	//测试查询凭证明细(文件形式)
	private static void test_parseCertFileDetail() {
		  String queryCertFileDetailXML="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<Transaction>\r\n" + 
				"	<Transaction_Header>\r\n" + 
				"		<SYS_TX_VRSN><![CDATA[01]]></SYS_TX_VRSN>\r\n" + 
				"		<SYS_MSG_LEN><![CDATA[]]></SYS_MSG_LEN>\r\n" + 
				"		<SYS_RECV_TIME><![CDATA[20181011140116168]]></SYS_RECV_TIME>\r\n" + 
				"		<SYS_RESP_TIME><![CDATA[20181011140116809]]></SYS_RESP_TIME>\r\n" + 
				"		<SYS_PKG_STS_TYPE><![CDATA[01]]></SYS_PKG_STS_TYPE>\r\n" + 
				"		<SYS_TX_STATUS><![CDATA[00]]></SYS_TX_STATUS>\r\n" + 
				"		<SYS_RESP_CODE><![CDATA[000000000000]]></SYS_RESP_CODE>\r\n" + 
				"		<SYS_RESP_DESC_LEN><![CDATA[]]></SYS_RESP_DESC_LEN>\r\n" + 
				"		<SYS_RESP_DESC><![CDATA[]]></SYS_RESP_DESC>\r\n" + 
				"		<SYS_EVT_TRACE_ID><![CDATA[1010010011539237676212640]]></SYS_EVT_TRACE_ID>\r\n" + 
				"		<SYS_SND_SERIAL_NO><![CDATA[]]></SYS_SND_SERIAL_NO>\r\n" + 
				"		<TOTAL_PAGE>0</TOTAL_PAGE>\r\n" + 
				"		<TOTAL_REC>0</TOTAL_REC>\r\n" + 
				"		<CURR_TOTAL_PAGE>0</CURR_TOTAL_PAGE>\r\n" + 
				"		<CURR_TOTAL_REC><![CDATA[]]></CURR_TOTAL_REC>\r\n" + 
				"		<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		<SYS_TX_CODE><![CDATA[P1CLP1055]]></SYS_TX_CODE>\r\n" + 
				"		<CHNL_CUST_NO><![CDATA[HE13000009021440001]]></CHNL_CUST_NO>\r\n" + 
				"		<tran_response>\r\n" + 
				"			<status><![CDATA[COMPLETE]]></status>\r\n" + 
				"		</tran_response>\r\n" + 
				"	</Transaction_Header>\r\n" + 
				"	<Transaction_Body>\r\n" + 
				"		<response>\r\n" + 
				"			<FILE_LIST_PACK p_type=\"G\">\r\n" + 
				"				<FILE_NUM>1</FILE_NUM>\r\n" + 
				"				<FILE_INFO p_type=\"G\">\r\n" + 
				"					<FILE_NAME><![CDATA[1_1010010011539229524167010_R.txt]]></FILE_NAME>\r\n" + 
				"					<FILE_PATH><![CDATA[20181011/P30281055/640/]]></FILE_PATH>\r\n" + 
				"				</FILE_INFO>\r\n" + 
				"			</FILE_LIST_PACK>\r\n" + 
				"			<TOTAL_PAGE>0</TOTAL_PAGE>\r\n" + 
				"			<TOTAL_REC>0</TOTAL_REC>\r\n" + 
				"			<CURR_TOTAL_PAGE>0</CURR_TOTAL_PAGE>\r\n" + 
				"			<CURR_TOTAL_REC><![CDATA[]]></CURR_TOTAL_REC>\r\n" + 
				"			<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"			<Txn_SN><![CDATA[113810458305358140]]></Txn_SN>\r\n" + 
				"			<EBnk_SvAr_ID><![CDATA[HE13000009021440001]]></EBnk_SvAr_ID>\r\n" + 
				"			<VchID><![CDATA[VS201810110000093989]]></VchID>\r\n" + 
				"			<TDnum><![CDATA[5]]></TDnum>\r\n" + 
				"			<TAmt>11.00</TAmt>\r\n" + 
				"			<Scss_Amt><![CDATA[11.00]]></Scss_Amt>\r\n" + 
				"			<Scss_Dnum><![CDATA[5]]></Scss_Dnum>\r\n" + 
				"			<Fail_Amt><![CDATA[0.00]]></Fail_Amt>\r\n" + 
				"			<Fail_Dnum><![CDATA[0]]></Fail_Dnum>\r\n" + 
				"			<Vchr_CtCd><![CDATA[1]]></Vchr_CtCd>\r\n" + 
				"			<Rmrk><![CDATA[]]></Rmrk>\r\n" + 
				"			<Vchr_St><![CDATA[700]]></Vchr_St>\r\n" + 
				"			<SRP_Err_Cd><![CDATA[]]></SRP_Err_Cd>\r\n" + 
				"			<RDsc><![CDATA[]]></RDsc>\r\n" + 
				"			<Succ_Dtl_File_Nm><![CDATA[]]></Succ_Dtl_File_Nm>\r\n" + 
				"			<Fail_Dtl_File_Nm><![CDATA[]]></Fail_Dtl_File_Nm>\r\n" + 
				"			<Tot_Dtl_File_Nm><![CDATA[1_1010010011539229524167010_R.txt]]></Tot_Dtl_File_Nm>\r\n" + 
				"			<SBSTRCVPY_PRJ_TPCD><![CDATA[0]]></SBSTRCVPY_PRJ_TPCD>\r\n" + 
				"			<Ret_Rslt_Cd><![CDATA[2]]></Ret_Rslt_Cd>\r\n" + 
				"			<EtrUnt_AccNo><![CDATA[13001618801050519642]]></EtrUnt_AccNo>\r\n" + 
				"			<EtrUnt_Nm><![CDATA[公司七八]]></EtrUnt_Nm>\r\n" + 
				"		</response>\r\n" + 
				"	</Transaction_Body>\r\n" + 
				"</Transaction>";
		ResponseCertDetailFileModeList list=parseCertDetailFileMode(queryCertFileDetailXML);
		logger.info("list size is:"+list.getCertFileDetailList().size());
		logger.info("field value is:"+list.getCertFileDetailList().get(0).getFILE_NAME());
	}
		
	
	

	//测试查询凭证明细.
	private static void test_parseCertificateDetail() {
		//查询凭证明细
		  String  queryCertificateDetailXML="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<Transaction>\r\n" + 
				"	<Transaction_Header>\r\n" + 
				"		<SYS_TX_VRSN><![CDATA[01]]></SYS_TX_VRSN>\r\n" + 
				"		<SYS_MSG_LEN><![CDATA[]]></SYS_MSG_LEN>\r\n" + 
				"		<SYS_RECV_TIME><![CDATA[20181011135937403]]></SYS_RECV_TIME>\r\n" + 
				"		<SYS_RESP_TIME><![CDATA[20181011135938011]]></SYS_RESP_TIME>\r\n" + 
				"		<SYS_PKG_STS_TYPE><![CDATA[01]]></SYS_PKG_STS_TYPE>\r\n" + 
				"		<SYS_TX_STATUS><![CDATA[00]]></SYS_TX_STATUS>\r\n" + 
				"		<SYS_RESP_CODE><![CDATA[000000000000]]></SYS_RESP_CODE>\r\n" + 
				"		<SYS_RESP_DESC_LEN><![CDATA[]]></SYS_RESP_DESC_LEN>\r\n" + 
				"		<SYS_RESP_DESC><![CDATA[]]></SYS_RESP_DESC>\r\n" + 
				"		<SYS_EVT_TRACE_ID><![CDATA[1010010011539237577203980]]></SYS_EVT_TRACE_ID>\r\n" + 
				"		<SYS_SND_SERIAL_NO><![CDATA[]]></SYS_SND_SERIAL_NO>\r\n" + 
				"		<TOTAL_PAGE>1</TOTAL_PAGE>\r\n" + 
				"		<TOTAL_REC>5</TOTAL_REC>\r\n" + 
				"		<CURR_TOTAL_PAGE>1</CURR_TOTAL_PAGE>\r\n" + 
				"		<CURR_TOTAL_REC><![CDATA[10]]></CURR_TOTAL_REC>\r\n" + 
				"		<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		<SYS_TX_CODE><![CDATA[P1CLP1055]]></SYS_TX_CODE>\r\n" + 
				"		<CHNL_CUST_NO><![CDATA[HE13000009021440001]]></CHNL_CUST_NO>\r\n" + 
				"		<tran_response>\r\n" + 
				"			<status><![CDATA[COMPLETE]]></status>\r\n" + 
				"		</tran_response>\r\n" + 
				"	</Transaction_Header>\r\n" + 
				"	<Transaction_Body>\r\n" + 
				"		<response>\r\n" + 
				"			<FILE_LIST_PACK p_type=\"G\">\r\n" + 
				"				<FILE_NUM><![CDATA[]]></FILE_NUM>\r\n" + 
				"				<FILE_MODE><![CDATA[]]></FILE_MODE>\r\n" + 
				"				<FILE_NODE><![CDATA[]]></FILE_NODE>\r\n" + 
				"				<FILE_NAME_PACK><![CDATA[]]></FILE_NAME_PACK>\r\n" + 
				"				<FILE_PATH_PACK><![CDATA[]]></FILE_PATH_PACK>\r\n" + 
				"			</FILE_LIST_PACK>\r\n" + 
				"			<TOTAL_PAGE>1</TOTAL_PAGE>\r\n" + 
				"			<TOTAL_REC>5</TOTAL_REC>\r\n" + 
				"			<CURR_TOTAL_PAGE>1</CURR_TOTAL_PAGE>\r\n" + 
				"			<CURR_TOTAL_REC><![CDATA[10]]></CURR_TOTAL_REC>\r\n" + 
				"			<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"			<Txn_SN><![CDATA[113810458305358140]]></Txn_SN>\r\n" + 
				"			<EBnk_SvAr_ID><![CDATA[HE13000009021440001]]></EBnk_SvAr_ID>\r\n" + 
				"			<VchID><![CDATA[VS201810110000093989]]></VchID>\r\n" + 
				"			<TDnum><![CDATA[5]]></TDnum>\r\n" + 
				"			<TAmt>11.00</TAmt>\r\n" + 
				"			<Scss_Amt><![CDATA[11.00]]></Scss_Amt>\r\n" + 
				"			<Scss_Dnum><![CDATA[5]]></Scss_Dnum>\r\n" + 
				"			<Fail_Amt><![CDATA[0.00]]></Fail_Amt>\r\n" + 
				"			<Fail_Dnum><![CDATA[0]]></Fail_Dnum>\r\n" + 
				"			<Vchr_CtCd><![CDATA[1]]></Vchr_CtCd>\r\n" + 
				"			<Rmrk><![CDATA[]]></Rmrk>\r\n" + 
				"			<Vchr_St><![CDATA[700]]></Vchr_St>\r\n" + 
				"			<SRP_Err_Cd><![CDATA[]]></SRP_Err_Cd>\r\n" + 
				"			<RDsc><![CDATA[]]></RDsc>\r\n" + 
				"			<Succ_Dtl_File_Nm><![CDATA[]]></Succ_Dtl_File_Nm>\r\n" + 
				"			<Fail_Dtl_File_Nm><![CDATA[]]></Fail_Dtl_File_Nm>\r\n" + 
				"			<Tot_Dtl_File_Nm><![CDATA[]]></Tot_Dtl_File_Nm>\r\n" + 
				"			<SBSTRCVPY_PRJ_TPCD><![CDATA[0]]></SBSTRCVPY_PRJ_TPCD>\r\n" + 
				"			<Ret_Rslt_Cd><![CDATA[1]]></Ret_Rslt_Cd>\r\n" + 
				"			<EtrUnt_AccNo><![CDATA[13001618801050519642]]></EtrUnt_AccNo>\r\n" + 
				"			<EtrUnt_Nm><![CDATA[公司七八]]></EtrUnt_Nm>\r\n" + 
				"			<INST_GRP p_type=\"G\">\r\n" + 
				"				<SCSP_SN><![CDATA[1]]></SCSP_SN>\r\n" + 
				"				<TrdPCt_AccNo><![CDATA[4340610130352336]]></TrdPCt_AccNo>\r\n" + 
				"				<TrdPCt_Nm><![CDATA[杨三八]]></TrdPCt_Nm>\r\n" + 
				"				<SRP_TxnAmt><![CDATA[1]]></SRP_TxnAmt>\r\n" + 
				"				<TrdPt_Acc_DepBnk_No><![CDATA[]]></TrdPt_Acc_DepBnk_No>\r\n" + 
				"				<TrdPt_Acc_DpBkNm><![CDATA[]]></TrdPt_Acc_DpBkNm>\r\n" + 
				"				<SCSP_Txn_StCd><![CDATA[2]]></SCSP_Txn_StCd>\r\n" + 
				"				<Err_Rsn><![CDATA[交易成功]]></Err_Rsn>\r\n" + 
				"				<SCSP_Smy_Dsc><![CDATA[安心付测试1]]></SCSP_Smy_Dsc>\r\n" + 
				"				<Rmrk1><![CDATA[测试一分钱]]></Rmrk1>\r\n" + 
				"				<Rmrk2><![CDATA[]]></Rmrk2>\r\n" + 
				"			</INST_GRP>\r\n" + 
				"			<INST_GRP p_type=\"G\">\r\n" + 
				"				<SCSP_SN><![CDATA[2]]></SCSP_SN>\r\n" + 
				"				<TrdPCt_AccNo><![CDATA[4340610130352336]]></TrdPCt_AccNo>\r\n" + 
				"				<TrdPCt_Nm><![CDATA[杨三八]]></TrdPCt_Nm>\r\n" + 
				"				<SRP_TxnAmt><![CDATA[2]]></SRP_TxnAmt>\r\n" + 
				"				<TrdPt_Acc_DepBnk_No><![CDATA[]]></TrdPt_Acc_DepBnk_No>\r\n" + 
				"				<TrdPt_Acc_DpBkNm><![CDATA[]]></TrdPt_Acc_DpBkNm>\r\n" + 
				"				<SCSP_Txn_StCd><![CDATA[2]]></SCSP_Txn_StCd>\r\n" + 
				"				<Err_Rsn><![CDATA[交易成功]]></Err_Rsn>\r\n" + 
				"				<SCSP_Smy_Dsc><![CDATA[安心付测试2]]></SCSP_Smy_Dsc>\r\n" + 
				"				<Rmrk1><![CDATA[测试一分钱]]></Rmrk1>\r\n" + 
				"				<Rmrk2><![CDATA[]]></Rmrk2>\r\n" + 
				"			</INST_GRP>\r\n" + 
				"			<INST_GRP p_type=\"G\">\r\n" + 
				"				<SCSP_SN><![CDATA[3]]></SCSP_SN>\r\n" + 
				"				<TrdPCt_AccNo><![CDATA[4340610130352336]]></TrdPCt_AccNo>\r\n" + 
				"				<TrdPCt_Nm><![CDATA[杨三八]]></TrdPCt_Nm>\r\n" + 
				"				<SRP_TxnAmt><![CDATA[2]]></SRP_TxnAmt>\r\n" + 
				"				<TrdPt_Acc_DepBnk_No><![CDATA[]]></TrdPt_Acc_DepBnk_No>\r\n" + 
				"				<TrdPt_Acc_DpBkNm><![CDATA[]]></TrdPt_Acc_DpBkNm>\r\n" + 
				"				<SCSP_Txn_StCd><![CDATA[2]]></SCSP_Txn_StCd>\r\n" + 
				"				<Err_Rsn><![CDATA[交易成功]]></Err_Rsn>\r\n" + 
				"				<SCSP_Smy_Dsc><![CDATA[安心付测试3]]></SCSP_Smy_Dsc>\r\n" + 
				"				<Rmrk1><![CDATA[测试一分钱]]></Rmrk1>\r\n" + 
				"				<Rmrk2><![CDATA[]]></Rmrk2>\r\n" + 
				"			</INST_GRP>\r\n" + 
				"			<INST_GRP p_type=\"G\">\r\n" + 
				"				<SCSP_SN><![CDATA[4]]></SCSP_SN>\r\n" + 
				"				<TrdPCt_AccNo><![CDATA[4340610130352336]]></TrdPCt_AccNo>\r\n" + 
				"				<TrdPCt_Nm><![CDATA[杨三八]]></TrdPCt_Nm>\r\n" + 
				"				<SRP_TxnAmt><![CDATA[3]]></SRP_TxnAmt>\r\n" + 
				"				<TrdPt_Acc_DepBnk_No><![CDATA[]]></TrdPt_Acc_DepBnk_No>\r\n" + 
				"				<TrdPt_Acc_DpBkNm><![CDATA[]]></TrdPt_Acc_DpBkNm>\r\n" + 
				"				<SCSP_Txn_StCd><![CDATA[2]]></SCSP_Txn_StCd>\r\n" + 
				"				<Err_Rsn><![CDATA[交易成功]]></Err_Rsn>\r\n" + 
				"				<SCSP_Smy_Dsc><![CDATA[安心付测试4]]></SCSP_Smy_Dsc>\r\n" + 
				"				<Rmrk1><![CDATA[测试一分钱]]></Rmrk1>\r\n" + 
				"				<Rmrk2><![CDATA[]]></Rmrk2>\r\n" + 
				"			</INST_GRP>\r\n" + 
				"			<INST_GRP p_type=\"G\">\r\n" + 
				"				<SCSP_SN><![CDATA[5]]></SCSP_SN>\r\n" + 
				"				<TrdPCt_AccNo><![CDATA[4340610130352336]]></TrdPCt_AccNo>\r\n" + 
				"				<TrdPCt_Nm><![CDATA[杨三八]]></TrdPCt_Nm>\r\n" + 
				"				<SRP_TxnAmt><![CDATA[3]]></SRP_TxnAmt>\r\n" + 
				"				<TrdPt_Acc_DepBnk_No><![CDATA[]]></TrdPt_Acc_DepBnk_No>\r\n" + 
				"				<TrdPt_Acc_DpBkNm><![CDATA[]]></TrdPt_Acc_DpBkNm>\r\n" + 
				"				<SCSP_Txn_StCd><![CDATA[2]]></SCSP_Txn_StCd>\r\n" + 
				"				<Err_Rsn><![CDATA[交易成功]]></Err_Rsn>\r\n" + 
				"				<SCSP_Smy_Dsc><![CDATA[安心付测试5]]></SCSP_Smy_Dsc>\r\n" + 
				"				<Rmrk1><![CDATA[测试一分钱]]></Rmrk1>\r\n" + 
				"				<Rmrk2><![CDATA[]]></Rmrk2>\r\n" + 
				"			</INST_GRP>\r\n" + 
				"		</response>\r\n" + 
				"	</Transaction_Body>\r\n" + 
				"</Transaction>";
		ResponseCertDetailPacketModeList list=parseCertDetailPacketMode(queryCertificateDetailXML);
		logger.info("list size is:"+list.getCertDetailList().size());
		logger.info("list size is:"+list.getCertDetailList().get(4).getSCSP_Smy_Dsc());
	}
	
	
	
	
	
	
	
	
	//测试解析:响应报文-查询单据状态   
	private static void test_parseQueryCertificate() {
		//查询单据状态
		  String queryCertificateXML="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<Transaction>\r\n" + 
				"	<Transaction_Header>\r\n" + 
				"		<SYS_TX_VRSN><![CDATA[01]]></SYS_TX_VRSN>\r\n" + 
				"		<SYS_MSG_LEN><![CDATA[]]></SYS_MSG_LEN>\r\n" + 
				"		<SYS_RECV_TIME><![CDATA[20181011124910907]]></SYS_RECV_TIME>\r\n" + 
				"		<SYS_RESP_TIME><![CDATA[20181011124911605]]></SYS_RESP_TIME>\r\n" + 
				"		<SYS_PKG_STS_TYPE><![CDATA[01]]></SYS_PKG_STS_TYPE>\r\n" + 
				"		<SYS_TX_STATUS><![CDATA[00]]></SYS_TX_STATUS>\r\n" + 
				"		<SYS_RESP_CODE><![CDATA[000000000000]]></SYS_RESP_CODE>\r\n" + 
				"		<SYS_RESP_DESC_LEN><![CDATA[]]></SYS_RESP_DESC_LEN>\r\n" + 
				"		<SYS_RESP_DESC><![CDATA[]]></SYS_RESP_DESC>\r\n" + 
				"		<SYS_EVT_TRACE_ID><![CDATA[1010010011539233350713060]]></SYS_EVT_TRACE_ID>\r\n" + 
				"		<SYS_SND_SERIAL_NO><![CDATA[]]></SYS_SND_SERIAL_NO>\r\n" + 
				"		<TOTAL_PAGE>1</TOTAL_PAGE>\r\n" + 
				"		<TOTAL_REC>1</TOTAL_REC>\r\n" + 
				"		<CURR_TOTAL_PAGE>1</CURR_TOTAL_PAGE>\r\n" + 
				"		<CURR_TOTAL_REC><![CDATA[1]]></CURR_TOTAL_REC>\r\n" + 
				"		<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		<SYS_TX_CODE><![CDATA[P1CLP1054]]></SYS_TX_CODE>\r\n" + 
				"		<CHNL_CUST_NO><![CDATA[HE13000009021440001]]></CHNL_CUST_NO>\r\n" + 
				"		<tran_response>\r\n" + 
				"			<status><![CDATA[COMPLETE]]></status>\r\n" + 
				"		</tran_response>\r\n" + 
				"	</Transaction_Header>\r\n" + 
				"	<Transaction_Body>\r\n" + 
				"		<response>\r\n" + 
				"			<TOTAL_PAGE>1</TOTAL_PAGE>\r\n" + 
				"			<TOTAL_REC>1</TOTAL_REC>\r\n" + 
				"			<CURR_TOTAL_PAGE>1</CURR_TOTAL_PAGE>\r\n" + 
				"			<CURR_TOTAL_REC><![CDATA[1]]></CURR_TOTAL_REC>\r\n" + 
				"			<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"			<CERT_LIST p_type=\"G\">\r\n" + 
				"				<Txn_SN><![CDATA[113810458305358140]]></Txn_SN>\r\n" + 
				"				<VchID><![CDATA[VS201810110000093989]]></VchID>\r\n" + 
				"				<EtrUnt_AccNo><![CDATA[13001618801050519642]]></EtrUnt_AccNo>\r\n" + 
				"				<EtrUnt_AccNm><![CDATA[公司七八]]></EtrUnt_AccNm>\r\n" + 
				"				<EtrUnt_Acc_DepBnk_No><![CDATA[130618801]]></EtrUnt_Acc_DepBnk_No>\r\n" + 
				"				<EtrUnt_Acc_DpBkNm><![CDATA[中国建设银行股份有限公司河北省分行直属支行]]></EtrUnt_Acc_DpBkNm>\r\n" + 
				"				<CntprtAcc><![CDATA[]]></CntprtAcc>\r\n" + 
				"				<Cntrprt_AccNm><![CDATA[]]></Cntrprt_AccNm>\r\n" + 
				"				<IwBk_BrNo><![CDATA[]]></IwBk_BrNo>\r\n" + 
				"				<IwBk_BkNm><![CDATA[]]></IwBk_BkNm>\r\n" + 
				"				<TAmt>11.00</TAmt>\r\n" + 
				"				<TDnum><![CDATA[5]]></TDnum>\r\n" + 
				"				<VCHR_TP_CODE><![CDATA[1]]></VCHR_TP_CODE>\r\n" + 
				"				<Vchr_St><![CDATA[700]]></Vchr_St>\r\n" + 
				"				<SRP_Err_Cd><![CDATA[]]></SRP_Err_Cd>\r\n" + 
				"				<SBSTRCVPY_PRJ_TPCD><![CDATA[0]]></SBSTRCVPY_PRJ_TPCD>\r\n" + 
				"				<IntrBnk_IndCd><![CDATA[1]]></IntrBnk_IndCd>\r\n" + 
				"				<Entrst_Prj_ID><![CDATA[130130070]]></Entrst_Prj_ID>\r\n" + 
				"				<Entrst_Prj_Nm><![CDATA[公司七八代付产品9642]]></Entrst_Prj_Nm>\r\n" + 
				"				<Prj_Use_ID><![CDATA[zldf00001]]></Prj_Use_ID>\r\n" + 
				"				<Prj_Use_Nm><![CDATA[直连客户代付专用]]></Prj_Use_Nm>\r\n" + 
				"				<SCSP_Smy_Dsc><![CDATA[]]></SCSP_Smy_Dsc>\r\n" + 
				"				<FILE_NM><![CDATA[1010010011539229524167010.txt]]></FILE_NM>\r\n" + 
				"				<Vchr_Gen_Dt><![CDATA[20181011114459]]></Vchr_Gen_Dt>\r\n" + 
				"				<TDP_ID><![CDATA[333333]]></TDP_ID>\r\n" + 
				"				<TDP_Nm><![CDATA[]]></TDP_Nm>\r\n" + 
				"				<Scss_Amt><![CDATA[11.00]]></Scss_Amt>\r\n" + 
				"				<Scss_Dnum><![CDATA[5]]></Scss_Dnum>\r\n" + 
				"				<Fail_Amt><![CDATA[0.00]]></Fail_Amt>\r\n" + 
				"				<Fail_Dnum><![CDATA[0]]></Fail_Dnum>\r\n" + 
				"			</CERT_LIST>\r\n" + 
				"		</response>\r\n" + 
				"	</Transaction_Body>\r\n" + 
				"</Transaction>";
		ResponseCertStateList list=parseQueryCertState(queryCertificateXML);
		logger.info("单据列表大小:"+list.getCertificateList().size());
		logger.info("某字段的值:"+list.getCertificateList().get(0).getPrj_Use_Nm());
	}
	
	
	
	
	
	
	//测试查询账户明细
	private static void test_parseQueryAccountDetail() {
		//查询账户明细
		  String accountDetailXML="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<Transaction>\r\n" + 
				"	<Transaction_Header>\r\n" + 
				"		<SYS_TX_VRSN><![CDATA[01]]></SYS_TX_VRSN>\r\n" + 
				"		<SYS_MSG_LEN><![CDATA[]]></SYS_MSG_LEN>\r\n" + 
				"		<SYS_RECV_TIME><![CDATA[20181009161157975]]></SYS_RECV_TIME>\r\n" + 
				"		<SYS_RESP_TIME><![CDATA[20181009161158431]]></SYS_RESP_TIME>\r\n" + 
				"		<SYS_PKG_STS_TYPE><![CDATA[01]]></SYS_PKG_STS_TYPE>\r\n" + 
				"		<SYS_TX_STATUS><![CDATA[00]]></SYS_TX_STATUS>\r\n" + 
				"		<SYS_RESP_CODE><![CDATA[000000000000]]></SYS_RESP_CODE>\r\n" + 
				"		<SYS_RESP_DESC_LEN><![CDATA[]]></SYS_RESP_DESC_LEN>\r\n" + 
				"		<SYS_RESP_DESC><![CDATA[成功]]></SYS_RESP_DESC>\r\n" + 
				"		<SYS_EVT_TRACE_ID><![CDATA[1010010011539072718066120]]></SYS_EVT_TRACE_ID>\r\n" + 
				"		<SYS_SND_SERIAL_NO><![CDATA[0000000000]]></SYS_SND_SERIAL_NO>\r\n" + 
				"		<TOTAL_PAGE>1</TOTAL_PAGE>\r\n" + 
				"		<TOTAL_REC>1</TOTAL_REC>\r\n" + 
				"		<CURR_TOTAL_PAGE>1</CURR_TOTAL_PAGE>\r\n" + 
				"		<CURR_TOTAL_REC><![CDATA[1]]></CURR_TOTAL_REC>\r\n" + 
				"		<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		<SYS_TX_CODE><![CDATA[P1CMSER65]]></SYS_TX_CODE>\r\n" + 
				"		<CHNL_CUST_NO><![CDATA[HE13000009021440001]]></CHNL_CUST_NO>\r\n" + 
				"		<tran_response>\r\n" + 
				"			<status><![CDATA[COMPLETE]]></status>\r\n" + 
				"		</tran_response>\r\n" + 
				"	</Transaction_Header>\r\n" + 
				"	<Transaction_Body>\r\n" + 
				"		<response>\r\n" + 
				"			<Rvl_Rcrd_Num><![CDATA[1]]></Rvl_Rcrd_Num>\r\n" + 
				"			<TOTAL_PAGE>1</TOTAL_PAGE>\r\n" + 
				"			<TOTAL_REC>1</TOTAL_REC>\r\n" + 
				"			<CURR_TOTAL_PAGE>1</CURR_TOTAL_PAGE>\r\n" + 
				"			<CURR_TOTAL_REC><![CDATA[1]]></CURR_TOTAL_REC>\r\n" + 
				"			<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"			<LIST1 p_type=\"G\">\r\n" + 
				"				<ExoStm_Py_Rmrk><![CDATA[1484304938977]]></ExoStm_Py_Rmrk>\r\n" + 
				"				<AccNo><![CDATA[13001618801050519604]]></AccNo>\r\n" + 
				"				<Txn_Dtl_No><![CDATA[114]]></Txn_Dtl_No>\r\n" + 
				"				<Enqr_Dtl_TpCd><![CDATA[2]]></Enqr_Dtl_TpCd>\r\n" + 
				"				<CCBS_Txn_CgyCd><![CDATA[02]]></CCBS_Txn_CgyCd>\r\n" + 
				"				<Txn_Dt><![CDATA[20181009]]></Txn_Dt>\r\n" + 
				"				<Txn_Tm><![CDATA[155720]]></Txn_Tm>\r\n" + 
				"				<DHAmt><![CDATA[0.00]]></DHAmt>\r\n" + 
				"				<Cr_HpnAm><![CDATA[190.00]]></Cr_HpnAm>\r\n" + 
				"				<Hpn_Amt><![CDATA[190.00]]></Hpn_Amt>\r\n" + 
				"				<AcBa><![CDATA[990.00]]></AcBa>\r\n" + 
				"				<Cntrprt_BrNo><![CDATA[130635808]]></Cntrprt_BrNo>\r\n" + 
				"				<CADBank_Nm><![CDATA[秦皇岛住房城建支行]]></CADBank_Nm>\r\n" + 
				"				<CntprtAcc><![CDATA[13001635808050514014]]></CntprtAcc>\r\n" + 
				"				<Cntrprt_AccNm><![CDATA[公司九七]]></Cntrprt_AccNm>\r\n" + 
				"				<CstPty_Py_Jrnl_No><![CDATA[1484304938977]]></CstPty_Py_Jrnl_No>\r\n" + 
				"				<CshEx_Cd><![CDATA[1]]></CshEx_Cd>\r\n" + 
				"				<Vchr_No><![CDATA[100002364191]]></Vchr_No>\r\n" + 
				"				<Vchr_TpCd><![CDATA[0038]]></Vchr_TpCd>\r\n" + 
				"				<Stm_Dt><![CDATA[20181009]]></Stm_Dt>\r\n" + 
				"				<Smy><![CDATA[网络转账]]></Smy>\r\n" + 
				"				<Smy_Cd><![CDATA[0332]]></Smy_Cd>\r\n" + 
				"				<CCBS_TxnSrlNo><![CDATA[1306358080NOPC7WZOU]]></CCBS_TxnSrlNo>\r\n" + 
				"				<Bnk_Only1_ID_ID><![CDATA[1306358080NOPC7WZOU114]]></Bnk_Only1_ID_ID>\r\n" + 
				"				<BO_ID><![CDATA[130635808]]></BO_ID>\r\n" + 
				"				<CCBS_Opr_No><![CDATA[333333]]></CCBS_Opr_No>\r\n" + 
				"				<Avl_Bal><![CDATA[990.00]]></Avl_Bal>\r\n" + 
				"				<Srpls_Can_Od_Lmt><![CDATA[0.00]]></Srpls_Can_Od_Lmt>\r\n" + 
				"				<Rmrk><![CDATA[11]]></Rmrk>\r\n" + 
				"				<DbtCrDrcCd><![CDATA[2]]></DbtCrDrcCd>\r\n" + 
				"				<CcyCd><![CDATA[156]]></CcyCd>\r\n" + 
				"				<Rltv_AccNo><![CDATA[13001618801050519604]]></Rltv_AccNo>\r\n" + 
				"				<Rltv_AccNo_Nm><![CDATA[]]></Rltv_AccNo_Nm>\r\n" + 
				"				<Dep_Txn_Medm_ID><![CDATA[]]></Dep_Txn_Medm_ID>\r\n" + 
				"			</LIST1>\r\n" + 
				"			<FILE_LIST_PACK p_type=\"G\">\r\n" + 
				"				<FILE_NUM><![CDATA[0]]></FILE_NUM>\r\n" + 
				"				<FILE_INFO p_type=\"G\">\r\n" + 
				"					<FILE_NAME><![CDATA[]]></FILE_NAME>\r\n" + 
				"				</FILE_INFO>\r\n" + 
				"			</FILE_LIST_PACK>\r\n" + 
				"		</response>\r\n" + 
				"	</Transaction_Body>\r\n" + 
				"</Transaction>";
		ResponseAccountDetailList list=parseQueryAccountDetail(accountDetailXML);
		logger.info("list size is:"+list.getAccountDetailList().size());
		logger.info("field value is:"+list.getAccountDetailList().get(0).getCADBank_Nm());
	}
	
	
	
	
	
	
	
	private static void test_parseQueryTransferResult() {
		//查询转换结果响应报文
		  String transferResultXML="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<Transaction>\r\n" + 
				"	<Transaction_Header>\r\n" + 
				"		<SYS_TX_VRSN><![CDATA[01]]></SYS_TX_VRSN>\r\n" + 
				"		<SYS_MSG_LEN><![CDATA[]]></SYS_MSG_LEN>\r\n" + 
				"		<SYS_RECV_TIME><![CDATA[20181009153657332]]></SYS_RECV_TIME>\r\n" + 
				"		<SYS_RESP_TIME><![CDATA[20181009153657573]]></SYS_RESP_TIME>\r\n" + 
				"		<SYS_PKG_STS_TYPE><![CDATA[01]]></SYS_PKG_STS_TYPE>\r\n" + 
				"		<SYS_TX_STATUS><![CDATA[00]]></SYS_TX_STATUS>\r\n" + 
				"		<SYS_RESP_CODE><![CDATA[000000000000]]></SYS_RESP_CODE>\r\n" + 
				"		<SYS_RESP_DESC_LEN><![CDATA[]]></SYS_RESP_DESC_LEN>\r\n" + 
				"		<SYS_RESP_DESC><![CDATA[成功]]></SYS_RESP_DESC>\r\n" + 
				"		<SYS_EVT_TRACE_ID><![CDATA[1010010011539070617002740]]></SYS_EVT_TRACE_ID>\r\n" + 
				"		<SYS_SND_SERIAL_NO><![CDATA[0000000000]]></SYS_SND_SERIAL_NO>\r\n" + 
				"		<TOTAL_PAGE>1</TOTAL_PAGE>\r\n" + 
				"		<TOTAL_REC>1</TOTAL_REC>\r\n" + 
				"		<CURR_TOTAL_PAGE>1</CURR_TOTAL_PAGE>\r\n" + 
				"		<CURR_TOTAL_REC><![CDATA[1]]></CURR_TOTAL_REC>\r\n" + 
				"		<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		<SYS_TX_CODE><![CDATA[P1CMSET36]]></SYS_TX_CODE>\r\n" + 
				"		<CHNL_CUST_NO><![CDATA[HE13000009021440001]]></CHNL_CUST_NO>\r\n" + 
				"		<tran_response>\r\n" + 
				"			<status><![CDATA[COMPLETE]]></status>\r\n" + 
				"		</tran_response>\r\n" + 
				"	</Transaction_Header>\r\n" + 
				"	<Transaction_Body>\r\n" + 
				"		<response>\r\n" + 
				"			<SvPt_Bsn_Dt><![CDATA[]]></SvPt_Bsn_Dt>\r\n" + 
				"			<Ret_File_Nm><![CDATA[]]></Ret_File_Nm>\r\n" + 
				"			<ASPD_ECD><![CDATA[]]></ASPD_ECD>\r\n" + 
				"			<WF_FCN_ID><![CDATA[]]></WF_FCN_ID>\r\n" + 
				"			<Rvl_Rcrd_Num><![CDATA[1]]></Rvl_Rcrd_Num>\r\n" + 
				"			<LIST1 p_type=\"G\">\r\n" + 
				"				<Txn_Dt><![CDATA[20181009]]></Txn_Dt>\r\n" + 
				"				<Txn_Tm><![CDATA[150933]]></Txn_Tm>\r\n" + 
				"				<Csz_Txn_ID><![CDATA[]]></Csz_Txn_ID>\r\n" + 
				"				<Csz_Txn_Nm><![CDATA[]]></Csz_Txn_Nm>\r\n" + 
				"				<CshMgt_TxnSrlNo><![CDATA[CN000s61201810091509334912307176]]></CshMgt_TxnSrlNo>\r\n" + 
				"				<WF_BIZ_ID><![CDATA[]]></WF_BIZ_ID>\r\n" + 
				"				<Cmpt_Rltv_Jrnl_No><![CDATA[]]></Cmpt_Rltv_Jrnl_No>\r\n" + 
				"				<CstPty_TxnSrlNo><![CDATA[1484304938975]]></CstPty_TxnSrlNo>\r\n" + 
				"				<Pyr_CCstTr_ID><![CDATA[CMN0000093068]]></Pyr_CCstTr_ID>\r\n" + 
				"				<Pyr_CCstTrNdID><![CDATA[ND97385045830423497300001]]></Pyr_CCstTrNdID>\r\n" + 
				"				<Pyr_DpBkNm><![CDATA[中国建设银行股份有限公司河北省分行直属支行]]></Pyr_DpBkNm>\r\n" + 
				"				<Pyr_DepBnk_No><![CDATA[130618801]]></Pyr_DepBnk_No>\r\n" + 
				"				<Pyr_BnkCD><![CDATA[]]></Pyr_BnkCD>\r\n" + 
				"				<Pyr_BkCgyCd><![CDATA[01]]></Pyr_BkCgyCd>\r\n" + 
				"				<Pyr_Cst_AccNo><![CDATA[13001618801050519642]]></Pyr_Cst_AccNo>\r\n" + 
				"				<Pyr_AccNm><![CDATA[公司某某]]></Pyr_AccNm>\r\n" + 
				"				<Pyr_Acc_CgyCd><![CDATA[02]]></Pyr_Acc_CgyCd>\r\n" + 
				"				<Pyr_Adr><![CDATA[]]></Pyr_Adr>\r\n" + 
				"				<RcvPrt_CCstTr_ID><![CDATA[]]></RcvPrt_CCstTr_ID>\r\n" + 
				"				<RcvPrt_CCstTrNdID><![CDATA[]]></RcvPrt_CCstTrNdID>\r\n" + 
				"				<RcvPrt_DpBkNm><![CDATA[中国建设银行股份有限公司秦皇岛住房城建支行]]></RcvPrt_DpBkNm>\r\n" + 
				"				<RcvPrt_DepBnk_No><![CDATA[130635808]]></RcvPrt_DepBnk_No>\r\n" + 
				"				<RcvPrt_BnkCD><![CDATA[]]></RcvPrt_BnkCD>\r\n" + 
				"				<RcvPrt_BkCgyCd><![CDATA[01]]></RcvPrt_BkCgyCd>\r\n" + 
				"				<RcvPrt_Cst_AccNo><![CDATA[13001635808050514014]]></RcvPrt_Cst_AccNo>\r\n" + 
				"				<RcvPtAc_Nm><![CDATA[公司九七]]></RcvPtAc_Nm>\r\n" + 
				"				<RcvPtAc_CgyCd><![CDATA[02]]></RcvPtAc_CgyCd>\r\n" + 
				"				<RcvPrt_Adr><![CDATA[]]></RcvPrt_Adr>\r\n" + 
				"				<Rqs_Amt>190.00</Rqs_Amt>\r\n" + 
				"				<HpnAm>190.00</HpnAm>\r\n" + 
				"				<Urgnt_TpCd><![CDATA[01]]></Urgnt_TpCd>\r\n" + 
				"				<Use_Nm><![CDATA[]]></Use_Nm>\r\n" + 
				"				<CshMgt_Use_ID><![CDATA[]]></CshMgt_Use_ID>\r\n" + 
				"				<Rmrk><![CDATA[11]]></Rmrk>\r\n" + 
				"				<RvPy_Bdgt_Use_ID><![CDATA[]]></RvPy_Bdgt_Use_ID>\r\n" + 
				"				<RvPy_Bdgt_Use_Nm><![CDATA[]]></RvPy_Bdgt_Use_Nm>\r\n" + 
				"				<Trgr_Amt>0.00</Trgr_Amt>\r\n" + 
				"				<Py_Cnd_TpCd><![CDATA[00]]></Py_Cnd_TpCd>\r\n" + 
				"				<STBal_Val>0.00</STBal_Val>\r\n" + 
				"				<Scl_Fctr>0.00000</Scl_Fctr>\r\n" + 
				"				<Roud_NbrOfBit><![CDATA[]]></Roud_NbrOfBit>\r\n" + 
				"				<RvPy_ExMd_Cd><![CDATA[0]]></RvPy_ExMd_Cd>\r\n" + 
				"				<Rsrvtn_Exec_Dt><![CDATA[]]></Rsrvtn_Exec_Dt>\r\n" + 
				"				<Rsrvtn_Exec_Tm><![CDATA[]]></Rsrvtn_Exec_Tm>\r\n" + 
				"				<Frq_MtdCd><![CDATA[]]></Frq_MtdCd>\r\n" + 
				"				<FxFrq_Exec_Dt_Dsc><![CDATA[]]></FxFrq_Exec_Dt_Dsc>\r\n" + 
				"				<Prsz_Inf_1><![CDATA[]]></Prsz_Inf_1>\r\n" + 
				"				<Prsz_Inf_2><![CDATA[]]></Prsz_Inf_2>\r\n" + 
				"				<Prsz_Inf_3><![CDATA[]]></Prsz_Inf_3>\r\n" + 
				"				<Prsz_Inf_4><![CDATA[]]></Prsz_Inf_4>\r\n" + 
				"				<Prsz_Inf_5><![CDATA[]]></Prsz_Inf_5>\r\n" + 
				"				<Prsz_Inf_6><![CDATA[]]></Prsz_Inf_6>\r\n" + 
				"				<Prsz_Inf_7><![CDATA[]]></Prsz_Inf_7>\r\n" + 
				"				<Prsz_Inf_8><![CDATA[]]></Prsz_Inf_8>\r\n" + 
				"				<Prsz_Inf_9><![CDATA[]]></Prsz_Inf_9>\r\n" + 
				"				<Prsz_Inf_10><![CDATA[]]></Prsz_Inf_10>\r\n" + 
				"				<Prsz_Inf_Nm_1><![CDATA[]]></Prsz_Inf_Nm_1>\r\n" + 
				"				<Prsz_Inf_Nm_2><![CDATA[]]></Prsz_Inf_Nm_2>\r\n" + 
				"				<Prsz_Inf_Nm_3><![CDATA[]]></Prsz_Inf_Nm_3>\r\n" + 
				"				<Prsz_Inf_Nm_4><![CDATA[]]></Prsz_Inf_Nm_4>\r\n" + 
				"				<Prsz_Inf_Nm_5><![CDATA[]]></Prsz_Inf_Nm_5>\r\n" + 
				"				<Prsz_Inf_Nm_6><![CDATA[]]></Prsz_Inf_Nm_6>\r\n" + 
				"				<Prsz_Inf_Nm_7><![CDATA[]]></Prsz_Inf_Nm_7>\r\n" + 
				"				<Prsz_Inf_Nm_8><![CDATA[]]></Prsz_Inf_Nm_8>\r\n" + 
				"				<Prsz_Inf_Nm_9><![CDATA[]]></Prsz_Inf_Nm_9>\r\n" + 
				"				<Prsz_Inf_Nm_10><![CDATA[]]></Prsz_Inf_Nm_10>\r\n" + 
				"				<Doc_Inf><![CDATA[]]></Doc_Inf>\r\n" + 
				"				<CshMgt_Txn_Rslt_Cd><![CDATA[1]]></CshMgt_Txn_Rslt_Cd>\r\n" + 
				"				<CCBS_Jrnl_No><![CDATA[1306188010N0PG6KXHC]]></CCBS_Jrnl_No>\r\n" + 
				"				<Hst_Txn_Dt><![CDATA[20181009]]></Hst_Txn_Dt>\r\n" + 
				"				<Cst_Inpt_Dt><![CDATA[]]></Cst_Inpt_Dt>\r\n" + 
				"				<Cst_Inpt_Tm><![CDATA[]]></Cst_Inpt_Tm>\r\n" + 
				"				<Cst_Dlv_Dt><![CDATA[20181009]]></Cst_Dlv_Dt>\r\n" + 
				"				<Cst_Dlv_Tm><![CDATA[114504215]]></Cst_Dlv_Tm>\r\n" + 
				"				<Cst_Csz_Dt><![CDATA[]]></Cst_Csz_Dt>\r\n" + 
				"				<Cst_Csz_Tm><![CDATA[]]></Cst_Csz_Tm>\r\n" + 
				"				<Tfr_MtdCd><![CDATA[5]]></Tfr_MtdCd>\r\n" + 
				"				<Rsrv_Fld_1><![CDATA[]]></Rsrv_Fld_1>\r\n" + 
				"				<Rsrv_Fld_2><![CDATA[]]></Rsrv_Fld_2>\r\n" + 
				"				<Rsrv_Fld_3><![CDATA[]]></Rsrv_Fld_3>\r\n" + 
				"				<Rsrv_Fld_4><![CDATA[]]></Rsrv_Fld_4>\r\n" + 
				"				<Rsrv_Fld_5><![CDATA[]]></Rsrv_Fld_5>\r\n" + 
				"				<Rsrv_Fld_6><![CDATA[]]></Rsrv_Fld_6>\r\n" + 
				"				<Rsrv_Fld_7><![CDATA[]]></Rsrv_Fld_7>\r\n" + 
				"				<Rsrv_Fld_8><![CDATA[]]></Rsrv_Fld_8>\r\n" + 
				"				<Rsrv_Fld_9><![CDATA[]]></Rsrv_Fld_9>\r\n" + 
				"				<Rsrv_Fld_10><![CDATA[]]></Rsrv_Fld_10>\r\n" + 
				"				<Err_Inf><![CDATA[]]></Err_Inf>\r\n" + 
				"				<CshMgt_Err_Cd><![CDATA[]]></CshMgt_Err_Cd>\r\n" + 
				"				<CstPty_Py_Jrnl_No><![CDATA[1484304938975]]></CstPty_Py_Jrnl_No>\r\n" + 
				"				<Cptl_Frz_TpCd><![CDATA[]]></Cptl_Frz_TpCd>\r\n" + 
				"			</LIST1>\r\n" + 
				"			<TOTAL_PAGE>1</TOTAL_PAGE>\r\n" + 
				"			<TOTAL_REC>1</TOTAL_REC>\r\n" + 
				"			<CURR_TOTAL_PAGE>1</CURR_TOTAL_PAGE>\r\n" + 
				"			<CURR_TOTAL_REC><![CDATA[1]]></CURR_TOTAL_REC>\r\n" + 
				"			<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		</response>\r\n" + 
				"	</Transaction_Body>\r\n" + 
				"</Transaction>";
		ResponseTransferResultList list=parseQueryTransferAccountResult(transferResultXML);
		logger.info("list size is:"+list.getTranferResultList().size());
		logger.info("filed CCBS_Jrnl_No's value is:"+list.getTranferResultList().get(0).getCCBS_Jrnl_No());
	}
	
	
	
	
	private static void test_parseTransferAccount() {
		//转账
		  String transferResponseXML="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<Transaction>\r\n" + 
				"	<Transaction_Header>\r\n" + 
				"		<SYS_TX_VRSN><![CDATA[01]]></SYS_TX_VRSN>\r\n" + 
				"		<SYS_MSG_LEN><![CDATA[]]></SYS_MSG_LEN>\r\n" + 
				"		<SYS_RECV_TIME><![CDATA[20181009160236665]]></SYS_RECV_TIME>\r\n" + 
				"		<SYS_RESP_TIME><![CDATA[20181009160240093]]></SYS_RESP_TIME>\r\n" + 
				"		<SYS_PKG_STS_TYPE><![CDATA[01]]></SYS_PKG_STS_TYPE>\r\n" + 
				"		<SYS_TX_STATUS><![CDATA[00]]></SYS_TX_STATUS>\r\n" + 
				"		<SYS_RESP_CODE><![CDATA[000000000000]]></SYS_RESP_CODE>\r\n" + 
				"		<SYS_RESP_DESC_LEN><![CDATA[]]></SYS_RESP_DESC_LEN>\r\n" + 
				"		<SYS_RESP_DESC><![CDATA[成功]]></SYS_RESP_DESC>\r\n" + 
				"		<SYS_EVT_TRACE_ID><![CDATA[1010010011539072156049230]]></SYS_EVT_TRACE_ID>\r\n" + 
				"		<SYS_SND_SERIAL_NO><![CDATA[0000000000]]></SYS_SND_SERIAL_NO>\r\n" + 
				"		<TOTAL_PAGE>0</TOTAL_PAGE>\r\n" + 
				"		<TOTAL_REC>0</TOTAL_REC>\r\n" + 
				"		<CURR_TOTAL_PAGE>0</CURR_TOTAL_PAGE>\r\n" + 
				"		<CURR_TOTAL_REC><![CDATA[0]]></CURR_TOTAL_REC>\r\n" + 
				"		<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		<SYS_TX_CODE><![CDATA[P1CMSET35]]></SYS_TX_CODE>\r\n" + 
				"		<CHNL_CUST_NO><![CDATA[HE13000009021440001]]></CHNL_CUST_NO>\r\n" + 
				"		<tran_response>\r\n" + 
				"			<status><![CDATA[COMPLETE]]></status>\r\n" + 
				"		</tran_response>\r\n" + 
				"	</Transaction_Header>\r\n" + 
				"	<Transaction_Body>\r\n" + 
				"		<response>\r\n" + 
				"			<SvPt_Bsn_Dt><![CDATA[]]></SvPt_Bsn_Dt>\r\n" + 
				"			<Ret_File_Nm><![CDATA[]]></Ret_File_Nm>\r\n" + 
				"			<ASPD_ECD><![CDATA[]]></ASPD_ECD>\r\n" + 
				"			<WF_FCN_ID><![CDATA[]]></WF_FCN_ID>\r\n" + 
				"			<CshMgt_TxnSrlNo><![CDATA[CN000s61201810091602084192307198]]></CshMgt_TxnSrlNo>\r\n" + 
				"			<Csz_Txn_ID><![CDATA[]]></Csz_Txn_ID>\r\n" + 
				"			<CCBS_Jrnl_No><![CDATA[1306358080NOPC7WZOU]]></CCBS_Jrnl_No>\r\n" + 
				"			<Hst_Txn_Dt><![CDATA[20181009]]></Hst_Txn_Dt>\r\n" + 
				"			<CshMgt_Txn_Rslt_Cd><![CDATA[1]]></CshMgt_Txn_Rslt_Cd>\r\n" + 
				"			<Err_Inf><![CDATA[]]></Err_Inf>\r\n" + 
				"			<CshMgt_Err_Cd><![CDATA[]]></CshMgt_Err_Cd>\r\n" + 
				"			<TOTAL_PAGE>0</TOTAL_PAGE>\r\n" + 
				"			<TOTAL_REC>0</TOTAL_REC>\r\n" + 
				"			<CURR_TOTAL_PAGE>0</CURR_TOTAL_PAGE>\r\n" + 
				"			<CURR_TOTAL_REC><![CDATA[0]]></CURR_TOTAL_REC>\r\n" + 
				"			<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		</response>\r\n" + 
				"	</Transaction_Body>\r\n" + 
				"</Transaction>";
		ResponseTransfer transfer=parseTransferAccount(transferResponseXML);
		logger.info("输入某个字段信息"+transfer.getCshMgt_TxnSrlNo());
	}
	
	
	
	
	private static void test_parseQueryAccountBalance() {
		//查询帐户余额XML-RESPONSE
		  String queryAccountBalanceResponse="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<Transaction>\r\n" + 
				"	<Transaction_Header>\r\n" + 
				"		<SYS_TX_VRSN><![CDATA[01]]></SYS_TX_VRSN>\r\n" + 
				"		<SYS_MSG_LEN><![CDATA[]]></SYS_MSG_LEN>\r\n" + 
				"		<SYS_RECV_TIME><![CDATA[20181009093229850]]></SYS_RECV_TIME>\r\n" + 
				"		<SYS_RESP_TIME><![CDATA[20181009093230667]]></SYS_RESP_TIME>\r\n" + 
				"		<SYS_PKG_STS_TYPE><![CDATA[01]]></SYS_PKG_STS_TYPE>\r\n" + 
				"		<SYS_TX_STATUS><![CDATA[00]]></SYS_TX_STATUS>\r\n" + 
				"		<SYS_RESP_CODE><![CDATA[000000000000]]></SYS_RESP_CODE>\r\n" + 
				"		<SYS_RESP_DESC_LEN><![CDATA[]]></SYS_RESP_DESC_LEN>\r\n" + 
				"		<SYS_RESP_DESC><![CDATA[成功]]></SYS_RESP_DESC>\r\n" + 
				"		<SYS_EVT_TRACE_ID><![CDATA[1010010011539048750247930]]></SYS_EVT_TRACE_ID>\r\n" + 
				"		<SYS_SND_SERIAL_NO><![CDATA[0000000000]]></SYS_SND_SERIAL_NO>\r\n" + 
				"		<TOTAL_PAGE>0</TOTAL_PAGE>\r\n" + 
				"		<TOTAL_REC>0</TOTAL_REC>\r\n" + 
				"		<CURR_TOTAL_PAGE>0</CURR_TOTAL_PAGE>\r\n" + 
				"		<CURR_TOTAL_REC><![CDATA[0]]></CURR_TOTAL_REC>\r\n" + 
				"		<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		<SYS_TX_CODE><![CDATA[P1CMSER18]]></SYS_TX_CODE>\r\n" + 
				"		<CHNL_CUST_NO><![CDATA[HE13000009021440001]]></CHNL_CUST_NO>\r\n" + 
				"		<tran_response>\r\n" + 
				"			<status><![CDATA[COMPLETE]]></status>\r\n" + 
				"		</tran_response>\r\n" + 
				"	</Transaction_Header>\r\n" + 
				"	<Transaction_Body>\r\n" + 
				"		<response>\r\n" + 
				"			<SvPt_Bsn_Dt><![CDATA[]]></SvPt_Bsn_Dt>\r\n" + 
				"			<Ret_File_Nm><![CDATA[]]></Ret_File_Nm>\r\n" + 
				"			<ASPD_ECD><![CDATA[]]></ASPD_ECD>\r\n" + 
				"			<WF_FCN_ID><![CDATA[]]></WF_FCN_ID>\r\n" + 
				"			<Rvl_Rcrd_Num><![CDATA[1]]></Rvl_Rcrd_Num>\r\n" + 
				
				"			<LIST1 p_type=\"G\">\r\n" + 
				"				<Acc_DpBkInNo><![CDATA[130618801]]></Acc_DpBkInNo>\r\n" + 
				"				<Acc_StCd><![CDATA[0]]></Acc_StCd>\r\n" + 
				"				<Acc_Ctrl_StCd><![CDATA[0]]></Acc_Ctrl_StCd>\r\n" + 
				"				<CCBS_Cst_ID><![CDATA[973850458304234973]]></CCBS_Cst_ID>\r\n" + 
				"				<Cst_Nm><![CDATA[公司七八]]></Cst_Nm>\r\n" + 
				"				<OpnAcc_Dt><![CDATA[20140710]]></OpnAcc_Dt>\r\n" + 
				"				<CnclAcct_Dt><![CDATA[]]></CnclAcct_Dt>\r\n" + 
				"				<Insn_Seq_No><![CDATA[]]></Insn_Seq_No>\r\n" + 
				"				<Cash_Cst_AccNo><![CDATA[13001618801050519642]]></Cash_Cst_AccNo>\r\n" + 
				"				<CshMgt_Acc_TpCd><![CDATA[1]]></CshMgt_Acc_TpCd>\r\n" + 
				"				<CcyCd><![CDATA[156]]></CcyCd>\r\n" + 
				"				<BkltNo><![CDATA[]]></BkltNo>\r\n" + 
				"				<Dep_DepSeqNo><![CDATA[]]></Dep_DepSeqNo>\r\n" + 
				"				<CshEx_Cd><![CDATA[1]]></CshEx_Cd>\r\n" + 
				"				<AcChar_Cd><![CDATA[1300]]></AcChar_Cd>\r\n" + 
				"				<YstdBl><![CDATA[]]></YstdBl>\r\n" + 
				"				<InfRpt_AcBa><![CDATA[0]]></InfRpt_AcBa>\r\n" + 
				"				<Acc_Avl_Bal><![CDATA[665.07]]></Acc_Avl_Bal>\r\n" + 
				"				<Frz_Amt><![CDATA[0]]></Frz_Amt>\r\n" + 
				"				<IntAr_Ind><![CDATA[Y]]></IntAr_Ind>\r\n" + 
				"				<DmdDep_Acm><![CDATA[0]]></DmdDep_Acm>\r\n" + 
				"				<Od_Amt><![CDATA[0]]></Od_Amt>\r\n" + 
				"				<Lqd_AcChar_Ind><![CDATA[0]]></Lqd_AcChar_Ind>\r\n" + 
				"				<Od_Dys><![CDATA[0]]></Od_Dys>\r\n" + 
				"				<DepBnk_Inst_Nm><![CDATA[中国建设银行股份有限公司河北省分行直属支行]]></DepBnk_Inst_Nm>\r\n" + 
				"				<Tms><![CDATA[20181009093201875]]></Tms>\r\n" + 
				"				<CshMgt_Rsp_Cd><![CDATA[000000000000]]></CshMgt_Rsp_Cd>\r\n" + 
				"				<Hst_Rsp_Inf><![CDATA[成功]]></Hst_Rsp_Inf>\r\n" + 
				"			</LIST1>\r\n" +
				
	"			<LIST1 p_type=\"G\">\r\n" + 
	"				<Acc_DpBkInNo><![CDATA[130618802]]></Acc_DpBkInNo>\r\n" + 
	"				<Acc_StCd><![CDATA[0]]></Acc_StCd>\r\n" + 
	"				<Acc_Ctrl_StCd><![CDATA[0]]></Acc_Ctrl_StCd>\r\n" + 
	"				<CCBS_Cst_ID><![CDATA[973850458304234973]]></CCBS_Cst_ID>\r\n" + 
	"				<Cst_Nm><![CDATA[公司七八]]></Cst_Nm>\r\n" + 
	"				<OpnAcc_Dt><![CDATA[20140710]]></OpnAcc_Dt>\r\n" + 
	"				<CnclAcct_Dt><![CDATA[]]></CnclAcct_Dt>\r\n" + 
	"				<Insn_Seq_No><![CDATA[]]></Insn_Seq_No>\r\n" + 
	"				<Cash_Cst_AccNo><![CDATA[13001618801050519642]]></Cash_Cst_AccNo>\r\n" + 
	"				<CshMgt_Acc_TpCd><![CDATA[1]]></CshMgt_Acc_TpCd>\r\n" + 
	"				<CcyCd><![CDATA[156]]></CcyCd>\r\n" + 
	"				<BkltNo><![CDATA[]]></BkltNo>\r\n" + 
	"				<Dep_DepSeqNo><![CDATA[]]></Dep_DepSeqNo>\r\n" + 
	"				<CshEx_Cd><![CDATA[1]]></CshEx_Cd>\r\n" + 
	"				<AcChar_Cd><![CDATA[1300]]></AcChar_Cd>\r\n" + 
	"				<YstdBl><![CDATA[]]></YstdBl>\r\n" + 
	"				<InfRpt_AcBa><![CDATA[0]]></InfRpt_AcBa>\r\n" + 
	"				<Acc_Avl_Bal><![CDATA[665.07]]></Acc_Avl_Bal>\r\n" + 
	"				<Frz_Amt><![CDATA[0]]></Frz_Amt>\r\n" + 
	"				<IntAr_Ind><![CDATA[Y]]></IntAr_Ind>\r\n" + 
	"				<DmdDep_Acm><![CDATA[0]]></DmdDep_Acm>\r\n" + 
	"				<Od_Amt><![CDATA[0]]></Od_Amt>\r\n" + 
	"				<Lqd_AcChar_Ind><![CDATA[0]]></Lqd_AcChar_Ind>\r\n" + 
	"				<Od_Dys><![CDATA[0]]></Od_Dys>\r\n" + 
	"				<DepBnk_Inst_Nm><![CDATA[中国建设银行股份有限公司河北省分行直属支行]]></DepBnk_Inst_Nm>\r\n" + 
	"				<Tms><![CDATA[20181009093201875]]></Tms>\r\n" + 
	"				<CshMgt_Rsp_Cd><![CDATA[000000000000]]></CshMgt_Rsp_Cd>\r\n" + 
	"				<Hst_Rsp_Inf><![CDATA[成功]]></Hst_Rsp_Inf>\r\n" + 
	"			</LIST1>\r\n" +
				
				"			<TOTAL_PAGE>0</TOTAL_PAGE>\r\n" + 
				"			<TOTAL_REC>0</TOTAL_REC>\r\n" + 
				"			<CURR_TOTAL_PAGE>0</CURR_TOTAL_PAGE>\r\n" + 
				"			<CURR_TOTAL_REC><![CDATA[0]]></CURR_TOTAL_REC>\r\n" + 
				"			<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		</response>\r\n" + 
				"	</Transaction_Body>\r\n" + 
				"</Transaction>";
		ResponseAccountBalanceList list=parseQueryAccountBalance(queryAccountBalanceResponse);
		logger.info("list size is:"+list.getAccountBalanceList().size());
	}
	
	/**
	 * 测试DOM4J 迭代器  已经测试
	 * @throws DocumentException 
	 */
	private static void test_Iterator() {
		//查询帐户余额XML-RESPONSE
		  String queryAccountBalanceResponse="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<Transaction>\r\n" + 
				"	<Transaction_Header>\r\n" + 
				"		<SYS_TX_VRSN><![CDATA[01]]></SYS_TX_VRSN>\r\n" + 
				"		<SYS_MSG_LEN><![CDATA[]]></SYS_MSG_LEN>\r\n" + 
				"		<SYS_RECV_TIME><![CDATA[20181009093229850]]></SYS_RECV_TIME>\r\n" + 
				"		<SYS_RESP_TIME><![CDATA[20181009093230667]]></SYS_RESP_TIME>\r\n" + 
				"		<SYS_PKG_STS_TYPE><![CDATA[01]]></SYS_PKG_STS_TYPE>\r\n" + 
				"		<SYS_TX_STATUS><![CDATA[00]]></SYS_TX_STATUS>\r\n" + 
				"		<SYS_RESP_CODE><![CDATA[000000000000]]></SYS_RESP_CODE>\r\n" + 
				"		<SYS_RESP_DESC_LEN><![CDATA[]]></SYS_RESP_DESC_LEN>\r\n" + 
				"		<SYS_RESP_DESC><![CDATA[成功]]></SYS_RESP_DESC>\r\n" + 
				"		<SYS_EVT_TRACE_ID><![CDATA[1010010011539048750247930]]></SYS_EVT_TRACE_ID>\r\n" + 
				"		<SYS_SND_SERIAL_NO><![CDATA[0000000000]]></SYS_SND_SERIAL_NO>\r\n" + 
				"		<TOTAL_PAGE>0</TOTAL_PAGE>\r\n" + 
				"		<TOTAL_REC>0</TOTAL_REC>\r\n" + 
				"		<CURR_TOTAL_PAGE>0</CURR_TOTAL_PAGE>\r\n" + 
				"		<CURR_TOTAL_REC><![CDATA[0]]></CURR_TOTAL_REC>\r\n" + 
				"		<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		<SYS_TX_CODE><![CDATA[P1CMSER18]]></SYS_TX_CODE>\r\n" + 
				"		<CHNL_CUST_NO><![CDATA[HE13000009021440001]]></CHNL_CUST_NO>\r\n" + 
				"		<tran_response>\r\n" + 
				"			<status><![CDATA[COMPLETE]]></status>\r\n" + 
				"		</tran_response>\r\n" + 
				"	</Transaction_Header>\r\n" + 
				"	<Transaction_Body>\r\n" + 
				"		<response>\r\n" + 
				"			<SvPt_Bsn_Dt><![CDATA[]]></SvPt_Bsn_Dt>\r\n" + 
				"			<Ret_File_Nm><![CDATA[]]></Ret_File_Nm>\r\n" + 
				"			<ASPD_ECD><![CDATA[]]></ASPD_ECD>\r\n" + 
				"			<WF_FCN_ID><![CDATA[]]></WF_FCN_ID>\r\n" + 
				"			<Rvl_Rcrd_Num><![CDATA[1]]></Rvl_Rcrd_Num>\r\n" + 
				
				"			<LIST1 p_type=\"G\">\r\n" + 
				"				<Acc_DpBkInNo><![CDATA[130618801]]></Acc_DpBkInNo>\r\n" + 
				"				<Acc_StCd><![CDATA[0]]></Acc_StCd>\r\n" + 
				"				<Acc_Ctrl_StCd><![CDATA[0]]></Acc_Ctrl_StCd>\r\n" + 
				"				<CCBS_Cst_ID><![CDATA[973850458304234973]]></CCBS_Cst_ID>\r\n" + 
				"				<Cst_Nm><![CDATA[公司七八]]></Cst_Nm>\r\n" + 
				"				<OpnAcc_Dt><![CDATA[20140710]]></OpnAcc_Dt>\r\n" + 
				"				<CnclAcct_Dt><![CDATA[]]></CnclAcct_Dt>\r\n" + 
				"				<Insn_Seq_No><![CDATA[]]></Insn_Seq_No>\r\n" + 
				"				<Cash_Cst_AccNo><![CDATA[13001618801050519642]]></Cash_Cst_AccNo>\r\n" + 
				"				<CshMgt_Acc_TpCd><![CDATA[1]]></CshMgt_Acc_TpCd>\r\n" + 
				"				<CcyCd><![CDATA[156]]></CcyCd>\r\n" + 
				"				<BkltNo><![CDATA[]]></BkltNo>\r\n" + 
				"				<Dep_DepSeqNo><![CDATA[]]></Dep_DepSeqNo>\r\n" + 
				"				<CshEx_Cd><![CDATA[1]]></CshEx_Cd>\r\n" + 
				"				<AcChar_Cd><![CDATA[1300]]></AcChar_Cd>\r\n" + 
				"				<YstdBl><![CDATA[]]></YstdBl>\r\n" + 
				"				<InfRpt_AcBa><![CDATA[0]]></InfRpt_AcBa>\r\n" + 
				"				<Acc_Avl_Bal><![CDATA[665.07]]></Acc_Avl_Bal>\r\n" + 
				"				<Frz_Amt><![CDATA[0]]></Frz_Amt>\r\n" + 
				"				<IntAr_Ind><![CDATA[Y]]></IntAr_Ind>\r\n" + 
				"				<DmdDep_Acm><![CDATA[0]]></DmdDep_Acm>\r\n" + 
				"				<Od_Amt><![CDATA[0]]></Od_Amt>\r\n" + 
				"				<Lqd_AcChar_Ind><![CDATA[0]]></Lqd_AcChar_Ind>\r\n" + 
				"				<Od_Dys><![CDATA[0]]></Od_Dys>\r\n" + 
				"				<DepBnk_Inst_Nm><![CDATA[中国建设银行股份有限公司河北省分行直属支行]]></DepBnk_Inst_Nm>\r\n" + 
				"				<Tms><![CDATA[20181009093201875]]></Tms>\r\n" + 
				"				<CshMgt_Rsp_Cd><![CDATA[000000000000]]></CshMgt_Rsp_Cd>\r\n" + 
				"				<Hst_Rsp_Inf><![CDATA[成功]]></Hst_Rsp_Inf>\r\n" + 
				"			</LIST1>\r\n" +
				
	"			<LIST1 p_type=\"G\">\r\n" + 
	"				<Acc_DpBkInNo><![CDATA[130618802]]></Acc_DpBkInNo>\r\n" + 
	"				<Acc_StCd><![CDATA[0]]></Acc_StCd>\r\n" + 
	"				<Acc_Ctrl_StCd><![CDATA[0]]></Acc_Ctrl_StCd>\r\n" + 
	"				<CCBS_Cst_ID><![CDATA[973850458304234973]]></CCBS_Cst_ID>\r\n" + 
	"				<Cst_Nm><![CDATA[公司七八]]></Cst_Nm>\r\n" + 
	"				<OpnAcc_Dt><![CDATA[20140710]]></OpnAcc_Dt>\r\n" + 
	"				<CnclAcct_Dt><![CDATA[]]></CnclAcct_Dt>\r\n" + 
	"				<Insn_Seq_No><![CDATA[]]></Insn_Seq_No>\r\n" + 
	"				<Cash_Cst_AccNo><![CDATA[13001618801050519642]]></Cash_Cst_AccNo>\r\n" + 
	"				<CshMgt_Acc_TpCd><![CDATA[1]]></CshMgt_Acc_TpCd>\r\n" + 
	"				<CcyCd><![CDATA[156]]></CcyCd>\r\n" + 
	"				<BkltNo><![CDATA[]]></BkltNo>\r\n" + 
	"				<Dep_DepSeqNo><![CDATA[]]></Dep_DepSeqNo>\r\n" + 
	"				<CshEx_Cd><![CDATA[1]]></CshEx_Cd>\r\n" + 
	"				<AcChar_Cd><![CDATA[1300]]></AcChar_Cd>\r\n" + 
	"				<YstdBl><![CDATA[]]></YstdBl>\r\n" + 
	"				<InfRpt_AcBa><![CDATA[0]]></InfRpt_AcBa>\r\n" + 
	"				<Acc_Avl_Bal><![CDATA[665.07]]></Acc_Avl_Bal>\r\n" + 
	"				<Frz_Amt><![CDATA[0]]></Frz_Amt>\r\n" + 
	"				<IntAr_Ind><![CDATA[Y]]></IntAr_Ind>\r\n" + 
	"				<DmdDep_Acm><![CDATA[0]]></DmdDep_Acm>\r\n" + 
	"				<Od_Amt><![CDATA[0]]></Od_Amt>\r\n" + 
	"				<Lqd_AcChar_Ind><![CDATA[0]]></Lqd_AcChar_Ind>\r\n" + 
	"				<Od_Dys><![CDATA[0]]></Od_Dys>\r\n" + 
	"				<DepBnk_Inst_Nm><![CDATA[中国建设银行股份有限公司河北省分行直属支行]]></DepBnk_Inst_Nm>\r\n" + 
	"				<Tms><![CDATA[20181009093201875]]></Tms>\r\n" + 
	"				<CshMgt_Rsp_Cd><![CDATA[000000000000]]></CshMgt_Rsp_Cd>\r\n" + 
	"				<Hst_Rsp_Inf><![CDATA[成功]]></Hst_Rsp_Inf>\r\n" + 
	"			</LIST1>\r\n" +
				
				"			<TOTAL_PAGE>0</TOTAL_PAGE>\r\n" + 
				"			<TOTAL_REC>0</TOTAL_REC>\r\n" + 
				"			<CURR_TOTAL_PAGE>0</CURR_TOTAL_PAGE>\r\n" + 
				"			<CURR_TOTAL_REC><![CDATA[0]]></CURR_TOTAL_REC>\r\n" + 
				"			<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		</response>\r\n" + 
				"	</Transaction_Body>\r\n" + 
				"</Transaction>";
		try {
			// 将字符串转为XML
			Document doc = DocumentHelper.parseText(queryAccountBalanceResponse);
			// 获取根节点
			Element rootElem = doc.getRootElement();

			// 获取BODY结点
			Element bodyElem = rootElem.element("Transaction_Body");

			Element responseElem = bodyElem.element("response");
			Iterator iter = responseElem.elementIterator("LIST1");
			while (iter.hasNext()) {
				Element list1Elem = (Element) iter.next();
				String deptNo = list1Elem.element("Acc_DpBkInNo").getTextTrim();
				logger.info("deptNo:" + deptNo);
			}
		} catch (Exception e) {

		}

	}
	
	/**
	 * 测试返回报文是否正常  已经测试 
	 */
	private static void test_isSuccessResponse() {
		//查询帐户余额XML-RESPONSE
		  String queryAccountBalanceResponse="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<Transaction>\r\n" + 
				"	<Transaction_Header>\r\n" + 
				"		<SYS_TX_VRSN><![CDATA[01]]></SYS_TX_VRSN>\r\n" + 
				"		<SYS_MSG_LEN><![CDATA[]]></SYS_MSG_LEN>\r\n" + 
				"		<SYS_RECV_TIME><![CDATA[20181009093229850]]></SYS_RECV_TIME>\r\n" + 
				"		<SYS_RESP_TIME><![CDATA[20181009093230667]]></SYS_RESP_TIME>\r\n" + 
				"		<SYS_PKG_STS_TYPE><![CDATA[01]]></SYS_PKG_STS_TYPE>\r\n" + 
				"		<SYS_TX_STATUS><![CDATA[00]]></SYS_TX_STATUS>\r\n" + 
				"		<SYS_RESP_CODE><![CDATA[000000000000]]></SYS_RESP_CODE>\r\n" + 
				"		<SYS_RESP_DESC_LEN><![CDATA[]]></SYS_RESP_DESC_LEN>\r\n" + 
				"		<SYS_RESP_DESC><![CDATA[成功]]></SYS_RESP_DESC>\r\n" + 
				"		<SYS_EVT_TRACE_ID><![CDATA[1010010011539048750247930]]></SYS_EVT_TRACE_ID>\r\n" + 
				"		<SYS_SND_SERIAL_NO><![CDATA[0000000000]]></SYS_SND_SERIAL_NO>\r\n" + 
				"		<TOTAL_PAGE>0</TOTAL_PAGE>\r\n" + 
				"		<TOTAL_REC>0</TOTAL_REC>\r\n" + 
				"		<CURR_TOTAL_PAGE>0</CURR_TOTAL_PAGE>\r\n" + 
				"		<CURR_TOTAL_REC><![CDATA[0]]></CURR_TOTAL_REC>\r\n" + 
				"		<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		<SYS_TX_CODE><![CDATA[P1CMSER18]]></SYS_TX_CODE>\r\n" + 
				"		<CHNL_CUST_NO><![CDATA[HE13000009021440001]]></CHNL_CUST_NO>\r\n" + 
				"		<tran_response>\r\n" + 
				"			<status><![CDATA[COMPLETE]]></status>\r\n" + 
				"		</tran_response>\r\n" + 
				"	</Transaction_Header>\r\n" + 
				"	<Transaction_Body>\r\n" + 
				"		<response>\r\n" + 
				"			<SvPt_Bsn_Dt><![CDATA[]]></SvPt_Bsn_Dt>\r\n" + 
				"			<Ret_File_Nm><![CDATA[]]></Ret_File_Nm>\r\n" + 
				"			<ASPD_ECD><![CDATA[]]></ASPD_ECD>\r\n" + 
				"			<WF_FCN_ID><![CDATA[]]></WF_FCN_ID>\r\n" + 
				"			<Rvl_Rcrd_Num><![CDATA[1]]></Rvl_Rcrd_Num>\r\n" + 
				
				"			<LIST1 p_type=\"G\">\r\n" + 
				"				<Acc_DpBkInNo><![CDATA[130618801]]></Acc_DpBkInNo>\r\n" + 
				"				<Acc_StCd><![CDATA[0]]></Acc_StCd>\r\n" + 
				"				<Acc_Ctrl_StCd><![CDATA[0]]></Acc_Ctrl_StCd>\r\n" + 
				"				<CCBS_Cst_ID><![CDATA[973850458304234973]]></CCBS_Cst_ID>\r\n" + 
				"				<Cst_Nm><![CDATA[公司七八]]></Cst_Nm>\r\n" + 
				"				<OpnAcc_Dt><![CDATA[20140710]]></OpnAcc_Dt>\r\n" + 
				"				<CnclAcct_Dt><![CDATA[]]></CnclAcct_Dt>\r\n" + 
				"				<Insn_Seq_No><![CDATA[]]></Insn_Seq_No>\r\n" + 
				"				<Cash_Cst_AccNo><![CDATA[13001618801050519642]]></Cash_Cst_AccNo>\r\n" + 
				"				<CshMgt_Acc_TpCd><![CDATA[1]]></CshMgt_Acc_TpCd>\r\n" + 
				"				<CcyCd><![CDATA[156]]></CcyCd>\r\n" + 
				"				<BkltNo><![CDATA[]]></BkltNo>\r\n" + 
				"				<Dep_DepSeqNo><![CDATA[]]></Dep_DepSeqNo>\r\n" + 
				"				<CshEx_Cd><![CDATA[1]]></CshEx_Cd>\r\n" + 
				"				<AcChar_Cd><![CDATA[1300]]></AcChar_Cd>\r\n" + 
				"				<YstdBl><![CDATA[]]></YstdBl>\r\n" + 
				"				<InfRpt_AcBa><![CDATA[0]]></InfRpt_AcBa>\r\n" + 
				"				<Acc_Avl_Bal><![CDATA[665.07]]></Acc_Avl_Bal>\r\n" + 
				"				<Frz_Amt><![CDATA[0]]></Frz_Amt>\r\n" + 
				"				<IntAr_Ind><![CDATA[Y]]></IntAr_Ind>\r\n" + 
				"				<DmdDep_Acm><![CDATA[0]]></DmdDep_Acm>\r\n" + 
				"				<Od_Amt><![CDATA[0]]></Od_Amt>\r\n" + 
				"				<Lqd_AcChar_Ind><![CDATA[0]]></Lqd_AcChar_Ind>\r\n" + 
				"				<Od_Dys><![CDATA[0]]></Od_Dys>\r\n" + 
				"				<DepBnk_Inst_Nm><![CDATA[中国建设银行股份有限公司河北省分行直属支行]]></DepBnk_Inst_Nm>\r\n" + 
				"				<Tms><![CDATA[20181009093201875]]></Tms>\r\n" + 
				"				<CshMgt_Rsp_Cd><![CDATA[000000000000]]></CshMgt_Rsp_Cd>\r\n" + 
				"				<Hst_Rsp_Inf><![CDATA[成功]]></Hst_Rsp_Inf>\r\n" + 
				"			</LIST1>\r\n" +
				
	"			<LIST1 p_type=\"G\">\r\n" + 
	"				<Acc_DpBkInNo><![CDATA[130618802]]></Acc_DpBkInNo>\r\n" + 
	"				<Acc_StCd><![CDATA[0]]></Acc_StCd>\r\n" + 
	"				<Acc_Ctrl_StCd><![CDATA[0]]></Acc_Ctrl_StCd>\r\n" + 
	"				<CCBS_Cst_ID><![CDATA[973850458304234973]]></CCBS_Cst_ID>\r\n" + 
	"				<Cst_Nm><![CDATA[公司七八]]></Cst_Nm>\r\n" + 
	"				<OpnAcc_Dt><![CDATA[20140710]]></OpnAcc_Dt>\r\n" + 
	"				<CnclAcct_Dt><![CDATA[]]></CnclAcct_Dt>\r\n" + 
	"				<Insn_Seq_No><![CDATA[]]></Insn_Seq_No>\r\n" + 
	"				<Cash_Cst_AccNo><![CDATA[13001618801050519642]]></Cash_Cst_AccNo>\r\n" + 
	"				<CshMgt_Acc_TpCd><![CDATA[1]]></CshMgt_Acc_TpCd>\r\n" + 
	"				<CcyCd><![CDATA[156]]></CcyCd>\r\n" + 
	"				<BkltNo><![CDATA[]]></BkltNo>\r\n" + 
	"				<Dep_DepSeqNo><![CDATA[]]></Dep_DepSeqNo>\r\n" + 
	"				<CshEx_Cd><![CDATA[1]]></CshEx_Cd>\r\n" + 
	"				<AcChar_Cd><![CDATA[1300]]></AcChar_Cd>\r\n" + 
	"				<YstdBl><![CDATA[]]></YstdBl>\r\n" + 
	"				<InfRpt_AcBa><![CDATA[0]]></InfRpt_AcBa>\r\n" + 
	"				<Acc_Avl_Bal><![CDATA[665.07]]></Acc_Avl_Bal>\r\n" + 
	"				<Frz_Amt><![CDATA[0]]></Frz_Amt>\r\n" + 
	"				<IntAr_Ind><![CDATA[Y]]></IntAr_Ind>\r\n" + 
	"				<DmdDep_Acm><![CDATA[0]]></DmdDep_Acm>\r\n" + 
	"				<Od_Amt><![CDATA[0]]></Od_Amt>\r\n" + 
	"				<Lqd_AcChar_Ind><![CDATA[0]]></Lqd_AcChar_Ind>\r\n" + 
	"				<Od_Dys><![CDATA[0]]></Od_Dys>\r\n" + 
	"				<DepBnk_Inst_Nm><![CDATA[中国建设银行股份有限公司河北省分行直属支行]]></DepBnk_Inst_Nm>\r\n" + 
	"				<Tms><![CDATA[20181009093201875]]></Tms>\r\n" + 
	"				<CshMgt_Rsp_Cd><![CDATA[000000000000]]></CshMgt_Rsp_Cd>\r\n" + 
	"				<Hst_Rsp_Inf><![CDATA[成功]]></Hst_Rsp_Inf>\r\n" + 
	"			</LIST1>\r\n" +
				
				"			<TOTAL_PAGE>0</TOTAL_PAGE>\r\n" + 
				"			<TOTAL_REC>0</TOTAL_REC>\r\n" + 
				"			<CURR_TOTAL_PAGE>0</CURR_TOTAL_PAGE>\r\n" + 
				"			<CURR_TOTAL_REC><![CDATA[0]]></CURR_TOTAL_REC>\r\n" + 
				"			<STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
				"		</response>\r\n" + 
				"	</Transaction_Body>\r\n" + 
				"</Transaction>";
		boolean success=isSuccessResponse(queryAccountBalanceResponse);
		logger.info("packet status:"+success);
	}
	
	
	public static  void main(String[] args) {
		//test_parseQueryAccountBalance();
		//test_parseTransferAccount();
		//test_parseQueryTransferResult();		
		//test_parseQueryAccountDetail();
		//test_parseQueryCertificate();		
		//test_parseCertificateDetail();
		//test_parseCertFileDetail();
		test_parseBatchWithhold();
	}
	
}
