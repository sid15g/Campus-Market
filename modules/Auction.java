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
	
 // Process Bidding procedure
	
import data.Post;
import data.Share;
import data.appData;
import handle.SqlDate;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;

	
public class Auction extends Transaction	{

	private Connection connect;
	private int bidAmount;
	private Common query;
	private Post clicked;
	private SqlDate now;
	private String UID;

	public Auction()	{
	
		this.connect = Connect.getConnection();
		this.now = new SqlDate( new Date() );
		this.UID = super.uniqueID;
		this.query = new Common();		
		
	}//End Of Constructor
	
	
	public void execute( int pid, int amount )			{
	
		this.bidAmount = amount;
		execute( pid );
	
	}//End Of Method
	

	@Override 							//Over rides the abstract function execute(), inherited from class Transaction
	public void execute( int pid )			{
		
		try			{
			
			this.clicked = this.query.getPost( pid );
			
			if( this.clicked!=null && check() )		{
				addBids();
			}else	{
				Transaction.homeRefer.ph.errorBox("ERROR: Post already booked/Check your Balance.");
			}//End Of IF Else
			
	
		}catch( Exception e )		{
			Transaction.homeRefer.ph.errorBox("ERROR: Completing the Transaction.");
		}//End Of Try Catch
	
	
	}//End Of Method
	
	
	@Override 							//Over rides the abstract function check(), inherited from class Transaction
	public boolean check() throws Exception		{
		
		String sql = "SELECT * FROM `"+appData.TABLE_POSTS+"` WHERE `"+appData.POSTS_PID+"`= ? AND `"+appData.POSTS_TYPE+"` = ?;";
		
		PreparedStatement mySql = this.connect.prepareStatement( sql );
		mySql.setInt( 1, clicked.getPid() );
		mySql.setString( 2, appData.BID );
		ResultSet postData = mySql.executeQuery();
		
		if( postData.next() )			{
		
			String status = postData.getString( appData.POSTS_STATUS );
			String user = postData.getString( appData.POSTS_USERNAME );
			
			if( status.equals(appData.ACTIVE) && !user.equals(Login.username) )		{
				
				int lastBid = this.clicked.getAmount();
				int initBid = this.clicked.getInitialBid();
				
				float rate = ((float)(this.bidAmount - lastBid) / (float)initBid ) * 100;
				float bidRate = (float)this.clicked.getBidRate();
				
				if( Login.credits >= this.bidAmount && rate >= bidRate )	
					return true;
					
			}//End Of If
		
		}//End Of Main If
		
		postData.close();
		mySql.close();		
		
		return false;
		
	}//End Of Method

	
	private void addBids() throws Exception		{	
	
		/** CANNOT RESERVE AN AUCTION POST NOW */
		
		/** Deducts the bidded amount from database */
		
		Login.credits = Login.credits - this.bidAmount;
		homeRefer.setCredits();
		
		String sql = "UPDATE `"+appData.TABLE_USERS+"` SET `"+appData.USER_BALANCE+"`=? WHERE `"+appData.USER_USERNAME+"`=?;";
		
		PreparedStatement mySql = this.connect.prepareStatement( sql );
		mySql.setInt( 1, Login.credits );
		mySql.setString( 2, Login.username );
		
		mySql.executeUpdate();
		
		/** Update amount, pid in post list of database */
		
		int oldPid = this.clicked.getPid();
		int newPid = this.query.getNewPid();		
		
		sql = "UPDATE `"+appData.TABLE_POSTS+"` SET `"+appData.POSTS_PID+"`=? , `"+appData.POSTS_AMT+"`=? WHERE `"+appData.POSTS_PID+"`=?;";
		
		mySql = this.connect.prepareStatement( sql );	
		
		mySql.setInt( 1, newPid );
		mySql.setInt( 2, this.bidAmount );
		mySql.setInt( 3, oldPid );
		
		mySql.executeUpdate();
		
		/** Checking for an already existing bid */
		
		sql = "SELECT * FROM `"+appData.TABLE_BIDS+"` WHERE `"+appData.BIDS_PID+"`= ? ORDER BY `"+appData.BIDS_TIME+"` DESC;";
		
		mySql = this.connect.prepareStatement( sql );
		mySql.setInt( 1, clicked.getPid() );
		
		ResultSet postData = mySql.executeQuery();
		
		if( postData.next() )		{
		
			String oldBuyer = postData.getString( appData.BIDS_USERNAME );
		
			/** Update entry of post in bank table */
		
			sql = "UPDATE `"+appData.TABLE_BANK+"` SET `"+appData.BANK_AMT+"`=? , `"+appData.BANK_PID+"`=? ,`"+appData.BANK_BUYER+"`=? , `"+appData.BANK_TIME+"`=?, `"+appData.BANK_PIN+"`=?  WHERE `"+appData.BANK_PID+"` = ?;";
			
			mySql = this.connect.prepareStatement( sql );	
			
			mySql.setInt( 1, this.bidAmount );
			mySql.setInt( 2, newPid );
			mySql.setString( 3, Login.username );
			mySql.setTimestamp( 4, this.now.getTimestamp() );			
			mySql.setString( 5, this.UID );
			mySql.setInt( 6, oldPid );
			
			mySql.executeUpdate();
			
			/** Update Bid previous table entries  */
			
			sql = "UPDATE `"+appData.TABLE_BIDS+"` SET `"+appData.BIDS_PID+"`=? WHERE `"+appData.BIDS_PID+"`=?;";
	
			mySql = this.connect.prepareStatement( sql );	
			
			mySql.setInt( 1, newPid );
			mySql.setInt( 2, oldPid );			
			
			mySql.executeUpdate();
			
			/** Returns money to the previous bidder */
			
			int money = this.clicked.getAmount();			
			
			sql = "SELECT * FROM `"+appData.TABLE_USERS+"` WHERE `"+appData.USER_USERNAME+"`=?;";
			
			mySql = this.connect.prepareStatement( sql );				
			mySql.setString( 1, oldBuyer );
			
			postData = mySql.executeQuery();
			postData.next();
			int balance =  postData.getInt( appData.USER_BALANCE );
			int finalBalance = balance + money;
			
			sql = "UPDATE `"+appData.TABLE_USERS+"` SET `"+appData.USER_BALANCE+"`=? WHERE `"+appData.USER_USERNAME+"`=?;";
			
			mySql = this.connect.prepareStatement( sql );
			mySql.setInt( 1, finalBalance );
			mySql.setString( 2, oldBuyer );			
		
			mySql.executeUpdate();
		
		}else		{
			
			/** Insert the post details in bank list */
			
			sql = "INSERT INTO `"+appData.TABLE_BANK+"` ( `"+appData.BANK_TID+"`, `"+appData.BANK_PIN+"`, `"+appData.BANK_SELLER+"`, `"+appData.BANK_AMT+"`, `"+appData.BANK_BUYER+"`, `"+appData.BANK_PID+"`, `"+appData.BANK_TYPE+"`, `"+appData.BANK_TIME+"` ) VALUES (?,?,?,?,?,?,?,?);";
			
			mySql = this.connect.prepareStatement( sql );	
			
			int newTid = this.query.getNewTid();
			mySql.setInt( 1, newTid );
			
			mySql.setString( 2, this.UID );
			mySql.setString( 3, this.clicked.getName() );
			mySql.setInt( 4, this.bidAmount );
			mySql.setString( 5, Login.username );
			mySql.setInt( 6, newPid );
			mySql.setString( 7, this.clicked.getType() );
			
			mySql.setTimestamp( 8, this.now.getTimestamp() );
			
			mySql.executeUpdate();	
		
		}//End Of If Else
		
		
		/** Add Bid table entry  */
		
		sql = "INSERT INTO `"+appData.TABLE_BIDS+"` ( `"+appData.BIDS_PID+"`, `"+appData.BIDS_USERNAME+"`, `"+appData.BIDS_AMT+"`, `"+appData.BIDS_TIME+"` ) VALUES (?,?,?,?);";
			
		mySql = this.connect.prepareStatement( sql );	
		
		mySql.setInt( 1, newPid );
		mySql.setString( 2, Login.username );
		mySql.setInt( 3, bidAmount );
		mySql.setTimestamp( 4, this.now.getTimestamp() );
		
		mySql.executeUpdate();			
		
		
		/** Update new pid in bidpost table of database */	
		
		sql = "UPDATE `"+appData.TABLE_BIDPOST+"` SET `"+appData.BPOST_PID+"`=?, `"+appData.BPOST_OLD+"`=? WHERE `"+appData.BPOST_PID+"`=?;";
		
		mySql = this.connect.prepareStatement( sql );	
		
		mySql.setInt( 1, newPid );
		mySql.setInt( 2, oldPid );
		mySql.setInt( 3, oldPid );
		
		mySql.executeUpdate();				
		
		postData.close();
		mySql.close();		
	
	}//End Of Method
	
	
	
