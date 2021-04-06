package com.fy.engineserver.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.xuanzhi.tools.gamestat.Average;
import com.xuanzhi.tools.gamestat.Count;
import com.xuanzhi.tools.gamestat.Distribute;
import com.xuanzhi.tools.gamestat.Index;
import com.xuanzhi.tools.gamestat.Mergeable;
import com.xuanzhi.tools.gamestat.Percent;

public class RowData implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1543690632507040080L;

	@Index
	long 角色等级;

	@Count
	long 人数;

	@Average(denominator = "人数")
	public long 充值金额;

	@Percent(denominator = "人数")
	long 元神开启;

	@Average(denominator = "元神开启")
	float 元神加成比;

	@Average(denominator = "元神开启")
	long 元神等级;

	@Average(denominator = "人数")
	int 渡劫;

	@Average(denominator = "人数")
	int 大师技能等级;

	装备 本尊装备;
	宝石 本尊宝石;
	宠物 出战宠物;
	人物技能 人物技能;
	器灵 本尊器灵;
	坐骑 本尊坐骑;
	法宝 本尊法宝;
//	坐骑装备 本尊坐骑装备;
	附魔 本尊附魔;

	装备 元神装备;
	宝石 元神宝石;
	器灵 元神器灵;
	坐骑 元神坐骑;
	进阶技能 元神进阶技能;
	法宝 元神法宝;
