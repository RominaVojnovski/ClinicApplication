package edu.stevens.cs548.clinic.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TreatmentVisitor implements ITreatmentVisitor {

	@Override
	public void visitDrugTreatment(long tid, String diagnosis, Provider p, Patient pat,
			String drug, float dosage) {
		System.out.println("VISITING DRUG TREATMENT: The drug treament info is diagnosis: "+
							diagnosis+" the drug is: "+drug+" and the dosage is: "+dosage+
							" and the provider is : "+p.getName()
				);
		

	}

	@Override
	public void visitSurgeryTreatment(long tid, String diagnosis, Provider p, Patient pat,
			Date date) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    String displaydate=formatter.format(date);
		System.out.println("VISITING SURGERY TREATMENT: The surgery treament info is diagnosis: "+
				diagnosis+" the provider is: "+p.getName()+" and the surgery date is: "+displaydate
				);
		

	}

	@Override
	public void visitRadiologyTreatment(long tid, String diagnosis, Provider p, Patient pat,
			List<Date> dates) {
		System.out.println("VISITING RADIOLOGY TREATMENT: The radiology treament info is diagnosis: "+
				diagnosis+" and the provider is: "+p.getName()
				);
		System.out.println("The dates for treatment are: ");
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    String displaydate;
	    
	    for(Date d : dates){
	    	 displaydate = formatter.format(d);
	    	 System.out.println("Radiology Date : " + displaydate);
	    }

	}

}
