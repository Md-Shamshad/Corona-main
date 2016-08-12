package com.hp.psg.corona.replication.common.cache.query;

/**
 * This Singleton class manage the process of reading Query xml file and cache the data.
 * 
 * * The query xml file format is:
 *    <replication>
 *    	   <group id="ServiceName"> ex:ServiceName="Config" or "Quote"
 *    			<operation id="OperationName"> ex: OperationName="Create", "Update", "Delete", "StatusChange" etc
 *    				<query id="QueryName"> i.e. QueryName="ProcessType" ex:QueryName="Table name to replicate" etc
 *    					<![CDATA[  Query Details ]]> 
 *    				</query>
 *    			</operation>
 *    		</group>
 *    </replication>
 *    
 * In the above format "id" is required and case sensitive. The CDATA should be 1 per query.
 * It reads xml file using DOM and JAXP and cache it in HashMap.
 * The HashMap format is: Key=ServiceName-OperationName-QueryName and Value="CDATA value" 
 * 
 * @author kn
 *
 */
public class ReplicationQueryManager extends QueryRepository{
	private static QueryRepository instance = null;
	private static final String DEFAULT_QUERY_FILE = "replication_querys.xml";
		
	private ReplicationQueryManager(){
		initialize();
	}
	
	public static QueryRepository getInstance(){
		if( instance == null){
			instance = new ReplicationQueryManager();
		}
		return instance;
	}//getInstance
	
	
	public void initialize(){
		initialize(DEFAULT_QUERY_FILE);
	}//initialize
		
	
	
	public static void main(String[] args){
		
		long start = System.currentTimeMillis();		
		QueryRepository queryRepository = ReplicationQueryManager.getInstance();			
		long end = System.currentTimeMillis();
		System.out.println("Time taken to cache the querys: " + (end-start));
		
		
		long startTime = System.currentTimeMillis();
		System.out.println("Query Details 1: "+queryRepository.getQuery("Quote","Create","Quote"));
		long endTime = System.currentTimeMillis();		
		System.out.println("Time taken to get the query: " + (endTime-startTime));
		
		System.out.println("Query Details 2: "+queryRepository.getQuery("Quote","CreateQuote","InsertQuote"));
		System.out.println("Query Details 3: "+queryRepository.getQuery("Config","CreateQuote","InsertQuote"));
	}//main		
	
}//end of class
