package com.xuanzhi.tools.gamestat;

import java.util.HashMap;
import java.util.Map;

public class RowData implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1543690632507040080L;

	@Index
	long 角色等级;
	
	@Count
	long 人数;
	
	@Percent(denominator="人数")
	long 元神开启;
	
	@Average(denominator="元神开启")
	float 元神加成比;
	
	@Average(denominator="元神开启")
	long 元神等级;
	
	装备 本尊装备;
	宝石 本尊宝石;
	宠物 出战宠物;
	人物技能 人物技能;																												
	器灵 本尊器灵;																											
	坐骑 本尊坐骑;
	
	装备 元神装备;
	宝石 元神宝石;
	器灵 元神器灵;																											
	坐骑 元神坐骑;
	进阶技能 元神技能;
	
	
	public RowData(){
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
		元神技能 = new 进阶技能();
	}
	
	@Mergeable
	public static class 装备 implements java.io.Serializable{
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
	
	public static class 单件装备 implements java.io.Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 770406857478321555L;

		@Count
		long 数量;
		
		@Distribute(members={"白","绿","蓝","紫","完美紫","橙","完美橙"},denominator="数量")
		Map<String,Long> 颜色 = new HashMap<String,Long>();
		
		@Percent(denominator="数量")
		long 铭刻;
		
		@Average(denominator="数量")
		long 星级;
		
		@Average(denominator="数量")
		long 等级;
		
		@Distribute(members={"普通","一般","优秀","卓越","完美","传奇","传说","神话"},denominator="数量")
		Map<String,Long> 鉴定 = new HashMap<String,Long>();
	}
	
	@Mergeable
	public static class 宝石 implements java.io.Serializable{
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
	}

	public static class 单个宝石 implements java.io.Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -1543849884322639583L;

		@Average(denominator="人数")
		long 孔数;
		
		@Average(denominator="镶嵌")
		long 等级;
		
		@Average(denominator="孔数")
		long 镶嵌;
		
	}
	
	public static class 宠物 implements java.io.Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1540565381704280189L
		;
		宠物基本情况 基本情况 = new 宠物基本情况();
		宠物强化 宠物强化 = new 宠物强化();					
		宠物技能 宠物技能 = new 宠物技能();
	}
	
	public static class  宠物基本情况 implements java.io.Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 735630911361379715L;

		@Percent(denominator="人数")
		long 数量;
		
		@Average(denominator="数量")
		long 携带等级;
		
		@Average(denominator="数量")
		long 等级;
		
		@Distribute(members={"随处可见","百里挑一","千载难逢","万年不遇"},denominator="数量")
		Map<String,Long> 稀有度  = new HashMap<String,Long>();
		
		@Distribute(members={"一代","二代","一级变异","二级变异","三级变异","四级变异","五级变异"},denominator="数量")
		Map<String,Long> 繁殖开蛋  = new HashMap<String,Long>();
		
		@Distribute(members={"忠诚","谨慎","精明","狡猾"},denominator="数量")
		Map<String,Long> 性格倾向  = new HashMap<String,Long>();
		
	}
	public static class 宠物强化 implements java.io.Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1544727987469254333L;

		@Percent(denominator="人数")
		long 炼妖;
		
		@Average(denominator="人数")
		long 强化;
		
		@Average(denominator="人数")
		long 进阶;
		
		@Percent(denominator="人数")
		long 天生技能;
		
		@Average(denominator="天生技能")
		long 天生技能等级;
		
		@Percent(denominator="人数")
		long 技能数量; //????

	}
	public static class 宠物技能 implements java.io.Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 4456615248927659845L;

		@Average(denominator="人数")
		long 主动技能格;
		
		@Average(denominator="人数")
		long 被动技能格;
		
		@Distribute(members={"空","1级初级","1级中级","1级高级","2级初级","2级中级","2级高级","3级初级","3级中级","3级高级"},denominator="主动技能格")
		Map<String,Long> 主动技能  = new HashMap<String,Long>();
		
		@Distribute(members={"空","1级","2级","3级"},denominator="被动技能格")
		Map<String,Long> 被动技能  = new HashMap<String,Long>();
	}
	
	public static class 人物技能 implements java.io.Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1175615185087757096L;
		进阶技能 本尊进阶技能 = new 进阶技能(); 
		心法 心法 = new 心法(); 
		注魂 注魂 = new 注魂();
	}
	
	public static class 进阶技能 implements java.io.Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2890131470960220243L;

		@Average(denominator="人数")
		long _41级等级;
	
		@Average(denominator="人数")
		long _56级等级;
		
		@Average(denominator="人数")
		long _71级等级;
		
		@Average(denominator="人数")
		long _86级等级;
		
		@Average(denominator="人数")
		long _101级等级;
		
		@Average(denominator="人数")
		long _116级等级;
		
		@Average(denominator="人数")
		long _131级等级;
		
		@Average(denominator="人数")
		long _146级等级;
		
		@Average(denominator="人数")
		long _161级等级;
		
		@Average(denominator="人数")
		long _176级等级;
		
		@Average(denominator="人数")
		long _191级等级;
		
		@Average(denominator="人数")
		long _206级等级;
	}
	
	public static class 心法 implements java.io.Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -614786164388871447L;

		@Average(denominator="人数")
		long 血心法等级;
		
		@Average(denominator="人数")
		long 外攻心法等级;
		
		@Average(denominator="人数")
		long 内法心法等级;
		
		@Average(denominator="人数")
		long 外防心法等级;
		
		@Average(denominator="人数")
		long 内防心法等级;
	}

	
	public static class 注魂 implements java.io.Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1694557871075023346L;

		@Average(denominator="人数")
		long 火攻等级;
		
		@Average(denominator="人数")
		long 火防等级;
		
		@Average(denominator="人数")
		long 火减抗等级;
		
		@Average(denominator="人数")
		long 冰攻等级;
		
		@Average(denominator="人数")
		long 冰防等级;
		
		@Average(denominator="人数")
		long 冰减抗等级;
		
		@Average(denominator="人数")
		long 雷攻等级;
		
		@Average(denominator="人数")
		long 雷防等级;
		
		@Average(denominator="人数")
		long 雷减抗等级;
		
		@Average(denominator="人数")
		long 风攻等级;
		
		@Average(denominator="人数")
		long 风防等级;
		
		@Average(denominator="人数")
		long 风减抗等级;

	}

	@Mergeable
	public static class 器灵 implements java.io.Serializable{
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
		单个器灵 玄冰之灵_玄冰攻击 = new 单个器灵();
		单个器灵 飓风之灵_疾风攻击 = new 单个器灵();
		单个器灵 惊雷之灵_狂雷攻击 = new 单个器灵();
		单个器灵 御火之灵_炎焚防御 = new 单个器灵();
		单个器灵 御冰之灵_玄冰防御 = new 单个器灵();
		单个器灵 御风之灵_疾风防御 = new 单个器灵();
		单个器灵 御雷之灵_狂雷防御 = new 单个器灵();											

	}
	public static class 单个器灵 implements java.io.Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1892433352187735726L;

		@Average(denominator="人数")
		long 槽数;
		
		@Average(denominator="槽数")
		long 镶嵌;
		
		@Distribute(members={"白器灵","绿器灵","蓝器灵","紫器灵","橙器灵"},denominator="镶嵌")
		Map<String,Long> 品质 = new HashMap<String,Long>();
		 
		@Average(denominator="品质",keyword="白器灵")
		float 白器灵填充 ;
		
		@Average(denominator="品质",keyword="绿器灵")
		float 绿器灵填充;
		
		@Average(denominator="品质",keyword="蓝器灵")
		float 蓝器灵填充;
		
		@Average(denominator="品质",keyword="紫器灵")
		float 紫器灵填充;
		
		@Average(denominator="品质",keyword="橙器灵")
		float 橙器灵填充;
	}
	
	@Mergeable
	public static class 坐骑 implements java.io.Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7096676095485933979L;

		@Percent(denominator="人数")
		long 坐骑数量;
		
		单个坐骑装备 面甲 = new 单个坐骑装备();
		单个坐骑装备 颈甲 = new 单个坐骑装备();
		单个坐骑装备 体铠 = new 单个坐骑装备();
		单个坐骑装备 鞍铠 = new 单个坐骑装备();
		单个坐骑装备 蹄甲 = new 单个坐骑装备();								

	}
	
	
	public static class 单个坐骑装备 implements java.io.Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 4953612742250960512L;

		@Percent(denominator="坐骑数量")
		long 数量;
		
		@Average(denominator="数量")
		long 强化;
		
		@Distribute(members={"白","绿","蓝","紫","完美紫","橙","完美橙"},denominator="数量")
		Map<String,Long> 颜色  = new HashMap<String,Long>();
	}
}



