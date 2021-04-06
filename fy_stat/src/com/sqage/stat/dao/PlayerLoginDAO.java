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

import com.sqage.stat.model.PlayerLogin;
import com.xuanzhi.tools.dbpool.DataSourceManager;

/**
 * A data access object (DAO) providing persistence and search support for
 * PlayerLogin entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.xuanzhi.stat.model.PlayerLogin
 * @author MyEclipse Persistence Tools
 */

public class PlayerLoginDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(PlayerLoginDAO.class);
	// property constants
	public static final String USERID = "userid";
	public static final String PLAYERID = "playerid";
	public static final String PLAYERLEVEL = "playerlevel";
	public static final String SERVERID = "serverid";
	public static final String CAREERID = "careerid";
	public static final String SEX = "sex";
	public static final String POLCAMP = "polcamp";
	public static final String CHANNELID = "channelid";
	public static final String CHANNELITEMID = "channelitemid";
	public static final String LOGINTIME = "logintime";
	
	public static final String tableName = "playerlogin";
	public static final Class pojoClass = PlayerLogin.class;

	protected void initDao() {
		// do nothing
	}

	public void save(PlayerLogin transientInstance) {
		log.debug("saving PlayerLogin instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PlayerLogin persistentInstance) {
		log.debug("deleting PlayerLogin instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PlayerLogin findById(java.lang.Long id) {
		log.debug("getting PlayerLogin instance with id: " + id);
		try {
			PlayerLogin instance = (PlayerLogin) getHibernateTemplate().get(
					"com.sqage.stat.model.PlayerLogin", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(PlayerLogin instance) {
		log.debug("finding PlayerLogin instance by example");
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
		log.debug("finding PlayerLogin instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from PlayerLogin as model where model."
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

	public List findByPolcamp(Object polcamp) {
		return findByProperty(POLCAMP, polcamp);
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

	public List findAll() {
		log.debug("finding all PlayerLogin instances");
		try {
			String queryString = "from PlayerLogin";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PlayerLogin merge(PlayerLogin detachedInstance) {
		log.debug("merging PlayerLogin instance");
		try {
			PlayerLogin result = (PlayerLogin) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PlayerLogin instance) {
		log.debug("attaching dirty PlayerLogin instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PlayerLogin instance) {
		log.debug("attaching clean PlayerLogin instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static PlayerLoginDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (PlayerLoginDAO) ctx.getBean("PlayerLoginDAO");
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
	
	public int getUniLoginPlayerNum(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(playerid)) from " +  tableName + " where logintime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getUniPlayerLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getUniLoginPlayerNum(Date starttime, Date endtime, long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(playerid)) from " +  tableName + " where logintime between ? and ? and serverid=?";
			pstmt = con.prepareStatement(sql);
			Calendar cal = Calendar.getInstance();
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getUniPlayerLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getLoginNum(Date starttime, Date endtime, long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(*) from " + tableName + " where logintime between ? and ? and serverid=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getLoginNum(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(*) from " + tableName + " where logintime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getLoginUserNum(Date starttime, Date endtime, long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(distinct(userid)) from " + tableName + " where logintime between ? and ? and serverid=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getChannelLoginUserNum(Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(distinct(userid)) from " + tableName + " where logintime between ? and ? and channelid=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getChannelLoginUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getChannelLoginUserNum(Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(distinct(userid)) from " + tableName + " where logintime between ? and ? and channelid=? and channelitemid=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getChannelLoginUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getLoginUserNum(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(distinct(userid)) from " + tableName + " where logintime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getNewPlayerLoginNum(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(*) from playerlogin pl, userstat us " +
					"where logintime between ? and ? " +
					"and pl.userid=us.id and us.regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(starttime.getTime()));
			pstmt.setTimestamp(4, new java.sql.Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getNewPlayerLoginNum(Date starttime, Date endtime, long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(*) from playerlogin pl, userstat us " +
					"where logintime between ? and ? and serverid=? " +
					"and pl.userid=us.id and us.regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			pstmt.setTimestamp(4, new java.sql.Timestamp(starttime.getTime()));
			pstmt.setTimestamp(5, new java.sql.Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getLoginNewPlayerNum(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(distinct(playerid)) from playerlogin pl, userstat us " +
					"where logintime between ? and ? " +
					"and pl.userid=us.id and us.regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(starttime.getTime()));
			pstmt.setTimestamp(4, new java.sql.Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getLoginNewPlayerNum(Date starttime, Date endtime, long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(distinct(playerid)) from playerlogin pl, userstat us " +
					"where logintime between ? and ? and serverid=? " +
					"and pl.userid=us.id and us.regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			pstmt.setTimestamp(4, new java.sql.Timestamp(starttime.getTime()));
			pstmt.setTimestamp(5, new java.sql.Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getNewUserLoginNum(Date regstart, Date regend, Date loginstart, Date loginend) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(distinct(userid)) from playerlogin pl, userstat us " +
					"where logintime between ? and ? " +
					"and pl.userid=us.id and us.regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(loginstart.getTime()));
			pstmt.setTimestamp(2, new Timestamp(loginend.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(regstart.getTime()));
			pstmt.setTimestamp(4, new java.sql.Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getNewUserLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getOldUserLoginNum(int levelstart, int levelend, Date loginstart, Date loginend, Date statstart, Date statend) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(distinct(userid)) from playerlogin pl " +
					"where logintime between ? and ? and exists (select distinct(userid) from " +
					"playerlogin where playerlevel between ? and ? and logintime between ? and ? and " +
					" userid=pl.userid)";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(statstart.getTime()));
			pstmt.setTimestamp(2, new Timestamp(statend.getTime()));
			pstmt.setLong(3, levelstart);
			pstmt.setLong(4, levelend);
			pstmt.setTimestamp(5, new java.sql.Timestamp(loginstart.getTime()));
			pstmt.setTimestamp(6, new java.sql.Timestamp(loginend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getOldUserLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getOldUserLoginNum(int levelstart, int levelend, Date loginstart, Date loginend) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(distinct(userid)) from " +
					"playerlogin where playerlevel between ? and ? and logintime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, levelstart);
			pstmt.setLong(2, levelend);
			pstmt.setTimestamp(3, new java.sql.Timestamp(loginstart.getTime()));
			pstmt.setTimestamp(4, new java.sql.Timestamp(loginend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getOldUserLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getNewUserLoginNumByChannel(long channelid, long channelitemid, Date regstart, Date regend, Date loginstart, Date loginend) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql =  "select count(distinct(userid)) from playerlogin pl, userstat us " +
					"where logintime between ? and ? " +
					"and pl.userid=us.id and us.regtime between ? and ? and pl.channelid=? and pl.channelitemid=?";
			if(channelitemid == -1) {
				 sql =  "select count(distinct(userid)) from playerlogin pl, userstat us " +
					"where logintime between ? and ? " +
					"and pl.userid=us.id and us.regtime between ? and ? and pl.channelid=?";
			}
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(loginstart.getTime()));
			pstmt.setTimestamp(2, new Timestamp(loginend.getTime()));
			pstmt.setTimestamp(3, new java.sql.Timestamp(regstart.getTime()));
			pstmt.setTimestamp(4, new java.sql.Timestamp(regend.getTime()));
			pstmt.setLong(5, channelid);
			if(channelitemid != -1) {
				pstmt.setLong(6, channelitemid);
			}
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) {
			log.error("getNewUserLoginNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
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
					"(select userid,max(playerlevel) as mlevel from playerlogin where logintime between ? and ? group by userid) " +
					"group by mlevel";
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
