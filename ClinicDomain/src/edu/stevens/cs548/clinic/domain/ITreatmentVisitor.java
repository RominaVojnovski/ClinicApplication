package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public interface ITreatmentVisitor {

	public void visitDrugTreatment(long tid,
									String diagnosis,
							 		Provider p, Patient pat,
									String drug,
							 		float dosage);
	
	public void visitSurgeryTreatment(long tid,
										String diagnosis,
										Provider p,  Patient pat,
										Date date);
	
	public void visitRadiologyTreatment(long tid,
										String diagnosis,
										Provider p, Patient pat,
										List<Date>dates);
	
	
}
