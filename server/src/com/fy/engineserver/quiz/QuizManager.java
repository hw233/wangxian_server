/**
 * 
 */
package com.fy.engineserver.quiz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.text.XmlUtil;
import com.xuanzhi.tools.watchdog.ConfigFileChangedAdapter;
import com.xuanzhi.tools.watchdog.ConfigFileChangedListener;

/**
 * @author Administrator
 *
 */
public class QuizManager implements Runnable ,ConfigFileChangedListener{
	
	static QuizManager self;
	
	ArrayList<Quiz> quiz;
	
	File quizConfig;
	
	public static Logger log=Game.logger;
	
	final String[] QUIZ_NAMES={Translate.text_5604};
	
	/**
	 * 报名信息存储
	 */
	File enrollData;
	
	ArrayList<Long> enrollPlayerId=new ArrayList<Long>();
	
	/**
	 * 
	 */
	public QuizManager() {
		// TODO Auto-generated constructor stub
	}
	
	public void init() throws Exception{
		long t=com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		QuizManager.self=this;
		this.quiz=new ArrayList<Quiz>();
		Thread th=new Thread(this);
		th.setName(Translate.text_5605);
		th.start();
		this.readConfig(this.quizConfig);
		this.readEnrollData();
		this.clearEnrollData();
		ConfigFileChangedAdapter.getInstance().addListener(this.quizConfig,this);
		System.out.println("[系统初始化] [问答活动管理器] [初始化完成] ["+this.getClass()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-t)+"]");
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		while(true){
			try{
				Thread.sleep(1000);
				
				for(Quiz ph:this.quiz){
					ph.heartbeat();
					
					if(ph instanceof QuizActivity){
						QuizActivity qa=(QuizActivity)ph;
						if(qa.isNeedSave){
							this.saveEnrollData(qa);
							qa.isNeedSave=false;
						}
						
						synchronized (this.enrollPlayerId) {
							if(qa.isEnrollTime()&&this.enrollPlayerId.size()>0){
								PlayerManager pm=PlayerManager.getInstance();
								for(long id:this.enrollPlayerId){
									if(pm!=null){
										Player p=null;
										try{
											p=pm.getPlayer(id);
										}catch (Exception e) {
											// TODO: handle exception
										}
										if(p!=null){
											qa.enroll(p);
										}
									}
								}
								this.enrollPlayerId.clear();
							}
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				log.error("[问答活动] [线程错误] [错误："+e+"]",e);
			}
			
		}
	}
	
	public static QuizManager getInstance(){
		return self;
	}
	
	public void addActivities(Quiz q){
		this.quiz.add(q);
		if(log.isInfoEnabled()){
//			log.info("[问答活动] [加载活动] [成功] [活动名称："+q.getName()+"]");
			if(log.isInfoEnabled())
				log.info("[问答活动] [加载活动] [成功] [活动名称：{}]", new Object[]{q.getName()});
		}
	}
	
	public Quiz getQuizByName(String name){
		for(Quiz q:this.quiz){
			if(q.getName().equals(name)){
				return q;
			}
		}
		return null;
	}

	public File getQuizConfig() {
		return quizConfig;
	}

	public void setQuizConfig(File quizConfig) {
		this.quizConfig = quizConfig;
	}
	
	private void readConfig(File file) throws Exception{
		ArrayList<Quiz> temp=new ArrayList<Quiz>();
		Document doc = null;
		doc = XmlUtil.load(file.toString());
		Element root = doc.getDocumentElement();
		Element[] quizies = XmlUtil.getChildrenByName(root, "quiz");
		this.enrollPlayerId.clear();
		for(int i=0;i<quizies.length;i++){
			String name=XmlUtil.getAttributeAsString(quizies[i], "name", TransferLanguage.getMap());
			int enrollStartHour=XmlUtil.getAttributeAsInteger(quizies[i], "enrollStartHour");
			int enrollStartMinute=XmlUtil.getAttributeAsInteger(quizies[i], "enrollStartMinute");
			int enrollEndHour=XmlUtil.getAttributeAsInteger(quizies[i], "enrollEndHour");
			int enrollEndMinute=XmlUtil.getAttributeAsInteger(quizies[i], "enrollEndMinute");
			int answerStartHour=XmlUtil.getAttributeAsInteger(quizies[i], "answerStartHour");
			int answerStartMinute=XmlUtil.getAttributeAsInteger(quizies[i], "answerStartMinute");
			int questionsNum=XmlUtil.getAttributeAsInteger(quizies[i], "questionNum");
			long questionInterval=XmlUtil.getAttributeAsLong(quizies[i], "questionInterval");
			int basePoints=XmlUtil.getAttributeAsInteger(quizies[i], "basePoints");
			String rewards=XmlUtil.getAttributeAsString(quizies[i], "rewards", "", TransferLanguage.getMap());
			int levelLimit=XmlUtil.getAttributeAsInteger(quizies[i], "levelLimit");
			if(name.equals(this.QUIZ_NAMES[i])){
				switch(i){
				case 0:
					QuizActivity qa=new QuizActivity(name);
					qa.setEnrollStartHour(enrollStartHour);
					qa.setEnrollStartMinute(enrollStartMinute);
					qa.setEnrollEndHour(enrollEndHour);
					qa.setEnrollEndMinute(enrollEndMinute);
					qa.setAnswerStartHour(answerStartHour);
					qa.setAnswerStartMinute(answerStartMinute);
					qa.setQuestionsNum(questionsNum);
					qa.setQuestionInterval(questionInterval);
					qa.setBasePoints(basePoints);
					qa.setSpecialRewards(rewards.split(":"));
					qa.setLevelLimit(levelLimit);
					
					temp.add(qa);
					break;
				}
			}else{
//				log.error("[问答活动] [初始化] [错误] [没有这个问答活动] [活动名称："+name+"]");
				log.error("[问答活动] [初始化] [错误] [没有这个问答活动] [活动名称：{}]", new Object[]{name});
				throw new Exception("没有这个问答活动："+name);
			}
		}
		this.quiz.clear();
		for(Quiz q:temp){
			this.addActivities(q);
		}
		if(log.isInfoEnabled()){
//			log.info("[问答活动] [初始化] [成功] [数量："+this.quiz.size()+"]");
			if(log.isInfoEnabled())
				log.info("[问答活动] [初始化] [成功] [数量：{}]", new Object[]{this.quiz.size()});
		}
	}

	public void fileChanged(File file) {
		// TODO Auto-generated method stub
		try {
			this.readConfig(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("[问答活动] [初始化] [错误："+e+"]",e);
		}
	}

	public File getEnrollData() {
		return enrollData;
	}

	public void setEnrollData(File enrollData) {
		this.enrollData = enrollData;
	}
	
	private void saveEnrollData(QuizActivity qa){
		FileOutputStream output=null;
		try{
			StringBuffer sb=new StringBuffer();
			sb.append("<?xml version='1.0' encoding='gbk'?>\n");
			sb.append("<enrollData time='"+new Date()+"'>\n");
			AnswerRecord[] ars = qa.participants.values().toArray(new AnswerRecord[0]);
			for(AnswerRecord ar:ars){
				sb.append("<id playerId='"+ar.getPlayerId()+"'/>\n");
			}
			sb.append("</enrollData>");
			output=new FileOutputStream(this.enrollData);
//			if(TransferLanguage.characterTransferormFlag){
				output.write(sb.toString().getBytes("utf-8"));
//			}else{
//				output.write(sb.toString().getBytes("gbk"));
//			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			log.error("[问答活动] [存储报名信息] [错误："+e+"]",e);
		}finally{
			if(output!=null){
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				output=null;
			}
		}
	}
	
	private void readEnrollData(){
		try{
			synchronized (this.enrollPlayerId) {
				Document doc = null;
//				if(TransferLanguage.characterTransferormFlag){
					InputStream is = new FileInputStream(enrollData);
					doc=XmlUtil.load(is,"utf-8");
//				}else{
//					doc=XmlUtil.load(this.enrollData.toString());
//				}
				Element root=doc.getDocumentElement();
				Element[] ids = XmlUtil.getChildrenByName(root, "id");
				for(Element id:ids){
					long playerId=XmlUtil.getAttributeAsLong(id, "playerId");
					this.enrollPlayerId.add(playerId);
				}
				log.error("[问答活动] [读取报名信息] [成功]");
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("[问答活动] [读取报名信息] [错误："+e+"]",e);
		}finally{
			
		}
	}
	
	private void clearEnrollData(){
		FileOutputStream output=null;
		try{
			StringBuffer sb=new StringBuffer();
			sb.append("<?xml version='1.0' encoding='gbk'?>\n");
			sb.append("<enrollData time='"+new Date()+"'>\n");
			
			sb.append("</enrollData>");
			output=new FileOutputStream(this.enrollData);
			output.write(sb.toString().getBytes("gbk"));
			
		}catch(Exception e){
			e.printStackTrace();
			log.error("[问答活动] [清空报名信息] [错误："+e+"]",e);
		}finally{
			if(output!=null){
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				output=null;
			}
		}
	}

}
