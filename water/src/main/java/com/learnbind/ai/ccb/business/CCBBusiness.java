package com.learnbind.ai.ccb.business;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.learnbind.ai.ccb.des.DesCBC;
import com.learnbind.ai.ccb.fileutil.CCBFileUtil;
import com.learnbind.ai.ccb.keyfile.KeyFileUtil;
import com.learnbind.ai.ccb.md5.MD5withRSA;
import com.learnbind.ai.ccb.processor.DownPacketProcessor;
import com.learnbind.ai.ccb.processor.UpPacketProcessor;
import com.learnbind.ai.ccb.requestpacket.AccountForDetailList;
import com.learnbind.ai.ccb.requestpacket.AccountInfoForBalanceList;
import com.learnbind.ai.ccb.requestpacket.CCBTransaction;
import com.learnbind.ai.ccb.requestpacket.FileInfoList;
import com.learnbind.ai.ccb.responsepacket.ResponseAccountBalanceList;
import com.learnbind.ai.ccb.responsepacket.ResponseAccountDetailList;
import com.learnbind.ai.ccb.responsepacket.ResponseBatchWithhold;
import com.learnbind.ai.ccb.responsepacket.ResponseCertDetailFileModeList;
import com.learnbind.ai.ccb.responsepacket.ResponseCertDetailPacketModeList;
import com.learnbind.ai.ccb.responsepacket.ResponseCertStateList;
import com.learnbind.ai.ccb.responsepacket.ResponseTransfer;
import com.learnbind.ai.ccb.responsepacket.ResponseTransferResultList;
import com.learnbind.ai.ccb.sdk.CCBMD5withRSA;
import com.learnbind.ai.ccb.sdk.Des3Util;
import com.learnbind.ai.util.HttpClientUtil;

/**
 * 业务类,用于处理
 * 
 * @author lenovo
 */
@Service
public class CCBBusiness {

	private static final Logger logger = LoggerFactory.getLogger(CCBBusiness.class);

	// 请求参数名称
	static final String PARAM_NAME_CHANNEL_CUST_NO = "chanl_cust_no";
	static final String PARAM_NAME_XML = "xml";
	static final String PARAM_NAME_SIGNATURE = "signature";
	// 响应报文前比缀字节数 用于标识
	static final int RESPONSE_PACKET_PREFIX_LENGTH = 10;

	/**
	 * 功能: 签到CCB系统 注:此过程在密钥交换之后
	 * 
	 * @Param bus_url CCB提供的URL地址(业务请求地址)
	 * @param headerParams    请求报文头参数(所有的请求报文均需要加入此参数)
	 * 
	 *                        将以下三个参数封装在一个对象中. KeyParams
	 * @param desKey          自CCB获取得到的DES密钥
	 * @param localPrivateKey 营业收费系统本地RSA 私钥 用于对原文签名
	 * @Param ccbPublicKey 来自于CCB的RSA 公钥 用于对响应报文的签名进行验证.
	 * 
	 * @return 如果登录成功返回true,否则返回false;
	 * @throws Exception
	 */
	public boolean login(String bus_url, HeaderParams headerParams, KeyParams keyParams) throws Exception {

		boolean login = false;
		login = loginOrLogOut(bus_url, headerParams, keyParams, CCBTransaction.TRANSACTION_CODE_LOGIN);
		return login;
	}

	/**
	 * 功能:签退CCB系统 参数功能同签到
	 * 
	 * @param bus_url
	 * @param headerParams
	 * @param keyParams
	 * @return 如果签退成功则返回true,否则返回false;
	 * @throws Exception
	 */
	public boolean logout(String bus_url, HeaderParams headerParams, KeyParams keyParams) throws Exception {
		boolean logout = false;
		logout = loginOrLogOut(bus_url, headerParams, keyParams, CCBTransaction.TRANSACTION_CODE_LOGOUT);
		return logout;
	}

	/**
	 * 功能:发送批量代扣请求 注:此功能的调用需要在上传批量代后文件后进行.
	 * 
	 * @param bus_url      请求的URL地址(CCB端提供)
	 * @param headerParams 请求报文头参数
	 * @param keyParams    密钥对象
	 * @param fileListPack 已经传送的批量文件列表 注:每次只能存放一个文件
	 * @throws Exception
	 * 
	 *                   注:一般情况下:在发送批量代扣请求后,所返回的状态不必处理.
	 */
	public ResponseBatchWithhold requestBatchWithhold(String bus_url, HeaderParams headerParams, BodyParams bodyParams,
			KeyParams keyParams, FileInfoList fileListPack) throws Exception {
		// (0)生成请求报文(批量代扣报文)-明文
		String xml = DownPacketProcessor.genP1CLP1051(headerParams, bodyParams, fileListPack);
		logger.info("申请处理批量代扣文件请求报文："+xml);
		
		String desKeyBase64Str = keyParams.getDesKey();
		//byte[] desKey=Base64.decodeBase64(desKeyBase64Str);
		
		PrivateKey localPrivateKey = keyParams.getLocalPrivateKey();
		PublicKey ccbPublicKey = keyParams.getCcbPublicKey();

		// (1)生成请求参数
		Map<String, String> params = createPacketRequestParams(xml, desKeyBase64Str, localPrivateKey, headerParams);
		// (2)向CCB发送请求,采用字节流方式获取响应报文
		byte[] respData = HttpClientUtil.doPostRespBytes(bus_url, params);
		// (3)解密响应报文
		String plainXML = decryptResponse(respData, desKeyBase64Str, ccbPublicKey);
		logger.info("申请处理批量代扣文件响应报文："+plainXML);
		
		// (4)处理报文业务而后返回相应的业务对象
		ResponseBatchWithhold batchWithhold = null;
		if (!StringUtil.isEmpty(plainXML)) {
			batchWithhold = UpPacketProcessor.parseBatchWithhold(plainXML);
		}

		return batchWithhold;
	}

