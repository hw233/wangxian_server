package com.sqage.stat.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sqage.stat.dao.ChannelStatDAO;
import com.sqage.stat.model.Channel;
import com.sqage.stat.model.ChannelItem;
import com.sqage.stat.model.ChannelStat;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.timer.Executable;

public class ChannelStatManager implements Executable {

	protected static ChannelStatManager m_self = null;
    
	protected static final Log log = LogFactory.getLog(ChannelStatManager.class);
	    
    protected ChannelStatDAO channelStatDAO;
        
    public static ChannelStatManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
		m_self = this;
		System.out.println("["+ChannelStatManager.class.getName()+"] [initialized]");
		log.info("["+ChannelStatManager.class.getName()+"] [initialized]");
	}
	
	public ChannelStat createChannelStat(ChannelStat ChannelStat) {
		if(getChannelStat(ChannelStat.getChannelid(), ChannelStat.getChannelitemid(), ChannelStat.getStatdate()) != null) {
			return getChannelStat(ChannelStat.getChannelid(), ChannelStat.getChannelitemid(), ChannelStat.getStatdate());
		}
		channelStatDAO.save(ChannelStat);
		return ChannelStat;
	}

	public ChannelStat getChannelStat(long id) {
		ChannelStat ChannelStat = channelStatDAO.findById(id);
		return ChannelStat;
	}

	public List<ChannelStat> getChannelStat(long channelid, Date date) {
		List list = channelStatDAO.findByChannelAndData(channelid, date);
		return list;
	}

	public ChannelStat getChannelStat(long channelid, long channelitemid, Date date) {
		List list = channelStatDAO.findByChannelAndData(channelid, channelitemid, date);
		if(list.size() > 0) {
			ChannelStat ChannelStat = (ChannelStat)list.get(0);
			return ChannelStat;
		}
		return null;
	}
	
	public int getCount() {
		return channelStatDAO.getRowNum();
	}
	
	public List getChannelStats() {
		return channelStatDAO.findAll();
	}
	
	public List getChannelStats(int start, int length) {
		return channelStatDAO.findPageRows(start, length);
	}
	
	public void updateChannelStat(ChannelStat d) {
		channelStatDAO.attachDirty(d);
	}
	
	public void deleteChannelStat(long id) {
		ChannelStat d = getChannelStat(id);
		if(d != null) {
			channelStatDAO.delete(getChannelStat(id));
		}
	}

	public ChannelStatDAO getChannelStatDAO() {
		return channelStatDAO;
	}

	public void setChannelStatDAO(ChannelStatDAO channelStatDAO) {
		this.channelStatDAO = channelStatDAO;
	}

	public void execute(String[] args) {
		// TODO Auto-generated method stub
		if(args.length == 0 || args[0].trim().length() == 0) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, -1);
			genChannelStat(cal.getTime());
		} else if(args.length == 1) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(args[0]));
			genChannelStat(cal.getTime());
		} else if(args.length == 2) {
			String startdate = args[0];
			String enddate = args[1];
			Calendar cal = Calendar.getInstance();
			Date starttime = DateUtil.parseDate(startdate, "yyyy-MM-dd");
			cal.setTime(starttime);
			while(!DateUtil.formatDate(cal.getTime(), "yyyy-MM-dd").equals(enddate)) {
				genChannelStat(cal.getTime());
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			genChannelStat(cal.getTime());
		}
	}
	
	public void genChannelStat(Date statdate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(statdate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date starttime = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date endtime = cal.getTime();
		ChannelManager cmanager = ChannelManager.getInstance();
		ChannelItemManager cimanager = ChannelItemManager.getInstance();
		ChannelStatManager csmanager = ChannelStatManager.getInstance();
		UserStatManager usmanager = UserStatManager.getInstance();
		SavingYuanbaoManager symanager = SavingYuanbaoManager.getInstance();
		List<Channel> channels = cmanager.getChannels();
		statdate = starttime;
		for(Channel channel : channels) {
			List<ChannelItem> items = cimanager.getChannelItemsByChannel(channel);
			for(ChannelItem item : items) {
				int num = usmanager.getChannelUniqMobileNormalUserNum(channel.getId(), item.getId(), starttime, endtime, 1);
				long amount = symanager.getSavingRmbPureAmount(starttime, endtime, channel.getId(), item.getId());
				Float prate = item.getPrate();
				if(prate  == null) {
					prate = 1f;
				}
				int pnum = new Float(prate*num).intValue();
				int pamount = new Float(prate*amount).intValue();
				
				ChannelStat stat = new ChannelStat();
				stat.setChannelid(channel.getId());
				stat.setChannelitemid(item.getId());
				stat.setPrate(prate);
				stat.setRegnum((long)num);
				stat.setStatdate(statdate);
				stat.setIncome((long)amount);
				csmanager.createChannelStat(stat);
				
				log.debug("[创建渠道统计] [日期:"+DateUtil.formatDate(statdate, "yyyy-MM-dd")+"] " +
						"[start:"+DateUtil.formatDate(starttime, "yyyyMMdd HH:mm:ss")+"] [end:"+DateUtil.formatDate(endtime, "yyyyMMdd HH:mm:ss")+"] " +
								"[渠道:"+channel.getName()+"] [子渠道:"+item.getName()+"] [新独立手机用户:"+num+"] [处理后:"+pnum+"] [新充值钱数:"+amount+"] [处理后:"+pamount+"] [statid:"+stat.getId()+"]");
			}
		}
	}

}
