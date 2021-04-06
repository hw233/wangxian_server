package com.fy.engineserver.uniteserver;

import java.lang.reflect.Method;

public class UnitedServerThread2 implements Runnable {

	/** 要执行的方法名字 */
	private String methodName;

	private Class<?>[] parameterTypes;

	private Object[] args;

	private boolean running = true;

	private long startTime;
	private long endTime;

	public UnitedServerThread2(String methodName, Class<?>[] parameterTypes, Object[] args) {
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.args = args;
	}

	@Override
	public void run() {
		UnitedServerManager2 instance = UnitedServerManager2.getInstance();
		try {
			startTime = System.currentTimeMillis();
			UnitedServerManager2.logger.warn("[调用方法] [" + methodName + "] [开始]");
			Method method = UnitedServerManager2.class.getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			Object result = method.invoke(instance, args);
			UnitedServerManager2.logger.warn("[调用方法] [" + methodName + "] [结束] [result:" + result + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			running = false;
			endTime = System.currentTimeMillis();
		} catch (Exception e) {
			UnitedServerManager2.logger.warn("[调用方法] [" + methodName + "] [异常]", e);
		}
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
}