	/**
	 * 功能描述 No.54、代发代扣直联单据查询 服务类型: 联机服务 服务ID 业务代码: P1CLP1054 功能描述:
	 * 用于直连客户根据查询条件分页查询凭证状态信息
	 * 
	 * @param bus_url      CCB提供的URL
	 * @param txn_sn       凭证序号
	 * @param headerParams 请求头参数对象
	 * @param bodyParams   请求BODY参数对象
	 * @param keyParams    密钥对象
	 * @throws Exception
	 */
	public ResponseCertStateList queryWithholdCertState(String bus_url, String txn_sn, HeaderParams headerParams,
			BodyParams bodyParams, KeyParams keyParams) throws Exception {
		// (0)生成请求报文(批量代扣报文)-明文
		String xml = DownPacketProcessor.genP1CLP1054(txn_sn, headerParams, bodyParams);
		logger.info("查询批量代扣文件处理状态(查询凭证状态)请求报文:"+xml);
		
		String desKey = keyParams.getDesKey();
		PrivateKey localPrivateKey = keyParams.getLocalPrivateKey();
		PublicKey ccbPublicKey = keyParams.getCcbPublicKey();

		// (1)生成请求参数
		Map<String, String> params = createPacketRequestParams(xml, desKey, localPrivateKey, headerParams);

		// (2)向CCB发送请求,采用字节流方式获取响应报文
		byte[] respData = HttpClientUtil.doPostRespBytes(bus_url, params);

		// (3)解密响应报文
		String plainXML = decryptResponse(respData, desKey, ccbPublicKey);
		logger.info("查询批量代扣文件处理状态(查询凭证状态)响应报文:"+xml);
		
		// 处理报文业务
		// (4)处理报文业务
		ResponseCertStateList certStateList = null;
		if (!StringUtil.isEmpty(plainXML)) {
			certStateList = UpPacketProcessor.parseQueryCertState(plainXML);
		}
		return certStateList;

	}

	/**
	 * 查询凭证明细---报文方式
	 * 
	 * @param bus_url
	 * @param txn_sn
	 * @param headerParams
	 * @param keyParams    此命令在项目中暂时不使用.
	 */
	public ResponseCertDetailPacketModeList queryWithholdCertDetailPacketMode(String bus_url, String txn_sn,
			HeaderParams headerParams, KeyParams keyParams) {
		String plainXML = queryWithholdCertDetail(bus_url, txn_sn, CCBTransaction.TRANSACTION_RETURN_RESULT_CODE_PACKET,
				headerParams, keyParams);

		// 处理报文业务
		// (4)处理报文业务
		ResponseCertDetailPacketModeList certDetailList = null;
		if (!StringUtil.isEmpty(plainXML)) {
			certDetailList = UpPacketProcessor.parseCertDetailPacketMode(plainXML);
		}
		return certDetailList;
	}

	/**
	 * 查询凭证明细---文件方式
	 * 
	 * @param bus_url
	 * @param txn_sn
	 * @param headerParams
	 * @param keyParams    通过此指令获取汇总文件(也可以获取成功文件与失败文件)
	 */
	public ResponseCertDetailFileModeList queryWithholdCertDetailFileMode(String bus_url, String txn_sn,
			HeaderParams headerParams, KeyParams keyParams) {
		String plainXML = queryWithholdCertDetail(bus_url, txn_sn, CCBTransaction.TRANSACTION_RETURN_RESULT_CODE_FILE,
				headerParams, keyParams);

		// 处理报文业务
		ResponseCertDetailFileModeList certDetailList = null;
		if (!StringUtil.isEmpty(plainXML)) {
			certDetailList = UpPacketProcessor.parseCertDetailFileMode(plainXML);
		}
		return certDetailList;
	}

	/**
	 * 查询多账户余额 业务代码:P1CMSER18
	 * 
	 * @param headerParams              请求报文头参数(合约号,交易员编号,本地IP地址)
	 * @param queryAccountBalanceParams 请求报文参数:客户编号 现金客户树编号 现金客户树节点编号 电子合约编号 *
	 * @param accountInfoList           帐户信息列表(主要是帐号) 一般情况下只设置一个帐户对象到列表中
	 * @param keyParams                 加解密密钥对象
	 * @return 查询的帐户余额信息列表(其中的每个对象表示一个帐户的余额信息.)
	 * @throws Exception
	 */
	public ResponseAccountBalanceList queryAccountBalance(String bus_url, HeaderParams headerParams,
			QueryAccountBalanceParams queryAccountBalanceParams, AccountInfoForBalanceList accountInfoList,
			KeyParams keyParams) throws Exception {
		// (0)生成请求报文(批量代扣报文)-明文
		String xml = DownPacketProcessor.genP1CMSER18(headerParams, queryAccountBalanceParams, accountInfoList);
		logger.info("查询多账户余额 业务代码:P1CMSER18 请求报文："+xml);
		
		String desKey = keyParams.getDesKey();
		PrivateKey localPrivateKey = keyParams.getLocalPrivateKey();
		PublicKey ccbPublicKey = keyParams.getCcbPublicKey();

		// (1)生成请求参数
		Map<String, String> params = createPacketRequestParams(xml, desKey, localPrivateKey, headerParams);

		// (2)向CCB发送请求,采用字节流方式获取响应报文
		byte[] respData = HttpClientUtil.doPostRespBytes(bus_url, params);

		// (3)解密响应报文
		String plainXML = decryptResponse(respData, desKey, ccbPublicKey);
		logger.info("查询多账户余额 业务代码:P1CMSER18 响应报文："+plainXML);

		// (4)处理报文业务
		ResponseAccountBalanceList accountBalanceList = null;
		if (!StringUtil.isEmpty(plainXML)) {
			accountBalanceList = UpPacketProcessor.parseQueryAccountBalance(plainXML);
		}

		return accountBalanceList;
	}

