package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import static javax.persistence.CascadeType.REMOVE;

/**
 * Entity implementation class for Entity: Provider
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(
		name = "SearchProviderByNameSpecialization",
		query = "select p from Provider p where p.name = :name and p.specialization = :specialization"),

	@NamedQuery(
		name = "SearchProviderByProviderID",
		query = "select p from Provider p where p.id = :pid")
})
@Table(name = "Provider")

public class Provider implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public Provider() {
		super();
		treatments = new ArrayList<Treatment>();
	}
   
	@Id
	private long id;
	private long providerId;
	private String name;
	private String specialization;
	
	@Transient
	private ITreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO(ITreatmentDAO tdao){
		this.treatmentDAO = tdao;
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
	
	@OneToMany(cascade = REMOVE, mappedBy = "provider")
	@OrderBy
	public List<Treatment> treatments;
	
	public List<Treatment> getTreatments(){
		return treatments;
	}
	
	public void setTreatments(List<Treatment>treatments){
		this.treatments = treatments;
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
		if(t.getProvider() == this){
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

}
