package com.learnbind.ai.ccb.business;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyParams {
	/**
	 * 自CCB获取到的DES KEY
	 */
	private String desKey;   
	private PrivateKey localPrivateKey;  //本地RSA private key
	private PublicKey ccbPublicKey;		//自CCB获取得到的 public key
	
	public String getDesKey() {
		return desKey;
	}
	/**
	 * @param desKey  自CCB获取到的DES KEY(BASE64编码)
	 * @throws UnsupportedEncodingException
	 */
	public void setDesKey(String desKey) throws UnsupportedEncodingException {		
		//byte[] decryptDesKeyArr=Base64.getDecoder().decode(desKey.getBytes("UTF-8"));		
		//this.desKey = new String(decryptDesKeyArr,"UTF-8");
		this.desKey = desKey;
	}
	public PrivateKey getLocalPrivateKey() {
		return localPrivateKey;
	}
	public void setLocalPrivateKey(PrivateKey localPrivateKey) {
		this.localPrivateKey = localPrivateKey;
	}
	public PublicKey getCcbPublicKey() {
		return ccbPublicKey;
	}
	public void setCcbPublicKey(PublicKey ccbPublicKey) {
		this.ccbPublicKey = ccbPublicKey;
	}
	
	@Override
	public String toString() {
		return "KeyParams [desKey=" + desKey + ", localPrivateKey=" + localPrivateKey + ", ccbPublicKey=" + ccbPublicKey
				+ "]";
	}
	
}
