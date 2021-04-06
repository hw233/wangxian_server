package com.fy.engineserver.datasource.article.data.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fy.engineserver.articleProtect.ArticleProtectData;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.articleProtect.PlayerArticleProtectData;
import com.fy.engineserver.combat.CombatCaculator;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeapon;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.model.ShouHunModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.AdditiveAttrModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.HiddenAttrModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponAttrModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponBaseModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponColorUpModule;
import com.fy.engineserver.datasource.article.data.magicweapon.model.TunShiModle;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.CONFIRM_MAGICWEAPON_EAT_REQ;
import com.fy.engineserver.message.CONFIRM_MAGICWEAPON_EAT_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
@SimpleEntity
public class NewMagicWeaponEntity extends ArticleEntity{
	private static final long serialVersionUID = 1L;

	protected long mtimes[];						//法宝的创建日期，神识日期
	
	protected int mcolorlevel;					//决定法宝的品阶(法宝当前等级)
	
	protected String basicpropertyname;			//前缀名（决定基础属性）
	protected transient String basicpropertyname_stat;			//前缀名（统计）
	
	protected String mtitle;						//称号
	
	protected int mstar;							//加持级别(强化等级)
	@SimpleColumn(length = 4000)
	protected List<MagicWeaponAttrModel> additiveAttr = new ArrayList<MagicWeaponAttrModel>();				//附加技能-需要根据配表级法宝等级判断是否生效
	@SimpleColumn(length = 4000)
	protected List<MagicWeaponAttrModel> hideproterty = new ArrayList<MagicWeaponAttrModel>();			//隐藏属性--只保存已经开启的隐藏属性
	/** 基础属性 -固定基本属性只有三个*/
	@SimpleColumn(length = 4000)
	protected MagicWeaponAttrModel[] basiAttr = null;
	/** 耐久度，为0时法宝失效（需要卸载玩家数值） */
	protected int mdurability;
	/** 用来记录此法宝刷新附加技能的次数。*/
	protected int illusionLevel = 0;
	/** 当前经验值 */
	protected long magicWeaponExp;
	/** 法宝变换后背包形象 **/
	public transient String icon = null;
	/** 法宝变换后npc形象 */
	public transient String avatarRace = null;
	/** 法宝变换后npc粒子 */
	public transient String particle = null;
	/** 法宝穿戴时长(此值超过120时清零切扣除一点法宝灵气)--没分钟+1 */
	private int wearTime;
	/** 附加技能附加的属性比例 */
	private transient int extraBasicAttrRate;
	
	public transient Player owner = null;
	
	public NewMagicWeaponEntity(long id) {
		// TODO Auto-generated constructor stub
		super(id);
	}
	public NewMagicWeaponEntity() {
		// TODO Auto-generated constructor stub
	}
	
	public void reinitBaseAttrNum() {
		try {
			if (basiAttr != null && basiAttr.length > 0) {
				for (int i=0; i<basiAttr.length; i++) {
					int tempAttr = MagicWeaponManager.instance.calcMagicweaponAttrNum(basiAttr[i].getAttrNum(), this.getColorType(), this.getMcolorlevel());
					if (extraBasicAttrRate > 0) {
						float tempF = extraBasicAttrRate / 1000f;
						int extraNum = (int) (tempAttr * tempF);
						tempAttr += extraNum;
					}
					basiAttr[i].setNewAttrNum(tempAttr);
				}
			}
		} catch (Exception e) {
			MagicWeaponManager.logger.warn("[重新计算法宝基础属性] [异常] [法宝id:" + this.getId() + "]", e);
		}
	}
	
