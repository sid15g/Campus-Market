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
	
package	settings;
	
 // Setting Up Cloud Server

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import modules.DocumentParser;
import java.net.InetAddress;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import handle.AdminPanel;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import modules.Secure;
import java.io.File;
import data.Share;

	
public class SetupServer	{

	private String domainName, database, username, password;
	public static boolean isCloudSet;
	private File cloudSetting;
	private InetAddress IP;
	private Document doc;

	public SetupServer()	{
	
		cloudSetting = new File("settings/", "cloudSettings.xml");
		
		if( cloudSetting.exists() )		{
		
			isCloudSet = true;
			doc = new DocumentParser(cloudSetting).parse();
			
		}else	{
			doc = null;
			isCloudSet = false;
		}//End Of If Else
		
	}//End Of Constructor
	
	
	public void setCloud()	{

		AdminPanel settings = new AdminPanel();
		Share<InetAddress> variables = settings.getInputs();
	
		setVariables( variables );
		settings.dispose();
	
		buildXMLTree();
		
		Transform createFile = new Transform( doc, cloudSetting );
		createFile.create();

		System.out.println("\n\tServer Setup Succesfull...");
		
	}//End Of Method
	
	
	
	private void setVariables( Share<InetAddress> var )			{
	
		Secure cipher = new Secure();
	
		this.domainName = var.str[0];
		this.IP = var.extra;
		this.database = cipher.encrypt( var.str[1] );
		this.username = cipher.encrypt( var.str[2] );
		this.password = cipher.encrypt( var.str[3] );
	
	}//End Of Method
	
	
	
	private void buildXMLTree()		{
	
		//Creating XML Tree for Cloud Settings
	
		try		{
		
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			this.doc = dBuilder.newDocument();
			
		}catch( Exception e )	{
			e.printStackTrace();
		}//End Of Try Catch	
		
		Text textNodes[] = new Text[8];
		Element settings = doc.createElement("settings");
		
		Element details = doc.createElement("details");
			
			Element domain = doc.createElement("domainName");
			textNodes[0] = doc.createTextNode(domainName);
			domain.appendChild(textNodes[0]);
			details.appendChild(domain);

			Element ipAddr = doc.createElement("serverIP");
			textNodes[1] = doc.createTextNode( IP.getHostAddress() );
			ipAddr.appendChild(textNodes[1]);
			details.appendChild(ipAddr);

			Element databaseName = doc.createElement("database");
			textNodes[2] = doc.createTextNode(database);
			databaseName.appendChild(textNodes[2]);				
			details.appendChild(databaseName);
		
			Element user = doc.createElement("user");
			textNodes[3] = doc.createTextNode(username);
			user.appendChild(textNodes[3]);	
			details.appendChild(user);				
			
			Element pass = doc.createElement("pass");
			textNodes[4] = doc.createTextNode(password);
			pass.appendChild(textNodes[4]);		
			details.appendChild(pass);				
		
		settings.appendChild( details );
		
		doc.appendChild(settings);
	
	}//End Of Method

	
	public File getCloudSettings()		{
	
		return cloudSetting;
	
	}//End Of Method
	
	
}//End Of Class