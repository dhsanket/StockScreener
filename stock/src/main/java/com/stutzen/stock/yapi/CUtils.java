package com.stutzen.stock.yapi;



import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CUtils {

	public static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

	public static String nz(String str1, String str2){
		return str1==null?str2:str1;
	}

	public static Object nz(String str1, Object str2){
		return str1==null?str2:str1;
	}

	public static Object nz(Object str1, Object str2){
		return str1==null?str2:str1;
	}

	public static Date formateDateTime(String date) {
		Date d=null;
		try {
			d =	DATETIME_FORMAT.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	public static Date formateDate(String date) {
		Date d=null;
		try {
			d =	DATE_FORMAT.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	public static String formateDate(Date date) {
		String d="";
		d =	DATE_FORMAT.format(date);
		return d;
	}

	public static String formateDateTime(Date date) {
		String d="";
		d =	DATETIME_FORMAT.format(date);
		return d;
	}

	/**
	 * Function to handle the special characters
	 * @param xValue
	 * @return
	 */
    public static String handleSymbols(String xValue){
    	String newVal     = "";
    	String newChar    = "";
    	int initPosition  = 0;
    	int finalPosition = xValue.length();
    	for ( int i = initPosition; i<finalPosition; i++ ){
    	    newChar = xValue.substring(i,i+1);
    	    if  (newChar.equals("'") )
    		newVal = newVal + "\\'";
    	    else if (newChar.equals("\\") ){
    		newVal = newVal + "\\";
    		newVal = newVal + newChar ;
    	    }
    	    else
    		newVal = newVal + newChar ;
    	}
    	return newVal;
    }

    /**
     *
     * @param str
     * @param c
     * @param n
     * @return
     */
	public static int nthOccurrence(String str, char c, int n) {
	    int pos = str.indexOf(c, 0);
	    while (n-- > 0 && pos != -1)
	        pos = str.indexOf(c, pos+1);
	    return pos;
	}

    /**
     * Funtion used to get the error trace from the exception object
     * @param e
     * @return
     * @throws Exception
     */
	public static String stackTraceToString(Throwable e){
	    StringBuilder sb = new StringBuilder();
		try{
		    for (StackTraceElement element : e.getStackTrace()) {
		        sb.append(element.toString());
		        sb.append("\n");
		    }
		}catch(Exception ex){
			ex.printStackTrace();
		}
	    return sb.toString();
	}

	/**
	 * Function to replace html characters
	 * @param result
	 * @return
	 */
	public static String validateSpecialQuote(String result){
		Pattern pattern = Pattern.compile("[&<>'\"]+");
		   Matcher matcher = pattern.matcher(result);
		   if(matcher.find()) {
		     	if(result.contains("&")){
		                   	   result = result.replaceAll("&","&#38;");
		       	}
		        if(result.contains("<")){
		       	   result = result.replaceAll("<","&#60;");
		       	}
		        if(result.contains(">")){
		       	   result = result.replaceAll(">","&#62;");
		       	}
		        if(result.contains("'")){
		       	   result = result.replaceAll("'","&#39;");
		       	}
		        if(result.contains("\"")){
		       	   result = result.replaceAll("\"","&#34;");
		        }
		    }
		return result;
	}

	  /**
     * Function to send HTTP GET Request to the given URL
     * @param url
     * @return
     */
    public static String sendRequest(String url){
    	StringBuffer result = null;
    	try {
    			HttpURLConnection conn = null;
    			DataInputStream inStream = null;
    			try{
    				 URL servletURL = new URL(url);
    	    		 conn = (HttpURLConnection) servletURL.openConnection();
    	    		 conn.setDoInput(true);
    	             conn.setDoOutput(true);
    	             conn.setUseCaches(false);
    	             conn.setRequestMethod("GET");
    	      		 inStream = new DataInputStream ( conn.getInputStream() );
    	      		 result = new StringBuffer();
    	      		 int chr;
    	      		 while ((chr=inStream.read())!=-1) {
    	      			 result.append((char) chr);
    	      		}
    			}catch (IOException ioex){
    	         	 throw ioex;
    	         }
    			catch(Exception e){
    	         	 throw e;
    			}finally{
    					try{
    		        		 if(inStream!=null)inStream.close();
    			        }catch(Exception e){
    	        	 }
    	         }
    	 	 }catch (Exception ex) {
    	    	 ex.printStackTrace();
    	     }
    	     return result.toString();
    }
}
