package com.fy.engineserver.sprite.pet2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.PetSkillProp;
import com.fy.engineserver.datasource.buff.BuffTemplate;
import com.fy.engineserver.datasource.buff.BuffTemplate_4shuxingGongFangJianFang;
import com.fy.engineserver.datasource.buff.BuffTemplate_JingYan;
import com.fy.engineserver.datasource.buff.BuffTemplate_OnceAttributeAttack;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill2.GenericBuff;
import com.fy.engineserver.datasource.skill2.GenericSkill;
import com.fy.engineserver.datasource.skill2.GenericSkillManager;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PET2_QUERY_BY_ARTICLE_REQ;
import com.fy.engineserver.message.PET2_QUERY_RES;
import com.fy.engineserver.message.PET_BOOK_UI_RES;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.qiancengta.QianCengTaManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;

/**
 * 
 * create on 2013年8月24日
 */
public class Pet2SkillCalc {
	public static int bothHaveRate = 50;
	public static int onlyMuRate = 35;
	public static int onlyGongRate = 30;
	public static int bothMissRate = 0;
	public static String lockSk1Key = "talentSkillBindA";
	public static String lockSk2Key = "talentSkillBindB";
	public static String[] jiChuNames = new String[0];
	public static String[] tianFuNames = new String[0];
	public static String[] allTakeSkillBookNames = new String[0];
	public static String[] lockSkillNames = new String[]{lockSk1Key,lockSk2Key };
	public static PetSkillProp[] jiChuSkillBook;
	public static PetSkillProp[] tianFuSkillBook;
	/**
	 * 天生技能
	 */
	public static GenericSkill[] bornSkillArr = new GenericSkill[0];
	public static GenericSkill[] tianFuSkillArr = new GenericSkill[0];
	/**
	 * 忠诚 精明 谨慎 狡诈 (0,1,2,3) 4 通用
	 */
	public static List<GenericSkill>[] skByXingGe;
	/** 高级技能--在开的时候有特定概率出高级技能 */
	public static List<GenericSkill>[] skByXingGeAndGrade;
	public Random rnd = new Random();
	public static Random rnd2 = new Random(System.currentTimeMillis());
	public static int[] openSlotNeedBooks = new int[10];
	/**
	 * 请勿直接使用。 
	 */
	public static Pet2SkillCalc inst;
	public static Pet2SkillCalc getInst(){
		if(inst == null){
			inst = new Pet2SkillCalc();
		}
		return inst;
	}
	public Pet2SkillCalc(){
		skByXingGe = new List[5];
		for(int i=0; i<5; i++){
			skByXingGe[i] = new ArrayList<GenericSkill>();
		}
		skByXingGeAndGrade = new List[5];
		for(int i=0; i<5; i++){
			skByXingGeAndGrade[i] = new ArrayList<GenericSkill>();
		}
	}
	/**
	 * 计算二代天生技能。
	 * @param a
	 * @param b
	 * @param pet 
	 * @return
	 */
	public boolean calc2TalentSkill(Pet a, Pet b, Pet pet){
		int rate = 0;
		Pet ci = a.getSex() == PetManager.母 ? a : b;
		Pet xiong = a.getSex() == PetManager.公 ? a : b;
		Logger log = Pet2Manager.log;
		calcWuXing(pet, log);
		if(ci == xiong){
			log.error("两个宠物同公母 {} {}",a.getId(), b.getId());
			return false;
		}
		if(a.talent1Skill>0 && b.talent2Skill>0){
			rate = bothHaveRate;
		}else if(ci.talent1Skill>0){
			rate = onlyMuRate;
		}else if(xiong.talent1Skill>0){
			rate = onlyGongRate;
		}else{
			rate = bothMissRate;
		}
		int r = rnd.nextInt(100);
		log.warn("random got {}, rate need {}, ci id {}, xiong id{}", 
				new Object[]{r, rate, ci.getId(), xiong.getId()});
		boolean hit = r<rate;
		if(hit){
			hitSk2(pet, log);
		}else{
			pet.talent2Skill = -1;
			log.warn("繁殖时<没>领悟天生技能二,pet is {}", pet.getId());
		}
//		calcTianFuSk(pet);
		return hit;
	}
	public void hitSk2(Pet pet, Logger log) {
		int id = pet.petProps == null ? 0 : pet.petProps.talent2skill;
		GenericSkill sk = findSkill(bornSkillArr, id);
		if(sk != null){
			pet.setTalent2Skill(id);
			log.warn("繁殖时领悟天生技能二,pet is {}", pet.getId());
		}else{
			log.error("天生技能2不存在 {} {}",pet.getCategory(), id);
		}
	}
	public void calcWuXing(Pet pet, Logger log) {
		pet.setWuXing(rnd.nextInt(100)+1);
		log.warn("{}悟性设置为{}",pet.getId(), pet.wuXing);
	}
	/**
	 * 计算一代天生技能。
	 * @param pet
	 */
	public void calc1TalentSkill(Pet pet) {
		calc1TalentSkill(pet, false);
	}
	public void calc1TalentSkill(Pet pet, boolean flag) {
		Logger log = Pet2Manager.log;
		calcWuXing(pet, log);
		rollTsk1(pet, log);
//		calcTianFuSk(pet, flag);
	}
	public void rollTsk1(Pet pet, Logger log) {
		int r = rnd.nextInt(10000);
		boolean hit = r < pet.getWuXing();
		if(hit){
			hitSk1(pet, log);
		}else{
			pet.talent1Skill = -1;
			log.warn("pet id {} 《miss》天生技能一", pet.getId());
		}
	}
	public void hitSk1(Pet pet, Logger log) {
		int id = pet.petProps == null ? 0 : pet.petProps.talent1skill;
		GenericSkill sk = findSkill(bornSkillArr, id);
		if(sk != null){
			pet.setTalent1Skill(id);
			log.warn("pet id {} 获得天生技能一", pet.getId());
		}else{
			log.error("天生技能1不存在 {} {}",pet.getCategory(), id);
		}
	}
	public GenericSkill findSkill(GenericSkill arr[], int id){
		if(arr == null){
			return null;
		}
		for(GenericSkill gs : arr){
			if(gs.getId() == id){
				return gs;
			}
		}
		return null;
	}
	
