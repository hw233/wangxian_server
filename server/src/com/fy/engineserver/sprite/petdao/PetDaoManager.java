package com.fy.engineserver.sprite.petdao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.RefreshMonsterPackage;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.pet.Option_Pet_Enter_MiCheng_Sure;
import com.fy.engineserver.message.ENTER_PET_DAO_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.npc.FlopCaijiNpc;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPCManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * 宠物迷城
 * 
 *
 */
public class PetDaoManager{
	
	public static Platform 要开的平台 []= {Platform.官方,Platform.台湾,Platform.腾讯,Platform.韩国};
	
	public static Logger log = LoggerFactory.getLogger(PetDaoManager.class);
	
	private static  PetDaoManager self;
	
	private String filename;
	
	private static final int 迷城类型数量 = 3;
	
	private static final int 迷城基础数据 = 0;
	private static final int 普通迷城箱子配置 = 1;
	private static final int 豪华迷城箱子配置 = 2;
	private static final int 至尊迷城箱子配置 = 3;
	
	private static int [] 箱子配置 = {普通迷城箱子配置,豪华迷城箱子配置,至尊迷城箱子配置};
	public static String [] 不同级别打折券 = {Translate.圣兽阁体验券普,Translate.圣兽阁体验券豪,Translate.圣兽阁体验券至};
	public static int 不同级别npc对应的id[] = {1000001,1000002,1000003};
	
	public static List<PetData> clientneewdata = new ArrayList<PetData>();
	
	public static int [] 不同级别颜色值 = {0xffffffff,0xffE106EA,0xffFDA700};
	public static int [] 等级 = {普通迷城箱子配置,豪华迷城箱子配置,至尊迷城箱子配置};
	public static String [] 等级使用物品描述 = {Translate.锤子,Translate.锤子,Translate.钥匙};
	public static String [] 等级消耗物品描述 = {Translate.罐子,Translate.罐子,Translate.宝箱};
	public static String [] 获得钥匙描述 = {Translate.迷城开启获得锤子提示,Translate.迷城开启获得锤子提示,Translate.迷城开启获得钥匙提示};
	public static String [] 道具不足提示 = {Translate.锤子不足,Translate.锤子不足,Translate.钥匙不足};
	
	/**
	 * 玩家副本进度，目前是一个玩家只有该类型的一个副本进度，如有多副本需求，value改为集合就ok
	 */
	public LinkedHashMap<Long,PetDao> datas;
	
	public DefaultDiskCache ddc;
	public File dataFile;
	
	/**
	 * 根据迷城级别(普通，豪华，至尊)获得对应的地图名
	 */
	public static List<String> mapnames = new ArrayList<String>();
	static{
		mapnames.add("putongshengshouge");
		mapnames.add("haohuashengshouge");
		mapnames.add("zhizunshengshouge");
	}
	
	public static int xypiont[] [] = new int[3][];
	static{
		xypiont[0] = new int[]{1159,1345};
		xypiont[1] = new int[]{2169,1571};
		xypiont[2] = new int[]{1973,798};
	}
	
	public static Map<Integer, Integer[]> 开放的服务器配置 = new HashMap<Integer, Integer[]>();
	
	static{
		开放的服务器配置.put(new Integer(90), new Integer[]{0});
		开放的服务器配置.put(new Integer(135), new Integer[]{0});
		开放的服务器配置.put(new Integer(180), new Integer[]{0});
		开放的服务器配置.put(new Integer(220), new Integer[]{0,1,2});
	}
	
	public static String 限量的钥匙名字 = "钥匙";
	
	public static int 初始给钥匙数 = 15;
	
	public static boolean isstart = true;
	
	public static long 持续时间 = 30*60*1000;
	
	/**
	 * eg:90级的,豪华迷城的,箱子信息
	 */
	public static Map<Integer, Map<Integer, ArrayList<RefreshMonsterPackage>>> packageinfos = new LinkedHashMap<Integer, Map<Integer,ArrayList<RefreshMonsterPackage>>>();
	
	public static int 迷城线程数 = 5;
	
	private PetDao_Thread [] threads = new PetDao_Thread[迷城线程数];
	
	public static int 下一个接受任务的线程;
	
	public static PetDaoManager getInstance(){
		return self;
	}
	
