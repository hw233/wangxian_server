package com.xuanzhi.tools.cache.distribute.concrete.protocol;

import java.io.ByteArrayOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.DatagramPacket;
import java.util.Comparator;
import java.util.Enumeration;

import com.xuanzhi.tools.cache.distribute.concrete.MessageTransport;

import com.xuanzhi.tools.ds.Heap;
import org.apache.log4j.Logger;

/**
 * 用于发送消息和接受消息，对应的有两个队列，分别是发送队列和接受队列，
 * 这两个队列都是优先级队列，优先处理重要的消息
 *
 */
public class DefaultMessageTransport implements MessageTransport{

	protected Heap receiveQueue = null;
	protected Heap sendQueue = null;
	
	protected Object sendLock = new Object(){};
	protected Object receiveLock = new Object(){};
	
	protected Thread receiveThread = null;
	protected Thread sendThread = null;
	
	protected DatagramSocket broadcastSocket;
	protected DatagramSocket receiveSocket;
	protected DatagramSocket sendSocket;
	
	protected InetAddress broadcastAddress;
	protected int port;

	protected InetAddress localAddress;
	
	protected static Logger logger = Logger.getLogger(DefaultMessageTransport.class);
	
	public DefaultMessageTransport(InetAddress broadcastAddress,int port) throws SocketException{
		
		this.port = port;
		this.broadcastAddress = broadcastAddress;
		
		findLocalAddress();
		
		checkAddress();
		
		receiveSocket = new DatagramSocket(port);
		sendSocket = new DatagramSocket();
		
		broadcastSocket = new DatagramSocket();
		broadcastSocket.setBroadcast(true);
		broadcastSocket.connect(broadcastAddress,port);
		
		Comparator c = new Comparator(){

			public int compare(Object arg0, Object arg1) {
				Message m1 = (Message)arg0;
				Message m2 = (Message)arg1;
				if(m1.getPriority() > m2.getPriority())
					return -1;
				else if(m1.getPriority() < m2.getPriority())
					return 1;
				return 0;
			}};
			
		receiveQueue = new Heap(c);
		sendQueue = new Heap(c);
		
		sendThread = new SendThread();
		sendThread.start();
		
		receiveThread = new ReceiveThread();
		receiveThread.start();
	}
	
	private void checkAddress() throws SocketException 
	{
		byte abytes[] = this.broadcastAddress.getAddress();
		byte bbytes[] = this.localAddress.getAddress();
		byte cbytes[] = new byte[4];
		for(int i = 0 ; i < abytes.length ; i ++)
		{
			cbytes[i] = (byte)(abytes[i]|bbytes[i]);
		}
		
		for(int i = 0 ; i < abytes.length ; i ++)
		{
			if(cbytes[i] != abytes[i])
				throw new SocketException("broadcast address and local address not compatible");
		}
	}
	/**
	 * obtain local address
	 */
	protected void findLocalAddress() throws SocketException 
	{
		long startTime = System.currentTimeMillis();
		try
		{
			Enumeration enum2 = NetworkInterface.getNetworkInterfaces();
			while(enum2.hasMoreElements())
			{
				NetworkInterface ni = (NetworkInterface)enum2.nextElement();
				Enumeration en = ni.getInetAddresses();
				while(en.hasMoreElements())
				{
					InetAddress address = (InetAddress)en.nextElement();
					
					if( !address.isLoopbackAddress() && !address.isAnyLocalAddress() && address.isSiteLocalAddress()){
						int k = address.getHostAddress().indexOf(".");
						String p = address.getHostAddress().substring(0,k);
						if(this.broadcastAddress.getHostAddress().startsWith(p)){
							this.localAddress = address;
							logger.debug("[find-local-address] [ok] ["+localAddress+"] [-] -- [0] [0] ["+(System.currentTimeMillis() - startTime)+"ms]");

						}
					}
				}
			}
		}catch(SocketException e)
		{
			System.err.println("getLocalAddress error " + e.toString());
			throw e;
		}
	}

