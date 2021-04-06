package com.xuanzhi.tools.timer;

public interface Executable {
	/**
	 * 这个执行方法应该不消耗任何时间，最好是起一个线程
	 *实现该接口的类均可 通过 Scheduler.java类调用,只需要在 
	 *schedule.conf 文件内写入 类名 和对应参数即可
	 *
	 */
	public void execute(String args[]);
}
