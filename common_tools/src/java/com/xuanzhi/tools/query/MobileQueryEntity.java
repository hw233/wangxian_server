package com.xuanzhi.tools.query;

import com.xuanzhi.tools.query.net.MobileClient;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Chris
 * Date: 2009-5-31
 * Time: 17:42:06
 */

/**
 * this class is used to storage the mobile phone numbers
 */
public class MobileQueryEntity {

    public static final int DEFUALT_KEY_LENGTH = 3;
    public static final int MOBILE_COUNT = 11;
    public static final SimpleQueryEntity.Weight DEFAULT_WEIGHT = SimpleQueryEntity.Weight.W4;
    public static final String DATA_EXT_NAME = ".ser";

    /**
     * this is the main data,and the String is the first 3 (default) number from the mobile phone number
     * and the other number is storaged by QueryEntity
     */

    protected Map<String,SimpleQueryEntity> map = new HashMap<String,SimpleQueryEntity>();
    protected Map<String,String> filesmap = new HashMap<String,String>();

    protected int key_length = DEFUALT_KEY_LENGTH;
    protected SimpleQueryEntity.Weight weight = DEFAULT_WEIGHT;

    protected String fileprofix;
    protected List<String> files;

    protected boolean autoSaveToSer = true;

    // --------------- constructor -----------------

    public MobileQueryEntity() {
    }

    public MobileQueryEntity(int key_length, SimpleQueryEntity.Weight weight) {
        this.key_length = key_length;
        this.weight = weight;
    }

    // ----------------- setter and getter-------------------

    public int getKey_length() {
        return key_length;
    }

    public void setKey_length(int key_length) {
        this.key_length = key_length;
    }

    public SimpleQueryEntity.Weight getWeight() {
        return weight;
    }

    public void setWeight(SimpleQueryEntity.Weight weight) {
        this.weight = weight;
    }

