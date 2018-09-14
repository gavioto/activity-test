package com.example.activities.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.activities.entity.ActivityType;

//Avoid auto-exposure of all the repository data through spring-mvc using hypermedia
@RepositoryRestResource(exported=false) 
public interface ActivityTypeRepository extends JpaRepository<ActivityType,Long>{

	boolean existsByNameIgnoreCase(String name);
	
	ActivityType findOneByNameIgnoreCase(String name);
	
}
