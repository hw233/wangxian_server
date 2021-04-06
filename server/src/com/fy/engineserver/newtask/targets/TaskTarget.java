package com.fy.engineserver.newtask.targets;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.actions.TaskAction;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskConfig.TargetType;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.StringTool;

public class TaskTarget {

	private TargetType targetType;

	private byte targetByteType;

	/**
	 * 任务目标,
	 * 如果长度是1则为单一条件 如果大于1则为或条件
	 * 比如 杀50个猴子 为单一条件。杀20个猴子或者20个斑马为或条件
	 * 当是或条件时候 暂不支持不同目标类型
	 * 或关系的共享任务目标数量
	 */
	private String[] targetName = new String[0];
	/** 目标所在地图信息 */
	private String[] mapName = new String[0];

	private String[] resMapName = new String[0];

	private int[] x = new int[0];
	private int[] y = new int[0];

	/** 目标颜色(品质) -1表示忽略颜色 即 任何颜色都行 */
	private int targetColor = -1;

	private int targetNum;

	private String[] showMonsterNames = new String[0];

	/** 目标数量变化时显示给玩家看的名字【聊天框】 */
	private String noticeName;

	public String toHtmlString(String cssClass) {
		return "";
	}

	/**
	 * 当接受任务时 判断任务是否已经满足完成条件/是否需要做一些特殊操作:如刷出NPC,intValue表示当时完成的数量
	 * @param task
	 */
	public CompoundReturn dealOnGet(Player player, Task task) {
		return CompoundReturn.createCompoundReturn().setBooleanValue(false).setIntValue(0);
	}

	public int getTargetColor() {
		return targetColor;
	}

	public void setTargetColor(int targetColor) {
		this.targetColor = targetColor;
	}

	/** 对目标的描述 */
	private String des;

	public TargetType getTargetType() {
		return targetType;
	}

	public void setTargetType(TargetType targetType) {
		this.targetType = targetType;
	}

	public String[] getTargetName() {
		return targetName;
	}

	public void setTargetName(String[] targetName) {
		this.targetName = targetName;
	}

	public int getTargetNum() {
		return targetNum;
	}

	public void setTargetNum(int targetNum) {
		this.targetNum = targetNum;
	}

	public String[] getMapName() {
		return mapName;
	}

	public void setMapName(String[] mapName) {
		this.mapName = mapName;
	}

	public int[] getX() {
		return x;
	}

	public void setX(int[] x) {
		this.x = x;
	}

	public int[] getY() {
		return y;
	}

	public void setY(int[] y) {
		this.y = y;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getNoticeName() {
		return noticeName;
	}

	public void setNoticeName(String noticeName) {
		this.noticeName = noticeName;
	}

	/**
	 * 默认的处理Action<BR/>
	 * 处理action的默认实现<BR/>
	 * 1.类型相同<BR/>
	 * 2.颜色相同<BR/>
	 * 3.名字相同<BR/>
	 * 其他需要各自的Target重新实现
	 * @param action
	 * @return
	 */
	public CompoundReturn dealAction(TaskAction action) {
		if (isSameType(action)) {
			if (colorFit(action)) {
				try {
					if(action.getName() != null){
						if (inTargetNames(action.getName())) {
							return CompoundReturn.createCompoundReturn().setBooleanValue(true).setIntValue(action.getNum());
						}
					}
				} catch (Exception e) {
				}
				
			}
		}
		return CompoundReturn.createCompoundReturn().setBooleanValue(false);
	}

	public void sendModifyMessage(int index, TaskEntity taskEntity) {
		if (getNoticeName() != null && !getNoticeName().isEmpty()) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(getNoticeName());
			buffer.append("<f>(</f>").append(StringTool.toColorfulMessage(taskEntity.getCompleted()[index] + "/" + taskEntity.getTaskDemand()[index], TaskConfig.noticeColor)).append("<f>)</f>");
			taskEntity.getOwner().sendNotice(buffer.toString());
		}
	}

