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
 
package errors; 
	
 // Throws and Exception for wrong user detail input 
	
public class NetConnectionException extends AllExceptions		{

	public NetConnectionException()	{
	
		super("ERROR: Connecting to Internet/Server");
		//System.exit(0);
		
	}//End Of Constructor
	
}//End Of Class