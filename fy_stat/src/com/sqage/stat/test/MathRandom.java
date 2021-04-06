package com.sqage.stat.test;

public class MathRandom {

	/**
	 * @param args
	 */
	public static long kb = 1024;
	
		public static void main(String[] args){
			
			String[] moneystep={"249-91","220-89","249-92","251-97","200-87","160-90","160-91","191-90","170-98"};
			for(int j=0;j<moneystep.length;j++)
			{
			String[] moneys= moneystep[j].split("-");	
			int data= Integer.valueOf(moneys[0]);
			int num= Integer.valueOf(moneys[1]);
			System.out.println("");
			System.out.println("原始数据:"+data+" 数量："+num+"个");
			for(int i=0;i<num;i++)
			{
				double ref= Math.random()*0.8;
				if(i%2==0){	ref=ref*-1;	}
				double result=(1+ref)*data;
				int dd= (int)result/10 *10;
				System.out.print(dd+",");
			}
			
			}
		}
		}
