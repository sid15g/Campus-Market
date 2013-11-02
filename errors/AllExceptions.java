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
	
 // Super Class Of All My Exceptions
	
public class AllExceptions extends Exception	{

	public static final long serialVersionUID = 7592299979L;
	
	public AllExceptions( String str )	{
	
		super(str);
	
	}//End Of Constructor
	
}//End Of Class