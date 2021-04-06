package com.fy.engineserver.lineup;

import java.util.HashMap;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 队伍角色对象
 * 
 *
 */
public class TeamRole {
	public static final byte 奶妈 = 0;
	
	public static final byte 坦克 = 1;
	
	public static final byte 输出 = 2;
	
	public static final String roledesp[] = new String[]{Translate.text_4687,Translate.text_4688,Translate.text_4689};
	public static final String iconid[] = new String[]{"59","56","79"};
	
	public static final HashMap<Integer, String[]> roleCareerMap = new HashMap<Integer, String[]>();
	
	static {
		roleCareerMap.put(0, new String[]{Translate.text_1156,Translate.text_1155});
		roleCareerMap.put(1, new String[]{Translate.text_1154});
		roleCareerMap.put(2, new String[]{Translate.text_1154,Translate.text_1155,Translate.text_1156,Translate.text_1157,Translate.text_1158});
	}
	
	public static String[] getRoleCareers(byte roleId) {
		return roleCareerMap.get(new Integer(roleId));
	}
	
	public static String getRoleDesp(byte roleid) {
		try {
			return roledesp[roleid];
		} catch(Exception e) {
			//
		}
		return Translate.text_1211;
	} 
	
	public static boolean canBeRole(String career, byte roleId) {
		String careers[] = getRoleCareers(roleId);
		if(careers == null || careers.length == 0) {
			return false;
		}
		for(String c : careers) {
			if(c.equals(career)) {
				return true;
			}
		}
		return false;
	}
}
