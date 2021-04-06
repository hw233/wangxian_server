package com.xuanzhi.tools.text;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 * @since 1.0
 */
public class Ucs2Encoder
{
	private static Ucs2Encoder def;
	private HashMap map1;
	private HashMap map2;
	
	private Ucs2Encoder()
	{
		Properties props = new Properties();
		map1 = new HashMap(); 
		map2 = new HashMap();
		try
		{
			InputStream in = null;
			String filename = System.getProperty("gb2ucs.map.filename");
			if(filename != null)
				in = new FileInputStream(filename);
			else{
				URL url = getClass().getClassLoader().getResource("com/xuanzhi/tools/text/gb2ucs.map");
				if(url == null)
					url = getClass().getClassLoader().getResource("gb2ucs.map");
				if(url != null)
					in = url.openStream();
			}
			if(in == null){
				throw new Exception("can't find resource gb2ucs.map");
			}
			props.load(in);
			in.close ();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Iterator it = props.entrySet().iterator() ;
		while(it.hasNext())
		{
			Map.Entry me = (Map.Entry)it.next();
			Integer in = Integer.decode((String)me.getKey());
			Integer out = Integer.decode((String)me.getValue());
			map1.put(in,out);
			map2.put(out,in);
		}
		
		System.err.println("Ucs2Encoder ... ... size of map is " + map1.size()); 
	}
	
	
	
	public static Ucs2Encoder getInstance()
	{
		if( def != null) return def;
		synchronized(Ucs2Encoder.class)
		{
			if( def != null) return def;
			def = new Ucs2Encoder();
			return def;
		}
	}
	
	public short encoding(byte b)
	{
		return (short)b;
	}
	
	public short encoding(byte b1,byte b2)
	{
		int b_1 = (int)(b1 & 0x000000ff);
		int b_2 = (int)(b2 & 0x000000ff);
		int i = (b_1 << 8) | b_2;
		Integer in = new Integer(i);
		
		Integer out= (Integer)map1.get(in);
		
		if( out != null)
		{
			return (short)(out.intValue() & 0xffff); 
		}
		else{
			return (short)(in.intValue() & 0xffff); 
		}
	}
	
	public byte[] decoding(byte b1,byte b2)
	{
		int b_1 = (int)(b1 & 0x000000ff);
		int b_2 = (int)(b2 & 0x000000ff);
		int i = (b_1 << 8) | b_2;
		Integer in = new Integer(i);
		
		Integer out= (Integer)map2.get(in);
		
		if( out != null)
		{
			short sh = (short)(out.intValue() & 0xffff); 
			return new byte[]{(byte)((sh >> 8)&0x00ff),(byte)(sh & 0x00ff)};
		}else{
			return new byte[]{b1,b2};
		}
	}
	
	
}
