package model;

import java.sql.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BillingModel {
	
	//Creating the db connection
	public Connection connect()
	{
		Connection con = null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");

			
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/electrogrid", "root", "");
		}
		catch (Exception e)
		{e.printStackTrace();}
		return con;
	} 
	
	
	public String insertbillingdata(String account_no, String from_d, String to_d, int current_r, String status )
	{
		String output = "";
		try
		{
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for inserting."; 
			}
			
			// create a prepared statement
			String query = " insert into billing values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			double p_amount = this.getPreviousAmount(account_no, status);
			String querynew = "update billing set status = 'Cancel'  where Account_No = ?";
			PreparedStatement preparedStmt1 = con.prepareStatement(querynew);
			preparedStmt1 .setString(1, account_no);
			
			preparedStmt1.execute();
			
			String name = this.getuserdetailsname(account_no);
			String address = this.getuserdetailsaddress(account_no);
			
			int previous_r = this.getpreviousreading(account_no, status);
			
			int units = this.calculateUnits(previous_r,current_r);
			double c_amount = this.calculateCurrentAmount(units);
			
			double t_amount = this.calculateTotalAmount(c_amount,p_amount);
			
			
			
			// binding values
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, account_no);
			preparedStmt.setString(3, name);
			preparedStmt.setString(4, address);
			preparedStmt.setString(5, from_d);
			preparedStmt.setInt(6, previous_r);
			preparedStmt.setString(7, to_d);
			preparedStmt.setInt(8, current_r);
			preparedStmt.setInt(9, units);
			preparedStmt.setDouble(10,  c_amount);
			preparedStmt.setDouble(11, p_amount);
			preparedStmt.setDouble(12,  t_amount);
			preparedStmt.setString(13, status);

			// execute the statement
			preparedStmt.execute();
			con.close();
			output = "Billing data added successfully";
		}
		catch (Exception e)
		{
			output = "Error occur during inserting\n";
			System.err.println(e.getMessage());
		}
		return output;
	}



	public double getPreviousAmount(String account_no, String status) {
		 
		double p_amount=0;
		 
		try {
			
			Connection con = connect();
			
			String getQuery = "select Total_amount\n"
								+ "from billing\n"
								+ "where Account_No = ? and Status='Pending'; ";
			
			PreparedStatement pstmt = con.prepareStatement(getQuery);
			pstmt.setString(1, account_no);

			double Total_a= 0;
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				
				Total_a = rs.getDouble("Total_amount");
				
			}
			con.close();
			
			 
			p_amount = Total_a;
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return p_amount;
	}


	public int getpreviousreading(String account_no, String status) {
		 
		int previous_r=0;
		 
		try {
			
			Connection con = connect();
			
			String getQuery = "select Current_Reading\n"
					+ "from billing\n"
					+ "where Account_No = ? and Status='Cancel'  ; ";
			
			PreparedStatement pstmt = con.prepareStatement(getQuery);
			pstmt.setString(1, account_no);

			int Current_R= 0;
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				
				Current_R = rs.getInt("Current_Reading");
				
			}
			con.close();
			
			previous_r = Current_R;
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return previous_r;
	}


	public String getuserdetailsaddress(String account_no) {
		 
		 String address="";
		 
		try {
			
			Connection con = connect();
			
			String getQuery = "select u.Address\n"
							+ "from user u\n"
							+ "where u.accountNo = ?; ";
			
			PreparedStatement pstmt = con.prepareStatement(getQuery);
			pstmt.setString(1, account_no);

			String getaddress ="";
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				getaddress = rs.getString("Address");
				
			}
			con.close();

			address = getaddress;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return address;
	}


	public String getuserdetailsname(String account_no) {
		 
		String name="";
		  
		try {
			
			Connection con = connect();
			
			String getQuery = "select u.Name \n"
							+ "from user u\n"
							+ "where u.accountNo = ?; ";
			
			PreparedStatement pstmt = con.prepareStatement(getQuery);
			pstmt.setString(1, account_no);
			
			String getname ="";
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				
				getname = rs.getString("Name");
	
			}
			con.close();
			
			name = getname;

			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return name;
	}


	public int calculateUnits(int previous_r, int current_r) {
		
		 int units = 0;
		 
		 //calculation
		 units = current_r - previous_r;
		
		 return units;
	}

	
	public double calculateCurrentAmount(int units) {
		
		double c_amount = 0;
		
		if(units<=60) {
			c_amount = (double) (units * 7.85);
		}else if(units>60 && units<=90){
			c_amount = (double) ((double) (60 * 7.85) + (units - 60) * 10.00);
		}else if(units>90 && units<=120){
			c_amount = (double) ((double) (60 * 7.85) + (30 * 10.00) + (units - 90) * 27.75);
		}else if(units>120 && units<=180){
			c_amount = (double) ((double) (60 * 7.85) + (30 * 10.00) + (30 * 27.75) + (units - 120) * 32.75);
		}else {
			c_amount = (double) ((double) (60 * 7.85) + (30 * 10.00) + (30 * 27.75) + (60 * 32.75) + (units - 180) * 45.00);
		}
		
		return c_amount;
	}


	public double calculateTotalAmount(double c_amount, double p_amount) {
		
		double t_amount = 0;
		
		t_amount = c_amount + p_amount;
		
		return t_amount;
	}


	public String readBilingDetails() {
		
		String output = "";
		
		try {
			
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for reading."; 
			}
			
			// Prepare the html table to be displayed
			output = "<table border='1' style=\"font-family: Arial, Helvetica, sans-serif; border-collapse: collapse; width: 100%; radius: 10px\">"
					+ "<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\">Bill ID</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Account No</th>" 
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Name</th>" 
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Address</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>From Date</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Previous Meter Reading</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>To date</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Current Meter Reading</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>No of Units Consumed</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Charge for electricity consumed</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Total amount according to the previous amount</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Total Amount</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Status (Pending / Done)</th></tr>";
			
			
			String query = "select * from billing";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			 // iterate through the rows in the result set
			 while (rs.next())
			 {
				 int ID= rs.getInt("ID");
				 String Account_No = rs.getString("Account_No");
				 String Name = rs.getString("Name");
				 String Address = rs.getString("Address");
				 Date From_Date = rs.getDate("From_Date");
				 int Previous_Reading = rs.getInt("Previous_Reading");
				 Date To_Date = rs.getDate("To_Date");
				 int Current_Reading = rs.getInt("Current_Reading");
				 int Units = rs.getInt("Units");
				 Double Current_amount = rs.getDouble("Current_amount");
				 Double Previous_amount = rs.getDouble("Previous_amount");
				 Double Total_amount = rs.getDouble("Total_amount");
				 String Status = rs.getString("Status");
				 
				 // Add a row into the html table
				 output += "<tr style=\"border: 1px solid #ddd; padding: 8px;\"><td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + ID + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Account_No + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Name + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Address + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + From_Date + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Previous_Reading + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + To_Date + "</td>"; 
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Current_Reading + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Units + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Current_amount + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Previous_amount + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Total_amount + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Status + "</td>";
				 // buttons
				 output += "<input name='ID' type='hidden' "
				 + " value='" + ID + "'>"
				 + "</form></td></tr>";
			 }
			 con.close();
			 // Complete the html table
			 output += "</table>";
			
		}catch(Exception e) {
			
			output = "Error while reading the bill betails of users";
			System.err.println(e.getMessage());
		}
		
		return output;
	}
	
	public String getuserBilingDetails(String account_no) {
		
		String output = "";
		
		try {
			
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for reading."; 
			}
			
			// Prepare the html table to be displayed
			output = "<table border='1' style=\"font-family: Arial, Helvetica, sans-serif; border-collapse: collapse; width: 100%; radius: 10px\">"
					+ "<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\">Bill ID</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Account No</th>" 
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Name</th>" 
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Address</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>From Date</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Previous Meter Reading</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>To date</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Current Meter Reading</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>No of Units Consumed</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Charge for electricity consumed</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Total amount according to the previous amount</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Total Amount</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Status (Pending / Done)</th></tr>";
			
			String query = "select * from billing where Account_No='"+account_no+"'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			 // iterate through the rows in the result set
			 while (rs.next())
			 {
				 String ID= Integer.toString(rs.getInt("ID"));
				 String Account_No = rs.getString("Account_No");
				 String Name = rs.getString("Name");
				 String Address = rs.getString("Address");
				 Date From_Date = rs.getDate("From_Date");
				 int Previous_Reading = rs.getInt("Previous_Reading");
				 Date To_Date = rs.getDate("To_Date");
				 int Current_Reading = rs.getInt("Current_Reading");
				 int Units = rs.getInt("Units");
				 Double Current_amount = rs.getDouble("Current_amount");
				 Double Previous_amount = rs.getDouble("Previous_amount");
				 Double Total_amount = rs.getDouble("Total_amount");
				 String Status = rs.getString("Status");
				 
				 // Add a row into the html table
				 output += "<tr style=\"border: 1px solid #ddd; padding: 8px;\"><td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + ID + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Account_No + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Name + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Address + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + From_Date + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Previous_Reading + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + To_Date + "</td>"; 
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Current_Reading + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Units + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Current_amount + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Previous_amount + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Total_amount + "</td>";
				 // buttons
				 
			 }
			 con.close();
			 // Complete the html table
			 output += "</table>";
			
		}catch(Exception e) {
			
			output = "Error while reading the bill details of users";
			System.err.println(e.getMessage());
		}
		
		return output;
		
	}
	
