package com.fy.engineserver.constants;

import com.fy.engineserver.core.res.Constants;
import com.fy.engineserver.datasource.language.Translate;

/**
 * 初始出生的配置
 * 
 */
public class InitialPlayerConstant {
	
	public static final String 各门派的名称[] = new String[]{Translate.通用, Translate.修罗, Translate.影魅, Translate.仙心, Translate.九黎, Translate.兽魁};
	
	public static final String 各门派的出生地图[] = new String[]{
		Constants.mapNameBorn,Constants.mapNameBorn,Constants.mapNameBorn,Constants.mapNameBorn,Constants.mapNameBorn,Constants.mapNameBorn
	};
	
	public static final String 各门派的出生地图的区域[][] = new String[][]{
		Constants.mapAreaBorn,Constants.mapAreaBorn,Constants.mapAreaBorn,Constants.mapAreaBorn,Constants.mapAreaBorn,Constants.mapAreaBorn
	};
	
	public static final String GM = "[Gg][Mm].*";
	
	public static final int GANGEXPENSE = 30000;
	public static final int JAIZU_CREATE_EXPENSE = 100 * 1000;
	
}
