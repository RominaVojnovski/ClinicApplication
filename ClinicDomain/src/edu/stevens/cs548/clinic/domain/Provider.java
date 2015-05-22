package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;

/**
 * Entity implementation class for Entity: Provider
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(
		name = "SearchProviderByName",
		query = "select p from Provider p where p.name = :name"),

	@NamedQuery(
		name = "SearchProviderByProviderID",
		query = "select p from Provider p where p.providerId = :pid")
})
@Table(name = "Provider")

public class Provider implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public Provider() {
		super();
		treatments = new ArrayList<Treatment>();
	}
   
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long providerId;
	private String name;
	private String specialization;
	
	@Transient
	private ITreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO(ITreatmentDAO tdao){
		this.treatmentDAO = tdao;
	}
	
	long addTreatment(Treatment t){
		if(this.treatmentDAO == null){
			
		}
		
		long tid = this.treatmentDAO.addTreatment(t);
		
		this.getTreatments().add(t);	
		
		if(t.getProvider()!= this)
			t.setProvider(this);

		System.out.println("CHECK 2 what is treatment t patient id is = "+t.getPatient().getId());
		return tid;
	}
	
	public long addDrugTreatment(String diagnosis, String drug, float dosage, Patient p){
		DrugTreatment treatment = new DrugTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDosage(dosage);
		treatment.setDrug(drug); 
		treatment.setPatient(p);
		long retval = this.addTreatment(treatment);
		return retval;
	}
	
	public long addSurgeryTreatment(String diagnosis, Date surgeryDate, Patient p){
		SurgeryTreatment treatment = new SurgeryTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setSurgeryDate(surgeryDate);
		treatment.setPatient(p);
		long retval = this.addTreatment(treatment);
		return retval;
		
	}
	
	public long addRadiologyTreatment(String diagnosis, List<Date> date,Patient p){
		RadiologyTreatment treatment = new RadiologyTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDates(date);
		treatment.setPatient(p);
		long retval = this.addTreatment(treatment);
		return retval;
	}
	
	
	
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	public void setProviderId(long pid){
		this.providerId = pid;
	}
	
	public long getProviderId(){
		return providerId;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getSpecialization(){
		return specialization;
	}
	public void setSpecialization(String specialization){
		this.specialization = specialization;
	}
	
	@OneToMany(mappedBy = "provider", orphanRemoval = false)
	@OrderBy
	private List<Treatment> treatments;
	
	protected List<Treatment> getTreatments(){
		return treatments;
	}
	
	protected void setTreatments(List<Treatment>treatments){
		this.treatments = treatments;
	}
	
	public List<Long> getTreatmentIds(){
		List <Long> tids = new ArrayList<Long>();
		for(Treatment t: this.getTreatments()){
			tids.add(t.getId());	
		}
		return tids;
	}

	public List<Long> getTreatmentIdsforPatient(Patient p){
		System.out.println("Inside Provider...get treatmentids for patient: ");
		List <Long> tids = new ArrayList<Long>();
		for(Treatment t: this.getTreatments()){
			if(t.getPatient().getId() == p.getId()){
				tids.add(t.getId());	
			}
		}
		return tids;
	}

	public void visitTreatment(long tid, ITreatmentVisitor visitor) throws TreatmentExn{
		Treatment t = this.treatmentDAO.getTreatmentByDbId(tid);
		if(t.getProvider() != this){
			System.out.println("ILLEGAL VISIT  Provider PK: "+id+" Treatment ID: "+tid);
			throw new TreatmentExn("Inappropriate treatment access: provider ="+id+
									"treatment id = "+tid);
		}
		t.visit(visitor);
	}
	
	public void visitTreatments(ITreatmentVisitor visitor){
		for(Treatment t: this.getTreatments()){
			t.visit(visitor);	
		}
	}
	
	public void deleteTreatment(long tid) throws TreatmentExn{
		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		if(t.getProvider() != this){
			System.out.println("ILLEGAL TREATMENT ACCESS Provider PK: "+id+" Treatment ID: "+tid);
			throw new TreatmentExn("Inappropriate treatment access: provider ="+id+
									"treatment id = "+tid);
		}
		treatmentDAO.deleteTreatment(t);
	}
	
	

}
