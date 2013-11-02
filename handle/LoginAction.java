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
	
 // Handles the Login View Page Event
	
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import view.LogIn;
	
public class LoginAction extends LogIn	{

	public static boolean DONE = false;
	private String username, password;
	private Register newUser;
	private boolean save;

	public LoginAction()	{
	
		super();
		this.setLook();
	
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
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LogIn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }	
	
	}//End Of Method
	
	
	
	@Override			//Over ridden from view.LogIn class to handle TextField Events
	public void jButton1ActionPerformed(ActionEvent evt) 		{
	
		this.username = jTextField1.getText();
		this.password = jPasswordField1.getText();
		
		this.newUser.dispose();
		this.DONE = true;
	
	}//End Of Listener
	
	
	@Override			//Over ridden from view.LogIn class to handle CheckBox Events
	public void jCheckBox1ActionPerformed(ActionEvent evt) 		{
	
		this.save = jCheckBox1.isSelected();
	
	}//End Of Listener
	
	
	@Override			//Over ridden from view.LogIn class to handle Sign Up Procedure
	public void jLabel4MouseClicked(MouseEvent evt) 		{
	
		setVisible( false );
		this.newUser.setVisible(true);
	
	}//End Of Listener
	
	
	public String getUsername()		{
	
		return this.username;
	
	}//End Of Method
	
	
	public String getPassword()		{
	
		return this.password;
	
	}//End Of Method
	

	public boolean getSavedStatus()		{
	
		return this.save;
	
	}//End Of Method

	
	public void saveObject( LoginAction obj )		{
	
		this.newUser = new Register();
		this.newUser.saveObject( obj );
	
	}//End Of Method	
	
	
	public void finalize( String username )		{
	
		jTextField1.setText( username );
		setVisible( true );
	
	}//End Of Method	
	
	
	
}//End Of Class