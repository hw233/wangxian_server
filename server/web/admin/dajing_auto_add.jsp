<%@page import="java.lang.reflect.Field"%>
<%@ page contentType="text/html;charset=utf-8" import="java.util.*,java.util.concurrent.*,
com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,com.xuanzhi.tools.servlet.HttpUtils,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,
com.xuanzhi.tools.queue.*,com.fy.engineserver.gateway.*,org.w3c.dom.*,
com.fy.engineserver.message.*,com.fy.engineserver.dajing.*"%><%! 



	public DajingStudio getDajingStudio(DajingStudioManager dm,String ip){
		if(ip == null || ip.length() == 0) return null;
		Iterator<String> it = dm.dajingMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			if(key.contains(ip)) return dm.dajingMap.get(key);
		}
		return null;
	}
%><%

DajingStudioManager dsManager = DajingStudioManager.getInstance();
if(dsManager.initialized == false){
	dsManager.init();
}

ConcurrentHashMap<String,DajingStudio> player2DajingMap = null;
Field fff = DajingStudioManager.class.getDeclaredField("player2DajingMap");
fff.setAccessible(true);
player2DajingMap = (ConcurrentHashMap<String,DajingStudio>)fff.get(dsManager);

String action = request.getParameter("action");
if(action == null ) action = "";

PlayerManager sm = PlayerManager.getInstance();

ArrayList<String> duokaiMessage = new ArrayList<String>();

if(action.equals("add_studio")){
	String studio = request.getParameter("studio");
	//ip-->userName,userName,userName

	//ip -->(userName,phoneType),(userName,phoneType),(userName,phoneType)
	HashMap<String,ArrayList<String[]>> ipUserListMap = (HashMap<String,ArrayList<String[]>>)JsonUtil.objectFromJson(studio,new com.fasterxml.jackson.core.type.TypeReference<HashMap<String,ArrayList<String[]>>>(){});
	
	//ip#phonetype --> userName,userName,userName
	HashMap<String,ArrayList<String>> ipPhoneType2GongZuoShi = new HashMap<String,ArrayList<String>>();
	
	Iterator<String> it = ipUserListMap.keySet().iterator();
	while(it.hasNext()){
		String ip = it.next();
		ArrayList<String[]> userList = ipUserListMap.get(ip);
		
		for(String[] usernamePhonetype : userList){
			String phoneType = usernamePhonetype[1];
			String username = usernamePhonetype[0];
			
			String key = ip+"#"+phoneType;
			ArrayList<String> al = ipPhoneType2GongZuoShi.get(key);
			if(al == null){
				al = new ArrayList<String>();
				ipPhoneType2GongZuoShi.put(key,al);
			}
			if(al.contains(username) == false)
				al.add(username);
		}
	}
		
	it = ipPhoneType2GongZuoShi.keySet().iterator();
	while(it.hasNext()){
			String key = it.next();

			ArrayList<String> al = ipPhoneType2GongZuoShi.get(key);
			String ss[] = key.split("#",2);
			String ip = ss[0];
			String phoneType = ss[1];
			DajingStudio ds = getDajingStudio(dsManager,ip);
			if(ds != null){
				DajingGroup dg = ds.getDajingGroup(phoneType);
				if(dg != null){
					for(int i = 0 ; i < al.size() ; i++){
						String username = al.get(i);
						if(dg.usernameList.contains(username) == false){
							dg.usernameList.add(username);
							duokaiMessage.add("[添加工作组新成员] [成功] ["+ds.ip+"] ["+dg.phoneType+"] ["+username+"]");
						}
					}
				}else{
					dg = new DajingGroup();
					dg.phoneType = phoneType;
					for(int i = 0 ; i < al.size() ; i++){
						String username = al.get(i);
						dg.usernameList.add(username);
					}
					duokaiMessage.add("[添加工作组] [成功] ["+ds.ip+"] ["+dg.phoneType+"] ["+dg.usernameList+"]");
					ds.groupList.add(dg);
				}
			}else{
				//判断是否存在工作室换ip的情况，
				HashMap<DajingStudio,Integer> map2 = new HashMap<DajingStudio,Integer>();
				for(int i = 0 ; i < al.size() ; i++){
					String username = al.get(i);
					DajingStudio d = player2DajingMap.get(username);
					//dsManager.getDajingStudioByPlayer(p);
					if(d != null){
						Integer k = map2.get(d);
						if(k == null) k = 1;
						else k++;
						map2.put(d,k);
					}
				}
				DajingStudio maxCountDs = null;
				int maxC = 0;
				Iterator<DajingStudio> it2 = map2.keySet().iterator();
				while(it2.hasNext()){
					DajingStudio d = it2.next();
					Integer c = map2.get(d);
					if(c >=2 && c > maxC){
						maxCountDs = d;
						maxC = c;
					}
				}
				if(maxCountDs != null){
					ds = maxCountDs;
					
					dsManager.dajingMap.remove(ds.ip);
					ds.ip = ds.ip+","+ip;
					dsManager.dajingMap.put(ds.ip,ds);
					
					DajingGroup dg = ds.getDajingGroup(phoneType);
					if(dg != null){
						for(int i = 0 ; i < al.size() ; i++){
							String username = al.get(i);
							if(dg.usernameList.contains(username) == false){
								dg.usernameList.add(username);
								duokaiMessage.add("[添加工作组新成员] [成功] ["+ds.ip+"] ["+dg.phoneType+"] ["+username+"]");
							}
						}
					}else{
						dg = new DajingGroup();
						dg.phoneType = phoneType;
						for(int i = 0 ; i < al.size() ; i++){
							String username= al.get(i);
							dg.usernameList.add(username);
						}
						duokaiMessage.add("[添加工作组] [成功] ["+ds.ip+"] ["+dg.phoneType+"] ["+dg.usernameList+"]");
						ds.groupList.add(dg);
					}
				}else{
					ds = new DajingStudio();
					ds.ip = ip;
					DajingGroup dg = new DajingGroup();
					dg.phoneType = phoneType;
					for(int i = 0 ; i < al.size() ; i++){
						String username = al.get(i);
						dg.usernameList.add(username);
					}
					duokaiMessage.add("[添加工作室] [成功] ["+ds.ip+"] ["+dg.phoneType+"] ["+dg.usernameList+"]");
					ds.groupList.add(dg);
					
					dsManager.dajingMap.put(ip,ds);
				}
			}
		
		
	}
	dsManager.saveForManual();
	
	for(String s : duokaiMessage){
		out.println(s);
	}
}
%>
