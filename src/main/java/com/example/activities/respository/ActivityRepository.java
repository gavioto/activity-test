package com.example.activities.respository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.activities.entity.Activity;
import com.example.activities.entity.ActivityType;
import com.example.activities.entity.Category;
import com.example.activities.entity.City;
import com.example.activities.entity.District;
import com.vividsolutions.jts.geom.Polygon;

/**
 * TODO
 * 
 * This repository will implement "findWithin" in the future for searching using geo-information
 * 
 *  On the other hand, it is prepared to implement searches creating a predicate, for complex queries
 *  The power of the solution is in the ActivityRepository.java class -> it is easy to create several queries in the future, having a data abstraction layer....
 *
 */
//Avoid auto-exposure of all the repository data through spring-mvc using hypermedia
@RepositoryRestResource(exported=false) 
public interface ActivityRepository extends JpaRepository<Activity,Long>{
//, QuerydslPredicateExecutor<Activity>{
	
	boolean existsByName(String name);
	
	Activity findOneByName(String name);
	
	//TODO consider adding IgnoreCase query also
	List<Activity> findByCategory(Category name);
	List<Activity> findByCategoryName(String categoryName);
	
	List<Activity> findByDistrict(District district);
	List<Activity> findByDistrictName(String districtName);
	
	/**
	 * WARNING location is not coordinates, it is an outdoor/indoor classification
	 * @param activityType
	 * @return
	 */
	List<Activity> findByLocation(ActivityType name);
	List<Activity> findByLocationName(String locationName);

	/**
	 * Custom query using JQPL to make advanced queries (it is validated by spring-data / hibernate on application start
	 * @param city
	 * @return
	 */
	//@RestResource(exported=true,path="/madrid")
	@Query("SELECT a " + 
			"FROM Activity a " + 
			"JOIN FETCH a.district d " + 
			"JOIN FETCH d.city c " + 
			"WHERE c = :city")
	Iterable<Activity> findByCity(@Param("city") City city);

	/**
	 * Example custom query using string
	 * @param city
	 * @return
	 */
	//@RestResource(exported=true,path="/CityByName")
	@Query("SELECT a " + 
			"FROM Activity a " + 
			"JOIN FETCH a.district d " + 
			"JOIN FETCH d.city c " + 
			"WHERE c.name = :city")
	Iterable<Activity> findByCityName(@Param("city") String city);

	@Query("SELECT a " + 
			"FROM Activity a " + 
			"JOIN FETCH a.district d " + 
			"JOIN FETCH d.city c " + 
			"WHERE upper(c.name) = upper(:city)")
	Iterable<Activity> findByCityNameIgnoreCase(@Param("city") String city);

	
	/**
	 * Example custom query using string to retrieve recommendations
	 * 
	 * TODO Improve the code using query builder instead of JPQL query to bring the benefits of Strong Type during development
	 * 
	 * @param city
	 * @return
	 */
	//@RestResource(exported=true,path="/CityByName")
	@Query("SELECT a " + 
			"FROM Activity a " +
			"JOIN FETCH a.category c "+
			"JOIN FETCH a.openingDates d " + 
			"JOIN FETCH d.openingHoursDetail det " + 
			"WHERE det.period = :weekday AND "+
			"d.validFrom >= :dateVisit AND d.validTo <= :dateVisit AND "+
			"det.closeHour - a.hoursSpent >= :timeStart  AND "+
			"det.openingHour + a.hoursSpent <= :timeEnd  AND "+
			"c.name = coalesce(:category,c.name) "+
			"ORDER BY a.hoursSpent desc")
	Iterable<Activity> findByTimeRange(@Param("weekday") DayOfWeek weekday,
			@Param("dateVisit") ZonedDateTime dateVisit,
			@Param("timeStart") LocalTime timeStart,
			@Param("timeEnd") LocalTime timeEnd,
			@Param("category") String category);
	
	
	/**
	 * Given an area return all the activities in a geographic location (example: a city, a radius)
	 * @param predicate
	 * @return
	 */
	//List<Activity> findByDistrict(Predicate predicate);

//	https://github.com/vasily-kartashov/postgis-spring-data-jpa-example/blob/master/src/main/java/com/kartashov/postgis/repositories/DeviceRepository.java

	@Query("SELECT a FROM Activity AS a WHERE within(a.geom, :polygon) = TRUE")
    List<Activity> findWithin(@Param("polygon") Polygon polygon);
	
//    @Query("SELECT d FROM Device AS d WHERE CAST(extract(d.status, 'stateOfCharge') float) > 0.1")
//    List<Device> findHealthyDevices();
}
