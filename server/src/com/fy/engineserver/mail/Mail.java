package com.fy.engineserver.mail;

import java.util.Calendar;
import java.util.Date;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.mail.service.concrete.DefaultMailManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.CacheSizes;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimplePostPersist;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 邮件
 * 邮件发送人发送后，进入接受人的邮箱，接受人可以查看不删除，可以取得里边的一部分物品不删除
 * 如果用户删除邮件，只做标识
 * 
 *
 */
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(name="Mail_St_Re",members={"status","receiver"},compress=1),
	@SimpleIndex(name="Mail_Re_Cr",members={"receiver","createDate"}),
	@SimpleIndex(name="Mail_St_Ex",members={"status","expireDate"},compress=1),
	@SimpleIndex(name="Mail_St_Re_Cr",members={"status","receiver","createDate"},compress=1)
})
public class Mail implements Cacheable, CacheListener {
	
	public static final int MAXCOUNT = 100;
	
	public static final int CELL_CAPACITY = 8;
	
	public static final int NORMAL_UNREAD = 0;
	public static final int NORMAL_READED = 1;
	public static final int DELETED = 2;
	public static final int APPENDIX_UNREAD = 3;
	public static final int APPENDIX_READED = 4;
	
	public static final String STATUS_DESP[] = new String[]{Translate.text_4690,Translate.text_4691,Translate.text_4692,Translate.text_4693,Translate.text_4694};
	
	public static final int TYPE_SYSTEM = 0;
	public static final int TYPE_PLAYER = 1;
	public static final int TYPE_GM = 2;
	
	public static final int defaultExpireDays = 30;
	
	@SimpleId
	private long id;
	
	@SimpleVersion
	protected int version2;

	private long poster;
	
	private long receiver;
	
	private String title = "";
	@SimpleColumn(length=1000)
	private String createReason = "";
	
	@SimpleColumn(length=4000)
	private String content = "";
	//游戏币
	private long coins;
	
	private String mailMsgDate;
	
	private int status;
	
	private Date createDate;
	
	private Date expireDate;
	
	private Cell cells[];
	
	private transient boolean dirty;
	
	private Date lastModifyDate;
	
	private long price;
	
	private int type;
	
	transient boolean newFlag;
	
	public Mail() {
		this.cells = new Cell[CELL_CAPACITY];
		for(int i=0; i<cells.length; i++) {
			cells[i] = new Cell();
		}
		this.status = NORMAL_UNREAD;
		Calendar cal = Calendar.getInstance();
		this.createDate = cal.getTime();
		cal.add(Calendar.DAY_OF_YEAR, defaultExpireDays);
		this.expireDate = cal.getTime();
		this.lastModifyDate = new Date();
	}

	@SimplePostPersist
	public void postPersist(){
		if(PlayerManager.getInstance() != null){
			Player receiver = PlayerManager.getInstance().getPlayerInCache(this.receiver);
			if(receiver != null){
				receiver.addNewMailCount();
			}
		}
	}
	
	/**
	 * 得到id
	 * @return
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 得到发送人
	 * @return
	 */
	public long getPoster() {
		return poster;
	}

	public void setPoster(long poster) {
		this.poster = poster;
		saveData("poster");
	}

	/**
	 * 得到接受人
	 * @return
	 */
	public long getReceiver() {
		return receiver;
	}

	public void setReceiver(long receiver) {
		this.receiver = receiver;
		saveData("receiver");
	}

	/**
	 * 得到标题
	 * @return
	 */
	public String getTitle() {
		if(title == null){
			return "";
		}
		return title;
	}

	public void setTitle(String title) {
		if(title == null) {
			title = "";
		}
		if(title.length() > 60){
			title = title.substring(0, 60);
		}
		this.title = title;
		saveData("title");
	}

	/**
	 * 得到内容,2000字内
	 * @return
	 */
	public String getContent() {
		if(content == null){
			return "";
		}
		return content;
	}

