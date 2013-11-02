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
	
package	data;	
	
 // Interface containing Application related Data Information
	
import java.util.List;
import java.util.LinkedList;	
	
	
public abstract interface appData	{
	
	public static final String TABLE_BANK = "bank";
	public static final String TABLE_BIDPOST = "bidpost";
	public static final String TABLE_BIDS = "bids";
	public static final String TABLE_DEMANDS = "demands";
	public static final String TABLE_POSTS = "posts";	
	public static final String TABLE_USERS = "users";
	
	public static final String BANK_TID = "TID";
	public static final String BANK_PIN = "pin";
	public static final String BANK_SELLER = "seller";
	public static final String BANK_AMT = "amount";
	public static final String BANK_BUYER = "buyer";
	public static final String BANK_PID = "PID";
	public static final String BANK_TYPE = "postType";
	public static final String BANK_TIME = "time";

	public static final String BPOST_PID = "PID";	
	public static final String BPOST_INITIAL = "initial";	
	public static final String BPOST_RATE = "rate";
	public static final String BPOST_OLD = "oldPid";	
	
	public static final String BIDS_PID = "PID";
	public static final String BIDS_USERNAME = "username";
	public static final String BIDS_AMT = "amount";
	public static final String BIDS_TIME = "time";

	public static final String DEMAND_PID = "PID";
	public static final String DEMAND_USERNAME = "username";
	public static final String DEMAND_AMT = "amount";
	public static final String DEMAND_DETAIL = "details";
	public static final String DEMAND_TIME = "time";
	public static final String DEMAND_STATUS = "status";
	
	public static final String POSTS_PID = "PID";
	public static final String POSTS_TYPE = "type";
	public static final String POSTS_USERNAME = "username";
	public static final String POSTS_STIME = "sTime";
	public static final String POSTS_ETIME = "eTime";
	public static final String POSTS_TITLE = "title";	
	public static final String POSTS_DETAILS = "details";	
	public static final String POSTS_AMT = "amount";	
	public static final String POSTS_STATUS = "status";	
	public static final String POSTS_CATEGORY = "category";	
	
	public static final String USER_USERNAME = "username";
	public static final String USER_PASSWORD = "password";
	public static final String USER_NAME = "fullName";
	public static final String USER_EMAIL = "email";
	public static final String USER_MOBILE = "mobile";
	public static final String USER_ADDRESS = "address";
	public static final String USER_CID = "collegeID";
	public static final String USER_DOB = "DOB";
	public static final String USER_BALANCE	= "balance";
	public static final String USER_TRANS = "transactions";	
	public static final String USER_VERIFIED = "verified";
	
	public static final String SALE = "Sale";	
	public static final String BID = "Auction";	
	public static final String BUY = "Demands";	
	
	public static final String ACTIVE = "active";
	public static final String BOOKED = "reserved";
	public static final String DONE = "done";
	public static final String DEAD = "dead";
	
	public static final String ACCEPT = "accepted";
	public static final String WAIT = "waiting";
	public static final String DENY = "rejected";
	
	public static final String ADMIN = "admin";
	
	public static final String FOOD = "Food";
	public static final String FUN = "Entertainment";
	public static final String COSMO = "Cosmetics";
	public static final String STNRY = "Stationary";
	public static final String GDGT = "Gadgets";
	public static final String SPRT = "Sports";
	public static final String OTHER = "Others";
	
	public static List<Post> PostList = new LinkedList<Post>();	
	
}//End Of Class