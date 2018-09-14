package com.example.activities.utils;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import com.example.activities.entity.Activity;
import com.example.activities.entity.ActivityType;
import com.example.activities.entity.Category;
import com.example.activities.entity.City;
import com.example.activities.entity.District;
import com.example.activities.entity.OpeningDates;
import com.example.activities.entity.OpeningHoursDetail;
import com.example.activities.respository.ActivityRepository;
import com.example.activities.respository.ActivityTypeRepository;
import com.example.activities.respository.CategoryRepository;
import com.example.activities.respository.CityRepository;
import com.example.activities.respository.DistrictRepository;
import com.example.activities.respository.OpeningDatesRepository;
import com.example.activities.respository.OpeningHoursDetailRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

//@Component
public class CustomActivityDeserializer extends JsonDeserializer<Activity> {

	private static final String FIELD_OPENING_HOURS_SU = "su";

	private static final String FIELD_OPENING_HOURS_SA = "sa";

	private static final String FIELD_OPENING_HOURS_FR = "fr";

	private static final String FIELD_OPENING_HOURS_TH = "th";

	private static final String FIELD_OPENING_HOURS_WE = "we";

	private static final String FIELD_OPENING_HOURS_TU = "tu";

	private static final String FIELD_OPENING_HOURS_MO = "mo";

	private static final String FIELD_OPENING_HOURS_SEPARATOR = "-";

	private static final String FIELD_OPENING_HOURS = "opening_hours";

	private static final String DEFAULT_PERIOD_END = "2100-01-01T00:00:00+01:00[Europe/Madrid]";

	private static final String DEFAULT_PERIOD_START = "2000-01-01T00:00:00+01:00[Europe/Madrid]";

	private static final String FIELD_HOURS_SPENT = "hours_spent";

	private static final String FIELD_LATLON = "latlng";

	private static final String FIELD_NAME = "name";

	private static final String FIELD_LOCATION = "location";

	private static final String FIELD_DISTRICT = "district";

	private static final String FIELD_CATEGORY = "category";

	private static final String CITY_DUMMY = "dummy";

	/**
	 * How to autowire some bean into JsonSerializer?
	 * 
	 * https://stackoverflow.com/questions/17576098/how-to-autowire-some-bean-into-jsonserializer
	 */

	@Autowired
	ActivityRepository activityRespository;

	@Autowired
	ActivityTypeRepository activityTypeRepository;

	@Autowired
	DistrictRepository districtRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	OpeningDatesRepository datesRepository;

	@Autowired
	OpeningHoursDetailRepository timetableRepository;

	@Autowired
	CityRepository cityRepository;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomActivityDeserializer() {
		// this(null);
	}

