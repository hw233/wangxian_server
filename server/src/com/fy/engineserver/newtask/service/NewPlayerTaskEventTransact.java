package com.fy.engineserver.newtask.service;

import java.util.List;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.ENTITY_MSG;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PROP_MSG;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.prizes.TaskPrize;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfArticle;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfCareer;
import com.fy.engineserver.newtask.service.TaskConfig.PrizeType;
import com.fy.engineserver.sprite.Player;

public class NewPlayerTaskEventTransact extends AbsTaskEventTransact{

	public static String newPlayer_task = Translate.text_肩负使命;
	
	public void init() {

	}
	private static String[] accept_task = new String[]{Translate.text_证明实力, Translate.text_义救仙心, Translate.text_营救九黎, Translate.text_清理爪牙};
	
	private static String[] done_task = new String[]{Translate.text_肩负使命, Translate.text_再救苍生, Translate.text_喜获神兵, Translate.text_宝甲护身, Translate.text_支援同道, Translate.text_证明实力, Translate.text_锦囊妙计, Translate.text_一技傍身, 
		Translate.text_助拳修罗, Translate.text_义救仙心, Translate.text_美色诱惑, Translate.text_险成大错, Translate.text_清理爪牙, Translate.text_恭候大驾, Translate.text_剿灭狐妖, Translate.text_证明实力, Translate.text_驰援影魅, Translate.text_营救九黎
		,Translate.text_清理谷口,Translate.text_宰牛用刀,Translate.text_修复阵法,Translate.text_禀明情况,Translate.text_有备无患,Translate.text_喜得法术,Translate.text_寻觅树灵,Translate.text_寻找兽王,Translate.text_大展拳脚,Translate.text_援助乡邻,Translate.text_再收宝甲,Translate.text_收获神兵,Translate.text_戾气渐染,Translate.text_清幽山谷};
	
	private static String[] deliver_task = new String[]{Translate.text_肩负使命, Translate.text_再救苍生, Translate.text_喜获神兵, Translate.text_宝甲护身, Translate.text_支援同道, Translate.text_风林火山, Translate.text_证明实力, Translate.text_锦囊妙计, Translate.text_一技傍身, 
		Translate.text_助拳修罗, Translate.text_义救仙心, Translate.text_驰援影魅, Translate.text_营救九黎, Translate.text_清理爪牙, Translate.text_美色诱惑, Translate.text_险成大错, Translate.text_恭候大驾, Translate.text_剿灭狐妖
		,Translate.text_清理谷口,Translate.text_宰牛用刀,Translate.text_修复阵法,Translate.text_禀明情况,Translate.text_有备无患,Translate.text_喜得法术,Translate.text_寻觅树灵,Translate.text_寻找兽王,Translate.text_大展拳脚,Translate.text_援助乡邻,Translate.text_再收宝甲,Translate.text_收获神兵,Translate.text_戾气渐染,Translate.text_清幽山谷};
	
	private static final String prop_task = Translate.text_助拳修罗;
	
	@Override
	public String[] getWannaDealWithTaskNames(Taskoperation action) {
		switch(action){
		case accept:
			return accept_task;
		case done:
			return done_task;
		case deliver:
			return deliver_task;
		}
		return null;
	}
	
	//接收
	public void handleAccept(Player player, Task task, Game game) {
//		FIND_WAY2TASK_RES res = new FIND_WAY2TASK_RES(GameMessageFactory.nextSequnceNum(), task.getId(), TaskConfig.TASK_STATUS_GET);
//		player.addMessageToRightBag(res);
	}

	//达到完成
	public void handleDone(Player player, Task task, Game game) {
//		FIND_WAY2TASK_RES res = new FIND_WAY2TASK_RES(GameMessageFactory.nextSequnceNum(), task.getId(), TaskConfig.TASK_STATUS_COMPLETE);
//		player.addMessageToRightBag(res);
	}
	
