package com.cryptoapi.infra.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "app",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "appname"),
           @UniqueConstraint(columnNames = "apitoken")
       })
public class Application {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  private String appname;

  @NotBlank
  @Size(max = 50)
  private String apitoken;

  @NotBlank
  @Size(max = 20)
  private String apikeyname;
  
  public Application() {
  }

  public Application(String appname, String apikeyname, String apitoken) {
    this.appname = appname;
    this.apikeyname = apikeyname;
    this.apitoken = apitoken;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAppname() {
    return appname;
  }

  public void setAppname(String appname) {
    this.appname = appname;
  }

  public String getApikeyname() {
	    return apikeyname;
	}

	public void setApikeyname(String apikeyname) {
		this.apikeyname = apikeyname;
	}

  
  public String getAPIToken() {
    return apitoken;
  }

  public void setAPIToken(String apitoken) {
    this.apitoken = apitoken;
  }
}
