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

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	//Declared iv object as global - IV need not be secret. 
	IvParameterSpec ivParameterSpec = AESUtil.generateIv();
	/*
	 * @GetMapping("/{appname}") public ResponseEntity<List<Application>>
	 * getAllApplications(@PathVariable String appname) {
	 * System.out.println("getAllApplications");
	 * 
	 * try { List<Application> applications = new ArrayList<Application>();
	 * 
	 * if (appname == null) appRepository.findAll().forEach(applications::add); else
	 * appRepository.findByapikeynameContaining("keyname1").forEach(applications::
	 * add);
	 * 
	 * if (applications.isEmpty()) { return new
	 * ResponseEntity<>(HttpStatus.UNAUTHORIZED); }
	 * 
	 * return new ResponseEntity<>(applications, HttpStatus.OK); } catch (Exception
	 * e) { return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); } }
	 */
	@PostMapping("/encrypt")
	public ResponseEntity<?> encrypt(@RequestBody String requestBodyStr, @RequestHeader HttpHeaders headers)
			throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
		// Convert request body to JsonObj
		JSONObject jsonObject = new JSONObject(requestBodyStr.replaceAll("[\\[\\]\\(\\)]", ""));

		// Store all value into Application Object
		Application req_app = new Application();
		req_app.setAppname(jsonObject.getString("appname"));
		req_app.setApikeyname(jsonObject.getString("apikeyname"));
		// Store API Token from request header
		req_app.setAPIToken(headers.get("X-API-KEY").toString().replaceAll("[\\[\\]\\(\\)]", ""));
		// Get plain text value from request body
		String req_plaintext = jsonObject.getString("plaintext");

		// Get from DB
		List<Application> db_queried_applications = new ArrayList<Application>();
		appRepository
		.findByapikeynameContaining(req_app.getApikeyname()).forEach(db_queried_applications::add);

		// Encryption

		//SecretKey key = AESUtil.generateKey(256);
		SecretKey key = AESUtil.convertStringToSecretKeyto(encryption_key_str);
		System.out.println("key==============>"+key.toString());
		//String secretkeystr = AESUtil.convertSecretKeyToString(key);
		//System.out.println(secretkeystr);
		String encrypted_text = AESUtil.encrypt("AES/CBC/PKCS5Padding", req_plaintext, key, ivParameterSpec);

		Authentication authentication = AuthenticationService.tokenAuthenticated(db_queried_applications, req_app);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		/*
		 * Authentication authentication = authenticationManager .authenticate(new
		 * UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
		 * loginRequest.getPassword()));
		 * 
		 * ApiKeyAuthentication(AUTH_TOKEN, AuthorityUtils.NO_AUTHORITIES);
		 */

		return new ResponseEntity<>(encrypted_text, HttpStatus.OK);
	}

	@PostMapping("/decrypt")
	public ResponseEntity<?> decrypt(@RequestBody String requestBodyStr, @RequestHeader HttpHeaders headers)
			throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
		// Convert request body to JsonObj
		JSONObject jsonObject = new JSONObject(requestBodyStr.replaceAll("[\\[\\]\\(\\)]", ""));
		// Store all value into Application Object
		Application req_app = new Application();
		req_app.setAppname(jsonObject.getString("appname"));
		req_app.setApikeyname(jsonObject.getString("apikeyname"));
		// Store API Token from request header
		req_app.setAPIToken(headers.get("X-API-KEY").toString().replaceAll("[\\[\\]\\(\\)]", ""));
		// Get plain text value from request body
		String req_ciphertext = jsonObject.getString("ciphertext");

		// Get from DB
		List<Application> db_queried_applications = new ArrayList<Application>();
		appRepository.findByapikeynameContaining(req_app.getApikeyname()).forEach(db_queried_applications::add);

		// Decryption
		System.out.println(encryption_key_str);
		SecretKey key = AESUtil.convertStringToSecretKeyto(encryption_key_str);
		System.out.println("key==============>"+key.toString());
		
		String decrypted_text = AESUtil.decrypt("AES/CBC/PKCS5Padding", req_ciphertext, key, ivParameterSpec);

		Authentication authentication = AuthenticationService.tokenAuthenticated(db_queried_applications, req_app);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return new ResponseEntity<>(decrypted_text, HttpStatus.OK);
	}

	@GetMapping("/home")
	public String homeEndpoint() {
		return "Baeldung !";
	}
	/*
	 * @GetMapping("/api") public String apiEndpoint() { Application app = new
	 * Application(); List<Application> applications = new ArrayList<Application>();
	 * applications = app.findAll(); System.out.println(applications.toString());
	 * return "API endpoint !"; }
	 */

}
