package com.cryptoapi.infra.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import com.cryptoapi.infra.model.Application;
import com.cryptoapi.infra.repository.AppRepository;

import jakarta.servlet.http.HttpServletRequest;

public class AuthenticationService {
	@Autowired
	AppRepository appRepository;
	private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
	private static final String AUTH_TOKEN = "Baeldung";

	public static Authentication getAuthentication(HttpServletRequest request) {
		String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
		/*
		 * // Using Regex to match H2 DB address, to allow access to DB UI without API
		 * key Pattern p = Pattern.compile("\\/h2-ui/.*"); Matcher m =
		 * p.matcher(request.getRequestURI()); boolean pathMatch = m.matches();
		 * 
		 * // Bypass if URL is h2 db url if (pathMatch) { return new
		 * ApiKeyAuthentication(AUTH_TOKEN, AuthorityUtils.NO_AUTHORITIES); }
		 * 
		 * if (apiKey == null || !apiKey.equals(AUTH_TOKEN)) { throw new
		 * BadCredentialsException("Invalid API Key"); }
		 */
		return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);

	}

	public static ApiKeyAuthentication tokenAuthenticated(List<Application> db_queried_applications,
			Application req_applications) {

		// Value from DB
		String db_apikey = db_queried_applications.get(0).getAPIToken();
		String db_apikeyname = db_queried_applications.get(0).getApikeyname();
		String db_appname = db_queried_applications.get(0).getAppname();
		System.out.println("db_apikey ===========> " + db_apikey);
		System.out.println("db_apikeyname ===========> " + db_apikeyname);
		System.out.println("db_appname ===========> " + db_appname);

		if (req_applications.getAPIToken().equals(db_apikey)) {
			if (!req_applications.getAppname().equals(db_appname)) {
				System.out.println("NOT SAME");
				throw new BadCredentialsException("Invalid App Name");
			}
		} else {
			throw new BadCredentialsException("Invalid API Key");
		}

		return new ApiKeyAuthentication(db_apikey, AuthorityUtils.NO_AUTHORITIES);
	}
}