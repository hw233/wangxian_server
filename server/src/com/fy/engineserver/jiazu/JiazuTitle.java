package com.fy.engineserver.jiazu;

import java.util.BitSet;
import java.util.HashMap;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.jiazu.service.JiazuManager;

/**
 * 家族的职称
 * 
 * 
 * 
 */
public enum JiazuTitle {
	/**
	 * 族长
	 */
	master(Translate.text_6172),
	/**
	 * 副族长
	 */
	vice_master(Translate.text_6173),
	/**
	 * 执法长老
	 */
	law_elder(Translate.text_6174),
	/**
	 * 龙血长老
	 */
	fight_elder(Translate.text_6175),
	/**
	 * 执事长老
	 */
	research_elder(Translate.text_6176),
	/**
	 * 精英
	 */
	elite(Translate.text_6177),
	/**
	 * 平民
	 */
	civilian(Translate.text_6178);

	private String name;
	private int id;
	final static public HashMap<JiazuTitle, BitSet> functionMap = new HashMap<JiazuTitle, BitSet>();
	static {
		int length = JiazuFunction.values().length;
		BitSet masterSet = new BitSet(length);
		masterSet.set(0, length);
		masterSet.set(JiazuFunction.request_master.ordinal(), false);
		masterSet.set(JiazuFunction.leave_jiazu.ordinal(), false);
		functionMap.put(master, masterSet);

		BitSet viceMasterSet = new BitSet(length);
		viceMasterSet.set(JiazuFunction.nominate.ordinal(), true);
		viceMasterSet.set(JiazuFunction.expel.ordinal(), true);
		viceMasterSet.set(JiazuFunction.set_rank_name.ordinal(), true);
		viceMasterSet.set(JiazuFunction.upgrade_or_repair_construction.ordinal(), true);
		viceMasterSet.set(JiazuFunction.downgrade_or_destroy_construction.ordinal(), true);
		viceMasterSet.set(JiazuFunction.setup_or_abandon_research.ordinal(), true);
		viceMasterSet.set(JiazuFunction.setup_or_abandon_business_line.ordinal(), true);
		viceMasterSet.set(JiazuFunction.request_master.ordinal(), true);
		viceMasterSet.set(JiazuFunction.use_jiazu_token.ordinal(), true);
		viceMasterSet.set(JiazuFunction.setup_transfer_goods.ordinal(), true);
		viceMasterSet.set(JiazuFunction.setup_fight_against_village.ordinal(), true);
		viceMasterSet.set(JiazuFunction.setup_fight_againt_jiazu.ordinal(), true);
		viceMasterSet.set(JiazuFunction.leave_jiazu.ordinal(), true);
		viceMasterSet.set(JiazuFunction.receive_salary.ordinal(), true);
		viceMasterSet.set(JiazuFunction.receive_business_task.ordinal(), true);
		viceMasterSet.set(JiazuFunction.modify_slogan.ordinal(), true);
		viceMasterSet.set(JiazuFunction.release_silkworm.ordinal(), true);
		viceMasterSet.set(JiazuFunction.release_forluck.ordinal(), true);
		viceMasterSet.set(JiazuFunction.release_silvercar.ordinal(), true);
		viceMasterSet.set(JiazuFunction.handle_join_request.ordinal(), true);
		viceMasterSet.set(JiazuFunction.add_buff.ordinal(), true);
		viceMasterSet.set(JiazuFunction.release_callIn.ordinal(), true);
		viceMasterSet.set(JiazuFunction.call_BOSS.ordinal(), true);
		viceMasterSet.set(JiazuFunction.stronger_BIAOCHE.ordinal(), true);
		viceMasterSet.set(JiazuFunction.release_JIAZU.ordinal(), true);
		functionMap.put(vice_master, viceMasterSet);

		BitSet lawElderSet = new BitSet(length);
		lawElderSet.set(JiazuFunction.nominate.ordinal(), true);
		lawElderSet.set(JiazuFunction.expel.ordinal(), true);
		lawElderSet.set(JiazuFunction.set_rank_name.ordinal(), true);
		lawElderSet.set(JiazuFunction.handle_join_request.ordinal(), true);
		lawElderSet.set(JiazuFunction.leave_jiazu.ordinal(), true);
		lawElderSet.set(JiazuFunction.receive_business_task.ordinal(), true);
		lawElderSet.set(JiazuFunction.receive_salary.ordinal(), true);
		lawElderSet.set(JiazuFunction.release_JIAZU.ordinal(), true);
		functionMap.put(law_elder, lawElderSet);

		BitSet fightElderSet = new BitSet(length);
		fightElderSet.set(JiazuFunction.setup_fight_against_village.ordinal(), true);
		fightElderSet.set(JiazuFunction.setup_fight_againt_jiazu.ordinal(), true);
		fightElderSet.set(JiazuFunction.leave_jiazu.ordinal(), true);
		fightElderSet.set(JiazuFunction.receive_business_task.ordinal(), true);
		fightElderSet.set(JiazuFunction.receive_salary.ordinal(), true);
		fightElderSet.set(JiazuFunction.release_JIAZU.ordinal(), true);
		functionMap.put(fight_elder, fightElderSet);

		BitSet researchElderSet = new BitSet(length);

		researchElderSet.set(JiazuFunction.upgrade_or_repair_construction.ordinal(), true);
		researchElderSet.set(JiazuFunction.stronger_BIAOCHE.ordinal(), true);
		researchElderSet.set(JiazuFunction.publish_construct_task.ordinal(), true);
		researchElderSet.set(JiazuFunction.setup_or_abandon_research.ordinal(), true);
		researchElderSet.set(JiazuFunction.setup_transfer_goods.ordinal(), true);
		researchElderSet.set(JiazuFunction.setup_construction_consume.ordinal(), true);
		researchElderSet.set(JiazuFunction.leave_jiazu.ordinal(), true);
		researchElderSet.set(JiazuFunction.receive_business_task.ordinal(), true);
		researchElderSet.set(JiazuFunction.receive_salary.ordinal(), true);
		researchElderSet.set(JiazuFunction.release_JIAZU.ordinal(), true);
		functionMap.put(research_elder, researchElderSet);

		BitSet eliteSet = new BitSet(length);
		eliteSet.set(JiazuFunction.leave_jiazu.ordinal(), true);
		eliteSet.set(JiazuFunction.receive_business_task.ordinal(), true);
		eliteSet.set(JiazuFunction.receive_salary.ordinal(), true);
		functionMap.put(elite, eliteSet);

		BitSet civilianSet = new BitSet(length);
		civilianSet.set(JiazuFunction.leave_jiazu.ordinal(), true);
		civilianSet.set(JiazuFunction.receive_business_task.ordinal(), true);
		civilianSet.set(JiazuFunction.receive_salary.ordinal(), true);
		functionMap.put(civilian, civilianSet);

	}