	public void setContent(String content) {
		if(content == null) {
			content = "";
		}
		if(content.length() > 1000){
			content = content.substring(0, 1000);
		}
		this.content = content;
		saveData("content");
	}

	/**
	 * 得到寄送的金钱
	 * @return
	 */
	public long getCoins() {
		return coins;
	}

	public void setCoins(long coins) {
		this.coins = coins;
		saveData("coins");
		setMailMsgDate(DefaultMailManager.createMsgData(this));
	}

	/**
	 * 得到状态
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		saveData("status");
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
		saveData("createDate");
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
		saveData("expireDate");
	}

	public Cell[] getCells() {
		return cells;
	}

	public void setCells(Cell[] cells) {
		this.cells = cells;
		saveData("cells");
	}
	
	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty,String field) {
		this.dirty = dirty;
		saveData(field);
	}

	public Date getLastModifyDate() {
		return lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
		saveData("lastModifyDate");
	}
	
	public void clearCell(int index) {
		cells[index].setEntityId(-1);
		cells[index].setCount(0);
		saveData("cells");
	}
	
	public void clearAllCell() {
		for(Cell cell : cells) {
			cell.setEntityId(-1);
			cell.setCount(0);
		}
		saveData("cells");
	}
	
	/**
	 * 是否有附件
	 * @return
	 */
	public boolean hasArticleEntities() {
		for(Cell cell : cells) {
			if(cell != null && cell.getEntityId() > 0 && cell.getCount() > 0) {
				return true;
			}
		}
		return false;
	}

	public int getSize() {
		// TODO Auto-generated method stub
        int size = 0;
        size += CacheSizes.sizeOfObject();              // overhead of object
        size += CacheSizes.sizeOfLong();      // id
        size += CacheSizes.sizeOfInt();	//status
        size += CacheSizes.sizeOfLong();	//coins
        size += CacheSizes.sizeOfInt();	//poster
        size += CacheSizes.sizeOfInt();	//receiver
        size += CacheSizes.sizeOfLong();	//price
        size += CacheSizes.sizeOfInt();	//type
        size += title==null?0:title.getBytes().length;	//title
        size += content==null?0:content.getBytes().length;	//content
        for(int i=0; i<cells.length; i++) {
        	if(cells[i] != null) {
        		size += cells[i].getSize();
        	}
        }
        size += CacheSizes.sizeOfBoolean();             // dirty
        size += CacheSizes.sizeOfDate();             // lastModifyDate
        size += CacheSizes.sizeOfDate();             // createdate
        size += CacheSizes.sizeOfDate();             // expiredate

        return size;
	}

