package com.cryptoapi.infra.service;

import java.util.List;
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
		String db_appname = db_queried_applications.get(0).getAppname();
		System.out.println("db_apikey==========>"+db_apikey);
		
		// Validate if APIToken from request is equals to from DB.
		if (req_applications.getAPIToken().equals(db_apikey)) {
			// Validate if App name from request is equals to from DB
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