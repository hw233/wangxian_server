<%@ page import="com.xuanzhi.tools.query.QueryItem" %><%@ page import="com.xuanzhi.tools.query.net.MobileServer" %><%@ page import="java.util.List" %><%@ page import="java.util.ArrayList" %><%@ page import="com.xuanzhi.tools.query.ByteUtil" %><%@ page import="java.util.Set" %><%@ page import="java.util.TreeSet" %><%
    String sub = request.getParameter("sub");
    StringBuffer msg = new StringBuffer();
    StringBuffer msg1 = new StringBuffer();
    String utype = request.getParameter("usertype");
    String ty = request.getParameter("ty");
    List<String> faileds = new ArrayList<String>();
    Set<String> keys = new TreeSet<String>();
    int succ = 0;
    int total = 0;
    boolean save_s = false;
    if("sub".equalsIgnoreCase(sub)){
        MobileServer ms = MobileServer.getInstance();
        String input = request.getParameter("input");
        String reason = request.getParameter("reason");
        if(reason == null){
            reason = "default reason";
        }
        if(input != null){
			input = input.replaceAll(",","\n");
		}
        long l = System.currentTimeMillis();

        if(input != null){
            String[] ss = input.split("\n");
            for(String s : ss){
                byte ret = 0;
                try{
                    ret = ms.query(s);
                    if("VIP".equalsIgnoreCase(utype)){
                        if("add".equalsIgnoreCase(ty)){
                            ret = (byte) (ret | 8);
                        }else{
                            ret = (byte) (ret & 7);
                        }
                    }else if("BLACK".equalsIgnoreCase(utype)){
                        if("add".equalsIgnoreCase(ty)){
                            ret = (byte) (ret | 4);
                        }else{
                            ret = (byte) (ret & 11);
                        }
                    }else if("WHITE".equalsIgnoreCase(utype)){
                        if("add".equalsIgnoreCase(ty)){
                            ret = (byte) (ret | 2);
                        }else{
                            ret = (byte) (ret & 13);
                        }
                    }else{
                        if("add".equalsIgnoreCase(ty)){
                            ret = (byte) (ret | 1);
                        }else{
                            ret = (byte) (ret & 14);
                        }
                    }
                    QueryItem qi = new QueryItem(s.trim(), ByteUtil.getPropsFromByte(ret,4));
                    if(ms.updateMobile(qi,reason)){
                        keys.add(qi.getKey().substring(0,3));
                        succ++;
                    }else{
                        faileds.add(s);
                    }

                }catch(Exception e){
                }
                total++;
            }
        }
        msg.append("finished succ = "+succ+" total = "+total+" cost = "+(System.currentTimeMillis() - l)+" ms");
        msg.append("<br>");
        for(String ts : faileds){
            msg.append(ts);
            msg.append("<br>");
        }
        for(String k : keys){
            if(ms.save(k,k)){
                msg1.append("[save] ["+k+"] [success]");
                save_s = true;
            }else{
                msg1.append("[save] ["+k+"] [failed]");
                save_s = false;
            }
            msg1.append("<br>");
        }
    }
    boolean ret = (total == succ);
    ret = ret && save_s;
    out.print(ret);
%>