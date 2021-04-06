package com.xuanzhi.tools.filesystem;

import java.util.ArrayList;

public class DBTree {

	ArrayList<DataBlock> dbList = new ArrayList<DataBlock>();
	
	public int size(){
		return dbList.size();
	}
	
	public DataBlock findNearestBigger(DataBlock db){
		for(int i = 0 ; i < dbList.size() ; i++){
			DataBlock d = dbList.get(i);
			if(d.length < db.length){
				continue;
			}else{
				return d;
			}
		}
		return null;
	}
	
	public void insert(DataBlock db){
		int biggerIndex = -1;
		for(int i = 0 ; i < dbList.size() ; i++){
			DataBlock d = dbList.get(i);
			if(d.length < db.length){
				continue;
			}else{
				biggerIndex = i;
				break;
			}
		}
		if(biggerIndex == -1){
			dbList.add(db);
		}else{
			dbList.add(biggerIndex, db);
		}
	}
	
	public void remove(DataBlock db){
		dbList.remove(db);
	}
	
	public boolean contains(DataBlock db){
		return dbList.contains(db);
	}
	
	public void clear(){
		dbList.clear();
	}
	
}