//	坐骑装备 元神坐骑装备;
	附魔 元神附魔;

	玩家翅膀  玩家翅膀;
	家族修炼 家族修炼;
	兽魂 玩家兽魂;
	
	public RowData() {
		// this.角色等级 = 角色等级;
		本尊装备 = new 装备();
		本尊宝石 = new 宝石();
		出战宠物 = new 宠物();
		人物技能 = new 人物技能();
		本尊器灵 = new 器灵();
		本尊坐骑 = new 坐骑();
		元神装备 = new 装备();
		元神宝石 = new 宝石();
		元神器灵 = new 器灵();
		元神坐骑 = new 坐骑();
		元神进阶技能 = new 进阶技能();
		本尊法宝 = new 法宝();
		元神法宝 = new 法宝();
		玩家翅膀 = new 玩家翅膀();
		家族修炼 = new 家族修炼();
		本尊附魔 = new 附魔();
		元神附魔 = new 附魔();
		玩家兽魂 = new 兽魂();
//		本尊坐骑装备 = new 坐骑装备();
//		元神坐骑装备 = new 坐骑装备();
	}

	@Mergeable
	public static class 装备 implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1802433874953551946L;

		单件装备 武器 = new 单件装备();
		单件装备 头部 = new 单件装备();
		单件装备 饰品 = new 单件装备();
		单件装备 腰带 = new 单件装备();
		单件装备 项链 = new 单件装备();
		单件装备 鞋子 = new 单件装备();
		单件装备 戒指 = new 单件装备();
		单件装备 护腕 = new 单件装备();
		单件装备 肩部 = new 单件装备();
		单件装备 胸部 = new 单件装备();

	}
	@Mergeable
	public static class 法宝 implements java.io.Serializable {
		@Percent(denominator = "人数")
		long 数量;
		@Average(denominator = "数量")
		long 等级;
		@Average(denominator = "数量")
		long 品阶;
		@Average(denominator = "数量")
		long 加持;
		
		@Distribute(members = { "通用", "修罗", "影魅", "九黎", "仙心" ,"兽魁"}, denominator = "数量")
		Map<String, Long> 职业类型 = new HashMap<String, Long>();
		
		@Distribute(members = {"白","绿","蓝","紫","橙","金"}, denominator = "数量")
		Map<String, Long> 颜色 = new HashMap<String, Long>();
		
		@Distribute(members = {"天魁","陀罗","恩光","擎羊","巨猿","野猪","英侠","巨鲸","野熊","雄鹰","禄存","贪狼","九幽","天机","天梁","灵猴","天钺","雪舞","猛虎","孤狼","野仙","猎鹰","武曲星君","破军","益寿","浩然","空明","刃舞","凌云","截空","夜枭","玲珑"}, denominator = "数量")
		Map<String, Long> 基础属性 = new HashMap<String, Long>();
		
		@Percent(denominator = "可开启隐藏属性最大数")
		double 隐藏属性开启比例;
		@Count
		double 可开启隐藏属性最大数;
		
		@Distribute(members = {"白属性","绿属性","蓝属性","紫属性","橙属性","金属性"}, denominator = "隐藏属性开启比例")
		Map<String, Long> 属性颜色 = new HashMap<String, Long>();
	
		@Distribute(members = {"2个","3个","4个","5个"}, denominator = "隐藏属性开启比例")
		Map<String, Integer> 同属性占比 = new HashMap<String, Integer>();
	

	}
		
	public static class 单件装备 implements java.io.Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 770406857478321555L;

		@Count
		long 数量;

		@Distribute(members = { "白", "绿", "蓝", "紫", "完美紫", "橙", "完美橙" }, denominator = "数量")
		Map<String, Long> 颜色 = new HashMap<String, Long>();

		@Percent(denominator = "数量")
		long 铭刻;

		@Average(denominator = "数量")
		long 星级;
		@Average(denominator = "数量")
		long 羽化;

		@Average(denominator = "数量")
		long 等级;

		@Distribute(members = { "未鉴定", "普通", "一般", "优秀", "卓越", "完美", "传奇", "传说", "神话", "永恒" }, denominator = "数量")
		Map<String, Long> 鉴定 = new HashMap<String, Long>();
	}

	@Mergeable
	public static class 宝石 implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -339809993039550547L;
		单个宝石 宝石竹清 = new 单个宝石();
		单个宝石 宝石枳香 = new 单个宝石();
		单个宝石 宝石幽橘 = new 单个宝石();
		单个宝石 宝石湛天 = new 单个宝石();
		单个宝石 宝石墨轮 = new 单个宝石();
		单个宝石 宝石炎焚 = new 单个宝石();
		单个宝石 宝石无相 = new 单个宝石();
		单个宝石 宝石魔渊 = new 单个宝石();
		单个宝石 宝石混沌 = new 单个宝石();

		单个宝石 宝石焚融 = new 单个宝石();
		单个宝石 宝石焚天 = new 单个宝石();
		单个宝石 宝石焚荒 = new 单个宝石();
		单个宝石 宝石焚焱 = new 单个宝石();

		单个宝石 宝石寒冰 = new 单个宝石();
		单个宝石 宝石寒霜 = new 单个宝石();
		单个宝石 宝石寒雨 = new 单个宝石();
		单个宝石 宝石寒颤 = new 单个宝石();

		单个宝石 宝石离火 = new 单个宝石();
		单个宝石 宝石朔风 = new 单个宝石();
		单个宝石 宝石狂风 = new 单个宝石();
		单个宝石 宝石暴风 = new 单个宝石();

		单个宝石 宝石雷霆 = new 单个宝石();
		单个宝石 宝石雷击 = new 单个宝石();
		单个宝石 宝石雷鸣 = new 单个宝石();
		单个宝石 宝石雷诸 = new 单个宝石();
	}

	public static class 单个宝石 implements java.io.Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1543849884322639583L;

		@Average(denominator = "人数")
		long 孔数;

		@Average(denominator = "镶嵌")
		long 等级;

		@Percent(denominator = "孔数")
		long 镶嵌;

	}

	public static class 宠物 implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1540565381704280189L;
		宠物基本情况 基本情况 = new 宠物基本情况();
		宠物强化 宠物强化 = new 宠物强化();
		宠物技能 宠物技能 = new 宠物技能();
	}

	public static class 宠物基本情况 implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 735630911361379715L;

		@Percent(denominator = "人数")
		long 数量;

		@Average(denominator = "数量")
		long 携带等级;

		@Average(denominator = "数量")
		long 等级;

		@Distribute(members = { "随处可见", "百里挑一", "千载难逢", "万年不遇" }, denominator = "数量")
		Map<String, Long> 稀有度 = new HashMap<String, Long>();

		@Distribute(members = { "未变异", "一级变异", "二级变异", "三级变异", "四级变异", "五级变异" }, denominator = "数量")
		Map<String, Long> 变异 = new HashMap<String, Long>();
		@Distribute(members = { "一代", "二代" }, denominator = "数量")
		Map<String, Long> 几代 = new HashMap<String, Long>();

		@Distribute(members = { "忠诚", "谨慎", "精明", "狡猾" }, denominator = "数量")
		Map<String, Long> 性格倾向 = new HashMap<String, Long>();

	}

	public static class 宠物强化 implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1544727987469254333L;

		@Average(denominator = "人数")
		float 炼妖;

		@Average(denominator = "人数")
		long 强化;

		@Average(denominator = "人数")
		long 进阶;

		@Percent(denominator = "人数")
		long 先天技能;
		@Percent(denominator = "人数")
		long 后天技能;

		@Average(denominator = "人数")
		long 天赋技能数量;

		@Percent(denominator = "人数")
		long 基础技能数量;

		@Distribute(members = { "空", "初级", "高级", "终级" }, denominator = "天赋技能数量")
		Map<String, Long> 天赋技能 = new HashMap<String, Long>();

		@Distribute(members = { "空", "1级", "2级", "3级", "4级" }, denominator = "基础技能数量")
		Map<String, Long> 基础技能 = new HashMap<String, Long>();

	}

	public static class 宠物技能 implements java.io.Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4456615248927659845L;

		@Average(denominator = "人数")
		long 主动技能格;
		@Average(denominator = "人数")
		long 主动技数量;

		@Average(denominator = "人数")
		long 被动技能格;
		@Average(denominator = "人数")
		long 被动技数量;

		@Distribute(members = { "空", "1级初级", "1级中级", "1级高级", "2级初级", "2级中级", "2级高级", "3级初级", "3级中级", "3级高级" }, denominator = "主动技能格")
		Map<String, Long> 主动技能 = new HashMap<String, Long>();

		@Distribute(members = { "空", "1级", "2级", "3级" }, denominator = "被动技能格")
		Map<String, Long> 被动技能 = new HashMap<String, Long>();
	}

	public static class 人物技能 implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1175615185087757096L;
		进阶技能 本尊进阶技能 = new 进阶技能();
		心法 心法 = new 心法();
		注魂 注魂 = new 注魂();
	}

	public static class 进阶技能 implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2890131470960220243L;

		@Average(denominator = "人数")
		long _41级等级;

		@Average(denominator = "人数")
		long _56级等级;

		@Average(denominator = "人数")
		long _71级等级;

		@Average(denominator = "人数")
		long _86级等级;

		@Average(denominator = "人数")
		long _101级等级;

		@Average(denominator = "人数")
		long _116级等级;

		@Average(denominator = "人数")
		long _131级等级;

		@Average(denominator = "人数")
		long _146级等级;

		@Average(denominator = "人数")
		long _161级等级;

		@Average(denominator = "人数")
		long _176级等级;

		@Average(denominator = "人数")
		long _191级等级;

		@Average(denominator = "人数")
		long _206级等级;
	}

	public static class 心法 implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -614786164388871447L;

		@Average(denominator = "人数")
		long 血心法等级;

		@Average(denominator = "人数")
		long 外攻心法等级;

		@Average(denominator = "人数")
		long 内法心法等级;

		@Average(denominator = "人数")
		long 外防心法等级;

		@Average(denominator = "人数")
		long 内防心法等级;
	}

	public static class 注魂 implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1694557871075023346L;

		@Average(denominator = "人数")
		long 火攻等级;

		@Average(denominator = "人数")
		long 金防等级;

		@Average(denominator = "人数")
		long 火减抗等级;

		@Average(denominator = "人数")
		long 冰攻等级;

		@Average(denominator = "人数")
		long 水防等级;

		@Average(denominator = "人数")
		long 冰减抗等级;

		@Average(denominator = "人数")
		long 雷攻等级;

		@Average(denominator = "人数")
		long 木防等级;

		@Average(denominator = "人数")
		long 雷减抗等级;

		@Average(denominator = "人数")
		long 风攻等级;

		@Average(denominator = "人数")
		long 火防等级;

		@Average(denominator = "人数")
		long 风减抗等级;

	}

	@Mergeable
	public static class 器灵 implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1445348473674646407L;
		单个器灵 长生之灵_生命 = new 单个器灵();
		单个器灵 擎天之灵_外攻 = new 单个器灵();
		单个器灵 琼浆之灵_内法 = new 单个器灵();
		单个器灵 金汤之灵_外防 = new 单个器灵();
		单个器灵 罡岚之灵_内防 = new 单个器灵();
		单个器灵 离火之灵_炎焚攻击 = new 单个器灵();
		单个器灵 葵水之灵_葵水攻击 = new 单个器灵();
		单个器灵 飓风之灵_离火攻击 = new 单个器灵();
		单个器灵 惊雷之灵_乙木攻击 = new 单个器灵();
		单个器灵 御火之灵_炎焚防御 = new 单个器灵();
		单个器灵 御冰之灵_葵水防御 = new 单个器灵();
		单个器灵 御风之灵_离金防御 = new 单个器灵();
		单个器灵 御雷之灵_乙木防御 = new 单个器灵();

	}

	public static class 单个器灵 implements java.io.Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -1892433352187735726L;

		@Average(denominator = "人数")
		long 槽数;

		@Percent(denominator = "槽数")
		long 镶嵌;

		@Distribute(members = { "白器灵", "绿器灵", "蓝器灵", "紫器灵", "橙器灵" }, denominator = "镶嵌")
		Map<String, Long> 品质 = new HashMap<String, Long>();

		@Percent(denominator = "品质", keyword = "白器灵")
		float 白器灵填充;

		@Percent(denominator = "品质", keyword = "绿器灵")
		float 绿器灵填充;

		@Percent(denominator = "品质", keyword = "蓝器灵")
		float 蓝器灵填充;

		@Percent(denominator = "品质", keyword = "紫器灵")
		float 紫器灵填充;

		@Percent(denominator = "品质", keyword = "橙器灵")
		float 橙器灵填充;

		public 单个器灵() {
			品质.put("白器灵", 0L);
			品质.put("绿器灵", 0L);
			品质.put("蓝器灵", 0L);
			品质.put("紫器灵", 0L);
			品质.put("橙器灵", 0L);
		}
	}

	@Mergeable
	public static class 坐骑 implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7096676095485933979L;

		@Average(denominator = "人数")
		long 普通坐骑数量;
		@Average(denominator = "人数")
		long 临时飞行坐骑数量;
		@Average(denominator = "人数")
		long 永久飞行坐骑数量;
		
		@Average(denominator = "人数")
		int 培养;
		
		@Average(denominator = "人数")
		int 血脉;
		
		坐骑技能 技能 = new 坐骑技能();

		@Distribute(members = { "白", "绿", "蓝", "紫", "橙" }, denominator = "普通坐骑数量")
		Map<String, Long> 品质 = new HashMap<String, Long>();


		@Distribute(members = { "爱的炫舞", "浪漫今生", "碧虚青鸾", "八卦仙蒲", "梦瞳仙鹤", "乾坤葫芦", "金极龙皇", "焚焰火扇", "深渊魔章" }, denominator = "永久飞行坐骑数量")
		Map<String, Long> 飞行坐骑 = new HashMap<String, Long>();

		单个坐骑装备 金盔 = new 单个坐骑装备();
		单个坐骑装备 剑翅 = new 单个坐骑装备();
		单个坐骑装备 腿甲 = new 单个坐骑装备();
		单个坐骑装备 配鞍 = new 单个坐骑装备();
		单个坐骑装备 鳞甲 = new 单个坐骑装备();
		

	}

	public static class 单个坐骑装备 implements java.io.Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4953612742250960512L;

		@Percent(denominator = "普通坐骑数量")
		long 数量;

		@Average(denominator = "数量")
		long 强化;
		
		@Distribute(members = { "白", "绿", "蓝", "紫", "完美紫", "橙", "完美橙" }, denominator = "数量")
		Map<String, Long> 颜色 = new HashMap<String, Long>();
	}
	
