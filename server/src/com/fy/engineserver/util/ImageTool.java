package com.fy.engineserver.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.newtask.service.TaskManager;

public class ImageTool {
	
	// 给定范围获得一个随机颜色
	private static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	public static byte[] buildPng(String value) {
		return bulidPng(value, 256, 64);
	}

	public static byte[] bulidPng(String value, int width, int height) {
		try{
			long now = System.currentTimeMillis();
			Random random = new Random();
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = (Graphics2D)image.getGraphics();// 获取图形上下文
			graphics.setColor(getRandColor(180, 255));// 设定背景色(最多色调255)
			graphics.fillRect(0, 0, width, height);
			Font font = new Font("Arial Bold", Font.BOLD, 45);
			graphics.setFont(font);
			int strX = 10 + random.nextInt(30);
			for (int i = 0; i < value.length(); i++) {
				int strY = height/2 + 5 + random.nextInt(20);
				String one = value.substring(i, i+1);
				graphics.setColor(getRandColor(40, 140));
				graphics.drawString(one, strX  , strY);
				strX += 25 + random.nextInt(20);
			}
			for (int i = 0; i < 200; i++) {
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				graphics.setColor(getRandColor(40, 140));
				int w = random.nextInt(3);
				int h = random.nextInt(3);
				graphics.drawOval(x, y, w, h);
			}
			
			for (int i = 0; i < 15; i++) {
				int x1 = random.nextInt(width);
				int x2 = random.nextInt(width);
				int y1 = random.nextInt(height);
				int y2 = random.nextInt(height);
				graphics.setColor(getRandColor(0, 255));
				graphics.drawLine(x1, y1, x2, y2);
			}
			
			graphics.dispose();// 释放图形上下文
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", bo);
			
			byte[] b = bo.toByteArray();
			bo.flush();
			bo.close();
			image.flush();
			EnterLimitManager.logger.warn("[答题图片生成消耗时间] ["+(System.currentTimeMillis() - now)+"ms]");
			return b;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
