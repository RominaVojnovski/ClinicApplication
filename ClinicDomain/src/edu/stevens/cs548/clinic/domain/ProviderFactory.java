package edu.stevens.cs548.clinic.domain;

public class ProviderFactory implements IProviderFactory {
	@Override
	public Provider createProvider(long pid, String name, String specialization) {
		Provider p = new Provider();
		p.setProviderId(pid);
		p.setName(name);
		p.setSpecialization(specialization);
		return p;
	}

	@Override
	public Provider createProvider(long pid, String name) {
		Provider p = new Provider();
		p.setProviderId(pid);
		p.setName(name);
		return p;
	}


}
