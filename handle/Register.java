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

// Handles the Sign Up View Page Event
	
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import modules.Connect;
import modules.Common;
import java.util.Date;
import data.appData;
import view.SignUp;
	
public class Register extends SignUp	{

	private boolean unique, passwordMatch, validEmail, validMobile, validDOB;
	private String username, password, fullName, email, rollNo;
	private LoginAction resume;
	private Connection connect;
	private long mobile;
	private Date dob;

	public Register()	{
	
		super();
		resume = null;
		this.setLook();
		setVisible(false);
		this.connect = Connect.getConnection();
	
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
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }	
	
	}//End Of Method	
	
	

	@Override			//Over ridden from view.SignUp class to perform action after pressing OK button
	public void jButton1ActionPerformed(ActionEvent evt) 		{
	
		this.passwordMatch = this.validEmail = this.validMobile = this.validDOB = false;
		this.unique = true;
		
		this.username = jTextField1.getText();
		checkUniqueness( appData.USER_USERNAME, username );
		
		String pass1 = jPasswordField1.getText();
		String pass2 = jPasswordField3.getText();
		
		if( pass1.equals( pass2 ) )		{
			this.passwordMatch = true;
			this.password = pass1;
		}
		
		this.fullName = jTextField2.getText();
		
		this.email = jTextField6.getText();
		isValidEmail( email );
		
		String mob = jTextField8.getText();	
		this.mobile = convertString( mob );
		
		this.rollNo = jTextField7.getText();
		checkUniqueness( appData.USER_CID, rollNo );
		
		try		{
		
			int year = Integer.parseInt( jTextField9.getText() );
			int month = Integer.parseInt( jTextField11.getText() );
			int date = Integer.parseInt( jTextField10.getText() );
			
			String DOB = ((Integer)year).toString()+"-"+((Integer)month).toString()+"-"+((Integer)date).toString()+" 00:00:00";
			
			this.dob = Common.SDF.parse( DOB );
			
			this.validDOB = true;
			
		}catch( Exception e )		{
			System.out.println( "Error: Invalid Date Of Birth" );
			this.dob = new Date();
			this.validDOB = false;
		}//End Of Try Catch
		
		if( this.unique && this.passwordMatch && this.validEmail && this.validMobile && this.validDOB )		{
		
			if( registerUser() )		{
				resume.finalize( this.username );
			}else		{
				resume.setVisible( true );
			}//End Of If Else
			
			setVisible( false );
			dispose();
			
		}else	{
		
			System.err.println("ERROR: Please check the fields again...");
		
		}//End Of If-Else

	}//End Of Listener


	
	@Override			//Over ridden from view.SignUp class to perform action after pressing CANCEL button
	public void jButton2ActionPerformed(ActionEvent evt) 		{
	
		setVisible( false );
		dispose();
		resume.setVisible( true );
	
	}//End Of Listener
	
	
	private void checkUniqueness( String field, String str )		{
	
		if( field.isEmpty() || str.isEmpty() )		{
			this.unique = false;
		}else			{
	
			try		{	
			
				String sql = "SELECT `"+field+"` FROM `"+appData.TABLE_USERS+"` WHERE `"+field+"`=?;";
				
				PreparedStatement mySql = this.connect.prepareStatement( sql );
				mySql.setString( 1, str );
				
				ResultSet userResult = mySql.executeQuery();
				String values;
				
				while( userResult.next() )		{
				
					this.unique = false;
					break;
				
				}//End Of Loop
			
				userResult.close();
				mySql.close();
			
			}catch( Exception e )	{					//SQLException
				System.err.println("ERROR: Required fields already exists");
			}//End Of Try-Catch	
			
			
		}//End Of If-Else
	
	}//End Of Method
	

	
	private void isValidEmail( String str )		{

		if( !str.isEmpty() )		{
	
			int at = str.indexOf( '@' );
			
			if( at > 0 )		{
			
				int dot = str.indexOf( '.', at );
				
				if( dot > 0 )
					this.validEmail = true;
				
			}
			
		}//End Of If
		
		if( this.validEmail && this.unique )		{
		
			checkUniqueness( appData.USER_EMAIL, str );
			
			if( this.unique == false )		{
				this.validEmail = false;
				this.unique = true;
			}
			
		}//End Of Main If

	}//End Of Method
	
	
	
	private long convertString( String str )		{
	
		int len = str.length();
		long num = 0;
		int digit = 0;
		
		if(len == 10)		{
			
			for( int i=0; i< len; i++ )		{
			
				digit = (int)str.charAt(i) - 48;
				num = (num*10) + digit;
			
			}//End Of Loop
			
			this.validMobile = true;
			
			if( this.validMobile && this.unique )		{
			
				checkUniqueness( appData.USER_MOBILE, str );
				
				if( this.unique )
					return num;
			
			}
			
		}//End Of Main If
			
		this.validMobile = false;
		this.unique = true;
		return 0L;
			
	}//End Of Method
	
	
	
	private boolean registerUser()			{
	
		try		{
		
			String sql = "INSERT INTO `"+appData.TABLE_USERS+"` ( `"+appData.USER_USERNAME+"`, `"+appData.USER_PASSWORD+"`, `"+appData.USER_NAME+"`, `"+appData.USER_EMAIL+"`, `"+appData.USER_MOBILE+"`, `"+appData.USER_ADDRESS+"`, `"+appData.USER_DOB+"`, `"+appData.USER_CID+"`, `"+appData.USER_BALANCE+"`, `"+appData.USER_TRANS+"`, `"+appData.USER_VERIFIED+"` ) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
			
			PreparedStatement mySql = this.connect.prepareStatement( sql );
			
			mySql.setString( 1, this.username );
			mySql.setString( 2, this.password );
			mySql.setString( 3, this.fullName );
			mySql.setString( 4, this.email );
			mySql.setLong( 5, this.mobile );
			mySql.setString( 6, "" );
			
			try			{
			
			SqlDate conv = new SqlDate( this.dob );
			mySql.setTimestamp( 7, conv.getTimestamp( true ) );
			
			}catch( Exception e )	{					//SQLException
				System.err.println("ERROR: Unknown Date Of Birth.");
			}//End Of Try-Catch	
			
			mySql.setString( 8, this.rollNo );
			mySql.setInt( 9, 0 );
			mySql.setInt( 10, 0 );
			mySql.setInt( 11, 0 );
			
			mySql.executeUpdate();

			mySql.close();
			
			return true;
	
		}catch( Exception e )	{					//SQLException
			System.err.println("ERROR: Completing the Registration, Check the fields again.");
		}//End Of Try-Catch		
		
		return false;
	
	}//End Of Method
	
	
	
	protected void saveObject( LoginAction obj )		{
	
		this.resume = obj;
	
	}//End Of Method
	
	
}//End Of Class