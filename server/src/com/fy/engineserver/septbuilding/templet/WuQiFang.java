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
 * 模板实例_武器坊
 * 
 * 
 */
@SimpleEmbeddable
public class WuQiFang extends SeptBuildingTemplet {

	/** 可出售武器的等级 */
	private transient int[] maxGradeOfWuQi = new int[MAX_LEVEL];

	private transient static WuQiFang instance;
	private transient String confPath;

	private WuQiFang() {

	}

	public static WuQiFang getInstance() {
		return instance;
	}

	public void init() throws Exception {
		
		instance = this;
		instance.setBuildingType(BuildingType.武器坊);
		instance.setName(getBuildingType().getName());
		instance.load();
		instance.initDepend();
		SeptBuildingManager.type_templet_map.put(instance.getBuildingType(), instance);
		ServiceStartRecord.startLog(this);
	}

	public void loadeSpecially(Element root) {
		Element[] es = null;
		es = XmlUtil.getChildrenByName(root, "maxGradeOfWuQi");
		setMaxGradeOfWuQi(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));
	}

	@Override
	public boolean canLvUp(SeptStation station) {
		// TODO Auto-generated method stub
		return false;
	}

	public int[] getMaxGradeOfWuQi() {
		return maxGradeOfWuQi;
	}

	public void setMaxGradeOfWuQi(int[] maxGradeOfWuQi) {
		this.maxGradeOfWuQi = maxGradeOfWuQi;
	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	@Override
	public void initDepend() {
		setDepend(new BuildingState(XianLingDong.getInstance(), 3));

	}

}
