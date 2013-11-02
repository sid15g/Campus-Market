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
	
 // Class is made to Use some common SQL queries or functionalities

import data.Post;
import data.User;	
import data.appData;
import handle.SqlDate;	
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
	
	
public class Common 					{

	public static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private List<Post> posts = appData.PostList;
	protected static int max = 0;
	private Connection connect;
	
	public Common()		{
	
		this.connect = Connect.getConnection();
	
	}//End Of Constructor
	
	
	
	public int getNewPid()			{
	
		int pid;

		try		{
	
			String sql = "SELECT MAX(`"+appData.POSTS_PID+"`) AS `MaxPid` FROM `"+appData.TABLE_POSTS+"`;";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			ResultSet dataResult = mySql.executeQuery();
			
			dataResult.next();	
			pid = dataResult.getInt( "MaxPid" );
			
			
			dataResult.close();
			mySql.close();
	
		}catch( Exception e )		{				//SQLException
			pid = -1;
			e.printStackTrace();
		}//End Of Try-Catch			
	
		pid++;
		return pid;
	
	}//End Of Method
	
	
	public void addPostEntry( Post p )			{
	
		SqlDate changeDate;
		String type;
		int pid;
	
		try		{
	
			String sql = "INSERT INTO `"+appData.TABLE_POSTS+"` ( `"+appData.POSTS_PID+"`, `"+appData.POSTS_TYPE+"`, `"+appData.POSTS_USERNAME+"`, `"+appData.POSTS_STIME+"`, `"+appData.POSTS_ETIME+"`, `"+appData.POSTS_TITLE+"`, `"+appData.POSTS_DETAILS+"`, `"+appData.POSTS_AMT+"`, `"+appData.POSTS_STATUS+"`, `"+appData.POSTS_CATEGORY+"` ) VALUES (?,?,?,?,?,?,?,?,?,?);";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			
			pid = p.getPid();
			mySql.setInt( 1, pid );
			
			type = p.getType();
			mySql.setString( 2, type );
			
			mySql.setString( 3, p.getName() );
			
			changeDate = new SqlDate( p.getStartTime() );
			mySql.setTimestamp( 4, changeDate.getTimestamp() );
			
			changeDate = new SqlDate( p.getEndTime() );
			mySql.setTimestamp( 5, changeDate.getTimestamp() );
			
			mySql.setString( 6, p.getTitle() );
			mySql.setString( 7, p.getDetails() );
			mySql.setInt( 8, p.getAmount() );
			mySql.setString( 9, p.getStatus() );
			mySql.setString( 10, p.getCategory() );
			
			mySql.executeUpdate();
			mySql.close();
			
			if( type.equals( appData.BID ) )		{
				addBidPostEntry( p );
			}
	
		}catch( Exception e )		{				//SQLException
			pid = 0;
			type = "";
			Transaction.homeRefer.ph.errorBox("ERROR: Could not complete the process. Check the fields.");
		}//End Of Try-Catch	
	
		this.posts.add( p );
	
	}//End Of Method
	
	
	
	private void addBidPostEntry( Post p ) throws Exception		{
	
		int pid = p.getPid();
		
		String sql = "INSERT INTO `"+appData.TABLE_BIDPOST+"` ( `"+appData.BPOST_PID+"`, `"+appData.BPOST_INITIAL+"`, `"+appData.BPOST_RATE+"`, `"+appData.BPOST_OLD+"` ) VALUES (?,?,?,?);";	

		PreparedStatement mySql = this.connect.prepareStatement( sql );	
		mySql.setInt( 1, pid );
		mySql.setInt( 2, p.getInitialBid() );
		mySql.setInt( 3, p.getBidRate() );
		mySql.setInt( 4, 0 );
		
		mySql.executeUpdate();
		mySql.close();
	
	}//End Of Method



