package com.fy.engineserver.operating;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class SystemAnnouncementManager {
	static SystemAnnouncementManager instance;
	DefaultDiskCache ddc;
	String announcement;
	
	File dataFile;

	public static SystemAnnouncementManager getInstance(){
		return instance;
	}
	
	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

	private final static String KEY_ANNOUNCEMENT = "@#$%%^";
	public void init() throws Exception{
		ddc = new DefaultDiskCache(dataFile, null,
				Translate.text_5540, 1L * 365 * 24 * 3600 * 1000L);
		announcement = (String)ddc.get(KEY_ANNOUNCEMENT);
		instance = this;
	}
	
	public String getAnnouncement(){
		return announcement;
	}
	
	public void setAnnouncement(String announcement){
		this.announcement = announcement;
		ddc.put(KEY_ANNOUNCEMENT, announcement);
	}
	
	private boolean isAnnouncementAvailable(){
		return announcement != null;
	}
	
	public boolean isAnnouncementSentToUser(String userName){
		Set<String> set = (Set<String>)ddc.get(announcement);
		return set != null && set.contains(userName);
	}
	
	public void sendAnnouncementToPlayer(Player player){
		if(!isAnnouncementAvailable()){
			return;
		}
		
		if(isAnnouncementSentToUser(player.getUsername())){
			return;
		}
		
		MenuWindow mw = new MenuWindow();
		mw.setTitle(Translate.text_4951);
		mw.setDescriptionInUUB(announcement);
		mw.setWidth(1000);
		mw.setHeight(1000);
		
		Option_Cancel o = new Option_Cancel();
		o.setText(Translate.text_2901);
		o.setIconId("135");
		
		mw.setOptions(new Option[]{o});
		
		QUERY_WINDOW_RES re = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(),
				mw,mw.getOptions());
		player.addMessageToRightBag(re);
		
		HashSet<String> set = (HashSet<String>)ddc.get(announcement);
		if(set == null){
			set = new HashSet<String>();
		}
		
		set.add(player.getUsername());
		
		ddc.put(announcement, set);
	}
	
	public void clear(){
		ddc.clear();
	}
}
