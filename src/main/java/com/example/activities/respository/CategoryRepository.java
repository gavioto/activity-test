package com.example.activities.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.activities.entity.Category;

//Avoid auto-exposure of all the repository data through spring-mvc using hypermedia
@RepositoryRestResource(exported=false) 
public interface CategoryRepository extends JpaRepository<Category,Long>{

	boolean existsByNameIgnoreCase(String name);
	
	Category findOneByNameIgnoreCase(String name);
	
}
