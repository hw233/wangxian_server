package com.fy.engineserver.util.datacheck.handler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.props.ArticleProperty;
import com.fy.engineserver.datasource.article.data.props.CareerPackageProps;
import com.fy.engineserver.datasource.article.data.props.PackageProps;
import com.fy.engineserver.datasource.article.data.props.RandomPackageProps;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

/**
 * 物品相关检查
 * 
 * 
 */
public class ArticleCheckHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "物品相关检查";
	}

	@Override
	public String[] involveConfigfiles() {
		return new String[] { "article.xls" };
	}

	@Override
	public CompoundReturn getCheckResult() {
		// TODO Auto-generated method stub
		CompoundReturn cr = CompoundReturn.create();
		String[] titles = new String[] { "物品类型", "统计名", "物品名字", "结果描述" };
		List<SendHtmlToMail> list = new LinkedList<SendHtmlToMail>();
		ArticleManager am = ArticleManager.getInstance();

		// 随机包厢
		RandomPackageProps[] allRandomPackageProps = am.getAllRandomPackageProps();
		if (allRandomPackageProps != null) {
			for (RandomPackageProps props : allRandomPackageProps) {
				Article a = am.getArticle(props.getName());
				if (a == null) {
					list.add(new SendHtmlToMail(titles, new String[] { "随机宝箱类", props.getName_stat(), props.getName(), "随机宝箱<font color=red>[" + props.getName() + "]</font>不存在" }));
				} else {
					Article a2 = am.getArticleByCNname(props.getName_stat());
					if (a2 == null) {
						list.add(new SendHtmlToMail(titles, new String[] { "随机宝箱类", props.getName_stat(), props.getName_stat(), "随机宝箱<font color=red>[" + props.getName_stat() + "]</font>不存在" }));
					} else {
						if (!a2.getName().equals(props.getName())) {
							list.add(new SendHtmlToMail(titles, new String[] { "随机宝箱类", props.getName_stat(), props.getName() + "--" + a2.getName(), "随机宝箱物品名和统计名对应错误" }));
						}
					}
				}
				ArticleProperty[] apps = props.getApps();
				ArticleProperty[] apps_stat = props.getApps_stat();
				if (apps != null&&apps_stat != null) {
					if (apps.length > 0) {
						if(apps.length==apps_stat.length){
							for (int j=0;j<apps.length;j++) {
								ArticleProperty ap =apps[j];
								Article aa = am.getArticle(ap.getArticleName());
								if (aa == null) {
									list.add(new SendHtmlToMail(titles, new String[] { "随机宝箱类", props.getName_stat(), props.getName(), "随机宝箱里配置的物品<font color=red>[" + ap.getArticleName() + "]</font>不存在" }));
								}else{
									Article aa2 = am.getArticleByCNname(apps_stat[j].getArticleName());
									if (aa2 == null) {
										list.add(new SendHtmlToMail(titles, new String[] { "随机宝箱类", props.getName_stat(), props.getName(), "随机宝箱里配置的物品<font color=red>[" + apps_stat[j].getArticleName() + "]</font>不存在" }));
									} else {
										if (!aa2.getName().equals(aa.getName())) {
											list.add(new SendHtmlToMail(titles, new String[] { "随机宝箱类", props.getName_stat()+"("+apps_stat[j].getArticleName()+")", "基础物品:" + aa2.getName() + "--宝箱中:" + aa.getName(), "物品名和统计名对应错误" }));
										}
									}
								}
							}
						}else{
							list.add(new SendHtmlToMail(titles, new String[] { "随机宝箱类", props.getName_stat(), "物品名num:"+apps.length+"--统计名num:"+apps_stat.length, "随机宝箱里物品名和统计名数量不一致！" }));
						}
					} else {
						list.add(new SendHtmlToMail(titles, new String[] { "随机宝箱类", props.getName_stat(), props.getName(), "随机宝箱里面物品为空！" }));
					}
				}
			}
		}

		// 宝箱类
		PackageProps[] allPackageProps = am.getAllPackageProps();
		for (PackageProps props : allPackageProps) {
			Article a = am.getArticle(props.getName());
			if (a == null) {
				list.add(new SendHtmlToMail(titles, new String[] { "宝箱类", props.getName_stat(), props.getName(), "宝箱<font color=red>[" + props.getName_stat() + "]</font>不存在" }));
			} else {
				Article a2 = am.getArticleByCNname(props.getName_stat());
				if (a2 == null) {
					list.add(new SendHtmlToMail(titles, new String[] { "宝箱类", props.getName_stat(), props.getName_stat(), "宝箱<font color=red>[" + props.getName_stat() + "]</font>不存在" }));
				} else {
					if (!a2.getName().equals(props.getName())) {
						list.add(new SendHtmlToMail(titles, new String[] { "宝箱类", props.getName_stat(), props.getName() + "--" + a2.getName(), "宝箱物品名和统计名对应错误" }));
					}
				}
			}
			ArticleProperty[] articleNames = props.getArticleNames();
			ArticleProperty[] articleNames_stat = props.getArticleNames_stat();
			if (articleNames != null&&articleNames_stat != null) {
				if (articleNames.length > 0) {
					if(articleNames.length==articleNames_stat.length){
						for (int j=0;j<articleNames.length;j++) { 
							ArticleProperty ap=articleNames[j];
							Article aa = am.getArticle(ap.getArticleName());
							if (aa == null) {
								list.add(new SendHtmlToMail(titles, new String[] { "宝箱类", props.getName_stat(), props.getName(), "宝箱里配置的物品<font color=red>[" + ap.getArticleName() + "]</font>不存在" }));
							}else{
								Article aa2 = am.getArticleByCNname(articleNames_stat[j].getArticleName());
								if (aa2 == null) {
									list.add(new SendHtmlToMail(titles, new String[] { "宝箱类", props.getName_stat(), props.getName(), "宝箱里配置的物品<font color=red>[" + ap.getArticleName() + "]</font>不存在" }));
								} else {
									if (!aa2.getName().equals(aa.getName())) {
										list.add(new SendHtmlToMail(titles, new String[] { "随机宝箱类", props.getName_stat()+"("+articleNames_stat[j].getArticleName()+")", "基础物品:" + aa2.getName() + "--宝箱中:" + aa.getName(), "物品名和统计名对应错误" }));
									}
								}
							}
						}
					}else{
						list.add(new SendHtmlToMail(titles, new String[] { "随机宝箱类", props.getName_stat(), "物品名num:"+articleNames.length+"--统计名num:"+articleNames_stat.length, "随机宝箱里物品名和统计名数量不一致！" }));
					}
				} else {
					list.add(new SendHtmlToMail(titles, new String[] { "宝箱类", props.getName_stat(), props.getName(), "宝箱里面物品为空！" }));
				}
			}

		}

		// 等级随机礼包
		// Article[] allArticles = am.getAllArticles();
		// for(Article aa:allArticles){
		// Article a = am.getArticle(aa.getName());
		// if(a!=null){
		// if(a instanceof Props){
		// Props p = (Props)a;
		// if(p instanceof LevelRandomPackage){
		//
		// }
		// }
		// }
		// }

		// 职业包裹
		CareerPackageProps[] allCareerPackageProps = am.getAllCareerPackageProps();
		int career[] = { 0, 1, 2, 3 };
		for (CareerPackageProps props : allCareerPackageProps) {
			Article a = am.getArticle(props.getName());
			if (a == null) {
				list.add(new SendHtmlToMail(titles, new String[] { "职业宝箱类", props.getName_stat(), props.getName(), "职业宝箱<font color=red>[" + props.getName_stat() + "]</font>不存在" }));
			} else {
				Article a2 = am.getArticleByCNname(props.getName_stat());
				if (a2 == null) {
					list.add(new SendHtmlToMail(titles, new String[] { "职业宝箱类", props.getName_stat(), props.getName_stat(), "职业宝箱<font color=red>[" + props.getName_stat() + "]</font>不存在" }));
				} else {
					if (!a2.getName().equals(props.getName())) {
						list.add(new SendHtmlToMail(titles, new String[] { "职业宝箱类", props.getName_stat(), props.getName() + "--" + a2.getName(), "职业宝箱物品名和统计名对应错误" }));
					}
				}

				ArticleProperty[][] articleNames = props.getArticleNames();

				// 职业装备职业是否符合
				for (int i = 0; i < career.length; i++) {
					ArticleProperty[] ap1 = articleNames[i];
					for (ArticleProperty ap : ap1) {
						Article aa = am.getArticle(ap.getArticleName());
						if (aa != null) {
							if (aa instanceof Equipment) {
								Equipment q = (Equipment) aa;
								if (q.getCareerLimit() - 1 != i && q.getCareerLimit() != 0) {
									list.add(new SendHtmlToMail(titles, new String[] { "职业宝箱类", props.getName_stat(), props.getName(), "职业宝箱里装备<font color=red>[" + aa.getName() + "]</font>职业不符" }));
								}
							}
						}
					}
				}

				// 职业宝箱开启的物品是否存在
				for (ArticleProperty[] ap1 : articleNames) {
					for (int i = 0; i < ap1.length; i++) {
						ArticleProperty ap = ap1[i];
						Article aa = am.getArticle(ap.getArticleName());
						if (aa == null) {
							list.add(new SendHtmlToMail(titles, new String[] { "职业宝箱类", props.getName_stat(), props.getName(), "职业宝箱里配置的物品<font color=red>[" + ap.getArticleName() + "]</font>不存在" }));
						} else {
							Article a3 = am.getArticleByCNname(ap.getArticleName_stat());
							if (a3 == null) {
								list.add(new SendHtmlToMail(titles, new String[] { "职业宝箱类", props.getName_stat(), props.getName_stat(), "宝箱<font color=red>[" + ap.getArticleName_stat() + "]</font>不存在" }));
							} else {
								if (!a3.getName().equals(ap.getArticleName())) {
									list.add(new SendHtmlToMail(titles, new String[] { "职业宝箱类", props.getName_stat()+"("+ap.getArticleName_stat()+")", "基础物品:"+ap.getArticleName() + "--宝箱中:" + a3.getName(), "宝箱物品名和统计名对应错误" }));
								}
							}
						}
					}
				}
			}
		}
		
		List<String> allArticleNames = new ArrayList<String>();
		Article[] allArticles = am.getAllArticles();
	    for(Article a : allArticles){
	    	if(a!=null){
	    		if(allArticleNames.contains(a.getName())){
					list.add(new SendHtmlToMail(titles, new String[] { "检查物品重名", a.getName_stat(), a.getName(), "物品<font color=red>[" + a.getName() + "]</font>重名" }));
					ArticleManager.logger.warn("[检查物品重名] [物品:"+a.getName()+"]");
	    		}else{
	    			allArticleNames.add(a.getName());
	    		}
	    	}
	    }

		return cr.setBooleanValue(list.size() > 0).setObjValue(list.toArray(new SendHtmlToMail[0]));

	}

}
