package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 有颜色等级区分物品的统一接口
 * 
 *
 */
public interface Gem {

	public static final int 白装 = 0;
	public static final int 绿装 = 1;
	public static final int 蓝装 = 2;
	public static final int 紫装 = 3;
	public static final int 橙装 = 4;
	
	public static final String[] COLOR_TYPE_NAMES = new String[]{Translate.text_3714,Translate.text_3715,Translate.text_975,Translate.text_3716,Translate.text_3717};
	
	public String getName();
	
	public String getIconId();
	
	public int getColorType();
}
