package com.fy.engineserver.datasource.article.data.entity;

import com.fy.engineserver.billboard.special.SpecialEquipmentBillBoard;
import com.fy.engineserver.billboard.special.SpecialEquipmentManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

/**
 * 伏天古宝(服务器不唯一装备，有特殊操作)
 * @author Administrator
 *
 */
@SimpleEntity
public class Special_2EquipmentEntity extends EquipmentEntity {

	public Special_2EquipmentEntity(){}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4647593609668565894L;

//	private List<Long> playerList = new ArrayList<Long>();
	
	private long ownerId;
	// 17个操作 "鉴定","铭刻","镶嵌","挖宝时","强化","炼化","升级","摘星",
	//"装星","装备合成","拾取到防爆包","移动到防爆包","移动到仓库",绑定,"器灵附灵","器灵摘取","装备炼器"
	public transient boolean[] oprate2 = {false,false,false,false,false,false,false,false,false,false,true,true,true,true,false,false,false};
	
	
	public boolean[] oprate = {};
	/**
	 * 指定操作是否能操作
	 * @param player
	 * @param isQuery 是否执行查询操作(查询操作不给人提示)
	 * @return
	 */
	public boolean isOprate(Player player,boolean isQuery,byte type){
		
		boolean result = oprate2[type];
		if(!result){
			if(SpecialEquipmentManager.logger.isInfoEnabled()){
				SpecialEquipmentManager.logger.info("[伏天古宝类型装备不能执行] ["+EquipmentEntity.oprateName[type]+"] ["+player.getLogString()+"]");
			}
			if(!isQuery){
//				player.sendError("伏天古宝类型装备不能执行"+EquipmentEntity.oprateName[type]+"操作");
				player.sendError(Translate.translateString(Translate.伏天古宝类型装备不能执行xx, new String[][]{{Translate.STRING_1,EquipmentEntity.oprateName[type]}}));
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
			SpecialEquipmentBillBoard special2 = SpecialEquipmentManager.getInstance().getSpecialEquipmentBillBoard();
			ArticleManager am = ArticleManager.getInstance();
			Article a = am.getArticle(this.getArticleName());
			if(a != null && a instanceof Equipment){
				Equipment em = (Equipment)a;
				String st = special2.removeSpecial2(em.getName(), this.getId());
				if(st != null){
					SpecialEquipmentManager.logger.error("[销毁特殊2成功] [玩家id:"+this.getOwnerId()+"]");
				}else{
					SpecialEquipmentManager.logger.error("[销毁特殊2失败] [玩家id:"+this.getOwnerId()+"]");
				}
			}
		}catch(Exception e){
			SpecialEquipmentManager.logger.error("[销毁特殊2异常] [玩家id:"+this.getOwnerId()+"]",e);
		}
	}
	
	/**
	 * 死亡后必然掉
	 * @return
	 */
	public boolean isDrop(){
		return false;
	}
	
	/**
	 * 生成时间 用于排序
	 */
	private long createTime;


	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
		saveData("createTime");
	}

//	public List<Long> getPlayerList() {
//		return playerList;
//	}
//
//	public void setPlayerList(List<Long> playerList) {
//		this.playerList = playerList;
//		saveData("playerList");
//	}
	
	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
		saveData("ownerId");
	}
	
}
