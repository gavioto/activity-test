package com.example.activities;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.SpringHandlerInstantiator;

import com.example.activities.entity.Activity;
import com.example.activities.entity.ActivityType;
import com.example.activities.entity.Category;
import com.example.activities.entity.City;
import com.example.activities.entity.District;
import com.example.activities.entity.OpeningDates;
import com.example.activities.entity.OpeningHoursDetail;
import com.example.activities.respository.ActivityRepository;
import com.example.activities.respository.CityRepository;
import com.example.activities.respository.DistrictRepository;
import com.example.activities.respository.OpeningHoursDetailRepository;
import com.example.activities.utils.ActivitiesUtils;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

@SpringBootApplication
public class ActivitiesApplication {

	private static final String DEFAULT_CITY_JSON = "madrid.json";

	public static void main(String[] args) {

		SpringApplication.run(ActivitiesApplication.class, args);
	}

	// Add initialization inserting the city.json in folder /data directly into the
	// repository
	// The same code can be used to create an import functionality in the REST API
	// or by command line
	@Transactional
	@Bean
	CommandLineRunner initActivities(ActivityRepository repoActivity, DistrictRepository repoDistrict,
			CityRepository repoCity, Jackson2ObjectMapperBuilder mapperBuilder) {
		return (String... strings) -> {

			// GOOD PRACTICE -> use orElseGet when possible to performance loss
			// https://www.baeldung.com/java-optional-or-else-vs-or-else-get
			City city = Optional.ofNullable(repoCity.findOneByNameIgnoreCase("madrid")).orElseGet(() -> {
				return repoCity.save(new City("madrid"));
			});

			List<Activity> fileList = ActivitiesUtils.ReadActivitiesFromFile(DEFAULT_CITY_JSON, mapperBuilder.build());

			// WORKAROUND JPA/HIBERNATE/SPRING-DATA detach/attach,
			// COMMENT due to the lack of city information in the original file, this
			// information should be linked in the entity by the application layer
			// TODO TECH-DEBT Less performance when loading objects
			fileList.stream().forEach(a -> {
				a.getDistrict().setCity(city);
				repoDistrict.save(a.getDistrict());
			});

			// ISSUE with JPA attach/detach
			repoActivity.saveAll(fileList);

//			populateDatabase(repo);

		};
	}

	/**
	 * This is a test TODO use JUnit for the tests
	 * 
	 * @param repo
	 */
	private void populateDatabase(OpeningHoursDetailRepository repo) {
		GeometryFactory factory = new GeometryFactory();

		ActivityType location = new ActivityType("test");
		City city = new City("madrid");
		Category category = new Category("category");
		District district = new District("Barrio letras", city);

		Activity theEvent = new Activity("Example event", category, district, location);

		Point point = factory.createPoint(new Coordinate(26, 62));
		point.setSRID(4326);
		theEvent.setGeom(point);
		theEvent.setHoursSpent(2);

		OpeningDates timetable = new OpeningDates(ZonedDateTime.now(), ZonedDateTime.now().plusDays(30));

		Set<OpeningDates> list = new HashSet<OpeningDates>();
		list.add(timetable);

		theEvent.setOpeningDates(list);

		OpeningHoursDetail detailTimes = new OpeningHoursDetail(DayOfWeek.MONDAY, LocalTime.parse("17:15:30"),
				LocalTime.parse("20:15:30"));

		repo.save(detailTimes);
	}

}
