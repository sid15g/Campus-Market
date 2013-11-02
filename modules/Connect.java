/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it.
 * This code is distributed in the hope that it will be useful.
 *
 * Developed By Students Of <The LNM Institute Of Information Technology, Jaipur>.
 * ---Shivam Agarwal
 * ---Shubham Bansal
 * ---Siddhant Goenka
 * ---Vijaya Choudhary
 * ---Vinod Choudhary
 *
 */
	
package	modules;
	
 // Creates connection to MySQL Server

import java.sql.DriverManager; 
import org.w3c.dom.Document;
import settings.SetupServer;
import java.sql.Connection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.sql.Driver;
import data.appData;
import java.io.File;
//import java.lang.Class;

	
public class Connect 			{

	private File cloudSetting;
	private static String serverIP;
	private static Connection connect;
	private String url, database, username, password;
	
	public Connect()		{
		
		SetupServer setup = new SetupServer();
		cloudSetting = setup.getCloudSettings();

		try		{
		
			//<Returns the Class object associated with the class or interface>
			Driver mySQLDriver = (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance();
			DriverManager.registerDriver(mySQLDriver);
			//JDBC Driver Registered.
	
			this.fetchCloudDetails();
			this.connect = DriverManager.getConnection(url,username,password);
			
		}catch( Exception e )	{							//ClassNotFoundException & InstantiationException & SQLException
			System.err.println("ERROR: Connecting to Server");
			System.exit(0);
		}//End Of Try-Catch
	
	}//End Of Constructor
	
	
	private void fetchCloudDetails()		{
	
		DocumentParser dp = new DocumentParser( cloudSetting );
		Document doc;
		
		Secure cipher = new Secure();
		doc = dp.parse();
		
		database = dp.getChildByTagName("database").getTextContent();
		database = cipher.decrypt( database );
		
		serverIP = dp.getChildByTagName("serverIP").getTextContent();
		
		url = "jdbc:mysql://"+serverIP+"/"+database;	
		
		username = dp.getChildByTagName("user").getTextContent();
		username = cipher.decrypt( username );
		
		password = dp.getChildByTagName("pass").getTextContent();
		password = cipher.decrypt( password );
	
	}//End Of Method
	
	
	public static Connection getConnection()		{
	
		return connect;
	
	}//End Of Method
	
	
	public static String getServerIP()		{
	
		return serverIP;
	
	}//End Of Method	
	
}//End Of Class