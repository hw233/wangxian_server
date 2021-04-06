package com.fy.engineserver.activity.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fy.engineserver.activity.extraquiz.RewardArticles;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.ANSWER_QUIZ_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.POP_QUIZ_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;


public class Quiz {

	
	private long id = 1l;
	/**
	 * 用于判断是否接着进行上次未完成的答题活动
	 */
	private long endTime;
	
	private QuizState qs;

	private List<Subject> subjectList = new ArrayList<Subject>();
	
	private int nextNum = 0;
	
	private String beginTime = "";
	/**
	 * 所有人排名 只有最后一个题才得到
	 */
	public transient List<Entry<Long, AnswerRecord>> lastTopList = new ArrayList<Entry<Long, AnswerRecord>>();
	
	/**
	 * 前20排名list
	 */
	private transient List<AnswerRecord> topList = new ArrayList<AnswerRecord>();
	
	/**
	 * 使用帮助
	 */
	private transient List<Player> useHelp = new ArrayList<Player>();
	
	/**
	 * 使用放大镜
	 */
	private transient List<Player> useAmplifier = new ArrayList<Player>();
	
	/**
	 * 某一个题的答题记录
	 */

	private  transient Map<Byte, List<Long>> oneQuizRecord = Collections.synchronizedMap(new HashMap<Byte, List<Long>>());
	
	/**
	 * 答题记录   long = playerId
	 */

	private transient Map<Long, AnswerRecord> quizRecord = Collections.synchronizedMap(new LinkedHashMap<Long, AnswerRecord>());
	
	/**
	 * 同意答题的人
	 */
	public transient List<Long> agreeQuizPlayers = new ArrayList<Long>();
	
	
	/**
	 * 不同意答题的人
	 */
	public transient List<Long> disAgreeQuizPlayers = new ArrayList<Long>();
	
	/**
	 * 正式开始答题时间
	 */
	private long runningTime;
	/**
	 * 上一个题时间
	 */
	private transient long lastUpdateTime = -1;
	/** 本次答题的题目数量 */
	private int quizNum;
	/** 额外给予的物品奖励信息 */
	private RewardArticles extraRewards;
	/** 获得额外奖励需要答对题目的数量 */
	private int extraNeedScore;
	
	
	public void heartBeat(){
		long now = SystemTime.currentTimeMillis();
		
		if(this.getQs() == QuizState.运行){
			if(lastUpdateTime == -1){
				lastUpdateTime = now;
			}
			if(now - lastUpdateTime >= QuizManager.ANSWER_INTERVAL){

				//结算一题
				settleOne();
				lastUpdateTime = now;
				setQs(QuizState.答题后);
				
			}
		}else if(this.getQs() == QuizState.答题后){
			
			if(lastUpdateTime == -1){
				lastUpdateTime = now;
			}
			if(nextNum >= QuizManager.getInstance().getSubjectNum()){
//			if(nextNum >= QuizManager.SUBJECTNUM){
				setQs(QuizState.完成);
			}else{
				if(now - lastUpdateTime >= QuizManager.WAIT_CHECK_INTERVAL){
					
					lastUpdateTime = now;
				
					this.setQs(QuizState.运行);
					// 去下一个题返回客户端
					if(subjectList != null){
						Subject subject = subjectList.get(nextNum);
						returnSubject(subject);
//						++nextNum;
					}
				}
			}
		}
		
	}
	
