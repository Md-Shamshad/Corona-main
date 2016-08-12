package com.hp.psg.corona.datachange.handler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.beans.DataChangeEvent;
import com.hp.psg.corona.common.constants.CoronaFwkConstants;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.datachange.dao.DataChangeEventsFwkDao;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginRequest;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginResult;
import com.hp.psg.corona.datachange.plugin.interfaces.IDataChangeEventsProcessor;
import com.hp.psg.corona.datachange.plugin.interfaces.ITransformationPlugin;
import com.hp.psg.corona.error.util.CoronaErrorHandler;


/**
 * @author dudeja
 * @version 1.0
 *
 */
public class DataChangeEventsProcessor implements IDataChangeEventsProcessor{
	private List<DataChangeEvent> pEvents = null;
	private String processingStatusGroup = null;
	private Long dataChangeEventBatchId = null;
	LoggerInfo logInfo=null;

	public DataChangeEventsProcessor() {
		// TODO Auto-generated constructor stub
	}
	public String getProcessingStatusGroup() {
		return processingStatusGroup;
	}

	public void setProcessingStatusGroup(String processingStatusGroup) {
		this.processingStatusGroup = processingStatusGroup;
	}

	public DataChangeEventsProcessor(List<DataChangeEvent> pEvents) {
		this.pEvents = pEvents;
		processingStatusGroup = CoronaFwkConstants.ProcessingStatus.IN_PROGRESS;
		process();
	}

	public List<DataChangeEvent> getDataChangeEvents() {
		return this.pEvents;
	}



	private CTODaxDataBeanGeneral createCtoDataDaxGeneralBeanObj(DataChangeEvent dge, 
			List<String > allKeysForMethodArgs, Map<String, String> processKeyMapValForMethodArg) 
	throws Exception{
		CTODaxDataBeanGeneral outputBean = null;

		logInfo = new LoggerInfo (this.getClass().getName());

		Class clDao = Class
		.forName("com.hp.psg.corona.datachange.handler.DataChangeCallHandler");

		try {
			// Create !!!
			Object[] inputParam = CoronaFwkUtil
			.getAllParamsArray(dge.getId(), dge
					.getTrnId(), allKeysForMethodArgs,
					processKeyMapValForMethodArg);

			Class[] par = new Class[allKeysForMethodArgs.size() + 2];
			par[0] = Long.class;
			par[1] = Long.class;

			for (int i = 2; i <= allKeysForMethodArgs.size() + 1; i++)
				par[i] = String.class;

			String methodName = CoronaFwkUtil
			.getProcessorMethodNameUsingCachedMap(
					"get", dge.getProcessType());
			java.lang.reflect.Method mthd = clDao.getMethod(
					methodName, par);
			outputBean = (CTODaxDataBeanGeneral) mthd.invoke(
					new DataChangeCallHandler(), inputParam);

		} catch (Exception e) {
			String errorMessage ="ObjectCreation failure (dge) - "+dge.getId()+" { "+e.getMessage()+" }";
			CoronaErrorHandler.logError(e,errorMessage,CoronaErrorConstants.FWKDCEP001);
			throw new Exception(errorMessage);
		}

		return outputBean;
	}

	private CTODaxDataBeanGeneral processTransformation(DataChangeEvent dge, CTODaxDataBeanGeneral cdgBeanToTranform) throws Exception {
		try {
			logInfo = new LoggerInfo (this.getClass().getName());

			List<String> stringClasses = CoronaFwkUtil
			.getListOfClasses(dge.getSource(),
			"Transformation");
			FeaturePluginResult featurePluginResult = null;
			FeaturePluginRequest featurePluginRequest = null;

			if (stringClasses != null && stringClasses.size() > 0) {
				featurePluginRequest = CoronaFwkUtil
				.getFeaturePluginRequestFromCTOBean(cdgBeanToTranform);
			}

			//Transformation for featureplugin request and response objects .
			if (stringClasses != null && stringClasses.size() > 0) {
				Logger.debug(logInfo, "process", "*******************************Construct plugin request start - "+dge.getId()+" **************************************************");

				for (String strClass : stringClasses) {
					Logger.info(logInfo, "process", "Load and Call APIs for >>>>>>> "
							+ strClass);
					ITransformationPlugin pluginImpl = (ITransformationPlugin)Class.forName(strClass).newInstance();

					//CoronaObjectWrapperUtil.prettyPrint(featurePluginRequest);
					featurePluginResult = pluginImpl
					.transform(featurePluginRequest);

					//CoronaObjectWrapperUtil.prettyPrint(featurePluginResult);
					featurePluginRequest
					.setFeatureBean(featurePluginResult
							.getFeatureBean());
				}

				cdgBeanToTranform = CoronaFwkUtil
				.getCTOBeanFromFeaturePluginResult(cdgBeanToTranform,
						featurePluginResult);

				Logger.debug(logInfo, "process", "*******************************Construct plugin request end "+dge.getId() +"**************************************************");
			}			
		}catch (Exception ex){
			CoronaErrorHandler.logError(ex, "Tranformation failure (dge) - "+dge.getId()+" { "+ex.getMessage()+" }", null);
		}
		return cdgBeanToTranform;
	}


