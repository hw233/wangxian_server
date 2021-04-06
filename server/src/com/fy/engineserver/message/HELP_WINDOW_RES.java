package com.fy.engineserver.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;

import com.fy.gamebase.help.HelpMessage;


/**
 * 网络数据包，此数据包是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 窗口的帮助请求<br>
 * 数据包的格式如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>字段名</td><td>数据类型</td><td>长度（字节数）</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td>length</td><td>int</td><td>getNumOfByteForMessageLength()个字节</td><td>包的整体长度，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>type</td><td>int</td><td>4个字节</td><td>包的类型，包头的一部分</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>seqNum</td><td>int</td><td>4个字节</td><td>包的序列号，包头的一部分</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpType</td><td>byte</td><td>1个字节</td><td>配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>id.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>id</td><td>String</td><td>id.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages.length</td><td>int</td><td>4个字节</td><td>HelpMessage数组长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].rectx</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].recty</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].rectw</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].recth</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].rectcolor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].rectlinew</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].lineX1.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].lineX1</td><td>short[]</td><td>helpMessages[0].lineX1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].lineY1.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].lineY1</td><td>short[]</td><td>helpMessages[0].lineY1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].lineX2.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].lineX2</td><td>short[]</td><td>helpMessages[0].lineX2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].lineY2.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].lineY2</td><td>short[]</td><td>helpMessages[0].lineY2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].lineColor.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].lineColor</td><td>int[]</td><td>helpMessages[0].lineColor.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].lineW.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].lineW</td><td>byte[]</td><td>helpMessages[0].lineW.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].btnx</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].btny</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].btnFontSize</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].btnColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].btnText.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].btnText</td><td>String</td><td>helpMessages[0].btnText.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].bubbleLineX1.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].bubbleLineX1</td><td>short[]</td><td>helpMessages[0].bubbleLineX1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].bubbleLineY1.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].bubbleLineY1</td><td>short[]</td><td>helpMessages[0].bubbleLineY1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].bubbleLineX2.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].bubbleLineX2</td><td>short[]</td><td>helpMessages[0].bubbleLineX2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].bubbleLineY2.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].bubbleLineY2</td><td>short[]</td><td>helpMessages[0].bubbleLineY2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].bubbleLineColor.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].bubbleLineColor</td><td>int[]</td><td>helpMessages[0].bubbleLineColor.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].bubbleLineW.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].bubbleLineW</td><td>byte[]</td><td>helpMessages[0].bubbleLineW.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].bubbleContentX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].bubbleContentY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].bubbleContentW</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].bubbleContentH</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].bubbleFontSize</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].bubbleTextColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[0].bubbleContent.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[0].bubbleContent</td><td>String</td><td>helpMessages[0].bubbleContent.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].rectx</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].recty</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].rectw</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].recth</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].rectcolor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].rectlinew</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].lineX1.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].lineX1</td><td>short[]</td><td>helpMessages[1].lineX1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].lineY1.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].lineY1</td><td>short[]</td><td>helpMessages[1].lineY1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].lineX2.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].lineX2</td><td>short[]</td><td>helpMessages[1].lineX2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].lineY2.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].lineY2</td><td>short[]</td><td>helpMessages[1].lineY2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].lineColor.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].lineColor</td><td>int[]</td><td>helpMessages[1].lineColor.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].lineW.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].lineW</td><td>byte[]</td><td>helpMessages[1].lineW.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].btnx</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].btny</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].btnFontSize</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].btnColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].btnText.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].btnText</td><td>String</td><td>helpMessages[1].btnText.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].bubbleLineX1.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].bubbleLineX1</td><td>short[]</td><td>helpMessages[1].bubbleLineX1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].bubbleLineY1.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].bubbleLineY1</td><td>short[]</td><td>helpMessages[1].bubbleLineY1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].bubbleLineX2.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].bubbleLineX2</td><td>short[]</td><td>helpMessages[1].bubbleLineX2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].bubbleLineY2.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].bubbleLineY2</td><td>short[]</td><td>helpMessages[1].bubbleLineY2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].bubbleLineColor.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].bubbleLineColor</td><td>int[]</td><td>helpMessages[1].bubbleLineColor.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].bubbleLineW.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].bubbleLineW</td><td>byte[]</td><td>helpMessages[1].bubbleLineW.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].bubbleContentX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].bubbleContentY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].bubbleContentW</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].bubbleContentH</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].bubbleFontSize</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].bubbleTextColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[1].bubbleContent.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[1].bubbleContent</td><td>String</td><td>helpMessages[1].bubbleContent.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].rectx</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].recty</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].rectw</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].recth</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].rectcolor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].rectlinew</td><td>byte</td><td>1个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].lineX1.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].lineX1</td><td>short[]</td><td>helpMessages[2].lineX1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].lineY1.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].lineY1</td><td>short[]</td><td>helpMessages[2].lineY1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].lineX2.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].lineX2</td><td>short[]</td><td>helpMessages[2].lineX2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].lineY2.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].lineY2</td><td>short[]</td><td>helpMessages[2].lineY2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].lineColor.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].lineColor</td><td>int[]</td><td>helpMessages[2].lineColor.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].lineW.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].lineW</td><td>byte[]</td><td>helpMessages[2].lineW.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].btnx</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].btny</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].btnFontSize</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].btnColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].btnText.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].btnText</td><td>String</td><td>helpMessages[2].btnText.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].bubbleLineX1.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].bubbleLineX1</td><td>short[]</td><td>helpMessages[2].bubbleLineX1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].bubbleLineY1.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].bubbleLineY1</td><td>short[]</td><td>helpMessages[2].bubbleLineY1.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].bubbleLineX2.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].bubbleLineX2</td><td>short[]</td><td>helpMessages[2].bubbleLineX2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].bubbleLineY2.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].bubbleLineY2</td><td>short[]</td><td>helpMessages[2].bubbleLineY2.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].bubbleLineColor.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].bubbleLineColor</td><td>int[]</td><td>helpMessages[2].bubbleLineColor.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].bubbleLineW.length</td><td>int</td><td>4个字节</td><td>对象属性为数组，数组的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].bubbleLineW</td><td>byte[]</td><td>helpMessages[2].bubbleLineW.length</td><td>*</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].bubbleContentX</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].bubbleContentY</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].bubbleContentW</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].bubbleContentH</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].bubbleFontSize</td><td>short</td><td>2个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].bubbleTextColor</td><td>int</td><td>4个字节</td><td>对象属性配置的长度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td>helpMessages[2].bubbleContent.length</td><td>short</td><td>2个字节</td><td>字符串实际长度</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td>helpMessages[2].bubbleContent</td><td>String</td><td>helpMessages[2].bubbleContent.length</td><td>字符串对应的byte数组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td colspan=4>......... 重复</td></tr>
 * </table>
 */
