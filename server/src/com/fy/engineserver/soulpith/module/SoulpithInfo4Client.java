package com.fy.engineserver.soulpith.module;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fy.engineserver.datasource.article.data.entity.SoulPithArticleEntity;
import com.fy.engineserver.datasource.article.data.soulpith.SoulPithArticle;
import com.fy.engineserver.datasource.article.data.soulpith.SoulPithArticleLevelData;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.soulpith.SoulPithConstant;
import com.fy.engineserver.soulpith.SoulPithEntityManager;
import com.fy.engineserver.soulpith.SoulPithManager;
import com.fy.engineserver.soulpith.instance.PlayerSoulpithInfo;
import com.fy.engineserver.soulpith.instance.SoulPithEntity;
import com.fy.engineserver.soulpith.property.AddPropertyTypes;
import com.fy.engineserver.soulpith.property.Propertys;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;

public class SoulpithInfo4Client {
	/** 已拥有的灵髓点数 */
	private int[] soulNums;
	/** 灵髓宝石镶嵌 (0代表未镶嵌  -1代表未开启)*/
	private long[] inlayInfos;
	/** 灵髓属性面板描述 */
	private String[] description;
	/** 激活的属性 */
	private String[] description2 = new String[0];
	
	
	private int[] basicsoulNums;
	
	public SoulpithInfo4Client() {
	}
	
	public SoulpithInfo4Client(Player player, int soulType) {
		Soul s = player.getSoul(soulType);
		SoulPithLevelModule splm = SoulPithManager.getInst().levelModules.get(SoulPithManager.getPlayerLevel(player, soulType));
		int[] careerSouls = splm.getCareerBaseSoulNum(s.getCareer());
		soulNums = Arrays.copyOf(careerSouls, careerSouls.length);
		basicsoulNums = Arrays.copyOf(careerSouls, careerSouls.length);
		inlayInfos = new long[SoulPithConstant.MAX_FILL_NUM];
		Arrays.fill(inlayInfos, -1);
		for (int i=0; i<SoulPithManager.getPlayerFillNum(player, soulType); i++) {
			inlayInfos[i] = 0;
		}
//		boolean isCurrent = player.getCurrSoul().getSoulType() == soulType;
		SoulPithEntity entity = SoulPithEntityManager.getInst().getEntity(player);
		PlayerSoulpithInfo info = entity.getPlayerSoulpithInfo(soulType);
		Map<Integer, Integer> propertyMaps = new HashMap<Integer, Integer>();
//		inlayInfos = Arrays.copyOf(info.getPiths(), info.getPiths().length);
		for (int i=0; i<info.getPiths().length; i++) {
			long id = info.getPiths()[i];
			if (id > 0) {
				inlayInfos[i] = id;
				SoulPithArticleEntity ae = (SoulPithArticleEntity) ArticleEntityManager.getInstance().getEntity(id);
				SoulPithArticle a = (SoulPithArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
				SoulPithArticleLevelData data = a.getLevelDatas().get(ae.getLevel());
				for (int j=0; j<data.getProperTypes().length; j++) {
					int num = (int) (data.getProperNums()[j] * SoulPithConstant.COLOR_RATE[ae.getColorType()]);			
					int key = data.getProperTypes()[j];
					if (!propertyMaps.containsKey(key)) {
						propertyMaps.put(key, 0);
					}
					int tempNum = propertyMaps.get(key);
					SoulLevelupExpModule sem = SoulPithManager.getInst().soulLevelModules.get(ae.getLevel());
					SoulPithArticleLevelData data2 = a.getLevelDatas().get(ae.getLevel()+1);
					if (ae.getExp() > 0 && data2 != null && ae.getLevel() < SoulPithConstant.SOUL_MAX_LEVEL) {
						int num2 = (int) (data2.getProperNums()[j] * SoulPithConstant.COLOR_RATE[ae.getColorType()]);	
						float rate = (float)ae.getExp() / (float)sem.getNeedExp();
						int addNum = (int)((num2 - num) * rate);
						num += addNum;
					}
					propertyMaps.put(key, tempNum + num);
				}
				for (int j=0; j<data.getTypes().length; j++) {
					soulNums[data.getTypes()[j].getId()] += data.getSoulNums()[j];
				}
			}
		}
		Iterator<Integer> ito = propertyMaps.keySet().iterator();
		description = new String[propertyMaps.size()];
		int tempIndex = 0;
		while (ito.hasNext()){
			int key = ito.next();
			int value = propertyMaps.get(key);
			Propertys p = Propertys.valueOf(key);
			description[tempIndex++] = p.getName() + " +" + value; 
			
		}
		for (int i=0; i<SoulPithManager.getInst().extraAttrs.size(); i++) {
			SoulPithExtraAttrModule module = SoulPithManager.getInst().extraAttrs.get(i);
			if (module.canAdd(soulNums)) {
				for (int j=0; j<module.getAddTypes().length; j++) {
					description2 = Arrays.copyOf(description2, description2.length + 1);
					Propertys p = Propertys.valueOf(module.getAttrTypes()[j]);
					String str = module.getAttrNums()[j] + "";
					if (module.getAddTypes()[j] == AddPropertyTypes.ADD_C_NUM.getIndex()) {
						float f = module.getAttrNums()[j] / 100f;
						str = f + "%";
					}
					description2[description2.length - 1] = p.getName() + " +" + str;
				}
			}
		}
	}
	
	public int[] getSoulNums() {
		return soulNums;
	}
	public void setSoulNums(int[] soulNums) {
		this.soulNums = soulNums;
	}
	public long[] getInlayInfos() {
		return inlayInfos;
	}
	public void setInlayInfos(long[] inlayInfos) {
		this.inlayInfos = inlayInfos;
	}
	public String[] getDescription() {
		return description;
	}
	public void setDescription(String[] description) {
		this.description = description;
	}

	public int[] getBasicsoulNums() {
		return basicsoulNums;
	}

	public void setBasicsoulNums(int[] basicsoulNums) {
		this.basicsoulNums = basicsoulNums;
	}

	public String[] getDescription2() {
		return description2;
	}

	public void setDescription2(String[] description2) {
		this.description2 = description2;
	}
}
