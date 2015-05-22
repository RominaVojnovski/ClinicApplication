(: XQuery main module :)
import schema namespace clin = "http://www.example.org/schemas/clinic" at "Clinic.xsd";
import schema namespace pat = "http://www.example.org/schemas/clinic/patient" at "Patient.xsd";
let $clinic := doc("ClinicData.xml")
return $clinic/clin:Clinic