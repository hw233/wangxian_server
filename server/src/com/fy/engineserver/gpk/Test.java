package com.fy.engineserver.gpk;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test {
	public static void main(String[] args) {
		ImageTool it=new ImageTool();
		BufferedImage bi=it.creatImage(64,32);
		
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		
		try {
			ImageIO.write(bi, "png", new File("a.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		baos.toByteArray();
	}
	
	public static byte[] getImageData(){
		ImageTool it=new ImageTool();
		BufferedImage bi=it.creatImage(64,24);
		
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		
		try {
			ImageIO.write(bi, "png", baos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
}
