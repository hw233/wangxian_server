package com.fy.engineserver.menu;
//package com.fy.engineserver.menu;
//
//import com.fy.engineserver.core.Game;
//import com.fy.engineserver.datasource.language.Translate;
//import com.fy.engineserver.message.GameMessageFactory;
//import com.fy.engineserver.message.HINT_REQ;
//import com.fy.engineserver.sprite.Player;
//import com.fy.engineserver.trade.boothsale.service.BoothsaleManager;
//import com.fy.engineserver.trade.exceptions.EntityNotFoundException;
//import com.fy.engineserver.trade.exceptions.NoEnoughMoneyException;
//import com.fy.engineserver.trade.exceptions.NoEnoughNumberException;
//import com.fy.engineserver.trade.exceptions.NoEnoughPositionException;
//import com.fy.engineserver.trade.exceptions.OutOfIndexException;
//import com.fy.engineserver.trade.exceptions.WrongStateException;
//import com.xuanzhi.tools.transport.RequestMessage;
//
//public class Option_TradeConfirm4BuyBooth extends Option {
//
//	private Player purchaser;
//	private Player saler;
//	private long itemId;
//	private int num;
//
//	public Option_TradeConfirm4BuyBooth() {
//
//	}
//
//	public Option_TradeConfirm4BuyBooth(Player purchaser, Player saler, long itemId, int num) {
//		this.purchaser = purchaser;
//		this.saler = saler;
//		this.itemId = itemId;
//		this.num = num;
//	}
//
//	@Override
//	public void doSelect(Game game, Player player) {
//		BoothsaleManager bm = BoothsaleManager.getInstance();
//		boolean canBuy = false;
//		String failreason = "";
//		try {
//			canBuy = bm.canBuy(getPurchaser(), getSaler(), getItemId(), getNum());
//		} catch (OutOfIndexException e) {
//			failreason = Translate.text_trade_004;
//		} catch (EntityNotFoundException e) {
//			failreason = Translate.text_trade_011;
//		} catch (NoEnoughNumberException e) {
//			failreason = Translate.text_trade_006;
//		} catch (NoEnoughMoneyException e) {
//			failreason = Translate.text_trade_007;
//		} catch (NoEnoughPositionException e) {
//			failreason = Translate.text_trade_008;
//		} catch (WrongStateException e) {
//			failreason = Translate.text_trade_003;
//		}
//		if (!canBuy) {
//			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, failreason);
//			player.addMessageToRightBag(req);
//			return;
//		}
//		bm.doRelBuy(purchaser, saler, itemId, num);
//
//	}
//
//	@Override
//	public byte getOType() {
//		return Option.OPTION_TYPE_SERVER_FUNCTION;
//	}
//
//	public Player getPurchaser() {
//		return purchaser;
//	}
//
//	public void setPurchaser(Player purchaser) {
//		this.purchaser = purchaser;
//	}
//
//	public Player getSaler() {
//		return saler;
//	}
//
//	public void setSaler(Player saler) {
//		this.saler = saler;
//	}
//
//	public long getItemId() {
//		return itemId;
//	}
//
//	public void setItemId(long itemId) {
//		this.itemId = itemId;
//	}
//
//	public int getNum() {
//		return num;
//	}
//
//	public void setNum(int num) {
//		this.num = num;
//	}
//}
