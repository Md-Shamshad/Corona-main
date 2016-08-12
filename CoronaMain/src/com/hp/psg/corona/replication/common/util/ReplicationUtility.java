package com.hp.psg.corona.replication.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.replication.common.exception.ReplicationException;

public class ReplicationUtility {
	private LoggerInfo logInfo = null;
	
	public ReplicationUtility(){
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	
	public static final String processKeyDelimiter = "^";
	
	public static String[] splitProcessKey(String processKey){
		StringTokenizer tokenizer = new StringTokenizer(processKey,processKeyDelimiter);
		String[] strArray = new String[tokenizer.countTokens()];
		int index =0;
		while(tokenizer.hasMoreTokens()){
			strArray[index]=tokenizer.nextToken();
			index++;
		}
		return strArray;
	}
	
	public static String dateString(Date dt) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		if(dt!=null)
			return df.format(dt);
		else
			return null;
	}
	
	public static String dateToTimeStampString(Date dt) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		if(dt!=null)
			return df.format(dt);
		else
			return null;
	}
	
	public static Date stringToDate(String dt) {
		Date convertedDate = null;
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				
		try{
			if(dt!=null)
				convertedDate = df.parse(dt);
			else
				convertedDate =  null;
		}
		catch(ParseException ex){
			
		}
		
		return convertedDate;
	}
	
	public static Date stringToDateConvert(String oracleDate, String dateFormat){
		Date userDate = null;

		if (oracleDate != null && !oracleDate.equals("")) {
		    try {
		    	DateFormat df = new SimpleDateFormat(dateFormat);
		    	userDate = df.parse(oracleDate.trim());
		    	userDate = new Timestamp(userDate.getTime());
		    }
		    catch (Exception ex) {
		    	ex.printStackTrace();
		    }
		}

		return userDate;
	  }
	
		
	
	public static void main(String[] args) {
		String[] str = splitProcessKey("12345677^8");
		for(String s : str){
		System.out.println(s);
		}
	}
}
