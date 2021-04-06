package com.xuanzhi.tools.text;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;

import com.fasterxml.jackson.core.type.TypeReference;


import junit.framework.TestCase;

public class JsonTestCase extends TestCase{

	public void testA() throws Exception{
		String haha = "haha";
		String s = JsonUtil.jsonFromObject(haha);
		System.out.println("==================java.lang.String====================");
		System.out.println(s);
		
		String hahas[] = new String[]{"你好","我们","他们","哈哈哈\"哈哈"};
		s = JsonUtil.jsonFromObject(hahas);
		System.out.println("==================write java.lang.String[] to json====================");
		System.out.println(s);
		Class<String[]> cl = String[].class;
		String[] xxxx = JsonUtil.objectFromJson(s, cl);
		System.out.println("==================read java.lang.String[] from json====================");
		for(int i = 0 ; i < xxxx.length  ;i++){
			System.out.println("xxxx["+i+"]=" + xxxx[i]);
		}
		
		byte bytes[] = new byte[1024];
		for(int i = 0 ; i < bytes.length ; i++){
			bytes[i] = (byte)i;
		}
		s = JsonUtil.jsonFromObject(bytes);
		System.out.println("==================write byte[] to json====================");
		System.out.println("length="+s.length());
		System.out.println(s);
		Class<byte[]> cl2 = byte[].class;
		byte[] xxxx2 = JsonUtil.objectFromJson(s, cl2);
		System.out.println("==================read byte[] from json====================");
		for(int i = 0 ; i < xxxx2.length  ;i++){
			System.out.print(xxxx2[i]+",");
		}
		
		long ids[] = new long[128];
		for(int i = 0 ; i < ids.length ; i++){
			ids[i] =  1000000000000000L + i;
		}
		s = JsonUtil.jsonFromObject(ids);
		System.out.println("==================write long[] to json====================");
		System.out.println("length="+s.length());
		System.out.println(s);
		Class<long[]> cl3 = long[].class;
		long[] xxxx3 = JsonUtil.objectFromJson(s, cl3);
		System.out.println("==================read long[] from json====================");
		for(int i = 0 ; i < xxxx3.length  ;i++){
			System.out.print(xxxx3[i]+",");
		}
		Field field = TEST_A.class.getDeclaredField("ids");
		System.out.println("type = " + field.getGenericType());
		xxxx3 = (long[])JsonUtil.objectFromJson(s, field.getGenericType());
		System.out.println("==================read long[] from json====================");
		for(int i = 0 ; i < xxxx3.length  ;i++){
			System.out.print(xxxx3[i]+",");
		}
		System.out.println();
		
		
		byte[][] bytess = new byte[102][16];
		for(int i = 0 ; i < bytess.length ; i++){
			for(int j = 0 ; j < bytess[i].length ; j++){
				bytess[i][j] = (byte)(i+j);
			}
		}
		s = JsonUtil.jsonFromObject(bytess);
		System.out.println("==================write byte[][] to json====================");
		System.out.println("length="+s.length());
		System.out.println(s);
		Class<byte[][]> cl4 = byte[][].class;
		byte[][] xxxx4 = JsonUtil.objectFromJson(s, cl4);
		System.out.println("==================read byte[][] from json====================");
		for(int i = 0 ; i < xxxx4.length  ;i++){
			for(int j = 0 ; j < xxxx4[i].length ; j++){
				System.out.print(xxxx4[i][j]+",");
			}
		}
		
		HashMap<String,Long> map1 = new HashMap<String,Long>();
		for(int i = 0 ; i < 100 ; i ++){
			map1.put("index-"+i, 1L*i);
		}
		s = JsonUtil.jsonFromObject(map1);
		System.out.println("==================write HashMap<String,Long> to json====================");
		System.out.println("length="+s.length());
		System.out.println(s);
		
		HashMap<String,Long> xxxx5 = JsonUtil.objectFromJson(s, new TypeReference<HashMap<String,Long>>(){});
		System.out.println("==================read HashMap<String,Long> from json====================");
		Iterator<String> it = xxxx5.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			Long l = xxxx5.get(key);
			System.out.print("{"+key+","+l+"},");
		}
		System.out.println("");
		
		field = TEST_A.class.getDeclaredField("map1");
		System.out.println("type = " + field.getType());
		System.out.println("type = " + field.getGenericType());
		xxxx5 = (HashMap<String,Long>)JsonUtil.objectFromJson(s, field.getGenericType());
		System.out.println("==================read HashMap<String,Long> from json====================");
		it = xxxx5.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			Long l = xxxx5.get(key);
			System.out.print("{"+key+","+l+"},");
		}
		
		
		
	}
	
	public static class TEST_A{
		HashMap<String,Long> map1 = new HashMap<String,Long>();
		long ids[];
	}
}
