package com.learnbind.ai.controller.ccb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.learnbind.ai.ccb.CcbTestConstant;
import com.learnbind.ai.ccb.business.CCBBusiness;
import com.learnbind.ai.ccb.business.HeaderParams;
import com.learnbind.ai.ccb.business.KeyParams;
import com.learnbind.ai.ccb.keyfile.KeyFileUtil;
import com.learnbind.ai.ccb.md5.MD5withRSA;
import com.learnbind.ai.ccb.sdk.Des3Util;
import com.learnbind.ai.ccb.sdk.rsa.PemRSAEncrypt;
import com.learnbind.ai.common.DateUtil;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.InterfaceConstant;
import com.learnbind.ai.model.SysInterfaceConfig;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;
import com.learnbind.ai.util.HttpClientUtil;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.ccb
 *
 * @Title: CcbKeyExchangeController.java
 * @Description: 中国建设银行秘钥交换
 *
 * @author Administrator
 * @date 2019年8月25日 下午22:29:46
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/ccb-key-exchange")
public class CcbKeyExchangeController {
	
	private static final Logger logger = LoggerFactory.getLogger(CcbKeyExchangeController.class);
	
	private static final String TEMPLATE_PATH = "ccb_key_exchange/";//页面目录
	
	@Autowired
	InterfaceConfigService interfaceConfigService;//接口配置
	@Autowired
	private UploadFileConfig uploadFileConfig;   //路径配置服务
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "key_exchange_starter";
	}
	
	/**
	 * @Title: generatorRsaKeyPairToFile
	 * @Description: 生成本地RSA密钥对
	 * @return 
	 */
	@RequestMapping(value = "/create-local-rsa", produces = "application/json")
	@ResponseBody
	public Object generatorRsaKeyPairToFile() {
		
		try {
			String uploadFolder = uploadFileConfig.getUploadFolder();
			String publicKeyFilePath = uploadFolder+this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PUBLIC_KEY_FILE_PATH);
			String privateKeyFilePath = uploadFolder+this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PRIVATE_KEY_FILE_PATH);
			
			logger.info("RSA公钥路径："+publicKeyFilePath);
			logger.info("RSA私钥路径："+privateKeyFilePath);
			
			this.generatorRsaKeyPairToFile(publicKeyFilePath, privateKeyFilePath);
			
			return RequestResultUtil.getResultSuccess("本地RSA密钥对已生成！");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("本地RSA密钥对生成异常，请检查日志！");
	}
	
	/**
	 * @Title: getCcbDesKeyToSave
	 * @Description: 自CCB获取DES并保存
	 * @return 
	 */
	@RequestMapping(value = "/get-ccb-des-to-save", produces = "application/json")
	@ResponseBody
	public Object getCcbDesKeyToSave() {
		
		try {
			String respMsg = this.getCcbDesKeyTransPost();
			return RequestResultUtil.getResultSuccess(respMsg);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("自CCB获取DES KEY异常，请检查日志！");
	}
	
	/**
	 * @Title: getCcbRsaPublicKeyToSave
	 * @Description: 获取CCB RSA 公钥并保存
	 * @return 
	 */
	@RequestMapping(value = "/get-ccb-rsa-public-key-to-save", produces = "application/json")
	@ResponseBody
	public Object getCcbRsaPublicKeyToSave() {
		
		try {
			String respMsg = this.getRsaPubKeyTransPost();
			return RequestResultUtil.getResultSuccess(respMsg);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("自CCB获取DES KEY异常，请检查日志！");
	}
	
	
	/**
	 * @Title: login
	 * @Description: 签到
	 * @return 
	 */
	@RequestMapping(value = "/login", produces = "application/json")
	@ResponseBody
	public Object login() {
		
		try {
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);
			HeaderParams headerParams = this.getHearderParams();
			KeyParams keyParams = this.getKeyParams();
			
			CCBBusiness ccb = new CCBBusiness();
			boolean flag = ccb.login(bus_url, headerParams, keyParams);
			if(flag) {
				return RequestResultUtil.getResultSuccess("签到成功");
			}
			return RequestResultUtil.getResultSuccess("签到失败");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("签到异常，请检查日志！");
	}
	
	/**
	 * @Title: logout
	 * @Description: 签退
	 * @return 
	 */
	@RequestMapping(value = "/logout", produces = "application/json")
	@ResponseBody
	public Object logout() {
		
		try {
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);
			HeaderParams headerParams = this.getHearderParams();
			KeyParams keyParams = this.getKeyParams();
			
			CCBBusiness ccb = new CCBBusiness();
			boolean flag = ccb.logout(bus_url, headerParams, keyParams);
			if(flag) {
				return RequestResultUtil.getResultSuccess("签退成功");
			}
			return RequestResultUtil.getResultSuccess("签退失败");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("签退异常，请检查日志！");
	}
	
	/**
	 * @Title: getHearderParams
	 * @Description: 获取hearder参数 
	 * @return 
	 */
	private HeaderParams getHearderParams() {
		
		String chanl_cust_no = this.getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO);//电子银行合约编号
		String txn_itt_ip_adr = this.getCCBConfigParm(InterfaceConstant.CCB_LOCAL_HOST_IP);
		String txn_stff_id = CcbTestConstant.OPERATOR_NO;//交易员编号（操作员编号）
		
		HeaderParams params = new HeaderParams();
		params.setChanl_cust_no(chanl_cust_no);
		params.setTxn_itt_ip_adr(txn_itt_ip_adr);
		params.setTxn_stff_id(txn_stff_id);
		return params;
	}
	
	/**
	 * @Title: getKeyParams
	 * @Description: 获取 KEY 参数
	 * @return 
	 */
	private KeyParams getKeyParams() {
		
		String path = this.getPath();
		
		String desKey = this.getCCBConfigParm(InterfaceConstant.CCB_DES_KEY);
		String localPrivateKeyPath = this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PRIVATE_KEY_FILE_PATH);
		PrivateKey localPrivateKey = KeyFileUtil.loadRsaPrivateKeyFromFile(path+localPrivateKeyPath);
		String ccbPublicKeyPath = this.getCCBConfigParm(InterfaceConstant.CCB_RSA_PUBLIC_KEY_FILE_PATH_CCB);
		PublicKey ccbPublicKey = KeyFileUtil.loadRsaPublicKeyFromFile(path+ccbPublicKeyPath);
		
		KeyParams keyParams = new KeyParams();
		try {
			keyParams.setDesKey(desKey);
			logger.info("从数据库中获取到的DES KEY:"+desKey);
		} catch (UnsupportedEncodingException e) {
			logger.info("设置 DES KEY 异常，请查看日志！");
			e.printStackTrace();
		}
		keyParams.setLocalPrivateKey(localPrivateKey);
		keyParams.setCcbPublicKey(ccbPublicKey);
		return keyParams;
	}
	
	/**
	 * 自CCB获取RSA PUB KEY数据.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private String getRsaPubKeyTransPost()
			throws ServletException, IOException {
		// KeyFileUtil.test_savePublicKeyToFile();

		// 请求参数名称常量
		final String KEY_CHANL_CUST_NO = "chanl_cust_no";
		final String KEY_TYPE = "type";

		// 请求参数值常量
		// 密钥类型
		final String VALUE_TYPE_PUB = "pub"; // RSA公钥
		// final String VALUE_TYPE_DES="des"; //DES密钥

		final int NO_ENCRYPT_LEN = 6; // 未加密的数据长度
		final String RESPONSE_CODE_OK = "000000"; // 返回状态码-OK

		logger.info("----do post to bank get des key ----");

		// (1)自系统中获取配置参数
		// (1.1)获取配置参数:获取CCB DES密钥的url地址.
		String requestUrl = getCCBConfigParm(InterfaceConstant.CCB_KEY_URL);
		logger.info("--Read URL:" + requestUrl + "--");

		// (1.2)获取配置参数:客户的电子银行合约编号
		String chan_cust_no = getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO);
		logger.info("--Read chan_cust_no:" + chan_cust_no + "--");

		// (2)通过httpClient发送参数
		// (2.1)设置请求参数
		Map<String, String> parms = new HashMap<>();
		parms.put(KEY_CHANL_CUST_NO, chan_cust_no);
		parms.put(KEY_TYPE, VALUE_TYPE_PUB);

		// (2.1)发送请求
		logger.info("--send http post  request --");
		byte[] respBytes = HttpClientUtil.doPostRespBytes(requestUrl, parms);

		// 如果接收到数据
		if (respBytes != null) {

			// (3)开始对数据进行解析
			logger.info("--begin parse received data--");

			byte[] allData = respBytes;

			// 获取前面未加密部分
			byte[] codeByte = new byte[NO_ENCRYPT_LEN];
			System.arraycopy(allData, 0, codeByte, 0, NO_ENCRYPT_LEN);
			String code = new String(codeByte);

			// 获取后面加密部分
			byte[] dataByte = new byte[allData.length - NO_ENCRYPT_LEN];
			System.arraycopy(allData, NO_ENCRYPT_LEN, dataByte, 0, allData.length - NO_ENCRYPT_LEN);

			if (code.equalsIgnoreCase(RESPONSE_CODE_OK)) {
				logger.info("------Begin decrypt Data------");
				logger.info("--Read common key To decrypt--");
				// 获取配置:企业客户号
				//String customerNo = getCCBConfigParm(InterfaceConstant.CCB_COMPANY_CUSTOMER_NO);
				// String customerNo="1234567890";
				logger.info("--Read neuqpay_ccb_customkey :" + chan_cust_no + "--");
				String commonKey = genCommonKey(chan_cust_no);
				logger.info("--DES common key is :" + commonKey + "--");

				byte[] srcData = Des3Util.keyDecrypt(dataByte, commonKey); // 解决RSA PUBLIC KEY
				String keyBase64Str = Base64.encodeBase64String(srcData); // 对解密后的数据进行BASE64编码.

				if (StringUtils.isNotBlank(keyBase64Str)) {
					logger.info("--Begin Save Key Data in Base64 format--");
					logger.info("--Key Data Base64:" + keyBase64Str + "--");

					RSAPublicKey publicKey = null;

					// 根据RSA PUBLIC KEY Base64 String-----> RSA PUBLIC OBJECT对象
					try {
						publicKey = PemRSAEncrypt.getPublicKeyByBase64Str(keyBase64Str);
						logger.info(Base64.encodeBase64String(publicKey.getEncoded()));

					} catch (Exception e) {
						logger.info("Process Key Exception:", e);
					}

					// 保存CCB RSA PUBLIC KEY
					if (publicKey != null) {
						logger.info("--Save Key Data To File Start--");
						// 获取配置:自CCB获取的RSA公钥-密钥文件
						String keyFilePath = getCCBConfigParm(InterfaceConstant.CCB_RSA_PUBLIC_KEY_FILE_PATH_CCB);
						logger.info("---------key file Path:" + keyFilePath + "-----------");
						String uploadFolder = uploadFileConfig.getUploadFolder();
						String fullKeyFilePath = uploadFolder + keyFilePath;
						logger.info("---------full key file Path:" + fullKeyFilePath + "-----------");

						// save public object to file
						KeyFileUtil.savePublicKeyToFile(publicKey, fullKeyFilePath);

						logger.info("--Save rsa Key Data To File Ok--");
						return fullKeyFilePath;
					}else {
						return "RSA PUBLIC KEY IS NULL";
					}
				}else {
					return "对数据进行BASE64编码后为空";
				}
			}else {
				return "请求返回状态码："+code;
			}
			//response.getWriter().write("receive RSA key from ccb is Ok");
			//logger.info("receive RSA key from ccb is Ok");
		} else {
			logger.info("请求RSA密钥时未获取到数据");
			//response.getWriter().write("----no received data---");
			return "请求RSA密钥时未获取到数据";
		}
	}
	
	/**
	 * @Title: getCcbDesKeyTransPost
	 * @Description: 自建行获取DES密钥
	 * @return
	 * @throws ServletException
	 * @throws IOException 
	 */
	private String getCcbDesKeyTransPost() throws ServletException, IOException {
		//请求参数名称常量
		final String KEY_CHANL_CUST_NO="chanl_cust_no";
		final String KEY_TYPE="type";
		
		//请求参数值常量
		//密钥类型
		//final String VALUE_TYPE_PUB="pub";  //RSA公钥
		final String VALUE_TYPE_DES="des";  //DES密钥
		
		final int NO_ENCRYPT_LEN=6;  //未加密的数据长度
		final String RESPONSE_CODE_OK="000000";  //返回状态码-OK
		
		logger.info("----do post to bank get des key ----");
		
		//(1)自系统中获取配置参数
		//(1.1)获取配置参数:获取CCB DES密钥的url地址.
		String requestUrl = getCCBConfigParm(InterfaceConstant.CCB_KEY_URL);		
		logger.info("--Read URL:"+requestUrl+"--");
		
		//(1.2)获取配置参数:客户的电子银行合约编号
		String chan_cust_no = getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO);		
		logger.info("--Read chan_cust_no:"+chan_cust_no+"--");
		
		//(2)通过httpClient发送参数
		//(2.1)设置请求参数
		Map<String,String> parms=new HashMap<>();
		parms.put(KEY_CHANL_CUST_NO, chan_cust_no);
		parms.put(KEY_TYPE, VALUE_TYPE_DES);
		
		//(2.1)发送请求
		logger.info("--send http post  request --");
		byte[] respBytes= HttpClientUtil.doPostRespBytes(requestUrl, parms);
		
		//如果接收到数据
		if(respBytes!=null) {
		
			//(3)开始对数据进行解析
			logger.info("--begin parse received data--");
			
			byte[] allData = respBytes;
			
			//获取前面未加密部分
			byte[] codeByte = new byte[NO_ENCRYPT_LEN];
			System.arraycopy(allData, 0, codeByte, 0, NO_ENCRYPT_LEN);
			String code = new String(codeByte);
			
			//获取后面加密部分
			byte[] dataByte = new byte[allData.length-NO_ENCRYPT_LEN];
			System.arraycopy(allData, NO_ENCRYPT_LEN, dataByte, 0, allData.length-NO_ENCRYPT_LEN);

			
			if(code.equalsIgnoreCase(RESPONSE_CODE_OK)){
				logger.info("------Begin decrypt Data------");
				logger.info("--Read common key To decrypt--");
				//获取配置:企业客户号				
				//String customerNo = getCCBConfigParm(InterfaceConstant.CCB_COMPANY_CUSTOMER_NO); 
				// String customerNo="1234567890";
				logger.info("--Read neuqpay_ccb_customkey :" + chan_cust_no + "--");
				String commonKey = genCommonKey(chan_cust_no);
				logger.info("--DES common key is :" + commonKey + "--");				
				
				byte[] srcData = Des3Util.keyDecrypt(dataByte,commonKey);
				String desKeyBase64Str = Base64.encodeBase64String(srcData);
				
				if(StringUtils.isNotBlank(desKeyBase64Str)) {					
					logger.info("--Begin Save Key Data in Base64 format--");					
					saveCCBDesKey(desKeyBase64Str);					
					logger.info("--Key Data Base64:"+desKeyBase64Str+"--");	
					logger.info("--Save Key Data in Base64 format Ok--");
					return "DES KEY:"+desKeyBase64Str;
				}else {
					return "DES KEY IS NULL";
				}
			}else {
				return "请求返回状态码："+code;
			}
			//response.getWriter().write("receive des key Ok");
			//logger.info("receive des key Ok");
		}
		else {
			logger.info("请求DES密钥时未获取到数据");
			//response.getWriter().write("----no received data---");
			return "请求DES密钥时未获取到数据";
		}	
		
	}
	
	/**
	 * 保存CCB DES KEY到数据库
	 * @param desKey
	 */
	private int saveCCBDesKey(String desKey) {
		Example example=new Example(SysInterfaceConfig.class);
		example.createCriteria()
				.andEqualTo("key", InterfaceConstant.CCB_DES_KEY)
				.andEqualTo("interfaceType", InterfaceConstant.INTERFACE_TYPE_CCB);	
		
		SysInterfaceConfig config = new SysInterfaceConfig();		
		config.setValue(desKey);
		int rows=interfaceConfigService.updateByExampleSelective(config, example);
		return rows;
	}
	
	/**
	 * 自CCB中获取配置参数
	 * 
	 * @param parmKey
	 * @return
	 */
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
	 *	 用于实际项目中. 测试状态:已经测试 生成通用KEY,用于交换密钥时使用
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
	
	/**
	 * @Title: getPath
	 * @Description: 根据操作系统类型获取上传文件目录
	 * @return 
	 */
	private String getPath() {
		String path = uploadFileConfig.getUploadFolder();
		System.out.println("----------上传文件目录:"+path);
		return path;
	}
	
	/**
	 * @Title: generatorRsaKeyPairToFile
	 * @Description: 自动生成RSA钥匙对并保存到对应文件
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws IOException 
	 */
	private void generatorRsaKeyPairToFile(String publicKeyFilePath, String privateKeyFilePath) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		KeyPair keyPair = MD5withRSA.getKeyPair(); // 获取钥匙对
		KeyFileUtil.saveRsaKeyPairToFile(keyPair, publicKeyFilePath, privateKeyFilePath);
	}
	
	/**
	 * @Title: main
	 * @Description: main方法
	 * @param args 
	 */
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString().toUpperCase().replaceAll("-+", ""));
		String path = "d:\\upload\\file\\abc.txt";
		System.out.println(path);
		System.out.println(File.separator);
		System.out.println(path.substring(0, path.lastIndexOf(File.separator)+1));
		System.out.println(path.substring(path.lastIndexOf(File.separator)+1));
		String abc="sLBeniPTT4+X2YoNhXNFRrCwXp4j00+P";  
        try {
			System.out.println(new String(Base64.decodeBase64(abc), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
}
