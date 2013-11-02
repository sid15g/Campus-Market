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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class PopupHandler {

    private Home parent;
    private String icon_path = "../handle/icons/";
    private String image_path = "../handle/images/";
    public int amtDeducted;
    public String TID;
    public int bidval;
    public String usr;
    public static boolean isError;
    public String details;
    public int amount;
    
    public PopupHandler(Home parent)
    {
       this.parent = parent;
     
    }
    
    public void viewUID()
    {
        isError = false;
        String passwd = authBox();
        parent.getUniqueID(Home.PID,passwd);
    }
    
    public void buyingProcess()
    {
        //ask if buyer is sure to proceed further
        isError = false;
        int chk = confirmBox("Are you sure you want to purchase?");
        if(chk == 0)
        {
          parent.getBuyInfo(Home.PID);  
          if(!isError)
          {
              ackBox("<html>Product reserved.<br>Amount deducted: "+amtDeducted+" INR<br><b>Make sure to note your Transaction ID: "+TID+"</b></html>");
          }
        }
        
    }
    
    public void biddingProcess()
    {
        isError = false;
        int chk1,amount;
        
        String tmp = bidBox();
        if(!tmp.equals(""))
        {
            amount = Integer.parseInt(tmp);
            chk1 = confirmBox("Are you sure you want to bid?");
            if(chk1 == 0)   
            {
                parent.getBidInfo(Home.PID,amount);
                if(!isError)
                {
                    ackBox("<html>Bidding done successfully..!!!<br><br> You will get a unique Transaction ID to avail the product if there is no Higher Bid posted within the next <b>72 hours</b>. <br> In case of a higher bid your amount will be refunded instantaneously.<html>");    
                }
            }
        }
        
    }
    
    public void fullFillProcess()
    {
        isError = false;
        boolean chk;
        chk = quoteBox();
        
        if(chk)
        {
            parent.getDemandInfo(Home.PID,amount,details);
            if(!isError)
            {
               ackBox("Price qouted successfully !!");    
            }
        }
    }
    
    
    public void exchangeID()
    {
        isError = false;
        String id;
        int UID;
        id = uniqueIDBox();
        
        if(!id.equals(""))
        {
            UID = Integer.parseInt(id);
            if(!isError)
            {
               ackBox("Transcation complete!!");    
            }
        }
    }
    
    
    public void acceptDemand(String desc,int amount,String username,int PID)
    {
        isError = false;
        int chk;
        chk = demandAcceptBox(desc,amount);
        if(chk == 0)
        {
            parent.getAcceptInfo(PID,username);
            if(!isError)
            {
               ackBox("<html>Product reserved.<br>Amount deducted: "+amtDeducted+" INR<br><b>Make sure to note your Transaction ID: "+TID+"</b></html>");
            }
        }
    }
    
  //------------------------------------------ option pane methods--------------------------------------------
    
    //
    public int demandAcceptBox(String descc,int amount)
    {
        Icon icon = new ImageIcon(getClass().getResource(icon_path + "question-icon.png"));
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setPreferredSize(new Dimension(220,150));
        
        //new edited
        JLabel lb = new JLabel("<html><b>Quote price:</b>   "+amount+"<br><b>Description:</b><br>"+descc+"</html>");
        panel.add(lb);
        
        /*JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JLabel lb1 = new JLabel("<html><b>Quoted Price:  </b></html>");
        JLabel tf1 = new JLabel(""+amount); 
        tf1.setPreferredSize(new Dimension(170,30));
        topPanel.add(lb1);
        topPanel.add(tf1);
       
        //JPanel bottomPanel = new JPanel();
        //JLabel desc = new JLabel("<html><b>Description:</b></html>");
        JLabel ta1 = new JLabel(descc);
        ta1.setPreferredSize(new Dimension(170,100));
        //bottomPanel.add(desc);
        //bottomPanel.add(ta1);
        
        panel.add(topPanel);
        //panel.add(desc);
        panel.add(ta1);*/
        
        int chk = JOptionPane.showConfirmDialog(parent, panel,"Accept",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE,icon);
        if(chk == 0)
        {
          return 0;
        }
        else
            return 1;
        
    }
    
    
    
    //confirm Dialog
    
    public int confirmBox(String msg)
    {
        //JOptionPane.showConfirmDialog(null,"choose one", "choose one", JOptionPane.YES_NO_OPTION);
        Icon icon = new ImageIcon(getClass().getResource(icon_path + "question-icon.png"));
        return JOptionPane.showConfirmDialog(parent,msg,"Confirmation",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,icon);
        
    }
    
    public void infoBox(String msg)
    {
        JOptionPane.showMessageDialog(parent,msg,"Process complete",JOptionPane.INFORMATION_MESSAGE);
    }
    public void ackBox(String msg)
    {
        Icon icon = new ImageIcon(getClass().getResource(icon_path + "confirm-icon.png"));
        JOptionPane.showMessageDialog(parent,msg,"Process complete",JOptionPane.INFORMATION_MESSAGE,icon);
    }
    
    public void errorBox(String msg)
    {
        isError = true;
        Icon icon = new ImageIcon(getClass().getResource(icon_path + "error-icon.png"));
        JOptionPane.showMessageDialog(parent,msg,"Error",JOptionPane.ERROR_MESSAGE, icon);
    }
    
    public String bidBox()
    {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel topPanel = new JPanel(new GridLayout(2,1));
        topPanel.setPreferredSize(new Dimension(250,80));
        topPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Current Highest Bid Holder"));
        
        JLabel name = new JLabel("<html><b>Username: </b>"+usr+"<html>");
        JLabel amt = new JLabel("<html><b>Bid amount: </b>"+bidval+"<html>");

        topPanel.add(name);
        topPanel.add(amt);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("Enter Higher Bid Amount:  ");
        JTextField textfield1 = new JTextField("");
        textfield1.setPreferredSize(new Dimension(100,30));
        bottomPanel.add(label);
        bottomPanel.add(textfield1);
        
        panel.add(topPanel,BorderLayout.NORTH);
        panel.add(bottomPanel);
        
        int chk = JOptionPane.showConfirmDialog(parent, panel,"Put your bid",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(chk == 0)
            return textfield1.getText();
        else
            return "";
    }

    public boolean quoteBox()
    {
        //return JOptionPane.showInputDialog(parent,"Quote Amount ");
        
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lb1 = new JLabel("Quote Amount ");
        JTextField tf1 = new JTextField(); 
        tf1.setPreferredSize(new Dimension(170,30));
        topPanel.add(lb1);
        topPanel.add(tf1);
       
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel desc = new JLabel("Description: ");
        JTextArea ta1 = new JTextArea();
        ta1.setPreferredSize(new Dimension(170,100));
        bottomPanel.add(desc,BorderLayout.NORTH);
        bottomPanel.add(ta1);
        
        panel.add(topPanel,BorderLayout.NORTH);
        panel.add(bottomPanel);
        
        int chk = JOptionPane.showConfirmDialog(parent, panel,"Put your bid",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
        if(chk == 0)
        {
            amount = Integer.parseInt(tf1.getText());
            details = ta1.getText();
            return true;
        
        }
        else
            return false;
    }
    
    public String uniqueIDBox()
    {
       return JOptionPane.showInputDialog(parent,"Enter Transaction ID: ");
    }
    
    public String authBox()
    {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter a password:");
        JPasswordField pass = new JPasswordField(20);
        panel.add(label);
        panel.add(pass);
        Icon icon = new ImageIcon(getClass().getResource(icon_path + "question-icon.png"));
        
        JOptionPane.showMessageDialog(parent,panel,"Authentication",JOptionPane.ERROR_MESSAGE, icon);
       //return JOptionPane.showInputDialog(parent,"Enter Password: ");
        return pass.getText();
    }
    
}

