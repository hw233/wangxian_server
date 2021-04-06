package com.xuanzhi.tools.simplejpa.impl;

/**
 * 此结构对应Mysql中的分区
 * 为什么要存在这个概念，是因为腾讯要求mysql表的行数有限制，希望在100万行以内，
 * 我们现在不知道具体原因是什么，据说他们的mysql自己改过，有这个限制，行数多了会很慢。
 * 
 * 
 * 此分区对应一个表中的某个分区，
 * 
 * 分区是自增长的，通过id来控制。
 * 
 * 所以分区有状态一说:
 * CLOSE
 * OPEN
 * IDLE
 * 
 * CLOSE状态，只能用于查询，更新，删除
 * OPEN状态，可以查询，更新，删除，插入
 * IDLE状态，不能进行任何操作
 * 
 * 但OPEN状态下的分区，达到行数上限，会切换到关闭状态，请启用下一个IDLE分区为OPEN。
 * 
 * 但OPEN分区达到50%满的时候，会定期检查IDLE是否存在，不存在会创建出新的分区
 * 
 *
 */
public class MysqlSection {

	//在这个分区插入数据的时候会更新此值
	public long currentRowNum = 0;
	
	
	//分区编号，编号从1开始
	public int sectionIndex;
	
	public static final int SECION_STATUS_OPEN = 1;
	public static final int SECION_STATUS_CLOSE = 2;
	public static final int SECION_STATUS_IDLE = 3;
	
	//分区状态
	public int status;
	
	//这个分区存储的最大的ID，在这个分区插入数据的时候会更新此值
	public long maxId;
	public long minId;
	public String getName(){
		return "S"+sectionIndex;
	}
	
	public String getNameForPreviousSection(){
		return "S"+(sectionIndex-1);
	}
	
	//这个分区中各个副表存储的最大的ID，在这个分区插入数据的时候会更新此值
	public boolean needCheckExistBeforeUpdateInSecondTables[];

	public boolean isNeedCheckExistBeforeUpdateInSecondTable(int secondTableIndex){
		if(needCheckExistBeforeUpdateInSecondTables == null){
			return false;
		}
		return needCheckExistBeforeUpdateInSecondTables[secondTableIndex];
	}
	
}
