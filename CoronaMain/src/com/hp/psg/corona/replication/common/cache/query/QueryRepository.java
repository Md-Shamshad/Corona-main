package com.hp.psg.corona.replication.common.cache.query;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hp.psg.corona.replication.common.util.Logger;

/**
 * This class is used to Read all the query's from xml file.
 *    
 *    
 * @author kn
 *
 */
public abstract class QueryRepository {
	//Used to store all query's
	private Map<String, String> querysMap;
	
	private ArrayList<String> queryKey = null;
	private static final String QUERY_NOT_DEFINED="NOTDEFINED";
		
	public QueryRepository(){
		
	}
	
	/**
	 * For initializing the xml read and cache the data
	 */
	public abstract void initialize();
	
	
	/**
	 * This initiates the process of reading Query xml file and cache the data into xml file.
	 * 
	 * @param xmlFileName
	 */
	public void initialize(String xmlFileName){
		Logger.info(getClass(),"Initializing the process of reading Query xml file...");

		querysMap = new HashMap<String, String>();			
		queryKey = new ArrayList<String>();
		
		//Read the xml file from a file system
		readQueryFile(xmlFileName);

		Logger.info(getClass(),"End of reading Query xml file process.");		
	}//initialize
	
	
	/**
	 * Read the query xml file and parse the document.
	 * 
	 * @param xmlFileName
	 */
	private void readQueryFile(String xmlFileName){
		Logger.debug(getClass(),"reading query xml file...");
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setValidating(false);
			dbf.setIgnoringElementContentWhitespace(true);
    
	      	DocumentBuilder db = dbf.newDocumentBuilder();
	      	InputStream xmlFile = QueryRepository.class.getClassLoader().getResourceAsStream(xmlFileName);
	      	if(xmlFile ==  null){
	      	    xmlFile = new FileInputStream(xmlFileName);
	      	}
			Document doc = db.parse(xmlFile);			
				
			//parse the query xml
			parseDocument(doc);
			
			Logger.debug(getClass(),"The cached query's are: "+querysMap);
			
			//nullifying
			doc = null;
			xmlFile = null;
			db = null;
			dbf = null;
		}
		catch(ParserConfigurationException pce) {
			Logger.error(getClass(),"Parser exception while reading query xml file: "+pce.getMessage(),pce);
		}catch(SAXException se) {
			Logger.error(getClass(),"Parser exception while reading query xml file: "+se.getMessage(),se);
		}catch(IOException ioe) {
			Logger.error(getClass(),"Exception while reading query xml file: "+ioe.getMessage(),ioe);
		}
		finally{
			queryKey=null;
		}
	}//readQueryFile
	
	
	/**
	 * It parse the given document
	 * 
	 * @param dom
	 */
	private void parseDocument(Document dom){
		//get the root element
		Element root = dom.getDocumentElement();
		
		//parse root element
		parseElement(root);		
	}//parseDocument
	
	
	/**
	 * It parse the element
	 * 
	 * @param root
	 */
	private void parseElement(Element root){
		//get a list of elements for element
		NodeList list = root.getChildNodes();
		int count = list.getLength();
		
		for (int i=0; i < count; i++) {	        
			//if it is element node
			if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element subnode = (Element) list.item(i);
				
	        	String elementId = subnode.getAttribute("id");  	
        		queryKey.add(elementId);
        		
        		parseElement(subnode);        		
	        }//if it is CDATA node
			else if( list.item(i).getNodeType() == Node.CDATA_SECTION_NODE){
				Node node = (Node) list.item(i);
        		String dataValue = getNodeData(node);
        		
        		//if data is empty
        		if( dataValue.trim().equals("")){
        			dataValue=QUERY_NOT_DEFINED;
        		}
        		
        		//to form a key
        		StringBuffer key = new StringBuffer();
        		int qSize = queryKey.size();
        		for(int j=0; j<qSize; j++){
        			key.append(queryKey.get(j));
        			if( j<qSize-1){
        				key.append("-");
        			}
        		}//j
        		
				Logger.debug(getClass(), "The Key and Value for respective query are: " + "Key="+key.toString()+"::: Value="+dataValue.trim());
        		//cache the data
				querysMap.put(key.toString(), dataValue.trim());	
        	}						
	    }//i
		
		// To remove recently added key in the list and move on to next level.
		if(queryKey.size()>0){
			queryKey.remove(queryKey.size()-1);
		}		
	}//parseElement
	
	
	/**
	 * Read CDATA node data
	 * 
	 * @param node
	 * @return
	 */
	private String getNodeData(Node node) {
	    StringBuffer result = new StringBuffer();
	    	    
	    //to read CDATA section value
	    NodeList list = node.getChildNodes();
	    int count = list.getLength();
	    if( count == 0 ){
	    	if (node.getNodeType() == Node.CDATA_SECTION_NODE) {
	        	result.append(node.getNodeValue());	        	
	    	}
	    	return result.toString();
	    }
	    return result.toString();
	}//getNodeData	
	
	
	/**
	 * To get query based on given parameter
	 * 
	 * @param groupName
	 * @param operationName
	 * @param queryName
	 * @return
	 */
	public String getQuery(String groupName, String operationName, String queryName){
		String query = null;
		
		if( (groupName == null || groupName.length()==0) || (operationName == null || operationName.length()==0) || (queryName == ""||queryName.length()==0)){
			return query;
		}
		
		String keyName=groupName+"-"+operationName+"-"+queryName;
		
		if( querysMap.containsKey(keyName)) {
			query = querysMap.get(keyName);
		}
		
		return query;
	}//getQuery

}//class
