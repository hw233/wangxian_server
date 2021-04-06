package com.sqage.stat.test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;



public class ReadFile {
	public void read() throws IOException{
	FileReader   fr   =   new   FileReader("D:\\result.txt");//         文件名 
    BufferedReader   bufferedreader   =   new   BufferedReader(fr); 
    SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String instring;
    while((instring=bufferedreader.readLine())!=null){
    
    	System.out.println(instring);
        String sss=instring.replaceAll(" "," "); 
       
       String substr=sss;
      // int aa= substr.indexOf("jieyh");
        if(substr.length()!=0) 
        { 
        String[] str=substr.split(" ");
        String date=str[0].trim();
        System.out.println(substr);
       long tiem= Long.valueOf(date);
       
        System.out.println( "时间:"+sf.format(new Date(tiem)));
        System.out.println( "用户名:"+str[1]);
        System.out.println( "分区:"+str[2]);
        System.out.println( "级别:"+str[3]);
        System.out.println( "支付类型:"+str[4]);
        System.out.println( "支付卡类型:"+str[5]);
        System.out.println( "渠道:"+str[6]);
        System.out.println( "充值金额:"+str[7]);
        System.out.println( "cost金额:"+str[8]);
        System.out.println( "、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、");
     
        	
        System.out.println(instring); 
        } 
    } 
    fr.close(); 
	}
	public static void main(String[] args) {
		ReadFile readFile=new ReadFile();
		try {
			readFile.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
      System.out.print("dfsf");
	}

	
}
