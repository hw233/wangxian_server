/**
 * 
 */
package com.fy.engineserver.menu;

import java.util.Date;

import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.quiz.AnswerRecord;
import com.fy.engineserver.quiz.Quiz;
import com.fy.engineserver.quiz.QuizManager;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.stat.model.PlayerActionFlow;

/**
 * @author Administrator
 *
 */
public class Option_QuizRewards extends Option {
	
	/**
	 * 奖励的种类 0游戏币 1经验
	 */
	byte rewardsType;
	
	/**
	 * 竞猜名字
	 */
	String quizName;

	/**
	 * 
	 */
	public Option_QuizRewards(String quizName, byte rewardsType) {
		// TODO Auto-generated constructor stub
		this.quizName=quizName;
		this.rewardsType=rewardsType;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		Quiz q=QuizManager.getInstance().getQuizByName(this.quizName);
		String info="";
		if(q!=null){
			if(q.isParticipant(player.getId())){
				AnswerRecord ar = q.getAnswerRecord(player.getId());
				long rewardValue=0;
				if(this.rewardsType==0){
					if(ar!=null){
						rewardValue=ar.getRewardMoney();
					}
					try {
//						BillingCenter.getInstance().playerSaving(player, rewardValue, CurrencyType.GAME_MONEY, SavingReasonType.QUIZ_REWARD, null);
						info=Translate.text_5382+rewardValue+Translate.text_5383;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else if(this.rewardsType==1){
					if(ar!=null){
						rewardValue=ar.getRewardExp();
					}
					player.addExp((int)rewardValue, ExperienceManager.ADDEXP_REASON_QUIZ);
					info=Translate.text_5382+rewardValue+Translate.text_5384;
				}
				q.removeParticipant(player.getId());
				Player.sendPlayerAction(player, PlayerActionFlow.行为类型_答题,PlayerActionFlow.行为名称_答题_领奖 , 0, new Date(), true);
			}else{
				info=Translate.text_5385;
			}
		}else{
			info=this.quizName+Translate.text_4976;
		}
		
		HINT_REQ req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,info);
		player.addMessageToRightBag(req);
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_QUIZ_REWARDS;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_5386;
	}

}
