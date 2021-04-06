package com.fy.engineserver.observe;



import java.util.HashSet;

import com.fy.engineserver.core.LivingObject;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.RequestMessage;

public class Observer {

	protected String name;
	
	protected String game;
	
	protected Connection conn;
	
	protected int x,y,viewWidth,viewHeight;
	
	public HashSet<LivingObject> viewMap = new HashSet<LivingObject>();
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getViewWidth() {
		return viewWidth;
	}

	public void setViewWidth(int viewWidth) {
		this.viewWidth = viewWidth;
	}

	public int getViewHeight() {
		return viewHeight;
	}

	public void setViewHeight(int viewHeight) {
		this.viewHeight = viewHeight;
	}

	public HashSet<LivingObject> getViewMap() {
		return viewMap;
	}

	public void setViewMap(HashSet<LivingObject> viewMap) {
		this.viewMap = viewMap;
	}

	ObserveSubSystem oss;
	public Observer(ObserveSubSystem oss,String name,Connection conn){
		this.name = name;
		this.conn = conn;
		this.oss = oss;
	}
	
	
	public void sendMessage(RequestMessage req){
		oss.sendMessage(conn, req, "");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

}
