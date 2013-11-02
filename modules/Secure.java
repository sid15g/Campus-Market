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
	
package	modules;
	
 // Encrypter & Decrypter

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.apache.commons.codec.binary.Base64;



public class Secure 	{

	private SecretKeyFactory getKey;
	private IvParameterSpec param;
	private SecretKey newKey;
	private Cipher crypt;
	private KeySpec key;
	
	public Secure()			{
		
		String ALGORITHM = "DESede";
		
		try		{	
		
			byte[] keyBytes = new byte[24];
			this.param = new IvParameterSpec( keyBytes );
			this.key = new DESedeKeySpec( keyBytes );
			this.getKey = SecretKeyFactory.getInstance(ALGORITHM);
			this.newKey = this.getKey.generateSecret( key );
			
			this.crypt = Cipher.getInstance(ALGORITHM);
		
		}catch( Exception nsae )		{					//NoSuchAlgorithmException, UnsupportedEncodingException
			nsae.printStackTrace();
		}//End Of Try-Catch		
		
	}//End Of Constructor
	
	
	
    public String encrypt( String data )			{

		byte[] text, encryptedData = null;
	
		try			{
	
			text = data.getBytes("utf-8");		
			
			this.crypt.init( Cipher.ENCRYPT_MODE, newKey );
			encryptedData = crypt.doFinal( text );		
			
		}catch( Exception e )	{				//InvalidKeyException , IllegalBlockSizeException
			e.printStackTrace();
		}//End Of Try Catch			
		
		String encryptedValue = new String( Base64.encodeBase64(encryptedData) );
		
		return encryptedValue;		
		
	}//End Of Method

	
	
    public String decrypt( String key )			{

		byte[] text, decryptedData = null;
			
		try			{
		
			text = Base64.decodeBase64( key );
		
			this.crypt.init( Cipher.DECRYPT_MODE, newKey );
			decryptedData = crypt.doFinal( text );
		
		}catch( Exception e )	{				//InvalidKeyException , IllegalBlockSizeException
			e.printStackTrace();
		}//End Of Try Catch				
		
		String decryptedValue = new String( decryptedData );
		return decryptedValue;
		
	}//End Of Method
	
	
}//End Of Class
