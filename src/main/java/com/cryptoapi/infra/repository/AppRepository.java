package com.cryptoapi.infra.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cryptoapi.infra.model.Application;

public interface AppRepository extends JpaRepository<Application, Long> {
	List<Application> findByapikeyname(String apikeyname);
	List<Application> findByapikeynameContaining(String ApiKeyName);
	Boolean existsByapikeyname(String ApiKeyName);
}
