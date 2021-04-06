package com.fy.boss.gm.gmuser.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.fy.boss.gm.gmuser.Gmuser;

/**
 * A data access object (DAO) providing persistence and search support for
 * Gmuser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.xuanzhi.boss.gmuser.model.Gmuser
 * @author MyEclipse Persistence Tools
 */

public class GmuserDAO extends HibernateDaoSupport {
	private static final Log log = LogFactory.getLog(GmuserDAO.class);
	// property constants
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String REALNAME = "realname";

	protected void initDao() {
		// do nothing
	}

	public void save(Gmuser transientInstance) {
		log.debug("saving Gmuser instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Gmuser persistentInstance) {
		log.debug("deleting Gmuser instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Gmuser findById(Integer id) {
		log.debug("getting Gmuser instance with id: " + id);
		try {
			Gmuser instance = (Gmuser) getHibernateTemplate().get(
					"com.xuanzhi.boss.gmuser.model.Gmuser", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Gmuser instance) {
		log.debug("finding Gmuser instance by example");
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
		log.debug("finding Gmuser instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Gmuser as model where model."
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

	public List findByPassword(Object password) {
		return findByProperty(PASSWORD, password);
	}

	public List findByRealname(Object realname) {
		return findByProperty(REALNAME, realname);
	}

	public List findAll() {
		log.debug("finding all Gmuser instances");
		try {
			String queryString = "from Gmuser";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Gmuser merge(Gmuser detachedInstance) {
		log.debug("merging Gmuser instance");
		try {
			Gmuser result = (Gmuser) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Gmuser instance) {
		log.debug("attaching dirty Gmuser instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Gmuser instance) {
		log.debug("attaching clean Gmuser instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static GmuserDAO getFromApplicationContext(ApplicationContext ctx) {
		return (GmuserDAO) ctx.getBean("GmuserDAO");
	}
}
