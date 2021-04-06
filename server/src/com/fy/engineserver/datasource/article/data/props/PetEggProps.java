package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.PetSubSystem;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.PET_BREEDING_RES;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetFlyState;
import com.fy.engineserver.sprite.pet.PetManager;

/**
 * 特殊道具：宠物蛋道具
 * 
 *
 */
public class PetEggProps extends Props implements Gem{
	
	//对应孵化出来的宠物物种名称
	protected String petArticleName;
	
	protected String petArticleName_stat;
	
	//稀有度
	protected int rarity = 0;
	
	//可携带等级
	protected int realTrainLevel = 0;
	
	private int maxLevl = 0;
	
	//职业倾向
	protected byte career = 0;
	
	//忠诚    精明     谨慎    狡诈  (0,1,2,3)
	private  int character =0;
	
	//物品分类,0普通1神宠
	private int type = 0 ;
	
	@Override
	public byte getArticleType() {
		// TODO Auto-generated method stub
		return ARTICLE_TYPE_PET_EGG;
	}
	public String getPetArticleName() {
		return petArticleName;
	}

	public void setPetArticleName(String petArticleName) {
		this.petArticleName = petArticleName;
	}

	public int getRarity() {
		return rarity;
	}

	public void setRarity(int rarity) {
		this.rarity = rarity;
	}


	
	public String getPetArticleName_stat() {
		return petArticleName_stat;
	}
	public void setPetArticleName_stat(String petArticleName_stat) {
		this.petArticleName_stat = petArticleName_stat;
	}
	public int getRealTrainLevel() {
		return realTrainLevel;
	}
	public void setRealTrainLevel(int realTrainLevel) {
		this.realTrainLevel = realTrainLevel;
	}
	public byte getCareer() {
		return career;
	}

	public void setCareer(byte career) {
		this.career = career;
	}

	/**
	 * 使用方法(重写父类方法)
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity aee){
		if(!super.use(game,player,aee)){
			return false;
		}
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		long eggId = aee.getId();
		String articleName = aee.getArticleName();
		Article article = ArticleManager.getInstance().getArticle(articleName);
		if(article == null || !(article instanceof PetEggProps)) {
//			PetSubSystem.logger.error("[宠物蛋孵化] [错误：宠物蛋道具错误] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] [物品id:"+eggId+"]", start);
			PetSubSystem.logger.error("[宠物蛋孵化] [错误：宠物蛋道具错误] [{}] [{}] [{}] [物品id:{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getUsername(),eggId,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
//			ArticleManager.logger.warn("[宠物蛋孵化] [错误：宠物蛋道具错误] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] [物品id:"+eggId+"]", start);
			if(ArticleManager.logger.isWarnEnabled())
				ArticleManager.logger.warn("[宠物蛋孵化] [错误：宠物蛋道具错误] [{}] [{}] [{}] [物品id:{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getUsername(),eggId,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
			return false;
		}
		
		PetManager petManager = PetManager.getInstance();
		
		//没飞升的玩家不能使用飞升的蛋
		try{
			long petId = ((PetEggPropsEntity) aee).getPetId();
			PetFlyState stat = PetManager.getInstance().getPetFlyState(petId,"宠物蛋孵化");
			if(stat != null && stat.getFlyState() == 1){
				TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
				if (entity == null || entity.getFeisheng() != 1) { 
					player.sendError(Translate.飞升后才能仙婴);
					return false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			ArticleManager.logger.warn("[宠物蛋孵化] [处理飞升限制出错] [articleName:"+articleName+"] [eggId:"+eggId+"] ["+player.getLogString()+"]");
		}
		
		Pet pet = petManager.breedingPet((PropsEntity)aee, player);
		if(pet == null){
			return false;
		}
		
		if(aee instanceof PetEggPropsEntity){
			byte type = ((PetEggPropsEntity)aee).getEggType();
			if(type == 1){
				pet.setEggType((byte)1);
				if (PetManager.logger.isDebugEnabled()) {
					PetManager.logger.debug("[宠物蛋孵化成功] [任务得到的宠物蛋] ["+player.getLogString()+"] []");
				}
			}
		}
		try {
			if (this.getName_stat().equals(PlayerAimManager.金宝蛇皇蛋)) {
				AchievementManager.getInstance().record(player, RecordAction.获得金宝蛇皇);
			}
		} catch (Exception e) {
			PlayerAimManager.logger.error("[目标系统] [统计玩家获得金宝蛇皇] [异常] [" + player.getLogString() + "]", e);
		}

		if(PetManager.logger.isWarnEnabled())
			PetManager.logger.warn("[宠物蛋孵化] [成功] [{}] {} [耗时:{}ms]", new Object[]{player.getLogString4Knap(),pet.getLogOfInterest(), com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
		if(ArticleManager.logger.isInfoEnabled())
			ArticleManager.logger.info("[宠物蛋孵化] [成功] [{}] [{}] [{}] [物品id:{}] [{}ms]", new Object[]{player.getId(),player.getName(),player.getUsername(),eggId,com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start});
		String result = Translate.宠物蛋孵化成功;
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, result);
		player.addMessageToRightBag(hreq);
		
		PET_BREEDING_RES res = new PET_BREEDING_RES(GameMessageFactory.nextSequnceNum(), "",pet.getPetPropsId());
		player.addMessageToRightBag(res);
		return true;
		
		
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
	public int getMaxLevl() {
		return maxLevl;
	}
	public void setMaxLevl(int maxLevl) {
		this.maxLevl = maxLevl;
	}
	public int getCharacter() {
		return character;
	}
	public void setCharacter(int character) {
		this.character = character;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}