package com.fy.engineserver.dajing;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

//这些数据包括：
//
//	面对面交易出工作室以外的银锭的数量
//  面对面交易出工作室以外的银块的数量
//  邮件附件出工作室以外的银块的数量
//  摆摊交易出工作室以外的银锭的数量
//  摆摊交易从工作室外获取的银锭数量
//  快速出售从工作室外获取的银锭数量
//
//
//	面对面交易出工作室以外的银锭的数量
//  面对面交易出工作室以外的银块的数量
//  邮件附件出工作室以外的银块的数量
//  摆摊交易出工作室以外的银锭的数量
//  
//  古董产出银块的数量
//  古董产出非绑定酒的数量
//  古董产出非绑定贴的数量
//  古董产出非绑定宝石的数量
//
//  打怪掉落并拾取的白装数量
//  打怪掉落并拾取的绿装数量
//  打怪掉落并拾取的蓝装数量  
//  打怪掉落并拾取的紫装数量
//  打怪掉落并拾取的白酒数量
//  打怪掉落并拾取的绿酒数量
//  打怪掉落并拾取的白贴数量
//	打怪掉落并拾取的绿贴数量
public class DajingStudioManager{
	static DajingStudioManager self;
	public static DajingStudioManager getInstance(){
		return self;
	} 

	//此类型定义的顺序很重要要，必须和产出类型一一对应，顺序一致
	public static int 打怪掉落并拾取的白酒数量 = 0;
	public static int 打怪掉落并拾取的绿酒数量 = 1;
	public static int 打怪掉落并拾取的白贴数量 = 2;
	public static int 打怪掉落并拾取的绿贴数量 = 3;
	public static int 打怪掉落并拾取的白装数量 = 4;
	public static int 打怪掉落并拾取的绿装数量 = 5;
	public static int 打怪掉落并拾取的蓝装数量   = 6;
	public static int 打怪掉落并拾取的紫装数量 = 7;
	public static int 古董产出银块的数量 = 8;
	public static int 古董产出非绑定酒的数量 = 9;
	public static int 古董产出非绑定贴的数量 = 10;
	public static int 古董产出非绑定宝石的数量 = 11;
	
	public static int 面对面交易出工作室以外的银锭的数量 = 12;
	public static int 面对面交易出工作室以外的银块的数量 = 13;
	public static int 邮件附件出工作室以外的银块的数量 = 14;
	public static int 摆摊交易出工作室以外的银锭的数量 = 15;
	public static int 摆摊交易从工作室外获取的银锭数量 = 16;
	public static int 快速出售从工作室外获取的银锭数量 = 17;
	
	public static String STATDATANAMES[] = new String[]{
		
		"打怪掉落并拾取的白酒数量",
		"打怪掉落并拾取的绿酒数量",
		"打怪掉落并拾取的白贴数量",
		"打怪掉落并拾取的绿贴数量",
		"打怪掉落并拾取的白装数量",
		"打怪掉落并拾取的绿装数量",
		"打怪掉落并拾取的蓝装数量",
		"打怪掉落并拾取的紫装数量",
	
		"古董产出银块的数量",
		"古董产出非绑定酒的数量",
		"古董产出非绑定贴的数量",
		"古董产出非绑定宝石的数量",
		
		"面对面交易出工作室以外的银锭的数量",
		"面对面交易出工作室以外的银块的数量",
		"邮件附件出工作室以外的银块的数量",
		"摆摊交易出工作室以外的银锭的数量",
		"摆摊交易从工作室外获取的银锭数量",
		"快速出售从工作室外获取的银锭数量",
	};
	
	
	public static int 每天打怪掉落白酒限制 = 0; 
	public static int 每天打怪掉落绿酒限制 = 1;
	public static int 每天打怪掉落白帖限制 = 2;
	public static int 每天打怪掉落绿帖限制= 3;
	public static int 每天打怪掉落白装限制= 4;
	public static int 每天打怪掉落绿装限制= 5;
	public static int 每天打怪掉落蓝装限制= 6;
	public static int 每天打怪掉落紫装限制= 7;
	public static int 每天古董产生非绑定银块限制= 8;
	public static int 每天古董产生非绑定酒限制= 9;
	public static int 每天古董产生非绑定帖限制= 10;
	public static int 每天古董产生非绑定宝石限制= 11;
	
