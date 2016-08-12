package com.hp.psg.corona.propagation.handler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.beans.PropagationEvent;
import com.hp.psg.corona.common.constants.CoronaFwkConstants;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.datachange.dao.DataChangeEventsFwkDao;
import com.hp.psg.corona.datachange.handler.DataChangeCallHandler;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginRequest;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginResult;
import com.hp.psg.corona.datachange.plugin.interfaces.IBusinessProcessPlugin;
import com.hp.psg.corona.datachange.plugin.interfaces.ITransformationPlugin;
import com.hp.psg.corona.datachange.plugin.interfaces.IValidationPlugin;
import com.hp.psg.corona.error.util.CoronaErrorHandler;
import com.hp.psg.corona.propagation.dao.PropagationEventsFwkDao;
import com.hp.psg.corona.propagation.handler.interfaces.IBeanPropagation;
import com.hp.psg.corona.util.cache.CtoTransformationUtilMap;


/*
 * @author dudeja
 * @version 1.0
 *
 */
public class PropagationEventsProcessor {
	private List<PropagationEvent> pEvents = null;
	private String processingStatusGroup = null;
	private Long propagationEventGroupId = null;

	public PropagationEventsProcessor() {
		// TODO Auto-generated constructor stub
	}
	public String getProcessingStatusGroup() {
		return processingStatusGroup;
	}

	public void setProcessingStatusGroup(String processingStatusGroup) {
		this.processingStatusGroup = processingStatusGroup;
	}

	public PropagationEventsProcessor(List<PropagationEvent> pEvents) {
		this.pEvents = pEvents;
		processingStatusGroup = CoronaFwkConstants.ProcessingStatus.IN_PROGRESS;
		process();
	}

	public List<PropagationEvent> getPropagationEvents() {
		return this.pEvents;
	}

	/**
	 * 
	 * @param dge
	 * @throws Exception
	 * This method is direct processing of process types not going thru transformation logic.
	 */
	private void processEventsInStoredProc(PropagationEvent pe)
	throws Exception{
		CTODaxDataBeanGeneral outputBean = null;

		LoggerInfo logInfo = new LoggerInfo(this.getClass().getName());

		Class clDao = Class
		.forName("com.hp.psg.corona.propagation.handler.PropagationEventsCallHandler");

		try {
			// Create !!!
			Object[] inputParam = CoronaFwkUtil
			.getAllParamsArray(pe.getId());

			Class[] par = new Class[1];
			par[0] = Long.class;

			String methodName = CoronaFwkUtil
			.getProcessorMethodNameUsingCachedMap(
					"processInStoredProc", pe.getProcessType());
			java.lang.reflect.Method mthd = clDao.getMethod(
					methodName, par);
			mthd.invoke(
					new DataChangeCallHandler(), inputParam);
			
			//Update the status to COMPLETED or PENDING_PROPAGATION.
			PropagationEventsFwkDao
			.updatePropagationEventStatus(
					pe,
					CoronaFwkConstants.ProcessingStatus.COMPLETED,
					"Event Success !");
		} catch (Exception e) {
			String errorMessage ="Failed at method call for object creation for "+pe.getId();
			CoronaErrorHandler.logError(e,errorMessage,CoronaErrorConstants.FWKDCEP001);
			throw new Exception(errorMessage);
		}

	}

