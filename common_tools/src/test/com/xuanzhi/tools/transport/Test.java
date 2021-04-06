package com.xuanzhi.tools.transport;

import java.util.Comparator;

import com.xuanzhi.tools.ds.AvlTree;
import com.xuanzhi.tools.ds.AvlTree.TreeNode;

import junit.framework.*;
import java.net.*;
import java.io.*;

public class Test{

	public static void main(String args[]) throws Exception{
		
		String host="112.25.14.10";
		int port = 5678;
		
		
		final Socket socket = new Socket(host,port);
		Thread t = new Thread(new Runnable(){
			public void run(){
				byte bytes[] = new byte[1024];
				while(true){
					try{
						int n = socket.getInputStream().read(bytes);
						if(n < 0){
							throw new IOException("socket close by peer!");
						}
						System.out.println("接受到数据："+toHex(bytes,0,n));
					}catch(IOException e){
						e.printStackTrace();
						break;
					}
				}
			}
		});
		t.start();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			String s = reader.readLine();
			s.trim();
			if(s.length() > 0){
				if(s.startsWith("exit")){
					System.exit(1);
				}else if(s.startsWith("test")){
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(output);
					out.writeInt(0);
					out.writeInt(0x00000FA3);
					out.writeInt(155);

					out.writeByte(1);
					out.writeInt(549538988);
					out.writeByte(0);
					
					byte content[] = output.toByteArray();
					System.arraycopy(numberToByteArray(content.length,4), 0, content, 0, 4);
					
					send(socket,content);
					
				}else{
					System.out.println("无效指令："+s);
					//send(socket,s);
				}
			}
		}
		
		
	}
	
	public static void send(Socket s,byte message[]) throws Exception{
		s.getOutputStream().write(message);
		s.getOutputStream().flush();
		System.out.println("发送数据给服务器：" + toHex(message,0,message.length));
	}
	

	public static void send(Socket s,String message) throws Exception{
		s.getOutputStream().write(message.getBytes());
		s.getOutputStream().flush();
		System.out.println("发送数据给服务器：" + message);
	}
	public static final String toHex(byte hash[],int offset,int len) {
		StringBuffer buf = new StringBuffer(hash.length * 2);
		int i;

		for (i = offset; i < hash.length && i < len; i++) {
			if (((int) hash[offset+i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) hash[offset+i] & 0xff, 16));
		}
		return buf.toString();
	}
	
	public static byte[] numberToByteArray(int n, int numOfBytes) {
		byte bytes[] = new byte[numOfBytes];
		for(int i = 0 ; i < numOfBytes ; i++)
			bytes[i] = (byte)((n >> (8*(numOfBytes - 1 - i))) & 0xFF);
		return bytes;
	}
}
