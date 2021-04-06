package com.xuanzhi.tools.query;

/**
 */
public class QueryItem {

    protected String key;
    protected boolean[] props;

    public QueryItem(String key, boolean[] props) {
        this.key = key;
        this.props = props;
    }

    public QueryItem(String line) throws Exception{
        if(line == null){
            throw new NullPointerException("line is null");
        }
        String[] ss = line.split(" ",2);
        if(ss == null || ss.length < 2){
            throw new Exception("format error");
        }
        this.key = ss[0];
        String[] tt = ss[1].split("[|]");
        props = new boolean[tt.length];
        for(int i =0; i < tt.length; i++){
            props[i] = "1".equalsIgnoreCase(tt[i]) || "true".equalsIgnoreCase(tt[i]);
        }
    }

    public String getKey() {
        return key;
    }

    public String getKeySafely(){
        if(key == null){
            return "00000000";
        }
        int length = key.length();
        StringBuffer sb = new StringBuffer();
        for(int i =0; i < (8 - length); i++){
            sb.append("0");
        }
        sb.append(key);
        return sb.toString();
    }

    public int getIndex(int profix_length){
        if(key == null || key.length() <= profix_length){
            return -1;
        }
        return Integer.parseInt(key.substring(profix_length));
    }

    public int getBits(){
        return props.length;
    }

    public boolean[] getProperties(){
        return props;
    }
}
