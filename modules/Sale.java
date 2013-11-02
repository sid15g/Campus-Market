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
	
 // Process Buying procedure

import data.Post;
import data.appData;
import handle.SqlDate;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
	
public class Sale extends Transaction	{

	private Connection connect;
	private Common query;
	private Post clicked;
	private String UID;
	
	public Sale()		{
	
		this.connect = Connect.getConnection();
		this.UID = super.uniqueID;
		this.query = new Common();
		
	}//End Of Constructor
	
	
	@Override 							//Over rides the abstract function execute(), inherited from class Transaction
	public void execute( int pid )			{
		
		try			{
			
			this.clicked = this.query.getPost( pid );
			
			if( this.clicked!=null && check() )		{
				reserveProduct();
			}else	{
				Transaction.homeRefer.ph.errorBox("ERROR: Post already booked/Check your Balance.");
			}//End Of IF Else
	
		}catch( Exception e )		{
			e.printStackTrace();
			Transaction.homeRefer.ph.errorBox("ERROR: Completing the Transaction.");
		}//End Of Try Catch
	
	
	}//End Of Method
	
	
	@Override 							//Over rides the abstract function check(), inherited from class Transaction
	public boolean check() throws Exception		{
		
		String sql = "SELECT * FROM `"+appData.TABLE_POSTS+"` WHERE `"+appData.POSTS_PID+"`= ? AND `"+appData.POSTS_TYPE+"` = ?;";
		
		PreparedStatement mySql = this.connect.prepareStatement( sql );
		mySql.setInt( 1, this.clicked.getPid() );
		mySql.setString( 2, appData.SALE );
		ResultSet postData = mySql.executeQuery();
		
		if( postData.next() )		{

			String status = postData.getString( appData.POSTS_STATUS );
			String user = postData.getString( appData.POSTS_USERNAME );
			
			if( status.equals(appData.ACTIVE) && !user.equals(Login.username) )		{
				
				if( Login.credits >= this.clicked.getAmount() )	
					return true;

			}//End Of If
		
		}//End Of Main If
		
		postData.close();
		mySql.close();
		
		return false;
		
	}//End Of Method
	
	
	@Override 							//Over rides the abstract function reserveProduct(), inherited from class Transaction
	public void reserveProduct() throws Exception		{
		
		/** Update the post as Reserved in database */
		
		String sql = "UPDATE `"+appData.TABLE_POSTS+"` SET `"+appData.POSTS_STATUS+"`=? WHERE `"+appData.POSTS_PID+"`=?;";
		
		PreparedStatement mySql = this.connect.prepareStatement( sql );
		mySql.setString( 1, appData.BOOKED );
		mySql.setInt( 2, clicked.getPid() );
		mySql.executeUpdate();

		/** Deduct the amount from buyer*/
		
		Login.credits = Login.credits - clicked.getAmount();
		homeRefer.setCredits();
		
		int trans = Login.ME.getTransactions();
		trans++;
		
		sql = "UPDATE `"+appData.TABLE_USERS+"` SET `"+appData.USER_BALANCE+"`=?, `"+appData.USER_TRANS+"`=? WHERE `"+appData.USER_USERNAME+"`=?;";
		
		mySql = this.connect.prepareStatement( sql );
		mySql.setInt( 1, Login.credits );
		mySql.setInt( 2, trans );
		mySql.setString( 3, Login.username );
		mySql.executeUpdate();
		
		/** Adds entry in the bank */
		
		sql = "INSERT INTO `"+appData.TABLE_BANK+"` ( `"+appData.BANK_TID+"`, `"+appData.BANK_PIN+"`, `"+appData.BANK_SELLER+"`, `"+appData.BANK_AMT+"`, `"+appData.BANK_BUYER+"`, `"+appData.BANK_PID+"`, `"+appData.BANK_TYPE+"`, `"+appData.BANK_TIME+"` ) VALUES (?,?,?,?,?,?,?,?);";
		
		mySql = this.connect.prepareStatement( sql );	
		
		int newTid = this.query.getNewTid();
		mySql.setInt( 1, newTid );
		
		mySql.setString( 2, this.UID );
		mySql.setString( 3, this.clicked.getName() );
		mySql.setInt( 4, this.clicked.getAmount() );
		mySql.setString( 5, Login.username );
		mySql.setInt( 6, this.clicked.getPid() );
		mySql.setString( 7, this.clicked.getType() );
		

		SqlDate now = new SqlDate( new Date() );
		mySql.setTimestamp( 8, now.getTimestamp() );
		
		mySql.executeUpdate();
		
		mySql.close();
		
		Transaction.homeRefer.setBuyInfo( this.clicked.getAmount() , this.UID );

	}//End Of Method
	
	
	@Override 							//Over rides the abstract function complete(), inherited from class Transaction
	public void complete( int pid, String pin )			{

		try		{
		
			String type = appData.SALE;
			String seller = Login.username;		
		
			String sql = "SELECT * FROM `"+appData.TABLE_BANK+"` WHERE `"+appData.BANK_PIN+"`=? AND `"+appData.BANK_SELLER+"`=? AND `"+appData.BANK_PID+"`=? AND `"+appData.BANK_TYPE+"`=?;";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );	
			mySql.setString( 1, pin );
			mySql.setString( 2, seller );
			mySql.setInt( 3, pid );
			mySql.setString( 4, type );
			
			ResultSet finalResult = mySql.executeQuery();

			if( finalResult.next() )		{
			
				int amount = finalResult.getInt( appData.BANK_AMT );
			
				/** Give the money to the seller */
				
				Login.credits = Login.credits + amount;
				homeRefer.setCredits();
				
				int trans = Login.ME.getTransactions();
				trans++;
				
				sql = "UPDATE `"+appData.TABLE_USERS+"` SET `"+appData.USER_BALANCE+"`=?, `"+appData.USER_TRANS+"`=? WHERE `"+appData.USER_USERNAME+"`=?;";
				
				mySql = this.connect.prepareStatement( sql );
				mySql.setInt( 1, Login.credits );
				mySql.setInt( 2, trans );
				mySql.setString( 3, Login.username );
				mySql.executeUpdate();
				
				/** Remove Post from bank table */
				
				sql = "DELETE FROM `"+appData.TABLE_BANK+"` WHERE `"+appData.BANK_PIN+"`=? AND `"+appData.BANK_SELLER+"`=? AND `"+appData.BANK_PID+"`=? AND `"+appData.BANK_TYPE+"`=?;";	
				
				mySql = this.connect.prepareStatement( sql );
				mySql.setString( 1, pin );
				mySql.setString( 2, seller );
				mySql.setInt( 3, pid );
				mySql.setString( 4, type );	
				mySql.executeUpdate();					
				
				/** Update post status to done */
				
				sql = "UPDATE `"+appData.TABLE_POSTS+"` SET `"+appData.POSTS_STATUS+"`=? WHERE `"+appData.POSTS_PID+"`=?;";
				
				mySql = this.connect.prepareStatement( sql );
				mySql.setString( 1, appData.DONE );
				mySql.setInt( 2, pid );
				mySql.executeUpdate();				
				
				Transaction.homeRefer.ph.ackBox("Transaction succesfully completed.");
			
			}else	{
				Transaction.homeRefer.ph.errorBox("ERROR: Incorrect UID" );
			}//End Of If Else
			
			finalResult.close();
			mySql.close();
			
		}catch( Exception sqle )		{				//SQLException
			Transaction.homeRefer.ph.errorBox("ERROR: Unable to complete transaction" );
		}//End Of Try Catch

	}//End Of Method	
	
	
}//End Of Class