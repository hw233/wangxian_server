package com.xuanzhi.tools.text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 词汇过滤器，过滤一些敏感词汇、色情词汇等等
 * 过滤词汇文本格式：
 * 	词汇\t级别
 * 如:
 *	江core	0
 *
 */
public class WordFilter {
	protected static WordFilter m_self = null;

	protected static final Log log = LogFactory.getLog(WordFilter.class.getName());
	
	private String filterFile;
	
	private HashMap<String, List<String>> wordMap = null;
	
	private HashMap<String, Integer> wordClassMap = null; 
	
	private HashMap<String, List<String>> rwordMap = null;
	
	private HashMap<String, Integer> rwordClassMap = null; 
	
	private String words = "";
	
	private String starts[] = null;

	public static WordFilter getInstance() {
		return m_self;
	}
    
	public void initialize() throws Exception{
		long now = System.currentTimeMillis();
    	wordMap = new HashMap<String, List<String>>();
    	rwordMap = new HashMap<String, List<String>>();
    	wordClassMap =  new HashMap<String, Integer>();
    	rwordClassMap =  new HashMap<String, Integer>();
        File file = new File(filterFile);
    	loadFile(file);	
        MyWatchDog dog = new MyWatchDog();
        dog.setDaemon(true);
        dog.setName("WordFilter-Watchlog");
        dog.addFile(file);
        dog.start();
		m_self = this;
		System.out.println("[系统初始化] [词汇过滤器] [初始化完成] [starts:"+starts.length+"] ["+getClass().getName()+"] [耗时："+(System.currentTimeMillis() - now)+"毫秒]");
		log.info("[系统初始化] [词汇过滤器] [初始化完成] [starts:"+starts.length+"] ["+getClass().getName()+"] [耗时："+(System.currentTimeMillis() - now)+"毫秒]");
	}
	
