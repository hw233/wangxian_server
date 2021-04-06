package com.fy.engineserver.sprite.monster;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface MonsterManager {
	
//	public static Logger logger = Logger.getLogger(MonsterManager.class.getName());
public	static Logger logger = LoggerFactory.getLogger(MonsterManager.class.getName());

	public Monster createMonster(int categoryId);
	
	public Monster createMonster(Monster template);
	
	public void removeMonster(Monster s);
	
	/**
	 * 判断一个精灵是否存在
	 * 
	 * @param autoId
	 * @return
	 */
	public boolean isExists(int autoId);

	/**
	 * 通过精灵的Id获得精灵
	 */
	public Monster getMonster(long autoId);

	/**
	 * 得到所有的精灵的个数
	 * 
	 * @return
	 */
	public int getAmountOfMonsters();

	/**
	 * 分页获取精灵，当没有更多精灵时，返回长度为0的数组
	 * 
	 * @param start
	 * @param size
	 * @return
	 */
	public Monster[] getMonstersByPage(int start, int size);

}