	/**
	 * 返回在线玩家每个题
	 * @param subject
	 */
	public void returnSubject(Subject subject){
		POP_QUIZ_RES res = null;
		
		synchronized (QuizManager.getInstance().syn) {
			int length = agreeQuizPlayers.size();
			long id = -1l;
			for(int i= 0;i<length;i++){
	//		for(long id : agreeQuizPlayers){
				try {
					id = agreeQuizPlayers.get(i);
				} catch (Exception e) {
					QuizManager.logger.error("[答题返回玩家题异常]",e);
				}
				
				if(QuizManager.logger.isDebugEnabled()){
					QuizManager.logger.debug("[弹出题] [id:"+id+"]");
				}
				if(id < 0){
					continue;
				}
				Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
				for(Player player : ps){
					if(player.getId() == id){
						//返回给在线玩家每个题
						
						AnswerRecord ar = quizRecord.get(id);
						if(ar == null){
							ar = new AnswerRecord();
							quizRecord.put(id, ar);
						}
//						res = new POP_QUIZ_RES(GameMessageFactory.nextSequnceNum(),nextNum, subject, QuizManager.ANSWER_INTERVAL_second,QuizManager.SUBJECTNUM-nextNum,ar.getHelpNum(),ar.getAmplifierNum());
						res = new POP_QUIZ_RES(GameMessageFactory.nextSequnceNum(),nextNum, subject, QuizManager.ANSWER_INTERVAL_second,QuizManager.getInstance().getSubjectNum()-nextNum,ar.getHelpNum(),ar.getAmplifierNum());
						
						player.addMessageToRightBag(res);
						if(QuizManager.logger.isWarnEnabled()){
							QuizManager.logger.warn("[给玩家弹出题] ["+player.getLogString()+"]");
						}
						break;
					}
				}
	//		}
			}
		}
	}
	
	
	//下一题,答题记录，
	private void settleOne(){
		
		try{
		if(this.useAmplifier != null&& this.useAmplifier.size() > 0){
			
			//判断哪个答案最多
			Map<Byte, List<Long>> map = getOneQuizRecord();
			byte key = 0;
			int num = 0;
			for(Entry<Byte, List<Long>> en : map.entrySet()){
				if(en.getValue().size() >= num){
					num = en.getValue().size();
					key = en.getKey();
				}
			}
			Map<Byte, List<Long>> oneQuizMap = getOneQuizRecord();
			List<Long> Playerlist = oneQuizMap.get(key);
			
			for(Player player : this.useAmplifier){
				
				if(player != null){
					if(Playerlist != null){
						if(!Playerlist.contains(player.getId())){
							Playerlist.add(player.getId());
						}else{
							if(QuizManager.logger.isWarnEnabled())
								QuizManager.logger.warn("[重复答题] ["+player.getLogString()+"] []");
						}
					}else{
						
						Playerlist = new ArrayList<Long>();
						Playerlist.add(player.getId());
						oneQuizMap.put(key,Playerlist);
					}
					
					AnswerRecord ard = getQuizRecord().get(player.getId());
					if(ard != null){
						ard.setLastResult(key);
					}
				}else{
					QuizManager.logger.error("[答题使用放大镜异常] [player null]");
				}
			}
			this.useAmplifier.clear();
		}
		
		
		
		if(this.useHelp != null && this.useHelp.size() > 0){
			if(topList != null && topList.size() > 0){
				AnswerRecord ar = topList.get(0);
				if(ar.getLastResult() != -1){
					byte result = (byte)ar.getLastResult();
					for(Player player : this.useHelp){
						Map<Byte, List<Long>> oneQuizMap = getOneQuizRecord();
						List<Long> Playerlist = oneQuizMap.get(result);
						if(Playerlist != null){
							if(player == null){
								continue;
							}
							if(!Playerlist.contains(player.getId())){
								Playerlist.add(player.getId());
							}else{
								if(QuizManager.logger.isWarnEnabled())
									QuizManager.logger.warn("[重复答题] ["+player.getLogString()+"] []");
							}
						}else{
							Playerlist = new ArrayList<Long>();
							Playerlist.add(player.getId());
							oneQuizMap.put(result,Playerlist);
						}
						
						Map<Long, AnswerRecord> map = getQuizRecord();
						if(map.get(player.getId()) != null){
							map.get(player.getId()).addTempRecord(result);
						}else{
							AnswerRecord arr = new AnswerRecord();
							arr.setPlayerId(player.getId());
							arr.addTempRecord(result);
							map.put(player.getId(), arr);
						}
					}
				}else{
					for(Player player : this.useHelp){
						player.send_HINT_REQ(Translate.第一名没有答题);
					}
					if(QuizManager.logger.isWarnEnabled()){
						QuizManager.logger.warn("[本题使用求助失败] [第一名没有答题] ["+nextNum+"]");
					}
				}
			}
			this.useHelp.clear();
		}
		
		Map<Long, AnswerRecord> map = this.quizRecord;
		Player p = null;
		PlayerManager pm = PlayerManager.getInstance();
		Set<Entry<Long, AnswerRecord>> set = map.entrySet();
		
		Set<Entry<Long, AnswerRecord>> setCopy = new HashSet<Entry<Long,AnswerRecord>>();
		try{
			setCopy.addAll(set);
		}catch(Exception e){
			QuizManager.logger.error("[结算时复制本题答题记录]",e);
			setCopy.addAll(set);
		}
		for(Entry<Long, AnswerRecord> entry : setCopy){
			Subject subject;
			try {
				p = pm.getPlayer(entry.getKey());
				subject = subjectList.get(nextNum);
				entry.getValue().settle(subject,p);
			} catch (Exception e) {
				QuizManager.logger.error("[答题结算时错误]",e);
//				return ;
			}
		}
		
		this.topList.clear();
		//排序
		List<Entry<Long, AnswerRecord>> arr = this.sort(map);
		int i = 0;
		for(Entry<Long, AnswerRecord> entry : arr){
			if(i >= QuizManager.TOP20){
				break;
			}
			topList.add(entry.getValue());
			++i;
		}
		
		//最后一个题   得到总体排名
//		if(nextNum >= QuizManager.SUBJECTNUM -1){
		if(nextNum >= QuizManager.getInstance().getSubjectNum() -1){
			lastTopList = arr;
		}
		
		int tempSize = this.getTopList().size();
		
		long[] ids = new long[tempSize];
		String[] names = new String[tempSize];
		int[] scores = new int[tempSize];
		int temp = 0;
		for(AnswerRecord ar :topList){
			
			ids[temp] = ar.getPlayerId();
			try {
				names[temp] = PlayerManager.getInstance().getPlayer(ar.getPlayerId()).getName();
				if(QuizManager.logger.isDebugEnabled()){
					QuizManager.logger.debug("[可以查出玩家名] ["+names[temp]+"] []");
				}
			} catch (Exception e) {
				names[temp] = "";
				QuizManager.logger.error("[查出不出玩家名] ["+names[temp]+"] []",e);
			}
			scores[temp] = ar.getScore();
			temp++;
		}
		
		Subject sub = this.getSubjectList().get(nextNum);
		
		AnswerRecord ar = null;
		
//		PlayerManager pm = PlayerManager.getInstance();
//		Player answerPlayer = null;
		if(sub != null){
			Player player = null;
			List<Long> rightPlayerList = new ArrayList<Long>();
			for(Entry<Byte, List<Long>> en : this.oneQuizRecord.entrySet()){
				if(en.getKey() == sub.getRightAnswer()){
					rightPlayerList = en.getValue();
					break;
				}
			}
			synchronized (QuizManager.getInstance().syn) {
				for(long id : agreeQuizPlayers){
					
					try {
						if(pm.isOnline(id)){
						player = pm.getPlayer(id);
						if(player.isOnline()){
							
							ar =this.quizRecord.get(id);
							if(ar == null){
								ar = new AnswerRecord();
								quizRecord.put(id, ar);
							}
							if(rightPlayerList.contains(player.getId())){
								int rank = 0;
								for(Entry<Long, AnswerRecord> ent :arr){
									++rank;
									if(ent.getKey() == player.getId()){
										break;
									}
								}
								ANSWER_QUIZ_RES res1 = new ANSWER_QUIZ_RES(GameMessageFactory.nextSequnceNum(), true, (byte)sub.getRightAnswer(),
										ids,names,scores,QuizManager.WAIT_CHECK_INTERVAL_second,ar.getScore(),rank,ar.getCulture(),player.getCulture());
								if(QuizManager.logger.isWarnEnabled()){
									QuizManager.logger.warn("[答错正确] ["+player.getLogString()+"] [玩家名:"+names[0]+"] [玩家个数:"+names.length+"] [得到的文采值:"+ar.getCulture()+"] [累积的文采值:"+player.getCulture()+"]");
								}
								player.addMessageToRightBag(res1);
							}else{
								int rank = 0;
								for(Entry<Long, AnswerRecord> ent :arr){
									++rank;
									if(ent.getKey() == player.getId()){
										break;
									}
								}
								ANSWER_QUIZ_RES res1 = new ANSWER_QUIZ_RES(GameMessageFactory.nextSequnceNum(), false, (byte)sub.getRightAnswer(),
										ids,names,scores,QuizManager.WAIT_CHECK_INTERVAL_second,ar.getScore(),rank,ar.getCulture(),player.getCulture());
								if(QuizManager.logger.isWarnEnabled()){
									QuizManager.logger.warn("[答错错误] ["+player.getLogString()+"] [玩家名:"+names[0]+"] [玩家个数:"+names.length+"] [得到的文采值:"+ar.getCulture()+"] [累积的文采值:"+player.getCulture()+"]");
								}
								player.addMessageToRightBag(res1);
							}
						}
						}
					} catch (Exception e) {
						QuizManager.logger.error("[答题返回正确错误答案]",e);
					}
					
				}
			}
			
		}else{
			if(QuizManager.logger.isWarnEnabled())
				QuizManager.logger.warn("[回答问题结算错误] [没有指定的题] ["+nextNum+"]");
		}
		}catch(Exception e){
			QuizManager.logger.error("[答题结算一题]",e);
		}
		++nextNum;
		this.oneQuizRecord.clear();
		QuizManager.logger.warn("[结算一题] ["+nextNum+"]");
		
	}
	

