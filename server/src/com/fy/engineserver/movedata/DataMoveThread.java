package com.fy.engineserver.movedata;

import java.lang.reflect.Method;

import com.fy.engineserver.uniteserver.UnitedServerManager2;

public class DataMoveThread implements Runnable{

	private String methodName;

	private Class<?>[] parameterTypes;

	private Object[] args;

	private boolean running = true;

	private long startTime;
	private long endTime;
	
	public DataMoveThread(String methodName, Class<?>[] parameterTypes, Object[] args) {
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.args = args;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			startTime = System.currentTimeMillis();
			DataMoveManager.logger.warn("[调用方法] [" + methodName + "] [开始]");
			Method method = DataMoveManager.class.getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			Object result = method.invoke(DataMoveManager.instance, args);
			DataMoveManager.logger.warn("[调用方法] [" + methodName + "] [结束] [result:" + result + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			running = false;
			endTime = System.currentTimeMillis();
		} catch (Exception e) {
			DataMoveManager.logger.warn("[调用方法] [" + methodName + "] [异常]", e);
		}
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

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
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

	
	
}
