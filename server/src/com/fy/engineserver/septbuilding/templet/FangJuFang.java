package com.fy.engineserver.septbuilding.templet;

import org.w3c.dom.Element;

import com.fy.engineserver.septbuilding.service.BuildingState;
import com.fy.engineserver.septbuilding.service.SeptBuildingManager;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 模板实例_防具坊
 * 
 * 
 */
@SimpleEmbeddable
public class FangJuFang extends SeptBuildingTemplet {

	/** 可出售防具的等级 */
	private transient int[] maxGradeOfFangJu = new int[MAX_LEVEL];

	private static FangJuFang instance;
	private transient String confPath;

	private FangJuFang() {
	}

	public static FangJuFang getInstance() {
		return instance;
	}

	@Override
	public void loadeSpecially(Element root) {
		Element[] es = null;
		es = XmlUtil.getChildrenByName(root, "maxGradeOfFangJu");
		setMaxGradeOfFangJu(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));

	}

	public int[] getMaxGradeOfFangJu() {
		return maxGradeOfFangJu;
	}

	public void setMaxGradeOfFangJu(int[] maxGradeOfFangJu) {
		this.maxGradeOfFangJu = maxGradeOfFangJu;
	}

	@Override
	public boolean canLvUp(SeptStation station) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initDepend() {
		setDepend(new BuildingState(JuBaoZhuang.getInstance(), 3));

	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	public void init() throws Exception {
		
		instance = this;
		instance.setBuildingType(BuildingType.防具坊);
		instance.setName(getBuildingType().getName());
		instance.load();
		instance.initDepend();
		SeptBuildingManager.type_templet_map.put(instance.getBuildingType(), instance);
		ServiceStartRecord.startLog(this);
	}
}
