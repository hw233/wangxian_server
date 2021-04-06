package com.xuanzhi.tools.query.net;

import com.xuanzhi.tools.transport.*;
import com.xuanzhi.tools.query.ByteUtil;
import com.xuanzhi.tools.query.SimpleQueryEntity;
import com.xuanzhi.tools.query.MobileQuery;
import com.xuanzhi.tools.query.MobileQueryEntity;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Hashtable;
import java.util.List;
import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 */
public class MobileClient implements ConnectionCreatedHandler, MessageHandler,
        MobileQuery,ConnectionTerminateHandler {

    public static final int DEFAULT_MINCONNNUM = 1;
    public static final int DEFAULT_MAXCONNNUM = 3;
    public static final long DEFAULT_TIMEOUT = 1000L;
    public static int DEFAULT_PORT = 6666;
    public static String DEFAULT_IP = "221.179.174.58";

    static {
    	DEFAULT_IP = System.getProperty("vip.server.ip", "221.179.174.58");
    	String portStr = System.getProperty("vip.server.port", "6666");
    	try{
    		DEFAULT_PORT = Integer.parseInt(portStr);
    	}catch(Exception e){
    		DEFAULT_PORT = 6666;
    	}
    }
    
    public static final STATUS DEFAULT_STATUS = new STATUS((byte) 0);

    static Logger logger = Logger.getLogger(MobileClient.class);


    protected Hashtable<RequestMessage,ResponseMessage> messageMap = new Hashtable<RequestMessage,ResponseMessage>();
    protected ConnectionSelector selector;

    protected long timeout = DEFAULT_TIMEOUT;
    protected boolean privateModel = false;
    protected String host;
    protected int port;
    protected int minConnectionNum = DEFAULT_MINCONNNUM;
    protected int maxConnectionNum = DEFAULT_MAXCONNNUM;
    protected int weight = SimpleQueryEntity.Weight.W4.getWeightByInt();

    private static final Object lock = new Object();

    protected static MobileClient m_self;

    public MobileClient(int port, String host) {
        this(port, host,DEFAULT_TIMEOUT,DEFAULT_MINCONNNUM,DEFAULT_MAXCONNNUM);
    }

    public MobileClient() {
        this(DEFAULT_PORT,DEFAULT_IP);
    }

    public MobileClient(int port, String host, long timeout, int minConnectionNum, int maxConnectionNum) {
        this.port = port;
        this.host = host;
        this.timeout = timeout;
        this.minConnectionNum = minConnectionNum;
        this.maxConnectionNum = maxConnectionNum;
    }

    // -------------------- setter ----------------------

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setMinConnectionNum(int minConnectionNum) {
        this.minConnectionNum = minConnectionNum;
    }

    public void setMaxConnectionNum(int maxConnectionNum) {
        this.maxConnectionNum = maxConnectionNum;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public static MobileClient getInstance(){
        if(m_self == null){
            synchronized(lock){
                if(m_self == null){
                    m_self = new MobileClient();
                    try{
                        m_self.init();
                    }catch(Exception e){
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return m_self;
    }

    // -------------------- implements ---------------------

    public void created(Connection conn, String attachment) throws IOException {
        conn.setMessageFactory(MobileQueryFactory.getInstance());
        conn.setMessageHandler(this);
    }

    public void discardRequestMessage(Connection conn, RequestMessage req) throws ConnectionException {
        if(req instanceof MOBILE_REQ){
            messageMap.put(req,null);
            synchronized(req){
				req.notify();
			}
        }
    }

    public void receiveResponseMessage(Connection conn, RequestMessage req, ResponseMessage res) throws ConnectionException {
        if(req instanceof MOBILE_REQ){
            messageMap.put(req,res);
            synchronized(req){
				req.notify();
			}
        }
    }

    public RequestMessage waitingTimeout(Connection conn, long timeout) throws ConnectionException {
        return new MOBILE_TEST_REQ(MobileQueryFactory.nextSequnceNum());
    }

    public ResponseMessage receiveRequestMessage(Connection conn, RequestMessage message) throws ConnectionException {
        if(message instanceof MOBILE_TEST_REQ){
            return new MOBILE_TEST_RES(MobileQueryFactory.nextSequnceNum());
        }
        return null;
    }

    public void ternimate(Connection conn, List<RequestMessage> noResponseMessages, ByteBuffer receiveBuffer) {
    	for(RequestMessage rm : noResponseMessages){
            if(noResponseMessages instanceof MOBILE_REQ){
                messageMap.put(rm,null);
                synchronized(noResponseMessages){
				    noResponseMessages.notify();
			    }
            }
        }
    }

    // -------------------------- function -----------------------------

    public void init() throws Exception{
        privateModel = true;
		DefaultConnectionSelector ds = new DefaultConnectionSelector(host,port,minConnectionNum,maxConnectionNum);
		ds.setConnectionCreatedHandler(this);
        ds.setConnectionTerminateHandler(this);
        ds.init();
		selector = ds;
        m_self = this;
    }

    public STATUS querySafe(String mobile) {
        //STATUS s = DEFAULT_STATUS;

        STATUS s;
        long l = System.currentTimeMillis();
        try{
            s = query(mobile);
            if(logger != null && logger.isInfoEnabled()){
                logger.info("["+mobile+"] ["+(s==null?"null":s)+"] ["+(System.currentTimeMillis() - l)+"]");
            }else{
                System.out.println("["+mobile+"] ["+(s==null?"null":s)+"] ["+(System.currentTimeMillis() - l)+"]");
            }
        }catch(Exception e){
            if(logger != null){
                logger.warn("query ["+mobile+"] [failed] [wait"+timeout+" try again]",e);
            }else{
                e.printStackTrace();
                System.out.println("query ["+mobile+"] [failed] "+e.getMessage()+" [wait"+timeout+" try again]");
            }
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            try {
                s = query(mobile);
                if(logger != null && logger.isInfoEnabled()){
                    logger.info("["+mobile+"] ["+(s==null?"null":s)+"] ["+(System.currentTimeMillis() - l)+"]");
                }else{
                    System.out.println("["+mobile+"] ["+(s==null?"null":s)+"] ["+(System.currentTimeMillis() - l)+"]");
                }
            } catch (Exception e1) {
                if(logger != null){
                    logger.warn("query ["+mobile+"] [failed_again] [default_value_has_been_given]",e1);
                }else{
                    e1.printStackTrace();
                    System.out.println("query ["+mobile+"] [failed_again] "+e1.getMessage()+" [default_value_has_been_given]");
                }
                s = DEFAULT_STATUS;
            }
        }

        return s;
    }

    public STATUS query(String mobile) throws Exception{
        if(mobile == null){
            return DEFAULT_STATUS;
        }
        Connection conn;
		conn = selector.takeoutConnection(SelectorPolicy.DefaultClientModelPolicy,timeout);

        if(conn == null){
			throw new Exception("no avaiable connection");
		}else{
            MOBILE_REQ req = new MOBILE_REQ(MobileQueryFactory.nextSequnceNum(),mobile);
            try{
				conn.writeMessage(req);
			}catch(IOException e){
				conn.close();
				throw e;
			}
            long endTime = System.currentTimeMillis() + timeout;
            MOBILE_RES res = (MOBILE_RES) messageMap.remove(req);
            if(res == null){
				try{
					synchronized(req){
						req.wait(timeout);
					}
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				res = (MOBILE_RES)messageMap.remove(req);
			}

            if(res != null){
				return new STATUS(res.getProp());
			}else{
				if(System.currentTimeMillis() > endTime){
					throw new Exception("timeout ["+timeout+"ms] for waiting reply of sending message");
				}else{
					throw new Exception("no reply of sending message");
				}
			}
        }
    }

    public void close() throws Exception{
        ((DefaultConnectionSelector) selector).destory();
    }

    /**
     * VIP | BLACK | WHITE | RED
     *  1       0       0       0   =   8
     *  0       1       0       0   =   4
     *  0       0       1       0   =   2
     *  0       0       0       1   =   1
     */

    public static class STATUS{
        protected byte bs;

        public STATUS(byte bs) {
            this.bs = bs;
        }

        public STATUS(boolean[] bs){
            this.bs = ByteUtil.getByte(bs,(ByteUtil.BIT - SimpleQueryEntity.Weight.W4.getWeightByInt() + ByteUtil.ADDONE));
        }

        public String format(){
            return ByteUtil.formatByte(bs);
        }

        public byte getByte(){
            return bs;
        }

        public boolean isVip(){
            return (bs & 8) != 0;
        }
        public boolean isBlack(){
            return (bs & 4) != 0;
        }
        public boolean isWhite(){
            return (bs & 2) != 0;
        }
        public boolean isRed(){
            return (bs & 1) != 0;
        }
        public String toString(){
            return "["+(isVip() ? "VIP":"")+"] ["+(isBlack()?"BLACK":"")+"] ["+(isWhite()?"WHITE":"")+"] ["+(isRed()?"RED":"")+"]";
        }
        public String toConf(){
            return (isVip()?"1":"0")+"|"+(isBlack()?"1":"0")+"|"+(isWhite()?"1":"0")+"|"+(isRed()?"1":"0");
        }
    }

    public static void printHelp(){
        StringBuffer sb = new StringBuffer();
        sb.append("===============================================\n");
        sb.append("=============== Mobile Client =================\n");
        sb.append("===============================================\n");
        sb.append("\n\n\n");
        sb.append("-file filename [-tran key] [-retran key] [-ip ip] [-port port] [-timeout timeout] [-minConnectionNum minConnectionNum] [-maxConnectionNum maxConnectionNum]");
        System.out.println(sb.toString());
    }

    public static void main(String[] args) throws Exception{
        if(args == null || args.length < 1){
            printHelp();
            return;
        }
        MobileClient mc;
        String ip = DEFAULT_IP;
        int port = DEFAULT_PORT;
        String fileName = null;
        String key = null;
        boolean tran = false;
        boolean retran = false;
        long timeout = DEFAULT_TIMEOUT;
        int minConnectionNum = DEFAULT_MINCONNNUM;
        int maxConnectionNum = DEFAULT_MAXCONNNUM;
        for(int i=0; i < args.length; i++){
            if("-file".equalsIgnoreCase(args[i])){
                fileName = args[i+1];
            }else if("-ip".equalsIgnoreCase(args[i])){
                ip = args[i+1];
            }else if("-port".equalsIgnoreCase(args[i])){
                port = Integer.parseInt(args[i+1]);
            }else if("-timeout".equalsIgnoreCase(args[i])){
                timeout = Long.parseLong(args[i+1]);
            }else if("-minConnectionNum".equalsIgnoreCase(args[i])){
                minConnectionNum = Integer.parseInt(args[i+1]);
            }else if("-maxConnectionNum".equalsIgnoreCase(args[i])){
                maxConnectionNum = Integer.parseInt(args[i+1]);
            }else if("-tran".equalsIgnoreCase(args[i])){
                tran = true;
                key = args[i+1];
            }else if("-retran".equalsIgnoreCase(args[i])){
                retran = true;
                key = args[i+1];
            }else if("-h".equalsIgnoreCase(args[i]) || "-help".equalsIgnoreCase(args[i])){
                printHelp();
                return;
            }
        }
        if(tran){
            MobileQueryEntity mqe = new MobileQueryEntity();
            mqe.load(key,fileName);
            mqe.write(fileName+MobileQueryEntity.DATA_EXT_NAME,key);
            System.out.println("write data finished");
            return;
        }
        if(retran){
            MobileQueryEntity.translate(fileName,fileName+".txt", key);
            return;
        }
        mc = new MobileClient(port, ip, timeout, minConnectionNum, maxConnectionNum);
        mc.init();
        int total = 0;
        int succ = 0;
        long l = System.currentTimeMillis();
        try{
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String s = null;
            while((s = br.readLine()) != null) {
                total++;
                STATUS st = null;
                try{
                    st = mc.query(s);
                }catch(Exception e){
                    st = null;
                }
                if(st != null){
                    System.out.println("["+s+"] "+st);
                    succ++;
                }else{
                    System.out.println("["+s+"] [query_failed]");
                }
            }
            br.close();
            fr.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("---------------------------finished-----------------------------");
        System.out.println("Succ:"+succ+" Total:"+total+" Cost:"+(System.currentTimeMillis() - l)+"ms");
        mc.close();
        System.exit(0);
    }
}
