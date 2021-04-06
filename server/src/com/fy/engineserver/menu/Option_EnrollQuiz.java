/**
 * 
 */
package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.quiz.Quiz;
import com.fy.engineserver.quiz.QuizManager;
import com.fy.engineserver.sprite.Player;

/**
 * @author Administrator
 *
 */
public class Option_EnrollQuiz extends Option {

	/**
	 * 竞猜名字
	 */
	String quizName;
	
	public Option_EnrollQuiz() {
		
	}
	
	/**
	 * 
	 */
	public Option_EnrollQuiz(String quizName) {
		// TODO Auto-generated constructor stub
		this.quizName=quizName;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		Quiz q=QuizManager.getInstance().getQuizByName(this.quizName);
		String info="";
		if(q!=null){
			if(q.isEnrollTime()){
				if(player.getLevel() >= q.getLevelLimit()){
					if(q.isParticipant(player.getId())){
						info=Translate.text_5213;
					}else{
						q.enroll(player);
						info=Translate.text_1807;
					}
				}else{
					info=Translate.text_4739+q.getLevelLimit()+Translate.text_5214;
				}
			}else{
				info=Translate.text_5215;
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
		return OptionConstants.SERVER_FUNCTION_ENROLL_QUIZ;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4874;
	}

	public String getQuizName() {
		return quizName;
	}

	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

}
