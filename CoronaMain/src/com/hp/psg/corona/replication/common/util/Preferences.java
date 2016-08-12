package com.hp.psg.corona.replication.common.util;

import com.hp.psg.corona.replication.common.cache.CacheTable;
import com.hp.psg.corona.replication.common.exception.CacheException;
import com.hp.psg.corona.replication.common.message.MessageConstants;
import com.hp.psg.corona.replication.common.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;


/**
 * Represents a list of properties stored in a configuration file. This class
 * is a simple wrapper around <tt>java.util.Properties</tt>. A <tt>Preferences</tt>
 * instance is global for the application, and initialized when the class is first
 * loaded. <tt>Preferences</tt> class can be used as follows:<p>
 * <tt>
 *	   Preferences prefs = Preferences.getInstance();
 *     String p1 = prefs.get("myapp.mypropertykey", "my default value");
 *     boolean p2 = prefs.getBoolean("myapp.myflag", true);
 *     boolean p3 = prefs.getInt("myapp.myvalue", 30);
 * </tt><p>
 * Using default values is mandatory so that the applications have a reasonable shot
 * at running even if a preferences file is unavailable.<p>
 *
 * @author  
 *
 */
public class Preferences extends CacheTable {
	private static Preferences INSTANCE = null;
    private static final String DEFAULT_PREFS_FILE = "corona.properties";
    private static final String PREF_APP_NAME = "app.name";
    private static String applicationName;
    public static final String PREF_APP = "Quote IDS";
    private HashMap list = null;
    private static boolean instanceCreated =false;

    
    /**
     * Private constructor for this method since the class is a singleton.
     * @throws HpWsException Thrown when the caching fails
     */
    private Preferences() throws CacheException {
        super();
        Logger.info(Preferences.class, "*** Start Loading Preferences in to Cache ***");
        createCacheTable();
        Logger.info(Preferences.class, "*** Finish Loading Preferences in to Cache ***");
        instanceCreated= true;
    }
    
    /**
     * Returns the single instance of the property list available for this
     * application.<p>
     *
     */
    public static Preferences getInstance() {
        try {
            if (INSTANCE == null) {
                INSTANCE = new Preferences();
            }
        }
        catch (Exception hpwsEx) {
            Logger.fatal(Preferences.class, "Preferences.getInstance() Caught Exception : " + hpwsEx.getMessage(),hpwsEx);
            hpwsEx.printStackTrace();
        }
        return INSTANCE;
    }//getInstance

    /**
     * Initialize the cache
     */
    @Override
	public void initialize() throws CacheException {
        getInstance();
    }

    /**
     * Create a HashMap using the DAO
     *
     * @param cacheTableId int
     */
    @SuppressWarnings("unchecked")
	@Override
	public void createCacheTable() throws CacheException {
        //get the preferences from the local .cfg file
        HashMap fileValues = getPreferencesFromFile();
        
        applicationName = (String) fileValues.get(PREF_APP_NAME);
        if (applicationName == null) {
        	applicationName = PREF_APP;
        }
         
        list = fileValues;
    }//createCacheTable
    

    /**
     * Look in hp-ws.cfg and put all the config info into the list object.
     */
    @SuppressWarnings("unchecked")
	public static HashMap getPreferencesFromFile() throws CacheException {
        Properties fileList = new Properties();

        HashMap fileMap = new HashMap();
        try {
        	System.out.println("************The path of file is *************"+new File(".").getCanonicalPath());
        	
        	System.out.println("%%%%%%%%%The path file%%%%%%%" +Preferences.class.getClassLoader().getSystemResource(DEFAULT_PREFS_FILE));

        	//create the properties file from a file stream
            InputStream ins = Preferences.class.getClassLoader().getResourceAsStream(DEFAULT_PREFS_FILE);
            if (ins == null){
        	ins = new FileInputStream(DEFAULT_PREFS_FILE); //use FileInputStream instead of getClass CAB                 
            }
            if (ins != null) {
                //put stream in to Properties object
                fileList.load(ins);
                Enumeration fileListKeysEnum = fileList.keys();
                
                //loop through all the keys in the properties object and put into list hash
                String propKey = null;
                String propValue = null;
                while (fileListKeysEnum.hasMoreElements()) {
                    propKey = (String) fileListKeysEnum.nextElement();
                    propValue = (String) fileList.get(propKey);
                    fileMap.put(propKey, propValue);
                }
                // Logger.debug(Preferences.class, fileMap);
                ins.close();
            }
            else {
                Logger.fatal(Preferences.class, "Problem creating Preferences object. Cannot populate default values.");
                Logger.error(Preferences.class,DEFAULT_PREFS_FILE + " could not be loaded properly");
                throw new CacheException(MessageConstants.INVALID_CONFIGURATION,MessageConstants.HP_MESSAGE_3001);
            }
        }
        catch (IOException ioe) {
            Logger.fatal(Preferences.class, "I/O exception reading from preferences file", ioe);
            Logger.error(Preferences.class,DEFAULT_PREFS_FILE + " could not be loaded properly");
            throw new CacheException(MessageConstants.INVALID_CONFIGURATION, "Please validate the configuration data.");
        }
        
        return fileMap;
    }//getPreferencesFromFile

