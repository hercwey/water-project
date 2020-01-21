package com.learnbind.ai.controller.ccb;

import java.io.IOException;
import java.io.OutputStream;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnbind.ai.ccb.keyfile.KeyFileUtil;
import com.learnbind.ai.ccb.sdk.ByteToHexStringUtil;
import com.learnbind.ai.ccb.sdk.Des3Util;
import com.learnbind.ai.common.DateUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.InterfaceConstant;
import com.learnbind.ai.model.SysInterfaceConfig;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;

/**
 * 
 * 功能: (1)营业收费系统对外接口 (2)建设银行调用.用于获取客户端RSA公钥.
 * 
 * @author lenovo
 *
 */
//@WebServlet(urlPatterns = { "/keydownload" }, description = "建行下载公钥数据")
@RestController
public class CcbGetNeuqPubKeyController {

	private static final Logger logger = LoggerFactory.getLogger(CcbGetNeuqPubKeyController.class);	

	@Autowired
	InterfaceConfigService interfaceConfigService;
	@Autowired
	private UploadFileConfig uploadFileConfig;   //路径配置服务

	@RequestMapping(value = "/api/keydownload")
	public void keyTransPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("----dopost keydownload ----");
		String type = request.getParameter("type");

		logger.info("--request.getParameter(" + type + ")--");
		if (type.equalsIgnoreCase("pub")) {
			OutputStream outputStream = response.getOutputStream();
			// byte[] header = new byte[6];

			/** 将文件中公钥对象读出 */
			PublicKey publicKey = null;
			
			logger.info("--ObjectInputStream Read key Start--");
			// 自文件读取客户端RSA公钥
			String keyFilePath = getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PUBLIC_KEY_FILE_PATH);
			logger.info("---------key file Path:" + keyFilePath + "-----------");			
			String uploadFolder=uploadFileConfig.getUploadFolder();
			String fullKeyFilePath=uploadFolder+keyFilePath;
			logger.info("---------full key file Path:" + fullKeyFilePath + "-----------");
			
			publicKey=KeyFileUtil.loadRsaPublicKeyFromFile(fullKeyFilePath);
			if(publicKey==null) {
				logger.info("-- ClassNotFoundException --");				
				outputStream.write("100001".getBytes("UTF-8"));// 往流中写数据
				outputStream.write("ClassNotFound From MX".getBytes("UTF-8"));
				outputStream.flush();// 刷空输出流
				outputStream.close();// 关闭流
			}
			else {
				logger.info("--Read key Ok Read To Encript--");
				// 电子银行合约编号
				String chan_cust_no = getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO); //
				// String customerKey="1234567890";
				logger.info("--Read neuqpay_ccb_customkey :" + chan_cust_no + "--");
				String commonKey = genCommonKey(chan_cust_no);
				logger.info("--DES common key is :" + commonKey + "--");

				outputStream.write("000000".getBytes("UTF-8"));

				logger.info("--Write Res Header :000000.getBytes(UTF-8)--");
				logger.info(ByteToHexStringUtil.bytesToHexString(publicKey.getEncoded()));

				// 对客户的RSA公钥采用 双方约定的密钥进行加密处理.
				byte[] bytesEn = Des3Util.keyEncrypt(publicKey.getEncoded(), commonKey);
				outputStream.write(bytesEn);

				logger.info("--Write Encript Data :OK--");
				
				logger.info("-------------Show Encript Data Begin-----------------");
				logger.info(ByteToHexStringUtil.bytesToHexString(bytesEn));
				logger.info("-------------Show Encript Data End-----------------");

				logger.info("--Test Decript Data :Bengin--");
				byte[] bytsDe = Des3Util.keyDecrypt(bytesEn, commonKey);
				logger.info(ByteToHexStringUtil.bytesToHexString(bytsDe));
				logger.info("--Test Decript Data :Ok--");

				outputStream.flush();
				outputStream.close();
				logger.info("--Stream Close :OK--");				
			}
			
		}
	}

	/**
	 * 自CCB中获取配置参数
	 * 
	 * @param parmKey
	 * @return
	 */
	//
	private String getCCBConfigParm(String parmKey) {
		String parmValue = "";
		SysInterfaceConfig config = new SysInterfaceConfig();
		config.setInterfaceType(InterfaceConstant.INTERFACE_TYPE_CCB);
		config.setKey(parmKey);
		SysInterfaceConfig result = interfaceConfigService.selectOne(config);
		if (result != null) {
			parmValue = result.getValue();
		}
		return parmValue;
	}

	/**
	 * 用于实际项目中. 
	 * 测试状态:已经测试 生成通用KEY,用于交换密钥时使用
	 * 
	 * @param customerNo 客户号
	 * @return
	 */
	private String genCommonKey(String customerNo) {
		String firstPart = "";
		if (customerNo.length() >= 10) {
			firstPart = customerNo.substring(customerNo.length() - 10);
		} else {
			firstPart = String.format("%10s", customerNo);
			firstPart = firstPart.replace(" ", "0");
		}
		String dateStr = DateUtil.format(new Date(), "yyMMdd");
		return firstPart + dateStr;
	}

	public static void main2(String[] args) {
		String dateKey = new SimpleDateFormat("YYMMdd").format(new Date());
		System.out.println(dateKey);
	}
}
