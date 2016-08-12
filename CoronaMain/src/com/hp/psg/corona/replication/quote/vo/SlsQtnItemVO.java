package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;
/**
 * Value object to hold SLS_QTN_ITEM table data
 * @author rohitc
 *
 */
public class SlsQtnItemVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String slsQtnItmSqnNr;
	private Long slsQtnID;
	private String slsQtnVrsnSqnNr;
	private Long slsQtnPrcgGrpID;
	private Double agrmntVrblRtPc;
	private Double agrmntVrblLclAmt;
	private String slsQtnSctnSqnNr;
	private String custSpcfDn;
	private Integer dsplySqnNr;
	private Date prodPrcEffTs;
	private Date prodLastPrcTs;
	private Long quantity;
	private Double lclXtnfNtAmt;
	private Double lclUntLstAmt;
	private Double lclUntNtAmt;
	private Double acctXtndNtAmt;
	private Double acctUntLstAmt;
	private Double acctUntNtAmt;
	private String explsnInd;
	private String prodClsfnCd;
	private Long prodID;
	private String untOfMsrCd;
	private String prodBsPrcTypCd;
	private Long wrldRgnID;
	private Long prodPrmtnID;
	private String slsAgrmntInd;
	private String slsOpprtntyInd;
	private String prcOvrrdInd;
	private String prcgGrpInd;
	private Date prcTs;
	private Long slsOprtnyID;
	private String slsOprtnyItmNr;
	private String slsEvtTrmsTmplGrpCd;
	private String agrmntVrblPrcInd;
	private String prcAdjMtdCd;
	private Double agrmntVrblAcctAmt;
	private char deleteInd;
	private Date createdTs;
	private String createdBy;
	private Date lastModifiedTs;
	private String lastModifiedBy;
	
	public String getSlsQtnItmSqnNr() {
		return slsQtnItmSqnNr;
	}
	public void setSlsQtnItmSqnNr(String slsQtnItmSqnNr) {
		this.slsQtnItmSqnNr = slsQtnItmSqnNr;
	}
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
	public Long getSlsQtnPrcgGrpID() {
		return slsQtnPrcgGrpID;
	}
	public void setSlsQtnPrcgGrpID(Long slsQtnPrcgGrpID) {
		this.slsQtnPrcgGrpID = slsQtnPrcgGrpID;
	}
	public Double getAgrmntVrblRtPc() {
		return agrmntVrblRtPc;
	}
	public void setAgrmntVrblRtPc(Double agrmntVrblRtPc) {
		this.agrmntVrblRtPc = agrmntVrblRtPc;
	}
	public Double getAgrmntVrblLclAmt() {
		return agrmntVrblLclAmt;
	}
	public void setAgrmntVrblLclAmt(Double agrmntVrblLclAmt) {
		this.agrmntVrblLclAmt = agrmntVrblLclAmt;
	}
	public String getSlsQtnSctnSqnNr() {
		return slsQtnSctnSqnNr;
	}
	public void setSlsQtnSctnSqnNr(String slsQtnSctnSqnNr) {
		this.slsQtnSctnSqnNr = slsQtnSctnSqnNr;
	}
	public String getCustSpcfDn() {
		return custSpcfDn;
	}
	public void setCustSpcfDn(String custSpcfDn) {
		this.custSpcfDn = custSpcfDn;
	}
	public Integer getDsplySqnNr() {
		return dsplySqnNr;
	}
	public void setDsplySqnNr(Integer dsplySqnNr) {
		this.dsplySqnNr = dsplySqnNr;
	}
	public Date getProdPrcEffTs() {
		return prodPrcEffTs;
	}
	public void setProdPrcEffTs(Date prodPrcEffTs) {
		this.prodPrcEffTs = prodPrcEffTs;
	}
	public Date getProdLastPrcTs() {
		return prodLastPrcTs;
	}
	public void setProdLastPrcTs(Date prodLastPrcTs) {
		this.prodLastPrcTs = prodLastPrcTs;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public Double getLclXtnfNtAmt() {
		return lclXtnfNtAmt;
	}
	public void setLclXtnfNtAmt(Double lclXtnfNtAmt) {
		this.lclXtnfNtAmt = lclXtnfNtAmt;
	}
	public Double getLclUntLstAmt() {
		return lclUntLstAmt;
	}
	public void setLclUntLstAmt(Double lclUntLstAmt) {
		this.lclUntLstAmt = lclUntLstAmt;
	}
	public Double getLclUntNtAmt() {
		return lclUntNtAmt;
	}
	public void setLclUntNtAmt(Double lclUntNtAmt) {
		this.lclUntNtAmt = lclUntNtAmt;
	}
	public Double getAcctXtndNtAmt() {
		return acctXtndNtAmt;
	}
	public void setAcctXtndNtAmt(Double acctXtndNtAmt) {
		this.acctXtndNtAmt = acctXtndNtAmt;
	}
	public Double getAcctUntLstAmt() {
		return acctUntLstAmt;
	}
	public void setAcctUntLstAmt(Double acctUntLstAmt) {
		this.acctUntLstAmt = acctUntLstAmt;
	}
	public Double getAcctUntNtAmt() {
		return acctUntNtAmt;
	}
	public void setAcctUntNtAmt(Double acctUntNtAmt) {
		this.acctUntNtAmt = acctUntNtAmt;
	}
	public String getExplsnInd() {
		return explsnInd;
	}
	public void setExplsnInd(String explsnInd) {
		this.explsnInd = explsnInd;
	}
	public String getProdClsfnCd() {
		return prodClsfnCd;
	}
	public void setProdClsfnCd(String prodClsfnCd) {
		this.prodClsfnCd = prodClsfnCd;
	}
	public Long getProdID() {
		return prodID;
	}
	public void setProdID(Long prodID) {
		this.prodID = prodID;
	}
	public String getUntOfMsrCd() {
		return untOfMsrCd;
	}
	public void setUntOfMsrCd(String untOfMsrCd) {
		this.untOfMsrCd = untOfMsrCd;
	}
	public String getProdBsPrcTypCd() {
		return prodBsPrcTypCd;
	}
	public void setProdBsPrcTypCd(String prodBsPrcTypCd) {
		this.prodBsPrcTypCd = prodBsPrcTypCd;
	}
	public Long getWrldRgnID() {
		return wrldRgnID;
	}
	public void setWrldRgnID(Long wrldRgnID) {
		this.wrldRgnID = wrldRgnID;
	}
	public Long getProdPrmtnID() {
		return prodPrmtnID;
	}
	public void setProdPrmtnID(Long prodPrmtnID) {
		this.prodPrmtnID = prodPrmtnID;
	}
	public String getSlsAgrmntInd() {
		return slsAgrmntInd;
	}
	public void setSlsAgrmntInd(String slsAgrmntInd) {
		this.slsAgrmntInd = slsAgrmntInd;
	}
	public String getSlsOpprtntyInd() {
		return slsOpprtntyInd;
	}
	public void setSlsOpprtntyInd(String slsOpprtntyInd) {
		this.slsOpprtntyInd = slsOpprtntyInd;
	}
	public String getPrcOvrrdInd() {
		return prcOvrrdInd;
	}
	public void setPrcOvrrdInd(String prcOvrrdInd) {
		this.prcOvrrdInd = prcOvrrdInd;
	}
	public String getPrcgGrpInd() {
		return prcgGrpInd;
	}
	public void setPrcgGrpInd(String prcgGrpInd) {
		this.prcgGrpInd = prcgGrpInd;
	}
	public Date getPrcTs() {
		return prcTs;
	}
	public void setPrcTs(Date prcTs) {
		this.prcTs = prcTs;
	}
	public Long getSlsOprtnyID() {
		return slsOprtnyID;
	}
	public void setSlsOprtnyID(Long slsOprtnyID) {
		this.slsOprtnyID = slsOprtnyID;
	}
	public String getSlsOprtnyItmNr() {
		return slsOprtnyItmNr;
	}
	public void setSlsOprtnyItmNr(String slsOprtnyItmNr) {
		this.slsOprtnyItmNr = slsOprtnyItmNr;
	}
	public String getSlsEvtTrmsTmplGrpCd() {
		return slsEvtTrmsTmplGrpCd;
	}
	public void setSlsEvtTrmsTmplGrpCd(String slsEvtTrmsTmplGrpCd) {
		this.slsEvtTrmsTmplGrpCd = slsEvtTrmsTmplGrpCd;
	}
	public String getAgrmntVrblPrcInd() {
		return agrmntVrblPrcInd;
	}
	public void setAgrmntVrblPrcInd(String agrmntVrblPrcInd) {
		this.agrmntVrblPrcInd = agrmntVrblPrcInd;
	}
	public String getPrcAdjMtdCd() {
		return prcAdjMtdCd;
	}
	public void setPrcAdjMtdCd(String prcAdjMtdCd) {
		this.prcAdjMtdCd = prcAdjMtdCd;
	}
	public Double getAgrmntVrblAcctAmt() {
		return agrmntVrblAcctAmt;
	}
	public void setAgrmntVrblAcctAmt(Double agrmntVrblAcctAmt) {
		this.agrmntVrblAcctAmt = agrmntVrblAcctAmt;
	}
	public char getDeleteInd() {
		return deleteInd;
	}
	public void setDeleteInd(char deleteInd) {
		this.deleteInd = deleteInd;
	}
	public Date getCreatedTs() {
		return createdTs;
	}
	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getLastModifiedTs() {
		return lastModifiedTs;
	}
	public void setLastModifiedTs(Date lastModifiedTs) {
		this.lastModifiedTs = lastModifiedTs;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
}
