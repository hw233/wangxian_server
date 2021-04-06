package com.fy.engineserver.datasource.article.data.props;

/**
 * 掉落方案
 * 
 * 
 */
public class FlopScheme {

	public static final int RANDOM_BASE = 10000;
	
	/**
	 * 单个模式，一个任务物品只掉落一个，组队的玩家都可以捡
	 * 一个玩家捡了，其他玩家就没有了
	 */
	public static final byte TASKFLOP_SINGLE = 0;
	
	/**
	 * 多个模式，一个任务物品掉落多个，组队的玩家都可以捡
	 * 每一个组队的人都有一份，大家可以互相捡自己的
	 */
	public static final byte TASKFLOP_MULTI = 1;
	
	
	/**
	 * 任务物品的掉落
	 * 
	 *
	 */
	public static class TaskFlop{
		/**
		 * 掉落的任务物品
		 */
		protected String articleName;
		
		/**
		 * 对应掉落的任务物品，每个物品的掉落几率
		 * 值从0～10000，0标识几率为0%，10000标识几率为100%
		 * 此数组的长度必须和articleNames数组的长度一致。
		 */
		protected int probabilitiy;
		
		/**
		 * 任务的类型，支持两种类型:	TASKFLOP_SINGLE 和 TASKFLOP_MULTI
		 * 
		 */
		protected byte flopType;

		public String getArticleName() {
			return articleName;
		}

		public void setArticleName(String articleName) {
			this.articleName = articleName;
		}

		public int getProbabilitiy() {
			return probabilitiy;
		}

		public void setProbabilitiy(int probabilitiy) {
			this.probabilitiy = probabilitiy;
		}

		public byte getFlopType() {
			return flopType;
		}

		public void setFlopType(byte flopType) {
			this.flopType = flopType;
		}
		
		
	}
	
	/**
	 * 掉落方案的名称，此属性为掉落方案的唯一标识，
	 * 所以必须唯一
	 */
	protected String name;
	
	/**
	 * 掉落的钱，单位为最小的单位
	 */
	protected int money;
	

	/**
	 * 任务物品的掉落
	 */
	protected TaskFlop [] taskFlops;
	
	/**
	 * 掉落集合的数组，此数组中所有的位置指定的掉落集合都必须存在
	 */
	protected String propSetNames[];
	
	
	/**
	 * 对应掉落的掉落集合，每个掉落集合的掉落几率
	 * 值从0～10000，0标识几率为0%，10000标识几率为100%
	 * 此数组的长度必须和propSetNames数组的长度一致。
	 */
	protected int propSetProbabilities[];


	public int getMoney() {
		return money;
	}


	public void setMoney(int money) {
		this.money = money;
	}


	

	public TaskFlop[] getTaskFlops() {
		return taskFlops;
	}


	public void setTaskFlops(TaskFlop[] taskFlops) {
		this.taskFlops = taskFlops;
	}


	public String[] getPropSetNames() {
		return propSetNames;
	}


	public void setPropSetNames(String[] propSetNames) {
		this.propSetNames = propSetNames;
	}


	public int[] getPropSetProbabilities() {
		return propSetProbabilities;
	}


	public void setPropSetProbabilities(int[] propSetProbabilities) {
		this.propSetProbabilities = propSetProbabilities;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
}
