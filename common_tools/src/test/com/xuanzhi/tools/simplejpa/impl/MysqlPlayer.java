package com.xuanzhi.tools.simplejpa.impl;

import java.util.ArrayList;
import java.util.HashMap;

import com.xuanzhi.tools.simplejpa.annotation.*;

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"username"}),
	@SimpleIndex(members={"playerName"}),
	@SimpleIndex(members={"levelOn2"}),
	@SimpleIndex(name="MyPlayer_xxxxx_idx",members={"level2","exp2","hp2","mp2"}),
})
public class MysqlPlayer {

	@SimpleId
	protected long id;
	
	@SimpleVersion
	protected int version;
	
	protected String username;
	
	protected String playerName;
	
	int levelOn2;
	int level2;
	int exp2;
	int hp2;
	int mp2;
	
	java.util.Date createTime;
	
	float x;
	float y;
	
	boolean isGM;
	
	protected ArrayList<String> list1 = new ArrayList<String>();
	
	@SimpleColumn(length=100000)
	protected HashMap<String,String> taskMap = new HashMap<String,String>();
}
