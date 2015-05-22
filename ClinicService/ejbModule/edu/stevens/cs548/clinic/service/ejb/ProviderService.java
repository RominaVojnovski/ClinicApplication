package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDao;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDao;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDTO;

/**
 * Session Bean implementation class ProviderService
 */
@Stateless
//@Local(IProviderServiceLocal.class)
//@Remote(IProviderServiceRemote.class)
//@LocalBean
public class ProviderService implements IProviderServiceLocal, IProviderServiceRemote {

    private ProviderFactory providerFactory;
    private IProviderDAO providerDAO;
    private IPatientDAO patientDAO;

    
    
    public ProviderService() {
    	providerFactory = new ProviderFactory();
    	
    }

    @PersistenceContext(unitName="ClinicDomain")
    private EntityManager em;
    
    @PostConstruct
    private void initialize(){
    	providerDAO = new ProviderDao(em);
    	patientDAO = new PatientDao(em);
 
    }

    public void createProvider(String name, long providerId) throws ProviderServiceExn {
       Provider p = this.providerFactory.createProvider(providerId, name);
       try {
		providerDAO.addProvider(p);
       } catch (ProviderExn e) {
    	   throw new ProviderServiceExn(e.toString());
       }
    }

    public ProviderDTO getProviderByDbId(long id) throws ProviderServiceExn {
		
    	try {
			Provider provider = providerDAO.getProviderByDbId(id);
	    	return new ProviderDTO(provider);

		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
    }
    
 
    public ProviderDTO getProviderByProviderId(long id) throws ProviderServiceExn {
    	try {
			Provider provider = providerDAO.getProviderByProviderId(id);
	    	return new ProviderDTO(provider);

		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
    }

    public ProviderDTO[] getProvidersByName(String name) {
    	List<Provider> providers = providerDAO.getProviderByName(name);
    	
    	ProviderDTO[] dto = new ProviderDTO[providers.size()];
    	
    	for (int i=0;i< dto.length;i++){
    		dto[i]=new ProviderDTO(providers.get(i));
    	}
    	return dto;
    }

	
    public void deleteProvider(String name, long id) throws ProviderServiceExn{
    	try {
			Provider p = providerDAO.getProviderByDbId(id);
			if((p.getName()).equals(name)){
					providerDAO.deleteProvider(p);
			}
			else{
				throw new ProviderServiceExn("DELETION FAILURE: Tried to delete wrong provider  name: "+name);
			}
			
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
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
	public TreatmentDto[] getTreaments(long id) throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn{
		try {
			Provider p = providerDAO.getProviderByDbId(id);
			List<Long> proids = p.getTreatmentIds();
			TreatmentDto[] treatments = new TreatmentDto[proids.size()];
			for(int i=0;i<treatments.length;i++){
				TreatmentPDOToDTO visitor = new TreatmentPDOToDTO();
				p.visitTreatment(proids.get(i), visitor);
				treatments[i]=visitor.getDTO();
			}
			return treatments;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
	}
	}
	
	@Override
	public TreatmentDto[] getTreatments(long id, long patid)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn {
		try {
			Provider p = providerDAO.getProviderByDbId(id);
			Patient pat = patientDAO.getPatientByDbId(patid);
			List<Long> proids = p.getTreatmentIdsforPatient(pat);
			TreatmentDto[] treatments = new TreatmentDto[proids.size()];
			for(int i=0;i<treatments.length;i++){
				TreatmentPDOToDTO visitor = new TreatmentPDOToDTO();
				p.visitTreatment(proids.get(i), visitor);
				treatments[i]=visitor.getDTO();
			}
			return treatments;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
		} catch (PatientExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}
	


	@Override
	public void deleteTreatment(long patid, long provid, long tid) throws ProviderNotFoundExn,
			TreatmentNotFoundExn, ProviderServiceExn {
			try {
				Patient pat = patientDAO.getPatientByDbId(patid);
				Provider prov = providerDAO.getProviderByDbId(provid);
				List<Long>tids = prov.getTreatmentIdsforPatient(pat);
				if(tids.contains(tid)){
					prov.deleteTreatment(tid);
				}
				else{
					throw new ProviderServiceExn("Delete Treatment Failure: Trying to delete treatment for wrong patient specified");
				}
				
			} catch (PatientExn e) {
				throw new ProviderServiceExn(e.toString());
				
			} catch (ProviderExn e) {
				throw new ProviderNotFoundExn(e.toString());
			} catch (TreatmentExn e) {
				throw new ProviderServiceExn(e.toString());
			}	
	}

	@Override
	public void addTreatment(long patid, long provid, TreatmentDto t) throws ProviderServiceExn,ProviderNotFoundExn, TreatmentNotFoundExn{

		try {
			Provider prov=providerDAO.getProviderByProviderId(provid);
			Patient pat = patientDAO.getPatientByPatientId(patid);
			if(t.getDrugTreatment()!=null){
				prov.addDrugTreatment(t.getDiagnosis(), t.getDrugTreatment().getName(),t.getDrugTreatment().getDosage(), pat);
				
			}
			else if(t.getRadiology()!=null){
				prov.addRadiologyTreatment(t.getDiagnosis(), t.getRadiology().getDate(), pat);
			}
			else if(t.getSurgery()!=null){
				prov.addSurgeryTreatment(t.getDiagnosis(), t.getSurgery().getDate(), pat);
			
			}
			else{
				throw new TreatmentNotFoundExn("Add Treatment Failure: No valid Treatment specified");
			}
			
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (PatientExn e) {
			throw new ProviderServiceExn(e.toString());
		} 
	}

	
	
}
