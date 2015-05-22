package edu.stevens.cs548.clinic.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ProviderDao implements IProviderDAO {
	private EntityManager em;
	private TreatmentDao treatmentDao;

	@Override
	public Provider getProviderByDbId(long id) throws ProviderExn {
		Provider p = em.find(Provider.class, id);
		if(p == null){
			throw new ProviderExn("Patient not found key: "+ id);
		}
		else{
			return p;
		}
	}
	
	@Override
	public Provider getProviderByProviderId(long provid) throws ProviderExn{
		TypedQuery <Provider> query = em.createNamedQuery("SearchProviderByProviderId", Provider.class)
									  .setParameter("pid", provid);
		List<Provider> providers = query.getResultList();
		if(providers.size()>1){
			throw new ProviderExn("Duplicate providers found: key="+provid);
		}
		else if(providers.size()<1){
			throw new ProviderExn("Provider not found key= "+provid);
		}
		else{
			return providers.get(0);
		}
	}
	
	@Override
	public List<Provider> getProviderByNameSpecialization(String name, String specialization) {
		TypedQuery <Provider> query = 
				em.createNamedQuery("SearchProviderByNameSpecialization", Provider.class)
				.setParameter("name", name)
				.setParameter("specialization", specialization);
		
		List<Provider> providers = query.getResultList();
		for(Provider p : providers){
			p.setTreatmentDAO(this.treatmentDao);
		}
		
		return providers;
	}
	

	@Override
	public void addProvider(Provider prov) {
		em.persist(prov);
	}

	@Override
	public void deleteProvider(Provider prov) {
		em.createQuery("delete from Patient p where p.provider.id = :id")
		.setParameter("id", prov.getId())
		.executeUpdate();
		em.remove(prov);
	}
	
	public ProviderDao(EntityManager em){
		this.em = em;
	}

}
