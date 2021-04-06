package com.fy.engineserver.septbuilding.templet;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.septbuilding.entity.JiazuBedge;
import com.fy.engineserver.septbuilding.service.SeptBuildingManager;
import com.fy.engineserver.septstation.SeptStation;
import com.fy.engineserver.septstation.SeptStationMapTemplet;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.StringTool;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;
import com.xuanzhi.tools.text.XmlUtil;

/**
 * 模板实例_仙灵洞
 * 
 * 
 */
@SimpleEmbeddable
public class XianLingDong extends SeptBuildingTemplet {

	private transient int[] maxMember = new int[MAX_LEVEL];

	private static XianLingDong instance;
	private transient String confPath;

	private XianLingDong() {
	}

	public static XianLingDong getInstance() {
		return instance;
	}

	public void init() throws Exception {
		
		instance = this;
		instance.setBuildingType(BuildingType.仙灵洞);
		instance.setName(getBuildingType().getName());
		instance.load();
		instance.initDepend();
		SeptBuildingManager.type_templet_map.put(instance.getBuildingType(), instance);
		ServiceStartRecord.startLog(this);
	}

	@Override
	public void loadeSpecially(Element root) {
		Element[] es = null;
		es = XmlUtil.getChildrenByName(root, "maxMember");
		setMaxMember(StringTool.string2IntArr(XmlUtil.getAttributeAsString(es[0], "value", null), ","));
	}

	@Override
	public boolean canLvUp(SeptStation station) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLevelUp(SeptStation station) {
		super.onLevelUp(station);
		int level = station.getBuildingLevel(getBuildingType());
		if (SeptStationMapTemplet.getInstance().getBedges().containsKey(level)) {
			List<JiazuBedge> openBedges = SeptStationMapTemplet.getInstance().getBedges().get(level);
			if (openBedges != null && openBedges.size() > 0) {
				Jiazu jiazu = station.getJiazu();
				List<Integer> openBedgeIds = new ArrayList<Integer>();
				for (JiazuBedge bedge : openBedges) {
					if (bedge.getType() == JiazuBedge.TYPE_BASE) {
						openBedgeIds.add(bedge.getId());
					}
				}

				jiazu.getBedges().addAll(openBedgeIds);
				jiazu.notifyFieldChange("bedges");
			}
		}
	}

	@Override
	public void onLevelDown(SeptStation station) {
		super.onLevelDown(station);
		int level = station.getBuildingLevel(getBuildingType()) + 1;
		if (SeptStationMapTemplet.getInstance().getBedges().containsKey(level)) {
			List<JiazuBedge> openBedges = SeptStationMapTemplet.getInstance().getBedges().get(level);
			if (openBedges != null && openBedges.size() > 0) {
				Jiazu jiazu = station.getJiazu();
				List<Integer> openBedgeIds = new ArrayList<Integer>();
				for (JiazuBedge bedge : openBedges) {
					if (bedge.getType() == JiazuBedge.TYPE_BASE) {
						openBedgeIds.remove(bedge.getId());
					}
				}
				jiazu.getBedges().removeAll(openBedgeIds);
			}
		}
	}

	@Override
	public void initDepend() {
		// TODO Auto-generated method stub

	}

	public int[] getMaxMember() {
		return maxMember;
	}

	public void setMaxMember(int[] maxMember) {
		this.maxMember = maxMember;
	}

	public String getConfPath() {
		return confPath;
	}

	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}

}
