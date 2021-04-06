package com.fy.engineserver.util.datacheck.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.resource.PlantCfg;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;
/**
 * 庄园物品相关检查
 * 
 *
 */
public class CaveCheakHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "庄园物品相关检查";
	}

	@Override
	public String[] involveConfigfiles() {
		return new String[]{"cave.xls"};
	}
	

	@Override
	public CompoundReturn getCheckResult() {
		CompoundReturn cr = CompoundReturn.create();
		String[] titles = new String[] { "植物名称", "名字", "描述" };
		List<SendHtmlToMail> list2 = new ArrayList<SendHtmlToMail>();
		
		FaeryManager fm = FaeryManager.getInstance();
		HashMap<Integer, List<PlantCfg>> plantGradeMap = fm.getPlantGradeMap();
		Iterator<Integer> it = plantGradeMap.keySet().iterator();
		List<PlantCfg> list = null;
		ArticleManager am = ArticleManager.getInstance();
		while(it.hasNext()){
			int level = (Integer)it.next();
			list = plantGradeMap.get(level);
			for(PlantCfg p:list){
				if(p.getOutputType()==0){
//					Article a = am.getArticle(p.getPlantGroup());
//					if(a==null){
//						list2.add(new SendHtmlToMail(titles, new String[] { p.getPlantGroup(), p.getPlantGroup(), "种植物<font color=red>[" + p.getPlantGroup() + "]</font>不存在" }));
//					}
					Article a2 = am.getArticle(p.getOutputName());
					if(a2==null){
						list2.add(new SendHtmlToMail(titles, new String[] { p.getOutputName(), p.getOutputName(), "产出物品<font color=red>[" + p.getOutputName() + "]</font>不存在" }));
					}
				}
			}
		}
		
		return cr.setBooleanValue(list2.size()>0).setObjValue(list2.toArray(new SendHtmlToMail[0]));
	}

}
