package org.vamdc.portal.entity.species;

// Generated 18.09.2012 16:57:53 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

/**
 * VamdcInchikeyExceptions generated by hbm2java
 */
@Entity
@Table(name = "vamdc_inchikey_exceptions", catalog = "vamdc_species_dev")
public class VamdcInchikeyExceptions implements java.io.Serializable {

	private Integer id;
	private VamdcSpecies vamdcSpecies;
	private String reason;

	public VamdcInchikeyExceptions() {
	}

	public VamdcInchikeyExceptions(VamdcSpecies vamdcSpecies, String reason) {
		this.vamdcSpecies = vamdcSpecies;
		this.reason = reason;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "species_id", nullable = false)
	@NotNull
	public VamdcSpecies getVamdcSpecies() {
		return this.vamdcSpecies;
	}

	public void setVamdcSpecies(VamdcSpecies vamdcSpecies) {
		this.vamdcSpecies = vamdcSpecies;
	}

	@Column(name = "reason", nullable = false)
	@NotNull
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
