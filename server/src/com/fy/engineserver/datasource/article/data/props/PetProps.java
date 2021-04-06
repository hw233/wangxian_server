package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;

/**
 * 特殊道具：宠物道具
 * 
 *
 */
public class PetProps extends Props implements Gem{
	public int talent1skill;
	public int talent2skill;
	private  String eggAticleName;
	private short objectScale;
	private String particleName = "";
	// 生物颜色值
	private int objectColor = -1;
	private boolean objectOpacity = false;
//	//对应的宠物类型
//	protected int category;

	/**
	 * 仅供avata用，看不懂问美术或单涛
	 */
	protected String avataRace="";
	protected String avataSex="";
	protected String title = "";
	protected String title_stat = "";
	
	public String getTitle_stat() {
		return title_stat;
	}

	public void setTitle_stat(String title_stat) {
		this.title_stat = title_stat;
	}

	public String getAvataRace() {
		return avataRace;
	}

	public void setAvataRace(String avataRace) {
		this.avataRace = avataRace;
	}

	public String getAvataSex() {
		return avataSex;
	}

	public void setAvataSex(String avataSex) {
		this.avataSex = avataSex;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

//	public int getCategory() {
//		return category;
//	}
//
//	public void setCategory(int category) {
//		this.category = category;
//	}

	public boolean isUsedUndisappear() {
		return true;
	}
@Override
public byte getArticleType() {
	// TODO Auto-generated method stub
	return ARTICLE_TYPE_PET;
}
	@Override
	public int getKnapsackType() {
		// TODO Auto-generated method stub
		return KNAP_宠物;
	}

	@Override
	public byte getCanUseType() {
		return 2;
	}
	/**
	 * 使用方法(重写父类方法)
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity aee){
		if(!super.use(game,player,aee)){
			return false;
		}
		//先检查宠物道具是否在玩家身上
		ArticleEntity ae = player.getArticleEntity(aee.getId());
		if(ae == null) {
//			ArticleManager.logger.warn("[宠物召唤错误] [玩家没有此宠物道具] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [宠物道具id:"+aee.getId()+"]");
			if(ArticleManager.logger.isWarnEnabled())
				ArticleManager.logger.warn("[宠物召唤错误] [玩家没有此宠物道具] [{}] [{}] [{}] [宠物道具id:{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),aee.getId()});
			return false;
		}
		//加载宠物
		PetManager mwm = PetManager.getInstance();
		if(aee instanceof PetPropsEntity){
			PetPropsEntity mwe = (PetPropsEntity)aee;
			Pet pet = mwm.getPet(mwe.getPetId());
			if(pet == null) {
//				ArticleManager.logger.warn("[宠物召唤错误] [宠物不存在] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] [petId:"+mwe.getPetId()+"]");
				if(ArticleManager.logger.isWarnEnabled())
					ArticleManager.logger.warn("[宠物召唤错误] [宠物不存在] [{}] [{}] [{}] [petId:{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),mwe.getPetId()});
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.服务器出现错误);
				player.addMessageToRightBag(hreq);
				return false;
			}
			
			if(pet.getHookInfo() != null){
				player.sendError(Translate.此宠物正在宠物房挂机不能执行此操作);
				return false;
			}
			if (WolfManager.getInstance().isWolfGame(player)) {
				//player.sendError(Translate.副本中不能召唤宠物);
				return false;
			}

			if(player.isIsUpOrDown()){
				if(player.isFlying()){
					//飞行状态下不能招出宠物
					player.send_HINT_REQ(Translate.飞行状态下不能招出宠物);
					return false;
				}
			}
			
			//召唤此宠物
			//先检查玩家有没有已召唤的宠物，有就收回，然后再召唤新的
			long activePetId = player.getActivePetId();
			if(activePetId > 0) {
				
				if(activePetId == pet.getId()) {
					//收回
					player.packupPet(true);
					
				}else{
					player.packupPet(true);
					//召唤
					player.summonPet(pet, (PetPropsEntity)aee,"召唤");
				}
			} else {
				//直接召唤
				player.summonPet(pet, (PetPropsEntity)aee,"直接召唤");
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 从玩家身上卸下
	 * @param player
	 */
	public void unloaded(Player player, ArticleEntity ee) {}

	/**
	 * 判断某个玩家是否可以使用此物品 子类可以重载此方法
	 * 返回null表示可以使用
	 * 返回字符串表示不能使用
	 * 字符串为不能使用的详细信息
	 * @param p
	 * @return
	 */
	public String canUse(Player p) {

		String resultStr = super.canUse(p);
		return resultStr;
	}

	public String getComment(){
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}


	public String getEggAticleName() {
		return eggAticleName;
	}

	public void setEggAticleName(String eggAticleName) {
		this.eggAticleName = eggAticleName;
	}

	public short getObjectScale() {
		return objectScale;
	}

	public void setObjectScale(short objectScale) {
		this.objectScale = objectScale;
	}

	public int getObjectColor() {
		return objectColor;
	}

	public void setObjectColor(int objectColor) {
		this.objectColor = objectColor;
	}

	public boolean isObjectOpacity() {
		return objectOpacity;
	}

	public void setObjectOpacity(boolean objectOpacity) {
		this.objectOpacity = objectOpacity;
	}

	public String getParticleName() {
		return particleName;
	}

	public void setParticleName(String particleName) {
		this.particleName = particleName;
	}
}
