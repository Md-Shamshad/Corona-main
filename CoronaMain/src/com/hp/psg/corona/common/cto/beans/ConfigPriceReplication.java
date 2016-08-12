package com.hp.psg.corona.common.cto.beans;

import java.util.Date;


public class ConfigPriceReplication extends CoronaBaseObject{

			public ConfigPriceReplication() {
				// TODO Auto-generated constructor stub
				this.setType("ConfigPriceReplication");
			}

			private Long cphiId;		
			private Date cphiLastModifiedDate;		
			private Long groupId;		
			private Date createdDate;		
			private String createdBy;		
			private Date lastModifiedDate;		
			private String lastModifiedBy;
			
			public Long getCphiId() {
				return cphiId;
			}
			public void setCphiId(Long cphiId) {
				this.cphiId = cphiId;
			}
			public Date getCphiLastModifiedDate() {
				return cphiLastModifiedDate;
			}
			public void setCphiLastModifiedDate(Date cphiLastModifiedDate) {
				this.cphiLastModifiedDate = cphiLastModifiedDate;
			}
			public Long getGroupId() {
				return groupId;
			}
			public void setGroupId(Long groupId) {
				this.groupId = groupId;
			}
			public Date getCreatedDate() {
				return createdDate;
			}
			public void setCreatedDate(Date createdDate) {
				this.createdDate = createdDate;
			}
			public String getCreatedBy() {
				return createdBy;
			}
			public void setCreatedBy(String createdBy) {
				this.createdBy = createdBy;
			}
			public Date getLastModifiedDate() {
				return lastModifiedDate;
			}
			public void setLastModifiedDate(Date lastModifiedDate) {
				this.lastModifiedDate = lastModifiedDate;
			}
			public String getLastModifiedBy() {
				return lastModifiedBy;
			}
			public void setLastModifiedBy(String lastModifiedBy) {
				this.lastModifiedBy = lastModifiedBy;
			}
			
}
