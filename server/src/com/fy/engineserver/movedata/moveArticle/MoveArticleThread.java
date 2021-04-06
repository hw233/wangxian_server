package com.fy.engineserver.movedata.moveArticle;

import java.lang.reflect.Method;

public class MoveArticleThread implements Runnable{
	
	private String methodName;

	private Class<?>[] parameterTypes;

	private Object[] args;
	
	private long startTime;
	
	public boolean running = true;

	public MoveArticleThread(String methodName, Class<?>[] parameterTypes, Object[] args) {
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.args = args;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			startTime = System.currentTimeMillis();
			MoveArticleManager.logger.warn("[调用方法] [" + methodName + "] [开始]");
			Method method = MoveArticleManager.class.getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			Object result = method.invoke(MoveArticleManager.inst, args);
			MoveArticleManager.logger.warn("[调用方法] [" + methodName + "] [结束] [result:" + result + "] [cost:" + (System.currentTimeMillis() - startTime) + "ms]");
			running = false;
		} catch (Exception e) {
			MoveArticleManager.logger.warn("[调用方法] [" + methodName + "] [异常]", e);
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
	
	

}
