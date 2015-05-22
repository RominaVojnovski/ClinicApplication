package edu.stevens.cs548.clinic.domain;

import edu.stevens.cs548.clinic.domain.Treatment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: SurgeryTreatment
 *
 */
@Entity
@DiscriminatorValue("S")

public class SurgeryTreatment extends Treatment implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.DATE)
	private Date surgeryDate;
	
	public Date getSurgeryDate() {
		return surgeryDate;
	}

	public void setSurgeryDate(Date surgeryDate) {
		this.surgeryDate = surgeryDate;
	}

	public void visit(ITreatmentVisitor visitor){
		visitor.visitSurgeryTreatment(this.getId(), 
									this.getDiagnosis(), 
									this.getProvider(),
									this.getPatient(),
									surgeryDate);
	}
	
	
	public SurgeryTreatment() {
		super();
		this.setTreatmentType("S");
	}
   
}
