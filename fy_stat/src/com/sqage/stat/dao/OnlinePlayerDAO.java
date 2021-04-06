package com.sqage.stat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sqage.stat.model.OnlinePlayer;
import com.sqage.stat.model.PlayerExpense;
import com.xuanzhi.tools.dbpool.DataSourceManager;
import com.xuanzhi.tools.text.NumberUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * OnlinePlayer entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sqage.stat.model.OnlinePlayer
 * @author MyEclipse Persistence Tools
 */

public class OnlinePlayerDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(OnlinePlayerDAO.class);
	// property constants
	public static final String ONLINENUM = "onlinenum";
	public static final String SERVERID = "serverid";
	
	public static final String tableName = "playerexpense";
	public static final Class pojoClass = PlayerExpense.class;

	protected void initDao() {
		// do nothing
	}

	public void save(OnlinePlayer transientInstance) {
		log.debug("saving OnlinePlayer instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(OnlinePlayer persistentInstance) {
		log.debug("deleting OnlinePlayer instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public OnlinePlayer findById(java.lang.Long id) {
		log.debug("getting OnlinePlayer instance with id: " + id);
		try {
			OnlinePlayer instance = (OnlinePlayer) getHibernateTemplate().get(
					"com.sqage.stat.model.OnlinePlayer", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(OnlinePlayer instance) {
		log.debug("finding OnlinePlayer instance by example");
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
		log.debug("finding OnlinePlayer instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from OnlinePlayer as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByOnlinenum(Object onlinenum) {
		return findByProperty(ONLINENUM, onlinenum);
	}

	public List findByServerid(Object serverid) {
		return findByProperty(SERVERID, serverid);
	}

	public List findAll() {
		log.debug("finding all OnlinePlayer instances");
		try {
			String queryString = "from OnlinePlayer";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public OnlinePlayer merge(OnlinePlayer detachedInstance) {
		log.debug("merging OnlinePlayer instance");
		try {
			OnlinePlayer result = (OnlinePlayer) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OnlinePlayer instance) {
		log.debug("attaching dirty OnlinePlayer instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OnlinePlayer instance) {
		log.debug("attaching clean OnlinePlayer instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static OnlinePlayerDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (OnlinePlayerDAO) ctx.getBean("OnlinePlayerDAO");
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
	
	public int getMaxOnlineNum() {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select max(num) from (select stattime, sum(onlinenum) as num from onlineplayer group by stattime)";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				int num = rs.getInt(1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getMaxOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getMaxOnlineNum(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select max(num) from (select stattime, sum(onlinenum) as num from onlineplayer " +
					"where stattime between ? and ? group by stattime)";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				int num = rs.getInt(1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getMaxOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getMaxOnlineNum(long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select max(num) from (select stattime, sum(onlinenum) as num from onlineplayer where serverid=? group by stattime)";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, serverid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				int num = rs.getInt(1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getMaxOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getMaxOnlineNum(Date starttime, Date endtime, long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select max(num) from (select stattime, sum(onlinenum) as num from onlineplayer " +
					"where stattime between ? and ? and serverid=? group by stattime)";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				int num = rs.getInt(1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getMaxOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getAvgOnlineNum() {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(num) from (select stattime, sum(onlinenum) as num from onlineplayer group by stattime)";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				float num = rs.getFloat(1);
				num = NumberUtils.round(num, 1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getMaxOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getChannelAvgOnlineNum(long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(num) from (select stattime, sum(onlinenum) as num from onlineplayer where channelid=? and channelitemid=? group by stattime)";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, channelid);
			pstmt.setLong(2, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				float num = rs.getFloat(1);
				num = NumberUtils.round(num, 1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getChannelAvgOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getChannelAvgOnlineNum(long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(num) from (select stattime, sum(onlinenum) as num from onlineplayer where channelid=? group by stattime)";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, channelid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				float num = rs.getFloat(1);
				num = NumberUtils.round(num, 1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getChannelAvgOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getAvgOnlineNum(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(num) from (select stattime, sum(onlinenum) as num from onlineplayer " +
					"where stattime between ? and ? group by stattime)";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				float num = rs.getFloat(1);
				num = NumberUtils.round(num, 1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getMaxOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getChannelAvgOnlineNum(Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(num) from (select stattime, sum(onlinenum) as num from onlineplayer " +
					"where stattime between ? and ? and channelid=? group by stattime)";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				float num = rs.getFloat(1);
				num = NumberUtils.round(num, 1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getChannelAvgOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getChannelAvgOnlineNum(Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(num) from (select stattime, sum(onlinenum) as num from onlineplayer " +
					"where stattime between ? and ? and channelid=? and channelitemid=? group by stattime)";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				float num = rs.getFloat(1);
				num = NumberUtils.round(num, 1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getChannelAvgOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getAvgOnlineNum(long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(num) from (select stattime, sum(onlinenum) as num from onlineplayer where serverid=? group by stattime)";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, serverid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				float num = rs.getFloat(1);
				num = NumberUtils.round(num, 1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getMaxOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getAvgOnlineNum(Date starttime, Date endtime, long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(num) from (select stattime, sum(onlinenum) as num from onlineplayer " +
					"where stattime between ? and ? and serverid=? group by stattime)";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				float num = rs.getFloat(1);
				num = NumberUtils.round(num, 1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getMaxOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getAvgOnlineNum(int hourFrom, int hourTo) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			StringBuffer sb = new StringBuffer();
			for(int i=hourFrom; i<=hourTo; i++) {
				String s = String.valueOf(i);
				if(s.length() == 1) {
					s = "0" + s;
				}
				sb.append("'" + s + "',");
			}
			String hours = sb.toString();
			if(hours.length() > 1) {
				hours.substring(0, hours.length()-1);
			}
			con = getConnection();
			String sql = "select avg(num) from (select stattime, sum(onlinenum) as num " +
					"from onlineplayer where to_char(stattime,'HH24') in ("+hours+") group by stattime)";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				float num = rs.getFloat(1);
				num = NumberUtils.round(num, 1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getMaxOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getAvgOnlineNum(long serverid, int hourFrom, int hourTo) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			StringBuffer sb = new StringBuffer();
			for(int i=hourFrom; i<=hourTo; i++) {
				String s = String.valueOf(i);
				if(s.length() == 1) {
					s = "0" + s;
				}
				sb.append("'" + s + "',");
			}
			String hours = sb.toString();
			if(hours.length() > 1) {
				hours.substring(0, hours.length()-1);
			}
			con = getConnection();
			String sql = "select avg(num) from (select stattime, sum(onlinenum) as num " +
					"from onlineplayer where serverid=? and to_char(stattime,'HH24') in ("+hours+") group by stattime)";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, serverid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				float num = rs.getFloat(1);
				num = NumberUtils.round(num, 1);
				return num;
			}
		} catch (Throwable e) {
			log.error("getMaxOnlineNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
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
	
	public void batchAdd(final List<OnlinePlayer> pslist) {
		getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session s)
							throws HibernateException, SQLException {
						Transaction tx = s.beginTransaction();
						for(int i=0; i<pslist.size(); i++) {
							s.save(pslist.get(i));
							if((i+1) % 500 == 0) {
								s.flush();
							    s.clear(); 
							}
						}
						tx.commit();
						s.close(); 
						return null; 
					}
				});
	}
	
	public Connection getConnection() throws SQLException {
		return DataSourceManager.getInstance().getConnection();
	}
}
