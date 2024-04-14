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

import org.springframework.beans.factory.annotation.Value;

import com.cryptoapi.infra.model.Encryption;

public class Crypto {


	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
		  
		  /*
		  SecretKey key = AesUtil_old.generateKey("password", "asdasd");
		  String input = "asdasdasd";
		  IvParameterSpec ivParameterSpec = AesUtil_old.generateIv();
		  String algorithm = "AES/CBC/PKCS5Padding";
		  
		  String ciphertxt = AesUtil_old.encrypt(algorithm,input, key, ivParameterSpec);
				  System.out.println(ciphertxt);
		  
		  
		  System.out.print(AesUtil_old.decrypt(algorithm,input, key, ivParameterSpec));
	  }
	  */
		
//		  String input = "baeldung";
//		    SecretKey key = AESUtil.generateKey(128);
//		    IvParameterSpec ivParameterSpec = AESUtil.generateIv();
//		    String algorithm = "AES/CBC/PKCS5Padding";
//		    String cipherText = AESUtil.encrypt(algorithm, input);
//		   
//		    
//		    String plainText = AESUtil.decrypt(algorithm, cipherText, key, ivParameterSpec);  
//		    System.out.println(cipherText);
//		    System.out.println(plainText);
	}
}
