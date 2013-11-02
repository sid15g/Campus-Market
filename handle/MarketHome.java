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
	
 // Manage Home Page
	
import java.text.SimpleDateFormat;	
import java.awt.event.MouseEvent;
import settings.ImageUpload;
import modules.Transaction;
import java.awt.CardLayout;	
import javax.swing.JTable;
import java.util.Iterator;
import modules.Account;
import modules.Auction;
import modules.Common;
import java.util.Date;
import java.util.List;
import modules.Demand;
import modules.Update;
import settings.Save;
import modules.Login;
import modules.Sale;
import java.io.File;
import data.appData;
import data.Share;
import data.Post;
import data.User;
import view.Home;
	
	
public class MarketHome extends Home implements Runnable		{

	private String username, title, category, type, sDate, eDate, details;
	private static List<Post> postList = appData.PostList;						/** static: to create the variable at compile time i.e. before super(); */
	private final String active = appData.ACTIVE;
	private int amount, rate, selectedPost;
	public static boolean toUpdate = true;
	private SimpleDateFormat SDF;
	private Common query;

	public MarketHome()		{
	
		super();
		this.setLook();
		super.setUserInfo( Login.username, Login.credits );
		setVisible(true);
		
		this.setMyProfile();
		this.query = new Common();
		this.SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	}//End Of Constructor
	
	
	private void setLook()			{
	
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(view.LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(view.LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(view.LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(view.LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }	
	
	}//End Of Method
	
	
	@Override								//Thread
	public void run()			{
	
		try		{
	
			while( toUpdate	)			{
			
				Thread.sleep( 6000 );

				Update update = new Update();
				update.start();				
				update.join();
				
			}//End Of Loop
		
		}catch( Exception ie )			{					//Interrupted Exception
			System.err.println("ERROR: Login Error...Please Try Again!!! ");
		}//End Of Try-Catch		
	
	}//End Of Thread
	
	
	private void setMyProfile()				{
	
		super.setUserProfile(
		
			Login.ME.getUsername(),
			Login.ME.getEmail(),
			Login.ME.getContact(),
			Login.ME.getAddress(),
			Login.ME.getDOB(),
			Login.ME.getCID(),
			Login.imagePath
		
		);
	
	}//End Of Method
	
	
	@Override			//Over ridden from view.Home class to change profile picture
    public String changeProfile()		{
	
		ImageUpload changePic = new ImageUpload();
		Login.imagePath = changePic.getPath();
	
		changePic.dispose();
	
        return Login.imagePath;
    
	}	
	

	@Override			//Over ridden from view.Home class to save users edited profile
    public void saveProfile()			    {

		try		{
		
			UpdateME myProfile = new UpdateME();
			myProfile.setDetails( profile.email, profile.mob, profile.addr );
	
		}catch( Exception e )		{
			Transaction.homeRefer.ph.errorBox("ERROR: Updating Profile");
		}//End Of Try Catch
		
    }//End Of Method	
	
	
	
	@Override			//Over ridden from view.Home class to handle reserved post view
    public void onClickPost( int pid, boolean reserved )			{	
	
		if( reserved )		{
		
			Post p = this.query.getRPost( pid );
			this.setPostVariables(p);
			
			if( this.type.equals( appData.SALE ) )		{

				setPostVars( this.username, this.title, this.amount, this.category, this.type, this.sDate, this.eDate, this.details );	
			
			}else if( this.type.equals( appData.BUY ) )		{
			
				setPostVars( this.username, this.title, this.category, this.type, this.sDate, this.details );	
			
			}else if( this.type.equals( appData.BID ) )		{
			
				setPostVars( this.username, this.title, this.amount, this.rate, this.category, this.type, this.sDate, this.eDate, this.details );		
				
				Share var = new Auction().lastBid( pid );
				Home.ph.usr = var.str[0];
				Home.ph.bidval = var.val[0];
				
			}//End Of If Else			
		
		}else	{
			onClickPost(pid);
		}//End Of If Else
	
	}//End Of Method
	
	
	@Override			//Over ridden from view.Home class to handle post view
    public void onClickPost( int pid )			{
		
		this.selectedPost = pid;
		fetchPostDetails();
		
		if( this.type.equals( appData.SALE ) )		{
		
			Transaction.recentView = 0;
			Transaction.recentPid = pid;
			setPostVars( this.username, this.title, this.amount, this.category, this.type, this.sDate, this.eDate, this.details );	
		
		}else if( this.type.equals( appData.BUY ) )		{
		
			Transaction.recentView = 1;
			Transaction.recentPid = pid;
			setPostVars( this.username, this.title, this.category, this.type, this.sDate, this.details );	
		
		}else if( this.type.equals( appData.BID ) )		{
		
			Transaction.recentView = 2;
			Transaction.recentPid = pid;
			setPostVars( this.username, this.title, this.amount, this.rate, this.category, this.type, this.sDate, this.eDate, this.details );		
			
			Share var = new Auction().lastBid( pid );
			Home.ph.usr = var.str[0];
			Home.ph.bidval = var.val[0];
			
		}//End Of If Else
		
    }//End Of Event Listener	
	
	
	
	private boolean fetchPostDetails()			{

		Iterator<Post> list = this.postList.iterator();
		
		while( list.hasNext() )		{
		
			Post p = list.next();
			int pid = p.getPid();
			
			if( this.selectedPost == pid )			{
			
				this.setPostVariables(p);
				return true;
			
			}//End Of If
		
		}//End Of Loop
		
		return false;
	
	}//End Of Method
	
	
	private void setPostVariables( Post p )			{
	
		this.username = p.getName();
		this.title = p.getTitle();
		this.category = p.getCategory();
		this.type = p.getType();				
		this.sDate = p.getStartTime().toString();
		this.eDate = p.getEndTime().toString();
		this.details = p.getDetails();
		
		if( this.type.equals( appData.SALE ) )		{
		
			this.amount = p.getAmount();
			this.rate = 0;
			
		}else if( this.type.equals( appData.BID ) )		{
		
			this.amount = p.getInitialBid();
			this.rate = p.getBidRate();
			
		}else	{
		
			this.amount = 0;
			this.rate = 0;
			
		}//End Of If Else	
		
	
	}//End Of Method
	
	
	@Override			//Over ridden from view.Home class to show the posters details
    public void setPopupDetails(String username)		{
        
		
		User postUser = this.query.fetchUserDetail( username );

        super.showPopup(
			postUser.getName(),
			postUser.getEmail(),
			postUser.getContact()
		);
		
    }//End Of Method
	
	
	@Override			//Over ridden from view.Home class to search any post
    public void searchPost(String text)			   {
	
		for( Post p: this.postList )			{
		
			String tmp = p.getTitle();
			String date = p.getStartTime().toString();
			String pid = ((Integer)p.getPid()).toString();
		
			if( tmp.toLowerCase().contains( text.toLowerCase() ) )			{ 
			
				super.showFilter(
					
					p.getTitle(),
					p.getDetails(),
					p.getCategory(),
					p.getType(),
					date,
					pid
				
				);
			
			}//End Of IF

		}//End Of Loop
		
    }//End Of Method
	
	
	
	/** Listeners to Show Posts */
	
	
	@Override			//Over ridden from view.Home class acting as event listener for OnSale Button
    public void onClickSaleButton()	    {	
	
		Iterator<Post> list = this.postList.iterator();
		
		while( list.hasNext() )			{
			this.showPost( list.next() );
		}//End Of Loop			
	
	}//End Of Method
	
	
	@Override			//Over ridden from view.Home class acting as event listener for OnAuction Button
    public void onClickAuctionButton()	    {	
	
		Iterator<Post> list = this.postList.iterator();
		
		while( list.hasNext() )			{
			this.showPost( list.next() );
		}//End Of Loop			
	
	}//End Of Method



	@Override			//Over ridden from view.Home class acting as event listener for Demands Button
    public void onClickDemandButton()	    {	
	
		Iterator<Post> list = this.postList.iterator();
		
		while( list.hasNext() )			{
			this.showPost( list.next() );
		}//End Of Loop		
	
	}//End Of Method	
	
	
	/** Listeners to Add Posts */
	
	
	@Override			//Over ridden from view.Home class to add On Sale Posts
    public void onClickSale()			{
	
		Date present, deadline = null;
		int newPid = this.query.getNewPid();
	
		present = new Date();
		String now = this.SDF.format( present );
	
		try		{
		
			String end = post_data.end_date + " " + post_data.end_time;
			deadline = this.SDF.parse( end );
		
		}catch( Exception pe )			{				//ParseException
			deadline = null;
			System.err.println("ERROR: Wrong Date Format...");
		}//End Of Exception
	
		int amt = Integer.parseInt( post_data.price );
	
		Post newPost = new Post( newPid, post_data.type, Login.username, present, deadline, post_data.title, post_data.description, amt, this.active, post_data.category );
		
//		this.showPost( newPost );
		this.query.addPostEntry( newPost );
		super.clearAllField();
		
    }//End Of Method
    
	
	@Override			//Over ridden from view.Home class to add On Bid Posts
    public void onClickAuction()	    {
	
		Date present, deadline = null;
		int newPid = this.query.getNewPid();
	
		present = new Date();
		String now = this.SDF.format( present );
	
		try		{
		
			String end = post_data.end_date + " " + post_data.end_time;
			deadline = this.SDF.parse( end );
		
		}catch( Exception pe )			{				//ParseException
			deadline = null;
			System.err.println("ERROR: Wrong Date Format...");
		}//End Of Exception
	
		int amt = Integer.parseInt( post_data.init_bid );
		int rate = Integer.parseInt( post_data.bid_rate );
	
		Post newPost = new Post( newPid, post_data.type, Login.username, present, deadline, post_data.title, post_data.description, amt, this.active, post_data.category, amt, rate );
		
//		this.showPost( newPost );
		this.query.addPostEntry( newPost );
		super.clearAllField();
		
    }//End Of Method
    
	
	@Override			//Over ridden from view.Home class to add On Demand Posts
    public void onClickDemands()	    {
	
		int newPid = this.query.getNewPid();
	
		Date present = new Date();
		String now = this.SDF.format( present );
		
		int month = present.getMonth() + 3;
		Date deadline = (Date)present.clone();
		deadline.setMonth( month );	
	
		Post newPost = new Post( newPid, post_data.type, Login.username, present, deadline, post_data.title, post_data.description, this.active, post_data.category );
		
//		this.showPost( newPost );
		this.query.addPostEntry( newPost );	
		super.clearAllField();

    }//End Of Method

	
	/** Listeners for Category Clicks */
	
	@Override			//Over ridden from view.Home class to handle post listing when clicked in a particular category
	public void onClickCategory( String category )			{
	
		Iterator<Post> list = this.postList.iterator();
		
		while( list.hasNext() )		{
		
			Post p = list.next();
			String added = p.getStartTime().toString();
			String cat = p.getCategory();
			
			if( cat.equals( category ) )		{
			
				super.showCategory(
				
					p.getTitle(),
					p.getDetails(),
					p.getType(),
					added,
					p.getPid()
				
				);
				
			}//End Of If
		
		}//End Of Loop
	
	}//End Of Method
	
	
	/** Action Events for Trasactions */
	
	
	@Override 						//Over ridden from view.Home class to add a demand
	public void getDemandInfo( int PID, int demandAmount, String details )			{
	
		Demand ask = new Demand();
		if( details.trim().isEmpty() )
			ask.execute( PID, demandAmount );
		else
			ask.execute( PID, demandAmount, details );
			
	}//End Of Method
	
	
	@Override 						//Over ridden from view.Home class to add a bid
	public void getBidInfo( int PID, int bidAmount )			{
	
		Auction bid = new Auction();
		bid.execute( PID, bidAmount );
	
	}//End Of Method

	
	@Override 						//Over ridden from view.Home class to reserve a product on sale.
	public void getBuyInfo( int PID )			{
	
		Sale sell = new Sale();
		sell.execute( PID );	
	
	}//End Of Method	
	

	@Override 						//Over ridden from view.Home class to complete the transaction by entering the PIN/UID after entering his password
    public void completeTransaction( String UID, int PID, String type, String password )				{

		try		{
	
			if( this.query.passwordCheck( password ) )			{
		
				if( type.equals( appData.SALE ) )		{
					Sale sell = new Sale();
					sell.complete( PID, UID );
				}else if( type.equals( appData.BID ) )		{
					Auction bid = new Auction();
					bid.complete( PID, UID );		
				}else if( type.equals( appData.BUY ) )		{
					Demand ask = new Demand();
					ask.complete( PID, UID );
				}//End Of If Else
			
			}else		{
				Transaction.homeRefer.ph.errorBox("ERROR: Username-Password Mismatch");
			}//End Of If Else
		
		}catch( Exception e )	{
			Transaction.homeRefer.ph.errorBox("ERROR: Sorry, Transaction could not be completed, Please Try Again.");
		}//End Of Try Catch
		
    }//End Of Method
	
	@Override			//Over ridden from view.Home class to handle the click by the buyers for viewing UID
	public void getUniqueID( int pid, String password )		{
	
		try		{
	
			if( this.query.passwordCheck( password ) )		{
				Transaction.homeRefer.ph.infoBox( Transaction.getUID( pid ) );
			}else	{
				Transaction.homeRefer.ph.errorBox( " ERROR: Username-Password Mismatch " );
			}//End Of If Else
	
		}catch( Exception e )		{
			Transaction.homeRefer.ph.errorBox("ERROR: Unable to get UID., Please Try Again");
		}//End Of Try Catch
	
	}//End Of Method
	
	
	/** Account Managing Listeners */
	
	
	@Override			//Over ridden from view.Home class to handle reserved post view
    public void listReservePosts()		    {

		new Account().showReservedPosts();
		
    }//End Of Method
	
    
	@Override			//Over ridden from view.Home class to handle user's active 'Sale' post view
    public void listSalePosts()				    {
	
        new Account().showOnSalePosts();
		
    }//End Of Method
	
    
	@Override			//Over ridden from view.Home class to handle user's active 'Auction' post view
    public void listAuctionPosts()				    {
	
		new Account().showOnAuctionPosts();
		
    }//End Of Method
	
    
	@Override			//Over ridden from view.Home class to handle user's active 'Demands' post view
    public void listDemandsPosts()				    {
	
        new Account().showOnDemandPosts();
        
    }//End Of Method
	
	
	@Override			//Over ridden from view.Home class to show the user the quoted prices of other users in his Demand post
    public void getQuoteDetails(int PID)			{
	
		Demand ask = new Demand();
		ask.showDemands( PID );
		
    }//End Of Method
	

	@Override			//Over ridden from view.Home class to show the user the bidders in his Auction post
    public void getCurrentBiddings(int PID)			{

		Auction bid = new Auction();
		bid.showBids( PID );
		
    }//End Of Method	
	
	
	@Override			//Over ridden from view.Home class to help the user to accept the quotation of a user in his Demand post
    public void getAcceptInfo(int PID,String username)			{
	
		Demand ask = new Demand();
		ask.reserveProduct( PID, username );
		
    }//End Of Method
	
	
	/** Logout Button Listener */
	
	
	@Override			//Over ridden from view.Home class to handle Logout button click event
	public void logout()			{
	
		File savedFile = new Save().getSavedDetails();
		
		if( savedFile.exists() )
			savedFile.delete();
		
		this.setVisible(false);
		
	}//End Of Method
	
	
	/** General Methods */
	
	
	public void showPost( Post p )			{
	
		String type = p.getType();
		String added = p.getStartTime().toString();
		String deadline = p.getEndTime().toString();
		
		
		if( type.equals( appData.SALE ) )			{
		
			super.showPost( 
				
				p.getTitle(),
				p.getAmount(),
				p.getCategory(),
				deadline,
				p.getPid()
			
			);
		
		}else if( type.equals( appData.BID ) )			{
		
			super.showPost( 
				
				p.getTitle(),
				p.getInitialBid(),
				p.getBidRate(),
				p.getCategory(),
				deadline,
				p.getPid()
			
			);
		
		}else if( type.equals( appData.BUY ) )			{
		
			super.showPost( 
				
				p.getTitle(),
				p.getDetails(),
				p.getCategory(),
				added,
				p.getPid()
			
			);
		
		}//End Of If Else		
		
	
	}//End Of Method	
	
	
	//To update user's balance in the home view
	public void setCredits()		{
	
		super.setUserInfo( Login.username, Login.credits );
	
	}//End Of Method
	
	
}//End Of Class