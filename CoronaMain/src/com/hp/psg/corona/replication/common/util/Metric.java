package com.hp.psg.corona.replication.common.util;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * 
 * Class to provide metric service for the application
 * 
 * @author mukartha
 * 
 */

public class Metric {

     public final static String LOGGER_TYPE_DEBUG = "DEBUG";
     public final static String LOGGER_TYPE_INFO = "INFO";
     public final static String LOGGER_TYPE_WARN = "WARN";
     public final static String LOGGER_TYPE_ERROR = "ERROR";
     private String sessionId = null;

    /**
     * Random number generator
     */
    public final static java.util.Random randomNumGen = new java.util.Random();

     private final long startTime;
     long intervalStartTime;
     static DecimalFormat dF = new DecimalFormat("##0.00000");
     private final HashMap breakPoints = new HashMap();

     /**
      * Constructor:  sets initial timestamp (startTime) and also intervalStartTime
      */
     public Metric() {
         startTime = System.currentTimeMillis();
         intervalStartTime = startTime;

	int randumNum = getRandomInt();
         sessionId = String.valueOf(System.currentTimeMillis()) + String.valueOf(randumNum);
     }

    public static int getRandomInt() {
	return randomNumGen.nextInt();
    }

     public void setSessionId(String sessionId) {
         this.sessionId = sessionId;
     }

     public String getSessionId() {
         return sessionId;
     }
     /**
      * Sets IntervalStartTime to current time
      */
     public void setIntervalStart() {
          intervalStartTime = System.currentTimeMillis();
     }

    /**
     * Sets intervalStartTime to current time and logs a message
     * @param message The message to log
     */
     public void setIntervalStart(String message) {
          Logger.debug(getClass(), message);
          intervalStartTime = System.currentTimeMillis();
     }

     /**
      * Gets time since constructor executes
      * @return (double) time (in seconds)
      */
     public double getElapsedTime() {
          return doElapsedCalc(startTime, System.currentTimeMillis());
     }

     /**
      * Gets elapsed time and places it into the Logger.debug processor
      * @param logMessage (String) message to be places in the log
      */
     public void logElapsedTime(String logMessage) {
          //Logger statement
          Logger.debug(getClass(), "*" + dF.format(getElapsedTime()) + "s - " + logMessage);
     }

     /**
      * Gets elapsed time and places it into the Logger.debug processor
      * @param logMessage (String) message to be places in the log
      */
     public void logElapsedTimeType(String logMessage, String LoggerType) {
          //Logger statement
          if (LoggerType.equalsIgnoreCase(LOGGER_TYPE_DEBUG))
               Logger.debug(getClass(), "[" + sessionId + "] " + dF.format(getInterval()) + "s - " + logMessage);
          else if (LoggerType.equalsIgnoreCase(LOGGER_TYPE_INFO))
               Logger.info(getClass(), "[" + sessionId + "] " + dF.format(getInterval()) + "s - " + logMessage);
          else if (LoggerType.equalsIgnoreCase(LOGGER_TYPE_WARN))
               Logger.warn(getClass(), "[" + sessionId + "] " + dF.format(getInterval()) + "s - " + logMessage);
          else if (LoggerType.equalsIgnoreCase(LOGGER_TYPE_ERROR))
               Logger.error(getClass(), "[" + sessionId + "] " + dF.format(getInterval()) + "s - " + logMessage);

     }

     /**
      * Gets elapsed time and places it into the Logger.debug processor
      * @param logMessage (String) message to be places in the log
      */
     public void logElapsedTimeType(String logMessage, String LoggerType, String breakPoint) {
          //Logger statement
          if (LoggerType.equalsIgnoreCase(LOGGER_TYPE_DEBUG))
               Logger.debug(getClass(), "[" + sessionId + "] " + dF.format(getInterval(breakPoint)) + "s - " + logMessage);
          else if (LoggerType.equalsIgnoreCase(LOGGER_TYPE_INFO))
               Logger.info(getClass(), "[" + sessionId + "] " + dF.format(getInterval(breakPoint)) + "s - " + logMessage);
          else if (LoggerType.equalsIgnoreCase(LOGGER_TYPE_WARN))
               Logger.warn(getClass(), "[" + sessionId + "] " + dF.format(getInterval(breakPoint)) + "s - " + logMessage);
          else if (LoggerType.equalsIgnoreCase(LOGGER_TYPE_ERROR))
               Logger.error(getClass(), "[" + sessionId + "] " + dF.format(getInterval(breakPoint)) + "s - " + logMessage);

     }

