package com.example.activities.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NaturalIdCache
//@EqualsAndHashCode(onlyExplicitlyIncluded=true)
//@NoArgsConstructor
public class ActivityType implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@NaturalId
	@EqualsAndHashCode.Include
	private String name;
	
	public ActivityType() {
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ActivityType(String name){
		this.name = name;
	}
}