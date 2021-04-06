package com.fy.gamegateway.message;

import com.xuanzhi.tools.transport.*;
import java.nio.ByteBuffer;


/**
 * 消息工厂类，此工厂类是由MessageComplier自动生成，请不要手动修改。<br>
 * 版本号：null<br>
 * 各数据包的定义如下：<br><br>
 * <table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
 * <tr bgcolor="#00FFFF" align="center"><td>请求消息</td><td>类型</td><td>响应消息</td><td>类型</td><td>说明</td></tr> * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HEARTBEAT_CHECK_REQ.html'>HEARTBEAT_CHECK_REQ</a></td><td>0x00000015</td><td><a href='./HEARTBEAT_CHECK_RES.html'>HEARTBEAT_CHECK_RES</a></td><td>0x80000015</td><td>心跳频率检测包，客户端每5秒发送一次</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_DISPLAYER_INFO_REQ.html'>QUERY_DISPLAYER_INFO_REQ</a></td><td>0x0000E002</td><td><a href='./QUERY_DISPLAYER_INFO_RES.html'>QUERY_DISPLAYER_INFO_RES</a></td><td>0x8000E002</td><td>获取与渠道相关的显示内容，</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PASSPORT_REGISTER_REQ.html'>PASSPORT_REGISTER_REQ</a></td><td>0x0000E001</td><td><a href='./PASSPORT_REGISTER_RES.html'>PASSPORT_REGISTER_RES</a></td><td>0x8000E001</td><td>注册一个通行证,返回推荐关系</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_REGISTER_IMAGE_REQ.html'>GET_REGISTER_IMAGE_REQ</a></td><td>0x0000E005</td><td><a href='./GET_REGISTER_IMAGE_RES.html'>GET_REGISTER_IMAGE_RES</a></td><td>0x8000E005</td><td>获取注册验证码</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_REGISTER_IMAGE_NEW_REQ.html'>GET_REGISTER_IMAGE_NEW_REQ</a></td><td>0x0000E006</td><td><a href='./GET_REGISTER_IMAGE_NEW_RES.html'>GET_REGISTER_IMAGE_NEW_RES</a></td><td>0x8000E006</td><td>获取注册验证码</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PASSPORT_REGISTER_NEW_REQ.html'>PASSPORT_REGISTER_NEW_REQ</a></td><td>0x0000E004</td><td><a href='./PASSPORT_REGISTER_NEW_RES.html'>PASSPORT_REGISTER_NEW_RES</a></td><td>0x8000E004</td><td>注册一个通行证,返回推荐关系</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PASSPORT_REGISTER_PRO_REQ.html'>PASSPORT_REGISTER_PRO_REQ</a></td><td>0x0000E010</td><td><a href='./PASSPORT_REGISTER_PRO_RES.html'>PASSPORT_REGISTER_PRO_RES</a></td><td>0x8000E010</td><td>注册一个通行证,返回推荐关系</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SEND_BANHAO_TO_CLIENT_REQ.html'>SEND_BANHAO_TO_CLIENT_REQ</a></td><td>0x0000E011</td><td><a href='./-.html'>-</a></td><td>-</td><td>发送给客户端版号信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./USER_GETBACK_PASSWORD_REQ.html'>USER_GETBACK_PASSWORD_REQ</a></td><td>0x0000F009</td><td><a href='./USER_GETBACK_PASSWORD_RES.html'>USER_GETBACK_PASSWORD_RES</a></td><td>0x8000F009</td><td>找回密码</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./USER_GET_PASSWORD_PROTECT_INFO_REQ.html'>USER_GET_PASSWORD_PROTECT_INFO_REQ</a></td><td>0x0000F010</td><td><a href='./USER_GET_PASSWORD_PROTECT_INFO_RES.html'>USER_GET_PASSWORD_PROTECT_INFO_RES</a></td><td>0x8000F010</td><td>查询账号保护信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./USER_LEAVE_SERVER_REQ.html'>USER_LEAVE_SERVER_REQ</a></td><td>0x0000F013</td><td><a href='./USER_LEAVE_SERVER_RES.html'>USER_LEAVE_SERVER_RES</a></td><td>0x8000F013</td><td>用户离开服务器</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./USER_QUERY_PROTECT_QUESTION_REQ.html'>USER_QUERY_PROTECT_QUESTION_REQ</a></td><td>0x0FFFF014</td><td><a href='./USER_QUERY_PROTECT_QUESTION_RES.html'>USER_QUERY_PROTECT_QUESTION_RES</a></td><td>0x8FFFF014</td><td>查询用户的密保问题</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./USER_CHANGE_PASSWORD_REQ.html'>USER_CHANGE_PASSWORD_REQ</a></td><td>0x0FFFF015</td><td><a href='./USER_CHANGE_PASSWORD_RES.html'>USER_CHANGE_PASSWORD_RES</a></td><td>0x8FFFF015</td><td>用户修改密码</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEW_USER_CHANGE_PASSWORD_REQ.html'>NEW_USER_CHANGE_PASSWORD_REQ</a></td><td>0x0234AB90</td><td><a href='./NEW_USER_CHANGE_PASSWORD_RES.html'>NEW_USER_CHANGE_PASSWORD_RES</a></td><td>0x0234AB91</td><td>新用户修改密码</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEW_USER_LOGIN_REQ.html'>NEW_USER_LOGIN_REQ</a></td><td>0x0234AB89</td><td><a href='./NEW_USER_LOGIN_RES.html'>NEW_USER_LOGIN_RES</a></td><td>0x8234AB89</td><td>用户登录包2013.4.18</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./USER_LOGIN_REQ.html'>USER_LOGIN_REQ</a></td><td>0x00000011</td><td><a href='./USER_LOGIN_RES.html'>USER_LOGIN_RES</a></td><td>0x80000011</td><td>用户登录包</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./USER_CLIENT_INFO_REQ.html'>USER_CLIENT_INFO_REQ</a></td><td>0x000EE007</td><td><a href='./-.html'>-</a></td><td>-</td><td>用户客户端信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./USER_SET_PASSWORD_PROTECT_INFO_REQ.html'>USER_SET_PASSWORD_PROTECT_INFO_REQ</a></td><td>0x0000F011</td><td><a href='./USER_SET_PASSWORD_PROTECT_INFO_RES.html'>USER_SET_PASSWORD_PROTECT_INFO_RES</a></td><td>0x8000F011</td><td>设置账号保护信息，已经设置过证件号，不允许重新设置</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./USER_UPDATE_PASSWORD_REQ.html'>USER_UPDATE_PASSWORD_REQ</a></td><td>0x0000F006</td><td><a href='./USER_UPDATE_PASSWORD_RES.html'>USER_UPDATE_PASSWORD_RES</a></td><td>0x8000F006</td><td>用户修改密码</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./USER_UPDATE_REQ.html'>USER_UPDATE_REQ</a></td><td>0x0000F002</td><td><a href='./USER_UPDATE_RES.html'>USER_UPDATE_RES</a></td><td>0x8000F002</td><td>更新一个通行证</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./USER_PROTECT_BAN_REQ.html'>USER_PROTECT_BAN_REQ</a></td><td>0x0000F020</td><td><a href='./USER_PROTECT_BAN_RES.html'>USER_PROTECT_BAN_RES</a></td><td>0x8000F020</td><td>用户自助保护性封停账号</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CHANGE_SERVER_REQ.html'>CHANGE_SERVER_REQ</a></td><td>0x0000EB01</td><td><a href='./CHANGE_SERVER_RES.html'>CHANGE_SERVER_RES</a></td><td>0x8000EB01</td><td>更换服务器通知，服务器收到后通知网关，网关关闭用户对应的管道，重新开始解析协议</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./LEAVE_GAME_NOTIFY_REQ.html'>LEAVE_GAME_NOTIFY_REQ</a></td><td>0x0000EB03</td><td><a href='./LEAVE_GAME_NOTIFY_RES.html'>LEAVE_GAME_NOTIFY_RES</a></td><td>0x8000EB03</td><td>更换服务器通知，服务器收到后通知网关，网关关闭用户对应的管道，重新开始解析协议</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ACTIVE_TEST_REQ.html'>ACTIVE_TEST_REQ</a></td><td>0x00000010</td><td><a href='./ACTIVE_TEST_RES.html'>ACTIVE_TEST_RES</a></td><td>0x80000010</td><td>链路检测包，Server和Client可以互发</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_TIPS_REQ.html'>GET_TIPS_REQ</a></td><td>0x000ff012</td><td><a href='./GET_TIPS_RES.html'>GET_TIPS_RES</a></td><td>0x800ff012</td><td>获取tip提示</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_SOME4ANDROID_REQ.html'>GET_SOME4ANDROID_REQ</a></td><td>0x00fff013</td><td><a href='./GET_SOME4ANDROID_RES.html'>GET_SOME4ANDROID_RES</a></td><td>0x80fff013</td><td>获取android信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_SOME4ANDROID_1_REQ.html'>GET_SOME4ANDROID_1_REQ</a></td><td>0x00fff014</td><td><a href='./GET_SOME4ANDROID_1_RES.html'>GET_SOME4ANDROID_1_RES</a></td><td>0x80fff014</td><td>获取android信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_1_REQ.html'>SERVER_HAND_CLIENT_1_REQ</a></td><td>0x0002A300</td><td><a href='./SERVER_HAND_CLIENT_1_RES.html'>SERVER_HAND_CLIENT_1_RES</a></td><td>0x8002A300</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_2_REQ.html'>SERVER_HAND_CLIENT_2_REQ</a></td><td>0x0002A301</td><td><a href='./SERVER_HAND_CLIENT_2_RES.html'>SERVER_HAND_CLIENT_2_RES</a></td><td>0x8002A301</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_3_REQ.html'>SERVER_HAND_CLIENT_3_REQ</a></td><td>0x0002A302</td><td><a href='./SERVER_HAND_CLIENT_3_RES.html'>SERVER_HAND_CLIENT_3_RES</a></td><td>0x8002A302</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_4_REQ.html'>SERVER_HAND_CLIENT_4_REQ</a></td><td>0x0002A303</td><td><a href='./SERVER_HAND_CLIENT_4_RES.html'>SERVER_HAND_CLIENT_4_RES</a></td><td>0x8002A303</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_5_REQ.html'>SERVER_HAND_CLIENT_5_REQ</a></td><td>0x0002A304</td><td><a href='./SERVER_HAND_CLIENT_5_RES.html'>SERVER_HAND_CLIENT_5_RES</a></td><td>0x8002A304</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_6_REQ.html'>SERVER_HAND_CLIENT_6_REQ</a></td><td>0x0002A305</td><td><a href='./SERVER_HAND_CLIENT_6_RES.html'>SERVER_HAND_CLIENT_6_RES</a></td><td>0x8002A305</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_7_REQ.html'>SERVER_HAND_CLIENT_7_REQ</a></td><td>0x0002A306</td><td><a href='./SERVER_HAND_CLIENT_7_RES.html'>SERVER_HAND_CLIENT_7_RES</a></td><td>0x8002A306</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_8_REQ.html'>SERVER_HAND_CLIENT_8_REQ</a></td><td>0x0002A307</td><td><a href='./SERVER_HAND_CLIENT_8_RES.html'>SERVER_HAND_CLIENT_8_RES</a></td><td>0x8002A307</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_9_REQ.html'>SERVER_HAND_CLIENT_9_REQ</a></td><td>0x0002A308</td><td><a href='./SERVER_HAND_CLIENT_9_RES.html'>SERVER_HAND_CLIENT_9_RES</a></td><td>0x8002A308</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_10_REQ.html'>SERVER_HAND_CLIENT_10_REQ</a></td><td>0x0002A309</td><td><a href='./SERVER_HAND_CLIENT_10_RES.html'>SERVER_HAND_CLIENT_10_RES</a></td><td>0x8002A309</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_11_REQ.html'>SERVER_HAND_CLIENT_11_REQ</a></td><td>0x0002A30A</td><td><a href='./SERVER_HAND_CLIENT_11_RES.html'>SERVER_HAND_CLIENT_11_RES</a></td><td>0x8002A30A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_12_REQ.html'>SERVER_HAND_CLIENT_12_REQ</a></td><td>0x0002A30B</td><td><a href='./SERVER_HAND_CLIENT_12_RES.html'>SERVER_HAND_CLIENT_12_RES</a></td><td>0x8002A30B</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_13_REQ.html'>SERVER_HAND_CLIENT_13_REQ</a></td><td>0x0002A30C</td><td><a href='./SERVER_HAND_CLIENT_13_RES.html'>SERVER_HAND_CLIENT_13_RES</a></td><td>0x8002A30C</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_14_REQ.html'>SERVER_HAND_CLIENT_14_REQ</a></td><td>0x0002A30D</td><td><a href='./SERVER_HAND_CLIENT_14_RES.html'>SERVER_HAND_CLIENT_14_RES</a></td><td>0x8002A30D</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_15_REQ.html'>SERVER_HAND_CLIENT_15_REQ</a></td><td>0x0002A30E</td><td><a href='./SERVER_HAND_CLIENT_15_RES.html'>SERVER_HAND_CLIENT_15_RES</a></td><td>0x8002A30E</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_16_REQ.html'>SERVER_HAND_CLIENT_16_REQ</a></td><td>0x0002A30F</td><td><a href='./SERVER_HAND_CLIENT_16_RES.html'>SERVER_HAND_CLIENT_16_RES</a></td><td>0x8002A30F</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_17_REQ.html'>SERVER_HAND_CLIENT_17_REQ</a></td><td>0x0002A310</td><td><a href='./SERVER_HAND_CLIENT_17_RES.html'>SERVER_HAND_CLIENT_17_RES</a></td><td>0x8002A310</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_18_REQ.html'>SERVER_HAND_CLIENT_18_REQ</a></td><td>0x0002A311</td><td><a href='./SERVER_HAND_CLIENT_18_RES.html'>SERVER_HAND_CLIENT_18_RES</a></td><td>0x8002A311</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_19_REQ.html'>SERVER_HAND_CLIENT_19_REQ</a></td><td>0x0002A312</td><td><a href='./SERVER_HAND_CLIENT_19_RES.html'>SERVER_HAND_CLIENT_19_RES</a></td><td>0x8002A312</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_20_REQ.html'>SERVER_HAND_CLIENT_20_REQ</a></td><td>0x0002A313</td><td><a href='./SERVER_HAND_CLIENT_20_RES.html'>SERVER_HAND_CLIENT_20_RES</a></td><td>0x8002A313</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_21_REQ.html'>SERVER_HAND_CLIENT_21_REQ</a></td><td>0x0002A314</td><td><a href='./SERVER_HAND_CLIENT_21_RES.html'>SERVER_HAND_CLIENT_21_RES</a></td><td>0x8002A314</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_22_REQ.html'>SERVER_HAND_CLIENT_22_REQ</a></td><td>0x0002A315</td><td><a href='./SERVER_HAND_CLIENT_22_RES.html'>SERVER_HAND_CLIENT_22_RES</a></td><td>0x8002A315</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_23_REQ.html'>SERVER_HAND_CLIENT_23_REQ</a></td><td>0x0002A316</td><td><a href='./SERVER_HAND_CLIENT_23_RES.html'>SERVER_HAND_CLIENT_23_RES</a></td><td>0x8002A316</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_24_REQ.html'>SERVER_HAND_CLIENT_24_REQ</a></td><td>0x0002A317</td><td><a href='./SERVER_HAND_CLIENT_24_RES.html'>SERVER_HAND_CLIENT_24_RES</a></td><td>0x8002A317</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_25_REQ.html'>SERVER_HAND_CLIENT_25_REQ</a></td><td>0x0002A318</td><td><a href='./SERVER_HAND_CLIENT_25_RES.html'>SERVER_HAND_CLIENT_25_RES</a></td><td>0x8002A318</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_26_REQ.html'>SERVER_HAND_CLIENT_26_REQ</a></td><td>0x0002A319</td><td><a href='./SERVER_HAND_CLIENT_26_RES.html'>SERVER_HAND_CLIENT_26_RES</a></td><td>0x8002A319</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_27_REQ.html'>SERVER_HAND_CLIENT_27_REQ</a></td><td>0x0002A31A</td><td><a href='./SERVER_HAND_CLIENT_27_RES.html'>SERVER_HAND_CLIENT_27_RES</a></td><td>0x8002A31A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_28_REQ.html'>SERVER_HAND_CLIENT_28_REQ</a></td><td>0x0002A31B</td><td><a href='./SERVER_HAND_CLIENT_28_RES.html'>SERVER_HAND_CLIENT_28_RES</a></td><td>0x8002A31B</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_29_REQ.html'>SERVER_HAND_CLIENT_29_REQ</a></td><td>0x0002A31C</td><td><a href='./SERVER_HAND_CLIENT_29_RES.html'>SERVER_HAND_CLIENT_29_RES</a></td><td>0x8002A31C</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_30_REQ.html'>SERVER_HAND_CLIENT_30_REQ</a></td><td>0x0002A31D</td><td><a href='./SERVER_HAND_CLIENT_30_RES.html'>SERVER_HAND_CLIENT_30_RES</a></td><td>0x8002A31D</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_31_REQ.html'>SERVER_HAND_CLIENT_31_REQ</a></td><td>0x0002A31E</td><td><a href='./SERVER_HAND_CLIENT_31_RES.html'>SERVER_HAND_CLIENT_31_RES</a></td><td>0x8002A31E</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_32_REQ.html'>SERVER_HAND_CLIENT_32_REQ</a></td><td>0x0002A31F</td><td><a href='./SERVER_HAND_CLIENT_32_RES.html'>SERVER_HAND_CLIENT_32_RES</a></td><td>0x8002A31F</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_33_REQ.html'>SERVER_HAND_CLIENT_33_REQ</a></td><td>0x0002A320</td><td><a href='./SERVER_HAND_CLIENT_33_RES.html'>SERVER_HAND_CLIENT_33_RES</a></td><td>0x8002A320</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_34_REQ.html'>SERVER_HAND_CLIENT_34_REQ</a></td><td>0x0002A321</td><td><a href='./SERVER_HAND_CLIENT_34_RES.html'>SERVER_HAND_CLIENT_34_RES</a></td><td>0x8002A321</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_35_REQ.html'>SERVER_HAND_CLIENT_35_REQ</a></td><td>0x0002A322</td><td><a href='./SERVER_HAND_CLIENT_35_RES.html'>SERVER_HAND_CLIENT_35_RES</a></td><td>0x8002A322</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_36_REQ.html'>SERVER_HAND_CLIENT_36_REQ</a></td><td>0x0002A323</td><td><a href='./SERVER_HAND_CLIENT_36_RES.html'>SERVER_HAND_CLIENT_36_RES</a></td><td>0x8002A323</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_37_REQ.html'>SERVER_HAND_CLIENT_37_REQ</a></td><td>0x0002A324</td><td><a href='./SERVER_HAND_CLIENT_37_RES.html'>SERVER_HAND_CLIENT_37_RES</a></td><td>0x8002A324</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_38_REQ.html'>SERVER_HAND_CLIENT_38_REQ</a></td><td>0x0002A325</td><td><a href='./SERVER_HAND_CLIENT_38_RES.html'>SERVER_HAND_CLIENT_38_RES</a></td><td>0x8002A325</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_39_REQ.html'>SERVER_HAND_CLIENT_39_REQ</a></td><td>0x0002A326</td><td><a href='./SERVER_HAND_CLIENT_39_RES.html'>SERVER_HAND_CLIENT_39_RES</a></td><td>0x8002A326</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_40_REQ.html'>SERVER_HAND_CLIENT_40_REQ</a></td><td>0x0002A327</td><td><a href='./SERVER_HAND_CLIENT_40_RES.html'>SERVER_HAND_CLIENT_40_RES</a></td><td>0x8002A327</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_41_REQ.html'>SERVER_HAND_CLIENT_41_REQ</a></td><td>0x0002A328</td><td><a href='./SERVER_HAND_CLIENT_41_RES.html'>SERVER_HAND_CLIENT_41_RES</a></td><td>0x8002A328</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_42_REQ.html'>SERVER_HAND_CLIENT_42_REQ</a></td><td>0x0002A329</td><td><a href='./SERVER_HAND_CLIENT_42_RES.html'>SERVER_HAND_CLIENT_42_RES</a></td><td>0x8002A329</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_43_REQ.html'>SERVER_HAND_CLIENT_43_REQ</a></td><td>0x0002A32A</td><td><a href='./SERVER_HAND_CLIENT_43_RES.html'>SERVER_HAND_CLIENT_43_RES</a></td><td>0x8002A32A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_44_REQ.html'>SERVER_HAND_CLIENT_44_REQ</a></td><td>0x0002A32B</td><td><a href='./SERVER_HAND_CLIENT_44_RES.html'>SERVER_HAND_CLIENT_44_RES</a></td><td>0x8002A32B</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_45_REQ.html'>SERVER_HAND_CLIENT_45_REQ</a></td><td>0x0002A32C</td><td><a href='./SERVER_HAND_CLIENT_45_RES.html'>SERVER_HAND_CLIENT_45_RES</a></td><td>0x8002A32C</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_46_REQ.html'>SERVER_HAND_CLIENT_46_REQ</a></td><td>0x0002A32D</td><td><a href='./SERVER_HAND_CLIENT_46_RES.html'>SERVER_HAND_CLIENT_46_RES</a></td><td>0x8002A32D</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_47_REQ.html'>SERVER_HAND_CLIENT_47_REQ</a></td><td>0x0002A32E</td><td><a href='./SERVER_HAND_CLIENT_47_RES.html'>SERVER_HAND_CLIENT_47_RES</a></td><td>0x8002A32E</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_48_REQ.html'>SERVER_HAND_CLIENT_48_REQ</a></td><td>0x0002A32F</td><td><a href='./SERVER_HAND_CLIENT_48_RES.html'>SERVER_HAND_CLIENT_48_RES</a></td><td>0x8002A32F</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_49_REQ.html'>SERVER_HAND_CLIENT_49_REQ</a></td><td>0x0002A330</td><td><a href='./SERVER_HAND_CLIENT_49_RES.html'>SERVER_HAND_CLIENT_49_RES</a></td><td>0x8002A330</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_50_REQ.html'>SERVER_HAND_CLIENT_50_REQ</a></td><td>0x0002A331</td><td><a href='./SERVER_HAND_CLIENT_50_RES.html'>SERVER_HAND_CLIENT_50_RES</a></td><td>0x8002A331</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_51_REQ.html'>SERVER_HAND_CLIENT_51_REQ</a></td><td>0x0002A332</td><td><a href='./SERVER_HAND_CLIENT_51_RES.html'>SERVER_HAND_CLIENT_51_RES</a></td><td>0x8002A332</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_52_REQ.html'>SERVER_HAND_CLIENT_52_REQ</a></td><td>0x0002A333</td><td><a href='./SERVER_HAND_CLIENT_52_RES.html'>SERVER_HAND_CLIENT_52_RES</a></td><td>0x8002A333</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_53_REQ.html'>SERVER_HAND_CLIENT_53_REQ</a></td><td>0x0002A334</td><td><a href='./SERVER_HAND_CLIENT_53_RES.html'>SERVER_HAND_CLIENT_53_RES</a></td><td>0x8002A334</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_54_REQ.html'>SERVER_HAND_CLIENT_54_REQ</a></td><td>0x0002A335</td><td><a href='./SERVER_HAND_CLIENT_54_RES.html'>SERVER_HAND_CLIENT_54_RES</a></td><td>0x8002A335</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_55_REQ.html'>SERVER_HAND_CLIENT_55_REQ</a></td><td>0x0002A336</td><td><a href='./SERVER_HAND_CLIENT_55_RES.html'>SERVER_HAND_CLIENT_55_RES</a></td><td>0x8002A336</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_56_REQ.html'>SERVER_HAND_CLIENT_56_REQ</a></td><td>0x0002A337</td><td><a href='./SERVER_HAND_CLIENT_56_RES.html'>SERVER_HAND_CLIENT_56_RES</a></td><td>0x8002A337</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_57_REQ.html'>SERVER_HAND_CLIENT_57_REQ</a></td><td>0x0002A338</td><td><a href='./SERVER_HAND_CLIENT_57_RES.html'>SERVER_HAND_CLIENT_57_RES</a></td><td>0x8002A338</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_58_REQ.html'>SERVER_HAND_CLIENT_58_REQ</a></td><td>0x0002A339</td><td><a href='./SERVER_HAND_CLIENT_58_RES.html'>SERVER_HAND_CLIENT_58_RES</a></td><td>0x8002A339</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_59_REQ.html'>SERVER_HAND_CLIENT_59_REQ</a></td><td>0x0002A33A</td><td><a href='./SERVER_HAND_CLIENT_59_RES.html'>SERVER_HAND_CLIENT_59_RES</a></td><td>0x8002A33A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_60_REQ.html'>SERVER_HAND_CLIENT_60_REQ</a></td><td>0x0002A33B</td><td><a href='./SERVER_HAND_CLIENT_60_RES.html'>SERVER_HAND_CLIENT_60_RES</a></td><td>0x8002A33B</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_61_REQ.html'>SERVER_HAND_CLIENT_61_REQ</a></td><td>0x0002A33C</td><td><a href='./SERVER_HAND_CLIENT_61_RES.html'>SERVER_HAND_CLIENT_61_RES</a></td><td>0x8002A33C</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_62_REQ.html'>SERVER_HAND_CLIENT_62_REQ</a></td><td>0x0002A33D</td><td><a href='./SERVER_HAND_CLIENT_62_RES.html'>SERVER_HAND_CLIENT_62_RES</a></td><td>0x8002A33D</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_63_REQ.html'>SERVER_HAND_CLIENT_63_REQ</a></td><td>0x0002A33E</td><td><a href='./SERVER_HAND_CLIENT_63_RES.html'>SERVER_HAND_CLIENT_63_RES</a></td><td>0x8002A33E</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_64_REQ.html'>SERVER_HAND_CLIENT_64_REQ</a></td><td>0x0002A33F</td><td><a href='./SERVER_HAND_CLIENT_64_RES.html'>SERVER_HAND_CLIENT_64_RES</a></td><td>0x8002A33F</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_65_REQ.html'>SERVER_HAND_CLIENT_65_REQ</a></td><td>0x0002A340</td><td><a href='./SERVER_HAND_CLIENT_65_RES.html'>SERVER_HAND_CLIENT_65_RES</a></td><td>0x8002A340</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_66_REQ.html'>SERVER_HAND_CLIENT_66_REQ</a></td><td>0x0002A341</td><td><a href='./SERVER_HAND_CLIENT_66_RES.html'>SERVER_HAND_CLIENT_66_RES</a></td><td>0x8002A341</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_67_REQ.html'>SERVER_HAND_CLIENT_67_REQ</a></td><td>0x0002A342</td><td><a href='./SERVER_HAND_CLIENT_67_RES.html'>SERVER_HAND_CLIENT_67_RES</a></td><td>0x8002A342</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_68_REQ.html'>SERVER_HAND_CLIENT_68_REQ</a></td><td>0x0002A343</td><td><a href='./SERVER_HAND_CLIENT_68_RES.html'>SERVER_HAND_CLIENT_68_RES</a></td><td>0x8002A343</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_69_REQ.html'>SERVER_HAND_CLIENT_69_REQ</a></td><td>0x0002A344</td><td><a href='./SERVER_HAND_CLIENT_69_RES.html'>SERVER_HAND_CLIENT_69_RES</a></td><td>0x8002A344</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_70_REQ.html'>SERVER_HAND_CLIENT_70_REQ</a></td><td>0x0002A345</td><td><a href='./SERVER_HAND_CLIENT_70_RES.html'>SERVER_HAND_CLIENT_70_RES</a></td><td>0x8002A345</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_71_REQ.html'>SERVER_HAND_CLIENT_71_REQ</a></td><td>0x0002A346</td><td><a href='./SERVER_HAND_CLIENT_71_RES.html'>SERVER_HAND_CLIENT_71_RES</a></td><td>0x8002A346</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_72_REQ.html'>SERVER_HAND_CLIENT_72_REQ</a></td><td>0x0002A347</td><td><a href='./SERVER_HAND_CLIENT_72_RES.html'>SERVER_HAND_CLIENT_72_RES</a></td><td>0x8002A347</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_73_REQ.html'>SERVER_HAND_CLIENT_73_REQ</a></td><td>0x0002A348</td><td><a href='./SERVER_HAND_CLIENT_73_RES.html'>SERVER_HAND_CLIENT_73_RES</a></td><td>0x8002A348</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_74_REQ.html'>SERVER_HAND_CLIENT_74_REQ</a></td><td>0x0002A349</td><td><a href='./SERVER_HAND_CLIENT_74_RES.html'>SERVER_HAND_CLIENT_74_RES</a></td><td>0x8002A349</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_75_REQ.html'>SERVER_HAND_CLIENT_75_REQ</a></td><td>0x0002A34A</td><td><a href='./SERVER_HAND_CLIENT_75_RES.html'>SERVER_HAND_CLIENT_75_RES</a></td><td>0x8002A34A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_76_REQ.html'>SERVER_HAND_CLIENT_76_REQ</a></td><td>0x0002A34B</td><td><a href='./SERVER_HAND_CLIENT_76_RES.html'>SERVER_HAND_CLIENT_76_RES</a></td><td>0x8002A34B</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_77_REQ.html'>SERVER_HAND_CLIENT_77_REQ</a></td><td>0x0002A34C</td><td><a href='./SERVER_HAND_CLIENT_77_RES.html'>SERVER_HAND_CLIENT_77_RES</a></td><td>0x8002A34C</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_78_REQ.html'>SERVER_HAND_CLIENT_78_REQ</a></td><td>0x0002A34D</td><td><a href='./SERVER_HAND_CLIENT_78_RES.html'>SERVER_HAND_CLIENT_78_RES</a></td><td>0x8002A34D</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_79_REQ.html'>SERVER_HAND_CLIENT_79_REQ</a></td><td>0x0002A34E</td><td><a href='./SERVER_HAND_CLIENT_79_RES.html'>SERVER_HAND_CLIENT_79_RES</a></td><td>0x8002A34E</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_80_REQ.html'>SERVER_HAND_CLIENT_80_REQ</a></td><td>0x0002A34F</td><td><a href='./SERVER_HAND_CLIENT_80_RES.html'>SERVER_HAND_CLIENT_80_RES</a></td><td>0x8002A34F</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_81_REQ.html'>SERVER_HAND_CLIENT_81_REQ</a></td><td>0x0002A350</td><td><a href='./SERVER_HAND_CLIENT_81_RES.html'>SERVER_HAND_CLIENT_81_RES</a></td><td>0x8002A350</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_82_REQ.html'>SERVER_HAND_CLIENT_82_REQ</a></td><td>0x0002A351</td><td><a href='./SERVER_HAND_CLIENT_82_RES.html'>SERVER_HAND_CLIENT_82_RES</a></td><td>0x8002A351</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_83_REQ.html'>SERVER_HAND_CLIENT_83_REQ</a></td><td>0x0002A352</td><td><a href='./SERVER_HAND_CLIENT_83_RES.html'>SERVER_HAND_CLIENT_83_RES</a></td><td>0x8002A352</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_84_REQ.html'>SERVER_HAND_CLIENT_84_REQ</a></td><td>0x0002A353</td><td><a href='./SERVER_HAND_CLIENT_84_RES.html'>SERVER_HAND_CLIENT_84_RES</a></td><td>0x8002A353</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_85_REQ.html'>SERVER_HAND_CLIENT_85_REQ</a></td><td>0x0002A354</td><td><a href='./SERVER_HAND_CLIENT_85_RES.html'>SERVER_HAND_CLIENT_85_RES</a></td><td>0x8002A354</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_86_REQ.html'>SERVER_HAND_CLIENT_86_REQ</a></td><td>0x0002A355</td><td><a href='./SERVER_HAND_CLIENT_86_RES.html'>SERVER_HAND_CLIENT_86_RES</a></td><td>0x8002A355</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_87_REQ.html'>SERVER_HAND_CLIENT_87_REQ</a></td><td>0x0002A356</td><td><a href='./SERVER_HAND_CLIENT_87_RES.html'>SERVER_HAND_CLIENT_87_RES</a></td><td>0x8002A356</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_88_REQ.html'>SERVER_HAND_CLIENT_88_REQ</a></td><td>0x0002A357</td><td><a href='./SERVER_HAND_CLIENT_88_RES.html'>SERVER_HAND_CLIENT_88_RES</a></td><td>0x8002A357</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_89_REQ.html'>SERVER_HAND_CLIENT_89_REQ</a></td><td>0x0002A358</td><td><a href='./SERVER_HAND_CLIENT_89_RES.html'>SERVER_HAND_CLIENT_89_RES</a></td><td>0x8002A358</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_90_REQ.html'>SERVER_HAND_CLIENT_90_REQ</a></td><td>0x0002A359</td><td><a href='./SERVER_HAND_CLIENT_90_RES.html'>SERVER_HAND_CLIENT_90_RES</a></td><td>0x8002A359</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_91_REQ.html'>SERVER_HAND_CLIENT_91_REQ</a></td><td>0x0002A35A</td><td><a href='./SERVER_HAND_CLIENT_91_RES.html'>SERVER_HAND_CLIENT_91_RES</a></td><td>0x8002A35A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_92_REQ.html'>SERVER_HAND_CLIENT_92_REQ</a></td><td>0x0002A35B</td><td><a href='./SERVER_HAND_CLIENT_92_RES.html'>SERVER_HAND_CLIENT_92_RES</a></td><td>0x8002A35B</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_93_REQ.html'>SERVER_HAND_CLIENT_93_REQ</a></td><td>0x0002A35C</td><td><a href='./SERVER_HAND_CLIENT_93_RES.html'>SERVER_HAND_CLIENT_93_RES</a></td><td>0x8002A35C</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_94_REQ.html'>SERVER_HAND_CLIENT_94_REQ</a></td><td>0x0002A35D</td><td><a href='./SERVER_HAND_CLIENT_94_RES.html'>SERVER_HAND_CLIENT_94_RES</a></td><td>0x8002A35D</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_95_REQ.html'>SERVER_HAND_CLIENT_95_REQ</a></td><td>0x0002A35E</td><td><a href='./SERVER_HAND_CLIENT_95_RES.html'>SERVER_HAND_CLIENT_95_RES</a></td><td>0x8002A35E</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_96_REQ.html'>SERVER_HAND_CLIENT_96_REQ</a></td><td>0x0002A35F</td><td><a href='./SERVER_HAND_CLIENT_96_RES.html'>SERVER_HAND_CLIENT_96_RES</a></td><td>0x8002A35F</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_97_REQ.html'>SERVER_HAND_CLIENT_97_REQ</a></td><td>0x0002A360</td><td><a href='./SERVER_HAND_CLIENT_97_RES.html'>SERVER_HAND_CLIENT_97_RES</a></td><td>0x8002A360</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_98_REQ.html'>SERVER_HAND_CLIENT_98_REQ</a></td><td>0x0002A361</td><td><a href='./SERVER_HAND_CLIENT_98_RES.html'>SERVER_HAND_CLIENT_98_RES</a></td><td>0x8002A361</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVER_HAND_CLIENT_99_REQ.html'>SERVER_HAND_CLIENT_99_REQ</a></td><td>0x0002A362</td><td><a href='./SERVER_HAND_CLIENT_99_RES.html'>SERVER_HAND_CLIENT_99_RES</a></td><td>0x8002A362</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SERVER_HAND_CLIENT_100_REQ.html'>SERVER_HAND_CLIENT_100_REQ</a></td><td>0x0002A363</td><td><a href='./SERVER_HAND_CLIENT_100_RES.html'>SERVER_HAND_CLIENT_100_RES</a></td><td>0x8002A363</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TRY_GETPLAYERINFO_REQ.html'>TRY_GETPLAYERINFO_REQ</a></td><td>0x00A3B000</td><td><a href='./TRY_GETPLAYERINFO_RES.html'>TRY_GETPLAYERINFO_RES</a></td><td>0x80A3B000</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./WAIT_FOR_OTHER_REQ.html'>WAIT_FOR_OTHER_REQ</a></td><td>0x00A3B001</td><td><a href='./WAIT_FOR_OTHER_RES.html'>WAIT_FOR_OTHER_RES</a></td><td>0x80A3B001</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_REWARD_2_PLAYER_REQ.html'>GET_REWARD_2_PLAYER_REQ</a></td><td>0x00A3B002</td><td><a href='./GET_REWARD_2_PLAYER_RES.html'>GET_REWARD_2_PLAYER_RES</a></td><td>0x80A3B002</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REQUESTBUY_GET_ENTITY_INFO_REQ.html'>REQUESTBUY_GET_ENTITY_INFO_REQ</a></td><td>0x00A3B003</td><td><a href='./REQUESTBUY_GET_ENTITY_INFO_RES.html'>REQUESTBUY_GET_ENTITY_INFO_RES</a></td><td>0x80A3B003</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_SOUL_REQ.html'>PLAYER_SOUL_REQ</a></td><td>0x00A3B004</td><td><a href='./PLAYER_SOUL_RES.html'>PLAYER_SOUL_RES</a></td><td>0x80A3B004</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CARD_TRYSAVING_REQ.html'>CARD_TRYSAVING_REQ</a></td><td>0x00A3B005</td><td><a href='./CARD_TRYSAVING_RES.html'>CARD_TRYSAVING_RES</a></td><td>0x80A3B005</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GANG_WAREHOUSE_JOURNAL_REQ.html'>GANG_WAREHOUSE_JOURNAL_REQ</a></td><td>0x00A3B006</td><td><a href='./GANG_WAREHOUSE_JOURNAL_RES.html'>GANG_WAREHOUSE_JOURNAL_RES</a></td><td>0x80A3B006</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_WAREHOUSE_REQ.html'>GET_WAREHOUSE_REQ</a></td><td>0x00A3B007</td><td><a href='./GET_WAREHOUSE_RES.html'>GET_WAREHOUSE_RES</a></td><td>0x80A3B007</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY__GETAUTOBACK_REQ.html'>QUERY__GETAUTOBACK_REQ</a></td><td>0x00A3B008</td><td><a href='./QUERY__GETAUTOBACK_RES.html'>QUERY__GETAUTOBACK_RES</a></td><td>0x80A3B008</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_ZONGPAI_NAME_REQ.html'>GET_ZONGPAI_NAME_REQ</a></td><td>0x00A3B009</td><td><a href='./GET_ZONGPAI_NAME_RES.html'>GET_ZONGPAI_NAME_RES</a></td><td>0x80A3B009</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TRY_LEAVE_ZONGPAI_REQ.html'>TRY_LEAVE_ZONGPAI_REQ</a></td><td>0x00A3B00A</td><td><a href='./TRY_LEAVE_ZONGPAI_RES.html'>TRY_LEAVE_ZONGPAI_RES</a></td><td>0x80A3B00A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REBEL_EVICT_NEW_REQ.html'>REBEL_EVICT_NEW_REQ</a></td><td>0x00A3B00B</td><td><a href='./REBEL_EVICT_NEW_RES.html'>REBEL_EVICT_NEW_RES</a></td><td>0x80A3B00B</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_PLAYERTITLE_REQ.html'>GET_PLAYERTITLE_REQ</a></td><td>0x00A3B00C</td><td><a href='./GET_PLAYERTITLE_RES.html'>GET_PLAYERTITLE_RES</a></td><td>0x80A3B00C</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_TRY_BEQSTART_REQ.html'>MARRIAGE_TRY_BEQSTART_REQ</a></td><td>0x00A3B00D</td><td><a href='./MARRIAGE_TRY_BEQSTART_RES.html'>MARRIAGE_TRY_BEQSTART_RES</a></td><td>0x80A3B00D</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MARRIAGE_GUESTNEW_OVER_REQ.html'>MARRIAGE_GUESTNEW_OVER_REQ</a></td><td>0x00A3B00E</td><td><a href='./MARRIAGE_GUESTNEW_OVER_RES.html'>MARRIAGE_GUESTNEW_OVER_RES</a></td><td>0x80A3B00E</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_HUNLI_REQ.html'>MARRIAGE_HUNLI_REQ</a></td><td>0x00A3B00F</td><td><a href='./MARRIAGE_HUNLI_RES.html'>MARRIAGE_HUNLI_RES</a></td><td>0x80A3B00F</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./COUNTRY_COMMENDCANCEL_REQ.html'>COUNTRY_COMMENDCANCEL_REQ</a></td><td>0x00A3B010</td><td><a href='./COUNTRY_COMMENDCANCEL_RES.html'>COUNTRY_COMMENDCANCEL_RES</a></td><td>0x80A3B010</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./COUNTRY_NEWQIUJIN_REQ.html'>COUNTRY_NEWQIUJIN_REQ</a></td><td>0x00A3B011</td><td><a href='./COUNTRY_NEWQIUJIN_RES.html'>COUNTRY_NEWQIUJIN_RES</a></td><td>0x80A3B011</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_COUNTRYJINKU_REQ.html'>GET_COUNTRYJINKU_REQ</a></td><td>0x00A3B012</td><td><a href='./GET_COUNTRYJINKU_RES.html'>GET_COUNTRYJINKU_RES</a></td><td>0x80A3B012</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_NEWBUILDING_REQ.html'>CAVE_NEWBUILDING_REQ</a></td><td>0x00A3B013</td><td><a href='./CAVE_NEWBUILDING_RES.html'>CAVE_NEWBUILDING_RES</a></td><td>0x80A3B013</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_FIELD_REQ.html'>CAVE_FIELD_REQ</a></td><td>0x00A3B014</td><td><a href='./CAVE_FIELD_RES.html'>CAVE_FIELD_RES</a></td><td>0x80A3B014</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_NEW_PET_REQ.html'>CAVE_NEW_PET_REQ</a></td><td>0x00A3B015</td><td><a href='./CAVE_NEW_PET_RES.html'>CAVE_NEW_PET_RES</a></td><td>0x80A3B015</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_TRY_SCHEDULE_REQ.html'>CAVE_TRY_SCHEDULE_REQ</a></td><td>0x00A3B016</td><td><a href='./CAVE_TRY_SCHEDULE_RES.html'>CAVE_TRY_SCHEDULE_RES</a></td><td>0x80A3B016</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_SEND_COUNTYLIST_REQ.html'>CAVE_SEND_COUNTYLIST_REQ</a></td><td>0x00A3B017</td><td><a href='./CAVE_SEND_COUNTYLIST_RES.html'>CAVE_SEND_COUNTYLIST_RES</a></td><td>0x80A3B017</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_NEW_LEVELUP_REQ.html'>PLAYER_NEW_LEVELUP_REQ</a></td><td>0x00A3B018</td><td><a href='./PLAYER_NEW_LEVELUP_RES.html'>PLAYER_NEW_LEVELUP_RES</a></td><td>0x80A3B018</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CLEAN_FRIEND_LIST_REQ.html'>CLEAN_FRIEND_LIST_REQ</a></td><td>0x00A3B019</td><td><a href='./CLEAN_FRIEND_LIST_RES.html'>CLEAN_FRIEND_LIST_RES</a></td><td>0x80A3B019</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DO_ACTIVITY_NEW_REQ.html'>DO_ACTIVITY_NEW_REQ</a></td><td>0x00A3B01A</td><td><a href='./DO_ACTIVITY_NEW_RES.html'>DO_ACTIVITY_NEW_RES</a></td><td>0x80A3B01A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REF_TESK_LIST_REQ.html'>REF_TESK_LIST_REQ</a></td><td>0x00A3B01B</td><td><a href='./REF_TESK_LIST_RES.html'>REF_TESK_LIST_RES</a></td><td>0x80A3B01B</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DELTE_PET_INFO_REQ.html'>DELTE_PET_INFO_REQ</a></td><td>0x00A3B01C</td><td><a href='./DELTE_PET_INFO_RES.html'>DELTE_PET_INFO_RES</a></td><td>0x80A3B01C</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_DOACTIVITY_REQ.html'>MARRIAGE_DOACTIVITY_REQ</a></td><td>0x00A3B01D</td><td><a href='./MARRIAGE_DOACTIVITY_RES.html'>MARRIAGE_DOACTIVITY_RES</a></td><td>0x80A3B01D</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./LA_FRIEND_REQ.html'>LA_FRIEND_REQ</a></td><td>0x00A3B01E</td><td><a href='./LA_FRIEND_RES.html'>LA_FRIEND_RES</a></td><td>0x80A3B01E</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TRY_NEWFRIEND_LIST_REQ.html'>TRY_NEWFRIEND_LIST_REQ</a></td><td>0x00A3B01F</td><td><a href='./TRY_NEWFRIEND_LIST_RES.html'>TRY_NEWFRIEND_LIST_RES</a></td><td>0x80A3B01F</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QINGQIU_PET_INFO_REQ.html'>QINGQIU_PET_INFO_REQ</a></td><td>0x00A3B020</td><td><a href='./QINGQIU_PET_INFO_RES.html'>QINGQIU_PET_INFO_RES</a></td><td>0x80A3B020</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REMOVE_ACTIVITY_NEW_REQ.html'>REMOVE_ACTIVITY_NEW_REQ</a></td><td>0x00A3B021</td><td><a href='./REMOVE_ACTIVITY_NEW_RES.html'>REMOVE_ACTIVITY_NEW_RES</a></td><td>0x80A3B021</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TRY_LEAVE_GAME_REQ.html'>TRY_LEAVE_GAME_REQ</a></td><td>0x00A3B022</td><td><a href='./TRY_LEAVE_GAME_RES.html'>TRY_LEAVE_GAME_RES</a></td><td>0x80A3B022</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_TESK_LIST_REQ.html'>GET_TESK_LIST_REQ</a></td><td>0x00A3B023</td><td><a href='./GET_TESK_LIST_RES.html'>GET_TESK_LIST_RES</a></td><td>0x80A3B023</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_GAME_PALAYERNAME_REQ.html'>GET_GAME_PALAYERNAME_REQ</a></td><td>0x00A3B024</td><td><a href='./GET_GAME_PALAYERNAME_RES.html'>GET_GAME_PALAYERNAME_RES</a></td><td>0x80A3B024</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_ACTIVITY_JOINIDS_REQ.html'>GET_ACTIVITY_JOINIDS_REQ</a></td><td>0x00A3B025</td><td><a href='./GET_ACTIVITY_JOINIDS_RES.html'>GET_ACTIVITY_JOINIDS_RES</a></td><td>0x80A3B025</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_GAMENAMES_REQ.html'>QUERY_GAMENAMES_REQ</a></td><td>0x00A3B026</td><td><a href='./QUERY_GAMENAMES_RES.html'>QUERY_GAMENAMES_RES</a></td><td>0x80A3B026</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_PET_NBWINFO_REQ.html'>GET_PET_NBWINFO_REQ</a></td><td>0x00A3B027</td><td><a href='./GET_PET_NBWINFO_RES.html'>GET_PET_NBWINFO_RES</a></td><td>0x80A3B027</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CLONE_FRIEND_LIST_REQ.html'>CLONE_FRIEND_LIST_REQ</a></td><td>0x00A3B028</td><td><a href='./CLONE_FRIEND_LIST_RES.html'>CLONE_FRIEND_LIST_RES</a></td><td>0x80A3B028</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_OTHERPLAYER_PET_MSG_REQ.html'>QUERY_OTHERPLAYER_PET_MSG_REQ</a></td><td>0x00A3B029</td><td><a href='./QUERY_OTHERPLAYER_PET_MSG_RES.html'>QUERY_OTHERPLAYER_PET_MSG_RES</a></td><td>0x80A3B029</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CSR_GET_PLAYER_REQ.html'>CSR_GET_PLAYER_REQ</a></td><td>0x00A3B02A</td><td><a href='./CSR_GET_PLAYER_RES.html'>CSR_GET_PLAYER_RES</a></td><td>0x80A3B02A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HAVE_OTHERNEW_INFO_REQ.html'>HAVE_OTHERNEW_INFO_REQ</a></td><td>0x00A3B02B</td><td><a href='./HAVE_OTHERNEW_INFO_RES.html'>HAVE_OTHERNEW_INFO_RES</a></td><td>0x80A3B02B</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SHANCHU_FRIENDLIST_REQ.html'>SHANCHU_FRIENDLIST_REQ</a></td><td>0x00A3B02C</td><td><a href='./SHANCHU_FRIENDLIST_RES.html'>SHANCHU_FRIENDLIST_RES</a></td><td>0x80A3B02C</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_TESK_LIST_REQ.html'>QUERY_TESK_LIST_REQ</a></td><td>0x00A3B02D</td><td><a href='./QUERY_TESK_LIST_RES.html'>QUERY_TESK_LIST_RES</a></td><td>0x80A3B02D</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CL_HORSE_INFO_REQ.html'>CL_HORSE_INFO_REQ</a></td><td>0x00A3B02E</td><td><a href='./CL_HORSE_INFO_RES.html'>CL_HORSE_INFO_RES</a></td><td>0x80A3B02E</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CL_NEWPET_MSG_REQ.html'>CL_NEWPET_MSG_REQ</a></td><td>0x00A3B02F</td><td><a href='./CL_NEWPET_MSG_RES.html'>CL_NEWPET_MSG_RES</a></td><td>0x80A3B02F</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_ACTIVITY_NEW_REQ.html'>GET_ACTIVITY_NEW_REQ</a></td><td>0x00A3B030</td><td><a href='./GET_ACTIVITY_NEW_RES.html'>GET_ACTIVITY_NEW_RES</a></td><td>0x80A3B030</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DO_SOME_REQ.html'>DO_SOME_REQ</a></td><td>0x00A3B031</td><td><a href='./DO_SOME_RES.html'>DO_SOME_RES</a></td><td>0x80A3B031</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TY_PET_REQ.html'>TY_PET_REQ</a></td><td>0x00A3B032</td><td><a href='./TY_PET_RES.html'>TY_PET_RES</a></td><td>0x80A3B032</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_GET_MSG_REQ.html'>EQUIPMENT_GET_MSG_REQ</a></td><td>0x00A3B033</td><td><a href='./EQUIPMENT_GET_MSG_RES.html'>EQUIPMENT_GET_MSG_RES</a></td><td>0x80A3B033</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQU_NEW_EQUIPMENT_REQ.html'>EQU_NEW_EQUIPMENT_REQ</a></td><td>0x00A3B034</td><td><a href='./EQU_NEW_EQUIPMENT_RES.html'>EQU_NEW_EQUIPMENT_RES</a></td><td>0x80A3B034</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DELETE_FRIEND_LIST_REQ.html'>DELETE_FRIEND_LIST_REQ</a></td><td>0x00A3B035</td><td><a href='./DELETE_FRIEND_LIST_RES.html'>DELETE_FRIEND_LIST_RES</a></td><td>0x80A3B035</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DO_PET_EQUIPMENT_REQ.html'>DO_PET_EQUIPMENT_REQ</a></td><td>0x00A3B036</td><td><a href='./DO_PET_EQUIPMENT_RES.html'>DO_PET_EQUIPMENT_RES</a></td><td>0x80A3B036</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QILING_NEW_INFO_REQ.html'>QILING_NEW_INFO_REQ</a></td><td>0x00A3B037</td><td><a href='./QILING_NEW_INFO_RES.html'>QILING_NEW_INFO_RES</a></td><td>0x80A3B037</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HORSE_QILING_INFO_REQ.html'>HORSE_QILING_INFO_REQ</a></td><td>0x00A3B038</td><td><a href='./HORSE_QILING_INFO_RES.html'>HORSE_QILING_INFO_RES</a></td><td>0x80A3B038</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HORSE_EQUIPMENT_QILING_REQ.html'>HORSE_EQUIPMENT_QILING_REQ</a></td><td>0x00A3B039</td><td><a href='./HORSE_EQUIPMENT_QILING_RES.html'>HORSE_EQUIPMENT_QILING_RES</a></td><td>0x80A3B039</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_EQU_QILING_REQ.html'>PET_EQU_QILING_REQ</a></td><td>0x00A3B03A</td><td><a href='./PET_EQU_QILING_RES.html'>PET_EQU_QILING_RES</a></td><td>0x80A3B03A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_TRYACTIVITY_REQ.html'>MARRIAGE_TRYACTIVITY_REQ</a></td><td>0x00A3B03B</td><td><a href='./MARRIAGE_TRYACTIVITY_RES.html'>MARRIAGE_TRYACTIVITY_RES</a></td><td>0x80A3B03B</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_TRY_QILING_REQ.html'>PET_TRY_QILING_REQ</a></td><td>0x00A3B03C</td><td><a href='./PET_TRY_QILING_RES.html'>PET_TRY_QILING_RES</a></td><td>0x80A3B03C</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PLAYER_CLEAN_QILINGLIST_REQ.html'>PLAYER_CLEAN_QILINGLIST_REQ</a></td><td>0x00A3B03D</td><td><a href='./PLAYER_CLEAN_QILINGLIST_RES.html'>PLAYER_CLEAN_QILINGLIST_RES</a></td><td>0x80A3B03D</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DELETE_TESK_LIST_REQ.html'>DELETE_TESK_LIST_REQ</a></td><td>0x00A3B03E</td><td><a href='./DELETE_TESK_LIST_RES.html'>DELETE_TESK_LIST_RES</a></td><td>0x80A3B03E</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_HEIMINGDAI_NEWLIST_REQ.html'>GET_HEIMINGDAI_NEWLIST_REQ</a></td><td>0x00A3B03F</td><td><a href='./GET_HEIMINGDAI_NEWLIST_RES.html'>GET_HEIMINGDAI_NEWLIST_RES</a></td><td>0x80A3B03F</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_CHOURENLIST_REQ.html'>QUERY_CHOURENLIST_REQ</a></td><td>0x00A3B040</td><td><a href='./QUERY_CHOURENLIST_RES.html'>QUERY_CHOURENLIST_RES</a></td><td>0x80A3B040</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QINCHU_PLAYER_REQ.html'>QINCHU_PLAYER_REQ</a></td><td>0x00A3B041</td><td><a href='./QINCHU_PLAYER_RES.html'>QINCHU_PLAYER_RES</a></td><td>0x80A3B041</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REMOVE_FRIEND_LIST_REQ.html'>REMOVE_FRIEND_LIST_REQ</a></td><td>0x00A3B042</td><td><a href='./REMOVE_FRIEND_LIST_RES.html'>REMOVE_FRIEND_LIST_RES</a></td><td>0x80A3B042</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TRY_REMOVE_CHOUREN_REQ.html'>TRY_REMOVE_CHOUREN_REQ</a></td><td>0x00A3B043</td><td><a href='./TRY_REMOVE_CHOUREN_RES.html'>TRY_REMOVE_CHOUREN_RES</a></td><td>0x80A3B043</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REMOVE_CHOUREN_REQ.html'>REMOVE_CHOUREN_REQ</a></td><td>0x00A3B044</td><td><a href='./REMOVE_CHOUREN_RES.html'>REMOVE_CHOUREN_RES</a></td><td>0x80A3B044</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DELETE_TASK_LIST_REQ.html'>DELETE_TASK_LIST_REQ</a></td><td>0x00A3B045</td><td><a href='./DELETE_TASK_LIST_RES.html'>DELETE_TASK_LIST_RES</a></td><td>0x80A3B045</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_TO_PLAYER_DEAL_REQ.html'>PLAYER_TO_PLAYER_DEAL_REQ</a></td><td>0x00A3B046</td><td><a href='./PLAYER_TO_PLAYER_DEAL_RES.html'>PLAYER_TO_PLAYER_DEAL_RES</a></td><td>0x80A3B046</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./AUCTION_NEW_LIST_MSG_REQ.html'>AUCTION_NEW_LIST_MSG_REQ</a></td><td>0x00A3B047</td><td><a href='./AUCTION_NEW_LIST_MSG_RES.html'>AUCTION_NEW_LIST_MSG_RES</a></td><td>0x80A3B047</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REQUEST_BUY_PLAYER_INFO_REQ.html'>REQUEST_BUY_PLAYER_INFO_REQ</a></td><td>0x00A3B048</td><td><a href='./REQUEST_BUY_PLAYER_INFO_RES.html'>REQUEST_BUY_PLAYER_INFO_RES</a></td><td>0x80A3B048</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOOTHER_PLAYER_MSGNAME_REQ.html'>BOOTHER_PLAYER_MSGNAME_REQ</a></td><td>0x00A3B049</td><td><a href='./BOOTHER_PLAYER_MSGNAME_RES.html'>BOOTHER_PLAYER_MSGNAME_RES</a></td><td>0x80A3B049</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOOTHER_MSG_CLEAN_REQ.html'>BOOTHER_MSG_CLEAN_REQ</a></td><td>0x00A3B04A</td><td><a href='./BOOTHER_MSG_CLEAN_RES.html'>BOOTHER_MSG_CLEAN_RES</a></td><td>0x80A3B04A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TRY_REQUESTBUY_CLEAN_ALL_REQ.html'>TRY_REQUESTBUY_CLEAN_ALL_REQ</a></td><td>0x00A3B04B</td><td><a href='./TRY_REQUESTBUY_CLEAN_ALL_RES.html'>TRY_REQUESTBUY_CLEAN_ALL_RES</a></td><td>0x80A3B04B</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SCHOOL_INFONAMES_REQ.html'>SCHOOL_INFONAMES_REQ</a></td><td>0x00A3B04C</td><td><a href='./SCHOOL_INFONAMES_RES.html'>SCHOOL_INFONAMES_RES</a></td><td>0x80A3B04C</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_NEW_LEVELUP_REQ.html'>PET_NEW_LEVELUP_REQ</a></td><td>0x00A3B04D</td><td><a href='./PET_NEW_LEVELUP_RES.html'>PET_NEW_LEVELUP_RES</a></td><td>0x80A3B04D</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./VALIDATE_ASK_NEW_REQ.html'>VALIDATE_ASK_NEW_REQ</a></td><td>0x00A3B04E</td><td><a href='./VALIDATE_ASK_NEW_RES.html'>VALIDATE_ASK_NEW_RES</a></td><td>0x80A3B04E</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JINGLIAN_NEW_TRY_REQ.html'>JINGLIAN_NEW_TRY_REQ</a></td><td>0x00A3B04F</td><td><a href='./JINGLIAN_NEW_TRY_RES.html'>JINGLIAN_NEW_TRY_RES</a></td><td>0x80A3B04F</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JINGLIAN_NEW_DO_REQ.html'>JINGLIAN_NEW_DO_REQ</a></td><td>0x00A3B050</td><td><a href='./JINGLIAN_NEW_DO_RES.html'>JINGLIAN_NEW_DO_RES</a></td><td>0x80A3B050</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JINGLIAN_PET_REQ.html'>JINGLIAN_PET_REQ</a></td><td>0x00A3B051</td><td><a href='./JINGLIAN_PET_RES.html'>JINGLIAN_PET_RES</a></td><td>0x80A3B051</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SEE_NEWFRIEND_LIST_REQ.html'>SEE_NEWFRIEND_LIST_REQ</a></td><td>0x00A3B052</td><td><a href='./SEE_NEWFRIEND_LIST_RES.html'>SEE_NEWFRIEND_LIST_RES</a></td><td>0x80A3B052</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQU_PET_HUN_REQ.html'>EQU_PET_HUN_REQ</a></td><td>0x00A3B053</td><td><a href='./EQU_PET_HUN_RES.html'>EQU_PET_HUN_RES</a></td><td>0x80A3B053</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_ADD_HUNPO_REQ.html'>PET_ADD_HUNPO_REQ</a></td><td>0x00A3B054</td><td><a href='./PET_ADD_HUNPO_RES.html'>PET_ADD_HUNPO_RES</a></td><td>0x80A3B054</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_ADD_SHENGMINGVALUE_REQ.html'>PET_ADD_SHENGMINGVALUE_REQ</a></td><td>0x00A3B055</td><td><a href='./PET_ADD_SHENGMINGVALUE_RES.html'>PET_ADD_SHENGMINGVALUE_RES</a></td><td>0x80A3B055</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HORSE_REMOVE_HUNPO_REQ.html'>HORSE_REMOVE_HUNPO_REQ</a></td><td>0x00A3B056</td><td><a href='./HORSE_REMOVE_HUNPO_RES.html'>HORSE_REMOVE_HUNPO_RES</a></td><td>0x80A3B056</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_REMOVE_HUNPO_REQ.html'>PET_REMOVE_HUNPO_REQ</a></td><td>0x00A3B057</td><td><a href='./PET_REMOVE_HUNPO_RES.html'>PET_REMOVE_HUNPO_RES</a></td><td>0x80A3B057</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_CLEAN_HORSEHUNLIANG_REQ.html'>PLAYER_CLEAN_HORSEHUNLIANG_REQ</a></td><td>0x00A3B058</td><td><a href='./PLAYER_CLEAN_HORSEHUNLIANG_RES.html'>PLAYER_CLEAN_HORSEHUNLIANG_RES</a></td><td>0x80A3B058</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_NEW_LEVELUP_REQ.html'>GET_NEW_LEVELUP_REQ</a></td><td>0x00A3B059</td><td><a href='./GET_NEW_LEVELUP_RES.html'>GET_NEW_LEVELUP_RES</a></td><td>0x80A3B059</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DO_HOSEE2OTHER_REQ.html'>DO_HOSEE2OTHER_REQ</a></td><td>0x00A3B05A</td><td><a href='./DO_HOSEE2OTHER_RES.html'>DO_HOSEE2OTHER_RES</a></td><td>0x80A3B05A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TRYDELETE_PET_INFO_REQ.html'>TRYDELETE_PET_INFO_REQ</a></td><td>0x00A3B05B</td><td><a href='./TRYDELETE_PET_INFO_RES.html'>TRYDELETE_PET_INFO_RES</a></td><td>0x80A3B05B</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HAHA_ACTIVITY_MSG_REQ.html'>HAHA_ACTIVITY_MSG_REQ</a></td><td>0x00A3B05C</td><td><a href='./HAHA_ACTIVITY_MSG_RES.html'>HAHA_ACTIVITY_MSG_RES</a></td><td>0x80A3B05C</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./VALIDATE_NEW_REQ.html'>VALIDATE_NEW_REQ</a></td><td>0x00A3B05D</td><td><a href='./VALIDATE_NEW_RES.html'>VALIDATE_NEW_RES</a></td><td>0x80A3B05D</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./VALIDATE_ANDSWER_NEW_REQ.html'>VALIDATE_ANDSWER_NEW_REQ</a></td><td>0x00A3B05E</td><td><a href='./VALIDATE_ANDSWER_NEW_RES.html'>VALIDATE_ANDSWER_NEW_RES</a></td><td>0x80A3B05E</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PLAYER_ASK_TO_OTHWE_REQ.html'>PLAYER_ASK_TO_OTHWE_REQ</a></td><td>0x00A3B05F</td><td><a href='./PLAYER_ASK_TO_OTHWE_RES.html'>PLAYER_ASK_TO_OTHWE_RES</a></td><td>0x80A3B05F</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GA_GET_SOME_REQ.html'>GA_GET_SOME_REQ</a></td><td>0x00A3B060</td><td><a href='./GA_GET_SOME_RES.html'>GA_GET_SOME_RES</a></td><td>0x80A3B060</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./OTHER_PET_LEVELUP_REQ.html'>OTHER_PET_LEVELUP_REQ</a></td><td>0x00A3B061</td><td><a href='./OTHER_PET_LEVELUP_RES.html'>OTHER_PET_LEVELUP_RES</a></td><td>0x80A3B061</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MY_OTHER_FRIEDN_REQ.html'>MY_OTHER_FRIEDN_REQ</a></td><td>0x00A3B062</td><td><a href='./MY_OTHER_FRIEDN_RES.html'>MY_OTHER_FRIEDN_RES</a></td><td>0x80A3B062</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DOSOME_SB_MSG_REQ.html'>DOSOME_SB_MSG_REQ</a></td><td>0x00A3B063</td><td><a href='./DOSOME_SB_MSG_RES.html'>DOSOME_SB_MSG_RES</a></td><td>0x80A3B063</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MIESHI_GET_VERSION_INFO_REQ.html'>MIESHI_GET_VERSION_INFO_REQ</a></td><td>0x0002A016</td><td><a href='./MIESHI_GET_VERSION_INFO_RES.html'>MIESHI_GET_VERSION_INFO_RES</a></td><td>0x8002A016</td><td>灭世游戏获取资源版本信息程序版本信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MIESHI_GET_RESOURCE_PACKAGE_INFO_REQ.html'>MIESHI_GET_RESOURCE_PACKAGE_INFO_REQ</a></td><td>0x0002A015</td><td><a href='./MIESHI_GET_RESOURCE_PACKAGE_INFO_RES.html'>MIESHI_GET_RESOURCE_PACKAGE_INFO_RES</a></td><td>0x8002A015</td><td>灭世游戏获取最新的资源包</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MIESHI_GET_RESOURCE_FILE_LIST_REQ.html'>MIESHI_GET_RESOURCE_FILE_LIST_REQ</a></td><td>0x0002A017</td><td><a href='./MIESHI_GET_RESOURCE_FILE_LIST_RES.html'>MIESHI_GET_RESOURCE_FILE_LIST_RES</a></td><td>0x8002A017</td><td>灭世游戏请求资源文件列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MIESHI_GET_RESOURCE_REQ.html'>MIESHI_GET_RESOURCE_REQ</a></td><td>0x07777777</td><td><a href='./MIESHI_GET_RESOURCE_RES.html'>MIESHI_GET_RESOURCE_RES</a></td><td>0x87777777</td><td>灭世游戏客户端从服务器获取资源</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MIESHI_RESOURCE_FILE_REQ.html'>MIESHI_RESOURCE_FILE_REQ</a></td><td>0x66666666</td><td><a href='./-.html'>-</a></td><td>-</td><td>灭世游戏服务器给客户端发送资源包，一个完整的资源包由多个小资源包拼接而成</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MIESHI_RESOURCE_PROGRESS_REQ.html'>MIESHI_RESOURCE_PROGRESS_REQ</a></td><td>0x66666669</td><td><a href='./MIESHI_RESOURCE_PROGRESS_RES.html'>MIESHI_RESOURCE_PROGRESS_RES</a></td><td>0x76666669</td><td>灭世游戏客户端给服务器发送资源更新进度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./OPEN_WINDOW_REQ.html'>OPEN_WINDOW_REQ</a></td><td>0x66666667</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端弹出窗口</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CLIENT_ERROR_REQ.html'>CLIENT_ERROR_REQ</a></td><td>0x66666668</td><td><a href='./-.html'>-</a></td><td>-</td><td>android客户端logcat检查到有崩的日志，发给服务器</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_ACCOUNT_VALID_REQ.html'>QUERY_ACCOUNT_VALID_REQ</a></td><td>0x0002A021</td><td><a href='./QUERY_ACCOUNT_VALID_RES.html'>QUERY_ACCOUNT_VALID_RES</a></td><td>0x8002A021</td><td>客户端给网关发送验证账号是否合法</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_SERVER_LIST_REQ.html'>QUERY_SERVER_LIST_REQ</a></td><td>0x0002A023</td><td><a href='./QUERY_SERVER_LIST_RES.html'>QUERY_SERVER_LIST_RES</a></td><td>0x8002A023</td><td>客户端查询的所有服务器状态</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PASSPORT_GETBACK_REQ.html'>PASSPORT_GETBACK_REQ</a></td><td>0x000EE002</td><td><a href='./PASSPORT_GETBACK_RES.html'>PASSPORT_GETBACK_RES</a></td><td>0x800EE002</td><td>忘记密码</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PHONE_UIID_INFO_REQ.html'>PHONE_UIID_INFO_REQ</a></td><td>0x000EE006</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端通知服务端用户手机唯一标示之类的信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SERVERSLIST_AND_PASSPORTQUESTION_REQ.html'>SERVERSLIST_AND_PASSPORTQUESTION_REQ</a></td><td>0x000EE003</td><td><a href='./SERVERSLIST_AND_PASSPORTQUESTION_RES.html'>SERVERSLIST_AND_PASSPORTQUESTION_RES</a></td><td>0x800EE003</td><td>返回游戏大区，服务器列表，密保问题</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SEND_CLIENT_EXTENDINFO_REQ.html'>SEND_CLIENT_EXTENDINFO_REQ</a></td><td>0x002EE100</td><td><a href='./SEND_CLIENT_EXTENDINFO_RES.html'>SEND_CLIENT_EXTENDINFO_RES</a></td><td>0x802EE100</td><td>发送客户端手机号等信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLATFORM_ARGS_REQ.html'>PLATFORM_ARGS_REQ</a></td><td>0x000EE001</td><td><a href='./PLATFORM_ARGS_RES.html'>PLATFORM_ARGS_RES</a></td><td>0x800EE001</td><td>客户端给网关请求腾讯的参数</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEW_MIESHI_GET_VERSION_INFO_REQ.html'>NEW_MIESHI_GET_VERSION_INFO_REQ</a></td><td>0x0002A018</td><td><a href='./-.html'>-</a></td><td>-</td><td>灭世游戏获取资源版本信息程序版本信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEW_QUERY_WINDOW_REQ.html'>NEW_QUERY_WINDOW_REQ</a></td><td>0x002EE106</td><td><a href='./-.html'>-</a></td><td>-</td><td>查询某个NPC身上的窗口</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEW_OPTION_SELECT_REQ.html'>NEW_OPTION_SELECT_REQ</a></td><td>0x002EE107</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端选择了某个窗口中的某个选项，客户端要根据选项中的类型来判断是否要发送此指令，以及是否要等待QUERY_WINDOW_RES响应</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MIESHI_MAP_LANG_TRANSLATE.html'>MIESHI_MAP_LANG_TRANSLATE</a></td><td>0x000EE008</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端地图名翻译</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./MIESHI_DELETE_ONE_RESOURCE_RES.html'>MIESHI_DELETE_ONE_RESOURCE_RES</a></td><td>0x0002A027</td><td>服务器通知客户端，删除某个资源,可以是mieshi_resource下的任何文件或是sd中的任何文件</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MIESHI_SKILL_INFO_REQ.html'>MIESHI_SKILL_INFO_REQ</a></td><td>0x0002A024</td><td><a href='./-.html'>-</a></td><td>-</td><td>灭世游戏服务器给网关发送技能数据，网关把数据存放到资源文件夹的other目录中</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MIESHI_UPDATE_PLAYER_INFO.html'>MIESHI_UPDATE_PLAYER_INFO</a></td><td>0x0002A025</td><td><a href='./-.html'>-</a></td><td>-</td><td>灭世游戏服务器给网关发送技能数据</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEW_MIESHI_UPDATE_PLAYER_INFO.html'>NEW_MIESHI_UPDATE_PLAYER_INFO</a></td><td>0x0002A030</td><td><a href='./-.html'>-</a></td><td>-</td><td>游戏服务器给网关发送角色数据</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./UPDATE_PLAYER_INFO_FOR_HEFU_REQ.html'>UPDATE_PLAYER_INFO_FOR_HEFU_REQ</a></td><td>0x0002A031</td><td><a href='./-.html'>-</a></td><td>-</td><td>游戏服务器给网关发送角色更新数据以便更新角色名称</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ATTENTIONS_REQ.html'>ATTENTIONS_REQ</a></td><td>0x000EE004</td><td><a href='./ATTENTIONS_RES.html'>ATTENTIONS_RES</a></td><td>0x800EE004</td><td>注意事项</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DENY_USER_REQ.html'>DENY_USER_REQ</a></td><td>0x002EE005</td><td><a href='./DENY_USER_RES.html'>DENY_USER_RES</a></td><td>0x802EE005</td><td>封帐号</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REPORT_ONLINENUM_REQ.html'>REPORT_ONLINENUM_REQ</a></td><td>0x002EE077</td><td><a href='./REPORT_ONLINENUM_RES.html'>REPORT_ONLINENUM_RES</a></td><td>0x802EE077</td><td>汇报服务器的在线人数</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REPORT_LONG_HEARTBEAT_REQ.html'>REPORT_LONG_HEARTBEAT_REQ</a></td><td>0x002EE078</td><td><a href='./-.html'>-</a></td><td>-</td><td>汇报服务器的超时心跳</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REPORT_LONG_PROTOCAL_REQ.html'>REPORT_LONG_PROTOCAL_REQ</a></td><td>0x002EE178</td><td><a href='./-.html'>-</a></td><td>-</td><td>汇报服务器的超时协议</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REPORT_CHAT_REQ.html'>REPORT_CHAT_REQ</a></td><td>0x002EE079</td><td><a href='./-.html'>-</a></td><td>-</td><td>汇报服务器的世界发言</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_CLIENT_INFO_REQ.html'>QUERY_CLIENT_INFO_REQ</a></td><td>0x002EE099</td><td><a href='./QUERY_CLIENT_INFO_RES.html'>QUERY_CLIENT_INFO_RES</a></td><td>0x802EE099</td><td>服务器查询某个玩家的客户端信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_LOGIN_INFO_REQ.html'>QUERY_LOGIN_INFO_REQ</a></td><td>0x002EE101</td><td><a href='./QUERY_LOGIN_INFO_RES.html'>QUERY_LOGIN_INFO_RES</a></td><td>0x802EE101</td><td>查询用户最近一次登录信息（目前只用来查询最近一次登陆时间）</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SESSION_VALIDATE_REQ.html'>SESSION_VALIDATE_REQ</a></td><td>0x002EE102</td><td><a href='./SESSION_VALIDATE_RES.html'>SESSION_VALIDATE_RES</a></td><td>0x802EE102</td><td>服务器向网关校验session</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTIFY_USER_ENTERSERVER_REQ.html'>NOTIFY_USER_ENTERSERVER_REQ</a></td><td>0x002EE103</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知网关某个用户进入</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_SERVER_TIREN_REQ.html'>NOTIFY_SERVER_TIREN_REQ</a></td><td>0x002EE104</td><td><a href='./-.html'>-</a></td><td>-</td><td>网关通知服务器T掉某个用户</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTIFY_USER_LEAVESERVER_REQ.html'>NOTIFY_USER_LEAVESERVER_REQ</a></td><td>0x002EE105</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通过网关某个用户离开</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GM_ACTION_REQ.html'>GM_ACTION_REQ</a></td><td>0x002EE109</td><td><a href='./-.html'>-</a></td><td>-</td><td>GM操作补发页面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_WAIGUA_PROCESS_NAMES_REQ.html'>GET_WAIGUA_PROCESS_NAMES_REQ</a></td><td>0x002EE110</td><td><a href='./GET_WAIGUA_PROCESS_NAMES_RES.html'>GET_WAIGUA_PROCESS_NAMES_RES</a></td><td>0x802EE110</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_NOTICE_REQ.html'>GET_NOTICE_REQ</a></td><td>0x002EE111</td><td><a href='./GET_NOTICE_RES.html'>GET_NOTICE_RES</a></td><td>0x802EE111</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./THIRDPART_PROMOTE_REQ.html'>THIRDPART_PROMOTE_REQ</a></td><td>0x002EE112</td><td><a href='./THIRDPART_PROMOTE_RES.html'>THIRDPART_PROMOTE_RES</a></td><td>0x802EE112</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEW_QUERY_SERVER_LIST_REQ.html'>NEW_QUERY_SERVER_LIST_REQ</a></td><td>0x002EE113</td><td><a href='./NEW_QUERY_SERVER_LIST_RES.html'>NEW_QUERY_SERVER_LIST_RES</a></td><td>0x802EE113</td><td>客户端查询的所有服务器状态</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ANNOUNCEMENT_REQ.html'>ANNOUNCEMENT_REQ</a></td><td>0x002EE114</td><td><a href='./ANNOUNCEMENT_RES.html'>ANNOUNCEMENT_RES</a></td><td>0x802EE114</td><td>公告与合服提示协议</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_WHITE_LIST_REQ.html'>QUERY_WHITE_LIST_REQ</a></td><td>0x002EE115</td><td><a href='./QUERY_WHITE_LIST_RES.html'>QUERY_WHITE_LIST_RES</a></td><td>0x802EE115</td><td>游戏服务器给网关发送角色数据</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./VALIDATE_DEVICE_INFO_REQ.html'>VALIDATE_DEVICE_INFO_REQ</a></td><td>0x002EE116</td><td><a href='./VALIDATE_DEVICE_INFO_RES.html'>VALIDATE_DEVICE_INFO_RES</a></td><td>0x802EE116</td><td>游戏服务器给网关发送角色数据</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MODIFY_VIP_INFO_REQ.html'>MODIFY_VIP_INFO_REQ</a></td><td>0x002EE117</td><td><a href='./MODIFY_VIP_INFO_RES.html'>MODIFY_VIP_INFO_RES</a></td><td>0x802EE117</td><td>是否有权限修改vip资料</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_PHONE_CODE_REQ.html'>GET_PHONE_CODE_REQ</a></td><td>0x002EE118</td><td><a href='./GET_PHONE_CODE_RES.html'>GET_PHONE_CODE_RES</a></td><td>0x802EE118</td><td>获取验证码</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUICK_REGISTER_REQ.html'>QUICK_REGISTER_REQ</a></td><td>0x002EE119</td><td><a href='./QUICK_REGISTER_RES.html'>QUICK_REGISTER_RES</a></td><td>0x802EE119</td><td>快速注册</td></tr>
 * </table>
 */
