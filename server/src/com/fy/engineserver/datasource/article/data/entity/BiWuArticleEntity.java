package com.fy.engineserver.datasource.article.data.entity;

import com.xuanzhi.tools.cache.CacheSizes;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

/**
 * 比武竞猜物品实体
 *
 */
@SimpleEntity
public class BiWuArticleEntity extends ArticleEntity{
	
	public static final long serialVersionUID = 235454754543050L;
	
	public BiWuArticleEntity(long id){
		this.id = id;
	}
	public BiWuArticleEntity(){
		
	}
	/**
	 * 重写玩家获得物品详细介绍信息
	 * @return
	 */
//	public String getInfoShow(Player player) {
//		StringBuffer sb = new StringBuffer();
//		ArticleManager am = ArticleManager.getInstance();
//		Article e = am.getArticle(articleName);
//		if(e == null){
//			ArticleManager.logger.warn("[查询物品出错][物品] ["+articleName+"] [实体存在物种为空]");
//			return "";
//		}
//		if(e instanceof BiWuJingCaiArticle){
//			sb.append(e.getName());
//			if(e.isHaveValidDays() && e.getMaxValidDays() != 0){
//				if(this.getInValidTime() != 0){
//					if(this.getInValidTime() > com.fy.engineserver.gametime.SystemTime.currentTimeMillis()){
//						sb.append(Translate.translate.text_3461+AritcleInfoHelper.formatTimeDisplay(this.getInValidTime() - com.fy.engineserver.gametime.SystemTime.currentTimeMillis()));
//					}else{
//						sb.append(Translate.translate.text_3462);
//					}
//				}else{
//					sb.append(Translate.translate.text_3463+AritcleInfoHelper.formatTimeDisplay(e.getMaxValidDays()*60000));
//				}
//			}
//
//			if(this.isBinded()){
//				sb.append(Translate.translate.text_3464);
//			}else{
//				if(e.getBindStyle() == Article.BINDTYPE_PICKUP || e.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP){
//					sb.append(Translate.translate.text_3465);
//				}
//			}
//
//			if(e.getDescription() != null && e.getDescription().length() != 0){
//				sb.append("\n[color="+0xffba00+"]"+e.getDescription()+"[/color]");
//			}
//			long signUpNumber = this.getSignUpNumber();
//			long playerId = this.getPlayerId();
//			if(signUpNumber != -1 && playerId != -1){
//				sb.append("\n"+signUpNumber+Translate.translate.text_2258);
//				PlayerManager pm = PlayerManager.getInstance();
//				if(pm != null){
//					try{
//					Player pp = pm.getPlayer(playerId);
//					sb.append("\n[color="+0xffba00+"]"+pp.getName()+"[/color]");
//					}catch(Exception ex){
//					}
//				}
//			}
//		}
//		return sb.toString();
//	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		int size =  super.getSize();
		size += CacheSizes.sizeOfInt();		//leftUsingTimes
		return size;
	}
	

}