	/**
	 * { "name": "El Rastro", "opening_hours": { "mo": [], "tu": [], "we": [], "th":
	 * [], "fr": [], "sa": [], "su": ["09:00-15:00"] }, "hours_spent": 2.5,
	 * "category": "shopping", "location": "outdoors", "district": "Centro",
	 * "latlng": [40.4087357,-3.7081466] }
	 */
	/**
	 * The city object should exists
	 * 
	 * TODO I'm aware that merging repositories in deserialization layer is not very
	 * orthodox, it sholuld be a
	 */
	@Override
	public Activity deserialize(JsonParser parser, DeserializationContext deserializer)
			throws IOException, JsonProcessingException {

		// SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

		GeometryFactory factory = new GeometryFactory();
		ObjectCodec codec = parser.getCodec();
		JsonNode node;
		Set<OpeningDates> periods = new HashSet<OpeningDates>();
		
		node = codec.readTree(parser);

		// TODO this is city entity WORKAROUND to avoid FK restriction when creating the
		// district.
		// TECH-DEBT
		City city = Optional.ofNullable(cityRepository.findOneByNameIgnoreCase(CITY_DUMMY)).orElseGet(() -> {
			return cityRepository.save(new City(CITY_DUMMY));
		});

		Category category = Optional
				.ofNullable(categoryRepository.findOneByNameIgnoreCase(node.get(FIELD_CATEGORY).asText()))
				.orElseGet(() -> {
					return categoryRepository.save(new Category(node.get(FIELD_CATEGORY).asText()));
				});

		District district = Optional
				.ofNullable(districtRepository.findOneByNameIgnoreCaseAndCity(node.get(FIELD_DISTRICT).asText(), city))
				.orElseGet(() -> {
					return districtRepository.save(new District(node.get(FIELD_DISTRICT).asText(), city));
				});

		ActivityType location = Optional
				.ofNullable(activityTypeRepository.findOneByNameIgnoreCase(node.get(FIELD_LOCATION).asText()))
				.orElseGet(() -> {
					return activityTypeRepository.save(new ActivityType(node.get(FIELD_LOCATION).asText()));
				});

		Activity activity = Optional.ofNullable(activityRespository.findOneByName(node.get(FIELD_NAME).asText()))
				.orElse(

						new Activity(node.get(FIELD_NAME).asText(), null, null, null)

				);

		// overwrite values
		activity.setCategory(category);
		activity.setDistrict(district);
		activity.setLocation(location);

		JsonNode array = node.get(FIELD_LATLON);

		Point point = factory.createPoint(new Coordinate(array.get(1).doubleValue(), array.get(0).doubleValue()));
		point.setSRID(4326);

		activity.setGeom(point);

		activity.setHoursSpent(node.get(FIELD_HOURS_SPENT).floatValue());

		// TODO at this moment the opening dates are not set because there is only one
		// period
		OpeningDates period = new OpeningDates(ZonedDateTime.parse(DEFAULT_PERIOD_START),
				ZonedDateTime.parse(DEFAULT_PERIOD_END));
//				datesRepository.save(new OpeningDates(activity, ZonedDateTime.parse(DEFAULT_PERIOD_START),
//				ZonedDateTime.parse(DEFAULT_PERIOD_END)));

		Set<OpeningHoursDetail> timetable = new HashSet<OpeningHoursDetail>();

		getTimeTable(node, period, timetable);

		period.setOpeningHoursDetail(timetable);
		
		//update instance including timetables
		//period = datesRepository.save(period);
		
		periods.add(period);

		activity.setOpeningDates(periods);

		return activity;
	}

	private void getTimeTable(JsonNode node, OpeningDates period, Set<OpeningHoursDetail> timetable)
			throws IOException {

		for (Iterator<Entry<String, JsonNode>> items = node.get(FIELD_OPENING_HOURS).fields(); items.hasNext();) {
			Entry<String, JsonNode> item = items.next();
			DayOfWeek weekDay = null;

			switch (item.getKey()) {
			case FIELD_OPENING_HOURS_MO:
				weekDay = DayOfWeek.MONDAY;
				break;
			case FIELD_OPENING_HOURS_TU:
				weekDay = DayOfWeek.TUESDAY;
				break;
			case FIELD_OPENING_HOURS_WE:
				weekDay = DayOfWeek.WEDNESDAY;
				break;
			case FIELD_OPENING_HOURS_TH:
				weekDay = DayOfWeek.THURSDAY;
				break;
			case FIELD_OPENING_HOURS_FR:
				weekDay = DayOfWeek.FRIDAY;
				break;
			case FIELD_OPENING_HOURS_SA:
				weekDay = DayOfWeek.SATURDAY;
				break;
			case FIELD_OPENING_HOURS_SU:
				weekDay = DayOfWeek.SUNDAY;
				break;
			default:
				weekDay = null;
			}

			if (weekDay == null) {
				throw new IOException("json field day of the week not recognized");
			}

			// iterate over the day times. TAKE INTO ACCOUNT THAT IT IS AN ARRAY
			// TODO validate overlapping between ranges
			for (final JsonNode time : item.getValue()) {

				String[] hours = time.asText().split(FIELD_OPENING_HOURS_SEPARATOR);

				timetable.add(
						new OpeningHoursDetail(weekDay, LocalTime.parse(hours[0]), LocalTime.parse(hours[1])));
			}
		}
	}
}