   private List<Entry<Long, AnswerRecord>> sort(Map<Long, AnswerRecord> map) {
       Set<Entry<Long, AnswerRecord>> set = map.entrySet();
       List<Entry<Long, AnswerRecord>> list = new ArrayList<Entry<Long, AnswerRecord>>();
       for(Entry<Long, AnswerRecord> e : set){
    	   list.add(e);
       }
       Collections.sort(list, QuizManager.comparator);
       return list;
   }  

	
	
	
	
/***************************************************************************************/
	public QuizState getQs() {
		return qs;
	}
	public void setQs(QuizState qs) {
		this.qs = qs;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public List<Subject> getSubjectList() {
		return subjectList;
	}
	public void setSubjectList(List<Subject> subjectList) {
		this.subjectList = subjectList;
	}
	public int getNextNum() {
		return nextNum;
	}
	public void setNextNum(int nextNum) {
		this.nextNum = nextNum;
	}
	public Map<Byte, List<Long>> getOneQuizRecord() {
		return oneQuizRecord;
	}
	public void setOneQuizRecord(Map<Byte, List<Long>> oneQuizRecord) {
		this.oneQuizRecord = oneQuizRecord;
	}
	public Map<Long, AnswerRecord> getQuizRecord() {
		return quizRecord;
	}
	public void setQuizRecord(Map<Long, AnswerRecord> quizRecord) {
		this.quizRecord = quizRecord;
	}

	public List<Long> getAgreeQuizPlayers() {
		return agreeQuizPlayers;
	}

	public void setAgreeQuizPlayers(List<Long> agreeQuizPlayers) {
		this.agreeQuizPlayers = agreeQuizPlayers;
	}

	public long getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(long runningTime) {
		this.runningTime = runningTime;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public List<AnswerRecord> getTopList() {
		return topList;
	}

	public void setTopList(List<AnswerRecord> topList) {
		this.topList = topList;
	}

	public List<Player> getUseHelp() {
		return useHelp;
	}

	public void setUseHelp(List<Player> useHelp) {
		this.useHelp = useHelp;
	}

	public List<Player> getUseAmplifier() {
		return useAmplifier;
	}

	public void setUseAmplifier(List<Player> useAmplifier) {
		this.useAmplifier = useAmplifier;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public int getQuizNum() {
		return quizNum;
	}

	public void setQuizNum(int quizNum) {
		this.quizNum = quizNum;
	}

	public RewardArticles getExtraRewards() {
		return extraRewards;
	}

	public void setExtraRewards(RewardArticles extraRewards) {
		this.extraRewards = extraRewards;
	}

	public int getExtraNeedScore() {
		return extraNeedScore;
	}

	public void setExtraNeedScore(int extraNeedScore) {
		this.extraNeedScore = extraNeedScore;
	}
}
