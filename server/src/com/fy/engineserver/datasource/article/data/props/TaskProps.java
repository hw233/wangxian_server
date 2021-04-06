package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.ComposeInterface;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.OPEN_ACCEPT_TASK_WINDOW_REQ;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.fy.engineserver.vip.VipManager;


/**
 * 特殊道具：任务卷轴
 *
 */
public class TaskProps extends Props implements ComposeInterface{

	/**
	 * 任务的唯一标识
	 */
	protected int[] ids;

	/**
	 * 任务归属地名
	 */
	protected String dependencyName;
	
	/**
	 * 任务名
	 */
	protected String taskName;
	
	private int type ;

	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
	}
	/**
	 * 客户端用的，0为不能合成 1为能合成
	 */
	public byte getComposeArticleType() {
		return 1;
	}
	@Override
	public boolean isUsedUndisappear() {
		// TODO Auto-generated method stub
		return true;
	}
	public String getDependencyName() {
		return dependencyName;
	}

	public void setDependencyName(String dependencyName) {
		this.dependencyName = dependencyName;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 使用道具
	 * @param player
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if(!super.use(game,player,ae)){
			return false;
		}
		// 需要给客户端发消息提示是否接任务
		int id = 0;
		if(ids != null && ids.length > 0){
			if(ids.length > ae.getColorType()){
				id = ids[ae.getColorType()];
			}else{
				id = ids[ids.length - 1];
			}
		}
		
		
		int drinkTimes = player.getTieTimes();
		long lastDrinkDate = player.getLastTieDate();
		int vipAdd = VipManager.getInstance().vip每日增加的道具使用次数(player,this.get物品二级分类());
		boolean isSameWeek = TimeTool.instance.isSame(lastDrinkDate, System.currentTimeMillis(), TimeDistance.DAY, 7);
		if(!isSameWeek){
			player.setTieTimes(0);
			player.setLastTieDate(System.currentTimeMillis());
			drinkTimes = 0;
		}
		if(drinkTimes >= 15 + vipAdd){
			player.sendError("每周只能使用封魔录"+(15 + vipAdd)+"次");
			return false;
		}
		
		Task task = TaskManager.getInstance().getTask(id);
		if (task == null) {
			HINT_REQ err = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.text_task_016);
			player.addMessageToRightBag(err);
			return false;
		}
		if (player.getCareer() != 5 && task.getName_stat().equals(CareerManager.兽魁进阶任务)) {
			player.sendError(Translate.只有兽魁可以使用);
			return false;
		}
		
		player.notifyTaskPropsTrigerTask((PropsEntity)ae, task);
		
		OPEN_ACCEPT_TASK_WINDOW_REQ taskmessage = new OPEN_ACCEPT_TASK_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),task, task.getTargets(), task.getPrizes(),
				ae.getId());
		player.addMessageToRightBag(taskmessage);
		return true;
	}
	
	@Override
	public void usingTimesLimitProp(Player player) {

	}

	public ArticleEntity getComposeEntity(Player player, ArticleEntity ae, boolean binded, int createCount) {
		// TODO Auto-generated method stub
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		if(am == null || aem == null){
			return null;
		}
		if(ae.getColorType() < ArticleManager.notEquipmentColorMaxValue){
			Article a = am.getArticle(ae.getArticleName());
			if(a != null){
				try{
					ArticleEntity aee = aem.createEntity(a, binded, ArticleEntityManager.CREATE_REASON_COMPOSE_ARTICLE, player, ae.getColorType()+1, createCount, true);
					return aee;
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			return null;
		}
		
		return null;
	}

	@Override
	public long getTempComposeEntityId(Player player, ArticleEntity ae,
			boolean binded) {
		long tempId = -1;
		if(ae != null){
			if(ae.getColorType() < ArticleManager.notEquipmentColorMaxValue){
				return 0;
			}
		}
		return tempId;
	}
	@Override
	public String getTempComposeEntityDescription(Player player,
			ArticleEntity ae, boolean binded) {
		// TODO Auto-generated method stub
		String description = "";
		return description;
	}
}
