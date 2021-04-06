package com.xuanzhi.tools.text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;

/**
 *
 * 
 */
public class XmlContentExporter {

	private String[] files = null;
	
	private List<String> contentList = new ArrayList<String>();
	
	public XmlContentExporter(String files[]) {
		this.files = files;
	}
	
	public void exportData() throws Exception  {
		for(String xml : files) {
			File ff = new File(xml);
			if(ff.isFile()) {
				Configuration conf = new DefaultConfigurationBuilder().buildFromFile(xml);
				recursiveExport(conf);
			} else if(ff.isDirectory()) {
				List<File> flist = new ArrayList<File>();
				getAllXmlFiles(ff, flist);
				for(int i=0; i<flist.size(); i++) {
					Configuration conf = new DefaultConfigurationBuilder().buildFromFile(flist.get(i));
					recursiveExport(conf);
				}
			}
		}
	}
	
	private void getAllXmlFiles(File dir, List<File> fileList) {
		File fs[] = dir.listFiles();
		for(int i=0; i<fs.length; i++) {
			if(fs[i].isFile() && fs[i].getName().toLowerCase().endsWith(".xml")) {
				fileList.add(fs[i]);
			} else if(fs[i].isDirectory()) {
				getAllXmlFiles(fs[i], fileList);
			}
		}
	}
	
	public void recursiveExport(Configuration config) {
		String value = config.getValue(null);
		
		if(value != null && value.length() > 0 && value.length() != value.getBytes().length) {
			if(!contentList.contains(value)) {
				contentList.add(value);
			}
		}
		
		String names[] = config.getAttributeNames();
		for(int i=0; i<names.length; i++) {
			value = config.getAttribute(names[i], null);
			if(value != null && value.length() > 0 && value.length() != value.getBytes().length) {
				if(!contentList.contains(value)) {
					contentList.add(value);
				}
			}
		}
		
		Configuration children[] = config.getChildren();
		for(int i=0; i<children.length; i++) {
			recursiveExport(children[i]);
		}
	}

	public List<String> getContentList() {
		return contentList;
	}
	
	public static void main(String args[]) {
		String files[] = new String[]{
					"D:/gamepj/webgame_server/conf/article.xml",
					"D:/gamepj/webgame_server/conf/bingzhong.xml",
					"D:/gamepj/webgame_server/conf/building.xml",
					"D:/gamepj/webgame_server/conf/chengfang.xml",
					"D:/gamepj/webgame_server/conf/farm.xml",
					"D:/gamepj/webgame_server/conf/huodong.xml",
					"D:/gamepj/webgame_server/conf/nation.xml",
					"D:/gamepj/webgame_server/conf/officer.xml",
					"D:/gamepj/webgame_server/conf/openCity.xml",
					"D:/gamepj/webgame_server/conf/resourceBase.xml",
					"D:/gamepj/webgame_server/conf/target.xml",
					"D:/gamepj/webgame_server/conf/task.xml"
		};
		XmlContentExporter exporter = new XmlContentExporter(files);
		try {
			exporter.exportData();
			List<String> list = exporter.getContentList();
			for(int i=0; i<list.size(); i++) {
				System.out.println(list.get(i));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
