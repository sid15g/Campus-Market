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
	
 // Stores details of bid posts
	
import errors.PostDetailsException;	
	
public class BidPost	{

	private int initial, rate;

	public BidPost()	{
	
		initial = rate = 0;

		try	{
			throw new PostDetailsException();
		}catch( PostDetailsException e )	{
			System.err.println(e.getMessage());
		}//End Of Try Catch
		
	}//End Of Constructor
	
	
	public BidPost( int i, int r )			{
	
		this.initial = i;
		this.rate = r;
	
	}//End Of Constructor
	
	
	public int getInitialBid()		{
	
		return initial;
	
	}//End Of Method
	
	
	public int getBidRate()		{
	
		return rate;
	
	}//End Of Method
	
	
}//End Of Class