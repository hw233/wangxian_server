package com.sqage.stat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

import com.sqage.stat.model.UserStat;
import com.xuanzhi.stat.service.Area;
import com.xuanzhi.tools.dbpool.DataSourceManager;

/**
 * A data access object (DAO) providing persistence and search support for
 * UserStat entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.sqage.stat.model.UserStat
 * @author MyEclipse Persistence Tools
 */

public class UserStatDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(UserStatDAO.class);
	// property constants
	public static final String USERNAME = "username";
	public static final String REGTYPE = "regtype";
	public static final String WHITEUSER = "whiteuser";
	public static final String NOWLEVEL = "nowlevel";
	public static final String CHANNELNAME = "channelname";
	public static final String CHANNELITEMNAME = "channelitemname";
	
	public static final String tableName = "userstat";
	public static final Class pojoClass = UserStat.class;

	protected void initDao() {
		// do nothing
	}

	public boolean save(UserStat transientInstance) {
		log.debug("saving UserStat instance");
		boolean result=false;
		try {
			getHibernateTemplate().save(transientInstance);
			result=true;
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
		return result;
	}

	public void delete(UserStat persistentInstance) {
		log.debug("deleting UserStat instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UserStat findById(java.lang.Long id) {
		log.debug("getting UserStat instance with id: " + id);
		try {
			UserStat instance = (UserStat) getHibernateTemplate().get(
					"com.sqage.stat.model.UserStat", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(UserStat instance) {
		log.debug("finding UserStat instance by example");
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
		log.debug("finding UserStat instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from UserStat as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByUsername(Object username) {
		return findByProperty(USERNAME, username);
	}

	public List findByRegtype(Object regtype) {
		return findByProperty(REGTYPE, regtype);
	}

	public List findByWhiteuser(Object whiteuser) {
		return findByProperty(WHITEUSER, whiteuser);
	}

	public List findByNowlevel(Object nowlevel) {
		return findByProperty(NOWLEVEL, nowlevel);
	}

	public List findByChannelname(Object channelname) {
		return findByProperty(CHANNELNAME, channelname);
	}

	public List findByChannelitemname(Object channelitemname) {
		return findByProperty(CHANNELITEMNAME, channelitemname);
	}

	public List findAll() {
		log.debug("finding all UserStat instances");
		try {
			String queryString = "from UserStat";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public List findNewUserStat(Date starttime, Date endtime) {
		log.debug("finding all UserStat instances");
		try {
			String queryString = "from UserStat where regtime between ? and ?";
			return getHibernateTemplate().find(queryString, new Object[]{starttime, endtime});
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public UserStat merge(UserStat detachedInstance) {
		log.debug("merging UserStat instance");
		try {
			UserStat result = (UserStat) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(UserStat instance) {
		log.debug("attaching dirty UserStat instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UserStat instance) {
		log.debug("attaching clean UserStat instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static UserStatDAO getFromApplicationContext(ApplicationContext ctx) {
		return (UserStatDAO) ctx.getBean("UserStatDAO");
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
	
	public int getRowNum(Date start, Date end) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where regtime between ? and ?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{start, end});
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
	
	public int getWhiteUserMobileRowNum(Date start, Date end) {		
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(mobile)) from " + tableName + " where regtime between ? and ? and whiteuser=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(start.getTime()));
			pstmt.setTimestamp(2, new Timestamp(end.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getMobileRowNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getRowNum(Date start, Date end, long channelid) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where regtime between ? and ? and channel=?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{start, end, channelid});
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
	
	public int getRowNum(Date start, Date end, long channelid, long channelitemid) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where regtime between ? and ? and channel=? and channelitem=?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{start, end, channelid, channelitemid});
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
	
	public int getWhiteUserRowNum(Date start, Date end) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where whiteuser=1 and regtime between ? and ?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{start, end});
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
	
	public int getWhiteUserRowNum(Date start, Date end, long channelid) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where whiteuser=1 and regtime between ? and ? and channel=? and channelitem=?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{start, end, channelid});
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
	
	public int getWhiteUserRowNum(Date start, Date end, long channelid, long channelitemid) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where whiteuser=1 and regtime between ? and ? and channel=? and channelitem=?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{start, end, channelid, channelitemid});
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
	
	public int getRowNum(int type, Date start, Date end) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where regtype=? and regtime between ? and ?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{type, start, end});
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
	
	public int getRowNum(int type, Date start, Date end, long channelid) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where regtype=?  and regtime between ? and ? and channel=? and channelitem=?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{type, start, end, channelid});
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
	
	public int getRowNum(int type, Date start, Date end, long channelid, long channelitemid) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where regtype=? and regtime between ? and ? and channel=? and channelitem=?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{start, end, channelid, channelitemid});
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
	
	public int findNologinNewUserNum(Date regstart, Date regend, Date lastlogintime) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where regtime between ? and ? and (lastlogintime is null or lastlogintime<?)";
			List result =  getHibernateTemplate().find(queryString, new Object[]{regstart, regend, lastlogintime});
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
	
	public int getNologinActiveOldUserNum(Date regtime, Date lastloginstart, Date lastloginend) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where lastlogintime between ? and ? and regtime<?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{lastloginstart, lastloginend, regtime});
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
	
	public int getNologinRowNum(Date statdate, int day) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(statdate);
			cal.add(Calendar.DAY_OF_YEAR, 0-day);
			Date time = cal.getTime();
			String queryString = "select count(*) from " + pojoClass.getName() + " where lastlogintime<?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{time});
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
	
	public int getNologinNewUserRowNum(Date statdate, int day) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(statdate);
			cal.add(Calendar.DAY_OF_YEAR, 0-day);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date starttime = cal.getTime();
			cal.add(Calendar.DAY_OF_YEAR, 1);
			Date endtime = cal.getTime();
			String queryString = "select count(*) from " + pojoClass.getName() + " where lastlogintime<? and regtime between ? and ?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{endtime, starttime, endtime});
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
	
	public int getActiveOldUserNum(Date loginstarttime, Date loginendtime, Date befregtime) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where lastlogintime between ? and ? and regtime<?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{loginstarttime, loginendtime, befregtime});
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
	
	public int getLoginUserNum(Date loginstarttime, Date loginendtime) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where lastlogintime between ? and ?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{loginstarttime, loginendtime});
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
	
	public int getChannelLoginUserNum(Date loginstarttime, Date loginendtime, long channelid) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where lastlogintime between ? and ? and channel=?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{loginstarttime, loginendtime, channelid});
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
	
	public int getChannelLoginUserNum(Date loginstarttime, Date loginendtime, long channelid, long channelitemid) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where lastlogintime between ? and ? and channel=? and channelitem=?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{loginstarttime, loginendtime, channelid, channelitemid});
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
	
	public int getUserNum() {
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
	
	public int getChannelUserNum(long channelid, long channelitemid) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where channel=? and channelitem=?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{channelid, channelitemid});
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
	
	public int getChannelUserNum(long channelid) {
		try {
			String queryString = "select count(*) from " + pojoClass.getName() + " where channel=?";
			List result =  getHibernateTemplate().find(queryString, new Object[]{channelid});
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
	
	public long getSavingYuanbaoTotal() {
		try {
			String queryString = "select sum(savingamount) from " + pojoClass.getName();
			List result =  getHibernateTemplate().find(queryString);
			if(result.size()>0) {
				Long count = (Long)result.get(0);
				return count;
			} else {
				return 0;
			}
		} catch (RuntimeException re) {
			log.error("find by getSavingTotal failed", re);
			throw re;
		}
	}
	
	public long getSavingRmbTotal() {
		try {
			String queryString = "select sum(savingrmb) from " + pojoClass.getName();
			List result =  getHibernateTemplate().find(queryString);
			if(result.size()>0) {
				Long count = (Long)result.get(0);
				return count;
			} else {
				return 0;
			}
		} catch (RuntimeException re) {
			log.error("find by getSavingTotal failed", re);
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
	
	public List findNologinNewUsers(final Date regstart, final Date regend , final Date lastlogintime) {
		List result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session s)
							throws HibernateException, SQLException {
						String q = "select * from " + tableName + " where regtime between ? and ? and lastlogintime<?";
						SQLQuery query = s.createSQLQuery(q);
						query.addEntity(pojoClass);
						query.setTimestamp(0, regstart);
						query.setTimestamp(1, regend);
						query.setTimestamp(2, lastlogintime);
						List list = query.list();
						return list;
					}
				});
		return result;
	}
	
	public HashMap<Long, Integer> statRegisterArea(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<Long, Integer> cmap = new HashMap<Long, Integer>();
		try {
			con = getConnection();
			String sql = "select province, count(*) from userstat where regtime between ? and ? group by province";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Long province = rs.getLong(1);
				int count = rs.getInt(2);
				cmap.put(province, count);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("statRegisterArea failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return cmap;
	}
	
	public HashMap<Long, Integer> statRegisterArea(Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<Long, Integer> cmap = new HashMap<Long, Integer>();
		try {
			con = getConnection();
			String sql = "select province, count(*) from userstat where regtime between ? and ? and channel=? group by province";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Long province = rs.getLong(1);
				int count = rs.getInt(2);
				cmap.put(province, count);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("statRegisterArea failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return cmap;
	}
	
	public HashMap<Long, Integer> statRegisterArea(Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<Long, Integer> cmap = new HashMap<Long, Integer>();
		try {
			con = getConnection();
			String sql = "select province, count(*) from userstat where regtime between ? and ? and channel=? and channelitem=? group by province";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Long province = rs.getLong(1);
				int count = rs.getInt(2);
				cmap.put(province, count);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("statRegisterArea failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return cmap;
	}
	
	public HashMap<Long, Integer> statAllUserArea() {
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<Long, Integer> cmap = new HashMap<Long, Integer>();
		try {
			con = getConnection();
			String sql = "select province, count(*) from userstat where group by province";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Long province = rs.getLong(1);
				int count = rs.getInt(2);
				cmap.put(province, count);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("statRegisterArea failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return cmap;
	}
	
	public HashMap<String, Integer> statActiveUserMobiletype(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<String, Integer> cmap = new HashMap<String, Integer>();
		try {
			con = getConnection();
			String sql = "select * from (select mobiletype, count(*)  as acount from userstat where " +
					"lastlogintime between ? and ? group by mobiletype order by acount desc) where rownum<200 ";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				String mt = rs.getString(1);
				if(mt == null || mt.length() == 0) {
					mt = "未知";
				}
				int count = rs.getInt(2);
				cmap.put(mt, count);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("statActiveUserArea failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return cmap;
	}
	
	public HashMap<Area, Integer> statActiveUserArea(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<Area, Integer> cmap = new HashMap<Area, Integer>();
		try {
			con = getConnection();
			String sql = "select province,city, count(*) from userstat where lastlogintime between ? and ? group by province,city";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Long province = rs.getLong(1);
				Long city=rs.getLong(2);
				int count = rs.getInt(3);
				cmap.put(new Area(province,city), count);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("statActiveUserArea failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return cmap;
	}
	
	public HashMap<Integer, Long> getNewUserMaxLevelPlayerLevelMap(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<Integer, Long> cmap = new HashMap<Integer, Long>();
		try {
			con = getConnection();
			String sql = "select nowlevel,count(*) from userstat where regtime between ? and ? and firstlogintime is not null group by nowlevel";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				cmap.put(rs.getInt(1), rs.getLong(2));
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getNewUserMaxLevelPlayerLevelMap failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return cmap;
	}
	
	public HashMap<Integer, Long> getLoginUserLevelMap(Date statdate) {
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<Integer, Long> cmap = new HashMap<Integer, Long>();
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(statdate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			Date start = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			Date end = cal.getTime();
			con = getConnection();
			String sql = "select nowlevel,count(*) from userstat where lastlogintime between ? and ? group by nowlevel";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(start.getTime()));
			pstmt.setTimestamp(2, new Timestamp(end.getTime()));
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				cmap.put(rs.getInt(1), rs.getLong(2));
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getLoginUserLevelMap failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return cmap;
	}
	
	public int getChannelUniqMobileUserNum(long channelid, Date starttime, Date endtime, int minLevel) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(mobile)) from userstat where regtime between ? and ? and channel=? and nowlevel>=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, minLevel);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getChannelUniqMobileUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getChannelUniqMobileNormalUserNum(long channelid, Date starttime, Date endtime, int minLevel) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(mobile)) from userstat where regtime between ? and ? " +
					"and channel=? and nowlevel>=? and regtype=? and firstlogintime is not null";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, minLevel);
			pstmt.setLong(5, UserStat.REGTYPE_NORMAL);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getChannelUniqMobileUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getChannelUniqMobileUserNum(long channelid, long channelitemid, Date starttime, Date endtime, int minLevel) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(mobile)) from userstat where regtime between ? and ? and channel=? and channelitem=? and nowlevel>=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			pstmt.setLong(5, minLevel);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getChannelUniqMobileUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getChannelUniqMobileNormalUserNum(long channelid, long channelitemid, Date starttime, Date endtime, int minLevel) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(mobile)) from userstat where regtime between ? and ? " +
					"and channel=? and channelitem=? and nowlevel>=? and regtype=? and firstlogintime is not null";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			pstmt.setLong(5, minLevel);
			pstmt.setLong(6, UserStat.REGTYPE_NORMAL);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getChannelUniqMobileUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public HashMap<Integer, Long> getNoLoginUserMaxLevelMap(int nologindays) {
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<Integer, Long> cmap = new HashMap<Integer, Long>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -nologindays);
		try {
			con = getConnection();
			String sql = "select nowlevel, count(*) from userstat where lastlogintime<? group by nowlevel";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(cal.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				cmap.put(rs.getInt(1), rs.getLong(2));
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getNoLoginUserMaxLevelMap failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return cmap;
	}
	
	public Connection getConnection() throws SQLException {
		return DataSourceManager.getInstance().getConnection();
	}
}