	public static String 工作室产出限制名称[] = new String[]{
		"每天打怪掉落白酒限制", 
		"每天打怪掉落绿酒限制",
		"每天打怪掉落白帖限制",
		"每天打怪掉落绿帖限制",
		"每天打怪掉落白装限制",
		"每天打怪掉落绿装限制",
		"每天打怪掉落蓝装限制",
		"每天打怪掉落紫装限制",
		"每天古董产生非绑定银块限制",
		"每天古董产生非绑定酒限制",
		"每天古董产生非绑定帖限制",
		"每天古董产生非绑定宝石限制",
	};
	public static int 打金工作室产出限制表[] = new int[]{
		50,
		10,
		50,
		10,
		500,
		100,
		20,
		4,
		40,
		10,
		10,
		10,
	};
	
	public void notify_打怪掉落白装(Player p){notify_产出(p,每天打怪掉落白装限制,1);}
	public void notify_打怪掉落绿装(Player p){notify_产出(p,每天打怪掉落绿装限制,1);}
	public void notify_打怪掉落蓝装(Player p){notify_产出(p,每天打怪掉落蓝装限制,1);}
	public void notify_打怪掉落紫装(Player p){notify_产出(p,每天打怪掉落紫装限制,1);}
	public void notify_打怪掉落白酒(Player p){notify_产出(p,每天打怪掉落白酒限制,1);}
	public void notify_打怪掉落绿酒(Player p){notify_产出(p,每天打怪掉落绿酒限制,1);}
	public void notify_打怪掉落白帖(Player p){notify_产出(p,每天打怪掉落白帖限制,1);}
	public void notify_打怪掉落绿帖(Player p){notify_产出(p,每天打怪掉落绿帖限制,1);}
	
	public void notify_古董产出银块(Player p,int amount){notify_产出(p,每天古董产生非绑定银块限制,amount);}
	public void notify_古董产出非绑定酒(Player p,int amount){notify_产出(p,每天古董产生非绑定酒限制,amount);}
	public void notify_古董产出非绑定帖(Player p,int amount){notify_产出(p,每天古董产生非绑定帖限制,amount);}
	public void notify_古董产出非绑定宝石(Player p,int amount){notify_产出(p,每天古董产生非绑定宝石限制,amount);}
	
	public void notify_产出(Player p,int type,int amount){
		DajingStudio ds = getDajingStudioByPlayer(p);
		if(ds == null) return ;
		if(type < 0 || type >= 打金工作室产出限制表.length) return ;
		if(ds.canchuData == null || ds.canchuData.length != 打金工作室产出限制表.length){
			ds.canchuData = new long[打金工作室产出限制表.length];
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ds.lastCanChuTime);
		int d1 = cal.get(Calendar.DAY_OF_YEAR);
		cal.setTimeInMillis(System.currentTimeMillis());
		int d2 = cal.get(Calendar.DAY_OF_YEAR);
		if(d1 != d2){
			ds.canchuData = new long[打金工作室产出限制表.length];
		}
		
		ds.canchuData[type]+=amount;
		ds.lastCanChuTime = System.currentTimeMillis();

		
		if(ds.statData == null || ds.statData.length != STATDATANAMES.length){
			ds.statData = new long[STATDATANAMES.length];
		}
		ds.statData[type]+=amount;
	}
	
