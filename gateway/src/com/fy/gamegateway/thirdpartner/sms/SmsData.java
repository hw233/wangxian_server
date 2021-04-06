/**
 * 
 */
package com.fy.gamegateway.thirdpartner.sms;

import java.util.Arrays;

import com.fy.gamegateway.gmaction.GmActionManager;
import com.fy.gamegateway.tools.TimeTool;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * @author Administrator
 *
 */
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"phoneNumber"})
})
public class SmsData {

	@SimpleId
	private long id;
	
	@SimpleVersion
	private int version;
	/**
	 * 手机号
	 * 短信验证码
	 * 登录账号
	 * 运营商限制同1个号码同1个签名的内容1分钟内只能接收1条，1小时内只能接收3条，一天最多接收10条，否则可能会被运营商屏蔽
	 * 
	 */
	private long phoneNumber;
	private String smsCode;
	private String userName;
	//分时天
	private long [] lastReceiverDate = new long[]{0,0,0};
	private int [] receiveNums = new int[]{0,0,0};
	
	
	public void recordSms(){
		long now = System.currentTimeMillis();
		if(!TimeTool.instance.isSame(now, lastReceiverDate[0], TimeTool.TimeDistance.MINUTE)){
			lastReceiverDate[0] = now;
			receiveNums[0] = 1;
		}else{
			receiveNums[0] += 1;
		}
		if(!TimeTool.instance.isSame(now, lastReceiverDate[1], TimeTool.TimeDistance.HOUR)){
			lastReceiverDate[1] = now;
			receiveNums[1] = 1;
		}else{
			receiveNums[1] += 1;
		}
		if(!TimeTool.instance.isSame(now, lastReceiverDate[2], TimeTool.TimeDistance.DAY)){
			lastReceiverDate[2] = now;
			receiveNums[2] = 1;
		}else{
			receiveNums[2] += 1;
		}
		GmActionManager.getInstance().smsEm.notifyFieldChange(this, "lastReceiverDate");
		GmActionManager.getInstance().smsEm.notifyFieldChange(this, "receiveNums");
	}
	
	public long[] getLastReceiverDate() {
		return lastReceiverDate;
	}
	public int[] getReceiveNums() {
		return receiveNums;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
		GmActionManager.getInstance().smsEm.notifyFieldChange(this, "phoneNumber");
	}
	public String getSmsCode() {
		return smsCode;
	}
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
		GmActionManager.getInstance().smsEm.notifyFieldChange(this, "smsCode");
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
		GmActionManager.getInstance().smsEm.notifyFieldChange(this, "userName");
	}
	@Override
	public String toString() {
		return "[id=" + id + ", lastReceiverDate="
				+ Arrays.toString(lastReceiverDate) + ", phoneNumber="
				+ phoneNumber + ", receiveNums=" + Arrays.toString(receiveNums)
				+ ", smsCode=" + smsCode + ", userName=" + userName
				+ ", version=" + version + "]";
	}
	
}
