package com.sqage.stat.language;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.watchdog.ConfigFileChangedAdapter;
import com.xuanzhi.tools.watchdog.ConfigFileChangedListener;


/**
 *
 * @author Frank E-mail:chudaping＠hotmail.com
 * @version 创建时间：Jul 15, 2011 11:07:15 AM
 * 
 */
public class MultiLanguageManager implements ConfigFileChangedListener {
	
//	public static Logger logger = Logger.getLogger(MultiLanguageManager.class.getName());
public	static Logger logger = LoggerFactory.getLogger(MultiLanguageManager.class.getName());
	
	/**
	 * 内容翻译文件
	 */
	private String lanMapFile;
	
	private boolean translate;
	
	public static HashMap<String, String> translateMap;
	
	public static MultiLanguageManager self;
	
	public static MultiLanguageManager getInstance() {
		return self;
	}
	
	public void init() {
		if(translate) {
			File f = new File(lanMapFile);
			if(f.isFile()) {
				readFromFile(f);
				ConfigFileChangedAdapter adapter = ConfigFileChangedAdapter.getInstance();
				adapter.addListener(f, this);
			} else {
				throw new RuntimeException("[初始化MultiLanguageManager失败] [国际化文件找不到] ["+lanMapFile+"]");
			}
			Class<Translate> clazz = com.sqage.stat.language.Translate.class;
			Field fields[] = clazz.getFields();
			Translate obj = new Translate();
			for(Field fi : fields) {
				if(fi.getType() == String.class) {
					try {
						String value = (String)fi.get(obj);
						String newValue = translateMap.get(value);
						if(newValue != null) {
							fi.set(obj, newValue);
							if(logger.isInfoEnabled())
								logger.info("[翻译Translate.java] [Field:{}] [{}] [{}]", new Object[]{fi.getName(),value,newValue});
						}
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		self = this;
		System.out.println("初始化MultiLanguageManager成功] ["+this.getClass().getName()+"]");
	}
	
	private void readFromFile(File file) {
		translateMap = new HashMap<String,String>();
		
		
		BufferedReader bufferedreader;
		try {
			bufferedreader = new BufferedReader(new InputStreamReader(new  FileInputStream(file.getPath()),"UTF-8"));
		    String instring=null;
		    while((instring=bufferedreader.readLine())!=null){
		    	//System.out.println(instring+"\n");
		    	 String substr=instring;
		    	 String ss[] = substr.split("\t");
					if(ss.length == 2) {
						translateMap.put(ss[0].trim(), ss[1].trim());
						if(logger.isInfoEnabled())
							logger.info("[{}] [{}]", new Object[]{ss[0].trim(),ss[1].trim()});
		    }
		    }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		    
		
		
	//String content = FileUtils.readFile(file.getPath());
//		String s[];
//		try {
//			s = new String(content.getBytes(),"utf-8").replace("\r","\n").split("\n");
//		
//		for(int i=0; i<s.length; i++) {
//			String ss[] = s[i].split("\t");
//			if(ss.length == 2) {
//				translateMap.put(ss[0].trim(), ss[1].trim());
//				if(logger.isInfoEnabled())
//					logger.info("[{}] [{}]", new Object[]{ss[0].trim(),ss[1].trim()});
//			}
//		}
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public String getLanMapFile() {
		return lanMapFile;
	}

	public void setLanMapFile(String lanMapFile) {
		this.lanMapFile = lanMapFile;
	}

	public boolean isTranslate() {
		return translate;
	}

	public void setTranslate(boolean translate) {
		this.translate = translate;
	}

	@Override
	public void fileChanged(File file) {
		// TODO Auto-generated method stub
		readFromFile(file);
	}

	public HashMap<String, String> getTranslateMap() {
		return translateMap;
	}

	public InputStream translateXml(InputStream stream)  throws Exception {
		Configuration conf = new DefaultConfigurationBuilder().build(stream);
		recursiveTranslate(conf);
		DefaultConfigurationSerializer s = new DefaultConfigurationSerializer();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		s.serialize(out, conf);
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		return in;
	}
	
	public void recursiveTranslate(Configuration config) {
		String value = config.getValue(null);
		
		if(value != null && value.length() > 0) {
			String trans = translateMap.get(value);
			if(trans != null) {
				config.setValue(trans);
			}
		}
		
		String names[] = config.getAttributeNames();
		for(int i=0; i<names.length; i++) {
			value = config.getAttribute(names[i], null);
			if(value != null && value.length() > 0) {
				String trans = translateMap.get(value);
				if(trans != null) {
					config.setAttribute(names[i], trans);
				}
			}
		}
		
		Configuration children[] = config.getChildren();
		for(int i=0; i<children.length; i++) {
			recursiveTranslate(children[i]);
		}
	}
	
	public static void main(String args[]) {
		MultiLanguageManager mm = new MultiLanguageManager();
		mm.setLanMapFile("d:/workspace/sanguo_new/webgame_server/conf/language.txt");
		mm.setTranslate(true);
		mm.init();
	}
}
