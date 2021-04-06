package com.fy.engineserver.constants;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 游戏中常量
 * 
 */
public interface GameConstant {
	/** 人物高度，用于绘制眩晕等效果 **/
	public final static int PLAYER_HEIGHT = 80;
	
	public final static byte 中立阵营 = 0;
	public final static byte 紫薇宫 = 1;
	public final static byte 日月盟 = 2;
	
	public final static String 阵营名称[] = new String[]{Translate.text_2637,Translate.text_1152,Translate.text_1153};

}
