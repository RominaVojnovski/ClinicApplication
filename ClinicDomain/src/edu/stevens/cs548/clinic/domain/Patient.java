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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private long patientId;
	private String name;
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	private long age;
	
	
   
	public long getAge() {
		return age;
	}

	public void setAge(long age) {
		this.age = age;
	}

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
	@OneToMany(mappedBy = "patient", cascade = REMOVE, orphanRemoval = true)
	@OrderBy
	private List<Treatment>treatments;
	
	
	protected List<Treatment> getTreatments(){
		return treatments;
	}
	
	protected void setTreatments(List<Treatment>treatments){
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
	
	
	
	public List<Long> getTreatmentIds(){
		List <Long> tids = new ArrayList<Long>();
		for(Treatment t: this.getTreatments()){
			tids.add(t.getId());	
		}
		return tids;
	}
	
	public void visitTreatment(long tid, ITreatmentVisitor visitor) throws TreatmentExn{
		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		if(t.getPatient() != this){
			System.out.println("ILLEGAL VISIT  Patient PK: "+id+" Treatment ID: "+tid);
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
	
	public Patient() {
		super();
		treatments = new ArrayList<Treatment>();
		
	}
	


}
	
