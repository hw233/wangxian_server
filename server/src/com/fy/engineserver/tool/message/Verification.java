package com.fy.engineserver.tool.message;


public class Verification	{

	int a = 0x67452301;
	int b = 0xEFCDAB89;
	int c = 0x98BADCFE;
	int d = 0x10325476;

	private int add(int x, int y) {
		return ((x&0x7FFFFFFF) + (y&0x7FFFFFFF)) ^ (x&0x80000000) ^ (y&0x80000000);
	}

	private int rol(int num, int cnt) {
		return (num << cnt) | (num >>> (32 - cnt));
	}

	private int cmn(int q, int a, int b, int x, int s, int t) {
		return add(rol(add(add(a, q), add(x, t)), s), b);
	}

	private int ff(int a, int b, int c, int d, int x, int s, int t) {
		return cmn((b & c) | ((~b) & d), a, b, x, s, t);
	}

	private int gg(int a, int b, int c, int d, int x, int s, int t)  {
		return cmn((b & d) | (c & (~d)), a, b, x, s, t);
	}

	private int hh(int a, int b, int c, int d, int x, int s, int t) {
		return cmn(b ^ c ^ d, a, b, x, s, t);
	}

	private int ii(int a, int b, int c, int d, int x, int s, int t) {
		return cmn(c ^ (b | (~d)), a, b, x, s, t);
	}

	public void update(byte bytes[],int offset,int len) {
		int n = offset + len;
		for(int i = offset; i < n; i ++){
			int olda = a;
			int oldb = b;
			int oldc = c;
			int oldd = d;

			a = ff(a, b, c, d, bytes[i], 7 , 0xD76AA478);
			d = gg(a, b, c, d, bytes[i], 5 , 0xF61E2562);
			b = hh(a, b, c, d, bytes[i], 4 , 0xFFFA3942);
			c = ii(a, b, c, d, bytes[i], 6 , 0xF4292244);
			a = add(a, olda);
			b = add(b, oldb);
			c = add(c, oldc);
			d = add(d, oldd);
		}
	}

	public byte[] digest(){
		byte bytes[] = new byte[16];
		bytes[0] = (byte)(a>>24);
		bytes[1] = (byte)(c>>18);
		bytes[2] = (byte)(b>>8);
		bytes[3] = (byte)(d);
		bytes[4] = (byte)(b>>24);
		bytes[5] = (byte)(a>>18);
		bytes[6] = (byte)(d>>8);
		bytes[7] = (byte)(b);
		bytes[8] = (byte)(c>>24);
		bytes[9] = (byte)(b>>18);
		bytes[10] = (byte)(a>>8);
		bytes[11] = (byte)(a);
		bytes[12] = (byte)(d>>24);
		bytes[13] = (byte)(d>>18);
		bytes[14] = (byte)(c>>8);
		bytes[15] = (byte)(c);
		a = 0x67452301;
		b = 0xEFCDAB89;
		c = 0x98BADCFE;
		d = 0x10325476;
		return bytes;
	}
	public static boolean equals(byte[] bytes1,byte[] bytes2){
		if(bytes1 == bytes2) return true;
		if(bytes1.length != bytes2.length) return false;
		for(int i = 0 ; i < bytes1.length ; i++){
			if(bytes1[i] != bytes2[i]) return false;
		}
		return true;
	}
}
