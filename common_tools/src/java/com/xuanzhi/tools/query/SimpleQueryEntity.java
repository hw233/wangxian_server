package com.xuanzhi.tools.query;

import com.xuanzhi.tools.query.net.MobileClient;

import java.io.*;
import java.util.UUID;

/**
 */
public class SimpleQueryEntity implements Serializable {

    private static final long serialVersionUID = 1244082560570654321L;

    /**
     * how many BIT should be used for storage one Item
     */

    public enum Weight{
        W1,
        W2,
        W4,
        W8;

        public int getWeight(){
            switch(this){
                case W1 : return 0;
                case W2 : return 1;
                case W4 : return 2;
                case W8 : return 3;
                default : return 0;
            }
        }

        public int getWeightByInt(){
            switch(this){
                case W1 : return 1;
                case W2 : return 2;
                case W4 : return 4;
                case W8 : return 8;
                default : return 1;
            }
        }
    }

    /**
     * how many items
     */

    private int x;

    /**
     * s position for array
     */

    private int real_x;

    /**
     * the data
     */

    private byte[] data;

    /**
     * the unique id
     */

    private String id;

    /**
     * the weight for array,
     * @see SimpleQueryEntity.Weight
     */

    private Weight weight;

    // ---------------------- constructor -----------------------

    public SimpleQueryEntity(int x, Weight weight) {
        this.x = x;
        this.weight = weight;
        if(!check()){
            throw new RuntimeException("one or more parameter(s) is wrong");
        }
        createEmptyData();
        this.id = UUID.randomUUID().toString();
    }

    public SimpleQueryEntity(int x,Weight weight, String file){
        this.x = x;
        this.weight = weight;
        if(!check()){
            throw new RuntimeException("one or more parameter(s) is wrong");
        }
        createEmptyData();
        loadDataFromBinaryFile(file);
        this.id = UUID.randomUUID().toString();
    }

    // ---------------------- getter -----------------------

    public int getX() {
        return x;
    }

    public int getReal_x() {
        return real_x;
    }

    public String getId() {
        return id;
    }

    public Weight getWeight() {
        return weight;
    }

    public int getLength(){
        return data.length << ByteUtil.BIT2 >> weight.getWeight();
    }

    // ----------------------- functions -----------------------

    //  ----------------- private functions ----------------

    /**
     * check the fields
     * @return false if someone is wrong
     */

    private boolean check(){
        if(x >= 0){
            return true;
        }
        if(this.weight == null){
            this.weight = Weight.W1;
        }
        return false;
    }

    private void createEmptyData(){
        int real_x = x << weight.getWeight() >> ByteUtil.BIT2;
        if(((x << weight.getWeight()) & (ByteUtil.BIT -1 )) != 0){
            real_x = real_x + ByteUtil.ADDONE;
        }
        data = new byte[real_x];
        this.real_x = real_x;
    }

    // --------------------- public functions ----------------------

    /**
     * get byte from the data arrays
     * @param x the x position for arrays
     * @return the byte
     */

    public byte getByte(int x){
        return data[x];
    }

    /**
     * format all data array to String,so be careful
     * @return the formated String
     */

