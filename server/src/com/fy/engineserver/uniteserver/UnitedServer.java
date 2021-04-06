package com.fy.engineserver.uniteserver;

import java.util.HashMap;

import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.impl.DefaultSimpleEntityManagerFactory;

/**
 * 要合并的服务器
 * 
 * 
 */
public class UnitedServer {

	/** 服务器的名字 */
	private String serverName;
	private String configFilePath;
	/** 是否是基础服务器,其他服务器合并到这台服务器 */
	private boolean isMainserver;

	private DefaultSimpleEntityManagerFactory factory;
	
	private SimpleEntityManager<ArticleEntity> articleManager;
	
	public UnitedServer(String serverName, String configFilePath, boolean isMainserver) {
		this.serverName = serverName;
		this.configFilePath = configFilePath;
		this.isMainserver = isMainserver;
		factory = new DefaultSimpleEntityManagerFactory(configFilePath);
		factory.getSimpleEntityManager(Player.class);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	public DefaultSimpleEntityManagerFactory getFactory() {
		return factory;
	}

	public boolean isMainserver() {
		return isMainserver;
	}

	public void setMainserver(boolean isMainserver) {
		this.isMainserver = isMainserver;
	}

	@Override
	public String toString() {
		return "UnitedServer [serverName=" + serverName + ", configFilePath=" + configFilePath + ", isMainserver=" + isMainserver + ", factory=" + factory + "]";
	}

	public void setArticleManager(SimpleEntityManager<ArticleEntity> articleManager) {
		this.articleManager = articleManager;
	}

	public SimpleEntityManager<ArticleEntity> getArticleManager() {
		if (articleManager == null) {
			articleManager = factory.getSimpleEntityManager(ArticleEntity.class);
		}
		return articleManager;
	}
}