	//a2b a给b的银子的数量，单位为文
	public void notify_面对面交易银锭(Player a,Player b,long a2bSilver,long b2aSilver){
		DajingStudio da = this.getDajingStudioByPlayer(a);
		DajingStudio db = this.getDajingStudioByPlayer(b);
		if(da != null && da != db && a2bSilver > 0){
			da.statData[面对面交易出工作室以外的银锭的数量]+=a2bSilver;
		}
		if(db != null && db != da && b2aSilver > 0){
			db.statData[面对面交易出工作室以外的银锭的数量]+=b2aSilver;
		}
	}
	//a2b a给b的银块的个数
	public void notify_面对面交易银块(Player a,Player b,int a2bAmount,int b2aAmount){
		DajingStudio da = this.getDajingStudioByPlayer(a);
		DajingStudio db = this.getDajingStudioByPlayer(b);
		if(da != null && da != db && a2bAmount > 0){
			da.statData[面对面交易出工作室以外的银块的数量]+=a2bAmount;
		}
		if(db != null && db != da && b2aAmount > 0){
			db.statData[面对面交易出工作室以外的银块的数量]+=b2aAmount;
		}
	}
	public void notify_邮件附件交易银块(Player sender,Player receiver,int amount){
		DajingStudio da = this.getDajingStudioByPlayer(sender);
		DajingStudio db = this.getDajingStudioByPlayer(receiver);
		if(da != null && da != db && amount > 0){
			da.statData[邮件附件出工作室以外的银块的数量]+=amount;
		}
		
	}
	public void notify_摆摊交易银锭(Player seller,Player buyer,long silver){
		DajingStudio da = this.getDajingStudioByPlayer(seller);
		DajingStudio db = this.getDajingStudioByPlayer(buyer);
		if(da != null && da != db && silver > 0){
			da.statData[摆摊交易从工作室外获取的银锭数量]+=silver;
		}
		if(db != null && db != da && silver > 0){
			db.statData[摆摊交易出工作室以外的银锭的数量]+=silver;
		}
	}
	public void notify_快速出售银锭(Player p,long silver){
		DajingStudio da = this.getDajingStudioByPlayer(p);
		if(da != null && silver > 0){
			da.statData[快速出售从工作室外获取的银锭数量]+=silver;
		}
	}
	
	
	//打金工作室，正在的数据
	public ConcurrentHashMap<String,DajingStudio> dajingMap = new ConcurrentHashMap<String,DajingStudio>();
	
	private DefaultDiskCache dataCache = null;
	//玩家对应打金工作室，取最新的打金工作室。每次修改和加载，需要重新构造这个对应表
	private ConcurrentHashMap<String,DajingStudio> player2DajingMap = new ConcurrentHashMap<String,DajingStudio>();
	
	public boolean initialized = false;
	
	protected String cacheFile;
	
	public void setCacheFile(String s){
		cacheFile = s;
	}
	public String getCacheFile(){
		return cacheFile;
	}
	
	public void init(){
		
		dataCache = new DefaultDiskCache(new File(cacheFile),"打金工作室",365L*24*3600000);
		
		dajingMap = (ConcurrentHashMap<String,DajingStudio>)dataCache.get("打金工作室");
		if(dajingMap == null){
			dajingMap = new ConcurrentHashMap<String,DajingStudio>();
		}
		
		player2Dajing();
		self = this;
		initialized = true;
		ServiceStartRecord.startLog(this);
	}
	
	
	
	public void destroy(){
		dataCache.put("打金工作室", dajingMap);
	}
	
	public void saveForManual(){
		//存盘
		
		player2Dajing();
		
		dataCache.put("打金工作室", dajingMap);
	}
	
	private void player2Dajing(){
		ConcurrentHashMap<String,DajingStudio> map = new ConcurrentHashMap<String,DajingStudio>();
		DajingStudio dds[] = this.getDajingStudios();
		for(int i = 0 ; i < dds.length ; i++){
			for(int j = 0 ; j < dds[i].groupList.size() ; j++){
				for(int k = 0 ; k < dds[i].groupList.get(j).usernameList.size() ; k++){
					String u =  dds[i].groupList.get(j).usernameList.get(k);
					map.put(u,dds[i]);
				}
			}
		}
		player2DajingMap = map;
	}
	
