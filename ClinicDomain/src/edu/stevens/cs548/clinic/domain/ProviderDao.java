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
			System.out.println("Provider not found:  id = "+id);
			throw new ProviderExn("Provider not found key: "+ id);
		}
		else{
			return p;
		}
	}
	
	@Override
	public Provider getProviderByProviderId(long provid) throws ProviderExn{
		TypedQuery <Provider> query = em.createNamedQuery("SearchProviderByProviderID", Provider.class)
									  .setParameter("pid", provid);
		List<Provider> providers = query.getResultList();
		if(providers.size()>1){
			throw new ProviderExn("Duplicate providers found: key="+provid);
		}
		else if(providers.size()<1){
			System.out.println("Provider not found: provider id = "+provid);
			return null;
		}
		else{
			return providers.get(0);
		}
	}
	
	@Override
	public List<Provider> getProviderByName(String name) {
		TypedQuery <Provider> query = 
				em.createNamedQuery("SearchProviderByName", Provider.class)
				.setParameter("name", name);
		
		List<Provider> providers = query.getResultList();
		for(Provider p : providers){
			p.setTreatmentDAO(this.treatmentDao);
		}
		
		return providers;
	}
	

	@Override
	public long addProvider(Provider prov) throws ProviderExn {
		long pid = prov.getProviderId();
		TypedQuery <Provider> query = 
				em.createNamedQuery("SearchProviderByProviderID", Provider.class)
				.setParameter("pid", pid);
			
		List<Provider> providers =query.getResultList();
		if(providers.size() < 1){
			em.persist(prov);
			em.flush();
			long retid=prov.getId();
			return retid;
		}
		else{
			Provider provider2 =  providers.get(0);
			System.out.println("Insertion unsuccesful: Providers with provider id :"+pid+
					"already exists with name = "+provider2.getName());
			
			throw new ProviderExn("Insertion: Providers with provider id :"+pid+
							"already exists with name = "+provider2.getName());
		}
	}

	@Override
	public void deleteProvider(Provider prov) {
		System.out.println("INSIDE THE DELETEPROVIDER IN PROVIDERODAO");
		em.createQuery("UPDATE Treatment t SET t.provider= null where t.provider.id = :id")
		.setParameter("id", prov.getId())
		.executeUpdate();
		em.flush();
		em.remove(prov);
		em.flush();
		System.out.println("AT END OF DELETPROVIDER METHOD");
	}
	
	public ProviderDao(EntityManager em){
		this.em = em;
		this.treatmentDao = new TreatmentDao(em);
	}

}
