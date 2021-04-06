package com.fy.engineserver.jsp.propertyvalue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillCategory {
	public int id;
	
	public List<SkillField> fields = new ArrayList<SkillField>();
	public Map<String , String> map = new HashMap<String, String>();
	
	public SkillCategory(){
		
	}
	
	public int getId(){
		return id;
	}
	
	public boolean hasField(String fieldName){
		if(fieldName.equals("id") || fieldName.equals("name")){
			return true;
		}
		
		for (SkillField field : fields) {
			if(field.getFieldName().equals(fieldName)){
				return true;
			}
		}
		
		return false;
	}
}