	private JiazuTitle() {

	}

	private JiazuTitle(String name) {
		this.name = name;
		this.id = this.ordinal();
	}

	/**
	 * 
	 * 获取默认的称呼
	 * 
	 * @param jiazuID
	 *            家族ID
	 * @return
	 */
	public String getName(long jiazuID) {
		JiazuManager jiazuManager = JiazuManager.getInstance();
		Jiazu jiazu = jiazuManager.getJiazu(jiazuID);
		return jiazu.getTitleName(this);
	}

	public String getName() {
		return name;
	}

	public static boolean hasPermission(JiazuTitle title, JiazuFunction func) {
		BitSet set = functionMap.get(title);
		return set.get(func.ordinal());
	}

	/**
	 * 返回一个级别的家族的一个职位的理论上的最大的人数
	 * 
	 * @param title
	 * @param level
	 * @return
	 */
	public static int getTitleNumMax(JiazuTitle title, int level) {
		switch (title) {
		case master:
			return 1;
		case vice_master: {
			if (level == 0) return 1;
			else {
				return 2;
			}
		}
		case law_elder: {
			if (level == 0) return 0;
			else return 4;
		}
		case fight_elder: {
			if (level == 0) return 0;
			else return 8;
		}
		case research_elder: {
			if (level == 0) return 0;
			else return 4;
		}
		case elite:
			return 10;
		case civilian: {
			switch (level) {
			case 0:
				return 29;
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				return 59;
			}
			break;
		}

		}
		return 0;
	}
}
