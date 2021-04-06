package com.fy.engineserver.util.datacheck.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.shop.Goods;
import com.fy.engineserver.shop.Shop;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.datacheck.DataCheckHandler;
import com.fy.engineserver.util.datacheck.DataCheckManager;
import com.fy.engineserver.util.datacheck.SendHtmlToMail;

public class ShopCheckHandler implements DataCheckHandler {

	@Override
	public String getHandlerName() {
		return "商店物品检查";
	}

	public static List<String> unCheckGoods = new ArrayList<String>();
	static{
		unCheckGoods.add("追魂魂石");
	}
	
	@Override
	public String[] involveConfigfiles() {
		return new String[] { "shops.xls" };
	}

	@Override
	/**
	 * 商店|物品|描述
	 * 商店名|物品名|不存在
	 * 商店名|物品名|价格偏低
	 * 商店名|物品名|店内id重复
	 */
	public CompoundReturn getCheckResult() {
		CompoundReturn cr = CompoundReturn.create();
		String[] titles = new String[] { "商店", "物品","物品统计", "描述" };
		List<SendHtmlToMail> mailList = new ArrayList<SendHtmlToMail>();
		
		ShopManager sm = ShopManager.getInstance();
		Map<String, Shop> map = sm.getShops();
		List<Goods> allGoods = new ArrayList<Goods>();
		String[] shopType = {"绑银商店","银子商店","工资商店","资源商店","挂机商店","历练商店","功勋商店","文采商店","兑换商店","商店银子","活跃度积分","积分商店","","战勋商店","充值商店","","跨服商店"};
		
		for (Iterator<String> itor = map.keySet().iterator(); itor.hasNext();) {
			String name = itor.next();
			Shop shop = map.get(name);
			if (shop == null) {
				continue;
			}
			List<Goods> list = shop.getGoods();
			List<Integer> gids = new ArrayList<Integer>();
			for(Goods g:list){
				if(g.getColor()==0){
					allGoods.add(g);
				}
			}
			for (Goods g : list) {
				if(g != null && unCheckGoods.contains(g.getArticleName_stat())){
					continue;
				}
				gids.add(g.getId());
				String articleName = g.getArticleName();
				Article article = ArticleManager.getInstance().getArticle(articleName);
				if (article == null) {
					mailList.add(new SendHtmlToMail(titles, new String[] { shop.getLogString()+ "[" + shopType[shop.shopType] + "]", articleName, g.getArticleName_stat(),"物品<font color=red>[" + g.getArticleName_stat() + "]</font>不存在" }));
				}
				
				//判断一个商品店内id出现的次数，超过两次的话就表示有重复
				int countId = 0;
				for(Integer id:gids){
					if(id.intValue() == g.getId()){
						countId++;
						if(countId>1) 
							mailList.add(new SendHtmlToMail(titles, new String[] { shop.getLogString()+ "[" + shopType[shop.shopType] + "]", articleName, g.getArticleName_stat(),"物品<font color=red>[" + g.getArticleName_stat() + "]</font>店内id重复" }));
					}
				}
				
				if(!DataCheckManager.getInstance().isRightColorOfArticle(g.getArticleName(), g.getColor()).getBooleanValue()){
					mailList.add(new SendHtmlToMail(titles, new String[] { shop.getLogString()+ "[" + shopType[shop.shopType] + "]", articleName, g.getArticleName_stat(),"物品<font color=red>[" + g.getArticleName_stat() + "]</font>颜色不对，错误("+DataCheckManager.getInstance().isRightColorOfArticle(g.getArticleName(), g.getColor()).getStringValue()+")" }));
				}
				//比较高品质颜色的价格和用白色合成同等颜色的物品花费是否一致，不考虑武器和装备
				/*int prePrice = 0;
				if(g.getColor()>0&&!(article instanceof Equipment || article instanceof Weapon)){
					for(Goods gg:allGoods){
						if(g.getArticleName().equals(gg.getArticleName())){
							prePrice = gg.getPrice()* (int)Math.pow(5, g.getColor());
							if(g.getPrice() < prePrice){
								mailList.add(new SendHtmlToMail(titles, new String[] { shop.getName()+ "[" + shopType[shop.shopType] + "]", articleName, "物品<font color=red>[" + articleName + "]</font>价格偏低" }));
							}
						} 
					}
				}*/
			}
		}
				
		return cr.setBooleanValue(mailList.size() > 0).setObjValue(mailList.toArray(new SendHtmlToMail[0]));
	}
}
