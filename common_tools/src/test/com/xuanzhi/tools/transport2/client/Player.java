package com.xuanzhi.tools.transport2.client;

import java.io.IOException;

import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.transport2.CHAT_MESSAGE_TEST_REQ;
import com.xuanzhi.tools.transport2.Connection2;
import com.xuanzhi.tools.transport2.GameMessageFactory;

/**
 * 模拟一个玩家
 * @author <a href='myzdf.bj@gmail.com'>yugang.wang</a>
 *
 */
public class Player {

	public Connection2 conn;
	
	public int id;
	
	public String name;
	
	ChatClient cc;
	long lastSendTime = System.currentTimeMillis();
	
	public Player(ChatClient cc){
		this.cc = cc;
	}
	
	public void heartbeat(long deltaT){
		if(conn == null){
			try {
				conn = cc.selector.takeoutNewConnection(cc.selector.getHost(),cc.selector.getPort());
				conn.setAttachment(this);
				System.out.println("[player-:"+this.id+":] [takeout] [conn="+conn+"]");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(conn != null){
			if(System.currentTimeMillis() - lastSendTime  > 1000L){
				if(chat()){
					lastSendTime = System.currentTimeMillis();
				}
			}
		}
	}
	
	public boolean chat(){
		double d = Math.random();
		if(d < 0.2){
			String content = getContent();
			CHAT_MESSAGE_TEST_REQ req = new CHAT_MESSAGE_TEST_REQ(GameMessageFactory.nextSequnceNum(),this.id,this.name,"聊天",content);
			try {
				conn.sendMessage(req);
				
				//System.out.println("[player-:"+this.id+":] [发现消息] [conn="+conn.getName()+"]");
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
	
	public String getContent(){
		String[] contents = new String[]{
				"In case you wish to migrate your Java source files to SLF4J, consider our migrator tool which can migrate your project to use the SLF4J AP",
				"SLF4J，即简单日志门面（Simple Logging Facade for Java），不是具体的日志解决方案，它只服务于各种各样的日志系统。按照官方的说法",
				"2014年2月7日 - 极品时刻表(火车时刻查询)是一款可以查询全国列车时刻表的软件,极品时刻表运行不需要网络支持,火车",
				"以美女图片|丝袜美腿|性感美女|搞笑图片为主的娱乐社区... 热搜: 妹妹图片网 极品美女网 校花 动漫美女 护士 空姐 视频 美女 COS 人体艺术写真 极品美女撸管 御...",
				"我身边的极品美女们是天下雪创作的一部都市言情小说，本文讲述了一个功夫小子玩转都市，纵横情场的故事",
				"暧昧办公室:我的极品美女上司 分享到 宅男陈天霖在机缘巧合下邂逅美 女总裁,第二天惊讶的发现面试主考官居然是昨夜春风一度的美 少妇。走了鸿运的他轻松进入世界.",
				"要花瓶型美女的老板们有福了!TEL:18814801983 18858175383刘言",
				"极品美女玫瑰花瓣浴室的顶级诱惑,比较精致的五官,性感妖娆的身材,这位极品美女是刚出道的新人嫩模,这么诱惑的比基尼写真出炉,意味着又将引起各界宅男们的火辣辣眼神注视她",
				"最佳答案: 不是这样说的吧~ 类似以色列国防军和警察已制定完成从加沙地带和部分约旦河西岸地区撤离的单边行动计划执行方案。的新闻不时见报,加沙的所谓控制权应该在.",
				"转贴:路透社社论: 以色列对加沙的海上封锁是否合法? 的精彩评论 先问问哈马斯的火箭弹合法吗? 难道违反国际法也是伊死兰的专利? 发表对 转贴:路透社社论: 以色列对...",
				"2009年1月25日 -  22日以色列外长利夫尼发表讲话称,以方“坚决保留对地道采取军事行动的权利”,这诱发外界猜测以军会不会以制止通过地道走私武器为借口,重新进攻加沙",
				"2009年1月15日 - 加沙城之战会否全面打响成为目前加沙战事演变的焦点。 以军发动开战来最猛攻势 以色列舆论将目前的军事行动定义为“第2.5阶段”,既区别于本月3日之后..."
		};
		
		int k = (int)(Math.random() * contents.length);
		return contents[k];
	}
}