	public Post getPost( int pid )			{			
		
		Iterator<Post> list = this.posts.iterator();
		
		while( list.hasNext() )		{
		
			Post p = list.next();
			int id = p.getPid();
			
			if( id == pid )
				return p;
			
		}//End Of Loop

		return null;
	
	}//End Of Method
	
	
	public Post getRPost( int pid )			{
	
		Post p = null;
		
		try			{
	
			String sql = "SELECT * FROM `"+appData.TABLE_POSTS+"` WHERE `"+appData.POSTS_PID+"`= ? AND `"+appData.POSTS_STATUS+"`= ?;";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			mySql.setInt( 1, pid );
			mySql.setString( 2, appData.BOOKED );
			
			ResultSet userResult = mySql.executeQuery();
	
			if( userResult.next() )			{
			
				String time;
				String type = userResult.getString( appData.POSTS_TYPE );
				String user = userResult.getString( appData.POSTS_USERNAME );
			
				time = userResult.getString( appData.POSTS_STIME );				
				Date sTime = this.SDF.parse( time );

				time = userResult.getString( appData.POSTS_ETIME );				
				Date eTime = this.SDF.parse( time );

				String title = userResult.getString( appData.POSTS_TITLE );
				String details = userResult.getString( appData.POSTS_DETAILS );
				int amount = userResult.getInt( appData.POSTS_AMT );
				String status = userResult.getString( appData.POSTS_STATUS );
				String category = userResult.getString( appData.POSTS_CATEGORY );

				if( type.equals( appData.BID ) )		{
				
					sql = "SELECT * FROM `"+appData.TABLE_BIDPOST+"` WHERE `"+appData.BPOST_PID+"`= ?;";
					
					mySql = connect.prepareStatement(sql);
					mySql.setInt( 1, pid );
					ResultSet dataSet = mySql.executeQuery();
					
					if( dataSet.next()	)		{
						
						int initialBid = dataSet.getInt( appData.BPOST_INITIAL );
						int rate = dataSet.getInt( appData.BPOST_RATE );					
						
						p = new Post( pid, type, user, sTime, eTime, title, details, amount, status, category, initialBid, rate);
						
					}
					
					userResult.close();
					dataSet.close();
					mySql.close();
				
				}else		{

					p = new Post( pid, type, user, sTime, eTime, title, details, amount, status, category );
					
				}//End Of If Else
				
				
			}//End Of IF
			
		}catch( Exception e )		{				//SQLException
			e.printStackTrace();
		}//End Of Try Catch
		
		return p;
	
	}//End Of Method
	
	
	public int getMaxPid()		{
	
		Iterator<Post> list = this.posts.iterator();
		
		while( list.hasNext() )		{
		
			Post p = list.next();
			int id = p.getPid();
			
			if( id > max )
				max = id;
				
		}//End Of Loop
		
		return max;
	
	}//End Of Method
	
	

	public int getNewTid()			{
	
		int tid;

		try		{
	
			String sql = "SELECT MAX(`"+appData.BANK_TID+"`) AS `MaxTid` FROM `"+appData.TABLE_BANK+"`;";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			ResultSet dataResult = mySql.executeQuery();
			
			dataResult.next();			
			tid = dataResult.getInt( "MaxTid" );
			
			dataResult.close();
			mySql.close();
	
		}catch( Exception e )		{				//SQLException
			tid = -1;
			e.printStackTrace();
		}//End Of Try-Catch			
	
		tid++;
		return tid;
	
	}//End Of Method
	
	
	
