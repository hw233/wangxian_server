package com.fy.engineserver.activity.activeness;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;

public class ActivenessConfig {
	public static ActivenessConfig instance;
	
	public ActivenessConfig(){
		instance = this;
		//
		if(PlatformManager.getInstance() == null){
		}else if(PlatformManager.getInstance().platformOf(Platform.腾讯)){
			ActivenessManager.open = false;
			ActivitySubSystem.logger.warn("Activeness close for {}",Platform.腾讯);
		}else if(PlatformManager.getInstance().platformOf(Platform.韩国)){
			ActivenessManager.open = false;
			ActivitySubSystem.logger.warn("Activeness close for {}",Platform.韩国);
		}else if(PlatformManager.getInstance().platformOf(Platform.台湾)){
			ActivenessManager.open = false;
			ActivitySubSystem.logger.warn("Activeness close for {}",Platform.台湾);
		}
	}
}