	/**
	 * 功能:查询帐户明细
	 * 
	 * @param bus_url                  业务URL(CCB提供)
	 * @param headerParams             请求报文头参数
	 * @param queryAccountDetailParams 请求报文(业务相关参数-在类中已做说明)
	 * @param accountList              需要查询明细的账户对象列表
	 * @throws Exception
	 */
	public ResponseAccountDetailList queryAccountDetail(String bus_url, HeaderParams headerParams,
			QueryAccountDetailParams queryAccountDetailParams, AccountForDetailList accountList, KeyParams keyParams)
			throws Exception {
		// (0)生成请求报文(批量代扣报文)-明文
		String xml = DownPacketProcessor.genP1CMSER65(headerParams, queryAccountDetailParams, accountList);
		logger.info("查询帐户明细请求报文："+xml);

		String desKey = keyParams.getDesKey();
		PrivateKey localPrivateKey = keyParams.getLocalPrivateKey();
		PublicKey ccbPublicKey = keyParams.getCcbPublicKey();

		// (1)生成请求参数
		Map<String, String> params = createPacketRequestParams(xml, desKey, localPrivateKey, headerParams);

		// (2)向CCB发送请求,采用字节流方式获取响应报文
		byte[] respData = HttpClientUtil.doPostRespBytes(bus_url, params);

		// (3)解密响应报文
		String plainXML = decryptResponse(respData, desKey, ccbPublicKey);
		logger.info("查询帐户明细响应报文："+plainXML);
		
		// (4)处理报文业务
		ResponseAccountDetailList accountDetailList = null;
		if (!StringUtil.isEmpty(plainXML)) {
			accountDetailList = UpPacketProcessor.parseQueryAccountDetail(plainXML);
		}
		return accountDetailList;
	}

	/**
	 * 功能:转帐 业务代码:P1CMSET35
	 * 
	 * @param bus_url        CCB提供的业务URL
	 * @param headerParams   请求报文头参数
	 * @param transferParams 请求报文(业务部分参数)BODY参数
	 * @param keyParams      密钥参数
	 * @throws Exception
	 */
	public ResponseTransfer transferAccount(String bus_url, HeaderParams headerParams, TransferParams transferParams,
			KeyParams keyParams) throws Exception {
		// (0)生成请求报文(批量代扣报文)-明文
		String xml = DownPacketProcessor.genP1CMSET35(headerParams, transferParams);
		logger.info("转帐 业务代码:P1CMSET35 请求报文："+xml);
		
		String desKey = keyParams.getDesKey();
		PrivateKey localPrivateKey = keyParams.getLocalPrivateKey();
		PublicKey ccbPublicKey = keyParams.getCcbPublicKey();

		// (1)生成请求参数
		Map<String, String> params = createPacketRequestParams(xml, desKey, localPrivateKey, headerParams);

		// (2)向CCB发送请求,采用字节流方式获取响应报文
		byte[] respData = HttpClientUtil.doPostRespBytes(bus_url, params);

		// (3)解密响应报文
		String plainXML = decryptResponse(respData, desKey, ccbPublicKey);
		logger.info("转帐 业务代码:P1CMSET35 响应报文："+plainXML);
		
		// (4)处理报文业务
		ResponseTransfer responseTransfer = null;
		if (!StringUtil.isEmpty(plainXML)) {
			responseTransfer = UpPacketProcessor.parseTransferAccount(plainXML);
		}
		return responseTransfer;

	}

	/**
	 * 功能:查询转账结果 业务代码:P1CMSET36
	 * 
	 * @throws Exception
	 * 
	 */
	public ResponseTransferResultList queryTransferAccountResult(String bus_url, HeaderParams headerParams,
			TransferResultParams transferResultParams, KeyParams keyParams) throws Exception {
		// (0)生成请求报文(批量代扣报文)-明文
		String xml = DownPacketProcessor.genP1CMSET36(headerParams, transferResultParams);
		logger.info("查询转账结果 业务代码:P1CMSET36 请求报文："+xml);
		
		String desKey = keyParams.getDesKey();
		PrivateKey localPrivateKey = keyParams.getLocalPrivateKey();
		PublicKey ccbPublicKey = keyParams.getCcbPublicKey();

		// (1)生成请求参数
		Map<String, String> params = createPacketRequestParams(xml, desKey, localPrivateKey, headerParams);

		// (2)向CCB发送请求,采用字节流方式获取响应报文
		byte[] respData = HttpClientUtil.doPostRespBytes(bus_url, params);

		// (3)解密响应报文
		String plainXML = decryptResponse(respData, desKey, ccbPublicKey);
		logger.info("查询转账结果 业务代码:P1CMSET36 响应报文："+plainXML);
		
		// (4)处理报文业务
		ResponseTransferResultList transferResultList = null;
		if (!StringUtil.isEmpty(plainXML)) {
			transferResultList = UpPacketProcessor.parseQueryTransferAccountResult(plainXML);
		}

		return transferResultList;
	}

