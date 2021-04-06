package com.sqage.stat;

import java.util.Random;

public class Demo {

	public static void main (String args[]) {
        Random rnd = new Random();
        for(int i=0;i<10000;i++){
        int p = rnd.nextInt(15);
         System.out.print(p+"  ");
         if(i%200==0)
         {
        	 System.out.println("");
        	 
         }
        }
}
}