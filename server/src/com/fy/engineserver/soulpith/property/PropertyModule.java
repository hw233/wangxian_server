package com.fy.engineserver.soulpith.property;

import java.util.ArrayList;
import java.util.List;

public class PropertyModule {
	private List<AddPropertyTypes> addTypes = new ArrayList<AddPropertyTypes>();
	private List<Propertys> propertys = new ArrayList<Propertys>();
	private List<Integer> propertyNums = new ArrayList<Integer>();
	
	@Override
	public String toString() {
		return "PropertyModule [addTypes=" + addTypes + ", propertys=" + propertys + ", propertyNums=" + propertyNums + "]";
	}
	public void addNewProperty(int addType, int property, int num) {
		AddPropertyTypes at = AddPropertyTypes.valueOf(addType);
		Propertys p = Propertys.valueOf(property);
		addTypes.add(at);
		propertys.add(p);
		propertyNums.add(num);
	}
	public void addNewProperty(AddPropertyTypes at, int property, int num) {
		Propertys p = Propertys.valueOf(property);
		addTypes.add(at);
		propertys.add(p);
		propertyNums.add(num);
	}
	
	public List<AddPropertyTypes> getAddTypes() {
		return addTypes;
	}
	public void setAddTypes(List<AddPropertyTypes> addTypes) {
		this.addTypes = addTypes;
	}
	public List<Propertys> getPropertys() {
		return propertys;
	}
	public void setPropertys(List<Propertys> propertys) {
		this.propertys = propertys;
	}
	public List<Integer> getPropertyNums() {
		return propertyNums;
	}
	public void setPropertyNums(List<Integer> propertyNums) {
		this.propertyNums = propertyNums;
	}
	
}
