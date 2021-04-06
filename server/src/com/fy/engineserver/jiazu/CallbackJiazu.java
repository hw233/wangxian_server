package com.fy.engineserver.jiazu;

import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.transport.Message;

/**
 * 
 * 回调方法，接受两个参数Message和Player,在execute方法之中完成具体的完成数据库查询之后的操作
 * 
 * 具体分成3个步骤： 1 生成一个Pair，指定dao查询的具体的方法，第2个参数为查询的方法的参数 Pair<DbOperation, Long>
 * pair = new Pair<DbOperation, Long>(DbOperation.select_jiazu_by_id, 1000L); 2
 * 生成一个CallbackJiazu的类，覆写execute方法，此方法在执行完查询之后自动执行 3
 * 调用JiazuManager的update方法，此方法的第一个参数为上面的创建的callBack,第2个参数为pair
 * manager.update(new CallbackJiazu(null, null), pair);
 * 
 * 
 * 
 */
public abstract class CallbackJiazu extends java.util.Observable {
	protected Message message;
	protected Player player;
	private long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

	public CallbackJiazu(Message message, Player player) {
		this.message = message;
		this.player = player;
	}

	abstract public void execute(Object res);
}