	public void remove(int type) {
		// TODO Auto-generated method stub
		if(type == CacheListener.LIFT_TIMEOUT || type == CacheListener.SIZE_OVERFLOW) {
			try{
				MailManager.getInstance().em.save(this);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			String tdesp = Translate.text_2364;
			if(type == CacheListener.SIZE_OVERFLOW) {
				tdesp = Translate.text_2365;
			}
//			MailManager.logger.info("[邮件从缓存中移除] [类型:"+tdesp+"] [ID:"+this.id+"] ["+this.getTitle()+"]");
			if(MailManager.logger.isInfoEnabled())
				MailManager.logger.info("[邮件从缓存中移除] [类型:{}] [ID:{}] [{}]", new Object[]{tdesp,this.id,this.getTitle()});
		}
	}

	/**
	 * 附件中置入一个物品
	 * @param entity
	 * @return
	 */
	public boolean put(ArticleEntity entity) {
		try {
			Article article = ArticleManager.getInstance().getArticle(entity.getArticleName());
			boolean overLaped = false;
			for(int i=0; i<cells.length; i++) {
				if(cells[i] == null) {
					cells[i] = new Cell();
					setDirty(true,"cells");
				} 
				if(!article.isOverlap()) {
					if(cells[i].getEntityId() <= 0) {
						cells[i].setEntityId(entity.getId());
						cells[i].setCount(1);
						setDirty(true,"cells");
						return true;
					}
				} else {
					if(cells[i].getEntityId() == entity.getId()) {
						int count = cells[i].getCount();
						if(count < article.getOverLapNum()) {
							cells[i].setCount(count+1);
							overLaped = true;
							setDirty(true,"cells");
							return true;
						}
					}
				}
			}
			if(!overLaped && article.isOverlap()) {
				for(int i=0; i<cells.length; i++) {
					if(cells[i] == null) {
						cells[i] = new Cell();
					}
					if(cells[i].getEntityId() <= 0) {
						cells[i].setEntityId(entity.getId());
						cells[i].setCount(1);
						setDirty(true,"cells");
						return true;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 附件中置入一个物品到指定格子
	 * @param index
	 * @param entity
	 * @return
	 */
	public boolean put(int index, ArticleEntity entity) {
		try {
			if(index >= cells.length || index < 0) {
				return false;
			}
			if(cells[index] == null) {
				cells[index] = new Cell();
				setDirty(true,"cells");
			}
			if(cells[index].getEntityId() <= 0) {
				cells[index].setEntityId(entity.getId());
				cells[index].setCount(1);
				setDirty(true,"cells");
				return true;
			} else {
				if(entity.getId() == cells[index].getEntityId()) {
					Article article = ArticleManager.getInstance().getArticle(entity.getArticleName());
					if(article.isOverlap() && cells[index].getCount() < article.getOverLapNum()) {
						cells[index].setCount(cells[index].getCount()+1);
						setDirty(true,"cells");
						return true;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 把一个格子放入邮箱附件
	 * 只有当有空格子时才能做此动作
	 * @param cell
	 * @param index
	 * @return
	 */
	public boolean putCell(int index, Cell cell) {
		if(index >= cells.length || index < 0) {
			return false;
		}
		if(this.cells[index] == null || this.cells[index].getEntityId() <= 0 || this.cells[index].getCount() <= 0) {
			this.cells[index] = cell;
			setDirty(true,"cells");
			return true;
		}
		return false;
	}
	
	/**
	 * 把一个格子替换现有的格子
	 * @param cell
	 * @param index
	 * @return
	 */
	public boolean replaceCell(Cell cell, int index) {
		if(index >= cells.length || index < 0) {
			return false;
		}
		this.cells[index] = cell;
		setDirty(true,"cells");
		return true;
	}
	
	/**
	 * 删除某个格子的所有物品
	 * @param index
	 */
	public boolean removeArticlesInCell(int index) {
		if(index >= cells.length || index < 0) {
			return false;
		}
		cells[index].setCount(0);
		cells[index].setEntityId(-1);
		setDirty(true,"cells");
		return true;
	}
	
	/**
	 * 删除某个格子的一个物品
	 * @param index
	 */
	public boolean removeOneArticleOnCell(int index) {
		if(index >= cells.length || index < 0) {
			return false;
		}
		if(cells[index].getCount() > 1) {
			cells[index].setCount(cells[index].getCount()-1);
		} else {
			cells[index].setCount(0);
			cells[index].setEntityId(-1);
		}
		setDirty(true,"cells");
		return true;
	}

	public boolean isNewFlag() {
		return newFlag;
	}

	public void setNewFlag(boolean newFlag) {
		this.newFlag = newFlag;
	}

	public String getCreateReason() {
		return createReason;
	}

	public void setCreateReason(String createReason) {
		this.createReason = createReason;
		setDirty(true,"createReason");
	}

	public void saveData(String field){
		MailManager mm = MailManager.getInstance();
		if(mm != null && mm.em != null){
			mm.em.notifyFieldChange(this, field);
		}
	}

	public void setMailMsgDate(String mailMsgDate) {
		this.mailMsgDate = mailMsgDate;
		setDirty(true, "mailMsgDate");
	}

	public String getMailMsgDate() {
		return mailMsgDate;
	}
}
