package com.fy.engineserver.trade.boothsale;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.message.BOOTHSALE_BOOTHCHANGE_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.trade.Message;
import com.fy.engineserver.trade.MessageBoard;
import com.fy.engineserver.trade.boothsale.service.BoothsaleManager;

/**
 * 角色的摆摊信息
 * 
 * 
 */
public class BoothSale {
	/** 所有者 */
	private long owner;
	/** 摊位名称*/
	private transient String salername;
	/** 广告语 */
	private transient String advertising;
	/** 广告语的颜色 */
	private transient String color;
	/** 广告语长度 */
	private transient int advertisingLength;
	/** 信息框 */
	private transient MessageBoard board = new MessageBoard(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	/** 修改状态 */
	private boolean modify = false;

	/** 摊位内物品状态标识 */
//	private byte[] changeFalg;
	/** 观察者列表 */
	private transient List<Long> observer = new ArrayList<Long>();
	/** 背包的链接 */
	private SoftLink4Package [] link ;
	
	public BoothSale() {

	}

	public SoftLink4Package[] getLink() {
		return link;
	}

	public void setLink(SoftLink4Package[] link) {
		this.link = link;
	}

	public void clearMessage() {
		if (board != null) {
			board.clear();
		}
	}

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public void setsalername(String salername) {
		this.salername = salername;
	}

	public String getSalername() {
		return salername;
	}
	
	public List<Long> getObserver() {
		return observer;
	}

	public void setObserver(List<Long> observer) {
		this.observer = observer;
	}
//
//	public byte[] getChangeFalg() {
//		return changeFalg;
//	}
//
//	public void setChangeFalg(byte[] changeFalg) {
//		this.changeFalg = changeFalg;
//	}

	public String getAdvertising() {
		return advertising;
	}

	public void setAdvertising(String advertising) {
		this.advertising = advertising;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getAdvertisingLength() {
		return advertisingLength;
	}

	public void setAdvertisingLength(int advertisingLength) {
		this.advertisingLength = advertisingLength;
	}

	public MessageBoard getBoard() {
		return board;
	}

	public void setBoard(MessageBoard board) {
		this.board = board;
	}

	public boolean isModify() {
		return modify;
	}

	public void setModify(boolean modify) {
		this.modify = modify;
	}

	/**
	 * 摊位是否已经满了
	 * @return
	 */
	public boolean isFull() {
		for (int i = 0, j = link.length; i < j; i++) {
			if (link[i] == null || link[i].isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 增加一个物品 返回物品放在的位置
	 * @param saleItem
	 * @return
	 */
	public int addSaleItem(SoftLink4Package saleItem) {
		for (int i = 0, j = link.length; i < j; i++) {
			if (link[i] == null || link[i].isEmpty()) {
				link[i] = saleItem;
				return i;
			}
		}
		return -1;
	}

	public SoftLink4Package getSaleItemByIndex(int index) {
		return link[index];
	}
	
	/**
	 * 得到具体在包裹中的格子
	 * @param link
	 * @return
	 */
	public Cell getCell2Pag(SoftLink4Package link){
		Player player = null;
		try {
			player = PlayerManager.getInstance().getPlayer(owner);
		} catch (Exception e) {
			BoothsaleManager.logger.error("找不到摆摊玩家对象" + owner, e);
		}
		if(player==null){
			BoothsaleManager.logger.error("找不到摆摊玩家对象" + owner);
			return null;
		}
		if(link==null || link.isEmpty()){
			return null;
		}
		return player.getKnapsack_common().getCell(link.getIndexOfPackage());
	}
	
	public SoftLink4Package getItem(int index){
		return link[index];
	}

	/**
	 * 移除某个位置上的物品
	 * @param index
	 */
	public void remove(int index) {
		getLink()[index] = null;
	}
	
	public String getLogString(){
		int linkNum = 0;
		StringBuffer artName = new StringBuffer("");
		if (link != null) {
			for (int i = 0; i < link.length; i++) {
				if (link[i].getPackageType() >= 0 && link[i].getIndexOfPackage() >= 0) {
					linkNum ++;
					if (BoothsaleManager.logger.isInfoEnabled()){
						artName.append("[ID=").append(link[i].getEntityId())
						.append(",pagIndex=").append(link[i].getIndexOfPackage())
						.append(",price=").append(link[i].getPrice()).append("] ");
						;
					}
				}
			}
		}
		StringBuffer bf = new StringBuffer("");
		bf.append("[所有者=").append(owner)
		.append("] [招牌=").append(salername)
		.append("] [物品种类=").append(linkNum).append("] ");
		if (BoothsaleManager.logger.isInfoEnabled()) {
			//这个数据可能比较多，只有info才打
			bf.append("[物品详细=").append(artName.toString()).append("]");
		}
		return bf.toString();
	}

	public BoothInfo4Client getBoothInfo4Client() {
		BoothInfo4Client boothInfo4Client = new BoothInfo4Client();
		long[] aeds = new long[link.length];
		long[] perPrices = new long[link.length];
		int[] nums = new int[link.length];
		int[] knapType = new int[link.length];
		int[] knapIndex = new int[link.length];
		for (int i = 0; i < link.length; i++) {
			SoftLink4Package item = link[i];
			if (item != null && !item.isEmpty()) {
				Cell cell = getCell2Pag(item);
				if (cell == null) {
					aeds[i] = -1;
					nums[i] = -1;
					perPrices[i] = -1;
					knapType[i] = -1;
					knapIndex[i] = -1;
					continue;
				}
				aeds[i] = cell.getEntityId();
				nums[i] = cell.getCount();
				perPrices[i] = item.getPrice();
				knapType[i] = item.getPackageType();
				knapIndex[i] = item.getIndexOfPackage();
			} else {
				aeds[i] = -1;
				nums[i] = -1;
				perPrices[i] = -1;
				knapType[i] = -1;
				knapIndex[i] = -1;
			}
		}
		boothInfo4Client.setCounts(nums);
		boothInfo4Client.setEntityId(aeds);
		boothInfo4Client.setPerPrice(perPrices);
		boothInfo4Client.setKnapType(knapType);
		boothInfo4Client.setKnapIndex(knapIndex);
		return boothInfo4Client;
	}

	public void noticeChange(int[] indexs, Message[] message) {
		indexs = indexs == null ? new int[0] : indexs;
		message = message == null ? new Message[0] : message;
		long[] changedEntityId = new long[indexs.length];
		int[] changedNum = new int[indexs.length];
		long[] changedPerPrice = new long[indexs.length];

		MessageBoardInfo4Client[] info4Clients = new MessageBoardInfo4Client[message.length];
		for (int i = 0; i < indexs.length; i++) {
			SoftLink4Package item = getSaleItemByIndex(indexs[i]);
			Cell cell = getCell2Pag(item);
			if (item == null || item.isEmpty() || cell == null || cell.isEmpty()) {
				link[indexs[i]] = new SoftLink4Package(-1, -1, -1);
				changedEntityId[i] = -1;
				changedNum[i] = -1;
				changedPerPrice[i] = 0;
			} else {
				changedEntityId[i] = cell.getEntityId();
				changedNum[i] = cell.getCount();
				changedPerPrice[i] = item.getPrice();
			}
		}
		for (int i = 0; i < message.length; i++) {
			getBoard().add(message[i]);
			info4Clients[i] = message[i].getInfo();
		}
		BOOTHSALE_BOOTHCHANGE_RES res = new BOOTHSALE_BOOTHCHANGE_RES(GameMessageFactory.nextSequnceNum(), owner, indexs, changedEntityId, changedNum, changedPerPrice, info4Clients);
		Long[] ids = getObserver().toArray(new Long[0]);
		for (Long playerID : ids) {
			if (PlayerManager.getInstance().isOnline(playerID)) {
				Player player = null;
				try {
					player = PlayerManager.getInstance().getPlayer(playerID);
				} catch (Exception e) {
					BoothsaleManager.logger.error("取摆摊观察者出错:" + e);
				}
				if (player != null) {
					if (player.getSeeBoothSale() == getOwner()) {
						player.addMessageToRightBag(res);
					}else {
						getObserver().remove(playerID);
					}
				}
			}
		}
		try {
			Player onwerPlayer = PlayerManager.getInstance().getPlayer(getOwner());
			if (onwerPlayer != null) {
				onwerPlayer.addMessageToRightBag(res);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
