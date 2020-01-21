package com.learnbind.ai.controller.ccb;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnbind.ai.ccb.sdk.Des3Util;
import com.learnbind.ai.common.DateUtil;
import com.learnbind.ai.constant.InterfaceConstant;
import com.learnbind.ai.model.SysInterfaceConfig;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;
import com.learnbind.ai.util.HttpClientUtil;

import tk.mybatis.mapper.entity.Example;
//import com.neuqsoft.pay.authorize.service.SystemParameterService;


/**
 * 客户自CCB获取DES密钥
 * @author lenovo
 */
//@WebServlet(urlPatterns = { "/ccbDesKeyDownAndSave" }, description = "下载建行对称密钥并保存")
@SuppressWarnings("deprecation")
@RestController
public class NeuqGetCcbDesKeyController {
	
	private static final Logger logger = LoggerFactory.getLogger(NeuqGetCcbDesKeyController.class);
	
	@Autowired
	InterfaceConfigService interfaceConfigService;
	
	@RequestMapping(value = "/api/ccbDesKeyDownAndSave")
	public void keyTransPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
				//String customerNo = getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO); 
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
				}
			}
			response.getWriter().write("receive des key Ok");
			logger.info("receive des key Ok");
		}
		else {
			logger.info("请求DES密钥时未获取到数据");
			response.getWriter().write("----no received data---");			
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
	 * TODO 用于实际项目中. 测试状态:已经测试 生成通用KEY,用于交换密钥时使用
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
		String dateStr = DateUtil.format(new Date(), "YYMMdd");
		return firstPart + dateStr;
	}
	
	
	public static void main(String[] args) {
		NeuqGetCcbDesKeyController test=new NeuqGetCcbDesKeyController();
		String key=test.genCommonKey("123");
		System.out.println("common key is:"+key);
	}


}
