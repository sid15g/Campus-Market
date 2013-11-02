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
	
 // User to Parse XML Document	
	
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import errors.DocumentParsingException;	
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.io.File;
	
public class DocumentParser		{
	
	private File XMLFile;
	private Document doc;
	private DocumentBuilder dBuilder;

	public DocumentParser()		{
	
		try		{
			throw new DocumentParsingException();
		}catch( Exception e )	{
			System.err.println(e.getMessage());
		}//End Of Try Catch
	
	}//End Of Constructor


	public DocumentParser( File f )		{
	
		XMLFile = f;
		
		try		{
		
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();			
			
		}catch( Exception e )	{
			System.err.println(e.getMessage());
		}//End Of Try-Catch	
	
	}//End Of Constructor	
	
	
	public Document parse()		{
	
		try		{
			doc = dBuilder.parse(XMLFile);
		}catch( Exception e )	{
			System.err.println(e.getMessage());
		}//End Of Try Catch
		
		return doc;
	
	}//End Of Method
	
	
	public Node getChildByTagName( String name )		{
	
		Element root;
		NodeList childs, subchilds;
		Node n, returnNode;
	
		doc = parse();
		root = doc.getDocumentElement();
		
		returnNode = n = (Node)root;
		
		if( name.equals( root.getNodeName() ) )			{
			returnNode = (Node)root;
		}else if( n.hasChildNodes() )			{
		
			childs = n.getChildNodes();
				
			for( int i=0; i< childs.getLength(); i++ )		{
			
				n = childs.item(i);
				
				if( name.equals( n.getNodeName() ) )		{
					returnNode = n;
					break;
				}else if( n.hasChildNodes() )	{
					
					subchilds = n.getChildNodes();
					
					for( int j=0; j< subchilds.getLength(); j++ )		{
					
						n = subchilds.item(j);
						if( name.equals( n.getNodeName() ) )	{
							returnNode = n;
							break;
						}
					}//End Of Loop(j)
				
				}//End Of If-Else
				
			}//End Of Loop	
			
		}//End Of If-Else
	
		return returnNode;
	
	}//End Of Method
	
	
}//End Of Class