package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.PetExperienceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet.SingleForPetPropsEntity;
import com.fy.engineserver.sprite.pet2.Pet2Manager;

/**
 * 简单的道具，直接用于修改属性值
 *
 */
public class SingleForPetProps extends Props{

	/**
	 * 此简单道具要修改的属性，以及增加的值
	 * 修改都是加法运算
	 * 按照规定的顺序给玩家宠物修改属性
	 * 数组顺序:hp,exp,level
	 */
	protected long []values;
	
	// 临时经验 由仙兽房得到的宠物经验
	private long tempExp = -1l;
	
	
	
	public long getTempExp() {
		return tempExp;
	}

	public void setTempExp(long tempExp) {
		this.tempExp = tempExp;
	}

	public long[] getValues() {
		return values;
	}

	public void setValues(long[] values) {
		this.values = values;
	}
	@Override
	public int getKnapsackType() {
		// TODO Auto-generated method stub
		return Article.KNAP_奇珍;
	}
	/**
	 * 使用道具
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity ae){
		if(!super.use(game,player,ae)){
			return false;
		}
		long petId = player.getActivePetId();
		Pet pet = null;
		try{
			pet = PetManager.getInstance().getPet(petId);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		if(pet == null){
			player.sendError(Translate.translateString(Translate.没有指定宠物不能使用, new String[][]{{Translate.STRING_1,this.getName()}}));
			return false;
		}
		 //0 不是洗天生技能的药物；   1 药物和宠物不匹配  2 吃掉了。
		int changeRet = Pet2Manager.getInst().changeTianSheng(player, pet, ae);
		if(changeRet == 0){
		}else if(changeRet == 1){
			return false;
		}else if(changeRet == 2){
			return true;
		}else{
			player.sendError(Translate.未知错误+changeRet);
			return false;
		}
		String result = null;
		
		if (PetManager.logger.isDebugEnabled()) {
			PetManager.logger.debug("[使用宠物经验丹] ["+ae.getClass()+"] ["+player.getLogString()+"]");
		}
		if(ae instanceof SingleForPetPropsEntity){
			if(pet.getLevel()>=PetExperienceManager.maxLevel){
				result = String.format(Translate.petLevelOver220, PetManager.getLevelDes(PetExperienceManager.maxLevel));
			}else{
				PetManager.lock.lock();
				try{
					this.tempExp = ((SingleForPetPropsEntity)ae).getValues()[1];
					if(PetManager.logger.isDebugEnabled()){
						PetManager.logger.debug("[使用挂机得到的宠物经验丹] ["+player.getLogString()+"] ["+this.getName()+"] [经验值:"+tempExp+"]");	
					}
					result= changePetProperty(player,pet);
				}catch (Exception e) {
					PetManager.logger.error("[使用挂机得到的宠物经验丹] ["+player.getLogString()+"] ["+this.getName()+"]",e);
				}finally{
					PetManager.lock.unlock();
				}
			}
		}else{
			result = changePetProperty(player,pet);
		}
		
		if(result != null && !result.equals("")){
			player.sendError(result);
			return false;
		}else{
			if(ArticleManager.logger.isDebugEnabled()){
	//			ArticleManager.logger.debug("[使用道具] [简单道具] [成功] ["+player.getName()+"] ["+this.getName()+"] ["+getComment()+"] [直接改变宠物属性]");
				if(ArticleManager.logger.isDebugEnabled())
					ArticleManager.logger.debug("[使用道具] [简单道具] [成功] [{}] [{}] [{}] [直接改变宠物属性]", new Object[]{player.getName(),this.getName(),getComment()});
			}
		}
		return true;
	}
	@Override
	public String canUse(Player p) {
		// TODO Auto-generated method stub
		String result = super.canUse(p);
		if(result == null){
			long petId = p.getActivePetId();
			Pet pet = null;
			try{
				pet = PetManager.getInstance().getPet(petId);
			}catch(Exception ex){
				
			}
			if(pet == null){
				result = Translate.translateString(Translate.没有指定宠物不能使用, new String[][]{{Translate.STRING_1,this.getName()}});
			}
		}
		return result;
	}
	/**
	 * 修改宠物属性
	 * @param player
	 * @param pet
	 * @param bln 是否删除道具(使用道具不删除)
	 * @return
	 */
	public String changePetProperty(Player player,Pet pet){
		
		if(player != null && values != null){
			
			for(int i = 0; i < values.length; i++){
				long value = values[i];
				if(value != 0){
					switch(i){
					case 0 :
						//血
						if(pet.getHp() == pet.getMaxHP()){
							return Translate.此宠物气血已经达到最大;
						}
						pet.setHp(pet.getHp() + (int)value);
						if(pet.getHp() < 0){
							pet.setHp(0);
						}else if(pet.getHp() > pet.getMaxHP()){
							pet.setHp(pet.getMaxHP());
						}
						break;
					case 1 :
						//经验
						if(tempExp > 0){
							value = this.tempExp;
							tempExp = -1l;
						}
						PetManager.logger.warn("[使用经验丹] ["+player.getLogString()+"] ["+pet.getLogString()+"] [value:"+value+"]");
						String result = pet.addExp(value, PetExperienceManager.ADDEXP_REASON_SINGLE_PROPS);
						return result;
					case 2 :
						//升级丹
						long exp = pet.getNextLevelExp();
						result = pet.addExp(exp, PetExperienceManager.ADDEXP_REASON_SINGLE_PROPS);
						return result;
					}
				}
			}
			return "";
		}
		return null;
	}
}