public class GameMessageFactory extends AbstractMessageFactory {

	private static long sequnceNum = 0L;
	public synchronized static long nextSequnceNum(){
		sequnceNum ++;
		if(sequnceNum >= 0x7FFFFFFF){
			sequnceNum = 1L;
		}
		return sequnceNum;
	}

	protected static GameMessageFactory self;

	public static GameMessageFactory getInstance(){
		if(self != null){
			return self;
		}
		synchronized(GameMessageFactory.class){
			if(self != null) return self;
			self = new GameMessageFactory();
		}
		return self;
	}

	public Message newMessage(byte[] messageContent,int offset,int size)
		throws MessageFormatErrorException, ConnectionException,Exception {
		int len = (int)byteArrayToNumber(messageContent,offset,getNumOfByteForMessageLength());
		if(len != size)
			throw new MessageFormatErrorException("message length not match");
		int end = offset + size;
		offset += getNumOfByteForMessageLength();
		long type = byteArrayToNumber(messageContent,offset,4);
		offset += 4;
		long sn = byteArrayToNumber(messageContent,offset,4);
		offset += 4;

			if(type == 0x00000015L){
					return new HEARTBEAT_CHECK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80000015L){
					return new HEARTBEAT_CHECK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E002L){
					return new QUERY_DISPLAYER_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000E002L){
					return new QUERY_DISPLAYER_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E001L){
					return new PASSPORT_REGISTER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000E001L){
					return new PASSPORT_REGISTER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E005L){
					return new GET_REGISTER_IMAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000E005L){
					return new GET_REGISTER_IMAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E006L){
					return new GET_REGISTER_IMAGE_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000E006L){
					return new GET_REGISTER_IMAGE_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E004L){
					return new PASSPORT_REGISTER_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000E004L){
					return new PASSPORT_REGISTER_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E010L){
					return new PASSPORT_REGISTER_PRO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000E010L){
					return new PASSPORT_REGISTER_PRO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E011L){
					return new SEND_BANHAO_TO_CLIENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F009L){
					return new USER_GETBACK_PASSWORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F009L){
					return new USER_GETBACK_PASSWORD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F010L){
					return new USER_GET_PASSWORD_PROTECT_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F010L){
					return new USER_GET_PASSWORD_PROTECT_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F013L){
					return new USER_LEAVE_SERVER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F013L){
					return new USER_LEAVE_SERVER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0FFFF014L){
					return new USER_QUERY_PROTECT_QUESTION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8FFFF014L){
					return new USER_QUERY_PROTECT_QUESTION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0FFFF015L){
					return new USER_CHANGE_PASSWORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8FFFF015L){
					return new USER_CHANGE_PASSWORD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0234AB90L){
					return new NEW_USER_CHANGE_PASSWORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0234AB91L){
					return new NEW_USER_CHANGE_PASSWORD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0234AB89L){
					return new NEW_USER_LOGIN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8234AB89L){
					return new NEW_USER_LOGIN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000011L){
					return new USER_LOGIN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80000011L){
					return new USER_LOGIN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EE007L){
					return new USER_CLIENT_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F011L){
					return new USER_SET_PASSWORD_PROTECT_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F011L){
					return new USER_SET_PASSWORD_PROTECT_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F006L){
					return new USER_UPDATE_PASSWORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F006L){
					return new USER_UPDATE_PASSWORD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F002L){
					return new USER_UPDATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F002L){
					return new USER_UPDATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F020L){
					return new USER_PROTECT_BAN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000F020L){
					return new USER_PROTECT_BAN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EB01L){
					return new CHANGE_SERVER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000EB01L){
					return new CHANGE_SERVER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EB03L){
					return new LEAVE_GAME_NOTIFY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8000EB03L){
					return new LEAVE_GAME_NOTIFY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000010L){
					return new ACTIVE_TEST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80000010L){
					return new ACTIVE_TEST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000ff012L){
					return new GET_TIPS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x800ff012L){
					return new GET_TIPS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00fff013L){
					return new GET_SOME4ANDROID_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80fff013L){
					return new GET_SOME4ANDROID_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00fff014L){
					return new GET_SOME4ANDROID_1_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80fff014L){
					return new GET_SOME4ANDROID_1_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A300L){
					return new SERVER_HAND_CLIENT_1_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A300L){
					return new SERVER_HAND_CLIENT_1_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A301L){
					return new SERVER_HAND_CLIENT_2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A301L){
					return new SERVER_HAND_CLIENT_2_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A302L){
					return new SERVER_HAND_CLIENT_3_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A302L){
					return new SERVER_HAND_CLIENT_3_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A303L){
					return new SERVER_HAND_CLIENT_4_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A303L){
					return new SERVER_HAND_CLIENT_4_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A304L){
					return new SERVER_HAND_CLIENT_5_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A304L){
					return new SERVER_HAND_CLIENT_5_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A305L){
					return new SERVER_HAND_CLIENT_6_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A305L){
					return new SERVER_HAND_CLIENT_6_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A306L){
					return new SERVER_HAND_CLIENT_7_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A306L){
					return new SERVER_HAND_CLIENT_7_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A307L){
					return new SERVER_HAND_CLIENT_8_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A307L){
					return new SERVER_HAND_CLIENT_8_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A308L){
					return new SERVER_HAND_CLIENT_9_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A308L){
					return new SERVER_HAND_CLIENT_9_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A309L){
					return new SERVER_HAND_CLIENT_10_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A309L){
					return new SERVER_HAND_CLIENT_10_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A30AL){
					return new SERVER_HAND_CLIENT_11_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A30AL){
					return new SERVER_HAND_CLIENT_11_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A30BL){
					return new SERVER_HAND_CLIENT_12_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A30BL){
					return new SERVER_HAND_CLIENT_12_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A30CL){
					return new SERVER_HAND_CLIENT_13_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A30CL){
					return new SERVER_HAND_CLIENT_13_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A30DL){
					return new SERVER_HAND_CLIENT_14_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A30DL){
					return new SERVER_HAND_CLIENT_14_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A30EL){
					return new SERVER_HAND_CLIENT_15_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A30EL){
					return new SERVER_HAND_CLIENT_15_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A30FL){
					return new SERVER_HAND_CLIENT_16_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A30FL){
					return new SERVER_HAND_CLIENT_16_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A310L){
					return new SERVER_HAND_CLIENT_17_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A310L){
					return new SERVER_HAND_CLIENT_17_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A311L){
					return new SERVER_HAND_CLIENT_18_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A311L){
					return new SERVER_HAND_CLIENT_18_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A312L){
					return new SERVER_HAND_CLIENT_19_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A312L){
					return new SERVER_HAND_CLIENT_19_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A313L){
					return new SERVER_HAND_CLIENT_20_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A313L){
					return new SERVER_HAND_CLIENT_20_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A314L){
					return new SERVER_HAND_CLIENT_21_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A314L){
					return new SERVER_HAND_CLIENT_21_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A315L){
					return new SERVER_HAND_CLIENT_22_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A315L){
					return new SERVER_HAND_CLIENT_22_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A316L){
					return new SERVER_HAND_CLIENT_23_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A316L){
					return new SERVER_HAND_CLIENT_23_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A317L){
					return new SERVER_HAND_CLIENT_24_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A317L){
					return new SERVER_HAND_CLIENT_24_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A318L){
					return new SERVER_HAND_CLIENT_25_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A318L){
					return new SERVER_HAND_CLIENT_25_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A319L){
					return new SERVER_HAND_CLIENT_26_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A319L){
					return new SERVER_HAND_CLIENT_26_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A31AL){
					return new SERVER_HAND_CLIENT_27_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A31AL){
					return new SERVER_HAND_CLIENT_27_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A31BL){
					return new SERVER_HAND_CLIENT_28_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A31BL){
					return new SERVER_HAND_CLIENT_28_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A31CL){
					return new SERVER_HAND_CLIENT_29_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A31CL){
					return new SERVER_HAND_CLIENT_29_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A31DL){
					return new SERVER_HAND_CLIENT_30_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A31DL){
					return new SERVER_HAND_CLIENT_30_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A31EL){
					return new SERVER_HAND_CLIENT_31_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A31EL){
					return new SERVER_HAND_CLIENT_31_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A31FL){
					return new SERVER_HAND_CLIENT_32_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A31FL){
					return new SERVER_HAND_CLIENT_32_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A320L){
					return new SERVER_HAND_CLIENT_33_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A320L){
					return new SERVER_HAND_CLIENT_33_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A321L){
					return new SERVER_HAND_CLIENT_34_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A321L){
					return new SERVER_HAND_CLIENT_34_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A322L){
					return new SERVER_HAND_CLIENT_35_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A322L){
					return new SERVER_HAND_CLIENT_35_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A323L){
					return new SERVER_HAND_CLIENT_36_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A323L){
					return new SERVER_HAND_CLIENT_36_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A324L){
					return new SERVER_HAND_CLIENT_37_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A324L){
					return new SERVER_HAND_CLIENT_37_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A325L){
					return new SERVER_HAND_CLIENT_38_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A325L){
					return new SERVER_HAND_CLIENT_38_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A326L){
					return new SERVER_HAND_CLIENT_39_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A326L){
					return new SERVER_HAND_CLIENT_39_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A327L){
					return new SERVER_HAND_CLIENT_40_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A327L){
					return new SERVER_HAND_CLIENT_40_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A328L){
					return new SERVER_HAND_CLIENT_41_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A328L){
					return new SERVER_HAND_CLIENT_41_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A329L){
					return new SERVER_HAND_CLIENT_42_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A329L){
					return new SERVER_HAND_CLIENT_42_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A32AL){
					return new SERVER_HAND_CLIENT_43_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A32AL){
					return new SERVER_HAND_CLIENT_43_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A32BL){
					return new SERVER_HAND_CLIENT_44_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A32BL){
					return new SERVER_HAND_CLIENT_44_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A32CL){
					return new SERVER_HAND_CLIENT_45_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A32CL){
					return new SERVER_HAND_CLIENT_45_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A32DL){
					return new SERVER_HAND_CLIENT_46_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A32DL){
					return new SERVER_HAND_CLIENT_46_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A32EL){
					return new SERVER_HAND_CLIENT_47_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A32EL){
					return new SERVER_HAND_CLIENT_47_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A32FL){
					return new SERVER_HAND_CLIENT_48_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A32FL){
					return new SERVER_HAND_CLIENT_48_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A330L){
					return new SERVER_HAND_CLIENT_49_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A330L){
					return new SERVER_HAND_CLIENT_49_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A331L){
					return new SERVER_HAND_CLIENT_50_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A331L){
					return new SERVER_HAND_CLIENT_50_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A332L){
					return new SERVER_HAND_CLIENT_51_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A332L){
					return new SERVER_HAND_CLIENT_51_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A333L){
					return new SERVER_HAND_CLIENT_52_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A333L){
					return new SERVER_HAND_CLIENT_52_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A334L){
					return new SERVER_HAND_CLIENT_53_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A334L){
					return new SERVER_HAND_CLIENT_53_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A335L){
					return new SERVER_HAND_CLIENT_54_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A335L){
					return new SERVER_HAND_CLIENT_54_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A336L){
					return new SERVER_HAND_CLIENT_55_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A336L){
					return new SERVER_HAND_CLIENT_55_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A337L){
					return new SERVER_HAND_CLIENT_56_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A337L){
					return new SERVER_HAND_CLIENT_56_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A338L){
					return new SERVER_HAND_CLIENT_57_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A338L){
					return new SERVER_HAND_CLIENT_57_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A339L){
					return new SERVER_HAND_CLIENT_58_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A339L){
					return new SERVER_HAND_CLIENT_58_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A33AL){
					return new SERVER_HAND_CLIENT_59_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A33AL){
					return new SERVER_HAND_CLIENT_59_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A33BL){
					return new SERVER_HAND_CLIENT_60_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A33BL){
					return new SERVER_HAND_CLIENT_60_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A33CL){
					return new SERVER_HAND_CLIENT_61_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A33CL){
					return new SERVER_HAND_CLIENT_61_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A33DL){
					return new SERVER_HAND_CLIENT_62_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A33DL){
					return new SERVER_HAND_CLIENT_62_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A33EL){
					return new SERVER_HAND_CLIENT_63_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A33EL){
					return new SERVER_HAND_CLIENT_63_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A33FL){
					return new SERVER_HAND_CLIENT_64_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A33FL){
					return new SERVER_HAND_CLIENT_64_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A340L){
					return new SERVER_HAND_CLIENT_65_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A340L){
					return new SERVER_HAND_CLIENT_65_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A341L){
					return new SERVER_HAND_CLIENT_66_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A341L){
					return new SERVER_HAND_CLIENT_66_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A342L){
					return new SERVER_HAND_CLIENT_67_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A342L){
					return new SERVER_HAND_CLIENT_67_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A343L){
					return new SERVER_HAND_CLIENT_68_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A343L){
					return new SERVER_HAND_CLIENT_68_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A344L){
					return new SERVER_HAND_CLIENT_69_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A344L){
					return new SERVER_HAND_CLIENT_69_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A345L){
					return new SERVER_HAND_CLIENT_70_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A345L){
					return new SERVER_HAND_CLIENT_70_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A346L){
					return new SERVER_HAND_CLIENT_71_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A346L){
					return new SERVER_HAND_CLIENT_71_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A347L){
					return new SERVER_HAND_CLIENT_72_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A347L){
					return new SERVER_HAND_CLIENT_72_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A348L){
					return new SERVER_HAND_CLIENT_73_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A348L){
					return new SERVER_HAND_CLIENT_73_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A349L){
					return new SERVER_HAND_CLIENT_74_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A349L){
					return new SERVER_HAND_CLIENT_74_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A34AL){
					return new SERVER_HAND_CLIENT_75_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A34AL){
					return new SERVER_HAND_CLIENT_75_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A34BL){
					return new SERVER_HAND_CLIENT_76_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A34BL){
					return new SERVER_HAND_CLIENT_76_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A34CL){
					return new SERVER_HAND_CLIENT_77_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A34CL){
					return new SERVER_HAND_CLIENT_77_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A34DL){
					return new SERVER_HAND_CLIENT_78_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A34DL){
					return new SERVER_HAND_CLIENT_78_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A34EL){
					return new SERVER_HAND_CLIENT_79_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A34EL){
					return new SERVER_HAND_CLIENT_79_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A34FL){
					return new SERVER_HAND_CLIENT_80_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A34FL){
					return new SERVER_HAND_CLIENT_80_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A350L){
					return new SERVER_HAND_CLIENT_81_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A350L){
					return new SERVER_HAND_CLIENT_81_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A351L){
					return new SERVER_HAND_CLIENT_82_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A351L){
					return new SERVER_HAND_CLIENT_82_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A352L){
					return new SERVER_HAND_CLIENT_83_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A352L){
					return new SERVER_HAND_CLIENT_83_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A353L){
					return new SERVER_HAND_CLIENT_84_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A353L){
					return new SERVER_HAND_CLIENT_84_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A354L){
					return new SERVER_HAND_CLIENT_85_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A354L){
					return new SERVER_HAND_CLIENT_85_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A355L){
					return new SERVER_HAND_CLIENT_86_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A355L){
					return new SERVER_HAND_CLIENT_86_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A356L){
					return new SERVER_HAND_CLIENT_87_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A356L){
					return new SERVER_HAND_CLIENT_87_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A357L){
					return new SERVER_HAND_CLIENT_88_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A357L){
					return new SERVER_HAND_CLIENT_88_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A358L){
					return new SERVER_HAND_CLIENT_89_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A358L){
					return new SERVER_HAND_CLIENT_89_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A359L){
					return new SERVER_HAND_CLIENT_90_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A359L){
					return new SERVER_HAND_CLIENT_90_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A35AL){
					return new SERVER_HAND_CLIENT_91_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A35AL){
					return new SERVER_HAND_CLIENT_91_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A35BL){
					return new SERVER_HAND_CLIENT_92_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A35BL){
					return new SERVER_HAND_CLIENT_92_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A35CL){
					return new SERVER_HAND_CLIENT_93_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A35CL){
					return new SERVER_HAND_CLIENT_93_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A35DL){
					return new SERVER_HAND_CLIENT_94_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A35DL){
					return new SERVER_HAND_CLIENT_94_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A35EL){
					return new SERVER_HAND_CLIENT_95_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A35EL){
					return new SERVER_HAND_CLIENT_95_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A35FL){
					return new SERVER_HAND_CLIENT_96_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A35FL){
					return new SERVER_HAND_CLIENT_96_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A360L){
					return new SERVER_HAND_CLIENT_97_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A360L){
					return new SERVER_HAND_CLIENT_97_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A361L){
					return new SERVER_HAND_CLIENT_98_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A361L){
					return new SERVER_HAND_CLIENT_98_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A362L){
					return new SERVER_HAND_CLIENT_99_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A362L){
					return new SERVER_HAND_CLIENT_99_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A363L){
					return new SERVER_HAND_CLIENT_100_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A363L){
					return new SERVER_HAND_CLIENT_100_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B000L){
					return new TRY_GETPLAYERINFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B000L){
					return new TRY_GETPLAYERINFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B001L){
					return new WAIT_FOR_OTHER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B001L){
					return new WAIT_FOR_OTHER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B002L){
					return new GET_REWARD_2_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B002L){
					return new GET_REWARD_2_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B003L){
					return new REQUESTBUY_GET_ENTITY_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B003L){
					return new REQUESTBUY_GET_ENTITY_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B004L){
					return new PLAYER_SOUL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B004L){
					return new PLAYER_SOUL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B005L){
					return new CARD_TRYSAVING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B005L){
					return new CARD_TRYSAVING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B006L){
					return new GANG_WAREHOUSE_JOURNAL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B006L){
					return new GANG_WAREHOUSE_JOURNAL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B007L){
					return new GET_WAREHOUSE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B007L){
					return new GET_WAREHOUSE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B008L){
					return new QUERY__GETAUTOBACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B008L){
					return new QUERY__GETAUTOBACK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B009L){
					return new GET_ZONGPAI_NAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B009L){
					return new GET_ZONGPAI_NAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B00AL){
					return new TRY_LEAVE_ZONGPAI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B00AL){
					return new TRY_LEAVE_ZONGPAI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B00BL){
					return new REBEL_EVICT_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B00BL){
					return new REBEL_EVICT_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B00CL){
					return new GET_PLAYERTITLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B00CL){
					return new GET_PLAYERTITLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B00DL){
					return new MARRIAGE_TRY_BEQSTART_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B00DL){
					return new MARRIAGE_TRY_BEQSTART_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B00EL){
					return new MARRIAGE_GUESTNEW_OVER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B00EL){
					return new MARRIAGE_GUESTNEW_OVER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B00FL){
					return new MARRIAGE_HUNLI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B00FL){
					return new MARRIAGE_HUNLI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B010L){
					return new COUNTRY_COMMENDCANCEL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B010L){
					return new COUNTRY_COMMENDCANCEL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B011L){
					return new COUNTRY_NEWQIUJIN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B011L){
					return new COUNTRY_NEWQIUJIN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B012L){
					return new GET_COUNTRYJINKU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B012L){
					return new GET_COUNTRYJINKU_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B013L){
					return new CAVE_NEWBUILDING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B013L){
					return new CAVE_NEWBUILDING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B014L){
					return new CAVE_FIELD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B014L){
					return new CAVE_FIELD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B015L){
					return new CAVE_NEW_PET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B015L){
					return new CAVE_NEW_PET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B016L){
					return new CAVE_TRY_SCHEDULE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B016L){
					return new CAVE_TRY_SCHEDULE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B017L){
					return new CAVE_SEND_COUNTYLIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B017L){
					return new CAVE_SEND_COUNTYLIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B018L){
					return new PLAYER_NEW_LEVELUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B018L){
					return new PLAYER_NEW_LEVELUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B019L){
					return new CLEAN_FRIEND_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B019L){
					return new CLEAN_FRIEND_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B01AL){
					return new DO_ACTIVITY_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B01AL){
					return new DO_ACTIVITY_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B01BL){
					return new REF_TESK_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B01BL){
					return new REF_TESK_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B01CL){
					return new DELTE_PET_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B01CL){
					return new DELTE_PET_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B01DL){
					return new MARRIAGE_DOACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B01DL){
					return new MARRIAGE_DOACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B01EL){
					return new LA_FRIEND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B01EL){
					return new LA_FRIEND_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B01FL){
					return new TRY_NEWFRIEND_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B01FL){
					return new TRY_NEWFRIEND_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B020L){
					return new QINGQIU_PET_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B020L){
					return new QINGQIU_PET_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B021L){
					return new REMOVE_ACTIVITY_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B021L){
					return new REMOVE_ACTIVITY_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B022L){
					return new TRY_LEAVE_GAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B022L){
					return new TRY_LEAVE_GAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B023L){
					return new GET_TESK_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B023L){
					return new GET_TESK_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B024L){
					return new GET_GAME_PALAYERNAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B024L){
					return new GET_GAME_PALAYERNAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B025L){
					return new GET_ACTIVITY_JOINIDS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B025L){
					return new GET_ACTIVITY_JOINIDS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B026L){
					return new QUERY_GAMENAMES_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B026L){
					return new QUERY_GAMENAMES_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B027L){
					return new GET_PET_NBWINFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B027L){
					return new GET_PET_NBWINFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B028L){
					return new CLONE_FRIEND_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B028L){
					return new CLONE_FRIEND_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B029L){
					return new QUERY_OTHERPLAYER_PET_MSG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B029L){
					return new QUERY_OTHERPLAYER_PET_MSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B02AL){
					return new CSR_GET_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B02AL){
					return new CSR_GET_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B02BL){
					return new HAVE_OTHERNEW_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B02BL){
					return new HAVE_OTHERNEW_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B02CL){
					return new SHANCHU_FRIENDLIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B02CL){
					return new SHANCHU_FRIENDLIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B02DL){
					return new QUERY_TESK_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B02DL){
					return new QUERY_TESK_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B02EL){
					return new CL_HORSE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B02EL){
					return new CL_HORSE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B02FL){
					return new CL_NEWPET_MSG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B02FL){
					return new CL_NEWPET_MSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B030L){
					return new GET_ACTIVITY_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B030L){
					return new GET_ACTIVITY_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B031L){
					return new DO_SOME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B031L){
					return new DO_SOME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B032L){
					return new TY_PET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B032L){
					return new TY_PET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B033L){
					return new EQUIPMENT_GET_MSG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B033L){
					return new EQUIPMENT_GET_MSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B034L){
					return new EQU_NEW_EQUIPMENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B034L){
					return new EQU_NEW_EQUIPMENT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B035L){
					return new DELETE_FRIEND_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B035L){
					return new DELETE_FRIEND_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B036L){
					return new DO_PET_EQUIPMENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B036L){
					return new DO_PET_EQUIPMENT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B037L){
					return new QILING_NEW_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B037L){
					return new QILING_NEW_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B038L){
					return new HORSE_QILING_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B038L){
					return new HORSE_QILING_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B039L){
					return new HORSE_EQUIPMENT_QILING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B039L){
					return new HORSE_EQUIPMENT_QILING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B03AL){
					return new PET_EQU_QILING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B03AL){
					return new PET_EQU_QILING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B03BL){
					return new MARRIAGE_TRYACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B03BL){
					return new MARRIAGE_TRYACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B03CL){
					return new PET_TRY_QILING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B03CL){
					return new PET_TRY_QILING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B03DL){
					return new PLAYER_CLEAN_QILINGLIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B03DL){
					return new PLAYER_CLEAN_QILINGLIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B03EL){
					return new DELETE_TESK_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B03EL){
					return new DELETE_TESK_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B03FL){
					return new GET_HEIMINGDAI_NEWLIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B03FL){
					return new GET_HEIMINGDAI_NEWLIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B040L){
					return new QUERY_CHOURENLIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B040L){
					return new QUERY_CHOURENLIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B041L){
					return new QINCHU_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B041L){
					return new QINCHU_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B042L){
					return new REMOVE_FRIEND_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B042L){
					return new REMOVE_FRIEND_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B043L){
					return new TRY_REMOVE_CHOUREN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B043L){
					return new TRY_REMOVE_CHOUREN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B044L){
					return new REMOVE_CHOUREN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B044L){
					return new REMOVE_CHOUREN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B045L){
					return new DELETE_TASK_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B045L){
					return new DELETE_TASK_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B046L){
					return new PLAYER_TO_PLAYER_DEAL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B046L){
					return new PLAYER_TO_PLAYER_DEAL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B047L){
					return new AUCTION_NEW_LIST_MSG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B047L){
					return new AUCTION_NEW_LIST_MSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B048L){
					return new REQUEST_BUY_PLAYER_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B048L){
					return new REQUEST_BUY_PLAYER_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B049L){
					return new BOOTHER_PLAYER_MSGNAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B049L){
					return new BOOTHER_PLAYER_MSGNAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B04AL){
					return new BOOTHER_MSG_CLEAN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B04AL){
					return new BOOTHER_MSG_CLEAN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B04BL){
					return new TRY_REQUESTBUY_CLEAN_ALL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B04BL){
					return new TRY_REQUESTBUY_CLEAN_ALL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B04CL){
					return new SCHOOL_INFONAMES_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B04CL){
					return new SCHOOL_INFONAMES_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B04DL){
					return new PET_NEW_LEVELUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B04DL){
					return new PET_NEW_LEVELUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B04EL){
					return new VALIDATE_ASK_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B04EL){
					return new VALIDATE_ASK_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B04FL){
					return new JINGLIAN_NEW_TRY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B04FL){
					return new JINGLIAN_NEW_TRY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B050L){
					return new JINGLIAN_NEW_DO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B050L){
					return new JINGLIAN_NEW_DO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B051L){
					return new JINGLIAN_PET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B051L){
					return new JINGLIAN_PET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B052L){
					return new SEE_NEWFRIEND_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B052L){
					return new SEE_NEWFRIEND_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B053L){
					return new EQU_PET_HUN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B053L){
					return new EQU_PET_HUN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B054L){
					return new PET_ADD_HUNPO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B054L){
					return new PET_ADD_HUNPO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B055L){
					return new PET_ADD_SHENGMINGVALUE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B055L){
					return new PET_ADD_SHENGMINGVALUE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B056L){
					return new HORSE_REMOVE_HUNPO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B056L){
					return new HORSE_REMOVE_HUNPO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B057L){
					return new PET_REMOVE_HUNPO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B057L){
					return new PET_REMOVE_HUNPO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B058L){
					return new PLAYER_CLEAN_HORSEHUNLIANG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B058L){
					return new PLAYER_CLEAN_HORSEHUNLIANG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B059L){
					return new GET_NEW_LEVELUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B059L){
					return new GET_NEW_LEVELUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B05AL){
					return new DO_HOSEE2OTHER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B05AL){
					return new DO_HOSEE2OTHER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B05BL){
					return new TRYDELETE_PET_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B05BL){
					return new TRYDELETE_PET_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B05CL){
					return new HAHA_ACTIVITY_MSG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B05CL){
					return new HAHA_ACTIVITY_MSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B05DL){
					return new VALIDATE_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B05DL){
					return new VALIDATE_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B05EL){
					return new VALIDATE_ANDSWER_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B05EL){
					return new VALIDATE_ANDSWER_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B05FL){
					return new PLAYER_ASK_TO_OTHWE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B05FL){
					return new PLAYER_ASK_TO_OTHWE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B060L){
					return new GA_GET_SOME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B060L){
					return new GA_GET_SOME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B061L){
					return new OTHER_PET_LEVELUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B061L){
					return new OTHER_PET_LEVELUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B062L){
					return new MY_OTHER_FRIEDN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B062L){
					return new MY_OTHER_FRIEDN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00A3B063L){
					return new DOSOME_SB_MSG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x80A3B063L){
					return new DOSOME_SB_MSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A016L){
					return new MIESHI_GET_VERSION_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A016L){
					return new MIESHI_GET_VERSION_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A015L){
					return new MIESHI_GET_RESOURCE_PACKAGE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A015L){
					return new MIESHI_GET_RESOURCE_PACKAGE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A017L){
					return new MIESHI_GET_RESOURCE_FILE_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A017L){
					return new MIESHI_GET_RESOURCE_FILE_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x07777777L){
					return new MIESHI_GET_RESOURCE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x87777777L){
					return new MIESHI_GET_RESOURCE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x66666666L){
					return new MIESHI_RESOURCE_FILE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x66666669L){
					return new MIESHI_RESOURCE_PROGRESS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x76666669L){
					return new MIESHI_RESOURCE_PROGRESS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x66666667L){
					return new OPEN_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x66666668L){
					return new CLIENT_ERROR_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A021L){
					return new QUERY_ACCOUNT_VALID_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A021L){
					return new QUERY_ACCOUNT_VALID_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A023L){
					return new QUERY_SERVER_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8002A023L){
					return new QUERY_SERVER_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EE002L){
					return new PASSPORT_GETBACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x800EE002L){
					return new PASSPORT_GETBACK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EE006L){
					return new PHONE_UIID_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EE003L){
					return new SERVERSLIST_AND_PASSPORTQUESTION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x800EE003L){
					return new SERVERSLIST_AND_PASSPORTQUESTION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE100L){
					return new SEND_CLIENT_EXTENDINFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE100L){
					return new SEND_CLIENT_EXTENDINFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EE001L){
					return new PLATFORM_ARGS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x800EE001L){
					return new PLATFORM_ARGS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A018L){
					return new NEW_MIESHI_GET_VERSION_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE106L){
					return new NEW_QUERY_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE107L){
					return new NEW_OPTION_SELECT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EE008L){
					return new MIESHI_MAP_LANG_TRANSLATE(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A027L){
					return new MIESHI_DELETE_ONE_RESOURCE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A024L){
					return new MIESHI_SKILL_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A025L){
					return new MIESHI_UPDATE_PLAYER_INFO(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A030L){
					return new NEW_MIESHI_UPDATE_PLAYER_INFO(sn,messageContent,offset,end - offset);
			}else if(type == 0x0002A031L){
					return new UPDATE_PLAYER_INFO_FOR_HEFU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EE004L){
					return new ATTENTIONS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x800EE004L){
					return new ATTENTIONS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE005L){
					return new DENY_USER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE005L){
					return new DENY_USER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE077L){
					return new REPORT_ONLINENUM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE077L){
					return new REPORT_ONLINENUM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE078L){
					return new REPORT_LONG_HEARTBEAT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE178L){
					return new REPORT_LONG_PROTOCAL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE079L){
					return new REPORT_CHAT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE099L){
					return new QUERY_CLIENT_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE099L){
					return new QUERY_CLIENT_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE101L){
					return new QUERY_LOGIN_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE101L){
					return new QUERY_LOGIN_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE102L){
					return new SESSION_VALIDATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE102L){
					return new SESSION_VALIDATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE103L){
					return new NOTIFY_USER_ENTERSERVER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE104L){
					return new NOTIFY_SERVER_TIREN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE105L){
					return new NOTIFY_USER_LEAVESERVER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE109L){
					return new GM_ACTION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE110L){
					return new GET_WAIGUA_PROCESS_NAMES_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE110L){
					return new GET_WAIGUA_PROCESS_NAMES_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE111L){
					return new GET_NOTICE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE111L){
					return new GET_NOTICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE112L){
					return new THIRDPART_PROMOTE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE112L){
					return new THIRDPART_PROMOTE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE113L){
					return new NEW_QUERY_SERVER_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE113L){
					return new NEW_QUERY_SERVER_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE114L){
					return new ANNOUNCEMENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE114L){
					return new ANNOUNCEMENT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE115L){
					return new QUERY_WHITE_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE115L){
					return new QUERY_WHITE_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE116L){
					return new VALIDATE_DEVICE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE116L){
					return new VALIDATE_DEVICE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE117L){
					return new MODIFY_VIP_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE117L){
					return new MODIFY_VIP_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE118L){
					return new GET_PHONE_CODE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE118L){
					return new GET_PHONE_CODE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x002EE119L){
					return new QUICK_REGISTER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x802EE119L){
					return new QUICK_REGISTER_RES(sn,messageContent,offset,end - offset);
			}else{
				throw new MessageFormatErrorException("unknown message type ["+type+"]");
			}
	}
	/**
	* 将对象转化为数组，可能抛出异常
	*/
	public byte[] objectToByteArray(Object obj) {
		if(obj == null) return new byte[0];
		try {
			java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(out);
			oos.writeObject(obj);
			oos.close();
			return out.toByteArray();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}
	/**
	* 将数组转化为对象，可能抛出异常
	*/
	public Object byteArrayToObject(byte[] bytes, int offset,int numOfBytes) throws Exception{
		if(numOfBytes == 0) return null;
		java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(bytes,offset,numOfBytes);
		java.io.ObjectInputStream o = new java.io.ObjectInputStream(input);
		Object obj = o.readObject();
		o.close();
		return obj;
	}
}
