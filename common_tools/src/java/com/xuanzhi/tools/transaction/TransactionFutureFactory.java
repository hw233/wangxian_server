package com.xuanzhi.tools.transaction;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.transaction.impl.JDBCTransactionFuture;

public class TransactionFutureFactory implements Runnable {
	
	protected static Logger logger = Logger.getLogger(TransactionFutureFactory.class);
	
	protected static TransactionFutureFactory self;
	
	private List<TransactionFuture> transactions;
	
	private int totalRegistered;
	
	private int totalFinished;
	
	public static TransactionFutureFactory getInstance(){
		return self;
	}
	
	public void initialize() {
		long now = System.currentTimeMillis();
		transactions = Collections.synchronizedList(new ArrayList<TransactionFuture>());
		Thread t = new Thread(this, "TransactionFutureFactory");
		t.start();
		self = this;
		System.out.println("[系统初始化] [事务工厂] [初始化完成] ["+this.getClass().getName()+"] [耗时："+(System.currentTimeMillis()-now)+"毫秒]");
		System.out.println(this.getClass().getName() + " initialize successfully ["+(System.currentTimeMillis()-now)+"]");
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(5*1000);
				long now = System.currentTimeMillis();
				Iterator<TransactionFuture> ite = transactions.iterator();
				int size = transactions.size();
				int finished = 0;
				while(ite.hasNext()) {
					TransactionFuture trans = ite.next();
					if(!trans.isTransactionFinished() && (now-trans.getStartTime().getTime())/1000 > 10) {
						logger.info("[WARN] [timeout] ["+(now-trans.getStartTime().getTime())/1000+"secs] ["+trans.getTrace()+"]");
					} else {
						if(trans.isTransactionFinished()) {
							ite.remove();
							finished++;
							logger.info("[OK##] [finished] ["+trans.getTrace()+"]");
						}
					}
				}
				totalFinished += finished;
				logger.info("[check_transactions] ["+size+"] [finished:"+finished+"] [totalFinished/totalRegistered:"+totalFinished+"/"+totalRegistered+"]");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 注册
	 * @param f
	 */
	private void register(TransactionFuture f) {
		transactions.add(f);
		totalRegistered++;
		logger.info("[register] ["+f.isAccomplished()+"] ["+DateUtil.formatDate(f.getStartTime(),"yyMMdd_HH:mm:ss")+"] ["+f.getTrace()+"]");
	}
	
	/**
	 * 获取一个事物处理结果
	 * @param con
	 * @param success
	 * @param resultObject
	 * @param trace
	 * @return
	 */
	public TransactionFuture getTransactionFuture(Connection con, boolean success, Object resultObject, String trace) {
		JDBCTransactionFuture f = new JDBCTransactionFuture(con, success, resultObject, trace);
		register(f);
		return f;
	}
}
