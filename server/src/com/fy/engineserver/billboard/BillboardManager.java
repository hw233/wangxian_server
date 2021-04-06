package com.fy.engineserver.billboard;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.billboard.concrete.BattleFieldBillboards;
import com.fy.engineserver.billboard.concrete.DungeonBillboards;
import com.fy.engineserver.billboard.concrete.EquipmentBillboards;
import com.fy.engineserver.billboard.concrete.ExpBillboards;
import com.fy.engineserver.billboard.concrete.GangBillboards;
import com.fy.engineserver.billboard.concrete.IncomeFromAuBillboards;
import com.fy.engineserver.billboard.concrete.KillingBillboards;
import com.fy.engineserver.billboard.concrete.OnlineTimeBillboards;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.DungeonsRecordManager;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.dbpool.DataSourceManager;

public class BillboardManager {
	
	public static final int MAX_LINES=200;
	
	Billboards[] billboards;
	
	BillboardMaker maker;
	
//	public static Logger log=Logger.getLogger("Billboard");
public	static Logger log = LoggerFactory.getLogger("Billboard");
	
	public static final int REQ_LINES=20;
	
//	public final static String[] BILLBOARDS_NAMES={"等级榜","帮派榜","杀人榜","战场榜","江湖榜","富商榜","装备榜","副本榜"};
	public final static String[] BILLBOARDS_NAMES={Translate.text_2309,Translate.text_2310,Translate.text_2311,Translate.text_2312,Translate.text_2313,Translate.text_2314,Translate.text_2315,Translate.text_2316};
	
//	final static byte[] BILLBOARDS_TYPES={BillboardType.TYPE_PLAYER,BillboardType.TYPE_GANG,BillboardType.TYPE_PLAYER,BillboardType.TYPE_PLAYER,BillboardType.TYPE_PLAYER,BillboardType.TYPE_PLAYER,BillboardType.TYPE_ITEM,BillboardType.TYPE_PLAYER};
	final static byte[] BILLBOARDS_TYPES={BillboardType.TYPE_PLAYER,BillboardType.TYPE_GANG,BillboardType.TYPE_ITEM,BillboardType.TYPE_PLAYER,BillboardType.TYPE_PLAYER,BillboardType.TYPE_PLAYER,BillboardType.TYPE_PLAYER,BillboardType.TYPE_PLAYER};
	
	final static byte KINDS_EXP=0;
	
	final static byte KINDS_GANG=1;
	
	final static byte KINDS_EQUIPMENT=2;
	
	final static byte KINDS_BATTLE_FIELD=3;
	
	final static byte KINDS_DUNGEON=4;
	
	final static byte KINDS_KILLING=5;
	
	final static byte KINDS_ONLINE_TIME=6;
	
	final static byte KINDS_INCOME_FROM_AU=7;
	
	protected DataSourceManager dataSourceManager;
	
	PlayerManager pm;
	

	
//	public HashMap<String, String> playerNames;
//	
//	public HashMap<String, String> gangNames;
	
	static BillboardManager self;
	
	DungeonsRecordManager drm;
	
	public BillboardManager(){
		BillboardManager.self=this;
	}
	
