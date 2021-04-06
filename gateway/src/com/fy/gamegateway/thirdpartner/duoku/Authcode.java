package com.fy.gamegateway.thirdpartner.duoku;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;

public class Authcode {
	

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] btInput = s.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String Encode(String string, String key) throws UnsupportedEncodingException{
		Date date = new Date();
		
		int ckey_length = 4;
		key = MD5(key);
		String keya = MD5(key.substring(0, 16));
		String keyb = MD5(key.substring(16));
		String keyc =  MD5(String.valueOf(date.getTime()));
		
		keyc =keyc.substring(keyc.length()-ckey_length);
		
		
		String cryptkey = keya+MD5(keya+keyc);
		int key_length = cryptkey.length();
		

		string = String.format("%010d", 0)+MD5(string+keyb).substring(0, 16)+string;
		int string_length = string.length();
		
		
		StringBuilder result = new StringBuilder();
		ArrayList box = new ArrayList();
		for(int i=0; i<=255;i++){
			box.add(i,i);
		}
		
		ArrayList rndkey = new ArrayList();
		for(int i=0; i<=255;i++){
			char c = cryptkey.charAt(i%key_length);
			rndkey.add(i,(int)c);
		}
		
		for(int i=0,j=0; i<=255;i++){
			j = (j + (Integer)box.get(i)+ (Integer)rndkey.get(i))%256;
			int tmp = (Integer)box.get(i);
			box.set(i, box.get(j));
			box.set(j, tmp);
		}
		
		for(int a=0, j=0, i=0; i<string_length;i++){
			a = (a+1)%256;
			j = (j + (Integer)box.get(a))%256;
			int tmp = (Integer)box.get(a);
			box.set(a, box.get(j));
			box.set(j, tmp);
			result.append((char)((int)(string.charAt(i))^((Integer)box.get(((Integer)box.get(a)+(Integer)box.get(j))%256))));
		}
		
		String enuri = keyc+ Base64.encode(result.toString()).replace("-", "");
		
		return URLEncoder.encode(enuri, "utf-8");
		
	}

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) {

		try {
			System.out.println(Encode("2w3sewrw", "3e4r"));
			
		} catch (UnsupportedEncodingException e) {
			System.out.println("error");
		}
	}

}
