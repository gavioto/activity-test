package com.example.activities.utils;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import com.example.activities.entity.Activity;
import com.example.activities.entity.City;
import com.example.activities.entity.OpeningDates;
import com.example.activities.entity.OpeningHoursDetail;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class ActivitiesUtils {

	/**
	 * TODO validation could be done in future versions. Or delegate for the data
	 * layer (spring-data) For Example: - Repeated activities - Incorrect field
	 * values - Missing fields
	 * 
	 * 
	 * @param file
	 * @return a list of Activities with attached City "Dummy"
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	static public List<Activity> ReadActivitiesFromFile(String file, ObjectMapper mapper)
			throws JsonParseException, JsonMappingException, IOException {

		InputStream fileStream = new ClassPathResource(file).getInputStream();

		// Deserializer configured using JsonDeserializer annotation, so it is not
		// necessary to register the new module manually
		List<Activity> activities = mapper.readValue(fileStream, new TypeReference<List<Activity>>() {
		});

		return activities;
	}

	/**
	 * This method uses a 3th party library for returning the GeoJSON It uses a
	 * GeoJSON POJO that later Spring-MVC converts into JSON.
	 * 
	 * Of course, this POJO library can be changed for other. This is one I have
	 * found on github.
	 * 
	 * https://github.com/opendatalab-de/geojson-jackson
	 * 
	 * Here you have an explanation from the author
	 * 
	 * http://blog.opendatalab.de/hack/2013/07/16/geojson-jackson
	 */
	public static FeatureCollection convertToJson(Iterable<Activity> result) {

		FeatureCollection geoJson = new FeatureCollection();

		for (Activity item : result) {
			Feature feature = new Feature();

			feature.setId(item.getName());

			feature.setProperty("district", item.getDistrict().getName());
			feature.setProperty("location", item.getLocation().getName());
			feature.setProperty("category", item.getCategory().getName());
			feature.setProperty("spent_hours", item.getHoursSpent());

			OpeningDates dates = item.getOpeningDates().stream()
					.filter(t -> t.getValidFrom().isBefore(ZonedDateTime.now()) && t.getValidTo().isAfter(ZonedDateTime.now() ))
					.sorted((t1, t2) -> t1.getValidFrom().compareTo(t2.getValidFrom()))
					.findFirst().orElse(null);

			Map<DayOfWeek, List<OpeningHoursDetail>> timeTableByDay = dates.getOpeningHoursDetail()
					.stream()
					.collect(Collectors.groupingBy(OpeningHoursDetail::getPeriod));

			//TODO transform to the JSON expected model 
//			Map<String,List<String>> timeTable = timeTableByDay
//	        .entrySet()
//	        .stream()
//	        .sorted((a,b) -> a.getKey().compareTo(b.getKey()))
//	        .red(Collectors.toMap(
//	        		k -> k.toString().toLowerCase().subSequence(0, 1),
//	        		x -> new ArrayList<String>(.getValue),
//	        		(old,latest) -> old.addAll(latest)
//	        		)
//        		);

			// TODO timetable using a custom Jackson Serializer
			feature.setProperty("opening_hours", timeTableByDay);

			com.vividsolutions.jts.geom.Point point = (com.vividsolutions.jts.geom.Point) item.getGeom();

			feature.setGeometry(new Point(point.getX(), point.getY()));

			geoJson.add(feature);
		}
		return geoJson;
	}

}
