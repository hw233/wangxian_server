package com.fy.engineserver.menu.fateActivity;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 仙缘活动  对方同意
 * @author Administrator
 *
 */
public class Option_FateActivity_Agree extends Option {
	
	private long activityId;
	private byte type;
	private int successNum;
	private int level;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
	/* (non-Javadoc)
	 * @see com.fy.engineserver.menu.Option#doSelect(com.fy.engineserver.core.Game, com.fy.engineserver.sprite.Player)
	 */
	@Override
	public void doSelect(Game game, Player player) {
//		
//		FateActivity fa = FateManager.getInstance().getFateActivity(activityId);
//		if(player.getActivityId(type) > 0){
//			player.send_HINT_REQ("你正在进行任务");
//			return;
//		}
//		// 修改状态
//		if(fa != null){
//			Player p1;
//			try {
//				p1 = PlayerManager.getInstance().getPlayer(fa.getInviteId());
//				if(!p1.isOnline()){
//					player.send_HINT_REQ(p1.getName()+"不在线");
//					return;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				return;
//			}
//			synchronized (p1) {
//				if(successNum >= fa.getSuccessNum()){
//					
//					if(fa.getState() == FateActivity.进行状态){
//						player.send_HINT_REQ("对方任务已经开始");
//					}else if(fa.getState() == FateActivity.完成状态){
//						player.send_HINT_REQ("对方任务已经完成");
//					}else{
//						if(successNum == 0){
//							if(p1.getKnapsack_common(Article.KNAP_任务).isFull()){
//								player.send_HINT_REQ(p1.getName()+"任务包裹已满");
//								p1.send_HINT_REQ("任务包裹已满");
//								return ;
//							}else{
//								String propsName = fa.getTemplate().getPropsName();
//								Article a = ArticleManager.getInstance().getArticle(propsName);
//								if(a instanceof FateActivityProps){
//									FateActivityProps fp = (FateActivityProps)a;
//									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(fp, true, ArticleEntityManager.CREATE_REASON_FATEACTIVITY, player, 0);
//									((FateActivityPropsEntity)ae).setActivityId(activityId);
//									ae.setDirty(true);
//									p1.putToKnapsacks(ae);
//									FateManager.logger.info("["+p1.getLogString()+"] [获得仙缘道具] []");
//								}
//							}
//						}
//						fa.setState(FateActivity.选人成功);
//						long temp = fa.getInvitedId();
//						if(temp > 0){
//							try {
//								Player p2 = PlayerManager.getInstance().getPlayer(temp);
//								p2.setActivityId(-1,type);
//								if(p2.isOnline()){
//									p2.send_HINT_REQ(p1.getName()+"重选了人进行任务");
//								}
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
//						player.setActivityId(activityId,type);
//						fa.setInvitedId(player.getId());
//						fa.addSucessNum();
//						fa.setMapName(fa.getTemplate().getMapName());
//						fa.setLevel(level);
//						p1.send_HINT_REQ(player.getName()+"同意了你的请求");
//						
//					}
//				}else{
//					player.send_HINT_REQ("请求过期");
//				}
//			}
//		
//		}
	}

	public long getActivityId() {
		return activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(int successNum) {
		this.successNum = successNum;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
