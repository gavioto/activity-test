package com.example.activities.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.activities.entity.OpeningHoursDetail;

//Avoid auto-exposure of all the repository data through spring-mvc using hypermedia
@RepositoryRestResource(exported=false) 
public interface OpeningHoursDetailRepository extends JpaRepository<OpeningHoursDetail,Long> {
//, QuerydslPredicateExecutor<OpeningHoursDetail> {
	
	//List<Activity> findActivityByOpeningHourAndCloseHour(ZonedDateTime opening, ZonedDateTime closing);

}
