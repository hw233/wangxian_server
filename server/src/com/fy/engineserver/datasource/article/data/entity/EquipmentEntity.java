package com.fy.engineserver.datasource.article.data.entity;

import com.fy.engineserver.articleEnchant.EnchantData;
import com.fy.engineserver.articleEnchant.EnchantManager;
import com.fy.engineserver.articleEnchant.model.EnchantModel;
import com.fy.engineserver.articleProtect.ArticleProtectData;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.articleProtect.PlayerArticleProtectData;
import com.fy.engineserver.datasource.article.concrete.ReadEquipmentExcelManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.equipextra.instance.EquipExtraData;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.equipments.NewSuitEquipment;
import com.fy.engineserver.datasource.article.data.equipments.Weapon;
import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayEntityManager;
import com.fy.engineserver.datasource.article.data.horseInlay.HorseEquInlayManager;
import com.fy.engineserver.datasource.article.data.horseInlay.instance.HorseEquInlay;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.ActiveSkill;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.Utils;
import com.xuanzhi.tools.cache.CacheSizes;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

/**
 * 装备实体
 * 
 */
@SimpleEntity
public class EquipmentEntity extends ArticleEntity {

	public static final String[] 坐骑装备阶 = { Translate.坐骑一阶, Translate.坐骑二阶, Translate.坐骑三阶, Translate.坐骑四阶, Translate.坐骑五阶, Translate.坐骑六阶, Translate.坐骑七阶, Translate.坐骑八阶, Translate.坐骑九阶, Translate.坐骑十阶 };

	public static final long serialVersionUID = 65342346454343L;

	public EquipmentEntity() {
	}

	public EquipmentEntity(long id) {
		this.id = id;
	}

	/**
	 * 装备的强化等级，从0开始
	 */
	@SimpleColumn(name = "equipmentLevel")
	int level = 0;

	/**
	 * 装备的攻防次数，每次攻防(武器攻击或装备防御)attackDefenceCount加1，不用存储
	 */
	transient int attackDefenceCount;
	
	private transient HorseEquInlay horseInlay;
	
	private transient EquipExtraData extraData;

	/**
	 * 装备的耐久值
	 * 
	 * 武器每攻击一次，耐久值就减一
	 * 其他装备每被攻击一次，耐久值就减一
	 * 
	 * 当耐久值为0时，装备就失效了，直到修复
	 */
	@SimpleColumn(saveInterval = 600)
	protected int durability;

	private transient boolean durabilityChangeFlag;

	/**
	 * 星级
	 */
	protected int star;
	public static int 满星 = 20;

	/**
	 * 铭刻标记，true铭刻 false未铭刻
	 */
	protected boolean inscriptionFlag;

	/**
	 * 资质
	 */
	protected int endowments;

	/**
	 * 基本属性值
	 * 基本属性包括：最大血量，外功，内法，外防，内防
	 * 顺序为上面所说顺序，在赋值时会按照顺序进行赋值
	 * 
	 */
	protected int[] basicPropertyValue = new int[5];

	/**
	 * 附加属性值
	 * 附加属性包括：法力值 破甲值 命中值 闪躲值 精准值 会心一击 会心防御
	 * 火攻值 冰攻值 风攻值 雷攻值 火抗值 冰抗值 风抗值 雷抗值
	 * 顺序为上面所说顺序，在赋值时会按照顺序进行赋值
	 * 
	 */

	private static final String[] additionPropertyStrings = new String[] { Translate.法力值, Translate.破甲, Translate.命中, Translate.闪躲, Translate.精准, Translate.暴击, Translate.免暴, Translate.庚金攻击, Translate.葵水攻击, Translate.离火攻击, Translate.乙木攻击, Translate.庚金抗性, Translate.葵水抗性, Translate.离火抗性, Translate.乙木抗性 };

	protected int[] additionPropertyValue = new int[15];

	/**
	 * 镶嵌宝石数组，必须于下面的宝石颜色数组对应，数组长度为可镶嵌宝石数量
	 */
	protected long[] inlayArticleIds = new long[0];

	/**
	 * 镶嵌宝石颜色数组，必须于宝石数组对应
	 */
	protected int[] inlayArticleColors = new int[0];

	/**
	 * 镶嵌器灵数组，必须于下面的器灵类型数组对应，数组长度为可镶嵌器灵数量
	 */
	protected long[] inlayQiLingArticleIds = new long[0];

	/**
	 * 镶嵌器灵类型数组，必须于器灵数组对应
	 */
	protected int[] inlayQiLingArticleTypes = new int[0];

	/**
	 * 制造者
	 */
	protected String createrName = "";

	/**
	 * 精炼经验
	 */
	protected int developEXP;

	/**
	 * 曾经达到的最大星级
	 */
	protected int onceMaxStar;
	/** 装备上的附魔 */
	private transient EnchantData enchantData;
	
	public transient byte enchantType;

	public int[] getBasicPropertyValue() {
		return basicPropertyValue;
	}

	public long[] getInlayArticleIds() {
		return inlayArticleIds;
	}

	public void setInlayArticleIds(long[] inlayArticleIds) {
		this.inlayArticleIds = inlayArticleIds;
		saveData("inlayArticleIds");
	}

	public int[] getInlayArticleColors() {
		return inlayArticleColors;
	}

	public void setInlayArticleColors(int[] inlayArticleColors) {
		this.inlayArticleColors = inlayArticleColors;
		saveData("inlayArticleColors");
	}

	public long[] getInlayQiLingArticleIds() {
		return inlayQiLingArticleIds;
	}

	public void setInlayQiLingArticleIds(long[] inlayQiLingArticleIds) {
		this.inlayQiLingArticleIds = inlayQiLingArticleIds;
		saveData("inlayQiLingArticleIds");
	}

	public int[] getInlayQiLingArticleTypes() {
		return inlayQiLingArticleTypes;
	}

