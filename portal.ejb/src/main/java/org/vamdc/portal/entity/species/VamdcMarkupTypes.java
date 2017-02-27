package org.vamdc.portal.entity.species;

// Generated 18.09.2012 16:57:53 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * VamdcMarkupTypes generated by hbm2java
 */
@Entity
@Table(name = "vamdc_markup_types", catalog = "vamdc_species_dev")
public class VamdcMarkupTypes implements java.io.Serializable {

	private int id;
	private String name;
	private Set<VamdcSpeciesStructFormulae> vamdcSpeciesStructFormulaes = new HashSet<VamdcSpeciesStructFormulae>(
			0);
	private Set<VamdcSpeciesNames> vamdcSpeciesNameses = new HashSet<VamdcSpeciesNames>(
			0);

	public VamdcMarkupTypes() {
	}

	public VamdcMarkupTypes(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public VamdcMarkupTypes(int id, String name,
			Set<VamdcSpeciesStructFormulae> vamdcSpeciesStructFormulaes,
			Set<VamdcSpeciesNames> vamdcSpeciesNameses) {
		this.id = id;
		this.name = name;
		this.vamdcSpeciesStructFormulaes = vamdcSpeciesStructFormulaes;
		this.vamdcSpeciesNameses = vamdcSpeciesNameses;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 30)
	@NotNull
	@Length(max = 30)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vamdcMarkupTypes")
	public Set<VamdcSpeciesStructFormulae> getVamdcSpeciesStructFormulaes() {
		return this.vamdcSpeciesStructFormulaes;
	}

	public void setVamdcSpeciesStructFormulaes(
			Set<VamdcSpeciesStructFormulae> vamdcSpeciesStructFormulaes) {
		this.vamdcSpeciesStructFormulaes = vamdcSpeciesStructFormulaes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vamdcMarkupTypes")
	public Set<VamdcSpeciesNames> getVamdcSpeciesNameses() {
		return this.vamdcSpeciesNameses;
	}

	public void setVamdcSpeciesNameses(
			Set<VamdcSpeciesNames> vamdcSpeciesNameses) {
		this.vamdcSpeciesNameses = vamdcSpeciesNameses;
	}

}
