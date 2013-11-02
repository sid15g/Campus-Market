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
 
// Starts the Application

import settings.SetupServer;
import java.sql.Connection;
import modules.Transaction;
import handle.MarketHome;
import modules.FetchData;
import modules.Connect;
import modules.Login;
import java.util.Date;


class Start		{

	private Connection connect;
	private boolean isCloudSet;
	private MarketHome home;

	public Start()		{
	
		SetupServer serverSettings = new SetupServer();
		this.isCloudSet = SetupServer.isCloudSet;	
		this.home = null;
		
		if( !isCloudSet )	{

			System.out.println("\nNo Server Settings Found.\n\tPlease enter new settings.");
			serverSettings.setCloud();
	
		}
		
	
	}//End Of Constructor
	
	
	public static void main( String args[] )		{
		
		Start app = new Start();	
		app.start();
	
	}//End Of Main
	
	
	private void start()		{							//NOTE: Not a Thread
	
		this.connect = new Connect().getConnection();
		FetchData fetchAppData = new FetchData();
		Thread updatePosts = null;
		Login login = new Login();
		
		if( login.authenticate() )		{

			fetchAppData.start();
			
			this.home = new MarketHome();
			Transaction.homeRefer = this.home;
			updatePosts = new Thread( home );
			updatePosts.start();

			try		{
				fetchAppData.join();
			}catch( Exception ie )			{					//InterruptedException
				System.err.println("ERROR: Login...Please Try Again!!! ");
			}//End Of Try-Catch	
			
			
		}//End Of If	
		
		while( home!=null && this.home.isVisible() )		{
		
			try		{
				wait();
			}catch( Exception e ){}
			
		}//End Of Loop
		
		try		{

			MarketHome.toUpdate = false;
			this.home.dispose();
			updatePosts.join();
			this.connect.close();
			
		}catch( Exception sqle )		{						//SQL Exception, InterruptedException
			System.err.println("ERROR: Closing Application");
		}//End Of Try Catch			
	
	}//End Of Method

	
}//End Of Class