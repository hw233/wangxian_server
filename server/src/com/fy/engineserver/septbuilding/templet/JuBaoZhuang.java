package com.fy.engineserver.septbuilding.templet;

import org.w3c.dom.Element;

import com.fy.engineserver.septbuilding.service.SeptBuildingManager;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 模板实例_聚宝庄
 * 
 * 
 */
@SimpleEmbeddable
public class JuBaoZhuang extends SeptBuildingTemplet {
	/** 活力,精力上限 */
	private transient int[] gradeforMaxHp = new int[MAX_LEVEL];
	private transient int[] gradeforMaxMp = new int[MAX_LEVEL];
	/** 基础维修费用 */
	private int[] baseMaintainCost = new int[MAX_LEVEL];
	
	/** 资金上限 */
	private transient long[] maxCoin = new long[MAX_LEVEL];

	private static JuBaoZhuang instance;
	private transient String confPath;

	private JuBaoZhuang() {
	}

	public static JuBaoZhuang getInstance() {
		return instance;
	}

	public void init() throws Exception {
		
		instance = this;
		instance.setBuildingType(BuildingType.聚宝庄);
		instance.setName(getBuildingType().getName());
		instance.initDepend();
		instance.load();
		SeptBuildingManager.type_templet_map.put(instance.getBuildingType(), instance);
		ServiceStartRecord.startLog(this);
	}

	@Override
	public void loadeSpecially(Element root) {
		Element[] es = null;
		es = XmlUtil.getChildrenByName(root, "maxCoin");
		setMaxCoin(StringTool.string2LongArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));
		es = XmlUtil.getChildrenByName(root, "gradeforMaxHp");
		setGradeforMaxHp(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));
		es = XmlUtil.getChildrenByName(root, "gradeforMaxMp");
		setGradeforMaxMp(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));
		es = XmlUtil.getChildrenByName(root, "baseMaintainCost");
		setBaseMaintainCost(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));
	}

	public int[] getBaseMaintainCost() {
		return baseMaintainCost;
	}

	public void setBaseMaintainCost(int[] baseMaintainCost) {
		this.baseMaintainCost = baseMaintainCost;
	}

	public int[] getGradeforMaxHp() {
		return gradeforMaxHp;
	}

	public void setGradeforMaxHp(int[] gradeforMaxHp) {
		this.gradeforMaxHp = gradeforMaxHp;
	}

	public int[] getGradeforMaxMp() {
		return gradeforMaxMp;
	}

	public void setGradeforMaxMp(int[] gradeforMaxMp) {
		this.gradeforMaxMp = gradeforMaxMp;
	}

	@Override
	public void initDepend() {

	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	@Override
	public boolean canLvUp(SeptStation station) {
		// TODO Auto-generated method stub
		return false;
	}

	public long[] getMaxCoin() {
		return maxCoin;
	}

	public void setMaxCoin(long[] maxCoin) {
		this.maxCoin = maxCoin;
	}
}
