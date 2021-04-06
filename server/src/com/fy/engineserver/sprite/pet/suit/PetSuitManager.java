package com.fy.engineserver.sprite.pet.suit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fy.engineserver.sprite.pet.PetManager;
/**
 * 宠物 套装
 * 
 * @date on create 2016年8月26日 下午3:17:51
 */
public class PetSuitManager {

	private static PetSuitManager inst;
	
	public Map<String, String> translate = new HashMap<String, String>();//temptranslate
	
	public void init() {
		inst = this;
	}
	
	public static PetSuitManager getInst() {
		return inst;
	}
	
	public String getTranslate(String key, Object ...obj) {
		try {
			String str = translate.get(key);
			if (str == null) {
				return key;
			}
			if (obj.length <= 0) {
				return str;
			}
			return String.format(str,obj);
		} catch (Exception e) {
			if (PetManager.logger.isDebugEnabled()) {
				PetManager.logger.debug("[获取翻译] [异常] [key:" + key + "] [" + Arrays.toString(obj) + "]", e);
			}
		}
		return key;
	}
}