	//交付
	public void handleDeliver(Player player, Task task, Game game) {
		List<Task> list = TaskManager.getInstance().getnextTask(task.getName());
//		if(list != null && list.size()>0){
//			Task task2 = list.get(0);
//			NEXT_TASK_OPEN res = new NEXT_TASK_OPEN(GameMessageFactory.nextSequnceNum(), task2, task2.getTargets(), task2.getPrizes());
//			player.addMessageToRightBag(res);
//		}
		TaskManager.logger.warn("[任务奖励测试0] [prizeLength:"+task.getPrizes().length+"] [taskname:"+task.getName()+"] [playername:"+player.getName()+"]");
		if(task.getPrizes().length > 0){
			for(int i = 0 ; i < task.getPrizes().length; i++){
				TaskPrize prize = task.getPrizes()[i];
				TaskManager.logger.warn("[任务奖励测试1] [prizeType:"+prize.getPrizeType()+"] [prizeLength:"+prize.getPrizeName().length+"] [taskname:"+task.getName()+"] [playername:"+player.getName()+"] [prize:"+prize.getClass().getSimpleName()+"]");
				if(prize.getPrizeType() == PrizeType.CAREER_ARTICLE && prize.getPrizeName().length == 5){
					for (int j = 0; j < prize.getPrizeName().length; j++) {
						Article article = ArticleManager.getInstance().getArticle(prize.getPrizeName()[j]);
						if(article!= null&&article instanceof Equipment){
							Equipment equ = (Equipment)article;
							if(equ.getCareerLimit() == player.getCareer()){
								int index = player.getKnapsack_common().getArticleCellPos(equ.getName());
								int color = 0;
								if (prize instanceof TaskPrizeOfCareer) {
									color = ((TaskPrizeOfCareer)prize).getColorType()[j];
								}else if (prize instanceof TaskPrizeOfArticle) {
									color = ((TaskPrizeOfArticle)prize).getColorType()[j];
								}
								try{
								EquipmentEntity entity = (EquipmentEntity)ArticleEntityManager.getInstance().createEntity(equ, false, ArticleEntityManager.CREATE_REASON_NEWPLAYER, player, color, 1, true);
								com.fy.engineserver.datasource.article.entity.client.EquipmentEntity client = new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity();
								client.setId(entity.getId());
								client.setShowName(entity.getShowName());
								client.setIconId(equ.getIconId());
								client.setColorType((byte)entity.getColorType());
								ENTITY_MSG res = new ENTITY_MSG(GameMessageFactory.nextSequnceNum(), index, client, entity.getInfoShow(player));
								player.addMessageToRightBag(res);
								}catch(Exception ex){
									ex.printStackTrace();
								}
							}
						}
					}
				}else {
					for (int j = 0; j < prize.getPrizeName().length; j++) {
						Article article = ArticleManager.getInstance().getArticle(prize.getPrizeName()[j]);
						if (article != null && (task.getName().equals(prop_task) || task.getName().equals(Translate.text_有备无患))) {
							int color = 0;
							if (prize instanceof TaskPrizeOfCareer) {
								color = ((TaskPrizeOfCareer)prize).getColorType()[j];
							}else if (prize instanceof TaskPrizeOfArticle) {
								color = ((TaskPrizeOfArticle)prize).getColorType()[j];
							}
							int index = player.getKnapsack_common().getArticleCellPos(article.getName());
							try{
							ArticleEntity entity = ArticleEntityManager.getInstance().createEntity(article, false, ArticleEntityManager.CREATE_REASON_NEWPLAYER, player, color, 1, true);
							PROP_MSG res = new PROP_MSG(GameMessageFactory.nextSequnceNum(), index, (int)prize.getPrizeNum()[j], entity.getId(), entity.getShowName(), article.getIconId(), (byte)entity.getColorType(), entity.getInfoShow(player));
							player.addMessageToRightBag(res);
							}catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	
	public void handleDrop(Player player, Task task, Game game) {
		
	}

}
