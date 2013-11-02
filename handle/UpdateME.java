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
	
 // Used to update user profile


import data.appData;	
import modules.Login;
import modules.Connect;
import modules.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
	
	
public class UpdateME				{

	private Connection connect;	

	public UpdateME()	{
	
		this.connect = Connect.getConnection();
	
	}//End Of Constructor
	
	
	
	public void setDetails( String email, long mobile, String address )	throws Exception	{
	
		Login.ME.setEmail( email );
		Login.ME.setContact( mobile );
		Login.ME.setAddress( address );
		
		this.save();
	
	}//End Of Method
	

	private void save() throws Exception			{
		
		String sql = "UPDATE `"+appData.TABLE_USERS+"` SET `"+appData.USER_EMAIL+"`=?, `"+appData.USER_MOBILE+"`=?, `"+appData.USER_ADDRESS+"`=? WHERE `"+appData.USER_USERNAME+"`=?;";
		
		PreparedStatement mySql = this.connect.prepareStatement( sql );
		
		mySql.setString( 1, Login.ME.getEmail() );
		mySql.setLong( 2, Login.ME.getContact() );
		mySql.setString( 3, Login.ME.getAddress() );
		mySql.setString( 4, Login.username );
		mySql.executeUpdate();
		
		mySql.close();
		
	}//End Of Method
	
}//End Of Class