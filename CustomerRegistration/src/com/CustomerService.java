package com;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
//For JSON
import com.google.gson.*;
//For XML 
import org.jsoup.*;
import org.jsoup.parser.*;
import org.jsoup.nodes.Document;

import model.Customer;

@Path("/Customer")
public class CustomerService {
	Customer registerObj = new Customer();

	@GET
	@Path("/")
	@Produces(MediaType.TEXT_HTML)
	public String readCustomer() {
		return registerObj.readCustomer();
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String insertCustomer(@FormParam("cName") String cName,
			
	 @FormParam("cAddress") String cAddress,
	 @FormParam("cEmail") String cEmail,
	 @FormParam("cDate") String cDate,
	 @FormParam("pno") String pno)
	{
	 String output = registerObj.insertCustomer(cName, cAddress, cEmail, cDate, pno);
	return output;
	}
	
	@PUT
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String updateCustomer(String customerData)
	{
	//Convert the input string to a JSON object
	 JsonObject regObject = new JsonParser().parse(customerData).getAsJsonObject();
	//Read the values from the JSON object
	 String cID = regObject.get("cID").getAsString();
	 String cName = regObject.get("cName").getAsString();
	 String cAddress = regObject.get("cAddress").getAsString();
	 String cEmail = regObject.get("cEmail").getAsString();
	 String cDate = regObject.get("cDate").getAsString();
	 String pno = regObject.get("pno").getAsString();
	 String output = registerObj.updateCustomer(cID, cName, cAddress, cEmail, cDate, pno);
	return output;
	} 
	
	@DELETE
	@Path("/")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteCustomer(String customerData)
	{
	//Convert the input string to an XML document
	 Document doc = Jsoup.parse(customerData, "", Parser.xmlParser());

	//Read the value from the element <ID>
	 String cID = doc.select("cID").text();
	 String output = registerObj.deleteCustomer(cID);
	return output;
	}
}
