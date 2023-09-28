package org.java.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
			
//			QUERY FOR ALL COUNTRIES
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
				
//				SEARCH FOR FILTER NOT ONLY IN COUNTRY NAME
				if (resRow.toLowerCase().contains(searchFilter.toLowerCase())) {
					System.out.println("\n" + resRow + "\n");
					System.out.println("----------------------------------------");
				}
			}
			
			System.out.print("\n\nEnter the ID of a country you want to know more about: ");
			int idFilter = Integer.valueOf(sc.nextLine());
			
			sc.close();
			
//			QUERY FOR COUNTRY LANGUAGES
			PreparedStatement languages = con.prepareStatement("SELECT c.name, l.`language` "
					+ "FROM countries c "
					+ "JOIN country_languages cl "
					+ "ON c.country_id = cl.country_id "
					+ "JOIN languages l "
					+ "ON cl.language_id = l.language_id "
					+ "WHERE c.country_id = ?;");
			
			languages.setInt(1, idFilter);
			
			res = languages.executeQuery();
			
			int counter = 0;
			String languagesString = "Languages: ";
			
			while(res.next()) {
				if (counter == 0) {
					System.out.println("\n\nDetails for country: " + res.getString(1) + "\n");
					languagesString += res.getString(2);
				} else {
					languagesString += ", " + res.getString(2);
				}
				
				counter++;
			}
			
			System.out.println(languagesString + "\n");
			
//			COUNTRY FOR RECENT STATS
			PreparedStatement stats = con.prepareStatement("SELECT cs.year, cs.population, cs.gdp "
					+ "FROM country_stats cs "
					+ "WHERE cs.country_id = ? "
					+ "ORDER BY cs.year DESC "
					+ "LIMIT 1;");
			
			stats.setInt(1, idFilter);
			
			res = stats.executeQuery();
			
			while(res.next()) {
				System.out.println("Most recent stats\n"
						+ "Year: " + res.getInt(1) + "\n"
						+ "Population: " + res.getLong(2) + "\n"
						+ "GDP: " + res.getLong(3));
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
