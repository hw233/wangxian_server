package com.fy.engineserver.datasource.article.manager;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.sprite.Player;

/**
 * 装备，道具，物品说明文字帮助类
 * 
 *
 */
public class AritcleInfoHelper {
//
//	/**
//	 * 孔没有镶嵌宝石时图标
//	 */
//	public static final String ICON_EMPTY_DRILL = "135";
//	
//	public static String [] cooldown = new String[]{"setupTimePercent","coolDownTimePercent"};
//	
//	public static String hujia[] = new String[]{"defenceB"};
//	public static String hujiaNames[] = new String[]{Translate.translate.text_3408};
//	
//	public static String basicProperties[] = new String[]{"strengthB","spellpowerB","dexterityB","constitutionB"};
//	public static String basicPropertyNames[] = new String[]{Translate.translate.text_2369,Translate.translate.text_2367,Translate.translate.text_2370,Translate.translate.text_2368};
//	
//	public static String otherProperties[] = new String[]{
//			"resistanceB","attackRatingB",
//			"dodgeB","criticalHitB","toughness",
//			"meleeAttackIntensityB","spellAttackIntensityB"};
//	
//	public static String otherPropertyNames[] = new String[]{
//		Translate.translate.text_2384,Translate.translate.text_2379,
//		Translate.translate.text_3409,Translate.translate.text_2381,Translate.translate.text_3410,
//		Translate.translate.text_2374,Translate.translate.text_2373};
//	
//	
//	//附加属性说明
//	public static String [] attachment = new String[]{"totalHp","totalMp",
//			"hpRecoverBase","mpRecoverBase",
//			"hpRecoverExtend","mpRecoverExtend",
//			"speed",
//			"attackRating","dodge","criticalHit",
//			"ironMaidenPercent","coolDownTimePercent",
//			"hpStealPercent","mpStealPercent","luck"};
//	
//	public static String[] attachmentNames = new String[]{Translate.translate.text_3411,Translate.translate.text_3412,
//			Translate.translate.text_3413,Translate.translate.text_3414,
//			Translate.translate.text_3415,Translate.translate.text_3416,
//			Translate.translate.text_2382,
//			Translate.translate.text_2478,Translate.translate.text_979,Translate.translate.text_2479,
//			Translate.translate.text_3417,Translate.translate.text_3418,
//			Translate.translate.text_3419,Translate.translate.text_3420,Translate.translate.text_3421};
//	
//	/**
//	 * 获取物品名字，包括颜色和链接的UBB代码
//	 * @param ae
//	 * @return
//	 */
//	public static String getName(ArticleEntity ae){
//		return ae.getArticleName();
//	}
//	
//	public static int getColorType(ArticleEntity ae){
//		
//		ArticleManager am = ArticleManager.getInstance();
//		Article article = am.getArticle(ae.getArticleName());
//		if(article instanceof Gem){
//			Gem gem = (Gem)article;
//			return gem.getColorType();
//		}else{
//			return 0;
//		}
//	}
//	/**
//	 * 格式化时间显示，输入是多少毫秒，输出是多少小时，多少分钟，多少秒
//	 * @param millisTime
//	 * @return
//	 */
//	public static String formatTimeDisplay(long millisTime){
//		StringBuffer sb = new StringBuffer();
//		int r = (int)(millisTime/1000);
//		if(r >= 3600){
//			int k = r/3600;
//			sb.append(k+Translate.translate.text_146);
//			r = r - 3600 * k;
//		}
//		if(r >= 60){
//			int k = r/60;
//			sb.append(k+Translate.translate.text_48);
//			r = r - 60 * k;
//		}
//		if(r > 0 && r < 60){
//			sb.append(r+Translate.translate.text_49);
//		}
//		if(sb.length() == 0){
//			sb.append(Translate.translate.text_3422);
//		}
//		return sb.toString();
//	}
	/**
	 * 获取某个道具的详细信息
	 * @param p
	 * @param pe
	 * @return
	 */
	public static String generate(Player p,ArticleEntity ae){
		return generate(p, ae, false);
	}
	
