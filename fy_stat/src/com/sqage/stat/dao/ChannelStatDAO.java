package com.sqage.stat.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sqage.stat.model.ChannelStat;

/**
 * A data access object (DAO) providing persistence and search support for
 * ChannelStat entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sqage.stat.model.ChannelStat
 * @author MyEclipse Persistence Tools
 */

public class ChannelStatDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(ChannelStatDAO.class);
	// property constants
	public static final String CHANNELID = "channelid";
	public static final String REGNUM = "regnum";
	public static final String PRATE = "prate";
	
	public static final String tableName = "channelstat";
	public static final Class pojoClass = ChannelStat.class;

	protected void initDao() {
		// do nothing
	}

	public void save(ChannelStat transientInstance) {
		log.debug("saving ChannelStat instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ChannelStat persistentInstance) {
		log.debug("deleting ChannelStat instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ChannelStat findById(java.lang.Long id) {
		log.debug("getting ChannelStat instance with id: " + id);
		try {
			ChannelStat instance = (ChannelStat) getHibernateTemplate().get(
					"com.sqage.stat.model.ChannelStat", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ChannelStat instance) {
		log.debug("finding ChannelStat instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding ChannelStat instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ChannelStat as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByChannelid(Object channelid) {
		return findByProperty(CHANNELID, channelid);
	}

	public List findByChannelAndData(long channelid, Date date) {
		log.debug("findByChannelAndData instances");
		try {
			String queryString = "from ChannelStat where channelid=? and statdate=?";
			return getHibernateTemplate().find(queryString, new Object[]{channelid, date});
		} catch (RuntimeException re) {
			log.error("findByChannelAndData failed", re);
			throw re;
		}
	}

	public List findByChannelAndData(long channelid, long channelitemid, Date date) {
		log.debug("findByChannelAndData instances");
		try {
			String queryString = "from ChannelStat where channelid=? and channelitemid=? and statdate=?";
			return getHibernateTemplate().find(queryString, new Object[]{channelid, channelitemid, date});
		} catch (RuntimeException re) {
			log.error("findByChannelAndData failed", re);
			throw re;
		}
	}

	public List findByRegnum(Object regnum) {
		return findByProperty(REGNUM, regnum);
	}

	public List findByPrate(Object prate) {
		return findByProperty(PRATE, prate);
	}

	public List findAll() {
		log.debug("finding all ChannelStat instances");
		try {
			String queryString = "from ChannelStat";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ChannelStat merge(ChannelStat detachedInstance) {
		log.debug("merging ChannelStat instance");
		try {
			ChannelStat result = (ChannelStat) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ChannelStat instance) {
		log.debug("attaching dirty ChannelStat instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ChannelStat instance) {
		log.debug("attaching clean ChannelStat instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ChannelStatDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ChannelStatDAO) ctx.getBean("ChannelStatDAO");
	}
	
	public int getRowNum() {
		try {
			String queryString = "select count(*) from " + pojoClass.getName();
			List result =  getHibernateTemplate().find(queryString);
			if(result.size()>0) {
				Integer count = (Integer)result.get(0);
				return count;
			} else {
				return 0;
			}
		} catch (RuntimeException re) {
			log.error("find by keyword failed", re);
			throw re;
		}
	}
	
	public List findPageRows(final int start, final int length) {
		List result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session s)
							throws HibernateException, SQLException {
						String q = "select * from " + tableName;
						SQLQuery query = s.createSQLQuery(q);
						query.addEntity(pojoClass);
						query.setFirstResult(start);
						query.setMaxResults(length);
						List list = query.list();
						return list;
					}
				});
		return result;
	}
}
