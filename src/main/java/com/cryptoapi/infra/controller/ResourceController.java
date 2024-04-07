package com.cryptoapi.infra.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cryptoapi.infra.model.Application;
import com.cryptoapi.infra.repository.AppRepository;

//@RequestMapping("/api")
@RestController
class ResourceController {
	@Autowired
	AppRepository appRepository;

	@GetMapping("api/{appname}")
	
	public ResponseEntity<List<Application>> getAllApplications(@PathVariable String appname) {
		System.out.println("getAllApplications");
		
		try {
			List<Application> applications = new ArrayList<Application>();

			if (appname == null)
				appRepository.findAll().forEach(applications::add);
			else
				appRepository.findByAppNameContaining(appname).forEach(applications::add);

			if (applications.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}

			return new ResponseEntity<>(applications, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
    @GetMapping("/home")
    public String homeEndpoint() {
        return "Baeldung !";
    }
    /*
    @GetMapping("/api")
    public String apiEndpoint() {
    	Application app = new Application();
		List<Application> applications = new ArrayList<Application>();
		applications = app.findAll();
		System.out.println(applications.toString());
        return "API endpoint !";
    }
    */
 
}

