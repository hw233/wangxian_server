package com.fy.boss.platform.yijie;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuickSDKUtil {
	

	private final static Pattern pattern = Pattern.compile("\\d+");

	private final static String charset="utf-8";

	public static String encode(String src,String key) { 
		try {
			byte[] data = src.getBytes(charset);
			byte[] keys = key.getBytes();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < data.length; i++) {
				int n = (0xff & data[i]) + (0xff & keys[i % keys.length]);
				sb.append("@" + n);
			}
			return sb.toString();
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return src;
	}

	public static String decode(String src,String key) {
		if(src == null || src.length() == 0){
			return src;
		}
		Matcher m = pattern.matcher(src);
		List<Integer> list = new ArrayList<Integer>();
		while (m.find()) {
			try {
				String group = m.group();
				list.add(Integer.valueOf(group));
			} catch (Exception e) {
				e.printStackTrace();
				return src;
			}
		}

		if (list.size() > 0) {
			try {
				byte[] data = new byte[list.size()];
				byte[] keys = key.getBytes();

				for (int i = 0; i < data.length; i++) {
					data[i] = (byte) (list.get(i) - (0xff & keys[i % keys.length]));
				}
				return new String(data, charset);
			} catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}
			return src;
		} else {
			return src;
		}
	}
}
