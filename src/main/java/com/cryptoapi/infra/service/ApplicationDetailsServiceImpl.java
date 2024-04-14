package com.cryptoapi.infra.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cryptoapi.infra.model.Application;
import com.cryptoapi.infra.repository.AppRepository;

public class ApplicationDetailsServiceImpl {
	@Autowired
	AppRepository appRepository;
	
	public List <Application> loadApplicationByApiKeyName(String apikeyname) throws UsernameNotFoundException {
		List<Application> app = new ArrayList<Application>();

		appRepository.findByapikeynameContaining(apikeyname).forEach(app::add);
		
		return app;
	  }
}