	/**
	 * 根据强化等级和附加技能计算法宝的额外数值加成---初始化、强化成功、法宝升级都需要调用此方法
	 */
	public void initAttrNum() {
		try{
			extraBasicAttrRate = 0;
			if(additiveAttr != null && additiveAttr.size() > 0) {
				for(int i=0; i<additiveAttr.size(); i++) {
					AdditiveAttrModel am = MagicWeaponManager.instance.mwAxtraAttr.get(additiveAttr.get(i).getId());
					if (this.mcolorlevel >= am.getNeedLevel() && "基础属性增加".equals(am.getAttrType())) {
						extraBasicAttrRate += am.getAttrNum();
					}
				}
			}
			
			this.reinitBaseAttrNum();
			try{
				initHiddenAttr();
			} catch(Exception e) {
				MagicWeaponManager.logger.error("[初始化隐藏属性颜色出错] [法宝id: "  + this.getId() + "]");
			}
			
		//根据强化等级计算出法宝基础属性加成-放入缓存中
			if(basiAttr != null && basiAttr.length > 0) {
				for(int i=0; i<basiAttr.length; i++) {
					int num = MagicWeaponManager.instance.getAidNum(basiAttr[i].getNewAttrNum(), mstar);
					basiAttr[i].setExtraAddAttr(num);
				}
				//根据法宝基础属性类型数值等将附加技能中给法宝自身属性加成的技能换算成具体数值存入缓存中
				if(additiveAttr != null && additiveAttr.size() > 0) {	//没有基础属性不存在
					for(int i=0; i<additiveAttr.size(); i++) {
						AdditiveAttrModel am = MagicWeaponManager.instance.mwAxtraAttr.get(additiveAttr.get(i).getId());
						if(this.mcolorlevel >= am.getNeedLevel()) {
							additiveAttr.get(i).setAct(true);
							int addType = AdditiveAttrModel.getAttrAddType(am.getBaseAttrNumByIndex());
							if(addType == MagicWeaponConstant.add_typePercent4MagicWeapon) {			//增加法宝自身基础属性百分比---其他增加类型需要在计算属性时计算
								for(int j=0; j<basiAttr.length; j++) {
									if(basiAttr[j].getId() == am.getBaseAttrNumByIndex()) {
										int tempN = basiAttr[j].getNewAttrNum() + basiAttr[j].getExtraAddAttr();
										additiveAttr.get(i).setExtraAddAttr(tempN * additiveAttr.get(i).getAttrNum() / 1000);
										break;
									}
								}
							}
						}
					}
				}
			}
			MagicWeapon mw = (MagicWeapon) ArticleManager.getInstance().getArticle(this.getArticleName());
			if(mw != null) {
				if(mw.getChangeLevel() != null && mw.getIllution_icons() != null) {
					if(mw.getChangeLevel().size() == mw.getIllution_icons().size() && mw.getChangeLevel().size() > 0) {
						List<Integer> temp = mw.getChangeLevel();
						for(int i=0; i<temp.size(); i++) {
							if(this.getMcolorlevel() < temp.get(i)) {
								continue;
							}
							icon = mw.getIllution_icons().get(i);
							avatarRace = mw.getIllution_avatars().get(i);
							int ii = i;
							if(mw.getIllution_avatars() != null && mw.getIllution_avatars().size() > 0) {
								if(ii >= mw.getIllution_avatars().size()) {
									ii = mw.getIllution_avatars().size() - 1;
								}
								particle = mw.getIllution_particle().get(ii);
							}
						}
					} else {
						MagicWeaponManager.logger.error("[获取法宝变换后形象出错1] [ "  + mw.getChangeLevel().size() + "] [" + mw.getIllution_icons().size() + "]");
					}
				}
			} else {
				MagicWeaponManager.logger.error("[获取法宝article为空] [ "  + getArticleName() + "]");
			}
		}catch(Exception e) {
			MagicWeaponManager.logger.error("[法宝初始化错误][" + this.getId() + "]", e);
		}
	}
	/**
	 * 初始化隐藏属性颜色
	 */
	public void initHiddenAttr() {
		try {
			if(hideproterty != null && hideproterty.size() > 0) {
				MagicWeapon mw = (MagicWeapon) ArticleManager.getInstance().getArticle(this.getArticleName());
				long list2 []= new long[this.getHideproterty().size()];
				long list3 []= new long[this.getHideproterty().size()];
				HiddenAttrModel hm = MagicWeaponManager.instance.hiddenDataMap.get(mw.getDataLevel());
				for(int i=0; i<this.hideproterty.size(); i++) {
					MagicWeaponAttrModel mm2 = this.getHideproterty().get(i);
					list3[i] = MagicWeaponManager.instance.getHiddenMaxAttr(hm.getAttrNumByType(mm2.getId())[1], this.getColorType(), hm.getAttrNumByType(mm2.getId())[0]);
					list2[i] = mm2.getAttrNum();
					int index = MagicWeaponManager.instance.getHiddenAttrColor((int)list2[i], (int)list3[i]);
					hideproterty.get(i).setColorType(index);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			MagicWeaponManager.logger.error("[初始化隐藏属性颜色出错2] [法宝id: "  + this.getId() + "]",e);
		}
	}
	
	public static String 属性等级颜色 = "<f color='0xffae00'>";
	public static String[] equiptLevelColor = new String[]{"<f color='0xff0000'>" ,"<f color='0xFFFFFF'>"};
	public static String[] additiveColor = new String[]{"<f color='0x5E5E5E'>" ,"<f color='0x00e4ff'>"};

	@Override
	public String getCommonInfoShow(Player player) {
		return this.getInfoShow(player);
	}
	
	@Override
	public String getInfoShow(Player player) {
		// TODO Auto-generated method stub
		try{
//			String testStr = "<i imagePath='/ui/texture_map1n2.png' rectX='787' rectY='1' rectW='36' rectH='36' width='20' height='20'></i>";
			ArticleManager am = ArticleManager.getInstance();
			if (am == null) {
				return Translate.法宝 + articleName;
			}
			MagicWeapon mw = (MagicWeapon) am.getArticle(this.getArticleName());
			if (mw == null) {
				return Translate.物品类型错误;
			}
			StringBuffer sb = new StringBuffer();
			ArticleManager.logger.warn("[getinfoshow] [name:" + player.getName() + "] [star:" + getMstar() + "] []");
			String color = equiptLevelColor[0];
			if(player != null) {
				color = player.getSoulLevel() >= mw.getLevellimit() ? equiptLevelColor[1] : equiptLevelColor[0];
			}
			if (mw.getLevellimit() > 220) {
				sb.append(color).append(Translate.装备等级).append(":").append(Translate.仙).append(mw.getLevellimit() - 220).append("</f>\n");
			} else {
				sb.append(color).append(Translate.装备等级).append(":").append(mw.getLevellimit()).append("</f>\n");
			}
			if (mw.getDataLevel() > 220) {
				sb.append(属性等级颜色).append(Translate.属性等级).append(":").append(Translate.仙).append(mw.getDataLevel() - 220).append("</f>\n");
			} else {
				sb.append(属性等级颜色).append(Translate.属性等级).append(":").append(mw.getDataLevel()).append("</f>\n");
			}
			for (int i = getMstar(); i > 21; i--) {
				sb.append("<i imagePath='/ui/texture_chongwu.png' rectX='165' rectY='180' rectW='36' rectH='35' width='20' height='20'>").append("</i>");
			}
			for (int i = getMstar() / 2; --i >= 0;) {
				if (getMstar() <= 10) {
					// 星星
					sb.append("<i imagePath='/ui/texture_fabao.png' rectX='36' rectY='166' rectW='30' rectH='32' width='20' height='20'>").append("</i>");
				} else if (getMstar() <= 16) {
					// 月亮
					sb.append("<i imagePath='/ui/texture_fabao.png' rectX='96' rectY='166' rectW='30' rectH='32' width='20' height='20'>").append("</i>");
				} else if (getMstar() <= 20) {
					// 太阳
					sb.append("<i imagePath='/ui/texture_fabao.png' rectX='156' rectY='166' rectW='30' rectH='32' width='20' height='20'>").append("</i>");
				}
				// sb.append("★");
			}
			if (getMstar() % 2 == 1) {
				if (getMstar() <= 10) {
					// 星星
					sb.append("<i imagePath='/ui/texture_fabao.png' rectX='6' rectY='166' rectW='30' rectH='32' width='20' height='20'>").append("</i>");
				} else if (getMstar() <= 16) {
					// 月亮
					sb.append("<i imagePath='/ui/texture_fabao.png' rectX='66' rectY='166' rectW='30' rectH='32' width='20' height='20'>").append("</i>");
				} else if (getMstar() <= 20) {
					// 太阳
					sb.append("<i imagePath='/ui/texture_fabao.png' rectX='126' rectY='166' rectW='30' rectH='32' width='20' height='20'>").append("</i>");
				}
				// sb.append("☆");
				sb.append("\n");
			} else if (getMstar() > 0) {
				sb.append("\n");
			}
			if (timer != null) {
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.有效期).append("：");
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
			
			String pinjie = MagicWeaponManager.instance.getJieJiMess(this.getId(),this.getMcolorlevel(),this.getColorType());
			sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.阶级).append(":").append(pinjie).append("</f>");
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
			Article a = ArticleManager.getInstance().getArticle(this.getArticleName());
			byte career = 0;
			if (a instanceof MagicWeapon) {
				career = ((MagicWeapon)a).getCareertype();
			}
			sb.append("\n").append(Translate.法宝).append("(").append(CareerManager.careerNameByType(career)).append(")");
//			sb.append("\n<f color='0x00FF00'>").append(Translate.法宝).append("(").append(CareerManager.careerNameByType(0)).append(")").append("</f>");
			sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.境界限定).append(":").append("</f>").append(PlayerManager.classlevel[mw.getClasslimit()]);
			if(mw.getSoultype() == 0) {
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.元神限制).append(":").append("</f>").append(Translate.本尊);
			} else if(mw.getSoultype() == 1) {
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.元神限制).append(":").append("</f>").append(Translate.元神);
			} else {
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.元神限制).append(":").append("</f>").append(Translate.通用);
			}
			sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.灵气).append(":").append("</f>").append(mdurability).append("/").append(mw.getNaijiudu());
			sb.append("\n");
			sb.append("\n");
			sb.append("<f color='0x00FF00' size='28'>");
			sb.append(Translate.基础属性);
			sb.append(":");
			sb.append("</f>");
