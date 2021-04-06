package com.fy.engineserver.util.datacheck.handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.equipments.Weapon;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.monster.FlopSet;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

/**
 * 怪物掉落检查
 * 
 * 
 */
public class MonsterFlopCheckHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "怪物掉落检查";
	}

	@Override
	public String[] involveConfigfiles() {
		return new String[] { "monster.xls" };
	}

	public static Map<Integer, int[]> map = new LinkedHashMap<Integer, int[]>();
	
	static {
		map.put(70, new int[]{60,90});
		map.put(90, new int[]{91,110});
		map.put(110, new int[]{111,130});
		map.put(130, new int[]{131,150});
		map.put(150, new int[]{151,170});
		map.put(170, new int[]{171,190});
		map.put(210, new int[]{191,220});
		map.put(230, new int[]{221,240});
		map.put(250, new int[]{241,260});
		map.put(270, new int[]{261,280});
		map.put(290, new int[]{281,300});
	}
	
	/**
	 * 怪物|名字|描述
	 * 
	 * 怪物A|怪物A|怪物A不存在
	 * 怪物A|物品A|物品A不存在
	 */
	@Override
	public CompoundReturn getCheckResult() {
		CompoundReturn cr = CompoundReturn.create();
		String[] titles = new String[] { "怪物", "名字", "描述" };
		List<SendHtmlToMail> list = new ArrayList<SendHtmlToMail>();
		try {
			Field f = MemoryMonsterManager.class.getDeclaredField("monster2FlopListMap");
			f.setAccessible(true);
			LinkedHashMap<String, FlopSet[]> monster2FlopListMap = (LinkedHashMap<String, FlopSet[]>) f.get((MemoryMonsterManager) MemoryMonsterManager.getMonsterManager());

			ArticleManager am = ArticleManager.getInstance();
			MemoryMonsterManager mmm = (MemoryMonsterManager) MemoryMonsterManager.getMonsterManager();

			for (Iterator<String> itor = monster2FlopListMap.keySet().iterator(); itor.hasNext();) {
				String monsterName = itor.next();
				Monster monster = mmm.getMonster(monsterName);
				if (monster == null) {
					list.add(new SendHtmlToMail(titles, new String[] { monsterName, monsterName, "怪物<font color=red>[" + monsterName + "]</font>不存在" }));
				}
				FlopSet[] sets = monster2FlopListMap.get(monsterName);
				for (FlopSet fs : sets) {
					for (String articleName : fs.getArticles()) {
						Article article = am.getArticle(articleName);
						if (article == null) {
							list.add(new SendHtmlToMail(titles, new String[] { monsterName, articleName, "物品<font color=red>[" + articleName + "]</font>不存在" }));
						}
					}
				}
			}
			
			//等级掉落物品是否存在,装备等级段匹配,物品等级段匹配,坐骑装备等级段匹配
			MemoryMonsterManager sm = (MemoryMonsterManager) MemoryMonsterManager.getMonsterManager();
			LinkedHashMap<int[], FlopSet[]> level2FlopListMap = sm.getLevel2FlopListMap();
			Iterator<int[]> it = level2FlopListMap.keySet().iterator();
			while(it.hasNext()){
				int[] levels = (int[])it.next();
				int 配置的级别限制 = 0;
				if(levels.length>0){
					配置的级别限制 = levels[0];
				}
				for(int i=0;i<level2FlopListMap.get(levels).length;i++){
					String articles[] = level2FlopListMap.get(levels)[i].getArticles();
					for(String article : articles){
						Article a = ArticleManager.getInstance().getArticle(article);
						
						if(a==null){
							titles = new String[] { "等级掉落物品", "物品名字", "描述" };
							list.add(new SendHtmlToMail(titles, new String[] { article, article, "物品<font color=red>[" + article + "]</font>不存在" }));
							continue;
						}
							
						if(a instanceof Equipment){
							Equipment q = (Equipment)a;
							int equipmentType = q.getEquipmentType();
							if(equipmentType<10 || equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang){
								if((q.getPlayerLevelLimit()-1)/20!=(配置的级别限制-1)/20){
									titles = new String[] { "等级掉落装备", "装备名字", "描述" };
									list.add(new SendHtmlToMail(titles, new String[] { article, article, "装备<font color=red>[" + article + "]</font>等级段不匹配" }));
								}								
							}else{
								int [] levellimit = map.get(q.getPlayerLevelLimit());
								if (levellimit == null) {
									continue;
								}
								if(q.getPlayerLevelLimit() <70){
									list.add(new SendHtmlToMail(titles, new String[] { article, article, "坐骑装备等级小于70级"}));
								}
								if(配置的级别限制 < levellimit[0] || 配置的级别限制 > levellimit[1]){
									list.add(new SendHtmlToMail(titles, new String[] { article, article, "坐骑装备<font color=red>[" + article + "]</font>等级段不匹配,正确等级范围："+Arrays.toString(levellimit)+"--表中配置的等级范围："+Arrays.toString(levels)+"--装备等级："+q.getPlayerLevelLimit()}));
								}
							}
						}
						
						if(a instanceof Weapon){
							Weapon w = (Weapon)a;
							if((w.getPlayerLevelLimit()-1)/20!=(配置的级别限制-1)/20){
								titles = new String[] { "等级掉落武器", "武器名字", "描述" };
								list.add(new SendHtmlToMail(titles, new String[] { article, article, "武器<font color=red>[" + article + "]</font>等级段不匹配" }));
							}								
						}
						
						if(a.get物品二级分类().equals(Translate.酒)){
							titles = new String[] { "等级掉落酒", "酒名字", "描述" };
							if (配置的级别限制 < 81) {
								if(!article.equals(Translate.白玉泉)){
									list.add(new SendHtmlToMail(titles, new String[] { article, article, "酒<font color=red>[" + article + "]</font>等级段不匹配" }));
								}
							} else if (配置的级别限制 < 161) {
								if(!article.equals(Translate.金浆醒)){
									list.add(new SendHtmlToMail(titles, new String[] { article, article, "酒<font color=red>[" + article + "]</font>等级段不匹配" }));
								}
							} else if(配置的级别限制 < 221){
								if(!article.equals(Translate.香桂郢酒)){
									list.add(new SendHtmlToMail(titles, new String[] { article, article, "酒<font color=red>[" + article + "]</font>等级段不匹配" }));
								}
							}else if(配置的级别限制 < 281){
								if(!article.equals(Translate.琼浆玉液)){
									list.add(new SendHtmlToMail(titles, new String[] { article, article, "酒<font color=red>[" + article + "]</font>等级段不匹配" }));
								}
							}else{
								if(!article.equals(Translate.诸神玉液)){
									list.add(new SendHtmlToMail(titles, new String[] { article, article, "酒<font color=red>[" + article + "]</font>等级段不匹配" }));
								}
							}
						}
					}
				}
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return cr.setBooleanValue(list.size() > 0).setObjValue(list.toArray(new SendHtmlToMail[0]));
	}

}
