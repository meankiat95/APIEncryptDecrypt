package com.cryptoapi.infra.controller;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cryptoapi.backend.AESUtil;
import com.cryptoapi.infra.model.Application;
import com.cryptoapi.infra.repository.AppRepository;
import com.cryptoapi.infra.service.AuthenticationService;

@RestController
@RequestMapping("/api")
class ResourceController {
	@Autowired
	AppRepository appRepository;
	AuthenticationService as = new AuthenticationService();
	@Value("${encryption.key}")
	private String encryption_key_str;

	// Declared iv object as global - IV need not be secret.
	IvParameterSpec ivParameterSpec = AESUtil.generateIv();

	@PostMapping("/encrypt")
	public ResponseEntity<?> encrypt(@RequestBody String requestBodyStr, @RequestHeader HttpHeaders headers)
			throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
		// Convert request body to JsonObj
		JSONObject jsonObject = new JSONObject(requestBodyStr.replaceAll("[\\[\\]\\(\\)]", ""));

		// Store all value into Application Object
		Application req_app = new Application();
		try {
		req_app.setAppname(jsonObject.getString("appname"));
		req_app.setApikeyname(jsonObject.getString("apikeyname"));
		} catch (JSONException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		
		// Get API Token from request header
		req_app.setAPIToken(headers.get("X-API-KEY").toString().replaceAll("[\\[\\]\\(\\)]", ""));
		// Get plain text value from request body
		String req_plaintext = jsonObject.getString("plaintext");
		System.out.println("req_app.getApikeyname()" + req_app.getApikeyname());
		
		// Query application details from DB
		List<Application> db_queried_applications = new ArrayList<Application>();
		appRepository.findByapikeyname(req_app.getApikeyname()).forEach(db_queried_applications::add);
		
		// Validation - Check if app details can be queried from DB
		if (db_queried_applications.isEmpty()) {
			return new ResponseEntity<>("Invalid APIKEYNAME.", HttpStatus.UNAUTHORIZED);
		}
		
		// Encryption
		SecretKey key = AESUtil.convertStringToSecretKeyto(encryption_key_str);
		String encrypted_text = AESUtil.encrypt("AES/CBC/PKCS5Padding", req_plaintext, key, ivParameterSpec);

		try {
		Authentication authentication = AuthenticationService.tokenAuthenticated(db_queried_applications, req_app);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		catch (Exception exp) {       
            return new ResponseEntity<>(exp.getMessage(), HttpStatus.UNAUTHORIZED);
        }

		return new ResponseEntity<>("Encrypted string : " + encrypted_text, HttpStatus.OK);
	}

	@PostMapping("/decrypt")
	public ResponseEntity<?> decrypt(@RequestBody String requestBodyStr, @RequestHeader HttpHeaders headers)
			throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
		// Convert request body to JsonObj
		JSONObject jsonObject = new JSONObject(requestBodyStr.replaceAll("[\\[\\]\\(\\)]", ""));
		// Store all value into Application Object
		Application req_app = new Application();
		try {
		req_app.setAppname(jsonObject.getString("appname"));
		req_app.setApikeyname(jsonObject.getString("apikeyname"));
		} catch (JSONException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		
		if (jsonObject.getString("apikeyname").isEmpty()){
			
		}
		
		// Get API Token from request header
		req_app.setAPIToken(headers.get("X-API-KEY").toString().replaceAll("[\\[\\]\\(\\)]", ""));
		// Get plain text value from request body
		String req_ciphertext = jsonObject.getString("ciphertext");

		// Query application details from DB
		List<Application> db_queried_applications = new ArrayList<Application>();
		appRepository.findByapikeynameContaining(req_app.getApikeyname()).forEach(db_queried_applications::add);

		// Validation - Check if app details can be queried from DB
		if (db_queried_applications.isEmpty()) {
			return new ResponseEntity<>("Invalid APIKEYNAME.", HttpStatus.UNAUTHORIZED);
		}
		
		// Decryption
		SecretKey key = AESUtil.convertStringToSecretKeyto(encryption_key_str);
		String decrypted_text = AESUtil.decrypt("AES/CBC/PKCS5Padding", req_ciphertext, key, ivParameterSpec);
		try {
		Authentication authentication = AuthenticationService.tokenAuthenticated(db_queried_applications, req_app);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		catch (Exception exp) {       
            return new ResponseEntity<>(exp.getMessage(), HttpStatus.UNAUTHORIZED);
        }
		return new ResponseEntity<>("Decrypted string : " + decrypted_text, HttpStatus.OK);
	}
}
