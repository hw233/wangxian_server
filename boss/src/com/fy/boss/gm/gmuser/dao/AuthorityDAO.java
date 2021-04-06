package com.fy.boss.gm.gmuser.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.fy.boss.gm.gmuser.Authority;
import com.xuanzhi.tools.dbpool.DataSourceManager;

/**
 * A data access object (DAO) providing persistence and search support for
 * Authority entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.xuanzhi.boss.gmuser.model.Authority
 * @author MyEclipse Persistence Tools
 */

public class AuthorityDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(AuthorityDAO.class);
	// property constants
	public static final String ROLEID = "roleid";

	protected void initDao() {
		// do nothing
	}

	public void save(Authority transientInstance) {
		//保存一个对象
		log.debug("saving Authority instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	public void saveAll(List<Authority> authorities) {
		//保存一个对象
		log.debug("saving Authority instance");
		try {
			getHibernateTemplate().saveOrUpdateAll(authorities);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
    
	public void delete(Authority persistentInstance) {
		//删除一个对象
		log.debug("deleting Authority instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public void deleteAll(List<Authority> authorities) {
		//删除一个对象
		log.debug("deleting Authority instance");
		try {
			getHibernateTemplate().deleteAll(authorities);
			log.debug("list delete successful");
		} catch (RuntimeException re) {
			log.error("list delete failed", re);
			throw re;
		}
	}

	public Authority findById(Integer id) {
		//根据id查找对象
		log.debug("getting Authority instance with id: " + id);
		try {
			Authority instance = (Authority) getHibernateTemplate().get(
					"com.xuanzhi.boss.gmuser.model.Authority", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Authority instance) {
		log.debug("finding Authority instance by example");
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

	public List<Authority> findByProperty(String propertyName, Object value) {
		log.debug("finding Authority instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Authority as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByRoleid(Object roleid) {
		return findByProperty(ROLEID, roleid);
	}

	public List findAll() {
		log.debug("finding all Authority instances");
		try {
			String queryString = "from Authority";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Authority merge(Authority detachedInstance) {
		log.debug("merging Authority instance");
		try {
			Authority result = (Authority) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Authority instance) {
		log.debug("attaching dirty Authority instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Authority instance) {
		log.debug("attaching clean Authority instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static AuthorityDAO getFromApplicationContext(ApplicationContext ctx) {
		return (AuthorityDAO) ctx.getBean("AuthorityDAO");
	}
	public Connection getConnection() throws SQLException {
		return DataSourceManager.getInstance().getConnection();
	}
	
	public void update(List<Authority> authorities,int id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			String sql1 ="delete from authority where userid="+id;
			con = getConnection();
			con.setAutoCommit(false);
			pstmt= con.prepareStatement(sql1);
			pstmt.executeUpdate();
			String sql = "insert into authority (id,userid,roleid) " +
			"values(SEQ_AUTHORITY.nextval,?,?)";
			for(Authority a : authorities) {
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, id);
				pstmt.setString(2,a.getRoleid());
				pstmt.executeUpdate();
			}
			con.commit();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {pstmt.close();} catch(Exception e) {}
			try {con.close();} catch(Exception e) {}
		}
	}
	public void addList(List<Authority> authorities,int id) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = getConnection();
			con.setAutoCommit(false);
			String sql = "insert into authority (id,userid,roleid) " +
					"values(SEQ_AUTHORITY.nextval,?,?)";
			for(Authority a : authorities) {
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, id);
				stmt.setString(2,a.getRoleid());
				stmt.executeUpdate();
			}
			con.commit();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {stmt.close();} catch(Exception e) {}
			try {con.close();} catch(Exception e) {}
		}
	}
}
