package com.example.activities.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import com.vividsolutions.jts.geom.Geometry;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NaturalIdCache
//@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded=true)
//@NoArgsConstructor
public class City implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@NaturalId
	@EqualsAndHashCode.Include
	private String name; 
	
	public City() {
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Geometry getArea() {
		return area;
	}

	public void setArea(Geometry area) {
		this.area = area;
	}

	private Geometry area;
	
	public City(@NotBlank String name){
		this.name = name;
	}
}