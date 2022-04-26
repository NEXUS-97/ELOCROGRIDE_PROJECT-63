package model;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Employee {

	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Provide the correct details: DBServer/DBName, username, password
			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/power?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
					"root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public String insertEmployee(String eName, String eAddress, String eEmail, String eDate, String pno) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for inserting.";
			}
			// create a prepared statement
			String query = " insert into employee(`eID`,`eName`,`eAddress`,`eEmail`,`eDate`,`pno`)"
					+ " values (?, ?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, eName);
			preparedStmt.setString(3, eAddress);
			preparedStmt.setString(4, eEmail);
			preparedStmt.setString(5, eDate);
			preparedStmt.setString(6, pno);
			// execute the statement
			preparedStmt.execute();
			con.close();
			output = "Inserted successfully";
		} catch (Exception e) {
			output = "Error while inserting the Employee.";
			System.err.println(e.getMessage());
		}
		return output;
	}

	public String readEmployee() {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for reading.";
			}
			// Prepare the html table to be displayed
			output = "<table border=\"1\"><tr><th>Employee ID</th><th>Employee Name</th><th>Address</th><th>Email</th><th>Date</th><th>Phone No</th></tr>";
			String query = "select * from employee";
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);
			// iterate through the rows in the result set
			while (rs.next()) {
				String eID = Integer.toString(rs.getInt("eID"));
				String eName = rs.getString("eName");
				String eAddress = rs.getString("eAddress");
				String eEmail = rs.getString("eEmail");
				String eDate = rs.getString("eDate");
				String pno = rs.getString("pno");

				// Add into the html table
				output += "<tr><td>" + eID + "</td>";
				output += "<td>" + eName + "</td>";
				output += "<td>" + eAddress + "</td>";
				output += "<td>" + eEmail + "</td>";
				output += "<td>" + eDate + "</td>";
				output += "<td>" + pno + "</td>";
				
			}
			con.close();
			// Complete the html table
			output += "</table>";
		} catch (Exception e) {
			output = "Error while reading the employee.";
			System.err.println(e.getMessage());
		}
		return output;
	}

	public String updateEmployee(String eID, String eName, String eAddress, String eEmail, String eDate, String pno) {
		String output = "";

		try {
			Connection con = connect();

			if (con == null) {
				return "Error while connecting to the database for updating.";
			}

			// create a prepared statement
			String query = "UPDATE employee SET eName=?,eAddress=?,eEmail=?,eDate=?,pno=?" + "WHERE eID=?";

			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setString(1, eName);
			preparedStmt.setString(2, eAddress);
			preparedStmt.setString(3, eEmail);
			preparedStmt.setString(4, eDate);
			preparedStmt.setString(5, pno);
			preparedStmt.setInt(6, Integer.parseInt(eID));

			// execute the statement
			preparedStmt.execute();
			con.close();

			output = "Updated successfully";
		} catch (Exception e) {
			output = "Error while updating the employee.";
			System.err.println(e.getMessage());
		}

		return output;
	}

	public String deleteEmployee(String eID) {
		String output = "";

		try {
			Connection con = connect();

			if (con == null) {
				return "Error while connecting to the database for deleting.";
			}

			// create a prepared statement
			String query = "delete from employee where eID=?";

			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setInt(1, Integer.parseInt(eID));

			// execute the statement
			preparedStmt.execute();
			con.close();

			output = "Deleted successfully";
		} catch (Exception e) {
			output = "Error while deleting the employee.";
			System.err.println(e.getMessage());
		}

		return output;
	}

}
