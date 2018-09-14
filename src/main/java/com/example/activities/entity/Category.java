package com.example.activities.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
//@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded=true) //https://github.com/rzwitserloot/lombok/issues/1707
@NaturalIdCache
//@NoArgsConstructor
public class Category implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	/* WARNING may cause performance issues when there are a lot of activities */
//	@OneToMany(targetEntity=Activity.class, fetch= FetchType.LAZY)
//	private List activities;
	
	@EqualsAndHashCode.Include
	@NaturalId
	private String name; //week, day, work-days	
	
	public Category() {
		
	}
	
	public Category(@NotBlank String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}
}