     /**
      * Get the interval from the intervalStartTime to the current time
      * @return (double) time in seconds
      */
     public double getInterval() {
          return doElapsedCalc(intervalStartTime, System.currentTimeMillis());
     }

     /**
      * Get inteval from the intervalStartTime to the current time... also resets the intervalStartTime
      * @param resetInterval (boolean) true causes reset of intervalStartTime
      * @return (double) interval time in seconds
      */
     public double getInterval(boolean resetInterval) {
          double elapsedTime = doElapsedCalc(intervalStartTime, System.currentTimeMillis());
          if (resetInterval)
               intervalStartTime = System.currentTimeMillis();
          return elapsedTime;
     }

     /**
      * Gets interval time and places it into the Logger.debug processor
      * @param logMessage (String) message to send to the Logger
      */
     public void logInterval(String logMessage) {
          //Logger statement
          Logger.debug(getClass(), " " + dF.format(getInterval()) + "s - " + logMessage);
     }
     /**
      * Gets interval time and places it into the Logger.debug processor
      * @param logMessage (String) message to send to the Logger
      */
     public void logIntervalType(String logMessage, String LoggerType) {
         logIntervalType(logMessage, LoggerType, false);
     }

     /**
      * Gets interval time and places it into the Logger.debug processor
      * @param logMessage (String) message to send to the Logger
      */
     public void logIntervalType(String logMessage, String LoggerType, boolean resetInterval) {
          //Logger statement
          if (LoggerType.equalsIgnoreCase(LOGGER_TYPE_DEBUG))
               Logger.debug(getClass(), "[" + sessionId + "] " + dF.format(getInterval()) + "s - " + logMessage);
          else if (LoggerType.equalsIgnoreCase(LOGGER_TYPE_INFO))
               Logger.info(getClass(), "[" + sessionId + "] " + dF.format(getInterval()) + "s - " + logMessage);
          else if (LoggerType.equalsIgnoreCase(LOGGER_TYPE_WARN))
               Logger.warn(getClass(), "[" + sessionId + "] " + dF.format(getInterval()) + "s - " + logMessage);
          else if (LoggerType.equalsIgnoreCase(LOGGER_TYPE_ERROR))
               Logger.error(getClass(), "[" + sessionId + "] " + dF.format(getInterval()) + "s - " + logMessage);
           if (resetInterval)
               intervalStartTime = System.currentTimeMillis();
       }

     /**
      * Gets elapsed time and places it into the Logger.debug processor, also resets the intervalStartTime
      * @param logMessage (String) message to send to the Logger
      * @param resetInterval (boolean) true resets IntervalStartTime
      */
     public void logInterval(String logMessage, boolean resetInterval) {
          Logger.debug(getClass(), " " + dF.format(getInterval()) + "s - " + logMessage);
          if (resetInterval)
               intervalStartTime = System.currentTimeMillis();
     }

     /**
      * Gets elapsed time and places it into the Logger.debug processor, also resets the intervalStartTime
      * @param logMessage (String) message to send to the Logger
      * @param resetInterval (boolean) true resets IntervalStartTime
      */
     public void logInterval(String logMessage, boolean resetInterval, boolean debugMode) {
          if (debugMode)
               Logger.debug(getClass(), " " + dF.format(getInterval()) + "s - " + logMessage);
          else
               Logger.info(getClass(), " " + dF.format(getInterval()) + "s - " + logMessage);

          if (resetInterval)
               intervalStartTime = System.currentTimeMillis();
     }

    /**
     * Logs a message
     * @param message The message to log
     */
     public void logMessage(String message) {
          Logger.debug(getClass(), message);
     }

