package com.fy.boss.operation.tools;

import com.fy.boss.operation.model.GameActivity;

public class Coverter4GameActivity {

	public static String gameActivitys2EventJson(GameActivity[] gameActivitys)
	{
		
		/**
		 * 		var myevents = [
									{
										title:'demo',
										start:new Date(),
										end:new Date(),
										allDay:false,
										color:'red'
									},
									
									{
										title: 'Lunch',
										start: new Date(y, m-1, d, 12, 0),
										end: new Date(y, m+1, d+2, 18, 0),
										allDay: false
									},
									
									{
										title: 'Lasdfasunch',
										start: new Date(y, m-1, d, 12, 0),
										end: new Date(y, m, d+2, 18, 0),
										allDay: false
									}
					];
		 */
		
		String eventArrayJsonHeader = "[\n\t";
		
		String eventArrayJsonBody = "";
		for(int i=0; i<gameActivitys.length; i++)
		{
			eventArrayJsonBody += gameActivity2EventJson(gameActivitys[i]);
			if(i != gameActivitys.length-1)
			{
				eventArrayJsonBody += ",";
			}
		}
		
		String eventArrayJsonTail = "\n];";
		
		return eventArrayJsonHeader+eventArrayJsonBody+eventArrayJsonTail;
	}
	
	public static String gameActivity2EventJson(GameActivity gameActivity)
	{
		String title = "title:";
		String start = "start:";
		String end = "end:";
		String allDay = "allDay:";
		String color = "color:";
		String url = "url:";
		
		title += "'"+gameActivity.getActivityName()+"'";
		start += "new Date("+gameActivity.getStartTime()+")";
		end += "new Date("+gameActivity.getEndTime()+")";
		allDay += "false";
		color += "'"+gameActivity.getColor()+"'";
		url += "'gameActivityDetail.jsp?id="+gameActivity.getId()+"'";
		
		return "{\n\t\t"+title+",\n\t\t"+start+",\n\t\t"+end+",\n\t\t"+allDay+",\n\t\t"+color+",\n\t\t"+url+"\n\t}";
	}

	public static void main(String[] args) {
		
		GameActivity gameActivity = new GameActivity();
		
		gameActivity.setActivityName("2233");
		gameActivity.setStartTime(System.currentTimeMillis());
		gameActivity.setEndTime(System.currentTimeMillis()+10000l);
		gameActivity.setColor("0000AC");
		
		GameActivity[] gameActivitys = new GameActivity[1];
		gameActivitys[0] = gameActivity;
		System.out.println(gameActivitys2EventJson(gameActivitys));
		
	}
	
}