	public void fetchPosts( ResultSet userResult )	throws Exception		{
	
		List<Post> posts = appData.PostList;
			
		while( userResult.next() )			{
		
			String time;
			int pid = userResult.getInt( appData.POSTS_PID );
			String type = userResult.getString( appData.POSTS_TYPE );
			String user = userResult.getString( appData.POSTS_USERNAME );
			
			time = userResult.getString( appData.POSTS_STIME );				
			Date sTime = this.SDF.parse( time );
			
			time = userResult.getString( appData.POSTS_ETIME );				
			Date eTime = this.SDF.parse( time );

			String title = userResult.getString( appData.POSTS_TITLE );
			String details = userResult.getString( appData.POSTS_DETAILS );
			int amount = userResult.getInt( appData.POSTS_AMT );
			String status = userResult.getString( appData.POSTS_STATUS );
			String category = userResult.getString( appData.POSTS_CATEGORY );

			if( type.equals( appData.BID ) )		{
			
				String sql = "SELECT * FROM `"+appData.TABLE_BIDPOST+"` WHERE `"+appData.BPOST_PID+"`= ?;";
				
				PreparedStatement mySql = connect.prepareStatement(sql);
				mySql.setInt( 1, pid );

				ResultSet dataSet = mySql.executeQuery();
				
				if( dataSet.next()	)		{
					
					int initialBid = dataSet.getInt( appData.BPOST_INITIAL );
					int rate = dataSet.getInt( appData.BPOST_RATE );					
					
					posts.add( new Post( pid, type, user, sTime, eTime, title, details, amount, status, category, initialBid, rate ) );
					
				}
				
				dataSet.close();
				mySql.close();
			
			}else		{

				posts.add( new Post( pid, type, user, sTime, eTime, title, details, amount, status, category ) );
				
			}//End Of If Else
			
		}//End Of While Loop
	
	}//End Of Method

	
	public User fetchUserDetail( String username )		{
		
		User u = null;
		
		try		{
		
			String sql = "SELECT * FROM `"+appData.TABLE_USERS+"` WHERE `"+appData.USER_USERNAME+"` = ?;";
			PreparedStatement mySql = connect.prepareStatement(sql);	
			mySql.setString( 1, username );
			
			ResultSet userResult = mySql.executeQuery();
		
			if( userResult.next() )		{

				String name = userResult.getString(appData.USER_NAME);
				String email = userResult.getString(appData.USER_EMAIL);
				long mobile = userResult.getLong(appData.USER_MOBILE);
				String address = userResult.getString(appData.USER_ADDRESS);
				String collegeId = userResult.getString(appData.USER_CID);
				Date dob = Common.SDF.parse( userResult.getString(appData.USER_DOB) );
				int balance = userResult.getInt(appData.USER_BALANCE);
				int transactions = userResult.getInt(appData.USER_TRANS);
				int verified = userResult.getInt(appData.USER_VERIFIED);
				
				u = new User(Login.username, name, email, mobile, address, collegeId, dob, balance, transactions, verified);
			
			}//End Of If
		
			userResult.close();
			mySql.close();
		
		}catch( Exception e )		{				//SQLException
			e.printStackTrace();
		}//End Of Try-Catch		
		
		return u;
		
	}//End Of Method
	
	
	public boolean passwordCheck( String password )	throws Exception		{
	
		boolean correct = false;
		
		String sql = "SELECT * FROM `"+appData.TABLE_USERS+"` WHERE `"+appData.USER_USERNAME+"`=? AND `"+appData.USER_PASSWORD+"`=?;";
		PreparedStatement mySql = connect.prepareStatement(sql);	
		mySql.setString( 1, Login.username );
		mySql.setString( 2, password );
		
		ResultSet userResult = mySql.executeQuery();
	
		if( userResult.next() )		{

			correct = true;
		
		}//End Of If
	
		userResult.close();
		mySql.close();		
		
		return correct;		
	
	}//End Of Method
	
	
	
	public int getNewBidPid( int oldPid ) throws Exception			{
	
		String sql = "SELECT * FROM `"+appData.TABLE_BIDPOST+"` WHERE `"+appData.BPOST_OLD+"`=?;";
		int newPid = -1;
		
		PreparedStatement mySql = this.connect.prepareStatement( sql );	
		mySql.setInt( 1, oldPid );
		
		ResultSet dataSet = mySql.executeQuery();
		
		if( dataSet.next() )		{
		
			newPid = dataSet.getInt( appData.BPOST_PID );
		
		}//End Of If Else
		
		return newPid;
	
	}//End Of Method
	
	
}//End Of Class