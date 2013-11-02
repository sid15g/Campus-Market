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
	
 // After Login, Synchronizes app Data with Server Data.

import data.Post;
import data.User; 
import data.appData;
import java.util.Date;
import java.util.List;
import handle.SqlDate;
import java.util.Iterator;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList; 
import java.util.LinkedList;	
import java.sql.PreparedStatement;
	
public class Update	extends Thread	{

	private List<Post> posts = appData.PostList;
	private boolean listUpdated = false;
	private Connection connect;
	private Common query;

	public Update()		{
	
		this.connect = Connect.getConnection();	
		this.query = new Common();
		
	}//End Of Constructor
	
	@Override						//Thread
	public void run()		{
	
		try		{
		
			this.cleanDeadPosts();
			this.checkBidPosts();
			this.cleanInactivePosts();
			
			if( this.checkNewPosts() || listUpdated )		{
			
				this.refreshTabs();
				this.refreshBalance();
				
				if( Transaction.recentView == 2 )			{
				
					Transaction.homeRefer.removeAllPost( 10 );
					Transaction.homeRefer.removeAllPost( 14 );
					
					int newPid = this.query.getNewBidPid( Transaction.recentPid );
					
					
					
					if( newPid>0 && newPid > Transaction.recentPid )	{
						Transaction.recentPid = newPid;
						Transaction.homeRefer.getCurrentBiddings( Transaction.recentPid );
					}
					
				}//End Of If				
				
				this.listUpdated = false;
				
			}//End Of Main If
		
			if( Transaction.recentView == 1 )		{
			
				Transaction.homeRefer.removeAllPost( 13 );
				Transaction.homeRefer.getQuoteDetails( Transaction.recentPid );
				
			}//End Of If
			
			
		}catch( Exception e )	{		//SQLException
			Transaction.homeRefer.ph.errorBox("ERROR: Connecting to Server.");
			Transaction.homeRefer.setVisible(false);
			Transaction.homeRefer.dispose();
		}//End Of Try Catch
		
	}//End Of Thread
	
	
	private void cleanDeadPosts() throws Exception			{
	
		String sql = "SELECT * FROM `"+appData.TABLE_POSTS+"` WHERE `"+appData.POSTS_STATUS+"` = ? AND `"+appData.POSTS_TYPE+"` != ? AND `"+appData.POSTS_ETIME+"` < ?;";
		
		
		PreparedStatement mySql = this.connect.prepareStatement( sql );
		mySql.setString( 1, appData.ACTIVE );
		mySql.setString( 2, appData.BID );
		
		SqlDate now = new SqlDate( new Date() );
		mySql.setTimestamp( 3, now.getTimestamp() );
		
		ResultSet dataSet = mySql.executeQuery();
		
		while( dataSet.next() )		{
		
			int pid = dataSet.getInt( appData.POSTS_PID );
		
			sql = "UPDATE `"+appData.TABLE_POSTS+"` SET `"+appData.POSTS_STATUS+"` = ? WHERE `"+appData.POSTS_PID+"` = ?;";
			mySql = this.connect.prepareStatement( sql );

			mySql.setString( 1, appData.DEAD );				
			mySql.setInt( 2, pid );
			
			mySql.executeUpdate();
			
		}//End Of Loop
		
		dataSet.close();
		mySql.close();
	
	}//End Of Method
	
	
	
