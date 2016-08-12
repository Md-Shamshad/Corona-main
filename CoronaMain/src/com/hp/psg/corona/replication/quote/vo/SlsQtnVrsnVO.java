package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;
/**
 * Value object to hold SLS_QTN_VRSN table data
 * @author rohitc
 *
 */
public class SlsQtnVrsnVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long slsQtnID;
	private String slsQtnVrsnSqnNr;
	private String description;
	private Date creationTS;
	private Date expiryTS;
	private String currencyCd;
	private Date handOffTS;
	private String slsQtnVrsnRsnCd;
	private Date effectiveTS;
	private String slsQtnVrsnTypCd;
	private String slsQtnVrsnRqstrCd;
	private String slsQtnVrsnTatGoalCd;
	private String countryCd;
	private String langCd;
	private String charScriptCd;
	private String slsQtnTatXcddRsnCd;
	private String custPrchOrdNr;
	private Date rqstFOrdRcvdTS;
	private Date custRqstHndOffTS;
	private String entrsLglEntNr;
	private String slsQtnVrsnSttsCd;
	private Long slsPersID;
	private String slsChnlCd;
	private Date slsQtnVrsnTatGoalTS;
	private String tatXcddInd;
	private Date dfltRqrdDlvryTS;
	private Long dfltRqrdDlvryLeadTmQty;
	private Long dfltInfcrOtrPrtySiteID;
	private Long dfltUltCnsgOtrPrtySiteID;
	private Long dfltPyrOtrPrtySiteInsnID;
	private Long dfltEndCustOtrPrtySiteID;
	private Long dfltInvToOtrPrtySiteID;
	private Long dfltShpToOtrPrtySiteID;
	private Long sldToOtrPrtySiteInsnID;
	private String cstmCnfgnInd;
	private String cnfgnOvrdAprvlInd;
	private char deleteInd;
	private Date createsTS;
	private String createdBy;
	private Date lastModifiedTS;
	private String lastModifiedBy;
	private Date custPrqstTS;
	
	public Long getSlsQtnID() {
		return slsQtnID;
	}
	public void setSlsQtnID(Long slsQtnID) {
		this.slsQtnID = slsQtnID;
	}
	public String getSlsQtnVrsnSqnNr() {
		return slsQtnVrsnSqnNr;
	}
	public void setSlsQtnVrsnSqnNr(String slsQtnVrsnSqnNr) {
		this.slsQtnVrsnSqnNr = slsQtnVrsnSqnNr;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreationTS() {
		return creationTS;
	}
	public void setCreationTS(Date creationTS) {
		this.creationTS = creationTS;
	}
	public Date getExpiryTS() {
		return expiryTS;
	}
	public void setExpiryTS(Date expiryTS) {
		this.expiryTS = expiryTS;
	}
	public String getCurrencyCd() {
		return currencyCd;
	}
	public void setCurrencyCd(String currencyCd) {
		this.currencyCd = currencyCd;
	}
	public Date getHandOffTS() {
		return handOffTS;
	}
	public void setHandOffTS(Date handOffTS) {
		this.handOffTS = handOffTS;
	}
	public String getSlsQtnVrsnRsnCd() {
		return slsQtnVrsnRsnCd;
	}
	public void setSlsQtnVrsnRsnCd(String slsQtnVrsnRsnCd) {
		this.slsQtnVrsnRsnCd = slsQtnVrsnRsnCd;
	}
	public Date getEffectiveTS() {
		return effectiveTS;
	}
	public void setEffectiveTS(Date effectiveTS) {
		this.effectiveTS = effectiveTS;
	}
	public String getSlsQtnVrsnTypCd() {
		return slsQtnVrsnTypCd;
	}
	public void setSlsQtnVrsnTypCd(String slsQtnVrsnTypCd) {
		this.slsQtnVrsnTypCd = slsQtnVrsnTypCd;
	}
	public String getSlsQtnVrsnRqstrCd() {
		return slsQtnVrsnRqstrCd;
	}
	public void setSlsQtnVrsnRqstrCd(String slsQtnVrsnRqstrCd) {
		this.slsQtnVrsnRqstrCd = slsQtnVrsnRqstrCd;
	}
	public String getSlsQtnVrsnTatGoalCd() {
		return slsQtnVrsnTatGoalCd;
	}
	public void setSlsQtnVrsnTatGoalCd(String slsQtnVrsnTatGoalCd) {
		this.slsQtnVrsnTatGoalCd = slsQtnVrsnTatGoalCd;
	}
	public String getCountryCd() {
		return countryCd;
	}
	public void setCountryCd(String countryCd) {
		this.countryCd = countryCd;
	}
	public String getLangCd() {
		return langCd;
	}
	public void setLangCd(String langCd) {
		this.langCd = langCd;
	}
	public String getCharScriptCd() {
		return charScriptCd;
	}
	public void setCharScriptCd(String charScriptCd) {
		this.charScriptCd = charScriptCd;
	}
	public String getSlsQtnTatXcddRsnCd() {
		return slsQtnTatXcddRsnCd;
	}
	public void setSlsQtnTatXcddRsnCd(String slsQtnTatXcddRsnCd) {
		this.slsQtnTatXcddRsnCd = slsQtnTatXcddRsnCd;
	}
	public String getCustPrchOrdNr() {
		return custPrchOrdNr;
	}
	public void setCustPrchOrdNr(String custPrchOrdNr) {
		this.custPrchOrdNr = custPrchOrdNr;
	}
	public Date getRqstFOrdRcvdTS() {
		return rqstFOrdRcvdTS;
	}
	public void setRqstFOrdRcvdTS(Date rqstFOrdRcvdTS) {
		this.rqstFOrdRcvdTS = rqstFOrdRcvdTS;
	}
	public Date getCustRqstHndOffTS() {
		return custRqstHndOffTS;
	}
	public void setCustRqstHndOffTS(Date custRqstHndOffTS) {
		this.custRqstHndOffTS = custRqstHndOffTS;
	}
	public String getEntrsLglEntNr() {
		return entrsLglEntNr;
	}
	public void setEntrsLglEntNr(String entrsLglEntNr) {
		this.entrsLglEntNr = entrsLglEntNr;
	}
	public String getSlsQtnVrsnSttsCd() {
		return slsQtnVrsnSttsCd;
	}
	public void setSlsQtnVrsnSttsCd(String slsQtnVrsnSttsCd) {
		this.slsQtnVrsnSttsCd = slsQtnVrsnSttsCd;
	}
	public Long getSlsPersID() {
		return slsPersID;
	}
	public void setSlsPersID(Long slsPersID) {
		this.slsPersID = slsPersID;
	}
	public String getSlsChnlCd() {
		return slsChnlCd;
	}
	public void setSlsChnlCd(String slsChnlCd) {
		this.slsChnlCd = slsChnlCd;
	}
	public Date getSlsQtnVrsnTatGoalTS() {
		return slsQtnVrsnTatGoalTS;
	}
	public void setSlsQtnVrsnTatGoalTS(Date slsQtnVrsnTatGoalTS) {
		this.slsQtnVrsnTatGoalTS = slsQtnVrsnTatGoalTS;
	}
	public String getTatXcddInd() {
		return tatXcddInd;
	}
	public void setTatXcddInd(String tatXcddInd) {
		this.tatXcddInd = tatXcddInd;
	}
	public Date getDfltRqrdDlvryTS() {
		return dfltRqrdDlvryTS;
	}
	public void setDfltRqrdDlvryTS(Date dfltRqrdDlvryTS) {
		this.dfltRqrdDlvryTS = dfltRqrdDlvryTS;
	}
	public Long getDfltRqrdDlvryLeadTmQty() {
		return dfltRqrdDlvryLeadTmQty;
	}
	public void setDfltRqrdDlvryLeadTmQty(Long dfltRqrdDlvryLeadTmQty) {
		this.dfltRqrdDlvryLeadTmQty = dfltRqrdDlvryLeadTmQty;
	}
	public Long getDfltInfcrOtrPrtySiteID() {
		return dfltInfcrOtrPrtySiteID;
	}
	public void setDfltInfcrOtrPrtySiteID(Long dfltInfcrOtrPrtySiteID) {
		this.dfltInfcrOtrPrtySiteID = dfltInfcrOtrPrtySiteID;
	}
	public Long getDfltUltCnsgOtrPrtySiteID() {
		return dfltUltCnsgOtrPrtySiteID;
	}
	public void setDfltUltCnsgOtrPrtySiteID(Long dfltUltCnsgOtrPrtySiteID) {
		this.dfltUltCnsgOtrPrtySiteID = dfltUltCnsgOtrPrtySiteID;
	}
	public Long getDfltPyrOtrPrtySiteInsnID() {
		return dfltPyrOtrPrtySiteInsnID;
	}
	public void setDfltPyrOtrPrtySiteInsnID(Long dfltPyrOtrPrtySiteInsnID) {
		this.dfltPyrOtrPrtySiteInsnID = dfltPyrOtrPrtySiteInsnID;
	}
	public Long getDfltEndCustOtrPrtySiteID() {
		return dfltEndCustOtrPrtySiteID;
	}
	public void setDfltEndCustOtrPrtySiteID(Long dfltEndCustOtrPrtySiteID) {
		this.dfltEndCustOtrPrtySiteID = dfltEndCustOtrPrtySiteID;
	}
	public Long getDfltInvToOtrPrtySiteID() {
		return dfltInvToOtrPrtySiteID;
	}
	public void setDfltInvToOtrPrtySiteID(Long dfltInvToOtrPrtySiteID) {
		this.dfltInvToOtrPrtySiteID = dfltInvToOtrPrtySiteID;
	}
	public Long getDfltShpToOtrPrtySiteID() {
		return dfltShpToOtrPrtySiteID;
	}
	public void setDfltShpToOtrPrtySiteID(Long dfltShpToOtrPrtySiteID) {
		this.dfltShpToOtrPrtySiteID = dfltShpToOtrPrtySiteID;
	}
	public Long getSldToOtrPrtySiteInsnID() {
		return sldToOtrPrtySiteInsnID;
	}
	public void setSldToOtrPrtySiteInsnID(Long sldToOtrPrtySiteInsnID) {
		this.sldToOtrPrtySiteInsnID = sldToOtrPrtySiteInsnID;
	}
	public String getCstmCnfgnInd() {
		return cstmCnfgnInd;
	}
	public void setCstmCnfgnInd(String cstmCnfgnInd) {
		this.cstmCnfgnInd = cstmCnfgnInd;
	}
	public String getCnfgnOvrdAprvlInd() {
		return cnfgnOvrdAprvlInd;
	}
	public void setCnfgnOvrdAprvlInd(String cnfgnOvrdAprvlInd) {
		this.cnfgnOvrdAprvlInd = cnfgnOvrdAprvlInd;
	}
	public char getDeleteInd() {
		return deleteInd;
	}
	public void setDeleteInd(char deleteInd) {
		this.deleteInd = deleteInd;
	}
	public Date getCreatesTS() {
		return createsTS;
	}
	public void setCreatesTS(Date createsTS) {
		this.createsTS = createsTS;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getLastModifiedTS() {
		return lastModifiedTS;
	}
	public void setLastModifiedTS(Date lastModifiedTS) {
		this.lastModifiedTS = lastModifiedTS;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public Date getCustPrqstTS() {
		return custPrqstTS;
	}
	public void setCustPrqstTS(Date custPrqstTS) {
		this.custPrqstTS = custPrqstTS;
	}
}
