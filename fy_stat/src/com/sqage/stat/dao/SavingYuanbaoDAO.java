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

import com.sqage.stat.model.SavingYuanbao;
import com.xuanzhi.tools.dbpool.DataSourceManager;
import com.xuanzhi.tools.text.NumberUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * SavingYuanbao entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.sqage.stat.model.SavingYuanbao
 * @author MyEclipse Persistence Tools
 */

public class SavingYuanbaoDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(SavingYuanbaoDAO.class);
	// property constants
	public static final String USERID = "userid";
	public static final String PLAYERID = "playerid";
	public static final String PROVINCEID = "provinceid";
	public static final String CITYID = "cityid";
	public static final String NOWLEVEL = "nowlevel";
	public static final String SERVERID = "serverid";
	public static final String SEXID = "sexid";
	public static final String POLCAMPID = "polcampid";
	public static final String CAREERID = "careerid";
	public static final String MAPID = "mapid";
	public static final String CHANNELID = "channelid";
	public static final String CHANNELITEMID = "channelitemid";
	public static final String PLATFORMID = "platformid";
	public static final String MEDIUMID = "mediumid";
	public static final String FIRSTSAVING = "firstsaving";
	public static final String YUANBAO = "yuanbao";
	public static final String RMB = "rmb";
	public static final String SUCC = "succ";
	public static final String COST = "cost";
	public static final String ORDERNO = "orderno";
	
	public static final String tableName = "savingyuanbao";
	public static final Class pojoClass = SavingYuanbao.class;

	protected void initDao() {
		// do nothing
	}

	public boolean save(SavingYuanbao transientInstance) {
		log.debug("saving SavingYuanbao instance");
		boolean result =false;
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

	public void delete(SavingYuanbao persistentInstance) {
		log.debug("deleting SavingYuanbao instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SavingYuanbao findById(java.lang.Long id) {
		log.debug("getting SavingYuanbao instance with id: " + id);
		try {
			SavingYuanbao instance = (SavingYuanbao) getHibernateTemplate()
					.get("com.sqage.stat.model.SavingYuanbao", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SavingYuanbao instance) {
		log.debug("finding SavingYuanbao instance by example");
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
		log.debug("finding SavingYuanbao instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from SavingYuanbao as model where model."
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

	public List findByProvinceid(Object provinceid) {
		return findByProperty(PROVINCEID, provinceid);
	}

	public List findByCityid(Object cityid) {
		return findByProperty(CITYID, cityid);
	}

	public List findByNowlevel(Object nowlevel) {
		return findByProperty(NOWLEVEL, nowlevel);
	}

	public List findByServerid(Object serverid) {
		return findByProperty(SERVERID, serverid);
	}

	public List findBySexid(Object sexid) {
		return findByProperty(SEXID, sexid);
	}

	public List findByPolcampid(Object polcampid) {
		return findByProperty(POLCAMPID, polcampid);
	}

	public List findByCareerid(Object careerid) {
		return findByProperty(CAREERID, careerid);
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

	public List findByPlatformid(Object platformid) {
		return findByProperty(PLATFORMID, platformid);
	}

	public List findByMediumid(Object mediumid) {
		return findByProperty(MEDIUMID, mediumid);
	}

	public List findByFirstsaving(Object firstsaving) {
		return findByProperty(FIRSTSAVING, firstsaving);
	}

	public List findByYuanbao(Object yuanbao) {
		return findByProperty(YUANBAO, yuanbao);
	}

	public List findByRmb(Object rmb) {
		return findByProperty(RMB, rmb);
	}

	public List findBySucc(Object succ) {
		return findByProperty(SUCC, succ);
	}

	public List findByCost(Object cost) {
		return findByProperty(COST, cost);
	}

	public List findByOrderno(Object orderno) {
		return findByProperty(ORDERNO, orderno);
	}

	public List findAll() {
		log.debug("finding all SavingYuanbao instances");
		try {
			String queryString = "from SavingYuanbao";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SavingYuanbao merge(SavingYuanbao detachedInstance) {
		log.debug("merging SavingYuanbao instance");
		try {
			SavingYuanbao result = (SavingYuanbao) getHibernateTemplate()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SavingYuanbao instance) {
		log.debug("attaching dirty SavingYuanbao instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SavingYuanbao instance) {
		log.debug("attaching clean SavingYuanbao instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static SavingYuanbaoDAO getFromApplicationContext(
			ApplicationContext ctx) {
		return (SavingYuanbaoDAO) ctx.getBean("SavingYuanbaoDAO");
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
	
	public long getSavingRMBTotal() {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where succ=1";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBTotal(long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where succ=1 and serverid=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, serverid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmount(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmount(Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBPureAmount(Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb-cost) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and channelitemid=? and succ=1 and platformid <> 108";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmount(Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and channelitemid=? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmount(Date regstart, Date regend , Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? and regtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(4, new Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmountPure(Date regstart, Date regend , Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? and " +
					"regtime between ? and ? and succ=1 and platformid not in (107,108)";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(4, new Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmountNoSms(Date regstart, Date regend , Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? and regtime between ? and ? and succ=1 and platformid<>?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(4, new Timestamp(regend.getTime()));
			pstmt.setLong(5, 107);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	

	
	public long getSavingRMBAmountNoSqage(Date regstart, Date regend , Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? and regtime between ? and ? and succ=1 and platformid<>?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(4, new Timestamp(regend.getTime()));
			pstmt.setLong(5, 108);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmount(Date regstart, Date regend ,Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and regtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setTimestamp(4, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(5, new Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmountPure(Date regstart, Date regend ,Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and regtime between ? and ? and succ=1 and platformid not in (107,108)";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setTimestamp(4, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(5, new Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmountNoSms(Date regstart, Date regend ,Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and regtime between ? and ? and succ=1 and platformid<>?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setTimestamp(4, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(5, new Timestamp(regend.getTime()));
			pstmt.setLong(6, 107);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmountNoSqage(Date regstart, Date regend ,Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and regtime between ? and ? and succ=1 and platformid<>?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setTimestamp(4, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(5, new Timestamp(regend.getTime()));
			pstmt.setLong(6, 108);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmount(Date regstart, Date regend ,Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and channelitemid=? and regtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			pstmt.setTimestamp(5, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(6, new Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmountPure(Date regstart, Date regend ,Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and channelitemid=? and regtime between ? and ? and succ=1 and platformid not in (107,108)";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			pstmt.setTimestamp(5, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(6, new Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmountNoSqage(Date regstart, Date regend ,Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and channelitemid=? and regtime between ? and ? and succ=1 and platformid<>?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			pstmt.setTimestamp(5, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(6, new Timestamp(regend.getTime()));
			pstmt.setLong(7, 108);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmountNoSms(Date regstart, Date regend ,Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and channelitemid=? and regtime between ? and ? and succ=1 and platformid<>?";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			pstmt.setTimestamp(5, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(6, new Timestamp(regend.getTime()));
			pstmt.setLong(7, 107);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBCost(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(cost) from " + tableName + " where createtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBCost failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBCost(Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(cost) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBCost failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBCost(Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(cost) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and channelitemid=? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBCost failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBCost(Date regstart, Date regend, Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(cost) from " + tableName + " where createtime between ? and ? and regtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(4, new Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBCost failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBCost(Date regstart, Date regend, Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(cost) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and regtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setTimestamp(4, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(5, new Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBCost failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBCost(Date regstart, Date regend, Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(cost) from " + tableName + " where createtime between ? and ? " +
					"and channelid=? and channelitemid=? and regtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			pstmt.setTimestamp(5, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(6, new Timestamp(regend.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBCost failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingRMBAmount(long serverid, Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(rmb) from " + tableName + " where serverid=? and createtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, serverid);
			pstmt.setTimestamp(2, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingYuanbaoTotal() {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(yuanbao) from " + tableName + " where succ=1";
			pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingYuanbaoAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingYuanbaoTotal(long serverid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(yuanbao) from " + tableName + " where succ=1 and serverid=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, serverid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingYuanbaoAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingYuanbaoAmount(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(yuanbao) from " + tableName + " where createtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingYuanbaoAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getNewUserSavingYuanbaoAmount(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(yuanbao) from " + tableName + " where createtime between ? and ? and regtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(4, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingYuanbaoAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingYuanbaoAmount(Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(yuanbao) from " + tableName + " where createtime between ? and ? and channelid=? and channelitemid=? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingYuanbaoAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingYuanbaoAmount(Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(yuanbao) from " + tableName + " where createtime between ? and ? and channelid=? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingYuanbaoAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getSavingYuanbaoAmount(long serverid, Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(yuanbao) from " + tableName + " where serverid=? and createtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, serverid);
			pstmt.setTimestamp(2, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public long getNewUserSavingYuanbaoAmount(long serverid, Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select sum(yuanbao) from " + tableName + " where serverid=? and createtime between ? and ? and regtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, serverid);
			pstmt.setTimestamp(2, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(endtime.getTime()));
			pstmt.setTimestamp(4, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(5, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getLong(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getNewUserSavingYuanbaoAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getSavingYuanbaoUserNum(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from " + tableName + " where createtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getSavingYuanbaoUserNum(Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from " + tableName + " where createtime between ? and ? and channelid=? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getSavingYuanbaoUserNum(Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from " + tableName + " where createtime between ? and ? and channelid=? and and channelitemid=? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			pstmt.setLong(3, channelid);
			pstmt.setLong(4, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getSavingYuanbaoNewUsernum(Date regstart, Date regend, Date starttime, Date endtime, long channelid, long channelitemid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from " + tableName + " where regtime between ? and ? and " +
					"createtime between ? and ? and channelid=? and and channelitemid=? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(2, new Timestamp(regend.getTime()));
			pstmt.setTimestamp(3, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(4, new Timestamp(endtime.getTime()));
			pstmt.setLong(5, channelid);
			pstmt.setLong(6, channelitemid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getSavingYuanbaoNewUsernum(Date regstart, Date regend, Date starttime, Date endtime, long channelid) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from " + tableName + " where regtime between ? and ? and " +
					"createtime between ? and ? and channelid=? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(2, new Timestamp(regend.getTime()));
			pstmt.setTimestamp(3, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(4, new Timestamp(endtime.getTime()));
			pstmt.setLong(5, channelid);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getSavingYuanbaoNewUsernum(Date regstart, Date regend, Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from " + tableName + " where regtime between ? and ? and " +
					"createtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(regstart.getTime()));
			pstmt.setTimestamp(2, new Timestamp(regend.getTime()));
			pstmt.setTimestamp(3, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(4, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public int getSavingYuanbaoUserNum(long serverid, Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select count(distinct(userid)) from " + tableName + " where serverid=? and createtime between ? and ? and succ=1";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, serverid);
			pstmt.setTimestamp(2, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingRMBAmount failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getSavingYuanbaoUserAvgLevel(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(nlevel) from (select max(nowlevel) as nlevel from " + tableName +
					" where createtime between ? and ? and succ=1 group by userid)";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return NumberUtils.round(rs.getFloat(1), 1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingYuanbaoUserAvgLevel failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getSavingYuanbaoUserAvgLevel(long serverid, Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(nlevel) from (select max(nowlevel) as nlevel from " + tableName +
					" where serverid=? and createtime between ? and ? and succ=1 group by userid)";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, serverid);
			pstmt.setTimestamp(2, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return NumberUtils.round(rs.getFloat(1), 1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getSavingYuanbaoUserAvgLevel failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getFirstSavingYuanbaoUserAvgLevel(Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(nlevel) from (select max(nowlevel) as nlevel from " + tableName +
					" where createtime between ? and ? and succ=1 and firstsaving=1 group by userid)";
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(2, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return NumberUtils.round(rs.getFloat(1), 1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getFirstSavingYuanbaoUserAvgLevel failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public float getFirstSavingYuanbaoUserAvgLevel(long serverid, Date starttime, Date endtime) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "select avg(nlevel) from (select max(nowlevel) as nlevel from " + tableName +
					" where serverid=? and createtime between ? and ? and succ=1 and firstsaving=1 group by userid)";
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, serverid);
			pstmt.setTimestamp(2, new Timestamp(starttime.getTime()));
			pstmt.setTimestamp(3, new Timestamp(endtime.getTime()));
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return NumberUtils.round(rs.getFloat(1), 1);
			}
		} catch (Throwable e) { e.printStackTrace();
			log.error("getFirstSavingYuanbaoUserAvgLevel failed", e);
		} finally {
			try {pstmt.close();} catch(Exception e) {e.printStackTrace();}
			try {con.close();} catch(Exception e) {e.printStackTrace();}
		}
		return 0;
	}
	
	public Connection getConnection() throws SQLException {
		return DataSourceManager.getInstance().getConnection();
	}
}
