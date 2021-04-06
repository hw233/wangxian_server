/**
 * 
 */
package com.fy.engineserver.menu;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.question.Question;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.quiz.AnswerRecord;
import com.fy.engineserver.quiz.Quiz;
import com.fy.engineserver.quiz.QuizManager;
import com.fy.engineserver.sprite.Player;

/**
 * @author Administrator
 *
 */
public class Option_AnswerQuiz extends Option {

	/**
	 * 答案选项
	 */
	int selectionIndex;
	
	/**
	 * 当前问题
	 */
	Question question;
	
	/**
	 * 竞猜名字
	 */
	String quizName;
	
	/**
	 * 
	 */
	public Option_AnswerQuiz(int selectionIndex,String quizName,Question question) {
		// TODO Auto-generated constructor stub
		this.selectionIndex=selectionIndex;
		this.quizName=quizName;
		this.question=question;
	}
	
	@Override
	public void doSelect(Game game, Player player) {
		// TODO Auto-generated method stub
		Quiz q=QuizManager.getInstance().getQuizByName(this.quizName);
		String info="";
		if(q!=null){
			if(q.isParticipant(player.getId())){
				AnswerRecord ar=q.getAnswerRecord(player.getId());
				if(ar!=null){
					if(ar.isAnswered(this.question.getId())){
						info=Translate.text_4971;
					}else{
						if(q.isAnswerTime()){
							if(q.answer(player.getId(), this.question, this.selectionIndex)){
								info=Translate.text_4972;
							}else{
								info=Translate.text_4973;
							}
						}else{
							info=Translate.text_4974;
						}
					}
				}else{
					info=Translate.text_4975;
				}
			}else{
				info=Translate.text_4975;
			}
		}else{
			info=this.quizName+Translate.text_4976;
		}
		HINT_REQ req=new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1,info);
		player.addMessageToRightBag(req);
	}
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public void setOType(byte type) {
		//oType = type;
	}

	public int getOId() {
		return OptionConstants.SERVER_FUNCTION_ANSWER_QUIZ;
	}

	public void setOId(int oid) {
	}
	
	public String toString(){
		return Translate.text_4872;
	}

}
