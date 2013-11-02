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
	
 //Fetches data from server

import data.User;
import data.appData;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class FetchData extends Thread			{

	private Common query;
	private Connection connect;
	
	public FetchData()		{
	
		this.connect = Connect.getConnection();
		this.query = new Common();
	
	}//End Of Constructor
	
	
	public void run()		{

		try			{
	
			this.fetchMyDetails();
			this.fetchPosts();

			Common.max = this.query.getNewPid() - 1;					/** Initializing the present Max Post PID value in Database */
		
		}catch( Exception e )		{				//SQLException, ParseException
			Transaction.homeRefer.ph.errorBox("ERROR: Application was not able to fetch posts from server, Please check your connection.");
		}//End Of Try-Catch						
		
	}//End Of Thread
	
	
	private void fetchPosts() throws Exception		{
		
		String sql = "SELECT * FROM `"+appData.TABLE_POSTS+"` WHERE `"+appData.POSTS_STATUS+"`= ? ORDER BY `"+appData.POSTS_PID+"` ASC;";
		
		PreparedStatement mySql = connect.prepareStatement(sql);
		mySql.setString( 1, appData.ACTIVE );
		
		ResultSet userResult = mySql.executeQuery();	
	
		this.query.fetchPosts( userResult );
		
		userResult.close();
		mySql.close();	
	
	}//End Of Method
	
	
	private void fetchMyDetails() throws Exception		{
		
		String sql = "SELECT * FROM `"+appData.TABLE_USERS+"` WHERE `"+appData.USER_USERNAME+"` = ?;";
		PreparedStatement mySql = connect.prepareStatement(sql);	
		mySql.setString( 1, Login.username );
		
		ResultSet userResult = mySql.executeQuery();
	
		if( userResult.next() )		{

			String name = userResult.getString(appData.USER_NAME);
			String email = userResult.getString(appData.USER_EMAIL);
			long mobile = userResult.getLong(appData.USER_MOBILE);
			String address = userResult.getString(appData.USER_ADDRESS);
			String collegeId = userResult.getString(appData.USER_CID);
			Date dob = userResult.getTimestamp(appData.USER_DOB);
			int balance = userResult.getInt(appData.USER_BALANCE);
			int transactions = userResult.getInt(appData.USER_TRANS);
			int verified = userResult.getInt(appData.USER_VERIFIED);
			
			Login.ME = new User(Login.username, name, email, mobile, address, collegeId, dob, balance, transactions, verified);
		
		}//End Of If
	
		userResult.close();
		mySql.close();
		
	}//End Of Method
	
	
}//End Of Class