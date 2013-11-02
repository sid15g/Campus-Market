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
	
 // Handle user account processing

import data.appData; 
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
	
public class Account		{

	private Connection connect;

	public Account()	{
	
		this.connect = Connect.getConnection();
	
	}//End Of Constructor
	
	
	public void showReservedPosts()		{
	
		try			{
		
			String sql;
			boolean admin = false;
			
			if( !Login.username.equals( appData.ADMIN ) )		{
			
				sql = "SELECT * FROM `"+appData.TABLE_POSTS+"`,`"+appData.TABLE_BANK+"` WHERE `"+appData.TABLE_BANK+"`."+appData.BANK_PID+" = `"+appData.TABLE_POSTS+"`."+appData.POSTS_PID+" AND `"+appData.TABLE_POSTS+"`."+appData.POSTS_STATUS+" = ? AND ( `"+appData.TABLE_BANK+"`."+appData.BANK_BUYER+" = ? OR `"+appData.TABLE_BANK+"`."+appData.BANK_SELLER+" = ? );";
				
			}else		{
			
				sql = "SELECT * FROM `"+appData.TABLE_POSTS+"`,`"+appData.TABLE_BANK+"` WHERE `"+appData.TABLE_BANK+"`."+appData.BANK_PID+" = `"+appData.TABLE_POSTS+"`."+appData.POSTS_PID+" AND `"+appData.TABLE_POSTS+"`."+appData.POSTS_STATUS+" = ?;";				
			
				admin = true;
			
			}//End Of If Else
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			mySql.setString( 1, appData.BOOKED );
			
			if( !admin )		{
				mySql.setString( 2, Login.username );
				mySql.setString( 3, Login.username );
			}
			
			ResultSet postSet = mySql.executeQuery();
			
			while( postSet.next() )			{
			
				Date time = Common.SDF.parse( postSet.getString( appData.BANK_TIME ) );
			
				String reservedBy, type;
				
				type = postSet.getString( appData.POSTS_TYPE );
				
				if( type.equals( appData.BUY ) )			{
					reservedBy = postSet.getString( appData.BANK_SELLER );
				}else	{
					reservedBy = postSet.getString( appData.BANK_BUYER );
				}//End Of If Else
				
				Transaction.homeRefer.showAccountPost(
				
					postSet.getString( appData.POSTS_TITLE ),
					postSet.getString( appData.POSTS_CATEGORY ),
					type,
					postSet.getString( appData.POSTS_USERNAME ),
					reservedBy,
					time.toString(),
					postSet.getInt( appData.POSTS_PID )
				
				);
			
			}//End Of Loop
			
			postSet.close();
			mySql.close();
		
		}catch( Exception e )	{				//SQLException
			e.printStackTrace();
		}//End Of Try Catch
	
	}//End Of Method
	
	
	
	public void showOnAuctionPosts()		{
	
		try			{
		
			String sql = "SELECT * FROM `"+appData.TABLE_POSTS+"`, `"+appData.TABLE_BIDPOST+"` WHERE `"+appData.TABLE_POSTS+"`."+appData.POSTS_PID+" = `"+appData.TABLE_BIDPOST+"`."+appData.BPOST_PID+" AND `"+appData.TABLE_POSTS+"`."+appData.POSTS_STATUS+" = ? AND `"+appData.TABLE_POSTS+"`."+appData.POSTS_USERNAME+" = ? AND `"+appData.TABLE_POSTS+"`."+appData.POSTS_TYPE+" = ?;";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			mySql.setString( 1, appData.ACTIVE );
			mySql.setString( 2, Login.username );
			mySql.setString( 3, appData.BID );
	
			ResultSet postSet = mySql.executeQuery();
			
			while( postSet.next() )			{
			
				Date time = Common.SDF.parse( postSet.getString( appData.POSTS_STIME ) );
			
				Transaction.homeRefer.showAccountPost(
				
					postSet.getString( appData.POSTS_TITLE ),
					postSet.getInt( appData.BPOST_INITIAL ),
					postSet.getInt( appData.BPOST_RATE ),
					postSet.getString( appData.POSTS_CATEGORY ),
					time.toString(),
					postSet.getInt( appData.POSTS_PID )
				
				);
			
			}//End Of Loop
			
			postSet.close();
			mySql.close();
		
		}catch( Exception e )	{				//SQLException
			e.printStackTrace();
		}//End Of Try Catch
	
	}//End Of Method	
	
	
	
	public void showOnDemandPosts()		{
	
		try			{
		
			String sql = "SELECT * FROM `"+appData.TABLE_POSTS+"` WHERE "+appData.POSTS_STATUS+" = ? AND "+appData.POSTS_USERNAME+" = ? AND "+appData.POSTS_TYPE+" = ?;";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			mySql.setString( 1, appData.ACTIVE );
			mySql.setString( 2, Login.username );
			mySql.setString( 3, appData.BUY );
	
			ResultSet postSet = mySql.executeQuery();
			
			while( postSet.next() )			{
			
				Date time = Common.SDF.parse( postSet.getString( appData.POSTS_STIME ) );
			
				Transaction.homeRefer.showAccountPost(
				
					postSet.getString( appData.POSTS_TITLE ),
					postSet.getString( appData.POSTS_DETAILS ),
					postSet.getString( appData.POSTS_CATEGORY ),
					time.toString(),
					postSet.getInt( appData.POSTS_PID )
				
				);
			
			}//End Of Loop
			
			postSet.close();
			mySql.close();
		
		}catch( Exception e )	{				//SQLException
			e.printStackTrace();
		}//End Of Try Catch
	
	}//End Of Method	
	
	

	public void showOnSalePosts()		{
	
		try			{
		
			String sql = "SELECT * FROM `"+appData.TABLE_POSTS+"` WHERE "+appData.POSTS_STATUS+" = ? AND "+appData.POSTS_USERNAME+" = ? AND "+appData.POSTS_TYPE+" = ?;";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			mySql.setString( 1, appData.ACTIVE );
			mySql.setString( 2, Login.username );
			mySql.setString( 3, appData.SALE );
	
			ResultSet postSet = mySql.executeQuery();
			
			while( postSet.next() )			{
			
				Date time = Common.SDF.parse( postSet.getString( appData.POSTS_STIME ) );
			
				Transaction.homeRefer.showAccountPost(
				
					postSet.getString( appData.POSTS_TITLE ),
					postSet.getInt( appData.POSTS_AMT ),
					postSet.getString( appData.POSTS_CATEGORY ),
					time.toString(),
					postSet.getInt( appData.POSTS_PID )
				
				);
			
			}//End Of Loop
			
			postSet.close();
			mySql.close();
		
		}catch( Exception e )	{				//SQLException
			e.printStackTrace();
		}//End Of Try Catch
	
	}//End Of Method		
	
	
}//End Of Class