	/**
	 * 文件上传接口 企业现金管理直连客户应用接口规范15 POST 参数 1：chanl_cust_no=电子银行合约编号 POST 参数
	 * 2：filename=文件名（不含路径的文件名，DESede(3DES)对称密钥加密） POST 参数
	 * 3：signature=数字签名（对文件名作的数字签名） POST 参数 4：data=文件内容（DESede(3DES)对称密钥加密） POST 参数
	 * 5：dirflag=是否上传至子目录（0-否，1-是，DESede(3DES)对称密钥加密)， 注： E
	 * 单通的质押清单文件上传时必须填1，其他情况请一律填 0） 在本项目中此处填写0 注：除参数 1 外的其他参数在加密或签名后都需做 Base64 编码。
	 */
	/**
	 * @param uploadUrl     上传文件url (CCB端提供)
	 * @param chanl_cust_no 电子银行合约编号
	 * @param filePath      文件路径
	 * @param fileName      文件名称
	 * @param keyParams     key参数对象
	 * @return 如果上传成功,则返回自CCB端响应的文件名称,否则返回""
	 * @throws Exception
	 */
	public String uploadFileToCCB(String uploadUrl, String chanl_cust_no, String filePath, String fileName,
			KeyParams keyParams) throws Exception {
		final String UPLOAD_RESULT_SUCCESS = "SUCCESS";
		final String UPLOAD_RESULT_FAIL = "FAIL";
		final String SPLIT_STR = "\\|";

		// (1)准备请求参数
		Map<String, String> params = createUploadFileRequestParams(chanl_cust_no, filePath, fileName, keyParams);
		logger.info("批量代扣文件上传请求参数："+params);
		
		// (2)向CCB发送请求,采用字节流方式获取响应报文
		byte[] respData = HttpClientUtil.doPostRespBytes(uploadUrl, params);

		// (3)对响应的内容进行处理.
		String desKey = keyParams.getDesKey();
		// PrivateKey localPrivateKey = keyParams.getLocalPrivateKey();
		PublicKey ccbPublicKey = keyParams.getCcbPublicKey();
		String plainText = decryptResponse(respData, desKey, ccbPublicKey);
		logger.info("批量代扣文件上传响应报文："+plainText);
		
		// (4)对响应结果进行分析
		// SUCCESS|||20140723/440106198595270330.DAT
		// FAIL|0001|文件上传错误|
		String returnFileName = "";
		if (!StringUtil.isEmpty(plainText)) {
			String[] fields = plainText.split(SPLIT_STR);
			if (fields[0].equalsIgnoreCase(UPLOAD_RESULT_SUCCESS)) {
				returnFileName = fields[3];
				logger.info("文件上传成功");
			}
		}

		return returnFileName;
	}

	/*
	 * 使用 HTTP POST 方法提交请求道这个地址： http://ip:port/interlink/DownloadFile POST 参数
	 * 1：chanl_cust_no=电子银行合约编号 POST 参数
	 * 2：filename=文件名（通知生成文件交易返回的相对路径，DESede(3DES)对称密 钥加密） POST 参数
	 * 3：signature=数字签名（对文件名作的数字签名） 注：除参数 1 外的其他参数在加密或签名后都需做 Base64 编码。
	 */

	/*
	 * 企业客户端接收到返回结果时，可先按照带数字签名的格式解密，如果解密并验证签名 成功，
	 * 则表示接收到的是下载失败的错误信息；如果解密或验证失败，表示接收到的是文件内容。
	 */

	/**
	 * @param downloadUrl   CCB提供的文件下载地址URL
	 * @param chanl_cust_no 电子银行合约编号
	 * @param fileName      文件名称
	 * @param keyParams     密钥参数
	 * @return map<String,String>
	 *         如果文件下载成功,则返回相应的文件内容,(UTF-8编码的文本内容),可将此处接收到的内容写入到业务相关的文件中. code=1
	 *         msg=文件中内容(utf-8编码) 如果文件下载失败: code=0 msg= 错误信息
	 * @throws Exception
	 */
	public Map<String, String> downloadFileFromCCB(String downloadUrl, String chanl_cust_no, String fileName,
			KeyParams keyParams) throws Exception {
		// Map中KEY常量
		final String KEY_CODE = "code";
		final String KEY_MSG = "msg";
		// code常量定义.
		final String CODE_DOWNLOAD_SUCCESS = "1"; // 文件下载成功
		final String CODE_DOWNLOAD_FAIL = "0"; // 文件下载失败

		// (1)准备请求参数
		Map<String, String> params = createDownloadFileRequestParams(chanl_cust_no, fileName, keyParams);
		logger.info("下载批量代扣文件处理结果请求参数："+params);
		
		// (2)向CCB发送请求,采用字节流方式获取响应报文
		byte[] respData = HttpClientUtil.doPostRespBytes(downloadUrl, params);

		// (3)对响应的内容进行处理.
		String desKey = keyParams.getDesKey();
		// PrivateKey localPrivateKey = keyParams.getLocalPrivateKey();
		PublicKey ccbPublicKey = keyParams.getCcbPublicKey();
		String plainText = decryptResponse(respData, desKey, ccbPublicKey);
		logger.info("下载批量代扣文件处理结果响应报文："+plainText);
		
		Map<String, String> map = new HashMap<>();
		String resultTxt = "";
		// (4)判定是否返回的是文件内容.
		// 如果解密后生成的结果为空,则说明所接收的是文件内容.
		// 否则说明CCB返回的是错误信息.
		if (StringUtils.isBlank(plainText)) {// 文件内容
			// 对文件内容进行解密处理
			//String encryptPacketStr = new String(respData, "UTF-8");
			resultTxt = Des3Util.decryptUseDES3(respData, desKey, "GBK"); // 解密后的文件内容
			map.put(KEY_CODE, CODE_DOWNLOAD_SUCCESS);
			map.put(KEY_MSG, resultTxt);
		} else {
			// 显示相应的错误信息.
			// 此错误信息是否需要反馈到调用端.
			logger.info("文件下载时出现错误:" + plainText);
			map.put(KEY_CODE, CODE_DOWNLOAD_FAIL);
			map.put(KEY_MSG, plainText);

		}
		return map;
	}

