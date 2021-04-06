package com.xuanzhi.tools.mem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 集合客户统计
 * 
 */
public class CollectionClientStater implements Runnable {
	
	public static Logger logger = Logger.getLogger(CollectionClientStater.class);
	
	protected ClientStatDAO statDAO;
	
	protected List<CollectionClient> clients = new ArrayList<CollectionClient>();
	
	protected static CollectionClientStater self;
	
	protected List<ClientStat> addQueue = new LinkedList<ClientStat>();
		
	public static CollectionClientStater getInstance() {
		return self;
	}
	
	public void init() {
		long start = System.currentTimeMillis();
		self = this;
		Thread t = new Thread(this, "CollectionClientStater");
		t.start();
		logger.info("[集合客户统计初始化成功] [共注册:"+clients.size()+"]", start);
	}
	
	public void destroy() {
		
	}
	
	public ClientStatDAO getStatDAO() {
		return statDAO;
	}

	public void setStatDAO(ClientStatDAO statDAO) {
		this.statDAO = statDAO;
	}

	/**
	 * 在集合客户统计中注册
	 * @param client
	 */
	public void register(CollectionClient client) {
		if(!clients.contains(client)) {
			clients.add(client);
			logger.info("[注册client] ["+client.getClientName()+"]");
		}
	}

	public List<CollectionClient> getClients() {
		return clients;
	}

	public void setClients(List<CollectionClient> clients) {
		this.clients = clients;
	}
	
	public ClientStat createClientStat(String clientName, String key, long num, long size) {
		ClientStat stat = new ClientStat();
		stat.setClientName(clientName);
		stat.setCollectionkey(key);
		stat.setNum(num);
		stat.setSize(size);
		stat.setCreateTime(System.currentTimeMillis());
		addQueue.add(stat);
		return stat;
	}
	
	/**
	 * 获得某段时间内的所有统计
	 * @param clientName
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public List<ClientStat> getClientStats(String clientName, Date starttime, Date endtime) {
		return statDAO.getClientStats(clientName, starttime.getTime(), endtime.getTime());
	}
	
	/**
	 * 获得某段时间内某个客户的分类统计
	 * @param clientName
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public HashMap<String, List<ClientStat>> getClientStatMap(String clientName, Date starttime, Date endtime) {
		List<ClientStat> list = getClientStats(clientName, starttime, endtime);
		HashMap<String, List<ClientStat>> map = new HashMap<String, List<ClientStat>>();
		for(int i=0,len=list.size(); i<len; i++) {
			String key = list.get(i).getCollectionkey();
			List<ClientStat> slist = map.get(key);
			if(slist == null) {
				slist = new ArrayList<ClientStat>();
				map.put(key, slist);
			}
			slist.add(list.get(i));
		}
		return map;
	}

	public void run() {
		// TODO Auto-generated method stub
		long start = 0;
		long loop = 0;
		while(true) {
			try {
				Thread.sleep(1000);
				if(loop % 20  == 0) {
					start = System.currentTimeMillis();
					if(addQueue.size() > 0) {
						List<ClientStat> list = new ArrayList<ClientStat>();
						for(int i=0, len=addQueue.size(); i<len; i++) {
							list.add(addQueue.remove(0));
						}
						statDAO.batchSaveNew(list);
						logger.info("[集合客户统计新增] ["+list.size()+"]", start);
					}
					logger.info("[集合客户统计新增] [0]", start);
				}
				if(loop % 120 == 0) {
					start = System.currentTimeMillis();
					int add = 0;
					List<CollectionClient> nlist = new ArrayList<CollectionClient>();
					nlist.addAll(clients);
					for(int i=0; i<nlist.size(); i++) {
						try {
							List<ClientStat> stats = nlist.get(i).getClientStats();
							for(int j=0; j<stats.size(); j++) {
								ClientStat stat = stats.get(j);
								this.createClientStat(stat.getClientName(), stat.getCollectionkey(), stat.getNum(), stat.getSize());
								logger.debug("[客户数据] ["+stat.getClientName()+"] ["+stat.getCollectionkey()+"] ["+stat.getNum()+"] ["+stat.getSize()+"]");
								add++;
							}
						} catch(Exception e) {
							logger.error("[统计客户数据时发生异常] ["+nlist.get(i).getClientName()+"]", e);
						}
					}
					logger.info("[统计客户数据] ["+nlist.size()+"] [总计:"+add+"]", start);
				}
				loop++;
			} catch(Throwable e) {
				logger.error("[集合客户统计心跳失败] ["+(System.currentTimeMillis()-start)+"ms]", e);
			}
		}
	}
	
}