//			sb.append("\n");
			if(basiAttr != null && basiAttr.length > 0) {
				if(MagicWeaponManager.logger.isDebugEnabled()) {
					MagicWeaponManager.logger.debug("[法宝] [articlename:" + articleName + "] [" + getBasicpropertyname() + "] [" + player.getLogString() + "]");
				}
				for(MagicWeaponAttrModel attr : basiAttr) {
					if(attr != null) {
						int tempType = MagicWeaponManager.instance.getAddTypeByType(MagicWeaponConstant.basiceAttr, attr.getId());
						if(tempType == MagicWeaponConstant.add_typeNum) {
							String bt = MagicWeaponConstant.mappingKeyValue2.get(attr.getId());
							if(bt == null) {
								MagicWeaponManager.logger.error("[法宝] [ 有属性没有描述 ] [" + attr.getId() + "]");
								continue;
							}
							sb.append("\n<f color='0xFFFFFF' size='28'>").append(bt.replace(hiddentColorStart, "").replace(hiddentColoreND, "").replace(hiddenTempStr, "")).append(":").append(attr.getNewAttrNum()).append("</f>");
							if(attr.getExtraAddAttr() > 0) {
								sb.append("<f color='0x00FF00' size='28'>").append("+").append(attr.getExtraAddAttr()).append("</f>");
							}
							int r = getRateByNumAndType((attr.getNewAttrNum() + attr.getExtraAddAttr()), attr.getId(), player.getSoulLevel(), player.getCareer());
							if(r >= 0) {
								sb.append("<f color='0x00e4ff' size='28'>").append("(").append((float)r / 10L).append("%").append(")").append("</f>");
							}
						} else {
							sb.append("\n<f color='0xFFFFFF' size='28'>").append(MagicWeaponConstant.mappingKeyValue2.get(attr.getId())).append(":").append(attr.getAttrNum()).append("%").append("</f>");
						}
						
					} else {
						MagicWeaponManager.logger.debug("[法宝基础属性不存在] [articlename:" + articleName + "] [" + getBasicpropertyname() + "] [" + player.getLogString() + "]");
					}
				}
			} else {
				sb.append("\n<f color='0x3A5FCD' size='28'>").append(Translate.未神识探查).append("</f>");
			}
			if(additiveAttr != null && additiveAttr.size() > 0) {
				sb.append("\n");
				sb.append("\n");
				sb.append("<f color='0x00FF00' size='28'>");
				sb.append(Translate.附加属性);
				sb.append(":");
				sb.append("</f>");
//				sb.append("\n");
				for(MagicWeaponAttrModel attr : additiveAttr) {
					if(attr != null) {
						AdditiveAttrModel amm = MagicWeaponManager.instance.mwAxtraAttr.get(attr.getId());
						String bt = MagicWeaponConstant.mappingKeyValue2.get(amm.getBaseAttrNumByIndex());
						if(bt == null) {
							MagicWeaponManager.logger.error("[法宝] [ 有属性没有描述 ] [" + attr.getId() + "]");
							continue;
						}
						int tempType = MagicWeaponManager.instance.getAddTypeByType(MagicWeaponConstant.addtiveAttr, amm.getBaseAttrNumByIndex());
						String str = bt.replace(hiddentColorStart, "").replace(hiddentColoreND, "").replace(hiddenTempStr, "");
						if(tempType == MagicWeaponConstant.add_typePercent4MagicWeapon) {
							str = str + "("+Translate.增加法宝自身属性+")";
						}
						if(getMcolorlevel() >= amm.getNeedLevel()) {
							sb.append("\n").append(additiveColor[1]).append(str).append(":").append((float)attr.getAttrNum()/10L).append("%").append("</f>");
							try {
								if(tempType == MagicWeaponConstant.add_typePercent4MagicWeapon && attr.getExtraAddAttr() > 0) {
									sb.append("<f color='0x00FF00' size='28'>").append("(+").append(attr.getExtraAddAttr()).append(")").append("</f>");
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								MagicWeaponManager.logger.error("[附加属性出错][" + this.getId() + "]", e);
							}
						} else {
							sb.append("\n").append(additiveColor[0]).append(str).append(":").append((float)attr.getAttrNum()/10L).append("%").append("</f>");
							String pinjieMess = MagicWeaponManager.getJieJiMess(amm.getNeedLevel());
							String msg = Translate.translateString(Translate.达到XX后开启, new String[][] {{Translate.STRING_1, pinjieMess}});
							sb.append("\n<f color='0xFFFF00' size='28'>").append("      (").append(msg).append(")").append("</f>");
						}
					} else {
						MagicWeaponManager.logger.debug("[法宝基础属性不存在] [articlename:" + articleName + "] [" + getBasicpropertyname() + "] [" + player.getLogString() + "]");
					}
				}
			}
			MagicWeaponBaseModel mbm = MagicWeaponManager.instance.mwBaseModel.get(getColorType());
			if(mbm != null && mbm.getHiddenAttrNum() > 0) {
				sb.append("\n");
				sb.append("\n");
				sb.append("<f color='0x00FF00' size='28'>");
				int len = this.hideproterty.size();
				sb.append(Translate.隐藏属性).append("(" + len + "/" +mbm.getHiddenAttrNum() + ")");
				sb.append(":");
				sb.append("</f>");
//				sb.append("\n");
				for(int i=0; i<mbm.getHiddenAttrNum(); i++) {
					if(i < len) {
						MagicWeaponAttrModel attr = hideproterty.get(i);
						try {
							String bt = MagicWeaponConstant.mappingKeyValue2.get(attr.getId());
							if(bt == null) {
								MagicWeaponManager.logger.error("[法宝] [ 有属性没有描述 ] [" + attr.getId() + "]");
								continue;
							}
							int tempType = MagicWeaponManager.instance.getAddTypeByType(MagicWeaponConstant.hiddenAttr, attr.getId());
							HiddenAttrModel hm = MagicWeaponManager.instance.hiddenDataMap.get(mw.getDataLevel());
							int maxAttrNum = MagicWeaponManager.instance.getHiddenMaxAttr(hm.getAttrNumByType(attr.getId())[1], this.getColorType(), hm.getAttrNumByType(attr.getId())[0]);
							int hindex = MagicWeaponManager.instance.getHiddenAttrColor(attr.getAttrNum(),maxAttrNum);
							String ts = bt.replace(hiddentColorStart, hiddenColorStart[hindex]).replace(hiddentColoreND, hiddenColorEnd).replace(hiddenTempStr, hiddenReplaceStr);
							if(tempType == MagicWeaponConstant.add_typeNum) {
								sb.append("\n").append(ts).append(hiddenColorStart[hindex]).append("  +").append(attr.getAttrNum()).append("</f>");
//								sb./*append("\n<f color='0xFFFFFF'>").*/append(bt.replace("　", testStr)).append(":").append(attr.getAttrNum())/*.append("</f>")*/;
							} else {
								float bb = (float)attr.getAttrNum() / (float)10;
								sb.append("\n").append(ts).append(hiddenColorStart[hindex]).append("  +").append(bb).append("%").append("</f>");
//								sb.append("\n<f color='0xFFFFFF'>").append(bt.replace("　", testStr)).append(":").append(bb).append("%")/*.append("</f>")*/;
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							sb.append("\n<f color='0x5E5E5E' size='28'>").append(Translate.未激活).append("</f>");
						}
					} else {
						sb.append("\n<f color='0x5E5E5E' size='28'>").append(Translate.未激活).append("</f>");
					}
				}
			}
			if (owner != null && owner.getHuntLifr() != null && owner.getEquipmentColumns().get(11) != null && owner.getEquipmentColumns().get(11).getId() == this.getId() && this.getMdurability() > 0) {
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.兽魂属性).append("</f>");
				for (int i=0; i<owner.getHuntLifr().getHuntDatas().length; i++) {
					if (owner.getHuntLifr().getHuntDatas()[i] <= 0) {
						continue ;
					}
					HuntLifeArticleEntity ae = (HuntLifeArticleEntity) DefaultArticleEntityManager.getInstance().getEntity(owner.getHuntLifr().getHuntDatas()[i]);
					int lv = ae.getExtraData().getLevel();
					ShouHunModel model = HuntLifeManager.instance.shouhunModels.get(lv);
					int addNum = model.getAddAttrNums()[i];
					if (owner.getShouhunAttr() > 0) {
						int extraNum = (int) ((player.getShouhunAttr() / 100f) * addNum);
						addNum += extraNum;
					}
					sb.append("\n<f size='28'>").append(HuntLifeManager.对应增加属性描述[i]).append(":").append(addNum).append("</f>");
				}
			}
			return sb.toString();
		}catch(Exception e) {
			MagicWeaponManager.logger.error("出错", e);
			return "错误数据！！";
		}
	}
	/** 隐藏属性文字颜色 */
	public static String hiddentColorStart = "(";
	public static String hiddentColoreND = ")";
	public static String[] hiddenColorStart = new String[]{"<f color='0xffffffff'>", "<f color='0xff00ff00'>", "<f color='0xff0000ff'>", "<f color='0xffE706EA'>", "<f color='0xffFDA700'>"};
	public static String hiddenColorEnd = "</f>";
	/** 隐藏属性替换图标 */
	public static String hiddenTempStr = "&";
	public static String hiddenReplaceStr = "<i imagePath='/ui/texture_map1n2.png' rectX='787' rectY='1' rectW='36' rectH='36' width='23.7' height='20'></i>";
	
	/**
	 * 根据属性类型和值计算出比率（千分比）
	 * @return
	 */
	public static int getRateByNumAndType(int x, int type, int level, int career) {
		int result = -1;
		switch (type) {
//		case MagicWeaponConstant.hpNum: 
//			break;
//		case MagicWeaponConstant.physicAttNum:
//			break;
//		case MagicWeaponConstant.magicAttNum: 
//			break;
//		case MagicWeaponConstant.physicDefanceNum:
//		case MagicWeaponConstant.magicDefanceNum:
//			break;
		case MagicWeaponConstant.hitNum: 
			result = CombatCaculator.CAL_命中率(x, level) - 800;
			break;
		case MagicWeaponConstant.dodgeNum: 
			result = CombatCaculator.CAL_闪避率(x, level, career) - 50;
			break;
		case MagicWeaponConstant.cirtNum: 
			result = CombatCaculator.CAL_会心一击率(x, level) - 50;
			break;
		}
		return result;
	}
	
	/**
	 * 法宝添加经验
	 * @param exp
	 * @param reason
	 * @return
	 */
	public CONFIRM_MAGICWEAPON_EAT_RES addExp(CONFIRM_MAGICWEAPON_EAT_REQ req,Player p , long exp) {
		long exps = exp;
		if(p!=null && exps>0){
			try{
				long oldexp = this.getMagicWeaponExp();
				int level = this.getMcolorlevel();
				MagicWeaponColorUpModule module = null;
				if (this.getColorType() >= 3) {
					module = MagicWeaponManager.instance.colorUpMaps.get(this.getColorType());
				}
				while(exps>0){
					long nextUpgrageExp = MagicWeaponManager.instance.getUpgradeExps(level+1,this.getColorType());
					MagicWeaponManager.logger.warn("[法宝吞噬确认==]  [法宝id:"+this.getId()+"] [exp:"+exp+"] [nextUpgrageExp:"+nextUpgrageExp+"] [exps:"+exps+"] [level:"+level+"] [color:"+this.getColorType()+"]");
					if (MagicWeaponManager.法宝阶飞升是否开启 &&  module != null && module.isBreakLv(level)) {
						p.sendError(Translate.需要突破才能继续吞噬升级法宝);
						break;
					}
					if(nextUpgrageExp>0 && exps-nextUpgrageExp>0){
						exps-=nextUpgrageExp;
						level++;
					}else{
						break;
					}
				}
				int oldlevel = this.getMcolorlevel();
				
				
				if(exps<0){
					exps=0;
				}
				
				if(level<0){
					level = 0;
				}
				
				if(level >= MagicWeaponManager.instance.getMaxP(this.getColorType())){
					level = MagicWeaponManager.instance.getMaxP(this.getColorType());
				}
				
				this.setMagicWeaponExp(exps);
				this.setMcolorlevel(level);
				
				int temp = level - oldlevel;
				if(temp > 0) {
					for(int i=0; i<basiAttr.length; i++) {
						if(basiAttr[i] != null) {
							basiAttr[i].setAttrNum(MagicWeaponManager.instance.getDevelAttrNum(basiAttr[i].getAttrNum(), getColorType(), level, oldlevel));
						} 
					}
					saveData("basiAttr");
				}
				initAttrNum();
				
				String currjiejimess = MagicWeaponManager.instance.getJieJiMess(this.getId(),oldlevel,this.getColorType());
				String nextjiejimess = MagicWeaponManager.instance.getJieJiMess(this.getId(),this.getMcolorlevel(),this.getColorType());
				//动画数据处理
				TunShiModle ts [] = new TunShiModle[temp];
				int lv = oldlevel+1;
				int allexps = 0;
				for(int i=0;i<temp;i++){
					TunShiModle t = new TunShiModle();
					long nextUpgrageExp = MagicWeaponManager.instance.getUpgradeExps(lv,this.getColorType());
					allexps+=nextUpgrageExp;
					if(allexps>0 && exp-allexps>=0){
						t.upgradEexps = nextUpgrageExp;
						if(i==0){
							if(this.getMagicWeaponExp()>0){
								t.startNums = oldexp;
							}
						}
						t.endNums = nextUpgrageExp;
						lv++;
					}else{
						t.upgradEexps = nextUpgrageExp;
						t.startNums = 0;
						t.endNums = this.getMagicWeaponExp();
					}
					ts[i] = t;
				}
				
				for(TunShiModle t:ts){
					MagicWeaponManager.logger.warn("[法宝吞噬请求] [信息测试] ["+t.toString()+"]");	
				}
				MagicWeaponManager.logger.warn("[法宝添加经验] [req:"+(req==null?"null":req.getTypeDescription())+"] [法宝id:"+this.getId()+"] [level:"+level+"] [exp:"+exp+"] [currjiejimess:"+currjiejimess+"] [nextjiejimess:"+nextjiejimess+"] [basiAttr:"+(basiAttr==null)+"] [exp:"+exp+"] [升级："+oldlevel+"] [升级后："+this.getMcolorlevel()+"] [剩余经验："+exps+"] ["+p.getName()+"]");
				return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), true, ts);
			}catch(Exception e){
				e.printStackTrace();
				MagicWeaponManager.logger.warn("[法宝添加经验] [异常] ["+e+"]");
			}
		}
		return new CONFIRM_MAGICWEAPON_EAT_RES(req.getSequnceNum(), false, new TunShiModle[0]);
	}
	
	public String getExtraAttrDes(Player player) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("\n");
		sb.append("<f color='0x00FF00' size='28'>");
		sb.append(Translate.附加属性);
		sb.append(":");
		sb.append("</f>");
		if(additiveAttr != null && additiveAttr.size() > 0) {
//			sb.append("\n");
			for(MagicWeaponAttrModel attr : additiveAttr) {
				if(attr != null) {
					AdditiveAttrModel amm = MagicWeaponManager.instance.mwAxtraAttr.get(attr.getId());
					String bt = MagicWeaponConstant.mappingKeyValue2.get(amm.getBaseAttrNumByIndex());
					if(bt == null) {
						MagicWeaponManager.logger.error("[法宝] [ 有属性没有描述 ] [" + attr.getId() + "] ["+amm.getBaseAttrNumByIndex()+"]");
						continue;
					}
					int tempType = MagicWeaponManager.instance.getAddTypeByType(MagicWeaponConstant.addtiveAttr, amm.getBaseAttrNumByIndex());
					String str = bt.replace(hiddentColorStart, "").replace(hiddentColoreND, "").replace(hiddenTempStr, "");
					if(tempType == MagicWeaponConstant.add_typePercent4MagicWeapon) {
						str = str + "("+Translate.增加法宝自身属性+")";
					}
					if(getMcolorlevel() >= amm.getNeedLevel()) {
						sb.append("\n").append(additiveColor[1]).append(str).append(":").append((float)attr.getAttrNum()/10L).append("%").append("</f>");
						try {
							if(tempType == MagicWeaponConstant.add_typePercent4MagicWeapon && attr.getExtraAddAttr() > 0) {
								sb.append("<f color='0x00FF00' size='28'>").append("(+").append(attr.getExtraAddAttr()).append(")").append("</f>");
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							MagicWeaponManager.logger.error("[附加属性出错][" + this.getId() + "]", e);
						}
					} else {
						sb.append("\n").append(additiveColor[0]).append(str).append(":").append((float)attr.getAttrNum()/10L).append("%").append("</f>");
						String pinjieMess = MagicWeaponManager.getJieJiMess(amm.getNeedLevel());
						String msg = Translate.translateString(Translate.达到XX后开启, new String[][] {{Translate.STRING_1, pinjieMess}});
						sb.append("\n<f color='0xFFFF00' size='28'>").append("      (").append(msg).append(")").append("</f>");
					}
				} else {
					MagicWeaponManager.logger.debug("[法宝基础属性不存在] [articlename:" + articleName + "] [" + getBasicpropertyname() + "] [" + player.getLogString() + "]");
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 法宝突破升阶时调用，计算法宝基础属性
	 */
	public void saveAutoAddBasicData(int oldLv) {
		try {
			int lv = this.getMcolorlevel();
			for(int i=0; i<basiAttr.length; i++) {
				if(basiAttr[i] != null) {
					basiAttr[i].setAttrNum(MagicWeaponManager.instance.getDevelAttrNum(basiAttr[i].getAttrNum(), getColorType(), lv, oldLv));
				} 
			}
			saveData("basiAttr");
			MagicWeaponManager.logger.warn("[法宝突破] [重新计算属性] [成功] [" + this.getId() + "] [lv:" + lv + "] [oldLv:" + oldLv + "]");
		} catch (Exception e) {
			MagicWeaponManager.logger.warn("[法宝突破] [异常] ["+e+"]");
		}
	}
	
	@Override
	public String toString() {
		return "NewMagicWeaponEntity [times=" + Arrays.toString(mtimes) + ", colorlevel=" + mcolorlevel + ", basicpropertyname=" + basicpropertyname + ", star=" + mstar + ", additiveAttr=" + additiveAttr + ", hideproterty=" + hideproterty + ", basiAttr=" + Arrays.toString(basiAttr) + ", durability=" + mdurability + ", illusionLevel=" + illusionLevel + ", magicWeaponExp=" + magicWeaponExp + "]";
	}

	public long getMagicWeaponExp() {
		return magicWeaponExp;
	}

	public void setMagicWeaponExp(long magicWeaponExp) {
		this.magicWeaponExp = magicWeaponExp;
		saveData("magicWeaponExp");
	}

	public long[] getMtimes() {
		return mtimes;
	}

	public void setMtimes(long[] times) {
		this.mtimes = times;
		saveData("mtimes");
	}

	public int getIllusionLevel() {
		return illusionLevel;
	}

	public void setIllusionLevel(int illusionLevel) {
		this.illusionLevel = illusionLevel;
		saveData("illusionLevel");
	}

	public int getMcolorlevel() {
		return mcolorlevel;
	}

	public void setMcolorlevel(int colorlevel) {
		this.mcolorlevel = colorlevel;
		saveData("mcolorlevel");
	}

	public String getBasicpropertyname() {
		return basicpropertyname;
	}

	public void setBasicpropertyname(String basicpropertyname) {
		this.basicpropertyname = basicpropertyname;
		saveData("basicpropertyname");
	}

	public String getMtitle() {
		return mtitle;
	}

	public void setMtitle(String title) {
		this.mtitle = title;
		saveData("mtitle");
	}

	public int getMstar() {
		return mstar;
	}

	public void setMstar(int star) {
		try {
			if (this.mstar != star) {
				this.initAttrNum();
			}
		} catch (Exception e) {
			
		}
		this.mstar = star;
		saveData("mstar");
	}

	public int getMdurability() {
		return mdurability;
	}

	public void setMdurability(int durability) {
		this.mdurability = durability;
		saveData("mdurability");
	}

	public List<MagicWeaponAttrModel> getAdditiveAttr() {
		return additiveAttr;
	}

	public void setAdditiveAttr(List<MagicWeaponAttrModel> additiveAttr) {
		this.additiveAttr = additiveAttr;
		saveData("additiveAttr");
	}

	public List<MagicWeaponAttrModel> getHideproterty() {
		return hideproterty;
	}

	public void setHideproterty(List<MagicWeaponAttrModel> hideproterty) {
		this.hideproterty = hideproterty;
		saveData("hideproterty");
	}

	public MagicWeaponAttrModel[] getBasiAttr() {
		return basiAttr;
	}

	public void setBasiAttr(MagicWeaponAttrModel[] basiAttr) {
		this.basiAttr = basiAttr;
		saveData("basiAttr");
	}
	public int getWearTime() {
		return wearTime;
	}
	public void setWearTime(int wearTime) {
		this.wearTime = wearTime;
		saveData("wearTime");
	}
	public String getBasicpropertyname_stat() {
		return basicpropertyname_stat;
	}
	public void setBasicpropertyname_stat(String basicpropertyname_stat) {
		this.basicpropertyname_stat = basicpropertyname_stat;
	}
	public int getExtraBasicAttrRate() {
		return extraBasicAttrRate;
	}
	public void setExtraBasicAttrRate(int extraBasicAttrRate) {
		this.extraBasicAttrRate = extraBasicAttrRate;
	}

}
