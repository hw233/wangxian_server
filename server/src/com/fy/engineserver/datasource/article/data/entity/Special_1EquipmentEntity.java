package com.fy.engineserver.datasource.article.data.entity;

import java.util.Date;
import java.util.Random;

import com.fy.engineserver.billboard.special.SpecialEquipmentBillBoard;
import com.fy.engineserver.billboard.special.SpecialEquipmentManager;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse.dateUtil.DateFormat;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;


/**
 * 鸿天帝宝(服务器唯一装备)
 * @author Administrator
 *
 */
@SimpleEntity
public class Special_1EquipmentEntity extends EquipmentEntity {

	public Special_1EquipmentEntity(){}
	public static long 间隔时间 = 60*1000;
	public static long 宝魂值 = 1000;
	public static long 在线间隔增加宝魂值 = 1;
	public static long 不在线间隔减少宝魂值= 1;
	
//	宝魂值	0	1-200	201-500	501-800	801-1000
//	掉落几率	0%	-1%	-5%	-10%	-15%
	public static int[] 区间 = {0,200,500,800,1000};
	public static int[] 掉落几率= {100,99,95,90,85};
	private static long maxSpecialValue = 2000;
	
	public int 获得所在区间索引(long specialValue) {
		for(int i=0; i<区间.length; i++) {
			if(specialValue < 区间[i]) {
				return i;
			}
		}
		return 4;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6243489697112263632L;

	/**
	 * 所有人id
	 */
	private long playerId;	
	/**
	 * 宝魂值
	 */
	private long specialValue = 1000;
	
	/**
	 * 排序
	 */
	@SimpleColumn(name="createTime1")
	private long createTime;
	
	// 14个操作 "鉴定","铭刻","镶嵌","挖宝时","强化","炼化","升级","摘星",
	//"装星","装备合成","拾取到防爆包","移动到防爆包","移动到仓库"，绑定,"器灵附灵","器灵摘取","装备炼器"
//	@SimpleColumn(name="oprate1")
	public transient boolean[] oprate1 = {false,false,false,false,false,false,false,false,false,false,true,true,true,true,false,false,false};
	
	@SimpleColumn(name="oprate1")
	public boolean[] oprate = {};
	
	/**
	 * 指定操作是否能操作
	 * @param player
	 * @param isQuery 是否执行查询操作(查询操作不给人提示)
	 * @return
	 */
	public boolean isOprate(Player player,boolean isQuery,byte type){
		boolean result = oprate1[type];
		if(!result){
			if(SpecialEquipmentManager.logger.isInfoEnabled()){
				SpecialEquipmentManager.logger.info("[鸿天帝宝类型装备不能执行] ["+EquipmentEntity.oprateName[type]+"] ["+player.getLogString()+"]");
			}
			if(!isQuery){
//				player.sendError("鸿天帝宝类型装备不能执行"+EquipmentEntity.oprateName[type]+"操作");
				player.sendError(Translate.translateString(Translate.鸿天帝宝类型装备不能执行xx, new String[][]{{Translate.STRING_1,EquipmentEntity.oprateName[type]}}));
			}
		}
		return result;
	}
	
	/**
	 * 销毁
	 * @param player
	 */
	public void destroyEntity(Player player){
		try{
			if(player == null){
				//掉落时间到销毁
				ArticleManager am = ArticleManager.getInstance();
				Equipment em = (Equipment)am.getArticle(this.getArticleName());
				SpecialEquipmentBillBoard special1 = SpecialEquipmentManager.getInstance().getSpecialEquipmentBillBoard();
				String st = special1.removeSpecial1(em.getName(), this.getId());
				if(st != null){
					SpecialEquipmentManager.logger.error("[销毁特殊1成功] [玩家id:"+this.getPlayerId()+"]");
				}else{
					SpecialEquipmentManager.logger.error("[销毁特殊1失败] [玩家id:"+this.getPlayerId()+"]");
				}
			}else{
				if(this.getPlayerId() == player.getId()){
					SpecialEquipmentBillBoard special1 = SpecialEquipmentManager.getInstance().getSpecialEquipmentBillBoard();
					ArticleManager am = ArticleManager.getInstance();
					Equipment em = (Equipment)am.getArticle(this.getArticleName());
					String st = special1.removeSpecial1(em.getName(), this.getId());
					if(st != null){
						SpecialEquipmentManager.logger.error("[销毁特殊1成功] [玩家id:"+this.getPlayerId()+"]");
					}else{
						SpecialEquipmentManager.logger.error("[销毁特殊1失败] [玩家id:"+this.getPlayerId()+"]");
					}
				}
			}
		}catch(Exception e){
			SpecialEquipmentManager.logger.error("[销毁特殊1异常] [玩家id:"+this.getPlayerId()+"]",e);
		}
	}
	
	public static Random random = new Random();
	@SimpleColumn(name="drop1")
	private boolean drop = false;
	/**
	 * 死亡后必然掉
	 * @return
	 */
	public boolean isDrop(){
		
		if(!drop){
			if(SystemTime.currentTimeMillis() - createTime >= SpecialEquipmentManager.产生装备不掉时间){
				drop = true;
				saveData("drop");
			}else{
				if(SpecialEquipmentManager.logger.isInfoEnabled()){
					SpecialEquipmentManager.logger.info("[玩家死亡特殊装备掉落] ["+this.getArticleName()+"] [ownerId:"+this.getPlayerId()+"] [不掉]");
				}
				return drop;
			}
		}
		int index = this.获得所在区间索引(specialValue);
		int randomInt = random.nextInt(100)+1;
		if(randomInt >= 掉落几率[index]){
			if(SpecialEquipmentManager.logger.isInfoEnabled()){
				SpecialEquipmentManager.logger.info("[玩家死亡特殊装备掉落] ["+this.getArticleName()+"] [ownerId:"+this.getPlayerId()+"] [不掉]");
			}
			return false;
		}
		if(SpecialEquipmentManager.logger.isInfoEnabled()){
			SpecialEquipmentManager.logger.info("[玩家死亡特殊装备掉落] ["+this.getArticleName()+"] [ownerId:"+this.getPlayerId()+"] [掉]");
		}
		return true;
	}
	
	public boolean isEscape(){
		return true;
	}
	
	public void addSpecailValue(int value){
		if(specialValue + value >= maxSpecialValue){
			setSpecialValue(maxSpecialValue);
		}else{
			setSpecialValue(specialValue + value);
		}
		if(SpecialEquipmentManager.logger.isWarnEnabled()){
			SpecialEquipmentManager.logger.warn("[特殊装备1宝魂值变化] [增加] [宝魂值:"+getSpecialValue()+"] ["+this.getArticleName()+"]");
		}
	}
	
	/**
	 * 是不是要判断逃跑
	 * @param value
	 * @return
	 */
	public boolean decreaseSpecailValue(int value){
		if(SpecialEquipmentManager.logger.isWarnEnabled()){
			SpecialEquipmentManager.logger.warn("[特殊装备1宝魂值变化] [减少:"+value+"] [宝魂值:"+getSpecialValue()+"] ["+this.getArticleName()+"]");
		}
		if(specialValue - value > 0){
			setSpecialValue(specialValue - value);
			return false;
		}else{
			return true;
		}
		
	}

	/************************************/
	public long getSpecialValue() {
		return specialValue;
	}

	public void setSpecialValue(long specialValue) {
		this.specialValue = specialValue;
		saveData("specialValue");
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
		saveData("createTime");
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
		saveData("playerId");
	}
	 
	
}
