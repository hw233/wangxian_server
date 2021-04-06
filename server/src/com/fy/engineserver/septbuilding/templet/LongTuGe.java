package com.fy.engineserver.septbuilding.templet;

import java.util.Arrays;

import org.w3c.dom.Element;

import com.fy.engineserver.septbuilding.service.SeptBuildingManager;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 模板实例_龙图阁
 * 
 * 
 */
@SimpleEmbeddable
public class LongTuGe extends SeptBuildingTemplet {
	/** 灵脉值上限 */
	private transient long[] maxSpirit = new long[MAX_LEVEL];
	/** 特殊商品个数 */
	private transient int[] maxSpecialGoodsNum = new int[MAX_LEVEL];
	/** 标志像Buff时间,秒 */
	private transient int[] timeOfBuff = new int[MAX_LEVEL];

	private transient String confPath;

	private static LongTuGe instance;

	private LongTuGe() {

	}

	public static LongTuGe getInstance() {
		return instance;
	}

	public void init() throws Exception {
		
		instance = this;
		instance.setBuildingType(BuildingType.龙图阁);
		instance.setName(getBuildingType().getName());
		instance.load();
		instance.initDepend();
		SeptBuildingManager.type_templet_map.put(instance.getBuildingType(), instance);
		ServiceStartRecord.startLog(this);
	}

	@Override
	public void loadeSpecially(Element root) {
		Element[] es = null;
		es = XmlUtil.getChildrenByName(root, "maxSpirit");
		setMaxSpirit(StringTool.string2LongArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));
		es = XmlUtil.getChildrenByName(root, "maxSpecialGoodsNum");
		setMaxSpecialGoodsNum(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));
		es = XmlUtil.getChildrenByName(root, "timeOfBuff");
		setTimeOfBuff(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));
//		System.out.println(toString());
	}

	public long[] getMaxSpirit() {
		return maxSpirit;
	}

	public void setMaxSpirit(long[] maxSpirit) {
		this.maxSpirit = maxSpirit;
	}

	public int[] getMaxSpecialGoodsNum() {
		return maxSpecialGoodsNum;
	}

	public void setMaxSpecialGoodsNum(int[] maxSpecialGoodsNum) {
		this.maxSpecialGoodsNum = maxSpecialGoodsNum;
	}

	public int[] getTimeOfBuff() {
		return timeOfBuff;
	}

	public void setTimeOfBuff(int[] timeOfBuff) {
		this.timeOfBuff = timeOfBuff;
	}

	@Override
	public boolean canLvUp(SeptStation station) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initDepend() {
		// TODO Auto-generated method stub

	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	@Override
	public String toString() {
		return "LongTuGe [maxSpirit=" + Arrays.toString(maxSpirit) + ", maxSpecialGoodsNum=" + Arrays.toString(maxSpecialGoodsNum) + ", timeOfBuff=" + Arrays.toString(timeOfBuff) + "]";
	}

}
