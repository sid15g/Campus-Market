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
	
 // Details Of Every Post

import java.util.Date;
import errors.PostDetailsException;

public class Post	{

	private BidPost bid;
	private int pid, amount;
	private Date start, end;
	private String username, type, title, details, status, category;

	public Post()		{
	
		this.username = this.type = this.title = this.details = this.category = null;
		this.start = this.end = new Date();
		this.pid = this.amount = 0;
		this.bid = null;
	
		try	{
			throw new PostDetailsException();
		}catch( PostDetailsException e )	{
			System.err.println(e.getMessage());
		}//End Of Try Catch
	
	}//End Of Constructor	


	public Post( int id, String t, String u, Date s, Date e, String tl, String d, int a, String st, String cat, int i, int r )	{

		this.pid = id;
		this.type = t;
		this.username = u;
		this.start = s;
		this.end = e;
		this.title = tl;
		this.details = d;
		this.amount = a;
		this.status = st;
		this.category = cat;
		this.bid = new BidPost( i, r );
	
	}//End Of Constructor	
	
	
	public Post( int id, String t, String u, Date s, Date e, String tl, String d, int a, String st, String cat )	{

		this.pid = id;
		this.type = t;
		this.username = u;
		this.start = s;
		this.end = e;
		this.title = tl;
		this.details = d;
		this.amount = a;
		this.status = st;
		this.category = cat;
		this.bid = null;
	
	}//End Of Constructor
	


	public Post( int id, String t, String u, Date s, Date dl, String tl, String d, String st, String cat )	{

		this.pid = id;
		this.type = t;
		this.username = u;
		this.start = s;
		this.end = dl;
		this.title = tl;
		this.details = d;
		this.status = st;
		this.category = cat;
		
		this.amount = 0;
		this.bid = null;
	
	}//End Of Constructor	

	
	
	public int getPid()		{
	
		return pid;
	
	}//End Of Method
	

	
	public String getType()		{
	
		return type;
	
	}//End Of Method		
	
	
	
	public String getName()		{
	
		return username;
	
	}//End Of Method	
	

	public Date getStartTime()		{
	
		return start;
	
	}//End Of Method		
	
	
	
	public Date getEndTime()		{
	
		return end;
	
	}//End Of Method			
	


	public String getTitle()		{
	
		return title;
	
	}//End Of Method	
	
	

	public String getDetails()		{
	
		return details;
	
	}//End Of Method	

	
	
	public int getAmount()		{
	
		return amount;
	
	}//End Of Method
	
	
	public String getStatus()		{
	
		return status;
	
	}//End Of Method

	
	public String getCategory()		{
	
		return category;
	
	}//End Of Method
	
	
	public int getInitialBid()		{
	
		return bid.getInitialBid();
	
	}//End Of Method


	public int getBidRate()		{
	
		return bid.getBidRate();
	
	}//End Of Method	
	
}//End Of Class