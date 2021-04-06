package com.fy.engineserver.sprite;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;



public class PlayerSimpleInfoManager{
	
	public static class SimplePlayerInfoWrapper implements Cacheable{
		ISimplePlayerInfo info;
		public int getSize(){return 0;}
	}

	public static Logger logger = LoggerFactory.getLogger(SocialManager.class);
	public static SimpleEntityManager<Player> em;
	
	private LruMapCache cache;

	private static PlayerSimpleInfoManager self = null;
	public static PlayerSimpleInfoManager getInstance(){
		return self;
	}
	
	public void init(){
		
		em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		cache = new LruMapCache(102400,1800000L,"PlayerSimpleInfo-Cache");
		
		self = this;
		ServiceStartRecord.startLog(this);
	}

	
	/**
	 * 根据一组ID得到对应的PlayerSimpleInfo，返回以ID为key的hashtable
	 * 这个方法注意，如果在启动服务的时候用，jiazuName可能没有
	 * @param ids
	 * @return
	 */
	public Hashtable<Long, PlayerSimpleInfo> getInfoByIds(ArrayList<Long> ids) {
		List<ISimplePlayerInfo> list = null;
		Hashtable<Long, PlayerSimpleInfo> re = new Hashtable<Long, PlayerSimpleInfo>();
		ArrayList<Long> noCacheList = new ArrayList<Long>();
		for (int i = 0; i < ids.size(); i++) {
			SimplePlayerInfoWrapper w = (SimplePlayerInfoWrapper)cache.get(ids.get(i));
			if (w != null) {
				ISimplePlayerInfo I = w.info;
				
				re.put(ids.get(i), creatPlayerSimpleInfo(I));
			}else {
				noCacheList.add(ids.get(i));
			}
		}
		if (noCacheList.size() > 0) {
			long[] noCache = new long[noCacheList.size()];
			for (int i = 0; i < noCacheList.size(); i++){
				noCache[i] = noCacheList.get(i);
			}
			
			try {
				list = em.queryFields(ISimplePlayerInfo.class, noCache);
			} catch (Exception e) {
				logger.error("[查询玩家简单属性异常] [id:"+ids.size()+"]",e);
			}
			if(list != null && list.size() > 0){
				for (int i = 0; i < list.size(); i++) {
					ISimplePlayerInfo I = list.get(i);
					if (I != null) {
						SimplePlayerInfoWrapper w = new SimplePlayerInfoWrapper();
						w.info = I;
						cache.put(I.getId(), w);
						
						re.put(I.getId(), creatPlayerSimpleInfo(I));
					}
				}
			}
		}
		
		return re;
	}
	
	/**
	 * 根据id得到一个简单属性
	 * @param id
	 * @return
	 */
	public PlayerSimpleInfo getInfoById(long id){
		
		try{
			SimplePlayerInfoWrapper w = (SimplePlayerInfoWrapper)cache.get(id);
			if(w == null){
				List<ISimplePlayerInfo> list = null;
				try {
					list = em.queryFields(ISimplePlayerInfo.class, new long[]{id});
				} catch (Exception e) {
					logger.error("[查询玩家简单属性异常] [id:"+id+"]",e);
				}
				if(list != null && list.size() > 0){
					ISimplePlayerInfo I = list.get(0);
					w = new SimplePlayerInfoWrapper();
					w.info = I;
					cache.put(id, w);
				}
			}
			
			if(w != null){
				ISimplePlayerInfo I = w.info;
				return creatPlayerSimpleInfo(I);
			}else{
				if (logger.isInfoEnabled()) {
					logger.info("[查询玩家简单属性错误] [id:"+id+"] ");
				}
			}
		}catch(Exception e){
			SocialManager.logger.error("[取玩家的简单信息异常] [玩家id:"+id+"]",e);
		}
		return null;
	}
	
	public PlayerSimpleInfo creatPlayerSimpleInfo(ISimplePlayerInfo I) {
		PlayerSimpleInfo info = new PlayerSimpleInfo();
		info.setId(I.getId());
//		info.setCareer((byte)1);
		info.setCareer(I.getMainCareer());
		info.setName(I.getName());
		info.setBrithDay(I.getBrithDay());
		info.setStar(I.getStar());
		info.setIcon(I.getIcon());
		info.setAge(I.getAge());
		info.setCountry(I.getCountry());
		info.setProvince(I.getProvince());
		info.setCity(I.getCity());
		info.setLoving(I.getLoving());
		info.setPersonShow(I.getPersonShow());
		info.setMood(I.getMood());
		info.setSeeState(I.getSeeState());
		info.setPlayerCountry(I.getPlayerCountry());
		info.setJiazuId(I.getJiazuId());
		info.setJiazuName("");
		info.setClassLevel(I.getClassLevel());
		info.setLevel(I.getSoulLevel());
		info.setSex(I.getSex());
		info.setCaveId(I.getCaveId());
		info.setDurationOnline(I.getDurationOnline());
		info.setUsername(I.getUsername());
		
		if(info.getJiazuId() > 0 && JiazuManager.getInstance() != null){
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(info.getJiazuId());
			if(jiazu != null){
				info.setJiazuName(jiazu.getName());
				long zpId = jiazu.getZongPaiId();
				if(zpId > 0){
					ZongPai zp = ZongPaiManager.getInstance().getZongPaiById(zpId);
					info.setZongPaiName(zp.getZpname());
				}
			}
		}
		
		if(logger.isInfoEnabled()){
			logger.info("[查询玩家简单属性成功] [id:"+I.getId()+"]");
		}
		return info;
	}
}
