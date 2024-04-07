package com.cryptoapi.infra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cryptoapi.infra.model.Application;

public interface AppRepository extends JpaRepository<Application, Long> {

	List<Application> findByAppNameContaining(String appname);
}
