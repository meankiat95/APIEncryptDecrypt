package com.cryptoapi.infra.model;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
	@NotBlank
	private String appname;
	private String apikeyname;
	private String apitoken;

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getApikeyName() {
		return apikeyname;
	}

	public void setApikeyName(String apikeyname) {
		this.apikeyname = apikeyname;
	}
	
	public String getApitoken() {
		return apitoken;
	}

	public void setApitoken(String apitoken) {
		this.apitoken = apitoken;
	}

}
