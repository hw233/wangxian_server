package com.fy.engineserver.unite;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.society.unite.Option_Unite_Exit;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.UNITE_CONFIRM_REQ;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class UniteSubSystem extends SubSystemAdapter  {

//	public static Logger logger = Logger.getLogger(UniteSubSystem.class.getName());
public	static Logger logger = LoggerFactory.getLogger(UniteManager.class);
	
	private UniteManager uniteManager;
	
	public String tagforbid[] = new String[] { "'", "\"", " or ", "μ", "Μ", "\n", "\t", " ", "　", "　" };
	
	public void init(){
		
		if(logger.isInfoEnabled())
			logger.info("初始化uniteSubSystem完成");
		ServiceStartRecord.startLog(this);
	}
	
	/**
	 * 判断是否含有禁用的标签字符
	 * 
	 * @param name
	 * @return
	 */
	public boolean tagValid(String name) {
		String aname = name.toLowerCase();
		for (String s : tagforbid) {
			if (aname.indexOf(s) != -1) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String[] getInterestedMessageTypes() {
		return new String[] { "UNITE_APPLY_REQ","UNITE_DISAGREE_REQ","UNITE_CONFIRM_REQ","UNITE_EXIT_REQ","UNITE_ADD_REQ"};
	}

	@Override
	public String getName() {
		return "UniteSubSystem";
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);
		if(useMethodCache){
			return super.handleReqeustMessage(conn, message, player);
		}
		try {
			Class clazz = this.getClass();
			Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
			ResponseMessage res = null;
			res = (ResponseMessage) m1.invoke(this, conn, message, player);
			return res;
		} catch (InvocationTargetException e) {
			e.getTargetException().printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		return false;
	}

	public void setUniteManager(UniteManager uniteManager) {
		this.uniteManager = uniteManager;
	}


	/**
	 * 发送结义申请 
	 * @param conn
	 * @param message
	 * @param player
	 * @return 
	 */
	public ResponseMessage handle_UNITE_APPLY_REQ(Connection conn, RequestMessage message, Player player) {
		
		try {
			uniteManager.uniteApply(player);
			if(logger.isInfoEnabled()){
				logger.info("[队长发送结义申请] ["+player.getLogString()+"]");
			}
		} catch (Exception e) {
			logger.error("[队长发送结义申请失败] ["+player.getLogString()+"]",e);
		}
		return null;
	}
	
	
	/**
	 * 队长不同意结义
	 * @param conn
	 * @param message
	 * @param player
	 * @return 
	 */
	public ResponseMessage handle_UNITE_DISAGREE_REQ(Connection conn, RequestMessage message, Player player) {
		
		Team team = player.getTeam();
		if(team != null){
			if(team.getCaptain() == player){
				String descript = Translate.translateString(Translate.text_xx结义还未考虑清楚结义失败, new String[][]{{Translate.STRING_1,player.getName()}});
				for(Player p1 : team.getMembers()){
					if(p1 != player){
						p1.send_HINT_REQ(descript);
					}
				}
				player.send_HINT_REQ(Translate.text_你拒绝了结义);
				if(logger.isInfoEnabled()){
					logger.info("[队长不同意结义] ["+player.getLogString()+"]");
				}
			}else{
				logger.error("[队长不同意结义失败] ["+player.getLogString()+"] [不是队长]");
			}
		}
		return null;
	}
	
	
	/**
	 * 队长同意结义
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_UNITE_CONFIRM_REQ(Connection conn,RequestMessage message, Player player) {
		UNITE_CONFIRM_REQ req = (UNITE_CONFIRM_REQ)message;
		String title = req.getTitle();
		String name = req.getName();
		
		try {
			String result = uniteManager.uniteBeforeValidate(player);
			if(!result.equals("")){
				player.sendError(result);
				return null;
			}
			result = uniteManager.uniteNameCheck(title, name);
			if(result.equals("")){
				result = uniteManager.unite_apply_confirm_first(player, req);
				if(result.equals("")){
//					for(Player p1 : player.getTeamMembers()){
//						p1.sendError(Translate.text_结义完成);
//					}
				}else{
					player.sendError(result);
				}
			}else{
				player.sendError(result);
			}
		} catch (Exception e) {
			logger.error("[队长同意结义异常] ["+player.getLogString()+"]",e);
		}
		return null;
	}
	
	
	/**
	 * 退出结义
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_UNITE_EXIT_REQ(Connection conn, RequestMessage message, Player player) {
		Relation relation = SocialManager.getInstance().getRelationById(player.getId());
		if(relation != null){
			Unite u = uniteManager.getUnite(relation.getUniteId());
			if(u != null){
				
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
				mw.setTitle(Translate.text_退出结义);
				mw.setDescriptionInUUB(Translate.text_您确定要和兄弟姐妹绝交吗);
				
				Option_Unite_Exit agree = new Option_Unite_Exit();
				agree.setText(Translate.text_62);
				agree.setColor(0xffffff);
				
				Option_Cancel disagree = new Option_Cancel();
				disagree.setText(Translate.text_364);
				disagree.setColor(0xffffff);
				mw.setOptions(new Option[]{agree,disagree});
				
				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(res);
//				logger.info("[退出结义申请] []["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []");
				if(logger.isInfoEnabled())
					logger.info("[退出结义申请] ["+player.getLogString()+"]");
			}else{
				player.send_HINT_REQ(Translate.text_你还没有结义);
				if(logger.isInfoEnabled()){
					logger.info("[退出结义申请错误] [还没有结义] ["+player.getLogString()+"]");
				}
			}
		}
		return null;
	}
	
	/**
	 * 加入结义
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_UNITE_ADD_REQ(Connection conn,RequestMessage message, Player player) {
		
		try {
			Relation re = SocialManager.getInstance().getRelationById(player.getId());
			if(re.getUniteId() != -1){
				player.send_HINT_REQ(Translate.你已经有结义);
				return null;
			}
			String result = this.uniteManager.addToUniteCheck(player);
			if(!result.equals("")){
				player.send_HINT_REQ(result);
				if(logger.isWarnEnabled())
					logger.warn("[加入结义申请失败] ["+player.getLogString()+"] ["+result+"]");
			}else{
				if(logger.isWarnEnabled())
					logger.warn("[加入结义申请] ["+player.getLogString()+"]");
			}
		} catch (Exception e) {
			logger.error("[加入结义异常] ["+player.getLogString()+"]",e);
		}
		return null;
	}
}
