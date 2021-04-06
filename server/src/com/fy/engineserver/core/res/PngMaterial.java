package com.fy.engineserver.core.res;

import java.io.DataInputStream;

/**
 * 素材:1张图片，许多切片
 * @author Administrator
 *
 */
public class PngMaterial extends AbstractResource{
	String name ="";
	

	public void load(DataInputStream is) throws Exception{		
		version = is.readInt();
		name = is.readUTF();		
		short nummod = is.readShort();
		for( int i=0;i< nummod;i++){
			is.readShort();
			is.readShort();
			is.readShort();
			is.readShort();			
		}
		is.readUTF();
	}
}
