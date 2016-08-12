package com.hp.psg.corona.propagation.beans;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.psg.corona.common.beans.PropagationEvent;
import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class PropagationEventsGroup extends CoronaBaseObject {

	List<PropagationEvent> propagationEvents = new ArrayList<PropagationEvent>();

	Long priority;
	Throwable error;
	Long propagationEventsGroupId;
	String status;

	String srcKey;
	String source;
	private String whereClause;
	Date lastModifiedDate;

	/** Gets the error value * */
	public Throwable getError() {
		return this.error;
	}

	/** Sets the error value * */
	public void setError(Throwable error) {
		this.error = error;
	}

	public Long getEvfPropEventsGroupId() {
		return (getPropagationEventsGroupId() != null
				? getPropagationEventsGroupId()
				: null);
	}

	public void setEvfPropEventsGroupId(Long evfPropEventsGroupId) {
		setPropagationEventsGroupId((evfPropEventsGroupId != null
				? evfPropEventsGroupId.longValue()
				: null));
	}

	/**
	 * 
	 * @return whereClause
	 */
	public String getWhereClause() {
		return whereClause;
	}

	/**
	 * 
	 * @param whereClause
	 */
	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	/** ***************************************************************** */
	private Map<Integer, List<PropagationEvent>> eventMap = new HashMap<Integer, List<PropagationEvent>>();

	/** no argument consturctor ** */
	public PropagationEventsGroup() {
		super();
	}

	public void reMoveGroup() {
		this.propagationEventsGroupId = null;
		for (PropagationEvent pe : this.propagationEvents) {
			pe.setGroupId(null);
		}
	}

	/**
	 * return a debug string for this object
	 * 
	 * @return
	 */
	public String toDebugString() {
		StringBuffer sb = new StringBuffer();
		sb.append("PropagationEventsGroupId=");
		sb.append(
				(this.propagationEventsGroupId == null)
						? "null"
						: this.propagationEventsGroupId).append("\n");
		sb.append("priority=");
		sb.append((this.priority == null) ? "null" : this.priority)
				.append("\n");
		PropagationEvent pe = null;

		if ((propagationEvents != null) && (propagationEvents.size() > 0)) {
			// for(PropagationEvent pe : PropagationEvents){
			for (int ii = 0; ii < propagationEvents.size(); ii++) {
				pe = (PropagationEvent) propagationEvents.get(ii);
				sb.append("PropagationEventObj=").append(pe.toString()).append(
						"\n");
			}
		} else {
			sb.append("No PropagationEvent.");
		}

		return sb.toString();

	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void addPropagationEvent(PropagationEvent propagationEvent) {
		// TODO check group id first
		if (propagationEvent != null) {
			propagationEvents.add(propagationEvent);
			propagationEvent.setGroupId(this.propagationEventsGroupId);
			doPriority(propagationEvent);
		}
	}

	protected void doPriority(PropagationEvent pEvent) {
		applyDefaultPriorityRule(pEvent);
	}

	protected void applyDefaultPriorityRule(PropagationEvent pEvent) {
		Long currPriority = this.getPriority();
		Long eventPriority = pEvent.getPriority();

		if (eventPriority != null) {
			if (currPriority != null) {
				if (eventPriority.compareTo(currPriority) > 0) {
					this.setPriority(eventPriority);
					this.setLastModifiedDate(pEvent.getLastModifiedDate());
				}
			} else {
				this.setPriority(eventPriority);
				this.setLastModifiedDate(pEvent.getLastModifiedDate());
			}
		}
	}

	public List<PropagationEvent> getPropagationEvents() {
		return propagationEvents;
	}

	public void setPropagationEvents(List<PropagationEvent> propagationEvents) {
		this.propagationEvents = propagationEvents;
	}

	public Long getPropagationEventsGroupId() {
		return propagationEventsGroupId;
	}

	public void setPropagationEventsGroupId(Long propagationEventsGroupId) {
		this.propagationEventsGroupId = propagationEventsGroupId;
	}

	public String getSrcKey() {
		return srcKey;
	}

	public void setSrcKey(String srcKey) {
		this.srcKey = srcKey;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getStatus() {
		return status;
	}

	/** The group id is passed in argument ** */
	public PropagationEventsGroup(Long propagationEventsGroupId) {
		this.propagationEventsGroupId = propagationEventsGroupId;
	}

	/**
	 * overwrite the super method.
	 */
	public void setStatus(String status) {
		for (PropagationEvent pe : propagationEvents) {
			pe.setProcessingStatus(status);
		}
	}

}
