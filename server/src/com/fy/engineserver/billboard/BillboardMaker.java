package com.fy.engineserver.billboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.fy.engineserver.billboard.concrete.BattleFieldBillboards;
import com.fy.engineserver.billboard.concrete.DungeonBillboards;
import com.fy.engineserver.billboard.concrete.EquipmentBillboards;
import com.fy.engineserver.billboard.concrete.ExpBillboards;
import com.fy.engineserver.billboard.concrete.GangBillboards;
import com.fy.engineserver.billboard.concrete.IncomeFromAuBillboards;
import com.fy.engineserver.billboard.concrete.KillingBillboards;
import com.fy.engineserver.billboard.concrete.OnlineTimeBillboards;
import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.XmlUtil;

public class BillboardMaker {
	String fileName;
	public File fileDir;
//	static Logger log=Logger.getLogger("Billboard");
public	static Logger log = LoggerFactory.getLogger("Billboard");
	
	static BillboardMaker self;
	
	public void init(){
		BillboardMaker.self=this;
		if(this.fileDir!=null){
			if(!this.fileDir.exists()){
				this.fileDir.mkdirs();
			}
		}
	}
	
//	public void outputBillboard(Billboards[] billboards) {
//		try {
//			Calendar ca=Calendar.getInstance();
//			ca.setTimeInMillis(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
//			int year=ca.get(Calendar.YEAR);
//			int month=ca.get(Calendar.MONTH)+1;
//			int day=ca.get(Calendar.DAY_OF_MONTH);
//			int hour=ca.get(Calendar.HOUR_OF_DAY);
//			int minute=ca.get(Calendar.MINUTE);
////			ByteArrayOutputStream baos=new ByteArrayOutputStream();
//			StringBuffer sb = new StringBuffer();
//			sb.append("<?xml version='1.0' encoding='utf-8'?>\n");
//			sb.append("<billboard time='"+year+"."+month+"."+day+"."+hour+"."+minute+"'>\n");
//			if (billboards != null) {
//				for (int i = 0; i < billboards.length; i++) {
//					if(billboards[i]!=null){
//						sb.append("<menu name='" + billboards[i].getName()
//								+ "' type='" + BillboardType.getType(billboards[i].getType())+ "'>\n");
//						if (billboards[i].billboard != null) {
//							for (int j = 0; j < billboards[i].billboard.length; j++) {
//								if (billboards[i].billboard[j] != null) {
//									sb.append("<submenu name='"
//											+ billboards[i].billboard[j].getName()
//											+ "' title='"+billboards[i].billboard[j].getTitlesString()+"'>\n");
//									if (billboards[i].billboard[j].data != null) {
//										for (int k = 0; k < billboards[i].billboard[j].data.length; k++) {
//											if (billboards[i].billboard[j].data[k] != null) {
//												sb.append("<data>\n");
//												
//												sb.append("<rankObject>");
////												sb.append("<![CDATA["+billboards[i].billboard[j].data[k].getRankingObject()+"]]>");
//												sb.append("<![CDATA[");
//												sb.append(billboards[i].billboard[j].data[k].getRankingObject());
//												sb.append("]]>");
//												sb.append("</rankObject>\n");
//												
//												sb.append("<value>");
//												sb.append("<![CDATA["+billboards[i].billboard[j].data[k].getValue()+"]]>");
//												sb.append("</value>\n");
//												
//												sb.append("<description>");
//												sb.append("<![CDATA["+billboards[i].billboard[j].data[k].getDescription()+"]]>");
//												sb.append("</description>\n");
//												
//												sb.append("<id>");
//												sb.append("<![CDATA["+billboards[i].billboard[j].data[k].getId()+"]]>");
//												sb.append("</id>\n");
//												
//												sb.append("<additionalInfo>");
//												sb.append("<![CDATA["+billboards[i].billboard[j].data[k].getAdditionalInfo()+"]]>");
//												sb.append("</additionalInfo>\n");
//												
//												sb.append("</data>\n");
//											}
////											baos.write(sb.toString().getBytes("utf-8"));
//										}
//									}
//									sb.append("</submenu>\n");
//								}
//							}
//						}
//						sb.append("</menu>\n");
//					}
//				}
//			}
//			sb.append("</billboard>\n");
//
//			// Configuration root=new DefaultConfiguration("billboard","") ;
//			// for(int i=0;i<this.submenu.length;i++){
//			// Configuration menu=new DefaultConfiguration("menu","");
//			// menu.setAttribute("name", this.menu[i]+this.billboard);
//			// menu.setAttribute("id", ""+(i+1));
//			// menu.setAttribute("type", ""+type[i]);
//			// menu.setAttribute("xml_src", this.menu[i]+this.billboard);
//			//			
//			// for(int j=0;j<this.submenu[i].length;j++){
//			// Configuration submenu=new DefaultConfiguration("submenu","");
//			// submenu.setAttribute("name", this.submenu[i][j]);
//			// submenu.setAttribute("id", (i+1)+""+j);
//			// menu.addChild(submenu);
//			// }
//			// root.addChild(menu);
//			//		}
//			
//			if(!this.fileDir.exists()){
//				this.fileDir.mkdir();
//			}
//			File file = new File(this.fileDir,this.fileName);
//			FileOutputStream output=new FileOutputStream(file);
//			output.write(sb.toString().getBytes("utf-8"));
//			output.flush();
//			output.close();
//			if(log.isInfoEnabled()){
//				log.info("[生成排行榜文件] [成功] ["+this.fileDir+"/"+this.fileName+"]");
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.warn("[生成排行榜文件] [失败] [发生错误] [错误："+e+"]",e);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.warn("[生成排行榜文件] [失败] [发生错误] [错误："+e+"]",e);
//		}
//	}
	