	public void processTranformedObjects (DataChangeEvent dge, CTODaxDataBeanGeneral inputBean) throws Exception{

		// Submit for processing
		try {
			logInfo = new LoggerInfo (this.getClass().getName());

			Class clDao = Class
			.forName("com.hp.psg.corona.datachange.handler.DataChangeCallHandler");

			List<Object> inputParamBeans = new ArrayList<Object>();
			List parList = new ArrayList();

			Class[] par = new Class[1];
			String methodName = CoronaFwkUtil
			.getProcessorMethodNameUsingCachedMap(
					"process", dge.getProcessType());

			if (!(DataChangeEventsFwkDao
					.getDataChangeCallHandlerMethodMap()
					.containsKey(methodName.trim()))) {
				throw new Exception(
						"Method signature "+methodName.trim()+"not found in cached Map for DataChangeEventHandlerMap !");
			} else {
				List<String> paramTypes = DataChangeEventsFwkDao
				.getDataChangeCallHandlerMethodMap()
				.get(methodName);
				Class classDxDataGeneralBean = Class
				.forName("com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral");

				inputParamBeans.add(dge.getId()); 

				for (String strParamType : paramTypes) {
					String beanMethod = "get" + strParamType;

					// First field was added to be a PE_ID.
					if (!"Long".equals(strParamType)) {
						java.lang.reflect.Method mthd = classDxDataGeneralBean
						.getMethod(beanMethod);

						inputParamBeans.add(mthd
								.invoke(inputBean));
						parList
						.add((Array
								.newInstance(
										Class
										.forName("com.hp.psg.corona.common.cto.beans."
												+ strParamType),
												1)));
					}

				}

				Class[] parArrayForList = new Class[parList
				                                    .size() + 1];

				parArrayForList[0] = Long.class; 

				for (int i = 0; i < parList.size(); i++) {
					Object obj = parList.get(i);
					parArrayForList[i + 1] = obj.getClass();

				}

				java.lang.reflect.Method mthd2 = clDao
				.getMethod(methodName, parArrayForList);

				mthd2.invoke(new DataChangeCallHandler(),
						inputParamBeans.toArray());
			}
			
			//Update the processing status to PENDING_PROPAGATION or COMPLETED state.
			updateDateChangeEventStatus(dge);
		} catch (Exception ex) {
			String errorMessage ="Process Failure (dge) "+dge.getId()+" { "+ex.getMessage()+" }";
			CoronaErrorHandler.logError(ex,errorMessage,CoronaErrorConstants.FWKDCEP002);
			throw new Exception(errorMessage);
		}

	}

		
	private void process() {
		try {
			List<String> allKeysForMethodArgs = null;
			logInfo = new LoggerInfo (this.getClass().getName());

			for (DataChangeEvent dge : pEvents) {
					//I expect it to have one single event to be procssed now. just using same 
				dataChangeEventBatchId = dge.getId();

				DataChangeEventsFwkDao.updateDataChangeEventStatus(dge,
						CoronaFwkConstants.ProcessingStatus.IN_PROGRESS, null);

				Logger.info(logInfo, "process", "Updated the status for "
						+ dge.getId()
						+ " to IN PROGRESS, START PROCESSING IT !!!! for source "
						+ dge.getSource());

				try {
					
					
					if (Config.doProcessingInStoredProc() && 
							(
							"CONFIG_PRICE".equalsIgnoreCase(dge.getProcessType()) 
									|| 
							"LIST_PRICE_UPDATE".equalsIgnoreCase(dge.getProcessType())
							||
							"LIST_PRICE_INSERT".equalsIgnoreCase(dge.getProcessType())
							)){
						Logger.debug(logInfo, "process", "Direct processing for process_type CONFIG_PRICE");
						
						long startTime=System.currentTimeMillis();
						processEventsInStoredProc(dge);
						Logger.perfInfo(logInfo, "process" , "(TimeStamp - "+new java.util.Date()+" ) - Datachange object processing for PROCESS_TYPE () - "+dge.getId()+" - took processing(timeInSeconds) - "+(System.currentTimeMillis()-startTime)/1000);

					}else {
						if (allKeysForMethodArgs == null)
							allKeysForMethodArgs = CoronaFwkUtil.getSourceFormatKeys(dge
									.getProcessType());

						Map<String, String> processKeyMapVal = CoronaFwkUtil
						.getFormatKeyValueMap(allKeysForMethodArgs, dge.getProcessKey());
						if (processKeyMapVal != null) {

							//Create !!
							Logger.debug(logInfo, "process", "Calling object creation for dataChangeEvent  - "+dge.getId());

							long startTime=System.currentTimeMillis();
							CTODaxDataBeanGeneral ctoDaxDgbean = createCtoDataDaxGeneralBeanObj(dge, allKeysForMethodArgs, processKeyMapVal);
							Logger.perfInfo(logInfo, "process" , "(TimeStamp - "+new java.util.Date()+" ) - Datachange object creation for - "+dge.getId()+" - took processing(timeInSeconds) - "+(System.currentTimeMillis()-startTime)/1000);

							CTODaxDataBeanGeneral outputBean=null;

							//Tranformation !!
							startTime=System.currentTimeMillis();
							if (ctoDaxDgbean != null){
								Logger.debug(logInfo, "process", "Calling Tranformation classes for dataChangeEvent  - "+dge.getId());
								outputBean=processTransformation(dge, ctoDaxDgbean);
							}else {
								CoronaErrorHandler.logError(new Exception("Output bean from transformation is null for "+dge.getId()), "Output bean from transformation is null for "+dge.getId(), null);
								outputBean = ctoDaxDgbean;
							}

							Logger.perfInfo(logInfo, "process", "(TimeStamp - "+new java.util.Date()+" ) - Datachange transformation for - "+dge.getId() +" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);

							//Update processed Objects
							Logger.debug(logInfo, "process", "Updating header information for dataChangeEvent - "+dge.getId());
							startTime=System.currentTimeMillis();

							processTranformedObjects(dge, outputBean);
							Logger.perfInfo(logInfo, "process" , "(TimeStamp - "+new java.util.Date()+" ) - Datachange stored proc call for - "+dge.getId() +" - took processing(timestamp)- "+(System.currentTimeMillis()-startTime)/1000);

						} else {
							Logger.info(logInfo, "process", "Source key format is coming not found for process type for datachange event id "+dge.getId());

							DataChangeEventsFwkDao.updateDataChangeEventStatus(dge,
									CoronaFwkConstants.ProcessingStatus.ERROR,
							"Source key format is coming not found for process type for datachange event id.");
						}
					}
				} catch (Exception ex) {
					DataChangeEventsFwkDao.updateDataChangeEventStatus(dge,
							CoronaFwkConstants.ProcessingStatus.ERROR, ex
							.getMessage());
					CoronaErrorHandler.logError(ex,null, null);
				}

			}
			processingStatusGroup = CoronaFwkConstants.Success;
		} catch (Exception e) {
			processingStatusGroup = CoronaFwkConstants.Failure;
			CoronaErrorHandler.logError(e,null, null);
		}
	}

	/**
	 * 
	 * @param dge
	 * @throws Exception
	 * This method is direct processing of process types not going thru transformation logic.
	 */
	private void processEventsInStoredProc(DataChangeEvent dge)
	throws Exception{
		CTODaxDataBeanGeneral outputBean = null;

		logInfo = new LoggerInfo (this.getClass().getName());

		Class clDao = Class
		.forName("com.hp.psg.corona.datachange.handler.DataChangeCallHandler");

		try {
			// Create !!!
			Object[] inputParam = CoronaFwkUtil
			.getAllParamsArray(dge.getId());

			Class[] par = new Class[1];
			par[0] = Long.class;

			String methodName = CoronaFwkUtil
			.getProcessorMethodNameUsingCachedMap(
					"processInStoredProc", dge.getProcessType());
			java.lang.reflect.Method mthd = clDao.getMethod(
					methodName, par);
			mthd.invoke(
					new DataChangeCallHandler(), inputParam);
			
			//Update the status to COMPLETED or PENDING_PROPAGATION.
			updateDateChangeEventStatus(dge);
		} catch (Exception e) {
			String errorMessage ="Failed at method call for object creation for "+dge.getId();
			CoronaErrorHandler.logError(e,errorMessage,CoronaErrorConstants.FWKDCEP001);
			throw new Exception(errorMessage);
		}

	}

	public void updateDateChangeEventStatus (DataChangeEvent dge) throws Exception{
			DataChangeEventsFwkDao
			.updateDataChangeEventStatus(
					dge,
					CoronaFwkConstants.ProcessingStatus.COMPLETED,
					null);
	}

	public Long getDataChangeEventBatchId() {
		return dataChangeEventBatchId;
	}

	public void setDataChangeEventBatchId(Long dataChangeEventBatchId) {
		this.dataChangeEventBatchId = dataChangeEventBatchId;
	}
	
	public void processEvent(Long dceId) {
		// TODO Auto-generated method stub
		List<DataChangeEvent> dceList= new ArrayList<DataChangeEvent>();
		System.out.println("Calling adhoc processer for eventIds passed");
		DataChangeEventsProcessor adProcessor = new DataChangeEventsProcessor(dceList);
		System.out.println("Event processed = "+adProcessor.getProcessingStatusGroup());
	}

}
