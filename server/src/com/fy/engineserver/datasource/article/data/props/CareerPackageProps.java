package com.fy.engineserver.datasource.article.data.props;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;

/**
 * 特殊道具：包裹
 * 包裹中有一些物品，打开包裹可以得到物品
 *
 */
public class CareerPackageProps extends Props implements Gem{

	/**
	 * 包裹中的物品名字数组
	 */
	private ArticleProperty[][] articleNames;
	
	/**
	 * 绑定标记，打开宝箱后宝箱中的物品是否绑定 0为绑定，1为不管
	 */
	private byte openBindType;

	/**
	 * 包裹打开后提示，恭喜"a"获得了白玉泉*2,
	 * 写在父类里表统一起来的改动会多
	 */
	private String openNotice;
	
	private String openNotice_stat;
	
	/**
	 * 广播频道类型
	 * 0标识2秒就消失的提示窗口，
	 * 1标识信息到聊天栏, 
	 * 2标识在屏幕上方显示文字，持续几秒,最多3条10秒，颜色服务器控制，
	 * 3标识从屏幕中间右向左滚动 字一个个显示一个个消失，颜色服务器控制，
	 * 4标识在屏幕下方从左向右显示滚动信息并带粒子, 2条，变颜色1秒，持续10秒, 
	 * 5覆盖所有窗口上面的提示窗口，一定时间后消失点击不消失，并加入聊天中.
	 */
	private int noticeType;
	
	public String getOpenNotice_stat() {
		return openNotice_stat;
	}

	public void setOpenNotice_stat(String openNotice_stat) {
		this.openNotice_stat = openNotice_stat;
	}

	public ArticleProperty[][] getArticleNames() {
		return articleNames;
	}

	public void setArticleNames(ArticleProperty[][] articleNames) {
		this.articleNames = articleNames;
	}

	public byte getOpenBindType() {
		return openBindType;
	}

	public void setOpenBindType(byte openBindType) {
		this.openBindType = openBindType;
	}

	/**
	 * 打开包裹方法(重写父类方法)
	 * @param player
	 */
	public boolean use(Game game,Player player, ArticleEntity aee){
		if(!super.use(game,player,aee)){
			return false;
		}
		//往玩家背包中放物品
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		boolean bind = true;
		if(this.openBindType == 1){
			bind = false;
		}
		int reason = ArticleEntityManager.CREATE_REASON_USE_PACKAGEPROPS;
		
		StringBuffer 广播物品 = new StringBuffer();
		
		if(am != null && aem != null && articleNames != null){

			ArticleProperty[] ss = articleNames[player.getCareer() - 1];
			
			if(ss != null){
				for(int i = 0; i < ss.length; i++){
					ArticleProperty s = ss[i];
					if(s.isNotice()){
						广播物品.append(","+s.articleName);
					}
					if(s != null){
						Article a = am.getArticle(s.articleName);
						if(a != null){
							ArticleEntity ae = null;
							int color = s.color;
							if(a.isOverlap()){
								try{
									ae = aem.createEntity(a, bind, reason, player, color, s.count, true);
									for(int j = 0; j < s.count; j++){
										player.putToKnapsacks(ae,"职业包裹");
										if(ArticleManager.logger.isInfoEnabled())
											ArticleManager.logger.info("[{}] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),ae.getArticleName(),ae.getId()});
									}
								}catch(Exception ex){
									ex.printStackTrace();
								}
							}else{
								for(int j = 0; j < s.count; j++){
									try{
										ae = aem.createEntity(a, bind, reason, player, color, 1, true);
										player.putToKnapsacks(ae,"职业包裹"); 
										if(ArticleManager.logger.isInfoEnabled())
											ArticleManager.logger.info("[{}] [{}] [{}] [{}] [{}]", new Object[]{player.getUsername(),player.getId(),player.getName(),ae.getArticleName(),ae.getId()});
									}catch(Exception ex){
										ex.printStackTrace();
									}
								}
							}
							
							//统计
							ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, s.count, "职业礼包获得", null);
							if(ae != null){
								player.send_HINT_REQ(Translate.translateString(Translate.恭喜您获得了,new String[][]{{Translate.COUNT_1,s.count+""},{Translate.STRING_1,ae.getArticleName()}}));
							}
						}
					}
				}
				if(广播物品.length()>0){
					String headmess = Translate.translateString(openNotice, new String[][] { { Translate.STRING_1, player.getName()},{ Translate.STRING_2, 广播物品.toString()}});
					ChatMessageService cms = ChatMessageService.getInstance();
					ChatMessage msg = new ChatMessage();
					msg.setMessageText(headmess);
					try {
						if(noticeType==1){
							cms.sendHintMessageToSystem(msg);
						}else if(noticeType==2){
							cms.sendMessageToSystem(msg);
						}else if(noticeType==3){
							cms.sendRoolMessageToSystem(msg);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
				
		}
		return true;
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

		String resultStr = super.canUse(p);
		if(resultStr == null){
			//TODO 判断背包剩余格子数是否满足要求
			if(articleNames != null){
				List<ArticleEntity> aeList = new ArrayList<ArticleEntity>();
				ArticleManager am = ArticleManager.getInstance();
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				int reason = ArticleEntityManager.CREATE_REASON_TEMP_ARTICLE;
				boolean bind = true;
				if(this.openBindType == 1){
					bind = false;
				}
				ArticleProperty[] ss = articleNames[p.getCareer() - 1];
				if(ss != null){
					for(int i = 0; i < ss.length; i++){
						ArticleProperty s = ss[i];
						if(s != null){
							Article a = am.getArticle(s.articleName);
							if(a != null){
								ArticleEntity ae = null;
								int color = s.color;
								for(int j = 0; j < s.count; j++){
									try{
										ae = aem.createTempEntity(a, bind, reason, p, color);
										aeList.add(ae);
									}catch(Exception ex){
										ex.printStackTrace();
									}
								}
							}
						}
					}
				}
				if(!p.putAllOK(aeList.toArray(new ArticleEntity[0]))){
					resultStr = Translate.背包空间不足;
				}
			}
		}
		return resultStr;
	}

	public String getComment(){
		StringBuffer sb = new StringBuffer();
//		sb.append(name+"中包括:\n");
//		if(money != 0){
//			sb.append("游戏币,\n");
//		}
//		if(bindyuanbao != 0){
//			sb.append("绑定元宝,\n");
//		}
//		if(rmbyuanbao != 0){
//			sb.append("人民币元宝,\n");
//		}
//		if(articleNames != null){
//			for(ArticleProperty str : articleNames){
//				sb.append(str.value+"个"+str.articleName+",\n");
//			}
//		}
//		if(sb.indexOf(",") > 0){
//			return sb.substring(0, sb.lastIndexOf(","));
//		}
		return sb.toString();
	}

	public String getOpenNotice() {
		return openNotice;
	}

	public void setOpenNotice(String openNotice) {
		this.openNotice = openNotice;
	}

	public int getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
	}

	
}