public String getuserBilingDetailsbyid(String id) {
		
		String output = "";
		
		try {
			
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for reading."; 
			}
			
			// Prepare the html table to be displayed
			output = "<table border='1' style=\"font-family: Arial, Helvetica, sans-serif; border-collapse: collapse; width: 100%; radius: 10px\">"
					+ "<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\">Bill ID</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Account No</th>" 
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Name</th>" 
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Address</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>From Date</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Previous Meter Reading</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>To date</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Current Meter Reading</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>No of Units Consumed</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Charge for electricity consumed</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Total amount according to the previous amount</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Total Amount</th>"
					+"<th style=\"padding-top: 12px; padding-bottom: 12px; text-align: left; background-color: #04AA6D; color: white;\"th>Status (Pending / Done)</th></tr>";
			
			String query = "select * from billing where ID='"+id+"'";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			 // iterate through the rows in the result set
			 while (rs.next())
			 {
				 String ID= Integer.toString(rs.getInt("ID"));
				 String Account_No = rs.getString("Account_No");
				 String Name = rs.getString("Name");
				 String Address = rs.getString("Address");
				 Date From_Date = rs.getDate("From_Date");
				 int Previous_Reading = rs.getInt("Previous_Reading");
				 Date To_Date = rs.getDate("To_Date");
				 int Current_Reading = rs.getInt("Current_Reading");
				 int Units = rs.getInt("Units");
				 Double Current_amount = rs.getDouble("Current_amount");
				 Double Previous_amount = rs.getDouble("Previous_amount");
				 Double Total_amount = rs.getDouble("Total_amount");
				 String Status = rs.getString("Status");
				 
				 // Add a row into the html table
				 output += "<tr style=\"border: 1px solid #ddd; padding: 8px;\"><td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + ID + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Account_No + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Name + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Address + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + From_Date + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Previous_Reading + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + To_Date + "</td>"; 
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Current_Reading + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Units + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Current_amount + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Previous_amount + "</td>";
				 output += "<td style=\"padding-top: 6px; padding-bottom: 6px; text-align: center; color: #3B3B3B;\">" + Total_amount + "</td>";
				 // buttons
				 
			 }
			 con.close();
			 // Complete the html table
			 output += "</table>";
			
		}catch(Exception e) {
			
			output = "Error while reading the bill details of users";
			System.err.println(e.getMessage());
		}
		
		return output;
		
	}
	
	public String updateBillDetails(String id,String account_no, String from_Date, String to_Date, String current_Reading, String status ) {
		
		String output = "";
		
		try {
			
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for updating."; 
			}
			
			
			
			// create a prepared statement
			String query = "UPDATE billing SET Account_No=?,Name=?,Address=?,From_Date=?,Previous_reading=?,To_Date=?,Current_Reading=?,Units=?,Current_amount=?,Previous_amount=?,Total_amount=?,Status=? where ID=?  ";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			String querynew = "update billing set status = 'Cancel'  where Account_No = ?";
			PreparedStatement preparedStmt1 = con.prepareStatement(querynew);
			preparedStmt1 .setString(1, account_no);
			
			double p_amount = this.getPreviousAmount(account_no, status);
			
			String name = this.getuserdetailsname(account_no);
			String address = this.getuserdetailsaddress(account_no);
			
			int previous_r = this.getpreviousreading(account_no, status);
			
			int units = this.calculateUnits(previous_r,Integer.parseInt(current_Reading));
			double c_amount = this.calculateCurrentAmount(units);
			
			double t_amount = this.calculateTotalAmount(c_amount,p_amount);
			
			// binding values
			preparedStmt.setString(1, account_no); 
			preparedStmt.setString(2, name); 
			preparedStmt.setString(3, address); 
			preparedStmt.setString(4, from_Date);
			preparedStmt.setInt(5, previous_r);
			preparedStmt.setString(6, to_Date);
			preparedStmt.setInt(7, Integer.parseInt(current_Reading));
			preparedStmt.setInt(8, units);
			preparedStmt.setDouble(9,  c_amount);
			preparedStmt.setDouble(10, p_amount);
			preparedStmt.setDouble(11,  t_amount);
			preparedStmt.setString(12, status);
			preparedStmt.setString(13, id); 
			
			
			// execute the statement
			preparedStmt.execute();
			con.close();
			output = "Updated successfully";
			   
		}catch(Exception e) {
			
			output = "Error while updating the billing details.";
			System.err.println(e.getMessage());
		}
		
		return output;
	}
	
	public String deletebill(String Account_No){
		
		String output = "";
		try
		{
			Connection con = connect();
			if (con == null)
			{
				return "Error while connecting to the database for deleting."; 
			}
			// create a prepared statement
			String query = "delete from billing where Account_No=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			
			// binding values
			preparedStmt.setString(1, Account_No);
			
			// execute the statement
			preparedStmt.execute();
			con.close();
			output = "Deleted successfully";
			
		}
		catch (Exception e)
		{
			output = "Error while deleting the bill";
			System.err.println(e.getMessage());
		}
		
		return output;
		
	}
	
 
}
