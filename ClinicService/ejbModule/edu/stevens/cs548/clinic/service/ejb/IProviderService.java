package edu.stevens.cs548.clinic.service.ejb;

import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDTO;

public interface IProviderService {
	public class ProviderServiceExn extends Exception{
	
		private static final long serialVersionUID = 1L;

		public ProviderServiceExn(String m){
			super(m);
		}
		
	}
	public class ProviderNotFoundExn extends Exception{
		
		private static final long serialVersionUID = 1L;

		public ProviderNotFoundExn(String m){
			super(m);
		}
		
	}
	public class TreatmentNotFoundExn extends Exception{
		
		private static final long serialVersionUID = 1L;

		public TreatmentNotFoundExn(String m){
			super(m);
		}
		
	}
	
	public void createProvider(String name, long providerId) throws ProviderServiceExn;
	
	public ProviderDTO getProviderByDbId(long id) throws ProviderServiceExn;
	
	public ProviderDTO getProviderByProviderId(long id) throws ProviderServiceExn;
	
	public ProviderDTO[] getProvidersByName(String name);
	
	public void deleteProvider(String name, long id) throws ProviderServiceExn;
	
	public void addTreatment(long patid, long provid, TreatmentDto t) throws ProviderServiceExn, ProviderNotFoundExn, TreatmentNotFoundExn;
	
	
	public TreatmentDto[] getTreaments(long id) throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;

	public TreatmentDto[] getTreatments(long id, long patid) throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;
	
	public void deleteTreatment(long patid, long provid, long tid) throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn;

}
