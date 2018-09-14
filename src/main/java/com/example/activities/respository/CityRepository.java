package com.example.activities.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.activities.entity.City;

/**
 * TODO
 * 
 * This repository will implement "find within" in the future for serching geolocated sites
 * 
 *  On the other hand, it is prepared to implement searches creating a predicate, for complex queries
 *
 */
//Avoid auto-exposure of all the repository data through spring-mvc using hypermedia
@RepositoryRestResource(exported=false) 
public interface CityRepository extends JpaRepository<City,Long>{

	boolean existsByNameIgnoreCase(String name);
	
	City findOneByNameIgnoreCase(String name);
	
}