	public CTODaxDataBeanGeneral createCtoDataDaxGeneralBeanObj (PropagationEvent pe, 
			List<String > allKeysForMethodArgs, Map<String, String> processKeyMapValForMethodArg ) 
	throws Exception {
		CTODaxDataBeanGeneral ctoDaxDgbean= null;

		try {

			Class clDao = Class
			.forName("com.hp.psg.corona.propagation.handler.PropagationEventsCallHandler");

			// Create !!!
			Object[] inputParam = CoronaFwkUtil
			.getAllParamsArray(pe.getId(), pe
					.getSrcEventId(), allKeysForMethodArgs,
					processKeyMapValForMethodArg);

			Class[] par = new Class[allKeysForMethodArgs.size() + 2];
			par[0] = Long.class; // Parm type for PE_ID
			par[1] = Long.class; // Parm type for srcEventId.

			for (int i = 2; i <= allKeysForMethodArgs.size() + 1; i++)
				par[i] = String.class;

			String methodName = CoronaFwkUtil
			.getProcessorMethodNameUsingCachedMap(
					"get", pe.getProcessType());
			java.lang.reflect.Method mthd = clDao.getMethod(
					methodName, par);
			ctoDaxDgbean = (CTODaxDataBeanGeneral) mthd.invoke(
					new PropagationEventsCallHandler(),
					inputParam);

		} catch (Exception e) {
			String errorMessage ="ObjectCreation Failure (pe) "+pe.getId()+" { "+e.getCause()+" }";
			CoronaErrorHandler.logError(e, errorMessage,null);
			throw new Exception(errorMessage);
		}

		return ctoDaxDgbean;
	}