    /**
     * Returns the value associated with the specified key in this property list.
     * Returns the specified default if there is no value associated with the key,
     * or the property list file is inaccessible.<p>
     *
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>.
     * @return the value associated with key, or <tt>def</tt> if no value is
     * 		associated with <tt>key</tt> or a property list file is unavailable.
     * @param key key whose associated value is to be returned.
     * @param def the value to be returned in the event this property list has
     * 		no value associated with <tt>key</tt>.
     */
    public String get(String key, String def) {
        if (key != null) {
            //check to see if value is in hash
            return (String) list.get(key);
        }
        else {
            throw new NullPointerException("key can't be null");
        }
    }//get

    /**
     * Returns the boolean value represented by the string associated with
     * the specified key in this property list. Valid strings are "<tt>true</tt>",
     * which represents true, and "<tt>false</tt>", which represents false. Case is
     * ignored, so, for example, "<tt>TRUE</tt>" and "<tt>False</tt>" are also valid. <p>
     *
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>.
     * @return the boolean value represented by the string associated with
     * 		<tt>key</tt> in this propterty list, or <tt>def</tt> if the
     * 		associated value does not exist or cannot be interpreted as a boolean.
     * @param key key whose associated value is to be returned as a boolean.
     * @param def the value to be returned in the event that this property list
     * 		has no value associated with <tt>key</tt> or the associated value cannot
     * 		be interpreted as a boolean.
     */
    public boolean getBoolean(String key, boolean def) {
        String value = null;
        boolean pref = def;

        if (key != null) {
            value = get(key, String.valueOf(def));

            // we can't use Boolean methods, they always return false
            // if the string rep is not equal to "true", ignoring case.
            if (value.equalsIgnoreCase("true")) {
                pref = true;
            }
            else if (value.equalsIgnoreCase("false")) {
                pref = false;
            }
            else {
                logInvalidEntry(key, value);
            }
        }
        else {
            throw new NullPointerException("key can't be null");
        }

        return pref;
    }//getBoolean

  
    

    /**
     * Returns the int value represented by the string associated with the
     * specified key. The string is converted to an integer as by
     * <tt>Integer.parseInt(String)</tt>. Returns the specified default if
     * there is no value associated with the key, the preferences file is
     * inaccessible, or if <tt>Integer.parseInt(String)</tt> would throw a
     * <tt>NumberFormatException</tt> if the associated value were passed.<p>
     *
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>.
     * @return the int value represented by the string associated with <tt>key</tt>,
     * 		or <tt>def</tt> if the associated value does not exist or cannot be
     * 		interpreted as an int.<p>
     * @param key key whose associated value is to be returned as an int
     * @param def the value to be returned in the event that there is no value
     * 		associated with <tt>key</tt> or the associated value cannot be
     * 		interpreted as an int, or preferences file is inaccessible
     */
    public int getInt(String key, int def) {
        String value = null;
        int pref = def;

        if (key != null) {
            value = get(key, String.valueOf(def));

            try {
                pref = Integer.parseInt(value);
            }
            catch (NumberFormatException nfe) {
                logInvalidEntry(key, value);
            }
        }
        else {
            throw new NullPointerException("key can't be null");
        }

        return pref;
    }//getInt

    /**
     * Logs a warning message if the the value in the Preferences table for this key is incorrect
     * @param key The key for which we are attempting to gat an entry
     * @param value The value found in the Preferences table
     */
    private void logInvalidEntry(String key, String value) {
        Logger.warn(
            Preferences.class, "Invalid preferences file entry \""
            + value
            + "\" for key\""
            + key
            + "\", default value will be used");
    }//logInvalidEntry
    

    /**
     * Splits a string into various tokens
     * 
     * @param text The text to split
     * @param token The separator
     * @return The list of substrings
     */
    public String[] readTokens(String text, String token) {
        StringTokenizer parser = new StringTokenizer(text, token);

        int numTokens = parser.countTokens();

        String[] list = new String[numTokens];

        for (int i = 0; i < numTokens; i++) {
            list[i] = parser.nextToken().trim();
        }
        return list;
    }//readTokens
      

	public static String getApplicationName() {
		return applicationName;
	}

	public static void setApplicationName(String applicationName) {
		Preferences.applicationName = applicationName;
	}
	
	@SuppressWarnings("unchecked")
	public void replaceEntry(String key, String value){
		list.remove(key);
		list.put(key,value);
	}
	
	public static boolean isInstCreated(){
		return instanceCreated;
	}
	
}//end of class