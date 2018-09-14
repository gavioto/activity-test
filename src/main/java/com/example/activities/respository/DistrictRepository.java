package com.example.activities.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.activities.entity.City;
import com.example.activities.entity.District;

//Avoid auto-exposure of all the repository data through spring-mvc
@RepositoryRestResource(exported=false) 
public interface DistrictRepository extends JpaRepository<District,Long>{
//, QuerydslPredicateExecutor<Activity>{
	
	boolean existsByName(String name);
	
	District findOneByNameIgnoreCaseAndCity(String name, City city);
	
	List<District> findByCity(City city);
	
}
