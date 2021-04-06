package com.sqage.stat.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class EncodeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			
			String ss=URLEncoder.encode("龙脉探秘", "UTF-8");
			System.out.println(ss);
			
			System.out.println(URLDecoder.decode(ss, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
