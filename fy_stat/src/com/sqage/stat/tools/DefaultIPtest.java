package com.sqage.stat.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.xuanzhi.tools.text.FileUtils;

import junit.framework.TestCase;  

public class DefaultIPtest extends TestCase {  
      
    public void testIp() throws IOException{  
                //指定纯真数据库的文件名，所在文件夹  
        IPSeeker ipseeker=new IPSeeker("qqwry.Dat","D:/workspace/sqage/stat_common_mieshi/src/com/sqage/stat/tools");  
         //测试IP 58.20.43.13  
        
        
        String name="allip";
        String srcFilename="D:\\"+name+".txt";
        String targetFilename="D:\\"+name+"_result.txt";
        System.out.println("srcFilename:"+srcFilename+"    targetFilename:"+targetFilename);
       
      	BufferedReader bufferedreader=new BufferedReader(new InputStreamReader(new  FileInputStream(srcFilename),"UTF-8"));
      	 FileUtils.chkFolder(targetFilename);
      	  BufferedWriter wrout = new BufferedWriter(new FileWriter(targetFilename));

      	  String instring=null;
      	 int  i=1;
      	 long now=System.currentTimeMillis();
      	  while((instring=bufferedreader.readLine())!=null){
      	       
      	            String param= instring.replaceAll("\t","").replaceAll("\r","").trim();
      	            
      	            //String ip = "120.52.92.3";  
      	            String ip=param;
      	            String address = "";  
      	            try {  
      	             address = ipseeker.getIPLocation(ip).getCountry()+":"+ipseeker.getIPLocation(ip).getArea();  
      	           i++;
      	            } catch (Exception e) {  
      	             e.printStackTrace();  
      	            }  
      	            if(address!=null){
      	            	wrout.write(ip+"\t"+address+"\n");
      	            //System.out.println(ip+"\t"+address+"\n"); 
      	            } else
      	            {
      	          	 // System.out.println(ip+"\t"+"无结果"+"\n");
      	          	wrout.write(ip+"\t"+"无结果"+"\n");
      	            }
      	            if(i%20000==0)
      	            { 
      	            	System.out.println("查询到第"+i+"条,用时"+(System.currentTimeMillis()-now)+"ms");
      	            	}
      	            
      	  }
      	wrout.flush();
      	wrout.close();
      	System.out.println("查询到第"+i+"条,用时"+(System.currentTimeMillis()-now)+"ms");
      
  
        
        //System.out.println(ipseeker.getIPLocation("36.40.113.135").getCountry()+":"+ipseeker.getIPLocation("58.20.43.13").getArea());  
   
    }  
}  
