package com.xuanzhi.tools.query;

/**
 * Created by IntelliJ IDEA.
 * User: Chris
 * Date: 2009-5-22
 * Time: 16:40:26
 */

/**
 * this is a tool for byte
 */
public class ByteUtil {

    public static final byte BIT = 8;
    public static final byte ADDONE = 1;
    public static final int BIT2 = 3;

    /**
     * fill the properties to one byte
     * @param bs properties
     * @param index the index of the first property in a byte
     * @return filled the byte
     */

    public static byte getByte(boolean[] bs, int index){
        byte temp = 0;
        if (bs == null || index <= 0 || index > BIT){
            return temp;
        }
        int i = 0;
        for(boolean b : bs){
            byte t = 0;
            if(b){
                t = 1;
                t = (byte) (t << (BIT - (index+i)));
                temp = (byte) (temp | t);
            }
            i++;
        }
        return temp;
    }

    public static String formatByte(byte b){
        String s = Integer.toBinaryString(b);
        if(s.length() > 8){
            s = s.substring(s.length()-8);
        }else if(s.length() < 8){
            int l = 8 - s.length();
            StringBuffer sb = new StringBuffer();
            for(int i=0; i < l; i++){
                sb.append("0");
            }
            s = sb.append(s).toString();
        }
        return s;
    }

    public static byte getNewByte(byte b, int from ,int end){
        if(from < 0 || from > BIT || end <0 || end > BIT || end < from){
            return 0;
        }
        boolean[] bs = {false,false,false,false,false,false,false,false};
        for(int i = 1; i <= 8; i++){
            if(i >= from && i <=end){
                bs[i-1] = true;
            }
        }
        byte temp = getByte(bs,1);
        return (byte) ((b&temp) >> (BIT - end));
    }

    public static byte getNewByte2(byte b,int from ,int end){
        if(from < 0 || from > BIT || end <0 || end > BIT || end < from){
            return 0;
        }
        boolean[] bs = {true,true,true,true,true,true,true,true};
        for(int i = 1; i <= 8; i++){
            if(i >= from && i <=end){
                bs[i-1] = false;
            }
        }
        return getByte(bs,1); 
    }

    public static boolean[] getPropsFromByte(byte b,int count){
        if(count <= 0 || count > BIT){
            return new boolean[0];
        }
        boolean[] bs = new boolean[count];
        byte temp = (byte) (b << (BIT - count));
        String s = formatByte(temp);
        for(int i=0; i < count; i++){
            bs[i] = s.charAt(i) == '1';
        }

        return bs;
    }

}
