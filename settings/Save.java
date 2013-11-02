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
	
 // Registers the user permamantly and check for it;
	
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import errors.NetConnectionException;
import modules.DocumentParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import modules.Secure;
import java.io.File;
	
public class Save	{

	private Document doc;
	private File registeredUser;
	
	public static boolean isLoginSaved = false;

	public Save()	{
	
		registeredUser = new File( "settings/", "accountDetails.xml" );
		
		if( registeredUser.exists() )		{
			isLoginSaved = true;
			doc = null;
		}else	{
			isLoginSaved = false;
			doc = null;
		}//End Of If Else	
	
	}//End Of Constructor
	
	
	
	
	public void registerUser( String username, String password )		{
	
		Secure cipher = new Secure();
		username = cipher.encrypt( username );
		password = cipher.encrypt( password );
	
		buildXMLTree( username, password );
		
		Transform createFile = new Transform( doc, registeredUser );
		createFile.create();
	
	}//End Of Method
	
	
	
	private void buildXMLTree( String u, String p )		{
	
		//Creating XML Tree for Saving Login Account
		try		{
		
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			this.doc = dBuilder.newDocument();
			
		}catch( Exception e )	{
			e.printStackTrace();
		}//End Of Try Catch		
	
		Text textNodes[] = new Text[2];
		Element root = doc.createElement("registered");
		
			Element user = doc.createElement("user");
			
				Element username = doc.createElement("username");
				textNodes[0] = doc.createTextNode(u);
				username.appendChild( textNodes[0] );
				
				Element password = doc.createElement("password");
				textNodes[1] = doc.createTextNode(p);
				password.appendChild( textNodes[1] );

			user.appendChild( username );				
			user.appendChild( password );
		
		root.appendChild( user );
		
		doc.appendChild( root );
	
	}//End Of Method	
	
	
	
	private String getDetails( String nodeName )		{
	
		DocumentParser readFile = new DocumentParser( registeredUser );
		this.doc = readFile.parse();
		
		String str = readFile.getChildByTagName( nodeName ).getTextContent();
		
		return str;
		
	}//End Of Method	
	
	
	
	public String getUsername()		{
	
		return getDetails("username");
		
	}//End Of Method	



	public String getPassword()		{
	
		return getDetails("password");
		
	}//End Of Method	
	
	
	public File getSavedDetails()		{
	
		return this.registeredUser;
	
	}//End Of Method
	
	
}//End Of Class