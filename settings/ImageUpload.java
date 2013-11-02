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
	
package	settings;
	
 // Process Image Uploading
	
import java.io.File;
import modules.Login;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import javax.swing.filechooser.FileFilter;
	
	
public class ImageUpload extends JFrame implements ActionListener		{

	private int result;
	private String imageName;
	private JFileChooser dailog;
	private MyImageFilter fJavaFilter;
	private File selectedFile, uploadedFile;
	
	public ImageUpload()		{

		this.imageName = Login.username + ".jpg";
		String url = "handle/images/users/" ;
		
		this.uploadedFile = new File( url, imageName );
		this.fJavaFilter = new MyImageFilter();
		
		this.dailog = new JFileChooser();
        this.dailog.setDialogTitle("Choose an Image");
        this.dailog.setFileSelectionMode(JFileChooser.FILES_ONLY );
        this.dailog.setCurrentDirectory( new File(".") );
        this.dailog.setFileFilter(fJavaFilter);	
		this.dailog.addActionListener(this);
		this.result = this.dailog.showOpenDialog(this);
		
	}//End Of Constructor
	
	
	public void actionPerformed( ActionEvent e )	 		{

		try			{
			
			String command = e.getActionCommand();
	
			if( command.equals( JFileChooser.CANCEL_SELECTION ) )		 {
			
				dispose();
			
			}else if( this.result == JFileChooser.APPROVE_OPTION )		 {

				this.selectedFile = this.dailog.getSelectedFile();
				FileInputStream fileInputStream = new FileInputStream(this.selectedFile);
				BufferedImage buffer = ImageIO.read( fileInputStream );
				
				if( this.uploadedFile.exists() )
					this.uploadedFile.delete();
				
				ImageIO.write( buffer, "jpg", this.uploadedFile );
				
			}
		
		}catch( Exception flne )		{			//FileNotFoundException 
			System.err.println("ERROR: Error uploading file...Please Ckeck the file Extension");
			dispose();
		}//End Of Try Catch
	
	}//End Of Listener
	
	
	class MyImageFilter extends FileFilter			{

		public boolean accept(File f) 		{
		
			return f.getName().toLowerCase().endsWith(".png") || f.getName().toLowerCase().endsWith(".jpg")	|| f.getName().toLowerCase().endsWith(".gif");
			
		}

		public String getDescription()	 {
			return "Image files (*.png)(*.jpg)(*.gif)";
		}

	}//End Of Inner Class

	
	public static void main( String args[] )		{
	
		ImageUpload f = new ImageUpload();
		f.dispose();
		
	}//End Of Main
	
	
	public String getPath()			{
	
		return "../handle/images/users/"+this.imageName;
	
	}//End Of Method
	
	
}//End Of Class