	public void setInlayQiLingArticleTypes(int[] inlayQiLingArticleTypes) {
		this.inlayQiLingArticleTypes = inlayQiLingArticleTypes;
		saveData("inlayQiLingArticleTypes");
	}

	public void setBasicPropertyValue(int[] basicPropertyValue) {
		this.basicPropertyValue = basicPropertyValue;
		saveData("basicPropertyValue");
	}

	public int[] getAdditionPropertyValue() {
		return additionPropertyValue;
	}

	public void setAdditionPropertyValue(int[] additionPropertyValue) {
		this.additionPropertyValue = additionPropertyValue;
		saveData("additionPropertyValue");
	}

	public boolean isDurabilityChangeFlag() {
		return durabilityChangeFlag;
	}

	public void setDurabilityChangeFlag(boolean durabilityChangeFlag) {
		this.durabilityChangeFlag = durabilityChangeFlag;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		saveData("level");
	}

	public int getAttackDefenceCount() {
		return attackDefenceCount;
	}

	public void setAttackDefenceCount(int attackDefenceCount) {
		this.attackDefenceCount = attackDefenceCount;
	}

	public int getDurability() {
		return durability;
	}

	public int getDevelopEXP() {
		return developEXP;
	}

	public void setDevelopEXP(int developEXP) {
		this.developEXP = developEXP;
		saveData("developEXP");
	}

	public void setDurability(int durability) {

		if (this.durability != durability) {
			this.durability = durability;
			if (durability < 0) {
				this.durability = 0;
			}
			durabilityChangeFlag = true;
			saveData("durability");
		}
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
		this.level = star;
		saveData("star");
		saveData("level");
	}

	public boolean isInscriptionFlag() {
		return inscriptionFlag;
	}

	public void setInscriptionFlag(boolean inscriptionFlag) {
		this.inscriptionFlag = inscriptionFlag;
		saveData("inscriptionFlag");
	}

	public int getEndowments() {
		return endowments;
	}

	public static int 满资质 = 5;

	public void setEndowments(int endowments) {
		this.endowments = endowments;
		saveData("endowments");
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
		saveData("createrName");
	}

	/**
	 * 耐久是否有效
	 * @return
	 */
	public boolean isDurability() {
		return durability > 0;
	}
	
	

	public void getInlayInfo(Player p,StringBuffer sb) {
		ArticleManager am = ArticleManager.getInstance();
		if (am != null) {
			for (int i = 0; i < inlayArticleIds.length; i++) {
				sb.append("\n<f color='0xFFFFFF'>").append(Translate.第).append((i + 1)).append(Translate.孔 + ":").append("</f>");
				if (inlayArticleIds[i] > 0) {
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(inlayArticleIds[i]);
					if (ae != null) {
						InlayArticle a = (InlayArticle) am.getArticle(ae.getArticleName());
						if (a == null) {
							continue;
						}
						
						if(a.getInlayType() > 1){
							BaoShiXiaZiData data = ArticleManager.getInstance().getxiLianData(p,inlayArticleIds[i]);
							if(data != null){
								String names[] = data.getNames();
								for(String name : names){
									if(name != null && !name.isEmpty()){
										Article aa = ArticleManager.getInstance().getArticle(name);
										if(aa != null && aa instanceof InlayArticle){
											InlayArticle ia = (InlayArticle)aa;
											int[] props = ia.getPropertysValues();
											if (props != null) {
												for (int j = 0; j < props.length; j++) {
													if (props[j] == 0) {
														continue;
													}
													sb.append("\n<f color='0x00e4ff'>").append(InlayArticle.propertysValuesStrings[j]).append(" + ");
													sb.append(props[j]).append("</f>");
												}
											}
										}
									}
								}
							}
						}else{
							int[] props = a.getPropertysValues();
							if (props != null) {
								for (int j = 0; j < props.length; j++) {
									if (props[j] == 0) {
										continue;
									}
									sb.append("\n<f color='0x00e4ff'>").append(InlayArticle.propertysValuesStrings[j]).append(" + ");
									sb.append(props[j]).append("</f>");
								}
							}
						}
					}
				} else {
					sb.append("<f color='0xFFFFFF'>" + Translate.空 + "</f>");
				}
			}
		}
	}

	public static byte 鉴定 = 0;
	public static byte 铭刻 = 1;
	public static byte 镶嵌 = 2;
	public static byte 挖宝石 = 3;
	public static byte 强化 = 4;

	public static byte 精炼 = 5;
	public static byte 升级 = 6;

	public static byte 摘星 = 7;
	public static byte 装星 = 8;

	public static byte 装备合成 = 9;
	public static byte 拾取到防爆包 = 10;
	public static byte 移动到防爆包 = 11;
	public static byte 移动到仓库 = 12;
	public static byte 绑定 = 13;
	public static byte 器灵附灵 = 14;
	public static byte 器灵摘取 = 15;
	public static byte 装备炼器 = 16;

	public static String[] oprateName = { Translate.鉴定, Translate.铭刻, Translate.镶嵌, Translate.挖宝石, Translate.强化, Translate.精炼, Translate.升级, Translate.摘星, Translate.装星, Translate.装备合成, Translate.拾取到防爆包, Translate.移动到防爆包, Translate.移动到仓库, Translate.绑定, Translate.器灵附灵, Translate.器灵摘取, Translate.装备炼器 };

	/**
	 * 指定操作是否能操作
	 * @param player
	 * @param isQueryisQuery
	 *            是否执行查询操作(查询操作不给人提示)
	 * @param type
	 * @return true能执行指定操作
	 */
	public boolean isOprate(Player player, boolean isQuery, byte type) {

		return true;
	}

	/**
	 * 死亡后必然掉
	 * @return
	 */
	public boolean isDrop() {
		return false;
	}

	/**
	 * 销毁
	 * @param player
	 */
	public void destroyEntity(Player player) {

	}

	/**
	 * 为了特殊装备的排序
	 * @return
	 */
	public long getCreateTime() {
		return -1l;
	}

