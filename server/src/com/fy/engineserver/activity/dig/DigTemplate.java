package com.fy.engineserver.activity.dig;

import com.fy.engineserver.core.g2d.Point;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class DigTemplate implements Cloneable {

	// 0 fengxuezhidian xxx 100 200
	private int index;
	private String mapName;
	private Point points;
	private String mapSegmentNames;
	private int range;
	private String showMap;
	private byte country;
	private byte inCountry;

	public DigTemplate() {
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public Point getPoints() {
		return points;
	}

	public void setPoints(Point points) {
		this.points = points;
	}

	public String getMapSegmentNames() {
		return mapSegmentNames;
	}

	public void setMapSegmentNames(String mapSegmentNames) {
		this.mapSegmentNames = mapSegmentNames;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public String getShowMap() {
		return showMap;
	}

	public void setShowMap(String showMap) {
		this.showMap = showMap;
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public byte getInCountry() {
		return inCountry;
	}

	public void setInCountry(byte inCountry) {
		this.inCountry = inCountry;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		DigTemplate newDigTemplate = new DigTemplate();
		newDigTemplate.index = this.index;
		newDigTemplate.mapName = this.mapName;
		newDigTemplate.points = this.points;
		newDigTemplate.mapSegmentNames = this.mapSegmentNames;
		newDigTemplate.range = this.range;
		newDigTemplate.showMap = this.showMap;
		newDigTemplate.country = this.country;
		newDigTemplate.inCountry = this.inCountry;
		return newDigTemplate;
	}

	@Override
	public String toString() {
		return "DigTemplate [country=" + country + ", inCountry=" + inCountry + ", index=" + index + ", mapName=" + mapName + ", mapSegmentNames=" + mapSegmentNames + ", points=" + points + ", range=" + range + ", showMap=" + showMap + "]";
	}

}
