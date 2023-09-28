package org.java.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		
		final String url = "jdbc:mysql://localhost:3306/db_nations";
		final String user = "root";
		final String password = "root";
		
		try (Connection con = DriverManager.getConnection(url, user, password)) {
			
			Scanner sc = new Scanner(System.in);
			
			System.out.print("Do you want to enter a filter?\nIf yes type it, if no just press 'Enter': ");
			String searchFilter = sc.nextLine();
			
			PreparedStatement sql = con.prepareStatement("SELECT c.name, c.country_id, r.name, c2.name "
					+ "FROM countries c "
					+ "JOIN regions r "
					+ "ON c.region_id = r.region_id "
					+ "JOIN continents c2 "
					+ "ON r.continent_id = c2.continent_id "
					+ "ORDER BY c.name;");
			
			ResultSet res = sql.executeQuery();
			
			while(res.next()) {
				String resRow = res.getString(1) + " | " + res.getInt(2) + " | " + res.getString(3) + " | " + res.getString(4);
				
				if (resRow.toLowerCase().contains(searchFilter.toLowerCase())) {
					System.out.println(resRow);
					System.out.println("----------------------------------------");
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
