package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.engineserver.activity.newChongZhiActivity.WeekAndMonthClientData;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 客户端请求周月反馈活动信息<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>showPlayerLevel</td><td>int</td><td>4个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys.length</td><td>int</td><td>4个字节</td><td>WeekAndMonthClientData数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[0].dataID</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[0].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[0].showRMBMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[0].needMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[0].startTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[0].startTime</td><td>String</td><td>weekActivitys[0].startTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[0].endTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[0].endTime</td><td>String</td><td>weekActivitys[0].endTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[0].rewardEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[0].rewardEntityIDs</td><td>long[]</td><td>weekActivitys[0].rewardEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[0].rewardEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[0].rewardEntityNums</td><td>int[]</td><td>weekActivitys[0].rewardEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[0].rewardRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[0].rewardRare</td><td>boolean[]</td><td>weekActivitys[0].rewardRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[0].buyEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[0].buyEntityIDs</td><td>long[]</td><td>weekActivitys[0].buyEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[0].buyEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[0].buyEntityNums</td><td>int[]</td><td>weekActivitys[0].buyEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[0].buyRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[0].buyRare</td><td>boolean[]</td><td>weekActivitys[0].buyRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[0].buyPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[0].totalMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[0].totalRMB</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[0].getValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[0].canBuy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[1].dataID</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[1].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[1].showRMBMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[1].needMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[1].startTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[1].startTime</td><td>String</td><td>weekActivitys[1].startTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[1].endTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[1].endTime</td><td>String</td><td>weekActivitys[1].endTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[1].rewardEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[1].rewardEntityIDs</td><td>long[]</td><td>weekActivitys[1].rewardEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[1].rewardEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[1].rewardEntityNums</td><td>int[]</td><td>weekActivitys[1].rewardEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[1].rewardRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[1].rewardRare</td><td>boolean[]</td><td>weekActivitys[1].rewardRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[1].buyEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[1].buyEntityIDs</td><td>long[]</td><td>weekActivitys[1].buyEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[1].buyEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[1].buyEntityNums</td><td>int[]</td><td>weekActivitys[1].buyEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[1].buyRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[1].buyRare</td><td>boolean[]</td><td>weekActivitys[1].buyRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[1].buyPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[1].totalMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[1].totalRMB</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[1].getValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[1].canBuy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[2].dataID</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[2].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[2].showRMBMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[2].needMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[2].startTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[2].startTime</td><td>String</td><td>weekActivitys[2].startTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[2].endTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[2].endTime</td><td>String</td><td>weekActivitys[2].endTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[2].rewardEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[2].rewardEntityIDs</td><td>long[]</td><td>weekActivitys[2].rewardEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[2].rewardEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[2].rewardEntityNums</td><td>int[]</td><td>weekActivitys[2].rewardEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[2].rewardRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[2].rewardRare</td><td>boolean[]</td><td>weekActivitys[2].rewardRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[2].buyEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[2].buyEntityIDs</td><td>long[]</td><td>weekActivitys[2].buyEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[2].buyEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[2].buyEntityNums</td><td>int[]</td><td>weekActivitys[2].buyEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[2].buyRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[2].buyRare</td><td>boolean[]</td><td>weekActivitys[2].buyRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[2].buyPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[2].totalMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[2].totalRMB</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>weekActivitys[2].getValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>weekActivitys[2].canBuy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td colspan=4>......... 重复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys.length</td><td>int</td><td>4个字节</td><td>WeekAndMonthClientData数组长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[0].dataID</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[0].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[0].showRMBMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[0].needMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[0].startTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[0].startTime</td><td>String</td><td>monthActivitys[0].startTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[0].endTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[0].endTime</td><td>String</td><td>monthActivitys[0].endTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[0].rewardEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[0].rewardEntityIDs</td><td>long[]</td><td>monthActivitys[0].rewardEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[0].rewardEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[0].rewardEntityNums</td><td>int[]</td><td>monthActivitys[0].rewardEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[0].rewardRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[0].rewardRare</td><td>boolean[]</td><td>monthActivitys[0].rewardRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[0].buyEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[0].buyEntityIDs</td><td>long[]</td><td>monthActivitys[0].buyEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[0].buyEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[0].buyEntityNums</td><td>int[]</td><td>monthActivitys[0].buyEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[0].buyRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[0].buyRare</td><td>boolean[]</td><td>monthActivitys[0].buyRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[0].buyPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[0].totalMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[0].totalRMB</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[0].getValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[0].canBuy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[1].dataID</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[1].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[1].showRMBMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[1].needMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[1].startTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[1].startTime</td><td>String</td><td>monthActivitys[1].startTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[1].endTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[1].endTime</td><td>String</td><td>monthActivitys[1].endTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[1].rewardEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[1].rewardEntityIDs</td><td>long[]</td><td>monthActivitys[1].rewardEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[1].rewardEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[1].rewardEntityNums</td><td>int[]</td><td>monthActivitys[1].rewardEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[1].rewardRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[1].rewardRare</td><td>boolean[]</td><td>monthActivitys[1].rewardRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[1].buyEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[1].buyEntityIDs</td><td>long[]</td><td>monthActivitys[1].buyEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[1].buyEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[1].buyEntityNums</td><td>int[]</td><td>monthActivitys[1].buyEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[1].buyRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[1].buyRare</td><td>boolean[]</td><td>monthActivitys[1].buyRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[1].buyPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[1].totalMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[1].totalRMB</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[1].getValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[1].canBuy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[2].dataID</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[2].type</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[2].showRMBMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[2].needMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[2].startTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[2].startTime</td><td>String</td><td>monthActivitys[2].startTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[2].endTime.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[2].endTime</td><td>String</td><td>monthActivitys[2].endTime.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[2].rewardEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[2].rewardEntityIDs</td><td>long[]</td><td>monthActivitys[2].rewardEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[2].rewardEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[2].rewardEntityNums</td><td>int[]</td><td>monthActivitys[2].rewardEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[2].rewardRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[2].rewardRare</td><td>boolean[]</td><td>monthActivitys[2].rewardRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[2].buyEntityIDs.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[2].buyEntityIDs</td><td>long[]</td><td>monthActivitys[2].buyEntityIDs.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[2].buyEntityNums.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[2].buyEntityNums</td><td>int[]</td><td>monthActivitys[2].buyEntityNums.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[2].buyRare.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[2].buyRare</td><td>boolean[]</td><td>monthActivitys[2].buyRare.length</td><td>*</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[2].buyPrice</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[2].totalMoney</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[2].totalRMB</td><td>long</td><td>8个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>monthActivitys[2].getValue</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>monthActivitys[2].canBuy</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class GET_WEEKANDMONTH_INFO_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	int showPlayerLevel;
	WeekAndMonthClientData[] weekActivitys;
	WeekAndMonthClientData[] monthActivitys;

	public GET_WEEKANDMONTH_INFO_RES(){
	}

	public GET_WEEKANDMONTH_INFO_RES(long seqNum,int showPlayerLevel,WeekAndMonthClientData[] weekActivitys,WeekAndMonthClientData[] monthActivitys){
		this.seqNum = seqNum;
		this.showPlayerLevel = showPlayerLevel;
		this.weekActivitys = weekActivitys;
		this.monthActivitys = monthActivitys;
	}

	public GET_WEEKANDMONTH_INFO_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		showPlayerLevel = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		weekActivitys = new WeekAndMonthClientData[len];
		for(int i = 0 ; i < weekActivitys.length ; i++){
			weekActivitys[i] = new WeekAndMonthClientData();
			weekActivitys[i].setDataID((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			weekActivitys[i].setType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			weekActivitys[i].setShowRMBMoney((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			weekActivitys[i].setNeedMoney((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			weekActivitys[i].setStartTime(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			weekActivitys[i].setEndTime(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] rewardEntityIDs_0001 = new long[len];
			for(int j = 0 ; j < rewardEntityIDs_0001.length ; j++){
				rewardEntityIDs_0001[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			weekActivitys[i].setRewardEntityIDs(rewardEntityIDs_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] rewardEntityNums_0002 = new int[len];
			for(int j = 0 ; j < rewardEntityNums_0002.length ; j++){
				rewardEntityNums_0002[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			weekActivitys[i].setRewardEntityNums(rewardEntityNums_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			boolean[] rewardRare_0003 = new boolean[len];
			for(int j = 0 ; j < rewardRare_0003.length ; j++){
				rewardRare_0003[j] = mf.byteArrayToNumber(content,offset,1) != 0;
				offset += 1;
			}
			weekActivitys[i].setRewardRare(rewardRare_0003);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] buyEntityIDs_0004 = new long[len];
			for(int j = 0 ; j < buyEntityIDs_0004.length ; j++){
				buyEntityIDs_0004[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			weekActivitys[i].setBuyEntityIDs(buyEntityIDs_0004);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] buyEntityNums_0005 = new int[len];
			for(int j = 0 ; j < buyEntityNums_0005.length ; j++){
				buyEntityNums_0005[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			weekActivitys[i].setBuyEntityNums(buyEntityNums_0005);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			boolean[] buyRare_0006 = new boolean[len];
			for(int j = 0 ; j < buyRare_0006.length ; j++){
				buyRare_0006[j] = mf.byteArrayToNumber(content,offset,1) != 0;
				offset += 1;
			}
			weekActivitys[i].setBuyRare(buyRare_0006);
			weekActivitys[i].setBuyPrice((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			weekActivitys[i].setTotalMoney((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			weekActivitys[i].setTotalRMB((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			weekActivitys[i].setGetValue((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			weekActivitys[i].setCanBuy((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		monthActivitys = new WeekAndMonthClientData[len];
		for(int i = 0 ; i < monthActivitys.length ; i++){
			monthActivitys[i] = new WeekAndMonthClientData();
			monthActivitys[i].setDataID((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			monthActivitys[i].setType((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			monthActivitys[i].setShowRMBMoney((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			monthActivitys[i].setNeedMoney((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monthActivitys[i].setStartTime(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			monthActivitys[i].setEndTime(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] rewardEntityIDs_0007 = new long[len];
			for(int j = 0 ; j < rewardEntityIDs_0007.length ; j++){
				rewardEntityIDs_0007[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			monthActivitys[i].setRewardEntityIDs(rewardEntityIDs_0007);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] rewardEntityNums_0008 = new int[len];
			for(int j = 0 ; j < rewardEntityNums_0008.length ; j++){
				rewardEntityNums_0008[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			monthActivitys[i].setRewardEntityNums(rewardEntityNums_0008);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			boolean[] rewardRare_0009 = new boolean[len];
			for(int j = 0 ; j < rewardRare_0009.length ; j++){
				rewardRare_0009[j] = mf.byteArrayToNumber(content,offset,1) != 0;
				offset += 1;
			}
			monthActivitys[i].setRewardRare(rewardRare_0009);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			long[] buyEntityIDs_0010 = new long[len];
			for(int j = 0 ; j < buyEntityIDs_0010.length ; j++){
				buyEntityIDs_0010[j] = (long)mf.byteArrayToNumber(content,offset,8);
				offset += 8;
			}
			monthActivitys[i].setBuyEntityIDs(buyEntityIDs_0010);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] buyEntityNums_0011 = new int[len];
			for(int j = 0 ; j < buyEntityNums_0011.length ; j++){
				buyEntityNums_0011[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			monthActivitys[i].setBuyEntityNums(buyEntityNums_0011);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			boolean[] buyRare_0012 = new boolean[len];
			for(int j = 0 ; j < buyRare_0012.length ; j++){
				buyRare_0012[j] = mf.byteArrayToNumber(content,offset,1) != 0;
				offset += 1;
			}
			monthActivitys[i].setBuyRare(buyRare_0012);
			monthActivitys[i].setBuyPrice((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			monthActivitys[i].setTotalMoney((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			monthActivitys[i].setTotalRMB((long)mf.byteArrayToNumber(content,offset,8));
			offset += 8;
			monthActivitys[i].setGetValue((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			monthActivitys[i].setCanBuy((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
		}
	}

	public int getType() {
		return 0x700EB103;
	}

	public String getTypeDescription() {
		return "GET_WEEKANDMONTH_INFO_RES";
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
		len += 4;
		for(int i = 0 ; i < weekActivitys.length ; i++){
			len += 4;
			len += 4;
			len += 8;
			len += 8;
			len += 2;
			if(weekActivitys[i].getStartTime() != null){
				try{
				len += weekActivitys[i].getStartTime().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(weekActivitys[i].getEndTime() != null){
				try{
				len += weekActivitys[i].getEndTime().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += weekActivitys[i].getRewardEntityIDs().length * 8;
			len += 4;
			len += weekActivitys[i].getRewardEntityNums().length * 4;
			len += 4;
			len += weekActivitys[i].getRewardRare().length * 1;
			len += 4;
			len += weekActivitys[i].getBuyEntityIDs().length * 8;
			len += 4;
			len += weekActivitys[i].getBuyEntityNums().length * 4;
			len += 4;
			len += weekActivitys[i].getBuyRare().length * 1;
			len += 8;
			len += 8;
			len += 8;
			len += 4;
			len += 4;
		}
		len += 4;
		for(int i = 0 ; i < monthActivitys.length ; i++){
			len += 4;
			len += 4;
			len += 8;
			len += 8;
			len += 2;
			if(monthActivitys[i].getStartTime() != null){
				try{
				len += monthActivitys[i].getStartTime().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 2;
			if(monthActivitys[i].getEndTime() != null){
				try{
				len += monthActivitys[i].getEndTime().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += monthActivitys[i].getRewardEntityIDs().length * 8;
			len += 4;
			len += monthActivitys[i].getRewardEntityNums().length * 4;
			len += 4;
			len += monthActivitys[i].getRewardRare().length * 1;
			len += 4;
			len += monthActivitys[i].getBuyEntityIDs().length * 8;
			len += 4;
			len += monthActivitys[i].getBuyEntityNums().length * 4;
			len += 4;
			len += monthActivitys[i].getBuyRare().length * 1;
			len += 8;
			len += 8;
			len += 8;
			len += 4;
			len += 4;
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

			buffer.putInt(showPlayerLevel);
			buffer.putInt(weekActivitys.length);
			for(int i = 0 ; i < weekActivitys.length ; i++){
				buffer.putInt((int)weekActivitys[i].getDataID());
				buffer.putInt((int)weekActivitys[i].getType());
				buffer.putLong(weekActivitys[i].getShowRMBMoney());
				buffer.putLong(weekActivitys[i].getNeedMoney());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = weekActivitys[i].getStartTime().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = weekActivitys[i].getEndTime().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(weekActivitys[i].getRewardEntityIDs().length);
				long[] rewardEntityIDs_0013 = weekActivitys[i].getRewardEntityIDs();
				for(int j = 0 ; j < rewardEntityIDs_0013.length ; j++){
					buffer.putLong(rewardEntityIDs_0013[j]);
				}
				buffer.putInt(weekActivitys[i].getRewardEntityNums().length);
				int[] rewardEntityNums_0014 = weekActivitys[i].getRewardEntityNums();
				for(int j = 0 ; j < rewardEntityNums_0014.length ; j++){
					buffer.putInt(rewardEntityNums_0014[j]);
				}
				buffer.putInt(weekActivitys[i].getRewardRare().length);
				boolean[] rewardRare_0015 = weekActivitys[i].getRewardRare();
				for(int j = 0 ; j < rewardRare_0015.length ; j++){
					buffer.put((byte)(rewardRare_0015[j]?1:0));
				}
				buffer.putInt(weekActivitys[i].getBuyEntityIDs().length);
				long[] buyEntityIDs_0016 = weekActivitys[i].getBuyEntityIDs();
				for(int j = 0 ; j < buyEntityIDs_0016.length ; j++){
					buffer.putLong(buyEntityIDs_0016[j]);
				}
				buffer.putInt(weekActivitys[i].getBuyEntityNums().length);
				int[] buyEntityNums_0017 = weekActivitys[i].getBuyEntityNums();
				for(int j = 0 ; j < buyEntityNums_0017.length ; j++){
					buffer.putInt(buyEntityNums_0017[j]);
				}
				buffer.putInt(weekActivitys[i].getBuyRare().length);
				boolean[] buyRare_0018 = weekActivitys[i].getBuyRare();
				for(int j = 0 ; j < buyRare_0018.length ; j++){
					buffer.put((byte)(buyRare_0018[j]?1:0));
				}
				buffer.putLong(weekActivitys[i].getBuyPrice());
				buffer.putLong(weekActivitys[i].getTotalMoney());
				buffer.putLong(weekActivitys[i].getTotalRMB());
				buffer.putInt((int)weekActivitys[i].getGetValue());
				buffer.putInt((int)weekActivitys[i].getCanBuy());
			}
			buffer.putInt(monthActivitys.length);
			for(int i = 0 ; i < monthActivitys.length ; i++){
				buffer.putInt((int)monthActivitys[i].getDataID());
				buffer.putInt((int)monthActivitys[i].getType());
				buffer.putLong(monthActivitys[i].getShowRMBMoney());
				buffer.putLong(monthActivitys[i].getNeedMoney());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = monthActivitys[i].getStartTime().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				try{
				tmpBytes2 = monthActivitys[i].getEndTime().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(monthActivitys[i].getRewardEntityIDs().length);
				long[] rewardEntityIDs_0019 = monthActivitys[i].getRewardEntityIDs();
				for(int j = 0 ; j < rewardEntityIDs_0019.length ; j++){
					buffer.putLong(rewardEntityIDs_0019[j]);
				}
				buffer.putInt(monthActivitys[i].getRewardEntityNums().length);
				int[] rewardEntityNums_0020 = monthActivitys[i].getRewardEntityNums();
				for(int j = 0 ; j < rewardEntityNums_0020.length ; j++){
					buffer.putInt(rewardEntityNums_0020[j]);
				}
				buffer.putInt(monthActivitys[i].getRewardRare().length);
				boolean[] rewardRare_0021 = monthActivitys[i].getRewardRare();
				for(int j = 0 ; j < rewardRare_0021.length ; j++){
					buffer.put((byte)(rewardRare_0021[j]?1:0));
				}
				buffer.putInt(monthActivitys[i].getBuyEntityIDs().length);
				long[] buyEntityIDs_0022 = monthActivitys[i].getBuyEntityIDs();
				for(int j = 0 ; j < buyEntityIDs_0022.length ; j++){
					buffer.putLong(buyEntityIDs_0022[j]);
				}
				buffer.putInt(monthActivitys[i].getBuyEntityNums().length);
				int[] buyEntityNums_0023 = monthActivitys[i].getBuyEntityNums();
				for(int j = 0 ; j < buyEntityNums_0023.length ; j++){
					buffer.putInt(buyEntityNums_0023[j]);
				}
				buffer.putInt(monthActivitys[i].getBuyRare().length);
				boolean[] buyRare_0024 = monthActivitys[i].getBuyRare();
				for(int j = 0 ; j < buyRare_0024.length ; j++){
					buffer.put((byte)(buyRare_0024[j]?1:0));
				}
				buffer.putLong(monthActivitys[i].getBuyPrice());
				buffer.putLong(monthActivitys[i].getTotalMoney());
				buffer.putLong(monthActivitys[i].getTotalRMB());
				buffer.putInt((int)monthActivitys[i].getGetValue());
				buffer.putInt((int)monthActivitys[i].getCanBuy());
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
	 *	多少级显示
	 */
	public int getShowPlayerLevel(){
		return showPlayerLevel;
	}

	/**
	 * 设置属性：
	 *	多少级显示
	 */
	public void setShowPlayerLevel(int showPlayerLevel){
		this.showPlayerLevel = showPlayerLevel;
	}

	/**
	 * 获取属性：
	 *	周活动
	 */
	public WeekAndMonthClientData[] getWeekActivitys(){
		return weekActivitys;
	}

	/**
	 * 设置属性：
	 *	周活动
	 */
	public void setWeekActivitys(WeekAndMonthClientData[] weekActivitys){
		this.weekActivitys = weekActivitys;
	}

	/**
	 * 获取属性：
	 *	月活动
	 */
	public WeekAndMonthClientData[] getMonthActivitys(){
		return monthActivitys;
	}

	/**
	 * 设置属性：
	 *	月活动
	 */
	public void setMonthActivitys(WeekAndMonthClientData[] monthActivitys){
		this.monthActivitys = monthActivitys;
	}

}