	public void outputBillboard(Billboards[] billboards) {
		try {
			Configuration rootConf = new DefaultConfiguration("billboard", "-");
			rootConf.setAttribute("time", DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			if (billboards != null) {
				for (int i = 0; i < billboards.length; i++) {
					if(billboards[i]!=null){
						Configuration menuConf = new DefaultConfiguration("menu", "-");
						menuConf.setAttribute("name", billboards[i].getName());
						menuConf.setAttribute("type", BillboardType.getType(billboards[i].getType()));
						if (billboards[i].billboard != null) {
							for (int j = 0; j < billboards[i].billboard.length; j++) {
								if (billboards[i].billboard[j] != null) {
									Configuration submenuConf = new DefaultConfiguration("submenu", "-");
									submenuConf.setAttribute("name",  billboards[i].billboard[j].getName());
									submenuConf.setAttribute("title", billboards[i].billboard[j].getTitlesString());
									if (billboards[i].billboard[j].data != null) {
										for (int k = 0; k < billboards[i].billboard[j].data.length; k++) {
											if (billboards[i].billboard[j].data[k] != null) {
												Configuration dataConf = new DefaultConfiguration("data", "-");
												Configuration conf = new DefaultConfiguration("rankObject", "-");
												conf.setValue(billboards[i].billboard[j].data[k].getRankingObject());
												dataConf.addChild(conf);
												conf = new DefaultConfiguration("value", "-");
												conf.setValue(String.valueOf(billboards[i].billboard[j].data[k].getValue()));
												dataConf.addChild(conf);
												conf = new DefaultConfiguration("description", "-");
												conf.setValue(String.valueOf(billboards[i].billboard[j].data[k].getDescription()));
												dataConf.addChild(conf);
												conf = new DefaultConfiguration("id", "-");
												conf.setValue(String.valueOf(billboards[i].billboard[j].data[k].getId()));
												dataConf.addChild(conf);
												conf = new DefaultConfiguration("additionalInfo", "-");
												conf.setValue(String.valueOf(billboards[i].billboard[j].data[k].getAdditionalInfo()));
												dataConf.addChild(conf);
												submenuConf.addChild(dataConf);
											}
										}
									}
									menuConf.addChild(submenuConf);
								}
							}
						}
						rootConf.addChild(menuConf);
					}
				}
			}
			new DefaultConfigurationSerializer().serializeToFile(new File(this.fileDir.getPath(), this.fileName), rootConf, "UTF-8");
			if(log.isInfoEnabled()){
//				log.info("[生成排行榜文件] [成功] ["+this.fileDir+"/"+this.fileName+"]");
				if(log.isInfoEnabled())
					log.info("[生成排行榜文件] [成功] [{}/{}]", new Object[]{this.fileDir,this.fileName});
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(log.isWarnEnabled())
				log.warn("[生成排行榜文件] [失败] [发生错误] [错误："+e+"]",e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(log.isWarnEnabled())
				log.warn("[生成排行榜文件] [失败] [发生错误] [错误："+e+"]",e);
		}
	}
	
	public Billboards[] makeBillboards(){
		try {
			Billboards[] billboards=null;
			Document doc = null;
			doc=XmlUtil.load(new FileInputStream(this.fileDir.getPath()+"/"+this.fileName), "UTF-8");
			if(doc!=null){
				Element root = doc.getDocumentElement();
				Element[] menus=XmlUtil.getChildrenByName(root, "menu");
				if(menus!=null){
					billboards=new Billboards[menus.length];
					for(int i=0;i<menus.length;i++){
						String menuName=XmlUtil.getAttributeAsString(menus[i], "name", null);
						String typeS=XmlUtil.getAttributeAsString(menus[i], "type", null);
						byte type=BillboardType.getTypeIndex(typeS);
						if(menuName!=null){
							int index=BillboardManager.isIncluding(menuName);
							if(index>=0){
								if(type>=0){
									if(BillboardManager.BILLBOARDS_NAMES[BillboardManager.KINDS_EXP].equals(menuName)){
										billboards[i]=new ExpBillboards(menuName,type);
									}else if(BillboardManager.BILLBOARDS_NAMES[BillboardManager.KINDS_GANG].equals(menuName)){
										billboards[i]=new GangBillboards(menuName,type);
									}else if(BillboardManager.BILLBOARDS_NAMES[BillboardManager.KINDS_KILLING].equals(menuName)){
										billboards[i]=new KillingBillboards(menuName,type);
									}else if(BillboardManager.BILLBOARDS_NAMES[BillboardManager.KINDS_BATTLE_FIELD].equals(menuName)){
										billboards[i]=new BattleFieldBillboards(menuName,type);
									}else if(BillboardManager.BILLBOARDS_NAMES[BillboardManager.KINDS_ONLINE_TIME].equals(menuName)){
										billboards[i]=new OnlineTimeBillboards(menuName,type);
									}else if(BillboardManager.BILLBOARDS_NAMES[BillboardManager.KINDS_INCOME_FROM_AU].equals(menuName)){
										billboards[i]=new IncomeFromAuBillboards(menuName,type);
									}else if(BillboardManager.BILLBOARDS_NAMES[BillboardManager.KINDS_EQUIPMENT].equals(menuName)){
										billboards[i]=new EquipmentBillboards(menuName,type);
									}else if(BillboardManager.BILLBOARDS_NAMES[BillboardManager.KINDS_DUNGEON].equals(menuName)){
										billboards[i]=new DungeonBillboards(menuName,type);
									}
									if(billboards[i]!=null){
										Element[] submenus=XmlUtil.getChildrenByName(menus[i], "submenu");
										if(submenus!=null){
											billboards[i].billboard=new Billboard[submenus.length];
											for(int j=0;j<submenus.length;j++){
												String submenuName=XmlUtil.getAttributeAsString(submenus[j], "name", null);
												String[] titles=XmlUtil.getAttributeAsString(submenus[j], "title", null).split(":");
												billboards[i].billboard[j]=new Billboard(submenuName,titles);
												Element[] data=XmlUtil.getChildrenByName(submenus[j], "data");
												if(data!=null){
													billboards[i].billboard[j].data=new BillboardData[data.length];
													for(int k=0;k<data.length;k++){
														billboards[i].billboard[j].data[k]=new BillboardData(k+1);
//														String rankObj=XmlUtil.getAttributeAsString(data[k], "rankObject");
//														long value=XmlUtil.getAttributeAsLong(data[k], "value");
//														String description=XmlUtil.getAttributeAsString(data[k], "description","");
//														long id=XmlUtil.getAttributeAsLong(data[k], "id");
//														int additionalInfo=XmlUtil.getAttributeAsInteger(data[k], "additionalInfo");
														
														Element rankObj=XmlUtil.getChildByName(data[k], "rankObject");
														Element value=XmlUtil.getChildByName(data[k], "value");
														Element description=XmlUtil.getChildByName(data[k], "description");
														Element id=XmlUtil.getChildByName(data[k], "id");
														Element additionalInfo=XmlUtil.getChildByName(data[k], "additionalInfo");
														
														billboards[i].billboard[j].data[k].setRankingObject(XmlUtil.getValueAsString(rankObj,"", null));
														billboards[i].billboard[j].data[k].setValue(XmlUtil.getValueAsLong(value));
														billboards[i].billboard[j].data[k].setDescription(XmlUtil.getValueAsString(description,"", null));
														billboards[i].billboard[j].data[k].setId(XmlUtil.getValueAsLong(id));
														billboards[i].billboard[j].data[k].setAdditionalInfo(XmlUtil.getValueAsInteger(additionalInfo));
													}
													if(log.isInfoEnabled()){
//														log.info("[从数据文件创建排行榜] [成功] [名称："+menuName+"] [子菜单："+submenuName+"]");
														if(log.isInfoEnabled())
															log.info("[从数据文件创建排行榜] [成功] [名称：{}] [子菜单：{}]", new Object[]{menuName,submenuName});
													}
												}else{
//													log.warn("[从数据文件创建排行榜] [失败] [没有排行榜数据] [名称："+menuName+"] [子菜单："+submenuName+"]");
													if(log.isWarnEnabled())
														log.warn("[从数据文件创建排行榜] [失败] [没有排行榜数据] [名称：{}] [子菜单：{}]", new Object[]{menuName,submenuName});
												}
											}
										}else{
//											log.warn("[从数据文件创建排行榜] [失败] [没有子菜单数据] [名称："+menuName+"]");
											if(log.isWarnEnabled())
												log.warn("[从数据文件创建排行榜] [失败] [没有子菜单数据] [名称：{}]", new Object[]{menuName});
										}
//										billboards[i].resetRankObject();
									}
								}else{
//									log.warn("[从数据文件创建排行榜] [失败] [不支持此种数据类型] [名称："+menuName+"] [type："+typeS+"]");
									if(log.isWarnEnabled())
										log.warn("[从数据文件创建排行榜] [失败] [不支持此种数据类型] [名称：{}] [type：{}]", new Object[]{menuName,typeS});
								}
							}else{
//								log.warn("[从数据文件创建排行榜] [失败] [不支持此排行榜] [名称："+menuName+"]");
								if(log.isWarnEnabled())
									log.warn("[从数据文件创建排行榜] [失败] [不支持此排行榜] [名称：{}]", new Object[]{menuName});
							}
						}else{
							if(log.isWarnEnabled())
								log.warn("[从数据文件创建排行榜] [失败] [菜单名为空]");
						}
					}
				}else{
					if(log.isWarnEnabled())
						log.warn("[从数据文件创建排行榜] [失败] [没有数据]");
				}
			}else{
//				log.warn("[从数据文件创建排行榜] [失败] [读取数据文件失败] [路径："+this.fileDir.toString()+"/"+this.fileName+"]");
				if(log.isWarnEnabled())
					log.warn("[从数据文件创建排行榜] [失败] [读取数据文件失败] [路径：{}/{}]", new Object[]{this.fileDir.toString(),this.fileName});
			}
			return billboards;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(log.isWarnEnabled())
				log.warn("[从数据文件创建排行榜] [失败] [发生错误] [错误："+e+"]",e);
		} catch(Exception e){
			e.printStackTrace();
			if(log.isWarnEnabled())
				log.warn("[从数据文件创建排行榜] [失败] [发生错误] [错误："+e+"]",e);
		}
		return null;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileDir(File fileDir) {
		this.fileDir = fileDir;
	}
	
	public static BillboardMaker getInstance(){
		return BillboardMaker.self;
	}
	
}
