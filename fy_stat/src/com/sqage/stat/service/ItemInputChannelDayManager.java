package com.sqage.stat.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.dao.ItemInputChannelDayDAO;
import com.sqage.stat.model.ItemInputChannelDay;

public class ItemInputChannelDayManager {

	protected static ItemInputChannelDayManager m_self = null;
    
	protected static final Log logger = LogFactory.getLog(ItemInputChannelDayManager.class);
	    
    protected ItemInputChannelDayDAO itemInputChannelDayDAO;
        
    public static ItemInputChannelDayManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
		m_self = this;
		System.out.println("["+ItemInputChannelDayManager.class.getName()+"] [initialized]");
		logger.info("["+ItemInputChannelDayManager.class.getName()+"] [initialized]");
	}

	public ItemInputChannelDay createItemInputChannelDay(ItemInputChannelDay ItemInputChannelDay) {
		itemInputChannelDayDAO.save(ItemInputChannelDay);
		return ItemInputChannelDay;
	}

	public ItemInputChannelDay getItemInputChannelDay(long id) {
		ItemInputChannelDay ItemInputChannelDay = itemInputChannelDayDAO.findById(id);
		return ItemInputChannelDay;
	}
	
	public int getCount() {
		return itemInputChannelDayDAO.getRowNum();
	}
	
	public List getItemInputChannelDays() {
		return itemInputChannelDayDAO.findAll();
	}
	
	public List getItemInputChannelDays(int start, int length) {
		return itemInputChannelDayDAO.findPageRows(start, length);
	}
	
	public void updateItemInputChannelDay(ItemInputChannelDay d) {
		itemInputChannelDayDAO.attachDirty(d);
	}
	
	public void deleteItemInputChannelDay(long id) {
		ItemInputChannelDay d = getItemInputChannelDay(id);
		if(d != null) {
			itemInputChannelDayDAO.delete(getItemInputChannelDay(id));
		}
	}
	
	/**
	 * 获得某月内，相关渠道的所有投入
	 * @param month yyyyMM
	 * @param channelid
	 * @param channelitemid
	 * @return
	 */
	public List<ItemInputChannelDay> getChannelInputs(long month, long channelid, long channelitemid) {
		return itemInputChannelDayDAO.listChannelInput(month, channelid, channelitemid);
	}
	
	/**
	 * 获得某月内，相关渠道的所有投入
	 * @param month yyyyMM
	 * @param channelid
	 * @return
	 */
	public List<ItemInputChannelDay> getChannelInputs(long month, long channelid) {
		return itemInputChannelDayDAO.listChannelInput(month, channelid, -1);
	}
	
	/**
	 * 获得某月内，相关渠道的所有投入
	 * @param month yyyyMM
	 * @return
	 */
	public List<ItemInputChannelDay> getChannelInputs(long month) {
		return itemInputChannelDayDAO.listChannelInput(month, -1, -1);
	}

	public ItemInputChannelDayDAO getItemInputChannelDayDAO() {
		return itemInputChannelDayDAO;
	}

	public void setItemInputChannelDayDAO(ItemInputChannelDayDAO itemInputChannelDayDAO) {
		this.itemInputChannelDayDAO = itemInputChannelDayDAO;
	}

}
