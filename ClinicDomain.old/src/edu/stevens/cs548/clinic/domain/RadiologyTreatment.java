package edu.stevens.cs548.clinic.domain;

import edu.stevens.cs548.clinic.domain.Treatment;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: RadiologyTreatment
 *
 */
@Entity
@DiscriminatorValue("R")

public class RadiologyTreatment extends Treatment implements Serializable {

	
	private static final long serialVersionUID = 1L;
	

	private List <Date> dates;
	
	public List<Date> getDates() {
		return dates;
	}

	public void setDates(List<Date> dates) {
		this.dates = dates;
	}

	public void visit(ITreatmentVisitor visitor){
		visitor.visitRadiologyTreatment(this.getId(), 
										this.getDiagnosis(), 
										this.getProvider(), 
										dates);
	}
	
	public RadiologyTreatment() {
		super();
		this.setTreatmentType("R");
	}
   
}
