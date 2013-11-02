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
	
 // Super class for all type of transactions

import data.appData;
import java.util.UUID; 
import handle.MarketHome;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;	
	
public abstract class Transaction	{

	public static MarketHome homeRefer;
	public static int recentView;
	public static int recentPid;
	String uniqueID;											/** For Security friendly, and not public */
	
	public Transaction()		{

		char[] array;
	
		while( true )			{
		
			int ID = UUID.randomUUID().toString().hashCode();
		
			if( ID < 0 )
				ID *= -1;
			
			String hex = Integer.toHexString( ID );
			
			array = hex.toUpperCase().toCharArray();
			
			if( array.length == 8 )
				break;
		
		}//End Of Loop
		
		char[] finalId = new char[9];
		int j=0;
		
		for( int i=0; j<8; i++ )			{
		
			if( i!=4 ) 		{
				finalId[i] = array[j];
				j++;
			}else			{
				finalId[i] = '-';
			}//End Of If Else	
		
		}//End Of Loop
		
		this.uniqueID = new String( finalId );
	
	}//End Of Constructor
	
	public abstract void execute( int pid );
	public abstract boolean check() throws Exception;
	public abstract void reserveProduct() throws Exception;
	public abstract void complete( int pid , String pin );
	
	public static void setHome( MarketHome home )		{
	
		homeRefer = home;
	
	}//End Of Method
	
	
	public static String getUID( int pid )		{
	
		String sql = "SELECT * FROM `"+appData.TABLE_BANK+"` WHERE `"+appData.BANK_PID+"`=?;";
		Connection connect = Connect.getConnection();
		String uid = "";

		try			{
		
			PreparedStatement mySql = connect.prepareStatement( sql );	
			mySql.setInt( 1, pid );
			
			ResultSet pinResult = mySql.executeQuery();
			
			if( pinResult.next() )		{
			
				uid = pinResult.getString( appData.BANK_PIN );
			
			}
		
			pinResult.close();
			mySql.close();
		
		}catch( Exception e )		{			//SQLException
			e.printStackTrace();
		}//End Of Try Catch
		
		return uid;
	
	}//End Of Method
	
	
}//End Of Class