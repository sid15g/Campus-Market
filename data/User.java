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
	
package	data;
	
 // Details Of Every User

import java.util.Date;
import errors.UserDetailsException;

public class User	{

	private String username, fullName, email, collegeID, address;
	private int balance, transactions;
	private boolean verified;
	private long mobile;
	private Date dob;

	public User()		{
	
		this.username = this.fullName = this.email = this.collegeID = this.address = null;
		this.mobile = this.balance = this.transactions = 0;
		this.verified = false;
	
		try	{
			throw new UserDetailsException();
		}catch( UserDetailsException e )	{
			System.err.println(e.getMessage());
		}//End Of Try Catch
	
	}//End Of Constructor	
	
	
	public User( String u, String fn, String e, long mob, String a, String cid, Date d, int b , int t )	{
	
		this( u, fn, e, mob, a, cid, d, b, t, false );
	
	}//End Of Constructor	

	
	public User( String u, String fn, String e, long mob, String a, String cid, Date d, int b, int t, boolean v )	{
	
		this.username = u;
		this.fullName = fn;
		this.email = e;
		this.mobile = mob;
		this.address = a;
		this.collegeID = cid;
		this.dob = d;
		this.balance = b;
		this.transactions = t;
		this.verified = v;
	
	}//End Of Constructor


	public User( String u, String fn, String e, long mob, String a, String cid, Date d, int b, int t, int v )	{

		this.username = u;
		this.fullName = fn;
		this.email = e;
		this.mobile = mob;
		this.address = a;
		this.collegeID = cid;
		this.dob = d;
		this.balance = b;
		this.transactions = t;

		if( v == 1 )
			this.verified = true;
		else
			this.verified = false;
		
	}//End Of Constructor
	
	
	public String getUsername()		{
	
		return username;
	
	}//End Of Method
	
	
	public String getName()		{
	
		return fullName;
	
	}//End Of Method	


	public String getEmail()		{
	
		return email;
	
	}//End Of Method	
	
	
	public long getContact()		{
	
		return mobile;
	
	}//End Of Method
	
	
	public String getAddress()		{
	
		return address;
	
	}//End Of Method
	
	
	
	public String getCID()		{
	
		return collegeID;
	
	}//End Of Method	


	public String getDOB()		{
	
		String date = new handle.SqlDate( this.dob ).getDate();
	
		return date;
	
	}//End Of Method	
	

	public int getTransactions()		{
	
		return transactions;
	
	}//End Of Method	
	
	
	public boolean isVerified()		{
	
		return verified;
	
	}//End Of Method
	
	
	public void setEmail( String e )		{
	
		this.email = e;
	
	}//End Of Method	
	
	
	public void setContact( long m )		{
	
		this.mobile = m;
	
	}//End Of Method
	
	
	public void setAddress( String a )		{
	
		this.address = a;
	
	}//End Of Method	
	
	
}//End Of Class