	/**
	 * 每个任务的目标.
	 * 变化的时候的提示
	 */
	public void initNotic() {
		if (getTargetName() != null && getTargetName().length > 0) {
			StringBuffer sbf = new StringBuffer();
			Article a = ArticleManager.getInstance().getArticle(getTargetName()[0]);
			if(a instanceof InlayArticle){
//				System.out.println("[action2:"+this+"] [actionName:"+getTargetName()[0]+"] [showName00:"+((InlayArticle)a).getShowName()+"] [targetName:"+(targetName!=null?Arrays.toString(targetName):"null")+"]");
				sbf.append(((InlayArticle)a).getShowName());
			}else{
				sbf.append(getTargetName()[0]);
			}
			for (int i = 1; i < getTargetName().length; i++) {
				sbf.append("/").append(getTargetName()[i]);
			}
			setNoticeName(StringTool.toColorfulMessage(sbf.toString(), TaskConfig.noticeColor));
		}
	}

	/**
	 * 颜色是否匹配
	 * @param color
	 * @return
	 */
	protected boolean colorFit(TaskAction action) {
		return getTargetColor() == -1 ? true : getTargetColor() == action.getColor();
	}

	/**
	 * 判断名字是否在目标的名字列表//思考。颜色要不要这样做
	 * @param name
	 * @return
	 */
	protected boolean inTargetNames(String name) {
		if (getTargetName() == null || getTargetName().length == 0) {
			return false;
		}
//		System.out.println("[inTargetNames:"+name+"] [getTargetName()[0]:"+getTargetName()[0]+"] [targetName:"+(targetName!=null?Arrays.toString(targetName):"null")+"]");
		if(getTargetName().length == 1 && getTargetName()[0].contains("宝石")){
			Article a = ArticleManager.getInstance().getArticle(getTargetName()[0]);
			if(a instanceof InlayArticle){
				if(a.getName().equals(name)){
					return true;
				}
			}
		}
		
		
		for (String targetName : getTargetName()) {
			if (targetName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	// public void initResName() {
	// String[] resName = new String[targetName.length];
	// for (int i = 0; i < resName.length; i++) {
	// String name = resName[i];
	// Game game = GameManager.getInstance().getGameByDisplayName(name, CountryManager.国家A);
	// if (game == null) {
	// game = GameManager.getInstance().getGameByDisplayName(name, CountryManager.中立);
	// }
	// if (game != null) {
	// resName[i] = game.getGameInfo().name;
	// } else {
	// TaskManager.logger.error("加载任务异常:任务目标所在地图不存在:{}", new Object[] { name });
	// TaskManager.notices.append("[任务目标所在地图不存在][任务ID:").append(TaskManager.currentLoadId).append("][地图名字:").append(name).append("]<BR/>");
	// }
	// }
	// }

	/**
	 * 是否是随机个数的任务目标
	 * @return
	 */
	public boolean isRandomNum() {
		return false;
	}
	
	public String gettarDes() {
		return "未知目标类型" + this.getClass().getName();
	}
	
	public int gettarLevel() {
		return -1;
	}

	/**
	 * 类型是否一样
	 * @param action
	 * @return
	 */
	protected boolean isSameType(TaskAction action) {
		return this.getTargetType().getIndex() == action.getTargetType().getIndex();
	}

	public byte getTargetByteType() {
		return targetByteType;
	}

	public void setTargetByteType(byte targetByteType) {
		this.targetByteType = targetByteType;
	}

	public String[] getShowMonsterNames() {
		return showMonsterNames;
	}

	public void setShowMonsterNames(String[] showMonsterNames) {
		this.showMonsterNames = showMonsterNames;
	}

	public String[] getResMapName() {
		return resMapName;
	}

	public void setResMapName(String[] resMapName) {
		this.resMapName = resMapName;
	}
}
