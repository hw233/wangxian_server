package com.sqage.stat.dao;

import java.sql.SQLException;
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

import com.sqage.stat.model.ChannelItem;


/**
 * A data access object (DAO) providing persistence and search support for
 * ChannelItem entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sqage.stat.model.ChannelItem
 * @author MyEclipse Persistence Tools
 */

public class ChannelItemDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(ChannelItemDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String KEY = "key";
	public static final String CHANNELID = "channelid";
	public static final String CHANNELITEMID = "channelitemid";
	
	public static final String tableName = "channelitem";
	public static final Class pojoClass = ChannelItem.class;

	protected void initDao() {
		// do nothing
	}

	public void save(ChannelItem transientInstance) {
		log.debug("saving ChannelItem instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ChannelItem persistentInstance) {
		log.debug("deleting ChannelItem instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ChannelItem findById(java.lang.Long id) {
		log.debug("getting ChannelItem instance with id: " + id);
		try {
			ChannelItem instance = (ChannelItem) getHibernateTemplate().get(
					"com.sqage.stat.model.ChannelItem", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ChannelItem instance) {
		log.debug("finding ChannelItem instance by example");
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
		log.debug("finding ChannelItem instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ChannelItem as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByKey(Object key) {
		return findByProperty(KEY, key);
	}

	public List findByChannelid(Object channelid) {
		return findByProperty(CHANNELID, channelid);
	}

	public List findByChannelitemid(Object channelitemid) {
		return findByProperty(CHANNELITEMID, channelitemid);
	}
	
	public List findAll() {
		log.debug("finding all ChannelItem instances");
		try {
			String queryString = "from ChannelItem";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ChannelItem merge(ChannelItem detachedInstance) {
		log.debug("merging ChannelItem instance");
		try {
			ChannelItem result = (ChannelItem) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ChannelItem instance) {
		log.debug("attaching dirty ChannelItem instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ChannelItem instance) {
		log.debug("attaching clean ChannelItem instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ChannelItemDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ChannelItemDAO) ctx.getBean("ChannelItemDAO");
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
