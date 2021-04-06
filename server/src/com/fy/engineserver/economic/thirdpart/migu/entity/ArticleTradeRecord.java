package com.fy.engineserver.economic.thirdpart.migu.entity;

import com.fasterxml.jackson.core.sym.Name;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
/**
 * 表名太长导致超出了索引的限制
 *
 */
@SimpleEntity(name="ArticleTradeRecord")
@SimpleIndices({
	@SimpleIndex(members={"tradeTime"},name="ATR_TT_INX"),
	@SimpleIndex(members={"buyPlayerId"},name="ATR_BP_INX"),
	@SimpleIndex(members={"sellPlayerId"},name="ATR_SP_INX"),
	@SimpleIndex(members={"desc"})
	})
public class ArticleTradeRecord implements java.io.Serializable {

	// Fields
	@SimpleId
	private long id = 0l;
	
	@SimpleVersion
	private int version;
	
	private long sellPlayerId;
	
	private long buyPlayerId;
	
	private long[] articleIds;
	
	private long tradeTime;
	
	@SimpleColumn(name="tradeDesc")
	private String desc;
	
	
	
	
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

	public long getSellPlayerId() {
		return sellPlayerId;
	}

	public void setSellPlayerId(long sellPlayerId) {
		this.sellPlayerId = sellPlayerId;
	}

	public long getBuyPlayerId() {
		return buyPlayerId;
	}

	public void setBuyPlayerId(long buyPlayerId) {
		this.buyPlayerId = buyPlayerId;
	}

	public long[] getArticleIds() {
		return articleIds;
	}

	public void setArticleIds(long[] articleIds) {
		this.articleIds = articleIds;
	}

	public long getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(long tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getLogStr() {
		return "[id:"+id+"] [version:"+version+"] [sellPlayerId:"+sellPlayerId+"] [buyPlayerId:"+buyPlayerId+"] [articleIds:"+printArticleIds()+"] [tradeTime:"+tradeTime+"] ["+desc+"]";
	}
	
	public String printArticleIds()
	{
		StringBuffer buffer = new StringBuffer();
		
		for(int i = 0; i < articleIds.length; i++)
		{
			if(i == 0)
			{
				buffer.append(articleIds[i]+"");
			}
			else
			{
				buffer.append(",");
				buffer.append(articleIds[i]+"");
			}
		}
		return buffer.toString();
	}
}