	public void init() throws Exception{
		
		long now = System.currentTimeMillis();
		self = this;
		ddc = new DefaultDiskCache(dataFile, null,"迷城数据", 100L * 365 * 24 * 3600 * 1000L);
		datas = (LinkedHashMap<Long, PetDao>) ddc.get("迷城数据");
		if(datas==null){
			datas = new LinkedHashMap<Long, PetDao>();
		}
		if(PlatformManager.getInstance().isPlatformOf(要开的平台)){
			loadPetData();
			for(int i=0;i<迷城线程数;i++){
				threads[i] = new PetDao_Thread();
				threads[i].start("宠物迷城线程--"+i);
			}
		}
		
		ServiceStartRecord.startLog(this);
	}
	
	public static int 等级索引(int level){
		int index = 0;
		for(int i=0;i<等级.length;i++){
			if(等级[i]==(level+1)){
				index =  i;
			}
		}
		return index;
	}
	
	/**
	 * 当前副本是否开放
	 * @param enterlevel
	 * @param entertype
	 * @return
	 */
	public boolean isOpen(int enterlevel,int entertype){
		try{
			Integer isopen = 开放的服务器配置.get(new Integer(enterlevel))[entertype];
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public void addPetDao(PetDao pd){
		if(下一个接受任务的线程>=迷城线程数){
			下一个接受任务的线程 = 0;
		}
		threads[下一个接受任务的线程].addTask(pd);
		下一个接受任务的线程++;
		log.warn("[宠物迷城] [线程队列] [线程--"+下一个接受任务的线程+"] [任务数量:"+threads[下一个接受任务的线程-1].allTasks.size()+"] ["+pd.getMc().getName()+"] [STAT:"+pd.getMc().getSTAT()+"] [剩余时间："+pd.getMc().getContinuetime()+"] [级别和等级："+pd.getTypelevel()+","+pd.getEnterlevel()+"]");
	}
	
	/**
	 * 加载宠物迷城数据
	 */
	private void loadPetData() throws Exception{
		long now = System.currentTimeMillis();
		
		File f = new File(filename);
		if(!f.exists()){
			throw new Exception("宠物迷城数据文件不存在！");
		}
		
		InputStream is = new FileInputStream(f);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		
		HSSFSheet sheet = workbook.getSheetAt(迷城基础数据);		
		int rows = sheet.getPhysicalNumberOfRows();
		
		for(int i=1;i<rows;i++){
			int index = 0;
			HSSFRow row = sheet.getRow(i);
			if(row!=null){
				PetData [] petdatas = new PetData[迷城类型数量];
				int minlevel = 0;
				try{
					minlevel = StringTool.getCellValue(row.getCell(index++), Integer.class); //Integer.valueOf(StringTool.modifyContent(cells[index++]));
				}catch(Exception e){
					throw new Exception("[宠物迷城数据加载minlevel] [页："+迷城基础数据+"] [行："+i+"] [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
				
				for(int j=0;j<迷城类型数量;j++){
					petdatas[j] = 设置迷城宠物属性(minlevel,i,row,index,j);
					index = index +5;
					clientneewdata.add(petdatas[j]);
				}
			}else{
				throw new Exception("[宠物迷城加载迷城基础数据] [失败] [原因：数据为空!]");
			}
		}
		
		
		/**
		 * 根据迷城等级和迷城级别进入对应的地图，刷新对应的箱子
		 * 开启箱子刷新对应的怪物，打死怪物掉落对应配置的物品
		 */
		int minlevel = 0;
		String boxname = "";
		String icon = "";
		String monsterids = "";
		String folp = "";
		String mappoint = "";
		int keyflop = 0;
		
		for(int j=0;j<箱子配置.length;j++){
			int 迷城级别 = j;
			now = System.currentTimeMillis();
			sheet = workbook.getSheetAt(箱子配置[j]);	
			rows = sheet.getPhysicalNumberOfRows();
			for(int i=1;i<rows;i++){
				int index = 0;
				HSSFRow row = sheet.getRow(i);
				try{
					minlevel =  StringTool.getCellValue(row.getCell(index++), Integer.class);
				}catch(Exception e){
					throw new Exception("[宠物迷城数据加载minlevel] [页："+箱子配置[j]+"] [行："+i+"]  [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
				try{
					boxname = StringTool.getCellValue(row.getCell(index++), String.class);
				}catch(Exception e){
					throw new Exception("[宠物迷城数据加载boxname] [页："+箱子配置[j]+"] [行："+i+"]  [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
				try{
					icon = StringTool.getCellValue(row.getCell(index++), String.class);
				}catch(Exception e){
					throw new Exception("[宠物迷城数据加载icon] [页："+箱子配置[j]+"] [行："+i+"]  [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
				try{
					monsterids = StringTool.getCellValue(row.getCell(index++), String.class);
				}catch(Exception e){
					throw new Exception("[宠物迷城数据加载monsterids] [页："+箱子配置[j]+"] [行："+i+"]  [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
				try{
					folp = StringTool.getCellValue(row.getCell(index++), String.class);
				}catch(Exception e){
					throw new Exception("[宠物迷城数据加载folp] [页："+箱子配置[j]+"] [行："+i+"]  [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
				try{
					mappoint = StringTool.getCellValue(row.getCell(index++), String.class);
				}catch(Exception e){
					throw new Exception("[宠物迷城数据加载mappoint] [页："+箱子配置[j]+"] [行："+i+"]  [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
				try{
					keyflop = StringTool.getCellValue(row.getCell(index++), Integer.class);
				}catch(Exception e){
					throw new Exception("[宠物迷城数据加载keyflop] [页："+箱子配置[j]+"] [行："+i+"]  [单元格："+(index-1)+"] [出错信息："+e+"]");
				}
				
				if(packageinfos.get(minlevel)==null){
					Map<Integer, ArrayList<RefreshMonsterPackage>> map = new LinkedHashMap<Integer, ArrayList<RefreshMonsterPackage>>();
					packageinfos.put(minlevel, map);
				}
				if(packageinfos.get(minlevel).get(迷城级别)==null){
					ArrayList<RefreshMonsterPackage> list = new ArrayList<RefreshMonsterPackage>();
					packageinfos.get(minlevel).put(迷城级别, list);
				}
				String boxnames [] = boxname.split(",");
				
				{
					String xys [] = mappoint.split(";");
					int [][] xypoint = new int[xys.length][];
					for(int jj=0;jj<xys.length;jj++){
						xypoint[jj] = StringToInt(xys[jj].split(","));
					}
					
					int [] ids = StringToInt(monsterids.split(","));
					
					int [] fs = StringToInt(folp.split(","));
					int []fss = new int[fs.length+1];
					for(int k=0;k<fs.length;k++){
						fss[k] = fs[k];
					}
					fss[fs.length] = keyflop;

					if(ids.length!=fs.length){
						throw new Exception("[宠物迷城] [初始化箱子] [出错] [原因：怪物id和怪物掉率长度不匹配] [怪物id长度："+ids.length+"] [掉率长度："+fs.length+"]");
					}
					
					if(boxnames.length!=xys.length){
						throw new Exception("[宠物迷城] [初始化箱子] [出错] [原因：箱子和箱子出生点长度不匹配] [箱子长度："+boxnames.length+"] [箱子出生点长度："+xys.length+"]");
					}
					
					for(int ii=0;ii<boxnames.length;ii++){
						RefreshMonsterPackage p = new RefreshMonsterPackage(ids, fss);
						p.setXy(xypoint[ii]);
						if(boxnames[ii]!=null){
							设置物品一般属性(p,boxnames[ii],icon);
							packageinfos.get(minlevel).get(迷城级别).add(p);
						}
					}
				}
				
			}
			Game.logger.warn("[宠物迷城] [箱子信息] [级别："+迷城级别+"] [加载完成] [加载完成rows:"+rows+"] [耗时："+(System.currentTimeMillis()-now)+"]");
		}
		
	}
	
	private int[] StringToInt(String[] a){
		int [] fs = new int[a.length];
		for(int k=0;k<a.length;k++){
			fs[k] = Integer.parseInt(a[k]);
		}
		return fs;
	}
	
	public void 设置物品一般属性(Article a,String name,String icon){
		a.setName(name);
		a.setIconId(icon);
	}
	
	public PetData 设置迷城宠物属性(int minlevel,int i,HSSFRow row,int index,int type) throws Exception{
		PetData data = new PetData();
		long costsilver = 0;
		String desc = "";
		String petname = "";
		String petnames = "";
		String peticon = "";
		
		try{
			costsilver = StringTool.getCellValue(row.getCell(index++), Long.class);
		}catch(Exception e){
			throw new Exception("[宠物迷城数据加载costsilver] [行："+i+"] [页："+箱子配置[type]+"] [单元格："+(index-1)+"] [出错信息："+e+"]");
		}
		try{
			desc = StringTool.getCellValue(row.getCell(index++), String.class);
		}catch(Exception e){
			throw new Exception("[宠物迷城数据加载desc] [行："+i+"] [页："+箱子配置[type]+"] [单元格："+(index-1)+"] [出错信息："+e+"]");
		}
		try{
			petname = StringTool.getCellValue(row.getCell(index++), String.class);
		}catch(Exception e){
			throw new Exception("[宠物迷城数据加载petname] [行："+i+"] [页："+箱子配置[type]+"] [单元格："+(index-1)+"] [出错信息："+e+"]");
		}
		try{
			petnames = StringTool.getCellValue(row.getCell(index++), String.class);
		}catch(Exception e){
			throw new Exception("[宠物迷城数据加载petnames] [行："+i+"] [页："+箱子配置[type]+"] [单元格："+(index-1)+"] [出错信息："+e+"]");
		}
		try{
			peticon = StringTool.getCellValue(row.getCell(index++), String.class);
		}catch(Exception e){
			throw new Exception("[宠物迷城数据加载peticon] [行："+i+"] [页："+箱子配置[type]+"] [单元格："+(index-1)+"] [出错信息："+e+"]");
		}
		data.setLevel(minlevel);
		data.setCostsilver(costsilver);
		data.setDesc(desc);
		String oldnames [] = petname.split(";");
		data.setPetnames(petnames.split(";"));
		String names [] = new String[oldnames.length];
		for(int j=0;j< oldnames.length;j++){
			names[j] = "<f size='28' color='"+不同级别颜色值[type]+"'>"+oldnames[j]+"</f>";
		}
		data.setPetname(names);
		data.setDaotype(type);
		data.setIconname(peticon.split(","));
		return data;
	}
	
	/**
	 * 进入宠物迷城
	 * @return
	 */
	public void handleEnterPetDao(Player player,ENTER_PET_DAO_REQ req){
		int michenglevel = req.getMinlevel();
		int michengtype = req.getDaotype();
		
		if(!isOpen(michenglevel,michengtype)){
			player.sendError(Translate.暂未开放);
			return;
		}
		long 进入迷城花费 = getSilverEnter(michenglevel,michengtype);
		
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		String typestr = "";
		if(michengtype==0){
			typestr = Translate.普通迷城;
		}else if(michengtype==1){
			typestr = Translate.豪华迷城;
		}else if(michengtype==2){
			typestr = Translate.至尊迷城;
		}
		String mes=Translate.免费进入提示;
		String costname =  PetDaoManager.getInstance().是否有迷城体验卷(player,michengtype);
		if(costname!=null && costname.trim().length()>0){
			mes = Translate.translateString(Translate.进入迷城消耗券提示, new String[][] {{Translate.STRING_1, costname}});
		}else{
			mes = Translate.translateString(Translate.进入迷城提示, new String[][] {{Translate.STRING_1, 进入迷城花费+""}, {Translate.STRING_2, michenglevel+""} , {Translate.STRING_3, typestr}});
		}
		mw.setDescriptionInUUB(mes);
		Option_Pet_Enter_MiCheng_Sure option = new Option_Pet_Enter_MiCheng_Sure();
		option.setReq(req);
		option.setText(Translate.确定);
		Option_Cancel cancle = new Option_Cancel();
		cancle.setText(Translate.取消);
		mw.setOptions(new Option[] { option, cancle });
		QUERY_WINDOW_RES res1 = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(res1);
		
	}
	
	/**
	 * 刷箱子
	 * @param michenglevel
	 * @param michengtype
	 */
	public boolean refreshBox(ArrayList<RefreshMonsterPackage> boxminfo,Game game,Player player,int typelevel){
		boolean issucc = false;
		try{
			for(int i=0;i<boxminfo.size();i++){
				RefreshMonsterPackage a = boxminfo.get(i);
				if(a!=null){
					if(a.getName()==null || a.getName().equals("")){
						if(a.getBoxname()!=null && a.getBoxname().length>0){
							a.setName(a.getBoxname()[0]);
						}else{
							a.setName(Translate.罐子);
						}
					}
					FlopCaijiNpc npc = getFlopCaiJiNpc(a,player,typelevel);
					if(npc!=null){
						npc.setX(a.getXy()[0]);
						npc.setY(a.getXy()[1]);
						game.addSprite(npc);
						issucc = true;
						PetDaoManager.log.warn("[宠物迷城] [刷箱子] [ok] [boxminfo:"+boxminfo.size()+"] ["+npc.getName()+"] ["+game.gi.name+"] ");
					}
				}
			}
		}catch(Exception e){
			PetDaoManager.log.warn("[宠物迷城] [刷箱子] [异常] ",e);
			issucc = false;
		}
		return issucc;
	}
	
	/**
	 * 刷怪物
	 * @param player
	 * @param mapname
	 */
	public void refreshMonster(Game game,PetDao pDao){
		try{
			if(pDao!=null){
				Sprite sprite = null;
				MonsterData overmonsters = pDao.getOvermonsters();
				if(overmonsters!=null && overmonsters.getMonsterids().size()>0){
					for(int i=0;i<overmonsters.getMonsterids().size();i++){
						int id = overmonsters.getMonsterids().get(i);
						sprite = MemoryMonsterManager.getMonsterManager().createMonster(id);
						if (sprite != null) {
							sprite.setX(overmonsters.getXys().get(i)[0]);
							sprite.setY(overmonsters.getXys().get(i)[1]);
							game.addSprite(sprite);
						}	
					}
				}
			}
		}catch (Exception e) {
			PetDaoManager.log.warn("[宠物迷城] [刷怪物] [异常] ",e);
		}
	}
	
	
	public void 进入千层塔测试(Player player,String mapname){
		Game game = null;
		try {
			GameManager gm = GameManager.getInstance();
			GameInfo gi = gm.getGameInfo(mapname);
			game = new Game(gm,gi);
			game.init();
			MapArea area = game.gi.getMapAreaByName(Translate.千层塔);
			int bornX = 300;
			int bornY = 300;
			if (area != null) {
				Random random = new Random();
				bornX = area.getX()+random.nextInt(area.getWidth());
				bornY = area.getY() + random.nextInt(area.getHeight());
			}
			TransportData transportData = new TransportData(0,0,0,0,mapname,bornX,bornY);
			
			//提示玩家跳地图，进入塔
			player.getCurrentGame().transferGame(player, transportData);
		} catch (Exception e) {
			game = null;
			if(log.isWarnEnabled())
				log.warn("[宠物迷城] [申请进入迷城] [千层塔测试] [mapname:"+mapname+"] [" + player.getLogString()+"]",e);
		}
	}
	
	private FlopCaijiNpc getFlopCaiJiNpc(Article article,Player p,int typelevel){
		ArticleEntity ae = null;
		try {
			ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.宠物迷城, null, article.getColorType(), 1, true);
			NPCManager nm = MemoryNPCManager.getNPCManager();
//			NPC npc =  nm.getNPC(不同级别npc对应的id[typelevel]);
			FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(不同级别npc对应的id[typelevel]);
//			FlopCaijiNpc fcn  = (FlopCaijiNpc) npc;
			fcn.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			fcn.setAllCanPickAfterTime(1000);
			fcn.setEndTime(SystemTime.currentTimeMillis() + 35*60*1000);
			ResourceManager.getInstance().getAvata(fcn);
			List<Long> list = new ArrayList<Long>();
			list.add(p.getId());
			fcn.setPlayersList(list);
			fcn.setArticle(article);
			fcn.setAe(ae);
			//测试
//			fcn.setAvataSex("qingyishusheng");
//			fcn.setAvataRace("NPC");
			fcn.setCount(1);
			fcn.setName(article.getName());
			return fcn;
		} catch (Exception e) {
			PetDaoManager.log.warn("[宠物迷城] [异常] [id:"+不同级别npc对应的id[typelevel]+"] [typelevel:"+typelevel+"] ["+p.getLogString()+"]",e);
		}
		return null;
	}
	
	/**
	 * 根据迷城等级和该等级所在的级别
	 * 获得进入迷城所需银子
	 */
	public long getSilverEnter(int michenglevel,int michengtype){
		for(PetData data : clientneewdata){
			if(data.getLevel()==michenglevel && data.getDaotype()==michengtype){
				return data.getCostsilver();
			}
		}
		return 0;
	}
	
	public String 是否有迷城体验卷(Player p,int type){
		String articlename = 不同级别打折券[type];
		int count = p.getKnapsack_common().countArticle(articlename);
		if(count > 0){
			return articlename;	
		}
		return "";
	}
	
	/**
	 *·	始终创建一个新的迷城
	 * @param mapname
	 * @return
	 */
	public PetDao createNewGame(Player player , String mapname,int michenglevel,int michengtype){
		try {
			MiChengSpeed mcChengSpeed = createJinUd(player,mapname);
			PetDao pd = new PetDao(mcChengSpeed,null);
			pd.setEnterlevel(michenglevel);
			pd.setTypelevel(michengtype);
			log.warn("[创建迷城副本] [成功] ["+player.getName()+"] [等级："+pd.getEnterlevel()+"] [级别："+pd.getTypelevel()+"] [进度总数据："+datas.size()+"]");
			return pd;
		} catch (Exception e) {
			if(log.isWarnEnabled()){
				log.warn("[创建迷城副本] [异常] [mapname:"+mapname+"] ["+player.getLogString()+"]",e);
			}
		}
		return null;
	} 

	/**
	 * 如果副本存在进度，根据进去得到副本
	 * @param player
	 * @param mapname
	 * @return
	 */
	public PetDao getPetDao(MiChengSpeed speed){
		Player player = speed.getP();
		String gamename = speed.getName();
		if(player!=null && gamename!=null){
			Game newGame = createGame(player,gamename);
			if(newGame!=null){
				PetDao pd = new PetDao(speed, newGame);
				return pd;
			}
		}
		return null;
	}
	
	public Game createGame(Player player,String mapname){
		GameManager gameManager = GameManager.getInstance();
		GameInfo gi = gameManager.getGameInfo(mapname);
		if(gi == null){
			if(log.isWarnEnabled())
				log.warn("[创建迷城副本] [失败] [对应的地图信息不存在][玩家:{}][{}][{}]", new Object[]{player.getName(),player.getId(),mapname});
			return null;
		}
		Game newGame = new Game(gameManager,gi);
		try {
			newGame.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newGame;
	}
	
	public MiChengSpeed createJinUd(Player player , String mapname){
		MiChengSpeed sp = new MiChengSpeed();
		sp.setName(mapname);
		sp.setP(player);
		sp.setPid(player.getId());
		sp.setContinuetime(持续时间);
		sp.setEndtime(System.currentTimeMillis()+持续时间);
		sp.setKeyovernum(PetDaoManager.初始给钥匙数);
		return sp;
	}
	
	/**
	 * 获得有效的副本
	 * @param p
	 * @param mapname
	 * @return
	 */
	public PetDao getPetDao(Player p,String mapname){
		try{
			PetDao pd = datas.get(p.getId());
			if(pd!=null){
				if(pd.getMc().getName().equals(mapname) && pd.getMc().getContinuetime()>0){
//					log.warn("[获得副本] [OK] ["+pd.getMc().getName()+"] [级别和等级："+pd.getEnterlevel()+","+pd.getTypelevel()+"] [地图:"+mapname+"] [剩余时间："+(pd.getMc().getContinuetime()/1000)+"秒] ["+p.getName()+"]");
					return pd;
				}
			}
		}catch(Exception e){
			log.warn("[获得副本] [异常] [mapname:"+mapname+"] [玩家："+p.getName()+"] ["+e+"]");
		}
		
		return null;
	}
	
	public boolean isPetDao(String mapname){
		if(mapnames.contains(mapname)){
			return true;
		}
		return false;
	}
	
	public void savePetDao(Player player,PetDao pd){
		try{
			if(pd!=null){
				pd.openingnum = 0;
				PetDaoManager.getInstance().datas.put(player.getId(), pd);
				PetDaoManager.getInstance().ddc.put("迷城数据", PetDaoManager.getInstance().datas);
				log.warn("[迷城数据保存] [成功] [玩家："+player.getName()+"] ["+pd.getMc().getName()+"] [级别和等级："+pd.getEnterlevel()+","+pd.getTypelevel()+"] [剩余时间："+pd.getMc().getContinuetime()+"]");
			}
		}catch(Exception e){
			log.warn("[迷城数据保存] [异常] [玩家："+player.getLogString()+"]",e);
		}
	}
	
	public void destroy() {
		try{
//			Iterator<PetDao> it = datas.values().iterator();
//			while(it.hasNext()){
//				PetDao pd = (PetDao)it.next();
//				if(pd!=null){
//					pd.openingnum = 0;
//					System.out.println("[宠物迷城destroy] [数据保存]"+pd.getMc().getP().getName()+"--"+pd.getEnterlevel()+"--"+pd.getTypelevel()+"--"+"--剩余钥匙："+pd.getMc().getKeyovernum()+"--剩余箱子："+pd.getMc().getBoxovernum()+"--已开出钥匙："+pd.getMc().getKeyrefreshnum()+"--STAT:"+pd.getMc().STAT);
//				}
//			}
			ddc.put("迷城数据", datas);
			System.out.println(" [宠物迷城destroy] [destroy1] [ok] [datas:"+datas.size()+"]");
		}catch(Exception e){
			System.out.println("===================宠物迷城destroy error============="+(datas!=null?datas.size():"0")+"========="+e);
		}
	} 
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	
}
