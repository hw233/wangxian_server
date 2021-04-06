package com.fy.engineserver.sprite;

public abstract class AbstractSummoned extends com.fy.engineserver.core.LivingObject {
// 召唤物的类型，如火球、闪电等
protected String effectType = "";

public void setEffectType(String value){
this.effectType = value;
}

public String getEffectType(){
return this.effectType;
}

// 召唤物的状态
protected byte state;

public void setState(byte value){
this.state = value;
}

public byte getState(){
return this.state;
}

// 召唤物面朝的方向
protected byte direction;

public void setDirection(byte value){
this.direction = value;
}

public byte getDirection(){
return this.direction;
}

// avata部件类型，比如:武器
protected transient byte[] avataType;

public void setAvataType(byte[] value){
this.avataType = value;
}

public byte[] getAvataType(){
return this.avataType;
}

// avata部件全名，比如:明月刀_人类_男
protected transient String[] avata;

public void setAvata(String[] value){
this.avata = value;
}

public String[] getAvata(){
return this.avata;
}

private transient boolean[] aroundMarks = new boolean[0];
public boolean[] getAroundMarks(){
 	return aroundMarks;
 }
public Object getValue(int id){
	switch(id){
	default :break;
	}
	return null;
}
public void setValue(int id, Object value) {
switch (id) {
 default :break;
}
}
public void clear(){
 	for(int i=aroundMarks.length-1;i>=0 ;i-- ){
aroundMarks[i]=false;
}
}
}