	public static final int probabbly = 500;
	/**
	 * 只有使用蛋孵化宠物的时候会有概率随机到高级天赋技能
	 * */
	public void calcTianFuSk(Pet pet) {
		calcTianFuSk(pet, false);
	}
	public void calcTianFuSk(Pet pet, boolean createByProp) {
		Logger log = Pet2Manager.log;
		int c = pet.getCharacter();
		List<GenericSkill> list = skByXingGe[c];
		List<GenericSkill> list2 = skByXingGeAndGrade[c];
		if(list == null){
			log.error("Pet2SkillCalc.calcTianFuSk: 没有初始化各个性格的天赋技能");
			return;
		}
		if(list.size()<10){
			log.error("Pet2SkillCalc.calcTianFuSk: 各个性格的天赋技能太少。");
			return;
		}
		int cnt = rnd.nextInt(3);//0,1,2
		int seedLen = list.size();
		log.warn("pet id {} gain tian fu skill cnt {}", pet.getId(), cnt);
		Set<Integer> set = new HashSet<Integer>();
		int times = 0;
		while(set.size()<cnt && times < 100){
			times ++;
			set.add(rnd.nextInt(seedLen));
		}
		Iterator<Integer> it = set.iterator();
		cnt = set.size();
		int[] newSkArr = new int[cnt];
		int[] newLvArr = new int[cnt];
		
		for(int i=0; i<cnt ;i++){
			int r = it.next();
			int randomInt = rnd.nextInt(10000);
			GenericSkill gs = list.get(r);
			if(log.isDebugEnabled()) {
				log.debug("[宠物孵化][是否通过蛋:" + createByProp + "][随机值:" + randomInt + "]");
			}
			try{
				if(createByProp && list2 != null && list2.size() > 0 && randomInt <= probabbly) {
					GenericSkill gs2 = list2.get(rnd.nextInt(list2.size()));
					boolean existFlag = false;
					for(int ii=0; ii<newSkArr.length; ii++) {
						if(gs2.getId() == newSkArr[ii]) {
							existFlag = true;
							if(log.isDebugEnabled()) {
								log.debug("[宠物孵化][有相同高级技能:" + pet.getId() + "]");
							}
							break;
						}
					}
					if(!existFlag) {
						gs = gs2;
					}
				}
			} catch(Exception e) {
				log.error("[宠物孵化，创建高级技能异常][宠物id:" + pet.getId() + "]", e);
				gs = list.get(r);
			}
			newSkArr[i] = gs.getId();
			newLvArr[i] = 1;
			log.warn("pet id {} gain tian fu skill {} lv {} ",
					new Object[]{pet.getId(), newSkArr[i], newLvArr[i]});
		}
		pet.setTianFuSkIds(newSkArr);
		pet.setTianFuSkIvs(newLvArr);
	}
	public void useBook(Player player, ArticleEntity ae) {
		Cell[] arr = player.getPetKnapsack().getCells();
		boolean has = false;
		for(Cell c : arr){
			if(c != null && c.getEntityId()>0){
				has = true;
				break;
			}
		}
		if(!has){
			player.sendError(Pet2Manager.getInst().getConfStr("youHaveNoPetWhenUseBook"));
			return;
		}
//		player.sendError(Pet2Manager.getInst().getConfStr("usePetSkillBookTip"));
		String articleName = ae.getArticleName();
		PET_BOOK_UI_RES s = makeBookRes(player, GameMessageFactory.nextSequnceNum(),articleName);
//		player.addMessageToRightBag(s, "通知打开传承界面");
		Logger log = Pet2Manager.log;
		log.warn("通知打开传承界面 {} {}", player.getName(), articleName);
	}
	public PET_BOOK_UI_RES makeBookRes(Player p, long l, String curName){
	String[] holeNames = {"holeNames"};
		PET_BOOK_UI_RES s = new PET_BOOK_UI_RES(l, 
				curName, jiChuNames,tianFuNames, holeNames, 
				Pet2SkillCalc.allTakeSkillBookNames, Pet2SkillCalc.lockSkillNames,
				openSlotNeedBooks);
		//TODO:去掉原来为了客户端bug所修改的代码 
//		if (p.getPetKnapsack().size() > 0) {
//			PET2_QUERY_BY_ARTICLE_REQ rr = new PET2_QUERY_BY_ARTICLE_REQ(GameMessageFactory.nextSequnceNum(), p.getPetKnapsack().getCell(0).getEntityId(), true);
//			PET2_QUERY_RES ret = (PET2_QUERY_RES)Pet2Manager.getInst().sendPetQueryByArt(p.getConn(), rr, p);
//			p.addMessageToRightBag(ret);
//			
//		}
		p.addMessageToRightBag(s);
		return null;
	}
	public void setAllBooks(ArrayList<Article> allArticle) {
		int size = allArticle.size();
		for(int i=0; i<size; i++){
			PetSkillProp article = (PetSkillProp) allArticle.get(i);
			if("TF".equals(article.cateKey)){
				tianFuNames = (String[]) ArrayUtils.add(tianFuNames, article.getName());
				tianFuSkillBook = (PetSkillProp[]) ArrayUtils.add(tianFuSkillBook, article);
			}else{
				jiChuNames = (String[]) ArrayUtils.add(jiChuNames, article.getName());
				jiChuSkillBook = (PetSkillProp[]) ArrayUtils.add(jiChuSkillBook, article);
			}
		}
	}
	/**
	 * 按照id和等级查找天赋技能书。
	 * @param curSkillId
	 * @param curSkillLv
	 * @return
	 */
	public PetSkillProp findBook(int curSkillId, int curSkillLv) {
		PetSkillProp[] arr = tianFuSkillBook;
		Logger log = Pet2Manager.log;
		if(arr == null){
			log.error("conf allPetSkillBook arr is null");
			return null;
		}
		PetSkillProp ret = null;
		for(PetSkillProp b : arr){
			if(b.skillId == curSkillId
//					&& b.skillLv == curSkillLv//天赋技能本质上没有区分等级，低中高其实是三个技能id。
					){
				ret = b;
				break;
			}
		}
		return ret;
	}
	public void petLevelUp(Pet pet) {
		Logger log = Pet2Manager.log;
		if(pet == null){
			return;
		}
		if(pet.petProps == null){
			log.error("pet skill calc, petLevelUp, pet props is null");
			return;
		}
		int r = rnd.nextInt(100000);
		if(r>=pet.getWuXing()){
			return;
		}
		if(pet.getGeneration() == 0){
			if(pet.getTalent1Skill()<=0){
				pet.setTalent1Skill(pet.petProps.talent1skill);
				pet.init();
				log.warn("petLevelUp : pet id {} gain 1 skill", pet.getId());
			}
		}else{
			if(pet.getTalent2Skill()<=0){
				pet.setTalent2Skill(pet.petProps.talent2skill);
				pet.init();
				log.warn("petLevelUp : pet id {} gain 2 skill", pet.getId());
			}
		}
	}
	/**
	 * 根据宠物技能（被动），为其添加被动buff。
	 * @param pet
	 */
	public void initSkill(Pet pet) {
		if(pet == null){
			return;
		}
		pet.pet2buff = null;
		pet.exp2playerRatio = 0;
		pet.towerRate = 0;
		if(pet.specialTargetFactor != null)pet.specialTargetFactor.clear();
		if(pet.getGeneration()<1 && pet.talent2Skill>0){
			pet.setTalent2Skill(0);
			Pet2Manager.log.warn("Pet2SkillCalc.initSkill: 一代宠物不应该有第二个天生技能,{}",pet.getId());
		}
		addSkill(pet, pet.talent1Skill, 1);
		addSkill(pet, pet.talent2Skill, 1);
		int[] tianFuSkIds = pet.tianFuSkIds;
		int[] lvArr = pet.tianFuSkIvs;
		if(tianFuSkIds != null){
			for(int i=0; i<tianFuSkIds.length; i++){
				int lv = 1;
				if(i<lvArr.length){
					lv = lvArr[i];
				}
				addSkill(pet, tianFuSkIds[i], lv);
			}
		}
	}
	/**
	 * 为宠物添加技能；这些技能会在宠物攻击时进行处理；或者在添加时就产生响应的效果。
	 * @param pet
	 * @param id
	 * @param lv
	 */
	public void addSkill(Pet pet, int id, int lv) {
		Logger log = Pet2Manager.log;
		GenericSkill sk = GenericSkillManager.getInst().maps.get(id);
		if(sk == null){
			if(id>0){
				log.error("Pet2SkillCalc addSkill skill not find {} for {} {}",
						new Object[]{id, pet.getId(), pet.getName()});
			}
			return ;
		}
		if(lv <= 0){
			lv = 1;
		}
		GenericBuff buffOnSk = sk.buff;
		if(buffOnSk == null){
			log.error("buff is null, skill {}", sk.getName());
			return ;
		}
		if(lv>buffOnSk.values.length){
			log.warn("lv out range, force to max {} want lv{}", sk.getName(), lv);
			lv = buffOnSk.values.length;
		}
		int curV = 0;
		if( (lv - 1) >=0 && ( lv - 1 ) < buffOnSk.values.length ){
			curV = buffOnSk.values[lv - 1];
		}
		switch(buffOnSk.attId){
		case GenericBuff.ATT_SPEED_POINT: {
			if (log.isDebugEnabled()) {
				log.debug("[坐骑增加移动速度] [生效:" + pet.getId() + "]");
			}
			if (buffOnSk.values != null && buffOnSk.values.length > 0 && buffOnSk.values[0] > 0) {
				pet.extraSpeedNum = buffOnSk.values[0];
				if (log.isDebugEnabled()) {
					log.debug("[坐骑增加移动速度] [生效:" + pet.getId() + "] [实际增加速度:" + pet.extraSpeedNum + "]");
				}
			}
			return;
		}
		case GenericBuff.ATT_SubA_AddB:{
			log.debug("Pet2SkillCalc.addSkill: {}",sk.getName());
			GenericBuff buffA = createBuff(sk, sk.buff, -curV);
			buffA.attId = buffOnSk.paramInt;
			addBuff(pet, buffA);
			log.debug("Pet2SkillCalc.addSkill: {} AA",sk.getName());
			
			GenericBuff buffB = createBuff(sk, sk.buff, curV);
			buffB.attId = buffOnSk.paramIntB;
			addBuff(pet, buffB);
			log.debug("Pet2SkillCalc.addSkill: {} BB",sk.getName());
			return;
		}
		case GenericBuff.ATT_DMG_SCALE_RATE:{
			if (pet.talent1Skill == id) {
				pet.dmgScaleRate = buffOnSk.paramInt;
				pet.dmgScale = buffOnSk.paramIntB;
			} else if (pet.talent2Skill == id) {
				pet.dmgScaleRate2 = buffOnSk.paramInt;
				pet.dmgScale2 = buffOnSk.paramIntB;
			}
			return;
		}
		case GenericBuff.ATT_HPANDANTI_SCALE_RATE:{
			if (pet.talent1Skill == id || pet.talent2Skill == id) {			//此技能只会有一个生效
				pet.setExtraHpC(buffOnSk.paramIntB);
				pet.antiInjuryRate = buffOnSk.paramInt;
			}
			return;
		}
		case GenericBuff.ATT_REMOVE_DEBUFF:{
			pet.setExtraHpC(pet.getExtraHpC() + buffOnSk.paramInt);
			pet.removeIntever = buffOnSk.paramIntB * 1000;
			pet.lastRemoveDebuffTime = System.currentTimeMillis();
			if (log.isDebugEnabled()) {
				log.debug("[加血上限并间隔移除负面状态] ["+pet.getId()+"] [血上限比例:" +  buffOnSk.paramInt + "] [移除间隔时间:" + buffOnSk.paramIntB + "] [血上限比例:" + pet.getExtraHpC() + "]");
			}
		}
		return ;
		case GenericBuff.ATT_QUANNENGFAJIE:{
			pet.setExtraHpC(pet.getExtraHpC() + buffOnSk.paramInt);
			pet.attributeAddRate =  buffOnSk.paramIntB;
			if (log.isDebugEnabled()) {
				log.debug("[全能法戒] ["+pet.getId()+"] [血上限比例:" +  buffOnSk.paramInt + "] [攻击加成比例:" + buffOnSk.paramIntB + "] [血上限比例:" + pet.getExtraHpC() + "]");
			}
		}
		return ;
		case GenericBuff.ATT_AURA_SKILL:{
			if (pet.talent1Skill == id || pet.talent2Skill == id) {			//光环技能目前只支持一个技能生效
				if (pet.getNagent() != null && pet.getNagent().getSkillId() <= 0) {
					pet.getNagent().setSkillId(id);
				}
			}
		}
		return ;
		case GenericBuff.ATT_Tower:{
			pet.towerRate = curV;
			log.debug("Pet2SkillCalc.addSkill: tower rate {}, pet {}",curV, pet.getName());
			return;
		}
		case GenericBuff.ATT_KanRenXiaCai:{
			Map<Integer, Integer> map = pet.specialTargetFactor;
			if(map == null){
				map = new HashMap<Integer, Integer>(3);
				pet.specialTargetFactor = map;
			}
			map.put(buffOnSk.paramInt, curV);
			return;
		}
		case GenericBuff.ATT_PLAYER_EXP_PERCENT:{
			pet.exp2playerRatio += curV;
			log.debug("Pet2SkillCalc.addSkill: exp2playerRatio {}",curV);
			return;
		}
		case GenericBuff.ATT_Feng_Gong:
			if(buffOnSk.percent)
				pet.setWindAttackC(buffOnSk.v);
			else
				pet.setWindAttackB(buffOnSk.v);
			log.debug("Pet2SkillCalc.addSkill: {} {}",buffOnSk.attName, buffOnSk.v);
			break;
		case GenericBuff.ATT_Huo_Gong:
			if(buffOnSk.percent)
				pet.setFireAttackC(buffOnSk.v);
			else
				pet.setFireAttackB(buffOnSk.v);
			log.debug("Pet2SkillCalc.addSkill: {} {}",buffOnSk.attName, buffOnSk.v);
			break;
		case GenericBuff.ATT_Lei_Gong:
			if(buffOnSk.percent)
				pet.setThunderAttackC(buffOnSk.v);
			else
				pet.setThunderAttackB(buffOnSk.v);
			log.debug("Pet2SkillCalc.addSkill: {} {}",buffOnSk.attName, buffOnSk.v);
			break;
		case GenericBuff.ATT_Bing_Gong:
			if(buffOnSk.percent)
				pet.setBlizzardAttackC(buffOnSk.v);
			else
				pet.setBlizzardAttackB(buffOnSk.v);
			log.debug("Pet2SkillCalc.addSkill: {} {}",buffOnSk.attName, buffOnSk.v);
			break;
		case GenericBuff.ATT_Feng_Kang:
			pet.setWindDefenceRateOther(buffOnSk.v);
			log.debug("Pet2SkillCalc.addSkill: {} {}",buffOnSk.attName, buffOnSk.v);
			break;
		case GenericBuff.ATT_Huo_Kang:
			pet.setFireDefenceRateOther(buffOnSk.v);
			log.debug("Pet2SkillCalc.addSkill: {} {}",buffOnSk.attName, buffOnSk.v);
			break;
		case GenericBuff.ATT_Lei_Kang:
			pet.setThunderDefenceRateOther(buffOnSk.v);
			log.debug("Pet2SkillCalc.addSkill: {} {}",buffOnSk.attName, buffOnSk.v);
			break;
		case GenericBuff.ATT_Bing_Kang:
			pet.setBlizzardDefenceRateOther(buffOnSk.v);
			log.debug("Pet2SkillCalc.addSkill: {} {}",buffOnSk.attName, buffOnSk.v);
			break;
		case GenericBuff.ATT_INVINCIBLE:
			pet.lastReliveTime = System.currentTimeMillis();
			if (log.isDebugEnabled()) {
				log.debug("[宠物拥有技能] [受到致命一击回血并无敌] [" + pet.getId() + "]");
			}
			return;
		}
		if(buffOnSk.getTemplate() instanceof BuffTemplate_4shuxingGongFangJianFang){
//			log.debug("Pet2SkillCalc.addSkill: 不给自己加 {}",buffOnSk.getTemplate().getName());
//			return;//要给自己加；此处只是加到队列里，不是给自己上buff。
		}
//		if (curV < 0 && buffOnSk.getTemplate() instanceof BuffTemplate_OnceAttributeAttack) {
//			curV = Pet2SkillCalc.rnd2.nextInt(4);
//		}
		GenericBuff newOne = createBuff(sk, buffOnSk, curV);
		if(buffOnSk.getTemplate() instanceof BuffTemplate_JingYan){
			if(pet.getMaster() != null){
				newOne.setStartTime(SystemTime.currentTimeMillis());
				newOne.setInvalidTime(newOne.getStartTime()+9999999999L);
				pet.getMaster().placeBuff(newOne);
				log.debug("Pet2SkillCalc.addSkill: 给主人加经验buff {}",pet.getMaster().getName());
			}else{
				log.debug("Pet2SkillCalc.addSkill: 主人是null");
			}
		} 
		if (id == 110160) {			//此技能除攻击加debuff外还加自身百分百血量
			pet.setMaxHPC(pet.getMaxHPC() + 100);
		}
		
		//
		addBuff(pet, newOne);
			log.warn("添加到技能列表 {}  att id {} {}, buff value {} percnet {} name{}",
					new Object[]{sk.getName(), newOne.attId, newOne.attName, newOne.v, newOne.percent,newOne.getDescription()});
	}
	public void addBuff(Pet pet, GenericBuff newOne) {
		Logger log = Pet2Manager.log;
		GenericBuff head = pet.pet2buff;
		if(head == null){
			pet.pet2buff = newOne;
			log.warn("Pet2SkillCalc.addBuff: set as head {}",newOne.attId);
		}else{
			GenericBuff tail;
			log.debug("Pet2SkillCalc.addBuff: find tail.");
			do{
				tail = head;
				log.debug("Pet2SkillCalc.addBuff: have {} {}",tail.attId,tail.attName);
				head = head.next;
			}while(head != null);
			log.debug("Pet2SkillCalc.addBuff: find finish.--");
			tail.next = newOne;
			log.warn("Pet2SkillCalc.addBuff: append {}", newOne.attId);
		}
	}
	public GenericBuff createBuff(GenericSkill sk, GenericBuff buffOnSk,
			int curV) {
		GenericBuff newOne = new GenericBuff();
		newOne.setIconId(sk.getIconId());
		newOne.attId = buffOnSk.attId;
		newOne.attName = buffOnSk.attName;
		newOne.v = curV;
		newOne.percent = buffOnSk.percent;
		newOne.srcSkName = sk.getName();
		//
		newOne.triggerCondtion = buffOnSk.triggerCondtion;
		newOne.lastMS = buffOnSk.lastMS;
		newOne.paramInt = buffOnSk.paramInt;
		newOne.paramIntB = buffOnSk.paramIntB;
		BuffTemplate buffT = buffOnSk.getTemplate();
		if(buffT != null){
			newOne.setTemplate(buffT);
		}
		return newOne;
	}
	
	public boolean isProtectedNPC(Fighter target){
		if(target instanceof NPC){
			NPC n = (NPC) target;
			switch(n.getnPCCategoryId()){
			case 800000001://		龙象释帝  		
			case 800000002://		龙象卫长  
			case 800000003://		龙象卫士  
			case 800000004://		空灵天兵
			case 800000005://		空玄天兵
			case 90000000://		城主
			case 410000001://		个人镖车
			case 410000002://		个人镖车
			case 410000003://		个人镖车
			case 410000004://		家族镖车
				Skill.logger.debug("是保护NPC {}",target.getName());
				return true;
			}
		}
		return false;
	}
}
