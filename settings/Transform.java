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
	
 // Document to File Transformer
 
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import org.w3c.dom.Document;
import java.io.File;

	
public class Transform	{

	private Document doc;
	private File file;
	
	public Transform( Document d, File f )		{

		this.doc = d;
		this.file = f;

	}//End Of Constructor
	
	
	public void create()			{

		try		{
		
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer trans = tFactory.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");		
		
			//Source Of XML Tree
			DOMSource source = new DOMSource(doc);
			//Stream for XML to write all data to file
			StreamResult result = new StreamResult(file);
			trans.transform(source, result);
			
		}catch( Exception tce )		{						//TransformerConfigurationException
			tce.printStackTrace();
		}//End Of Try-Catch		

	}//End Of Method
	
	
}//End Of Class