     /**
      * Private method to perform time calculation
      * @param starttime (long) starting time (in milliseconds)
      * @param endtime (long) ending time (in milliseconds)
      * @return (double) elapsed time (in seconds)
      */
     private double doElapsedCalc(long starttime, long endtime) {
          return ( (endtime - starttime) / 1000.000);
     }

    /**
     * Adds a breakpoint to the existing set. We typically add a breakpoint at the start and at the end of a method call
     * This way we can do a computation
     * @param breakPoint The name of the breakpoint
     */ 
     @SuppressWarnings("unchecked")
	public void setBreakPoint(String breakPoint) {
          breakPoints.put(breakPoint.toLowerCase(), new Long(System.currentTimeMillis()));
     }

    /**
     * Logs the time elapsed from the start to the end of the call
     * @param message The message to log
     * @param startPoint The name of the start breakpoint
     * @param endPoint The name of the end breakpoint
     */
     public void logInterval(String message, String startPoint, String endPoint) {
          if (breakPoints.get(startPoint.toLowerCase()) != null && breakPoints.get(endPoint.toLowerCase()) != null) {
               doElapsedCalc( ( (Long) breakPoints.get(startPoint.toLowerCase())).longValue(),
                                           ( (Long) breakPoints.get(endPoint.toLowerCase())).longValue());
               Logger.debug(getClass(), " " + dF.format(getInterval()) + "s - " + message);
          }
          else {
               Logger.error(getClass(), "Invalid breakpoint '" + startPoint + "' specified.");
          }

     }

    /**
     * Logs the time elapsed from the start of a call
     * @param message The message to log
     * @param startPoint The name of the start breakpoint
     */
     public void logInterval(String message, String startPoint) {
          double elapsedTime = 0;
          if (breakPoints.get(startPoint.toLowerCase()) != null) {
               elapsedTime = doElapsedCalc( ( (Long) breakPoints.get(startPoint.toLowerCase())).longValue(), System.currentTimeMillis());
               Logger.debug(getClass(), " " + dF.format(elapsedTime) + "s - " + message);
          }
          else {
               Logger.error(getClass(), "Invalid breakpoint '" + startPoint + "' specified.");
          }

     }

    /**
     * Gets the time between the start and end of a call
     * @param startPoint The name of the start breakpoint
     * @param endPoint The name of the end breakpoint
     * @return The time elapsed
     */
     public double getInterval(String startPoint, String endPoint) {
          double elapsedTime = 0;
          if (breakPoints.get(startPoint.toLowerCase()) != null && breakPoints.get(endPoint.toLowerCase()) != null) {
               elapsedTime = doElapsedCalc( ( (Long) breakPoints.get(startPoint.toLowerCase())).longValue(),
                                           ( (Long) breakPoints.get(endPoint.toLowerCase())).longValue());
               //Logger.debug(" " + dF.format(getInterval()) + "s - " + message);
          }
          else {
               Logger.error(getClass(), "Invalid breakpoint '" + startPoint + "' specified.");
          }
          return elapsedTime;
     }

    /**
     * Gets the time elapsed from the start of a call
     * @param startPoint The name of the start breakpoint
     * @return The time elapsed
     */
     public double getInterval(String startPoint) {
          double elapsedTime = 0;
          if (breakPoints.get(startPoint.toLowerCase()) != null) {
               elapsedTime = doElapsedCalc( ( (Long) breakPoints.get(startPoint.toLowerCase())).longValue(), System.currentTimeMillis());
               //Logger.debug(" " + dF.format(elapsedTime) + "s - " + message);
          }
          else {
               Logger.error(getClass(), "Invalid breakpoint '" + startPoint + "' specified.");
          }
          return elapsedTime;
     }

    /**
     * Formats a time in milliseconds to one in seconds
     * @param milliSec The time in milliseconds
     * @return The formatted time in seconds
     */
     public String formatIntoSec(double milliSec) {
          return dF.format(milliSec / 1000.000);

     }

     // CV:TopConfig 03112004 Begin --->
     public String decFormat(double sec) {
         return dF.format(sec);
     }
     // CV:TopConfig 03112004 End   <---
}
