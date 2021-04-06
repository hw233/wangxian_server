<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,org.apache.log4j.Logger,
com.fy.gamegateway.stat.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.tools.*"%><%

	long now = System.currentTimeMillis();

	Logger logger = Logger.getLogger(StatManager.class);

	java.io.InputStream input = request.getInputStream();
	java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
	byte [] buffer = new byte[1024];
	int n = 0;
	while( (n = input.read(buffer)) > 0){
		bout.write(buffer,0,n);
	}
	input.close();
	
	JacksonManager jm = JacksonManager.getInstance();
	String json = new String(bout.toByteArray());
	Number appId = 0;
	Number type = 0;
	String startDate = "";
	String endDate = "";
	String content = "";
	
	long startTime = 0;
	long endTime = 0;
	try{
		Map map = (Map)jm.jsonDecodeObject(json);
		appId = (Number)map.get("AppleID");
		if(appId == null) appId = 0;
		type = (Number)map.get("Type");
		if(type == null) type = 0;
		startDate = (String)map.get("StartDate");
		if(startDate == null) startDate = DateUtil.formatDate(new Date(),"yyyy-MM-dd HH");
		endDate = (String)map.get("EndDate");
		if(endDate == null) endDate = DateUtil.formatDate(new Date(),"yyyy-MM-dd HH");
		content = (String)map.get("Content");
		if(content == null) content = "";
		
		startTime = DateUtil.parseDate(startDate,"yyyy-MM-dd HH").getTime();
		endTime = DateUtil.parseDate(endDate,"yyyy-MM-dd HH").getTime();
		if(endTime == startTime) endTime = startTime + 3600000;
	}catch(Exception e){
		
		StringBuffer ret = new StringBuffer();
		ret.append("{\n");
		ret.append("\"AppleID\":"+appId+",\n");
		ret.append("\"IsError\":1,\n");
		ret.append("\"ErrorMsg\":\"parse json from adsage catch "+e.getClass().getName()+"@"+e.getMessage()+"\",\n");
		ret.append("\"MatchCnt\":0,\n");
		ret.append("\"MatchContents\":\"\"\n");
		ret.append("}");
		out.println(ret);
		
		logger.warn("[推广效果比对] [adsage] [失败] [出现异常] [matchCnt:0] [AppleID:"+appId+"] [Type:"+type+"] [startDate:"+startDate+"] [endDate:"+endDate+"] [content:"+content+"] [cost:"+(System.currentTimeMillis() - now)+"ms]",e);
		return;
	}
	StatManager stat = StatManager.getInstance();
	String items[] = content.split(",");
	
	ArrayList<String> al = new ArrayList<String>();
	for(int i = 0 ; i < items.length ; i++){
		String s = items[i].trim();
		if(s.length() == 0) continue;
		
		Client client = null;
		if(type.intValue() == 0){
			List<Client> list = stat.em4Client.query(Client.class,"uuid=?",new Object[]{s},"timeOfFirstConnected desc",1,2);
			if(list.size() > 0) client = list.get(0);
		}else{
			List<Client> list = stat.em4Client.query(Client.class,"macAddress=?",new Object[]{s},"timeOfFirstConnected desc",1,2);
			if(list.size() > 0) client = list.get(0);
		}
		
		if(client != null){
			
			logger.warn("[推广效果比对] [adsage] [逐个比对] [成功匹配] ["+s+"]");

			
			if(client.getTimeOfFirstConnected().getTime() >= startTime){
				if(type.intValue() == 0){
					if(al.contains(client.getUuid()) == false){
						al.add(client.getUuid());	
					}
				}else {
					if(al.contains(client.getMacAddress()) == false){
						al.add(client.getMacAddress());	
					}
				}
			}
		}else{
			logger.warn("[推广效果比对] [adsage] [逐个比对] [未匹配] ["+s+"]");
		}
	}
	
	StringBuffer ret = new StringBuffer();
	ret.append("{\n");
	ret.append("\"AppleID\":"+appId+",\n");
	ret.append("\"IsError\":0,\n");
	ret.append("\"ErrorMsg\":\"\",\n");
	ret.append("\"MatchCnt\":"+al.size()+",\n");
	ret.append("\"MatchContents\":\"");
	for(int i = 0 ; i < al.size() ; i++){
		ret.append(al.get(i));
		if(i < al.size() -1){
			ret.append(",");
		}
	}
	ret.append("\"\n");
	ret.append("}");
	out.println(ret);
	
	logger.warn("[推广效果比对] [adsage] [成功] [OK] [matchCnt:"+al.size()+"] [AppleID:"+appId+"] [Type:"+type+"] [startDate:"+startDate+"] [endDate:"+endDate+"] [content:"+content+"] [cost:"+(System.currentTimeMillis() - now)+"ms]");
	
%>