	public int getOnceMaxStar() {
		return onceMaxStar;
	}

	public void setOnceMaxStar(int onceMaxStar) {
		this.onceMaxStar = onceMaxStar;
		saveData("onceMaxStar");
	}

	/**
	 * 重写玩家获取物品详细信息
	 */
	public static String[] qilingInfoNames = new String[] { Translate.气血器灵, Translate.外攻器灵, Translate.内攻器灵, Translate.外防器灵, Translate.内防器灵, Translate.炎攻器灵, Translate.冰攻器灵, Translate.风攻器灵, Translate.雷攻器灵, Translate.炎防器灵, Translate.水防器灵, Translate.火防器灵, Translate.木防器灵 };
	public static String[] qilingInfoColor = new String[] { "0x20FF2A", "0xFF7200", "0xFF7200", "0x9F9F9F", "0x9F9F9F", "0xF01D1D", "0xB8E7E6", "0xF0EE1D", "0xFC00FF", "0xF01D1D", "0xB8E7E6", "0xF0EE1D", "0xFC00FF" };
	
	public String[] getSimpleEnchantInfo() {
		String[] result = new String[2];
		if (this.getEnchantData() != null && this.getEnchantData().getEnchants().size() > 0) {
			EnchantModel model = EnchantManager.instance.modelMap.get(getEnchantData().getEnchants().get(0).getEnchantId());
			//sb.append("<f color='"+ArticleManager.getColorValue(a, model.getColorType())+"' size='28'>");
			Article a = ArticleManager.getInstance().getArticle(this.getArticleName());
			int colors = ArticleManager.getColorValue(a, model.getColorType());
//			if (this.getEnchantData().isLock()) {
//				colors = 0xA6A6A6;
//			}
			result[0] = "<f color='"+colors+"' size='28'>" + model.getEnchatName() + "</f>";
			result[1] = "<f size='28'>" + Translate.灵性 + ":" + getEnchantData().getEnchants().get(0).getDurable() + "/" + model.getDurable() + "</f>";
		} else {
			result[0] = "";
			result[1] = "";
		}
		return result;
	}
	
	public static String[] colors = new String[]{"0xffff75e6", "0xA6A6A6"};

	public boolean isEnchant(){
		if(enchantData != null){
			return enchantData.getEnchants().size() > 0;
		}
		return false;
	}
	/**
	 * 获取附魔颜色
	 * @return   -1为没附魔
	 */
	public int getEnchantColor() {
		if (enchantData != null && enchantData.getEnchants().size() > 0) {
			int id = enchantData.getEnchants().get(0).getEnchantId();
			EnchantModel em = EnchantManager.instance.modelMap.get(id);
			return em.getColorType();
		}
		return -1;
	}
	
	/**
	 * 
	 * @param player
	 * @param flag  true为装备泡泡  附魔部分全部用粉色
	 * @return
	 */
	public String getEnchantInfoShow(Player player, boolean flag) {
		StringBuffer sb = new StringBuffer();
		try {
			if (this.getEnchantData() != null && this.getEnchantData().getEnchants().size() > 0) {
				EnchantModel model = EnchantManager.instance.modelMap.get(getEnchantData().getEnchants().get(0).getEnchantId());
				String color = colors[0];
				if (this.getEnchantData().isLock()) {
					color = colors[1];
				}
				if (flag && this.getEnchantData().isLock()) {
					sb.append("\n<f color='"+color+"' size='28'>");
					sb.append(Translate.附魔已锁定 + "</f>");
				}/* else if (!flag) {
					sb.append("\n");
				}*/
				
				if (!flag) {
					Article a = ArticleManager.getInstance().getArticle(this.getArticleName());
					sb.append("<f color='"+ArticleManager.getColorValue(a, model.getColorType())+"' size='28'>");
				} else {
					sb.append("\n<f color='"+color+"' size='28'>");
				}
				sb.append(model.getEnchatName()).append("</f>\n");
				if (model != null) {
					if (flag) {
						sb.append("<f color='"+color+"' size='28'>");
					} else {
						sb.append("\n");
						sb.append("<f color='0xFFFF00' size='28'>");
					}
					if (!flag) {
						sb.append(Translate.附魔效果);
					}
					String des = model.getDes();
					if (des.contains("%s")) {
						if (des.contains("%s%%")) {
							float ff = getEnchantData().getEnchants().get(0).getAttrNum() / 10f;
							des = String.format(des, ff);
						} else {
							des = String.format(des, getEnchantData().getEnchants().get(0).getAttrNum());
						}
					}
					sb.append(des).append("</f>\n");
				}
				if (flag) {
					sb.append("<f color='"+color+"' size='28'>");
					sb.append(Translate.灵性).append(":").append(getEnchantData().getEnchants().get(0).getDurable()).append("/").append(model.getDurable());
					sb.append("</f>");
				} else {
					sb.append("\n<f size='28'>");
					sb.append(Translate.灵性).append(":").append(getEnchantData().getEnchants().get(0).getDurable()).append("/").append(model.getDurable());
					sb.append("</f>");
				}
			}
		} catch (Exception e) {
			EnchantManager.logger.warn("[获取装备附魔信息] [异常] [" + player.getLogString() + "] [equipId:" + this.getId() + "]", e);
		}
		return sb.toString();
	}
	
	public String getInfoShow(Player player) {
		return getInfoShow(player, false);
	}
	
	
	public String getInfoShow(Player player, boolean showInlayDet) {
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			return Translate.装备 + articleName;
		}
		Equipment e = (Equipment) am.getArticle(articleName);
		if (e == null) {
			return Translate.物品类型错误;
		}
		StringBuffer sb = new StringBuffer();

