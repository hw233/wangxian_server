package com.sqage.stat.test;

public class MiMaCeShi {
	 //加密
	  public String jiaMi(String s,String key){
	    String str = "";
	    int ch;
	    if(key.length() == 0){
	        return s;
	    }
	    else if(!s.equals(null)){
	        for(int i = 0,j = 0;i < s.length();i++,j++){
	          if(j > key.length() - 1){
	            j = j % key.length();
	          }
	          ch = s.codePointAt(i) + key.codePointAt(j);
	          if(ch > 65535){
	            ch = ch % 65535;//ch - 33 = (ch - 33) % 95 ;
	          }
	          str += (char)ch;
	        }
	    }
	    return str;

	  } 
	  //解密
	  public String jieMi(String s,String key){
	    String str = "";
	    int ch;
	    if(key.length() == 0){
	        return s;
	    }
	    else if(!s.equals(key)){
	        for(int i = 0,j = 0;i < s.length();i++,j++){
	          if(j > key.length() - 1){
	            j = j % key.length();
	          }
	          ch = (s.codePointAt(i) + 65535 - key.codePointAt(j));
	          if(ch > 65535){
	            ch = ch % 65535;//ch - 33 = (ch - 33) % 95 ;
	          }
	          str += (char)ch;
	        }
	    }
	    return str;
	  }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MiMaCeShi MiMaCeShi=new MiMaCeShi();
		
		String ss="1509原来如此，这就是原文";
		String key="留好榜样给子孙后代";
		System.out.println("原文："+ss);
		System.out.println("    key："+key);
		
		String miwen=MiMaCeShi.jiaMi(ss,key);
		System.out.println("加密后的密文："+miwen);
		
		String yuanma=MiMaCeShi.jieMi(miwen,key);
		System.out.println("解压后的原文："+yuanma);

	}

}
