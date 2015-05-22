package edu.stevens.cs548.clinic.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class PatientDao implements IPatientDAO {
	
	private EntityManager em;
	private TreatmentDao treatmentDao;
	
	@Override
	public Patient getPatientByDbId(long id) throws PatientExn {
		Patient p = em.find(Patient.class, id);
		if(p == null){
			System.out.println("Patient not found: primary key = "+id);
			throw new PatientExn("Patient not found: primary key = "+id);
		}else{
			p.setTreatmentDAO(this.treatmentDao);
			return p;
		}
	}
	
	@Override
	public Patient getPatientByPatientId(long pid) throws PatientExn {
		TypedQuery <Patient> query = 
				em.createNamedQuery("SearchPatientByPatientID", Patient.class)
				.setParameter("pid", pid);
		List<Patient> patients =query.getResultList();
		if(patients.size()>1){
			System.out.println("Duplicate patient records found with patient id = "+pid);
			throw new PatientExn("Duplicate patient records: patient id ="+pid);
		}
		else if(patients.size() < 1){
			System.out.println("Patient not found: patient id = "+pid);
			return null;
		}
		else{
			Patient p =  patients.get(0);
			p.setTreatmentDAO(this.treatmentDao);
			return p;
		}	
	}

	

	@Override
	public List<Patient> getPatientByNameDob(String name, Date dob) {
		TypedQuery <Patient> query = 
				em.createNamedQuery("SearchPatientByNameDOB", Patient.class)
				.setParameter("name", name)
				.setParameter("dob", dob);
		
		List<Patient> patients = query.getResultList();
		for(Patient p : patients){
			p.setTreatmentDAO(this.treatmentDao);
		}
		
		return patients;
	}

	@Override
	public long addPatient(Patient patient) throws PatientExn{
		long pid = patient.getPatientId();
		if(pid>0){
		
			TypedQuery <Patient> query = 
				em.createNamedQuery("SearchPatientByPatientID", Patient.class)
				.setParameter("pid", pid);
			List<Patient> patients =query.getResultList();
			if(patients.size() < 1){
				//age sanity check
				Date date1 = patient.getBirthDate();
				Calendar now = Calendar.getInstance();
				Date date2 = now.getTime();
				long years =((long)( (date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24)))/365;
				
				if(patient.getAge()==years){
					em.persist(patient);
					em.flush();
					long retid=patient.getId();
					patient.setTreatmentDAO(this.treatmentDao);
					return retid;
				}
				else{
					System.out.println("Insertion failure: Patients age and dob do not match");
					throw new PatientExn("Insertion: Patient with patient id : "+pid+
	    							"has an illegal dob and age match!");
				}
			}
			else{
				Patient patient2 =  patients.get(0);
				System.out.println("Insertion FAIL: Patient with patient id :"+pid+
						"already exists with name = "+patient2.getName());
				throw new PatientExn("Insertion FAIL: Patient with patient id :"+pid+
								"already exists with name = "+patient2.getName());
			}
		
		}
		//patient has not provided the patient id field
		else{
			//age sanity check
			Date date1 = patient.getBirthDate();
			Calendar now = Calendar.getInstance();
			Date date2 = now.getTime();
			long years =((long)( (date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24)))/365;
			
			if(patient.getAge()==years){
				em.persist(patient);
				em.flush();
				long retid=patient.getId();
				patient.setTreatmentDAO(this.treatmentDao);
				return retid;
			}
			else{
				System.out.println("Insertion failure: Patients age and dob do not match");
				throw new PatientExn("Insertion: Patient with patient id : "+pid+
    							"has an illegal dob and age match!");
			}
		}
	}

	@Override
	public void deletePatient(Patient patient) throws PatientExn {
		em.createQuery("delete from Treatment t where t.patient.id = :id")
		.setParameter("id", patient.getId())
		.executeUpdate();
		em.remove(patient);
		em.flush();
		

	}
	
	@Override
	public void deletePatientbyID(long id) throws PatientExn {
		em.createQuery("delete from Treatment t where t.patient.id = :id")
		.setParameter("id", id)
		.executeUpdate();
		
		Patient patient=this.getPatientByDbId(id);
		em.remove(patient);
		em.flush();
		

	}
	
	public PatientDao(EntityManager em){
		this.em = em;
		this.treatmentDao = new TreatmentDao(em);
	}


}
