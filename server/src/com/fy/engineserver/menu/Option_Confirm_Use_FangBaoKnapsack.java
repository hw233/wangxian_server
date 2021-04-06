package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.data.props.KnapsackExpandProps;
import com.fy.engineserver.datasource.article.data.props.Knapsack_TimeLimit;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;
/**
 * 防爆背包使用确认功能
 * 
 * 
 *
 */
public class Option_Confirm_Use_FangBaoKnapsack extends Option{

	int index;
	
	long articleId;
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	/**
	 * 当oType = OPTION_TYPE_SERVER_FUNCTION时，子类需要实现此方法来实现具体的功能
	 * @param game
	 * @param player
	 */
	public void doSelect(Game game,Player player){
		if(game == null || player == null){
			return;
		}
		if(index >= Player.防爆包最大个数 || index < 0){
			return;
		}
		ArticleManager am = ArticleManager.getInstance();
		if(am != null){
			ArticleEntity ae = player.getArticleEntity(articleId);
			if(ae != null){
				if(ae.getTimer() != null){
					if(ae.getTimer().isClosed()){
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.物品已经到期);
						player.addMessageToRightBag(hreq);
						return;
					}
				}
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
							CONFIRM_WINDOW_REQ creq = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(),mw.getId() , mw.descriptionInUUB, options);
							player.addMessageToRightBag(creq);
							return;
						}else{
							HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.新包无法放下原有包的所有物品);
							player.addMessageToRightBag(hreq);
							return;
						}
					}else{
						synchronized (player.tradeKey) {
							ArticleEntity aee = player.removeArticleEntityFromKnapsackByArticleId(articleId,"使用防爆包删除",false);
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
								HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)1, Translate.背包使用成功);
								player.addMessageToRightBag(hreq);
								return;
							}
						}
					}
				}
			}
		}
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_TUOYUN;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.服务器选项;
	}
}
