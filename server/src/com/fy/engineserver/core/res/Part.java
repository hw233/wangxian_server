package com.fy.engineserver.core.res;

import java.io.DataInputStream;

public class Part extends AbstractResource implements Constants {
	public String name="";
	private String type="";
	private byte itype;

	@Override
	public void load(DataInputStream is) throws Exception {
		short num = is.readShort();
		String array[] = new String[num];
		for( int i=0;i< num;i++){
			array[i] = is.readUTF();
		}
		
		version = is.readInt();
		name = is.readUTF();	
		type = is.readUTF();
		itype = Appearance.getPartTypeInt(type);
		
		short numanim = is.readShort();
		for( int i=0;i< numanim;i++){
			// load Animation
			is.readUTF();
			is.readShort();
			short framenum = is.readShort();
			for( int j=0;j< framenum;j++){
				//loadframe
				is.readShort();
				short totalnum = is.readShort();
				for( int k=0;k< totalnum;k++){
					is.readByte();
					//load pngmodule object
					is.readShort();
					is.readShort();
					is.readShort();
					is.readShort();
					is.readByte();
				}
				
			}
			is.readShort();
			is.readShort();
			is.readShort();
			is.readShort();
			boolean hasproj = is.readBoolean();
			if( hasproj){
				is.readShort();
				is.readShort();
				is.readShort();
				is.readShort();
				is.readShort();
				is.readShort();				
			}		
		}
	}
	public String getPartType() {
		return type;
	}
	public byte getPartTypeInt(){
		return itype;
	}
}