	/**
	 * @param bus_url
	 * @param txn_sn
	 * @param retResultCode TRANSACTION_RETURN_RESULT_CODE_PACKET="1"; //明细返回方式_报文
	 *                      TRANSACTION_RETURN_RESULT_CODE_FILE="2"; //明细返回方式_文件
	 * @param headerParams
	 */
	private String queryWithholdCertDetail(String bus_url, String txn_sn, String retResultCode,
			HeaderParams headerParams, KeyParams keyParams) {

		try {
			// (0)生成请求报文(批量代扣报文)-明文
			String xml = DownPacketProcessor.genP1CLP1055(txn_sn, retResultCode, headerParams);
			if(retResultCode.equals(CCBTransaction.TRANSACTION_RETURN_RESULT_CODE_PACKET)) {
				logger.info("查询凭证明细-报文方式请求报文："+xml);
			}else {
				logger.info("查询凭证明细-文件方式请求报文："+xml);
			}
			
			String desKey = keyParams.getDesKey();
			PrivateKey localPrivateKey = keyParams.getLocalPrivateKey();
			PublicKey ccbPublicKey = keyParams.getCcbPublicKey();

			// (1)生成请求参数
			Map<String, String> params = createPacketRequestParams(xml, desKey, localPrivateKey, headerParams);

			// (2)向CCB发送请求,采用字节流方式获取响应报文
			byte[] respData = HttpClientUtil.doPostRespBytes(bus_url, params);

			// (3)解密响应报文
			String plainXML = decryptResponse(respData, desKey, ccbPublicKey);
			if(retResultCode.equals(CCBTransaction.TRANSACTION_RETURN_RESULT_CODE_PACKET)) {
				logger.info("查询凭证明细-报文方式响应报文："+plainXML);
			}else {
				logger.info("查询凭证明细-文件方式响应报文："+plainXML);
			}
			return plainXML;

		} catch (Exception e) {
			logger.info(e.getMessage());
			return "";
		}
	}

	/**
	 * 签到/签退
	 * 
	 * @param bus_url
	 * @param headerParams
	 * @param keyParams
	 * @param transactionCode 签到或签退的业务代码(见CCB开发文档)
	 * @return
	 * @throws Exception
	 */
	private boolean loginOrLogOut(String bus_url, HeaderParams headerParams, KeyParams keyParams,
			String transactionCode) throws Exception {
		boolean logFlag = false;

		String desKey = keyParams.getDesKey();
		PrivateKey localPrivateKey = keyParams.getLocalPrivateKey();
		PublicKey ccbPublicKey = keyParams.getCcbPublicKey();

		// (0)生成报文-明文
		String xml = DownPacketProcessor.genP1OPME00X(transactionCode, headerParams);
		logger.info("----------签到/签退请求报文："+xml);
		// (1)生成请求参数
		Map<String, String> params = createPacketRequestParams(xml, desKey, localPrivateKey, headerParams);

		// (2)向CCB发送请求,采用字节流方式获取响应报文
		byte[] respData = HttpClientUtil.doPostRespBytes(bus_url, params);

		// (3)解密响应报文
		String plainXML = decryptResponse(respData, desKey, ccbPublicKey);
		logger.info("----------签到/签退响应报文："+plainXML);

		// (4)处理报文业务
		if (!StringUtil.isEmpty(plainXML)) {
			logFlag = UpPacketProcessor.isSuccessResponse(plainXML);
			if (transactionCode.equalsIgnoreCase(CCBTransaction.TRANSACTION_CODE_LOGIN)) {
				logger.info("签到CCB成功.....");
			} else {
				logger.info("签退CCB成功.....");
			}
		}
		return logFlag;
	}

