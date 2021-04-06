package com.xuanzhi.tools.text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取中文字符串的拼音
 * 需要提供汉字拼音的字典文件,common_tools/conf/chinese.txt
 * @author
 *
 */
public class ChineseUtil {
	public static HashMap<String, String> map = null;

	public String dicFile;
	
	public static ChineseUtil self;
	
	public static ChineseUtil getInstance() {
		return self;
	}
	
	public void setDicFile(String dicFile) {
		this.dicFile = dicFile;
	}

	public void initialize() {
		long now = System.currentTimeMillis();
		map = new HashMap<String, String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new BufferedReader(
					new InputStreamReader(new FileInputStream(dicFile))));
			String str = null;
			while ((str = reader.readLine()) != null) {
				String arr[] = str.split("\\s{1,}");
				if (arr.length == 2) {
					map.put(arr[0], arr[1]);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		self = this;
		System.out.println(this.getClass().getName() + " initialize successfully ["+map.size()+"] ["+(System.currentTimeMillis()-now)+"]");
	}

	/** 
	 * 获取全拼
	 * @param str
	 * @return
	 */
	public String getFullPin(String str) {
		StringBuffer buf = new StringBuffer();
		str = str.replaceAll("[\\r\\n]{1,}", "\r\n");
		for (char p : str.toCharArray()) {
			if (Character.isDigit(p) || Character.isLowerCase(p)
					|| Character.isUpperCase(p) || Character.isSpaceChar(p)) {
				buf.append(p);
				continue;
			} else {
				boolean find = false;
				for (Map.Entry<String, String> entry : map.entrySet()) {
					if (entry.getValue().indexOf(p) != -1) {
						buf.append(entry.getKey().toLowerCase());
						find = true;
						break;
					}

				}
				if (find == false) {
					System.out.println("error,can not find map\t" + p);
					buf.append(p);
				}
			}
		}
		return buf.toString();
	}

	/**
	 * 获取首字母
	 * @param str
	 * @return
	 */
	public String getFirstPin(String str) {
		str = str.replaceAll("[\\r\\n]{1,}", "\r\n");
		StringBuffer buf = new StringBuffer();
		for (char p : str.toCharArray()) {
			if (Character.isDigit(p) || Character.isLowerCase(p)
					|| Character.isUpperCase(p) || Character.isSpaceChar(p)) {
				buf.append(p);
				continue;
			} else {
				boolean find = false;
				for (Map.Entry<String, String> entry : map.entrySet()) {
					if (entry.getValue().indexOf(p) != -1) {
						buf.append(entry.getKey().toLowerCase().charAt(0));
						find = true;
						break;
					}
				}
				if (find == false) {
					buf.append(p);
				}
			}
		}
		return buf.toString();
	}

	/**
	 * 获取开始字母
	 * @param str
	 * @return
	 */
	public String getStartPinWord(String str) {
		str = str.substring(0,1);
		return getFirstPin(str);
	}

	public static void main(String args[]) {
		ChineseUtil util = new ChineseUtil();
		util.setDicFile("d:/gamepj/common_tools/conf/chinese.txt");
		util.initialize();
		long now = System.currentTimeMillis();
		String s = util.getStartPinWord("大傻逼");
		System.out.println(s);
	}

}
