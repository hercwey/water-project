package com.learnbind.ai.config;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.learnbind.ai.common.util.RsaUtil;
import com.learnbind.ai.constant.SessionConstant;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.config
 *
 * @Title: CustomWebAuthenticationDetails.java
 * @Description: 前端请求多个参数时，可在处获取参数并进行校验
 *
 * @author Administrator
 * @date 2019年1月28日 下午8:39:08
 * @version V1.0 
 *
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6975601077710753878L;
	
	private final Log log = LogFactory.getLog(getClass());//日志
	
	private String username;
	private String password;
	

	public CustomWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		//this.username = request.getParameter("username");
		//this.password = request.getParameter("password");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		this.decryptData(request, username, password);//对用户名和密码解密
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "CustomWebAuthenticationDetails [username=" + username + ", password=" + password + "]";
	}

	/**
	 * @Title: decryptData
	 * @Description: 对用户名和密码解密
	 * @param request
	 * @param username
	 * @param password 
	 */
	private void decryptData(HttpServletRequest request, String username, String password) {
		// 自session读取公钥与私钥
		HttpSession session = request.getSession();
		List<String> keyList = (List<String>)session.getAttribute(SessionConstant.KEY_LIST);
		log.debug("keyList:"+keyList);
		
		String strpk = ((List<String>) session.getAttribute(SessionConstant.KEY_LIST)).get(RsaUtil.PUBLIC_KEY_INDEX); // 公钥
		String strprivk = ((List<String>) session.getAttribute(SessionConstant.KEY_LIST))
				.get(RsaUtil.PRIVATE_KEY_INDEX); // 私钥

		X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decodeBase64(strpk.getBytes()));
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(strprivk.getBytes()));

		KeyFactory keyf;
		String decryptUsername = "";
		String decryptPassword = "";
		try {
			keyf = KeyFactory.getInstance("RSA", "BC");
			PublicKey pubKey = keyf.generatePublic(pubX509);
			PrivateKey privKey = keyf.generatePrivate(priPKCS8);
			// 对接收的数据使用私钥进行解密处理
			this.username = RsaUtil.decryptData(username, privKey);
			this.password = RsaUtil.decryptData(password, privKey);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
