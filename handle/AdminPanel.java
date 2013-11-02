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
	
 // Handles Admin panel events

import errors.NetConnectionException; 
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import view.Settings;
import data.Share;


public class AdminPanel extends Settings		{

	private String domain, database, username, password;
	private boolean isClicked;
	private InetAddress IP;
	private int portNum;

	public AdminPanel()		{
	
		super();
		setVisible(true);
		
		this.isClicked = false;
	
	}//End Of Constructor
	
	
	@Override			//Over ridden from view.Settings class to handle CANCEL Button Event
    public void jButton2ActionPerformed(ActionEvent evt) 		{
	
		setVisible( false );
		dispose();
		System.exit(0);
        
    }//End Of Listener

	
	@Override			//Over ridden from view.Settings class to handle SAVE Button Event
    public void jButton3ActionPerformed(ActionEvent evt) 		{
	
		this.domain = jTextField1.getText();
		this.database = jTextField2.getText();
		this.username = jTextField3.getText();
		this.password = jTextField4.getText();
		
		if( this.domain.isEmpty() || this.database.isEmpty() || this.username.isEmpty() || this.password.isEmpty() )		{
		
			System.err.print("ERROR: Please fill the required fields");
			this.isClicked = false;
		
		}else	{
		
			try		{
			
				byte[] IPAddr = getIP( domain );			
				byte[] localhost = {127, 0, 0, 1};
				
				InetAddress local = InetAddress.getByAddress( localhost );
				this.IP = InetAddress.getByAddress( IPAddr );
				
				if( this.IP.equals( local ) )
					this.domain = "localhost";
				else
					this.domain = this.IP.getCanonicalHostName();
				
				this.isClicked = true;
				
			}catch( Exception e )		{							//TransformerConfigurationException
			
				if( isValidDomain( domain ) )		{
					this.isClicked = true;
				}else	{
					this.isClicked = false;
				}
			
			}//End Of Try Catch
		
		}//End Of If Else
		
    }//End Of Listener
	
	
	public Share<InetAddress> getInputs()			{

		while( !isClicked )		{
		
			try		{
				wait();
			}catch(Exception e){}
			
		}//End Of Loop
		
		Share<InetAddress> variables = new Share<InetAddress>( 4, 0 );
		
		variables.str[0] = this.domain;
		variables.extra = this.IP;
		variables.str[1] = this.database;
		variables.str[2] = this.username;
		variables.str[3] = this.password;
		
		return variables;	

	}//End Of Method

	
	
	private byte[] getIP( String ipAddr ) throws Exception	{
	
		byte IParr[] = new byte[4];
		byte segment=0;
		char ch;
		int j=0;
		
		ipAddr += ".";
		
		for( int i=0; i<4; i++ )		{
		
			while( (ch=ipAddr.charAt(j))!='.' )	{
			
				segment *= 10;
				segment += (byte)ch - 48;
				j++;
			
			}//End Of While
			
			IParr[i] = segment;
			segment = 0;
			j++;
	
		}//End Of For(i)	
	
		return IParr;	
	
	}//End Of Method	
	
	
	
	private boolean isValidDomain( String domain )	{
	
		boolean valid = false;

		try		{
			this.IP = InetAddress.getByName( domain );
			valid = true;
		}catch( Exception uhe )			{							//UnknownHostException
			
			try		{
				throw new NetConnectionException();
			}catch( Exception nce )		{
				System.err.println(nce.getMessage());
			}
			
		}//End Of Try Catch
		
		return valid;
	
	}//End Of Method	
	
	
}//End Of Class