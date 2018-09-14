package com.example.activities.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.activities.entity.OpeningDates;

//Avoid auto-exposure of all the repository data through spring-mvc using hypermedia
@RepositoryRestResource(exported=false) 
public interface OpeningDatesRepository extends JpaRepository<OpeningDates,Long> {
//, QuerydslPredicateExecutor<OpeningHoursDetail> {
	
	/**
	 * future support for complex queries
	 */
	//boolean existsByName(String name);
	
}