		if (this instanceof Special_1EquipmentEntity) {
			Special_1EquipmentEntity sp = (Special_1EquipmentEntity) this;
			if (sp.getSpecialValue() >= 0) {
				sb.append("<f color='0x00FF00' size='28'>").append(Translate.宝魂值).append(":").append(sp.getSpecialValue()).append("</f>\n");
			}
		}
		ArticleManager.logger.warn("[getinfoshow] [name:" + player.getName() + "] [star:" + getStar() + "] []");
		if (e.getPlayerLevelLimit() > 220) {
			sb.append("<f color='0xFFFFFF' size='28'>").append(Translate.装备等级).append(":").append(Translate.仙).append(e.getPlayerLevelLimit() - 220).append("</f>\n");
		} else {
			sb.append("<f color='0xFFFFFF' size='28'>").append(Translate.装备等级).append(":").append(e.getPlayerLevelLimit()).append("</f>\n");
		}
		
		
		for (int i = getStar(); i > 21; i--) {
			sb.append("<i imagePath='/ui/texture_chongwu.png' rectX='165' rectY='180' rectW='36' rectH='35' width='40' height='40'>").append("</i>");
		}

		for (int i = getStar() / 2; --i >= 0;) {
			if (getStar() <= 10) {
				// 星星
				sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='643' rectY='1' rectW='36' rectH='36' width='40' height='40'>").append("</i>");
			} else if (getStar() <= 16) {
				// 月亮
				sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='715' rectY='1' rectW='36' rectH='36' width='40' height='40'>").append("</i>");
			} else if (getStar() <= 20) {
				// 太阳
				sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='787' rectY='1' rectW='36' rectH='36' width='40' height='40'>").append("</i>");
			}
			// sb.append("★");
		}
		if (getStar() % 2 == 1) {
			if (getStar() <= 10) {
				// 星星
				sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='607' rectY='1' rectW='36' rectH='36' width='40' height='40'>").append("</i>");
			} else if (getStar() <= 16) {
				// 月亮
				sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='679' rectY='1' rectW='36' rectH='36' width='40' height='40'>").append("</i>");
			} else if (getStar() <= 20) {
				// 太阳
				sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='751' rectY='1' rectW='36' rectH='36' width='40' height='40'>").append("</i>");
			}
			// sb.append("☆");
			sb.append("\n");
		} else if (getStar() > 0) {
			sb.append("\n");
		}
		
		if (timer != null) {
			sb.append("\n<f color='0x00e4ff'  size='28'>").append(Translate.有效期).append("：");
			long time = timer.getEndTime() - com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			long day = time / 1000 / 60 / 60 / 24;
			long hour = time / 1000 / 60 / 60 % 24;
			if (day > 0) {
				sb.append(day).append(Translate.text_jiazu_114);
			}
			if (hour > 0) {
				sb.append(hour).append(Translate.text_小时);
			}
			if (day <= 0 && hour <= 0) {
				sb.append(Translate.text_不足1小时);
			}
			sb.append("</f>");
		}
		if (getCreaterName() != null && !getCreaterName().equals("")) {
			if (!getCreaterName().equals(player.getName())) {
				setCreaterName(player.getName());
			}
			sb.append("\n<f color='0xFFFF00' size='28'>").append(Translate.已铭刻).append("</f>");
		} else {
			if (inscriptionFlag) {
				sb.append("\n<f color='0xFFFF00' size='28'>").append(Translate.已铭刻).append("</f>");
			} else if (e.getEquipmentType() == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
				// 翅膀目前不显示铭刻。 2013年9月16日17:05:38 康建虎
			} else {
				sb.append("\n<f color='0xFFFF00' size='28'>").append(Translate.未铭刻).append("</f>");
			}
		}
		if (isBinded()) {
			sb.append("\n<f color='0xFFFF00' size='28'>").append(Translate.已绑定).append("</f>");
		} else {
			sb.append("\n<f color='0xFFFF00' size='28'>").append(Translate.未绑定).append("</f>");
		}

		PlayerArticleProtectData data = ArticleProtectManager.instance.getPlayerData(player);
		if (data != null) {
			ArticleProtectData da = data.getProtectData(this);
			if (da != null) {
				String daString = null;
				if (da.getState() == ArticleProtectDataValues.ArticleProtect_State_Common) {
					daString = Translate.以锁魂;
				} else if (da.getState() == ArticleProtectDataValues.ArticleProtect_State_High) {
					daString = Translate.以高级锁魂;
				}
				if (da.getRemoveTime() > 0) {
					daString = Translate.正在解魂;
					long haveTime = da.getRemoveTime() - System.currentTimeMillis();
					if (haveTime < 0) {
						haveTime = 0;
					}
					daString += ":" + (haveTime / 1000 / 60 / 60) + Translate.小时 + (haveTime / 1000 / 60 % 60) + Translate.分钟;
				}
				if (daString != null) {
					sb.append("\n<f color='0xFFFF00' size='28'>").append(daString).append("</f>");
				}
			}
		}
		sb.append(getEnchantInfoShow(player, true));

