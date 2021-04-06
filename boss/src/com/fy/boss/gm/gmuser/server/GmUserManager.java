package com.fy.boss.gm.gmuser.server;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fy.boss.gm.gmuser.Authority;
import com.fy.boss.gm.gmuser.Gmuser;
import com.fy.boss.gm.gmuser.dao.AuthorityDAO;
import com.fy.boss.gm.gmuser.dao.GmuserDAO;
import com.fy.boss.gm.gmuser.server.GmUserManager;
import com.xuanzhi.tools.cache.LruMapCache;

public class GmUserManager {
protected static GmUserManager m_self = null;
    
	protected static final Log log = LogFactory.getLog(GmUserManager.class.getName());
	    
    protected GmuserDAO  gmuserDao;
    protected AuthorityDAO authorityDao;
    protected static LruMapCache mCache; 
    private Set<String> gmnames=new HashSet<String>();
   public static GmUserManager getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
		long now = System.currentTimeMillis();
		mCache = new LruMapCache();
		m_self = this;
		List<Gmuser> users = gmuserDao.findAll();
		for(Gmuser gu :users)
			gmnames.add(gu.getGmname());
		
		System.out.println("["+GmUserManager.class.getName()+"] [initialized]["+(System.currentTimeMillis()-now)+"]");
		log.info("["+GmUserManager.class.getName()+"] [initialized]["+(System.currentTimeMillis()-now)+"]");
	}
    public Gmuser creatGmuser(Gmuser guser){
    	//增加新的gm账户
    	try {
			if(getGmuser(guser.getUsername()) != null) {
				log.warn("[创建gm账户] [gm账户名称已存在] ["+guser.getUsername()+"]");
				return null;
			}
			gmnames.add(guser.getGmname());
			gmuserDao.save(guser);
			//System.out.println(guser.getId()+"at gmusermanager");
			authorityDao.addList(guser.getAuthos(), guser.getId());
			log.info("gmuser"+guser.getUsername()+" added at "+new java.util.Date());
			mCache.__put(guser.getId(), guser);
			mCache.__put(guser.getUsername(), guser);
			return guser;
		} catch (Exception e) {
			log.info("gmuser ["+guser.getUsername()+"] create fail for "+e.getMessage());
			return null;
		}
    } 
    
    public void deleteGmuser(int id) {  
    	//删除gm账户
    	Gmuser gmuser=null;
		try {
			 gmuser = getGmuser(id);
			if(gmuser != null) {
				gmnames.remove(gmuser.getGmname());
				mCache.remove(gmuser.getGmname());
				gmuserDao.delete(gmuser);
			    mCache.remove(id);
				authorityDao.deleteAll(gmuser.getAuthos());
				log.info("gmuser"+gmuser.getUsername()+" deleted at "+new java.util.Date());
				mCache.remove(gmuser.getId());
			}
		} catch (Exception e) {
			log.info("gmuser delete by id["+id+"]  fail for "+e.getMessage());
			e.printStackTrace();
		}
	}
    public void updateGmuser(Gmuser gmuser){
    	try {
			gmnames.add(gmuser.getGmname());
			authorityDao.update(gmuser.getAuthos(), gmuser.getId());
			gmuserDao.attachDirty(gmuser);
			mCache.__put(gmuser.getId(), gmuser);
			mCache.__put(gmuser.getUsername(), gmuser);
			log.info("gmuser "+gmuser.getUsername()+" update at "+new java.util.Date());
		} catch (Exception e) {
			log.info("gmuser update ["+gmuser.getUsername()+"]  fail for "+e.getMessage());
			e.printStackTrace();
		}
    }
    
   public List<Gmuser> getAllUser(){
	   try {
		List<Gmuser> users =null;
		   users = gmuserDao.findAll();
		   for(Gmuser user:users){
			   user.setAuthos(findByUserId(user.getId()));
		   }
		   return users;
	} catch (Exception e) {
		log.info("gmuser list get fail for "+e.getMessage());
		return null;
	}
   }
    
    
    public Gmuser getGmuser(int id) {
    	//根据id查询用户
    	try {
			Gmuser gmuser=null;
			if(mCache.__get(id) != null) {
				gmuser = (Gmuser)mCache.__get(id);
			}
			 gmuser = gmuserDao.findById(id);
			gmuser.setAuthos(findByUserId(id));
			if(gmuser != null) {
				mCache.__put(gmuser.getId(), gmuser);
				mCache.__put(gmuser.getUsername(), gmuser);
				log.info("[获得gm账户] [SUCC] ["+id+"] ["+gmuser.getUsername()+"]");
			} else {
				log.info("[获得gm账户] [FAILED] [gm账户没找到] ["+id+"]");
			}
			return gmuser;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.info("gmuser get by ["+id+"]  fail for "+e.getMessage());
			return null;
		}
	}
    public boolean login(String username,String passport){
    	boolean result =false;
    	try {
			Gmuser gmuser = getGmuser(username);
			if(passport.equals(gmuser.getPassword()))
				result = true;
		} catch (Exception e) {
			log.info(username+"登录失败  for"+e.getMessage());
			return false;
		}
        return result;
    }
	public Gmuser getGmuser(String username) {
		//根据用户名查询用户 
		try {
			if(mCache.__get(username) != null) {
				Gmuser gmuser = (Gmuser)mCache.__get(username);
				gmuser.setAuthos(findByUserId(gmuser.getId()));
				log.info("[获得gm账户] [SUCC] ["+username+"] ["+gmuser.getId()+"]");
				return gmuser;
			}
			List list = gmuserDao.findByUsername(username);
			if(list.size() > 0) {
				Gmuser gmuser = (Gmuser)list.get(0);
				gmuser.setAuthos(findByUserId(gmuser.getId()));
				mCache.__put(gmuser.getId(), gmuser);
				mCache.__put(gmuser.getUsername(), gmuser);
				log.info("[获得gm账户] [SUCC] ["+username+"] ["+gmuser.getId()+"]");
				return gmuser;
			} else {
				log.info("[获得gm账户] [FAILED] [gm账户没找到] ["+username+"]");
			}
			return null;
		} catch (Exception e) {
			log.info("gmuser getby ["+username+"] get fail for "+e.getMessage());
			return null;
		}
	}
	public List<Authority> findByUserId(int id){
		try {
			List<Authority> authorities =null;
			authorities = authorityDao.findByProperty("userid", id);
			return authorities;
		} catch (Exception e) {
			log.info("gmuser get by["+id+"]  fail for "+e.getMessage());
			return null;
		}		
	}
		
	public GmuserDAO getGmuserDao() {
		return gmuserDao;
	}

	public void setGmuserDao(GmuserDAO gmuserDao) {
		this.gmuserDao = gmuserDao;
	}

	public AuthorityDAO getAuthorityDao() {
		return authorityDao;
	}

	public void setAuthorityDao(AuthorityDAO authorityDao) {
		this.authorityDao = authorityDao;
	}

	public Set<String> getGmnames() {
		return gmnames;
	}

	public void setGmnames(Set<String> gmnames) {
		this.gmnames = gmnames;
	}
    
	
	
}