	//这些方法需要在相应的模块中调用，所有方法都是先判断是否为打金工作室成员
	//如果不是，直接返回true。如果是，那么走打金工作室的产出限制。
	//此方法需要和notify_系列方法一起配合使用采用效果。
	public boolean 打怪是否可以掉落白酒(Player p){return 检测是否可以产出(p,每天打怪掉落白酒限制);}
	public boolean 打怪是否可以掉落绿酒(Player p){return 检测是否可以产出(p,每天打怪掉落绿酒限制);}
	public boolean 打怪是否可以掉落白帖(Player p){return 检测是否可以产出(p,每天打怪掉落白帖限制);}
	public boolean 打怪是否可以掉落绿帖(Player p){return 检测是否可以产出(p,每天打怪掉落绿帖限制);}
	public boolean 打怪是否可以掉落白装(Player p){return 检测是否可以产出(p,每天打怪掉落白装限制);}
	public boolean 打怪是否可以掉落绿装(Player p){return 检测是否可以产出(p,每天打怪掉落绿装限制);}
	public boolean 打怪是否可以掉落蓝装(Player p){return 检测是否可以产出(p,每天打怪掉落蓝装限制);}
	public boolean 打怪是否可以掉落紫装(Player p){return 检测是否可以产出(p,每天打怪掉落紫装限制);}
	public boolean 古董是否可以产生非绑定银块(Player p){return 检测是否可以产出(p,每天古董产生非绑定银块限制);}
	public boolean 古董是否可以产生非绑定酒(Player p){return 检测是否可以产出(p,每天古董产生非绑定酒限制);}
	public boolean 古董是否可以产生非绑定帖(Player p){return 检测是否可以产出(p,每天古董产生非绑定帖限制);}
	public boolean 古董是否可以产生非绑定宝石(Player p){return 检测是否可以产出(p,每天古董产生非绑定宝石限制);}
	
	public boolean 检测是否可以产出(Player p,int type){
		DajingStudio ds = getDajingStudioByPlayer(p);
		if(ds == null) return true;
		if(type < 0 || type >= 打金工作室产出限制表.length) return true;
		if(ds.canchuData == null || ds.canchuData.length != 打金工作室产出限制表.length){
			ds.canchuData = new long[打金工作室产出限制表.length];
		}
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ds.lastCanChuTime);
		int d1 = cal.get(Calendar.DAY_OF_YEAR);
		cal.setTimeInMillis(System.currentTimeMillis());
		int d2 = cal.get(Calendar.DAY_OF_YEAR);
		if(d1 != d2){
			ds.canchuData = new long[打金工作室产出限制表.length];
			ds.lastCanChuTime = System.currentTimeMillis();
		}
		
		int maxlimit = 打金工作室产出限制表[type];
		long limit = ds.canchuData[type];
		if(limit >= maxlimit) return false;
		return true;
	}
	
	public DajingStudio getDajingStudio(String ip){
		return dajingMap.get(ip);
	}
	//获取玩家对应的打金工作室，如果玩家在多个工作室中，返回最近激活的那个
	public DajingStudio getDajingStudioByPlayer(Player p){
		return this.player2DajingMap.get(p.getUsername());
	}
	//按时间排序
	public DajingStudio[] getDajingStudios(){
		DajingStudio ds[] = dajingMap.values().toArray(new DajingStudio[0]);
		Arrays.sort(ds,new Comparator<DajingStudio>(){
			public int compare(DajingStudio p1,DajingStudio p2){
				if(p1.createTime.getTime() < p2.createTime.getTime()) return -1;
				if(p1.createTime.getTime() > p2.createTime.getTime()) return 1;
				return p1.ip.compareTo(p2.ip);
			}
		});
		return ds;
	}
}