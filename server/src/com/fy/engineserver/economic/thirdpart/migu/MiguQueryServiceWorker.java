package com.fy.engineserver.economic.thirdpart.migu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.fy.engineserver.economic.GameServerSavingNotifyService;
import com.fy.engineserver.economic.thirdpart.migu.entity.SaleRecordManager;
import com.fy.engineserver.economic.thirdpart.migu.entity.model.TempPlayerInfo;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.xuanzhi.tools.text.JsonUtil;

public class MiguQueryServiceWorker extends HttpServlet{
	public static Logger logger = GameServerSavingNotifyService.logger;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		String remoteAddr = req.getRemoteAddr();
		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		String userName = req.getParameter("username");
		String op = req.getParameter("op");
//		JacksonManager jacksonManager = JacksonManager.getInstance();
		try {
			if ("queryAllRole".equals(op)) {				//查询账号下所有的角色信息
				if (!MiGuTradeServiceWorker.是否可以上架) {
					return;
				}
				if (userName == null || userName.isEmpty()) {
					if (logger.isInfoEnabled()) {
						logger.info("[米谷查询角色信息] [失败] [账号为空] [" + userName + "]");
					}
					return ;
				}
				Player[] players = GamePlayerManager.getInstance().getPlayerByUser(userName);
				List<TempPlayerInfo> list = new ArrayList<TempPlayerInfo>();
				String serverName = "";
				for (Player p : players) {
					if (SaleRecordManager.getInstance().canPlayerSeal4Show(p)) {
						TempPlayerInfo info = new TempPlayerInfo(p.getId(), p.getName(), serverName, p.getCareer(), p.getLevel(), p.getVipLevel(), p.getCountry());
						list.add(info);
					}
				}
//				JsonUtil.jsonFromObject(list);
				res.getWriter().write(JsonUtil.jsonFromObject(list));
				if (logger.isInfoEnabled()) {
					logger.info("[米谷查询角色信息] [成功] [耗时:" + (System.currentTimeMillis() - start) + "] [userName:" + userName + "] [返回角色数:" + list.size() + "]");
				}
			}
		} catch (Exception e) {
			logger.warn("[米谷查询角色信息] [异常] [op:" + op + "] [userName:" + userName + "]", e);
		}
	}
	
	

}
