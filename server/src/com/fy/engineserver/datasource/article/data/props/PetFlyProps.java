package com.fy.engineserver.datasource.article.data.props;

import java.util.List;
import java.util.Random;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PET_FLY_BUTTON_ONCLICK_RES;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetEatProp2Rule;
import com.fy.engineserver.sprite.pet.PetFlySkillInfo;
import com.fy.engineserver.sprite.pet.PetFlyState;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet2.GradePet;
import com.fy.engineserver.sprite.pet2.Pet2Manager;

public class PetFlyProps extends Props {

	Random random = new Random(); 	
	
	@Override
	public String canUse(Player p) {
		if(super.canUse(p) != null){
			return super.canUse(p);
		}
		Pet pet = PetManager.getInstance().getPet(p.getActivePetId());
		if(pet == null){
			return Translate.出战状态的宠物才能使用升华露;
		}
		
		PetFlyState pstate = PetManager.getInstance().getPetFlyState(pet.getId(), p);
		if(pstate == null){
			return Translate.使用升华露异常;
		}
		
		if(pstate.getLingXingPoint() < 50){
			return Translate.使用升华露灵性不够;
		}
		
		if(pstate.getUseTimes() >= 7){
			return Translate.使用升华露次数达上限;
		}
		
		if(pstate.getLingXingPoint() >= 110){
			return Translate.灵性值已达上限;
		}
		
		if(pstate.getLingXingPoint() > 100){
			return Translate.灵性值100不可使用;
		}
		return null;
	}

