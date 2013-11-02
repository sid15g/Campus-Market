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

 // Provides Login functionality

import data.User;
import data.appData;
import settings.Save;
import java.util.List;
import java.io.Console;
import handle.LoginAction;
import java.sql.ResultSet;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.PreparedStatement;


public class Login	{
	
	private LoginAction getData = new LoginAction();							/** If not initantiated here, than Home's look gets chenged */
	private Save userDetails = new Save();
	
	private boolean alreadyRegistered = Save.isLoginSaved;
	public static String username, imagePath;
	public static int credits;
	private String password;
	private int status = -1;
	public static User ME;
	
	
	public Login()		{
		
		if( alreadyRegistered )		{

			Secure cipher = new Secure();
		
			this.username = cipher.decrypt( userDetails.getUsername() );
//			System.out.println("\nUsername: "+username);			
			
			this.password = cipher.decrypt( userDetails.getPassword() );
//			System.out.println("\nPassword: "+password);
			
		}else	{

			getData.setVisible( true );
			getData.saveObject( getData );
			
			while( !LoginAction.DONE )		{
			
				try		{
					wait();
				}catch( Exception ie )	{}
				
			}//End Of While Loop
			
			this.username = getData.getUsername();
			this.password = getData.getPassword();
			
			boolean saveStatus = getData.getSavedStatus();
			
			if( saveStatus )		{
				userDetails.registerUser( username, password );
			}
			
			getData.setVisible( false );

		}//End Of If Else
		
		getData.dispose();

	}//End Of Constructor


	public boolean authenticate()		{

		Connection connect = Connect.getConnection();
		boolean registeredUser = false;
		
		try		{
		
			String sql = "SELECT * FROM `"+appData.TABLE_USERS+"` WHERE `"+appData.USER_USERNAME+"`=?;";
			
			PreparedStatement mySql = connect.prepareStatement(sql);
			mySql.setString( 1, username );
			
			ResultSet userResult = mySql.executeQuery();

			String pass = "";
			int verified = 0;
				
			if( userResult.next() )		{	
				pass = userResult.getString( appData.USER_PASSWORD );
				verified = userResult.getInt( appData.USER_VERIFIED );
				registeredUser = true;
				status = 2;
			}//End Of If: Registered User
			
			if( registeredUser && password.equals( pass ) && verified == 1 )		{
				this.credits = userResult.getInt( appData.USER_BALANCE );
				status = 4;
			}//End Of If: Password Matched and Verified
		
			userResult.close();
			mySql.close();
		
		}catch( Exception e )	{					//SQLException
		
			this.username = "";
			this.password = "";
			System.err.println( "ERROR: Login Unsucessfull" );
			
			return false;
			
		}//End Of Try-Catch		
		
		
		if( status%4 != 0 )		{
		
			this.username = "";
			this.password = "";
			System.err.println("ERROR: Check Username/Password or Confirm Verification.");
			return false;
			
		}else		{
			System.out.println("\n\tLogin Successfull");
			imagePath = "../handle/images/users/"+Login.username+".jpg";
			return true;
		}//End Of If Else

	}//End Of Method


}//End Of Class