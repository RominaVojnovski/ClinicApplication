package edu.stevens.cs548.clinic.domain;

import edu.stevens.cs548.clinic.domain.Treatment;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: DrugTreatment
 *
 */
@Entity
@DiscriminatorValue("D")
public class DrugTreatment extends Treatment implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private String drug;
	public String getDrug() {
		return drug;
	}

	public void setDrug(String drug) {
		this.drug = drug;
	}

	private float dosage;
	
	public float getDosage() {
		return dosage;
	}

	public void setDosage(float dosage) {
		this.dosage = dosage;
	}


	public void visit(ITreatmentVisitor visitor){
		visitor.visitDrugTreatment(this.getId(), 
				this.getDiagnosis(), 
				this.getProvider(),
				this.getPatient(),
				this.drug, this.dosage);
	}
	
	public DrugTreatment() {
		super();
		this.setTreatmentType("D");
	
	}
   
}
