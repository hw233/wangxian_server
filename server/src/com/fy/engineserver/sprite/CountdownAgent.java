package com.fy.engineserver.sprite;

//import org.apache.log4j.Logger;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_COUNTDOWN_REQ;

/**
 * 
 * 
 *
 */
public class CountdownAgent {

	public static Logger logger = LoggerFactory.getLogger(CountdownAgent.class);

	private Player owner;

	public static final byte COUNT_TYPE_VILLAGE = 0;
	public static final byte COUNT_TYPE_JIAZU_FEED = 1;
	public static final byte COUNT_TYPE_DUJIE = 2;
	public static final byte COUNT_TYPE_DISASTER = 3;
	public static final byte COUNT_TYPE_WOLF = 4;
	public static final byte COUNT_TYPE_DICE = 5;
	public static final byte COUNT_TYPE_CROSSPK = 6;
	
	public static final String[] COUNT_TYPE_DES = new String[]{Translate.矿战};
	
	public static String getCountdownDes(byte type){
		if(type >= 0 && type < COUNT_TYPE_DES.length){
			return COUNT_TYPE_DES[type];
		}
		return "";
	}
	public CountdownAgent(Player player) {
		this.owner = player;
	}

	public Hashtable<Byte,Long> countdownMap = new Hashtable<Byte,Long>();
	
	public void 通知玩家倒计时(){
		if(owner != null){
			if(countdownMap != null && !countdownMap.isEmpty()){
				try{
					long now = SystemTime.currentTimeMillis();
					for(Byte b : countdownMap.keySet()){
						Long i = countdownMap.get(b);
						if(i != null && i > 0){
							int leftTime = (int)((i-now)/1000);
							if(leftTime > 0){
								NOTICE_CLIENT_COUNTDOWN_REQ req = new NOTICE_CLIENT_COUNTDOWN_REQ(GameMessageFactory.nextSequnceNum(), b, leftTime, getCountdownDes(b));
								owner.addMessageToRightBag(req);
							}
						}
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}
}