	public CTODaxDataBeanGeneral processTransformation(PropagationEvent pe, CTODaxDataBeanGeneral cdgBeanToTranform) throws Exception {
		CTODaxDataBeanGeneral outputBean= null;
		LoggerInfo logInfo = new LoggerInfo(this.getClass().getName());

		try {
			if (DataChangeEventsFwkDao
					.getHmTransoformationClassList().containsKey(
							pe.getProcessType().toUpperCase())) {
				List<CtoTransformationUtilMap> transformationObjects = DataChangeEventsFwkDao
				.getHmTransoformationClassList().get(
						pe.getProcessType().toUpperCase());
				if (transformationObjects != null
						&& transformationObjects.size() > 0) {
					for (CtoTransformationUtilMap utilObj : transformationObjects) {
						// Check for mappings defined in object or
						// not.
						if (utilObj.getMappingList() != null
								&& utilObj.getMappingList().size() > 0) {
							for (com.hp.psg.corona.util.cache.CtoTransformationUtilMap.MappingInfo mapRequired : utilObj
									.getMappingList()) {
								PropagationHandlerUtilities
								.mapRelationsWithMultipleKeys(
										cdgBeanToTranform,
										mapRequired
										.getCboParent(),
										mapRequired
										.getCboChild(),
										mapRequired
										.getBindingKey(),
										mapRequired
										.getBindingKeyDelim());
							}
						} else {
							Logger.info(logInfo,"process","No Mapping creation required for Object Mappings, Mapping List not created in XML document");
						}

						if (utilObj.getListObjectsOnKeys() != null
								&& utilObj.getListObjectsOnKeys()
								.size() > 0) {
							for (com.hp.psg.corona.util.cache.CtoTransformationUtilMap.MappingListOnKeyObject mapRequiredOfObjectsOnKeys : utilObj
									.getListObjectsOnKeys()) {
								PropagationHandlerUtilities
								.populateListObjectsWithMultipleKeys(
										cdgBeanToTranform,
										mapRequiredOfObjectsOnKeys
										.getCboObject(),
										mapRequiredOfObjectsOnKeys
										.getBindingKey(),
										mapRequiredOfObjectsOnKeys
										.getBindingKeyDelim());
							}
						} else {
							Logger.info(logInfo,"process","No Mapping creation required Object List on keys, Mapping List not created in XML document");
						}

						// Execute transformation class passing
						// ctoDaxObj.
						if (DataChangeEventsFwkDao
								.getHmPTtoPluginTypeMap()
								.containsKey(
										pe.getProcessType()
										.toUpperCase())) {
							String pluginType = DataChangeEventsFwkDao
							.getHmPTtoPluginTypeMap().get(
									pe.getProcessType()
									.toUpperCase());


							if ("internal"
									.equalsIgnoreCase(pluginType)) {
								Class transformClass = Class
								.forName(utilObj
										.getClassName());
								
								IBeanPropagation tranformCallObj = (IBeanPropagation) transformClass
								.newInstance();
								
								//To print the transformation mapping available in objects un-comment below statement.
								//cdgBeanToTranform.printItemsInRelationMap();

								//Added a new feature for prepare object first to set the variables.. before calling bean tranformation.
								if (utilObj.getConstructArguments() != null && utilObj.getConstructArguments().size() > 0){
									Map<String, Object> prepareBeanInitializer = CoronaFwkUtil.formatTranformInitMapObjects(utilObj.getConstructArguments());
									tranformCallObj.prepareBean(prepareBeanInitializer);
								}else {
									// Transofrmation does not need any prepare bean method call.
									Logger.debug(logInfo, "processTransformation", "Transofrmation does not need any prepare bean method call : "+utilObj.getClassName());
								}
								
								outputBean = tranformCallObj.processBeans(cdgBeanToTranform);
								
							} else if ("feature_businessprocess"
									.equalsIgnoreCase(pluginType)) {
								// Need to have the type of plugin
								// call also.
								Class transformClass = Class
								.forName(utilObj
										.getClassName());
								IBusinessProcessPlugin tranformCallObj = (IBusinessProcessPlugin) transformClass
								.newInstance();
								// Create feature plugin request
								// object.
								FeaturePluginRequest fpRequest = CoronaFwkUtil
								.getFeaturePluginRequestFromCTOBean(cdgBeanToTranform);
								FeaturePluginResult fpResult = tranformCallObj
								.execute(fpRequest);
								CTODaxDataBeanGeneral ctoDaxDgoutbean = CoronaFwkUtil
								.getCTOBeanFromFeaturePluginResult(
										outputBean,
										fpResult);
							} else if ("feature_validation"
									.equalsIgnoreCase(pluginType)) {
								// Need to have the type of plugin
								// call also.
								Class transformClass = Class
								.forName(utilObj
										.getClassName());
								IValidationPlugin tranformCallObj = (IValidationPlugin) transformClass
								.newInstance();
								// Create feature plugin request
								// object.
								FeaturePluginRequest fpRequest = CoronaFwkUtil
								.getFeaturePluginRequestFromCTOBean(cdgBeanToTranform);
								FeaturePluginResult fpResult = tranformCallObj
								.validate(fpRequest);
								CTODaxDataBeanGeneral ctoDaxDgoutbean = CoronaFwkUtil
								.getCTOBeanFromFeaturePluginResult(
										outputBean,
										fpResult);
							} else if ("feature_transformation"
									.equalsIgnoreCase(pluginType)) {
								// Need to have the type of plugin
								// call also.
								Class transformClass = Class
								.forName(utilObj
										.getClassName());
								ITransformationPlugin tranformCallObj = (ITransformationPlugin) transformClass
								.newInstance();
								// Create feature plugin request
								// object.
								FeaturePluginRequest fpRequest = CoronaFwkUtil
								.getFeaturePluginRequestFromCTOBean(cdgBeanToTranform);
								FeaturePluginResult fpResult = tranformCallObj
								.transform(fpRequest);
								CTODaxDataBeanGeneral ctoDaxDgoutbean = CoronaFwkUtil
								.getCTOBeanFromFeaturePluginResult(
										outputBean,
										fpResult);
							}
						} else {
							Logger.error(logInfo,"process","Whats the plugin type to be called ???? internal or feature, please update the XML.", new Exception("Type of plugin is not correct"));
						}
					}
				} else {
					// No trnasformation needed.
					Logger.info(logInfo,"process","No transformation needed... Map does not contain the key for "
							+ pe.getProcessType().toUpperCase());
					outputBean = cdgBeanToTranform;
				}
			} else {
				// No trnasformation needed.
				Logger.info(logInfo,"process","No transformation needed... Map does not contain the key for "
						+ pe.getProcessType().toUpperCase());
				outputBean = cdgBeanToTranform;
			}
		}catch (Exception ex){
			String errorMessage ="Transformation Failure (pe) "+pe.getId()+" { "+ex.getCause()+" }";
			CoronaErrorHandler.logError(ex, errorMessage, null);
		}
		return outputBean;
	}



