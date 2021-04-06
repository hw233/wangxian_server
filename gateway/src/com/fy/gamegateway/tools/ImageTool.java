package com.fy.gamegateway.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageTool {
	
	// 给定范围获得一个随机颜色
	private static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	/**
	 * 对图片的byte[]加密，公式为 byte[i] = byte[i] + (i*i + 5 * i + 3)
	 * @param b
	 * @return
	 */
	public static byte[] jiaMiImageByte (byte[] b) {
		byte[] re = new byte[b.length];
		for (int i = 0; i < b.length; i++) {
			re[i] = (byte)(b[i] + (i * i + 5 * i + 3));
		}
		return re;
	}
	
	public static byte[] buildPng(String value) {
		return bulidPng(value, 256, 64);
	}

	public static byte[] bulidPng(String value, int width, int height) {
		try{
			Random random = new Random();
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = (Graphics2D)image.getGraphics();// 获取图形上下文
			graphics.setColor(getRandColor(180, 250));// 设定背景色(最多色调255)
			graphics.fillRect(0, 0, width, height);
			Font font = new Font("Arial Bold", Font.BOLD, 30);
			graphics.setFont(font);
			int strX = 20 + random.nextInt(30);
			int strH = (int)font.getStringBounds("1", graphics.getFontRenderContext()).getHeight();
			for (int i = 0; i < value.length(); i++) {
				String one = value.substring(i, i+1);
				graphics.setColor(getRandColor(40, 140));
				graphics.drawString(one, strX  , height/2 + strH/2);
				strX += font.getStringBounds(one, graphics.getFontRenderContext()).getWidth() + 5;
			}
			for (int i = 0; i < 50; i++) {
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				graphics.setColor(getRandColor(40, 140));
				graphics.drawOval(x, y, 1, 1);
			}
			graphics.dispose();// 释放图形上下文
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", bo);
			
			byte[] b = bo.toByteArray();
			bo.flush();
			bo.close();
			image.flush();
			return b;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
