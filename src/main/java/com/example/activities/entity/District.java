package com.example.activities.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
//@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded=true) //https://github.com/rzwitserloot/lombok/issues/1707
@Entity
@NaturalIdCache
//@NoArgsConstructor(access=AccessLevel.PUBLIC)
public class District implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@EqualsAndHashCode.Include
	@NaturalId
	private String name;

	public District() {
		
	}
	
	public District(@NotBlank String name, City city) {
		this.name = name;
		this.city = city;
	}

	@EqualsAndHashCode.Include
	@ManyToOne(cascade = {CascadeType.MERGE}, optional=false)
	private City city;

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}