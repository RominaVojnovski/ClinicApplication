package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;

import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDTO;

public interface IPatientService {
	
	public class PatientServiceExn extends Exception{

		private static final long serialVersionUID = 1L;

		public PatientServiceExn(String m){
			super(m);
		}
	}
	public class PatientNotFoundExn extends Exception{

		private static final long serialVersionUID = 1L;
		
		public PatientNotFoundExn(String m){
			super(m);
		}
		
	}
	public class TreatmentNotFoundExn extends Exception{

		private static final long serialVersionUID = 1L;
		
		public TreatmentNotFoundExn(String m){
			super(m);
		}
		
	}
	
	public long createPatient(String name, Date dob, long patientId, long age) throws PatientServiceExn;

	public PatientDTO getPatientByDbId(long id) throws PatientServiceExn;
	
	public PatientDTO getPatientByPatientId(long id) throws PatientServiceExn;
	
	public PatientDTO[] getPatientsByNameDob (String name, Date dob);
	
	public void deletePatient(String name, long id) throws PatientServiceExn;
	
	public TreatmentDto[] getTreatments(long id) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn;
	
}
