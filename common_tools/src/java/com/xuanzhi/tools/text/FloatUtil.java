package com.xuanzhi.tools.text;

import java.util.Random;


/**
 * 
 *
 */
public class FloatUtil {

	// convert float to half_float
	static short[] basetable = new short[512];
	static byte[] shifttable = new byte[512];
	
	// convert half_float to float
	static int[] mantissatable = new int[2048];
	static int[] exponenttable = new int[64];
	static int[] offsettable = new int[64];
	
	static int convertmantissa(int i){
		int m=i<<13; // Zero pad mantissa bits
		int e=0; // Zero exponent 
		while((m&0x00800000) != 0){ // While not normalized
			e-=0x00800000; // Decrement exponent (1<<23)
			m<<=1; // Shift mantissa
		}
		 m&=~0x00800000; // Clear leading 1 bit
		 e+=0x38800000; // Adjust bias ((127-14)<<23)
		return m | e; // Return combined number
	}

	static{
		///////////////////////////////////////////////////////////////////////////
			 int i;
			 int e;
			 for(i=0; i<256; ++i){
				 e=i-127;
				 if(e<-24){ // Very small numbers map to zero
					 basetable[i|0x000]=0x0000;
					 basetable[i|0x100]=(short)0x8000;
					 shifttable[i|0x000]=24;
					 shifttable[i|0x100]=24;
				 }
				 else if(e<-14){ // Small numbers map to denorms
					 basetable[i|0x000]=(short)(0x0400>>(-e-14));
					 basetable[i|0x100]=(short)((0x0400>>(-e-14)) | 0x8000);
					 shifttable[i|0x000]=(byte)(-e-1);
					 shifttable[i|0x100]=(byte)(-e-1);
				 }
				 else if(e<=15){ // Normal numbers just lose precision
					 basetable[i|0x000]=(short)((e+15)<<10);
					 basetable[i|0x100]=(short)(((e+15)<<10) | 0x8000);
					 shifttable[i|0x000]=13;
					 shifttable[i|0x100]=13;
				 }
				 else if(e<128){ // Large numbers map to Infinity
					 basetable[i|0x000]=0x7C00;
					 basetable[i|0x100]=(short)0xFC00;
					 shifttable[i|0x000]=24;
					 shifttable[i|0x100]=24;
				 }
				 else{ // Infinity and NaN's stay Infinity and NaN's
					 basetable[i|0x000]=0x7C00;
					 basetable[i|0x100]=(short)0xFC00;
					 shifttable[i|0x000]=13;
					 shifttable[i|0x100]=13;
				 }
			 }
			 
		/////////////////////////////////////////////////////////////////
			 mantissatable[0] = 0;
			 for(i=1;i<1024;++i){
				 mantissatable[i] = convertmantissa(i);
			 }
			 for(i=1024;i<2048;++i){
				 mantissatable[i] = 0x38000000 + ((i-1024)<<13);
			 }
			 //
			 exponenttable[0] = 0;
			 exponenttable[32]= 0x80000000;
			 for(i=1;i<31;++i){
				 exponenttable[i] = i<<23;// for i = 1..30
			 }
			 for(i=33;i<63;++i){
				 exponenttable[i] = 0x80000000 + (i-32)<<23;// for i = 33..62
			 }
			 exponenttable[31]= 0x47800000;
			 exponenttable[63]= 0xC7800000;
			 //
			 offsettable[0] = 0;
			 offsettable[32]= 0;
			 for(i=1;i<32;i++){
				 offsettable[i] = 1024;
			 }
			 for(i=33;i<64;i++){
				 offsettable[i] = 1024;
			 }
	}
	
	/**
	 * convert float to half_float, and return the half_float in 2 bytes.
	 * @param a
	 * @return
	 */
	public static int float2halffloat(float a){
		int f = Float.floatToIntBits(a);
		return basetable[(f>>23)&0x1ff]+((f&0x007fffff)>>shifttable[(f>>23)&0x1ff]);
	}
	
	/**
	 * convert half_float to float, and the half_float in 2 bytes.
	 * and return the float. 
	 * @param h
	 * @return
	 */
	public static float halffloat2float(int h){
		int f = mantissatable[offsettable[h>>10]+(h&0x3ff)]+exponenttable[h>>10];
		return Float.intBitsToFloat(f);
	}
	
