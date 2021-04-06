package com.sqage.stat.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.dao.ChannelItemDAO;
import com.sqage.stat.model.Channel;
import com.sqage.stat.model.ChannelItem;
import com.xuanzhi.tools.cache.LruMapCache;

public class ChannelItemManager {

	protected static ChannelItemManager m_self = null;
    
	protected static final Log log = LogFactory.getLog(ChannelItemManager.class);
	
	protected static LruMapCache mCache;
    
    protected ChannelItemDAO channelItemDAO;
        
    public static ChannelItemManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
    	mCache = new LruMapCache();
		m_self = this;
		System.out.println("["+ChannelItemManager.class.getName()+"] [initialized]");
		log.info("["+ChannelItemManager.class.getName()+"] [initialized]");
	}
	
	public ChannelItem createChannelItem(ChannelItem channelItem) {
		if(getChannelItem(channelItem.getName()) != null) {
			return null;
		}
		channelItemDAO.save(channelItem);
		mCache.__put(channelItem.getId(), channelItem);
		mCache.__put(channelItem.getName(), channelItem);
		mCache.__put(channelItem.getKey(), channelItem);
		return channelItem;
	}

	public ChannelItem getChannelItem(long id) {
		if(mCache.__get(id) != null) {
			return (ChannelItem)mCache.__get(id);
		}
		ChannelItem channelItem = channelItemDAO.findById(id);
		if(channelItem != null) {
			mCache.__put(channelItem.getId(), channelItem);
			mCache.__put(channelItem.getName(), channelItem);
			mCache.__put(channelItem.getKey(), channelItem);
		} else {
		}
		return channelItem;
	}

	public ChannelItem getChannelItem(String key) {
		if(mCache.__get(key) != null) {
			return (ChannelItem)mCache.__get(key);
		}
		List list = channelItemDAO.findByKey(key);
		if(list.size() > 0) {
			ChannelItem channelItem = (ChannelItem)list.get(0);
			mCache.__put(channelItem.getId(), channelItem);
			mCache.__put(channelItem.getKey(), channelItem);
			mCache.__put(channelItem.getName(), channelItem);
			return channelItem;
		}
		return null;
	}
	
	public int getCount() {
		return channelItemDAO.getRowNum();
	}
	
	public List getChannelItems() {
		return channelItemDAO.findAll();
	}
	
	public List getChannelItemsByChannel(Channel channel) {
		return channelItemDAO.findByChannelid(channel.getId());
	}
	
	public List getChannelItems(int start, int length) {
		return channelItemDAO.findPageRows(start, length);
	}
	
	public void updateChannelItem(ChannelItem d) {
		channelItemDAO.attachDirty(d);
		mCache.remove(d.getId());
		mCache.remove(d.getName());
		mCache.remove(d.getKey());
	}
	
	public void deleteChannelItem(long id) {
		ChannelItem d = getChannelItem(id);
		if(d != null) {
			channelItemDAO.delete(getChannelItem(id));
			mCache.remove(d.getId());
			mCache.remove(d.getName());
			mCache.remove(d.getKey());
		}
	}

	public ChannelItemDAO getChannelItemDAO() {
		return channelItemDAO;
	}

	public void setChannelItemDAO(ChannelItemDAO channelItemDAO) {
		this.channelItemDAO = channelItemDAO;
	}

}
