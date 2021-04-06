package com.fy.engineserver.notice;

import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class Notice {

	@SimpleId
	private long id;
	@SimpleVersion
	private int version;
	
	public Notice(){
		
	}
	public Notice(long id){
		this.id = id;
	}
	
	private String noticeName;
	@SimpleColumn(length = 4000)
	private String content = "";  //<c0>白</c0> <c1>绿</c1> 白绿蓝紫橙
	//内部可以看见的内容  玩家看不见
	@SimpleColumn(length = 4000)
	private String contentInside = "";
	
	//临时通知0   循环通知1   新加2  活动公告
	private int noticeType = 0;
	
	//公告创建时间
	private long createTime;
	

	//对于临时通知只有开始时间
	private long beginTime;
	private long endTime;
	
	//循环通知会有绑银
	private boolean havaBindSivler;
	private int bindSivlerNum;
	
	@SimpleColumn(name="deleted")
	private boolean delete;
	
	//心跳判断是否生效  0新建  1生效  2失效
	public int effectState ;
	
	public String qudao;
	
	//20171123,每天唯一key
	public int noticeId;
	
	public String getLogString(){
		return "id:"+id+"Name:"+noticeName+"Type:"+noticeType+"noticeId:"+noticeId;
	}
	
	/*******************************get set*********************************************************************/
	
	
	public long getId() {
		return id;
	}
	public int getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
		NoticeManager.em.notifyFieldChange(this, "noticeId");
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
		NoticeManager.em.notifyFieldChange(this, "createTime");
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNoticeName() {
		return noticeName;
	}
	public void setNoticeName(String noticeName) {
		this.noticeName = noticeName;
		NoticeManager.em.notifyFieldChange(this, "noticeName");
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
		NoticeManager.em.notifyFieldChange(this, "content");
	}
	public String getContentInside() {
		return contentInside;
	}
	public void setContentInside(String contentInside) {
		this.contentInside = contentInside;
		NoticeManager.em.notifyFieldChange(this, "contentInside");
	}
	public int getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
		NoticeManager.em.notifyFieldChange(this, "noticeType");
	}
	public long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
		NoticeManager.em.notifyFieldChange(this, "beginTime");
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
		NoticeManager.em.notifyFieldChange(this, "endTime");
	}
	public boolean isHavaBindSivler() {
		return havaBindSivler;
	}
	public void setHavaBindSivler(boolean havaBindSivler) {
		this.havaBindSivler = havaBindSivler;
		NoticeManager.em.notifyFieldChange(this, "havaBindSivler");
	}
	public int getBindSivlerNum() {
		return bindSivlerNum;
	}
	public void setBindSivlerNum(int bindSivlerNum) {
		this.bindSivlerNum = bindSivlerNum;
		NoticeManager.em.notifyFieldChange(this, "bindSivlerNum");
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
		NoticeManager.em.notifyFieldChange(this, "delete");
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Notice n = new Notice();
		n.id = id;
		n.noticeType = noticeType;
		n.noticeName = noticeName;
		n.content = content;
		n.havaBindSivler = true;
		n.bindSivlerNum = 0;
		return n;
	}

	public int getEffectState() {
		return effectState;
	}
	public void setEffectState(int effectState) {
		this.effectState = effectState;
		NoticeManager.em.notifyFieldChange(this, "effectState");
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getQudao() {
		return qudao;
	}
	public void setQudao(String qudao) {
		this.qudao = qudao;
		NoticeManager.em.notifyFieldChange(this, "qudao");
	}
	
}
