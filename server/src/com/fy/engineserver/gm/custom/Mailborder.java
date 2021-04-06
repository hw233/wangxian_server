package com.fy.engineserver.gm.custom;

import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.PlayerManager;

public class Mailborder {
   
	public long getGMMailsize(){
		PlayerManager pmanager = PlayerManager.getInstance();
		MailManager mmanager = MailManager.getInstance();
		try {
			return mmanager.getCount((pmanager.getPlayer("GM01")).getId());
		} catch (Exception e) {
			return 0;
		}
		
	}
	
}
