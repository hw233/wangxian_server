package com.fy.engineserver.septbuilding.templet;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.w3c.dom.Element;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.septbuilding.service.BuildingState;
import com.fy.engineserver.septbuilding.service.SeptBuildingManager;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 模板实例_标志像白虎
 * 
 * 
 */
@SimpleEmbeddable
public class BiaoZhiXiangBH extends SeptBuildingTemplet {

	private static BiaoZhiXiangBH instance;

	private String confPath;

	/**
	 * buffer的名字
	 */
	private transient String bufferName = Translate.家族白虎;
	/**
	 * buffer的持续时间
	 */
	private transient long[] durations = new long[MAX_LEVEL];
	/**
	 * BUFF价格
	 */
	private transient Long[] costs = new Long[MAX_LEVEL];

	private BiaoZhiXiangBH() {

	}

	public static BiaoZhiXiangBH getInstance() {
		return instance;
	}

	public void init() throws Exception {
		
		instance = this;
		instance.setBuildingType(BuildingType.标志像白虎);
		instance.setName(getBuildingType().getName());
		instance.load();
		instance.initDepend();
		SeptBuildingManager.type_templet_map.put(instance.getBuildingType(), instance);
		ServiceStartRecord.startLog(this);
	}

	@Override
	public boolean canLvUp(SeptStation station) {
		return false;
	}

	@Override
	public void loadeSpecially(Element root) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Element[] es = null;
		// es = XmlUtil.getChildrenByName(root, "bufferName");
		// bufferName = XmlUtil.getAttributeAsString(es[0], "value", null);
		es = XmlUtil.getChildrenByName(root, "durations");
		setDurations(StringTool.string2LongArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));

		es = XmlUtil.getChildrenByName(root, "cost");
		setCosts(StringTool.string2Array(XmlUtil.getAttributeAsString(es[0], "value", null), ",", Long.class));
	}

	@Override
	public void initDepend() {
		setDepend(new BuildingState(LongTuGe.getInstance(), 3));
	}

	public String getBufferName() {
		return bufferName;
	}

	public void setBufferName(String bufferName) {
		this.bufferName = bufferName;
	}

	public long[] getDurations() {
		return durations;
	}

	public void setDurations(long[] durations) {
		this.durations = durations;
	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

	public Long[] getCosts() {
		return costs;
	}

	public void setCosts(Long[] costs) {
		this.costs = costs;
	}

	@Override
	public String toString() {
		return "BiaoZhiXiangBH [confPath=" + confPath + ", bufferName=" + bufferName + ", durations=" + Arrays.toString(durations) + ", costs=" + Arrays.toString(costs) + "]";
	}

}
