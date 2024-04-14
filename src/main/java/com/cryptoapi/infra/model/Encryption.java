package com.cryptoapi.infra.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class Encryption {
	
	private static String static_passphrase;

	@Value("${encryption.SALT}")
	private String static_salt;
	
	public void setSalt (String salt) {
		static_salt = salt;
	}

	public String getSalt() {
	    return static_salt;
	}
	
	@Value("${passphrase}")
	public void setPassphrase (String passphrase) {
		static_passphrase = passphrase;
	}

	public static String getPassphrase() {
	    return static_passphrase;
	}
}
