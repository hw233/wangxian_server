package com.xuanzhi.tools.query;

import java.util.UUID;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Chris
 * Date: 2009-5-25
 * Time: 10:03:23
 */

/**
 * storage the data and provide the functions to query
 */
public class QueryEntity implements Serializable {

    /**
     * the data
     */

    protected byte[][] data;

    /**
     * for item
     */

    protected int x;

    /**
     * for item
     */

    protected int y;

    /**
     * for array
     */

    protected int a_x;

    /**
     * for array
     */

    protected int a_y;

    /**
     * how many bits will be used for one item
     */

    protected int weight;

    /**
     * for unique
     */

    protected String id;

    // --------------- constructor -----------------

    public QueryEntity(int x, int y, int weight) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        if(!check()){
            throw new RuntimeException("one or more parameter(s) is wrong");
        }
        createEmptyData();
        this.id = UUID.randomUUID().toString();
    }

    // ----------------- getter -------------------

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getA_x() {
        return a_x;
    }

    public int getA_y() {
        return a_y;
    }

    public String getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    // ------------------ functions --------------------


    //  ----------------- private functions ----------------
    /**
     * check the fields
     * @return false if someone is wrong
     */

    private boolean check(){
        if(x >= 0 && y >= 0 && weight >=1){
            return true;
        }
        return false;
    }

    private void createEmptyData(){
        int real_x = x*weight/ByteUtil.BIT;
        if(x*weight%ByteUtil.BIT!=0){
            real_x = real_x + ByteUtil.ADDONE;
        }
        data = new byte[real_x][y];
        this.a_x = real_x;
        this.a_y = y;
    }

    // --------------------- public functions ----------------------

    /**
     * important function
     * It shows us how to input one Item data,
     * you can override it ,and remenber the data from high bit and the count is this.weight
     * @param item the data you want to add
     * @param x the x position for item
     * @param y the y position for item
     * @return the normal data it can input to the data arrays.
     */

    public byte addOneItemData(boolean[] item, int x, int y){
        if(item == null || x < 0 || y < 0){
            return 0;
        }
        byte temp = 0;
        int real_x = x * this.weight / ByteUtil.BIT;
        int real_xx = x * this.weight % ByteUtil.BIT;
        if(real_xx == 0){
            byte old = getByte(real_x,y);
            temp = ByteUtil.getByte(item,1);
            temp = (byte)(temp|old);
        }else{
            byte old = getByte(real_x,y);
            byte add = ByteUtil.getByte(item,real_xx+1);
            temp = (byte)(old | add);
        }

        try{
            this.data[real_x][y] = temp;
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
     * @param y the y position for arrays
     * @return the normal data it can input to the data arrays.
     */

    public byte addOneByteData(byte data, int x, int y){
        if(x < 0 || y < 0){
            return 0;
        }
        byte temp = data;
        try{
            this.data[x][y] = temp;
        }catch(Exception e){
            temp = 0;
        }
        return temp;
    }

    /**
     * get the item data from the data arrays
     * @param x the x position for item
     * @param y the y position for item
     * @return the byte ,remenber it also from the high bit and the count is this.weight
     */

    public byte getTheItemData(int x, int y){
        byte temp = 0;

        int real_x = x * this.weight / ByteUtil.BIT;
        int real_xx = x * this.weight % ByteUtil.BIT;

        if(ByteUtil.BIT - (real_xx + this.weight) >= 0){
            byte t = getByte(real_x,y);
            temp = ByteUtil.getNewByte(t,real_xx+1,real_xx+weight);
        }else{
            byte t1 = (byte) (getByte(real_x,y) >> (ByteUtil.BIT - (real_xx)));
            int b = 2*ByteUtil.BIT-this.weight-real_xx;
            byte t2 = (byte) (getByte(real_x + ByteUtil.ADDONE,y) >> b);
            temp = (byte) (t1 << b | t2);
        }

        return temp;
    }

    /**
     * get byte from the data arrays
     * @param x the x position for arrays
     * @param y the y position for arrays
     * @return the byte
     */

    public byte getByte(int x, int y){
        return data[x][y];    
    }

    /**
     * format all data array to String,so be careful
     * @return the formated String
     */

    public String getData(){
        StringBuffer sb = new StringBuffer();
        for(int i =0; i < a_y; i++){
            for(int j=0; j < a_x; j++){
                sb.append(ByteUtil.formatByte(data[j][i]));
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // for hashmap and so on

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryEntity that = (QueryEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    public static void main(String[] args) {
        QueryEntity qe = new QueryEntity(5,2,2);
        System.out.println(qe.getData());
        System.out.println("----------------------------");
        boolean[] bs = {true,true,true,true,false,true,true,true};
        boolean[] bs2 = {false,true};
        qe.addOneItemData(bs2,3,0);
        qe.addOneItemData(bs2,1,0);
        System.out.println(qe.getData());
        System.out.println(ByteUtil.formatByte(qe.getTheItemData(3,0)));
        boolean[] bs3 = ByteUtil.getPropsFromByte(qe.getTheItemData(3,0),2);
        for(boolean bbb : bs3){
            System.out.println(bbb);
        }
    }
}