//	public static class 坐骑装备 implements java.io.Serializable{
//
//		private static final long serialVersionUID = 6111238620162344125L;
//		@Average(denominator = "人数")
//		int 等级;
//		
//		@Average(denominator = "人数")
//		int 星级;
//		
//		@Percent(denominator = "普通坐骑数量")
//		long 坐骑数量;
//		
//		@Distribute(members = { "0", "1", "2", "3", "4" }, denominator = "坐骑数量")
//		Map<String, Long> 品质 = new HashMap<String, Long>();
//		
//		坐骑技能 技能 = new 坐骑技能();
//		
//	}

	public static class 坐骑技能 implements java.io.Serializable{
		

		/**
		 * 
		 */
		private static final long serialVersionUID = -7612550028071893382L;
		//分母
		int 本尊坐骑已开启技能格总数;
		int 中低级技能总数;
		int 中高级技能总数;
		
		@Percent(denominator = "本尊坐骑已开启技能格总数")
		int 低级技能占比;
		@Percent(denominator = "中低级技能总数")
		int 一级低级技能占比;
		@Percent(denominator = "中低级技能总数")
		int 二级低级技能占比;
		@Percent(denominator = "中低级技能总数")
		int 三级低级技能占比;
		@Percent(denominator = "本尊坐骑已开启技能格总数")
		int 高级技能占比;
		@Percent(denominator = "中高级技能总数")
		int 一级高级技能占比;
		@Percent(denominator = "中高级技能总数")
		int 二级高级技能占比;
		@Percent(denominator = "中高级技能总数")
		int 三级高级技能占比;
	}
	
	public static class 玩家翅膀 implements java.io.Serializable{
		private static final long serialVersionUID = 2517171193035186191L;
		
		翅膀收集数量 翅膀收集数量 = new 翅膀收集数量();
		翅膀强化 翅膀强化 = new 翅膀强化();
		翅膀属性宝石镶嵌率 翅膀属性宝石镶嵌率 = new 翅膀属性宝石镶嵌率();
		翅膀属性宝石镶嵌等级 翅膀属性宝石镶嵌等级 = new 翅膀属性宝石镶嵌等级();
		光效宝石镶嵌率 光效宝石镶嵌率 = new 光效宝石镶嵌率();
		
	}
	
	public static class 翅膀收集数量 implements java.io.Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5526958139971401783L;
		@Percent(denominator = "人数")
		int 无翅膀人数; 
		int 一号翅膀数;
		int 二号翅膀数;
		int 三号翅膀数;
		int 四号翅膀数;
		int 五号翅膀数;
		int 六号翅膀数;
		int 七号翅膀数;
		int 八号翅膀数;
		int 九号翅膀数;
		int 十号翅膀数;
	}
	
	public static class 翅膀强化 implements java.io.Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 9052353304338864037L;
		int 有翅膀人数; 
		@Average(denominator = "有翅膀人数")
		int 强化平均等级;
	}
	
	public static class 翅膀属性宝石镶嵌率 implements java.io.Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1812498667555311479L;
		int 激活翅膀的人数;
		@Percent(denominator = "激活翅膀的人数")
		double 绿色镶嵌率;
		@Percent(denominator = "激活翅膀的人数")
		double 橙色镶嵌率;
		@Percent(denominator = "激活翅膀的人数")
		double 蓝色镶嵌率;
		@Percent(denominator = "激活翅膀的人数")
		double 黑色镶嵌率;
		
	}
	
	public static class 翅膀属性宝石镶嵌等级 implements java.io.Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 4648568881071172700L;
		int 激活翅膀的人数;
		@Average(denominator = "激活翅膀的人数")
		double 绿色镶嵌等级;
		@Average(denominator = "激活翅膀的人数")
		double 橙色镶嵌等级;
		@Average(denominator = "激活翅膀的人数")
		double 蓝色镶嵌等级;
		@Average(denominator = "激活翅膀的人数")
		double 黑色镶嵌等级;
		
	}
	
	public static class 光效宝石镶嵌率 implements java.io.Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 533428093240071090L;

		int 激活翅膀的人数;
		
		@Percent(denominator = "激活翅膀的人数")
		int 无光效宝石;
		@Percent(denominator = "激活翅膀的人数")
		int 三天初级宝石;
		@Percent(denominator = "激活翅膀的人数")
		int 三天中级宝石;
		@Percent(denominator = "激活翅膀的人数")
		int 三天高级宝石;
		@Percent(denominator = "激活翅膀的人数")
		int 十五天初级宝石;
		@Percent(denominator = "激活翅膀的人数")
		int 十五天中级宝石;
		@Percent(denominator = "激活翅膀的人数")
		int 十五天高级宝石;
	}
	
	public static class 附魔 implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1183053265882457053L;
		@Percent(denominator = "人数")
		int 武器附魔普及率;
		@Percent(denominator = "人数")
		int 戒指附魔普及率;
		@Percent(denominator = "人数")
		int 头部附魔普及率;
		@Percent(denominator = "人数")
		int 胸甲附魔普及率;
		
		@Percent(denominator = "武器附魔普及率")
		int 武器附魔蓝色占比; 
		@Percent(denominator = "戒指附魔普及率")
		int 戒指附魔蓝色占比; 
		@Percent(denominator = "头部附魔普及率")
		int 头盔附魔蓝色占比; 
		@Percent(denominator = "胸甲附魔普及率")
		int 胸甲附魔蓝色占比; 
		
	}
	
	public static class 兽魂 implements java.io.Serializable{
		
		int 已开启兽魂人数;
		int 攻击宝石个数;
		int 暴击宝石个数;
		int 精准宝石个数;
		int 破甲宝石个数;
		int 命中宝石个数;
		int 气血宝石个数;
		int 物防宝石个数;
		int 法防宝石个数;
		int 免爆宝石个数;
		int 躲闪宝石个数;
		
		@Percent(denominator = "已开启兽魂人数")
		int 镶嵌率;
		
		@Percent(denominator = "攻击宝石个数")
		int 攻击等级;
		@Average(denominator = "暴击宝石个数")
		int 暴击等级;
		@Average(denominator = "精准宝石个数")
		int 精准等级;
		@Average(denominator = "破甲宝石个数")
		int 破甲等级;
		@Average(denominator = "命中宝石个数")
		int 命中等级;
		@Average(denominator = "气血宝石个数")
		int 气血等级;
		@Average(denominator = "物防宝石个数")
		int 物防等级;
		@Average(denominator = "法防宝石个数")
		int 法防等级;
		@Average(denominator = "免爆宝石个数")
		int 免爆等级;
		@Average(denominator = "躲闪宝石个数")
		int 躲闪等级;
		
	}
	
	
	public static class 家族修炼 implements java.io.Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 2224614499034382341L;

		@Percent(denominator = "人数")
		int 无家族占比;
		
		int 有家族人数;
		@Average(denominator = "有家族人数")
		int 物攻;
		@Average(denominator = "有家族人数")
		int 法功;
		@Average(denominator = "有家族人数")
		int 暴击;
		@Average(denominator = "有家族人数")
		int 精准;
		@Average(denominator = "有家族人数")
		int 破甲;
		@Average(denominator = "有家族人数")
		int 命中;
		@Average(denominator = "有家族人数")
		int 气血;
		@Average(denominator = "有家族人数")
		int 物防;
		@Average(denominator = "有家族人数")
		int 法防;
		@Average(denominator = "有家族人数")
		int 免爆;
		@Average(denominator = "有家族人数")
		int 躲闪;
	}
	
	
}
