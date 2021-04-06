package com.fy.gamegateway.thirdpartner;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * ckey
渠道号,例:135331234567,由金山提供
32
clickid
点击 id,例:7894248
10
udid
苹果的 openudid,例:28c88f1c3414d576ac822e58f36d7e6c6251636d
40
mac
设备的 mac,例:c8e0eb685992
12
idfa
idfa,例:9BF9B426F3404301A9B0C2C546877006,
32
ip
用户的 ip 地址,例:128.129.130.131
15
dm
可选
设备的类型,例:iPhone6,2/iPod4,1/iPad3,4/iPhone
10
dv
可选
设备的系统版本,例:5.1.1/6.1.3/7.0.1
10
 *
 */

@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"idfa"},name="THIRDPART_DATA_IDFA_INX"),
	@SimpleIndex(members={"mac"},name="THIRDPART_DATA_MAC_INX"),
	@SimpleIndex(members={"clickid"},name="THIRDPART_DATA_CLICKID_INX"),
	@SimpleIndex(members={"createTime"},name="THIRDPART_DATA_CREATETIME_INX"),
	@SimpleIndex(members={"updateTime"},name="THIRDPART_DATA_UPDATETIME_INX"),
	@SimpleIndex(members={"status"},name="THIRDPART_DATA_STATUS_INX")
})
public class ThirdPartDataEntity {
	public static final int DATA_WAIT_VALID = 0;
	public static final int DATA_IS_INVALID = -1;
	public static final int DATA_IS_VALID = 1;
	
	
	@SimpleId
	private long id=0l;
	
	@SimpleVersion
	private int version;
	
	//金山对应于其ckey
	private String qudaohao;
	private String clickid;
	private String openudid;
	private String mac;
	private String idfa;
	private String ip;
	private String dm;
	private String dv;
	private String v;
	private String channel;
	private String clientId;
	private long createTime;
	private long updateTime;
	private int status;
	private String defaultproperty1;
	private String defaultproperty2;
	private String defaultproperty3;
	private String defaultproperty4;
	private String defaultproperty5;
	private String defaultproperty6;
	private String defaultproperty7;
	private String defaultproperty8;
	private String defaultproperty9;
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
	public String getQudaohao() {
		return qudaohao;
	}
	public void setQudaohao(String qudaohao) {
		this.qudaohao = qudaohao;
	}
	public String getClickid() {
		return clickid;
	}
	public void setClickid(String clickid) {
		this.clickid = clickid;
	}
	public String getOpenudid() {
		return openudid;
	}
	public void setOpenudid(String openudid) {
		this.openudid = openudid;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getIdfa() {
		return idfa;
	}
	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDm() {
		return dm;
	}
	public void setDm(String dm) {
		this.dm = dm;
	}
	public String getDv() {
		return dv;
	}
	public void setDv(String dv) {
		this.dv = dv;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDefaultproperty1() {
		return defaultproperty1;
	}
	public void setDefaultproperty1(String defaultproperty1) {
		this.defaultproperty1 = defaultproperty1;
	}
	public String getDefaultproperty2() {
		return defaultproperty2;
	}
	public void setDefaultproperty2(String defaultproperty2) {
		this.defaultproperty2 = defaultproperty2;
	}
	public String getDefaultproperty3() {
		return defaultproperty3;
	}
	public void setDefaultproperty3(String defaultproperty3) {
		this.defaultproperty3 = defaultproperty3;
	}
	public String getDefaultproperty4() {
		return defaultproperty4;
	}
	public void setDefaultproperty4(String defaultproperty4) {
		this.defaultproperty4 = defaultproperty4;
	}
	public String getDefaultproperty5() {
		return defaultproperty5;
	}
	public void setDefaultproperty5(String defaultproperty5) {
		this.defaultproperty5 = defaultproperty5;
	}
	public String getDefaultproperty6() {
		return defaultproperty6;
	}
	public void setDefaultproperty6(String defaultproperty6) {
		this.defaultproperty6 = defaultproperty6;
	}
	public String getDefaultproperty7() {
		return defaultproperty7;
	}
	public void setDefaultproperty7(String defaultproperty7) {
		this.defaultproperty7 = defaultproperty7;
	}
	public String getDefaultproperty8() {
		return defaultproperty8;
	}
	public void setDefaultproperty8(String defaultproperty8) {
		this.defaultproperty8 = defaultproperty8;
	}
	public String getDefaultproperty9() {
		return defaultproperty9;
	}
	public void setDefaultproperty9(String defaultproperty9) {
		this.defaultproperty9 = defaultproperty9;
	}
	
	public String getV() {
		return v;
	}
	public void setV(String v) {
		this.v = v;
	}
	
	
	public String getLogString()
	{
		String info = "["+id+"] ["+version+"] ["+qudaohao+"] ["+clickid+"] ["+openudid+"] ["+mac+"] ["+idfa+"] ["+ip+"] ["+v+"] ["+dm+"] ["+dv+"] ["+channel+"] ["+clientId+"] ["+createTime+"] ["+updateTime+"] ["+status+"] ["+defaultproperty1+"] ["+defaultproperty2+"] ["+defaultproperty3+"] ["+defaultproperty4+"] ["+defaultproperty5+"] ["+defaultproperty6+"] ["+defaultproperty7+"] ["+defaultproperty8+"] ["+defaultproperty9+"]";
		return info;
	}
}
