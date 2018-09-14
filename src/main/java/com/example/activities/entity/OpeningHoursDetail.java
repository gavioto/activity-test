package com.example.activities.entity;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.NoArgsConstructor;

@Entity
@NaturalIdCache
//@NoArgsConstructor
public class OpeningHoursDetail implements Serializable {

	private static final String JSON_FORMATTER = "HH:mm";

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@JsonIgnore
	private Long id;

//	@NaturalId
//	@JsonManagedReference
//	@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REMOVE}, fetch = FetchType.EAGER)
//	@JsonIgnore
//	private OpeningDates openingDates;

	/* TODO WARNING don't forget to create a converter for future type changes */
	@JsonIgnore
	private DayOfWeek period; // week, day, work-days

	@JsonIgnore
	private LocalTime openingHour;

	@JsonIgnore
	private LocalTime closeHour;

	@JsonSerialize
	@JsonProperty("")
	public String toString() {
		return openingHour.format(DateTimeFormatter.ofPattern(JSON_FORMATTER))+"-"+closeHour.format(DateTimeFormatter.ofPattern(JSON_FORMATTER));
	}
	
	public OpeningHoursDetail() {
		
	}
	
	public OpeningHoursDetail(
			//OpeningDates validDates, 
			DayOfWeek weekDay, LocalTime openingHour, LocalTime closeHour) {
		//this.openingDates = validDates;
		this.period = weekDay;
		this.openingHour = openingHour;
		this.closeHour = closeHour;
	}

//	public OpeningDates getOpeningDates() {
//		return openingDates;
//	}
//
//	public void setOpeningDates(OpeningDates openingDates) {
//		this.openingDates = openingDates;
//	}

	public DayOfWeek getPeriod() {
		return period;
	}

	public void setPeriod(DayOfWeek period) {
		this.period = period;
	}

	public LocalTime getOpeningHour() {
		return openingHour;
	}

	public void setOpeningHour(LocalTime openingHour) {
		this.openingHour = openingHour;
	}

	public LocalTime getCloseHour() {
		return closeHour;
	}

	public void setCloseHour(LocalTime closeHour) {
		this.closeHour = closeHour;
	}

}