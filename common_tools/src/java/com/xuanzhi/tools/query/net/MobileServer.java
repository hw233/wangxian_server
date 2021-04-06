package com.xuanzhi.tools.query.net;

import com.xuanzhi.tools.transport.*;
import com.xuanzhi.tools.query.MobileQueryEntity;
import com.xuanzhi.tools.query.SimpleQueryEntity;
import com.xuanzhi.tools.query.ByteUtil;
import com.xuanzhi.tools.query.QueryItem;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

/**
 */
public class MobileServer
        implements ConnectionConnectedHandler, MessageHandler {

    public static final String NAME = "com.xuanzhi.tools.query.net.MobileServer";

    protected static Logger logger = Logger.getLogger(MobileServer.class);
    protected static Logger conflogger = Logger.getLogger("com.xuanzhi.tools.query.net.MobileServer.conf");

    protected MobileQueryEntity mqe;
    protected String profixfile;
    protected List<String> files;
    protected int port;
    protected boolean autoSaveToSer = true;
    protected long timeout = 1000L;

    protected static MobileServer m_self;

    // ----------------- setter ---------------------

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public void setProfixfile(String profixfile) {
        this.profixfile = profixfile;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public static MobileServer getInstance(){
        return m_self;
    }

    public void setAutoSaveToSer(boolean autoSaveToSer) {
        this.autoSaveToSer = autoSaveToSer;
    }

    /**
     * init
     * @throws Exception any Exception we can meet
     */

    public void init() throws Exception{
        mqe = new MobileQueryEntity(3, SimpleQueryEntity.Weight.W4);
        mqe.setFileprofix(profixfile);
        mqe.setFiles(files);
        mqe.setAutoSaveToSer(autoSaveToSer);
        mqe.init();

        // ------------------ init server -----------------
        DefaultConnectionSelector selector = new DefaultConnectionSelector();
        selector.setClientModel(false);
        selector.setPort(port);
        selector.setConnectionConnectedHandler(this);
        selector.init();

        m_self = this;
    }

    // ------------------- implements -------------------

    public void connected(Connection conn) throws IOException {
        conn.setMessageFactory(MobileQueryFactory.getInstance());
        conn.setMessageHandler(this);
    }

    public void discardRequestMessage(Connection conn, RequestMessage req) throws ConnectionException {
    }

    public void receiveResponseMessage(Connection conn, RequestMessage req, ResponseMessage res) throws ConnectionException {
    }

    public RequestMessage waitingTimeout(Connection conn, long timeout) throws ConnectionException {
        return new MOBILE_TEST_REQ(MobileQueryFactory.nextSequnceNum());
    }

    public ResponseMessage receiveRequestMessage(Connection conn, RequestMessage message) throws ConnectionException {
        if(message instanceof MOBILE_REQ){
            MOBILE_REQ req = (MOBILE_REQ) message;
            String m = req.getMobile();
            byte ret = 0;
            long l = System.currentTimeMillis();
            if(m != null){
                try {
                    ret = mqe.getMobileItem(m);
                    logger.info("[getMobileItem] [success] ["+m+"] ["+ ByteUtil.formatByte(ret).substring(4)+"] [cost:"+(System.currentTimeMillis() - l)+"ms]");
                } catch (Exception e) {
                    logger.error("[getMobileItem] [error] ["+m+"] [cost:"+(System.currentTimeMillis() - l)+"]",e);
                }
            }
            return new MOBILE_RES(req.getSequnceNum(),ret);
        }else if(message instanceof MOBILE_TEST_REQ){
            MOBILE_TEST_REQ req = (MOBILE_TEST_REQ) message;
            return new MOBILE_TEST_RES(req.getSequnceNum());
        }
        return null;
    }

    public byte query(String mobile){
        byte ret = 0;
            long l = System.currentTimeMillis();
            if(mobile != null){
                try {
                    ret = mqe.getMobileItem(mobile);
                    logger.info("[getMobileItem] [success] ["+mobile+"] ["+ ByteUtil.formatByte(ret).substring(4)+"] [cost:"+(System.currentTimeMillis() - l)+"ms]");
                } catch (Exception e) {
                    logger.error("[getMobileItem] [error] ["+mobile+"] [cost:"+(System.currentTimeMillis() - l)+"]",e);
                }
            }
        return ret;
    }

    public boolean updateMobile(QueryItem item){
        if(item == null || item.getKey() == null){
            logger.warn("update mobile [null] failed");
            return false;
        }
        long l = System.currentTimeMillis();
        try{
            mqe.updateMobile(item);
            conflogger.info("update mobile ["+item.getKey()+"] success cost ["+(System.currentTimeMillis() - l)+"ms]");
        }catch(Exception e){
            conflogger.warn("update mobile ["+item.getKey()+"] failed",e);
            return false;
        }
        return true;
    }

    public boolean updateMobile(QueryItem item, String reason){
        if(item == null || item.getKey() == null){
            logger.warn("update mobile [null] failed");
            return false;
        }
        long l = System.currentTimeMillis();
        byte ret = 0;
        try{
            ret = query(item.getKey());
            mqe.updateMobile(item);
            conflogger.info("update mobile ["+item.getKey()+"] success from ["+ByteUtil.formatByte(ret)+"] to ["+ByteUtil.formatByte(ByteUtil.getByte(item.getProperties(),1))+"] reason ["+(reason==null?"":reason)+"] cost ["+(System.currentTimeMillis() - l)+"ms]");
        }catch(Exception e){
            conflogger.warn("update mobile ["+item.getKey()+"] failed from ["+ByteUtil.formatByte(ret)+"] to ["+ByteUtil.formatByte(ByteUtil.getByte(item.getProperties(),1))+"] reason ["+(reason==null?"":reason)+"]",e);
            return false;
        }
        return true;
    }

    public List<String> getMobilePortList(){
        return mqe.getAllConfKeys();
    }

    public boolean reloadAll(){
        long l = System.currentTimeMillis();
        try{
            mqe.loadall();
            conflogger.info("reload all success cost ["+(System.currentTimeMillis() - l)+"ms]");
        }catch(Exception e){
            e.printStackTrace();
            conflogger.error("reload all error",e);
            return false;
        }
        return true;
    }

    public boolean reload(String confkey, String key){
        String fileName = mqe.getFileName(confkey);
        long l = System.currentTimeMillis();
        if(fileName != null){
            try {
                mqe.load(key, confkey);
                conflogger.info("reload ["+confkey+"] ["+key+"] success cost ["+(System.currentTimeMillis() - l)+"ms]");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                conflogger.error("reload ["+confkey+"] ["+key+"] error ",e);
            }
        }
        conflogger.error("reload ["+confkey+"] ["+key+"] file is null ");
        return false;
    }

    public boolean loadFrom(String file ,String key){
        long l = System.currentTimeMillis();
        try {
            mqe.load(key,file);
            conflogger.info("load from ["+file+"] ["+key+"] success cost ["+(System.currentTimeMillis() - l)+"ms]");
        } catch (Exception e) {
            e.printStackTrace();
            conflogger.error("load from ["+file+"] ["+key+"] error",e);
            return false;
        }
        return true;
    }

    public boolean save(String key, String confkey){
        String fileName = mqe.getFileName(confkey);
        if(fileName != null && !fileName.endsWith(MobileQueryEntity.DATA_EXT_NAME)){
            fileName = fileName + MobileQueryEntity.DATA_EXT_NAME;
        }
        long l = System.currentTimeMillis();
        if(fileName != null){
            try {
                mqe.save(key,fileName);
                conflogger.info("save ["+key+"] ["+confkey+"] success cost ["+(System.currentTimeMillis() - l)+"ms]");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                conflogger.error("save ["+key+"] ["+confkey+"] error ",e);
            }
        }
        conflogger.error("save ["+key+"] ["+confkey+"] error file is null");
        return false;
    }

    public boolean saveTo(String file,String key){
        long l = System.currentTimeMillis();
        if(file != null && !file.endsWith(MobileQueryEntity.DATA_EXT_NAME)){
            file = file + MobileQueryEntity.DATA_EXT_NAME;
        }
        try {
            mqe.save(key,file);
            conflogger.info("save to ["+file+"] ["+key+"] success cost ["+(System.currentTimeMillis() - l)+"ms]");
        } catch (Exception e) {
            e.printStackTrace();
            conflogger.error("save to ["+file+"] ["+key+"] error",e);
            return false;
        }
        return true;
    }
}