	public void init() {
		long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Connection con = null;
//		this.playerNames=new HashMap<String, String>();
//		this.gangNames=new HashMap<String, String>();
		try {
			this.billboards = this.maker.makeBillboards();
			if (this.billboards == null||this.billboards.length==0) {
				this.billboards = new Billboards[BillboardManager.BILLBOARDS_NAMES.length];
				for (int i = 0; i < this.billboards.length; i++) {
					switch (i) {
					case BillboardManager.KINDS_EXP:
						this.billboards[i] = new ExpBillboards(
								BillboardManager.BILLBOARDS_NAMES[i],
								BillboardManager.BILLBOARDS_TYPES[i]);
						break;

					case BillboardManager.KINDS_GANG:
						this.billboards[i] = new GangBillboards(
								BillboardManager.BILLBOARDS_NAMES[i],
								BillboardManager.BILLBOARDS_TYPES[i]);
						break;

					case BillboardManager.KINDS_KILLING:
						this.billboards[i] = new KillingBillboards(
								BillboardManager.BILLBOARDS_NAMES[i],
								BillboardManager.BILLBOARDS_TYPES[i]);
						break;

					case BillboardManager.KINDS_BATTLE_FIELD:
						this.billboards[i] = new BattleFieldBillboards(
								BillboardManager.BILLBOARDS_NAMES[i],
								BillboardManager.BILLBOARDS_TYPES[i]);
						break;

					case BillboardManager.KINDS_ONLINE_TIME:
						this.billboards[i] = new OnlineTimeBillboards(
								BillboardManager.BILLBOARDS_NAMES[i],
								BillboardManager.BILLBOARDS_TYPES[i]);
						break;
						
					case BillboardManager.KINDS_INCOME_FROM_AU:
						this.billboards[i] = new IncomeFromAuBillboards(
								BillboardManager.BILLBOARDS_NAMES[i],
								BillboardManager.BILLBOARDS_TYPES[i]);
						break;
						
					case BillboardManager.KINDS_EQUIPMENT:
						this.billboards[i] = new EquipmentBillboards(
								BillboardManager.BILLBOARDS_NAMES[i],
								BillboardManager.BILLBOARDS_TYPES[i]);
						break;
						
					case BillboardManager.KINDS_DUNGEON:
						this.billboards[i] = new DungeonBillboards(
								BillboardManager.BILLBOARDS_NAMES[i],
								BillboardManager.BILLBOARDS_TYPES[i]);
						break;
					}
					con = this.getConnection();
					this.billboards[i].update(con);
				}
				this.maker.outputBillboard(this.billboards);
			}
			EquipmentBillboards eb= (EquipmentBillboards)this.getBillboards(Translate.text_2311);
			if(eb!=null){
				eb.loadFromXml();
			}else{
				if(log.isWarnEnabled())
					log.warn("[初始化排行榜] [装备排行榜不存在]");
			}
			if(log.isInfoEnabled()){
//				log.info("[初始化排行榜] [成功] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
				if(log.isInfoEnabled())
					log.info("[初始化排行榜] [成功] [耗时：{}]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)});
			}
			System.out.println("[系统初始化] [排行榜管理器] [初始化完成] [com.fy.engineserver.billboard.BillboardManager] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
		} catch (Exception e) {
			e.printStackTrace();
			if(log.isWarnEnabled())
				log.warn("[初始化排行榜] [失败] [发生错误] [错误："+e+"]",e);
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setDataSourceManager(DataSourceManager dataSourceManager) {
		this.dataSourceManager = dataSourceManager;
	}

	public BillboardData[] getBillboardData(String menu,String submenu){
		Billboards bbs=this.getBillboards(menu);
		if(bbs!=null){
			Billboard bb=bbs.getBillboard(submenu);
			if(bb!=null){
				BillboardData[] data=bb.getBillboardData(BillboardManager.REQ_LINES);
				if(bbs.getName().equals(Translate.text_2311)){
					for(int i=0;i<bbs.getSubmenu().length;i++){
						if(bbs.getSubmenu()[i].equals(submenu)){
							EquipmentBillboards eb=(EquipmentBillboards)bbs;
							data= new BillboardData[eb.al[i].size()];
							for(int j=0;j<data.length;j++){
								data[j]=eb.al[i].get(j);
							}
						}
					}
				}
				if(data!=null){
					if(log.isInfoEnabled()){
//						log.info("[获取排行榜数据] [成功] [菜单："+menu+"] [子菜单："+submenu+"]");
						if(log.isInfoEnabled())
							log.info("[获取排行榜数据] [成功] [菜单：{}] [子菜单：{}]", new Object[]{menu,submenu});
					}
				}else{
//					log.warn("[获取排行榜数据] [失败] [没有数据] [菜单："+menu+"] [子菜单："+submenu+"]");
					if(log.isWarnEnabled())
						log.warn("[获取排行榜数据] [失败] [没有数据] [菜单：{}] [子菜单：{}]", new Object[]{menu,submenu});
				}
				return data;
			}else{
//				log.warn("[获取排行榜数据] [失败] [没有这个子榜单] [菜单："+menu+"] [子菜单："+submenu+"]");
				if(log.isWarnEnabled())
					log.warn("[获取排行榜数据] [失败] [没有这个子榜单] [菜单：{}] [子菜单：{}]", new Object[]{menu,submenu});
			}
		}else{
//			log.warn("[获取排行榜数据] [失败] [没有此类排行榜] [菜单："+menu+"] [子菜单："+submenu+"]");
			if(log.isWarnEnabled())
				log.warn("[获取排行榜数据] [失败] [没有此类排行榜] [菜单：{}] [子菜单：{}]", new Object[]{menu,submenu});
		}
		return null;
	}
	
	public Billboards getBillboards(String name){
		if(this.billboards!=null){
			for(int i=0;i<this.billboards.length;i++){
				if(this.billboards[i]!=null){
					if(this.billboards[i].getName().equals(name)){
						if(log.isInfoEnabled()){
//							log.info("[获取排行榜] [成功] [名称："+name+"]");
							if(log.isInfoEnabled())
								log.info("[获取排行榜] [成功] [名称：{}]", new Object[]{name});
						}
						return this.billboards[i];
					}
				}
			}
		}
//		log.warn("[获取排行榜] [失败] [没有此类排行榜] [名称："+name+"]");
		if(log.isWarnEnabled())
			log.warn("[获取排行榜] [失败] [没有此类排行榜] [名称：{}]", new Object[]{name});
		return null;
	}
	
	public Billboards[] getBillboards(){
		return this.billboards;
	}
	
	public void setMaker(BillboardMaker maker) {
		this.maker = maker;
	}
	
	public static int isIncluding(String menu){
		for(int i=0;i<BillboardManager.BILLBOARDS_NAMES.length;i++){
			if(BillboardManager.BILLBOARDS_NAMES[i].equals(menu)){
				return i;
			}
		}
		return -1;
	}
	
	private Connection getConnection() throws Exception {
		return dataSourceManager.getConnection();
	}
	
	public static BillboardManager getInstance(){
		return BillboardManager.self;
	}
	
	public void destroy(){
		long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		try{
			Billboards bbs=this.getBillboards(Translate.text_2311);
			if(bbs!=null){
				EquipmentBillboards eb=(EquipmentBillboards)bbs;
				eb.outputXml();
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("[DESTROY] [装备排行榜] [发生错误] [错误："+e+"]",e);
		}
		System.out.println("[Destroy] 调用destroy方法保存装备排行榜数据, cost " + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - t) + " ms");
		if(log.isInfoEnabled()){
//			log.info("[DESTROY] [装备排行榜] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - t)+"]");
			if(log.isInfoEnabled())
				log.info("[DESTROY] [装备排行榜] [耗时：{}]", new Object[]{(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - t)});
		}
	}

	public PlayerManager getPm() {
		return pm;
	}

	public void setPm(PlayerManager pm) {
		this.pm = pm;
	}



	public DungeonsRecordManager getDrm() {
		return drm;
	}

	public void setDrm(DungeonsRecordManager drm) {
		this.drm = drm;
	}

}
