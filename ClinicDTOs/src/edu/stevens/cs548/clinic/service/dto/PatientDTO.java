package edu.stevens.cs548.clinic.service.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import edu.stevens.cs548.clinic.domain.Patient;


public class PatientDTO {
	
	public long id;
	
	@XmlElement(name="patient-id")
	public long patientId;
	
	@XmlElement(required=true)
	public String name;
	
	@XmlElement(required=true,name="dob")
	public Date birthDate;
	
	@XmlElement(required=true)
	public long age;
	
	public long[]treatments;
	
	public PatientDTO(){}
	
	public PatientDTO(Patient patient){
		this.id=patient.getId();
		this.patientId=patient.getPatientId();
		this.name = patient.getName();
		this.birthDate = patient.getBirthDate();
		this.age = patient.getAge();
		List<Long>tids = patient.getTreatmentIds();
		this.treatments = new long[tids.size()];
		
		
		for(int i=0;i<treatments.length;i++){
			this.treatments[i] = tids.get(i);
		}
		
	}
	

}
