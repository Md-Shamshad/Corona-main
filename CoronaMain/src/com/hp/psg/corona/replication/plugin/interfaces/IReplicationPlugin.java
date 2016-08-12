package com.hp.psg.corona.replication.plugin.interfaces;

import java.util.List;

import com.hp.psg.corona.replication.beans.PriceCacheDataBean;


/**
 * @author dudeja
 * @version 1.0
 * 
 */
public interface IReplicationPlugin  {
	public ReplicationResult replicate(List<PriceCacheDataBean> dataBean);
}