	private void checkBidPosts() throws Exception	{
	
		/** Checking for 72 hrs concept */
	
		String sql = "SELECT * FROM `"+appData.TABLE_BANK+"` WHERE `"+appData.BANK_TYPE+"`=?;";
		
		PreparedStatement mySql = this.connect.prepareStatement( sql );
		mySql.setString( 1, appData.BID );
		
		ResultSet dataSet = mySql.executeQuery();
		
		while( dataSet.next() )		{
		
			int pid = dataSet.getInt( appData.BANK_PID );
		
			Date postTime = Common.SDF.parse( dataSet.getString( appData.BANK_TIME ) );
			Date today = new Date();
			
			long diff = today.getTime() - postTime.getTime();
			long inHours = diff / (long)(1000 * 60 * 60 );
			
			if( inHours >= 72L )		{
			
				Auction post = new Auction();
				post.reserveProduct( pid );
			
			}//End Of If
		
		}//End Of Loop	
	
	
		/** Checking for Dead post concept */
	
	
		sql = "SELECT * FROM `"+appData.TABLE_POSTS+"`, `"+appData.TABLE_BIDPOST+"` WHERE "+appData.TABLE_POSTS+".`"+appData.POSTS_STATUS+"`=?  AND "+appData.TABLE_POSTS+".`"+appData.POSTS_TYPE+"`=?  AND "+appData.TABLE_POSTS+".`"+appData.POSTS_ETIME+"` < ? AND "+appData.TABLE_POSTS+".`"+appData.POSTS_PID+"` = "+appData.TABLE_BIDPOST+".`"+appData.BPOST_PID+"` ;";
		
		mySql = this.connect.prepareStatement( sql );
		mySql.setString( 1, appData.ACTIVE );
		mySql.setString( 2, appData.BID );
		
		SqlDate now = new SqlDate( new Date() );
		mySql.setTimestamp( 3, now.getTimestamp() );		
		
		dataSet = mySql.executeQuery();	
		
		while( dataSet.next() )			{
		
			int initBid = dataSet.getInt( appData.BPOST_INITIAL );
			int currentAmt = dataSet.getInt( appData.POSTS_AMT );
			int pid = dataSet.getInt( appData.POSTS_PID );
			
			if( initBid == currentAmt )			{
			
				sql = "UPDATE `"+appData.TABLE_POSTS+"` SET `"+appData.POSTS_STATUS+"` = ? WHERE `"+appData.POSTS_PID+"` = ?;";
				mySql = this.connect.prepareStatement( sql );

				mySql.setString( 1, appData.DEAD );				
				mySql.setInt( 2, pid );
				
				mySql.executeUpdate();			
			
			
			}else		{
			
				Auction post = new Auction();
				post.reserveProduct( pid );				
			
			}//End Of If Else
		
		}//End Of Post
		

		dataSet.close();
		mySql.close();
	
	}//End Of Method
	
	
	
	private void cleanInactivePosts() throws Exception	{
	
		List<Post> postToDelete = new ArrayList<Post>();
		Iterator<Post> list = this.posts.iterator();
	
		while( list.hasNext() )		{
		
			Post thisPost = list.next();
			
			String sql = "SELECT * FROM `"+appData.TABLE_POSTS+"` WHERE `"+appData.POSTS_PID+"` = ? AND `"+appData.POSTS_STATUS+"` = ?;";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			mySql.setInt( 1, thisPost.getPid() );
			mySql.setString( 2, appData.ACTIVE );
			
			ResultSet dataSet = mySql.executeQuery();
		
			if( !dataSet.next() )		{
			
				postToDelete.add( thisPost );
				
			}//End Of IF
			
			dataSet.close();
			mySql.close();	
		
		}//End Of Loop	
		
		for( Post p: postToDelete )		{
		
			this.listUpdated = true;
			this.posts.remove( p );
		
		}//End Of Loop	
		
		postToDelete.clear();
	
	}//End Of Method	
	
	
	private boolean checkNewPosts() throws Exception	{
	
		int upcoming = this.query.getNewPid();
		int maxInList = this.query.getMaxPid();
		
		if( maxInList+1 < upcoming )	{
		
			String sql = "SELECT * FROM `"+appData.TABLE_POSTS+"` WHERE `"+appData.POSTS_STATUS+"` = ? AND `"+appData.POSTS_PID+"` > ?;";
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			
			mySql.setString( 1, appData.ACTIVE );
			mySql.setInt( 2, maxInList );
			
			ResultSet userResult = mySql.executeQuery();
			this.query.fetchPosts( userResult );
		
			mySql.close();
			
			return true;
		
		}else		{
			return false;
		}//End Of IF ELSE
		
	}//End Of Method
	
	
	private void refreshTabs() throws Exception	{

		Transaction.homeRefer.removeAllPost( 1 );
		Transaction.homeRefer.removeAllPost( 3 );
		Transaction.homeRefer.removeAllPost( 4 );
	
		Iterator<Post> list = this.posts.iterator();
		
		while( list.hasNext() )			{
		
			Transaction.homeRefer.showPost( list.next() );
		
		}//End Of Loop			

	}//End Of Method
	
	
	
	private void refreshBalance() throws Exception	{

		String sql = "SELECT * FROM `"+appData.TABLE_USERS+"` WHERE `"+appData.USER_USERNAME+"`=?;";
		
		PreparedStatement mySql = this.connect.prepareStatement(sql);
		mySql.setString( 1, Login.username );
		
		ResultSet data = mySql.executeQuery();
		
		if( data.next() )		{
		
			Login.credits = data.getInt( appData.USER_BALANCE );
			Transaction.homeRefer.setCredits();
		
		}//End Of If

	}//End Of Method	
	
}//End Of Class