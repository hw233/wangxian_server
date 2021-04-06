package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.datasource.article.entity.client.ArticleEntity;
import com.fy.engineserver.datasource.article.entity.client.PropsEntity;
import com.fy.engineserver.datasource.article.entity.client.EquipmentEntity;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端发送物品编号，请求物品对象，可以一次请求多个物品<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities.length</td><td>int</td><td>4个字节</td><td>ArticleEntity数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[0].sellPrice</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[0].colorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[0].articleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[0].sequelType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[0].sequelPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[0].overlap</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[0].overLapNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[0].showName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[0].showName</td><td>String</td><td>articleEntities[0].showName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[0].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[0].iconId</td><td>String</td><td>articleEntities[0].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[0].binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[0].bindStyle</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[0].composeArticleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[0].knapsackType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[0].oneClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[0].oneClass</td><td>String</td><td>articleEntities[0].oneClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[0].twoClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[0].twoClass</td><td>String</td><td>articleEntities[0].twoClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[1].sellPrice</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[1].colorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[1].articleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[1].sequelType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[1].sequelPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[1].overlap</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[1].overLapNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[1].showName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[1].showName</td><td>String</td><td>articleEntities[1].showName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[1].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[1].iconId</td><td>String</td><td>articleEntities[1].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[1].binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[1].bindStyle</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[1].composeArticleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[1].knapsackType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[1].oneClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[1].oneClass</td><td>String</td><td>articleEntities[1].oneClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[1].twoClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[1].twoClass</td><td>String</td><td>articleEntities[1].twoClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[2].sellPrice</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[2].colorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[2].articleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[2].sequelType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[2].sequelPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[2].overlap</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[2].overLapNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[2].showName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[2].showName</td><td>String</td><td>articleEntities[2].showName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[2].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[2].iconId</td><td>String</td><td>articleEntities[2].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[2].binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[2].bindStyle</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[2].composeArticleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[2].knapsackType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[2].oneClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[2].oneClass</td><td>String</td><td>articleEntities[2].oneClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>articleEntities[2].twoClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>articleEntities[2].twoClass</td><td>String</td><td>articleEntities[2].twoClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities.length</td><td>int</td><td>4个字节</td><td>PropsEntity数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[0].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[0].iconId</td><td>String</td><td>propsEntities[0].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[0].binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[0].bindStyle</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[0].composeArticleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[0].sellPrice</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[0].showName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[0].showName</td><td>String</td><td>propsEntities[0].showName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[0].levelLimit</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[0].propsType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[0].categoryName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[0].categoryName</td><td>String</td><td>propsEntities[0].categoryName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[0].fightStateLimit</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[0].colorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[0].articleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[0].sequelType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[0].sequelPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[0].overlap</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[0].overLapNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[0].knapsackType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[0].oneClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[0].oneClass</td><td>String</td><td>propsEntities[0].oneClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[0].twoClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[0].twoClass</td><td>String</td><td>propsEntities[0].twoClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[1].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[1].iconId</td><td>String</td><td>propsEntities[1].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[1].binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[1].bindStyle</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[1].composeArticleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[1].sellPrice</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[1].showName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[1].showName</td><td>String</td><td>propsEntities[1].showName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[1].levelLimit</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[1].propsType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[1].categoryName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[1].categoryName</td><td>String</td><td>propsEntities[1].categoryName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[1].fightStateLimit</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[1].colorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[1].articleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[1].sequelType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[1].sequelPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[1].overlap</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[1].overLapNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[1].knapsackType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[1].oneClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[1].oneClass</td><td>String</td><td>propsEntities[1].oneClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[1].twoClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[1].twoClass</td><td>String</td><td>propsEntities[1].twoClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[2].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[2].iconId</td><td>String</td><td>propsEntities[2].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[2].binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[2].bindStyle</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[2].composeArticleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[2].sellPrice</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[2].showName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[2].showName</td><td>String</td><td>propsEntities[2].showName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[2].levelLimit</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[2].propsType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[2].categoryName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[2].categoryName</td><td>String</td><td>propsEntities[2].categoryName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[2].fightStateLimit</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[2].colorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[2].articleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[2].sequelType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[2].sequelPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[2].overlap</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[2].overLapNum</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[2].knapsackType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[2].oneClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[2].oneClass</td><td>String</td><td>propsEntities[2].oneClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>propsEntities[2].twoClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>propsEntities[2].twoClass</td><td>String</td><td>propsEntities[2].twoClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities.length</td><td>int</td><td>4个字节</td><td>EquipmentEntity数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].bindStyle</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].composeArticleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].sellPrice</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].levelLimit</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].showName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].showName</td><td>String</td><td>equipmentEntities[0].showName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].iconId</td><td>String</td><td>equipmentEntities[0].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].equipmentType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].colorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].durability</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].star</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].inscriptionFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].endowments</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].basicPropertyValue.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].basicPropertyValue</td><td>int[]</td><td>equipmentEntities[0].basicPropertyValue.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].additionPropertyValue.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].additionPropertyValue</td><td>int[]</td><td>equipmentEntities[0].additionPropertyValue.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].inlayArticleIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].inlayArticleIds</td><td>long[]</td><td>equipmentEntities[0].inlayArticleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].inlayArticleColors.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].inlayArticleColors</td><td>int[]</td><td>equipmentEntities[0].inlayArticleColors.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].createrName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].createrName</td><td>String</td><td>equipmentEntities[0].createrName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].weaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].articleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].sequelType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].sequelPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].developEXP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].developUpValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].knapsackType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].oneClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].oneClass</td><td>String</td><td>equipmentEntities[0].oneClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].twoClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].twoClass</td><td>String</td><td>equipmentEntities[0].twoClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].careerLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].strongParticles.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].strongParticles[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].strongParticles[0]</td><td>String</td><td>equipmentEntities[0].strongParticles[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].strongParticles[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].strongParticles[1]</td><td>String</td><td>equipmentEntities[0].strongParticles[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[0].strongParticles[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[0].strongParticles[2]</td><td>String</td><td>equipmentEntities[0].strongParticles[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].bindStyle</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].composeArticleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].sellPrice</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].levelLimit</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].showName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].showName</td><td>String</td><td>equipmentEntities[1].showName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].iconId</td><td>String</td><td>equipmentEntities[1].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].equipmentType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].colorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].durability</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].star</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].inscriptionFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].endowments</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].basicPropertyValue.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].basicPropertyValue</td><td>int[]</td><td>equipmentEntities[1].basicPropertyValue.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].additionPropertyValue.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].additionPropertyValue</td><td>int[]</td><td>equipmentEntities[1].additionPropertyValue.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].inlayArticleIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].inlayArticleIds</td><td>long[]</td><td>equipmentEntities[1].inlayArticleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].inlayArticleColors.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].inlayArticleColors</td><td>int[]</td><td>equipmentEntities[1].inlayArticleColors.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].createrName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].createrName</td><td>String</td><td>equipmentEntities[1].createrName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].weaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].articleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].sequelType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].sequelPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].developEXP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].developUpValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].knapsackType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].oneClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].oneClass</td><td>String</td><td>equipmentEntities[1].oneClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].twoClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].twoClass</td><td>String</td><td>equipmentEntities[1].twoClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].careerLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].strongParticles.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].strongParticles[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].strongParticles[0]</td><td>String</td><td>equipmentEntities[1].strongParticles[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].strongParticles[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].strongParticles[1]</td><td>String</td><td>equipmentEntities[1].strongParticles[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[1].strongParticles[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[1].strongParticles[2]</td><td>String</td><td>equipmentEntities[1].strongParticles[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].binded</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].bindStyle</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].composeArticleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].sellPrice</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].levelLimit</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].id</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].showName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].showName</td><td>String</td><td>equipmentEntities[2].showName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].iconId.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].iconId</td><td>String</td><td>equipmentEntities[2].iconId.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].equipmentType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].level</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].colorType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].durability</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].star</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].inscriptionFlag</td><td>boolean</td><td>1个字节</td><td>布尔型长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].endowments</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].basicPropertyValue.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].basicPropertyValue</td><td>int[]</td><td>equipmentEntities[2].basicPropertyValue.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].additionPropertyValue.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].additionPropertyValue</td><td>int[]</td><td>equipmentEntities[2].additionPropertyValue.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].inlayArticleIds.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].inlayArticleIds</td><td>long[]</td><td>equipmentEntities[2].inlayArticleIds.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].inlayArticleColors.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].inlayArticleColors</td><td>int[]</td><td>equipmentEntities[2].inlayArticleColors.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].createrName.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].createrName</td><td>String</td><td>equipmentEntities[2].createrName.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].weaponType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].articleType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].sequelType</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].sequelPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].developEXP</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].developUpValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].knapsackType</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].oneClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].oneClass</td><td>String</td><td>equipmentEntities[2].oneClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].twoClass.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].twoClass</td><td>String</td><td>equipmentEntities[2].twoClass.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].careerLimit</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].strongParticles.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].strongParticles[0].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].strongParticles[0]</td><td>String</td><td>equipmentEntities[2].strongParticles[0].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].strongParticles[1].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].strongParticles[1]</td><td>String</td><td>equipmentEntities[2].strongParticles[1].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>equipmentEntities[2].strongParticles[2].length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>equipmentEntities[2].strongParticles[2]</td><td>String</td><td>equipmentEntities[2].strongParticles[2].length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class QUERY_ARTICLE_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	ArticleEntity[] articleEntities;
	PropsEntity[] propsEntities;
	EquipmentEntity[] equipmentEntities;

	public QUERY_ARTICLE_RES(){
	}

	public QUERY_ARTICLE_RES(long seqNum,ArticleEntity[] articleEntities,PropsEntity[] propsEntities,EquipmentEntity[] equipmentEntities){
		this.seqNum = seqNum;
		this.articleEntities = articleEntities;
		this.propsEntities = propsEntities;
		this.equipmentEntities = equipmentEntities;
	}

	public QUERY_ARTICLE_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		articleEntities = new ArticleEntity[len];
		for(int i = 0 ; i < articleEntities.length ; i++){
			articleEntities[i] = new ArticleEntity();
			articleEntities[i].setSellPrice((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			articleEntities[i].setColorType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			articleEntities[i].setArticleType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			articleEntities[i].setSequelType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			articleEntities[i].setSequelPrice((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			articleEntities[i].setOverlap(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			articleEntities[i].setOverLapNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			articleEntities[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			articleEntities[i].setShowName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			articleEntities[i].setIconId(new String(content,offset,len,"UTF-8"));
			offset += len;
			articleEntities[i].setBinded(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			articleEntities[i].setBindStyle((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			articleEntities[i].setComposeArticleType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			articleEntities[i].setKnapsackType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			articleEntities[i].setOneClass(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			articleEntities[i].setTwoClass(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		propsEntities = new PropsEntity[len];
		for(int i = 0 ; i < propsEntities.length ; i++){
			propsEntities[i] = new PropsEntity();
			propsEntities[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			propsEntities[i].setIconId(new String(content,offset,len,"UTF-8"));
			offset += len;
			propsEntities[i].setBinded(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			propsEntities[i].setBindStyle((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			propsEntities[i].setComposeArticleType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			propsEntities[i].setSellPrice((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			propsEntities[i].setShowName(new String(content,offset,len,"UTF-8"));
			offset += len;
			propsEntities[i].setLevelLimit((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			propsEntities[i].setPropsType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			propsEntities[i].setCategoryName(new String(content,offset,len,"UTF-8"));
			offset += len;
			propsEntities[i].setFightStateLimit(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			propsEntities[i].setColorType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			propsEntities[i].setArticleType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			propsEntities[i].setSequelType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			propsEntities[i].setSequelPrice((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			propsEntities[i].setOverlap(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			propsEntities[i].setOverLapNum((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			propsEntities[i].setKnapsackType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			propsEntities[i].setOneClass(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			propsEntities[i].setTwoClass(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		equipmentEntities = new EquipmentEntity[len];
		for(int i = 0 ; i < equipmentEntities.length ; i++){
			equipmentEntities[i] = new EquipmentEntity();
			equipmentEntities[i].setBinded(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			equipmentEntities[i].setBindStyle((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			equipmentEntities[i].setComposeArticleType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			equipmentEntities[i].setSellPrice((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			equipmentEntities[i].setLevelLimit((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			equipmentEntities[i].setId((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			equipmentEntities[i].setShowName(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			equipmentEntities[i].setIconId(new String(content,offset,len,"UTF-8"));
			offset += len;
			equipmentEntities[i].setEquipmentType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			equipmentEntities[i].setLevel((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			equipmentEntities[i].setColorType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			equipmentEntities[i].setDurability((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			equipmentEntities[i].setStar((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			equipmentEntities[i].setInscriptionFlag(mf.byteArrayToNumber(content,offset,1) != 0);
			offset += 1;
			equipmentEntities[i].setEndowments((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] basicPropertyValue_0001 = new int[len];
			for(int j = 0 ; j < basicPropertyValue_0001.length ; j++){
				basicPropertyValue_0001[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			equipmentEntities[i].setBasicPropertyValue(basicPropertyValue_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] additionPropertyValue_0002 = new int[len];
			for(int j = 0 ; j < additionPropertyValue_0002.length ; j++){
				additionPropertyValue_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			equipmentEntities[i].setAdditionPropertyValue(additionPropertyValue_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] inlayArticleIds_0003 = new long[len];
			for(int j = 0 ; j < inlayArticleIds_0003.length ; j++){
				inlayArticleIds_0003[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			equipmentEntities[i].setInlayArticleIds(inlayArticleIds_0003);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] inlayArticleColors_0004 = new int[len];
			for(int j = 0 ; j < inlayArticleColors_0004.length ; j++){
				inlayArticleColors_0004[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			equipmentEntities[i].setInlayArticleColors(inlayArticleColors_0004);
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			equipmentEntities[i].setCreaterName(new String(content,offset,len,"UTF-8"));
			offset += len;
			equipmentEntities[i].setWeaponType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			equipmentEntities[i].setArticleType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			equipmentEntities[i].setSequelType((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			equipmentEntities[i].setSequelPrice((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			equipmentEntities[i].setDevelopEXP((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			equipmentEntities[i].setDevelopUpValue((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			equipmentEntities[i].setKnapsackType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			equipmentEntities[i].setOneClass(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			equipmentEntities[i].setTwoClass(new String(content,offset,len,"UTF-8"));
			offset += len;
			equipmentEntities[i].setCareerLimit((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			String[] strongParticles_0005 = new String[len];
			for(int j = 0 ; j < strongParticles_0005.length ; j++){
				len = (int)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
				if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
				strongParticles_0005[j] = new String(content,offset,len,"UTF-8");
				offset += len;
			}
			equipmentEntities[i].setStrongParticles(strongParticles_0005);
		}
	}

	public int getType() {
		return 0x700000F3;
	}

	public String getTypeDescription() {
		return "QUERY_ARTICLE_RES";
	}

	public String getSequenceNumAsString() {
		return String.valueOf(seqNum);
	}

	public long getSequnceNum(){
		return seqNum;
	}

	private int packet_length = 0;

	public int getLength() {
		if(packet_length > 0) return packet_length;
		int len =  mf.getNumOfByteForMessageLength() + 4 + 4;
		len += 4;
		for(int i = 0 ; i < articleEntities.length ; i++){
			len += 4;
			len += 1;
			len += 1;
			len += 1;
			len += 8;
			len += 1;
			len += 4;
			len += 8;
			len += 2;
			if(articleEntities[i].getShowName() != null){
				try{
				len += articleEntities[i].getShowName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(articleEntities[i].getIconId() != null){
				try{
				len += articleEntities[i].getIconId().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
			len += 1;
			len += 4;
			len += 2;
			if(articleEntities[i].getOneClass() != null){
				try{
				len += articleEntities[i].getOneClass().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(articleEntities[i].getTwoClass() != null){
				try{
				len += articleEntities[i].getTwoClass().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		for(int i = 0 ; i < propsEntities.length ; i++){
			len += 8;
			len += 2;
			if(propsEntities[i].getIconId() != null){
				try{
				len += propsEntities[i].getIconId().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
			len += 1;
			len += 4;
			len += 2;
			if(propsEntities[i].getShowName() != null){
				try{
				len += propsEntities[i].getShowName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			len += 1;
			len += 2;
			if(propsEntities[i].getCategoryName() != null){
				try{
				len += propsEntities[i].getCategoryName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
			len += 1;
			len += 1;
			len += 8;
			len += 1;
			len += 4;
			len += 4;
			len += 2;
			if(propsEntities[i].getOneClass() != null){
				try{
				len += propsEntities[i].getOneClass().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(propsEntities[i].getTwoClass() != null){
				try{
				len += propsEntities[i].getTwoClass().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		len += 4;
		for(int i = 0 ; i < equipmentEntities.length ; i++){
			len += 1;
			len += 1;
			len += 1;
			len += 4;
			len += 2;
			len += 8;
			len += 2;
			if(equipmentEntities[i].getShowName() != null){
				try{
				len += equipmentEntities[i].getShowName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(equipmentEntities[i].getIconId() != null){
				try{
				len += equipmentEntities[i].getIconId().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 2;
			len += 1;
			len += 1;
			len += 1;
			len += 1;
			len += 4;
			len += 4;
			len += equipmentEntities[i].getBasicPropertyValue().length * 4;
			len += 4;
			len += equipmentEntities[i].getAdditionPropertyValue().length * 4;
			len += 4;
			len += equipmentEntities[i].getInlayArticleIds().length * 8;
			len += 4;
			len += equipmentEntities[i].getInlayArticleColors().length * 4;
			len += 2;
			if(equipmentEntities[i].getCreaterName() != null){
				try{
				len += equipmentEntities[i].getCreaterName().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 1;
			len += 1;
			len += 1;
			len += 8;
			len += 4;
			len += 4;
			len += 4;
			len += 2;
			if(equipmentEntities[i].getOneClass() != null){
				try{
				len += equipmentEntities[i].getOneClass().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(equipmentEntities[i].getTwoClass() != null){
				try{
				len += equipmentEntities[i].getTwoClass().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += 4;
			String[] strongParticles = equipmentEntities[i].getStrongParticles();
			for(int j = 0 ; j < strongParticles.length; j++){
				len += 2;
				try{
					len += strongParticles[j].getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
		}
		packet_length = len;
		return len;
	}

	public int writeTo(ByteBuffer buffer) {
		int messageLength = getLength();
		if(buffer.remaining() < messageLength) return 0;
		int oldPos = buffer.position();
		buffer.mark();
		try{
			buffer.put(mf.numberToByteArray(messageLength,mf.getNumOfByteForMessageLength()));
			buffer.putInt(getType());
			buffer.putInt((int)seqNum);

			buffer.putInt(articleEntities.length);
			for(int i = 0 ; i < articleEntities.length ; i++){
				buffer.putInt((int)articleEntities[i].getSellPrice());
				buffer.put((byte)articleEntities[i].getColorType());
				buffer.put((byte)articleEntities[i].getArticleType());
				buffer.put((byte)articleEntities[i].getSequelType());
				buffer.putLong(articleEntities[i].getSequelPrice());
				buffer.put((byte)(articleEntities[i].isOverlap()==false?0:1));
				buffer.putInt((int)articleEntities[i].getOverLapNum());
				buffer.putLong(articleEntities[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = articleEntities[i].getShowName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = articleEntities[i].getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(articleEntities[i].isBinded()==false?0:1));
				buffer.put((byte)articleEntities[i].getBindStyle());
				buffer.put((byte)articleEntities[i].getComposeArticleType());
				buffer.putInt((int)articleEntities[i].getKnapsackType());
				try{
				tmpBytes2 = articleEntities[i].getOneClass().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = articleEntities[i].getTwoClass().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(propsEntities.length);
			for(int i = 0 ; i < propsEntities.length ; i++){
				buffer.putLong(propsEntities[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = propsEntities[i].getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(propsEntities[i].isBinded()==false?0:1));
				buffer.put((byte)propsEntities[i].getBindStyle());
				buffer.put((byte)propsEntities[i].getComposeArticleType());
				buffer.putInt((int)propsEntities[i].getSellPrice());
				try{
				tmpBytes2 = propsEntities[i].getShowName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putShort((short)propsEntities[i].getLevelLimit());
				buffer.put((byte)propsEntities[i].getPropsType());
				try{
				tmpBytes2 = propsEntities[i].getCategoryName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)(propsEntities[i].isFightStateLimit()==false?0:1));
				buffer.put((byte)propsEntities[i].getColorType());
				buffer.put((byte)propsEntities[i].getArticleType());
				buffer.put((byte)propsEntities[i].getSequelType());
				buffer.putLong(propsEntities[i].getSequelPrice());
				buffer.put((byte)(propsEntities[i].isOverlap()==false?0:1));
				buffer.putInt((int)propsEntities[i].getOverLapNum());
				buffer.putInt((int)propsEntities[i].getKnapsackType());
				try{
				tmpBytes2 = propsEntities[i].getOneClass().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = propsEntities[i].getTwoClass().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
			}
			buffer.putInt(equipmentEntities.length);
			for(int i = 0 ; i < equipmentEntities.length ; i++){
				buffer.put((byte)(equipmentEntities[i].isBinded()==false?0:1));
				buffer.put((byte)equipmentEntities[i].getBindStyle());
				buffer.put((byte)equipmentEntities[i].getComposeArticleType());
				buffer.putInt((int)equipmentEntities[i].getSellPrice());
				buffer.putShort((short)equipmentEntities[i].getLevelLimit());
				buffer.putLong(equipmentEntities[i].getId());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = equipmentEntities[i].getShowName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = equipmentEntities[i].getIconId().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)equipmentEntities[i].getEquipmentType());
				buffer.putShort((short)equipmentEntities[i].getLevel());
				buffer.put((byte)equipmentEntities[i].getColorType());
				buffer.put((byte)equipmentEntities[i].getDurability());
				buffer.put((byte)equipmentEntities[i].getStar());
				buffer.put((byte)(equipmentEntities[i].isInscriptionFlag()==false?0:1));
				buffer.putInt((int)equipmentEntities[i].getEndowments());
				buffer.putInt(equipmentEntities[i].getBasicPropertyValue().length);
				int[] basicPropertyValue_0006 = equipmentEntities[i].getBasicPropertyValue();
				for(int j = 0 ; j < basicPropertyValue_0006.length ; j++){
					buffer.putInt(basicPropertyValue_0006[j]);
				}
				buffer.putInt(equipmentEntities[i].getAdditionPropertyValue().length);
				int[] additionPropertyValue_0007 = equipmentEntities[i].getAdditionPropertyValue();
				for(int j = 0 ; j < additionPropertyValue_0007.length ; j++){
					buffer.putInt(additionPropertyValue_0007[j]);
				}
				buffer.putInt(equipmentEntities[i].getInlayArticleIds().length);
				long[] inlayArticleIds_0008 = equipmentEntities[i].getInlayArticleIds();
				for(int j = 0 ; j < inlayArticleIds_0008.length ; j++){
					buffer.putLong(inlayArticleIds_0008[j]);
				}
				buffer.putInt(equipmentEntities[i].getInlayArticleColors().length);
				int[] inlayArticleColors_0009 = equipmentEntities[i].getInlayArticleColors();
				for(int j = 0 ; j < inlayArticleColors_0009.length ; j++){
					buffer.putInt(inlayArticleColors_0009[j]);
				}
				try{
				tmpBytes2 = equipmentEntities[i].getCreaterName().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.put((byte)equipmentEntities[i].getWeaponType());
				buffer.put((byte)equipmentEntities[i].getArticleType());
				buffer.put((byte)equipmentEntities[i].getSequelType());
				buffer.putLong(equipmentEntities[i].getSequelPrice());
				buffer.putInt((int)equipmentEntities[i].getDevelopEXP());
				buffer.putInt((int)equipmentEntities[i].getDevelopUpValue());
				buffer.putInt((int)equipmentEntities[i].getKnapsackType());
				try{
				tmpBytes2 = equipmentEntities[i].getOneClass().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = equipmentEntities[i].getTwoClass().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt((int)equipmentEntities[i].getCareerLimit());
				buffer.putInt(equipmentEntities[i].getStrongParticles().length);
				String[] strongParticles_0010 = equipmentEntities[i].getStrongParticles();
				for(int j = 0 ; j < strongParticles_0010.length ; j++){
				try{
					buffer.putShort((short)strongParticles_0010[j].getBytes("UTF-8").length);
					buffer.put(strongParticles_0010[j].getBytes("UTF-8"));
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				}
			}
		}catch(Exception e){
		 e.printStackTrace();
			buffer.reset();
			throw new RuntimeException("in writeTo method catch exception :",e);
		}
		int newPos = buffer.position();
		buffer.position(oldPos);
		buffer.put(mf.numberToByteArray(newPos-oldPos,mf.getNumOfByteForMessageLength()));
		buffer.position(newPos);
		return newPos-oldPos;
	}

	/**
	 * 获取属性：
	 *	普通物品集合
	 */
	public ArticleEntity[] getArticleEntities(){
		return articleEntities;
	}

	/**
	 * 设置属性：
	 *	普通物品集合
	 */
	public void setArticleEntities(ArticleEntity[] articleEntities){
		this.articleEntities = articleEntities;
	}

	/**
	 * 获取属性：
	 *	可使用物品集合
	 */
	public PropsEntity[] getPropsEntities(){
		return propsEntities;
	}

	/**
	 * 设置属性：
	 *	可使用物品集合
	 */
	public void setPropsEntities(PropsEntity[] propsEntities){
		this.propsEntities = propsEntities;
	}

	/**
	 * 获取属性：
	 *	装备集合
	 */
	public EquipmentEntity[] getEquipmentEntities(){
		return equipmentEntities;
	}

	/**
	 * 设置属性：
	 *	装备集合
	 */
	public void setEquipmentEntities(EquipmentEntity[] equipmentEntities){
		this.equipmentEntities = equipmentEntities;
	}

}