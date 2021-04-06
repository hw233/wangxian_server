package com.fy.gamegateway.mieshi.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.configuration.Configuration;
import com.xuanzhi.tools.configuration.DefaultConfiguration;
import com.xuanzhi.tools.configuration.DefaultConfigurationBuilder;
import com.xuanzhi.tools.configuration.DefaultConfigurationSerializer;

public class TipManager {
	static Logger logger = Logger.getLogger(MieshiGatewayServer.class);
	private String tipConfigFile;
	private static TipManager self;
	private List<String> tipe1s = new ArrayList<String>();

	public static String title = "测试";
	/**
	 * * 文本显示
 * 
 * 输入为一个自定义好的字符串
 * 格式如下：
 
 * 标签大类分为 F,I,A,\n,\t,' ',
 
 F :TextItem  支持属性 color(文字颜色),size(文字大小),style(斜体),
					 float(布局)，onclick(事件),以及其他自定义props
 
 I :ImageItem 支持属性 imagePath,bgImagePath,iconId,float(布局),onclick(事件)，以及其他自定义props
 
 A :AnimationItem 支持属性 partName,animName,animIndex, width,height,float(布局),onclick(事件),以及其他自定义props
 
 E :EntityButton  支持属性 articleId,bgImagePath, onclick(事件)，以及其他自定义props
 
 \n:换行
 
 \t:空2个字距
 
 空格:空1格字距

 * 例子0
 * 		<f color='#ffffffff' size='32' style='italic' float='left' >文本文本</f>
 * 		<i iconId='hongyaoshui' ></i>
 * 		<a width='100' height='100' partName='ren_name' animName='站'></a>
 * 
 * 例子1：
 *       <f color='#45FF9F' size='40' style='bold'>兽王琳固化</f>
 *       <i width='50' id='1234'></i><f style='italic'>威力值：</f><f color='#FF0000' style='bold'>570</f>
 *       <u color='#45FF9F' id='1234'>张三</i>
 *       <f>品质：普通</f>
 *       <f>装备位置：头部</f>
 *       <f>耐久度：300/300</f>
 *       <f>等级：26</f>

	 */
	public static String descriptionInUUB = "<f color='#ffffffff' size='32' style='italic'>由于在线更新出现严重技术性BUG，导致更新1.3.5版本无法通过在线完成，为此给您带来的不便我们深表歉意。请您前往当乐，UC等网站下载灭世OL1.3.5版本客户端进行安装更新。</f>\n\t由于在线更新出现严重技术性BUG，导致更新1.3.5版本无法通过在线完成，为此给您带来的不便我们深表歉意。请您前往当乐，UC等网站下载灭世OL1.3.5版本客户端进行安装更新。由于在线更新出现严重技术性BUG，导致更新1.3.5版本无法通过在线完成，为此给您带来的不便我们深表歉意。请您前往当乐，UC等网站下载灭世OL1.3.5版本客户端进行安装更新。";
	public static int width = 0;
	public static int height = 0;
	public static String[] btns = new String[]{"关闭","退出游戏"};
	public static byte[] oType = new byte[]{(byte)0,(byte)1};
	public static boolean tipOpen = false;
	
	public static TipManager getInstance() {
		return self;
	}

	public void initialize() {
		long now = System.currentTimeMillis();
		try {
			tipe1s = loadTipPage();
			self = this;
			System.out.println(this.getClass().getName()
					+ " initialize successfully ["
					+ (System.currentTimeMillis() - now) + "]");
			logger.info(this.getClass().getName()
					+ " initialize successfully ["
					+ (System.currentTimeMillis() - now) + "][saveFile:"
					+ tipConfigFile + "]");
		} catch (Exception e) {
			logger.info(this.getClass().getName()
					+ " initialize fail [saveFile:" + tipConfigFile + "]");
		}
	}

	public String[] getArrayTips() {
		String res[] = new String[tipe1s.size()];
		for (int i = 0; i < tipe1s.size(); i++) {
			res[i] = tipe1s.get(i);
		}
		
		return res;
	}

	public  List<String> loadTipPage() {

		List<String> tipes = new ArrayList<String>();
		try {
			Configuration rootConf = new DefaultConfigurationBuilder()
					.buildFromFile(tipConfigFile);
			Configuration tipesConf[] = rootConf.getChildren();
			for (int i = 0; i < tipesConf.length; i++) {
				tipes.add(tipesConf[i].getAttribute("value"));
			}
			logger.info("load tips sussess");
			return tipes;
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("load tips fail");
			return tipes;
		}
	}

	public  void saveActionPage(List<String> tipes) throws Exception {

		Configuration rootConf = new DefaultConfiguration("tipes", "-");
		for (String tip : tipes) {
			Configuration acConf = new DefaultConfiguration("tip", "-");
			rootConf.addChild(acConf);
			acConf.setAttribute("value", tip);
		}
		new DefaultConfigurationSerializer().serializeToFile(new File(
				tipConfigFile), rootConf);
		logger.info("save success and tipes length is "+tipes.size());
	}

	public String getTipConfigFile() {
		return tipConfigFile;
	}

	public void setTipConfigFile(String tipConfigFile) {
		this.tipConfigFile = tipConfigFile;
	}

	public List<String> getTipe1s() {
		return tipe1s;
	}

	public void setTipe1s(List<String> tipe1s) {
		this.tipe1s = tipe1s;
	}

}
