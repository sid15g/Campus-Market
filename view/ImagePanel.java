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
 
package view;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.net.URL;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ImagePanel extends JPanel{
    
    private BufferedImage src;
    private BufferedImage image;
    private String image_path = "../handle/images/";
  
    public void setProfileImage(String path)
    {
        Dimension d = this.getPreferredSize();
        try{
            src = ImageIO.read(getClass().getResource(path));
            image = resize(src,d.width,d.height);
            repaint();
        }
        catch(Exception e)
        {
            try {
                src = ImageIO.read(getClass().getResource(image_path + "me.png"));
                image = resize(src,d.width,d.height);
                repaint();
            } catch (Exception ex) {
                Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        
    }
    
    public void setHomeProfile(String path)
    {
        Dimension d = this.getPreferredSize();
        try{
            src = ImageIO.read(getClass().getResource(path));
            image = resize(src,d.width,d.height);
            repaint();
        }
        catch(Exception e)
        {
            try {
                src = ImageIO.read(getClass().getResource(image_path + "me.png"));
                image = resize(src,d.width,d.height);
                repaint();
            } catch (IOException ex) {
                Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } 
        
    }
    
    
    
    
    
   public void setImage(URL path)
    {
        try{
            src = ImageIO.read(path);
            repaint();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
  
   public ImagePanel()
   {
       
   }
    public ImagePanel(URL path)
    {
        try{
            src = ImageIO.read(path);
            //System.out.println("width= "+getWidth()+"  Height=  "+getHeight());
            image = resize(src,115,85);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
     }
    
    public ImagePanel(URL path,int flag)
    {
        try{
            src = ImageIO.read(path);
            //System.out.println("width= "+getWidth()+"  Height=  "+getHeight());
            if(flag == 0)                       //category images
                image = resize(src,162, 205);
            else if(flag == 1)
                image = resize(src,116, 138);    //user photo at home page
            else if(flag == 2)                  
                image = resize(src,100,100);    //notification popmenu's icons
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
     }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image,0,0,null);
    }
    
    private BufferedImage resize(BufferedImage img, int newW, int newH) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
        g.dispose();  
        return dimg;  
       }  
}
