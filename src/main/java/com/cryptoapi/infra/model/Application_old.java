package com.cryptoapi.infra.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_old")
public class Application_old {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "appname")
	private String appName;

	@Column(name = "apitoken")
	private String apitoken;
	
	public Application_old() {

	  }
	
	public Application_old(String token, String appName) {
		    this.apitoken = token;
		    this.appName = appName;
		  }
	public void setToken(String token) {
	    this.apitoken = token;
	  }
	  
	public String getToken() {
	    return apitoken;
	  }
	public void setappName(String appName) {
	    this.appName = appName;
	  }
	  
	public String getappName() {
	    return appName;
	  }
}

