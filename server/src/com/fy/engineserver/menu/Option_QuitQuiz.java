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
public class Option_QuitQuiz extends Option {
	
	String quizName;

	/**
	 * 
	 */
	public Option_QuitQuiz(String quizName) {
		// TODO Auto-generated constructor stub
		this.quizName=quizName;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		Quiz q=QuizManager.getInstance().getQuizByName(this.quizName);
		String info="";
		if(q!=null){
			if(q.isParticipant(player.getId())){
				q.quit(player.getId());
				info=Translate.text_5380;
			}else{
				info=Translate.text_5381;
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
		return OptionConstants.SERVER_FUNCTION_QUIT_QUIZ;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4873;
	}

}
