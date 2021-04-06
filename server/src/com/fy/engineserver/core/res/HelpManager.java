package com.fy.engineserver.core.res;

import java.io.File;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.datasource.language.TransferLanguage;
import com.fy.gamebase.help.HelpMessage;
import com.xuanzhi.tools.text.XmlUtil;

public class HelpManager {
	private static HelpManager self ;
	public static HelpManager getInstance(){
		return self;
	}
	File helpWindowXml;
	
	class Screen{
		int width,height;
		HelpMessage[] data;
	};
	/**
	 * key: windowId
	 * value: Screen[]
	 */
	HashMap<String, Screen[]> helps = new HashMap<String, Screen[]>();
	
	public void init() throws Exception{
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		self = this;
		helps.clear();
		
		Document dom = XmlUtil.load(helpWindowXml.getAbsolutePath(),"utf-8");
		Element root = dom.getDocumentElement();

		Element eles[] = XmlUtil.getChildrenByName(root, "help");
		for (int i = 0; i < eles.length; i++) {
			Element eleHelp = eles[i];
			String id = XmlUtil.getAttributeAsString(eleHelp, "id", null);
			Element eleScreen[] = XmlUtil.getChildrenByName(eleHelp, "screen");
			Screen[] screen = new Screen[eleScreen.length];
			
			for( int j=0;j< eleScreen.length;j++){
				Element elescr = eleScreen[j];
				screen[j] = new Screen();
				screen[j].width = XmlUtil.getAttributeAsInteger(elescr, "width", 0);
				screen[j].height = XmlUtil.getAttributeAsInteger(elescr, "height", 0);
				
				Element[] func = XmlUtil.getChildrenByName(elescr, "function");
				screen[j].data = new HelpMessage[func.length];
				for( int k=0;k< func.length;k++){
					HelpMessage m = new HelpMessage();
					{
						Element rect = XmlUtil.getChildByName(func[k], "rect");
						m.setRectx((short)XmlUtil.getAttributeAsInteger(rect, "x", 0));
						m.setRecty((short)XmlUtil.getAttributeAsInteger(rect, "y", 0));
						m.setRectw((short)XmlUtil.getAttributeAsInteger(rect, "w", 0));
						m.setRecth((short)XmlUtil.getAttributeAsInteger(rect, "h", 0));
						m.setRectcolor(XmlUtil.getAttributeAsInteger(rect, "color", 0));
						m.setRectlinew((byte)XmlUtil.getAttributeAsInteger(rect, "linew", 0));
					}
					{
						Element[] lines = XmlUtil.getChildrenByName(func[k], "line");
						short x1[] = new short[lines.length];
						short x2[] = new short[lines.length];
						short y1[] = new short[lines.length];
						short y2[] = new short[lines.length];
						int color[] = new int[lines.length];
						byte linew[] = new byte[lines.length];
						for( int l = 0;l< lines.length;l++){
							x1[l] = (short)XmlUtil.getAttributeAsInteger(lines[l], "x1", 0);
							y1[l] = (short)XmlUtil.getAttributeAsInteger(lines[l], "y1", 0);
							x2[l] = (short)XmlUtil.getAttributeAsInteger(lines[l], "x2", 0);
							y2[l] = (short)XmlUtil.getAttributeAsInteger(lines[l], "y2", 0);
							color[l] = XmlUtil.getAttributeAsInteger(lines[l], "color", 0);
							linew[l] = (byte)XmlUtil.getAttributeAsInteger(lines[l], "linew", 0);
						}
						m.setLineX1(x1);
						m.setLineX2(x2);
						m.setLineY1(y1);
						m.setLineY2(y2);
						m.setLineColor(color);
						m.setLineW(linew);
					}
					{
						Element btn = XmlUtil.getChildByName(func[k], "btn");
						m.setBtnx((short)XmlUtil.getAttributeAsInteger(btn, "x", 0));
						m.setBtny((short)XmlUtil.getAttributeAsInteger(btn, "y", 0));
						m.setBtnText( XmlUtil.getAttributeAsString(btn, "text", null,TransferLanguage.getMap()));
						m.setBtnColor( XmlUtil.getAttributeAsInteger(btn, "color", 0));
						m.setBtnFontSize((short)XmlUtil.getAttributeAsInteger(btn, "fontsize", 14));
					}
					
					Element bubb = XmlUtil.getChildByName(func[k], "bubble");
					{
						Element[] lines = XmlUtil.getChildrenByName(bubb, "line");
						short x1[] = new short[lines.length];
						short x2[] = new short[lines.length];
						short y1[] = new short[lines.length];
						short y2[] = new short[lines.length];
						int color[] = new int[lines.length];
						byte linew[] = new byte[lines.length];									
						for( int l = 0;l< lines.length;l++){
							x1[l] = (short)XmlUtil.getAttributeAsInteger(lines[l], "x1", 0);
							y1[l] = (short)XmlUtil.getAttributeAsInteger(lines[l], "y1", 0);
							x2[l] = (short)XmlUtil.getAttributeAsInteger(lines[l], "x2", 0);
							y2[l] = (short)XmlUtil.getAttributeAsInteger(lines[l], "y2", 0);
							color[l] = XmlUtil.getAttributeAsInteger(lines[l], "color", 0);
							linew[l] = (byte)XmlUtil.getAttributeAsInteger(lines[l], "linew", 0);
						}
						m.setBubbleLineX1(x1);
						m.setBubbleLineY1(y1);
						m.setBubbleLineX2(x2);
						m.setBubbleLineY2(y2);
						m.setBubbleLineColor(color);
						m.setBubbleLineW(linew);
					}
					
					Element bubContent = XmlUtil.getChildByName(bubb, "content");
					{
						m.setBubbleTextColor(XmlUtil.getAttributeAsInteger(bubContent, "color", 0));
						m.setBubbleContentX( (short)XmlUtil.getAttributeAsInteger(bubContent, "x", 0));
						m.setBubbleContentY( (short)XmlUtil.getAttributeAsInteger(bubContent, "y", 0));
						m.setBubbleContentW( (short)XmlUtil.getAttributeAsInteger(bubContent, "w", 0));
						m.setBubbleContentH( (short)XmlUtil.getAttributeAsInteger(bubContent, "h", 0));
						m.setBubbleContent( XmlUtil.getValueAsString(bubContent,TransferLanguage.getMap() ));
						m.setBubbleFontSize((short)XmlUtil.getAttributeAsInteger(bubContent, "fontsize", 14));
					}
					
					
					
					screen[j].data[k] = m;
				}
			}
			
			helps.put(id, screen);
		}
		System.out.println("[系统初始化] [HelpManager] [初始化完成] [" + getClass().getName() + "] [耗时：" + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime) + "毫秒]");
	}
	
	public void destroy() {
		
	}
	
	public HelpMessage[] get(byte helpType,String windowId,int screenWidth,int screenHeight){
		Screen[] screen = helps.get(windowId);
		if( screen == null) return new HelpMessage[0];
		int nearest = 10000;
		Screen finded = null;
		for( int i=0;i< screen.length;i++){
			int d = Math.abs(screen[i].width-screenWidth)+Math.abs(screen[i].height-screenHeight);
			if(finded == null ||  d< nearest){
				finded = screen[i];
				nearest = d;
			}			
		}
		return finded.data;
	}
	public HashMap<String, Screen[]> getHelps() {
		return helps;
	}
	public void setHelps(HashMap<String, Screen[]> helps) {
		this.helps = helps;
	}
	public File getHelpWindowXml() {
		return helpWindowXml;
	}
	public void setHelpWindowXml(File helpWindowXml) {
		this.helpWindowXml = helpWindowXml;
	}
	public static void main(String[] args) {
		HelpManager h = new HelpManager();
		h.helpWindowXml =new File("D:\\helpWindow.xml");
		try {
			h.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
