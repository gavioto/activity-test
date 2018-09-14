package com.example.activities.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import com.example.activities.utils.CustomActivityDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@NaturalIdCache
//@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded=true)
//@NoArgsConstructor
@JsonDeserialize(using = CustomActivityDeserializer.class)
public class Activity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public ActivityType getLocation() {
		return location;
	}

	public void setLocation(ActivityType location) {
		this.location = location;
	}

	/**
	 * This field represents the Natural Id of an activity
	 */
	@NaturalId
	@Column(nullable = false, unique = true)
	@EqualsAndHashCode.Include
	private String name;

	private float hoursSpent;
	
	//The type is inferred by hibernate automatically JTS and Geolatte types can be used
	//@Type(type = "jts_geometry")
	//@Column(columnDefinition = "geometry(Point,4326)")
	private Point geom;

	@ManyToOne(cascade={CascadeType.MERGE,CascadeType.REMOVE})
	private Category category;

	@ManyToOne(cascade={CascadeType.MERGE,CascadeType.REMOVE})
	private District district;

	@ManyToOne(cascade= {CascadeType.MERGE,CascadeType.REMOVE})
	private ActivityType location;
	
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(fetch=FetchType.EAGER, orphanRemoval=true, cascade= {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private Set<OpeningDates> openingDates;

	public Set<OpeningDates> getOpeningDates() {
		return openingDates;
	}

	/**
	 * It will overwrite all information in database, use it carefully or use the OpeningDates repository
	 * @param openingHours
	 */
	public void setOpeningDates(Set<OpeningDates> openingHours) {
		this.openingDates = openingHours;
	}

	public Activity() {
		
	}
	
	public Activity(@NotBlank String name, @NotNull Category category, @NotNull District district,
			@NotNull ActivityType location) {
		this.name = name;
		this.category = category;
		this.district = district;
		this.location = location;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Geometry getGeom() {
		return geom;
	}

	public void setGeom(Point geom) {
		this.geom = geom;
	}

	public float getHoursSpent() {
		return hoursSpent;
	}

	public void setHoursSpent(float hoursSpent) {
		this.hoursSpent = hoursSpent;
	}
	
	@Override
	public String toString() {
		return "Activity [id=" + id + ", name=" + name + ", geom=" + geom + "]";
	}

}