	public Message receive(long timeout) {
		long startTime = System.currentTimeMillis();
		if(timeout < 0) timeout = 0;
		long waitedTime = 0L;
		while(true){
			if(timeout > 0 && waitedTime >= timeout)
				break;
			
			long tt = System.currentTimeMillis();
			if(receiveQueue.size() == 0){
				synchronized(receiveLock){
					if(receiveQueue.size() == 0){
						try{
							if(timeout == 0)
								receiveLock.wait();
							else
								receiveLock.wait(timeout - waitedTime);
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}
				}
			}
			waitedTime += System.currentTimeMillis() - tt;
			if(timeout > 0 && waitedTime > timeout)
				break;
			
			tt = System.currentTimeMillis();
			if(receiveQueue.size() > 0){
				synchronized(receiveLock){
					if(receiveQueue.size() > 0){
						Message m = (Message)receiveQueue.extract();
						logger.debug("[get-message] [ok] ["+m.getName()+"] ["+timeout+"] -- ["+receiveQueue.size()+"] ["+sendQueue.size()+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
						return m;
					}
				}
			}
			waitedTime += System.currentTimeMillis() - tt;
		}
		
		logger.debug("[get-message] [timeout] [-] ["+timeout+"] -- ["+receiveQueue.size()+"] ["+sendQueue.size()+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
		return null;
	}

	public void send(Message message) {
		long startTime = System.currentTimeMillis();
		synchronized(sendQueue){
			sendQueue.insert(message);
		}
		
		synchronized(sendLock){
			sendLock.notifyAll();
		}
		
		logger.debug("[put-message] [ok] ["+message.getName()+"] [-] -- ["+receiveQueue.size()+"] ["+sendQueue.size()+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
	}
	
	protected class SendThread extends Thread{
		
		public void run(){
			MyByteArrayOutputStream baos = new MyByteArrayOutputStream(4096);
			while(Thread.currentThread().isInterrupted() == false){
				try{
					long startTime = System.currentTimeMillis();
					Message m = null;
					synchronized(sendQueue){
						m = (Message)sendQueue.extract();
					}
					
					if(m != null){
						try{
							baos.reset();
							m.writeTo(baos);
							if(m.getSendType() == MessageFactory.SEND_TYPE_MULTICAST){
								DatagramPacket packet = new DatagramPacket(baos.getBuffer(),baos.size(),broadcastAddress,port);
								broadcastSocket.send(packet);
								logger.debug("[send-message-broadcast] [succ] ["+broadcastAddress+":"+port+"] "+m+" -- ["+receiveQueue.size()+"] ["+sendQueue.size()+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
							}else{
								DatagramPacket packet = new DatagramPacket(baos.getBuffer(),baos.size(),m.getToAddress(),port);
								sendSocket.send(packet);
								logger.debug("[send-message-unicast] [succ] ["+m.getToAddress()+":"+port+"] "+m+" -- ["+receiveQueue.size()+"] ["+sendQueue.size()+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
							}
						}catch(Exception e){
							logger.error("[send-message] [fail] [-] "+m+" -- ["+receiveQueue.size()+"] ["+sendQueue.size()+"] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
						}
					}else{
						try{
							synchronized(sendLock){
								sendLock.wait(1000L);
							}
						}catch(InterruptedException e){
							e.printStackTrace();
						}
					}
				}catch(Throwable e){
					logger.error("[send-message] [fail] [-] [unkown-error] -- ["+receiveQueue.size()+"] ["+sendQueue.size()+"] [-]",e);
				}
			}
		}
	}
	
	protected class ReceiveThread extends Thread{
		
		public void run(){
			byte buffer[] = new byte[1024 * 512];
			while(Thread.currentThread().isInterrupted() == false){
				try{
					DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
					long startTime = System.currentTimeMillis();
					try{
						receiveSocket.receive(packet);
						
						if(packet.getAddress().equals(localAddress)){
							continue;
						}
						
						try{
							Message m = MessageFactory.constructMessage(buffer,packet.getLength());
							m.setFromAddress(packet.getAddress());
							synchronized(receiveLock){
								receiveQueue.insert(m);
								receiveLock.notify();
							}
							logger.debug("[receive-message] [succ] ["+packet.getAddress()+":"+packet.getPort()+"] "+m+" -- ["+receiveQueue.size()+"] ["+sendQueue.size()+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
						}catch(Exception e){
							logger.error("[receive-message] [fail] ["+packet.getAddress()+":"+packet.getPort()+"] [format-error] -- ["+receiveQueue.size()+"] ["+sendQueue.size()+"] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
						}
					}catch(Exception e){
						logger.error("[receive-message] [fail] [-] [socket-error] -- ["+receiveQueue.size()+"] ["+sendQueue.size()+"] ["+(System.currentTimeMillis() - startTime)+"ms]",e);
					}
				}catch(Throwable e){
					logger.error("[receive-message] [fail] [-] [unkown-error] -- ["+receiveQueue.size()+"] ["+sendQueue.size()+"] [-]",e);
				}
			}
		}
	}
	
	public static class MyByteArrayOutputStream extends ByteArrayOutputStream{
		public MyByteArrayOutputStream(int size){
			super(size);
		}
		
		public byte[] getBuffer(){
			return buf;
		}
	}
	
}