	public byte getArticleType() {
		return ARTICLE_TYPE_PET_FLY;
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		List<PetEatProp2Rule> eat2Rules = PetManager.eat2Rules;
		PetEatProp2Rule rule = null;
		for(PetEatProp2Rule ru : eat2Rules){
			if(ru != null && ru.getPropName().equals(this.getName())){
				rule = ru;
			}
		}
		
		if(rule == null){
			ArticleManager.logger.warn("[使用道具] [失败:升华露配置错误] [道具:{}] [] [玩家:{}]",new Object[]{this.getName(),this.getName_stat(),player.getLogString()});
			return false;
		}
		
		int basicLingXingValue = rule.getBasicAddPoint();
		int baoJiValue = 0;
		int baoJiPoints [] = rule.getBaoJiAddPoints();
		int baoJiNums [] = rule.getBaoJiNums();
		int rValue = random.nextInt(1000);
		if(baoJiNums == null){
			ArticleManager.logger.warn("[使用道具] [失败:baoJiNums == null] [道具:{}] [] [玩家:{}]",new Object[]{this.getName(),this.getName_stat(),player.getLogString()});
			return false;
		}
//		Arrays.sort(baoJiNums);
		
		for(int i=0;i<baoJiNums.length;i++){
			if(baoJiNums[i] >= rValue){
				if(baoJiValue < baoJiPoints[i]){
					baoJiValue = baoJiPoints[i];
				}
			}
		}
		
		if(baoJiValue >= 1000){
			ArticleManager.logger.warn("[使用道具] [失败:baoJiValue >= 1000] [道具:{}] [] [baoJiValue:{}] [玩家:{}]",new Object[]{this.getName(),this.getName_stat(),baoJiValue,player.getLogString()});
			return false;
		}
		
		Pet pet = PetManager.getInstance().getPet(player.getActivePetId());
		if(pet != null){
			PetFlyState pstate = PetManager.getInstance().getPetFlyState(pet.getId(), player);
			if(pstate != null){
				pstate.setUseTimes(pstate.getUseTimes()+1);
				int oldLX = pstate.getLingXingPoint();
				int oldQN = pstate.getQianNengPoint();
				int allLXPoints = oldLX+basicLingXingValue+baoJiValue;
				if(allLXPoints > 110){
					allLXPoints = 110;
				}
				pstate.setLingXingPoint(allLXPoints);
				try {
					if (pstate.getLingXingPoint() >= 85) {
						AchievementManager.getInstance().record(player, RecordAction.使用易筋丹使宠物灵性达到85);
						if (pstate.getLingXingPoint() >= 100) {
							AchievementManager.getInstance().record(player, RecordAction.使用易筋丹使宠物灵性达到100);
							if (pstate.getLingXingPoint() >= 110) {
								AchievementManager.getInstance().record(player, RecordAction.使用易筋丹使宠物灵性达到110);
							}
						}
					}
				} catch (Exception e) {
					PlayerAimManager.logger.warn("[目标系统] [统计宠物灵性次数次数] [异常] [" + player.getLogString() + "]", e);
				}
				String flyAvata = "";
				ArticleEntity artE = player.getArticleEntity(pet.getPetPropsId());
				if (artE != null && artE instanceof PetPropsEntity) {
					PetPropsEntity entity = (PetPropsEntity) artE;
					GradePet gradePet = Pet2Manager.inst.findGradePetConf(entity.getArticleName());
					if(gradePet != null){
						flyAvata = gradePet.flyAvata;
					}
				}
			
				int basicAddQNValue = 0;
				int baojiAddQNValue = 0;
//				if(pstate.getHistoryLingXingPoint() < pstate.getLingXingPoint()){
					pstate.setHistoryLingXingPoint(pstate.getLingXingPoint());
					if(pstate.getLingXingPoint() <= 100){
						basicAddQNValue = basicLingXingValue *5;
						baojiAddQNValue = baoJiValue*5;
					}else if(pstate.getLingXingPoint() >=101 && pstate.getLingXingPoint() <= 105){
						basicAddQNValue = basicLingXingValue *10;
						baojiAddQNValue = baoJiValue*10;
					}else if(pstate.getLingXingPoint() >=106 && pstate.getLingXingPoint() <= 110){
						basicAddQNValue = basicLingXingValue *20;
						baojiAddQNValue = baoJiValue*20;
					}
					pstate.setQianNengPoint(oldQN + basicAddQNValue + baojiAddQNValue);
//				}
				pet.initPetFlyAvata("useProp");
				try {
					PetManager.getInstance().savePetFlyState(pstate, pet.getId(), player);
					int buttonType = 0;
					if(pstate.getLingXingPoint() >= 105){
						buttonType = 1;
					}
					PetFlySkillInfo info = PetManager.petFlySkills.get(pstate.getSkillId());
					if(info == null){
						info = new PetFlySkillInfo();
					}
					int animation = 1;
					if(pstate.getAnimation() == 1){
						animation = 0;
					}
//					pstate.setAnimation(1);
					PetManager.getInstance().savePetFlyState(pstate, pet.getId(), player);
					PET_FLY_BUTTON_ONCLICK_RES res = new PET_FLY_BUTTON_ONCLICK_RES(GameMessageFactory.nextSequnceNum(),flyAvata , animation,buttonType,pstate.getLingXingPoint(), pstate.getXiaoHuaDate(), (7 - pstate.getUseTimes()), "",pet,info);
					player.addMessageToRightBag(res);
					if(baoJiValue > 0){
//						String mess = Translate.translateString(Translate.吃升华露成功暴击描述, new String[][] { { Translate.COUNT_1, String.valueOf(basicLingXingValue) },{ Translate.STRING_1, "+"+baoJiValue }, { Translate.COUNT_2, String.valueOf(basicAddQNValue) }, { Translate.STRING_2, "+"+baojiAddQNValue }});
						String mess = Translate.translateString(Translate.吃升华露成功描述2, new String[][] { { Translate.COUNT_1, String.valueOf(basicLingXingValue+baoJiValue) }, { Translate.COUNT_2, String.valueOf(basicAddQNValue+baojiAddQNValue) }});
						player.sendError(mess);
					}else{
						String mess = Translate.translateString(Translate.吃升华露成功描述, new String[][] { { Translate.COUNT_1, String.valueOf(basicLingXingValue) }, { Translate.COUNT_2, String.valueOf(basicAddQNValue) }});
						player.sendError(mess);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					ArticleManager.logger.warn("[使用宠物飞升道具] [存储异常]",e);
				}
				ArticleManager.logger.warn("[使用宠物飞升道具] [成功] [宠物:{}] [宠物id:{}] [道具:{}] [灵性变化:{}] [潜能变化:{}] [使用次数:{}] [技能id:{}] [basicLingXingValue:{}] [baoJiValue:{}] [basicAddQNValue:{}] [baojiAddQNValue:{}] [玩家:{}]",
						new Object[]{pet.getName(),pet.getId(),this.getName(), oldLX+"-->"+pstate.getLingXingPoint(),oldQN+"-->"+pstate.getQianNengPoint(),pstate.getUseTimes(),pstate.getSkillId(), basicLingXingValue,baoJiValue,basicAddQNValue,baojiAddQNValue,player.getLogString()});
				return true;
			}
		}
		return false;
	}


}