	/**
	 * 生成下载文件时的请求参数
	 * 
	 * @param chanl_cust_no
	 * @param fileName
	 * @param keyParams
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> createDownloadFileRequestParams(String chanl_cust_no, String fileName,
			KeyParams keyParams) throws Exception {
		// 参数名称
		final String PARAM_CHANL_CUST_NO = "chanl_cust_no";
		final String PARAM_FILENAME = "filename";
		final String PARAM_SIGNATURE = "signature";

		String desKey = keyParams.getDesKey();
		PrivateKey localPrivateKey = keyParams.getLocalPrivateKey();

		// (1)准备请求参数
		Map<String, String> params = new HashMap<>();
		// (1.1)参数-电子银行合约编号
		params.put(PARAM_CHANL_CUST_NO, chanl_cust_no);
		// (1.2)生成文件名称密文
		String encyptFileNameBase64 = Des3Util.encryptUseDES3(fileName, desKey, "UTF-8");
		params.put(PARAM_FILENAME, encyptFileNameBase64);

		// (1.3)对文件名做数字签名
		// 生成签名算法 MD5withRSA
		byte[] signByte = CCBMD5withRSA.signMethod(fileName, localPrivateKey); // 生成签名
		//String signBase64Str = Base64.getEncoder().encodeToString(signByte);
		String signBase64Str = Base64.encodeBase64String(signByte);
		params.put(PARAM_SIGNATURE, signBase64Str);

		return params;
	}

	/**
	 * 生成上传文件的请求参数
	 * 
	 * @param chanl_cust_no
	 * @param filePath
	 * @param fileName
	 * @param keyParams
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> createUploadFileRequestParams(String chanl_cust_no, String filePath, String fileName,
			KeyParams keyParams) throws Exception {
		// 参数名称
		final String PARAM_CHANL_CUST_NO = "chanl_cust_no";
		final String PARAM_FILENAME = "filename";
		final String PARAM_SIGNATURE = "signature";
		final String PARAM_DATA = "data";
		final String PARAM_DIRFLAG = "dirflag";

		String desKey = keyParams.getDesKey();
		PrivateKey localPrivateKey = keyParams.getLocalPrivateKey();
		// PublicKey ccbPublicKey = keyParams.getCcbPublicKey();

		// (1)准备请求参数
		Map<String, String> params = new HashMap<>();
		// (1.1)参数-电子银行合约编号
		params.put(PARAM_CHANL_CUST_NO, chanl_cust_no);
		// (1.2)生成文件名称密文
		String encyptFileNameBase64 = Des3Util.encryptUseDES3(fileName, desKey, "UTF-8");
		params.put(PARAM_FILENAME, encyptFileNameBase64);

		// (1.3)对文件名做数字签名
		// 生成签名算法 MD5withRSA
		byte[] signByte = CCBMD5withRSA.signMethod(fileName, localPrivateKey); // 生成签名
		//String signBase64Str = Base64.getEncoder().encodeToString(signByte);
		String signBase64Str = Base64.encodeBase64String(signByte);
		params.put(PARAM_SIGNATURE, signBase64Str);

		// (1.4) 对data进行加密处理
		byte[] dataStr = CCBFileUtil.readToByte(filePath, fileName);
		String encyptDataBase64 = Des3Util.encryptUseDES3(dataStr, desKey, "UTF-8");
		params.put(PARAM_DATA, encyptDataBase64);

		// (1.5)dirflag=是否上传至子目录
		String encyptDirFlagBase64 = Des3Util.encryptUseDES3("0", desKey, "UTF-8");
		params.put(PARAM_DIRFLAG, encyptDirFlagBase64);

		return params;
	}

	/**
	 * 生成发送到CCB的HTTP请求的参数
	 * 
	 * @param xml             请求报文明文
	 * @param desKey          自CCB获取的DES KEY,用于对报文加密
	 * @param localPrivateKey 营业系统自身的RSA私钥,用于生成报文明文的签名
	 * @param headerParams    请求报文头参数
	 * @return 参数列表
	 * @throws Exception
	 */
	private Map<String, String> createPacketRequestParams(String xml, String desKey, PrivateKey localPrivateKey,
			HeaderParams headerParams) throws Exception {
//		请求参数格式		
//		POST 参数 1：chanl_cust_no=渠道客户号
//		POST 参数 2：xml=交易请求报文
//		POST 参数 3：signature=数字签名

//		参数加密过程如下所示:		
//		报文加解密算法为：DESede (3DES)  采用自CCB获取的密钥
//		数字签名算法为：MD5withRSA		采用营业收费系统RSA私钥
//		加密前字符集为：UTF-8
//		解密后字符集为：UTF-8
//		byte[]与字符串互相转换的编码：Base64
		
		//byte[] desKeyArr=Base64.decodeBase64(desKey);

		Map<String, String> params = new HashMap<>(); // 请求参数列表
		params.put(PARAM_NAME_CHANNEL_CUST_NO, headerParams.getChanl_cust_no());

		// 生成请求参数
		// (1)生成签名 MD5withRSA
		byte[] signByte = CCBMD5withRSA.signMethod(xml, localPrivateKey); // 生成签名
		//String signBase64Str = Base64.getEncoder().encodeToString(signByte);
		String signBase64Str = Base64.encodeBase64String(signByte);
		params.put(PARAM_NAME_SIGNATURE, signBase64Str);

		// (2)对XML进行3DES加密
		String encyptXML = Des3Util.encryptUseDES3(xml, desKey, "UTF-8"); // 生成密文
		params.put(PARAM_NAME_XML, encyptXML);

		return params;
	}

