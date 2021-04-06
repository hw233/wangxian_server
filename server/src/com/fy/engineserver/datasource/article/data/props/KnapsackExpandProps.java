package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Confirm_Use_FangBaoKnapsack_Change;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;


/**
 * 
 * 背包扩充
 * 
 */
public class KnapsackExpandProps extends Props{

	private int expandNum;

	public int getExpandNum() {
		return expandNum;
	}

	public void setExpandNum(int expandNum) {
		this.expandNum = expandNum;
	}


	public byte getInvalidAfterAction() {
		return 0;
	}

	@Override
	public boolean isUsedUndisappear() {
		// TODO Auto-generated method stub
		return true;
	}
	/**
	 * 增加背包格子数量
	 */
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if(game == null || player == null){
			return false;
		}
		ArticleManager am = ArticleManager.getInstance();
		if(am != null){
			if(ae != null){
				if(ae.getTimer() != null){
					if(ae.getTimer().isClosed()){
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.物品已经到期);
						player.addMessageToRightBag(hreq);
						return false;
					}
				}
				int index = 0;
				long articleId = ae.getId();
				Article a = am.getArticle(ae.getArticleName());
				if(a instanceof KnapsackExpandProps){
					KnapsackExpandProps aa = (KnapsackExpandProps)a;
					int count = aa.getExpandNum();
					Knapsack_TimeLimit ks = player.getKnapsacks_fangBao();
//					long[] knapsack_fangBao_Ids = player.getKnapsack_fangBao_Ids(); 
					Knapsack knapsack = player.getKnapsacks_fangBao();
//					if(ks != null && ks.length > index){
						knapsack = ks;
//					}
					//如果防爆包要放的位置上有防爆包，那么执行判断并确认操作
					if(knapsack != null){
						//如果新防爆包格子大于原有防爆包的格子，那么给替换确认提示
						boolean canChange = false;
						if(knapsack.getCells().length <= count){
							canChange = true;
						}else{
							int oldCount = knapsack.getCells().length - ks.得到剩余格子数不论是否有效();
							if(count >= oldCount){
								canChange = true;
							}
						}
						if(canChange){
							MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
							mw.setTitle(Translate.使用防爆背包替换);
							mw.setDescriptionInUUB(Translate.使用防爆背包替换);
							Option_Confirm_Use_FangBaoKnapsack_Change option = new Option_Confirm_Use_FangBaoKnapsack_Change();
							option.setText(Translate.确定);
							option.setArticleId(articleId);
							option.setIndex(index);
							Option cancel = new Option_UseCancel();
							cancel.setText(Translate.取消);
							Option[] options = new Option[]{option,cancel};
							mw.setOptions(options);
							CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId() , mw.getDescriptionInUUB(), options);
							player.addMessageToRightBag(creq);
							return true;
						}else{
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.新包无法放下原有包的所有物品);
							player.addMessageToRightBag(hreq);
							return false;
						}
					}else{
						ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(articleId,"使用防爆包删除", false);
						if(aee != null){
							
							//统计
							ArticleStatManager.addToArticleStat(player, null, aee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "使用防爆包删除", null);

//							if(ks == null){
//								ks = new Knapsack_TimeLimit(player,count,articleId);
//								knapsack_fangBao_Ids = new long[Player.防爆包最大个数];
//							}
//							else if(ks.length != Player.防爆包最大个数){
//								Knapsack_TimeLimit[] kss = new Knapsack_TimeLimit[Player.防爆包最大个数];
//								long[] knapsack_fangBao_Idss = new long[Player.防爆包最大个数];
//								for(int i = 0; i < ks.length && i < kss.length; i++){
//									kss[i] = ks[i];
//								}
//								for(int i = 0; i < ks.length && i < kss.length; i++){
//									knapsack_fangBao_Idss[i] = knapsack_fangBao_Ids[i];
//								}
//								ks = kss;
//								knapsack_fangBao_Ids = knapsack_fangBao_Idss;
//							}
//							ks[index] = new Knapsack_TimeLimit(player,count,articleId);
							ks = new Knapsack_TimeLimit(player,count,articleId);
							long knapsack_fangBao_Id = articleId;
							player.setKnapsack_fangBao_Id(knapsack_fangBao_Id);
							player.setKnapsacks_fangBao(ks);
							player.notifyAllKnapsack();
							player.notifyKnapsackFB(player);
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)1, Translate.背包使用成功);
							player.addMessageToRightBag(hreq);
							if(AchievementManager.getInstance() != null){
								AchievementManager.getInstance().record(player, RecordAction.获得防爆背包格数, count);
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 判断某个玩家是否可以使用此物品 子类可以重载此方法
	 * 返回null表示可以使用
	 * 返回字符串表示不能使用
	 * 字符串为不能使用的详细信息
	 * @param p
	 * @return
	 */
	public String canUse(Player p) {
		String result = super.canUse(p);
		if(result == null){
			
		}
		return result;
	}

}
