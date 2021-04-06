package com.fy.engineserver.sprite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.datasource.language.TransferLanguage;
import com.xuanzhi.tools.text.XmlUtil;
import com.xuanzhi.tools.watchdog.ConfigFileChangedAdapter;
import com.xuanzhi.tools.watchdog.ConfigFileChangedListener;

public class PlayerUpgradeTips implements ConfigFileChangedListener{
	File dataFile;
	
	HashMap<Integer, String> tips;
	
	static PlayerUpgradeTips self;
	
	public PlayerUpgradeTips(){
		
	}
	
	public void init(){
		this.tips=new HashMap<Integer, String>();
		try {
			this.readFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConfigFileChangedAdapter.getInstance().addListener(this.dataFile,this);
		PlayerUpgradeTips.self=this;
	}
	
	public static PlayerUpgradeTips getInstance(){
		return PlayerUpgradeTips.self;
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}
	
	public String getTip(int level){
		if(this.tips.containsKey(level)){
			return this.tips.get(level);
		}else{
			return "";
		}
	}
	
	private void readFile() throws Exception{
		FileInputStream fis=null;
		try{
			fis=new FileInputStream(this.dataFile);
			Document doc=XmlUtil.load(fis);
			Element root=doc.getDocumentElement();
			Element[] tips=XmlUtil.getChildrenByName(root, "tip");
			for(Element tip:tips){
				int level=XmlUtil.getAttributeAsInteger(tip, "level");
				Element text=XmlUtil.getChildByName(tip, "text");
				this.tips.put(level, XmlUtil.getValueAsString(text, "", TransferLanguage.getMap()));
			}
		}finally{
			if(fis!=null){
				fis.close();
				fis=null;
			}
		}
	}

	public void fileChanged(File file) {
		// TODO Auto-generated method stub
		FileInputStream fis=null;
		try{
			this.tips.clear();
			fis=new FileInputStream(file);
			Document doc=XmlUtil.load(fis);
			Element root=doc.getDocumentElement();
			Element[] tips=XmlUtil.getChildrenByName(root, "tip");
			for(Element tip:tips){
				int level=XmlUtil.getAttributeAsInteger(tip, "level");
				Element text=XmlUtil.getChildByName(tip, "text");
				this.tips.put(level, XmlUtil.getValueAsString(text, "", TransferLanguage.getMap()));
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fis=null;
			}
		}
	}
}