	/**
	 * convert float array to half_float array, and return the half_float array.
	 * 
	 * @param a
	 * @return
	 */
	public static short[] float2halffloat(float a[]){
		short hh[] = new short[a.length];
		int f;
		for(int i = 0 ; i < a.length ; ++i){
			f  = Float.floatToIntBits(a[i]);
			hh[i] = (short)(basetable[(f>>23)&0x1ff]+((f&0x007fffff)>>shifttable[(f>>23)&0x1ff]));
		}
		return hh;
		
	}
	
	/**
	 * convert float array to half_float array, and return the half_float array.
	 * the float array is store in intbitArray.such as read from network.
	 * 
	 * @param floatToIntBits the float array in intbitArray.
	 * @return
	 */
	public static short[] float2halffloat(int floatToIntBits[]){
		short hh[] = new short[floatToIntBits.length];
		int f;
		for(int i = 0 ; i < floatToIntBits.length ; ++i){
			f  = floatToIntBits[i];
			hh[i] = (short)(basetable[(f>>23)&0x1ff]+((f&0x007fffff)>>shifttable[(f>>23)&0x1ff]));
		}
		return hh;
		
	}
	
	
	/**
	 * convert half_float array to float array, and return the float array.
	 * 
	 * @param hh the half_float array 
	 * @return the float array.
	 */
	public static float[] halffloat2float(short hh[]){
		float ff[] = new float[hh.length];
		int h;
		int f;
		for(int i = 0 ; i < hh.length ; ++i){
			h = hh[i];
			f = mantissatable[offsettable[h>>10]+(h&0x3ff)]+exponenttable[h>>10];
			ff[i] = Float.intBitsToFloat(f);
		}
		return ff;
	}
	
	
	/**
	 * convert half_float array to float array, and return the float intBitsArray.
	 * the float array is store in intbitArray.such as ready write to network.
	 * 
	 * @param hh the half_float array 
	 * @return the float array in intbitArray.
	 */
	public static int[] halffloat2floatintbits(short hh[]){
		int ff[] = new int[hh.length];
		int h;
		int f;
		for(int i = 0 ; i < hh.length ; ++i){
			h = hh[i];
			f = mantissatable[offsettable[h>>10]+(h&0x3ff)]+exponenttable[h>>10];
			ff[i] = f;
		}
		return ff;
	}
	
	public static void main(String args[]){
		Random r = new Random(System.currentTimeMillis());
		
		int n = 1000000;
		float ff[] = new float[n];
		int hh[] = new int[n];
		
		long now = System.currentTimeMillis();
		
		
		for(int i = 0 ; i < n ; i++){
			ff[i] = r.nextFloat();
		}
		
		long now1 = System.currentTimeMillis();
		
		
		
		for(int i = 0 ; i < n ; i++){
			hh[i] = float2halffloat(ff[i]);
		}
		
		long now2 = System.currentTimeMillis();
		
		for(int i = 0 ; i < n ; i++){
			long l = System.currentTimeMillis();
			System.nanoTime();
		}
		
		long now3 = System.currentTimeMillis();
		
		System.out.println("Float to HalfFloat: generate ["+n+"] floats cost "+(now1 - now)+" ms, convert cost "+(now2-now1)+" ms. avg cost "+((now2-now1)*1000000.0/n)+" ns And currentTimeMillis cost "+((now3-now2)*1000000.0/n)+" ns");
		
		float a = 0;
		int h = float2halffloat(a);
		float b = halffloat2float(h);
		
		System.out.println("[Convert] {"+a+"} -> [0x"+Integer.toHexString(h).toUpperCase()+"] -> {"+b+"}, 誤差："+((b-a))+"");
		n = 100;
		for(int i = 0 ; i < n ; i++){
			a = r.nextFloat()* 10;
			
			h = float2halffloat(a);
			
			b = halffloat2float(h);
			
			//System.out.println("[Convert] {"+a+"} -> [0x"+Integer.toHexString(h).toUpperCase()+"] -> {"+b+"}, 誤差："+((b-a))+"");
		}
		
		
	}
}
