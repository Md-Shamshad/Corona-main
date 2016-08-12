package com.hp.psg.corona.common.constants;

/**
 * @author dudeja
 * @version 1.0
 * 
 */

public class CTOConstants {

	public static final String VALID_STATUS = "V";
	public static final String INVALID_STATUS = "I";
	public static final String PENDING_STATUS = "P";
	public static final String PRICE_PENDING_STATUS = "W";
	public static final String ERROR_STATUS = "E";
	public static final String MISSING_STATUS = "M";
	public static final String REGISTERED_STATUS = "R";

	public static final String REGION_WW = "WW";
	public static final String REGION_US = "US";
	public static final String REGION_NA = "NA";
	public static final String REGION_EU = "EU";
	public static final String REGION_AP = "AP";

	// Constants to look up MS descriptions
	public static final String MS_OBJ_NAME = "CtoDescPropagation";
	public static final String MS_ATTR_NAME = "DESC_KEYWORD";
	
	//Max Long description Length
	public static final int MAX_LENGTH_LONG_DESC = 1970;
	
	public static final String STARTING_PT_FLAG_SET = "Y";
	public static final String STARTING_PT_FLAG_NOT_SET = "N";

	public static final String UPDT_FLAG_UNCHANGED = "U";
	public static final String UPDT_FLAG_MODIFIED = "M";
	public static final String UPDT_FLAG_ADD = "A";
	public static final String UPDT_FLAG_DELETE = "D";

	public static final String DELETE_FLAG_SET = "Y";
	public static final String DELETE_FLAG_NOT_SET = "N";

	public static final String PRODUCT_ID_DELIMITER = "#";

	public static final int DEFAULT_START_DAYS = 0;
	public static final int DEFAULT_END_DAYS = 365*89;
	//public static final int DEFAULT_BEFORE_START_DAYS = -30;
	//public static final int DEFAULT_AFTER_END_DAYS = -30;

	// Constants to lookup ConfigPermutation Transformation
	public static final String CPI2CHI_LASTMOD_NAME = "CPI_2_CHI";
	public static final Long CPI2CHI_TRANSACTION_ID = new Long(0);

	// Class Names
	public static final String CONFIG_HEADER_INFO = "ConfigHeaderInfo";
	public static final String CONFIG_COMPONENT_INFO = "ConfigComponentInfo";
	public static final String CONFIG_DESCRIPTION = "ConfigDescription";
	public static final String PRODUCT_DESCRIPTION = "ProductDescription";
	public static final String CONFIG_PERMUTATION_INFO = "ConfigPermutationInfo";
	public static final String CONFIG_PERMUTATION_PRICE_INFO = "ConfigPermutationPriceInfo";

}