	public static String generate(Player p,ArticleEntity ae, boolean showInlDet) {
		if(ae == null){
//			ArticleManager.logger.warn("[查询物品出错][道具][实体为空][playerId:"+((p != null ? p.getId():""))+"]["+(p != null ? p.getName():"")+"]");
			if(ArticleManager.logger.isWarnEnabled())
				ArticleManager.logger.warn("[查询物品出错][道具][实体为空][playerId:{}][{}]", new Object[]{((p != null ? p.getId():"")),(p != null ? p.getName():"")});
			return "";
		}
		if (ae instanceof EquipmentEntity) {
			return ((EquipmentEntity)ae).getInfoShow(p, showInlDet);
		}else if(ae instanceof PetPropsEntity){
			return ((PetPropsEntity)ae).getInfoShow(p);
		} else {
			return ae.getInfoShow(p);
		}
	}
//	
//	/**
//	 * 获取某个道具的详细信息
//	 * @param p
//	 * @param pe
//	 * @return
//	 */
//	public static String generate(Player p,ArticleEntity ae){
//		if(ae instanceof PropsEntity){
//			return generate(p,(PropsEntity)ae);
//		}else if(ae instanceof EquipmentEntity){
//			return generate(p,(EquipmentEntity)ae);
//		}
//		if(ae == null){
//			ArticleManager.logger.warn("[查询物品出错][物品][实体为空][playerId:"+(p != null ? p.getId():"")+"]["+(p != null ? p.getName():"")+"]");
//			return "";
//		}
//		return ae.getInfoShow(p);
//	}
//	
//	/**
//	 * 获取某个装备的详细信息，返回的是UBB
//	 * 
//	 * [color=#0000ff]text[/color] 颜色
//	 * [color=red]text[/color] 颜色
//	 * [icon]icon_name[/icon]     图标
//	 * 
//	 * @param p
//	 * @param ee
//	 * @return
//	 */
//	public static String generate(Player p,EquipmentEntity ee){
//		if(ee == null){
//			ArticleManager.logger.warn("[查询物品出错][装备][实体为空][playerId:"+(p != null ? p.getId():"")+"]["+(p != null ? p.getName():"")+"]");
//			return "";
//		}
//		return ee.getInfoShow(p);
//	}
//	
//	/**
//	 * 获取某个道具的详细信息
//	 * @param p
//	 * @param pe
//	 * @return
//	 */
//	public static String generate(Props e){
//		if(e == null){
//			ArticleManager.logger.warn("[查询物品出错][道具][物种为空]");
//			return "";
//		}
//		return e.getInfoShow();
//	}
//	
//	/**
//	 * 获取某个道具的详细信息
//	 * @param p
//	 * @param pe
//	 * @return
//	 */
//	public static String generate(Article e){
//		if(e instanceof Props){
//			return generate((Props)e);
//		}else if(e instanceof Equipment){
//			return generate((Equipment)e);
//		}
//		if(e == null){
//			ArticleManager.logger.warn("[查询物品出错][物品][物种为空]");
//			return "";
//		}
//
//		return e.getInfoShow();
//	}
//	
//	/**
//	 * 获取某个装备的详细信息，返回的是UBB
//	 * 
//	 * [color=#0000ff]text[/color] 颜色
//	 * [color=red]text[/color] 颜色
//	 * [icon]icon_name[/icon]     图标
//	 * 
//	 * @param p
//	 * @param ee
//	 * @return
//	 */
//	public static String generate(Equipment e) {
//		if(e == null){
//			ArticleManager.logger.warn("[查询物品出错][装备][物种为空]");
//			return "";
//		}
//		return e.getInfoShow();
//	}
//	
//	public static IntProperty getIntProperty(Equipment e,int level,String name){
//		if(level < e.ips.length){
//			IntProperty[] ips = e.ips[level];
//			for(int i = 0 ; ips != null && i < ips.length ; i++){
//				if(ips[i] != null){
//					if(ips[i].fieldName.equals(name)){
//						return ips[i];
//					}
//				}
//			}
//			return null;
//		}else{
//			return null;
//		}
//	}
}