    public void setFileprofix(String fileprofix) {
        this.fileprofix = fileprofix;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public void setAutoSaveToSer(boolean autoSaveToSer) {
        this.autoSaveToSer = autoSaveToSer;
    }

    // -------------------- function ---------------------

    /**
     * main function get the item
     * @param mobile mobile phone number
     * @return the item uses byte
     * @throws Exception any Exception we can meet
     */

    public byte getMobileItem(String mobile) throws Exception{
        if(mobile == null){
            throw new NullPointerException("mobile is null");
        }
        if(mobile.length() <= key_length){
            throw new Exception("mobile length is too short");
        }
        String key = mobile.substring(0,key_length);
        String m = mobile.substring(key_length);
        SimpleQueryEntity qe = map.get(key);
        if(qe == null){
            return 0;
        }
        int index = 0;
        try{
            index = Integer.parseInt(m);
        }catch(NumberFormatException e){
            throw new Exception("mobile is not number");
        }
        return qe.getTheItemData(index);
    }

    /**
     * update or add one mobile phone number item
     * @param item the item wants to update
     * @return success or not
     * @throws Exception any exception we can meet
     */

    public boolean updateMobile(QueryItem item)throws Exception{
        if(item == null){
            throw new NullPointerException("item is null");
        }
        String mobile = item.getKey();
        if(mobile.length() <= key_length){
            throw new Exception("mobile length is too short");
        }
        String key = mobile.substring(0,key_length);
        String m = mobile.substring(key_length);
        SimpleQueryEntity qe = map.get(key);
        if(qe == null){
            int x = (int) (Math.pow(10,(MOBILE_COUNT - key_length)));
            qe = new SimpleQueryEntity(x,weight);
            map.put(key,qe);
        }
        qe.addOneItemData(item.getProperties(), Integer.parseInt(m));
        return true;
    }

    /**
     * please sorted the file
     * @param file do Not null
     * @throws Exception any Exception we can meet
     */

    public void loadFromSortedFile(String file) throws Exception{
        if(file == null){
            throw new Exception("file is null");
        }
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String s = null;
            while((s = br.readLine()) != null) {
                QueryItem qi = null;
                try{
                    qi = new QueryItem(s);
                }catch(Exception e){
                    System.out.println("["+s+"] load error");
                    continue;
                }
                updateMobile(qi);
            }

            br.close();
            fr.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("load finished");
    }

    public void write(String file, String key)throws Exception{
        if(file == null || key == null){
            throw new NullPointerException("fileName or key is null");
        }
        SimpleQueryEntity sqe = null;
        sqe = map.get(key);
        if(sqe == null){
            throw new NullPointerException("SimpleQueryEntity is null");
        }
        File f=new File(file);

        FileOutputStream out=new FileOutputStream(f);
        ObjectOutputStream obj=new ObjectOutputStream(out);
        obj.writeObject(sqe);
        obj.flush();
        obj.close();
        out.close();
    }

    public SimpleQueryEntity loadData(String file, String key, boolean addtosystem) throws Exception{
        SimpleQueryEntity sqe = null;
        if(file == null || key == null){
            throw new NullPointerException("fileName or key is null");
        }
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        sqe = (SimpleQueryEntity) ois.readObject();
        ois.close();
        fis.close();
        if(sqe != null && addtosystem){
            map.put(key,sqe);
        }
        return sqe;
    }

    public SimpleQueryEntity loadData2(String file, String key, boolean addtosystem) throws Exception{
        SimpleQueryEntity sqe = null;
        if(file == null || key == null){
            throw new NullPointerException("fileName or key is null");
        }
        int x = (int) (Math.pow(10,(MOBILE_COUNT - key_length)));
        sqe = new SimpleQueryEntity(x, weight, file);
        if(sqe != null && addtosystem){
            map.put(key,sqe);
        }
        return sqe;
    }

    /**
     * init
     * @throws Exception any Exception we can meet
     */

    public void init() throws Exception{
        for(String s : files){
            filesmap.put(s,fileprofix+s);
        }
        loadall();
    }

    public List<String> getAllConfKeys(){
        return new ArrayList<String>(filesmap.keySet());
    }

    public String getFileName(String confkey){
        return filesmap.get(confkey);
    }

    public void loadall() throws Exception{
        for(String key : filesmap.keySet()){
            String filename = filesmap.get(key);
            load(key,filename);
        }
    }

    public void save(String key,String fileName) throws Exception{
        SimpleQueryEntity sqe = map.get(key);
        if(sqe != null){
            sqe.dumpItems2(fileName,key);
        }else{
            throw new Exception("do not have the Entity for the ["+key+"]");
        }
    }

    public void load(String key, String fileName) throws Exception{
        if(fileName == null){
            throw new Exception(fileName+"["+key+"] is null");
        }
        int succ  = 0;
        int total = 0;
        File f = new File(fileName + DATA_EXT_NAME);
        if(f.exists()){
            System.out.println("["+(fileName + DATA_EXT_NAME)+"] is exist, load from the file");
            SimpleQueryEntity sqe = loadData2(fileName + DATA_EXT_NAME,key, true);
            if(sqe == null){
                System.out.println("load from "+(fileName + DATA_EXT_NAME) + " failed ,next load from "+fileName);
            }else{
                System.out.println("load from "+(fileName + DATA_EXT_NAME) + " success");
                int length = sqe.getLength();
                System.out.println("load ["+key+"] finished ["+length+"/"+length+"]");
                return;
            }
        }
        SimpleQueryEntity sqe = null;
        try{
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            String s = null;
            int x = (int) (Math.pow(10,(MOBILE_COUNT - key_length)));
            sqe = new SimpleQueryEntity(x,weight);
            while((s = br.readLine()) != null) {
                QueryItem qi = null;
                total++;
                try{
                    qi = new QueryItem(s);
                    int index = qi.getIndex(key_length);
                    if(index < 0){
                        System.out.println("mobile key parse error"+s);
                        continue;
                    }
                    sqe.addOneItemData(qi.getProperties(),index);
                    succ++;
                }catch(Exception e){
                    System.out.println("["+s+"] load error");
                    continue;
                }
            }
            br.close();
            fr.close();
            if(map.containsKey(key)){
                map.remove(key);
            }
            map.put(key,sqe);
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("load ["+key+"] finished ["+succ+"/"+total+"]");
        if(autoSaveToSer){
            String fi = fileName;
            if(fi != null && !fi.endsWith(DATA_EXT_NAME)){
                fi = fileName + DATA_EXT_NAME;
            }
            //write(fi,key);
            sqe.dumpItems2(fi,key);
            System.out.println("[autoSaveToSer] [active] [save] [finished]");    
        }
    }



    public static void translate(String sourceFile ,String descFile, String key)throws Exception{
        if(sourceFile == null || descFile == null || !sourceFile.endsWith(DATA_EXT_NAME)){
            throw new Exception("sourceFile or descFile is null, maybe you file is not a .ser File");
        }
        File sf = new File(sourceFile);
        if(!sf.exists()){
            throw new Exception("sourceFile not exist");
        }
        long l = System.currentTimeMillis();
        MobileQueryEntity m = new MobileQueryEntity();
        SimpleQueryEntity sqe = m.loadData2(sourceFile,key,false);
        if(sqe == null){
            System.out.println("load failed");
            return;
        }
        sqe.dumpItems(descFile,key);
        System.out.println("translate finished cost :"+(System.currentTimeMillis() - l));
    }

    /**
     * just for testing
     * @param key
     * @return
     */

    protected String getData(String key){
        if(key == null){
            return "";
        }
        SimpleQueryEntity sqe = map.get(key);
        if(sqe == null){
            return "";
        }
        return sqe.getData();
    }

    /**
     * dump all the data to ser file
     * @throws Exception any Exception we can meet
     */

    public void dumpAll() throws Exception{
        for(String key : filesmap.keySet()){
            this.write(filesmap.get(key) + DATA_EXT_NAME,key);
        }
    }
}
