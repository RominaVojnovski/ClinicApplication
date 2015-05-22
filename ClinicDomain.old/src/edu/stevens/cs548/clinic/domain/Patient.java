package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import static javax.persistence.CascadeType.REMOVE;

/**
 * Entity implementation class for Entity: Patient
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(
			name = "SearchPatientByNameDOB",
			query = "select p from Patient p where p.name = :name and p.birthDate = :dob"),

	@NamedQuery(
			name = "SearchPatientByPatientID",
			query = "select p from Patient p where p.patientId = :pid")

})
@Table(name="Patient")


public class Patient implements Serializable {

	
	private static final long serialVersionUID = 1L;
	@Id
	private long id;
	private long patientId;
	private String name;
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	
	
   
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public void setPatientId(long pid){
		this.patientId = pid;
	}
	
	public long getPatientId(){
		return patientId;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public void setBirthDate(Date birthDate){
		this.birthDate = birthDate;
	}
	public Date getBirthDate(){
		return birthDate;
	}
	@OneToMany(mappedBy = "patient", cascade = REMOVE)
	@OrderBy
	public List<Treatment>treatments;
	
	
	public List<Treatment> getTreatments(){
		return treatments;
	}
	
	public void setTreatments(List<Treatment>treatments){
		this.treatments = treatments;
	}
	
	@Transient
	private ITreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO(ITreatmentDAO tdao){
		this.treatmentDAO = tdao;
	}
	
	void addTreatment(Treatment t){
		this.treatmentDAO.addTreatment(t);
		this.getTreatments().add(t);
		if(t.getPatient()!= this)
			t.setPatient(this);
	}
	
	public void addDrugTreatment(String diagnosis, String drug, float dosage, Provider p){
		DrugTreatment treatment = new DrugTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDosage(dosage);
		treatment.setDrug(drug); 
		treatment.setProvider(p);
		this.addTreatment(treatment);
	}
	
	public void addSurgeryTreatment(String diagnosis, Date surgeryDate, Provider p){
		SurgeryTreatment treatment = new SurgeryTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setSurgeryDate(surgeryDate);
		treatment.setProvider(p);
		this.addTreatment(treatment);
		
	}
	
	public void addRadiologyTreatment(String diagnosis, List<Date> date,Provider p){
		RadiologyTreatment treatment = new RadiologyTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDates(date);
		treatment.setProvider(p);
		this.addTreatment(treatment);
	}
	
	public List<Long> getTreatmentIds(){
		List <Long> tids = new ArrayList<Long>();
		for(Treatment t: this.getTreatments()){
			tids.add(t.getId());	
		}
		return tids;
	}
	
	public void visitTreatment(long tid, ITreatmentVisitor visitor) throws TreatmentExn{
		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		if(t.getPatient() == this){
			throw new TreatmentExn("Inappropriate treatment access: patient ="+id+
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
		if(t.getPatient() == this){
			throw new TreatmentExn("Inappropriate treatment access: patient ="+id+
									"treatment id = "+tid);
		}
		treatmentDAO.deleteTreatment(t);
	}
	
	
	public Patient() {
		super();
		treatments = new ArrayList<Treatment>();
		
	}
	


}
	