	/**
	 * 解密出报文请求响应:报文原文
	 * 
	 * @param respData 接收到的数据
	 * @return 如成功解密:报文原文 否则返回null
	 * @throws Exception
	 */
	private static String decryptResponse(byte[] respData, String desKey, PublicKey ccbPublicKey) {

//		  返回的 byte[]的格式：数字签名的长度(固定长度 10 个字节) +数字签名数据+加密后的数 据。解密时应先根据前10
//		  位算出数字签名的长度，然后再截取数字签名及报文密文。 如： byte[] len_byte = new byte[10] System.
//		  Arraycopy(data，0，len_byte，0，10); //len_byte 中的内容是 10
//		  个字符表示的长度，如“0000000128”，表示数字签名长度为 128 字节。

//		  public static native void arraycopy(Object src, int srcPos, Object dest, int
//		  destPos, int length);

		String plainText = null;
		
		//byte[] desKey=Base64.decodeBase64(desKeyStr);

		try {
			// (1)数字签名长度
			byte[] lenByte = new byte[RESPONSE_PACKET_PREFIX_LENGTH]; // 此部分数据未加密
			System.arraycopy(respData, 0, lenByte, 0, RESPONSE_PACKET_PREFIX_LENGTH);
			int signLen = Integer.parseInt(new String(lenByte, "UTF-8")); // 获取到数据签名的长度
			
			// (2)数字签名部分
			byte[] base64SignData = new byte[signLen]; // 签名数据 BASE64编码
			System.arraycopy(respData, RESPONSE_PACKET_PREFIX_LENGTH, base64SignData, 0, signLen);
			//byte[] sign = Base64.getDecoder().decode(base64SignData); // 签名数据 BASE64解码
			//logger.info("签名的BASE64字符串是:"+new String(base64SignData,"utf-8"));
			//byte[] sign = Base64.decodeBase64(base64SignData); // 签名数据 BASE64解码

			// (3)报文密文部分
			int packetLen = respData.length - RESPONSE_PACKET_PREFIX_LENGTH - signLen;
			byte[] base64PacketData = new byte[packetLen]; // BASE64编码
			System.arraycopy(respData, RESPONSE_PACKET_PREFIX_LENGTH + signLen, base64PacketData, 0, packetLen);

			// (4)解密出原文
			String plainXml = Des3Util.decryptUseDES3(base64PacketData, desKey, "UTF-8");
			//logger.info("解密后的明文是:"+plainXml);

			// (5)对签名进行验证(CCB端采用自身的私钥对报文签名,以接收端采用CCB的公钥对签名进行验证)
			boolean verify = CCBMD5withRSA.verifyMethod(plainXml, base64SignData, ccbPublicKey);
			if (verify) { // 如果验证通过,则开始处理报文
				plainText = plainXml;
				logger.info("checked ok-----------------");
			}
			else {
				logger.info("checked errro-----------------");
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return plainText;

	}
	
	

	// -----------------------以下为测试代码--------------------------
	private static void test_decryptRequestResponse1() throws Exception {
		// DES密钥
		String desKeyBase64 = "sLBeniPTT4+X2YoNhXNFRrCwXp4j00+P"; // deskey
		//String desKeyBase64 = "PTT4+X2YoNhXNFRrCwXp4j00+P"; // deskey
		
		//读取base64编码后的key,解码成原始KEY
		byte desKeyArr[]=Base64.decodeBase64(desKeyBase64);		
		logger.info("deskey 密钥数组的长度是:"+desKeyArr.length);   //24长度
		
		String desKey=new String(desKeyArr,"UTF-8");		
		logger.info("des key is:"+ desKey);
		
		PublicKey ccbPublicKey =KeyFileUtil.loadRsaPublicKeyFromFile("f:/key/public_ccb.key");
		
		//测试自CCB服务器获取到的数据.
		String encryptStr="303030303030303132389de9e10f9ff798cbc5b740faf9ffc5955bb82046a1d0453398703d56b8a01673286f9a09484865fb76d6af9a80210fa706724e0159d3148c6f848c23605beb2844927505fd2d110858e883f04cc6db6cbcd365c9889ecd24bebb9efa674029ef69905009f0aa892d163cba87025dff11decb079a71c87422d5faa337686affdb5608f91889692b9b8d8315de81a4b09664b869dc61bb2c176e1c1061a9b76a1cf30e9f073502ca3cbd4f18947d42e870dc24771f68ed1b3e576cb1cdeef37d5529c153b9eefe350549b44f9e2bdc0246c68919d8107e9e198bb5df085cd6ebfc70f7eb9751910fb4a58320cbc21344123aaddde606d50be7f49d2c55d42c01e44ecf225411aaf3d6ad3fd192ebc359ad2302c1eeeee41fcc194ab10f8f5bc7d4416f9a68a341300795009be551df7184aadf2aec9902d58f92ef718eb5a1ff2ce46bcb1243fa8bc51cdc31159e5b5c1fcdff1db98a71a2c57eac2a600ed95c1fe31b38501ec0fcaf1b88e530831ae92d01779b6b78a5cac55450c24d04bf3fa1cdff1db98a71a2c59a4327687b4b7744ec0389a44f82f59cf49d2c55d42c01e45450c24d04bf3fa1ad3fd192ebc359ad7358bb60ce9730d3df95c86aad1d43a72af5da0071ae338f6e469e4c7cde0537efec662c71441ac2557fd420411e542586a587a57eb8f9cebd40581b0731bf3ad2fce45b7e3e6a3d106f82ace7d1c0835fd073e50f87541299a234b82685e9a3bd40581b0731bf3a6ebe256b717bc8f42a01e27b55aed43b12eac9d13f8493782af5da0071ae338fce185e32d545207c303b5e244e589ed314db6623eb0d50aaddb4c85e8d4b481b2a01e27b55aed43bbee6f9e49a3b48c812a487ddc6bc90e74372569796ec5b29162e17649a7a5aba677c4bed752119c4787771cb9349729e1c94cbd8f3dd453c5e725f4687633a9c1af88768a95981f0d71519e38283883612026d398a3df4293553e36da7ef6e25d9eb3edb23f4fca95f755a3d095e43018d8db3f81a18bd292b799bae1a2506a7325997303f1f73981ff0e8eabe65b4de7798d3ad3255f16a01deb04f8ae9cc203749d57f501718b558dc539bf7108f1bcdff1db98a71a2c5923bf2b1e516dd558f1556fe73018f3688ba90a1947317be9f933bda7c9e94a104206d5c40ade15cc8bbe2e5d332223c576cb1cdeef37d5529c153b9eefe350556991ef5dffb0262bbf3eb03fa906a8802ca8343dfe99f47350dd58b45c48776fbf555b0f75ca17f";
		byte[] packetArr=DesCBC.parseHexStr2Byte(encryptStr);		

		// 开始解密处理
		String plainText = decryptResponse(packetArr, desKeyBase64, ccbPublicKey);
//		String plainText="";

		logger.info("解密后的数据是:" + plainText);
	}
	
	
	/**
	 * (1)模拟数据生成:模拟CCB端生成测试数据 (2)测试LOCAL端解密函数
	 * 
	 * @throws Exception
	 */
	private static void test_decryptRequestResponse() throws Exception {

		String testStr = "汉字11111abcdefgABCDEFG!@#$%^&*()<>";
		
		String desKeyBase64 = "sLBeniPTT4+X2YoNhXNFRrCwXp4j00+P"; // deskey
		//String desKeyBase64 = "PTT4+X2YoNhXNFRrCwXp4j00+P"; // deskey
		
		//读取base64编码后的key,解码成原始KEY
		//byte desKeyArr[]=Base64.decodeBase64(desKeyBase64);		
		
		// testStr.getBytes()

		// DES密钥
		//byte[] desKey = "12345678".getBytes("UTF-8"); // deskey
		
		
		KeyPair ccbKeyPair = MD5withRSA.getKeyPair();
		KeyPair locKeyPair = MD5withRSA.getKeyPair();

		// CCB方
		PublicKey ccbPublicKey = MD5withRSA.getPublicKey(ccbKeyPair);
		PrivateKey ccbPrivateKey = MD5withRSA.getPrivateKey(ccbKeyPair);

		// LOC方
		PublicKey locPublicKey = MD5withRSA.getPublicKey(locKeyPair);
		PrivateKey locPrivateKey = MD5withRSA.getPrivateKey(locKeyPair);

		// (1)生成加密的测试数据(模拟CCB生成的加密报文)
		// (1.1)根据CCB Private key 生成签名BASE64编码.并计算签名的长度.
		byte[] signByte = CCBMD5withRSA.signMethod(testStr, ccbPrivateKey); // 生成签名
		//String signBase64Str = Base64.getEncoder().encodeToString(signByte);
		//String signBase64Str = Base64.encodeBase64String(signByte);
		String lenStr = String.format("%010d", signByte.length); // 签名的长度字符串
		
		// 签名的长度
		logger.info("签名的长度是:" + lenStr);
		// (1.2)根据desKey采用 根据CCB的DES密钥对明文进行加密处理.
		String encryptBase64Str = Des3Util.encryptUseDES3(testStr, desKeyBase64,"UTF-8");
		logger.debug("加密后结果是:"+encryptBase64Str);
		

		// (2)对上面的数据进行组装 置于一个byte[]中
		// 组装的顺序如下: 签名的长度(10字节,未加密)+数字签名+密文
		List<Byte> packetList = new ArrayList<Byte>();

		// (2.1)
		byte[] signLenArr = lenStr.getBytes("UTF-8");
		for (int i = 0; i < signLenArr.length; i++) {
			packetList.add(signLenArr[i]);
		}

		// (2.2)
		//byte[] signArr = new byte[signByte.length];
		for (int i = 0; i < signByte.length; i++) {
			packetList.add(signByte[i]);
		}

		// (2.3)
		//byte[] encryptMsgArr = encryptMsgBase64Str.getBytes("UTF-8");
//		for (int i = 0; i < encryptMsgBase64Arr.length; i++) {
//			packetList.add(encryptMsgBase64Arr[i]);
//		}
//
//		byte[] packetArr = new byte[packetList.size()];
//		for (int i = 0; i < packetList.size(); i++) {
//			packetArr[i] = packetList.get(i);
//		}
//
//		// 开始解密处理
//		String plainText = decryptResponse(packetArr, desKeyArr, ccbPublicKey);
//
//		logger.info("解密后的数据是:" + plainText);
	}

	

	/**
	 * 字符串分隔测试
	 */
	private static void test() {
		final String SPLIT_STR = "\\|";
		String strx = "SUCCESS|||20140723/440106198595270330.DAT";
		// 以上字符分隔后生成4个字符串 "SUCCESS" "" "" "20140723/440106198595270330.DAT"
		String[] fields = strx.split(SPLIT_STR);
		System.out.println("arr length is:" + fields.length);
		System.out.println("filename:" + fields[3]);
	}

	public static void main(String[] args) throws Exception {
		// test();
		
		
		//test_decryptRequestResponse1();
		
		
		test_decryptRequestResponse();
	}

}
