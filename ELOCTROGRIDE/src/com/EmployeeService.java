package com;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
//For JSON
import com.google.gson.*;
//For XML 
import org.jsoup.*;
import org.jsoup.parser.*;
import org.jsoup.nodes.Document;

import model.Employee;

@Path("/Employee")
public class EmployeeService {
	Employee registerObj = new Employee();

	@GET
	@Path("/")
	@Produces(MediaType.TEXT_HTML)
	public String readEmployee() {
		return registerObj.readEmployee();
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String insertEmployee(@FormParam("eName") String eName,
			
	 @FormParam("eAddress") String eAddress,
	 @FormParam("eEmail") String eEmail,
	 @FormParam("eDate") String eDate,
	 @FormParam("pno") String pno)
	{
	 String output = registerObj.insertEmployee(eName, eAddress, eEmail, eDate, pno);
	return output;
	}
	
	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCustomer(String employeeData)
	{
	//Convert the input string to a JSON object
	 JsonObject regObject = new JsonParser().parse(employeeData).getAsJsonObject();
	//Read the values from the JSON object
	 String eID = regObject.get("eID").getAsString();
	 String eName = regObject.get("eName").getAsString();
	 String eAddress = regObject.get("eAddress").getAsString();
	 String eEmail = regObject.get("eEmail").getAsString();
	 String eDate = regObject.get("eDate").getAsString();
	 String pno = regObject.get("pno").getAsString();
	 String output = registerObj.updateEmployee(eID, eName, eAddress, eEmail, eDate, pno);
	return output;
	} 
	
	@DELETE
	@Path("/")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteCustomer(String employeeData)
	{
	//Convert the input string to an XML document
	 Document doc = Jsoup.parse(employeeData, "", Parser.xmlParser());

	//Read the value from the element <ID>
	 String eID = doc.select("eID").text();
	 String output = registerObj.deleteEmployee(eID);
	return output;
	}
}
