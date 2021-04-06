package com.sqage.stat.test;

public class TT {

	/**
	 * @param args
	 */
	public static long kb = 1024;
	
		public static void main(String[] args){
			while(true){
				Runtime runtime = Runtime.getRuntime();
				 System.out.print("\r"+"freeMemory:"+(runtime.freeMemory())+" maxMemory:"+runtime.maxMemory()+" totalMemory:"+runtime.totalMemory());
				 try{
					 Thread.sleep(10000);
				 }catch(Exception ex){
					 
				 }
			}
		}
		}
