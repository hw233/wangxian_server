package com.sqage.stat.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.dao.ChannelDAO;
import com.sqage.stat.model.Channel;
import com.xuanzhi.tools.cache.LruMapCache;

public class ChannelManager {

	protected static ChannelManager m_self = null;
    
	protected static final Log log = LogFactory.getLog(ChannelManager.class);
	
	protected static LruMapCache mCache;
    
    protected ChannelDAO channelDAO;
        
    public static ChannelManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
    	mCache = new LruMapCache();
		m_self = this;
		System.out.println("["+ChannelManager.class.getName()+"] [initialized]");
		log.info("["+ChannelManager.class.getName()+"] [initialized]");
	}
	
	public Channel createChannel(Channel channel) {
		if(getChannel(channel.getName()) != null) {
			return null;
		}
		channelDAO.save(channel);
		mCache.__put(channel.getId(), channel);
		mCache.__put(channel.getName(), channel);
		mCache.__put(channel.getKey(), channel);
		return channel;
	}

	public Channel getChannel(long id) {
		if(mCache.__get(id) != null) {
			return (Channel)mCache.__get(id);
		}
		Channel channel = channelDAO.findById(id);
		if(channel != null) {
			mCache.__put(channel.getId(), channel);
			mCache.__put(channel.getName(), channel);
			mCache.__put(channel.getKey(), channel);
		} else {
		}
		return channel;
	}

	public Channel getChannel(String key) {
		if(mCache.__get(key) != null) {
			return (Channel)mCache.__get(key);
		}
		List list = channelDAO.findByKey(key);
		if(list.size() > 0) {
			Channel channel = (Channel)list.get(0);
			mCache.__put(channel.getId(), channel);
			mCache.__put(channel.getKey(), channel);
			mCache.__put(channel.getName(), channel);
			return channel;
		}
		return null;
	}
	
	public int getCount() {
		return channelDAO.getRowNum();
	}
	
	public List getChannels() {
		return channelDAO.findAll();
	}
	
	public List getChannels(int start, int length) {
		return channelDAO.findPageRows(start, length);
	}
	
	public void updateChannel(Channel d) {
		channelDAO.attachDirty(d);
		mCache.remove(d.getId());
		mCache.remove(d.getName());
		mCache.remove(d.getKey());
	}
	
	public void deleteChannel(long id) {
		Channel d = getChannel(id);
		if(d != null) {
			channelDAO.delete(getChannel(id));
			mCache.remove(d.getId());
			mCache.remove(d.getName());
			mCache.remove(d.getKey());
		}
	}

	public ChannelDAO getChannelDAO() {
		return channelDAO;
	}

	public void setChannelDAO(ChannelDAO channelDAO) {
		this.channelDAO = channelDAO;
	}

}
