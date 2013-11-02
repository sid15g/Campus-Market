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


import java.awt.*;  
import java.awt.event.*;  
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

class DatePicker {  

int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
JLabel l = new JLabel("", JLabel.CENTER);  
String day = "";
JPopupMenu d;
JButton[] button = new JButton[49];
private JTextField tf1;
private int currD,currM;
        
public DatePicker(JPanel parent,JTextField tf,int xx,int yy) {  
tf1 = tf;
d = new JPopupMenu();
d.setLayout(new BorderLayout());
DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
Date date = new Date();
currD = Integer.parseInt((dateFormat.format(date).split("/"))[2]);
currM = Integer.parseInt((dateFormat.format(date).split("/"))[1]);
//System.out.println(currD);           
           
//d.setModal(true);
String[] header = { "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
JPanel p1 = new JPanel(new GridLayout(7, 7));
p1.setPreferredSize(new Dimension(430, 120));
for (int x = 0; x < button.length; x++) {
    final int selection = x;
    button[x] = new JButton();
    button[x].setFocusPainted(false);
    button[x].setBackground(Color.white);
    if (x > 6)
        button[x].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                day = button[selection].getActionCommand();
                tf1.setText(year+"-"+(month+1)+"-"+day);
                d.setVisible(false);
            }
        });
        if (x < 7) {
            button[x].setText(header[x]);
            button[x].setForeground(Color.red);
        }
        p1.add(button[x]);
}
JPanel p2 = new JPanel(new GridLayout(1, 3));
final JButton previous = new JButton("<< Previous");
previous.setEnabled(false);

previous.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent ae) {
        month--;
        displayDate();
        if(month <= (currM-1))
            previous.setEnabled(false);
    }
});
p2.add(previous);
p2.add(l);
JButton next = new JButton("Next >>");
next.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent ae) {
        month++;
        displayDate();
        if(month > (currM-1))
            previous.setEnabled(true);
    }
});
p2.add(next);
d.add(p1, BorderLayout.CENTER);
d.add(p2, BorderLayout.SOUTH);
d.pack();
d.show(parent,xx,yy);
displayDate();

}

public void displayDate() {
for (int x = 7; x < button.length; x++)
    button[x].setText("");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM yyyy");
java.util.Calendar cal = java.util.Calendar.getInstance();
cal.set(year, month, 1);
int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++)
{
    button[x].setText("" + day);
    
    //System.out.println(month+"  "+currM);
    
    if(day < currD && (month+1) == currM)
        button[x].setEnabled(false);
    else
        button[x].setEnabled(true);
}
l.setText(sdf.format(cal.getTime()));
//d.setTitle("Date Picker");

}

public String setPickedDate() {
if (day.equals(""))
    return day;
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
java.util.Calendar cal = java.util.Calendar.getInstance();
cal.set(year, month, Integer.parseInt(day));
return sdf.format(cal.getTime());
}

}

 

