package edu.stevens.cs548.clinic.service.dto;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.domain.Provider;

@XmlRootElement(name="provider-dto", namespace="http://www.example2.org/clinic/schemas/provider")
public class ProviderDTO {
	
	public long id;
	
	@XmlElement(name="provider-id")
	public long providerId;
	
	@XmlElement(required=true)
	public String name;
	
	public String specialization;
	
	public long[]treatments;
	
	public ProviderDTO(){}
	
	public ProviderDTO(Provider p){
		this.id=p.getId();
		this.providerId=p.getProviderId();
		this.name = p.getName();
		this.specialization = p.getSpecialization();
		List<Long>tids = p.getTreatmentIds();
		this.treatments = new long[tids.size()];
		
		
		for(int i=0;i<treatments.length;i++){
			this.treatments[i] = tids.get(i);
		}
		
	}
	

}
