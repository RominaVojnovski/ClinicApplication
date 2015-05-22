package edu.stevens.cs548.clinic.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.stevens.cs548.clinic.domain.ClinicGateway;
import edu.stevens.cs548.clinic.domain.IClinicGateway;
import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IPatientFactory;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.IProviderFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDao;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDao;
import edu.stevens.cs548.clinic.domain.TreatmentVisitor;

/**
 * Session Bean implementation class TestBean
 */
@Singleton
@LocalBean
@Startup
public class TestBean {
		
	private static Logger logger =	
			Logger.getLogger(TestBean.class.getCanonicalName());
		
	private static void info(String	m)	{
			logger.info(m);
	}
    public TestBean() {
    }
    
    @PersistenceContext(unitName="ClinicDomain")
    private EntityManager	em;
    @PostConstruct
    public void init(){
    
    	info("Initializing	the	user database.");
    
    	IClinicGateway gateway = new ClinicGateway();
    	info("After creation of gateway");
    	IPatientFactory PatientFactory = gateway.getPatientFactory();
    	IPatientDAO	patientDAO	=	new PatientDao(em);
    	IProviderFactory ProviderFactory = gateway.getProviderFactory();
    	IProviderDAO providerDAO = new ProviderDao(em);
    	
    	//creates dob date for patient Romina
    	String oldstring = "1981-08-24";
    	Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(oldstring);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		
		//adds 1st patient Romina to the database with no patient id provided
		//NOTE: DOB and AGE not compliant
		//Will not be persisted to database
		Patient romina=PatientFactory.createPatient("Romina", date,31);
    	info("After FACTORY creation of PATIENT "+romina.getName());
    	
    	
		try {
			long pid = patientDAO.addPatient(romina);
			info("The primary key of PATIENT: "+romina.getName()+" is: "+pid);
			
		} 
		catch (PatientExn e) {
			info("Sorry couldn't add patient to database");
		}
		
		
		//creates dob for patient Vlad
		String oldstring2 = "1985-04-14";
    	Date date2 = null;
		try {
			date2 = new SimpleDateFormat("yyyy-MM-dd").parse(oldstring2);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		//***HW LOGIC 1***adds 2nd patient Vlade to the database with patient id provided
		Patient vlade=PatientFactory.createPatient(1000, "Vlade", date2,29);
    	info("After FACTORY creation of PATIENT "+vlade.getName());
    	
    	
		try {
			long pid2 = patientDAO.addPatient(vlade);
			info("The primary key of Patient: "+vlade.getName()+" is: "+pid2);
			} 
		catch (PatientExn e) {
			info("Sorry couldn't add patient to database");
		}
		
    	
		//creates dob for patient John
				
		String oldstring3 = "1975-06-22";
		Date date3 = null;
		try {
			date3 = new SimpleDateFormat("yyyy-MM-dd").parse(oldstring3);
			}
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		//adds 3rd patient John to the database with patient id provided
		
		Patient john = PatientFactory.createPatient(1001, "John", date3,39);
		info("After FACTORY creation of PATIENT "+john.getName());
		    	
		try {
			long pid3 = patientDAO.addPatient(john);
			info("The primary key of Patient: "+john.getName()+" is: "+pid3);
		} 
		catch (PatientExn e) {
			info("Sorry couldn't add patient to database");
		}
		
		
				
		//adds 4th patient John to the database with patient id provided
		//has same dob as other John
				
		Patient john2 = PatientFactory.createPatient(1005,"John", date3,39);
		info("After FACTORY creation of PATIENT "+john2.getName());
				    	
		try {
			long pid4 = patientDAO.addPatient(john2);
			info("The primary key of Patient: "+john2.getName()+" is: "+pid4);
		} 
		catch (PatientExn e) {
			info("Sorry couldn't add patient to database");
		}
				
    	//***HW LOGIC 2*** retrieves patient aggregate with primary key 1 (Romina) from db
		try {
			info("LOOKUP the patient with primary key 1");
			Patient ret=patientDAO.getPatientByDbId(1);
			info("LOOKUP SUCCESS: The patient with PRIMARY KEY "+ret.getId()+
				" is -> "+ret.getName());
			
		} catch (PatientExn e) {
			info("No user found with that primary key");
		}
		
		//***HW LOGIC 4*** retrieves patient aggregate given patient id 1000 (Vlade) from db
		try {
			Patient ret2=patientDAO.getPatientByPatientId(1000);
			info("LOOKUP SUCCESS: The patient with PATIENT ID "+ret2.getPatientId()+
				" is -> "+ret2.getName());		
		} catch (PatientExn e) {
			info("No user found with that Patient ID");
		}
		
		
		//***HW LOGIC 3*** delete patient aggregate with primary key 3 (John) from db tables
		try {
			patientDAO.deletePatientbyID(3);
			info("SUCCESS DELETION OF PATIENT AGGREGATE");
		} catch (PatientExn e) {
			info("Could not delete patient aggrate");
		}
		
		
		//***HW LOGIC 5***retrieves list of patients with name John and dob 1975-06-22 (date3)
		
		List <Patient> patients= patientDAO.getPatientByNameDob("John", date3);
		for(Patient p: patients){
			info("LOOKUP SUCCESS: found a patient with name John and dob: 1975-06-22");
		}
		
		
		
		
		//***HW LOGIC 6***creating providers
		
		Provider smith1= ProviderFactory.createProvider(200, "Doctor Smith", "Radiology");
		info("Factory creation of Smith provider");
		try {
			providerDAO.addProvider(smith1);
			info("SUCCESFUL INSERTION of provider"+smith1.getName()+" to database");
		} catch (ProviderExn e) {
			info("UNSUCCESFUL INSERTION");
		}
		
		
		
		
		
		Provider smith2= ProviderFactory.createProvider(205, "Doctor Smith", "Surgery");
		info("Factory creation of Smith provider");
		try {
			providerDAO.addProvider(smith2);
			info("SUCCESFUL INSERTION of provider"+smith2.getName()+" to database");
		} catch (ProviderExn e) {
			info("UNSUCCESFUL INSERTION");
		}
		
		//***HW LOGIC 7a***retrieves list of providers with same name(Doctor Smith)
		
		List <Provider> providers= providerDAO.getProviderByName("Doctor Smith");
		for(Provider p: providers){
			info("found a provider with name Doctor Smith");
		}
		
		
		//***HW LOGIC 7b***retrieves the provider with a given provider ID
		Provider ret5;
		try {
			ret5 = providerDAO.getProviderByProviderId(205);
			info("LOOKUP SUCCESS: Found provider with name "+ret5.getName()+
					" and provider id "+ret5.getProviderId());
		} catch (ProviderExn e) {
			info("LOOKUP FAIL: No provider found with given provider id");
		}
		
		//***HW LOGIC 9***add drug treatment for Patient Vlade 
		try {
			Patient v=patientDAO.getPatientByPatientId(1000);
			Provider s=providerDAO.getProviderByProviderId(205);
			long t = s.addDrugTreatment("migraine", "aspirin", 10, v);
			info("DrugTreatment added with treatment primary key "+t);
		} catch (PatientExn e) {
			info("unable to get patient v");
		} catch (ProviderExn e) {
			info("unable to get provider s");
		}
		
		//add radiology treatment for Patient Vlade
		
		String day1 = "2014-10-10";
		String day2 = "2014-10-20";
		
    	Date d1 = null;
    	Date d2 = null;
		
		try {
			d1 = new SimpleDateFormat("yyyy-MM-dd").parse(day1);
			d2 = new SimpleDateFormat("yyyy-MM-dd").parse(day2);
			Patient v=patientDAO.getPatientByPatientId(1000);
			Provider s=providerDAO.getProviderByProviderId(200);
			List<Date> dates = new ArrayList<Date>();
			dates.add(d1);
			dates.add(d2);
			
			long t = s.addRadiologyTreatment("migraine", dates, v);
			info("Radiology Treatment added with treatment primary key "+t);
		} catch (PatientExn e) {
			info("unable to get patient v");
		} catch (ProviderExn e) {
			info("unable to get provider s");
		} catch (ParseException e) {
			info("unable to parse dates");
		}
		
		
		
		
		
		
		
		//***HW LOGIC 8***deletes a provider with provider id 200
		//will leave orphan entries in treatments table with provider null
		
		
		Provider deleteprov;
		try {
			deleteprov = providerDAO.getProviderByProviderId(200);
			providerDAO.deleteProvider(deleteprov);
			info("SUCCESS DELETION of provider with provider ID 200");
			
			
		} catch (ProviderExn e) {
			info("Not able to delete provider with provider id 200");
		}
		
		
		//***HW LOGIC 10a***get list of treatment ids for patient id 1000
		try {
			Patient v=patientDAO.getPatientByPatientId(1000);
			List<Long> treatids = v.getTreatmentIds();
			System.out.println("LOOKUP TREATMENT IDS FOR PATIENT");
			for (int index=0; index < treatids.size(); index++) {
				info("PATIENT : "+v.getName()+" TREATMENT ID: "+treatids.get(index));
	        }
			
		
		} catch (PatientExn e) {
			info("could not find treatment ids for patient 1000");
		}
		
		
		//***HW LOGIC 10b***VISIT TREATMENT USING PATIENT AGGREGATE
		try {
			info("USING VISITOR PATTERN VIA PATIENT AGGREGATE");
			Patient v=patientDAO.getPatientByPatientId(1000);
			TreatmentVisitor visit = new TreatmentVisitor();
			v.visitTreatment(1, visit);
			
		} catch (PatientExn e) {
			info("Patient exception");
		} catch (TreatmentExn e) {
			info("Treatment exception");
		} 
		//***HW LOGIC 11a***get list of treatment ids for provider with id
		try {
			Provider provx =providerDAO.getProviderByProviderId(205);
			List<Long> treatids = provx.getTreatmentIds();
			System.out.println("LOOKUP TREATMENT IDS FOR PROVIDER");
			
			for (int index=0; index < treatids.size(); index++) {
				info("PROVIDER : "+provx.getName()+" TREATMENT ID: "+treatids.get(index)); 
			}
				
		} catch (ProviderExn e) {
			info("could not find treatment ids for provider 205");
		}
		
		//***HW LOGIC 11b***LOOKUP Treatment IDS via Provider given a Patient
		try {
			Provider smith = providerDAO.getProviderByProviderId(205);
			Patient v = patientDAO.getPatientByPatientId(1000);
			List<Long> tids = smith.getTreatmentIdsforPatient(v);
			if(tids != null){
				System.out.println("LOOKUP Treatment IDS via Provider"+ smith.getName());
				for(Long t : tids){
					info("Treatment ID: "+t+" Patient: "+v.getName());
				}
			}
			
		} catch (ProviderExn e) {
			info("Could not find a provider smith");
		} catch (PatientExn e) {
			// TODO Auto-generated catch block
			info("Could not find a patient vlade");
		}

		//***HW LOGIC 11c***VISIT TREATMENT USING PROVIDER AGGREGATE
		//note this one will be an illegal visit 
		//note this one will cause exception because provider has been
		//deleted in HW LOGIC 8
		try {
			info("USING VISITOR PATTERN VIA PROVIDER AGGREGATE");
			Provider provsmith=providerDAO.getProviderByDbId(1);
			TreatmentVisitor radiovisit = new TreatmentVisitor();
			provsmith.visitTreatment(1, radiovisit);
			
		} catch (TreatmentExn e) {
			info("Couldnt visit treatment");
		} catch (ProviderExn e) {
			info("Couldnt get a provider");
		} 
		
		
		//***HW LOGIC 11d***DELETE TREATMENT USING PROVIDER AGGREGATE
		try {
			Provider drsmith=providerDAO.getProviderByProviderId(205);
			long deletetid=1;
			drsmith.deleteTreatment(deletetid);
			info("SUCCESS DELETION OF TREATMENT WITH Treatment ID: "+deletetid);
		} catch (ProviderExn e) {
			info("Unable to retrieve provider with provider id 205");
		} catch (TreatmentExn e) {
			info("Unable to delete treatment using provider aggregate");
		}
		
		
		info("END OF PROGRAM");
    }
}
