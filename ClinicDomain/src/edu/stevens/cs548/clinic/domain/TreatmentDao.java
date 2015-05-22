package edu.stevens.cs548.clinic.domain;

import javax.persistence.EntityManager;

public class TreatmentDao implements ITreatmentDAO {

	public TreatmentDao(EntityManager em){
		this.em=em;
	}
	
	
	private EntityManager em;


	public Treatment getTreatmentByDbId(long id) throws TreatmentExn {
		Treatment t = em.find(Treatment.class, id);
		if(t == null){
			throw new TreatmentExn("Treatment not found primary key = "+id);
		}
		else{
			return t;
		}
	}

	@Override
	public long addTreatment(Treatment t) {
	
		em.persist(t);
	
		em.flush();
		long retval = t.getId();
		return retval;
	}
	
	@Override
	public void deleteTreatment(Treatment t){
		
		em.remove(t);
		
		em.flush();
	}

	
}
