package com.fy.engineserver.menu.activity.exchange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.BaseActivityInstance;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
/**
 * 渡劫活动
 * 
 *
 */
public class DuJieActivity extends BaseActivityInstance{

	String activityName = "";
	int chonglevel [] = new int[0];
	String priceNames [] = new String[0];
	int priceColors [] = new int[0];
	int priceNums [] = new int[0];
	String mailTitle = "";
	String mailContent = "";
	int type = 0;
	
	
	public DuJieActivity(String startTime, String endTime, String platForms, String openServerNames, String notOpenServers) throws Exception {
		super(startTime, endTime, platForms, openServerNames, notOpenServers);
	}
	public void setOtherVar(String activityName, int []chonglevel, String[] priceNames, int[] priceColors, int[] priceNums, String mailTitle, String mailContent,int type) {
		this.activityName = activityName;
		this.chonglevel = chonglevel;
		this.priceNames = priceNames;
		this.priceColors = priceColors;
		this.priceNums = priceNums;
		this.mailTitle = mailTitle;
		this.mailContent = mailContent;
		this.type = type;
	}
	
	
	
	@Override
	public String toString() {
		return "DuJieActivity [activityName=" + activityName + ", chonglevel=" + Arrays.toString(chonglevel) + ", priceNames=" + Arrays.toString(priceNames) + ", priceColors=" + Arrays.toString(priceColors) + ", priceNums=" + Arrays.toString(priceNums) + ", mailTitle=" + mailTitle + ", mailContent=" + mailContent + ", type=" + type + "]";
	}
	/**
	 * 返回结果和原因
	 * @param p
	 * @return
	 */
	public CompoundReturn iseffect(Player p){
		CompoundReturn cr = CompoundReturn.create();
		String result = this.isThisServerFit();
		if(result != null) {
			return cr.setBooleanValue(false).setStringValue(result);
		}
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(p.getId());
		if(entity==null){
			return cr.setBooleanValue(false).setStringValue(Translate.获取渡劫信息出错);
		}
		
		int	robberyLevel = entity.getCurrentLevel();
		if(robberyLevel<=0){
			return cr.setBooleanValue(false).setStringValue(Translate.渡劫没开启);
		}
		return cr.setBooleanValue(true).setStringValue("openserver:"+this.getServerfit().getOpenServerNames());
	} 
	
	public boolean doPrice(Player p,int rightChong){
		if(priceNames==null || priceNames.length==0){
			ActivitySubSystem.logger.warn("[渡劫活动] [奖励出错] [priceNames==null] ["+toString()+"] ["+p.getLogString()+"]");
			return false;
		}
		List<ArticleEntity> list = new ArrayList<ArticleEntity>();
		try {
			if(rightChong<0 || rightChong>=priceNames.length){
				ActivitySubSystem.logger.warn("[领取奖励] [失败] [配置的物品数量和重数不匹配] [请确定奖励规则] [rightChong:"+rightChong+"] [priceNames:"+priceNames.length+"]");
				return false;
			}
			Article a = ArticleManager.getInstance().getArticle(priceNames[rightChong]);
			if(a==null){
				ActivitySubSystem.logger.warn("[领取奖励] [失败] [物品不存在："+priceNames[rightChong]+"] [rightChong:"+rightChong+"] [priceNames:"+priceNames.length+"]");
			}
			
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_ACHIEVEMENT, p, priceColors[rightChong], priceNums[rightChong],true);
			
			if(ae==null){
				ActivitySubSystem.logger.warn("[领取奖励] [失败] [创建物品失败："+priceNames[rightChong]+"] [rightChong:"+rightChong+"] [priceNames:"+priceNames.length+"]");
			}
			list.add(ae);
			
			if(list.size()>0){
				MailManager.getInstance().sendMail(p.getId(), list.toArray(new ArticleEntity[]{}), new int[]{priceNums[rightChong]}, mailTitle, mailContent, 0, 0, 0, "渡劫活动");
				ActivitySubSystem.logger.warn("[渡劫活动] [奖励成功] [rightChong:"+rightChong+"] [priceNums:"+Arrays.toString(priceNums)+"] [list:"+list.size()+"] ["+this.toString()+"] ["+p.getLogString()+"]");
				return true;
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.warn("[渡劫活动] [奖励异常] [priceNames==null] ["+toString()+"] ["+p.getLogString()+"]",e);
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public String getInfoShow() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("[活动内容:渡劫奖励][奖励类型:" + this.type + "]");
		sb.append("[奖励:");
		int len = chonglevel.length;
		try {
			for(int i=0; i<len; i++) {
				sb.append("第<" + chonglevel[i] + ">层奖励 <" + priceNames[i] + "><" + priceNums[i] + ">个,颜色:" + priceColors[i] + "]");
				if(i < (len-1)) {
					sb.append("  <br>  [");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			sb.append("] [服务器数据异常:" + e.getStackTrace() +"]");
		}
		return sb.toString();
	}
	
}
