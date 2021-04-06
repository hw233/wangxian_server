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

import com.sqage.stat.model.PlayerLogout;
import com.xuanzhi.tools.dbpool.DataSourceManager;

/**
 * A data access object (DAO) providing persistence and search support for
 * PlayerLogout entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.xuanzhi.stat.model.PlayerLogout
 * @author MyEclipse Persistence Tools
 */

public class PlayerLogoutDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(PlayerLogoutDAO.class);
	// property constants
	public static final String USERID = "userid";
	public static final String PLAYERID = "playerid";
	public static final String PLAYERLEVEL = "playerlevel";
	public static final String SERVERID = "serverid";
	public static final String CAREERID = "careerid";
	public static final String SEX = "sex";
	public static final String POLCAMPID = "polcampid";
	public static final String MAPID = "mapid";
	public static final String CHANNELID = "channelid";
	public static final String CHANNELITEMID = "channelitemid";
	public static final String LOGINTIME = "logintime";
	public static final String LOGINOUTTIME = "loginouttime";
	public static final String ONLINETIME = "onlinetime";
	
	public static final String tableName = "playerlogout";
	public static final Class pojoClass = PlayerLogout.class;

	protected void initDao() {
		// do nothing
	}

	public void save(PlayerLogout transientInstance) {
		log.debug("saving PlayerLogout instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PlayerLogout persistentInstance) {
		log.debug("deleting PlayerLogout instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PlayerLogout findById(java.lang.Long id) {
		log.debug("getting PlayerLogout instance with id: " + id);
		try {
			PlayerLogout instance = (PlayerLogout) getHibernateTemplate().get(
					"com.sqage.stat.model.PlayerLogout", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(PlayerLogout instance) {
		log.debug("finding PlayerLogout instance by example");
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
		log.debug("finding PlayerLogout instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from PlayerLogout as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByUserid(Object userid) {
		return findByProperty(USERID, userid);
	}

	public List findByPlayerid(Object playerid) {
		return findByProperty(PLAYERID, playerid);
	}

	public List findByPlayerlevel(Object playerlevel) {
		return findByProperty(PLAYERLEVEL, playerlevel);
	}

	public List findByServerid(Object serverid) {
		return findByProperty(SERVERID, serverid);
	}

	public List findByCareerid(Object careerid) {
		return findByProperty(CAREERID, careerid);
	}

	public List findBySex(Object sex) {
		return findByProperty(SEX, sex);
	}

	public List findByPolcampid(Object polcampid) {
		return findByProperty(POLCAMPID, polcampid);
	}

	public List findByMapid(Object mapid) {
		return findByProperty(MAPID, mapid);
	}

	public List findByChannelid(Object channelid) {
		return findByProperty(CHANNELID, channelid);
	}

	public List findByChannelitemid(Object channelitemid) {
		return findByProperty(CHANNELITEMID, channelitemid);
	}

	public List findByLogintime(Object logintime) {
		return findByProperty(LOGINTIME, logintime);
	}

	public List findByLoginouttime(Object loginouttime) {
		return findByProperty(LOGINOUTTIME, loginouttime);
	}

	public List findByOnlinetime(Object onlinetime) {
		return findByProperty(ONLINETIME, onlinetime);
	}

	public List findAll() {
		log.debug("finding all PlayerLogout instances");
		try {
			String queryString = "from PlayerLogout";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PlayerLogout merge(PlayerLogout detachedInstance) {
		log.debug("merging PlayerLogout instance");
		try {
			PlayerLogout result = (PlayerLogout) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PlayerLogout instance) {
		log.debug("attaching dirty PlayerLogout instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PlayerLogout instance) {
		log.debug("attaching clean PlayerLogout instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static PlayerLogoutDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (PlayerLogoutDAO) ctx.getBean("PlayerLogoutDAO");
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
	
	public long getOnlinetime(java.util.Date statdate) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(onlinetime) from " +  tableName + " where logintime between ? and ?";
			pstmt = con.prepareStatement(sql);
			Calendar cal = Calendar.getInstance();
			cal.setTime(statdate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			long starttime = cal.getTimeInMillis();
			cal.add(Calendar.DAY_OF_YEAR, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			long endtime = cal.getTimeInMillis();
			pstmt.setTimestamp(1, new Timestamp(starttime));
			pstmt.setTimestamp(2, new Timestamp(endtime));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getOnlinetime(java.util.Date statdate, long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(onlinetime) from " +  tableName + " where logintime between ? and ? and serverid=?";
			pstmt = con.prepareStatement(sql);
			Calendar cal = Calendar.getInstance();
			cal.setTime(statdate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			long starttime = cal.getTimeInMillis();
			cal.add(Calendar.DAY_OF_YEAR, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			long endtime = cal.getTimeInMillis();
			pstmt.setTimestamp(1, new Timestamp(starttime));
			pstmt.setTimestamp(2, new Timestamp(endtime));
			pstmt.setLong(3, serverid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getOnlinetime(java.util.Date starttime, Date endtime, long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(onlinetime) from " +  tableName + " where logintime between ? and ? and serverid=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getOnlinetime(java.util.Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(onlinetime) from " +  tableName + " where logintime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getNewPlayerOnlinetime(java.util.Date statdate) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(pl.onlinetime) from playerlogout pl, userstat us " +
					"where logintime between ? and ? " +
					"and pl.userid=us.id and us.regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			Calendar cal = Calendar.getInstance();
			cal.setTime(statdate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			long starttime = cal.getTimeInMillis();
			cal.add(Calendar.DAY_OF_YEAR, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			long endtime = cal.getTimeInMillis();
			pstmt.setTimestamp(1, new Timestamp(starttime));
			pstmt.setTimestamp(2, new Timestamp(endtime));
			pstmt.setTimestamp(3, new java.sql.Timestamp(starttime));
			pstmt.setTimestamp(4, new java.sql.Timestamp(endtime));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getNewPlayerOnlinetime(java.util.Date statdate, long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(pl.onlinetime) from playerlogout pl, userstat us " +
					"where logintime between ? and ? and serverid=? " +
					"and pl.userid=us.id and us.regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			Calendar cal = Calendar.getInstance();
			cal.setTime(statdate);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			long starttime = cal.getTimeInMillis();
			cal.add(Calendar.DAY_OF_YEAR, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			long endtime = cal.getTimeInMillis();
			pstmt.setTimestamp(1, new Timestamp(starttime));
			pstmt.setTimestamp(2, new Timestamp(endtime));
			pstmt.setLong(3, serverid);
			pstmt.setTimestamp(4, new java.sql.Timestamp(starttime));
			pstmt.setTimestamp(5, new java.sql.Timestamp(endtime));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getNewUserPlayerOnlinetime(java.util.Date starttime, Date endtime, long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(pl.onlinetime) from playerlogout pl, userstat us " +
					"where logintime between ? and ? and pl.userid=us.id and us.regtime between ? and ? and serverid=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(4, new Timestamp(endtime.getTime()));
			pstmt.setLong(5, serverid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getNewUserPlayerOnlinetime(java.util.Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(pl.onlinetime) from playerlogout pl, userstat us " +
					"where logintime between ? and ? and pl.userid=us.id and us.regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(4, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public HashMap<Integer, Integer> getNewUserPlayerOnlinetimeMap(java.util.Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<Integer, Integer> map = new HashMap<Integer,Integer>();
		try {
			con = getConnection();
			String sql = "select us.id,sum(pl.onlinetime)/(60*10) as otime from playerlogout pl, userstat us " +
					"where logintime between ? and ? and pl.userid=us.id and us.regtime between ? and ? group by us.id order by otime";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(4, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				float time = rs.getFloat(2);
				int timeint = new Float(time).intValue();
				Integer num = map.get(timeint);
				if(num == null) {
					map.put(timeint, 1);
				} else {
					map.put(timeint, num+1);
				}
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return map;
	}
	
	public HashMap<Integer, Integer> getLoginUserOnlinetimeMap(java.util.Date statdate) {
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<Integer, Integer> map = new HashMap<Integer,Integer>();
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
			String sql = "select userid,sum(pl.onlinetime)/(60*10) as otime from playerlogout pl,userstat us " +
					"where logintime between ? and ? and pl.userid=us.id and us.regtime<? group by userid order by otime";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(start.getTime()));
			pstmt.setTimestamp(2, new Timestamp(end.getTime()));
			pstmt.setTimestamp(3, new Timestamp(start.getTime()));
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				float time = rs.getFloat(2);
				int timeint = new Float(time).intValue();
				Integer num = map.get(timeint);
				if(num == null) {
					map.put(timeint, 1);
				} else {
					map.put(timeint, num+1);
				}
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return map;
	}
	
	public HashMap<Integer, Integer> getNewUserPlayerOnlinetimeMap(java.util.Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<Integer, Integer> map = new HashMap<Integer,Integer>();
		try {
			con = getConnection();
			String sql = "select us.id,sum(pl.onlinetime)/(60*10) as otime from playerlogout pl, userstat us " +
					"where logintime between ? and ? and pl.userid=us.id and us.regtime between ? and ? and us.channel=? group by us.id order by otime";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(4, new Timestamp(endtime.getTime()));
			pstmt.setLong(5, channelid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				float time = rs.getFloat(2);
				int timeint = new Float(time).intValue();
				Integer num = map.get(timeint);
				if(num == null) {
					map.put(timeint, 1);
				} else {
					map.put(timeint, num+1);
				}
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return map;
	}
	
	public HashMap<Integer, Integer> getNewUserPlayerOnlinetimeMap(java.util.Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		HashMap<Integer, Integer> map = new HashMap<Integer,Integer>();
		try {
			con = getConnection();
			String sql = "select us.id,sum(pl.onlinetime)/(60*10) as otime from playerlogout pl, userstat us " +
					"where logintime between ? and ? and pl.userid=us.id and us.regtime between ? and ? " +
					"and us.channel=? and us.channelitem=? group by us.id order by otime";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(4, new Timestamp(endtime.getTime()));
			pstmt.setLong(5, channelid);
			pstmt.setLong(6, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				float time = rs.getFloat(2);
				int timeint = new Float(time).intValue();
				Integer num = map.get(timeint);
				if(num == null) {
					map.put(timeint, 1);
				} else {
					map.put(timeint, num+1);
				}
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return map;
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
			String sql = "select mlevel,count(*) from " +
					"(select userid,max(playerlevel) as mlevel from playerlogout pl,userstat us where " +
					"logintime between ? and ? and pl.userid=us.id and us.regtime<? group by userid) " +
					"group by mlevel";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(start.getTime()));
			pstmt.setTimestamp(2, new Timestamp(end.getTime()));
			pstmt.setTimestamp(3, new Timestamp(start.getTime()));
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
	
	public long getPlayerOnlinetime(java.util.Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(onlinetime) from playerlogout " +
					"where logintime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getChannelPlayerOnlinetime(java.util.Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(onlinetime) from playerlogout " +
					"where logintime between ? and ? and channelid=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getChannelPlayerOnlinetime(java.util.Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(onlinetime) from playerlogout " +
					"where logintime between ? and ? and channelid=? and channelitemid=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) {
			log.error("getOnlinetime failed", e);
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
	
	public Connection getConnection() throws SQLException {
		return DataSourceManager.getInstance().getConnection();
	}
}
