package com.sqage;

public class Mathmethod {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long [] y=new long[] {6237,6594,5956,5874,6003,6092,18162,6552,6141,5933};
		
		long [] x=new long[] {3823,2465,2218,2156,2419,2324,2387,1971,1652,1641};
		
		Mathmethod mm=new Mathmethod();
		mm.sub(x, y,10);
		
		
	}
	
	
	 public long[][] sub(long [] x,long [] y,int n){
		long[][] z=new long[n][n];
		for(int i=0;i<n;i++)
		{
			for(int j=i;j<n;j++){
				
			long a=0;
			long b=0;
			for(int k=i;k<=j-1;k++){a+=z[i][k];}
			for(int k=0;k<=i-1;k++){b+=z[k][j];}
			z[i][j]=Math.max(0,Math.min(x[i]-a, y[j]-b));
			
			}
		}
		
		
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++){
				long a = 0;
				for(int k = 0 ; k <= j ; k++){
					a+=z[i][k];
				}
				if(i<=j)
					System.out.print((x[i]- a)+"  ");
				else
					System.out.print("    ");
			}
			System.out.println("");
		}
		return z;
	}
	
}
