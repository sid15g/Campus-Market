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
	
 // To transfer variables from one class to another
	
import java.net.InetAddress;
	
public class Share<T>	{

	public String[] str;
	public int[] val;
	public T extra;

	public Share( int x, int y )	{
	
		str = new String[x];
		val = new int[y];
	
	}//End Of Constructor
	
	
}//End Of Class