	public void processTranformedObjects (PropagationEvent pe, CTODaxDataBeanGeneral processedBean) throws Exception{
		LoggerInfo logInfo = new LoggerInfo(this.getClass().getName());
		
		try {
			Class clDao = Class
			.forName("com.hp.psg.corona.propagation.handler.PropagationEventsCallHandler");

			List<Object> inputParamBeans = new ArrayList<Object>();
			List parList = new ArrayList();

			Class[] par = new Class[1];
			String methodName = CoronaFwkUtil
			.getProcessorMethodNameUsingCachedMap(
					"process", pe.getProcessType());

			if (!(PropagationEventsFwkDao
					.getPropagationCallHandlerMethodMap()
					.containsKey(methodName.trim()))) {
				throw new Exception(
				"Method signature not found in cached Map for PropagationCallHandlerMethodMap !");
			} else {
				List<String> paramTypes = PropagationEventsFwkDao
				.getPropagationCallHandlerMethodMap()
				.get(methodName);
				Class classDxDataGeneralBean = Class
				.forName("com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral");

				inputParamBeans.add(pe.getId()); // For new
				// parameter
				// ID added.

				for (String strParamType : paramTypes) {
					String beanMethod = "get" + strParamType;
					if (!"Long".equals(strParamType)) {

						java.lang.reflect.Method mthd = classDxDataGeneralBean
						.getMethod(beanMethod);
						Logger.info(logInfo,"process","Adding method and type for "
								+ strParamType
								+ " for method name "
								+ mthd.getName());
						inputParamBeans.add(mthd
								.invoke(processedBean));

						// ToDo : Thinking here all methods
						// required to be passed as Array and
						// only need the beans.
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

				parArrayForList[0] = Long.class; // Param for
				// Id

				for (int i = 0; i < parList.size(); i++) {
					Object obj = parList.get(i);
					parArrayForList[i + 1] = obj.getClass();
				}

				java.lang.reflect.Method mthd2 = clDao
				.getMethod(methodName, parArrayForList);

				mthd2.invoke(
						new PropagationEventsCallHandler(),
						inputParamBeans.toArray());
			}
			pe.setEndDate(new java.util.Date());
			PropagationEventsFwkDao
			.updatePropagationEventStatus(
					pe,
					CoronaFwkConstants.ProcessingStatus.COMPLETED,
					"Event Success !");
		} catch (Exception ex) {
			String errorMessage ="Processing Failure (pe) "+pe.getId()+" { "+ex.getCause()+" }";
			CoronaErrorHandler.logError(ex, errorMessage,null);
			throw new Exception(errorMessage);
		}
	}
	
	public void process() {
		LoggerInfo logInfo = new LoggerInfo(this.getClass().getName());
		
		try {
			String groupStatusTillNow = CoronaFwkConstants.Success;
			
			for (PropagationEvent pe : pEvents) {
				if (propagationEventGroupId == null)
					propagationEventGroupId = pe.getGroupId();

				pe.setStartDate(new java.util.Date());
				PropagationEventsFwkDao.updatePropagationEventStatus(pe,
						CoronaFwkConstants.ProcessingStatus.IN_PROGRESS, null);

				
				if ((CoronaFwkConstants.Failure).equals(groupStatusTillNow)){
					//If status is ERROR for this GROUP, set rest of events to ERROR_WAIT.
					PropagationEventsFwkDao.updatePropagationEventStatus(pe,
							CoronaFwkConstants.ProcessingStatus.ERROR_WAIT, "Priority events in this group already failed !, no need to proceed further ! ");
				}else {
					//Continue processing further.
					
					try {
						
						if (Config.doProcessingInStoredProc() && 
								("CONFIG_PRICE_UPDATE".equalsIgnoreCase(pe.getProcessType())
										||
								 "LIST_PRICE_ADD".equalsIgnoreCase(pe.getProcessType())
										)){
							Logger.debug(logInfo, "process", "Direct processing for process_type CONFIG_PRICE_UPDATE in propagation handler ");
							
							long startTime=System.currentTimeMillis();
							processEventsInStoredProc(pe);
							Logger.perfInfo(logInfo, "process" , "(TimeStamp - "+new java.util.Date()+" ) - Datachange object processing for PROCESS_TYPE () - "+pe.getId()+" - took processing(timeInSeconds) - "+(System.currentTimeMillis()-startTime)/1000);

						}else {
							
							List<String> allKeysForMethodArgs = CoronaFwkUtil.getSourceFormatKeys(pe
									.getProcessType());
		
							Map<String, String> processKeyMapVal = CoronaFwkUtil
							.getFormatKeyValueMap(allKeysForMethodArgs, pe.getProcessKey());
							if (processKeyMapVal != null) {
								CTODaxDataBeanGeneral outputBean=null;
		
								//Create !!
								Logger.debug(logInfo, "process", "Calling object creation for propagationEvent  - "+pe.getId());
								long startTime=System.currentTimeMillis();
								
								CTODaxDataBeanGeneral ctoDaxDgbean= createCtoDataDaxGeneralBeanObj(pe, allKeysForMethodArgs, processKeyMapVal);
		
								//Tranformation !!
								Logger.perfInfo(logInfo, "process" , "(TimeStamp - "+new java.util.Date()+" ) - Propagation object creation for - "+pe.getId()+" took processing(timeInSeconds) - "+(System.currentTimeMillis()-startTime)/1000);
								startTime=System.currentTimeMillis();
								
								if (ctoDaxDgbean != null){
									Logger.debug(logInfo, "process", "Calling Tranformation classes for propagationEvent  - "+pe.getId());
									outputBean=processTransformation(pe, ctoDaxDgbean);
								}else {
									CoronaErrorHandler.logError(new Exception("Output bean from transformation is null for "+pe.getId()), "Output bean from transformation is null for "+pe.getId(), null);
									outputBean = ctoDaxDgbean;
								}
		
								//Update processed Objects
								Logger.perfInfo(logInfo, "process", "(TimeStamp - "+new java.util.Date()+" ) - Propagation transformation for - "+pe.getId() +" took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
								startTime=System.currentTimeMillis();
								
								processTranformedObjects(pe, outputBean);
								
								Logger.perfInfo(logInfo, "process" , "(TimeStamp - "+new java.util.Date()+" ) - Propagation stored proc call for - "+pe.getId() +" took processing(timestamp)- "+(System.currentTimeMillis()-startTime)/1000);
		
							} else {
								Logger.info(logInfo,"process","No transformation needed... Map does not contain the key for "
										+ pe.getProcessType().toUpperCase());
								PropagationEventsFwkDao.updatePropagationEventStatus(
										pe, CoronaFwkConstants.ProcessingStatus.ERROR,
								"XML does not contain the process type for this event!!");
								
								//Setting group status ='ERROR'
								groupStatusTillNow = CoronaFwkConstants.Failure;
								
							}
						}
					} catch (Exception ex) {
						PropagationEventsFwkDao.updatePropagationEventStatus(pe,
								CoronaFwkConstants.ProcessingStatus.ERROR, ex
								.getMessage());
						CoronaErrorHandler.logError(ex, "XML does not contains this process type "+pe.getProcessType(), null);
						
						//Setting group status ='ERROR'
						groupStatusTillNow = CoronaFwkConstants.Failure;
					}
				}
			}
			processingStatusGroup = CoronaFwkConstants.Success;
		} catch (Exception e) {
			processingStatusGroup = CoronaFwkConstants.Failure;
			CoronaErrorHandler.logError(e, null, null);
		}
	}

	public Long getPropagationEventGroupId() {
		return propagationEventGroupId;
	}

	public void setPropagationEventGroupId(Long propagationEventGroupId) {
		this.propagationEventGroupId = propagationEventGroupId;
	}
}
