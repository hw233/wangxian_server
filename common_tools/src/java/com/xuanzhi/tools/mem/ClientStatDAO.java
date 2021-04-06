package com.xuanzhi.tools.mem;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;



/**
 *
 * 
 */
public class ClientStatDAO {
	public static Logger logger = Logger.getLogger(ClientStatDAO.class);

	/**
	 * 数据文件
	 */
	private String dbfile = "";
	
	private EntityManagerFactory emf = null;
    	
	/**
	 * 组件初始化
	 */
	public void init() {
		com.objectdb.jpa.Provider p = new com.objectdb.jpa.Provider();
		this.emf = p.createEntityManagerFactory(dbfile, new HashMap());
	}
	
	/**
	 * 虚拟机关闭时调用，在spring中配置
	 */
	public void destroy() {
		emf.close();
	}
	
	public String getDbfile() {
		return dbfile;
	}

	public void setDbfile(String dbfile) {
		this.dbfile = dbfile;
	}

	public void batchSaveNew(List<ClientStat> plist) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			for(ClientStat p : plist) {
				em.persist(p);
				logger.info("[批量保存统计实体] [persist] ["+p.getLogStr()+"]", start);
			}
			em.getTransaction().commit();
			logger.info("[批量保存统计实体] [数量："+plist.size()+"]", start);
		} catch(javax.persistence.PersistenceException e) {
			logger.warn("[批量保存统计实体失败] [批量保存数量:"+plist.size()+"]", e);
			throw e;
	    } catch(Exception e) {
	    	logger.error(e);
	    } finally {
			em.close();
		}
	}
	
	public void saveOne(ClientStat stat) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(stat);
			logger.debug("[保存统计实体] [persist] ["+stat.getLogStr()+"]", start);
			em.getTransaction().commit();
		} catch(javax.persistence.PersistenceException e) {
			em.getTransaction().rollback();
			try {
				logger.warn("[保存统计实体] [已回滚] ["+stat.getLogStr()+"] ["+start+"]", start);
			} catch(Exception ee) {
				logger.error(e);
			}
	    } catch(Exception e) {
	    	logger.error(e);
	    } finally {
			em.close();
		}
	}

	public void batchUpdate(List<ClientStat> plist) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			for(ClientStat p : plist) {
				try {
					em.merge(p);
					logger.debug("[批量更新统计实体] [merge] ["+p.getLogStr()+"]", start);
				} catch(IllegalArgumentException e) {
					logger.warn("[批量更新统计实体异常]  ["+p.getLogStr()+"]");
				}
			}
			em.getTransaction().commit();
			logger.info("[批量更新统计实体] [数量："+plist.size()+"]", start);
		} catch(javax.persistence.PersistenceException e) {
			logger.warn("[批量更新统计实体异常] [批量保存数量:"+plist.size()+"]", e);
			throw e;
	    } catch(Exception e) {
	    	logger.error(e);
	    } finally {
			em.close();
		}
	}

	public void updateOne(ClientStat stat) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		EntityManager em = emf.createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(stat);
			logger.debug("[更新统计实体] [merge] ["+stat.getLogStr()+"]", start);
			em.getTransaction().commit();
		} catch(javax.persistence.PersistenceException e) {
			em.getTransaction().rollback();
			try {
				logger.warn("[更新统计实体] [已回滚] ["+stat.getLogStr()+"] ["+start+"]", start);
			} catch(Exception ee) {
				logger.error(e);
			}
	    } catch(Exception e) {
	    	logger.error(e);
	    } finally {
			em.close();
		}
	}

	public List<ClientStat> getClientStats(String clientName, long startTime, long endTime) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		EntityManager em = emf.createEntityManager();
		try {
			TypedQuery<ClientStat> query = em.createQuery("select k from ClientStat k where k.clientName=:clientName and " +
					"k.createTime>=:starttime and k.createTime<=:endtime order by k.createTime", ClientStat.class);
			List<ClientStat> result = query.setParameter("clientName", clientName).setParameter("starttime", startTime)
				.setParameter("endtime", endTime).getResultList();
			logger.info("[获得某个客户的所有统计] ["+clientName+"] ["+startTime+"] ["+endTime+"] ["+result.size()+"]", start);
			return result;
		} finally {
			em.close();
		}
	}
}