    public String getData(){
        StringBuffer sb = new StringBuffer();
        for(int i =0; i < real_x; i++){
                sb.append(ByteUtil.formatByte(data[i]));
                sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * get the item data from the data arrays
     * @param x the x position for item
     * @return the byte ,remenber it also from the high bit and the count is this.weight
     */

    public byte getTheItemData(int x){
        byte temp = 0;

        int real_x = x << this.weight.getWeight() >> ByteUtil.BIT2;
        int real_xx = x << this.weight.getWeight() & (ByteUtil.BIT - 1);

        if(ByteUtil.BIT - (real_xx + this.weight.getWeightByInt()) >= 0){
            byte t = getByte(real_x);
            temp = ByteUtil.getNewByte(t,real_xx+1,real_xx+weight.getWeightByInt());
        }else{
            byte t1 = (byte) (getByte(real_x) >> (ByteUtil.BIT - (real_xx)));
            int b = 2*ByteUtil.BIT-this.weight.getWeightByInt()-real_xx;
            byte t2 = (byte) (getByte(real_x + ByteUtil.ADDONE) >> b);
            temp = (byte) (t1 << b | t2);
        }

        return temp;
    }

    /**
     * important function
     * It shows us how to input one Item data,
     * you can override it ,and remenber the data from high bit and the count is this.weight
     * @param item the data you want to add
     * @param x the x position for item
     * @return the normal data it can input to the data arrays.
     */

    public byte addOneItemData(boolean[] item, int x){
        if(item == null || x < 0){
            return 0;
        }
        byte temp = 0;
        int real_x = x << this.weight.getWeight() >> ByteUtil.BIT2;
        int real_xx = x << this.weight.getWeight() & (ByteUtil.BIT - 1);
        if(real_xx == 0){
            byte old = getByte(real_x);
            temp = ByteUtil.getByte(item,1);
            temp = addItem(old,temp,1,this.weight.getWeightByInt());
        }else{
            byte old = getByte(real_x);
            byte add = ByteUtil.getByte(item,real_xx+1);
            temp = addItem(old,add,real_xx+1,real_xx+this.weight.getWeightByInt());
        }

        try{
            this.data[real_x] = temp;
        }catch(Exception e){
            temp = 0;
        }
        return temp;
    }

    /**
     * and also this is another important function to add the data
     * and the different from addOneItemData is that maybe you can add one or more data to the
     * data arrays at once,just make sure that is less than 1 byte ,that's enough
     * @param data the data you want to add
     * @param x the x position for arrays
     * @return the normal data it can input to the data arrays.
     */

    public byte addOneByteData(byte data, int x){
        if(x < 0){
            return 0;
        }
        byte temp = data;
        try{
            this.data[x] = temp;
        }catch(Exception e){
            temp = 0;
        }
        return temp;
    }

    /**
     * dump the all item to file
     * @param file filename
     * @param key the KEY do not null
     * @throws Exception any Exception we can meet
     */

    public void dumpItems(String file, String key) throws Exception{
        if(file == null || key == null){
            throw new Exception("File or key is null");   
        }
        FileWriter fw=new FileWriter(file,false);
        BufferedWriter bw=new BufferedWriter(fw);
        for(int i =0; i < getLength(); i++){
            byte b = getTheItemData(i);
            QueryItem qi = new QueryItem(String.valueOf(i),ByteUtil.getPropsFromByte(b,weight.getWeightByInt()));
            MobileClient.STATUS sta = new MobileClient.STATUS(qi.getProperties());
            if(sta.getByte() != 0){
                String line = key+qi.getKeySafely()+" "+ sta.toConf()+"\n";
                bw.write(line);
            }
        }
        bw.close();
        fw.close();
    }

    /**
     * dump the data which belongs to the key to the file with binary
     * @param file filename
     * @param key the KEY do not null
     * @throws Exception any Exception we can meet
     */

    public void dumpItems2(String file, String key) throws Exception{
        if(file == null || key == null){
            throw new Exception("File or key is null");
        }
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        out.write(data, 0, data.length);
        out.close();
    }

    public void loadDataFromBinaryFile(String file) throws RuntimeException{
        if(file == null){
            throw new RuntimeException("file is null");
        }
        BufferedInputStream in = null;
        try{
            in = new BufferedInputStream(new FileInputStream(file));
            int len;
            while ((len = in.read(data)) > 0){
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace(); 
            }
        }

    }

    public static byte addItem(byte source, byte add, int from ,int end){
        byte temp = ByteUtil.getNewByte2((byte) -1,from,end);
        return (byte) (source & temp | add);
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleQueryEntity that = (SimpleQueryEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    public static void main(String[] args) throws Exception{
        SimpleQueryEntity sqe = new SimpleQueryEntity(50,Weight.W4);
        boolean[] bs1 ={true,false,false,false};
        boolean[] bs2 ={true,true,true,true};
        boolean[] bs3 ={true,false,false,true};
        boolean[] bs4 ={false,false,false,false};
        sqe.addOneItemData(bs1,0);
        System.out.println(sqe.getData());
        System.out.println("-----------");
        sqe.addOneItemData(bs2,1);
        System.out.println(sqe.getData());
        System.out.println("-----------");
        sqe.addOneItemData(bs3,2);
        System.out.println(sqe.getData());
        System.out.println("-----------");
        sqe.addOneItemData(bs2,3);
        System.out.println(sqe.getData());
        System.out.println("-----------");
        sqe.addOneItemData(bs1,4);
        System.out.println(sqe.getData());
        System.out.println("-----------");
        sqe.addOneItemData(bs4,1);
        System.out.println(sqe.getData());
        System.out.println("-----------");
        sqe.addOneItemData(bs4,2);
        System.out.println(sqe.getData());
    }
}
