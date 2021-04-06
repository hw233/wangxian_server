package com.fy.engineserver.menu.activity.specailEquipment;

import com.fy.engineserver.menu.Option;

public class Option_Query_SpecialEquipment extends Option {
	
	//查询装备类型鸿天帝宝伏天古宝(false)
	private boolean type;
	
	//鸿天帝宝(150  190)
	private int maxLevel;
	
	private int minLevel;
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
//	@Override
//	public void doSelect(Game game, Player player) {
//		
//		SpecialEquipmentManager sem = SpecialEquipmentManager.getInstance();
//		
//		SpecialEquipmentBillBoard sbb = sem.getSpecialEquipmentBillBoard();
//		if(sbb == null) {
//			if(SpecialEquipmentManager.logger.isWarnEnabled())
//				SpecialEquipmentManager.logger.warn("[查询特殊装备错误] [SpecialEquipmentBillBoard为null] []");
//			return ;
//		}
//		ArticleEntityManager aem = ArticleEntityManager.getInstance();
//		PlayerManager pm = PlayerManager.getInstance();
//		if(type){
//			
//			//鸿天帝宝
//			List<Boolean> appearList = new ArrayList<Boolean>();
//			List<String> equpmentNames = new ArrayList<String>();
//			List<Integer> equpmentLevels = new ArrayList<Integer>();
//			List<Integer> equpmentClasses = new ArrayList<Integer>();
//			List<String> equpmentOnwerNames = new ArrayList<String>();
//			List<Integer> equpmentShowTypes = new ArrayList<Integer>();
//			
//			Map<String, List<Long>> map = sbb.getSpecial1Map();
//			Equipment e = null;
//			for(Entry<String, List<Long>> en : map.entrySet()){
//				
//				Article a = ArticleManager.getInstance().getArticle(en.getKey());
//				if( a != null && a instanceof Equipment){
//					e = (Equipment)a;
//					if(e.getArticleLevel() < minLevel || e.getArticleLevel() > maxLevel){
//						continue;
//					}
//					if(en.getValue().size() > 0){
//						//出现
//						try {
//							ArticleEntity ae = aem.getEntity(en.getValue().get(0));
//							if(ae instanceof Special_1EquipmentEntity){
//								appearList.add(true);
//								equpmentNames.add(e.getName());
////								equpmentLevels.add(e.getArticleLevel());
//								equpmentLevels.add(e.getPlayerLevelLimit());
//								equpmentClasses.add(e.getClassLimit());
//								equpmentOnwerNames.add("");
//								int t =	e.getEquipmentType();
//								int showType = 0;
//								if(t == 0){
//									//武器
//									if(e instanceof Weapon){
//										//武器类型改为从1开始  0为空手  需要改
//										showType = (int)((Weapon)e).getWeaponType() -1;
//										equpmentShowTypes.add(showType);
//									}else{
//										if(SpecialEquipmentManager.logger.isWarnEnabled())
//											SpecialEquipmentManager.logger.warn("[查询特殊装备1错误] [不是武器类型] []");
//									}
//								}else{
//									showType = SpecialEquipmentManager.武器总类+t;
//									equpmentShowTypes.add(showType);
//								}
//							}else{
//								if(SpecialEquipmentManager.logger.isWarnEnabled())
//									SpecialEquipmentManager.logger.warn("[查询特殊装备错误] [不是指定类型1] ["+ae.getClass()+"]");
//							}
//						} catch (Exception eee) {
//							SpecialEquipmentManager.logger.error("~~~"+en.getValue().get(0), eee);
//						}
//					}else{
//						appearList.add(false);
//					}
//				}
//			}
//			
//			//转化
//			boolean[] appearArr = new boolean[appearList.size()];
//			String[] equpmentNameArr = new String[0];
//			int[] equpmentLevelArr = new int[0];
//			int[] equpmentClasseArr = new int[0];
//			String[] equpmentOnwerNameArr = new String[0];
//			int[] equpmentShowTypeArr = new int[0];
//			if(equpmentNames.size() >0){
//				equpmentNameArr = new String[equpmentNames.size()];
//				equpmentLevelArr = new int[equpmentNames.size()];
//				equpmentClasseArr = new int[equpmentNames.size()];
//				equpmentOnwerNameArr = new String[equpmentNames.size()];
//				equpmentShowTypeArr = new int[equpmentNames.size()];
//			}
//			int i = 0;
//			int z = 0;
//			for(boolean bln : appearList){
//				appearArr[i] = bln;
//				if(bln){
//					equpmentNameArr[z] = equpmentNames.get(z);
//					equpmentLevelArr[z] = equpmentLevels.get(z);
//					equpmentClasseArr[z] = equpmentClasses.get(z);
//					equpmentOnwerNameArr[z] = equpmentOnwerNames.get(z);
//					equpmentShowTypeArr[z] = equpmentShowTypes.get(z);
//					
//					if(SpecialEquipmentManager.logger.isInfoEnabled()){
//						SpecialEquipmentManager.logger.info("[查看装备的显示类型] ["+equpmentNameArr[z]+"] ["+equpmentShowTypeArr[z]+"]");
//					}
//					++z;
//				}
//				++i;
//			}
//			
//			if(SpecialEquipmentManager.logger.isWarnEnabled()){
//				SpecialEquipmentManager.logger.warn("[查询特殊装备1成功] ["+minLevel+"] ["+maxLevel+"] [数量:"+appearArr.length+"]");
//			}
//			QUERY_SPECIAL_EQUIPMENT_RES res = new QUERY_SPECIAL_EQUIPMENT_RES(GameMessageFactory.nextSequnceNum(), true, "鸿天帝宝", appearArr, equpmentNameArr, equpmentLevelArr, equpmentClasseArr, equpmentOnwerNameArr,equpmentShowTypeArr);
//			player.addMessageToRightBag(res);
//
//		}else{
//			//伏天古宝
////			sbb.getMap2();
//			List<Boolean> appearList = new ArrayList<Boolean>();
//			List<String> equpmentNames = new ArrayList<String>();
//			List<Integer> equpmentLevels = new ArrayList<Integer>();
//			List<Integer> equpmentClasses = new ArrayList<Integer>();
//			List<String> equpmentOnwerNames = new ArrayList<String>();
//			List<Integer> equpmentShowTypes = new ArrayList<Integer>();
//			
//			Map<String, List<Long>> map = sbb.getSpecial2Map();
//			Equipment e = null;
//			for(Entry<String, List<Long>> en : map.entrySet()){
//				
//				Article a = ArticleManager.getInstance().getArticle(en.getKey());
//				if( a != null && a instanceof Equipment){
//					e = (Equipment)a;
//					if(en.getValue().size() > 0){
//						//出现
//						ArticleEntity ae = aem.getEntity(en.getValue().get(0));
//						if(ae instanceof Special_2EquipmentEntity){
//							appearList.add(true);
//							equpmentNames.add(e.getName());
////							equpmentLevels.add(e.getArticleLevel());
//							equpmentLevels.add(e.getPlayerLevelLimit());
//							equpmentClasses.add(e.getClassLimit());
//							equpmentOnwerNames.add("");
//							int t =	e.getEquipmentType();
//							if(t == 0){
//								//武器
//								if(e instanceof Weapon){
//									//武器类型改为从1开始  0为空手  需要改
//									equpmentShowTypes.add((int)((Weapon)e).getWeaponType()-1);
//								}else{
//									if(SpecialEquipmentManager.logger.isWarnEnabled())
//										SpecialEquipmentManager.logger.warn("[查询特殊装备2错误] [不是武器类型] []");
//								}
//							}else{
//								equpmentShowTypes.add(SpecialEquipmentManager.武器总类+t);
//							}
//						}else{
//							if(SpecialEquipmentManager.logger.isWarnEnabled())
//								SpecialEquipmentManager.logger.warn("[查询特殊装备错误] [不是指定类型2] ["+ae.getClass()+"]");
//						}
//					}else{
//						appearList.add(false);
//					}
//				}else{
//					SpecialEquipmentManager.logger.warn("[查询特殊装备错误] [不是装备] ["+(a != null ? a.getClass():"null")+"] ["+en.getKey()+"]");
//				}
//			}
//			
//			//转化
//			boolean[] appearArr = new boolean[appearList.size()];
//			String[] equpmentNameArr = new String[0];
//			int[] equpmentLevelArr = new int[0];
//			int[] equpmentClasseArr = new int[0];
//			String[] equpmentOnwerNameArr = new String[0];
//			int[] equpmentShowTypeArr = new int[0];
//			if(equpmentNames.size() >0){
//				equpmentNameArr = new String[equpmentNames.size()];
//				equpmentLevelArr = new int[equpmentNames.size()];
//				equpmentClasseArr = new int[equpmentNames.size()];
//				equpmentOnwerNameArr = new String[equpmentNames.size()];
//				equpmentShowTypeArr = new int[equpmentNames.size()];
//			}
//			int i = 0;
//			int z = 0;
//			for(boolean bln : appearList){
//				appearArr[i] = bln;
//				if(bln){
//					equpmentNameArr[z] = equpmentNames.get(z);
//					equpmentLevelArr[z] = equpmentLevels.get(z);
//					equpmentClasseArr[z] = equpmentClasses.get(z);
//					equpmentOnwerNameArr[z] = equpmentOnwerNames.get(z);
//					equpmentShowTypeArr[z] = equpmentShowTypes.get(z);
//					++z;
//				}
//				++i;
//			}
//			
//			if(SpecialEquipmentManager.logger.isWarnEnabled()){
//				SpecialEquipmentManager.logger.warn("[查询特殊装备2成功] ["+player.getLogString()+"] ["+(map != null ?map.size():"null")+"]");
//			}
//			QUERY_SPECIAL_EQUIPMENT_RES res = new QUERY_SPECIAL_EQUIPMENT_RES(GameMessageFactory.nextSequnceNum(), false, "伏天古宝", appearArr, equpmentNameArr, equpmentLevelArr, equpmentClasseArr, equpmentOnwerNameArr,equpmentShowTypeArr);
//			player.addMessageToRightBag(res);
//		}
//		
//	}

	
	
	public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

}