public class HELP_WINDOW_RES implements ResponseMessage{

	static GameMessageFactory mf = GameMessageFactory.getInstance();

	long seqNum;
	byte helpType;
	String id;
	HelpMessage[] helpMessages;

	public HELP_WINDOW_RES(){
	}

	public HELP_WINDOW_RES(long seqNum,byte helpType,String id,HelpMessage[] helpMessages){
		this.seqNum = seqNum;
		this.helpType = helpType;
		this.id = id;
		this.helpMessages = helpMessages;
	}

	public HELP_WINDOW_RES(long seqNum,byte[] content,int offset,int size) throws Exception{
		this.seqNum = seqNum;
		helpType = (byte)mf.byteArrayToNumber(content,offset,1);
		offset += 1;
		int len = 0;
		len = (int)mf.byteArrayToNumber(content,offset,2);
		offset += 2;
		if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
		id = new String(content,offset,len,"UTF-8");
		offset += len;
		len = (int)mf.byteArrayToNumber(content,offset,4);
		offset += 4;
		if(len < 0 || len > 8192) throw new Exception("object array length ["+len+"] big than the max length [8192]");
		helpMessages = new HelpMessage[len];
		for(int i = 0 ; i < helpMessages.length ; i++){
			helpMessages[i] = new HelpMessage();
			helpMessages[i].setRectx((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			helpMessages[i].setRecty((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			helpMessages[i].setRectw((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			helpMessages[i].setRecth((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			helpMessages[i].setRectcolor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			helpMessages[i].setRectlinew((byte)mf.byteArrayToNumber(content,offset,1));
			offset += 1;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] lineX1_0001 = new short[len];
			for(int j = 0 ; j < lineX1_0001.length ; j++){
				lineX1_0001[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			helpMessages[i].setLineX1(lineX1_0001);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] lineY1_0002 = new short[len];
			for(int j = 0 ; j < lineY1_0002.length ; j++){
				lineY1_0002[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			helpMessages[i].setLineY1(lineY1_0002);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] lineX2_0003 = new short[len];
			for(int j = 0 ; j < lineX2_0003.length ; j++){
				lineX2_0003[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			helpMessages[i].setLineX2(lineX2_0003);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] lineY2_0004 = new short[len];
			for(int j = 0 ; j < lineY2_0004.length ; j++){
				lineY2_0004[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			helpMessages[i].setLineY2(lineY2_0004);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] lineColor_0005 = new int[len];
			for(int j = 0 ; j < lineColor_0005.length ; j++){
				lineColor_0005[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			helpMessages[i].setLineColor(lineColor_0005);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] lineW_0006 = new byte[len];
			System.arraycopy(content,offset,lineW_0006,0,len);
			offset += len;
			helpMessages[i].setLineW(lineW_0006);
			helpMessages[i].setBtnx((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			helpMessages[i].setBtny((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			helpMessages[i].setBtnFontSize((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			helpMessages[i].setBtnColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			helpMessages[i].setBtnText(new String(content,offset,len,"UTF-8"));
			offset += len;
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bubbleLineX1_0007 = new short[len];
			for(int j = 0 ; j < bubbleLineX1_0007.length ; j++){
				bubbleLineX1_0007[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			helpMessages[i].setBubbleLineX1(bubbleLineX1_0007);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bubbleLineY1_0008 = new short[len];
			for(int j = 0 ; j < bubbleLineY1_0008.length ; j++){
				bubbleLineY1_0008[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			helpMessages[i].setBubbleLineY1(bubbleLineY1_0008);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bubbleLineX2_0009 = new short[len];
			for(int j = 0 ; j < bubbleLineX2_0009.length ; j++){
				bubbleLineX2_0009[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			helpMessages[i].setBubbleLineX2(bubbleLineX2_0009);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			short[] bubbleLineY2_0010 = new short[len];
			for(int j = 0 ; j < bubbleLineY2_0010.length ; j++){
				bubbleLineY2_0010[j] = (short)mf.byteArrayToNumber(content,offset,2);
				offset += 2;
			}
			helpMessages[i].setBubbleLineY2(bubbleLineY2_0010);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			int[] bubbleLineColor_0011 = new int[len];
			for(int j = 0 ; j < bubbleLineColor_0011.length ; j++){
				bubbleLineColor_0011[j] = (int)mf.byteArrayToNumber(content,offset,4);
				offset += 4;
			}
			helpMessages[i].setBubbleLineColor(bubbleLineColor_0011);
			len = (int)mf.byteArrayToNumber(content,offset,4);
			offset += 4;
			if(len < 0 || len > 4096) throw new Exception("array length ["+len+"] big than the max length [4096]");
			byte[] bubbleLineW_0012 = new byte[len];
			System.arraycopy(content,offset,bubbleLineW_0012,0,len);
			offset += len;
			helpMessages[i].setBubbleLineW(bubbleLineW_0012);
			helpMessages[i].setBubbleContentX((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			helpMessages[i].setBubbleContentY((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			helpMessages[i].setBubbleContentW((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			helpMessages[i].setBubbleContentH((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			helpMessages[i].setBubbleFontSize((short)mf.byteArrayToNumber(content,offset,2));
			offset += 2;
			helpMessages[i].setBubbleTextColor((int)mf.byteArrayToNumber(content,offset,4));
			offset += 4;
			len = (int)mf.byteArrayToNumber(content,offset,2);
			offset += 2;
			if(len < 0 || len > 16384) throw new Exception("string length ["+len+"] big than the max length [16384]");
			helpMessages[i].setBubbleContent(new String(content,offset,len,"UTF-8"));
			offset += len;
		}
	}

	public int getType() {
		return 0x70000F21;
	}

	public String getTypeDescription() {
		return "HELP_WINDOW_RES";
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
		len += 1;
		len += 2;
		try{
			len +=id.getBytes("UTF-8").length;
		}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
			throw new RuntimeException("unsupported encoding [UTF-8]",e);
		}
		len += 4;
		for(int i = 0 ; i < helpMessages.length ; i++){
			len += 2;
			len += 2;
			len += 2;
			len += 2;
			len += 4;
			len += 1;
			len += 4;
			len += helpMessages[i].getLineX1().length * 2;
			len += 4;
			len += helpMessages[i].getLineY1().length * 2;
			len += 4;
			len += helpMessages[i].getLineX2().length * 2;
			len += 4;
			len += helpMessages[i].getLineY2().length * 2;
			len += 4;
			len += helpMessages[i].getLineColor().length * 4;
			len += 4;
			len += helpMessages[i].getLineW().length * 1;
			len += 2;
			len += 2;
			len += 2;
			len += 4;
			len += 2;
			if(helpMessages[i].getBtnText() != null){
				try{
				len += helpMessages[i].getBtnText().getBytes("UTF-8").length;
				}catch(java.io.UnsupportedEncodingException e){
		 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			}
			len += 4;
			len += helpMessages[i].getBubbleLineX1().length * 2;
			len += 4;
			len += helpMessages[i].getBubbleLineY1().length * 2;
			len += 4;
			len += helpMessages[i].getBubbleLineX2().length * 2;
			len += 4;
			len += helpMessages[i].getBubbleLineY2().length * 2;
			len += 4;
			len += helpMessages[i].getBubbleLineColor().length * 4;
			len += 4;
			len += helpMessages[i].getBubbleLineW().length * 1;
			len += 2;
			len += 2;
			len += 2;
			len += 2;
			len += 2;
			len += 4;
			len += 2;
			if(helpMessages[i].getBubbleContent() != null){
				try{
				len += helpMessages[i].getBubbleContent().getBytes("UTF-8").length;
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

			buffer.put(helpType);
			byte[] tmpBytes1;
				try{
			 tmpBytes1 = id.getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
			buffer.putShort((short)tmpBytes1.length);
			buffer.put(tmpBytes1);
			buffer.putInt(helpMessages.length);
			for(int i = 0 ; i < helpMessages.length ; i++){
				buffer.putShort((short)helpMessages[i].getRectx());
				buffer.putShort((short)helpMessages[i].getRecty());
				buffer.putShort((short)helpMessages[i].getRectw());
				buffer.putShort((short)helpMessages[i].getRecth());
				buffer.putInt((int)helpMessages[i].getRectcolor());
				buffer.put((byte)helpMessages[i].getRectlinew());
				buffer.putInt(helpMessages[i].getLineX1().length);
				short[] lineX1_0013 = helpMessages[i].getLineX1();
				for(int j = 0 ; j < lineX1_0013.length ; j++){
					buffer.putShort(lineX1_0013[j]);
				}
				buffer.putInt(helpMessages[i].getLineY1().length);
				short[] lineY1_0014 = helpMessages[i].getLineY1();
				for(int j = 0 ; j < lineY1_0014.length ; j++){
					buffer.putShort(lineY1_0014[j]);
				}
				buffer.putInt(helpMessages[i].getLineX2().length);
				short[] lineX2_0015 = helpMessages[i].getLineX2();
				for(int j = 0 ; j < lineX2_0015.length ; j++){
					buffer.putShort(lineX2_0015[j]);
				}
				buffer.putInt(helpMessages[i].getLineY2().length);
				short[] lineY2_0016 = helpMessages[i].getLineY2();
				for(int j = 0 ; j < lineY2_0016.length ; j++){
					buffer.putShort(lineY2_0016[j]);
				}
				buffer.putInt(helpMessages[i].getLineColor().length);
				int[] lineColor_0017 = helpMessages[i].getLineColor();
				for(int j = 0 ; j < lineColor_0017.length ; j++){
					buffer.putInt(lineColor_0017[j]);
				}
				buffer.putInt(helpMessages[i].getLineW().length);
				buffer.put(helpMessages[i].getLineW());
				buffer.putShort((short)helpMessages[i].getBtnx());
				buffer.putShort((short)helpMessages[i].getBtny());
				buffer.putShort((short)helpMessages[i].getBtnFontSize());
				buffer.putInt((int)helpMessages[i].getBtnColor());
				byte[] tmpBytes2 ;
				try{
				tmpBytes2 = helpMessages[i].getBtnText().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
				buffer.putInt(helpMessages[i].getBubbleLineX1().length);
				short[] bubbleLineX1_0018 = helpMessages[i].getBubbleLineX1();
				for(int j = 0 ; j < bubbleLineX1_0018.length ; j++){
					buffer.putShort(bubbleLineX1_0018[j]);
				}
				buffer.putInt(helpMessages[i].getBubbleLineY1().length);
				short[] bubbleLineY1_0019 = helpMessages[i].getBubbleLineY1();
				for(int j = 0 ; j < bubbleLineY1_0019.length ; j++){
					buffer.putShort(bubbleLineY1_0019[j]);
				}
				buffer.putInt(helpMessages[i].getBubbleLineX2().length);
				short[] bubbleLineX2_0020 = helpMessages[i].getBubbleLineX2();
				for(int j = 0 ; j < bubbleLineX2_0020.length ; j++){
					buffer.putShort(bubbleLineX2_0020[j]);
				}
				buffer.putInt(helpMessages[i].getBubbleLineY2().length);
				short[] bubbleLineY2_0021 = helpMessages[i].getBubbleLineY2();
				for(int j = 0 ; j < bubbleLineY2_0021.length ; j++){
					buffer.putShort(bubbleLineY2_0021[j]);
				}
				buffer.putInt(helpMessages[i].getBubbleLineColor().length);
				int[] bubbleLineColor_0022 = helpMessages[i].getBubbleLineColor();
				for(int j = 0 ; j < bubbleLineColor_0022.length ; j++){
					buffer.putInt(bubbleLineColor_0022[j]);
				}
				buffer.putInt(helpMessages[i].getBubbleLineW().length);
				buffer.put(helpMessages[i].getBubbleLineW());
				buffer.putShort((short)helpMessages[i].getBubbleContentX());
				buffer.putShort((short)helpMessages[i].getBubbleContentY());
				buffer.putShort((short)helpMessages[i].getBubbleContentW());
				buffer.putShort((short)helpMessages[i].getBubbleContentH());
				buffer.putShort((short)helpMessages[i].getBubbleFontSize());
				buffer.putInt((int)helpMessages[i].getBubbleTextColor());
				try{
				tmpBytes2 = helpMessages[i].getBubbleContent().getBytes("UTF-8");
				}catch(java.io.UnsupportedEncodingException e){
			 e.printStackTrace();
					throw new RuntimeException("unsupported encoding [UTF-8]",e);
				}
				buffer.putShort((short)tmpBytes2.length);
				buffer.put(tmpBytes2);
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
	 *	0:窗口的帮助,1-主界面的帮助
	 */
	public byte getHelpType(){
		return helpType;
	}

	/**
	 * 设置属性：
	 *	0:窗口的帮助,1-主界面的帮助
	 */
	public void setHelpType(byte helpType){
		this.helpType = helpType;
	}

	/**
	 * 获取属性：
	 *	关键字，对窗口而言是窗口的ID
	 */
	public String getId(){
		return id;
	}

	/**
	 * 设置属性：
	 *	关键字，对窗口而言是窗口的ID
	 */
	public void setId(String id){
		this.id = id;
	}

	/**
	 * 获取属性：
	 *	帮组信息
	 */
	public HelpMessage[] getHelpMessages(){
		return helpMessages;
	}

	/**
	 * 设置属性：
	 *	帮组信息
	 */
	public void setHelpMessages(HelpMessage[] helpMessages){
		this.helpMessages = helpMessages;
	}

}