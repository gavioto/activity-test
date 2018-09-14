package com.example.activities.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalIdCache;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded=true) //https://github.com/rzwitserloot/lombok/issues/1707
@Entity
@NaturalIdCache
//@NoArgsConstructor
public class OpeningDates implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Activities are retrieved EAGER because there is an use case about finding activities by hour.
	 */
//	@EqualsAndHashCode.Include
//	@ManyToOne(fetch=FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REMOVE})
//	@NotNull
//	private Activity activity;
	
	@EqualsAndHashCode.Include
	private ZonedDateTime validFrom;
	
	@EqualsAndHashCode.Include
	private ZonedDateTime validTo;
	
	@OneToMany(fetch=FetchType.EAGER,orphanRemoval=true, cascade= {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REMOVE})
	private Set<OpeningHoursDetail> openingHoursDetail;
	
	public OpeningDates() {
		
	}
	
	public OpeningDates(//Activity activity, 
			ZonedDateTime validFrom, ZonedDateTime validTo) {
		//this.activity= activity;
		this.validFrom=validFrom;
		this.validTo=validTo;
	}

	public ZonedDateTime getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(ZonedDateTime validFrom) {
		this.validFrom = validFrom;
	}

	public ZonedDateTime getValidTo() {
		return validTo;
	}

	public void setValidTo(ZonedDateTime validTo) {
		this.validTo = validTo;
	}

	public Set<OpeningHoursDetail> getOpeningHoursDetail() {
		return openingHoursDetail;
	}

	public void setOpeningHoursDetail(Set<OpeningHoursDetail> openingHoursDetail) {
		this.openingHoursDetail = openingHoursDetail;
	}

//	public Activity getActivity() {
//		return activity;
//	}
}