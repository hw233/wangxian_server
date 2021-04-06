package com.xuanzhi.tools.time;
import java.io.IOException;
import java.util.Date;
import com.xuanzhi.tools.transport.*;
public class TimeClient
	implements ConnectionCreatedHandler,MessageHandler{

	/**
	 * ���ͻ��������Ϸ�������ʱ��Lamp��ص��������
	 * �˷�������Ҫ�Ĺ������������ӵ���Ϣ��������Ϣ������
	 */
	public void created(Connection conn, String attachment) throws IOException {
		conn.setMessageFactory(TimeMessageFactory.getInstance());
		conn.setMessageHandler(this);
	}

	public void discardRequestMessage(Connection conn, RequestMessage req)
			throws ConnectionException {
	}

	public ResponseMessage receiveRequestMessage(Connection conn,
			RequestMessage message) throws ConnectionException {
		
		return null;
	}
	
	/**
	 * ���ͻ����յ�����������Ӧʱ������ô˷���
	 */
	public void receiveResponseMessage(Connection conn, RequestMessage req,
			ResponseMessage res) throws ConnectionException {
		if(req instanceof TIME_REQ){
			TIME_RES s = (TIME_RES)res;
			System.out.println("["+new Date()+"] [Client] [Receive-From-Server] ["+new Date(s.getTime())+"]");
		}
	}

	public RequestMessage waitingTimeout(Connection conn, long timeout)
			throws ConnectionException {
		return null;
	}

	public static void main(String args[]) throws Exception{
		String host = "127.0.0.1";
		int port = 7777;
		TimeClient client = new TimeClient();
		DefaultConnectionSelector selector = new DefaultConnectionSelector(host,port,0,1);
		selector.setClientModel(true);
		selector.setConnectionCreatedHandler(client);
		selector.init();
		int c = 0;
		while(c < 100){
			c++;
			Connection conn = selector.takeoutConnection(SelectorPolicy.DefaultClientModelPolicy, 1000);
			if(conn != null){
				conn.writeMessage(new TIME_REQ(TimeMessageFactory.nextSequnceNum()));
				System.out.println("["+c+"] ["+new Date()+"] [Client] [Send-To-Server] [-]");
			}
			//Thread.sleep(1);
		}
	}

}
