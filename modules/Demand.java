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
	
 // Process Demand procedure
	
import data.Post;	
import data.appData;
import handle.SqlDate;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;	
	
public class Demand extends Transaction		{

	private String UID, seller, demandDetail;
	private Connection connect;
	private int demandAmount;
	private Common query;
	private Post clicked;
	private SqlDate now;

	public Demand()	{
	
		this.connect = Connect.getConnection();
		this.now = new SqlDate( new Date() );
		this.UID = super.uniqueID;
		this.query = new Common();
	
	}//End Of Constructor
	
	
	public void execute( int pid, int amount, String desc )			{
	
		this.demandAmount = amount;
		this.demandDetail = desc;
		execute( pid );
	
	}//End Of Method
	
	
	public void execute( int pid, int amount )			{						//Optional Details
	
		this.demandAmount = amount;
		this.demandDetail = "";
		execute( pid );
	
	}//End Of Method
	
	

	@Override 							//Over rides the abstract function execute(), inherited from class Transaction
	public void execute( int pid )			{
		
		try			{
			
			this.clicked = this.query.getPost( pid );
			
			if( this.clicked!=null && check() )		{
				addDemands();
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
		mySql.setString( 2, appData.BUY );
		ResultSet postData = mySql.executeQuery();
		
		if( postData.next() )			{
		
			String status = postData.getString( appData.POSTS_STATUS );
			String user = postData.getString( appData.POSTS_USERNAME );
			
			if( status.equals(appData.ACTIVE) && !user.equals(Login.username) )		{
				
				/** Check for the user has already done a demand or not */
				
				sql = "SELECT * FROM `"+appData.TABLE_DEMANDS+"` WHERE `"+appData.DEMAND_PID+"`= ? AND `"+appData.DEMAND_USERNAME+"` = ?;";
				
				mySql = this.connect.prepareStatement( sql );
				mySql.setInt( 1, this.clicked.getPid() );
				mySql.setString( 2, Login.username );
				postData = mySql.executeQuery();				
				
				if( !postData.next() )
					return true;
				
			}//End Of If
		
		}//End Of Main If
		
		postData.close();
		mySql.close();
		
		return false;
		
	}//End Of Method

	

	private void addDemands() throws Exception		{	

		/** CANNOT RESERVE A DEMAND POST NOW */
		
		/** Add the post details in demand table */
		
		String sql = "INSERT INTO `"+appData.TABLE_DEMANDS+"` ( `"+appData.DEMAND_PID+"`, `"+appData.DEMAND_USERNAME+"`, `"+appData.DEMAND_AMT+"`, `"+appData.DEMAND_DETAIL+"`, `"+appData.DEMAND_TIME+"`, `"+appData.DEMAND_STATUS+"` ) VALUES (?,?,?,?,?,?);";		
		
		PreparedStatement mySql = this.connect.prepareStatement( sql );	
		
		mySql.setInt( 1, this.clicked.getPid() );
		mySql.setString( 2, Login.username );
		mySql.setInt( 3, this.demandAmount );
		mySql.setString( 4, this.demandDetail );
		mySql.setTimestamp( 5, this.now.getTimestamp() );
		mySql.setString( 6, appData.WAIT );
		
		mySql.executeUpdate();
		mySql.close();

	}//End Of Method
	
	
	
	public void reserveProduct( int pid, String username )		{

		this.seller = username;
		
		try		{
		
			String sql = "SELECT * FROM `"+appData.TABLE_DEMANDS+"` WHERE `"+appData.DEMAND_USERNAME+"`= ? AND `"+appData.DEMAND_PID+"`= ? AND `"+appData.DEMAND_STATUS+"`= ?;";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			mySql.setString( 1, this.seller );
			mySql.setInt( 2, pid );
			mySql.setString( 3, appData.WAIT );
			
			ResultSet dataSet = mySql.executeQuery();
		
			if( dataSet.next()	)			{
				this.demandAmount = dataSet.getInt( appData.BIDS_AMT );
				this.clicked = this.query.getPost( pid );
				reserveProduct();
			}else	{
				Transaction.homeRefer.ph.errorBox("ERROR: Post already Reserved");
			}//End Of If Else
			
			dataSet.close();
			mySql.close();
			
		}catch( Exception e )				{				//SQLException
			e.printStackTrace();
		}//End Of Try Catch		
		
	
	}//End Of Method
	
	
	@Override 							//Over rides the abstract function reserveProduct(), inherited from class Transaction
	public void reserveProduct() throws Exception		{
	
		/** Checking for the post in bank list */
		
		String sql = "SELECT * FROM `"+appData.TABLE_BANK+"` WHERE `"+appData.BANK_PID+"`= ?;";
		
		PreparedStatement mySql = this.connect.prepareStatement( sql );
		mySql.setInt( 1, this.clicked.getPid() );
		
		ResultSet postData = mySql.executeQuery();		
		
		if( !postData.next() )		{
		
			/** Insert the post details in bank list */
			
			sql = "INSERT INTO `"+appData.TABLE_BANK+"` ( `"+appData.BANK_TID+"`, `"+appData.BANK_PIN+"`, `"+appData.BANK_SELLER+"`, `"+appData.BANK_AMT+"`, `"+appData.BANK_BUYER+"`, `"+appData.BANK_PID+"`, `"+appData.BANK_TYPE+"`, `"+appData.BANK_TIME+"` ) VALUES (?,?,?,?,?,?,?,?);";
			
			mySql = this.connect.prepareStatement( sql );	
			
			int newTid = this.query.getNewTid();
			mySql.setInt( 1, newTid );
			
			mySql.setString( 2, this.UID );
			mySql.setString( 3, this.seller );
			mySql.setInt( 4, this.demandAmount );
			mySql.setString( 5, this.clicked.getName() );
			mySql.setInt( 6, this.clicked.getPid() );
			mySql.setString( 7, this.clicked.getType() );
			
			SqlDate now = new SqlDate( new Date() );
			mySql.setTimestamp( 8, this.now.getTimestamp() );
			
			mySql.executeUpdate();				
		
		}//End Of If
		
		
		/** Update post amount, status in the post table */
		
		sql = "UPDATE `"+appData.TABLE_POSTS+"` SET `"+appData.POSTS_STATUS+"`=? , `"+appData.POSTS_AMT+"`=? WHERE `"+appData.POSTS_PID+"`=?;";
		
		mySql = this.connect.prepareStatement( sql );	
		
		mySql.setString( 1, appData.BOOKED );
		mySql.setInt( 2, this.demandAmount );
		mySql.setInt( 3, this.clicked.getPid() );
		
		mySql.executeUpdate();		
		
		/** Change status of the demanded post in the demand table */
		
		sql = "UPDATE `"+appData.TABLE_DEMANDS+"` SET `"+appData.DEMAND_STATUS+"`= ? WHERE `"+appData.DEMAND_USERNAME+"`= ? AND `"+appData.DEMAND_PID+"`= ?;";
		
		mySql = this.connect.prepareStatement( sql );
		mySql.setString( 1, appData.ACCEPT );
		mySql.setString( 2, this.seller );
		mySql.setInt( 3, this.clicked.getPid() );
		
		mySql.executeUpdate();
		
		sql = "UPDATE `"+appData.TABLE_DEMANDS+"` SET `"+appData.DEMAND_STATUS+"`= ? WHERE `"+appData.DEMAND_STATUS+"`= ? AND `"+appData.DEMAND_PID+"`= ?;";
		
		mySql = this.connect.prepareStatement( sql );
		mySql.setString( 1, appData.DENY );
		mySql.setString( 2, appData.WAIT );
		mySql.setInt( 3, this.clicked.getPid() );
		
		mySql.executeUpdate();			
		
		/** Deducts the demanded amount from buyers acc */
		
		Login.credits = Login.credits - this.demandAmount;
		homeRefer.setCredits();
		
		int trans = Login.ME.getTransactions();
		trans++;
		
		sql = "UPDATE `"+appData.TABLE_USERS+"` SET `"+appData.USER_BALANCE+"`=?, `"+appData.USER_TRANS+"`=? WHERE `"+appData.USER_USERNAME+"`=?;";
		
		mySql = this.connect.prepareStatement( sql );
		mySql.setInt( 1, Login.credits );
		mySql.setInt( 2, trans );
		mySql.setString( 3, Login.username );
		mySql.executeUpdate();

		Transaction.homeRefer.setBuyInfo( this.demandAmount, this.UID );
		
		postData.close();
		mySql.close();		
		
	
	}//End Of Method
	
	
	@Override 							//Over rides the abstract function complete(), inherited from class Transaction
	public void complete( int pid, String pin )				{
	
		try		{
		
			String type = appData.BUY;
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
	
	
	
	public void showDemands( int pid )			{
	
		try		{

			String sql = "SELECT * FROM `"+appData.TABLE_DEMANDS+"` WHERE `"+appData.DEMAND_PID+"`= ? ORDER BY `"+appData.DEMAND_TIME+"` ASC;";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			mySql.setInt( 1, pid );

			ResultSet demands = mySql.executeQuery();
			
			while( demands.next() )		{
			
				Date bTime = Common.SDF.parse( demands.getString( appData.DEMAND_TIME ) );
			
				Transaction.homeRefer.showQuoteDetails(
				
					demands.getString( appData.DEMAND_USERNAME ),
					demands.getInt( appData.DEMAND_AMT ),
					demands.getString( appData.DEMAND_DETAIL ),
					bTime.toString(),
					demands.getInt( appData.DEMAND_PID )
					
				);
			
			}//End Of Loop
			
			demands.close();
			mySql.close();
	
		}catch( Exception sqle )		{				//SQLException
			sqle.printStackTrace();
		}//End Of TryCatch		
	
	}//End Of Method	
	
	
	
}//End Of Class