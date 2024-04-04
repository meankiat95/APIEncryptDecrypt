package com.cryptoapi.backend;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.cryptoapi.backend.AesUtil;

public class Crypto {
	static void myMethod() {
	    System.out.println("Hello World!");
	  }
		
	  public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
		  SecretKey key = AesUtil.generateKey("password", "asdasd");
		  String input = "asdasdasd";
		  IvParameterSpec ivParameterSpec = AesUtil.generateIv();
		  String algorithm = "AES/CBC/PKCS5Padding";
		  System.out.print(AesUtil.encrypt(algorithm,input, key, ivParameterSpec));
		  
	  }
}
