package com.fy.engineserver.closebetatest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xuanzhi.tools.text.XmlUtil;

public class TestClass {
	public static HashMap<String,String> accountNumberMap = new HashMap<String,String>();
	public static int accountNum = 0;
	private void readAccountNumberMapXML(File file) throws Exception{

		if(file != null && file.isFile() && file.exists()){
			Document dom = XmlUtil.load(file.getAbsolutePath());
			Element root = dom.getDocumentElement();
			Element eles[] = XmlUtil.getChildrenByName(root, "player");
			if(eles == null){
				return;
			}
			accountNum = XmlUtil.getAttributeAsInteger(root, "accountNum",0);
			accountNumberMap = new HashMap<String,String>();
			for(int i = 0 ; i < eles.length ; i++){
				Element e = eles[i];
//				String key = null;
				String key = XmlUtil.getValueAsString(e, null);
//				NodeList nl = e.getChildNodes();
//				int max = nl.getLength();
//				for (int m = 0; m < max; m++) {
//					Node n = nl.item(m);
//					System.out.println(n.getNodeType()+"  "+m+(n.getNodeType() == Node.CDATA_SECTION_NODE));
//					if (n.getNodeType() == Node.TEXT_NODE) {
//						System.out.println(n.getNodeValue());
//						key = n.getNodeValue();
//					}
//				}
				String value = XmlUtil.getAttributeAsString(e, "value",null, null);
				if(key != null && value != null)
					accountNumberMap.put(key, value);
			}
			if(accountNumberMap.keySet() == null){
			}
		}
	}
	public static void main(String[] args){
		
//		TestClass tc = new TestClass();
//		File accountNumberMapFile = new File("C:\\Program Files\\SecureCRT\\download\\2.xml");
//		try {
//			tc.readAccountNumberMapXML(accountNumberMapFile);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String oneHoursAgoTime = "";
        Calendar cal = Calendar.getInstance();
        oneHoursAgoTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS")
                .format(cal.getTime());

	}
}
