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
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.xuanzhi.stat.model.ExpenseReason;
import com.xuanzhi.stat.model.PlayerExpense;
import com.xuanzhi.stat.service.ExpenseReasonManager;
import com.xuanzhi.tools.dbpool.DataSourceManager;
import com.xuanzhi.tools.text.NumberUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * PlayerExpense entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.xuanzhi.stat.model.PlayerExpense
 * @author MyEclipse Persistence Tools
 */

public class PlayerExpenseDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(PlayerExpenseDAO.class);
	// property constants
	public static final String NOWLEVEL = "nowlevel";
	public static final String SERVER = "server";
	public static final String CAREER = "career";
	public static final String SEX = "sex";
	public static final String POLCAMP = "polcamp";
	public static final String MAP = "map";
	public static final String EXPENSEREASON = "expensereason";
	public static final String CURRENCYTYPE = "currencytype";
	public static final String EXPENSEAMOUNT = "expenseamount";
	public static final String CHANNEL = "channel";
	public static final String CHANNELITEM = "channelitem";
	public static final String UPTIME = "uptime";
	public static final String REGTIME = "regtime";
	
	public static final String tableName = "playerexpense";
	public static final Class pojoClass = PlayerExpense.class;

	protected void initDao() {
		// do nothing
	}

	public void save(PlayerExpense transientInstance) {
		log.debug("saving PlayerExpense instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PlayerExpense persistentInstance) {
		log.debug("deleting PlayerExpense instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PlayerExpense findById(java.lang.Long id) {
		log.debug("getting PlayerExpense instance with id: " + id);
		try {
			PlayerExpense instance = (PlayerExpense) getHibernateTemplate()
					.get("com.xuanzhi.stat.model.PlayerExpense", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(PlayerExpense instance) {
		log.debug("finding PlayerExpense instance by example");
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
		log.debug("finding PlayerExpense instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from PlayerExpense as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByNowlevel(Object nowlevel) {
		return findByProperty(NOWLEVEL, nowlevel);
	}

	public List findByServer(Object server) {
		return findByProperty(SERVER, server);
	}

	public List findByCareer(Object career) {
		return findByProperty(CAREER, career);
	}

	public List findBySex(Object sex) {
		return findByProperty(SEX, sex);
	}

	public List findByPolcamp(Object polcamp) {
		return findByProperty(POLCAMP, polcamp);
	}

	public List findByMap(Object map) {
		return findByProperty(MAP, map);
	}

	public List findByExpensereason(Object expensereason) {
		return findByProperty(EXPENSEREASON, expensereason);
	}

	public List findByCurrencytype(Object currencytype) {
		return findByProperty(CURRENCYTYPE, currencytype);
	}

	public List findByExpenseamount(Object expenseamount) {
		return findByProperty(EXPENSEAMOUNT, expenseamount);
	}

	public List findByChannel(Object channel) {
		return findByProperty(CHANNEL, channel);
	}

	public List findByChannelitem(Object channelitem) {
		return findByProperty(CHANNELITEM, channelitem);
	}

	public List findByUptime(Object uptime) {
		return findByProperty(UPTIME, uptime);
	}

	public List findByRegtime(Object regtime) {
		return findByProperty(REGTIME, regtime);
	}

	public List findAll() {
		log.debug("finding all PlayerExpense instances");
		try {
			String queryString = "from PlayerExpense";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PlayerExpense merge(PlayerExpense detachedInstance) {
		log.debug("merging PlayerExpense instance");
		try {
			PlayerExpense result = (PlayerExpense) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PlayerExpense instance) {
		log.debug("attaching dirty PlayerExpense instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PlayerExpense instance) {
		log.debug("attaching clean PlayerExpense instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static PlayerExpenseDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (PlayerExpenseDAO) ctx.getBean("PlayerExpenseDAO");
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
	
	public int getExpenseUniUserNum(Date starttime, Date endtime, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from " +  tableName + " where uptime between ? and ? and currencytype=?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniPlayerNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getExpenseUniUserNum(Date starttime, Date endtime, int currencyType, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from " +  tableName + " where uptime between ? and ? and currencytype=? and channel=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			pstmt.setLong(4, channelid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniPlayerNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getExpenseUniUserNum(Date starttime, Date endtime, int currencyType, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from " +  tableName + " where uptime between ? and ? and currencytype=? " +
					"and channel=? and channelitem=?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			pstmt.setLong(4, channelid);
			pstmt.setLong(5, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniPlayerNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getExpenseUniUserNum(Date starttime, Date endtime, long serverid,  int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from " +  tableName + " where uptime between ? and ? and server=? and currencytype=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			pstmt.setLong(4, currencyType);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniPlayerNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getExpenseUniNewUserNum(Date regstart, Date regend, Date starttime, Date endtime, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from playerexpense" +
					" where uptime between ? and ? and currencytype=? and regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			pstmt.setTimestamp(4, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(5, new Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniNewUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getExpenseUniNewUserNum(Date regstart, Date regend, Date starttime, Date endtime, int currencyType, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from playerexpense" +
					" where uptime between ? and ? and currencytype=? and regtime between ? and ? and channel=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			pstmt.setTimestamp(4, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(5, new Timestamp(regend.getTime()));
			pstmt.setLong(6, channelid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniNewUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getExpenseUniNewUserNum(Date regstart, Date regend, Date starttime, Date endtime, int currencyType, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from playerexpense" +
					" where uptime between ? and ? and currencytype=? and regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			pstmt.setTimestamp(4, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(5, new Timestamp(regend.getTime()));
			pstmt.setLong(6, channelid);
			pstmt.setLong(7, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniNewUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getExpenseUniNewUserNum(Date starttime, Date endtime, long serverid,  int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from playerexpense pe, userstat us " +
					"where uptime between ? and ? and server=? and currencytype=? and userid=us.id and us.regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			pstmt.setLong(4, currencyType);
			pstmt.setTimestamp(5, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(6, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniNewUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getExpenseUniOldUserNum(Date starttime, Date endtime, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from playerexpense pe,userstat us " +
					"where uptime between ? and ? and currencytype=? and userid=us.id and us.regtime<?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			pstmt.setTimestamp(4, new Timestamp(starttime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniOldUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getExpenseUniOldUserNum(Date starttime, Date endtime, long serverid,  int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from playerexpense pe, userstat us " +
					"where uptime between ? and ? and server=? and currencytype=? and userid=us.id and us.regtime<?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			pstmt.setLong(4, currencyType);
			pstmt.setTimestamp(5, new Timestamp(starttime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniOldUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getNologinExpenseUniOldUserNum(Date logintime, Date starttime, Date endtime, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from playerexpense pe,userstat us " +
					"where uptime between ? and ? and currencytype=? and userid=us.id and us.lastlogintime<?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			pstmt.setTimestamp(4, new Timestamp(logintime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniOldUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getNologinExpenseUniOldUserNum(Date logintime, Date starttime, Date endtime, long serverid,  int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from playerexpense pe, userstat us " +
					"where uptime between ? and ? and server=? and currencytype=? and userid=us.id and us.lastlogintime<?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			pstmt.setLong(4, currencyType);
			pstmt.setTimestamp(5, new Timestamp(logintime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniOldUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getNoExpenseUniOldUserNum(Date expensetime, Date starttime, Date endtime, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from playerexpense pe,userstat us " +
					"where uptime between ? and ? and currencytype=? and userid=us.id and us.lastexpensetime<?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			pstmt.setTimestamp(4, new Timestamp(expensetime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniOldUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	/**
	 * 获取付费账号停费数
	 * @param expensetime
	 * @param starttime
	 * @param endtime
	 * @param serverid
	 * @param currencyType
	 * @return
	 */
	public int getNoExpenseUniOldUserNum(Date expensetime, Date starttime, Date endtime, long serverid,  int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from playerexpense pe, userstat us " +
					"where uptime between ? and ? and server=? and currencytype=? and userid=us.id and us.lastexpensetime<?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			pstmt.setLong(4, currencyType);
			pstmt.setTimestamp(5, new Timestamp(expensetime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniOldUserNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getExpenseUniPlayerNum(Date starttime, Date endtime, long serverid, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(playerid)) from " +  tableName + " where uptime between ? and ? and server=? and currencytype=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			pstmt.setLong(4, currencyType);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUniPlayerNum failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getExpenseAmount(Date starttime, Date endtime, long serverid, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(expenseamount) from " + tableName + " where uptime between ? and ? and server=? and currencytype=?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			pstmt.setLong(4, currencyType);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getExpenseAmount(Date starttime, Date endtime, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(expenseamount) from " + tableName + " where uptime between ? and ? and currencytype=?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getExpenseAmount(Date regstart, Date regend, Date starttime, Date endtime, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(expenseamount) from " + tableName + " where uptime between ? and ? " +
					"and currencytype=? and regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			pstmt.setTimestamp(4, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(5, new Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getRecycleAmount(Date starttime, Date endtime, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			ExpenseReasonManager ermanager = ExpenseReasonManager.getInstance();
			long expenseReasonIds[] = ermanager.getTypeIds(ExpenseReason.GAME_RECYCLE);
			StringBuffer sb = new StringBuffer();
			sb.append("(");
			for(long r : expenseReasonIds) {
				sb.append(r + ",");
			}
			String tsql = sb.toString();
			if(tsql.length() > 1) {
				tsql = tsql.substring(0, tsql.length()-1);
			}
			tsql = tsql + ")";
			con = getConnection();
			String sql = "select sum(expenseamount) from " + tableName + " where uptime between ? and ? " +
					"and currencytype=? and expensereason in " + tsql;
			System.out.println(sql);
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getRecycleAmount(Date starttime, Date endtime, long serverid, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			ExpenseReasonManager ermanager = ExpenseReasonManager.getInstance();
			long expenseReasonIds[] = ermanager.getTypeIds(ExpenseReason.GAME_RECYCLE);
			StringBuffer sb = new StringBuffer();
			sb.append("(");
			for(long r : expenseReasonIds) {
				sb.append(r + ",");
			}
			String tsql = sb.toString();
			if(tsql.length() > 1) {
				tsql = tsql.substring(0, tsql.length()-1);
			}
			tsql = tsql + ")";
			con = getConnection();
			String sql = "select sum(expenseamount) from " + tableName + " where uptime between ? and ? " +
					"and server=? and currencytype=? and expensereason in " + tsql;
			pstmt = con.prepareStatement(sql);
			
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			pstmt.setLong(4, currencyType);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getExpenseAmount(Date starttime, Date endtime, int currencyType, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(expenseamount) from " + tableName + " where uptime between ? and ? and currencytype=? and channel=? and channelitem=?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			pstmt.setLong(4, channelid);
			pstmt.setLong(5, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getExpenseAmount(Date regstart, Date regend, Date starttime, Date endtime, int currencyType, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(expenseamount) from " + tableName + " where uptime between ? and ? and currencytype=? " +
					"and channel=? and channelitem=? and regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			pstmt.setLong(4, channelid);
			pstmt.setLong(5, channelitemid);
			pstmt.setTimestamp(6, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(7, new Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getExpenseAmount(Date starttime, Date endtime, int currencyType, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(expenseamount) from " + tableName + " where uptime between ? and ? and currencytype=? and channel=?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			pstmt.setLong(4, channelid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getExpenseAmount(Date regstart, Date regend, Date starttime, Date endtime, int currencyType, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(expenseamount) from " + tableName + " where uptime between ? and ? and currencytype=? " +
					"and channel=? and regtime between ? and ?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			pstmt.setLong(4, channelid);
			pstmt.setTimestamp(5, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(6, new Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getExpenseUserAvgLevel(Date starttime, Date endtime, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(nlevel) from (select max(nowlevel) as nlevel from " +  tableName + 
							" where uptime between ? and ? and currencytype=? group by userid)";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return NumberUtils.round(rs.getFloat(1),1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUserAvgLevel failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getExpenseUserAvgLevel(Date starttime, Date endtime, long serverid, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(nlevel) from (select max(nowlevel) as nlevel from " +  tableName + 
							" where uptime between ? and ? and server=? and currencytype=? group by userid)";			
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			pstmt.setLong(4, currencyType);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return NumberUtils.round(rs.getFloat(1),1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getExpenseUserAvgLevel failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getFirstExpenseUserAvgLevel(Date starttime, Date endtime, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(nlevel) from (select max(nowlevel) as nlevel from " +  tableName + 
							" where uptime between ? and ? and currencytype=? and firstexpenseyuanbao=1 group by userid)";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, currencyType);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return NumberUtils.round(rs.getFloat(1),1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getFirstExpenseUserAvgLevel failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getFirstExpenseUserAvgLevel(Date starttime, Date endtime, long serverid, int currencyType) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(nlevel) from (select max(nowlevel) as nlevel from " +  tableName + 
							" where uptime between ? and ? and server=? and currencytype=? and firstexpenseyuanbao=1 group by userid)";			
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, serverid);
			pstmt.setLong(4, currencyType);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return NumberUtils.round(rs.getFloat(1),1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getFirstExpenseUserAvgLevel failed", e);
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
