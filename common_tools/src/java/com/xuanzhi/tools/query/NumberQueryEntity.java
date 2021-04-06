package com.xuanzhi.tools.query;

import java.util.Map;
import java.util.HashMap;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

/**
 * Created by IntelliJ IDEA.
 * User: Chris
 * Date: 2009-5-26
 * Time: 9:48:28
 */

/**
 * this class is used to storage the mobile phone numbers
 */
public class NumberQueryEntity {

    public static final int MAX_X = 100000;
    public static final int DEFUALT_KEY_LENGTH = 3;
    public static final int MOBILE_COUNT = 11;
    public static final int DEFAULT_WEIGHT = 1;

    /**
     * this is the main data,and the String is the first 3 (default) number from the mobile phone number
     * and the other number is storaged by QueryEntity
     */

    protected Map<String,QueryEntity> map = new HashMap<String,QueryEntity>();

    protected int key_length = DEFUALT_KEY_LENGTH;
    protected int weight = DEFAULT_WEIGHT;

    // --------------- constructor -----------------

    public NumberQueryEntity() {
    }

    public NumberQueryEntity(int key_length, int weight) {
        this.key_length = key_length;
        this.weight = weight;
    }

    // ----------------- setter and getter-------------------

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getKey_length() {
        return key_length;
    }

    public void setKey_length(int key_length) {
        if(key_length <= 0){
            this.key_length = DEFUALT_KEY_LENGTH;    
        }
        this.key_length = key_length;
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
        QueryEntity qe = map.get(key);
        if(qe == null){
            return 0;
        }
        int index = 0;
        try{
            index = Integer.parseInt(m);
        }catch(NumberFormatException e){
            throw new Exception("mobile is not number");
        }
        int y = index / MAX_X;
        int x = index % MAX_X;
        return qe.getTheItemData(x,y);
    }

    /**
     * update or add one mobile phone number item
     * @param item the item wants to update
     * @return success or not
     * @throws Exception any exception we can meet
     */

    public boolean updateMobile(QueryItem item)throws Exception{
        if(item == null){
            throw new NullPointerException("mobile is null");
        }
        String mobile = item.getKey();
        if(mobile.length() <= key_length){
            throw new Exception("mobile length is too short");
        }

        // just want to check the mobile is number
        int index = Integer.parseInt(mobile);
        String key = mobile.substring(0,key_length);
        String m = mobile.substring(key_length);
        QueryEntity qe = map.get(key);
        int ry = index / MAX_X;
        int rx = index % MAX_X;
        if(qe == null){
            int y = (int) (Math.pow(10,(MOBILE_COUNT - key_length))/MAX_X);
            qe = new QueryEntity(MAX_X,y,weight);
            map.put(key,qe);
        }
        qe.addOneItemData(item.getProperties(),rx,ry);
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
}
