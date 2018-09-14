package com.example.activities;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.activities.entity.Activity;
import com.example.activities.respository.ActivityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActiviesApplicationTests {

	@Autowired
	ActivityRepository repo;

	@Test
	public void contextLoads() {

	}

	/**
	 * Should return something not empty
	 */
	@Test
	public void pruebaRecomendacion() {

		List<Activity> result = (List<Activity>) repo.findByTimeRange(DayOfWeek.MONDAY, ZonedDateTime.now(),
				LocalTime.parse("17:16:30"), LocalTime.parse("20:14:30"), "category");

		assertEquals(false, result.isEmpty());

	}

}
