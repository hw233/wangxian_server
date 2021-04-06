package com.fy.engineserver.septbuilding.templet;

import org.w3c.dom.Element;

import com.fy.engineserver.septbuilding.service.SeptBuildingManager;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 模板实例_家族大旗
 * 
 * 
 */
@SimpleEmbeddable
public class JiaZuQi extends SeptBuildingTemplet {

	/** 大旗生命 */
	private transient int[] hp = new int[MAX_LEVEL];

	private static JiaZuQi instance;

	private transient String confPath;

	public JiaZuQi() {

	}

	public static JiaZuQi getInstance() {
		return instance;
	}

	public void init() throws Exception {
		
		instance = this;
		instance.setBuildingType(BuildingType.家族大旗);
		instance.load();
		instance.initDepend();
		instance.setName(getBuildingType().getName());
		SeptBuildingManager.type_templet_map.put(instance.getBuildingType(), instance);
		ServiceStartRecord.startLog(this);
	}

	@Override
	public void loadeSpecially(Element root) {
		Element[] es = null;
		es = XmlUtil.getChildrenByName(root, "hp");
		setHp(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));
	}

	public int[] getHp() {
		return hp;
	}

	public void setHp(int[] hp) {
		this.hp = hp;
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
}