	public void reserveProduct( int pid )			{
		
		try		{
	
			this.clicked = this.query.getPost( pid );
	
			if( this.clicked!=null && this.clicked.getStatus().equals( appData.ACTIVE ) )		{
			
				reserveProduct();
			
			}//End Of If

		}catch( Exception e )			{			//SQLException
			e.printStackTrace();
		}//End Of Try Catch
	
	}//End Of Method
	
	
	@Override 							//Over rides the abstract function reserveProduct(), inherited from class Transaction
	public void reserveProduct() throws Exception		{		
	
		/** Reserve the post */
	
		String sql = "UPDATE `"+appData.TABLE_POSTS+"` SET `"+appData.POSTS_STATUS+"`=? WHERE `"+appData.POSTS_PID+"`=?;";

		PreparedStatement mySql = this.connect.prepareStatement( sql );	
		mySql.setString( 1, appData.BOOKED );
		mySql.setInt( 2, this.clicked.getPid() );
		
		mySql.executeUpdate();
		
		
		/** Get transaction details of buyer */
		
		sql = "SELECT * FROM `"+appData.TABLE_BANK+"`, `"+appData.TABLE_USERS+"` WHERE `"+appData.BANK_PID+"`=? AND `"+appData.TABLE_BANK+"`."+appData.BANK_BUYER+" = `"+appData.TABLE_USERS+"`."+appData.USER_USERNAME+";";
		
		mySql = this.connect.prepareStatement( sql );	
		mySql.setInt( 1, this.clicked.getPid() );
		
		ResultSet dataSet = mySql.executeQuery();
		
		if( dataSet.next() )		{
		
			String buyer = dataSet.getString( appData.BANK_BUYER );
			
			int trans = dataSet.getInt( appData.USER_TRANS );
			trans++;
			
			sql = "UPDATE `"+appData.TABLE_USERS+"` SET `"+appData.USER_TRANS+"`=? WHERE `"+appData.USER_USERNAME+"`=?;";
			
			mySql = this.connect.prepareStatement( sql );
			mySql.setInt( 1, trans );
			mySql.setString( 2, buyer );
			mySql.executeUpdate();			
		
		}else			{
			System.err.println("ERROR: Error completing transaction");
		}//End Of If Else
		
		dataSet.close();
		mySql.close();
	
	}//End Of Method
	
	
	@Override 							//Over rides the abstract function complete(), inherited from class Transaction
	public void complete( int pid, String pin )				{

		try		{
		
			String type = appData.BID;
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
	
	

	public Share lastBid( int pid )			{
	
		Share variable = new Share( 1, 1 );
			
		try		{
		
			String sql = "SELECT * FROM `"+appData.TABLE_BIDS+"` WHERE `"+appData.BIDS_PID+"`= ? ORDER BY `"+appData.BIDS_TIME+"` DESC;";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			mySql.setInt( 1, pid );
			
			ResultSet postData = mySql.executeQuery();	

			if( postData.next() )		{
			
				variable.str[0] = postData.getString( appData.BIDS_USERNAME );
				variable.val[0] = postData.getInt( appData.BIDS_AMT );
			
			}else	{
		
				variable.str[0] = "";
				variable.val[0] = -1;
			
			}//End Of If Else
			
			postData.close();
			mySql.close();
		
		}catch( Exception sqle )		{				//SQLException
			sqle.printStackTrace();
		}//End Of TryCatch
		
		return variable;
	
	}//End Of Method

	
	
	public void showBids( int pid )			{
	
		try		{

			String sql = "SELECT * FROM `"+appData.TABLE_BIDS+"` WHERE `"+appData.BIDS_PID+"`= ? ORDER BY `"+appData.BIDS_TIME+"` ASC;";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			mySql.setInt( 1, pid );

			ResultSet bids = mySql.executeQuery();
			
			while( bids.next() )		{
			
				Date bTime = Common.SDF.parse( bids.getString( appData.BIDS_TIME ) );

				Transaction.homeRefer.showCurrentBids(
				
					bids.getString( appData.BIDS_USERNAME ),
					bids.getInt( appData.BIDS_AMT ),
					bTime.toString()
					
				);
			
			}//End Of Loop
			
			bids.close();
			mySql.close();
	
		}catch( Exception sqle )		{				//SQLException
			sqle.printStackTrace();
		}//End Of TryCatch		
	
	}//End Of Method
	
	
}//End Of Class