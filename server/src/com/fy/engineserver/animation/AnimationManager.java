package com.fy.engineserver.animation;

import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.PLAY_ANIMATION_REQ;

public class AnimationManager {

	/**
	 * 和动画名称数组对应的类型
	 */
	public static final int 头像转圈 = 0;
	public static final int 冷却完毕 = 1;
	public static final int 可以释放 = 2;
	public static final int 装备升级成功 = 3;
	public static final int 装备升级失败 = 4;
	public static final int 装备摘星成功 = 5;
	public static final int 装备摘星失败 = 6;
	public static final int 装备鉴定成功 = 7;
	public static final int 装备精炼绑定铭刻成功 = 8;
	public static final int 装备装星成功 = 9;
	public static final int 装备镶嵌成功 = 10;
	public static final int 合成成功 = 11;
	public static final int 宠物强化成功 = 12;
	public static final int 宠物强化失败 = 13;
	public static final int 宠物还童成功 = 14;
	
	/**
	 * 客户端光效播放的位置，如果客户端找不到位置类型，那么就用后面的xy来定位播放位置
	 */
	public static final int 动画播放位置类型_默认 = 0;
	public static final int 动画播放位置类型_武器强化中间孔 = 1;
	public static final int 动画播放位置类型_精炼绑定铭刻 = 2;
	public static final int 动画播放位置类型_装备鉴定 = 3;
	public static final int 动画播放位置类型_摘星 = 4;
	public static final int 动画播放位置类型_装星 = 5;
	public static final int 动画播放位置类型_镶嵌 = 6;
	public static final int 动画播放位置类型_宠物合体 = 7;
	public static final int 动画播放位置类型_宠物繁殖 = 8;
	public static final int 动画播放位置类型_宠物强化升星 = 9;
	public static final int 动画播放位置类型_宠物鉴定 = 10;
	public static final int 动画播放位置类型_宠物还童 = 11;
	
	public static final String[][] 动画名称 = new String[][]{{"fever","0"},{"fever","1"},{"fever","2"},{"面板光效/合成成功","1"},{"面板光效/合成失败","1"},{"面板光效/星星飞向背包","1"},{"面板光效/星星碎裂","1"},{"面板光效/装备绑定","1"},{"面板光效/装备绑定","1"},{"面板光效/附星动画","1"},{"面板光效/装备绑定","1"},{"面板光效/合成成功","1"},
		{"面板光效/合成成功","1"},{"面板光效/合成失败","1"},{"面板光效/合成成功","1"}};
	
	/**
	 * 当播放位置为默认时，参数x,y表示横纵坐标百分比，不为默认时这两个参数不起作用
	 * @param animationType
	 * @param round
	 * @param locationType
	 * @param x
	 * @param y
	 * @return
	 */
	public static PLAY_ANIMATION_REQ 组织播放动画协议(int animationType, byte round, int locationType, int x, int y){
		PLAY_ANIMATION_REQ req = null;
		if(animationType >= 0 && animationType < 动画名称.length){
			try{
				req = new PLAY_ANIMATION_REQ(GameMessageFactory.nextSequnceNum(), 动画名称[animationType][0], 动画名称[animationType][1], round, (byte)locationType, (byte)x, (byte)y);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return req;
	}
	
}
