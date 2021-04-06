package com.fy.engineserver.honor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;

import com.fy.engineserver.datasource.language.Translate;

public class HonorDataFlieMaker {

	LinkedHashMap<String, HonorTemplete> templetes;

	File xlsFile;

	File xmlFile;

	static HonorDataFlieMaker self;

	public HonorDataFlieMaker() {

	}

	public void init() {
		this.templetes = new LinkedHashMap<String, HonorTemplete>();
		HonorDataFlieMaker.self = this;
	}

	public static HonorDataFlieMaker getInstance() {
		return HonorDataFlieMaker.self;
	}

	/**
	 * 从excel文件中读取称号模板数据
	 * 
	 * @param file
	 */
	public void loadTempleteDataFromXls(File file) {
//		try {
//			POIFSFileSystem pss = new POIFSFileSystem(new FileInputStream(file));
//			HSSFWorkbook wb = new HSSFWorkbook(pss);
//			HSSFSheet sh = wb.cloneSheet(0);
//			Iterator<HSSFRow> rows = sh.rowIterator();
//			int line = 0;
//			while (rows.hasNext()) {
//				try {
//					HSSFRow row = rows.next();
//					if (line > 0) {
//						String sort = "";
//						int sortId = -1;
//						String honorName = "";
//						String buffName = "";
//						int days = -1;
//						boolean isUnique = false;
//						int color = -1;
//						int iconId = -1;
//						String description = "";
//						boolean isRight = true;
//						for (short i = 0; i < 9; i++) {
//							HSSFCell cell = row.getCell(i);
//							int num = -1;
//							String s = "";
//							s = HonorDataFlieMaker.getCellContant(cell);
//							if(s!=null){
//								s=s.trim();
//							}
//							switch (i) {
//							case 0:
//								if (s != null && s.length() > 0) {
//									sort = s;
//								} else {
//									System.out.println("sort is error!!!"
//											+ line);
//									isRight = false;
//								}
//								break;
//
//							case 1:
//								if (s != null && s.length() > 0) {
//									num = Integer.parseInt(s);
//									if (num >= 0) {
//										sortId = num;
//									} else {
//										System.out.println("sortId is error!!!"
//												+ line);
//										isRight = false;
//									}
//								} else {
//									System.out.println("sortId is error!!!"
//											+ line);
//									isRight = false;
//								}
//								break;
//
//							case 2:
//								if (s != null && s.length() > 0) {
//									honorName = s;
//								} else {
//									System.out.println("honorName is error!!!"
//											+ line);
//									isRight = false;
//									break;
//								}
//								break;
//
//							case 3:
//								if (s != null && s.length() > 0) {
//									buffName = s;
//								}
//								break;
//
//							case 4:
//								if (s != null && s.length() > 0) {
//									num = Integer.parseInt(s);
//									days = num;
//								} else {
//									days = -1;
//								}
//								break;
//
//							case 5:
//								if (s != null && s.length() > 0) {
//									isUnique = Integer.parseInt(s) == 1;
//								} else {
//									isUnique = false;
//								}
//								break;
//
//							case 6:
//								if (s != null && s.length() > 0) {
//									color = Integer
//											.parseInt(s.substring(1), 16);
//								} else {
//									color = 0xffffff;
//								}
//								break;
//
//							case 7:
//								if (s != null && s.length() > 0) {
//									iconId = Integer.parseInt(s);
//								} else {
//									iconId = -1;
//								}
//								break;
//
//							case 8:
//								if (s != null && s.length() > 0) {
//									description = s;
//								} else {
//									description = "";
//								}
//								break;
//							}
//						}
//						if (isRight) {
//							HonorTemplete ht = new HonorTemplete();
//							ht.setName(honorName);
//							ht.setSort(sort);
//							ht.setSortId(sortId);
//							ht.setBuffName(buffName);
//							ht.setColor(color);
//							ht.setDesp(description);
//							ht.setIconId(iconId);
//							ht.setUnique(isUnique);
//							ht.setUsefulLife(days);
//
//							if (!this.templetes.containsKey(ht.getName())) {
//								this.templetes.put(ht.getName(), ht);
//								System.out.println("load a templete honorName:"
//										+ honorName + " sort:" + sort
//										+ " sortId:" + sortId + " buffName:"
//										+ buffName + " color:" + color
//										+ " description:" + description
//										+ " iconId:" + iconId + " isUnique:"
//										+ isUnique + " days:" + days);
//							} else {
//								System.out
//										.println("警告，称号名称重复！ " + ht.getName());
//							}
//						} else {
//							System.out.println("templete data error:"
//									+ (line + 1));
//						}
//					}
//
//				} catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
//					System.out.println("error cell row:" + (line + 1));
//				}
//				line++;
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
	}

	public static String getCellContant(HSSFCell cell) {
		if (cell != null) {
			if (cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				return "";
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
				return "" + cell.getBooleanCellValue();
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_ERROR) {
				return "" + cell.getErrorCellValue();
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
				return cell.getCellFormula();
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				return "" + (int) cell.getNumericCellValue();
			} else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			}
		}
		return null;
	}

	public File getXmlFile() {
		return xmlFile;
	}

	public void setXmlFile(File xmlFile) {
		this.xmlFile = xmlFile;
	}

	public void outputTempletesDataFile(File xmlFile) {
		StringBuffer sb = new StringBuffer();
		HonorTemplete[] hts = this.templetes.values().toArray(
				new HonorTemplete[0]);
		sb.append("<?xml version='1.0' encoding='gb2312'?>\n");
		sb.append("<honorTempletes time='" + new Date() + "'>\n");
		for (HonorTemplete ht : hts) {
			sb.append("<templete name='" + ht.getName() + "' sort='"
					+ ht.getSort() + "' sortId='" + ht.getSortId() + "' buff='"
					+ ht.getBuffName() + "' color='" + ht.getColor()
					+ "' description='" + ht.getDesp() + "' iconId='"
					+ ht.getIconId() + "' isUnique='" + ht.isUnique()
					+ "' usefulLife='" + ht.getUsefulLife() + "'/>\n");
		}
		sb.append("</honorTempletes>");
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(xmlFile);
			output.write(sb.toString().getBytes());
			output.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		HonorDataFlieMaker hdfm=new HonorDataFlieMaker();
		hdfm.init();
		hdfm.loadTempleteDataFromXls(new File(Translate.text_4229));
		hdfm.outputTempletesDataFile(new File("honorTempletes.xml"));
	}
}
