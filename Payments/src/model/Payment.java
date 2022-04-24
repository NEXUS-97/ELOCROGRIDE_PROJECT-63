package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Payment {

	public Connection connect() {
		Connection con = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/electrogrid", "root", "");
			// For testing
			System.out.print("Successfully connected");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return con;
	}

	// insert method
	public String insertPayment(String date, String no, String name, String type, String amount) {
		Connection con = connect();
		String output = "";
		if (con == null) {
			return "Error while connecting to the database";

		}

		// create a prepared statement
		String query = " insert into payments (`PaymentID`,`PaymentDate`,`AccountNo`,`PaymentType`,`Amount`)"
				+ " values (?, ?, ?, ?, ?)";

		PreparedStatement preparedStmt;
		try {
			preparedStmt = con.prepareStatement(query);

			preparedStmt.setInt(1, 0);
			preparedStmt.setString(2, date);
			preparedStmt.setString(3, name);
			preparedStmt.setString(4, no);
			preparedStmt.setString(5, type);
			preparedStmt.setString(6, amount);

			preparedStmt.execute();
			con.close();
			output = "Inserted successfully";
		} catch (SQLException e) {
			output = "Error while inserting";
			System.err.println(e.getMessage());
		}

		return output;
	}

	// read method
	public String readPayments() {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for reading.";
			}
			// Prepare the html table to be displayed
			output = "<table border='1'><tr><th>Payment Date</th><th>CustomerName</th>"+"<th>AccountNo</th>" + "<th>Payment Type</th>"+ "<th>Amount</th></tr>" ;

			String query = "select * from payments";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			// iterate through the rows in the result set
			while (rs.next()) {
				String PaymentID = Integer.toString(rs.getInt("PaymentID"));
				String PaymentDate = rs.getString("PaymentDate");
				String CustomerName = rs.getString("CustomerName");
				String AccountNo = rs.getString("AccountNo");
				String PaymentType = rs.getString("PaymentType");
				String Amount = rs.getString("Amount");

				// Add into the html table
				output += "<tr><td>" + PaymentDate + "</td>";
				output += "<td>" + CustomerName + "</td>";
				output += "<td>" + AccountNo + "</td>";
				output += "<td>" + PaymentType + "</td>";
				output += "<td>" + Amount + "</td>";

			}
			con.close();

			// Complete the html table
			output += "</table>";
		} catch (Exception e) {
			output = "Error while reading the payment Details.";
			System.err.println(e.getMessage());
		}
		return output;
	}

	// update method
	public String updatePayment(String ID, String date, String name, String no, String type, String amount)

	{
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for updating.";
			}

			String query = " update payments set PaymentDate= ? , CustomerName = ? , AccountNo = ?  , PaymentType = ? , Amount = ? where PaymentID = ? ";

			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setString(1, date);
			preparedStmt.setString(2, name);
			preparedStmt.setString(3, no);
			preparedStmt.setString(4, type);
			preparedStmt.setString(5, amount);
			preparedStmt.setInt(6, Integer.parseInt(ID));

			// execute the statement
			preparedStmt.execute();
			con.close();
			output = "Updated successfully";
		} catch (Exception e) {
			output = "Error while updating the Payment Details.";
			System.err.println(e.getMessage());
		}
		return output;
	}

	// delete method
	public String deletePayment(String PaymentID) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for deleting.";
			}
			// create a prepared statement
			String query = "delete from payments where PaymentID=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);

			// binding values
			preparedStmt.setInt(1, Integer.parseInt(PaymentID));

			// execute the statement
			preparedStmt.execute();
			con.close();
			output = "Deleted successfully";
		} catch (Exception e) {
			output = "Error while deleting the payment Details.";
			System.err.println(e.getMessage());
		}
		return output;
	}
}
