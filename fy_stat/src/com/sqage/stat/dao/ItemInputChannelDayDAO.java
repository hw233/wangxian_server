package com.sqage.stat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import com.sqage.stat.model.ItemInputChannelDay;
import com.xuanzhi.tools.dbpool.DataSourceManager;

/**
 * A data access object (DAO) providing persistence and search support for
 * ItemInputChannelDay entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sqage.stat.model.ItemInputChannelDay
 * @author MyEclipse Persistence Tools
 */

public class ItemInputChannelDayDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory
			.getLog(ItemInputChannelDayDAO.class);
	// property constants
	public static final String INPUTMONTH = "inputmonth";
	public static final String CHANNELID = "channelid";
	public static final String CHANNELITEMID = "channelitemid";
	public static final String AMOUNT = "amount";
	
	public static final String tableName = "item_input_channel_day";
	public static final Class pojoClass = ItemInputChannelDay.class;

	protected void initDao() {
		// do nothing
	}

	public void save(ItemInputChannelDay transientInstance) {
		log.debug("saving ItemInputChannelDay instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ItemInputChannelDay persistentInstance) {
		log.debug("deleting ItemInputChannelDay instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ItemInputChannelDay findById(java.lang.Long id) {
		log.debug("getting ItemInputChannelDay instance with id: " + id);
		try {
			ItemInputChannelDay instance = (ItemInputChannelDay) getHibernateTemplate()
					.get("com.sqage.stat.model.ItemInputChannelDay", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ItemInputChannelDay instance) {
		log.debug("finding ItemInputChannelDay instance by example");
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
		log.debug("finding ItemInputChannelDay instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ItemInputChannelDay as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByInputmonth(Object inputmonth) {
		return findByProperty(INPUTMONTH, inputmonth);
	}

	public List findByChannelid(Object channelid) {
		return findByProperty(CHANNELID, channelid);
	}

	public List findByChannelitemid(Object channelitemid) {
		return findByProperty(CHANNELITEMID, channelitemid);
	}

	public List findByAmount(Object amount) {
		return findByProperty(AMOUNT, amount);
	}

	public List findAll() {
		log.debug("finding all ItemInputChannelDay instances");
		try {
			String queryString = "from ItemInputChannelDay";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ItemInputChannelDay merge(ItemInputChannelDay detachedInstance) {
		log.debug("merging ItemInputChannelDay instance");
		try {
			ItemInputChannelDay result = (ItemInputChannelDay) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ItemInputChannelDay instance) {
		log.debug("attaching dirty ItemInputChannelDay instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ItemInputChannelDay instance) {
		log.debug("attaching clean ItemInputChannelDay instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ItemInputChannelDayDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (ItemInputChannelDayDAO) ctx.getBean("ItemInputChannelDayDAO");
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
	
	public List<ItemInputChannelDay> listChannelInput(long month, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		List<ItemInputChannelDay> list = new ArrayList<ItemInputChannelDay>();
		try {
			con = getConnection();
			String sql = "select id from " + tableName + " where inputmonth=? order by channelid,channelitemid,id";
			if(channelid != -1) {
				sql = "select id from " + tableName + " where inputmonth=? and channelid=? order by channelid,channelitemid,id";
			}
			if(channelitemid != -1) {
				sql = "select id from " + tableName + " where inputmonth=? and channelid=? and channelitemid=? order by channelid,channelitemid,id";
			}
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, month);
			if(channelid != -1) {
				pstmt.setLong(2, channelid);
			}
			if(channelitemid != -1) {
				pstmt.setLong(3, channelitemid);
			}
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				long id = rs.getLong(1);
				ItemInputChannelDay input = this.findById(id);
				if(input != null) {
					list.add(input);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			log.error("getChannelInput failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return list;
	}
	
	public Connection getConnection() throws SQLException {
		return DataSourceManager.getInstance().getConnection();
	}
}
