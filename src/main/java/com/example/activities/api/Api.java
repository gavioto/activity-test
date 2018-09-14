package com.example.activities.api;

import org.geojson.FeatureCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.activities.entity.Activity;
import com.example.activities.respository.ActivityRepository;
import com.example.activities.utils.ActivitiesUtils;

@RestController("/activity")
public class Api {

	@Autowired
	private ActivityRepository activiyRepository;

	/**
	 * @param name
	 * @return
	 */
	@GetMapping("/{city}")
	public FeatureCollection findAll(@PathVariable(required = true, name = "city") String city) {

		// check if category is null or empty
		if (StringUtils.isEmpty(city)) {
			throw new ResourceNotFoundException();
		}

		Iterable<Activity> result = activiyRepository.findByCityNameIgnoreCase(city);

		FeatureCollection geoJson = ActivitiesUtils.convertToJson(result);

		return geoJson;
	}

	@GetMapping("/{city}/category/{name}")
	public FeatureCollection findAllByCategory(
			@PathVariable(required = true, name = "city") String city,
			@PathVariable(required = false, name="name") String name) {

		// check if category is null or empty
		if (StringUtils.isEmpty(name)) {
			return this.findAll(city);
		}

		Iterable<Activity> result = activiyRepository.findByCategoryName(name);

		FeatureCollection geoJson = ActivitiesUtils.convertToJson(result);

		return geoJson;
	}

	@GetMapping("/{city}/district/{name}")
	public FeatureCollection findAllByDistrict(
			@PathVariable(required = true, name = "city") String city,
			@PathVariable(required = false, name="name") String name) {

		// check if category is null or empty
		if (StringUtils.isEmpty(name)) {
			return this.findAll(city);
		}

		Iterable<Activity> result = activiyRepository.findByDistrictName(name);

		FeatureCollection geoJson = ActivitiesUtils.convertToJson(result);

		return geoJson;
	}

	@GetMapping("/{city}/location/{name}")
	public FeatureCollection findAllByLocation(
			@PathVariable(required = true, name = "city") String city, 
			@PathVariable(required = false, name="name") String name) {

		// check if parameter is null or empty
		if (StringUtils.isEmpty(name)) {
			return this.findAll(city);
		}

		Iterable<Activity> result = activiyRepository.findByLocationName(name);

		FeatureCollection geoJson = ActivitiesUtils.convertToJson(result);

		return geoJson;
	}

}