		int equipmentType = e.getEquipmentType();
		if (equipmentType == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
			makePlayerEquip(e, sb);
		} else if (equipmentType >= 10) {

			if (e.getArticleType() == Article.ARTICLE_TYPE_WEAPON) {
				Weapon weapon = (Weapon) e;
				sb.append("\n<f color='0x00FF00' size='28'>").append(Weapon.WEAPONTYPE_NAME[weapon.getWeaponType()]).append("(").append(CareerManager.careerNameByType(0)).append(")").append("</f>");
			} else {
				sb.append("\n<f color='0x00FF00' size='28'>").append(EquipmentColumn.ALL_EQUIPMENT_TYPE_NAMES[equipmentType]).append("(").append(CareerManager.careerNameByType(0)).append(")").append("</f>");
			}
			// 坐骑装备
			int rank = (e.getClassLimit() * 10 + 1);
//			sb.append("\n<f color='0x00FF00'>").append(Translate.等阶限定).append(":").append(坐骑装备阶[rank]).append("</f>");
			sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.等阶限定).append(":").append(Horse2Manager.getJieJiMess(rank)).append("</f>");
		} else {

			makePlayerEquip(e, sb);
		}
		sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.耐久度).append(":").append(durability).append("/").append(e.getDurability()).append("</f>");
		sb.append("\n");
		sb.append("<f color='0x00FF00' size='28'>");
		sb.append(Translate.基础属性);
		sb.append(":");
		sb.append("</f>");
		sb.append("\n");
		for (int i = 0; i < basicPropertyValue.length; i++) {
			if (basicPropertyValue[i] == 0) {
				continue;
			}
			int value = basicPropertyValue[i];
			if (star >= 0 && star <= ArticleManager.starMaxValue) {
				double jiacheng = 1.0;
				jiacheng += ReadEquipmentExcelManager.strongValues[star];
				if (endowments >= 0 && endowments <= ArticleManager.maxEndowments) {
					jiacheng += ReadEquipmentExcelManager.endowmentsValues[endowments];
				} else if (endowments >= ArticleManager.newEndowments) {
					jiacheng += ((double) (endowments - ArticleManager.newEndowments) / 100);
				}
				if (inscriptionFlag) {
					jiacheng += ReadEquipmentExcelManager.inscriptionValue;
				}
				value = (int) (value * jiacheng);
			}
			switch (i) {
			case 0:
				sb.append("\n<f color='0xFFFFFF' size='28'>").append(Translate.血量).append(":").append(value).append("</f>");
				break;
			case 1:
				sb.append("\n<f color='0xFFFFFF' size='28'>").append(Translate.物理攻击).append(":").append(value).append("</f>");
				break;
			case 2:
				sb.append("\n<f color='0xFFFFFF' size='28'>").append(Translate.法术攻击).append(":").append(value).append("</f>");
				break;
			case 3:
				sb.append("\n<f color='0xFFFFFF' size='28'>").append(Translate.物理防御).append(":").append(value).append("</f>");
				break;
			case 4:
				sb.append("\n<f color='0xFFFFFF' size='28'>").append(Translate.法术防御).append(":").append(value).append("</f>");
				break;
			}
		}
		// 附加属性
		if (this.isTempEntityFlag()) {
			if (e.getEquipmentType() != EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
				sb.append("\n").append(Translate.附加属性随机);
				if (!(this instanceof Special_2EquipmentEntity)) {
					sb.append("\n").append(Translate.宝石孔随机);
				}
			}
		} else {
			int addPropNum = 0;
			for (int i = 0; i < additionPropertyValue.length; i++) {
				if (additionPropertyValue[i] == 0) {
					continue;
				}
				addPropNum++;
			}
			sb.append("\n");
			sb.append("<f color='0x00FF00' size='28'>");
			sb.append(Translate.附加属性).append("(" + addPropNum + "/" + additionPropertyValue.length + "):");
			sb.append("</f>");
			sb.append("\n");
			for (int i = 0; i < additionPropertyValue.length; i++) {
				if (additionPropertyValue[i] == 0) {
					continue;
				}
				int value = (int) (additionPropertyValue[i] * (1 + ReadEquipmentExcelManager.starQuanZhong[star]));
				sb.append("\n<f color='0x00e4ff' size='28'>").append(additionPropertyStrings[i]).append(":").append(value).append("</f>");
			}

			// 宝石
			if (this instanceof Special_2EquipmentEntity) {

			} else {
				if (inlayArticleIds.length > 0) {
					sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.有凹槽).append("(").append(inlayArticleIds.length).append(")</f>");
					sb.append("\n");
					for (int i = 0; i < inlayArticleIds.length; i++) {
						String icon = "";
						if (inlayArticleIds[i] > 0) {
							icon = "/ui/gem" + (inlayArticleColors[i] + 1) + ".png";
						}
						String bgicon = "/ui/slot" + (inlayArticleColors[i] + 1) + ".png";

						sb.append("<i imagePath='").append(icon).append("' bgImagePath='").append(bgicon).append("' width='85' height='85'").append("></i>");
					}
					sb.append("　\n<f> </f>\n");
					for (int i = 0; i < inlayArticleIds.length; i++) {
						if (inlayArticleIds[i] > 0) {
							sb.append("\n<f color='0xFFFFFF' size='28'>").append(Translate.第).append(i + 1).append(Translate.孔).append("</f>");
							ArticleEntity ab = ArticleEntityManager.getInstance().getEntity(inlayArticleIds[i]);
							InlayArticle a = (InlayArticle) am.getArticle(ab.getArticleName());
							if (a == null) {
								return Translate.物品类型错误;
							}
							if (showInlayDet) {
								sb.append("(" + (ab.isBinded() ? "绑定" : "未绑定") + ")");
							}

							if(a.getInlayType() > 1){
								BaoShiXiaZiData baoshidata = ArticleManager.getInstance().getxiLianData(player,inlayArticleIds[i]);
								if(baoshidata != null){
									String names[] = baoshidata.getNames();
									for(String name : names){
										if(name != null && !name.isEmpty()){
											Article aa = ArticleManager.getInstance().getArticle(name);
											if(aa != null && aa instanceof InlayArticle){
												InlayArticle ia = (InlayArticle)aa;
												int[] props = ia.getPropertysValues();
												for (int j = 0; j < props.length; j++) {
													if (props[j] == 0) {
														continue;
													}
													sb.append("\n<f color='0x00e4ff' size='28'>").append(InlayArticle.propertysValuesStrings[j]).append(" + ").append(props[j]).append("</f>");
												}
											}
										}
									}
								}
							}else{
								int[] props = a.getPropertysValues();
								for (int j = 0; j < props.length; j++) {
									if (props[j] == 0) {
										continue;
									}
									sb.append("\n<f color='0x00e4ff' size='28'>").append(InlayArticle.propertysValuesStrings[j]).append(" + ").append(props[j]).append("</f>");
								}
							}
						}
					}
				}
				try {
					HorseEquInlay entity = HorseEquInlayEntityManager.getInst().getEntity(this);
					if (entity != null) {
						int tempNum = 0;
						for (int i=0; i<entity.getInlayColorType().length; i++) {
							if (entity.getInlayColorType()[i] >= 0) {
								tempNum ++ ;
							}
						}
						sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.有凹槽).append("(").append(tempNum).append(")</f>");
						sb.append("\n");
						for (int i=0; i<entity.getInlayColorType().length; i++) {
							if (entity.getInlayColorType()[i] < 0) {
								continue;
							}
							String icon = "";
							if (entity.getInlayArticleIds()[i] > 0) {
								icon = "/ui/inlay/gem" + (entity.getInlayColorType()[i] + 1) + ".png";
							}
							String bgicon = "/ui/inlay/slot" + (entity.getInlayColorType()[i] + 1) + ".png";

							sb.append("<i imagePath='").append(icon).append("' bgImagePath='").append(bgicon).append("'").append("></i>");
						}
						sb.append("\n\n");
						for (int i=0; i<entity.getInlayColorType().length; i++) {
							if (entity.getInlayColorType()[i] < 0) {
								break;//
							}
							if (entity.getInlayArticleIds()[i] <= 0) {
								continue;
							}
							sb.append("\n<f color='0xFFFFFF' size='28'>").append(Translate.第).append(i + 1).append(Translate.孔).append("</f>");
							if (entity.getInlayArticleIds()[i] > 0) {
								ArticleEntity ab = ArticleEntityManager.getInstance().getEntity(entity.getInlayArticleIds()[i]);
								if (showInlayDet) {
									sb.append("(" + (ab.isBinded() ? "绑定" : "未绑定") + ")");
								}
								BaoShiXiaZiData baoshidata = ArticleManager.getInstance().getxiLianData(player,entity.getInlayArticleIds()[i]);
								if(baoshidata != null){
									String names[] = baoshidata.getNames();
									for(String name : names){
										if(name != null && !name.isEmpty()){
											Article aa = ArticleManager.getInstance().getArticle(name);
											if(aa != null && aa instanceof InlayArticle){
												InlayArticle ia = (InlayArticle)aa;
												int[] props = ia.getPropertysValues();
												for (int j = 0; j < props.length; j++) {
													if (props[j] == 0) {
														continue;
													}
													sb.append("\n<f color='0x00e4ff' size='28'>").append(InlayArticle.propertysValuesStrings[j]).append(" + ").append(props[j]).append("</f>");
												}
											}
										}
									}
								}
							} else {
//								sb.append("测试--颜色:" + entity.getInlayColorType()[i]).append("\n");
							}
						}
					}
				} catch (Exception exx) {
					HorseEquInlayManager.logger.warn("[坐骑装备孔泡泡] [异常] [" + this.getId() + "]", e);
				}
			}
		}
		
		{//套装属性
			try{
				if(player.getEquipmentColumns()!=null && player.getEquipmentColumns().currEquipmentName!=null && player.getEquipmentColumns().currEquipmentName.length>0){
					NewSuitEquipment currSuit = null;	//该装备影响的套装
					end:for(NewSuitEquipment ee: ArticleManager.suits){
						String suitPart[] = ee.getSuitPart();
						for(String name:suitPart){
							if(this.getArticleName().equals(name)){
								currSuit = ee;
								break end;
							}	
						}
					}
//					System.out.println("currEquipmentName:"+Arrays.toString(player.getEquipmentColumns().currEquipmentName)+"--请求的名字："+this.getArticleName()+"--"+player.getName());
					if(currSuit!=null && currSuit.getSuitPart()!=null && currSuit.getSuitPart().length>0){
						int currNums = 0;
						StringBuffer sb2 = new StringBuffer();
						for(int i=0;i<currSuit.getSuitPart().length;i++){
							boolean ishas = false;
							currend:for(String name:player.getEquipmentColumns().currEquipmentName){
								if(name.equals(currSuit.getSuitPart()[i])){
									currNums++;
									ishas = true;
									break currend;
								}
							}
							if(!ishas && currSuit.getSuitPart()[i].length()>0){
								sb2.append("<f size='28' color='0x888888' size='28'>").append(currSuit.getSuitPart()[i]).append("</f>").append("\n");
							}
						}
						sb.append("\n<f color='0x00FF00' size='28'>");
						sb.append(Translate.套装属性).append("(" + currNums + "/" + currSuit.getSuitPart().length + "):").append("</f>").append("\n");
						
						for(String str:currSuit.getSuitPart()){
							if(str!=null && str.length()>0){
								for(String ss: player.getEquipmentColumns().currEquipmentName){
									if(ss.equals(str)){
										sb.append("<f size='28' color='0x00e4ff'>").append(str).append("</f>").append("\n");
									}
								}
							}
						}
						sb.append(sb2.toString());
						sb2 = null;
						sb.append("<f color='0x00FF00' size='28'>");
						sb.append(Translate.套装效果).append(":</f>").append("\n");
						
						if(currSuit.getSuitResult()!=null && currSuit.getSuitResult().length>0){
							for(int i=0;i<currSuit.getSuitResult().length;i++){
								if(currSuit.getSuitResult()[i]<=currNums){
									sb.append("<f size='28' color='0x00e4ff' size='28'>").append("(").append(currSuit.getSuitResult()[i]).append(Translate.text_5623).append("):").append("</f>");
								}else{
									sb.append("<f size='28' color='0x888888' size='28'>").append("(").append(currSuit.getSuitResult()[i]).append(Translate.text_5623).append("):").append("</f>");
								}
								int tempCount = 0;
								for(int j=0;j<currSuit.propValues.length;j++){
									if(i<currSuit.propValues[j].length){
										int value = (int)currSuit.propValues[j][i];
										if(value>0){
											if (tempCount >= 1) {
												sb.append("\n").append("<f size='28' color='0x00e4ff' size='28'>").append("           ").append("</f>");
											}
											if(currSuit.getSuitResult()[i]<=currNums){
												sb.append("<f size='28' color='0x00e4ff' size='28'>").append(currSuit.propNames_ch[j]).append(":").append("+").append(value).append("</f> ");
											}else{
												sb.append("<f size='28' color='0x888888' size='28'>").append(currSuit.propNames_ch[j]).append(":").append("+").append(value).append("</f> ");
											}
											tempCount++;
										}
									}
									
								}
								sb.append("\n");
							}
						}
					}
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		
		
		if (e.getEquipmentType() == EquipmentColumn.EQUIPMENT_TYPE_ChiBang) {
			// 翅膀不显示装备资质 2013年9月16日17:08:48 康建虎
		} else if (endowments >= 0 && endowments <= ArticleManager.maxEndowments) {
			sb.append("<f> </f>\n<f color='0x00FF00' size='28'>").append(Translate.装备资质).append(":").append("</f>").append("<f size='28' color='").append(ArticleManager.getEndowmentsColor(endowments)).append("'>").append(ArticleManager.getEndowmentsString(endowments)).append("</f>");
			if (endowments > 0) {
				sb.append("\n<f color='0xFFFF00' size='28'>").append(Translate.基础属性).append(" + ").append((int) (ReadEquipmentExcelManager.endowmentsValues[endowments] * 100)).append("%").append("</f>");
			}
		} else if (endowments >= ArticleManager.newEndowments) {
			String ziZhi = "";
			int ziZhiAdd = 0;
			for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
				int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
				if (value >= endowments - ArticleManager.newEndowments) {
					ziZhiAdd = endowments - ArticleManager.newEndowments;
					ziZhi = ReadEquipmentExcelManager.endowmentsNewNames[kk - 1];
					break;
				}
			}
			sb.append("\n<f> </f>\n<f color='0x00FF00' size='28'>").append(Translate.装备资质).append(":").append("</f>").append("<f color='0xFDA700' size='28'>").append(ziZhi).append("</f>");
			sb.append("\n<f color='0xFFFF00' size='28'>").append(Translate.基础属性).append(" + ").append(ziZhiAdd).append("%").append("</f>");
		}
		if (isInscriptionFlag()) {
			sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.铭刻加成).append(":").append("</f>").append("<f size='28' color='0xFFFF00'>").append(Translate.基础属性).append(" + ").append((int) (ReadEquipmentExcelManager.inscriptionValue * 100)).append("%").append("</f>");
		}
		if (star > 0) {
			if (star <= 20) {
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.炼星加成).append(":").append("</f>").append("<f  size='28' color='0xFFFF00'>").append(star).append(Translate.星).append(Translate.全部属性).append(" + ").append((int) (ReadEquipmentExcelManager.strongValues[star] * 100)).append("%").append("</f>");
			} else {
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.羽化加成).append(":").append("</f>").append("<f  size='28' color='0xFFFF00'>").append(ArticleManager.获得对应的羽化描述.get(new Integer(star))).append(Translate.全部属性).append(" + ").append((int) (ReadEquipmentExcelManager.strongValues[star] * 100)).append("%").append("</f>");
			}

		}
		if (developEXP > 0) {
			sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.装备精炼度).append(":").append("</f>").append("<f  size='28' color='0xFFFF00'>").append((int) (developEXP * 100 / ArticleManager.developEXPUpValue)).append("%").append("</f>");
		}
		if (e.getSkillId() > 0) {
			Skill skill = CareerManager.getInstance().getSkillById(e.getSkillId());
			if (skill != null && skill instanceof ActiveSkill) {
				ActiveSkill activeSkill = (ActiveSkill) skill;
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.技能名称).append(":").append("</f>");
				sb.append("<f color='0xFFFF00' size='28'>").append(skill.getName()).append("</f>");
				if (activeSkill.getMp().length > 0) {
					sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.消耗).append(":").append("</f>");
					sb.append("<f color='0xFFFF00' size='28'>").append(activeSkill.getMp()[0]).append("</f>");
				}
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.释放距离).append(":").append("</f>");
				sb.append("<f color='0xFFFF00' size='28'>").append(activeSkill.getRange() / 5).append(Translate.码).append("</f>");

				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.描述).append(":").append("</f>");
				sb.append("<f color='0xFFFF00' size='28'>").append(skill.getShortDescription()).append("</f>");

				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.方式).append(":").append("</f>");
				if (activeSkill.getDuration1() == 0) {
					sb.append("<f color='0xFFFF00' size='28'>").append(Translate.瞬发).append("</f>");
				} else {
					sb.append("<f color='0xFFFF00' size='28'>").append(Utils.formatTimeDisplay2(activeSkill.getDuration1()) + Translate.吟唱).append("</f>");
				}
				if (activeSkill.getDuration3() > 100) {
					sb.append("\n<f color='0x00FF00' size='28'>").append("cd:").append("</f>");
					sb.append("<f color='0xFFFF00' size='28'>").append(Utils.formatTimeDisplay2(activeSkill.getDuration3()) + " " + Translate.冷却).append("</f>");
				}
			}
		}
		if (this instanceof Special_2EquipmentEntity) {

		} else {
			if (getInlayQiLingArticleTypes().length > 0 && !this.isTempEntityFlag()) {
				int idNums = 0;
				for (int i = 0; i < getInlayQiLingArticleIds().length; i++) {
					if (getInlayQiLingArticleIds()[i] > 0) {
						idNums++;
					}
				}
				sb.append("\n<f> </f>\n<f color='0xFFFF00' size='28'>").append(Translate.器灵槽).append(":").append(idNums).append("/").append(getInlayQiLingArticleTypes().length).append("</f>\n");
				for (int i = 0; i < getInlayQiLingArticleTypes().length; i++) {
					String icon = "";
					if (getInlayQiLingArticleIds()[i] > 0) {
						icon = "/ui/qiling/add" + (getInlayQiLingArticleTypes()[i]) + ".png";
					} else {
						icon = "/ui/qiling/aslot" + (getInlayQiLingArticleTypes()[i]) + ".png";
					}
					sb.append("<i imagePath='").append(icon).append("'").append("></i>");
				}
				sb.append(" \n");
				sb.append(" <f> </f>\n");
				for (int i = 0; i < getInlayQiLingArticleTypes().length; i++) {
					sb.append("\n<f color='").append(qilingInfoColor[getInlayQiLingArticleTypes()[i]]).append("' size='28'>").append(qilingInfoNames[getInlayQiLingArticleTypes()[i]]).append(":");
					if (getInlayQiLingArticleIds()[i] > 0) {
						QiLingArticleEntity entity = (QiLingArticleEntity) ArticleEntityManager.getInstance().getEntity(getInlayQiLingArticleIds()[i]);

						sb.append("+").append(entity.getPropertyValue());
					} else {
						sb.append(Translate.空);
					}
					sb.append("</f>");
				}
			}
		}

		return sb.toString();
	}
	
	/**
	 * 装备洗练时的显示信息
	 * @return int附加属性个数,string面板显示信息
	 */
	public CompoundReturn getAddPropInfoShow(){
		CompoundReturn cr=new CompoundReturn();
		StringBuffer sb=new StringBuffer();
		int addPropNum = 0;
		for (int i = 0; i < additionPropertyValue.length; i++) {
			if (additionPropertyValue[i] == 0) {
				continue;
			}
			addPropNum++;
		}
		cr.setIntValue(addPropNum);
		sb.append("\n");
		sb.append("<f color='0x00FF00' size='28'>");
		sb.append(Translate.附加属性).append("(" + addPropNum + "/" + additionPropertyValue.length + "):");
		sb.append("</f>");
		sb.append("\n");
		for (int i = 0; i < additionPropertyValue.length; i++) {
			if (additionPropertyValue[i] == 0) {
				continue;
			}
			int value = (int) (additionPropertyValue[i] * (1 + ReadEquipmentExcelManager.starQuanZhong[star]));
			sb.append("\n<f color='0x00e4ff' size='28'>").append(additionPropertyStrings[i]).append(":").append(value).append("</f>");
		}
		cr.setStringValue(sb.toString());
		return cr;
	}

	public void makePlayerEquip(Equipment e, StringBuffer sb) {
		if (e.getArticleType() == Article.ARTICLE_TYPE_WEAPON) {
			Weapon weapon = (Weapon) e;
			sb.append("\n<f color='0x00FF00' size='28'>").append(Weapon.WEAPONTYPE_NAME[weapon.getWeaponType()]).append("(").append(CareerManager.careerNameByType(e.getCareerLimit())).append(")").append("</f>");
		} else {
			sb.append("\n<f color='0x00FF00' size='28'>").append(EquipmentColumn.ALL_EQUIPMENT_TYPE_NAMES[e.getEquipmentType()]).append("(").append(CareerManager.careerNameByType(e.getCareerLimit())).append(")").append("</f>");
		}

		sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.境界限定).append(":").append(PlayerManager.classlevel[e.getClassLimit()]).append("</f>");
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		int size = super.getSize();
		size += CacheSizes.sizeOfInt(); // level
		size += CacheSizes.sizeOfInt(); // durability
		size += CacheSizes.sizeOfBoolean(); // durabilityChangeFlag
		// for(int i=0; inlayEntites != null && i<inlayEntites.length; i++) {
		// size += CacheSizes.sizeOfObject(); // level
		// }

		return size;
	}
	public String getLogString(){
		StringBuffer sbf=new StringBuffer();
		long[] inlayArticleIds = this.getInlayArticleIds();
		if (inlayArticleIds.length > 0) {
			for (int j = 0; j < inlayArticleIds.length; j++) {
				if (inlayArticleIds[j] > 0) {
					ArticleEntity inlayArticle = ArticleEntityManager.getInstance().getEntity(inlayArticleIds[j]);
					if (inlayArticle == null) { // 装备上镶嵌的宝石无实体
						sbf.append("{宝石不存在,id:" + inlayArticleIds[j] + "}");
					} else {
						sbf.append("{" + inlayArticle.getShowName() + "/" + inlayArticle.getId() + "/" + inlayArticle.getColorType() + "}");

					}
				}
			}
		}

		long[] inlayQiLingArticleIds = this.getInlayQiLingArticleIds();
		if (inlayQiLingArticleIds.length > 0) {
			for (int j = 0; j < inlayQiLingArticleIds.length; j++) {
				if (inlayQiLingArticleIds[j] > 0) {
					ArticleEntity inlayQiLingArticle = ArticleEntityManager.getInstance().getEntity(inlayQiLingArticleIds[j]);
					if (inlayQiLingArticle == null) { // 装备上镶嵌的器灵无实体
						sbf.append("{器灵不存在,id:" + inlayQiLingArticleIds[j] + "}");
					} else {
						sbf.append("{" + inlayQiLingArticle.getShowName() + "/" + inlayQiLingArticle.getId() + "/" + inlayQiLingArticle.getColorType() + "}");

					}
				}
			}
		}
		return sbf.toString();
	}

	public EnchantData getEnchantData() {
		return enchantData;
	}

	public void setEnchantData(EnchantData enchantData) {
		this.enchantData = enchantData;
		if (this.enchantData != null &&  this.enchantData.getEnchants().size() > 0) {
			EnchantModel model = EnchantManager.instance.modelMap.get(this.enchantData.getEnchants().get(0).getEnchantId());
			if (model != null) {
				enchantType = model.getType();
			}
		}
	}

	public EquipExtraData getExtraData() {
		return extraData;
	}

	public void setExtraData(EquipExtraData extraData) {
		this.extraData = extraData;
	}

	public HorseEquInlay getHorseInlay() {
		return horseInlay;
	}

	public void setHorseInlay(HorseEquInlay horseInlay) {
		this.horseInlay = horseInlay;
	}
}
