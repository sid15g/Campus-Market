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
	
package	handle;
	
 // To convert java.util.Date to java.sql.Date.
	
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import errors.DateFormatException;
	
public class SqlDate	 	{

	private Date sDate;
	private Time sTime;
	private Timestamp finalDate;

	public SqlDate( java.util.Date uDate )			{

		try		{
		
			this.sDate = new Date( uDate.getTime() );
			this.sTime = new Time( uDate.getTime() );
			
			String timestamp = sDate.toString() +" "+ sTime.toString();
		
			this.finalDate = Timestamp.valueOf( timestamp );
	
		}catch(Exception e)	{	
			System.err.println("ERROR: Could not convert the Date");
		}//End Of Try Catch		
		
	}//End Of Constructor
	

	public Timestamp getTimestamp()		{
		
		return this.finalDate;
	
	}//End Of Method
	
	

	public Timestamp getTimestamp( boolean dob ) throws Exception		{
		
		if( finalDate.before( new Timestamp( new java.util.Date().getTime() ) ) )		{
		
			return this.finalDate;
		
		}else		{
		
			throw new DateFormatException();
		
		}//End Of If Else
	
	}//End Of Method	
	
	
	public String getDate()		{
		
		return this.sDate.toString();
	
	}//End Of Method	
	
	
}//End Of Class