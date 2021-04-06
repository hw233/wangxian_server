package com.fy.gamegateway.mieshi.waigua;

import com.xuanzhi.tools.text.StringUtil;

public class ClientIDManager {

	public static String CLIENTID_MIYAO = "sqage99wx";
	public static int[] CLIENT_MD5_INDEXS = new int[]{1,9,8,4,0,7,1,7,1,5};
	
	public static boolean isRightClientID (String clientID) {
		if (clientID == null || clientID.length() != 20) {
			return false;
		}
		String md5Temp = clientID.substring(0, 10) + CLIENTID_MIYAO;
		String MD5 = StringUtil.hash(md5Temp);
		
		String md5_ascii = "";
		for (int i = 0 ; i < 10; i++) {
			char a = MD5.charAt(CLIENT_MD5_INDEXS[i]);
			int ascii = (int)a;
			int need = ascii % 10;
			md5_ascii = md5_ascii + need;
		}
		String serverCliendID = clientID.substring(0, 10) + md5_ascii;
		if (clientID.equals(serverCliendID)) {
			return true;
		}
		return false;
	}
}
