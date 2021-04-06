package com.fy.engineserver.gm.record;

public class FileType {
	
	/**
	 * image file
	 */
	public static final int IMAGE = 1;
	
	/**
	 * audio file
	 */
	public static final int AUDIO = 2;
	
	/**
	 * text file
	 */
	public static final int TEXT  = 3;
	
	/**
	 * video file
	 */
	public static final int VIDEO = 4;
	
	/**
	 * type name
	 */
	public static final String TYPE_NAMES[] = {"unknown","image","audio","text","video"};

	/**
	 * 从文件后缀得到类型
	 * @param suffix 后缀 gif,jpg,wmv,mp4
	 * @return
	 */
	public static int getType(String suffix)  {
		if(suffix.equalsIgnoreCase("gif") || 
				suffix.equalsIgnoreCase("jpg") ||
				suffix.equalsIgnoreCase("jpeg") ||
				suffix.equalsIgnoreCase("bmp") ||
				suffix.equalsIgnoreCase("tif")) {
			return IMAGE;
		}
		if(suffix.equalsIgnoreCase("mid") ||
				suffix.equalsIgnoreCase("mp3") ||
				suffix.equalsIgnoreCase("wav") ||
				suffix.equalsIgnoreCase("amr")) {
			return AUDIO;
		}
		if(suffix.equalsIgnoreCase("wmv") ||
				suffix.equalsIgnoreCase("avi") ||
				suffix.equalsIgnoreCase("3gp") ||
				suffix.equalsIgnoreCase("mp4") ||
				suffix.equalsIgnoreCase("rm") ||
				suffix.equalsIgnoreCase("flv")) {
			return VIDEO;
		}
		if(suffix.equalsIgnoreCase("txt") ||
				suffix.equalsIgnoreCase("log") ||
				suffix.equalsIgnoreCase("xml") ||
				suffix.equalsIgnoreCase("xconf")) {
			return TEXT;
		}
		return -1;
	}

}
