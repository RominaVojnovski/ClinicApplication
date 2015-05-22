package edu.stevens.cs548.clinic.service.ejb;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDao;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDTO;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class PatientService
 */
@Stateless
//@Local(IPatientServiceLocal.class)
//@Remote(IPatientServiceRemote.class)
//@LocalBean
public class PatientService implements IPatientServiceLocal, IPatientServiceRemote {

    private PatientFactory patientFactory;
    private IPatientDAO patientDAO;
    
   
    public PatientService() {
        patientFactory = new PatientFactory();
        
    }
    @PersistenceContext(unitName="ClinicDomain")
    private EntityManager em;
    
    @PostConstruct
    private void initialize(){
    	patientDAO = new PatientDao(em);
    	
    }
    
    
    public PatientDTO getPatientByDbId(long id) throws PatientServiceExn{
     
    	try {
    		Patient patient = patientDAO.getPatientByDbId(id);
    		return new PatientDTO(patient);
    		
    	} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
    }

    
    public PatientDTO getPatientByPatientId(long id) throws PatientServiceExn {
   	 
    	try {
    		Patient patient = patientDAO.getPatientByPatientId(id);
    		return new PatientDTO(patient);
    		
    	} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
    }

    public void deletePatient(String name, long id) throws PatientServiceExn{
    	try {
			Patient p = patientDAO.getPatientByDbId(id);
			if((p.getName()).equals(name)){
					patientDAO.deletePatient(p);
			}
			else{
				throw new PatientServiceExn("DELETION FAILURE: Tried to delete wrong patient  name: "+name);
			}
			
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
    	
    }


    public PatientDTO[] getPatientsByNameDob(String name, Date dob) {
    	List<Patient> patients = patientDAO.getPatientByNameDob(name, dob);
    	PatientDTO[] dto = new PatientDTO[patients.size()];
    	
    	for(int i=0; i<dto.length;i++){
    		dto[i]=new PatientDTO(patients.get(i));
    	}
    	return dto;
    }

    public long createPatient(String name, Date dob, long patientId, long age) throws PatientServiceExn {
    	
    	Patient p = this.patientFactory.createPatient(patientId, name, dob, age);
    	try {
			long key = patientDAO.addPatient(p);
			return key;
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
    }

    static class TreatmentPDOToDTO implements ITreatmentVisitor{

    	private TreatmentDto dto;
    	public TreatmentDto getDTO(){
    		return dto;
    	}
		@Override
		public void visitDrugTreatment(long tid, String diagnosis, Provider p,
				String drug, float dosage) {
			dto = new TreatmentDto();
			dto.setId(tid);
			dto.setProviderId(p.getId());
			dto.setDiagnosis(diagnosis);
			DrugTreatmentType drugInfo = new DrugTreatmentType();
			drugInfo.setDosage(dosage);
			drugInfo.setName(drug);
			dto.setDrugTreatment(drugInfo);
			
			
		}

		@Override
		public void visitSurgeryTreatment(long tid, String diagnosis,
				Provider p, Date date) {
			dto = new TreatmentDto();
			dto.setId(tid);
			dto.setProviderId(p.getId());
			dto.setDiagnosis(diagnosis);
			SurgeryType surgInfo = new SurgeryType();
			surgInfo.setDate(date);
			dto.setSurgery(surgInfo);	
		}

		@Override
		public void visitRadiologyTreatment(long tid, String diagnosis,
				Provider p, List<Date> dates) {
			dto = new TreatmentDto();
			dto.setId(tid);
			dto.setProviderId(p.getId());
			dto.setDiagnosis(diagnosis);
			RadiologyType radioType = new RadiologyType();
			for(Date d: dates){
				radioType.getDate().add(d);
			}
			dto.setRadiology(radioType);
			
		}
    }
	@Override
	public TreatmentDto[] getTreatments(long id) throws PatientNotFoundExn,
			TreatmentNotFoundExn, PatientServiceExn {
		try {
			Patient p = patientDAO.getPatientByDbId(id);
			List<Long> patids = p.getTreatmentIds();
			TreatmentDto[] treatments = new TreatmentDto[patids.size()];
			for(int i=0;i<treatments.length;i++){
				TreatmentPDOToDTO visitor = new TreatmentPDOToDTO();
				p.visitTreatment(patids.get(i), visitor);
				treatments[i]=visitor.getDTO();
			}
			return treatments;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new PatientServiceExn(e.toString());
	}
}
}
