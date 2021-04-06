package com.fy.engineserver.menu;

import com.fy.engineserver.constants.InitialPlayerConstant;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.exception.WrongFormatMailException;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuCreateException;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.mail.Mail;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.mail.service.concrete.DefaultMailManager;
import com.fy.engineserver.message.JIAZU_CREATE_REQ;
import com.fy.engineserver.message.JIAZU_CREATE_RES;
import com.fy.engineserver.newtask.service.TaskConfig.ModifyType;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.exception.BillFailedException;
import com.fy.boss.authorize.exception.NoEnoughMoneyException;

public class Option_JiazuCreate_confirm extends Option {
	JIAZU_CREATE_REQ req;

	public Option_JiazuCreate_confirm(JIAZU_CREATE_REQ req) {
		this.req = req;
	}

	@Override
	public int getOId() {
		// TODO Auto-generated method stub
		return OptionConstants.SERVER_FUNCTION_CREATE_JIAZU;
	}

	@Override
	public String getOptionId() {
		// TODO Auto-generated method stub
		return super.getOptionId();
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		JiazuManager jiazuManager = JiazuManager.getInstance();
		MailManager mailManager = DefaultMailManager.getInstance();
		String jiazuName = req.getJiazuname();
		if (player.getJiazuName() != null && player.getJiazuName().trim().length() > 0) {
			JIAZU_CREATE_RES res = new JIAZU_CREATE_RES(req.getSequnceNum(), (byte) 1, Translate.text_6087);
			player.addMessageToRightBag(res);
		} else if (player.getLevel() < JiazuSubSystem.CREATE_JIAZU_LEVLE) {
			JIAZU_CREATE_RES res = new JIAZU_CREATE_RES(req.getSequnceNum(), (byte) 1, Translate.text_6088);
			player.addMessageToRightBag(res);
		} else if (req.getJiazuname() == null || req.getJiazuname().length() == 0) {
			JIAZU_CREATE_RES res = new JIAZU_CREATE_RES(req.getSequnceNum(), (byte) 1, Translate.text_6089);
			player.addMessageToRightBag(res);
		} else {
			try {
				Jiazu jiazu = jiazuManager.createJiazu(player, jiazuName, req.getSlogan());
				JIAZU_CREATE_RES res = new JIAZU_CREATE_RES(req.getSequnceNum(), (byte) 0, Translate.text_6096);
				player.addMessageToRightBag(res);
				player.setJiazuName(jiazuName);
				player.initJiazuTitleAndIcon();
				if (!player.bindSilverEnough(InitialPlayerConstant.JAIZU_CREATE_EXPENSE)) {
					player.sendError(Translate.translateString(Translate.text_jiazu_006, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(InitialPlayerConstant.JAIZU_CREATE_EXPENSE) } }));
					return;
				}
				BillingCenter.getInstance().playerExpense(player, InitialPlayerConstant.JAIZU_CREATE_EXPENSE, CurrencyType.BIND_YINZI, ExpenseReasonType.JIAZU_CREATE, "");
				// 更新任务
				player.checkFunctionNPCModify(ModifyType.JIAZU_GOT);
				player.checkFunctionNPCModify(ModifyType.JIAZU_TITLE_MODIFY);

				jiazu.addContructionConsume(ExpenseReasonType.JIAZU_CREATE);
				jiazu.setCountry(player.getCountry());

				player.sendError(Translate.text_jiazu_015);
				if (JiazuSubSystem.logger.isWarnEnabled()) {
					JiazuSubSystem.logger.warn(player.getLogString() + "[申请创建家族] [成功] [家族名字:{}] [标语:{}]", new Object[] { jiazuName, req.getSlogan() });
				}
				// 发送邮件
				if (mailManager != null) {
					Mail mail = new Mail();
					mail.setPoster(-1);
					mail.setContent(Translate.text_6097 + req.getJiazuname() + Translate.text_6098);
					mail.setReceiver(player.getId());
					mail.setTitle(Translate.text_6349);
					try {
						mail = mailManager.createMail(mail);
						if (MailManager.logger.isInfoEnabled()) MailManager.logger.info("[创建邮件][家族创建邮件] [成功] [邮件id:{}]  [接受人:{}/{}/{}] [{}] [{}]", new Object[] { mail.getId(), player.getId(), player.getName(), player.getUsername(), mail.getTitle(), mail.getContent() });
					} catch (WrongFormatMailException e) {
						mail.setCoins(0);
						mail.setLastModifyDate(new java.util.Date());
						MailManager.logger.error("[创建邮件][家族创建邮件] [异常,中断群发] [邮件id:" + mail.getId() + "]  [接收人:" + player.getId() + "/" + player.getName() + "/" + player.getUsername() + "]", e);
					}
				}
			} catch (JiazuCreateException e) {
				JIAZU_CREATE_RES res = new JIAZU_CREATE_RES(req.getSequnceNum(), (byte) 1, e.getMessage());
				player.addMessageToRightBag(res);
				JiazuSubSystem.logger.error(player.getLogString() + "[创建家族异常]", e);
			} catch (NoEnoughMoneyException e) {
				JiazuSubSystem.logger.error(player.getLogString() + "[创建家族异常]", e);
			} catch (BillFailedException e) {
				JiazuSubSystem.logger.error(player.getLogString() + "[创建家族异常]", e);
			} catch (Exception e) {
				JiazuSubSystem.logger.error(player.getLogString() + "[创建家族异常]", e);
			}
		}
	}

}