    public class MyWatchDog extends com.xuanzhi.tools.watchdog.FileWatchdog {
	    public void doOnChange(File file) {
	        try {
	            loadFile(file);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
    }
	
	private void loadFile(File filterFile) {
		HashMap<String, List<String>> _wordMap = new HashMap<String, List<String>>();
		HashMap<String, List<String>> _rwordMap = new HashMap<String, List<String>>();
		HashMap<String, Integer> _wordClassMap =  new HashMap<String, Integer>();
		HashMap<String, Integer> _rwordClassMap =  new HashMap<String, Integer>();
		HashSet<String> lineset = new HashSet<String>();
		int linenum = 0;
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filterFile));
			int count = 0;
			StringBuffer wordSb = new StringBuffer();
			while((line = br.readLine()) != null) 
			{
				linenum++;
				line = line.trim().toLowerCase();
				if(lineset.contains(line)) {
					continue;
				}
				lineset.add(line);
				String str[] = line.split("\\t");
				if(str.length == 2) {
					++count;
					wordSb.append(str[0] + "\n");
					String startW  = str[0].substring(0,1);
					List<String> existsW = _wordMap.get(startW);
					if(existsW == null) {
						existsW = new ArrayList<String>();
					}
					existsW.add(str[0].trim());
					_wordMap.put(startW, existsW);
					List<String> existsRW = _rwordMap.get(startW);
					if(existsRW == null) {
						existsRW = new ArrayList<String>(); 
					}
					StringBuffer sb = new StringBuffer();
					for(int i=0; i<str[0].length(); i++) {
						sb.append(str[0].substring(i, i+1));
						if(i < str[0].length() - 1) {
							sb.append(".{0,5}");
						} else {
							if(i != 1) {
								sb.append(".*");
							}
						}
					}
					existsRW.add(sb.toString());
					_rwordMap.put(startW, existsRW);
					_wordClassMap.put(str[0].trim(), Integer.parseInt(str[1].trim()));
					_rwordClassMap.put(sb.toString(), Integer.parseInt(str[1].trim()));
					log.info("[add] [start:"+startW+"] [word:"+str[0].trim()+"] [class:"+Integer.parseInt(str[1].trim())+"]");
				} else {
					log.info("[此行格式错误] [num:"+linenum+"] ["+line+"] [len:"+str.length+"]");
				}
			}
			br.close();
	    	wordMap = _wordMap;
	    	rwordMap = _rwordMap;
	    	wordClassMap =  _wordClassMap;
	    	rwordClassMap =  _rwordClassMap;
			starts = wordMap.keySet().toArray(new String[0]);
			words = wordSb.toString();
			lineset.clear();
			log.info("[load] [starts:"+starts.length+"] ["+count+"]");
		} catch(Exception e) {
			System.out.println("[WordFilter初始化发生错误] [行数:"+linenum+"] ["+line+"]");
			e.printStackTrace();
			log.error(StringUtil.getStackTrace(e));
		}
	}
	
	/**
	 * 判断内容是否合法
	 * 包含测试，并且包含的字符级别必须小于等于指定的级别。
	 * 并且把非法内容替换掉
	 * 
	 * @param content
	 * @param wordClass	级别, 适合于需要分级是的情形，此数值必须大于等于屏蔽词配置的级别才生效
	 * @return
	 */
	public String nvalidAndReplace(String content, int wordClass,String replaceContent){
		ArrayList<String> al = new ArrayList<String>();
		try {
	    	int calCount = 0;
	    	for(String start : starts) {
	    		String _content = new String(content).toLowerCase();
	    		int index = _content.indexOf(start);
				List<String> words = wordMap.get(start);
	    		while(index != -1) {
	    			_content = _content.substring(index);
	    			for(String word : words) {
	    				++calCount;
	    				int wc = wordClassMap.get(word)==null?0:wordClassMap.get(word);
	    				if(_content.startsWith(word) &&  wc <= wordClass) {
	    					//匹配到了：start + word
	    					if(!al.contains(word)) {
	    						al.add(word);
	    					}
	    					if(log.isDebugEnabled()) {
	    						log.debug("[匹配敏感词] [内容:"+content+"] [当前开始:"+_content+"] [敏感词:"+word+"]");
	    					}
	    				} else if(_content.startsWith(word)) {
	    					if(log.isDebugEnabled()) {
	    						log.debug("[匹配敏感词但是class不够] [内容:"+content+"] [当前开始:"+_content+"] [敏感词:"+word+"] [wc:"+wc+"] [class:"+wordClass+"]");
	    					}
	    				}
	    			}
	    			_content = _content.substring(1);
	    			index = _content.indexOf(start);
	    		}
	    	}
	    	
	    	for(int i = 0 ; i < al.size() ; i++){
		    	StringBuffer sb = new StringBuffer();
		    	String _content = new String(content).toLowerCase();
	    		String w = al.get(i);
	    		int index = -1;
	    		while((index = _content.indexOf(w)) != -1) {
	    			sb.append(content.substring(0, index));
	    			sb.append(replaceContent);
	    			_content = _content.substring(index+w.length());
	    			content = content.substring(index+w.length());
	    		}
	    		sb.append(content);
	    		content = sb.toString();
	    	}
	    	if(al.size() == 0) {
	    		if(log.isDebugEnabled()) {
					log.debug("[未匹配敏感词] [内容:"+content+"] [检索次数:"+calCount+"]");
	    		}
	    	}
	    	return content;
	    	
		} catch(Exception e) {
			e.printStackTrace();
			return content;
		}
	}
	
	/**
	 * 判断内容是否合法
	 * 包含测试，并且包含的字符级别必须小于等于指定的级别。
	 * 
	 * @param content
	 * @param wordClass	级别, 适合于需要分级是的情形，此数值必须大于等于屏蔽词配置的级别才生效
	 * @return
	 */
	public boolean nvalid(String content, int wordClass) {
		try {
	    	long now = System.currentTimeMillis();
	    	int calCount = 0;
	    	for(String start : starts) {
	    		String _content = new String(content);
	    		int index = _content.indexOf(start);
				List<String> words = wordMap.get(start);
	    		while(index != -1) {
	    			_content = _content.substring(index);
	    			for(String word : words) {
	    				++calCount;
	    				int wc = wordClassMap.get(word)==null?0:wordClassMap.get(word);
	    				if(_content.startsWith(word) &&  wc <= wordClass) {
	    					log.info("[filter] [full] [invalid] ["+word+"] ["+wc+"] [input:"+wordClass+"] ["+calCount+" times_caled] ["+(System.currentTimeMillis()-now)+"]");
	    					return false;
	    				}
	    			}
	    			_content = _content.substring(1);
	    			index = _content.indexOf(start);
	    		}
	    	}
	    	log.info("[filter] [full] [valid] [input:"+content+"] ["+calCount+" times_caled] ["+(System.currentTimeMillis()-now)+"]");
	    	return true;
		} catch(Exception e) {
			log.error(StringUtil.getStackTrace(e));
			return false;
		}
	}
	
	/**
	 * 判断内容是否合法，包含测试

	 * 包含测试，并且包含的字符级别必须等于指定的级别。
	 * @param content
	 * @param wordClass	级别, 适合于需要分级是的情形
	 * @return
	 */
	public boolean cvalid(String content, int wordClass) {
		try {
	    	long now = System.currentTimeMillis();
	    	content = content.toLowerCase();
	    	int calCount = 0;
	    	for(String start : starts) {
	    		String _content = new String(content.toLowerCase());
	    		int index = _content.indexOf(start);
				List<String> words = wordMap.get(start);
	    		while(index != -1) {
	    			_content = _content.substring(index);
	    			for(String word : words) {
	    				++calCount;
	    				int wc = wordClassMap.get(word)==null?0:wordClassMap.get(word);
	    				if(wc == wordClass && _content.startsWith(word)) {
	    					log.info("[过滤词汇] [非法] [包含过滤词] ["+word+"] ["+wc+"] ["+calCount+" times_caled] ["+content+"] ["+(System.currentTimeMillis()-now)+"]");
	    					return false;
	    				}
	    			}
	    			_content = _content.substring(1);
	    			index = _content.indexOf(start);
	    		}
	    	}
	    	log.info("[过滤词汇] [包含测试] [正常] [input:"+content+"] ["+calCount+" times_caled] ["+(System.currentTimeMillis()-now)+"]");
	    	return true;
		} catch(Exception e) {
			log.error(StringUtil.getStackTrace(e));
			return false;
		}
	}
	
	/**
	 * 判断内容是否合法，等于测试
	 * 等于测试，词汇和敏感词汇必须一致。级别也一致。
	 * 
	 * @param content
	 * @param wordClass	级别, 适合于需要分级是的情形
	 * @return
	 */
	public boolean evalid(String content, int wordClass) {
		try {
	    	long now = System.currentTimeMillis();
	    	content = content.toLowerCase();
	    	int calCount = 0;
	    	for(String start : starts) {
	    		String _content = new String(content);
	    		int index = _content.indexOf(start);
				List<String> words = wordMap.get(start);
	    		if(index == 0) {
	    			for(String word : words) {
	    				++calCount;
	    				int wc = wordClassMap.get(word)==null?0:wordClassMap.get(word);
	    				if(wc == wordClass && _content.equals(word)) {
	    					log.info("[过滤词汇] [非法] [等于滤词] ["+word+"] ["+wc+"] ["+content+"] ["+calCount+" times_caled] ["+(System.currentTimeMillis()-now)+"]");
	    					return false;
	    				}
	    			}
	    			_content = _content.substring(1);
	    			index = _content.indexOf(start);
	    		}
	    	}
	    	log.info("[过滤词汇] [等于测试] [正常] [input:"+content+"] ["+calCount+" times_caled] ["+(System.currentTimeMillis()-now)+"]");
	    	return true;
		} catch(Exception e) {
			log.error(StringUtil.getStackTrace(e));
			return false;
		}
	}
	
	/**
	 * 判断内容是否合法，采用模糊匹配的方式
	 * @param content
	 * @param wordClass	级别, 适合于需要分级是的情形
	 * @return
	 */
	public boolean rvalid(String content, int wordClass) {
		try {
	    	long now = System.currentTimeMillis();
	    	content = content.toLowerCase();
	    	//String keys[] = new String[]{"^","$","?","*","+",",","|","!","\\","[","]","(",")","{","}","-"};
	    	content = content.replaceAll("[$]", "\\\\\\$"); 
	    	int calCount = 0;
	    	for(String start : starts) {
	    		String _content = new String(content);
	    		int index = _content.indexOf(start);
				List<String> words = rwordMap.get(start);
	    		while(index != -1) {
	    			_content = _content.substring(index);
	    			for(String word : words) {
	    				++calCount;
	    				int wc = rwordClassMap.get(word);
	    				if(_content.matches(word) &&  wc <= wordClass) {
	    					log.info("[filter] [regex] [invalid] ["+word+"] ["+wc+"] [input:"+wordClass+"] ["+calCount+" times_caled] ["+(System.currentTimeMillis()-now)+"]");
	    					return false;
	    				}
	    			}
	    			_content = _content.substring(1);
	    			index = _content.indexOf(start);
	    		}
	    	}
	    	log.info("[filter] [匹配测试] [valid] [input:"+content+"] ["+calCount+" times_caled] ["+(System.currentTimeMillis()-now)+"]");
	    	return true;
		} catch(Exception e) {
			log.error(StringUtil.getStackTrace(e));
			return false;
		}
	}
	
	/**
	 * 判断内容是否合法
	 * @param content
	 * @return
	 */
	public boolean nvalid(String content) {
		return nvalid(content, 0);
	}
	
	/**
	 * 判断内容是否合法，采用模糊匹配的方式
	 * @param content
	 * @return
	 */
	public boolean rvalid(String content) {
		return rvalid(content, 0);
	}

	public String getFilterFile() {
		return filterFile;
	}

	public void setFilterFile(String filterFile) {
		this.filterFile = filterFile;
	}
	
	public String getWords() {
		return words;
	}
	
	public void setWords(String words) {
		this.words = words;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filterFile));
			bw.write(words);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

