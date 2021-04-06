package com.sqage.stat.tools;

import java.util.ArrayList;
import java.util.Collections;
  
public class ListStrSort implements Comparable<ListStrSort>{  
  
    private String[] sts;
    private int orderNum;  //按数组的第orderNum个数据排序
    @Override  
    public int compareTo(ListStrSort o) {  
        return Integer.parseInt(sts[orderNum])-Integer.parseInt(o.sts[orderNum]);  
    }  
      
    ListStrSort(String[] sts,int orderNum)  
    {  
        this.sts=sts;  
        this.orderNum=orderNum;
    }  
      
    @Override  
    public String toString() {
    	String rs="";
    	for (int i =0;i<sts.length;i++)
    	{
    		rs+=sts[i]+" ";
    	}
        return rs;  
    }  
    public static void main(String []args)  
    {  
    	int order=5;
    	String[] str1 = new String[9];
    	str1[0]="123";
    	str1[1]="124";
    	str1[2]="23";
    	str1[3]="283";
    	str1[4]="83";
    	str1[5]="93";
    	str1[6]="12";
    	str1[7]="453";
    	str1[8]="623";
    	ListStrSort c1 = new ListStrSort(str1,order); 
        
        String[] str2 = new String[9];
    	str2[0]="321";
    	str2[1]="412";
    	str2[2]="203";
    	str2[3]="23";
    	str2[4]="73";
    	str2[5]="133";
    	str2[6]="72";
    	str2[7]="153";
    	str2[8]="823";
    	ListStrSort c2 = new ListStrSort(str2,order); 
        
        String[] str3 = new String[9];
    	str3[0]="111";
    	str3[1]="612";
    	str3[2]="563";
    	str3[3]="56";
    	str3[4]="73";
    	str3[5]="83";
    	str3[6]="762";
    	str3[7]="1053";
    	str3[8]="833";
    	ListStrSort c3 = new ListStrSort(str3,order);
        
        String[] str4 = new String[9];
        str4[0]="102";
    	str4[1]="212";
    	str4[2]="143";
    	str4[3]="656";
    	str4[4]="723";
    	str4[5]="823";
    	str4[6]="762";
    	str4[7]="193";
    	str4[8]="843";
    	ListStrSort c4 = new ListStrSort(str4,order);  
          
        ArrayList<ListStrSort> list = new ArrayList<ListStrSort>();  
        list.add(c1);  
        list.add(c2);  
        list.add(c3);  
        list.add(c4); 
        for (ListStrSort listStrSort : list) {  System.out.println(listStrSort);  }  
        
        System.out.println("...................................."); 
        
        Collections.sort(list);  
        for (ListStrSort listStrSort : list) {  System.out.println(listStrSort);  }  
          
    }  
}  