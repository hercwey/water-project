package com.learnbind.ai.config;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return DigestUtils.md5Hex(rawPassword.toString()).toUpperCase();
		//return new BCryptPasswordEncoder().encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		String encoderPassword = DigestUtils.md5Hex(rawPassword.toString()).toUpperCase();
		boolean flag = false;
		if(encoderPassword.equalsIgnoreCase(encodedPassword)) {
			flag = true;
		}
		return flag;
		//return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
	}

}
