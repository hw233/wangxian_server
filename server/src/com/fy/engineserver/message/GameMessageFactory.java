package com.fy.engineserver.message;

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
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TIME_SYNC_REQ.html'>TIME_SYNC_REQ</a></td><td>0x00FFFF01</td><td><a href='./TIME_SYNC_RES.html'>TIME_SYNC_RES</a></td><td>0x70FFFF01</td><td>时间同步包，有服务器发送给客户端，用于测试客户端的网络延迟，客户端收到此数据包后，应立即返回响应包</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TIME_SETTING_REQ.html'>TIME_SETTING_REQ</a></td><td>0x00FFFF02</td><td><a href='./-.html'>-</a></td><td>-</td><td>时间同步包，服务器发送给客户端，</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_PLAYER_BY_ID_REQ.html'>GET_PLAYER_BY_ID_REQ</a></td><td>0x0000F100</td><td><a href='./GET_PLAYER_BY_ID_RES.html'>GET_PLAYER_BY_ID_RES</a></td><td>0x7000F100</td><td>用户查找玩家，用于跳转到玩家所在地</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./USER_ENTER_SERVER_REQ.html'>USER_ENTER_SERVER_REQ</a></td><td>0x0010F012</td><td><a href='./USER_ENTER_SERVER_RES.html'>USER_ENTER_SERVER_RES</a></td><td>0x7000F012</td><td>用户进入服务器</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./USER_ENTER_SERVER2_REQ.html'>USER_ENTER_SERVER2_REQ</a></td><td>0x0010F016</td><td><a href='./-.html'>-</a></td><td>-</td><td>用户进入服务器 ,返回USER_ENTER_SERVER_RES</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEW_USER_ENTER_SERVER_REQ.html'>NEW_USER_ENTER_SERVER_REQ</a></td><td>0x001AF016</td><td><a href='./NEW_USER_ENTER_SERVER_RES.html'>NEW_USER_ENTER_SERVER_RES</a></td><td>0x70EAF016</td><td>用户进入服务器 ,返回USER_ENTER_SERVER_RES</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./LINEUP_STATUS_REQ.html'>LINEUP_STATUS_REQ</a></td><td>0x0001F013</td><td><a href='./LINEUP_STATUS_RES.html'>LINEUP_STATUS_RES</a></td><td>0x700FF013</td><td>用户查询排队情况</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./LEAVE_LINEUP_REQ.html'>LEAVE_LINEUP_REQ</a></td><td>0x000FF016</td><td><a href='./LEAVE_LINEUP_RES.html'>LEAVE_LINEUP_RES</a></td><td>0x700FF016</td><td>用户取消排队</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_USER_PLAYER_REQ.html'>GET_USER_PLAYER_REQ</a></td><td>0x0000F004</td><td><a href='./GET_USER_PLAYER_RES.html'>GET_USER_PLAYER_RES</a></td><td>0x7000F004</td><td>获得用户在服务器上的角色数量</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./USER_BIND_MOBILE_REQ.html'>USER_BIND_MOBILE_REQ</a></td><td>0x0000F005</td><td><a href='./USER_BIND_MOBILE_RES.html'>USER_BIND_MOBILE_RES</a></td><td>0x7000F005</td><td>用户绑定手机</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_PLAYER_YUANBAO_REQ.html'>GET_PLAYER_YUANBAO_REQ</a></td><td>0x0000F007</td><td><a href='./GET_PLAYER_YUANBAO_RES.html'>GET_PLAYER_YUANBAO_RES</a></td><td>0x7000F007</td><td>获得玩家的元宝数量，由两部分组成，一部分是角色身上的，一部分在账户上，其中账户上的元宝是多个角色间共享的</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_YUANBAO_CHANGED_REQ.html'>PLAYER_YUANBAO_CHANGED_REQ</a></td><td>0x0000F008</td><td><a href='./PLAYER_YUANBAO_CHANGED_RES.html'>PLAYER_YUANBAO_CHANGED_RES</a></td><td>0x7000F008</td><td>通知玩家的元宝数量改变，由两部分组成，一部分是角色身上的，一部分在账户上，其中账户上的元宝是多个角色间共享的</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CREATE_PLAYER_REQ.html'>CREATE_PLAYER_REQ</a></td><td>0x00000212</td><td><a href='./CREATE_PLAYER_RES.html'>CREATE_PLAYER_RES</a></td><td>0x70000212</td><td>客户端向服务器发送创建新的角色的请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_PLAYER_REQ.html'>QUERY_PLAYER_REQ</a></td><td>0x00000013</td><td><a href='./QUERY_PLAYER_RES.html'>QUERY_PLAYER_RES</a></td><td>0x70000013</td><td>客户端向服务器发送查询角色的请求</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_SIMPLE_PLAYER_INFO_REQ.html'>QUERY_SIMPLE_PLAYER_INFO_REQ</a></td><td>0x00001000</td><td><a href='./QUERY_SIMPLE_PLAYER_INFO_RES.html'>QUERY_SIMPLE_PLAYER_INFO_RES</a></td><td>0x70001000</td><td>客户端向服务器发送查询角色简单信息的请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REMOVE_PLAYER_REQ.html'>REMOVE_PLAYER_REQ</a></td><td>0x00000014</td><td><a href='./REMOVE_PLAYER_RES.html'>REMOVE_PLAYER_RES</a></td><td>0x70000014</td><td>客户端向服务器发送删除角色的请求</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PLAYER_ENTER_REQ.html'>PLAYER_ENTER_REQ</a></td><td>0x00000016</td><td><a href='./PLAYER_ENTER_RES.html'>PLAYER_ENTER_RES</a></td><td>0x70000016</td><td>角色进入游戏请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FUNCTION_NPC_REQ.html'>FUNCTION_NPC_REQ</a></td><td>0x00000019</td><td><a href='./FUNCTION_NPC_RES.html'>FUNCTION_NPC_RES</a></td><td>0x70000019</td><td>客户端向服务器发送请求包</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NONTARGET_SKILL_REQ.html'>NONTARGET_SKILL_REQ</a></td><td>0x00000020</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端通知服务器，无目标地施放技能</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TARGET_SKILL_REQ.html'>TARGET_SKILL_REQ</a></td><td>0x00000021</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端通知服务器，有目标地施放技能</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NONTARGET_SKILL_BROADCAST_REQ.html'>NONTARGET_SKILL_BROADCAST_REQ</a></td><td>0x00000022</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器向客户端广播技能使用事件，限定为使用者广播区内的所有玩家，不包括使用者本人</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TARGET_SKILL_BROADCAST_REQ.html'>TARGET_SKILL_BROADCAST_REQ</a></td><td>0x00000023</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器向客户端广播技能使用事件，限定为使用者广播区内的所有玩家，不包括使用者本人</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./LASTING_SKILL_BROADCAST_REQ.html'>LASTING_SKILL_BROADCAST_REQ</a></td><td>0x0F000123</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器向客户端广播持续性技能使用事件，限定为使用者广播区内的所有玩家，不包括使用者本人</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_UPDATE_REQ.html'>PLAYER_UPDATE_REQ</a></td><td>0x00000024</td><td><a href='./PLAYER_UPDATE_RES.html'>PLAYER_UPDATE_RES</a></td><td>0x70000024</td><td>玩家修改信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./USE_AURASKILL_REQ.html'>USE_AURASKILL_REQ</a></td><td>0x00000026</td><td><a href='./-.html'>-</a></td><td>-</td><td>玩家使用光环技能的请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_SKILL_REQ.html'>EQUIPMENT_SKILL_REQ</a></td><td>0xA0000026</td><td><a href='./-.html'>-</a></td><td>-</td><td>发送装备技能数据给客户端</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ENTER_GAME_REQ.html'>ENTER_GAME_REQ</a></td><td>0x00000090</td><td><a href='./ENTER_GAME_RES.html'>ENTER_GAME_RES</a></td><td>0x70000090</td><td>客户端发送请求给服务器，要求进入某个地图</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./LEAVE_CURRENT_GAME_REQ.html'>LEAVE_CURRENT_GAME_REQ</a></td><td>0x00000091</td><td><a href='./-.html'>-</a></td><td>-</td><td>要求离开当前地图，服务器和客户端均可发送</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHANGE_GAME_REQ.html'>CHANGE_GAME_REQ</a></td><td>0x00000092</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器发送，要求客户端离开当前地图，然后请求进入指定的下一张地图</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_RECONN_REQ.html'>PLAYER_RECONN_REQ</a></td><td>0x000000A0</td><td><a href='./PLAYER_RECONN_RES.html'>PLAYER_RECONN_RES</a></td><td>0x700000A0</td><td>客户端通知服务器，刚才断网，重新连接上来，要求继续进行游戏</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PLAYER_MOVE_REQ.html'>PLAYER_MOVE_REQ</a></td><td>0x000000C0</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端通知服务器，精灵沿着导航点移动到目的地，以及到达时间</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_MOVETRACE_REQ.html'>PLAYER_MOVETRACE_REQ</a></td><td>0x000000C1</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端通知服务器，精灵沿着按多个目标点表达的路径移动到目的地，包括到达的时间</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SPECIAL_SKILL_MOVETRACE_REQ.html'>SPECIAL_SKILL_MOVETRACE_REQ</a></td><td>0x000000CA</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，精灵沿着按路径移动到目的地，包括到达的时间</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SET_POSITION_REQ.html'>SET_POSITION_REQ</a></td><td>0x000000C2</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，精灵立刻移动到目的地；或者玩家中止移动时发送此指令给服务器</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TOUCH_TRANSPORT_REQ.html'>TOUCH_TRANSPORT_REQ</a></td><td>0x000000C3</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端通知服务端到达传送点</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_CLIENT_UPDATE_MAPRES_REQ.html'>NOTICE_CLIENT_UPDATE_MAPRES_REQ</a></td><td>0x000000C4</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端更新资源</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SPRITES_IN_GAME.html'>SPRITES_IN_GAME</a></td><td>0x000111D0</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器主动向客户端发送的数据包，告诉客户端本地图上有哪些怪,此数组客户端应该保留在内存中，在AROUND_CHANGE_REQ会通知此怪的下标来节省流量</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./AROUND_CHANGE_REQ.html'>AROUND_CHANGE_REQ</a></td><td>0x001100D0</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器主动向客户端发送的数据包，告诉客户端其周围发生的变化，包括谁来了，谁走了，谁的什么属性发生了什么变化，以及谁在移动</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHANGE_PKMODE_REQ.html'>CHANGE_PKMODE_REQ</a></td><td>0x00F0EF08</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端通知服务器，更改pk状态</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_EVENT_REQ.html'>NOTIFY_EVENT_REQ</a></td><td>0x000000D1</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器向客户端发送，通知客户端发生了某些事情，比如获得经验值，钱，升级了，任务完成了等等，客户端根据不同的事情，作不同的现实</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTIFY_EVENTS_REQ.html'>NOTIFY_EVENTS_REQ</a></td><td>0x000000DF</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器向客户端发送，通知客户端发生了某些事情，比如获得经验值，钱，升级了，任务完成了等等，客户端根据不同的事情，作不同的现实</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_BUFF_REQ.html'>NOTIFY_BUFF_REQ</a></td><td>0x000000D2</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器向客户端发送，通知客户端种植了一个新的BUFF</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTIFY_SELFCHANGE_REQ.html'>NOTIFY_SELFCHANGE_REQ</a></td><td>0x000000D3</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器向客户端发送，通知客户端玩家自身变量的改变</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_BUFF_REMOVED_REQ.html'>NOTIFY_BUFF_REMOVED_REQ</a></td><td>0x000000D4</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器向客户端发送，通知客户端移除了一个BUFF</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTIFY_FLOPAVAILABLE_REQ.html'>NOTIFY_FLOPAVAILABLE_REQ</a></td><td>0x000000D5</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，那些怪身上有东西可以捡，或者采集NPC有东西可以采集</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_FLOPAVAILABLE_REQ.html'>QUERY_FLOPAVAILABLE_REQ</a></td><td>0x000000D6</td><td><a href='./QUERY_FLOPAVAILABLE_RES.html'>QUERY_FLOPAVAILABLE_RES</a></td><td>0x700000D6</td><td>客户端查询某个怪，有什么东西可以捡的</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_FLOP_SIMPLE_REQ.html'>QUERY_FLOP_SIMPLE_REQ</a></td><td>0x000000D7</td><td><a href='./QUERY_FLOP_SIMPLE_RES.html'>QUERY_FLOP_SIMPLE_RES</a></td><td>0x700000D7</td><td>客户端查询某个怪，有什么东西可以捡的。简版通知，只下发物品编号，不下发物品实体</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PICKUP_FLOP_REQ.html'>PICKUP_FLOP_REQ</a></td><td>0x000000D8</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端拾取某个怪物身上的东西</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PICKUP_ALLFLOP_REQ.html'>PICKUP_ALLFLOP_REQ</a></td><td>0x000000DA</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端拾取屏幕内所有可拾取的怪</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PREPARE_PLAY_DICE_REQ.html'>PREPARE_PLAY_DICE_REQ</a></td><td>0x000000D9</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端要求掷骰子来获取物品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CPATAIN_ASSIGN_REQ.html'>CPATAIN_ASSIGN_REQ</a></td><td>0x000000DC</td><td><a href='./-.html'>-</a></td><td>-</td><td>队长分配物品请求，客户端发给服务器</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PICKUP_MONEY_REQ.html'>PICKUP_MONEY_REQ</a></td><td>0x000000DD</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端拾取某个怪物身上的钱</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PICKUP_CAIJINPC_REQ.html'>PICKUP_CAIJINPC_REQ</a></td><td>0x000000DE</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端拾取某个采集NPC携带的东西</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./OTHER_USER_REQ.html'>OTHER_USER_REQ</a></td><td>0x000000E0</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，其他人用此帐户尝试使用此角色进入游戏，要求客户端断开连接</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_SKILLINFO_REQ.html'>QUERY_SKILLINFO_REQ</a></td><td>0x000000E1</td><td><a href='./QUERY_SKILLINFO_RES.html'>QUERY_SKILLINFO_RES</a></td><td>0x700000E1</td><td>请求某个技能的详细信息，此信息包括当前级别和下一个级别的信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_SKILLINFO_PET_REQ.html'>QUERY_SKILLINFO_PET_REQ</a></td><td>0x000000F1</td><td><a href='./QUERY_SKILLINFO_PET_RES.html'>QUERY_SKILLINFO_PET_RES</a></td><td>0x700000F1</td><td>请求宠物某个技能的详细信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PARTICLE_REQ.html'>PARTICLE_REQ</a></td><td>0x000000EE</td><td><a href='./-.html'>-</a></td><td>-</td><td>npc等的粒子效果，如暴风雪npc的暴风雪效果</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_ACTIVESKILL_REQ.html'>QUERY_ACTIVESKILL_REQ</a></td><td>0x000000E2</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端请求服务器关于主动技能的数据，服务器返回给客户端所有的主动技能数据</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SEND_ACTIVESKILL_REQ.html'>SEND_ACTIVESKILL_REQ</a></td><td>0x000000E3</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端请求服务器关于主动技能的数据，服务器返回给客户端所有的主动技能数据</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ALLOCATE_SKILL_REQ.html'>ALLOCATE_SKILL_REQ</a></td><td>0x000000E5</td><td><a href='./-.html'>-</a></td><td>-</td><td>用户请求分配技能点</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CLEAN_SKILL_REQ.html'>CLEAN_SKILL_REQ</a></td><td>0x000000EA</td><td><a href='./-.html'>-</a></td><td>-</td><td>用户请求洗魂</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NEW_XINGFA_SKILL_MSG_RES.html'>NEW_XINGFA_SKILL_MSG_RES</a></td><td>0x700100E8</td><td>新心法技能限制等级和描述</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_CAREER_INFO_REQ.html'>QUERY_CAREER_INFO_REQ</a></td><td>0x000000E6</td><td><a href='./QUERY_CAREER_INFO_RES.html'>QUERY_CAREER_INFO_RES</a></td><td>0x700000E6</td><td>查询门派信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEW_QUERY_CAREER_INFO_REQ.html'>NEW_QUERY_CAREER_INFO_REQ</a></td><td>0x000000EC</td><td><a href='./NEW_QUERY_CAREER_INFO_RES.html'>NEW_QUERY_CAREER_INFO_RES</a></td><td>0x700000EC</td><td>查询门派信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEW_QUERY_CAREER_INFO_BY_ID_REQ.html'>NEW_QUERY_CAREER_INFO_BY_ID_REQ</a></td><td>0x000001E7</td><td><a href='./NEW_QUERY_CAREER_INFO_BY_ID_RES.html'>NEW_QUERY_CAREER_INFO_BY_ID_RES</a></td><td>0x700001E7</td><td>查询门派信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./QUERY_CAREER_XINFA_INFO_RES.html'>QUERY_CAREER_XINFA_INFO_RES</a></td><td>0x700000EA</td><td>因为上面那条协议太长发不过去新加的只发心法协议</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./QUERY_CAREER_JINJIE_INFO_RES.html'>QUERY_CAREER_JINJIE_INFO_RES</a></td><td>0x700000ED</td><td>因为上面那条协议太长发不过去新加的只发进阶协议</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_DUJIE_SKILL_INFO_REQ.html'>QUERY_DUJIE_SKILL_INFO_REQ</a></td><td>0x000000EB</td><td><a href='./QUERY_DUJIE_SKILL_INFO_RES.html'>QUERY_DUJIE_SKILL_INFO_RES</a></td><td>0x700000EB</td><td>取渡劫关于技能的信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_CAREER_INFO_BY_ID_REQ.html'>QUERY_CAREER_INFO_BY_ID_REQ</a></td><td>0x000001E6</td><td><a href='./QUERY_CAREER_INFO_BY_ID_RES.html'>QUERY_CAREER_INFO_BY_ID_RES</a></td><td>0x700001E6</td><td>查询门派信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_XINFA_SKILL_IDLIST_REQ.html'>QUERY_XINFA_SKILL_IDLIST_REQ</a></td><td>0x000001E8</td><td><a href='./QUERY_XINFA_SKILL_IDLIST_RES.html'>QUERY_XINFA_SKILL_IDLIST_RES</a></td><td>0x700001E8</td><td>查询心法技能ID列表，主要是心法技能数据分多条协议传过去有时候会乱</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_ARTICLE_REQ.html'>QUERY_ARTICLE_REQ</a></td><td>0x000000F3</td><td><a href='./QUERY_ARTICLE_RES.html'>QUERY_ARTICLE_RES</a></td><td>0x700000F3</td><td>客户端发送物品编号，请求物品对象，可以一次请求多个物品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_DURABILITY_CHANGE_REQ.html'>NOTIFY_DURABILITY_CHANGE_REQ</a></td><td>0x000000FB</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，磨损的变化</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTIFY_EQUIPMENT_CHANGE_REQ.html'>NOTIFY_EQUIPMENT_CHANGE_REQ</a></td><td>0x000000FC</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，装备道具本身属性发生了变化</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_ARTICLE_INFO_REQ.html'>QUERY_ARTICLE_INFO_REQ</a></td><td>0x000000FD</td><td><a href='./QUERY_ARTICLE_INFO_RES.html'>QUERY_ARTICLE_INFO_RES</a></td><td>0x700000FD</td><td>请求物品详细描述</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_SUIT_INFO_REQ.html'>QUERY_SUIT_INFO_REQ</a></td><td>0x000001FD</td><td><a href='./QUERY_SUIT_INFO_RES.html'>QUERY_SUIT_INFO_RES</a></td><td>0x700001FD</td><td>请求星級套，顏色套详细描述</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_RESET_REQ.html'>EQUIPMENT_RESET_REQ</a></td><td>0x000000FE</td><td><a href='./EQUIPMENT_RESET_RES.html'>EQUIPMENT_RESET_RES</a></td><td>0x700000FE</td><td>请求装备重置</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TEAM_QUERY_REQ.html'>TEAM_QUERY_REQ</a></td><td>0x00000F00</td><td><a href='./TEAM_QUERY_RES.html'>TEAM_QUERY_RES</a></td><td>0x70000F00</td><td>客户端向服务器发送请求，查询玩家当前所在的队伍</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TEAM_INVITE_REQ.html'>TEAM_INVITE_REQ</a></td><td>0x00000F01</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端向服务器发送请求，某个玩家邀请另外一个玩家加入团队</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TEAM_INVITE_NOTIFY_REQ.html'>TEAM_INVITE_NOTIFY_REQ</a></td><td>0x00000F02</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端，某人邀请他加入团队</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TEAM_INVITE_RESULT_REQ.html'>TEAM_INVITE_RESULT_REQ</a></td><td>0x00000F03</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知服务器，同意或者拒绝某人的邀请</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TEAM_DISSOLVE_REQ.html'>TEAM_DISSOLVE_REQ</a></td><td>0x00000F04</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端向服务器发送请求，队长要求解散所在的团队。服务器也使用相同的协议通知客户端团队解散</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TEAM_APPLY_JION_REQ.html'>TEAM_APPLY_JION_REQ</a></td><td>0x00000F05</td><td><a href='./-.html'>-</a></td><td>-</td><td>非组队玩家向队长提交入队申请</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TEAM_APPLY_NOTIFY_REQ.html'>TEAM_APPLY_NOTIFY_REQ</a></td><td>0x00000F06</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器提示客户端，有人申请加入团队</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TEAM_APPLY_RESULT_REQ.html'>TEAM_APPLY_RESULT_REQ</a></td><td>0x00000F07</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端发送给服务器，同意或者拒绝申请</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TEAM_APPLY_LEAVE_REQ.html'>TEAM_APPLY_LEAVE_REQ</a></td><td>0x00000F08</td><td><a href='./-.html'>-</a></td><td>-</td><td>由客户端发起，通知某人离开团队。如果发起者不是队长，表示队员主动请求离队，否则表示队长踢人</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TEAM_SET_CAPTAIN_REQ.html'>TEAM_SET_CAPTAIN_REQ</a></td><td>0x00000F09</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端发起要求设置新的队长，只有队长能发起此请求</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TEAM_CHANGE_CAPTAIN_REQ.html'>TEAM_CHANGE_CAPTAIN_REQ</a></td><td>0x00000F0A</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，您所在的团队更换队长了</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TEAM_SET_ASSIGN_RULE_REQ.html'>TEAM_SET_ASSIGN_RULE_REQ</a></td><td>0x00000F0B</td><td><a href='./-.html'>-</a></td><td>-</td><td>队长修改团队物品分配规则。客户端、服务器均可发起</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TEAM_MEMBER_JOIN_REQ.html'>TEAM_MEMBER_JOIN_REQ</a></td><td>0x00000F0C</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，某个玩家加入到当前玩家所在的团队。新加入的玩家也会收到此指令</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TEAM_MEMBER_LEAVE_REQ.html'>TEAM_MEMBER_LEAVE_REQ</a></td><td>0x00000F0D</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，某个玩家离开到当前玩家所在的团队。新离开的玩家也会收到此指令</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TEAM_QUERY_PLAYER_REQ.html'>TEAM_QUERY_PLAYER_REQ</a></td><td>0x00000F0E</td><td><a href='./TEAM_QUERY_PLAYER_RES.html'>TEAM_QUERY_PLAYER_RES</a></td><td>0x70000F0E</td><td>客户端请求玩家简略信息，仅供显示。这些队友不在主玩家的广播区内</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_OTHER_PLAYER_REQ.html'>QUERY_OTHER_PLAYER_REQ</a></td><td>0x000000E7</td><td><a href='./QUERY_OTHER_PLAYER_RES.html'>QUERY_OTHER_PLAYER_RES</a></td><td>0x700000E7</td><td>查询其他玩家的信息，主要供界面显示</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_PERSON_TEAM_REQ.html'>QUERY_PERSON_TEAM_REQ</a></td><td>0x000000E8</td><td><a href='./QUERY_PERSON_TEAM_RES.html'>QUERY_PERSON_TEAM_RES</a></td><td>0x700000E8</td><td>查询自己允许进队状态，队伍分配方式，附近的队伍,如果有队伍显示队伍的分配规则</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SET_INTEAM_RULE_REQ.html'>SET_INTEAM_RULE_REQ</a></td><td>0x000000E9</td><td><a href='./-.html'>-</a></td><td>-</td><td>玩家设置进队状态 0 自动进队 1弹提示</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SAVE_SHORTCUT_REQ.html'>SAVE_SHORTCUT_REQ</a></td><td>0x00000F30</td><td><a href='./-.html'>-</a></td><td>-</td><td>发送快捷键信息至服务器保存，不需要回复</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_SHORTCUT_REQ.html'>QUERY_SHORTCUT_REQ</a></td><td>0x00000F31</td><td><a href='./QUERY_SHORTCUT_RES.html'>QUERY_SHORTCUT_RES</a></td><td>0x70000F31</td><td>客户端向服务器请求快捷键信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHAT_MESSAGE_REQ.html'>CHAT_MESSAGE_REQ</a></td><td>0x00000E02</td><td><a href='./CHAT_MESSAGE_RES.html'>CHAT_MESSAGE_RES</a></td><td>0x70000E02</td><td>客户端发送聊天消息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FEE_CHAT_ALLOW_REQ.html'>FEE_CHAT_ALLOW_REQ</a></td><td>0x00000E03</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知玩家此频道是否能发言，是否有道具</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FEE_CHAT_CONFIRM_REQ.html'>FEE_CHAT_CONFIRM_REQ</a></td><td>0x00000E04</td><td><a href='./-.html'>-</a></td><td>-</td><td>玩家是否确定要喊话，如果玩家有道具，直接扣量，如果没有道具，直接在商店购买一个</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CHAT_FAILED_REQ.html'>CHAT_FAILED_REQ</a></td><td>0x00000E06</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知玩家发言失败</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHAT_CHANNEL_STATUS_REQ.html'>CHAT_CHANNEL_STATUS_REQ</a></td><td>0x00000E07</td><td><a href='./CHAT_CHANNEL_STATUS_RES.html'>CHAT_CHANNEL_STATUS_RES</a></td><td>0x70000E07</td><td>获取聊天频道状态</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CHAT_CHANNEL_SET_REQ.html'>CHAT_CHANNEL_SET_REQ</a></td><td>0x00000E08</td><td><a href='./CHAT_CHANNEL_SET_RES.html'>CHAT_CHANNEL_SET_RES</a></td><td>0x70000E08</td><td>设置聊天频道状态</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHAT_VOICE_REQ.html'>CHAT_VOICE_REQ</a></td><td>0x00000E09</td><td><a href='./CHAT_VOICE_RES.html'>CHAT_VOICE_RES</a></td><td>0x70000E09</td><td>发送语音聊天,这里面的文件大小是zip压缩后的</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CHAT_VOICE_INFO_REQ.html'>CHAT_VOICE_INFO_REQ</a></td><td>0x00000E10</td><td><a href='./CHAT_VOICE_INFO_RES.html'>CHAT_VOICE_INFO_RES</a></td><td>0x70000E10</td><td>语音文件具体数据ZIP格式的,最大8KB</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHAT_VOICE_SUCC_REQ.html'>CHAT_VOICE_SUCC_REQ</a></td><td>0x00000E0A</td><td><a href='./-.html'>-</a></td><td>-</td><td>语音是否发送成功</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TASK_QUERY_TASK_REQ.html'>TASK_QUERY_TASK_REQ</a></td><td>0x00000FA0</td><td><a href='./TASK_QUERY_TASK_RES.html'>TASK_QUERY_TASK_RES</a></td><td>0x70000FA0</td><td>客户端查询某个任务详细数据</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TASK_QUERY_ENTITIES_REQ.html'>TASK_QUERY_ENTITIES_REQ</a></td><td>0x00000FA1</td><td><a href='./TASK_QUERY_ENTITIES_RES.html'>TASK_QUERY_ENTITIES_RES</a></td><td>0x70000FA1</td><td>服务器将玩家正在做的任务发送给玩家</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TASK_SEND_ENTITY_REQ.html'>TASK_SEND_ENTITY_REQ</a></td><td>0x00000FA2</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，某个任务实体发生了变化</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TASK_SEND_ACTION_REQ.html'>TASK_SEND_ACTION_REQ</a></td><td>0x00000FA3</td><td><a href='./TASK_SEND_ACTION_RES.html'>TASK_SEND_ACTION_RES</a></td><td>0x70000FA3</td><td>客户端将操作发送给服务器</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./OPEN_ACCEPT_TASK_WINDOW_REQ.html'>OPEN_ACCEPT_TASK_WINDOW_REQ</a></td><td>0x00000FA4</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器要求客户端自动打开接任务窗口</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TAKE_TASK_BY_ARTICLE_REQ.html'>TAKE_TASK_BY_ARTICLE_REQ</a></td><td>0x000000A8</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端发送通过道具接任务的申请</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FUNCTION_NPC_MODIFY_REQ.html'>FUNCTION_NPC_MODIFY_REQ</a></td><td>0x00000FA5</td><td><a href='./-.html'>-</a></td><td>-</td><td>当前地图NPC可接任务变化</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAN_ACCEPT_TASK_MODIFY_REQ.html'>CAN_ACCEPT_TASK_MODIFY_REQ</a></td><td>0x00000FA6</td><td><a href='./-.html'>-</a></td><td>-</td><td>可接任务列表变化</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_CAN_ACCEPT_TASK_REQ.html'>QUERY_CAN_ACCEPT_TASK_REQ</a></td><td>0x00000FA7</td><td><a href='./QUERY_CAN_ACCEPT_TASK_RES.html'>QUERY_CAN_ACCEPT_TASK_RES</a></td><td>0x70000FA7</td><td>查询可接任务列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_AUTO_HOOK_USE_PROP_REQ.html'>QUERY_AUTO_HOOK_USE_PROP_REQ</a></td><td>0x00000FA8</td><td><a href='./QUERY_AUTO_HOOK_USE_PROP_RES.html'>QUERY_AUTO_HOOK_USE_PROP_RES</a></td><td>0x70000FA8</td><td>查询自动挂机可使用道具列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./COLLECTION_NPC_MODIFY_REQ.html'>COLLECTION_NPC_MODIFY_REQ</a></td><td>0x00000FA9</td><td><a href='./COLLECTION_NPC_MODIFY_RES.html'>COLLECTION_NPC_MODIFY_RES</a></td><td>0x70000FA9</td><td>通知客户端可采集列表的变化</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./WANNA_COLLECTION_REQ.html'>WANNA_COLLECTION_REQ</a></td><td>0x00000FAA</td><td><a href='./WANNA_COLLECTION_RES.html'>WANNA_COLLECTION_RES</a></td><td>0x70000FAA</td><td>想要采集物品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_CLIENT_READ_TIMEBAR_REQ.html'>NOTICE_CLIENT_READ_TIMEBAR_REQ</a></td><td>0x00000FAB</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端读一个进度条</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTICE_CLIENT_READ_TIMEBAR_INTERRUPT_REQ.html'>NOTICE_CLIENT_READ_TIMEBAR_INTERRUPT_REQ</a></td><td>0x00000FAE</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端打断进度条</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_CLIENT_COUNTDOWN_REQ.html'>NOTICE_CLIENT_COUNTDOWN_REQ</a></td><td>0x00000FFB</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端显示一个倒计时</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTICE_CLIENT_FOLLOWNPC_FARAWAY_REQ.html'>NOTICE_CLIENT_FOLLOWNPC_FARAWAY_REQ</a></td><td>0x00000FAC</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端护送NPC太远了</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_CLIENT_FOLLOWNPC_NEAR_REQ.html'>NOTICE_CLIENT_FOLLOWNPC_NEAR_REQ</a></td><td>0x00000FAD</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端靠近了护送NPC</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PASSPORT_RANDOM_REQ.html'>PASSPORT_RANDOM_REQ</a></td><td>0x0000E000</td><td><a href='./PASSPORT_RANDOM_RES.html'>PASSPORT_RANDOM_RES</a></td><td>0x7000E000</td><td>快速注册时产生一个随机通行证,客户端不要使用这个</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PASSPORT_UPDATE_REQ.html'>PASSPORT_UPDATE_REQ</a></td><td>0x0000E003</td><td><a href='./PASSPORT_UPDATE_RES.html'>PASSPORT_UPDATE_RES</a></td><td>0x7000E003</td><td>更新一个通行证</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CREATE_DEAL_REQ.html'>CREATE_DEAL_REQ</a></td><td>0x0000A001</td><td><a href='./-.html'>-</a></td><td>-</td><td>创建一个deal, 服务器和客户端共享此协议，客户端用户发起交易时向服务器请求，服务器创建一个交易，并且向另一方玩家发起这个请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DEAL_CREATED_REQ.html'>DEAL_CREATED_REQ</a></td><td>0x0000A002</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知玩家双方，交易被创建</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DEAL_ADD_ARTICLE_REQ.html'>DEAL_ADD_ARTICLE_REQ</a></td><td>0x0000A003</td><td><a href='./DEAL_ADD_ARTICLE_RES.html'>DEAL_ADD_ARTICLE_RES</a></td><td>0x7000A003</td><td>更改条件，增加物品到交易栏</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DEAL_DELETE_ARTICLE_REQ.html'>DEAL_DELETE_ARTICLE_REQ</a></td><td>0x0000A004</td><td><a href='./-.html'>-</a></td><td>-</td><td>更改条件，从交易栏删减物品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DEAL_MOD_COINS_REQ.html'>DEAL_MOD_COINS_REQ</a></td><td>0x0000A005</td><td><a href='./-.html'>-</a></td><td>-</td><td>更改条件，修改交易金钱</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DEAL_UPDATE_REQ.html'>DEAL_UPDATE_REQ</a></td><td>0x0000A006</td><td><a href='./-.html'>-</a></td><td>-</td><td>更改条件，通知玩家交易栏状态</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DEAL_AGREE_REQ.html'>DEAL_AGREE_REQ</a></td><td>0x0000A007</td><td><a href='./-.html'>-</a></td><td>-</td><td>同意交易，服务器和客户端共享此协议，客户端玩家向服务端发同意请求，服务器端收到后，如果另一方还没有同意，那么向另一方发这个同意请求，如果也已经同意，则完成这次交易。</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DEAL_DISAGREE_REQ.html'>DEAL_DISAGREE_REQ</a></td><td>0x0000A008</td><td><a href='./-.html'>-</a></td><td>-</td><td>取消同意交易状态，服务器和客户端共享此协议，玩家取消同意状态</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DEAL_CANCEL_REQ.html'>DEAL_CANCEL_REQ</a></td><td>0x0000A009</td><td><a href='./-.html'>-</a></td><td>-</td><td>取消交易状态，服务器和客户端共享此协议，用户不接受交易请求或中途退出交易</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DEAL_MAKED_REQ.html'>DEAL_MAKED_REQ</a></td><td>0x0000A00A</td><td><a href='./-.html'>-</a></td><td>-</td><td>交易达成，双发同意交易，物品放置彼此背包后，通知双方交易已达成</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DEAL_LOCK_REQ.html'>DEAL_LOCK_REQ</a></td><td>0x0000A00B</td><td><a href='./DEAL_LOCK_RES.html'>DEAL_LOCK_RES</a></td><td>0x7000A00B</td><td>锁定交易，当锁定后如果再放入东西或点击取消锁定就解锁</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./WAREHOUSE_GET_REQ.html'>WAREHOUSE_GET_REQ</a></td><td>0x0000B001</td><td><a href='./WAREHOUSE_GET_RES.html'>WAREHOUSE_GET_RES</a></td><td>0x7000B001</td><td>获取一个仓库</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./WAREHOUSE_GET_CARRY_REQ.html'>WAREHOUSE_GET_CARRY_REQ</a></td><td>0x0000B011</td><td><a href='./WAREHOUSE_GET_CARRY_RES.html'>WAREHOUSE_GET_CARRY_RES</a></td><td>0x7000B011</td><td>随身获取一个仓库</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./WAREHOUSE_MOVE_ARTICLE_REQ.html'>WAREHOUSE_MOVE_ARTICLE_REQ</a></td><td>0x0000B002</td><td><a href='./-.html'>-</a></td><td>-</td><td>物品移动，从普通背包到仓库，从防爆背包到仓库，从仓库到普通背包，从仓库到防爆背包，从仓库到仓库</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./WAREHOUSE_ARRANGE_REQ.html'>WAREHOUSE_ARRANGE_REQ</a></td><td>0x0000B005</td><td><a href='./WAREHOUSE_ARRANGE_RES.html'>WAREHOUSE_ARRANGE_RES</a></td><td>0x7000B005</td><td>自动整理一个仓库</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./WAREHOUSE_SET_PASSWORD_REQ.html'>WAREHOUSE_SET_PASSWORD_REQ</a></td><td>0x0000B006</td><td><a href='./WAREHOUSE_SET_PASSWORD_RES.html'>WAREHOUSE_SET_PASSWORD_RES</a></td><td>0x7000B006</td><td>req为客户端向服务器发送设置密码请求，res为服务器告诉客户端打开设置密码窗口</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./WAREHOUSE_MODIFY_PASSWORD_REQ.html'>WAREHOUSE_MODIFY_PASSWORD_REQ</a></td><td>0x0000B007</td><td><a href='./WAREHOUSE_MODIFY_PASSWORD_RES.html'>WAREHOUSE_MODIFY_PASSWORD_RES</a></td><td>0x7000B007</td><td>req为客户端向服务器发送修改密码请求，res为服务器告诉客户端打开修改密码窗口</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./WAREHOUSE_INPUT_PASSWORD_REQ.html'>WAREHOUSE_INPUT_PASSWORD_REQ</a></td><td>0x0000B008</td><td><a href='./WAREHOUSE_INPUT_PASSWORD_RES.html'>WAREHOUSE_INPUT_PASSWORD_RES</a></td><td>0x7000B008</td><td>req为客户端向服务器发送输入密码请求，res为服务器告诉客户端打开输入密码窗口</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MAIL_LIST_REQ.html'>MAIL_LIST_REQ</a></td><td>0x0000C001</td><td><a href='./MAIL_LIST_RES.html'>MAIL_LIST_RES</a></td><td>0x7000C001</td><td>获取邮件列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MAIL_LIST_CARRY_REQ.html'>MAIL_LIST_CARRY_REQ</a></td><td>0x0000C011</td><td><a href='./MAIL_LIST_CARRY_RES.html'>MAIL_LIST_CARRY_RES</a></td><td>0x7000C011</td><td>随身获取邮件列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MAIL_LIST_BY_STATUS_REQ.html'>MAIL_LIST_BY_STATUS_REQ</a></td><td>0x0000C006</td><td><a href='./MAIL_LIST_BY_STATUS_RES.html'>MAIL_LIST_BY_STATUS_RES</a></td><td>0x7000C006</td><td>获取有状态的邮件列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MAIL_LIST_NEW_REQ.html'>MAIL_LIST_NEW_REQ</a></td><td>0x0000C021</td><td><a href='./MAIL_LIST_NEW_RES.html'>MAIL_LIST_NEW_RES</a></td><td>0x7000C021</td><td>获取邮件列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MAIL_LIST_CARRY_NEW_REQ.html'>MAIL_LIST_CARRY_NEW_REQ</a></td><td>0x0000C022</td><td><a href='./MAIL_LIST_CARRY_NEW_RES.html'>MAIL_LIST_CARRY_NEW_RES</a></td><td>0x7000C022</td><td>随身获取邮件列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MAIL_LIST_BY_STATUS_NEW_REQ.html'>MAIL_LIST_BY_STATUS_NEW_REQ</a></td><td>0x0000C023</td><td><a href='./MAIL_LIST_BY_STATUS_NEW_RES.html'>MAIL_LIST_BY_STATUS_NEW_RES</a></td><td>0x7000C023</td><td>获取有状态的邮件列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MAIL_GET_REQ.html'>MAIL_GET_REQ</a></td><td>0x0000C002</td><td><a href='./MAIL_GET_RES.html'>MAIL_GET_RES</a></td><td>0x7000C002</td><td>获取邮件</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MAIL_GETOUT_ARTICLE_REQ.html'>MAIL_GETOUT_ARTICLE_REQ</a></td><td>0x0000C003</td><td><a href='./MAIL_GETOUT_ARTICLE_RES.html'>MAIL_GETOUT_ARTICLE_RES</a></td><td>0x7000C003</td><td>获取邮件的附件物品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MAIL_DELETE_REQ.html'>MAIL_DELETE_REQ</a></td><td>0x0000C004</td><td><a href='./MAIL_DELETE_RES.html'>MAIL_DELETE_RES</a></td><td>0x7000C004</td><td>删除邮件</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MAIL_DELETE_ALL_READED_REQ.html'>MAIL_DELETE_ALL_READED_REQ</a></td><td>0x0000C012</td><td><a href='./MAIL_DELETE_ALL_READED_RES.html'>MAIL_DELETE_ALL_READED_RES</a></td><td>0x7000C012</td><td>删除所有已读没有附件的邮件</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MAIL_CREATE_REQ.html'>MAIL_CREATE_REQ</a></td><td>0x0000C005</td><td><a href='./-.html'>-</a></td><td>-</td><td>创建邮件</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./MAIL_MSG_RES.html'>MAIL_MSG_RES</a></td><td>0x7000C015</td><td>邮件相关信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./AUCTION_LIST_REQ.html'>AUCTION_LIST_REQ</a></td><td>0x0000D001</td><td><a href='./AUCTION_LIST_RES.html'>AUCTION_LIST_RES</a></td><td>0x7000D001</td><td>查询拍卖</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./AUCTION_CREATE_REQ.html'>AUCTION_CREATE_REQ</a></td><td>0x0000D002</td><td><a href='./-.html'>-</a></td><td>-</td><td>创建一个拍卖</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./AUCTION_BUY_REQ.html'>AUCTION_BUY_REQ</a></td><td>0x0000D004</td><td><a href='./-.html'>-</a></td><td>-</td><td>购买拍卖品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./AUCTION_TYPE_GET_REQ.html'>AUCTION_TYPE_GET_REQ</a></td><td>0x0000D005</td><td><a href='./AUCTION_TYPE_GET_RES.html'>AUCTION_TYPE_GET_RES</a></td><td>0x7000D005</td><td>拍卖类型</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./AUCTION_CANCEL_REQ.html'>AUCTION_CANCEL_REQ</a></td><td>0x0000D006</td><td><a href='./-.html'>-</a></td><td>-</td><td>取消拍卖</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SHOPS_NAME_GET_REQ.html'>SHOPS_NAME_GET_REQ</a></td><td>0x0000EA11</td><td><a href='./SHOPS_NAME_GET_RES.html'>SHOPS_NAME_GET_RES</a></td><td>0x7000EA11</td><td>得到商店</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SHOP_GET_REQ.html'>SHOP_GET_REQ</a></td><td>0x0000EA01</td><td><a href='./SHOP_GET_RES.html'>SHOP_GET_RES</a></td><td>0x7000EA01</td><td>得到商店</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./SHOP_OTHER_INFO_RES.html'>SHOP_OTHER_INFO_RES</a></td><td>0x7000EA05</td><td>shop其他信息 跟着SHOP_GET_RES 一起发给客户端</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SHOP_GET_BY_QUERY_CONDITION_REQ.html'>SHOP_GET_BY_QUERY_CONDITION_REQ</a></td><td>0x0000EA12</td><td><a href='./SHOP_GET_BY_QUERY_CONDITION_RES.html'>SHOP_GET_BY_QUERY_CONDITION_RES</a></td><td>0x7000EA12</td><td>根据条件查询到满足条件的商品组装成一个商店</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SHOP_BUY_REQ.html'>SHOP_BUY_REQ</a></td><td>0x0000EA02</td><td><a href='./SHOP_BUY_RES.html'>SHOP_BUY_RES</a></td><td>0x7000EA02</td><td>购买商品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SHOP_SELL_REQ.html'>SHOP_SELL_REQ</a></td><td>0x0000EA03</td><td><a href='./-.html'>-</a></td><td>-</td><td>出售商品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SHOP_BUYBACK_REQ.html'>SHOP_BUYBACK_REQ</a></td><td>0x0000EA04</td><td><a href='./-.html'>-</a></td><td>-</td><td>回购商品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PLAYER_DEAD_REQ.html'>PLAYER_DEAD_REQ</a></td><td>0x0000EAF0</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，玩家死亡，客户端弹出窗口提示用户已死亡</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_REVIVED_REQ.html'>PLAYER_REVIVED_REQ</a></td><td>0x0000EAF1</td><td><a href='./PLAYER_REVIVED_RES.html'>PLAYER_REVIVED_RES</a></td><td>0x7000EAF1</td><td>玩家复活</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PLAYER_REVIVED_INFO_REQ.html'>PLAYER_REVIVED_INFO_REQ</a></td><td>0x0000EAF2</td><td><a href='./PLAYER_REVIVED_INFO_RES.html'>PLAYER_REVIVED_INFO_RES</a></td><td>0x7000EAF2</td><td>玩家复活信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_WINDOW_REQ.html'>QUERY_WINDOW_REQ</a></td><td>0x00F0EEED</td><td><a href='./QUERY_WINDOW_RES.html'>QUERY_WINDOW_RES</a></td><td>0xF0F0EEED</td><td>查询某个NPC身上的窗口</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./OPTION_SELECT_REQ.html'>OPTION_SELECT_REQ</a></td><td>0x00F0EEEC</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端选择了某个窗口中的某个选项，客户端要根据选项中的类型来判断是否要发送此指令，以及是否要等待QUERY_WINDOW_RES响应</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CONFIRM_WINDOW_REQ.html'>CONFIRM_WINDOW_REQ</a></td><td>0x00F0EEEE</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器要求客户端弹出一个确认窗口，要求用户确认。用户选择确认后，客户端发送OPTION_SELECT_REQ指令给服务器</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./INPUT_WINDOW_REQ.html'>INPUT_WINDOW_REQ</a></td><td>0x00F0EEEF</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器要求客户端弹出一个输入窗口，要求用户输入。用户输入确认后，客户端发送OPTION_INPUT_REQ指令给服务器</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./INPUT_WINDOW_PNG_REQ.html'>INPUT_WINDOW_PNG_REQ</a></td><td>0x00F0EEE1</td><td><a href='./-.html'>-</a></td><td>-</td><td>带图片数据的INPUT_WINDOW</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./OPTION_INPUT_REQ.html'>OPTION_INPUT_REQ</a></td><td>0x00F0EEF0</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端完成了输入后确认，客户端发送此指令给服务器</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NPC_REPAIR_QUERY_REQ.html'>NPC_REPAIR_QUERY_REQ</a></td><td>0x00F0EEF2</td><td><a href='./NPC_REPAIR_QUERY_RES.html'>NPC_REPAIR_QUERY_RES</a></td><td>0x70F0EEF2</td><td>查询玩家身上各个装备的修理价格</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NPC_REPAIR_REPAIR_REQ.html'>NPC_REPAIR_REPAIR_REQ</a></td><td>0x00F0EEF3</td><td><a href='./NPC_REPAIR_REPAIR_RES.html'>NPC_REPAIR_REPAIR_RES</a></td><td>0x70F0EEF3</td><td>修理装备</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REPAIR_CARRY_REQ.html'>REPAIR_CARRY_REQ</a></td><td>0x00F0EE13</td><td><a href='./REPAIR_CARRY_RES.html'>REPAIR_CARRY_RES</a></td><td>0x70F0EE13</td><td>随身修理装备</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_COMPOUND_PREPARE_REQ.html'>EQUIPMENT_COMPOUND_PREPARE_REQ</a></td><td>0x00F1EEF0</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，准备装备的合成</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_COMPOUND_REQ.html'>EQUIPMENT_COMPOUND_REQ</a></td><td>0x00F1EEF1</td><td><a href='./EQUIPMENT_COMPOUND_RES.html'>EQUIPMENT_COMPOUND_RES</a></td><td>0x70F1EEF1</td><td>客户端通知服务器，要合成某个装备</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_DRILL_REQ.html'>EQUIPMENT_DRILL_REQ</a></td><td>0x00F1EEF2</td><td><a href='./EQUIPMENT_DRILL_RES.html'>EQUIPMENT_DRILL_RES</a></td><td>0x70F1EEF2</td><td>客户端请求服务器，装备打孔的各种信息，包括需要什么材料，需要几个，用什么来提高成功率，需要多少手续费等</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TRANSIENTENEMY_CHANGE_REQ.html'>TRANSIENTENEMY_CHANGE_REQ</a></td><td>0x00F0EEFF</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，增加或者去除一个临时敌人</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HINT_REQ.html'>HINT_REQ</a></td><td>0x00F0EF00</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，0标识2秒就消失的提示窗口，1标识信息到聊天栏, 2标识在屏幕上方显示文字，持续几秒,最多3条10秒，颜色服务器控制，3标识从屏幕中间右向左滚动 字一个个显示一个个消失，颜色服务器控制，4标识在屏幕下方从左向右显示滚动信息并带粒子, 2条，变颜色1秒，持续10秒, 5覆盖所有窗口上面的提示窗口，一定时间后消失点击不消失，并加入聊天中</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_TOPO_DIAGRAM_REQ.html'>QUERY_TOPO_DIAGRAM_REQ</a></td><td>0x00F1EEEE</td><td><a href='./QUERY_TOPO_DIAGRAM_RES.html'>QUERY_TOPO_DIAGRAM_RES</a></td><td>0xF0F1EEEE</td><td>查询游戏的拓扑结构</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CONNECT_FAILED_REQ.html'>CONNECT_FAILED_REQ</a></td><td>0x0000EB04</td><td><a href='./-.html'>-</a></td><td>-</td><td>和服务器的连接创建失败</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_CREATE_REQ.html'>JIAZU_CREATE_REQ</a></td><td>0x000AEE01</td><td><a href='./JIAZU_CREATE_RES.html'>JIAZU_CREATE_RES</a></td><td>0x700AEE01</td><td>客户端请求创建家族</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_LIST_REQ.html'>JIAZU_LIST_REQ</a></td><td>0x000AEE02</td><td><a href='./JIAZU_LIST_RES.html'>JIAZU_LIST_RES</a></td><td>0x700AEE02</td><td>客户端请求获取家族列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_APPLY_REQ.html'>JIAZU_APPLY_REQ</a></td><td>0x000AEE03</td><td><a href='./JIAZU_APPLY_RES.html'>JIAZU_APPLY_RES</a></td><td>0x700AEE03</td><td>客户端申请加入家族</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_QUERY_APPLY_REQ.html'>JIAZU_QUERY_APPLY_REQ</a></td><td>0x000AEE04</td><td><a href='./JIAZU_QUERY_APPLY_RES.html'>JIAZU_QUERY_APPLY_RES</a></td><td>0x700AEE04</td><td>查询申请加入家族的列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_APPROVE_APPLY_REQ.html'>JIAZU_APPROVE_APPLY_REQ</a></td><td>0x000AEE05</td><td><a href='./JIAZU_APPROVE_APPLY_RES.html'>JIAZU_APPROVE_APPLY_RES</a></td><td>0x700AEE05</td><td>审核加入家族</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_EXPEL_MEMBER_REQ.html'>JIAZU_EXPEL_MEMBER_REQ</a></td><td>0x000AEE06</td><td><a href='./JIAZU_EXPEL_MEMBER_RES.html'>JIAZU_EXPEL_MEMBER_RES</a></td><td>0x700AEE06</td><td>踢出家族</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_LEAVE_REQ.html'>JIAZU_LEAVE_REQ</a></td><td>0x000AEE07</td><td><a href='./JIAZU_LEAVE_RES.html'>JIAZU_LEAVE_RES</a></td><td>0x700AEE07</td><td>离开家族</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_QURRY_BASE_LIST_REQ.html'>JIAZU_QURRY_BASE_LIST_REQ</a></td><td>0x000AEE08</td><td><a href='./JIAZU_QURRY_BASE_LIST_RES.html'>JIAZU_QURRY_BASE_LIST_RES</a></td><td>0x700AEE08</td><td>获取驻地申请列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_BASE_REQ.html'>JIAZU_BASE_REQ</a></td><td>0x000AEE09</td><td><a href='./JIAZU_BASE_RES.html'>JIAZU_BASE_RES</a></td><td>0x700AEE09</td><td>申请驻地</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_MASTER_RESIGN_REQ.html'>JIAZU_MASTER_RESIGN_REQ</a></td><td>0x000AEE10</td><td><a href='./JIAZU_MASTER_RESIGN_RES.html'>JIAZU_MASTER_RESIGN_RES</a></td><td>0x700AEE10</td><td>族长禅让</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_APPLY_MASTER_REQ.html'>JIAZU_APPLY_MASTER_REQ</a></td><td>0x000AEE11</td><td><a href='./JIAZU_APPLY_MASTER_RES.html'>JIAZU_APPLY_MASTER_RES</a></td><td>0x700AEE11</td><td>副族长申请为族长</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_DISMISS_REQ.html'>JIAZU_DISMISS_REQ</a></td><td>0x000AEE12</td><td><a href='./JIAZU_DISMISS_RES.html'>JIAZU_DISMISS_RES</a></td><td>0x700AEE12</td><td>解散家族</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_MODIFY_SLOGAN_REQ.html'>JIAZU_MODIFY_SLOGAN_REQ</a></td><td>0x000AEE13</td><td><a href='./JIAZU_MODIFY_SLOGAN_RES.html'>JIAZU_MODIFY_SLOGAN_RES</a></td><td>0x700AEE13</td><td>修改家族宗旨</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_APPOINT_REQ.html'>JIAZU_APPOINT_REQ</a></td><td>0x000AEE14</td><td><a href='./JIAZU_APPOINT_RES.html'>JIAZU_APPOINT_RES</a></td><td>0x700AEE14</td><td>任命家族</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_SET_RANK_NAME_REQ.html'>JIAZU_SET_RANK_NAME_REQ</a></td><td>0x000AEE15</td><td><a href='./JIAZU_SET_RANK_NAME_RES.html'>JIAZU_SET_RANK_NAME_RES</a></td><td>0x700AEE15</td><td>设定家族称号</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_SET_ICON_REQ.html'>JIAZU_SET_ICON_REQ</a></td><td>0x000AEE16</td><td><a href='./JIAZU_SET_ICON_RES.html'>JIAZU_SET_ICON_RES</a></td><td>0x700AEE16</td><td>更改家族徽章</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_LIST_SALARY_REQ.html'>JIAZU_LIST_SALARY_REQ</a></td><td>0x000AEE17</td><td><a href='./JIAZU_LIST_SALARY_RES.html'>JIAZU_LIST_SALARY_RES</a></td><td>0x700AEE17</td><td>查询工资</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_SALARY_CEREMONY_REQ.html'>JIAZU_SALARY_CEREMONY_REQ</a></td><td>0x000AEE18</td><td><a href='./JIAZU_SALARY_CEREMONY_RES.html'>JIAZU_SALARY_CEREMONY_RES</a></td><td>0x700AEE18</td><td>工资仪式</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_QUERY_MEMBER_SALARY_REQ.html'>JIAZU_QUERY_MEMBER_SALARY_REQ</a></td><td>0x000AEE19</td><td><a href='./JIAZU_QUERY_MEMBER_SALARY_RES.html'>JIAZU_QUERY_MEMBER_SALARY_RES</a></td><td>0x700AEE19</td><td>查询单个成员的贡献度与工资范围</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_P_SALARY_REQ.html'>JIAZU_P_SALARY_REQ</a></td><td>0x000AEE20</td><td><a href='./JIAZU_P_SALARY_RES.html'>JIAZU_P_SALARY_RES</a></td><td>0x700AEE20</td><td>发送工资</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_INFO_REQ.html'>JIAZU_INFO_REQ</a></td><td>0x000AEE21</td><td><a href='./JIAZU_INFO_RES.html'>JIAZU_INFO_RES</a></td><td>0x700AEE21</td><td>查询家族信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_BUY_BEDGE_REQ.html'>JIAZU_BUY_BEDGE_REQ</a></td><td>0x000AEE23</td><td><a href='./JIAZU_BUY_BEDGE_RES.html'>JIAZU_BUY_BEDGE_RES</a></td><td>0x700AEE23</td><td>购买家族徽章</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_REPLACE_BEDGE_REQ.html'>JIAZU_REPLACE_BEDGE_REQ</a></td><td>0x000AEE24</td><td><a href='./JIAZU_REPLACE_BEDGE_RES.html'>JIAZU_REPLACE_BEDGE_RES</a></td><td>0x700AEE24</td><td>替换徽章</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_QUERY_BY_ID_REQ.html'>JIAZU_QUERY_BY_ID_REQ</a></td><td>0x000AEE25</td><td><a href='./JIAZU_QUERY_BY_ID_RES.html'>JIAZU_QUERY_BY_ID_RES</a></td><td>0x700AEE25</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_INVITE_REQ.html'>JIAZU_INVITE_REQ</a></td><td>0x000AEE26</td><td><a href='./JIAZU_INVITE_RES.html'>JIAZU_INVITE_RES</a></td><td>0x700AEE26</td><td>邀请</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_SHOW_JIAZU_FUNCTION_REQ.html'>JIAZU_SHOW_JIAZU_FUNCTION_REQ</a></td><td>0x000AEE27</td><td><a href='./JIAZU_SHOW_JIAZU_FUNCTION_RES.html'>JIAZU_SHOW_JIAZU_FUNCTION_RES</a></td><td>0x700AEE27</td><td>告诉客户端弹出创建家族的窗口</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_QUERY_STATION_REQ.html'>JIAZU_QUERY_STATION_REQ</a></td><td>0x000AEE28</td><td><a href='./JIAZU_QUERY_STATION_RES.html'>JIAZU_QUERY_STATION_RES</a></td><td>0x700AEE28</td><td>驻地进入信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_TITLE_CHANGE_REQ.html'>JIAZU_TITLE_CHANGE_REQ</a></td><td>0x000AEE29</td><td><a href='./JIAZU_TITLE_CHANGE_RES.html'>JIAZU_TITLE_CHANGE_RES</a></td><td>0x700AEE29</td><td>家族职位更改</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_BATCH_SEND_SALARY_REQ.html'>JIAZU_BATCH_SEND_SALARY_REQ</a></td><td>0x000AEE2A</td><td><a href='./JIAZU_BATCH_SEND_SALARY_RES.html'>JIAZU_BATCH_SEND_SALARY_RES</a></td><td>0x700AEE2A</td><td>批量发送工资</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_CONTRIBUTE_MONEY_REQ.html'>JIAZU_CONTRIBUTE_MONEY_REQ</a></td><td>0x000AEE2B</td><td><a href='./JIAZU_CONTRIBUTE_MONEY_RES.html'>JIAZU_CONTRIBUTE_MONEY_RES</a></td><td>0x700AEE2B</td><td>家族捐献</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./JIAZU_WAREHOUSE_RES.html'>JIAZU_WAREHOUSE_RES</a></td><td>0x700AEE2C</td><td>家族仓库</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_WAREHOUSE_TOPLAYER_REQ.html'>JIAZU_WAREHOUSE_TOPLAYER_REQ</a></td><td>0x000AEE2D</td><td><a href='./JIAZU_WAREHOUSE_TOPLAYER_RES.html'>JIAZU_WAREHOUSE_TOPLAYER_RES</a></td><td>0x700AEE2D</td><td>分配家族物品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_INFO_ADD_TIME_REQ.html'>JIAZU_INFO_ADD_TIME_REQ</a></td><td>0x000AEE2E</td><td><a href='./JIAZU_INFO_ADD_TIME_RES.html'>JIAZU_INFO_ADD_TIME_RES</a></td><td>0x700AEE2E</td><td>查询家族信息(2012-7-9 11:33:21新增协议)</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SEPTBUILDING_CREATE_REQ.html'>SEPTBUILDING_CREATE_REQ</a></td><td>0x000EEE01</td><td><a href='./SEPTBUILDING_CREATE_RES.html'>SEPTBUILDING_CREATE_RES</a></td><td>0x700EEE01</td><td>客户端请求建造建筑</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SEPTBUILDING_LVUP_REQ.html'>SEPTBUILDING_LVUP_REQ</a></td><td>0x000EEE02</td><td><a href='./SEPTBUILDING_LVUP_RES.html'>SEPTBUILDING_LVUP_RES</a></td><td>0x700EEE02</td><td>客户端请求升级建筑</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SEPTBUILDING_LVDOWN_REQ.html'>SEPTBUILDING_LVDOWN_REQ</a></td><td>0x000EEE03</td><td><a href='./SEPTBUILDING_LVDOWN_RES.html'>SEPTBUILDING_LVDOWN_RES</a></td><td>0x700EEE03</td><td>客户端请求降级建筑</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SEPTBUILDING_QUERY_CANUP_REQ.html'>SEPTBUILDING_QUERY_CANUP_REQ</a></td><td>0x000EEE04</td><td><a href='./SEPTBUILDING_QUERY_CANUP_RES.html'>SEPTBUILDING_QUERY_CANUP_RES</a></td><td>0x700EEE04</td><td>查看可升级建筑</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SEPTBUILDING_QUERY_BUILDING_INFO_REQ.html'>SEPTBUILDING_QUERY_BUILDING_INFO_REQ</a></td><td>0x000EEE05</td><td><a href='./SEPTBUILDING_QUERY_BUILDING_INFO_RES.html'>SEPTBUILDING_QUERY_BUILDING_INFO_RES</a></td><td>0x700EEE05</td><td>查看建筑信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SEPTBUILDING_CREAT_SEPT_REQ.html'>SEPTBUILDING_CREAT_SEPT_REQ</a></td><td>0x000EEE06</td><td><a href='./SEPTBUILDING_CREAT_SEPT_RES.html'>SEPTBUILDING_CREAT_SEPT_RES</a></td><td>0x700EEE06</td><td>创建一个默认的家族驻地</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SEPTBUILDING_DESTROY_REQ.html'>SEPTBUILDING_DESTROY_REQ</a></td><td>0x000EEE08</td><td><a href='./SEPTBUILDING_DESTROY_RES.html'>SEPTBUILDING_DESTROY_RES</a></td><td>0x700EEE08</td><td>摧毁建筑</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SEPTBUILDING_INFO_REQ.html'>SEPTBUILDING_INFO_REQ</a></td><td>0x000EEE09</td><td><a href='./SEPTBUILDING_INFO_RES.html'>SEPTBUILDING_INFO_RES</a></td><td>0x700EEE09</td><td>查看家族建筑信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_CALL_IN_REQ.html'>JIAZU_CALL_IN_REQ</a></td><td>0x000EEE0A</td><td><a href='./-.html'>-</a></td><td>-</td><td>族长召集家族成员</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_CLIENT_SURFACENPC_REQ.html'>NOTICE_CLIENT_SURFACENPC_REQ</a></td><td>0x000EEE0B</td><td><a href='./-.html'>-</a></td><td>-</td><td>地物NPC信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTICE_CLIENT_JIAZUBOSS_REQ.html'>NOTICE_CLIENT_JIAZUBOSS_REQ</a></td><td>0x000EEE0C</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端打开/关闭查询家族BOSS活动按钮</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_JIAZUBOSS_DAMAGE_REQ.html'>QUERY_JIAZUBOSS_DAMAGE_REQ</a></td><td>0x000EEE0D</td><td><a href='./QUERY_JIAZUBOSS_DAMAGE_RES.html'>QUERY_JIAZUBOSS_DAMAGE_RES</a></td><td>0x700EEE0D</td><td>查询伤害列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOOTHSALE_LOOK_OVER_REQ.html'>BOOTHSALE_LOOK_OVER_REQ</a></td><td>0x00F00001</td><td><a href='./BOOTHSALE_LOOK_OVER_RES.html'>BOOTHSALE_LOOK_OVER_RES</a></td><td>0x70F00001</td><td>查询玩家的摊位</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOOTHSALE_SELECT_FOR_SALE_REQ.html'>BOOTHSALE_SELECT_FOR_SALE_REQ</a></td><td>0x00F00002</td><td><a href='./BOOTHSALE_SELECT_FOR_SALE_RES.html'>BOOTHSALE_SELECT_FOR_SALE_RES</a></td><td>0x70F00002</td><td>选择出售物品,追售</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOOTHSALE_CANCEL_FOR_SALE_REQ.html'>BOOTHSALE_CANCEL_FOR_SALE_REQ</a></td><td>0x00F00003</td><td><a href='./BOOTHSALE_CANCEL_FOR_SALE_RES.html'>BOOTHSALE_CANCEL_FOR_SALE_RES</a></td><td>0x70F00003</td><td>取消物品出售</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOOTHSALE_ADVERTISING_REQ.html'>BOOTHSALE_ADVERTISING_REQ</a></td><td>0x00F00004</td><td><a href='./BOOTHSALE_ADVERTISING_RES.html'>BOOTHSALE_ADVERTISING_RES</a></td><td>0x70F00004</td><td>提交广告语</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOOTHSALE_BUY_REQ.html'>BOOTHSALE_BUY_REQ</a></td><td>0x00F00005</td><td><a href='./BOOTHSALE_BUY_RES.html'>BOOTHSALE_BUY_RES</a></td><td>0x70F00005</td><td>购买物品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOOTHSALE_LEAVE_WORD_REQ.html'>BOOTHSALE_LEAVE_WORD_REQ</a></td><td>0x00F00006</td><td><a href='./BOOTHSALE_LEAVE_WORD_RES.html'>BOOTHSALE_LEAVE_WORD_RES</a></td><td>0x70F00006</td><td>给摊主留言</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOOTHSALE_CANCEL_BOOTHSALE_REQ.html'>BOOTHSALE_CANCEL_BOOTHSALE_REQ</a></td><td>0x00F00007</td><td><a href='./BOOTHSALE_CANCEL_BOOTHSALE_RES.html'>BOOTHSALE_CANCEL_BOOTHSALE_RES</a></td><td>0x70F00007</td><td>收摊</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOOTHSALE_BOOTHSALE_REQUEST_REQ.html'>BOOTHSALE_BOOTHSALE_REQUEST_REQ</a></td><td>0x00F00008</td><td><a href='./BOOTHSALE_BOOTHSALE_REQUEST_RES.html'>BOOTHSALE_BOOTHSALE_REQUEST_RES</a></td><td>0x70F00008</td><td>申请摆摊</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOOTHSALE_BOOTHCHANGE_REQ.html'>BOOTHSALE_BOOTHCHANGE_REQ</a></td><td>0x00F00010</td><td><a href='./BOOTHSALE_BOOTHCHANGE_RES.html'>BOOTHSALE_BOOTHCHANGE_RES</a></td><td>0x70F00010</td><td>摊位变化</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOOTHSALE_LEAVE_BOOTH_REQ.html'>BOOTHSALE_LEAVE_BOOTH_REQ</a></td><td>0x00F00011</td><td><a href='./BOOTHSALE_LEAVE_BOOTH_RES.html'>BOOTHSALE_LEAVE_BOOTH_RES</a></td><td>0x70F00011</td><td>离开摊位</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOOTHSALE_QUIT_BOOTHSALE_REQUEST_REQ.html'>BOOTHSALE_QUIT_BOOTHSALE_REQUEST_REQ</a></td><td>0x00F00012</td><td><a href='./BOOTHSALE_QUIT_BOOTHSALE_REQUEST_RES.html'>BOOTHSALE_QUIT_BOOTHSALE_REQUEST_RES</a></td><td>0x70F00012</td><td>取消申请摆摊</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOOTHSALE_CHANAGE_ADVERTISING_REQ.html'>BOOTHSALE_CHANAGE_ADVERTISING_REQ</a></td><td>0x00F00014</td><td><a href='./BOOTHSALE_CHANAGE_ADVERTISING_RES.html'>BOOTHSALE_CHANAGE_ADVERTISING_RES</a></td><td>0x70F00014</td><td>修改广告语</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOOTHSALE_NEW_BUY_REQ.html'>BOOTHSALE_NEW_BUY_REQ</a></td><td>0x00F00015</td><td><a href='./-.html'>-</a></td><td>-</td><td>购买物品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REQUESTBUY_RELEASE_REQ.html'>REQUESTBUY_RELEASE_REQ</a></td><td>0x00F00102</td><td><a href='./REQUESTBUY_RELEASE_RES.html'>REQUESTBUY_RELEASE_RES</a></td><td>0x70F00102</td><td>发布求购信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REQUESTBUY_QUERY_SELF_REQ.html'>REQUESTBUY_QUERY_SELF_REQ</a></td><td>0x00F00103</td><td><a href='./REQUESTBUY_QUERY_SELF_RES.html'>REQUESTBUY_QUERY_SELF_RES</a></td><td>0x70F00103</td><td>查看自己的求购</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REQUESTBUY_CANCEL_SELF_REQ.html'>REQUESTBUY_CANCEL_SELF_REQ</a></td><td>0x00F00104</td><td><a href='./REQUESTBUY_CANCEL_SELF_RES.html'>REQUESTBUY_CANCEL_SELF_RES</a></td><td>0x70F00104</td><td>取消自己的求购</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REQUESTBUY_QRY_HIGH_PRICE_REQ.html'>REQUESTBUY_QRY_HIGH_PRICE_REQ</a></td><td>0x00F00105</td><td><a href='./REQUESTBUY_QRY_HIGH_PRICE_RES.html'>REQUESTBUY_QRY_HIGH_PRICE_RES</a></td><td>0x70F00105</td><td>试图出售物品(查看最高价)</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REQUESTBUY_SALE_REQ.html'>REQUESTBUY_SALE_REQ</a></td><td>0x00F00106</td><td><a href='./REQUESTBUY_SALE_RES.html'>REQUESTBUY_SALE_RES</a></td><td>0x70F00106</td><td>出售物品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REQUESTBUY_QRY_BY_TERM_REQ.html'>REQUESTBUY_QRY_BY_TERM_REQ</a></td><td>0x00F00107</td><td><a href='./REQUESTBUY_QRY_BY_TERM_RES.html'>REQUESTBUY_QRY_BY_TERM_RES</a></td><td>0x70F00107</td><td>查询当前所有符合条件的求购</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REQUESTBUY_ASK_KNAP_REMAI_REQ.html'>REQUESTBUY_ASK_KNAP_REMAI_REQ</a></td><td>0x00F00108</td><td><a href='./REQUESTBUY_ASK_KNAP_REMAI_RES.html'>REQUESTBUY_ASK_KNAP_REMAI_RES</a></td><td>0x70F00108</td><td>查询当前包裹符合热卖条件的求购</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REQUESTBUY_CONTIDION_TYPE_REQ.html'>REQUESTBUY_CONTIDION_TYPE_REQ</a></td><td>0x00f00109</td><td><a href='./REQUESTBUY_CONTIDION_TYPE_RES.html'>REQUESTBUY_CONTIDION_TYPE_RES</a></td><td>0x70F00109</td><td>请求求购物品一级二级相关分类</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REQUESTBUY_GET_PROPNAME_REQ.html'>REQUESTBUY_GET_PROPNAME_REQ</a></td><td>0x00f0010A</td><td><a href='./REQUESTBUY_GET_PROPNAME_RES.html'>REQUESTBUY_GET_PROPNAME_RES</a></td><td>0x70F0010A</td><td>根据1,2级分类取相关物品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REQUESTBUY_GET_ENTITY_REQ.html'>REQUESTBUY_GET_ENTITY_REQ</a></td><td>0x00f0010B</td><td><a href='./REQUESTBUY_GET_ENTITY_RES.html'>REQUESTBUY_GET_ENTITY_RES</a></td><td>0x70F0010B</td><td>查询要求购的物品的Entity对象</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REQUESTBUY_FASTBUY_REQ.html'>REQUESTBUY_FASTBUY_REQ</a></td><td>0x00f0010C</td><td><a href='./REQUESTBUY_FASTBUY_RES.html'>REQUESTBUY_FASTBUY_RES</a></td><td>0x70F0010C</td><td>查询当前快速求购列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REQUESTBUY_SUISHEN_REQ.html'>REQUESTBUY_SUISHEN_REQ</a></td><td>0x00f0010D</td><td><a href='./REQUESTBUY_SUISHEN_RES.html'>REQUESTBUY_SUISHEN_RES</a></td><td>0x70f0010D</td><td>随身求购</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_ACTIVATION_SOUL_REQ.html'>PLAYER_ACTIVATION_SOUL_REQ</a></td><td>0x00FA0100</td><td><a href='./PLAYER_ACTIVATION_SOUL_RES.html'>PLAYER_ACTIVATION_SOUL_RES</a></td><td>0x70FA0100</td><td>激活元神</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PLAYER_SWITCH_SOUL_REQ.html'>PLAYER_SWITCH_SOUL_REQ</a></td><td>0x00FA0101</td><td><a href='./PLAYER_SWITCH_SOUL_RES.html'>PLAYER_SWITCH_SOUL_RES</a></td><td>0x70FA0101</td><td>切换元神</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_SOUL_CHANGE_REQ.html'>PLAYER_SOUL_CHANGE_REQ</a></td><td>0x00FA0102</td><td><a href='./PLAYER_SOUL_CHANGE_RES.html'>PLAYER_SOUL_CHANGE_RES</a></td><td>0x70FA0102</td><td>元神属性变化</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CARD_SAVING_REQ.html'>CARD_SAVING_REQ</a></td><td>0x0000EF00</td><td><a href='./CARD_SAVING_RES.html'>CARD_SAVING_RES</a></td><td>0x7000EF00</td><td>用户充值</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SAVING_HISTORY_REQ.html'>SAVING_HISTORY_REQ</a></td><td>0x0000EF01</td><td><a href='./SAVING_HISTORY_RES.html'>SAVING_HISTORY_RES</a></td><td>0x7000EF01</td><td>客户端请求充值记录</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SET_QUEUE_READYNUM_REQ.html'>SET_QUEUE_READYNUM_REQ</a></td><td>0x70F00000</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端设置网络参数,1 含义是有数据包立即发送  2 含义是有2个数据包立即发送，如果只有一个数据包，等到 0.2 * 2 = 0.4秒   3 含义是有3个数据包立即发送，如果只有一个或者两个数据包，等到 0.2 * 3 = 0.6秒</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GANGWAREHOUSE_GET_REQ.html'>GANGWAREHOUSE_GET_REQ</a></td><td>0x0000EF02</td><td><a href='./GANGWAREHOUSE_GET_RES.html'>GANGWAREHOUSE_GET_RES</a></td><td>0x7000EF02</td><td>获取一个帮派仓库</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GANGWAREHOUSE_PUT_REQ.html'>GANGWAREHOUSE_PUT_REQ</a></td><td>0x0000EF03</td><td><a href='./-.html'>-</a></td><td>-</td><td>往帮派仓库中放置一个物品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GANGWAREHOUSE_TAKE_REQ.html'>GANGWAREHOUSE_TAKE_REQ</a></td><td>0x0000EF04</td><td><a href='./-.html'>-</a></td><td>-</td><td>从帮派仓库中取出一个物品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GANGWAREHOUSE_ARRANGE_REQ.html'>GANGWAREHOUSE_ARRANGE_REQ</a></td><td>0x0000EF05</td><td><a href='./-.html'>-</a></td><td>-</td><td>整理帮派仓库</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CONTRIBUTION_REQ.html'>CONTRIBUTION_REQ</a></td><td>0x0000EF06</td><td><a href='./-.html'>-</a></td><td>-</td><td>捐款或者捐物</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GANG_WARE_HOUSE_JOURNAL_REQ.html'>GANG_WARE_HOUSE_JOURNAL_REQ</a></td><td>0x0000EF07</td><td><a href='./GANG_WARE_HOUSE_JOURNAL_RES.html'>GANG_WARE_HOUSE_JOURNAL_RES</a></td><td>0x7000EF07</td><td>请求公会仓库日志</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MAP_POLYGON_MODIFY_REQ.html'>MAP_POLYGON_MODIFY_REQ</a></td><td>0x0000EF08</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器发送给客户端，通知客户端增加或者删除某个碰撞区域</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SKILL_CD_MODIFY_REQ.html'>SKILL_CD_MODIFY_REQ</a></td><td>0x0000EF09</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器发送给客户端，通知客户端技能CD状态，防止冷却时间长的技能在服务端释放失败，导致无谓的长时间冷却。</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PROPS_CD_MODIFY_REQ.html'>PROPS_CD_MODIFY_REQ</a></td><td>0x0200EF09</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器发送给客户端，通知客户端技能CD状态，防止冷却时间长的技能在服务端释放失败，导致无谓的长时间冷却。</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_BILLBOARD_MENU_REQ.html'>GET_BILLBOARD_MENU_REQ</a></td><td>0x0000EF10</td><td><a href='./GET_BILLBOARD_MENU_RES.html'>GET_BILLBOARD_MENU_RES</a></td><td>0x7000EF10</td><td>请求排行榜菜单</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_BILLBOARD_REQ.html'>GET_BILLBOARD_REQ</a></td><td>0x0000EF11</td><td><a href='./GET_BILLBOARD_RES.html'>GET_BILLBOARD_RES</a></td><td>0x7000EF11</td><td>请求排行榜</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_JOIN_BILLBOARD_REQ.html'>EQUIPMENT_JOIN_BILLBOARD_REQ</a></td><td>0x0000EF12</td><td><a href='./-.html'>-</a></td><td>-</td><td>请求参与排行</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_CARD_CHARGE_INFO_REQ.html'>GET_CARD_CHARGE_INFO_REQ</a></td><td>0x0000EF13</td><td><a href='./GET_CARD_CHARGE_INFO_RES.html'>GET_CARD_CHARGE_INFO_RES</a></td><td>0x7000EF13</td><td>请求充值卡充值信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_SMS_CHARGE_INFO_REQ.html'>GET_SMS_CHARGE_INFO_REQ</a></td><td>0x0000EF14</td><td><a href='./GET_SMS_CHARGE_INFO_RES.html'>GET_SMS_CHARGE_INFO_RES</a></td><td>0x7000EF14</td><td>请求短信充值信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SMS_CHARGE_REQ.html'>SMS_CHARGE_REQ</a></td><td>0x0000EF15</td><td><a href='./-.html'>-</a></td><td>-</td><td>短信充值信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SMS_BIND_REQ.html'>SMS_BIND_REQ</a></td><td>0x0000EF17</td><td><a href='./SMS_BIND_RES.html'>SMS_BIND_RES</a></td><td>0x7000EF17</td><td>短信操作:mode: 0-绑定手机号,1-取消绑定,2-取回账号密码</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEW_RECOMMEND_REQ.html'>NEW_RECOMMEND_REQ</a></td><td>0x0000AF01</td><td><a href='./NEW_RECOMMEND_RES.html'>NEW_RECOMMEND_RES</a></td><td>0x7000AF01</td><td>创建新的推荐，当返回的所有字段都为空是，表示服务器端发生错误，推荐失败</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_ACTIVITIES_INFO_REQ.html'>QUERY_ACTIVITIES_INFO_REQ</a></td><td>0x0000AF02</td><td><a href='./QUERY_ACTIVITIES_INFO_RES.html'>QUERY_ACTIVITIES_INFO_RES</a></td><td>0x7000AF02</td><td>查询运营活动信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_ACTIVITIES_DETAIL_REQ.html'>QUERY_ACTIVITIES_DETAIL_REQ</a></td><td>0x0000AF03</td><td><a href='./QUERY_ACTIVITIES_DETAIL_RES.html'>QUERY_ACTIVITIES_DETAIL_RES</a></td><td>0x7000AF03</td><td>查询运营活动细节</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_NETWORKFLOW_REQ.html'>QUERY_NETWORKFLOW_REQ</a></td><td>0x0000AF05</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_HONOR_REQ.html'>QUERY_HONOR_REQ</a></td><td>0x0000AF0C</td><td><a href='./QUERY_HONOR_RES.html'>QUERY_HONOR_RES</a></td><td>0x7000AF0C</td><td>客户端查询称号，服务器返回的称号信息应按显示顺序排列</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_HONOR_INFO_REQ.html'>QUERY_HONOR_INFO_REQ</a></td><td>0x0000AF0D</td><td><a href='./QUERY_HONOR_INFO_RES.html'>QUERY_HONOR_INFO_RES</a></td><td>0x7000AF0D</td><td>请求称号详细描述</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_HONOR_INFO_BY_NAME_REQ.html'>QUERY_HONOR_INFO_BY_NAME_REQ</a></td><td>0x0000AF1D</td><td><a href='./QUERY_HONOR_INFO_BY_NAME_RES.html'>QUERY_HONOR_INFO_BY_NAME_RES</a></td><td>0x7000AF1D</td><td>请求称号详细描述</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HONOR_OPRATION_REQ.html'>HONOR_OPRATION_REQ</a></td><td>0x0000AF0E</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端发起的对称号的操作，如装备、取消装备，删除等</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MASTERS_AND_PRENTICES_OPERATION_REQ.html'>MASTERS_AND_PRENTICES_OPERATION_REQ</a></td><td>0x0000AF0F</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端发起的对师徒关系的操作</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_MASTERS_AND_PRENTICES_REQ.html'>QUERY_MASTERS_AND_PRENTICES_REQ</a></td><td>0x0000AF10</td><td><a href='./QUERY_MASTERS_AND_PRENTICES_RES.html'>QUERY_MASTERS_AND_PRENTICES_RES</a></td><td>0x7000AF10</td><td>请求师徒信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_MASTERS_INFO_REQ.html'>QUERY_MASTERS_INFO_REQ</a></td><td>0x0000AF11</td><td><a href='./QUERY_MASTERS_INFO_RES.html'>QUERY_MASTERS_INFO_RES</a></td><td>0x7000AF11</td><td>请求自己的身份，师德，师徒积分，拜师时间，出师时间， 师傅的 师傅名，等级，，师德，师徒积分，帮会，拜师时间，出师时间，最近上线,是否在线</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_PRENTICES_INFO_REQ.html'>QUERY_PRENTICES_INFO_REQ</a></td><td>0x0000AF12</td><td><a href='./QUERY_PRENTICES_INFO_RES.html'>QUERY_PRENTICES_INFO_RES</a></td><td>0x7000AF12</td><td>请求徒弟信息，徒弟名，等级，帮会，拜师时间，出师时间，最近上线</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NOTICE_DELETE_MASTERPRENTICE_RES.html'>NOTICE_DELETE_MASTERPRENTICE_RES</a></td><td>0x0000AF13</td><td>服务器通知客户端删除某一条师徒申请记录</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_BATTLEFIELDLIST_REQ.html'>QUERY_BATTLEFIELDLIST_REQ</a></td><td>0x0000AF20</td><td><a href='./QUERY_BATTLEFIELDLIST_RES.html'>QUERY_BATTLEFIELDLIST_RES</a></td><td>0x7000AF20</td><td>查询战场列表信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BATTLEFIELD_ACTION_REQ.html'>BATTLEFIELD_ACTION_REQ</a></td><td>0x0000AF21</td><td><a href='./-.html'>-</a></td><td>-</td><td>战场排队</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_BATTLEFIELD_INFO_REQ.html'>QUERY_BATTLEFIELD_INFO_REQ</a></td><td>0x0000AF22</td><td><a href='./QUERY_BATTLEFIELD_INFO_RES.html'>QUERY_BATTLEFIELD_INFO_RES</a></td><td>0x7000AF22</td><td>查询战场详细信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DISPLAY_INFO_ON_SCREEN.html'>DISPLAY_INFO_ON_SCREEN</a></td><td>0x0000AF23</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器让客户端在屏幕上显示文字</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GANG_MAIL_CREATE_REQ.html'>GANG_MAIL_CREATE_REQ</a></td><td>0x0000AF30</td><td><a href='./-.html'>-</a></td><td>-</td><td>帮派群发创建邮件</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_ENTER_DUELBATTLE_MONI_REQ.html'>PLAYER_ENTER_DUELBATTLE_MONI_REQ</a></td><td>0x0000FF12</td><td><a href='./PLAYER_ENTER_DUELBATTLE_MONI_RES.html'>PLAYER_ENTER_DUELBATTLE_MONI_RES</a></td><td>0x7000FF12</td><td>玩家模拟进入比赛地图</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SET_PET_ACTIVATIONTYPE_REQ.html'>SET_PET_ACTIVATIONTYPE_REQ</a></td><td>0x00F0EF10</td><td><a href='./-.html'>-</a></td><td>-</td><td>设置宠物的行为模式</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_PET_STRONG_REQ.html'>QUERY_PET_STRONG_REQ</a></td><td>0x00F0EE10</td><td><a href='./QUERY_PET_STRONG_RES.html'>QUERY_PET_STRONG_RES</a></td><td>0x70F0EE10</td><td>客户端请求服务器，宠物强化的各种信息，包括需要什么材料，需要几个，用什么来提高成功率，需要多少手续费等</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_STRONG_REQ.html'>PET_STRONG_REQ</a></td><td>0x00F0EE12</td><td><a href='./PET_STRONG_RES.html'>PET_STRONG_RES</a></td><td>0x70F0EE12</td><td>客户端请求服务器，宠物强化，服务端发送强化成功消息前必须再次发出QUERY_EQUIPMENT_STRONG_RES通知客户端材料变化</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_SKILL_CAST_REQ.html'>PET_SKILL_CAST_REQ</a></td><td>0x0000FF13</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，当前宠物可以施放的技能</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_NONTARGET_SKILL_REQ.html'>PET_NONTARGET_SKILL_REQ</a></td><td>0x0000FF14</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端通知服务器，宠物无目标地施放技能</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_TARGET_SKILL_REQ.html'>PET_TARGET_SKILL_REQ</a></td><td>0x0000FF15</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端通知服务器，宠物有目标地施放技能</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_QUERY_REQ.html'>PET_QUERY_REQ</a></td><td>0x0000FF16</td><td><a href='./PET_QUERY_RES.html'>PET_QUERY_RES</a></td><td>0x7000FF16</td><td>请求宠物信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_BREEDING_REQ.html'>PET_BREEDING_REQ</a></td><td>0x0000FF17</td><td><a href='./PET_BREEDING_RES.html'>PET_BREEDING_RES</a></td><td>0x7000FF17</td><td>宠物蛋孵化</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_MATING_REQ.html'>PET_MATING_REQ</a></td><td>0x0000FF18</td><td><a href='./PET_MATING_RES.html'>PET_MATING_RES</a></td><td>0x7000FF18</td><td>和不同玩家发起一个宠物繁殖的会话，发起方发给服务端后，服务端转发这个请求给对方客户端</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_MATING_ADDPET_REQ.html'>PET_MATING_ADDPET_REQ</a></td><td>0x0000FF19</td><td><a href='./PET_MATING_ADDPET_RES.html'>PET_MATING_ADDPET_RES</a></td><td>0x7000FF19</td><td>放入自己的宠物</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_MATING_CHANGE_REQ.html'>PET_MATING_CHANGE_REQ</a></td><td>0x0000FF34</td><td><a href='./-.html'>-</a></td><td>-</td><td>把对方提交的繁殖宠物道具id发给我，如果我已经处于确认状态，那么需要重新回到未确认状态</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_MATING_CANCEL_REQ.html'>PET_MATING_CANCEL_REQ</a></td><td>0x0000FF35</td><td><a href='./PET_MATING_CANCEL_RES.html'>PET_MATING_CANCEL_RES</a></td><td>0x7000FF35</td><td>取消当前的繁殖</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_MATING_CONFIRM_REQ.html'>PET_MATING_CONFIRM_REQ</a></td><td>0x0000FF20</td><td><a href='./PET_MATING_CONFIRM_RES.html'>PET_MATING_CONFIRM_RES</a></td><td>0x7000FF20</td><td>提交宠物道具后，确认完成，当对方改变宠物道具时，本方会重新回到未确认状态</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_MATING_SESSION_OVER_REQ.html'>PET_MATING_SESSION_OVER_REQ</a></td><td>0x0000FF31</td><td><a href='./-.html'>-</a></td><td>-</td><td>宠物繁殖会话结束</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_IDENTIFY_QUALITY_REQ.html'>PET_IDENTIFY_QUALITY_REQ</a></td><td>0x0000FF21</td><td><a href='./PET_IDENTIFY_QUALITY_RES.html'>PET_IDENTIFY_QUALITY_RES</a></td><td>0x7000FF21</td><td>宠物鉴定</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_GENGUDAN_COMPOUND_REQ.html'>PET_GENGUDAN_COMPOUND_REQ</a></td><td>0x0000FF22</td><td><a href='./PET_GENGUDAN_COMPOUND_RES.html'>PET_GENGUDAN_COMPOUND_RES</a></td><td>0x7000FF22</td><td>根骨丹合成，允许多个低等级的直接合成高等级根骨丹</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_GENGU_UP_REQ.html'>PET_GENGU_UP_REQ</a></td><td>0x0000FF23</td><td><a href='./PET_GENGU_UP_RES.html'>PET_GENGU_UP_RES</a></td><td>0x7000FF23</td><td>提升根骨</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_PET_MERGE_REQ.html'>QUERY_PET_MERGE_REQ</a></td><td>0x0000FF37</td><td><a href='./QUERY_PET_MERGE_RES.html'>QUERY_PET_MERGE_RES</a></td><td>0x7000FF37</td><td>宠物合体</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_MERGE_REQ.html'>PET_MERGE_REQ</a></td><td>0x0000FF24</td><td><a href='./PET_MERGE_RES.html'>PET_MERGE_RES</a></td><td>0x7000FF24</td><td>宠物合体</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_BECHILD_REQ.html'>PET_BECHILD_REQ</a></td><td>0x0000FF25</td><td><a href='./PET_BECHILD_RES.html'>PET_BECHILD_RES</a></td><td>0x7000FF25</td><td>宠物还童</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_PET_FEED_REQ.html'>QUERY_PET_FEED_REQ</a></td><td>0x0000FF38</td><td><a href='./QUERY_PET_FEED_RES.html'>QUERY_PET_FEED_RES</a></td><td>0x7000FF38</td><td>喂养宠物查询</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_FEED_REQ.html'>PET_FEED_REQ</a></td><td>0x0000FF26</td><td><a href='./PET_FEED_RES.html'>PET_FEED_RES</a></td><td>0x7000FF26</td><td>喂养宠物</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_NONTARGET_SKILL_BROADCAST_REQ.html'>PET_NONTARGET_SKILL_BROADCAST_REQ</a></td><td>0x0000FF27</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器向客户端广播技能使用事件，限定为使用者广播区内的所有玩家，不包括使用者本人</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_TARGET_SKILL_BROADCAST_REQ.html'>PET_TARGET_SKILL_BROADCAST_REQ</a></td><td>0x0000FF28</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器向客户端广播技能使用事件，限定为使用者广播区内的所有玩家，不包括使用者本人</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_CALLBACK_REQ.html'>PET_CALLBACK_REQ</a></td><td>0x0000FF29</td><td><a href='./PET_CALLBACK_RES.html'>PET_CALLBACK_RES</a></td><td>0x7000FF29</td><td>收回当前激活的宠物</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_QUERY_BY_ARTICLE_REQ.html'>PET_QUERY_BY_ARTICLE_REQ</a></td><td>0x0000FF30</td><td><a href='./-.html'>-</a></td><td>-</td><td>通过宠物物品id请求宠物</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_RENAME_REQ.html'>PET_RENAME_REQ</a></td><td>0x0000FF32</td><td><a href='./PET_RENAME_RES.html'>PET_RENAME_RES</a></td><td>0x7000FF32</td><td>宠物改名</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_ALLOCATE_POINTS_REQ.html'>PET_ALLOCATE_POINTS_REQ</a></td><td>0x0000FF33</td><td><a href='./PET_ALLOCATE_POINTS_RES.html'>PET_ALLOCATE_POINTS_RES</a></td><td>0x7000FF33</td><td>给宠物分配属性点</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./OPRATE_PET_ALLOCATE_POINTS_REQ.html'>OPRATE_PET_ALLOCATE_POINTS_REQ</a></td><td>0x0000FF89</td><td><a href='./-.html'>-</a></td><td>-</td><td>确认(true)还是取消给宠物分配属性点</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_MATING_PERSONAL_REQ.html'>PET_MATING_PERSONAL_REQ</a></td><td>0x0000FF36</td><td><a href='./PET_MATING_PERSONAL_RES.html'>PET_MATING_PERSONAL_RES</a></td><td>0x7000FF36</td><td>自己创建一个繁殖</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_PET_CHUZHAN_REQ.html'>QUERY_PET_CHUZHAN_REQ</a></td><td>0x0000FF60</td><td><a href='./QUERY_PET_CHUZHAN_RES.html'>QUERY_PET_CHUZHAN_RES</a></td><td>0x7000FF60</td><td>查询背包中哪个宠物出战</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_SEAL_REQ.html'>PET_SEAL_REQ</a></td><td>0x0000FF61</td><td><a href='./-.html'>-</a></td><td>-</td><td>宠物封印</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ARTICLE_OPRATE_RESULT.html'>ARTICLE_OPRATE_RESULT</a></td><td>0x0000FF62</td><td><a href='./-.html'>-</a></td><td>-</td><td>收回宠物时通知客户端</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_REPAIR_REQ.html'>PET_REPAIR_REQ</a></td><td>0x0000FF63</td><td><a href='./PET_REPAIR_RES.html'>PET_REPAIR_RES</a></td><td>0x7000FF63</td><td>宠物炼化</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_REPAIR_REPLACE_REQ.html'>PET_REPAIR_REPLACE_REQ</a></td><td>0x0000FF64</td><td><a href='./PET_REPAIR_REPLACE_RES.html'>PET_REPAIR_REPLACE_RES</a></td><td>0x7000FF64</td><td>宠物炼化替换</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_ALL_ACHIEVEMENT_SERIES_REQ.html'>QUERY_ALL_ACHIEVEMENT_SERIES_REQ</a></td><td>0x0001FF01</td><td><a href='./QUERY_ALL_ACHIEVEMENT_SERIES_RES.html'>QUERY_ALL_ACHIEVEMENT_SERIES_RES</a></td><td>0x7001FF01</td><td>成就所有系列查询</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_ACHIEVEMENT_SERIES_REQ.html'>QUERY_ACHIEVEMENT_SERIES_REQ</a></td><td>0x0001FF02</td><td><a href='./QUERY_ACHIEVEMENT_SERIES_RES.html'>QUERY_ACHIEVEMENT_SERIES_RES</a></td><td>0x7001FF02</td><td>某个成就系列查询</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_ACHIEVEMENT_REQ.html'>QUERY_ACHIEVEMENT_REQ</a></td><td>0x0001FF03</td><td><a href='./QUERY_ACHIEVEMENT_RES.html'>QUERY_ACHIEVEMENT_RES</a></td><td>0x7001FF03</td><td>某个成就查询</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MASTER_AND_PRENTICE_RELATIONSHIP_CHANGED_REQ.html'>MASTER_AND_PRENTICE_RELATIONSHIP_CHANGED_REQ</a></td><td>0x0001FF04</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端师徒关系发生变化，提示客户端重新申请师徒关系信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_EQUIPMENT_JIANDING_REQ.html'>QUERY_EQUIPMENT_JIANDING_REQ</a></td><td>0x0003A000</td><td><a href='./QUERY_EQUIPMENT_JIANDING_RES.html'>QUERY_EQUIPMENT_JIANDING_RES</a></td><td>0x7003A000</td><td>客户端查询装备鉴定需要什么材料，只有装备才能鉴定</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_JIANDING_REQ.html'>EQUIPMENT_JIANDING_REQ</a></td><td>0x0003A001</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端装备鉴定</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NEW_JIANDING_MSG_RES.html'>NEW_JIANDING_MSG_RES</a></td><td>0x7003A0A0</td><td>新鉴定信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEW_JIANDING_OK_REQ.html'>NEW_JIANDING_OK_REQ</a></td><td>0x0003A0A1</td><td><a href='./NEW_JIANDING_OK_RES.html'>NEW_JIANDING_OK_RES</a></td><td>0x7003A0A1</td><td>新的鉴定</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_EQUIPMENT_INSCRIPTION_REQ.html'>QUERY_EQUIPMENT_INSCRIPTION_REQ</a></td><td>0x0003A002</td><td><a href='./QUERY_EQUIPMENT_INSCRIPTION_RES.html'>QUERY_EQUIPMENT_INSCRIPTION_RES</a></td><td>0x7003A002</td><td>客户端查询装备铭刻需要什么材料，只有装备才能铭刻</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_INSCRIPTION_REQ.html'>EQUIPMENT_INSCRIPTION_REQ</a></td><td>0x0003A003</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端装备铭刻</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_INLAY_UUB_REQ.html'>EQUIPMENT_INLAY_UUB_REQ</a></td><td>0x0003A020</td><td><a href='./EQUIPMENT_INLAY_UUB_RES.html'>EQUIPMENT_INLAY_UUB_RES</a></td><td>0x7003A020</td><td>客户端查询装备的宝石镶嵌效果</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_INLAY_REQ.html'>EQUIPMENT_INLAY_REQ</a></td><td>0x0003A004</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端装备镶嵌</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_OUTLAY_REQ.html'>EQUIPMENT_OUTLAY_REQ</a></td><td>0x0003A005</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端装备挖宝石</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_EQUIPMENT_STRONG_REQ.html'>QUERY_EQUIPMENT_STRONG_REQ</a></td><td>0x00F0EEF5</td><td><a href='./QUERY_EQUIPMENT_STRONG_RES.html'>QUERY_EQUIPMENT_STRONG_RES</a></td><td>0x70F0EEF5</td><td>客户端请求服务器，装备强化的各种信息，包括需要什么材料，需要几个，用什么来提高成功率，需要多少手续费等</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_STRONG_REQ.html'>EQUIPMENT_STRONG_REQ</a></td><td>0x00F0EEF6</td><td><a href='./EQUIPMENT_STRONG_RES.html'>EQUIPMENT_STRONG_RES</a></td><td>0x70F0EEF6</td><td>客户端请求服务器，装备强化，服务端发送强化成功消息前必须再次发出QUERY_EQUIPMENT_STRONG_RES通知客户端材料变化</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_EQUIPMENT_PICKSTAR_REQ.html'>QUERY_EQUIPMENT_PICKSTAR_REQ</a></td><td>0x0003A006</td><td><a href='./QUERY_EQUIPMENT_PICKSTAR_RES.html'>QUERY_EQUIPMENT_PICKSTAR_RES</a></td><td>0x7003A006</td><td>装备摘星查询</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_PICKSTAR_REQ.html'>EQUIPMENT_PICKSTAR_REQ</a></td><td>0x0003A007</td><td><a href='./-.html'>-</a></td><td>-</td><td>装备摘星</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_EQUIPMENT_INSERTSTAR_REQ.html'>QUERY_EQUIPMENT_INSERTSTAR_REQ</a></td><td>0x0003A015</td><td><a href='./QUERY_EQUIPMENT_INSERTSTAR_RES.html'>QUERY_EQUIPMENT_INSERTSTAR_RES</a></td><td>0x7003A015</td><td>装备装星查询</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_INSERTSTAR_REQ.html'>EQUIPMENT_INSERTSTAR_REQ</a></td><td>0x0003A008</td><td><a href='./-.html'>-</a></td><td>-</td><td>装备装星</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_DEVELOP_REQ.html'>EQUIPMENT_DEVELOP_REQ</a></td><td>0x0003A009</td><td><a href='./EQUIPMENT_DEVELOP_RES.html'>EQUIPMENT_DEVELOP_RES</a></td><td>0x7003A009</td><td>装备炼化</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_EQUIPMENT_BIND_REQ.html'>QUERY_EQUIPMENT_BIND_REQ</a></td><td>0x0003A010</td><td><a href='./QUERY_EQUIPMENT_BIND_RES.html'>QUERY_EQUIPMENT_BIND_RES</a></td><td>0x7003A010</td><td>查询装备绑定</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_BIND_REQ.html'>EQUIPMENT_BIND_REQ</a></td><td>0x0003A012</td><td><a href='./-.html'>-</a></td><td>-</td><td>装备绑定</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_DEVELOPUP_REQ.html'>EQUIPMENT_DEVELOPUP_REQ</a></td><td>0x0003A013</td><td><a href='./-.html'>-</a></td><td>-</td><td>装备炼化后升级</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ARTICLE_COMPOSE_REQ.html'>ARTICLE_COMPOSE_REQ</a></td><td>0x000000FF</td><td><a href='./-.html'>-</a></td><td>-</td><td>请求物品合成</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_ARTICLE_COMPOSE_REQ.html'>QUERY_ARTICLE_COMPOSE_REQ</a></td><td>0x000002FF</td><td><a href='./QUERY_ARTICLE_COMPOSE_RES.html'>QUERY_ARTICLE_COMPOSE_RES</a></td><td>0x700002FF</td><td>放入想要合成的物品，支持一种物品合成另外一种物品，同一种物品升级颜色，装备合成即多件装备(可以是不同的)合成一件主装备，提升主装备的颜色</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PLAY_ANIMATION_REQ.html'>PLAY_ANIMATION_REQ</a></td><td>0x000003FF</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器想让客户端播放动画，比如装备升级成功播放动画</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_EQUIPMENT_TABLE_REQ.html'>QUERY_EQUIPMENT_TABLE_REQ</a></td><td>0x000000F4</td><td><a href='./QUERY_EQUIPMENT_TABLE_RES.html'>QUERY_EQUIPMENT_TABLE_RES</a></td><td>0x700000F4</td><td>查询主玩家的装备栏</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTIFY_EQUIPMENT_TABLECHANGE_REQ.html'>NOTIFY_EQUIPMENT_TABLECHANGE_REQ</a></td><td>0x000000FA</td><td><a href='./NOTIFY_EQUIPMENT_TABLECHANGE_RES.html'>NOTIFY_EQUIPMENT_TABLECHANGE_RES</a></td><td>0x700000FA</td><td>服务器通知客户端，装备栏发生了变化</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_KNAPSACK_REQ.html'>QUERY_KNAPSACK_REQ</a></td><td>0x000000F5</td><td><a href='./QUERY_KNAPSACK_RES.html'>QUERY_KNAPSACK_RES</a></td><td>0x700000F5</td><td>查询玩家的背包，整理背包也使用这个指令</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_KNAPSACK_FB_REQ.html'>QUERY_KNAPSACK_FB_REQ</a></td><td>0x000001F7</td><td><a href='./QUERY_KNAPSACK_FB_RES.html'>QUERY_KNAPSACK_FB_RES</a></td><td>0x700001F7</td><td>查询玩家的背包，整理背包也使用这个指令</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./Fangbao_KNAPSACK_REQ.html'>Fangbao_KNAPSACK_REQ</a></td><td>0x000001F5</td><td><a href='./Fangbao_KNAPSACK_RES.html'>Fangbao_KNAPSACK_RES</a></td><td>0x700001F5</td><td>查询玩家的防爆背包，删除防爆背包也使用这个指令</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ARTICLE_OPRATION_REQ.html'>ARTICLE_OPRATION_REQ</a></td><td>0x000000F6</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端发起的对背包中物品进行的操作，如使用、装备、丢弃等</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HOOK_USE_PROP_REQ.html'>HOOK_USE_PROP_REQ</a></td><td>0x0000A0F6</td><td><a href='./-.html'>-</a></td><td>-</td><td>挂机使用物品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ARTICLE_OPRATION_MOVE_REQ.html'>ARTICLE_OPRATION_MOVE_REQ</a></td><td>0x000001F6</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端发起的对背包中物品进行移动</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REMOVE_EQUIPMENT_REQ.html'>REMOVE_EQUIPMENT_REQ</a></td><td>0x000000F7</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端发起的卸载装备的请求</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REMOVE_AVATAPROPS_REQ.html'>REMOVE_AVATAPROPS_REQ</a></td><td>0x00000100</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端发起的卸载时装的请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SWITCH_SUIT_REQ.html'>SWITCH_SUIT_REQ</a></td><td>0x000000F8</td><td><a href='./-.html'>-</a></td><td>-</td><td>切换装备套的请求，启用另一套装备</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTIFY_KNAPSACKCHANGE_REQ.html'>NOTIFY_KNAPSACKCHANGE_REQ</a></td><td>0x000000F9</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端，背包某个格子发生了变化</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HELP_WINDOW_REQ.html'>HELP_WINDOW_REQ</a></td><td>0x00000F21</td><td><a href='./HELP_WINDOW_RES.html'>HELP_WINDOW_RES</a></td><td>0x70000F21</td><td>窗口的帮助请求</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./RESOURCE_DATA_REQ.html'>RESOURCE_DATA_REQ</a></td><td>0x00000F20</td><td><a href='./RESOURCE_DATA_RES.html'>RESOURCE_DATA_RES</a></td><td>0x70000F20</td><td>客户端的资源请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./RESOURCE_DATA_REQ_PROCESS.html'>RESOURCE_DATA_REQ_PROCESS</a></td><td>0x00000018</td><td><a href='./-.html'>-</a></td><td>0x70000018</td><td>服务器向客户端发送资源数据包</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_PLAYER_HORSE_REQ.html'>QUERY_PLAYER_HORSE_REQ</a></td><td>0x00000127</td><td><a href='./QUERY_PLAYER_HORSE_RES.html'>QUERY_PLAYER_HORSE_RES</a></td><td>0x70000127</td><td>玩家登陆查询正在骑的马，默认的马的id</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SET_DEFAULT_HORSE_REQ.html'>SET_DEFAULT_HORSE_REQ</a></td><td>0x00000126</td><td><a href='./SET_DEFAULT_HORSE_RES.html'>SET_DEFAULT_HORSE_RES</a></td><td>0x70000126</td><td>玩家设置默认的马</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_HORSE_LIST.html'>QUERY_HORSE_LIST</a></td><td>0x00000118</td><td><a href='./QUERY_HORSE_LIST_RES.html'>QUERY_HORSE_LIST_RES</a></td><td>0x70000118</td><td>客户端向服务器发送查询马匹列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_HORSE_CHANGE_REQ.html'>NOTIFY_HORSE_CHANGE_REQ</a></td><td>0x00000119</td><td><a href='./-.html'>-</a></td><td>-</td><td>根据坐骑心跳，服务器向客户端发送，体力值，体力值影响属性百分比</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HORSE_PUTONOROFF_REQ.html'>HORSE_PUTONOROFF_REQ</a></td><td>0x00000120</td><td><a href='./HORSE_PUTONOROFF_RES.html'>HORSE_PUTONOROFF_RES</a></td><td>0x70000120</td><td>服务器向客户端发送，通知客户端发生了某些事情，客户端根据不同的事情，作不同的现实</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HORSE_RIDE_REQ.html'>HORSE_RIDE_REQ</a></td><td>0x00000121</td><td><a href='./HORSE_RIDE_RES.html'>HORSE_RIDE_RES</a></td><td>0x70000121</td><td>客户端向服务端发送 上马，下马（快捷键)</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_FEED_HORSE_REQ.html'>QUERY_FEED_HORSE_REQ</a></td><td>0x00000125</td><td><a href='./QUERY_FEED_HORSE_RES.html'>QUERY_FEED_HORSE_RES</a></td><td>0x70000125</td><td>查询喂马食物 </td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FEED_HORSE_REQ.html'>FEED_HORSE_REQ</a></td><td>0x00000122</td><td><a href='./FEED_HORSE_RES.html'>FEED_HORSE_RES</a></td><td>0x70000122</td><td>喂马</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_HORSE_EQUIPMENTS_REQ.html'>QUERY_HORSE_EQUIPMENTS_REQ</a></td><td>0x00000123</td><td><a href='./QUERY_HORSE_EQUIPMENTS_RES.html'>QUERY_HORSE_EQUIPMENTS_RES</a></td><td>0x70000123</td><td>查询坐骑装备</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./USE_HORSE_RESULT_REQ.html'>USE_HORSE_RESULT_REQ</a></td><td>0x00000124</td><td><a href='./-.html'>-</a></td><td>-</td><td>使用坐骑道具结果</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_TYPEPLAYER_REQ.html'>GET_TYPEPLAYER_REQ</a></td><td>0x0000EC01</td><td><a href='./GET_TYPEPLAYER_RES.html'>GET_TYPEPLAYER_RES</a></td><td>0x7000EC01</td><td>获取玩家列表,好友或黑名单,或仇人</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_TYPEPLAYER_REQ2.html'>GET_TYPEPLAYER_REQ2</a></td><td>0x0000EC30</td><td><a href='./GET_TYPEPLAYER_RES2.html'>GET_TYPEPLAYER_RES2</a></td><td>0x7000EC30</td><td>腾讯好友列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./TX_GAMELEVEL_RES.html'>TX_GAMELEVEL_RES</a></td><td>0x7000EC31</td><td>腾讯魔钻信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_HOOK_INFO_REQ.html'>PLAYER_HOOK_INFO_REQ</a></td><td>0x0000EC32</td><td><a href='./PLAYER_HOOK_INFO_RES.html'>PLAYER_HOOK_INFO_RES</a></td><td>0x7000EC32</td><td>挂机通知协议，客户端操作通知服务器，服务器也可以主动通知客户端</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_CHATGROUPS_REQ.html'>GET_CHATGROUPS_REQ</a></td><td>0x0000EC22</td><td><a href='./GET_CHATGROUPS_RES.html'>GET_CHATGROUPS_RES</a></td><td>0x7000EC22</td><td>获取玩家的聊天列表组</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_GROUPMEMBERS_REQ.html'>QUERY_GROUPMEMBERS_REQ</a></td><td>0x0000EC17</td><td><a href='./QUERY_GROUPMEMBERS_RES.html'>QUERY_GROUPMEMBERS_RES</a></td><td>0x7000EC17</td><td>获取玩家列表,好友或黑名单,或仇人</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SOCIAL_RELATION_MANAGE_REQ.html'>SOCIAL_RELATION_MANAGE_REQ</a></td><td>0x0000EC02</td><td><a href='./SOCIAL_RELATION_MANAGE_RES.html'>SOCIAL_RELATION_MANAGE_RES</a></td><td>0x7000EC02</td><td>管理玩家的社会关系，包括好友、黑名单和仇人。操作结果用通用提示指令通知</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SOCIAL_REFUSE_FRIEND_REQ.html'>SOCIAL_REFUSE_FRIEND_REQ</a></td><td>0x0000EC08</td><td><a href='./-.html'>-</a></td><td>-</td><td>拒绝添加好友</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SOCIAL_QUERY_PLAYERINFO_REQ.html'>SOCIAL_QUERY_PLAYERINFO_REQ</a></td><td>0x0000EC03</td><td><a href='./SOCIAL_QUERY_PLAYERINFO_RES.html'>SOCIAL_QUERY_PLAYERINFO_RES</a></td><td>0x7000EC03</td><td>管理玩家的社会关系，包括好友、黑名单和仇人。操作结果用通用提示指令通知</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SEND_SOCIETY_OPRATE_REQ.html'>SEND_SOCIETY_OPRATE_REQ</a></td><td>0x0000EC04</td><td><a href='./SEND_SOCIETY_OPRATE_RES.html'>SEND_SOCIETY_OPRATE_RES</a></td><td>0x7000EC04</td><td>发送添加好友请求</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_FRIEND_REQ.html'>QUERY_FRIEND_REQ</a></td><td>0x0000EC05</td><td><a href='./QUERY_FRIEND_RES.html'>QUERY_FRIEND_RES</a></td><td>0x7000EC05</td><td>发送查询好友请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_FRIEND2_REQ.html'>QUERY_FRIEND2_REQ</a></td><td>0x0000EC20</td><td><a href='./QUERY_FRIEND2_RES.html'>QUERY_FRIEND2_RES</a></td><td>0x7000EC20</td><td>发送查询好友请求</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_PERSONNAL_INFO_REQ.html'>QUERY_PERSONNAL_INFO_REQ</a></td><td>0x0000EC07</td><td><a href='./QUERY_PERSONNAL_INFO_RES.html'>QUERY_PERSONNAL_INFO_RES</a></td><td>0x7000EC07</td><td>查询玩家附加信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SET_PLAYERINFO_REQ.html'>SET_PLAYERINFO_REQ</a></td><td>0x0000EC06</td><td><a href='./SET_PLAYERINFO_RES.html'>SET_PLAYERINFO_RES</a></td><td>0x7000EC06</td><td>设置玩家附加信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CREATE_GROUP_REQ.html'>CREATE_GROUP_REQ</a></td><td>0x0000EC09</td><td><a href='./CREATE_GROUP_RES.html'>CREATE_GROUP_RES</a></td><td>0x7000EC09</td><td>玩家创建聊天分组请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DISMISS_GROUP_REQ.html'>DISMISS_GROUP_REQ</a></td><td>0x0000EC10</td><td><a href='./DISMISS_GROUP_RES.html'>DISMISS_GROUP_RES</a></td><td>0x7000EC10</td><td>玩家解散聊天分组请求</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DEL_GROUP_MEMBER_REQ.html'>DEL_GROUP_MEMBER_REQ</a></td><td>0x0000EC11</td><td><a href='./DEL_GROUP_MEMBER_RES.html'>DEL_GROUP_MEMBER_RES</a></td><td>0x7000EC11</td><td>群主把某人从某分组中删除 </td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./LEAVE_GROUP_REQ.html'>LEAVE_GROUP_REQ</a></td><td>0x0000EC12</td><td><a href='./LEAVE_GROUP_RES.html'>LEAVE_GROUP_RES</a></td><td>0x7000EC12</td><td>某人从某分组中离开 </td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./APPLY_GROUP_REQ.html'>APPLY_GROUP_REQ</a></td><td>0x0000EC13</td><td><a href='./APPLY_GROUP_RES.html'>APPLY_GROUP_RES</a></td><td>0x7000EC13</td><td>某人向某分组申请</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GROUPMASTER_APPLY_GROUP_REQ.html'>GROUPMASTER_APPLY_GROUP_REQ</a></td><td>0x0000EC14</td><td><a href='./GROUPMASTER_APPLY_GROUP_RES.html'>GROUPMASTER_APPLY_GROUP_RES</a></td><td>0x7000EC14</td><td>群主向某一玩家申请添加 </td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_GROUP_REQ.html'>QUERY_GROUP_REQ</a></td><td>0x0000EC18</td><td><a href='./QUERY_GROUP_RES.html'>QUERY_GROUP_RES</a></td><td>0x7000EC18</td><td>玩家查询群组 </td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SET_AUTOBACK_REQ.html'>SET_AUTOBACK_REQ</a></td><td>0x0000EC15</td><td><a href='./SET_AUTOBACK_RES.html'>SET_AUTOBACK_RES</a></td><td>0x7000EC15</td><td>设置玩家的状态 （自动回复）</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_AUTOBACK_REQ.html'>QUERY_AUTOBACK_REQ</a></td><td>0x0000EC16</td><td><a href='./QUERY_AUTOBACK_RES.html'>QUERY_AUTOBACK_RES</a></td><td>0x7000EC16</td><td>查询玩家的状态</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CREATE_ZONGPAI_APPLY_REQ.html'>CREATE_ZONGPAI_APPLY_REQ</a></td><td>0x0000F001</td><td><a href='./CREATE_ZONGPAI_APPLY_RES.html'>CREATE_ZONGPAI_APPLY_RES</a></td><td>0x7000F001</td><td>创建宗派申请</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CREATE_ZONGPAI_CHECK_NAME_REQ.html'>CREATE_ZONGPAI_CHECK_NAME_REQ</a></td><td>0x0000F021</td><td><a href='./CREATE_ZONGPAI_CHECK_NAME_RES.html'>CREATE_ZONGPAI_CHECK_NAME_RES</a></td><td>0x7000F021</td><td>创建宗派宗派名验证</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CREATE_ZONGPAI_INPUT_PASSWORD_REQ.html'>CREATE_ZONGPAI_INPUT_PASSWORD_REQ</a></td><td>0x0000F022</td><td><a href='./-.html'>-</a></td><td>-</td><td>创建宗派输入密码</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CREATE_ZONGPAI_SUCCESS_REQ.html'>CREATE_ZONGPAI_SUCCESS_REQ</a></td><td>0x7000F050</td><td><a href='./-.html'>-</a></td><td>-</td><td>创建宗派成功</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ZONGZHU_CHECK_REQ.html'>ZONGZHU_CHECK_REQ</a></td><td>0x0000F113</td><td><a href='./ZONGZHU_CHECK_RES.html'>ZONGZHU_CHECK_RES</a></td><td>0x7000F113</td><td>修改宗派宗旨权限判定</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DISMISS_ZONGPAI_REQ.html'>DISMISS_ZONGPAI_REQ</a></td><td>0x0000F112</td><td><a href='./DISMISS_ZONGPAI_RES.html'>DISMISS_ZONGPAI_RES</a></td><td>0x7000F112</td><td>解散宗派</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./UPDATE_ZONGPAI_DECLARATION_REQ.html'>UPDATE_ZONGPAI_DECLARATION_REQ</a></td><td>0x0000F014</td><td><a href='./UPDATE_ZONGPAI_DECLARATION_RES.html'>UPDATE_ZONGPAI_DECLARATION_RES</a></td><td>0x7000F014</td><td>修改宗派宗旨确定</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./KICK_ZONGPAI_REQ.html'>KICK_ZONGPAI_REQ</a></td><td>0x0000F015</td><td><a href='./-.html'>-</a></td><td>-</td><td>逐出宗派</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DEMISE_ZONGPAI_REQ.html'>DEMISE_ZONGPAI_REQ</a></td><td>0x0000F116</td><td><a href='./DEMISE_ZONGPAI_RES.html'>DEMISE_ZONGPAI_RES</a></td><td>0x7000F116</td><td>宗主禅让</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./LEAVE_ZONGPAI_APPLY_REQ.html'>LEAVE_ZONGPAI_APPLY_REQ</a></td><td>0x0000F017</td><td><a href='./LEAVE_ZONGPAI_APPLY_RES.html'>LEAVE_ZONGPAI_APPLY_RES</a></td><td>0x7000F017</td><td>离开宗派申请判断 </td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ADD_ZONGPAI_REQ.html'>ADD_ZONGPAI_REQ</a></td><td>0x0000F018</td><td><a href='./-.html'>-</a></td><td>-</td><td>添加宗派</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_ZONGPAI_REQ.html'>QUERY_ZONGPAI_REQ</a></td><td>0x0000F019</td><td><a href='./ZONGPAI_INFO_RES.html'>ZONGPAI_INFO_RES</a></td><td>0x7000F019</td><td>查询宗派</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./UNITE_APPLY_REQ.html'>UNITE_APPLY_REQ</a></td><td>0x0000F030</td><td><a href='./UNITE_APPLY_RES.html'>UNITE_APPLY_RES</a></td><td>0x7000F030</td><td>请求结义</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./UNITE_DISAGREE_REQ.html'>UNITE_DISAGREE_REQ</a></td><td>0x0000F031</td><td><a href='./UNITE_AGREEORNO_RES.html'>UNITE_AGREEORNO_RES</a></td><td>0x7000F031</td><td>成员同意还是不同意结义;队长不同意结义</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./UNITE_CONFIRM_REQ.html'>UNITE_CONFIRM_REQ</a></td><td>0x0000F032</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端输入名称等信息后队长同意 </td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./UNITE_EXIT_REQ.html'>UNITE_EXIT_REQ</a></td><td>0x0000F034</td><td><a href='./UNITE_EXIT_RES.html'>UNITE_EXIT_RES</a></td><td>0x7000F034</td><td>退出结义</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./UNITE_DISMISS_RES.html'>UNITE_DISMISS_RES</a></td><td>0x7000F035</td><td>结义解散</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./UNITE_ADD_REQ.html'>UNITE_ADD_REQ</a></td><td>0x0000F036</td><td><a href='./-.html'>-</a></td><td>-</td><td>加入结义  返回unite_finish_res</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./CLOSE_WINDOWS_RES.html'>CLOSE_WINDOWS_RES</a></td><td>0x7000F037</td><td>关闭窗口</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TAKE_MASTER_PRNTICE_REQ.html'>TAKE_MASTER_PRNTICE_REQ</a></td><td>0x0000F101</td><td><a href='./-.html'>-</a></td><td>-</td><td>拜师或是收徒请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./TAKE_MASTER_PRNTICE_RES.html'>TAKE_MASTER_PRNTICE_RES</a></td><td>0x7000F101</td><td>拜师或收徒成功返回信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REBEL_EVICT_REQ.html'>REBEL_EVICT_REQ</a></td><td>0x0000F102</td><td><a href='./REBEL_EVICT_RES.html'>REBEL_EVICT_RES</a></td><td>0x7000F102</td><td>判师或逐徒请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REGESTER_CANCLE_REQ.html'>REGESTER_CANCLE_REQ</a></td><td>0x0000F103</td><td><a href='./-.html'>-</a></td><td>-</td><td>发布拜师，收徒 </td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_MASTERPRENTICE_REQ.html'>QUERY_MASTERPRENTICE_REQ</a></td><td>0x0000F105</td><td><a href='./QUERY_MASTERPRENTICE_RES.html'>QUERY_MASTERPRENTICE_RES</a></td><td>0x7000F105</td><td>查询自己的师傅或徒弟 </td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_MASTERPRENTICEINFO_REQ.html'>QUERY_MASTERPRENTICEINFO_REQ</a></td><td>0x0000F106</td><td><a href='./QUERY_MASTERPRENTICEINFO_RES.html'>QUERY_MASTERPRENTICEINFO_RES</a></td><td>0x7000F106</td><td>查询所有的登记的师徒信息  true 收徒</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ACCEPT_CHUANGONG_ARTICLE_REQ.html'>ACCEPT_CHUANGONG_ARTICLE_REQ</a></td><td>0x0000F200</td><td><a href='./-.html'>-</a></td><td>-</td><td>领取传功石</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./APPLY_CHUANGONG_REQ.html'>APPLY_CHUANGONG_REQ</a></td><td>0x0000F201</td><td><a href='./-.html'>-</a></td><td>-</td><td>申请传功</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./AGREE_CHUANGONG_RES.html'>AGREE_CHUANGONG_RES</a></td><td>0x7000F202</td><td>同意传功后返回传功id</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FINISH_CHUANGONG_REQ.html'>FINISH_CHUANGONG_REQ</a></td><td>0x0000F203</td><td><a href='./FINISH_CHUANGONG_RES.html'>FINISH_CHUANGONG_RES</a></td><td>0x7000F203</td><td>完成传功</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_PLAYER_TITLE_REQ.html'>QUERY_PLAYER_TITLE_REQ</a></td><td>0x0000F300</td><td><a href='./QUERY_PLAYER_TITLE_RES.html'>QUERY_PLAYER_TITLE_RES</a></td><td>0x7000F300</td><td>查询自己所有称号</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SET_DEFAULT_TITLE_REQ.html'>SET_DEFAULT_TITLE_REQ</a></td><td>0x0000F301</td><td><a href='./SET_DEFAULT_TITLE_RES.html'>SET_DEFAULT_TITLE_RES</a></td><td>0x7000F301</td><td>设置默认称号</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_WORLD_MAP_REQ.html'>QUERY_WORLD_MAP_REQ</a></td><td>0x0000F040</td><td><a href='./QUERY_WORLD_MAP_RES.html'>QUERY_WORLD_MAP_RES</a></td><td>0x7000F040</td><td>查询世界地图地域 </td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_WORLD_MAP_AREAMAP_REQ.html'>QUERY_WORLD_MAP_AREAMAP_REQ</a></td><td>0x0000F041</td><td><a href='./QUERY_WORLD_MAP_AREAMAP_RES.html'>QUERY_WORLD_MAP_AREAMAP_RES</a></td><td>0x7000F041</td><td>查询世界地图区域 包含的地图</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_MAP_SEARCH_REQ.html'>QUERY_MAP_SEARCH_REQ</a></td><td>0x0000F048</td><td><a href='./QUERY_MAP_SEARCH_RES.html'>QUERY_MAP_SEARCH_RES</a></td><td>0x7000F048</td><td>查询地图是否被探索过</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_WORLD_MAP_AREAMAP_BY_MAPNAME_REQ.html'>QUERY_WORLD_MAP_AREAMAP_BY_MAPNAME_REQ</a></td><td>0x0000F045</td><td><a href='./QUERY_WORLD_MAP_AREAMAP_BY_MAPNAME_RES.html'>QUERY_WORLD_MAP_AREAMAP_BY_MAPNAME_RES</a></td><td>0x7000F045</td><td>查询世界地图区域根据地图名</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_WORLD_MAP_AREAMAP_BY_PLAYER_REQ.html'>QUERY_WORLD_MAP_AREAMAP_BY_PLAYER_REQ</a></td><td>0x0000F046</td><td><a href='./QUERY_WORLD_MAP_AREAMAP_BY_PLAYER_RES.html'>QUERY_WORLD_MAP_AREAMAP_BY_PLAYER_RES</a></td><td>0x7000F046</td><td>查询人物所在世界地图区域</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_GAMEMAP_NPCMONSTER_REQ.html'>QUERY_GAMEMAP_NPCMONSTER_REQ</a></td><td>0x0000F042</td><td><a href='./QUERY_GAMEMAP_NPCMONSTER_RES.html'>QUERY_GAMEMAP_NPCMONSTER_RES</a></td><td>0x7000F042</td><td>查询某一地图的NPC，怪物位置数据</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_GAMEMAP_MOVE_LIVINGOBJECT_REQ.html'>QUERY_GAMEMAP_MOVE_LIVINGOBJECT_REQ</a></td><td>0x0000F043</td><td><a href='./QUERY_GAMEMAP_MOVE_LIVINGOBJECT_RES.html'>QUERY_GAMEMAP_MOVE_LIVINGOBJECT_RES</a></td><td>0x7000F043</td><td>查询某一地图的动态位置数据</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MARRIAGE_TEST_REQ.html'>MARRIAGE_TEST_REQ</a></td><td>0x00010001</td><td><a href='./-.html'>-</a></td><td>-</td><td>结婚的一个测试协议，因为目前NPC菜单还未做,主要是返回正常点NPC出的菜单项</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_BEQSTART_REQ.html'>MARRIAGE_BEQSTART_REQ</a></td><td>0x00010002</td><td><a href='./-.html'>-</a></td><td>-</td><td>求婚初始化</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MARRIAGE_BEQ_REQ.html'>MARRIAGE_BEQ_REQ</a></td><td>0x00010003</td><td><a href='./-.html'>-</a></td><td>-</td><td>求婚</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_MENU.html'>MARRIAGE_MENU</a></td><td>0x00010004</td><td><a href='./-.html'>-</a></td><td>-</td><td>结婚相关提示框</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MARRIAGE_REQ.html'>MARRIAGE_REQ</a></td><td>0x00010005</td><td><a href='./-.html'>-</a></td><td>-</td><td>结婚</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_ASSIGN_REQ.html'>MARRIAGE_ASSIGN_REQ</a></td><td>0x00010006</td><td><a href='./MARRIAGE_ASSIGN_RES.html'>MARRIAGE_ASSIGN_RES</a></td><td>0x70010006</td><td>布置婚礼,选择婚礼规模</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MARRIAGE_ASSIGN_CHOOSE1_REQ.html'>MARRIAGE_ASSIGN_CHOOSE1_REQ</a></td><td>0x00010007</td><td><a href='./MARRIAGE_ASSIGN_CHOOSE1_RES.html'>MARRIAGE_ASSIGN_CHOOSE1_RES</a></td><td>0x70010007</td><td>选择婚礼规模</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_ASSIGN_CHOOSE2_REQ.html'>MARRIAGE_ASSIGN_CHOOSE2_REQ</a></td><td>0x00010008</td><td><a href='./MARRIAGE_ASSIGN_CHOOSE2_RES.html'>MARRIAGE_ASSIGN_CHOOSE2_RES</a></td><td>0x70010008</td><td>让客户端选择嘉宾</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MARRIAGE_CHOOSE_GUEST_REQ.html'>MARRIAGE_CHOOSE_GUEST_REQ</a></td><td>0x00010009</td><td><a href='./MARRIAGE_CHOOSE_GUEST_RES.html'>MARRIAGE_CHOOSE_GUEST_RES</a></td><td>0x70010009</td><td>选择嘉宾</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_CANCEL_GUEST_REQ.html'>MARRIAGE_CANCEL_GUEST_REQ</a></td><td>0x00010010</td><td><a href='./MARRIAGE_CANCEL_GUEST_RES.html'>MARRIAGE_CANCEL_GUEST_RES</a></td><td>0x70010010</td><td>取消嘉宾</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MARRIAGE_GUEST_OVER_REQ.html'>MARRIAGE_GUEST_OVER_REQ</a></td><td>0x00010011</td><td><a href='./MARRIAGE_GUEST_OVER_RES.html'>MARRIAGE_GUEST_OVER_RES</a></td><td>0x70010011</td><td>选择嘉宾完成,还是取消完成</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_TIME_REQ.html'>MARRIAGE_TIME_REQ</a></td><td>0x00010012</td><td><a href='./MARRIAGE_TIME_RES.html'>MARRIAGE_TIME_RES</a></td><td>0x70010012</td><td>请求设定时间</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MARRIAGE_TIME_OK_REQ.html'>MARRIAGE_TIME_OK_REQ</a></td><td>0x00010013</td><td><a href='./-.html'>-</a></td><td>-</td><td>设定时间</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_FINISH_REQ.html'>MARRIAGE_FINISH_REQ</a></td><td>0x00010014</td><td><a href='./MARRIAGE_FINISH_RES.html'>MARRIAGE_FINISH_RES</a></td><td>0x70010014</td><td>婚礼最后确定</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./MARRIAGE_BEQ_FLOWER_RES.html'>MARRIAGE_BEQ_FLOWER_RES</a></td><td>0x70010015</td><td>求婚花</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_JIAOHUAN_REQ.html'>MARRIAGE_JIAOHUAN_REQ</a></td><td>0x00010016</td><td><a href='./MARRIAGE_JIAOHUAN_RES.html'>MARRIAGE_JIAOHUAN_RES</a></td><td>0x70010016</td><td>结婚交换戒指</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./MARRIAGE_JIAOHUAN2OTHER_RES.html'>MARRIAGE_JIAOHUAN2OTHER_RES</a></td><td>0x70010017</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_CANCEL_REQ.html'>MARRIAGE_CANCEL_REQ</a></td><td>0x00010018</td><td><a href='./MARRIAGE_CANCEL_RES.html'>MARRIAGE_CANCEL_RES</a></td><td>0x70010018</td><td>结婚各个状态退出或取消通知</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MARRIAGE_JOIN_HUNLI_SCREEN_REQ.html'>MARRIAGE_JOIN_HUNLI_SCREEN_REQ</a></td><td>0x00010019</td><td><a href='./MARRIAGE_JOIN_HUNLI_SCREEN_RES.html'>MARRIAGE_JOIN_HUNLI_SCREEN_RES</a></td><td>0x70010019</td><td>参加婚礼</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_MARRIAGE_FRIEND_REQ.html'>GET_MARRIAGE_FRIEND_REQ</a></td><td>0x00010020</td><td><a href='./GET_MARRIAGE_FRIEND_RES.html'>GET_MARRIAGE_FRIEND_RES</a></td><td>0x70010020</td><td>获取可以求婚的并且在线的玩家列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MARRIAGE_TARGET_MENU_REQ.html'>MARRIAGE_TARGET_MENU_REQ</a></td><td>0x00010021</td><td><a href='./MARRIAGE_TARGET_MENU_RES.html'>MARRIAGE_TARGET_MENU_RES</a></td><td>0x70010021</td><td>目标头像送花和糖</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MARRIAGE_DELAYTIME_BEGIN_REQ.html'>MARRIAGE_DELAYTIME_BEGIN_REQ</a></td><td>0x00010022</td><td><a href='./MARRIAGE_DELAYTIME_BEGIN_RES.html'>MARRIAGE_DELAYTIME_BEGIN_RES</a></td><td>0x70010022</td><td>修改延期时间</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MARRIAGE_TARGET_SEND_REQ.html'>MARRIAGE_TARGET_SEND_REQ</a></td><td>0x00010023</td><td><a href='./-.html'>-</a></td><td>-</td><td>选目标送花或糖</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./COUNTRY_COMMISSION_OR_RECALL_REQ.html'>COUNTRY_COMMISSION_OR_RECALL_REQ</a></td><td>0x00000001</td><td><a href='./-.html'>-</a></td><td>-</td><td>国家任命玩家，客户端发任命协议给服务器，服务器传回任命后的结果</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_COUNTRY_COMMISSION_OR_RECALL_REQ.html'>QUERY_COUNTRY_COMMISSION_OR_RECALL_REQ</a></td><td>0x00000AA2</td><td><a href='./QUERY_COUNTRY_COMMISSION_OR_RECALL_RES.html'>QUERY_COUNTRY_COMMISSION_OR_RECALL_RES</a></td><td>0x70000AA2</td><td>国家任命玩家查询，客户端发任命协议给服务器，服务器传回任命后的结果，当客户端收到RES后弹出任命UI</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_COUNTRY_QIUJIN_JINYAN_REQ.html'>QUERY_COUNTRY_QIUJIN_JINYAN_REQ</a></td><td>0x00000AAA</td><td><a href='./QUERY_COUNTRY_QIUJIN_JINYAN_RES.html'>QUERY_COUNTRY_QIUJIN_JINYAN_RES</a></td><td>0x70000AAA</td><td>查询本国囚禁和禁言的人，在线</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./COUNTRY_HONOURSOR_OR_CANCEL_REQ.html'>COUNTRY_HONOURSOR_OR_CANCEL_REQ</a></td><td>0x00000002</td><td><a href='./-.html'>-</a></td><td>-</td><td>国家授勋玩家，客户端发授勋协议给服务器，服务器传回授勋后的结果</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./COUNTRY_COMMEND_OR_CANCEL_REQ.html'>COUNTRY_COMMEND_OR_CANCEL_REQ</a></td><td>0x00000003</td><td><a href='./-.html'>-</a></td><td>-</td><td>国家表彰玩家，客户端发表彰协议给服务器，服务器传回表彰后的结果</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_COUNTRY_VOTE_REQ.html'>QUERY_COUNTRY_VOTE_REQ</a></td><td>0x00000AA6</td><td><a href='./QUERY_COUNTRY_VOTE_RES.html'>QUERY_COUNTRY_VOTE_RES</a></td><td>0x70000AA6</td><td>投票，客户端发投票协议给服务器，服务器传回投票后的结果</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./COUNTRY_VOTE_REQ.html'>COUNTRY_VOTE_REQ</a></td><td>0x00000006</td><td><a href='./-.html'>-</a></td><td>-</td><td>投票，客户端发投票协议给服务器</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./COUNTRY_WANGZHEZHIYIN_REQ.html'>COUNTRY_WANGZHEZHIYIN_REQ</a></td><td>0x00000007</td><td><a href='./COUNTRY_WANGZHEZHIYIN_RES.html'>COUNTRY_WANGZHEZHIYIN_RES</a></td><td>0x70000007</td><td>服务器端给客户端传回res，客户端收到res后打开地图，玩家可以点击地图上的标记传送到该地图相应位置</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./COUNTRY_QIUJIN_REQ.html'>COUNTRY_QIUJIN_REQ</a></td><td>0xA0000008</td><td><a href='./-.html'>-</a></td><td>-</td><td>囚禁</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./COUNTRY_JINYAN_REQ.html'>COUNTRY_JINYAN_REQ</a></td><td>0xA0000009</td><td><a href='./-.html'>-</a></td><td>-</td><td>禁言</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./COUNTRY_JIECHU_QIUJIN_REQ.html'>COUNTRY_JIECHU_QIUJIN_REQ</a></td><td>0xA000006</td><td><a href='./-.html'>-</a></td><td>-</td><td>解除囚禁</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./COUNTRY_JIECHU_JINYAN_REQ.html'>COUNTRY_JIECHU_JINYAN_REQ</a></td><td>0xA0000010</td><td><a href='./-.html'>-</a></td><td>-</td><td>解除禁言</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_COUNTRY_MAIN_PAGE_REQ.html'>QUERY_COUNTRY_MAIN_PAGE_REQ</a></td><td>0x00000AA0</td><td><a href='./QUERY_COUNTRY_MAIN_PAGE_RES.html'>QUERY_COUNTRY_MAIN_PAGE_RES</a></td><td>0x70000AA0</td><td>国家主页面查询，服务器传回结果，当客户端收到RES后弹出国家主页面UI</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./COUNTRY_LINGQU_FENGLU_REQ.html'>COUNTRY_LINGQU_FENGLU_REQ</a></td><td>0x00000AA3</td><td><a href='./-.html'>-</a></td><td>-</td><td>领取俸禄</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./COUNTRY_FAFANG_FENGLU_REQ.html'>COUNTRY_FAFANG_FENGLU_REQ</a></td><td>0x00000AA8</td><td><a href='./-.html'>-</a></td><td>-</td><td>发放俸禄</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_COUNTRY_WANGZHEZHIYIN_REQ.html'>QUERY_COUNTRY_WANGZHEZHIYIN_REQ</a></td><td>0x00000AA4</td><td><a href='./QUERY_COUNTRY_WANGZHEZHIYIN_RES.html'>QUERY_COUNTRY_WANGZHEZHIYIN_RES</a></td><td>0x70000AA4</td><td>国家王者之印使用次数</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_COUNTRY_YULINJUN_REQ.html'>QUERY_COUNTRY_YULINJUN_REQ</a></td><td>0x00000AA5</td><td><a href='./QUERY_COUNTRY_YULINJUN_RES.html'>QUERY_COUNTRY_YULINJUN_RES</a></td><td>0x70000AA5</td><td>国家御林卫队</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_COUNTRY_JINKU_REQ.html'>QUERY_COUNTRY_JINKU_REQ</a></td><td>0x00000AA7</td><td><a href='./QUERY_COUNTRY_JINKU_RES.html'>QUERY_COUNTRY_JINKU_RES</a></td><td>0x70000AA7</td><td>查询国家金库</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_COUNTRY_GONGGAO_REQ.html'>QUERY_COUNTRY_GONGGAO_REQ</a></td><td>0x00000AB1</td><td><a href='./QUERY_COUNTRY_GONGGAO_RES.html'>QUERY_COUNTRY_GONGGAO_RES</a></td><td>0x70000AB1</td><td>修改国家公告</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_QUERY_FAERYLIST_REQ.html'>CAVE_QUERY_FAERYLIST_REQ</a></td><td>0x0F000001</td><td><a href='./CAVE_QUERY_FAERYLIST_RES.html'>CAVE_QUERY_FAERYLIST_RES</a></td><td>0x8F000001</td><td>查看所有仙境列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_OPTION_CAVE_REQ.html'>CAVE_OPTION_CAVE_REQ</a></td><td>0x0F000002</td><td><a href='./CAVE_OPTION_CAVE_RES.html'>CAVE_OPTION_CAVE_RES</a></td><td>0x8F000002</td><td>仙府操作</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_LVUP_BUILDING_REQ.html'>CAVE_LVUP_BUILDING_REQ</a></td><td>0x0F000003</td><td><a href='./CAVE_LVUP_BUILDING_RES.html'>CAVE_LVUP_BUILDING_RES</a></td><td>0x8F000003</td><td>仙府内建筑升级</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_DOOR_OPTION_REQ.html'>CAVE_DOOR_OPTION_REQ</a></td><td>0x0F000004</td><td><a href='./CAVE_DOOR_OPTION_RES.html'>CAVE_DOOR_OPTION_RES</a></td><td>0x8F000004</td><td>对门的操作</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_ASSART_FIELD_REQ.html'>CAVE_ASSART_FIELD_REQ</a></td><td>0x0F000005</td><td><a href='./CAVE_ASSART_FIELD_RES.html'>CAVE_ASSART_FIELD_RES</a></td><td>0x8F000005</td><td>开垦田地</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_QUERY_ALL_PLANT_REQ.html'>CAVE_QUERY_ALL_PLANT_REQ</a></td><td>0x0F000006</td><td><a href='./CAVE_QUERY_ALL_PLANT_RES.html'>CAVE_QUERY_ALL_PLANT_RES</a></td><td>0x8F000006</td><td>查询可种植列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_PLANT_REQ.html'>CAVE_PLANT_REQ</a></td><td>0x0F000007</td><td><a href='./CAVE_PLANT_RES.html'>CAVE_PLANT_RES</a></td><td>0x8F000007</td><td>种植</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_HARVEST_PLANT_REQ.html'>CAVE_HARVEST_PLANT_REQ</a></td><td>0x0F000008</td><td><a href='./CAVE_HARVEST_PLANT_RES.html'>CAVE_HARVEST_PLANT_RES</a></td><td>0x8F000008</td><td>收获</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_CONVERT_PLANT_REQ.html'>CAVE_CONVERT_PLANT_REQ</a></td><td>0x0F000009</td><td><a href='./CAVE_CONVERT_PLANT_RES.html'>CAVE_CONVERT_PLANT_RES</a></td><td>0x8F000009</td><td>兑换果实</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_STORE_SIZEUP_REQ.html'>CAVE_STORE_SIZEUP_REQ</a></td><td>0x0F000010</td><td><a href='./CAVE_STORE_SIZEUP_RES.html'>CAVE_STORE_SIZEUP_RES</a></td><td>0x8F000010</td><td>提高仓库容量</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_STORE_PET_REQ.html'>CAVE_STORE_PET_REQ</a></td><td>0x0F000011</td><td><a href='./CAVE_STORE_PET_RES.html'>CAVE_STORE_PET_RES</a></td><td>0x8F000011</td><td>宠物存放/挂机</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_TAKEOUT_PET_REQ.html'>CAVE_TAKEOUT_PET_REQ</a></td><td>0x0F000012</td><td><a href='./CAVE_TAKEOUT_PET_RES.html'>CAVE_TAKEOUT_PET_RES</a></td><td>0x8F000012</td><td>拿出宠物</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_CHECK_PLANT_REQ.html'>CAVE_CHECK_PLANT_REQ</a></td><td>0x0F000013</td><td><a href='./CAVE_CHECK_PLANT_RES.html'>CAVE_CHECK_PLANT_RES</a></td><td>0x8F000013</td><td>查看种植物成熟时间</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_CANCEL_SCHEDULE_REQ.html'>CAVE_CANCEL_SCHEDULE_REQ</a></td><td>0x0F000014</td><td><a href='./CAVE_CANCEL_SCHEDULE_RES.html'>CAVE_CANCEL_SCHEDULE_RES</a></td><td>0x8F000014</td><td>取消工作</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_INFO_REQ.html'>CAVE_INFO_REQ</a></td><td>0x0F000015</td><td><a href='./CAVE_INFO_RES.html'>CAVE_INFO_RES</a></td><td>0x8F000015</td><td>查看仙府信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_ELECTRIC_ATTACK_REQ.html'>CAVE_ELECTRIC_ATTACK_REQ</a></td><td>0x0F000016</td><td><a href='./CAVE_ELECTRIC_ATTACK_RES.html'>CAVE_ELECTRIC_ATTACK_RES</a></td><td>0x8F000016</td><td>点击周围玩家</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_LEVEL_UP_MOTICE_REQ.html'>CAVE_LEVEL_UP_MOTICE_REQ</a></td><td>0x0F000017</td><td><a href='./CAVE_LEVEL_UP_MOTICE_RES.html'>CAVE_LEVEL_UP_MOTICE_RES</a></td><td>0x8F000017</td><td>向客户端弹出升级提示界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_QUERY_SCHEDULE_REQ.html'>CAVE_QUERY_SCHEDULE_REQ</a></td><td>0x0F000018</td><td><a href='./CAVE_QUERY_SCHEDULE_RES.html'>CAVE_QUERY_SCHEDULE_RES</a></td><td>0x8F000018</td><td>工作进度</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_QUERY_PETSTORE_REQ.html'>CAVE_QUERY_PETSTORE_REQ</a></td><td>0x0F000019</td><td><a href='./CAVE_QUERY_PETSTORE_RES.html'>CAVE_QUERY_PETSTORE_RES</a></td><td>0x8F000019</td><td>查看宠物存放仓库信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_QUERY_PETHOOK_REQ.html'>CAVE_QUERY_PETHOOK_REQ</a></td><td>0x0F000020</td><td><a href='./CAVE_QUERY_PETHOOK_RES.html'>CAVE_QUERY_PETHOOK_RES</a></td><td>0x8F000020</td><td>查看宠物挂机拦信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_OPEN_COUNTYLIST_REQ.html'>CAVE_OPEN_COUNTYLIST_REQ</a></td><td>0x0F000021</td><td><a href='./CAVE_OPEN_COUNTYLIST_RES.html'>CAVE_OPEN_COUNTYLIST_RES</a></td><td>0x8F000021</td><td>打开国家列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_QUERY_SELFFAERY_REQ.html'>CAVE_QUERY_SELFFAERY_REQ</a></td><td>0x0F000022</td><td><a href='./CAVE_QUERY_SELFFAERY_RES.html'>CAVE_QUERY_SELFFAERY_RES</a></td><td>0x8F000022</td><td>查看自己洞府所在仙境</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_NOTICE_CONVERTARTICLE_REQ.html'>CAVE_NOTICE_CONVERTARTICLE_REQ</a></td><td>0x0F000023</td><td><a href='./CAVE_NOTICE_CONVERTARTICLE_RES.html'>CAVE_NOTICE_CONVERTARTICLE_RES</a></td><td>0x8F000023</td><td>通知客户端打开果实兑换资源界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_HOOK_PET_REQ.html'>CAVE_HOOK_PET_REQ</a></td><td>0x0F000024</td><td><a href='./CAVE_HOOK_PET_RES.html'>CAVE_HOOK_PET_RES</a></td><td>0x8F000024</td><td>宠物存放/挂机</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_FIND_SELFCAVE_REQ.html'>CAVE_FIND_SELFCAVE_REQ</a></td><td>0x0F000025</td><td><a href='./CAVE_FIND_SELFCAVE_RES.html'>CAVE_FIND_SELFCAVE_RES</a></td><td>0x8F000025</td><td>查询自己庄园所在的坐标--门牌的坐标</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_LEAVE_CAVE_REQ.html'>CAVE_LEAVE_CAVE_REQ</a></td><td>0x0F000026</td><td><a href='./CAVE_LEAVE_CAVE_RES.html'>CAVE_LEAVE_CAVE_RES</a></td><td>0x8F000026</td><td>离开庄园</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_SHOW_TOOLBAR_REQ.html'>CAVE_SHOW_TOOLBAR_REQ</a></td><td>0x0F000027</td><td><a href='./CAVE_SHOW_TOOLBAR_RES.html'>CAVE_SHOW_TOOLBAR_RES</a></td><td>0x8F000027</td><td>弹出工具栏</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_QUERY_EXCHANGE_REQ.html'>CAVE_QUERY_EXCHANGE_REQ</a></td><td>0x0F00002A</td><td><a href='./CAVE_QUERY_EXCHANGE_RES.html'>CAVE_QUERY_EXCHANGE_RES</a></td><td>0x8F00002A</td><td>查看兑换资源数量</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_ACCELERATE_REQ.html'>CAVE_ACCELERATE_REQ</a></td><td>0x0F00002B</td><td><a href='./CAVE_ACCELERATE_RES.html'>CAVE_ACCELERATE_RES</a></td><td>0x8F00002B</td><td>加速</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_SIMPLE_REQ.html'>CAVE_SIMPLE_REQ</a></td><td>0x0F00002C</td><td><a href='./CAVE_SIMPLE_RES.html'>CAVE_SIMPLE_RES</a></td><td>0x8F00002C</td><td>查看仙府简信息,是否有仙府,仙府状态</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_READ_DYNAMIC_REQ.html'>CAVE_READ_DYNAMIC_REQ</a></td><td>0x0F00002D</td><td><a href='./CAVE_READ_DYNAMIC_RES.html'>CAVE_READ_DYNAMIC_RES</a></td><td>0x8F00002D</td><td>通知服务器玩家阅读了动态</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_DETAILED_INFO_REQ.html'>CAVE_DETAILED_INFO_REQ</a></td><td>0x0F00002E</td><td><a href='./CAVE_DETAILED_INFO_RES.html'>CAVE_DETAILED_INFO_RES</a></td><td>0x8F00002E</td><td>获得仙府的详细信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_DYNAMIC_REQ.html'>CAVE_DYNAMIC_REQ</a></td><td>0x0F000050</td><td><a href='./CAVE_DYNAMIC_RES.html'>CAVE_DYNAMIC_RES</a></td><td>0x8F000050</td><td>获取仙府动态</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_DYNAMIC_NOTICE_REQ.html'>CAVE_DYNAMIC_NOTICE_REQ</a></td><td>0x0F000051</td><td><a href='./CAVE_DYNAMIC_NOTICE_RES.html'>CAVE_DYNAMIC_NOTICE_RES</a></td><td>0x8F000051</td><td>仙府动态数量提示</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CAVE_QUERY_PLANT_REQ.html'>CAVE_QUERY_PLANT_REQ</a></td><td>0x0F000052</td><td><a href='./CAVE_QUERY_PLANT_RES.html'>CAVE_QUERY_PLANT_RES</a></td><td>0x8F000052</td><td>查询可种植列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAVE_QUERY_RESOURCECOLLECTIO_REQ.html'>CAVE_QUERY_RESOURCECOLLECTIO_REQ</a></td><td>0x0F000053</td><td><a href='./CAVE_QUERY_RESOURCECOLLECTION_RES.html'>CAVE_QUERY_RESOURCECOLLECTION_RES</a></td><td>0x8F000053</td><td>获得仙府资源</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOURN_LEVELUP_REQ.html'>BOURN_LEVELUP_REQ</a></td><td>0x0F100001</td><td><a href='./BOURN_LEVELUP_RES.html'>BOURN_LEVELUP_RES</a></td><td>0x8F100001</td><td>境界升级</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOURN_ZEZEN_REQ.html'>BOURN_ZEZEN_REQ</a></td><td>0x0F100002</td><td><a href='./BOURN_ZEZEN_RES.html'>BOURN_ZEZEN_RES</a></td><td>0x8F100002</td><td>境界打坐</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOURN_REFRESH_TASK_REQ.html'>BOURN_REFRESH_TASK_REQ</a></td><td>0x0F100003</td><td><a href='./BOURN_REFRESH_TASK_RES.html'>BOURN_REFRESH_TASK_RES</a></td><td>0x8F100003</td><td>刷新任务</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOURN_INFO_REQ.html'>BOURN_INFO_REQ</a></td><td>0x0F100004</td><td><a href='./BOURN_INFO_RES.html'>BOURN_INFO_RES</a></td><td>0x8F100004</td><td>查看境界信息 </td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOURN_OF_TRAINING_REQ.html'>BOURN_OF_TRAINING_REQ</a></td><td>0x0F100005</td><td><a href='./BOURN_OF_TRAINING_RES.html'>BOURN_OF_TRAINING_RES</a></td><td>0x8F100005</td><td>境界修炼</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOURN_OF_PURIFY_REQ.html'>BOURN_OF_PURIFY_REQ</a></td><td>0x0F100006</td><td><a href='./BOURN_OF_PURIFY_RES.html'>BOURN_OF_PURIFY_RES</a></td><td>0x8F100006</td><td>杂念</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_CLIENT_DELIVER_TASK_REQ.html'>NOTICE_CLIENT_DELIVER_TASK_REQ</a></td><td>0x0F100007</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端交付某个任务</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTICE_DELIVER_BOURN_TASK_NUM_REQ.html'>NOTICE_DELIVER_BOURN_TASK_NUM_REQ</a></td><td>0x0F100008</td><td><a href='./-.html'>-</a></td><td>-</td><td>完成的杂念任务</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_SEAL_REQ.html'>QUERY_SEAL_REQ</a></td><td>0x0F000128</td><td><a href='./QUERY_SEAL_RES.html'>QUERY_SEAL_RES</a></td><td>0x8F000128</td><td>查询封印</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TAKE_SEAL_TASK_REQ.html'>TAKE_SEAL_TASK_REQ</a></td><td>0x0F000130</td><td><a href='./-.html'>-</a></td><td>-</td><td>接取封印任务</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_FLOPNPC_REQ.html'>QUERY_FLOPNPC_REQ</a></td><td>0x0F000129</td><td><a href='./QUERY_FLOPNPC_RES.html'>QUERY_FLOPNPC_RES</a></td><td>0x8F000129</td><td>查询掉落NPC</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./LEVELUP_REQ.html'>LEVELUP_REQ</a></td><td>0x0F000126</td><td><a href='./-.html'>-</a></td><td>-</td><td>玩家主动升级</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_SHOW_TOOLBAR_CHUJIJIANDING_REQ.html'>EQUIPMENT_SHOW_TOOLBAR_CHUJIJIANDING_REQ</a></td><td>0x0F000028</td><td><a href='./-.html'>-</a></td><td>-</td><td>弹出初级鉴定</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIJIANDING_REQ.html'>EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIJIANDING_REQ</a></td><td>0x0F000029</td><td><a href='./-.html'>-</a></td><td>-</td><td>弹出装备鉴定</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIMINGKE_REQ.html'>EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIMINGKE_REQ</a></td><td>0x0F000030</td><td><a href='./-.html'>-</a></td><td>-</td><td>弹出装备铭刻</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIFUXING_REQ.html'>EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIFUXING_REQ</a></td><td>0x0F000031</td><td><a href='./-.html'>-</a></td><td>-</td><td>弹出装备附星</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIBANGDING_REQ.html'>EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIBANGDING_REQ</a></td><td>0x0F000032</td><td><a href='./-.html'>-</a></td><td>-</td><td>弹出装备绑定</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIZHAIXING_REQ.html'>EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIZHAIXING_REQ</a></td><td>0x0F000033</td><td><a href='./-.html'>-</a></td><td>-</td><td>弹出装备摘星</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEISHENGJI_REQ.html'>EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEISHENGJI_REQ</a></td><td>0x0F000034</td><td><a href='./-.html'>-</a></td><td>-</td><td>弹出装备升级</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_SHOW_TOOLBAR_BAOSHIHECHENG_REQ.html'>EQUIPMENT_SHOW_TOOLBAR_BAOSHIHECHENG_REQ</a></td><td>0x0F000035</td><td><a href='./-.html'>-</a></td><td>-</td><td>弹出装备镶嵌</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EQUIPMENT_SHOW_TOOLBAR_BAOSHIXIANGQIAN_REQ.html'>EQUIPMENT_SHOW_TOOLBAR_BAOSHIXIANGQIAN_REQ</a></td><td>0x0F000036</td><td><a href='./-.html'>-</a></td><td>-</td><td>弹出宝石镶嵌</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EQUIPMENT_SHOW_TOOLBAR_BAOSHIWAQU_REQ.html'>EQUIPMENT_SHOW_TOOLBAR_BAOSHIWAQU_REQ</a></td><td>0x0F000037</td><td><a href='./-.html'>-</a></td><td>-</td><td>弹出宝石挖取</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FUCK_TRANSPORT_GAME.html'>FUCK_TRANSPORT_GAME</a></td><td>0x0F000038</td><td><a href='./-.html'>-</a></td><td>-</td><td>跳地图</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./FIND_WAY2TASK_RES.html'>FIND_WAY2TASK_RES</a></td><td>0x0F200001</td><td>寻路，指定某个任务</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./ENTITY_MSG.html'>ENTITY_MSG</a></td><td>0x0F200002</td><td>具体entity对象的描述，加一些按钮</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./SKILL_MSG.html'>SKILL_MSG</a></td><td>0x0F200003</td><td>技能描述</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./PROP_MSG.html'>PROP_MSG</a></td><td>0x0F200005</td><td>具体prop对象的描述，加一些按钮</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NEXT_TASK_OPEN.html'>NEXT_TASK_OPEN</a></td><td>0x0F200004</td><td>下个任务</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SHOW_MEMBER_FORLUCK_REQ.html'>SHOW_MEMBER_FORLUCK_REQ</a></td><td>0x0F300003</td><td><a href='./-.html'>-</a></td><td>-</td><td>弹出分配树上果实的菜单</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HANDOUT_FORLUCK_TREE_REQ.html'>HANDOUT_FORLUCK_TREE_REQ</a></td><td>0x0F300004</td><td><a href='./HANDOUT_FORLUCK_TREE_RES.html'>HANDOUT_FORLUCK_TREE_RES</a></td><td>0x8F300004</td><td>分配树上的祝福果</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CHECK_STORAGE_FORLUCK_REQ.html'>CHECK_STORAGE_FORLUCK_REQ</a></td><td>0x0F300006</td><td><a href='./-.html'>-</a></td><td>-</td><td>查看仓库中的果实-通过OPTION触发：客户端只接收</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HANDOUT_FORLUCK_STORAGE_REQ.html'>HANDOUT_FORLUCK_STORAGE_REQ</a></td><td>0x0F300007</td><td><a href='./HANDOUT_FORLUCK_STORAGE_RES.html'>HANDOUT_FORLUCK_STORAGE_RES</a></td><td>0x8F300007</td><td>分配仓库的祝福果</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_CLIENT_EXCHANGE_FORLUCK_REQ.html'>NOTICE_CLIENT_EXCHANGE_FORLUCK_REQ</a></td><td>0x0F300008</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端弹出兑换祝福果窗口</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ACTIVITY_FORLUCK_FRUIT_EXCHANGE_REQ.html'>ACTIVITY_FORLUCK_FRUIT_EXCHANGE_REQ</a></td><td>0x0F300009</td><td><a href='./-.html'>-</a></td><td>-</td><td>兑换祝福果</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOTTLE_OPEN_INFO_REQ.html'>BOTTLE_OPEN_INFO_REQ</a></td><td>0x0F300010</td><td><a href='./-.html'>-</a></td><td>-</td><td>使用瓶子后查看瓶子中的信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOTTLE_OPEN_REQ.html'>BOTTLE_OPEN_REQ</a></td><td>0x0F300012</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端发出开瓶子信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOTTLE_PICK_ARTICLE_REQ.html'>BOTTLE_PICK_ARTICLE_REQ</a></td><td>0x0F300013</td><td><a href='./-.html'>-</a></td><td>-</td><td>得到瓶子里开启的物品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CLIFFORD_START_REQ.html'>CLIFFORD_START_REQ</a></td><td>0x0F300016</td><td><a href='./CLIFFORD_START_RES.html'>CLIFFORD_START_RES</a></td><td>0x8F300016</td><td>祈福开始或重新祈福</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CLIFFORD_REQ.html'>CLIFFORD_REQ</a></td><td>0x0F300017</td><td><a href='./-.html'>-</a></td><td>-</td><td>祈福开始或重新祈福</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CLIFFORD_LOOKOVER_REQ.html'>CLIFFORD_LOOKOVER_REQ</a></td><td>0x0F300018</td><td><a href='./-.html'>-</a></td><td>-</td><td>查看未祈福的剩余物品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TOURNAMENT_TIME_REQ.html'>TOURNAMENT_TIME_REQ</a></td><td>0x0F300019</td><td><a href='./-.html'>-</a></td><td>-</td><td>比武时间显示</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TOURNAMENT_START_REQ.html'>TOURNAMENT_START_REQ</a></td><td>0x0F300020</td><td><a href='./-.html'>-</a></td><td>-</td><td>比武回合开始</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TOURNAMENT_END_REQ.html'>TOURNAMENT_END_REQ</a></td><td>0x0F300021</td><td><a href='./-.html'>-</a></td><td>-</td><td>比武回合结束</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TOURNAMENT_QUERY_SIDE_REQ.html'>TOURNAMENT_QUERY_SIDE_REQ</a></td><td>0x0F300025</td><td><a href='./-.html'>-</a></td><td>-</td><td>比武双方通知</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TOURNAMENT_RANK_REQ.html'>TOURNAMENT_RANK_REQ</a></td><td>0x0F300026</td><td><a href='./-.html'>-</a></td><td>-</td><td>比武排名面板</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TOURNAMENT_REWARD_REQ.html'>TOURNAMENT_REWARD_REQ</a></td><td>0x0F300027</td><td><a href='./TOURNAMENT_REWARD_RES.html'>TOURNAMENT_REWARD_RES</a></td><td>0x8F300027</td><td>比武奖励面板</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TOURNAMENT_REWARD_SELF_REQ.html'>TOURNAMENT_REWARD_SELF_REQ</a></td><td>0x0F300128</td><td><a href='./TOURNAMENT_REWARD_SELF_RES.html'>TOURNAMENT_REWARD_SELF_RES</a></td><td>0x8F300128</td><td>个人比武奖励面板</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TOURNAMENT_TAKE_REWARD_REQ.html'>TOURNAMENT_TAKE_REWARD_REQ</a></td><td>0x0F30002A</td><td><a href='./-.html'>-</a></td><td>-</td><td>比武领取奖励</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CONTINUE_KILL_REQ.html'>CONTINUE_KILL_REQ</a></td><td>0x0F30002B</td><td><a href='./-.html'>-</a></td><td>-</td><td>连斩信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SEND_VILLAGE_CIRCLE_REQ.html'>SEND_VILLAGE_CIRCLE_REQ</a></td><td>0x0F300022</td><td><a href='./-.html'>-</a></td><td>-</td><td>发送村庄战争圆圈信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SEND_VILLAGE_CIRCLE_DIS_REQ.html'>SEND_VILLAGE_CIRCLE_DIS_REQ</a></td><td>0x0F300023</td><td><a href='./-.html'>-</a></td><td>-</td><td>发送村庄战争圆圈消失消息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SEND_VILLAGE_STATE_REQ.html'>SEND_VILLAGE_STATE_REQ</a></td><td>0x0F100025</td><td><a href='./-.html'>-</a></td><td>-</td><td>发送村庄战争状态信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SEND_CITYFIGHT_STATE_REQ.html'>SEND_CITYFIGHT_STATE_REQ</a></td><td>0x0F100026</td><td><a href='./-.html'>-</a></td><td>-</td><td>发送城市战争状态信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ACTIVITY_FARMING_PLATE_REQ.html'>ACTIVITY_FARMING_PLATE_REQ</a></td><td>0x0F300014</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端弹出圆盘</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ACTIVITY_FARMING_PLATE_RESPONSE_REQ.html'>ACTIVITY_FARMING_PLATE_RESPONSE_REQ</a></td><td>0x0F300015</td><td><a href='./-.html'>-</a></td><td>-</td><td>反馈给服务器，转盘结束</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./ENTER_GETACTIVITY_RES.html'>ENTER_GETACTIVITY_RES</a></td><td>0x700F0000</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_FATEACTIVITY_REQ.html'>GET_FATEACTIVITY_REQ</a></td><td>0x000F0001</td><td><a href='./GET_FATEACTIVITY_RES.html'>GET_FATEACTIVITY_RES</a></td><td>0x700F0001</td><td>得到进行中的仙缘活动</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHOOSE_ACTIVITY_REQ.html'>CHOOSE_ACTIVITY_REQ</a></td><td>0x000F0002</td><td><a href='./CHOOSE_ACTIVITY_RES.html'>CHOOSE_ACTIVITY_RES</a></td><td>0x700F0002</td><td>选择活动</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REAL_CHOOSE_ACTIVITY_REQ.html'>REAL_CHOOSE_ACTIVITY_REQ</a></td><td>0x000F0003</td><td><a href='./REAL_CHOOSE_ACTIVITY_RES.html'>REAL_CHOOSE_ACTIVITY_RES</a></td><td>0x700F0003</td><td>真正选择活动</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./AGREE_ACTIVITY_REQ.html'>AGREE_ACTIVITY_REQ</a></td><td>0x000F0004</td><td><a href='./-.html'>-</a></td><td>-</td><td>被邀请方同意活动</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DISAGREE_ACTIVITY_REQ.html'>DISAGREE_ACTIVITY_REQ</a></td><td>0x000F0005</td><td><a href='./-.html'>-</a></td><td>-</td><td>被邀请方不同意活动</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GIVEUP_ACTIVITY_REQ.html'>GIVEUP_ACTIVITY_REQ</a></td><td>0x000F0006</td><td><a href='./GIVEUP_ACTIVITY_RES.html'>GIVEUP_ACTIVITY_RES</a></td><td>0x700F0006</td><td>放弃活动</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./BEGIN_ACTIVITY_RES.html'>BEGIN_ACTIVITY_RES</a></td><td>0x700F0007</td><td>开始活动</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./FINISH_ACTIVITY_RES.html'>FINISH_ACTIVITY_RES</a></td><td>0x700F0008</td><td>完成活动</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./SEEM_HINT_RES.html'>SEEM_HINT_RES</a></td><td>0x700FF000</td><td>类似hint_req</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./POP_WAIT_TIME_RES.html'>POP_WAIT_TIME_RES</a></td><td>0x700F0105</td><td>点击同意答题，倒计时时间</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./POP_QUIZ_RES.html'>POP_QUIZ_RES</a></td><td>0x700F0100</td><td>弹出题（题干，选项，名次）</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ANSWER_QUIZ_REQ.html'>ANSWER_QUIZ_REQ</a></td><td>0x000F0101</td><td><a href='./ANSWER_QUIZ_RES.html'>ANSWER_QUIZ_RES</a></td><td>0x700F0101</td><td>回答问题</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./ANSWER_USE_PROPS_RES.html'>ANSWER_USE_PROPS_RES</a></td><td>0x700F0104</td><td>回答问题使用道具返回</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./ANSWER_QUIZ_FINISH_RES.html'>ANSWER_QUIZ_FINISH_RES</a></td><td>0x700F0102</td><td>回答问题完毕</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ANSWER_QUIZ_CANCEL_REQ.html'>ANSWER_QUIZ_CANCEL_REQ</a></td><td>0x000F0103</td><td><a href='./ANSWER_QUIZ_CANCEL_RES.html'>ANSWER_QUIZ_CANCEL_RES</a></td><td>0x700F0103</td><td>玩家关闭答题活动</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./QUERY_SPECIAL_EQUIPMENT_RES.html'>QUERY_SPECIAL_EQUIPMENT_RES</a></td><td>0x700F0200</td><td>查询特殊装备</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_ONE_SPECIAL_EQUIPMENT_REQ.html'>QUERY_ONE_SPECIAL_EQUIPMENT_REQ</a></td><td>0x000F0201</td><td><a href='./QUERY_ONE_SPECIAL_EQUIPMENT_RES.html'>QUERY_ONE_SPECIAL_EQUIPMENT_RES</a></td><td>0x700F0201</td><td>查询指定某一特殊特殊装备</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./USE_EXPLORE_RESOURCEMAP_RES.html'>USE_EXPLORE_RESOURCEMAP_RES</a></td><td>0x700F0300</td><td>使用寻宝藏宝图道具，得到是那个地图</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./USE_EXPLOREPROPS_RES.html'>USE_EXPLOREPROPS_RES</a></td><td>0x700F0301</td><td>使用寻宝道具，得到在那个位置:很远 ((byte)0),附近((byte)1),正上方((byte)2),右上方((byte)3),右边((byte)4),    右下方((byte)5),正下方((byte)6),左下方((byte)7),左边((byte)8),左上方((byte)9);</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SPECIAL_DEAL_REQ.html'>SPECIAL_DEAL_REQ</a></td><td>0x000F0302</td><td><a href='./-.html'>-</a></td><td>-</td><td>一种特殊的交易</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./SPECIAL_DEAL_AGREE_RES.html'>SPECIAL_DEAL_AGREE_RES</a></td><td>0x700F0303</td><td>一种特殊的交易</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SPECIAL_DEAL_ADD_ARTICLE_REQ.html'>SPECIAL_DEAL_ADD_ARTICLE_REQ</a></td><td>0x000F0304</td><td><a href='./SPECIAL_DEAL_ADD_ARTICLE_RES.html'>SPECIAL_DEAL_ADD_ARTICLE_RES</a></td><td>0x700F0304</td><td>更改条件，增加物品到交换栏</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SPECIAL_DEAL_DEL_ARTICLE_REQ.html'>SPECIAL_DEAL_DEL_ARTICLE_REQ</a></td><td>0x000F0305</td><td><a href='./SPECIAL_DEAL_DEL_ARTICLE_RES.html'>SPECIAL_DEAL_DEL_ARTICLE_RES</a></td><td>0x700F0305</td><td>更改条件，把物品从交换栏脱下</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SPECIAL_DEAL_FINISH_REQ.html'>SPECIAL_DEAL_FINISH_REQ</a></td><td>0x000F0306</td><td><a href='./SPECIAL_DEAL_FINISH_RES.html'>SPECIAL_DEAL_FINISH_RES</a></td><td>0x700F0306</td><td>交换生成的新物品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CLOSE_DEAL_REQ.html'>CLOSE_DEAL_REQ</a></td><td>0x000F0307</td><td><a href='./CLOSE_DEAL_RES.html'>CLOSE_DEAL_RES</a></td><td>0x700F0307</td><td>关闭交换</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SEND_MAP_SOUND_REQ.html'>SEND_MAP_SOUND_REQ</a></td><td>0x0F300028</td><td><a href='./-.html'>-</a></td><td>-</td><td>发送地图音乐信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_CLIENT_NEWPLAYER_LEAD_REQ.html'>NOTICE_CLIENT_NEWPLAYER_LEAD_REQ</a></td><td>0x0F300029</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端新手引导</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NOTIFY_HOTSPOT_CHANGE_RES.html'>NOTIFY_HOTSPOT_CHANGE_RES</a></td><td>0x0F400001</td><td>通知客户端热点数据变化</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HOTSPOT_SEE_REQ.html'>HOTSPOT_SEE_REQ</a></td><td>0x0F400002</td><td><a href='./HOTSPOT_SEE_RES.html'>HOTSPOT_SEE_RES</a></td><td>0x8F400002</td><td>查看某个热点</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./HOTSPOT_OVER_RES.html'>HOTSPOT_OVER_RES</a></td><td>0x8F400003</td><td>热点完成</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./HOTSPOT_OPEN_RES.html'>HOTSPOT_OPEN_RES</a></td><td>0x8F400004</td><td>热点开启</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SEQUENCE_NUM_PARAMETERS_REQ.html'>SEQUENCE_NUM_PARAMETERS_REQ</a></td><td>0x0F300030</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端生成序列号的4个参数</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_INVALID_TIME_REQ.html'>QUERY_INVALID_TIME_REQ</a></td><td>0x0F300031</td><td><a href='./QUERY_INVALID_TIME_RES.html'>QUERY_INVALID_TIME_RES</a></td><td>0x8F300031</td><td>查询物品到期时间</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PLAY_SOUND_REQ.html'>PLAY_SOUND_REQ</a></td><td>0x0F300033</td><td><a href='./-.html'>-</a></td><td>-</td><td>要求客户端播放声音</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_PROPERTY_MAX_VALUE_INFO_REQ.html'>PLAYER_PROPERTY_MAX_VALUE_INFO_REQ</a></td><td>0x0F300035</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端这个人物角色的最大属性面板值</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NOTICE_NPC_FLASH_RES.html'>NOTICE_NPC_FLASH_RES</a></td><td>0x8F300032</td><td>告诉客户端那个npc闪光</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./PUSH_CONFIRMACTION_ACTIVITY_RES.html'>PUSH_CONFIRMACTION_ACTIVITY_RES</a></td><td>0x8F3000A5</td><td>某一激活码活动数据</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EXCHANGE_CONFIRMACTION_CODE_REQ.html'>EXCHANGE_CONFIRMACTION_CODE_REQ</a></td><td>0x0F3000A6</td><td><a href='./EXCHANGE_CONFIRMACTION_CODE_RES.html'>EXCHANGE_CONFIRMACTION_CODE_RES</a></td><td>0x8F3000A6</td><td>兑换激活码</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CREATE_FEEDBACK_REQ.html'>CREATE_FEEDBACK_REQ</a></td><td>0x0F400030</td><td><a href='./-.html'>-</a></td><td>-</td><td>用户创建一个反馈信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_FEEDBACK_LIST_REQ.html'>QUERY_FEEDBACK_LIST_REQ</a></td><td>0x0F400031</td><td><a href='./QUERY_FEEDBACK_LIST_RES.html'>QUERY_FEEDBACK_LIST_RES</a></td><td>0x8F400031</td><td>玩家查询自己创建的反馈</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_SPECIAL_FEEDBACK_REQ.html'>QUERY_SPECIAL_FEEDBACK_REQ</a></td><td>0x0F400032</td><td><a href='./QUERY_SPECIAL_FEEDBACK_RES.html'>QUERY_SPECIAL_FEEDBACK_RES</a></td><td>0x8F400032</td><td>玩家查询指定某一反馈</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REPLY_SPECIAL_FEEDBACK_REQ.html'>REPLY_SPECIAL_FEEDBACK_REQ</a></td><td>0x0F400033</td><td><a href='./-.html'>-</a></td><td>-</td><td>玩家回复一个反馈</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./REPLY_SPECIAL_FEEDBACK_RES.html'>REPLY_SPECIAL_FEEDBACK_RES</a></td><td>0x8F400034</td><td>通知玩家gm回复你一个反馈</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JUDGE_SPECIAL_FEEDBACK_REQ.html'>JUDGE_SPECIAL_FEEDBACK_REQ</a></td><td>0x0F400035</td><td><a href='./JUDGE_SPECIAL_FEEDBACK_RES.html'>JUDGE_SPECIAL_FEEDBACK_RES</a></td><td>0x8F400035</td><td>玩家评价</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./FEEDBACK_NOTICE_CLIENT_ALLJUDGE_RES.html'>FEEDBACK_NOTICE_CLIENT_ALLJUDGE_RES</a></td><td>0x8F400135</td><td>通知客户端所有的评价id</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./FEEDBACK_NOTICE_CLIENT_RES.html'>FEEDBACK_NOTICE_CLIENT_RES</a></td><td>0x8F400136</td><td>通知客户端有gm回复评价等</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NOTICE_CLIENT_PLAY_PARTICLE_RES.html'>NOTICE_CLIENT_PLAY_PARTICLE_RES</a></td><td>0x8F300033</td><td>通知客户端播放一个粒子效果</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SIFANG_TEST_NPCMENU_REQ.html'>SIFANG_TEST_NPCMENU_REQ</a></td><td>0x00100001</td><td><a href='./-.html'>-</a></td><td>-</td><td>四方神兽测试协议，返回NPC菜单项</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./SIFANG_ENLIST_PLAYER_RES.html'>SIFANG_ENLIST_PLAYER_RES</a></td><td>0x70100002</td><td>设定参赛玩家</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SIFANG_OVER_MSG_REQ.html'>SIFANG_OVER_MSG_REQ</a></td><td>0x00100003</td><td><a href='./SIFANG_OVER_MSG_RES.html'>SIFANG_OVER_MSG_RES</a></td><td>0x70100003</td><td>四方神兽活动结果查询</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SIFANG_CHOOSE_PLAYER_REQ.html'>SIFANG_CHOOSE_PLAYER_REQ</a></td><td>0x00100004</td><td><a href='./SIFANG_CHOOSE_PLAYER_RES.html'>SIFANG_CHOOSE_PLAYER_RES</a></td><td>0x70100004</td><td>选择玩家参赛</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./SIFANG_SHOW_INFO_BUTTON_RES.html'>SIFANG_SHOW_INFO_BUTTON_RES</a></td><td>0x70100005</td><td>让客户端显示按钮</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./SIFANG_SHOW_START_TIME_RES.html'>SIFANG_SHOW_START_TIME_RES</a></td><td>0x70100006</td><td>让客户端显示倒计时</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./RECOMEND_COUNTRY_REQ.html'>RECOMEND_COUNTRY_REQ</a></td><td>0x0F600036</td><td><a href='./-.html'>-</a></td><td>-</td><td>推荐国家</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./RANDOM_NAME_REQ.html'>RANDOM_NAME_REQ</a></td><td>0x0F400036</td><td><a href='./RANDOM_NAME_RES.html'>RANDOM_NAME_RES</a></td><td>0x8F400036</td><td>客户端随机一个名字</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EXPEND_KNAPSACK_REQ.html'>EXPEND_KNAPSACK_REQ</a></td><td>0x0F600068</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端申请扩展背包格</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_DESCRIPTION_BY_ARTICLE_REQ.html'>GET_DESCRIPTION_BY_ARTICLE_REQ</a></td><td>0x0F600056</td><td><a href='./GET_DESCRIPTION_BY_ARTICLE_RES.html'>GET_DESCRIPTION_BY_ARTICLE_RES</a></td><td>0x8F600056</td><td>客户端通过一个名字得到物品的基本信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHANGE_GAME_DISPLAYNAME_REQ.html'>CHANGE_GAME_DISPLAYNAME_REQ</a></td><td>0x0F600057</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端修改地图的显示名字</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NOTICE_TEAM_CHANGE_RES.html'>NOTICE_TEAM_CHANGE_RES</a></td><td>0x8F600057</td><td>队伍成员变化通知其他人 0: hp;1: maxHP;2: mp;3: maxMP;4:level</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QIANCENGTA_OPEN_INFO_REQ.html'>QIANCENGTA_OPEN_INFO_REQ</a></td><td>0x0F700001</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端打开千层塔</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QIANCENGTA_QUERY_INFO_REQ.html'>QIANCENGTA_QUERY_INFO_REQ</a></td><td>0x0F700002</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端主动请求更新界面信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QIANCENGTA_FLUSH_REQ.html'>QIANCENGTA_FLUSH_REQ</a></td><td>0x0F700003</td><td><a href='./-.html'>-</a></td><td>-</td><td>刷新</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QIANCENGTA_AUTO_PATA_REQ.html'>QIANCENGTA_AUTO_PATA_REQ</a></td><td>0x0F700004</td><td><a href='./QIANCENGTA_AUTO_PATA_RES.html'>QIANCENGTA_AUTO_PATA_RES</a></td><td>0x8F700004</td><td>自动爬塔</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QIANCENGTA_MANUAL_PATA_REQ.html'>QIANCENGTA_MANUAL_PATA_REQ</a></td><td>0x0F700005</td><td><a href='./-.html'>-</a></td><td>-</td><td>挑战</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QIANCENGTA_GET_REWARD_REQ.html'>QIANCENGTA_GET_REWARD_REQ</a></td><td>0x0F700006</td><td><a href='./-.html'>-</a></td><td>-</td><td>领取奖励</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QIANCENGTA_REWARD_INFO_REQ.html'>QIANCENGTA_REWARD_INFO_REQ</a></td><td>0x0F700007</td><td><a href='./QIANCENGTA_REWARD_INFO_RES.html'>QIANCENGTA_REWARD_INFO_RES</a></td><td>0x8F700007</td><td>取千层塔某道的奖励</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./QIANCHENGTA_MANUAL_OVER_RES.html'>QIANCHENGTA_MANUAL_OVER_RES</a></td><td>0x8F700010</td><td>手动爬塔胜利后</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./QIANCHENGTA_MANUAL_MSG_RES.html'>QIANCHENGTA_MANUAL_MSG_RES</a></td><td>0x8F700011</td><td>塔的说明信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QIANCHENGTA_MANUAL_START_REQ.html'>QIANCHENGTA_MANUAL_START_REQ</a></td><td>0x0F700012</td><td><a href='./QIANCHENGTA_MANUAL_START_RES.html'>QIANCHENGTA_MANUAL_START_RES</a></td><td>0x8F700012</td><td>手动爬塔正式开始，开始刷怪</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./QIANCHENGTA_MANUAL_FAIL_RES.html'>QIANCHENGTA_MANUAL_FAIL_RES</a></td><td>0x8F700013</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QIANCHENGTA_MANUAL_BACKHOME_REQ.html'>QIANCHENGTA_MANUAL_BACKHOME_REQ</a></td><td>0x8F700014</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEWQIANCENGTA_OPEN_INFO_REQ.html'>NEWQIANCENGTA_OPEN_INFO_REQ</a></td><td>0x0F710001</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端打开千层塔</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEWQIANCENGTA_QUERY_INFO_REQ.html'>NEWQIANCENGTA_QUERY_INFO_REQ</a></td><td>0x0F710002</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端主动请求更新界面信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEWQIANCENGTA_FLUSH_REQ.html'>NEWQIANCENGTA_FLUSH_REQ</a></td><td>0x0F710003</td><td><a href='./-.html'>-</a></td><td>-</td><td>刷新</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEWQIANCENGTA_AUTO_PATA_REQ.html'>NEWQIANCENGTA_AUTO_PATA_REQ</a></td><td>0x0F710004</td><td><a href='./NEWQIANCENGTA_AUTO_PATA_RES.html'>NEWQIANCENGTA_AUTO_PATA_RES</a></td><td>0x8F710004</td><td>自动爬塔</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEWQIANCENGTA_MANUAL_PATA_REQ.html'>NEWQIANCENGTA_MANUAL_PATA_REQ</a></td><td>0x0F710005</td><td><a href='./-.html'>-</a></td><td>-</td><td>挑战</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEWQIANCENGTA_GET_REWARD_REQ.html'>NEWQIANCENGTA_GET_REWARD_REQ</a></td><td>0x0F710006</td><td><a href='./-.html'>-</a></td><td>-</td><td>领取奖励</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEWQIANCENGTA_REWARD_INFO_REQ.html'>NEWQIANCENGTA_REWARD_INFO_REQ</a></td><td>0x0F710007</td><td><a href='./NEWQIANCENGTA_REWARD_INFO_RES.html'>NEWQIANCENGTA_REWARD_INFO_RES</a></td><td>0x8F710007</td><td>取千层塔某道的奖励</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NEWQIANCHENGTA_MANUAL_OVER_RES.html'>NEWQIANCHENGTA_MANUAL_OVER_RES</a></td><td>0x8F710010</td><td>手动爬塔胜利后</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NEWQIANCHENGTA_MANUAL_MSG_RES.html'>NEWQIANCHENGTA_MANUAL_MSG_RES</a></td><td>0x8F710011</td><td>塔的说明信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NEWQIANCHENGTA_MANUAL_FAIL_RES.html'>NEWQIANCHENGTA_MANUAL_FAIL_RES</a></td><td>0x8F710013</td><td>手动爬塔失败</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEWQIANCENGTA_GET_FIRST_REWARD_REQ.html'>NEWQIANCENGTA_GET_FIRST_REWARD_REQ</a></td><td>0x0F710014</td><td><a href='./NEWQIANCENGTA_GET_FIRST_REWARD_RES.html'>NEWQIANCENGTA_GET_FIRST_REWARD_RES</a></td><td>0x0F710015</td><td>领取首次通道奖励</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TRY_CLIENT_MSG_A_REQ.html'>TRY_CLIENT_MSG_A_REQ</a></td><td>0x00FFA001</td><td><a href='./-.html'>-</a></td><td>-</td><td>Android协议</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TRY_CLIENT_MSG_I_REQ.html'>TRY_CLIENT_MSG_I_REQ</a></td><td>0x00FFA002</td><td><a href='./-.html'>-</a></td><td>-</td><td>IOS协议</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TRY_CLIENT_MSG_W_REQ.html'>TRY_CLIENT_MSG_W_REQ</a></td><td>0x00FFA005</td><td><a href='./-.html'>-</a></td><td>-</td><td>Win8协议</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CLIENT_MSG_A_REQ.html'>CLIENT_MSG_A_REQ</a></td><td>0x00FFA003</td><td><a href='./CLIENT_MSG_A_RES.html'>CLIENT_MSG_A_RES</a></td><td>0x70FFA003</td><td>A信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CLIENT_MSG_I_REQ.html'>CLIENT_MSG_I_REQ</a></td><td>0x00FFA004</td><td><a href='./CLIENT_MSG_I_RES.html'>CLIENT_MSG_I_RES</a></td><td>0x70FFA004</td><td>I信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CLIENT_MSG_W_REQ.html'>CLIENT_MSG_W_REQ</a></td><td>0x00FFA006</td><td><a href='./CLIENT_MSG_W_RES.html'>CLIENT_MSG_W_RES</a></td><td>0x70FFA006</td><td>Win8信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_EFFECT_NOTICE_REQ.html'>GET_EFFECT_NOTICE_REQ</a></td><td>0x0F700008</td><td><a href='./EFFECT_NOTICE_RES.html'>EFFECT_NOTICE_RES</a></td><td>0x8F700008</td><td>查询公告，登陆返回公告，弹出临时公告</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./OBTAIN_NOTICE_SIVLER_REQ.html'>OBTAIN_NOTICE_SIVLER_REQ</a></td><td>0x0F700009</td><td><a href='./OBTAIN_NOTICE_SIVLER_RES.html'>OBTAIN_NOTICE_SIVLER_RES</a></td><td>0x8F700009</td><td>领取公告的绑银</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ACTIVITY_INFO_RES.html'>ACTIVITY_INFO_RES</a></td><td>0x0F70000A</td><td><a href='./-.html'>-</a></td><td>-</td><td>活动信息面板</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_BILLBOARD_MENUS_REQ.html'>GET_BILLBOARD_MENUS_REQ</a></td><td>0x0070001A</td><td><a href='./GET_BILLBOARD_MENUS_RES.html'>GET_BILLBOARD_MENUS_RES</a></td><td>0x0F70001A</td><td>打开排行榜</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_ONE_BILLBOARD_REQ.html'>GET_ONE_BILLBOARD_REQ</a></td><td>0x0070002A</td><td><a href='./GET_ONE_BILLBOARD_RES.html'>GET_ONE_BILLBOARD_RES</a></td><td>0x0F70002A</td><td>得到指定排行榜数据</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REQUEST_ACHIEVEMENT_LIST_REQ.html'>REQUEST_ACHIEVEMENT_LIST_REQ</a></td><td>0x0070002B</td><td><a href='./REQUEST_ACHIEVEMENT_LIST_RES.html'>REQUEST_ACHIEVEMENT_LIST_RES</a></td><td>0x0F70002B</td><td>查询成就分类列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REQUEST_ACHIEVEMENT_DONE_REQ.html'>REQUEST_ACHIEVEMENT_DONE_REQ</a></td><td>0x0070002C</td><td><a href='./REQUEST_ACHIEVEMENT_DONE_RES.html'>REQUEST_ACHIEVEMENT_DONE_RES</a></td><td>0x0F70002C</td><td>查询某一类成就完成情况</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REQUEST_ONE_DONE_ACHIEVEMENT_REQ.html'>REQUEST_ONE_DONE_ACHIEVEMENT_REQ</a></td><td>0x0070002D</td><td><a href='./REQUEST_ONE_DONE_ACHIEVEMENT_RES.html'>REQUEST_ONE_DONE_ACHIEVEMENT_RES</a></td><td>0x0F70002D</td><td>查询某个人已经完成的成就信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_CLIENT_DONE_ACHIEVEMENT_REQ.html'>NOTICE_CLIENT_DONE_ACHIEVEMENT_REQ</a></td><td>0x0070002E</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知角色达成成就</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./VIP_REQ.html'>VIP_REQ</a></td><td>0x00800001</td><td><a href='./VIP_RES.html'>VIP_RES</a></td><td>0x70800001</td><td>查询VIP信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./VIP_2_REQ.html'>VIP_2_REQ</a></td><td>0x00F0EF69</td><td><a href='./VIP_2_RES.html'>VIP_2_RES</a></td><td>0x70F0EF69</td><td>查询VIP信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_VIP_REWARD_REQ.html'>GET_VIP_REWARD_REQ</a></td><td>0x00800002</td><td><a href='./-.html'>-</a></td><td>-</td><td>获得vip奖励</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./VIP_PULL_FRIEND_REQ.html'>VIP_PULL_FRIEND_REQ</a></td><td>0x00800003</td><td><a href='./-.html'>-</a></td><td>-</td><td>VIP拉好友传送</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GUOZHAN_ORDERS_REQ.html'>GUOZHAN_ORDERS_REQ</a></td><td>0x00EE0001</td><td><a href='./GUOZHAN_ORDERS_RES.html'>GUOZHAN_ORDERS_RES</a></td><td>0x70EE0001</td><td>获得所有国战命令，包括预设的三条</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GUOZHAN_ADD_ORDER_REQ.html'>GUOZHAN_ADD_ORDER_REQ</a></td><td>0x00EE0002</td><td><a href='./GUOZHAN_ADD_ORDER_RES.html'>GUOZHAN_ADD_ORDER_RES</a></td><td>0x70EE0002</td><td>添加一条命令，返回添加后的命令列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GUOZHAN_REMOVE_ORDER_REQ.html'>GUOZHAN_REMOVE_ORDER_REQ</a></td><td>0x00EE0003</td><td><a href='./GUOZHAN_REMOVE_ORDER_RES.html'>GUOZHAN_REMOVE_ORDER_RES</a></td><td>0x70EE0003</td><td>删除一条命令，返回删除后的列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GUOZHAN_CONTROL_PANNEL_REQ.html'>GUOZHAN_CONTROL_PANNEL_REQ</a></td><td>0x00EE0004</td><td><a href='./GUOZHAN_CONTROL_PANNEL_RES.html'>GUOZHAN_CONTROL_PANNEL_RES</a></td><td>0x70EE0004</td><td>国战的主控制面板，不同的角色具有不同的权限</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GUOZHAN_MAKE_ORDER_REQ.html'>GUOZHAN_MAKE_ORDER_REQ</a></td><td>0x00EE0005</td><td><a href='./-.html'>-</a></td><td>-</td><td>国王发布命令</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./IOS_CLIENT_MSG_REQ.html'>IOS_CLIENT_MSG_REQ</a></td><td>0x00FFB005</td><td><a href='./-.html'>-</a></td><td>-</td><td>IOS协议</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GUOZHAN_CURE_BOSS_REQ.html'>GUOZHAN_CURE_BOSS_REQ</a></td><td>0x00EE0006</td><td><a href='./GUOZHAN_CURE_BOSS_RES.html'>GUOZHAN_CURE_BOSS_RES</a></td><td>0x70EE0006</td><td>治疗boss</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GUOZHAN_DEALY_REQ.html'>GUOZHAN_DEALY_REQ</a></td><td>0x00EE0007</td><td><a href='./GUOZHAN_DEALY_RES.html'>GUOZHAN_DEALY_RES</a></td><td>0x70EE0007</td><td>国战延时</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GUOZHAN_PULL_PLAYER_REQ.html'>GUOZHAN_PULL_PLAYER_REQ</a></td><td>0x00EE0008</td><td><a href='./-.html'>-</a></td><td>-</td><td>国战拉人</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GUOZHAN_INFO_REQ.html'>GUOZHAN_INFO_REQ</a></td><td>0x00EE0009</td><td><a href='./-.html'>-</a></td><td>-</td><td>国战信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GUOZHAN_HISTORY_REQ.html'>GUOZHAN_HISTORY_REQ</a></td><td>0x00EE0010</td><td><a href='./GUOZHAN_HISTORY_RES.html'>GUOZHAN_HISTORY_RES</a></td><td>0x70EE0010</td><td>国战历史</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GUOZHAN_LIST_REQ.html'>GUOZHAN_LIST_REQ</a></td><td>0x00EE0011</td><td><a href='./GUOZHAN_LIST_RES.html'>GUOZHAN_LIST_RES</a></td><td>0x70EE0011</td><td>国战查询</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GUOZHAN_STAT_REQ.html'>GUOZHAN_STAT_REQ</a></td><td>0x00EE0012</td><td><a href='./GUOZHAN_STAT_RES.html'>GUOZHAN_STAT_RES</a></td><td>0x70EE0012</td><td>国战统计</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GUOZHAN_RESULT_REQ.html'>GUOZHAN_RESULT_REQ</a></td><td>0x00EE0013</td><td><a href='./-.html'>-</a></td><td>-</td><td>国战结果</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GUOZHAN_DETAIL_REQ.html'>GUOZHAN_DETAIL_REQ</a></td><td>0x00EE0014</td><td><a href='./GUOZHAN_DETAIL_RES.html'>GUOZHAN_DETAIL_RES</a></td><td>0x70EE0014</td><td>国战详情</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GUOZHAN_MAP_POINT_REQ.html'>GUOZHAN_MAP_POINT_REQ</a></td><td>0x00EE0015</td><td><a href='./GUOZHAN_MAP_POINT_RES.html'>GUOZHAN_MAP_POINT_RES</a></td><td>0x70EE0015</td><td>查询国战小地图上几个点的信息，对于玩家，两个点如果都被本方占据，则连接为实线，否则虚线；对于玩家，如果点被本方占据，则显示旗子，否则显示x</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GUOZHAN_MAP_ORDER_REQ.html'>GUOZHAN_MAP_ORDER_REQ</a></td><td>0x00EE0016</td><td><a href='./GUOZHAN_MAP_ORDER_RES.html'>GUOZHAN_MAP_ORDER_RES</a></td><td>0x70EE0016</td><td>国战指挥员在国战小地图上发布命令</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GUOZHAN_MAP_ORDER_MAKED_REQ.html'>GUOZHAN_MAP_ORDER_MAKED_REQ</a></td><td>0x00EE0017</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知国战小地图命令发布了</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GUOZHAN_RETURN_BACK_REQ.html'>GUOZHAN_RETURN_BACK_REQ</a></td><td>0x00EE0018</td><td><a href='./-.html'>-</a></td><td>-</td><td>回到本国边境复活点</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GUOZHAN_RESULT_NEW_REQ.html'>GUOZHAN_RESULT_NEW_REQ</a></td><td>0x00EE0019</td><td><a href='./-.html'>-</a></td><td>-</td><td>国战结果(新)</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GUOZHAN_CONTROL_PANNEL_NEW_REQ.html'>GUOZHAN_CONTROL_PANNEL_NEW_REQ</a></td><td>0x00EE0020</td><td><a href='./GUOZHAN_CONTROL_PANNEL_NEW_RES.html'>GUOZHAN_CONTROL_PANNEL_NEW_RES</a></td><td>0x70EE0020</td><td>国战的主控制面板，不同的角色具有不同的权限(新)</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ACTIVITY_LIST_REQ.html'>ACTIVITY_LIST_REQ</a></td><td>0x00EEE001</td><td><a href='./ACTIVITY_LIST_RES.html'>ACTIVITY_LIST_RES</a></td><td>0x70EEE001</td><td>活动列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_PLAYERTITLES_REQ.html'>GET_PLAYERTITLES_REQ</a></td><td>0x00EEE002</td><td><a href='./GET_PLAYERTITLES_RES.html'>GET_PLAYERTITLES_RES</a></td><td>0x70EEE002</td><td>查看个人称号</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SETDEFAULT_PLAYERTITLE_REQ.html'>SETDEFAULT_PLAYERTITLE_REQ</a></td><td>0x00EEE003</td><td><a href='./-.html'>-</a></td><td>-</td><td>设置个人默认称号</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./CREATE_SPECIALEQUIPMENT_BROADCAST_RES.html'>CREATE_SPECIALEQUIPMENT_BROADCAST_RES</a></td><td>0x70EEE004</td><td>生成混沌万灵榜广播</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_PWD_PROTECT_REQ.html'>QUERY_PWD_PROTECT_REQ</a></td><td>0x00EEEE01</td><td><a href='./QUERY_PWD_PROTECT_RES.html'>QUERY_PWD_PROTECT_RES</a></td><td>0x70EEEE01</td><td>查看密保信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SET_PWD_PROTECT_REQ.html'>SET_PWD_PROTECT_REQ</a></td><td>0x00EEEE02</td><td><a href='./SET_PWD_PROTECT_RES.html'>SET_PWD_PROTECT_RES</a></td><td>0x70EEEE02</td><td>设置密保</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_CHARGE_LIST_REQ.html'>QUERY_CHARGE_LIST_REQ</a></td><td>0x00EEEE03</td><td><a href='./QUERY_CHARGE_LIST_RES.html'>QUERY_CHARGE_LIST_RES</a></td><td>0x70EEEE03</td><td>查询充值左侧列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_CHARGE_MONEY_CONFIGURE_REQ.html'>QUERY_CHARGE_MONEY_CONFIGURE_REQ</a></td><td>0x00EEEE04</td><td><a href='./QUERY_CHARGE_MONEY_CONFIGURE_RES.html'>QUERY_CHARGE_MONEY_CONFIGURE_RES</a></td><td>0x70EEEE04</td><td>查询充值信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CHARGE_REQ.html'>CHARGE_REQ</a></td><td>0x00EEEE05</td><td><a href='./CHARGE_RES.html'>CHARGE_RES</a></td><td>0x70EEEE05</td><td>充值</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_ORDER_LIST_REQ.html'>QUERY_ORDER_LIST_REQ</a></td><td>0x00EEEE07</td><td><a href='./QUERY_ORDER_LIST_RES.html'>QUERY_ORDER_LIST_RES</a></td><td>0x70EEEE07</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./AY_ARGS_REQ.html'>AY_ARGS_REQ</a></td><td>0x00EEEE08</td><td><a href='./A_ARGS_RES.html'>A_ARGS_RES</a></td><td>0x70EEEE08</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./A_GET_ORDERID_REQ.html'>A_GET_ORDERID_REQ</a></td><td>0x00EEEE09</td><td><a href='./A_GET_ORDERID_RES.html'>A_GET_ORDERID_RES</a></td><td>0x70EEEE09</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_CLIENT_APP_CHARGE_REQ.html'>NOTICE_CLIENT_APP_CHARGE_REQ</a></td><td>0x00EEEEAA</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端苹果充值请求已经收到</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SET_VIEW_PORT.html'>SET_VIEW_PORT</a></td><td>0x00EEEE0A</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端设置视野</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SET_CLIENT_DISPLAY_FLAG.html'>SET_CLIENT_DISPLAY_FLAG</a></td><td>0x00EEEE0B</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端设置显示屏蔽</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIU1_GET_CHARGE_ORDER_REQ.html'>JIU1_GET_CHARGE_ORDER_REQ</a></td><td>0x00EEEE0C</td><td><a href='./JIU1_GET_CHARGE_ORDER_RES.html'>JIU1_GET_CHARGE_ORDER_RES</a></td><td>0x70EEEE0C</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_OPEN_WINDOW_REQ.html'>NOTIFY_OPEN_WINDOW_REQ</a></td><td>0xA0000036</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端打开某个窗口</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./YINGYONGHUI_GET_CHARGE_ORDER_REQ.html'>YINGYONGHUI_GET_CHARGE_ORDER_REQ</a></td><td>0x00EEEE0D</td><td><a href='./YINGYONGHUI_GET_CHARGE_ORDER_RES.html'>YINGYONGHUI_GET_CHARGE_ORDER_RES</a></td><td>0x70EEEE0D</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ORDER_STATUS_CHANGE_REQ.html'>ORDER_STATUS_CHANGE_REQ</a></td><td>0x00EEEE0E</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./LENOVO_GET_CHARGE_ORDER_REQ.html'>LENOVO_GET_CHARGE_ORDER_REQ</a></td><td>0x00EEEE0F</td><td><a href='./LENOVO_GET_CHARGE_ORDER_RES.html'>LENOVO_GET_CHARGE_ORDER_RES</a></td><td>0x70EEEE0F</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./UCSDK_NOTICE_CHARGE_REQ.html'>UCSDK_NOTICE_CHARGE_REQ</a></td><td>0x00EEEE1E</td><td><a href='./UCSDK_NOTICE_CHARGE_RES.html'>UCSDK_NOTICE_CHARGE_RES</a></td><td>0x70EEEE1E</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./APPSTORE_SAVING_VERIFY_REQ.html'>APPSTORE_SAVING_VERIFY_REQ</a></td><td>0x00EE9901</td><td><a href='./APPSTORE_SAVING_VERIFY_RES.html'>APPSTORE_SAVING_VERIFY_RES</a></td><td>0x70EE9901</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_OPEN_URL_ARGS_REQ.html'>NOTICE_OPEN_URL_ARGS_REQ</a></td><td>0x00EEEF12</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./IAPPP_GET_CHARGE_ORDER_REQ.html'>IAPPP_GET_CHARGE_ORDER_REQ</a></td><td>0x00EE9902</td><td><a href='./IAPPP_GET_CHARGE_ORDER_RES.html'>IAPPP_GET_CHARGE_ORDER_RES</a></td><td>0x70EE9902</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DEFAULT_GET_CHARGEORDER_REQ.html'>DEFAULT_GET_CHARGEORDER_REQ</a></td><td>0x00EEEEE0</td><td><a href='./DEFAULT_GET_CHARGEORDER_RES.html'>DEFAULT_GET_CHARGEORDER_RES</a></td><td>0x70EEEEE0</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_CHARGE_ORDER_MULTIIO_REQ.html'>GET_CHARGE_ORDER_MULTIIO_REQ</a></td><td>0x00EEEEE1</td><td><a href='./GET_CHARGE_ORDER_MULTIIO_RES.html'>GET_CHARGE_ORDER_MULTIIO_RES</a></td><td>0x70EEEEE1</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NOTICE_ACTIVITY_STATUS_RES.html'>NOTICE_ACTIVITY_STATUS_RES</a></td><td>0x70EEEE13</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_EXCHANGE_REQ.html'>QUERY_EXCHANGE_REQ</a></td><td>0x00EEEE14</td><td><a href='./QUERY_EXCHANGE_RES.html'>QUERY_EXCHANGE_RES</a></td><td>0x70EEEE14</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./EXCHANGE_REQ.html'>EXCHANGE_REQ</a></td><td>0x00EEEE15</td><td><a href='./EXCHANGE_RES.html'>EXCHANGE_RES</a></td><td>0x70EEEE15</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DOWNCITY_PREPARE_WINDOW_REQ.html'>DOWNCITY_PREPARE_WINDOW_REQ</a></td><td>0x00EEEAB0</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DOWNCITY_PLAYER_STATUS_CHANGE_REQ.html'>DOWNCITY_PLAYER_STATUS_CHANGE_REQ</a></td><td>0x00EEEAB1</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DOWNCITY_CREATE_REQ.html'>DOWNCITY_CREATE_REQ</a></td><td>0x00EEEAB2</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_TUNSHI_REQ.html'>QUERY_TUNSHI_REQ</a></td><td>0x00EEEABA</td><td><a href='./QUERY_TUNSHI_RES.html'>QUERY_TUNSHI_RES</a></td><td>0x70EEEABA</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TUNSHI_REQ.html'>TUNSHI_REQ</a></td><td>0x00EEEABB</td><td><a href='./TUNSHI_RES.html'>TUNSHI_RES</a></td><td>0x70EEEABB</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_XILIAN_REQ.html'>QUERY_XILIAN_REQ</a></td><td>0x00EEEABC</td><td><a href='./QUERY_XILIAN_RES.html'>QUERY_XILIAN_RES</a></td><td>0x70EEEABC</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./XILIAN_REQ.html'>XILIAN_REQ</a></td><td>0x00EEEABD</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QILING_INLAY_REQ.html'>QILING_INLAY_REQ</a></td><td>0x00EEEABE</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端装备镶嵌器灵</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QILING_OUTLAY_REQ.html'>QILING_OUTLAY_REQ</a></td><td>0x00EEEABF</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端装备挖器灵</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_LIANQI_REQ.html'>QUERY_LIANQI_REQ</a></td><td>0x00EEAABA</td><td><a href='./QUERY_LIANQI_RES.html'>QUERY_LIANQI_RES</a></td><td>0x70EEAABA</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./LIANQI_REQ.html'>LIANQI_REQ</a></td><td>0x00EEAABB</td><td><a href='./LIANQI_RES.html'>LIANQI_RES</a></td><td>0x70EEAABB</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_EQUIPMENT_QILING_REQ.html'>QUERY_EQUIPMENT_QILING_REQ</a></td><td>0x00EEAABC</td><td><a href='./QUERY_EQUIPMENT_QILING_RES.html'>QUERY_EQUIPMENT_QILING_RES</a></td><td>0x70EEAABC</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_KNAPSACK_QILING_REQ.html'>QUERY_KNAPSACK_QILING_REQ</a></td><td>0x00EEAABD</td><td><a href='./QUERY_KNAPSACK_QILING_RES.html'>QUERY_KNAPSACK_QILING_RES</a></td><td>0x70EEAABD</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./OPEN_QILING_WINDOW_REQ.html'>OPEN_QILING_WINDOW_REQ</a></td><td>0x00EEAABF</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./KNAPSACK_QILING_MOVE_REQ.html'>KNAPSACK_QILING_MOVE_REQ</a></td><td>0x00EEAABE</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_VIP_DISPLAY_REQ.html'>QUERY_VIP_DISPLAY_REQ</a></td><td>0x01EE9902</td><td><a href='./QUERY_VIP_DISPLAY_RES.html'>QUERY_VIP_DISPLAY_RES</a></td><td>0x81EE9902</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SUPER_GM_REQ.html'>SUPER_GM_REQ</a></td><td>0x00EEEE10</td><td><a href='./SUPER_GM_RES.html'>SUPER_GM_RES</a></td><td>0x70EEEE10</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SUPER_GM_SELECT_REQ.html'>SUPER_GM_SELECT_REQ</a></td><td>0x00EEEE11</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTICE_OPEN_URL_REQ.html'>NOTICE_OPEN_URL_REQ</a></td><td>0x00EEEE12</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FEEDBACK_HOME_PAGE_REQ.html'>FEEDBACK_HOME_PAGE_REQ</a></td><td>0x00EEEE21</td><td><a href='./FEEDBACK_HOME_PAGE_RES.html'>FEEDBACK_HOME_PAGE_RES</a></td><td>0x70EEEE21</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FEEDBACK_DELETE_REQ.html'>FEEDBACK_DELETE_REQ</a></td><td>0x00EEEE22</td><td><a href='./FEEDBACK_DELETE_RES.html'>FEEDBACK_DELETE_RES</a></td><td>0x70EEEE22</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FEEDBACK_SCORE_REQ.html'>FEEDBACK_SCORE_REQ</a></td><td>0x00EEEE23</td><td><a href='./FEEDBACK_SCORE_RES.html'>FEEDBACK_SCORE_RES</a></td><td>0x70EEEE23</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FEEDBACK_PLAYER_TALK_REQ.html'>FEEDBACK_PLAYER_TALK_REQ</a></td><td>0x00EEEE24</td><td><a href='./FEEDBACK_PLAYER_TALK_RES.html'>FEEDBACK_PLAYER_TALK_RES</a></td><td>0x70EEEE24</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./FEEDBACK_GM_TALK_RES.html'>FEEDBACK_GM_TALK_RES</a></td><td>0x70EEEE25</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FEEDBACK_LOOK_REQ.html'>FEEDBACK_LOOK_REQ</a></td><td>0x00EEEE26</td><td><a href='./FEEDBACK_LOOK_RES.html'>FEEDBACK_LOOK_RES</a></td><td>0x70EEEE26</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FEEDBACK_LOOK_SCORE_REQ.html'>FEEDBACK_LOOK_SCORE_REQ</a></td><td>0x00EEEE27</td><td><a href='./FEEDBACK_LOOK_SCORE_RES.html'>FEEDBACK_LOOK_SCORE_RES</a></td><td>0x70EEEE27</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FEEDBACK_COMMIT_REQ.html'>FEEDBACK_COMMIT_REQ</a></td><td>0x00EEEE28</td><td><a href='./FEEDBACK_COMMIT_RES.html'>FEEDBACK_COMMIT_RES</a></td><td>0x70EEEE28</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./ARTICLEPROTECT_MSG_RES.html'>ARTICLEPROTECT_MSG_RES</a></td><td>0x700EFE01</td><td>给客户端发他自己的装备锁魂情况</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ARTICLEPROTECT_BLOCK_REQ.html'>ARTICLEPROTECT_BLOCK_REQ</a></td><td>0x000EFE02</td><td><a href='./-.html'>-</a></td><td>-</td><td>锁魂</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ARTICLEPROTECT_UNBLOCK_REQ.html'>ARTICLEPROTECT_UNBLOCK_REQ</a></td><td>0x000EFE03</td><td><a href='./-.html'>-</a></td><td>-</td><td>解锁魂</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ARTICLEPROTECT_OTHERMSG_REQ.html'>ARTICLEPROTECT_OTHERMSG_REQ</a></td><td>0x000EFE04</td><td><a href='./ARTICLEPROTECT_OTHERMSG_RES.html'>ARTICLEPROTECT_OTHERMSG_RES</a></td><td>0x700EFE04</td><td>关于锁魂的一些信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ANDROID_PROCESS_REQ.html'>ANDROID_PROCESS_REQ</a></td><td>0x000EAE01</td><td><a href='./ANDROID_PROCESS_RES.html'>ANDROID_PROCESS_RES</a></td><td>0x700EAE01</td><td>取安卓进程信息,发送res到客户端客户端会再取一次发过来</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./RSA_DATA_REQ.html'>RSA_DATA_REQ</a></td><td>0x000EAE02</td><td><a href='./-.html'>-</a></td><td>-</td><td>apk密钥</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_RSA_DATA_REQ.html'>GET_RSA_DATA_REQ</a></td><td>0x000EAE03</td><td><a href='./GET_RSA_DATA_RES.html'>GET_RSA_DATA_RES</a></td><td>0x700EAE03</td><td>服务器去取密钥文件</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./OPEN_PHONENUM_SHOW_RES.html'>OPEN_PHONENUM_SHOW_RES</a></td><td>0x700EAE04</td><td>是否开启绑定解绑手机号</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TRY_BING_PHONENUM_REQ.html'>TRY_BING_PHONENUM_REQ</a></td><td>0x000EAE05</td><td><a href='./TRY_BING_PHONENUM_RES.html'>TRY_BING_PHONENUM_RES</a></td><td>0x700EAE05</td><td>尝试去绑定手机</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TRY_SEND_PHONTNUM_REQ.html'>TRY_SEND_PHONTNUM_REQ</a></td><td>0x000EAE06</td><td><a href='./TRY_SEND_PHONTNUM_RES.html'>TRY_SEND_PHONTNUM_RES</a></td><td>0x700EAE06</td><td>尝试获取校验码</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BING_PHONENUM_REQ.html'>BING_PHONENUM_REQ</a></td><td>0x000EAE07</td><td><a href='./BING_PHONENUM_RES.html'>BING_PHONENUM_RES</a></td><td>0x700EAE07</td><td>绑定手机</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TRY_UNBING_PHONENUM_REQ.html'>TRY_UNBING_PHONENUM_REQ</a></td><td>0x000EAE08</td><td><a href='./TRY_UNBING_PHONENUM_RES.html'>TRY_UNBING_PHONENUM_RES</a></td><td>0x700EAE08</td><td>尝试去解绑手机</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TRY_SEND_UNBIND_REQ.html'>TRY_SEND_UNBIND_REQ</a></td><td>0x000EAE09</td><td><a href='./TRY_SEND_UNBIND_RES.html'>TRY_SEND_UNBIND_RES</a></td><td>0x700EAE09</td><td>尝试获取解绑校验码</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./UNBIND_PHONENUM_REQ.html'>UNBIND_PHONENUM_REQ</a></td><td>0x000EAE10</td><td><a href='./UNBIND_PHONENUM_RES.html'>UNBIND_PHONENUM_RES</a></td><td>0x700EAE10</td><td>解绑手机号</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PHONENUM_ASK_REQ.html'>PHONENUM_ASK_REQ</a></td><td>0x000EAE11</td><td><a href='./PHONENUM_ASK_RES.html'>PHONENUM_ASK_RES</a></td><td>0x700EAE11</td><td>手机号绑定答题</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_PHONE_INFO_1_REQ.html'>GET_PHONE_INFO_1_REQ</a></td><td>0x000EAE12</td><td><a href='./GET_PHONE_INFO_1_RES.html'>GET_PHONE_INFO_1_RES</a></td><td>0x700EAE12</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_PHONE_INFO_2_REQ.html'>GET_PHONE_INFO_2_REQ</a></td><td>0x000EAE13</td><td><a href='./GET_PHONE_INFO_2_RES.html'>GET_PHONE_INFO_2_RES</a></td><td>0x700EAE13</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_PHONE_INFO_3_REQ.html'>GET_PHONE_INFO_3_REQ</a></td><td>0x000EAE14</td><td><a href='./GET_PHONE_INFO_3_RES.html'>GET_PHONE_INFO_3_RES</a></td><td>0x700EAE14</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_PHONE_INFO_4_REQ.html'>GET_PHONE_INFO_4_REQ</a></td><td>0x000EAE15</td><td><a href='./GET_PHONE_INFO_4_RES.html'>GET_PHONE_INFO_4_RES</a></td><td>0x700EAE15</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_PHONE_INFO_5_REQ.html'>GET_PHONE_INFO_5_REQ</a></td><td>0x000EAE16</td><td><a href='./GET_PHONE_INFO_5_RES.html'>GET_PHONE_INFO_5_RES</a></td><td>0x700EAE16</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_PHONE_INFO_6_REQ.html'>GET_PHONE_INFO_6_REQ</a></td><td>0x000EAE17</td><td><a href='./GET_PHONE_INFO_6_RES.html'>GET_PHONE_INFO_6_RES</a></td><td>0x700EAE17</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_PHONE_INFO_7_REQ.html'>GET_PHONE_INFO_7_REQ</a></td><td>0x000EAE18</td><td><a href='./GET_PHONE_INFO_7_RES.html'>GET_PHONE_INFO_7_RES</a></td><td>0x700EAE18</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_PHONE_INFO_8_REQ.html'>GET_PHONE_INFO_8_REQ</a></td><td>0x000EAE19</td><td><a href='./GET_PHONE_INFO_8_RES.html'>GET_PHONE_INFO_8_RES</a></td><td>0x700EAE19</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_PHONE_INFO_9_REQ.html'>GET_PHONE_INFO_9_REQ</a></td><td>0x000EAE20</td><td><a href='./GET_PHONE_INFO_9_RES.html'>GET_PHONE_INFO_9_RES</a></td><td>0x700EAE20</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_PHONE_INFO_10_REQ.html'>GET_PHONE_INFO_10_REQ</a></td><td>0x000EAE21</td><td><a href='./GET_PHONE_INFO_10_RES.html'>GET_PHONE_INFO_10_RES</a></td><td>0x700EAE21</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_PHONE_INFO_11_REQ.html'>GET_PHONE_INFO_11_REQ</a></td><td>0x000EAE22</td><td><a href='./GET_PHONE_INFO_11_RES.html'>GET_PHONE_INFO_11_RES</a></td><td>0x700EAE22</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_CLIENT_INFO_REQ.html'>GET_CLIENT_INFO_REQ</a></td><td>0x000EAE23</td><td><a href='./GET_CLIENT_INFO_RES.html'>GET_CLIENT_INFO_RES</a></td><td>0x700EAE23</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_CLIENT_INFO_1_REQ.html'>GET_CLIENT_INFO_1_REQ</a></td><td>0x000EAE24</td><td><a href='./GET_CLIENT_INFO_1_RES.html'>GET_CLIENT_INFO_1_RES</a></td><td>0x700EAE24</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_CLIENT_INFO_2_REQ.html'>GET_CLIENT_INFO_2_REQ</a></td><td>0x000EAE25</td><td><a href='./GET_CLIENT_INFO_2_RES.html'>GET_CLIENT_INFO_2_RES</a></td><td>0x700EAE25</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_CLIENT_INFO_3_REQ.html'>GET_CLIENT_INFO_3_REQ</a></td><td>0x000EAE26</td><td><a href='./GET_CLIENT_INFO_3_RES.html'>GET_CLIENT_INFO_3_RES</a></td><td>0x700EAE26</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_CLIENT_INFO_4_REQ.html'>GET_CLIENT_INFO_4_REQ</a></td><td>0x000EAE27</td><td><a href='./GET_CLIENT_INFO_4_RES.html'>GET_CLIENT_INFO_4_RES</a></td><td>0x700EAE27</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_CLIENT_INFO_5_REQ.html'>GET_CLIENT_INFO_5_REQ</a></td><td>0x000EAE28</td><td><a href='./GET_CLIENT_INFO_5_RES.html'>GET_CLIENT_INFO_5_RES</a></td><td>0x700EAE28</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_CLIENT_INFO_6_REQ.html'>GET_CLIENT_INFO_6_REQ</a></td><td>0x000EAE29</td><td><a href='./GET_CLIENT_INFO_6_RES.html'>GET_CLIENT_INFO_6_RES</a></td><td>0x700EAE29</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_CLIENT_INFO_7_REQ.html'>GET_CLIENT_INFO_7_REQ</a></td><td>0x000EAE30</td><td><a href='./GET_CLIENT_INFO_7_RES.html'>GET_CLIENT_INFO_7_RES</a></td><td>0x700EAE30</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_CLIENT_INFO_8_REQ.html'>GET_CLIENT_INFO_8_REQ</a></td><td>0x000EAE31</td><td><a href='./GET_CLIENT_INFO_8_RES.html'>GET_CLIENT_INFO_8_RES</a></td><td>0x700EAE31</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_CLIENT_INFO_9_REQ.html'>GET_CLIENT_INFO_9_REQ</a></td><td>0x000EAE32</td><td><a href='./GET_CLIENT_INFO_9_RES.html'>GET_CLIENT_INFO_9_RES</a></td><td>0x700EAE32</td><td>取客户端信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./VALIDATE_INFO_REQ.html'>VALIDATE_INFO_REQ</a></td><td>0x000EAE33</td><td><a href='./VALIDATE_INFO_RES.html'>VALIDATE_INFO_RES</a></td><td>0x700EAE33</td><td>答题信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_RMB_REWARDMSG_REQ.html'>GET_RMB_REWARDMSG_REQ</a></td><td>0x000EB101</td><td><a href='./GET_RMB_REWARDMSG_RES.html'>GET_RMB_REWARDMSG_RES</a></td><td>0x700EB101</td><td>客户端取首充和累计充值活动数据</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_RMBREWARD_REQ.html'>GET_RMBREWARD_REQ</a></td><td>0x000EB102</td><td><a href='./GET_RMBREWARD_RES.html'>GET_RMBREWARD_RES</a></td><td>0x700EB102</td><td>客户端发送领取某个活动奖励</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_WEEKANDMONTH_INFO_REQ.html'>GET_WEEKANDMONTH_INFO_REQ</a></td><td>0x000EB103</td><td><a href='./GET_WEEKANDMONTH_INFO_RES.html'>GET_WEEKANDMONTH_INFO_RES</a></td><td>0x700EB103</td><td>客户端请求周月反馈活动信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_WEEKMONTH_REWARD_REQ.html'>GET_WEEKMONTH_REWARD_REQ</a></td><td>0x000EB104</td><td><a href='./GET_WEEKMONTH_REWARD_RES.html'>GET_WEEKMONTH_REWARD_RES</a></td><td>0x700EB104</td><td>领取或购买周月活动奖励</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_WEEKACTIVITY_REQ.html'>GET_WEEKACTIVITY_REQ</a></td><td>0x000EB105</td><td><a href='./GET_WEEKACTIVITY_RES.html'>GET_WEEKACTIVITY_RES</a></td><td>0x700EB105</td><td>新充值活动信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_WEEK_REWARD_REQ.html'>GET_WEEK_REWARD_REQ</a></td><td>0x000EB106</td><td><a href='./GET_WEEK_REWARD_RES.html'>GET_WEEK_REWARD_RES</a></td><td>0x700EB106</td><td>新充值活动奖励领取</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./WEEKACTIVITY_STATE_RES.html'>WEEKACTIVITY_STATE_RES</a></td><td>0x700EB107</td><td>新充值活动状态</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CURRENT_ACTIVITY_REQ.html'>CURRENT_ACTIVITY_REQ</a></td><td>0x0E0EAE01</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SIGNUP_ACTIVITY_REQ.html'>SIGNUP_ACTIVITY_REQ</a></td><td>0x0E0EAE02</td><td><a href='./SIGNUP_ACTIVITY_RES.html'>SIGNUP_ACTIVITY_RES</a></td><td>0x8E0EAE01</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SOUL_MESSAGE_REQ.html'>SOUL_MESSAGE_REQ</a></td><td>0x0E0EAE03</td><td><a href='./SOUL_MESSAGE_RES.html'>SOUL_MESSAGE_RES</a></td><td>0x8E0EAE03</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SOUL_UPGRADE_REQ.html'>SOUL_UPGRADE_REQ</a></td><td>0x0E0EAE04</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./LOGIN_ACTIVITY_MESS_RES.html'>LOGIN_ACTIVITY_MESS_RES</a></td><td>0x8E0EAE04</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./LOGIN_ACTIVITY_GET_REQ.html'>LOGIN_ACTIVITY_GET_REQ</a></td><td>0x0E0EAE05</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./CONTINUE_ACTIVITY_MESS_RES.html'>CONTINUE_ACTIVITY_MESS_RES</a></td><td>0x8E0EAE05</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CONTINUE_ACTIVITY_GET_REQ.html'>CONTINUE_ACTIVITY_GET_REQ</a></td><td>0x0E0EAE06</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEED_CHANGE_NAME_REQ.html'>NEED_CHANGE_NAME_REQ</a></td><td>0x0E0EAA03</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHANGE_PLAYER_NAME_REQ.html'>CHANGE_PLAYER_NAME_REQ</a></td><td>0x0E0EAA04</td><td><a href='./CHANGE_PLAYER_NAME_RES.html'>CHANGE_PLAYER_NAME_RES</a></td><td>0x8E0EAA04</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CALL_FRIEND_DRINKING_REQ.html'>CALL_FRIEND_DRINKING_REQ</a></td><td>0x0E0EAA05</td><td><a href='./CALL_FRIEND_DRINKING_RES.html'>CALL_FRIEND_DRINKING_RES</a></td><td>0x8E0EAA05</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DRINKING_FRIEND_DO_REQ.html'>DRINKING_FRIEND_DO_REQ</a></td><td>0x0E0EAA06</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./PET_DAO_DATA_RES.html'>PET_DAO_DATA_RES</a></td><td>0x8E0EAA06</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ENTER_PET_DAO_REQ.html'>ENTER_PET_DAO_REQ</a></td><td>0x0E0EAA24</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_JIN_JIE_REQ.html'>PET_JIN_JIE_REQ</a></td><td>0x0E0EAA54</td><td><a href='./PET_JIN_JIE_RES.html'>PET_JIN_JIE_RES</a></td><td>0x8E0EAA54</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_LIST_REQ.html'>PET_LIST_REQ</a></td><td>0x0E0EAA55</td><td><a href='./PET_LIST_RES.html'>PET_LIST_RES</a></td><td>0x8E0EAA55</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_FIND_CUR_INDEX_REQ.html'>PET_FIND_CUR_INDEX_REQ</a></td><td>0x0E0EAA5A</td><td><a href='./PET_FIND_CUR_INDEX_RES.html'>PET_FIND_CUR_INDEX_RES</a></td><td>0x8E0EAA5A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_DETAIL_REQ.html'>PET_DETAIL_REQ</a></td><td>0x0E0EAA56</td><td><a href='./PET_DETAIL_RES.html'>PET_DETAIL_RES</a></td><td>0x8E0EAA56</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_CHONG_BAI_REQ.html'>PET_CHONG_BAI_REQ</a></td><td>0x0E0EAA57</td><td><a href='./PET_CHONG_BAI_RES.html'>PET_CHONG_BAI_RES</a></td><td>0x8E0EAA58</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_BOOK_UI_REQ.html'>PET_BOOK_UI_REQ</a></td><td>0x0E0EAA59</td><td><a href='./PET_BOOK_UI_RES.html'>PET_BOOK_UI_RES</a></td><td>0x8E0EAA59</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_SKILL_TAKE_REQ.html'>PET_SKILL_TAKE_REQ</a></td><td>0x0E0EAA60</td><td><a href='./PET_SKILL_TAKE_RES.html'>PET_SKILL_TAKE_RES</a></td><td>0x8E0EAA60</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_TALENT_UP_REQ.html'>PET_TALENT_UP_REQ</a></td><td>0x0E0EAA61</td><td><a href='./PET_TALENT_UP_RES.html'>PET_TALENT_UP_RES</a></td><td>0x8E0EAA61</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_SKILL_UP_REQ.html'>PET_SKILL_UP_REQ</a></td><td>0x0E0EAA62</td><td><a href='./PET_SKILL_UP_RES.html'>PET_SKILL_UP_RES</a></td><td>0x8E0EAA62</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET2_QUERY_BY_ARTICLE_REQ.html'>PET2_QUERY_BY_ARTICLE_REQ</a></td><td>0x0E0EAA63</td><td><a href='./-.html'>-</a></td><td>-</td><td>通过宠物物品id请求宠物</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET3_QUERY_BY_ARTICLE_REQ.html'>PET3_QUERY_BY_ARTICLE_REQ</a></td><td>0x0E0EAA7E</td><td><a href='./-.html'>-</a></td><td>-</td><td>通过宠物物品id请求宠物</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET2_LiZi_REQ.html'>PET2_LiZi_REQ</a></td><td>0x0E0EAA7F</td><td><a href='./PET2_LiZi_RES.html'>PET2_LiZi_RES</a></td><td>0x8E0EAA7F</td><td>请求宠物粒子信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET2_QUERY_REQ.html'>PET2_QUERY_REQ</a></td><td>0x0E0EAA64</td><td><a href='./PET2_QUERY_RES.html'>PET2_QUERY_RES</a></td><td>0x8E0EAA64</td><td>请求宠物信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_SKILLINFO_PET2_REQ.html'>QUERY_SKILLINFO_PET2_REQ</a></td><td>0x0E0EAA65</td><td><a href='./QUERY_SKILLINFO_PET2_RES.html'>QUERY_SKILLINFO_PET2_RES</a></td><td>0x8E0EAA65</td><td>请求宠物某个《天赋or天生》技能的详细信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET2_LIAN_HUN_REQ.html'>PET2_LIAN_HUN_REQ</a></td><td>0x0E0EAA66</td><td><a href='./PET2_LIAN_HUN_REs.html'>PET2_LIAN_HUN_REs</a></td><td>0x8E0EAA66</td><td>宠物炼魂，吃道具</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET2_HunArticle_REQ.html'>PET2_HunArticle_REQ</a></td><td>0x0E0EAA6B</td><td><a href='./PET2_HunArticle_RES.html'>PET2_HunArticle_RES</a></td><td>0x8E0EAA6B</td><td>宠物有哪些魂道具可以吃</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET2_LIAN_DAN_REQ.html'>PET2_LIAN_DAN_REQ</a></td><td>0x0E0EAA67</td><td><a href='./PET2_LIAN_DAN_RES.html'>PET2_LIAN_DAN_RES</a></td><td>0x8E0EAA67</td><td>把宠物转换为炼魂道具。</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET2_UP_LV_REQ.html'>PET2_UP_LV_REQ</a></td><td>0x0E0EAA69</td><td><a href='./PET2_UP_LV_RES.html'>PET2_UP_LV_RES</a></td><td>0x8E0EAA69</td><td>宠物炼魂升级</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET2_UI_DESC_REQ.html'>PET2_UI_DESC_REQ</a></td><td>0x0E0EAA68</td><td><a href='./PET2_UI_DESC_RES.html'>PET2_UI_DESC_RES</a></td><td>0x8E0EAA68</td><td>宠物界面描述</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET2_XueMai_REQ.html'>PET2_XueMai_REQ</a></td><td>0x0E0EAA6A</td><td><a href='./PET2_XueMai_RES.html'>PET2_XueMai_RES</a></td><td>0x8E0EAA6A</td><td>查询血脉</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ACTIVENESS_LIST_REQ.html'>ACTIVENESS_LIST_REQ</a></td><td>0x0E0EAA25</td><td><a href='./ACTIVENESS_LIST_RES.html'>ACTIVENESS_LIST_RES</a></td><td>0x8E0EAA07</td><td>活跃度活动列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./LOTTERY_REQ.html'>LOTTERY_REQ</a></td><td>0x0E0EAA26</td><td><a href='./LOTTERY_RES.html'>LOTTERY_RES</a></td><td>0x8E0EAA08</td><td>活跃度活动抽奖</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./LOTTERY_FINISH_REQ.html'>LOTTERY_FINISH_REQ</a></td><td>0x0E0EAA27</td><td><a href='./LOTTERY_FINISH_RES.html'>LOTTERY_FINISH_RES</a></td><td>0x8E0EAA10</td><td>活跃度活动抽奖完成</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ACTIVENESS_DES_REQ.html'>ACTIVENESS_DES_REQ</a></td><td>0x0E0EAA28</td><td><a href='./ACTIVENESS_DES_RES.html'>ACTIVENESS_DES_RES</a></td><td>0x8E0EAA11</td><td>活跃度查看活动详细介绍</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_PRIZE_REQ.html'>GET_PRIZE_REQ</a></td><td>0x0E0EAA29</td><td><a href='./GET_PRIZE_RES.html'>GET_PRIZE_RES</a></td><td>0x8E0EAA12</td><td>领取活动宝箱</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./INITDATA_MINIGAME_ACTIVITY_REQ.html'>INITDATA_MINIGAME_ACTIVITY_REQ</a></td><td>0x0E0EAA30</td><td><a href='./INITDATA_MINIGAME_ACTIVITY_RES.html'>INITDATA_MINIGAME_ACTIVITY_RES</a></td><td>0x8E0EAA30</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HANDLE_MINIGAME_ACTIVITY_REQ.html'>HANDLE_MINIGAME_ACTIVITY_REQ</a></td><td>0x0E0EAA31</td><td><a href='./HANDLE_MINIGAME_ACTIVITY_RES.html'>HANDLE_MINIGAME_ACTIVITY_RES</a></td><td>0x8E0EAA31</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BUY_LIFE_MINIGAME_ACTIVITY_REQ.html'>BUY_LIFE_MINIGAME_ACTIVITY_REQ</a></td><td>0x0E0EAA32</td><td><a href='./BUY_LIFE_MINIGAME_ACTIVITY_RES.html'>BUY_LIFE_MINIGAME_ACTIVITY_RES</a></td><td>0x8E0EAA32</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TIPS_FOR_MINIGAME_ACTIVITY_REQ.html'>TIPS_FOR_MINIGAME_ACTIVITY_REQ</a></td><td>0x0E0EAA39</td><td><a href='./TIPS_FOR_MINIGAME_ACTIVITY_RES.html'>TIPS_FOR_MINIGAME_ACTIVITY_RES</a></td><td>0x8E0EAA41</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./START_MINIGAME_ACTIVITY_REQ.html'>START_MINIGAME_ACTIVITY_REQ</a></td><td>0x0E0EAA36</td><td><a href='./START_MINIGAME_ACTIVITY_RES.html'>START_MINIGAME_ACTIVITY_RES</a></td><td>0x8E0EAA36</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./PET_DAO_NUM_INFO_RES.html'>PET_DAO_NUM_INFO_RES</a></td><td>0x8E0EAA33</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAN_GET_REQ.html'>CAN_GET_REQ</a></td><td>0x8E0EAA34</td><td><a href='./CAN_GET_RES.html'>CAN_GET_RES</a></td><td>0x8E0EAA37</td><td>是否有可领取的宝箱</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_SHOP_REQ.html'>GET_SHOP_REQ</a></td><td>0x8E0EAA35</td><td><a href='./GET_SHOP_RES.html'>GET_SHOP_RES</a></td><td>0x8E0EAA38</td><td>商店首界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NPC_ACTION_RES.html'>NPC_ACTION_RES</a></td><td>0x8E0EAA39</td><td>点击NPC播放动画</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SkEnh_INFO_REQ.html'>SkEnh_INFO_REQ</a></td><td>0x0E0EAA6F</td><td><a href='./SkEnh_INFO_RES.html'>SkEnh_INFO_RES</a></td><td>0x8E0EAA6F</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SkEnh_exINFO_REQ.html'>SkEnh_exINFO_REQ</a></td><td>0x0E0EAA6E</td><td><a href='./SkEnh_exINFO_RES.html'>SkEnh_exINFO_RES</a></td><td>0x8E0EAA6E</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SkEnh_Exp2point_REQ.html'>SkEnh_Exp2point_REQ</a></td><td>0x0E0EAA70</td><td><a href='./SkEnh_Exp2point_RES.html'>SkEnh_Exp2point_RES</a></td><td>0x8E0EAA70</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SkEnh_Add_point_REQ.html'>SkEnh_Add_point_REQ</a></td><td>0x0E0EAA71</td><td><a href='./SkEnh_Add_point_RES.html'>SkEnh_Add_point_RES</a></td><td>0x8E0EAA71</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SkEnh_Detail_REQ.html'>SkEnh_Detail_REQ</a></td><td>0x0E0EAA7A</td><td><a href='./SkEnh_Detail_RES.html'>SkEnh_Detail_RES</a></td><td>0x8E0EAA7A</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ENTER_ROBBERT_REQ.html'>ENTER_ROBBERT_REQ</a></td><td>0x0E0EAA81</td><td><a href='./ENTER_ROBBERT_RES.html'>ENTER_ROBBERT_RES</a></td><td>0x8E0EAA89</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./START_ROBBERT_REQ.html'>START_ROBBERT_REQ</a></td><td>0x0E0EAA91</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FEISHENG_END_REQ.html'>FEISHENG_END_REQ</a></td><td>0x0E0EAA95</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ROBBERY_TIPS_REQ.html'>ROBBERY_TIPS_REQ</a></td><td>0x0E0EAA84</td><td><a href='./ROBBERY_TIPS_RES.html'>ROBBERY_TIPS_RES</a></td><td>0x8E0EAA82</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTIFY_RAYROBBERY_REQ.html'>NOTIFY_RAYROBBERY_REQ</a></td><td>0x8E0EAA85</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端控制雷云的出现和消失（天劫）</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BACE_TOWN_IN_ROBBERT_REQ.html'>BACE_TOWN_IN_ROBBERT_REQ</a></td><td>0x8E0EAA94</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTIFY_CLIENT_VICTORY_TIPS_REQ.html'>NOTIFY_CLIENT_VICTORY_TIPS_REQ</a></td><td>0x8E0EAA92</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端弹出胜利、失败字体</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_CLIENT_ROBBERY_CLIENT_REQ.html'>NOTIFY_CLIENT_ROBBERY_CLIENT_REQ</a></td><td>0x8E0EAA93</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户渡劫胜利显示回城按钮</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTIFY_ROBBERY_COUNTDOWN_REQ.html'>NOTIFY_ROBBERY_COUNTDOWN_REQ</a></td><td>0x8E0EAA86</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端天劫相应倒计时</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_FEISHENG_DONGHUA_REQ.html'>NOTIFY_FEISHENG_DONGHUA_REQ</a></td><td>0x8E0EAA87</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器通知客户端飞升</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHANAGE_PLAYER_AVATA_REQ.html'>CHANAGE_PLAYER_AVATA_REQ</a></td><td>0x0E0EAA88</td><td><a href='./CHANAGE_PLAYER_AVATA_RES.html'>CHANAGE_PLAYER_AVATA_RES</a></td><td>0x8E0EAA88</td><td>切换avata</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SIGN_REQ.html'>SIGN_REQ</a></td><td>0x0E0EAA82</td><td><a href='./SIGN_RES.html'>SIGN_RES</a></td><td>0x8E0EAA72</td><td>签到</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_SIGNAWARD_REQ.html'>GET_SIGNAWARD_REQ</a></td><td>0x0E0EAA83</td><td><a href='./-.html'>-</a></td><td>-</td><td>签到领奖</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_WORLD__XJ_MAP_REQ.html'>QUERY_WORLD__XJ_MAP_REQ</a></td><td>0x0E0EAA85</td><td><a href='./QUERY_WORLD__XJ_MAP_RES.html'>QUERY_WORLD__XJ_MAP_RES</a></td><td>0x8E0EAA73</td><td>查询世界地图地域 </td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./IS_XJ_MAP_REQ.html'>IS_XJ_MAP_REQ</a></td><td>0x0E0EAA86</td><td><a href='./IS_XJ_MAP_RES.html'>IS_XJ_MAP_RES</a></td><td>0x8E0EAA74</td><td>查询世界地图地域 </td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_NEW_EQUIPMENT_STRONG_REQ.html'>QUERY_NEW_EQUIPMENT_STRONG_REQ</a></td><td>0x0E0EAA87</td><td><a href='./QUERY_NEW_EQUIPMENT_STRONG_RES.html'>QUERY_NEW_EQUIPMENT_STRONG_RES</a></td><td>0x8E0EAA76</td><td>客户端请求服务器，装备强化的各种信息，包括需要什么材料，需要几个，用什么来提高成功率，需要多少手续费等</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEW_EQUIPMENT_STRONG_REQ.html'>NEW_EQUIPMENT_STRONG_REQ</a></td><td>0x00F0EEF8</td><td><a href='./NEW_EQUIPMENT_STRONG_RES.html'>NEW_EQUIPMENT_STRONG_RES</a></td><td>0x70F0EEF8</td><td>客户端请求服务器，装备强化，服务端发送强化成功消息前必须再次发出QUERY_EQUIPMENT_STRONG_RES通知客户端材料变化</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./FIND_WAY_RES.html'>FIND_WAY_RES</a></td><td>0x70F0EF00</td><td>寻路</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./LOTTERY_FINISH_NEW_REQ.html'>LOTTERY_FINISH_NEW_REQ</a></td><td>0x00F0EF01</td><td><a href='./LOTTERY_FINISH_NEW_RES.html'>LOTTERY_FINISH_NEW_RES</a></td><td>0x70F0EF01</td><td>活跃度活动抽奖完成</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FUNCTION_OPEN_REQ.html'>FUNCTION_OPEN_REQ</a></td><td>0x00F0EF0F</td><td><a href='./FUNCTION_OPEN_RES.html'>FUNCTION_OPEN_RES</a></td><td>0x70F0EF0F</td><td>祈福是否开启</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SYNC_MAGICWEAPON_FOR_KNAPSACK_REQ.html'>SYNC_MAGICWEAPON_FOR_KNAPSACK_REQ</a></td><td>0x00F0EF02</td><td><a href='./SYNC_MAGICWEAPON_FOR_KNAPSACK_RES.html'>SYNC_MAGICWEAPON_FOR_KNAPSACK_RES</a></td><td>0x70F0EF02</td><td>同步背包中法宝信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_SHENSHI_REQ.html'>QUERY_SHENSHI_REQ</a></td><td>0x00F0EF03</td><td><a href='./QUERY_SHENSHI_RES.html'>QUERY_SHENSHI_RES</a></td><td>0x70F0EF03</td><td>请求神识</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CONFIRM_SHENSHI_REQ.html'>CONFIRM_SHENSHI_REQ</a></td><td>0x00F0EF04</td><td><a href='./CONFIRM_SHENSHI_RES.html'>CONFIRM_SHENSHI_RES</a></td><td>0x70F0EF04</td><td>确认神识</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_CLIENT_SUMMON_MAGICWEAPON_REQ.html'>NOTIFY_CLIENT_SUMMON_MAGICWEAPON_REQ</a></td><td>0x8E0EAA99</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户玩家装备上法宝</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_MAGICWEAPON_STRONG_REQ.html'>QUERY_MAGICWEAPON_STRONG_REQ</a></td><td>0x00F0EF05</td><td><a href='./QUERY_MAGICWEAPON_STRONG_RES.html'>QUERY_MAGICWEAPON_STRONG_RES</a></td><td>0x70F0EF05</td><td>法宝强化</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MAGICWEAPON_STRONG_REQ.html'>MAGICWEAPON_STRONG_REQ</a></td><td>0x00F0EF06</td><td><a href='./MAGICWEAPON_STRONG_RES.html'>MAGICWEAPON_STRONG_RES</a></td><td>0x70F0EF06</td><td>法宝强化确认</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_MAGICWEAPON_EAT_REQ.html'>QUERY_MAGICWEAPON_EAT_REQ</a></td><td>0x00F0EF07</td><td><a href='./QUERY_MAGICWEAPON_EAT_RES.html'>QUERY_MAGICWEAPON_EAT_RES</a></td><td>0x70F0EF07</td><td>法宝吞噬</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CONFIRM_MAGICWEAPON_EAT_REQ.html'>CONFIRM_MAGICWEAPON_EAT_REQ</a></td><td>0x00F0EF09</td><td><a href='./CONFIRM_MAGICWEAPON_EAT_RES.html'>CONFIRM_MAGICWEAPON_EAT_RES</a></td><td>0x70F0EF17</td><td>法宝吞噬确认</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_MAGICWEAPON_HIDDLE_PROP_REQ.html'>QUERY_MAGICWEAPON_HIDDLE_PROP_REQ</a></td><td>0x00F0EF18</td><td><a href='./QUERY_MAGICWEAPON_HIDDLE_PROP_RES.html'>QUERY_MAGICWEAPON_HIDDLE_PROP_RES</a></td><td>0x70F0EF18</td><td>隐藏属性</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIHUO_MAGICWEAPON_HIDDLE_PROP_REQ.html'>JIHUO_MAGICWEAPON_HIDDLE_PROP_REQ</a></td><td>0x00F0EF19</td><td><a href='./JIHUO_MAGICWEAPON_HIDDLE_PROP_RES.html'>JIHUO_MAGICWEAPON_HIDDLE_PROP_RES</a></td><td>0x70F0EF19</td><td>激活隐藏属性</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ.html'>REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ</a></td><td>0x00F0EF20</td><td><a href='./REFRESH_MAGICWEAPON_HIDDLE_PROP_RES.html'>REFRESH_MAGICWEAPON_HIDDLE_PROP_RES</a></td><td>0x70F0EF20</td><td>激活隐藏属性</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./OPEN_MAGICWEAPON_OPTION_RES.html'>OPEN_MAGICWEAPON_OPTION_RES</a></td><td>0x70F0EF21</td><td>打开法宝操作界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./OPEN_MAGICWEAPON_ZHULING_RES.html'>OPEN_MAGICWEAPON_ZHULING_RES</a></td><td>0x0E0EAA99</td><td><a href='./-.html'>-</a></td><td>-</td><td>打开法宝注灵界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_MAGICWEAPON_NAIJIU_REQ.html'>QUERY_MAGICWEAPON_NAIJIU_REQ</a></td><td>0x00F0EF26</td><td><a href='./QUERY_MAGICWEAPON_NAIJIU_RES.html'>QUERY_MAGICWEAPON_NAIJIU_RES</a></td><td>0x70F0EF26</td><td>请求法宝灵气值</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./COMFIRM_MAGICWEAPON_NAIJIU_REQ.html'>COMFIRM_MAGICWEAPON_NAIJIU_REQ</a></td><td>0x00F0EF27</td><td><a href='./COMFIRM_MAGICWEAPON_NAIJIU_RES.html'>COMFIRM_MAGICWEAPON_NAIJIU_RES</a></td><td>0x70F0EF27</td><td>补充法宝灵气</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_CLIENT_MAGICWEAPON_CHANGE_REQ.html'>NOTIFY_CLIENT_MAGICWEAPON_CHANGE_REQ</a></td><td>0x0E0EAA98</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端法宝有变化</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ENTER_DEVILSQUARE_REQ.html'>ENTER_DEVILSQUARE_REQ</a></td><td>0x0E0EAA96</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DEVILSQUARE_TIPS_REQ.html'>DEVILSQUARE_TIPS_REQ</a></td><td>0x0E0EAA89</td><td><a href='./-.html'>-</a></td><td>-</td><td>恶魔广场副本介绍内容,开启副本等级等</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DEVILSQUARE_COUNTDOWNTIME_REQ.html'>DEVILSQUARE_COUNTDOWNTIME_REQ</a></td><td>0x0E0EAA90</td><td><a href='./-.html'>-</a></td><td>-</td><td>恶魔广场中各种倒计时</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DEVILSQUARE_COMPOSE_TIPS_REQ.html'>DEVILSQUARE_COMPOSE_TIPS_REQ</a></td><td>0x0E0EAA93</td><td><a href='./-.html'>-</a></td><td>-</td><td>门票合成通知客户端</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ENTER_COMPOSE_REQ.html'>ENTER_COMPOSE_REQ</a></td><td>0x00F0EF23</td><td><a href='./ENTER_COMPOSE_RES.html'>ENTER_COMPOSE_RES</a></td><td>0x70F0EF23</td><td>物品合成</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./SKILL_POINT_SHOW_RES.html'>SKILL_POINT_SHOW_RES</a></td><td>0x70F0EF16</td><td>技能点显示</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GOD_EQUIPMENT_UPGRADE_REQ.html'>GOD_EQUIPMENT_UPGRADE_REQ</a></td><td>0x00F0EF24</td><td><a href='./GOD_EQUIPMENT_UPGRADE_RES.html'>GOD_EQUIPMENT_UPGRADE_RES</a></td><td>0x70F0EF24</td><td>仙装合成</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GOD_EQUIPMENT_UPGRADE_SURE_REQ.html'>GOD_EQUIPMENT_UPGRADE_SURE_REQ</a></td><td>0x00F0EF25</td><td><a href='./GOD_EQUIPMENT_UPGRADE_SURE_RES.html'>GOD_EQUIPMENT_UPGRADE_SURE_RES</a></td><td>0x70F0EF25</td><td>仙装合成确认</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./FAIRY_SHOW_ELECTORS_RES.html'>FAIRY_SHOW_ELECTORS_RES</a></td><td>0x70F0EF28</td><td>查看投票榜</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FAIRY_VOTE_REQ.html'>FAIRY_VOTE_REQ</a></td><td>0x00F0EF28</td><td><a href='./FAIRY_VOTE_RES.html'>FAIRY_VOTE_RES</a></td><td>0x70F0EF29</td><td>投票</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FAIRY_DECLARE_REQ.html'>FAIRY_DECLARE_REQ</a></td><td>0x00F0EF29</td><td><a href='./FAIRY_DECLARE_RES.html'>FAIRY_DECLARE_RES</a></td><td>0x70F0EF30</td><td>设置宣言</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FAIRY_THANK_REQ.html'>FAIRY_THANK_REQ</a></td><td>0x00F0EF30</td><td><a href='./FAIRY_THANK_RES.html'>FAIRY_THANK_RES</a></td><td>0x70F0EF31</td><td>打开一键答谢界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FAIRY_DOTHANK_REQ.html'>FAIRY_DOTHANK_REQ</a></td><td>0x00F0EF31</td><td><a href='./FAIRY_DOTHANK_RES.html'>FAIRY_DOTHANK_RES</a></td><td>0x70F0EF32</td><td>一键答谢</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FAIRY_VOTERECORD_REQ.html'>FAIRY_VOTERECORD_REQ</a></td><td>0x00F0EF33</td><td><a href='./FAIRY_VOTERECORD_RES.html'>FAIRY_VOTERECORD_RES</a></td><td>0x70F0EF33</td><td>查看投票记录</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./FAIRY_AWARD_RES.html'>FAIRY_AWARD_RES</a></td><td>0x70F0EF34</td><td>打开设置膜拜奖励等级界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FAIRY_SET_AWARDLEVEL_REQ.html'>FAIRY_SET_AWARDLEVEL_REQ</a></td><td>0x00F0EF32</td><td><a href='./FAIRY_SET_AWARDLEVEL_RES.html'>FAIRY_SET_AWARDLEVEL_RES</a></td><td>0x70F0EF35</td><td>设置膜拜奖励等级</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FAIRY_REFRESH_REQ.html'>FAIRY_REFRESH_REQ</a></td><td>0x00F0EF34</td><td><a href='./-.html'>-</a></td><td>-</td><td>请求刷新投票榜</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_ALL_AIMS_CHAPTER_REQ.html'>QUERY_ALL_AIMS_CHAPTER_REQ</a></td><td>0x00F0EF40</td><td><a href='./QUERY_ALL_AIMS_CHAPTER_RES.html'>QUERY_ALL_AIMS_CHAPTER_RES</a></td><td>0x70F0EF40</td><td>请求所有目标章节名（顺序）</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_ONE_CHAPTER_REQ.html'>QUERY_ONE_CHAPTER_REQ</a></td><td>0x00F0EF41</td><td><a href='./QUERY_ONE_CHAPTER_RES.html'>QUERY_ONE_CHAPTER_RES</a></td><td>0x70F0EF41</td><td>通过章节名请求章节信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_ONE_AIMS_REQ.html'>QUERY_ONE_AIMS_REQ</a></td><td>0x00F0EF42</td><td><a href='./QUERY_ONE_AIMS_RES.html'>QUERY_ONE_AIMS_RES</a></td><td>0x70F0EF42</td><td>请求单个目标信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./RECEIVE_AIM_REWARD_REQ.html'>RECEIVE_AIM_REWARD_REQ</a></td><td>0x00F0EF43</td><td><a href='./RECEIVE_AIM_REWARD_RES.html'>RECEIVE_AIM_REWARD_RES</a></td><td>0x70F0EF43</td><td>领取目标奖励</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_PARTICLE_REQ.html'>NOTICE_PARTICLE_REQ</a></td><td>0x0E0EAA97</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端显示目标按钮粒子（有未领取的目标奖励）</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_COMPLETE_AIM_REQ.html'>QUERY_COMPLETE_AIM_REQ</a></td><td>0x00F0EF44</td><td><a href='./QUERY_COMPLETE_AIM_RES.html'>QUERY_COMPLETE_AIM_RES</a></td><td>0x70F0EF44</td><td>达成目标或者查看别人发出的目标</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NOTICE_PLAYER_MUBIAO_CHANGE_RES.html'>NOTICE_PLAYER_MUBIAO_CHANGE_RES</a></td><td>0x70F0EF89</td><td>通知客户端玩家目标进度变化</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_HORSE_LIST2_REQ.html'>QUERY_HORSE_LIST2_REQ</a></td><td>0x00F0EF47</td><td><a href='./QUERY_HORSE_LIST2_RES.html'>QUERY_HORSE_LIST2_RES</a></td><td>0x70F0EF47</td><td>客户端向服务器发送查询马匹列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_HORSE_SELFCHANGE_REQ.html'>NOTIFY_HORSE_SELFCHANGE_REQ</a></td><td>0x00F0EF48</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器向客户端发送，通知客户端坐骑自身变量的改变</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_HORSE_DATA_REQ.html'>QUERY_HORSE_DATA_REQ</a></td><td>0x00F0EF49</td><td><a href='./QUERY_HORSE_DATA_RES.html'>QUERY_HORSE_DATA_RES</a></td><td>0x70F0EF49</td><td>请求坐骑相关数据</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_SKILLS_MAP_REQ.html'>QUERY_SKILLS_MAP_REQ</a></td><td>0x00F0EF50</td><td><a href='./QUERY_SKILLS_MAP_RES.html'>QUERY_SKILLS_MAP_RES</a></td><td>0x70F0EF50</td><td>请求技能列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HORSE_BLOODSTAR_STRONG_REQ.html'>HORSE_BLOODSTAR_STRONG_REQ</a></td><td>0x00F0EF51</td><td><a href='./HORSE_BLOODSTAR_STRONG_RES.html'>HORSE_BLOODSTAR_STRONG_RES</a></td><td>0x70F0EF51</td><td>进行血脉升星</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HORSE_RANKSTAR_STRONG_REQ.html'>HORSE_RANKSTAR_STRONG_REQ</a></td><td>0x00F0EF55</td><td><a href='./HORSE_RANKSTAR_STRONG_RES.html'>HORSE_RANKSTAR_STRONG_RES</a></td><td>0x70F0EF55</td><td>进行阶升星</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REFRESH_HORSE_SKILL_REQ.html'>REFRESH_HORSE_SKILL_REQ</a></td><td>0x00F0EF56</td><td><a href='./REFRESH_HORSE_SKILL_RES.html'>REFRESH_HORSE_SKILL_RES</a></td><td>0x70F0EF56</td><td>刷新坐骑技能</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./LEARN_HORSE_SKILL_REQ.html'>LEARN_HORSE_SKILL_REQ</a></td><td>0x00F0EF57</td><td><a href='./LEARN_HORSE_SKILL_RES.html'>LEARN_HORSE_SKILL_RES</a></td><td>0x70F0EF57</td><td>学习刷新出来的技能</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HORSE_COLOR_STRONG_REQ.html'>HORSE_COLOR_STRONG_REQ</a></td><td>0x00F0EF58</td><td><a href='./HORSE_COLOR_STRONG_RES.html'>HORSE_COLOR_STRONG_RES</a></td><td>0x70F0EF58</td><td>升级坐骑颜色</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./UPGRADE_HORSE_SKILL_REQ.html'>UPGRADE_HORSE_SKILL_REQ</a></td><td>0x00F0EF59</td><td><a href='./UPGRADE_HORSE_SKILL_RES.html'>UPGRADE_HORSE_SKILL_RES</a></td><td>0x70F0EF59</td><td>升级坐骑技能</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_HORSE_SKILL_INFO_SHOW_REQ.html'>GET_HORSE_SKILL_INFO_SHOW_REQ</a></td><td>0x00F0EF60</td><td><a href='./GET_HORSE_SKILL_INFO_SHOW_RES.html'>GET_HORSE_SKILL_INFO_SHOW_RES</a></td><td>0x70F0EF60</td><td>请求坐骑技能描述</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_HORSE_SKILL_WINDOW_INFO_REQ.html'>GET_HORSE_SKILL_WINDOW_INFO_REQ</a></td><td>0x00F0EF61</td><td><a href='./GET_HORSE_SKILL_WINDOW_INFO_RES.html'>GET_HORSE_SKILL_WINDOW_INFO_RES</a></td><td>0x70F0EF61</td><td>打开坐骑花道具刷新技能界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_HORSE_RANK_WINDOW_INFO_REQ.html'>GET_HORSE_RANK_WINDOW_INFO_REQ</a></td><td>0x00F0EF62</td><td><a href='./GET_HORSE_RANK_WINDOW_INFO_RES.html'>GET_HORSE_RANK_WINDOW_INFO_RES</a></td><td>0x70F0EF62</td><td>打开坐骑阶培养界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_HORSE_BLOOD_WINDOW_INFO_REQ.html'>GET_HORSE_BLOOD_WINDOW_INFO_REQ</a></td><td>0x00F0EF63</td><td><a href='./GET_HORSE_BLOOD_WINDOW_INFO_RES.html'>GET_HORSE_BLOOD_WINDOW_INFO_RES</a></td><td>0x70F0EF63</td><td>打开坐骑血脉升星界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_HORSE_NEED_ARTICLE_WIND_REQ.html'>GET_HORSE_NEED_ARTICLE_WIND_REQ</a></td><td>0x00F0EF64</td><td><a href='./GET_HORSE_NEED_ARTICLE_WIND_RES.html'>GET_HORSE_NEED_ARTICLE_WIND_RES</a></td><td>0x70F0EF64</td><td>获取成长、升级技能需要物品描述窗口信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_HORSE_SKILL_OPEN_INFO_REQ.html'>GET_HORSE_SKILL_OPEN_INFO_REQ</a></td><td>0x00F0EF65</td><td><a href='./GET_HORSE_SKILL_OPEN_INFO_RES.html'>GET_HORSE_SKILL_OPEN_INFO_RES</a></td><td>0x70F0EF65</td><td>技能格开启条件请求</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_HORSE_HELP_INFO_REQ.html'>GET_HORSE_HELP_INFO_REQ</a></td><td>0x00F0EF75</td><td><a href='./GET_HORSE_HELP_INFO_RES.html'>GET_HORSE_HELP_INFO_RES</a></td><td>0x70F0EF75</td><td>请求坐骑帮助描述</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_HORSE_COLOR_STRONG_WIND_REQ.html'>GET_HORSE_COLOR_STRONG_WIND_REQ</a></td><td>0x00F0EF76</td><td><a href='./GET_HORSE_COLOR_STRONG_WIND_RES.html'>GET_HORSE_COLOR_STRONG_WIND_RES</a></td><td>0x70F0EF76</td><td>获取升级技能需要物品描述窗口信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_ARTICLE_EXCHANGE_REQ.html'>QUERY_ARTICLE_EXCHANGE_REQ</a></td><td>0x00F0EF52</td><td><a href='./QUERY_ARTICLE_EXCHANGE_RES.html'>QUERY_ARTICLE_EXCHANGE_RES</a></td><td>0x70F0EF52</td><td>物品合成请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CONFIRM_ARTICLE_EXCHANGE_REQ.html'>CONFIRM_ARTICLE_EXCHANGE_REQ</a></td><td>0x00F0EF53</td><td><a href='./CONFIRM_ARTICLE_EXCHANGE_RES.html'>CONFIRM_ARTICLE_EXCHANGE_RES</a></td><td>0x70F0EF53</td><td>物品合成确认</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SEAL_TASK_INFO_REQ.html'>SEAL_TASK_INFO_REQ</a></td><td>0x00F0EF54</td><td><a href='./SEAL_TASK_INFO_RES.html'>SEAL_TASK_INFO_RES</a></td><td>0x70F0EF54</td><td>封印任务描述信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./COLLECT_MATERIAL_FOR_BOSS_REQ.html'>COLLECT_MATERIAL_FOR_BOSS_REQ</a></td><td>0x00F0EF66</td><td><a href='./COLLECT_MATERIAL_FOR_BOSS_RES.html'>COLLECT_MATERIAL_FOR_BOSS_RES</a></td><td>0x70F0EF66</td><td>捐献材料削弱boss</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CONFIRM_COLLENT_MATERIAL_FOR_BOSS_REQ.html'>CONFIRM_COLLENT_MATERIAL_FOR_BOSS_REQ</a></td><td>0x00F0EF67</td><td><a href='./CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES.html'>CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES</a></td><td>0x70F0EF67</td><td>确认捐献材料削弱boss</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ACTIVITY_FIRST_PAGE_REQ.html'>ACTIVITY_FIRST_PAGE_REQ</a></td><td>0x00F0EF68</td><td><a href='./ACTIVITY_FIRST_PAGE_RES.html'>ACTIVITY_FIRST_PAGE_RES</a></td><td>0x70F0EF68</td><td>活动首界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_ACTIVITY_INFO_REQ.html'>GET_ACTIVITY_INFO_REQ</a></td><td>0x00F0EF99</td><td><a href='./GET_ACTIVITY_INFO_RES.html'>GET_ACTIVITY_INFO_RES</a></td><td>0x70F0EF99</td><td>请求活动</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SEAL_TASK_STAT_REQ.html'>SEAL_TASK_STAT_REQ</a></td><td>0x00F0EF70</td><td><a href='./SEAL_TASK_STAT_RES.html'>SEAL_TASK_STAT_RES</a></td><td>0x70F0EF70</td><td>封印任务状态</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTIFY_SHOW_DOWNCITY_TUN_REQ.html'>NOTIFY_SHOW_DOWNCITY_TUN_REQ</a></td><td>0x00F0EF71</td><td><a href='./-.html'>-</a></td><td>-</td><td>服务器向客户端发送，通知打开通过副本奖励转盘</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAY_DOWNCITY_TUN_REQ.html'>PLAY_DOWNCITY_TUN_REQ</a></td><td>0x00F0EF72</td><td><a href='./PLAY_DOWNCITY_TUN_RES.html'>PLAY_DOWNCITY_TUN_RES</a></td><td>0x70F0EF72</td><td>转盘抽奖</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./GET_ACTIVITY_MESS_RES.html'>GET_ACTIVITY_MESS_RES</a></td><td>0x70F0EF73</td><td>活动信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_ACTIVITY_STAT_REQ.html'>NOTICE_ACTIVITY_STAT_REQ</a></td><td>0x00F0EF74</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知活动状态</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./OPEN_WAFEN_ACTIVITY_WINDOW_REQ.html'>OPEN_WAFEN_ACTIVITY_WINDOW_REQ</a></td><td>0x00F0EF78</td><td><a href='./OPEN_WAFEN_ACTIVITY_WINDOW_RES.html'>OPEN_WAFEN_ACTIVITY_WINDOW_RES</a></td><td>0x70F0EF78</td><td>打开挖坟活动界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_ONE_FENMU_INFO_REQ.html'>GET_ONE_FENMU_INFO_REQ</a></td><td>0x00F0EF79</td><td><a href='./GET_ONE_FENMU_INFO_RES.html'>GET_ONE_FENMU_INFO_RES</a></td><td>0x70F0EF79</td><td>请求单个坟墓信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DIG_FENMU_REQ.html'>DIG_FENMU_REQ</a></td><td>0x00F0EF80</td><td><a href='./DIG_FENMU_RES.html'>DIG_FENMU_RES</a></td><td>0x70F0EF80</td><td>挖坟</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./RECEIVE_FENMU_REWARD_REQ.html'>RECEIVE_FENMU_REWARD_REQ</a></td><td>0x00F0EF81</td><td><a href='./RECEIVE_FENMU_REWARD_RES.html'>RECEIVE_FENMU_REWARD_RES</a></td><td>0x70F0EF81</td><td>一键领取所有挖出来的奖励</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./EXCHANGE_CHANZI_REQ.html'>EXCHANGE_CHANZI_REQ</a></td><td>0x00F0EF82</td><td><a href='./EXCHANGE_CHANZI_RES.html'>EXCHANGE_CHANZI_RES</a></td><td>0x70F0EF82</td><td>兑换铲子</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DIG_FENMU_TEN_REQ.html'>DIG_FENMU_TEN_REQ</a></td><td>0x00F0EF84</td><td><a href='./DIG_FENMU_TEN_RES.html'>DIG_FENMU_TEN_RES</a></td><td>0x70F0EF84</td><td>十连挖</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BICHAN_ARTICLE_REQ.html'>BICHAN_ARTICLE_REQ</a></td><td>0x00F0EF85</td><td><a href='./BICHAN_ARTICLE_RES.html'>BICHAN_ARTICLE_RES</a></td><td>0x70F0EF85</td><td>请求坟墓必产物品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_TITLES_List_REQ.html'>PLAYER_TITLES_List_REQ</a></td><td>0x00F0EF86</td><td><a href='./PLAYER_TITLES_List_RES.html'>PLAYER_TITLES_List_RES</a></td><td>0x70F0EF86</td><td>请求玩家称号列表</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_TITLE_PRODUCE_REQ.html'>QUERY_TITLE_PRODUCE_REQ</a></td><td>0x00F0EF88</td><td><a href='./QUERY_TITLE_PRODUCE_RES.html'>QUERY_TITLE_PRODUCE_RES</a></td><td>0x70F0EF88</td><td>请求称号产出</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_ACTIVITY_BUTTON_STAT_REQ.html'>NOTICE_ACTIVITY_BUTTON_STAT_REQ</a></td><td>0x00F0EF83</td><td><a href='./NOTICE_ACTIVITY_BUTTON_STAT_RES.html'>NOTICE_ACTIVITY_BUTTON_STAT_RES</a></td><td>0x70F0EF83</td><td>活动按钮状态</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ARTICLE_INFO_REQ.html'>ARTICLE_INFO_REQ</a></td><td>0x00FF0000</td><td><a href='./ARTICLE_INFO_RES.html'>ARTICLE_INFO_RES</a></td><td>0x70FF0000</td><td>查询物品属性</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_VIP_NOTICE_INFO_REQ.html'>GET_VIP_NOTICE_INFO_REQ</a></td><td>0x00FF0001</td><td><a href='./GET_VIP_NOTICE_INFO_RES.html'>GET_VIP_NOTICE_INFO_RES</a></td><td>0x70FF0001</td><td>获取vip公告信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./ARTICLE_REMOVE_SUCC_RES.html'>ARTICLE_REMOVE_SUCC_RES</a></td><td>0x70FF0002</td><td>物品是否删除成功</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REFRESH_SHOP_GOODS_REQ.html'>REFRESH_SHOP_GOODS_REQ</a></td><td>0x00FF0009</td><td><a href='./REFRESH_SHOP_GOODS_RES.html'>REFRESH_SHOP_GOODS_RES</a></td><td>0x70FF0009</td><td>花钱刷新商店物品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CAN_REFRESH_SHOP_NAME_REQ.html'>CAN_REFRESH_SHOP_NAME_REQ</a></td><td>0x00FF0010</td><td><a href='./CAN_REFRESH_SHOP_NAME_RES.html'>CAN_REFRESH_SHOP_NAME_RES</a></td><td>0x70FF0010</td><td>请求有刷新道具功能的商店名</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_INFO_REQ2.html'>JIAZU_INFO_REQ2</a></td><td>0x00FF0015</td><td><a href='./JIAZU_INFO_RES2.html'>JIAZU_INFO_RES2</a></td><td>0x70FF0015</td><td>查询家族信息2</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_INFO_ADD_TIME_REQ2.html'>JIAZU_INFO_ADD_TIME_REQ2</a></td><td>0x00FF0016</td><td><a href='./JIAZU_INFO_ADD_TIME_RES2.html'>JIAZU_INFO_ADD_TIME_RES2</a></td><td>0x70FF0016</td><td>查询家族信息(2014年8月14日10:03:56)</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_BIAOCHE_QIANGHUA_INFO_REQ.html'>JIAZU_BIAOCHE_QIANGHUA_INFO_REQ</a></td><td>0x00FF0017</td><td><a href='./JIAZU_BIAOCHE_QIANGHUA_INFO_RES.html'>JIAZU_BIAOCHE_QIANGHUA_INFO_RES</a></td><td>0x70FF0017</td><td>家族镖车强化信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_BIAOCHE_QIANGHUA_REQ.html'>JIAZU_BIAOCHE_QIANGHUA_REQ</a></td><td>0x00FF0018</td><td><a href='./JIAZU_BIAOCHE_QIANGHUA_RES.html'>JIAZU_BIAOCHE_QIANGHUA_RES</a></td><td>0x70FF0018</td><td>家族镖车强化</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BRIGHTINLAY_EXCAVATE_REQ.html'>BRIGHTINLAY_EXCAVATE_REQ</a></td><td>0x00FF0034</td><td><a href='./BRIGHTINLAY_EXCAVATE_RES.html'>BRIGHTINLAY_EXCAVATE_RES</a></td><td>0x70FF0034</td><td>光效宝石镶嵌挖取</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_XIULIAN_INFO_REQ.html'>JIAZU_XIULIAN_INFO_REQ</a></td><td>0x00FF0035</td><td><a href='./JIAZU_XIULIAN_INFO_RES.html'>JIAZU_XIULIAN_INFO_RES</a></td><td>0x70FF0035</td><td>家族修炼信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_XIULIAN_REQ.html'>JIAZU_XIULIAN_REQ</a></td><td>0x00FF0036</td><td><a href='./JIAZU_XIULIAN_RES.html'>JIAZU_XIULIAN_RES</a></td><td>0x70FF0036</td><td>进行家族修炼</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_JINENG_INFO_REQ.html'>JIAZU_JINENG_INFO_REQ</a></td><td>0x00FF0037</td><td><a href='./JIAZU_JINENG_INFO_RES.html'>JIAZU_JINENG_INFO_RES</a></td><td>0x70FF0037</td><td>家族技能信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_JINENG_REQ.html'>JIAZU_JINENG_REQ</a></td><td>0x00FF0038</td><td><a href='./JIAZU_JINENG_RES.html'>JIAZU_JINENG_RES</a></td><td>0x70FF0038</td><td>学习家族技能</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_TASK_WINDOW_OPEN_REQ.html'>JIAZU_TASK_WINDOW_OPEN_REQ</a></td><td>0x00FF0039</td><td><a href='./JIAZU_TASK_WINDOW_OPEN_RES.html'>JIAZU_TASK_WINDOW_OPEN_RES</a></td><td>0x70FF0039</td><td>打开家族任务面板</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_TASK_JIEQU_REQ.html'>JIAZU_TASK_JIEQU_REQ</a></td><td>0x00FF0040</td><td><a href='./JIAZU_TASK_JIEQU_RES.html'>JIAZU_TASK_JIEQU_RES</a></td><td>0x70FF0040</td><td>接取任务</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_TASK_REFRESH_REQ.html'>JIAZU_TASK_REFRESH_REQ</a></td><td>0x00FF0041</td><td><a href='./JIAZU_TASK_REFRESH_RES.html'>JIAZU_TASK_REFRESH_RES</a></td><td>0x70FF0041</td><td>刷新任务列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_JIAZU_SKILL_INFO_REQ.html'>GET_JIAZU_SKILL_INFO_REQ</a></td><td>0x00FF0043</td><td><a href='./GET_JIAZU_SKILL_INFO_RES.html'>GET_JIAZU_SKILL_INFO_RES</a></td><td>0x70FF0043</td><td>家族技能泡泡</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ACTIVITY_FIRST_PAGE2_REQ.html'>ACTIVITY_FIRST_PAGE2_REQ</a></td><td>0x00FF0046</td><td><a href='./ACTIVITY_FIRST_PAGE2_RES.html'>ACTIVITY_FIRST_PAGE2_RES</a></td><td>0x70FF0046</td><td>活动首界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_PINGTAI_PARAM_REQ.html'>GET_PINGTAI_PARAM_REQ</a></td><td>0x00FF0049</td><td><a href='./GET_PINGTAI_PARAM_RES.html'>GET_PINGTAI_PARAM_RES</a></td><td>0x70FF0049</td><td>获取第三方交易平台参数</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HIDDEN_POPWINDOW_REQ.html'>HIDDEN_POPWINDOW_REQ</a></td><td>0x00FF0058</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端通知服务器玩家屏蔽某些选项</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_AUTOFEED_HORSE_REQ.html'>NOTIFY_AUTOFEED_HORSE_REQ</a></td><td>0x00FF0059</td><td><a href='./-.html'>-</a></td><td>-</td><td>客户端通知服务器玩家屏蔽某些选项</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JIAZU_FENGYING_STATUS_REQ.html'>JIAZU_FENGYING_STATUS_REQ</a></td><td>0x00FF0063</td><td><a href='./JIAZU_FENGYING_STATUS__RES.html'>JIAZU_FENGYING_STATUS__RES</a></td><td>0x70FF0063</td><td>请求家族封印状态</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./COMMON_RELEVANT_DES_REQ.html'>COMMON_RELEVANT_DES_REQ</a></td><td>0x00FF0064</td><td><a href='./COMMON_RELEVANT_DES_RES.html'>COMMON_RELEVANT_DES_RES</a></td><td>0x70FF0064</td><td>请求家族界面相关描述</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_NEW_SHOP_REQ.html'>GET_NEW_SHOP_REQ</a></td><td>0x00FF0065</td><td><a href='./GET_NEW_SHOP_RES.html'>GET_NEW_SHOP_RES</a></td><td>0x70FF0065</td><td>商店首界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ACTIVITY_EXTEND_REQ.html'>ACTIVITY_EXTEND_REQ</a></td><td>0x00FF0067</td><td><a href='./-.html'>-</a></td><td>-</td><td>活动扩张</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MORE_ARGS_ORDER_STATUS_CHANGE_REQ.html'>MORE_ARGS_ORDER_STATUS_CHANGE_REQ</a></td><td>0x00FF0068</td><td><a href='./-.html'>-</a></td><td>-</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MONTH_CARD_ACTIVITY_REQ.html'>MONTH_CARD_ACTIVITY_REQ</a></td><td>0x00FF0114</td><td><a href='./MONTH_CARD_ACTIVITY_RES.html'>MONTH_CARD_ACTIVITY_RES</a></td><td>0x70FF0114</td><td>月卡活动</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MONTH_CARD_ACTIVITY_GET_REWARD_REQ.html'>MONTH_CARD_ACTIVITY_GET_REWARD_REQ</a></td><td>0x00FF0069</td><td><a href='./MONTH_CARD_ACTIVITY_GET_REWARD_RES.html'>MONTH_CARD_ACTIVITY_GET_REWARD_RES</a></td><td>0x70FF0069</td><td>领取奖励</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MONTH_CARD_ACTIVITY_BUY_REQ.html'>MONTH_CARD_ACTIVITY_BUY_REQ</a></td><td>0x00FF0070</td><td><a href='./MONTH_CARD_ACTIVITY_BUY_RES.html'>MONTH_CARD_ACTIVITY_BUY_RES</a></td><td>0x70FF0070</td><td>购买月卡</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./OPEN_DAILY_TURN_WINDOW_REQ.html'>OPEN_DAILY_TURN_WINDOW_REQ</a></td><td>0x00FF0071</td><td><a href='./OPEN_DAILY_TURN_WINDOW_RES.html'>OPEN_DAILY_TURN_WINDOW_RES</a></td><td>0x70FF0071</td><td>打开每日转盘活动界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAY_DAILY_TURN_REQ.html'>PLAY_DAILY_TURN_REQ</a></td><td>0x00FF0072</td><td><a href='./PLAY_DAILY_TURN_RES.html'>PLAY_DAILY_TURN_RES</a></td><td>0x70FF0072</td><td>抽奖</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_ONE_DAILY_TURN_REQ.html'>GET_ONE_DAILY_TURN_REQ</a></td><td>0x00FF0073</td><td><a href='./GET_ONE_DAILY_TURN_RES.html'>GET_ONE_DAILY_TURN_RES</a></td><td>0x70FF0073</td><td>获取单个转盘信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BUY_EXTRA_TIMES4TURN_REQ.html'>BUY_EXTRA_TIMES4TURN_REQ</a></td><td>0x00FF0075</td><td><a href='./BUY_EXTRA_TIMES4TURN_RES.html'>BUY_EXTRA_TIMES4TURN_RES</a></td><td>0x70FF0075</td><td>购买额外参与次数</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./CHARGE_AGRS_RES.html'>CHARGE_AGRS_RES</a></td><td>0x70FF0074</td><td>充值参数</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./OPEN_TREASUREACTIVITY_WINDOW_REQ.html'>OPEN_TREASUREACTIVITY_WINDOW_REQ</a></td><td>0x00FF0076</td><td><a href='./OPEN_TREASUREACTIVITY_WINDOW_RES.html'>OPEN_TREASUREACTIVITY_WINDOW_RES</a></td><td>0x70FF0076</td><td>打开极地寻宝界面窗口</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_ONE_TREASUREACTIVITY_INFO_REQ.html'>GET_ONE_TREASUREACTIVITY_INFO_REQ</a></td><td>0x00FF0077</td><td><a href='./GET_ONE_TREASUREACTIVITY_INFO_RES.html'>GET_ONE_TREASUREACTIVITY_INFO_RES</a></td><td>0x70FF0077</td><td>获取单个宝藏信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAY_TREASUREACTIVITY_REQ.html'>PLAY_TREASUREACTIVITY_REQ</a></td><td>0x00FF0078</td><td><a href='./PLAY_TREASUREACTIVITY_RES.html'>PLAY_TREASUREACTIVITY_RES</a></td><td>0x70FF0078</td><td>挖取宝藏</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./OPEN_LEVELUPREWARD_WINDOW_REQ.html'>OPEN_LEVELUPREWARD_WINDOW_REQ</a></td><td>0x00FF0079</td><td><a href='./OPEN_LEVELUPREWARD_WINDOW_RES.html'>OPEN_LEVELUPREWARD_WINDOW_RES</a></td><td>0x70FF0079</td><td>打开冲级红利界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BUY_LEVELUPREWARD_GOOD_REQ.html'>BUY_LEVELUPREWARD_GOOD_REQ</a></td><td>0x00FF0080</td><td><a href='./-.html'>-</a></td><td>-</td><td>购买冲级返利</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./CHARGE_GET_PACKAGE_RES.html'>CHARGE_GET_PACKAGE_RES</a></td><td>0x70FF0081</td><td>打开冲级红利界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CHARGE_GET_PACKAGE_SURE_REQ.html'>CHARGE_GET_PACKAGE_SURE_REQ</a></td><td>0x00FF0082</td><td><a href='./-.html'>-</a></td><td>-</td><td>确定充值购买礼包</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_NEWPLAYERCAREERS_REQ.html'>GET_NEWPLAYERCAREERS_REQ</a></td><td>0x00FF0083</td><td><a href='./GET_NEWPLAYERCAREERS_RES.html'>GET_NEWPLAYERCAREERS_RES</a></td><td>0x70FF0083</td><td>客户端请求新职业</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_CLIENT_BIANSHEN_CD_REQ.html'>NOTICE_CLIENT_BIANSHEN_CD_REQ</a></td><td>0x00FF0088</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端兽魁变身按钮CD时间</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NOTICE_CLIENT_PLAYE_CARTOON_REQ.html'>NOTICE_CLIENT_PLAYE_CARTOON_REQ</a></td><td>0x00FF0087</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端播放动画</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_SHOUKUI_COMMENSKILLS_REQ.html'>GET_SHOUKUI_COMMENSKILLS_REQ</a></td><td>0x00FF0089</td><td><a href='./GET_SHOUKUI_COMMENSKILLS_RES.html'>GET_SHOUKUI_COMMENSKILLS_RES</a></td><td>0x70FF0089</td><td>客户端请求新职业</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SWITCH_BIANSHENJI_SKILLS_REQ.html'>SWITCH_BIANSHENJI_SKILLS_REQ</a></td><td>0x00FF0084</td><td><a href='./SWITCH_BIANSHENJI_SKILLS_RES.html'>SWITCH_BIANSHENJI_SKILLS_RES</a></td><td>0x70FF0084</td><td>切换变身技能</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./QUERY_CAREER_BIANSHEN_INFO_RES.html'>QUERY_CAREER_BIANSHEN_INFO_RES</a></td><td>0x70FF0085</td><td>因为上面那条协议太长发不过去新加的只发变身协议</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_NEW_PLAYER_REQ.html'>QUERY_NEW_PLAYER_REQ</a></td><td>0x00FF0086</td><td><a href='./QUERY_NEW_PLAYER_RES.html'>QUERY_NEW_PLAYER_RES</a></td><td>0x70FF0086</td><td>客户端向服务器发送查询角色的请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./RESET_BIANSHEN_SKILLS_RES.html'>RESET_BIANSHEN_SKILLS_RES</a></td><td>0x70FF0087</td><td>重设变身技能</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./TOURNAMENT_QUERY_PLAYER_ICON_INFO_RES.html'>TOURNAMENT_QUERY_PLAYER_ICON_INFO_RES</a></td><td>0x70FF0088</td><td>武圣请求头像信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_ENCHANT_DESCRIPTION_REQ.html'>GET_ENCHANT_DESCRIPTION_REQ</a></td><td>0x00FF0091</td><td><a href='./SGET_ENCHANT_DESCRIPTION_RES.html'>SGET_ENCHANT_DESCRIPTION_RES</a></td><td>0x70FF0091</td><td>请求附魔规则</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_ARTICLE_DES_REQ.html'>GET_ARTICLE_DES_REQ</a></td><td>0x00FF0092</td><td><a href='./GET_ARTICLE_DES_RES.html'>GET_ARTICLE_DES_RES</a></td><td>0x70FF0092</td><td>请求附魔道具（装备）描述</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ENCHANT_EQUIPMENT_REQ.html'>ENCHANT_EQUIPMENT_REQ</a></td><td>0x00FF0093</td><td><a href='./ENCHANT_EQUIPMENT_RES.html'>ENCHANT_EQUIPMENT_RES</a></td><td>0x70FF0093</td><td>附魔</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./OPEN_ENCHANT_LOCK_WINDOW_REQ.html'>OPEN_ENCHANT_LOCK_WINDOW_REQ</a></td><td>0x00FF0094</td><td><a href='./OPEN_ENCHANT_LOCK_WINDOW_RES.html'>OPEN_ENCHANT_LOCK_WINDOW_RES</a></td><td>0x70FF0094</td><td>打开锁定界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ENCHANT_LOCK_REQ.html'>ENCHANT_LOCK_REQ</a></td><td>0x00FF0095</td><td><a href='./ENCHANT_LOCK_RES.html'>ENCHANT_LOCK_RES</a></td><td>0x70FF0095</td><td>锁定附魔</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ENCHANT_UNLOCK_REQ.html'>ENCHANT_UNLOCK_REQ</a></td><td>0x00FF0096</td><td><a href='./ENCHANT_UNLOCK_RES.html'>ENCHANT_UNLOCK_RES</a></td><td>0x70FF0096</td><td>解锁附魔</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ENCHANT_ALL_EQUIPT_REQ.html'>ENCHANT_ALL_EQUIPT_REQ</a></td><td>0x00FF0100</td><td><a href='./ENCHANT_ALL_EQUIPT_RES.html'>ENCHANT_ALL_EQUIPT_RES</a></td><td>0x70FF0100</td><td>一键解锁（锁定）</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./JUBAO_PLAYER_REQ.html'>JUBAO_PLAYER_REQ</a></td><td>0x00FF0097</td><td><a href='./JUBAO_PLAYER_RES.html'>JUBAO_PLAYER_RES</a></td><td>0x70FF0097</td><td>举报玩家</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XILIAN_PAGE_REQ.html'>XILIAN_PAGE_REQ</a></td><td>0x00FF0098</td><td><a href='./XILIAN_PAGE_RES.html'>XILIAN_PAGE_RES</a></td><td>0x70FF0098</td><td>宝石洗练界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./XILIAN_CONFIRM_REQ.html'>XILIAN_CONFIRM_REQ</a></td><td>0x00FF0099</td><td><a href='./XILIAN_CONFIRM_RES.html'>XILIAN_CONFIRM_RES</a></td><td>0x70FF0099</td><td>宝石洗练</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./OPEN_SHOUHUN_WINDOW_REQ.html'>OPEN_SHOUHUN_WINDOW_REQ</a></td><td>0x00FF0101</td><td><a href='./OPEN_SHOUHUN_WINDOW_RES.html'>OPEN_SHOUHUN_WINDOW_RES</a></td><td>0x70FF0101</td><td>兽魂主界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REPLACE_ALL_SHOUHUN_REQ.html'>REPLACE_ALL_SHOUHUN_REQ</a></td><td>0x00FF0102</td><td><a href='./REPLACE_ALL_SHOUHUN_RES.html'>REPLACE_ALL_SHOUHUN_RES</a></td><td>0x70FF0102</td><td>一键替换装备兽魂（最优）</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./REPLACE_ONE_SHOUHUN_REQ.html'>REPLACE_ONE_SHOUHUN_REQ</a></td><td>0x00FF0103</td><td><a href='./REPLACE_ONE_SHOUHUN_RES.html'>REPLACE_ONE_SHOUHUN_RES</a></td><td>0x70FF0103</td><td>单个装备兽魂</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SHOUHUN_TUNSHI_REQ.html'>SHOUHUN_TUNSHI_REQ</a></td><td>0x00FF0104</td><td><a href='./SHOUHUN_TUNSHI_RES.html'>SHOUHUN_TUNSHI_RES</a></td><td>0x70FF014</td><td>兽魂吞噬</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./OPEN_SHOUHUN_LUCK_WINDOW_REQ.html'>OPEN_SHOUHUN_LUCK_WINDOW_REQ</a></td><td>0x00FF0107</td><td><a href='./OPEN_SHOUHUN_LUCK_WINDOW_RES.html'>OPEN_SHOUHUN_LUCK_WINDOW_RES</a></td><td>0x70FF0107</td><td>打开兽魂抽奖界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TAKE_SHOUHUN_LUCK_REQ.html'>TAKE_SHOUHUN_LUCK_REQ</a></td><td>0x00FF0108</td><td><a href='./TAKE_SHOUHUN_LUCK_RES.html'>TAKE_SHOUHUN_LUCK_RES</a></td><td>0x70FF0108</td><td>兽魂抽奖</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XILIAN_PUT_REQ.html'>XILIAN_PUT_REQ</a></td><td>0x00FF0105</td><td><a href='./XILIAN_PUT_RES.html'>XILIAN_PUT_RES</a></td><td>0x70FF015</td><td>宝石拖入格子内</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./XILIAN_REMOVE_REQ.html'>XILIAN_REMOVE_REQ</a></td><td>0x00FF0106</td><td><a href='./XILIAN_REMOVE_RES.html'>XILIAN_REMOVE_RES</a></td><td>0x70FF016</td><td>删除格子内宝石</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./TAKEOUT_SHOUHUN_REQ.html'>TAKEOUT_SHOUHUN_REQ</a></td><td>0x00FF0109</td><td><a href='./TAKEOUT_SHOUHUN_RES.html'>TAKEOUT_SHOUHUN_RES</a></td><td>0x70FF0109</td><td>摘取兽魂</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_SHOUHUN_ADDEXPS_REQ.html'>GET_SHOUHUN_ADDEXPS_REQ</a></td><td>0x00FF0110</td><td><a href='./GET_SHOUHUN_ADDEXPS_RES.html'>GET_SHOUHUN_ADDEXPS_RES</a></td><td>0x70FF0110</td><td>吞噬兽魂材料获取经验</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_SHOUHUN_KNAP_REQ.html'>GET_SHOUHUN_KNAP_REQ</a></td><td>0x00FF0111</td><td><a href='./GET_SHOUHUN_KNAP_RES.html'>GET_SHOUHUN_KNAP_RES</a></td><td>0x70FF0111</td><td>获取兽魂仓库</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ZHENLI_SHOUHUN_KNAP_REQ.html'>ZHENLI_SHOUHUN_KNAP_REQ</a></td><td>0x00FF0112</td><td><a href='./ZHENLI_SHOUHUN_KNAP_RES.html'>ZHENLI_SHOUHUN_KNAP_RES</a></td><td>0x70FF0112</td><td>整理兽魂仓库</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTIFY_SHOUHUN_KNAP_CHANGE_REQ.html'>NOTIFY_SHOUHUN_KNAP_CHANGE_REQ</a></td><td>0x00FF0113</td><td><a href='./NOTIFY_SHOUHUN_KNAP_CHANGE_RES.html'>NOTIFY_SHOUHUN_KNAP_CHANGE_RES</a></td><td>0x70FF0113</td><td>通知客户端兽魂仓库更新</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_FLY_STATE_REQ.html'>PET_FLY_STATE_REQ</a></td><td>0x00FF0115</td><td><a href='./PET_FLY_STATE_RES.html'>PET_FLY_STATE_RES</a></td><td>0x70FF0115</td><td>宠物飞升状态</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_FLY_BUTTON_ONCLICK_REQ.html'>PET_FLY_BUTTON_ONCLICK_REQ</a></td><td>0x00FF0116</td><td><a href='./PET_FLY_BUTTON_ONCLICK_RES.html'>PET_FLY_BUTTON_ONCLICK_RES</a></td><td>0x70FF0116</td><td>点击宠物飞升按钮</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_EAT_FLY_PROP_REQ.html'>PET_EAT_FLY_PROP_REQ</a></td><td>0x00FF0119</td><td><a href='./PET_EAT_FLY_PROP_RES.html'>PET_EAT_FLY_PROP_RES</a></td><td>0x70FF0119</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_CLIENT_ALCHEMY_REQ.html'>NOTICE_CLIENT_ALCHEMY_REQ</a></td><td>0x70FF0117</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端打开炼丹界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ALCHEMY_END_REQ.html'>ALCHEMY_END_REQ</a></td><td>0x00FF0118</td><td><a href='./ALCHEMY_END_RES.html'>ALCHEMY_END_RES</a></td><td>0x70FF0118</td><td>炼丹结束</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_CLEAR_PROP_CD_REQ.html'>PET_CLEAR_PROP_CD_REQ</a></td><td>0x00FF0120</td><td><a href='./PET_CLEAR_PROP_CD_RES.html'>PET_CLEAR_PROP_CD_RES</a></td><td>0x70FF0120</td><td>清楚宠物易经丹cd</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./START_DRAW_REQ.html'>START_DRAW_REQ</a></td><td>0x00FF0121</td><td><a href='./START_DRAW_RES.html'>START_DRAW_RES</a></td><td>0x70FF0121</td><td>打开宝箱</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./END_DRAW_REQ.html'>END_DRAW_REQ</a></td><td>0x00FF0122</td><td><a href='./END_DRAW_RES.html'>END_DRAW_RES</a></td><td>0x70FF0122</td><td>关闭宝箱或者抽取道具结束</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_PET_FLY_SKILLS_REQ.html'>QUERY_PET_FLY_SKILLS_REQ</a></td><td>0x00FF0123</td><td><a href='./QUERY_PET_FLY_SKILLS_RES.html'>QUERY_PET_FLY_SKILLS_RES</a></td><td>0x70FF0123</td><td>宠物飞升技能集</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEW_PET_LIST_REQ.html'>NEW_PET_LIST_REQ</a></td><td>0x00FF0124</td><td><a href='./NEW_PET_LIST_RES.html'>NEW_PET_LIST_RES</a></td><td>0x70FF0124</td><td>无说明</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEW_PET_DETAIL_REQ.html'>NEW_PET_DETAIL_REQ</a></td><td>0x00FF0125</td><td><a href='./NEW_PET_DETAIL_RES.html'>NEW_PET_DETAIL_RES</a></td><td>0x70FF0125</td><td>无说明</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_PLAYER_OUT_OF_MAP_REQ.html'>NOTICE_PLAYER_OUT_OF_MAP_REQ</a></td><td>0x00FF0126</td><td><a href='./NOTICE_PLAYER_OUT_OF_MAP_RES.html'>NOTICE_PLAYER_OUT_OF_MAP_RES</a></td><td>0x70FF0126</td><td>客户端通知服务器玩家进入地图碰撞区</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TALENT_FIRST_PAGE_REQ.html'>TALENT_FIRST_PAGE_REQ</a></td><td>0x00FF0127</td><td><a href='./TALENT_FIRST_PAGE_RES.html'>TALENT_FIRST_PAGE_RES</a></td><td>0x70FF0127</td><td>仙婴首界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_TALENT_EXP_REQ.html'>QUERY_TALENT_EXP_REQ</a></td><td>0x00FF0128</td><td><a href='./QUERY_TALENT_EXP_RES.html'>QUERY_TALENT_EXP_RES</a></td><td>0x70FF0128</td><td>喂养请求</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CONFIRM_TALENT_EXP_REQ.html'>CONFIRM_TALENT_EXP_REQ</a></td><td>0x00FF0129</td><td><a href='./CONFIRM_TALENT_EXP_RES.html'>CONFIRM_TALENT_EXP_RES</a></td><td>0x70FF0129</td><td>喂养确认</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XIULIAN_TALENT_EXP_REQ.html'>XIULIAN_TALENT_EXP_REQ</a></td><td>0x00FF0130</td><td><a href='./XIULIAN_TALENT_EXP_RES.html'>XIULIAN_TALENT_EXP_RES</a></td><td>0x70FF0130</td><td>修炼</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./PET_FLY_ANIMATION_RES.html'>PET_FLY_ANIMATION_RES</a></td><td>0x70FF0131</td><td>宠物飞升动画</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_TALENT_SKILLS_REQ.html'>QUERY_TALENT_SKILLS_REQ</a></td><td>0x00FF0132</td><td><a href='./QUERY_TALENT_SKILLS_RES.html'>QUERY_TALENT_SKILLS_RES</a></td><td>0x70FF0132</td><td>天赋技能界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_FAIRY_ROBBERY_STATE_REQ.html'>GET_FAIRY_ROBBERY_STATE_REQ</a></td><td>0x00FF0133</td><td><a href='./GET_FAIRY_ROBBERY_STATE_RES.html'>GET_FAIRY_ROBBERY_STATE_RES</a></td><td>0x70FF0133</td><td>仙界渡劫</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FLY_TALENT_ADD_POINTS_REQ.html'>FLY_TALENT_ADD_POINTS_REQ</a></td><td>0x00FF0134</td><td><a href='./FLY_TALENT_ADD_POINTS_RES.html'>FLY_TALENT_ADD_POINTS_RES</a></td><td>0x70FF0134</td><td>天赋加减点</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CONFIRM_FLY_TALENT_ADD_POINTS_REQ.html'>CONFIRM_FLY_TALENT_ADD_POINTS_REQ</a></td><td>0x00FF0135</td><td><a href='./CONFIRM_FLY_TALENT_ADD_POINTS_RES.html'>CONFIRM_FLY_TALENT_ADD_POINTS_RES</a></td><td>0x70FF0135</td><td>确认天赋加减点</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NOTICE_TALENT_BUTTON_RES.html'>NOTICE_TALENT_BUTTON_RES</a></td><td>0x70FF0136</td><td>天赋按钮</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ONCLICK_TALENT_BUTTON_REQ.html'>ONCLICK_TALENT_BUTTON_REQ</a></td><td>0x00FF0136</td><td><a href='./-.html'>-</a></td><td>-</td><td>附体</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./WILL_CHANGE_NAME_REQ.html'>WILL_CHANGE_NAME_REQ</a></td><td>0x00FF0137</td><td><a href='./WILL_CHANGE_NAME_RES.html'>WILL_CHANGE_NAME_RES</a></td><td>0x70FF0137</td><td>玩家要改名字</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./CHANGE_NAME_REQ.html'>CHANGE_NAME_REQ</a></td><td>0x00FF0138</td><td><a href='./CHANGE_NAME_RES.html'>CHANGE_NAME_RES</a></td><td>0x70FF0138</td><td>新名字</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_USED_NAME_REQ.html'>QUERY_USED_NAME_REQ</a></td><td>0x00FF0139</td><td><a href='./QUERY_USED_NAME_RES.html'>QUERY_USED_NAME_RES</a></td><td>0x70FF0139</td><td>查看曾用名</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_CHEST_TYPE_TIME_REQ.html'>QUERY_CHEST_TYPE_TIME_REQ</a></td><td>0x00FF0140</td><td><a href='./QUERY_CHEST_TYPE_TIME_RES.html'>QUERY_CHEST_TYPE_TIME_RES</a></td><td>0x70FF0140</td><td>获取玩家</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_CLIENT_PLAYE_CARTOON2_REQ.html'>NOTICE_CLIENT_PLAYE_CARTOON2_REQ</a></td><td>0x00FF0141</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端播放动画2</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_NEW_EQUIPMENT_STRONG2_REQ.html'>QUERY_NEW_EQUIPMENT_STRONG2_REQ</a></td><td>0x00FF0142</td><td><a href='./QUERY_NEW_EQUIPMENT_STRONG2_RES.html'>QUERY_NEW_EQUIPMENT_STRONG2_RES</a></td><td>0x70FF0142</td><td>客户端请求服务器，装备强化的各种信息，包括需要什么材料，需要几个，用什么来提高成功率，需要多少手续费等</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XILIAN_EQUIPMENT_REQ.html'>XILIAN_EQUIPMENT_REQ</a></td><td>0x00FF0143</td><td><a href='./XILIAN_EQUIPMENT_RES.html'>XILIAN_EQUIPMENT_RES</a></td><td>0x70FF0143</td><td>打开洗炼面板</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./XILIAN_PUTEQUIPMENT_REQ.html'>XILIAN_PUTEQUIPMENT_REQ</a></td><td>0x00FF0144</td><td><a href='./XILIAN_PUTEQUIPMENT_RES.html'>XILIAN_PUTEQUIPMENT_RES</a></td><td>0x70FF0144</td><td>洗炼放入装备</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XILIAN_EQUIPMENT_SURE_REQ.html'>XILIAN_EQUIPMENT_SURE_REQ</a></td><td>0x00FF0145</td><td><a href='./XILIAN_EQUIPMENT_SURE_RES.html'>XILIAN_EQUIPMENT_SURE_RES</a></td><td>0x70FF0145</td><td>确认洗炼</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./OPEN_MAGICWEAPON_BREAK_WINDOW_REQ.html'>OPEN_MAGICWEAPON_BREAK_WINDOW_REQ</a></td><td>0x00FF0146</td><td><a href='./OPEN_MAGICWEAPON_BREAK_WINDOW_RES.html'>OPEN_MAGICWEAPON_BREAK_WINDOW_RES</a></td><td>0x70FF0146</td><td>打开法宝进阶突破窗口</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_MAGICWEAPON_BREAK_NEEDPROP_REQ.html'>QUERY_MAGICWEAPON_BREAK_NEEDPROP_REQ</a></td><td>0x00FF0147</td><td><a href='./QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES.html'>QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES</a></td><td>0x70FF0147</td><td>法宝进阶突破查询</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MAGICWEAPON_BREAK_REQ.html'>MAGICWEAPON_BREAK_REQ</a></td><td>0x00FF0148</td><td><a href='./MAGICWEAPON_BREAK_RES.html'>MAGICWEAPON_BREAK_RES</a></td><td>0x70FF0148</td><td>法宝阶突破</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DISASTER_RANK_INFO_REQ.html'>DISASTER_RANK_INFO_REQ</a></td><td>0x00FF0149</td><td><a href='./DISASTER_RANK_INFO_RES.html'>DISASTER_RANK_INFO_RES</a></td><td>0x70FF0149</td><td>通知客户端金猴天灾游戏排名</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DISASTER_END_INFO_REQ.html'>DISASTER_END_INFO_REQ</a></td><td>0x70FF0151</td><td><a href='./-.html'>-</a></td><td>-</td><td>通知客户端金猴天灾游戏结束</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DISASTER_START_INFO_REQ.html'>DISASTER_START_INFO_REQ</a></td><td>0x70FF0152</td><td><a href='./-.html'>-</a></td><td>-</td><td>玩家进入金猴天灾游戏场景开始信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DISASTER_MATCH_SUCCESS_REQ.html'>DISASTER_MATCH_SUCCESS_REQ</a></td><td>0x70FF0153</td><td><a href='./-.html'>-</a></td><td>-</td><td>匹配成功弹窗提醒玩家</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DISASTER_SIGN_SUCCESS_REQ.html'>DISASTER_SIGN_SUCCESS_REQ</a></td><td>0x70FF0154</td><td><a href='./-.html'>-</a></td><td>-</td><td>报名成功匹配用时</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DISASTER_OPT_REQ.html'>DISASTER_OPT_REQ</a></td><td>0x00FF0155</td><td><a href='./DISASTER_OPT_RES.html'>DISASTER_OPT_RES</a></td><td>0x70FF0155</td><td>进入、放弃、离开空岛大冒险或者取消排队</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_IN_SPESCENE_REQ.html'>PLAYER_IN_SPESCENE_REQ</a></td><td>0x00FF0156</td><td><a href='./PLAYER_IN_SPESCENE_RES.html'>PLAYER_IN_SPESCENE_RES</a></td><td>0x70FF0156</td><td>玩家是否在某个特殊场景</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SHOOK_DICE_REQ.html'>SHOOK_DICE_REQ</a></td><td>0x00FF0157</td><td><a href='./SHOOK_DICE_RES.html'>SHOOK_DICE_RES</a></td><td>0x70FF0157</td><td>摇骰子请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SHOOK_DICE_RESULT_REQ.html'>SHOOK_DICE_RESULT_REQ</a></td><td>0x00FF0158</td><td><a href='./-.html'>-</a></td><td>-</td><td>摇骰子结果</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./SHOOK_DICE2_RES.html'>SHOOK_DICE2_RES</a></td><td>0x70FF0159</td><td>摇骰子请求2</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./DICE_BILLBOARD_RES.html'>DICE_BILLBOARD_RES</a></td><td>0x70FF0160</td><td>骰子副本排行</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./DICE_END_NOTICE_RES.html'>DICE_END_NOTICE_RES</a></td><td>0x70FF0161</td><td>结束通知</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_COUNTRY_OFFICER_REQ.html'>QUERY_COUNTRY_OFFICER_REQ</a></td><td>0x00FF0162</td><td><a href='./QUERY_COUNTRY_OFFICER_RES.html'>QUERY_COUNTRY_OFFICER_RES</a></td><td>0x70FF0162</td><td>发送国家官员信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DISASTER_SKILL_INFO_REQ.html'>DISASTER_SKILL_INFO_REQ</a></td><td>0x00FF0163</td><td><a href='./DISASTER_SKILL_INFO_RES.html'>DISASTER_SKILL_INFO_RES</a></td><td>0x70FF0163</td><td>请求金猴天灾技能列表</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DICE_TRANSFER_REQ.html'>DICE_TRANSFER_REQ</a></td><td>0x00FF0164</td><td><a href='./-.html'>-</a></td><td>-</td><td>传送请求</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SOULPITH_INFO_REQ.html'>SOULPITH_INFO_REQ</a></td><td>0x00FF0165</td><td><a href='./SOULPITH_INFO_RES.html'>SOULPITH_INFO_RES</a></td><td>0x70FF0165</td><td>请求玩家灵根信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./INLAY_SOULPITH_REQ.html'>INLAY_SOULPITH_REQ</a></td><td>0x00FF0166</td><td><a href='./INLAY_SOULPITH_RES.html'>INLAY_SOULPITH_RES</a></td><td>0x70FF0166</td><td>镶嵌灵髓</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TAKEOUT_SOULPITH_REQ.html'>TAKEOUT_SOULPITH_REQ</a></td><td>0x00FF0167</td><td><a href='./TAKEOUT_SOULPITH_RES.html'>TAKEOUT_SOULPITH_RES</a></td><td>0x70FF0167</td><td>取出灵髓</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./POURIN_SOULPITH_REQ.html'>POURIN_SOULPITH_REQ</a></td><td>0x00FF0168</td><td><a href='./POURIN_SOULPITH_RES.html'>POURIN_SOULPITH_RES</a></td><td>0x70FF0168</td><td>注灵</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DEVOUR_SOULPITH_REQ.html'>DEVOUR_SOULPITH_REQ</a></td><td>0x00FF0169</td><td><a href='./DEVOUR_SOULPITH_RES.html'>DEVOUR_SOULPITH_RES</a></td><td>0x70FF0169</td><td>吞噬</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ARTIFICE_SOULPITH_REQ.html'>ARTIFICE_SOULPITH_REQ</a></td><td>0x00FF0170</td><td><a href='./ARTIFICE_SOULPITH_RES.html'>ARTIFICE_SOULPITH_RES</a></td><td>0x70FF0170</td><td>炼化</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SOULPITH_EXTRA_INFO_REQ.html'>SOULPITH_EXTRA_INFO_REQ</a></td><td>0x00FF0171</td><td><a href='./SOULPITH_EXTRA_INFO_RES.html'>SOULPITH_EXTRA_INFO_RES</a></td><td>0x70FF0171</td><td>灵根激活属性</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GOURD_INFO_REQ.html'>GOURD_INFO_REQ</a></td><td>0x00FF0172</td><td><a href='./GOURD_INFO_RES.html'>GOURD_INFO_RES</a></td><td>0x70FF0172</td><td>仙气葫芦临时道具id请求</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./VIP_LOTTERY_OPEN_REQ.html'>VIP_LOTTERY_OPEN_REQ</a></td><td>0x00FFF001</td><td><a href='./-.html'>-</a></td><td>-</td><td>VIP活动弹出转盘</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./VIP_LOTTERY_CLICK_REQ.html'>VIP_LOTTERY_CLICK_REQ</a></td><td>0x00FFF002</td><td><a href='./VIP_LOTTERY_CLICK_RES.html'>VIP_LOTTERY_CLICK_RES</a></td><td>0x70FFF002</td><td>VIP活动点击转盘</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HUNSHI_JIANDING_REQ.html'>HUNSHI_JIANDING_REQ</a></td><td>0x00FFF003</td><td><a href='./HUNSHI_JIANDING_RES.html'>HUNSHI_JIANDING_RES</a></td><td>0x70FFF003</td><td>客户端查询魂石鉴定需要什么材料</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HUNSHI_JIANDING_SURE_REQ.html'>HUNSHI_JIANDING_SURE_REQ</a></td><td>0x00FFF004</td><td><a href='./HUNSHI_JIANDING_SURE_RES.html'>HUNSHI_JIANDING_SURE_RES</a></td><td>0x70FFF004</td><td>客户端魂石鉴定</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./WOLF_SIGN_UP_QUERY_REQ.html'>WOLF_SIGN_UP_QUERY_REQ</a></td><td>0x00FFF005</td><td><a href='./WOLF_SIGN_UP_QUERY_RES.html'>WOLF_SIGN_UP_QUERY_RES</a></td><td>0x70FFF005</td><td>请求报名界面信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./WOLF_SIGN_UP_SURE_REQ.html'>WOLF_SIGN_UP_SURE_REQ</a></td><td>0x00FFF006</td><td><a href='./WOLF_SIGN_UP_SURE_RES.html'>WOLF_SIGN_UP_SURE_RES</a></td><td>0x70FFF006</td><td>确认报名</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./WOLF_FIGHT_QUERY_REQ.html'>WOLF_FIGHT_QUERY_REQ</a></td><td>0x00FFF007</td><td><a href='./WOLF_FIGHT_QUERY_RES.html'>WOLF_FIGHT_QUERY_RES</a></td><td>0x70FFF007</td><td>战斗界面信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./WOLF_STATE_NOTICE_RES.html'>WOLF_STATE_NOTICE_RES</a></td><td>0x70FFF008</td><td>状态通知</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./WOLF_NOTICE_ENTER_REQ.html'>WOLF_NOTICE_ENTER_REQ</a></td><td>0x00FFF009</td><td><a href='./WOLF_NOTICE_ENTER_RES.html'>WOLF_NOTICE_ENTER_RES</a></td><td>0x70FFF009</td><td>通知进入界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./WOLF_ENTER_REQ.html'>WOLF_ENTER_REQ</a></td><td>0x00FFF010</td><td><a href='./WOLF_ENTER_RES.html'>WOLF_ENTER_RES</a></td><td>0x70FFF010</td><td>进入确认</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./WOLF_UPDATE_FIGHT_INFO_RES.html'>WOLF_UPDATE_FIGHT_INFO_RES</a></td><td>0x70FFF011</td><td>战斗信息更新</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./WOLF_START_FIGHT_NOTICE_RES.html'>WOLF_START_FIGHT_NOTICE_RES</a></td><td>0x70FFF012</td><td>战斗即将开始</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./WOLF_END_NOTICE_RES.html'>WOLF_END_NOTICE_RES</a></td><td>0x70FFF015</td><td>战斗结束通知</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./HUNSHI_OPEN_RES.html'>HUNSHI_OPEN_RES</a></td><td>0x70FFF016</td><td>打开魂石预览界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HUNSHI_MERGE_AIM_REQ.html'>HUNSHI_MERGE_AIM_REQ</a></td><td>0x00FFF017</td><td><a href='./HUNSHI_MERGE_AIM_RES.html'>HUNSHI_MERGE_AIM_RES</a></td><td>0x70FFF017</td><td>魂石合成目标查询</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HUNSHI_SPECIAL_REQ.html'>HUNSHI_SPECIAL_REQ</a></td><td>0x00FFF018</td><td><a href='./HUNSHI_SPECIAL_RES.html'>HUNSHI_SPECIAL_RES</a></td><td>0x70FFF018</td><td>魂石特有信息查询</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HUNSHI_MERGE_REQ.html'>HUNSHI_MERGE_REQ</a></td><td>0x00FFF019</td><td><a href='./HUNSHI_MERGE_RES.html'>HUNSHI_MERGE_RES</a></td><td>0x70FFF019</td><td>魂石合成确认</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HUNSHI2_OPEN_REQ.html'>HUNSHI2_OPEN_REQ</a></td><td>0x00FFF020</td><td><a href='./HUNSHI2_OPEN_RES.html'>HUNSHI2_OPEN_RES</a></td><td>0x70FFF020</td><td>套装魂石合成界面打开</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HUNSHI2_KIND_REQ.html'>HUNSHI2_KIND_REQ</a></td><td>0x00FFF021</td><td><a href='./HUNSHI2_KIND_RES.html'>HUNSHI2_KIND_RES</a></td><td>0x70FFF021</td><td>套装魂石分类请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HUNSHI_PUTON_OPEN_REQ.html'>HUNSHI_PUTON_OPEN_REQ</a></td><td>0x00FFF022</td><td><a href='./HUNSHI_PUTON_OPEN_RES.html'>HUNSHI_PUTON_OPEN_RES</a></td><td>0x70FFF022</td><td>打开魂石镶嵌界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SOUL_ARTICLE_INFO_REQ.html'>SOUL_ARTICLE_INFO_REQ</a></td><td>0x00FFF023</td><td><a href='./SOUL_ARTICLE_INFO_RES.html'>SOUL_ARTICLE_INFO_RES</a></td><td>0x70FFF023</td><td>请求灵髓宝石属性（等级、经验等）</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./SOULPITH_LEVEL_INFO_REQ.html'>SOULPITH_LEVEL_INFO_REQ</a></td><td>0x00FFF024</td><td><a href='./SOULPITH_LEVEL_INFO_RES.html'>SOULPITH_LEVEL_INFO_RES</a></td><td>0x70FFF024</td><td>灵髓宝石等级信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./SOUL_LEVELUP_INFO_REQ.html'>SOUL_LEVELUP_INFO_REQ</a></td><td>0x00FFF025</td><td><a href='./SOUL_LEVELUP_INFO_RES.html'>SOUL_LEVELUP_INFO_RES</a></td><td>0x70FFF025</td><td>请求灵髓宝石升级所需经验</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HUNSHI_PUTON_REQ.html'>HUNSHI_PUTON_REQ</a></td><td>0x00FFF026</td><td><a href='./HUNSHI_PUTON_RES.html'>HUNSHI_PUTON_RES</a></td><td>0x70FFF026</td><td>镶嵌挖取请求</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HUNSHI_PROP_VIEW_REQ.html'>HUNSHI_PROP_VIEW_REQ</a></td><td>0x00FFF027</td><td><a href='./HUNSHI_PROP_VIEW_RES.html'>HUNSHI_PROP_VIEW_RES</a></td><td>0x70FFF027</td><td>魂石属性总览</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HUNSHI2_PUTON_OPEN_REQ.html'>HUNSHI2_PUTON_OPEN_REQ</a></td><td>0x00FFF028</td><td><a href='./HUNSHI2_PUTON_OPEN_RES.html'>HUNSHI2_PUTON_OPEN_RES</a></td><td>0x70FFF028</td><td>打开套装魂石镶嵌界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HUNSHI2_PROP_VIEW_REQ.html'>HUNSHI2_PROP_VIEW_REQ</a></td><td>0x00FFF029</td><td><a href='./HUNSHI2_PROP_VIEW_RES.html'>HUNSHI2_PROP_VIEW_RES</a></td><td>0x70FFF029</td><td>套装魂石属性总览</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HUNSHI2_CELL_BUY_REQ.html'>HUNSHI2_CELL_BUY_REQ</a></td><td>0x00FFF030</td><td><a href='./HUNSHI2_CELL_BUY_RES.html'>HUNSHI2_CELL_BUY_RES</a></td><td>0x70FFF030</td><td>购买套装石格子</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./WOLF_SIGNUP_NUM_REQ.html'>WOLF_SIGNUP_NUM_REQ</a></td><td>0x00FFF031</td><td><a href='./WOLF_SIGNUP_NUM_RES.html'>WOLF_SIGNUP_NUM_RES</a></td><td>0x70FFF031</td><td>获取报名人数</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./WOLF_SKILL_ID_RES.html'>WOLF_SKILL_ID_RES</a></td><td>0x70FFF032</td><td>获取技能id</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./WOLF_USE_SKILL_REQ.html'>WOLF_USE_SKILL_REQ</a></td><td>0x00FFF033</td><td><a href='./WOLF_USE_SKILL_RES.html'>WOLF_USE_SKILL_RES</a></td><td>0x70FFF033</td><td>技能使用</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DICE_SIGN_UP_QUERY_REQ.html'>DICE_SIGN_UP_QUERY_REQ</a></td><td>0x00FFF034</td><td><a href='./DICE_SIGN_UP_QUERY_RES.html'>DICE_SIGN_UP_QUERY_RES</a></td><td>0x70FFF034</td><td>请求报名界面信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./DICE_SIGN_UP_SURE_REQ.html'>DICE_SIGN_UP_SURE_REQ</a></td><td>0x00FFF035</td><td><a href='./DICE_SIGN_UP_SURE_RES.html'>DICE_SIGN_UP_SURE_RES</a></td><td>0x70FFF035</td><td>确认报名</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NOTICE_DISASTER_COUNTDOWN_REQ.html'>NOTICE_DISASTER_COUNTDOWN_REQ</a></td><td>0x00FFF036</td><td><a href='./-.html'>-</a></td><td>-</td><td>空岛大冒险倒计时</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HUNSHI_JIANDING_QINGQIU_REQ.html'>HUNSHI_JIANDING_QINGQIU_REQ</a></td><td>0x00FFF037</td><td><a href='./HUNSHI_JIANDING_QINGQIU_RES.html'>HUNSHI_JIANDING_QINGQIU_RES</a></td><td>0x70FFF037</td><td>坐骑魂石打开鉴定界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEW_WAREHOUSE_GET_REQ.html'>NEW_WAREHOUSE_GET_REQ</a></td><td>0x00FFF038</td><td><a href='./NEW_WAREHOUSE_GET_RES.html'>NEW_WAREHOUSE_GET_RES</a></td><td>0x70FFF038</td><td>获取一个仓库(新)</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEW_WAREHOUSE_ARRANGE_REQ.html'>NEW_WAREHOUSE_ARRANGE_REQ</a></td><td>0x00FFF039</td><td><a href='./NEW_WAREHOUSE_ARRANGE_RES.html'>NEW_WAREHOUSE_ARRANGE_RES</a></td><td>0x70FFF039</td><td>自动整理一个仓库</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEW_ACVITITY_SHOW_INFO_REQ.html'>NEW_ACVITITY_SHOW_INFO_REQ</a></td><td>0x00FFF040</td><td><a href='./NEW_ACVITITY_SHOW_INFO_RES.html'>NEW_ACVITITY_SHOW_INFO_RES</a></td><td>0x70FFF040</td><td>新活动界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEW_WAREHOUSE_INPUT_PASSWORD_REQ.html'>NEW_WAREHOUSE_INPUT_PASSWORD_REQ</a></td><td>0x00FFF041</td><td><a href='./NEW_WAREHOUSE_INPUT_PASSWORD_RES.html'>NEW_WAREHOUSE_INPUT_PASSWORD_RES</a></td><td>0x70FFF041</td><td>req为客户端向服务器发送输入密码请求，res为服务器告诉客户端打开输入密码窗口</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HUNSHI_JIANDING2_QINGQIU_REQ.html'>HUNSHI_JIANDING2_QINGQIU_REQ</a></td><td>0x00FFF042</td><td><a href='./HUNSHI_JIANDING2_QINGQIU_RES.html'>HUNSHI_JIANDING2_QINGQIU_RES</a></td><td>0x70FFF042</td><td>坐骑魂石打开魂石神隐洗炼界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HUNSHI_JIANDING2_REQ.html'>HUNSHI_JIANDING2_REQ</a></td><td>0x00FFF043</td><td><a href='./HUNSHI_JIANDING2_RES.html'>HUNSHI_JIANDING2_RES</a></td><td>0x70FFF043</td><td>客户端查询魂石神隐洗炼需要什么材料</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HUNSHI_JIANDING2_SURE_REQ.html'>HUNSHI_JIANDING2_SURE_REQ</a></td><td>0x00FFF044</td><td><a href='./HUNSHI_JIANDING2_SURE_RES.html'>HUNSHI_JIANDING2_SURE_RES</a></td><td>0x70FFF044</td><td>客户端魂石神隐洗炼</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./NEW_QUERY_ARTICLE_INFO_REQ.html'>NEW_QUERY_ARTICLE_INFO_REQ</a></td><td>0x00FFF045</td><td><a href='./NEW_QUERY_ARTICLE_INFO_RES.html'>NEW_QUERY_ARTICLE_INFO_RES</a></td><td>0x70FFF045</td><td>请求物品详细描述</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./HUNSHI2_INFOSHOW_REQ.html'>HUNSHI2_INFOSHOW_REQ</a></td><td>0x00FFF046</td><td><a href='./HUNSHI2_INFOSHOW_RES.html'>HUNSHI2_INFOSHOW_RES</a></td><td>0x70FFF046</td><td>客户端请求已镶嵌的套装魂石泡泡</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./OPEN_MAGICWEAPON_EXTRAATTR_REQ.html'>OPEN_MAGICWEAPON_EXTRAATTR_REQ</a></td><td>0x00FFF059</td><td><a href='./OPEN_MAGICWEAPON_EXTRAATTR_RES.html'>OPEN_MAGICWEAPON_EXTRAATTR_RES</a></td><td>0x70FFF059</td><td>打开法宝附加属性刷新界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MAGICWEAPON_EXTRAATTR_INFO_REQ.html'>MAGICWEAPON_EXTRAATTR_INFO_REQ</a></td><td>0x00FFF060</td><td><a href='./MAGICWEAPON_EXTRAATTR_INFO_RES.html'>MAGICWEAPON_EXTRAATTR_INFO_RES</a></td><td>0x70FFF060</td><td>请求法宝附加属性描述</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./RESET_MAGICWEAPON_EXTRAATTR_REQ.html'>RESET_MAGICWEAPON_EXTRAATTR_REQ</a></td><td>0x00FFF061</td><td><a href='./RESET_MAGICWEAPON_EXTRAATTR_RES.html'>RESET_MAGICWEAPON_EXTRAATTR_RES</a></td><td>0x70FFF061</td><td>刷新法宝附加属性</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XIANLING_OPEN_MAIN_REQ.html'>XIANLING_OPEN_MAIN_REQ</a></td><td>0x00FFF047</td><td><a href='./XIANLING_OPEN_MAIN_RES.html'>XIANLING_OPEN_MAIN_RES</a></td><td>0x70FFF067</td><td>打开仙灵主面板</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./HELP_REQ.html'>HELP_REQ</a></td><td>0x00FFF069</td><td><a href='./HELP_RES.html'>HELP_RES</a></td><td>0x70FFF069</td><td>帮助</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XL_CHALLENGE_REQ.html'>XL_CHALLENGE_REQ</a></td><td>0x00FFF070</td><td><a href='./XL_CHALLENGE_RES.html'>XL_CHALLENGE_RES</a></td><td>0x70FFF070</td><td>点关卡图标</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./XL_CHALLENGE_SURE_REQ.html'>XL_CHALLENGE_SURE_REQ</a></td><td>0x00FFF071</td><td><a href='./-.html'>-</a></td><td>-</td><td>确认挑战</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XL_OPEN_MEETMONSTER_BUFF_REQ.html'>XL_OPEN_MEETMONSTER_BUFF_REQ</a></td><td>0x00FFF072</td><td><a href='./XL_OPEN_MEETMONSTER_BUFF_RES.html'>XL_OPEN_MEETMONSTER_BUFF_RES</a></td><td>0x70FFF072</td><td>打开激活buff界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./XL_ACT_MEETMONSTER_BUFF_REQ.html'>XL_ACT_MEETMONSTER_BUFF_REQ</a></td><td>0x00FFF073</td><td><a href='./XL_ACT_MEETMONSTER_BUFF_RES.html'>XL_ACT_MEETMONSTER_BUFF_RES</a></td><td>0x70FFF073</td><td>确认激活buff</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XL_OPEN_SCORE_BUFF_REQ.html'>XL_OPEN_SCORE_BUFF_REQ</a></td><td>0x00FFF074</td><td><a href='./XL_OPEN_SCORE_BUFF_RES.html'>XL_OPEN_SCORE_BUFF_RES</a></td><td>0x70FFF074</td><td>打开积分卡界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./XL_USE_SCORE_CARD_REQ.html'>XL_USE_SCORE_CARD_REQ</a></td><td>0x00FFF075</td><td><a href='./XL_USE_SCORE_CARD_RES.html'>XL_USE_SCORE_CARD_RES</a></td><td>0x70FFF075</td><td>确认使用积分卡</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XL_TIMEDTASK_REQ.html'>XL_TIMEDTASK_REQ</a></td><td>0x00FFF076</td><td><a href='./XL_TIMEDTASK_RES.html'>XL_TIMEDTASK_RES</a></td><td>0x70FFF076</td><td>打开限时任务界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./XL_TIMEDTASK_PRIZE_REQ.html'>XL_TIMEDTASK_PRIZE_REQ</a></td><td>0x00FFF077</td><td><a href='./XL_TIMEDTASK_PRIZE_RES.html'>XL_TIMEDTASK_PRIZE_RES</a></td><td>0x70FFF077</td><td>领取限时任务奖励</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XL_GET_SCORE_PRIZE_REQ.html'>XL_GET_SCORE_PRIZE_REQ</a></td><td>0x00FFF079</td><td><a href='./XL_GET_SCORE_PRIZE_RES.html'>XL_GET_SCORE_PRIZE_RES</a></td><td>0x70FFF079</td><td>打开积分奖励界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./REFRESH_CROSS_SHOP_REQ.html'>REFRESH_CROSS_SHOP_REQ</a></td><td>0x00FFF0782</td><td><a href='./-.html'>-</a></td><td>-</td><td>刷新跨服商店</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XL_BUTENERGY_REQ.html'>XL_BUTENERGY_REQ</a></td><td>0x00FFF083</td><td><a href='./XL_BUTENERGY_RES.html'>XL_BUTENERGY_RES</a></td><td>0x70FFF083</td><td>打开购买真气界面</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./XL_BUTENERGY_SURE_REQ.html'>XL_BUTENERGY_SURE_REQ</a></td><td>0x00FFF0784</td><td><a href='./XL_BUTENERGY_SURE_RES.html'>XL_BUTENERGY_SURE_RES</a></td><td>0x70FFF084</td><td>购买真气确认</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_NEW_EQUIPMENT_STRONG3_REQ.html'>QUERY_NEW_EQUIPMENT_STRONG3_REQ</a></td><td>0x00FFF084</td><td><a href='./QUERY_NEW_EQUIPMENT_STRONG3_RES.html'>QUERY_NEW_EQUIPMENT_STRONG3_RES</a></td><td>0x70FFF087</td><td>客户端请求服务器，装备强化的各种信息，包括需要什么材料，需要几个，用什么来提高成功率，需要多少手续费等</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./XL_USE_SKILL_REQ.html'>XL_USE_SKILL_REQ</a></td><td>0x00FFF085</td><td><a href='./XL_USE_SKILL_RES.html'>XL_USE_SKILL_RES</a></td><td>0x70FFF085</td><td>使用技能</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XL_QUERY_SKILL_REQ.html'>XL_QUERY_SKILL_REQ</a></td><td>0x00FFF086</td><td><a href='./XL_QUERY_SKILL_RES.html'>XL_QUERY_SKILL_RES</a></td><td>0x70FFF086</td><td>仙灵技能</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_PET_ORNAID_REQ.html'>QUERY_PET_ORNAID_REQ</a></td><td>0x00FFF088</td><td><a href='./QUERY_PET_ORNAID_RES.html'>QUERY_PET_ORNAID_RES</a></td><td>0x70FFF088</td><td>获取宠物饰品</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PUTON_PET_ORNA_REQ.html'>PUTON_PET_ORNA_REQ</a></td><td>0x00FFF089</td><td><a href='./PUTON_PET_ORNA_RES.html'>PUTON_PET_ORNA_RES</a></td><td>0x70FFF089</td><td>宠物穿饰品</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./XL_EXIT_CHALLENGE_REQ.html'>XL_EXIT_CHALLENGE_REQ</a></td><td>0x00FFF090</td><td><a href='./-.html'>-</a></td><td>-</td><td>退出挑战</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./XL_SHOW_SKILL_HELP_RES.html'>XL_SHOW_SKILL_HELP_RES</a></td><td>0x70FFF091</td><td>显示技能蒙板</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./XL_CATCH_SUCC_RES.html'>XL_CATCH_SUCC_RES</a></td><td>0x70FFF092</td><td>捕捉成功</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./XL_REFRESH_ENERGY_REQ.html'>XL_REFRESH_ENERGY_REQ</a></td><td>0x00FFF093</td><td><a href='./XL_REFRESH_ENERGY_RES.html'>XL_REFRESH_ENERGY_RES</a></td><td>0x70FFF093</td><td>刷新真气</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./XL_NOTICE_REQ.html'>XL_NOTICE_REQ</a></td><td>0x00FFF094</td><td><a href='./XL_NOTICE_RES.html'>XL_NOTICE_RES</a></td><td>0x70FFF094</td><td>红点提示玩家</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET3_QUERY_REQ.html'>PET3_QUERY_REQ</a></td><td>0x00FFF095</td><td><a href='./PET3_QUERY_RES.html'>PET3_QUERY_RES</a></td><td>0x70FFF095</td><td>请求宠物信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_PLAYER_CREATE_TIME_REQ.html'>QUERY_PLAYER_CREATE_TIME_REQ</a></td><td>0x00FFF096</td><td><a href='./QUERY_PLAYER_CREATE_TIME_RES.html'>QUERY_PLAYER_CREATE_TIME_RES</a></td><td>0x70FFF096</td><td>角色创建时间</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./QUERY_PALYER_PROP_INFO_REQ.html'>QUERY_PALYER_PROP_INFO_REQ</a></td><td>0x00FFF099</td><td><a href='./QUERY_PALYER_PROP_INFO_RES.html'>QUERY_PALYER_PROP_INFO_RES</a></td><td>0x70FFF099</td><td>查询玩家属性</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_HORSE_EQUIPMENT_INFO_REQ.html'>QUERY_HORSE_EQUIPMENT_INFO_REQ</a></td><td>0x00FFF100</td><td><a href='./QUERY_HORSE_EQUIPMENT_INFO__RES.html'>QUERY_HORSE_EQUIPMENT_INFO__RES</a></td><td>0x70FFF100</td><td>查询坐骑装备开孔情况</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ACTIVITY_HORSEEQU_INLAY_REQ.html'>ACTIVITY_HORSEEQU_INLAY_REQ</a></td><td>0x00FFF101</td><td><a href='./ACTIVITY_HORSEEQU_INLAY_RES.html'>ACTIVITY_HORSEEQU_INLAY_RES</a></td><td>0x70FFF101</td><td>激活或者重置坐骑装备孔</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./QUERY_INLAY_WINDOW_INFO_REQ.html'>QUERY_INLAY_WINDOW_INFO_REQ</a></td><td>0x00FFF102</td><td><a href='./QUERY_INLAY_WINDOW_INFO_RES.html'>QUERY_INLAY_WINDOW_INFO_RES</a></td><td>0x70FFF102</td><td>打开坐骑装备镶嵌界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./INLAYTAKE_HORSEBAOSHI_REQ.html'>INLAYTAKE_HORSEBAOSHI_REQ</a></td><td>0x00FFF103</td><td><a href='./INLAYTAKE_HORSEBAOSHI_RES.html'>INLAYTAKE_HORSEBAOSHI_RES</a></td><td>0x70FFF103</td><td>镶嵌货取出坐骑装备神匣</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./INLAYTAKE_WINDOW_REQ.html'>INLAYTAKE_WINDOW_REQ</a></td><td>0x00FFF104</td><td><a href='./INLAYTAKE_WINDOW_RES.html'>INLAYTAKE_WINDOW_RES</a></td><td>0x70FFF104</td><td>打开坐骑开孔操作界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./NEW_GET_ONE_TREASUREACTIVITY_INFO_REQ.html'>NEW_GET_ONE_TREASUREACTIVITY_INFO_REQ</a></td><td>0x00FFF105</td><td><a href='./NEW_GET_ONE_TREASUREACTIVITY_INFO_RES.html'>NEW_GET_ONE_TREASUREACTIVITY_INFO_RES</a></td><td>0x70FFF105</td><td>获取单个宝藏信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOOTH_OFFLINE_REQ.html'>BOOTH_OFFLINE_REQ</a></td><td>0x00FFF111</td><td><a href='./BOOTH_OFFLINE_RES.html'>BOOTH_OFFLINE_RES</a></td><td>0x70FFF111</td><td>离线摆摊</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOOTH_OFFLINE_SURE_REQ.html'>BOOTH_OFFLINE_SURE_REQ</a></td><td>0x00FFF112</td><td><a href='./BOOTH_OFFLINE_SURE_RES.html'>BOOTH_OFFLINE_SURE_RES</a></td><td>0x70FFF112</td><td>离线摆摊确认</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOOTH_FIRST_PAGE_REQ.html'>BOOTH_FIRST_PAGE_REQ</a></td><td>0x00FFF113</td><td><a href='./BOOTH_FIRST_PAGE_RES.html'>BOOTH_FIRST_PAGE_RES</a></td><td>0x70FFF113</td><td>boss副本首界面</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOOTH_HELP_REQ.html'>BOOTH_HELP_REQ</a></td><td>0x00FFF114</td><td><a href='./BOOTH_HELP_RES.html'>BOOTH_HELP_RES</a></td><td>0x70FFF114</td><td>帮助信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOOTH_START_FIGHT_REQ.html'>BOOTH_START_FIGHT_REQ</a></td><td>0x00FFF115</td><td><a href='./-.html'>-</a></td><td>-</td><td>发起挑战</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./BOOTH_BATTLE_INFO_REQ.html'>BOOTH_BATTLE_INFO_REQ</a></td><td>0x00FFF116</td><td><a href='./BOOTH_BATTLE_INFO_RES.html'>BOOTH_BATTLE_INFO_RES</a></td><td>0x70FFF116</td><td>战斗信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BOOTH_GULI_REQ.html'>BOOTH_GULI_REQ</a></td><td>0x00FFF117</td><td><a href='./-.html'>-</a></td><td>-</td><td>鼓励</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_CELL_HELP_REQ.html'>PET_CELL_HELP_REQ</a></td><td>0x00FFF118</td><td><a href='./PET_CELL_HELP_RES.html'>PET_CELL_HELP_RES</a></td><td>0x70FFF118</td><td>助战信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_CELL_UPGRADE_QUERY_REQ.html'>PET_CELL_UPGRADE_QUERY_REQ</a></td><td>0x00FFF119</td><td><a href='./PET_CELL_UPGRADE_QUERY_RES.html'>PET_CELL_UPGRADE_QUERY_RES</a></td><td>0x70FFF119</td><td>升级请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_CELL_UPGRADE_CONFIRM_REQ.html'>PET_CELL_UPGRADE_CONFIRM_REQ</a></td><td>0x00FFF120</td><td><a href='./PET_CELL_UPGRADE_CONFIRM_RES.html'>PET_CELL_UPGRADE_CONFIRM_RES</a></td><td>0x70FFF120</td><td>升级弹窗确认</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_CELL_FIRST_PAGE_REQ.html'>PET_CELL_FIRST_PAGE_REQ</a></td><td>0x00FFF121</td><td><a href='./QUERY_PET_INFO_RES.html'>QUERY_PET_INFO_RES</a></td><td>0x70FFF121</td><td>点击宠物助战按钮</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./MONTH_CARD_FIRST_PAGE_REQ.html'>MONTH_CARD_FIRST_PAGE_REQ</a></td><td>0x00FFF122</td><td><a href='./MONTH_CARD_FIRST_PAGE_RES.html'>MONTH_CARD_FIRST_PAGE_RES</a></td><td>0x70FFF122</td><td>月卡界面信息</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./MONTH_CARD_BUY_REQ.html'>MONTH_CARD_BUY_REQ</a></td><td>0x00FFF123</td><td><a href='./-.html'>-</a></td><td>-</td><td>购买某一种月卡</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./LOGIN_REWARD_REQ.html'>LOGIN_REWARD_REQ</a></td><td>0x00FFF124</td><td><a href='./LOGIN_REWARD_RES.html'>LOGIN_REWARD_RES</a></td><td>0x70FFF124</td><td>七日登录奖励</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./GET_LOGIN_REWARD_REQ.html'>GET_LOGIN_REWARD_REQ</a></td><td>0x00FFF125</td><td><a href='./-.html'>-</a></td><td>-</td><td>领取七日登录奖励</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FIRST_CHARGE_STATE_REQ.html'>FIRST_CHARGE_STATE_REQ</a></td><td>0x00FFF127</td><td><a href='./FIRST_CHARGE_STATE_RES.html'>FIRST_CHARGE_STATE_RES</a></td><td>0x70FFF127</td><td>首冲状态，0;没冲过，1:冲过</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./FIRST_CHARGE_REQ.html'>FIRST_CHARGE_REQ</a></td><td>0x00FFF126</td><td><a href='./FIRST_CHARGE_RES.html'>FIRST_CHARGE_RES</a></td><td>0x70FFF126</td><td>首冲</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_JOIN_CELL_REQ.html'>PET_JOIN_CELL_REQ</a></td><td>0x00FFF128</td><td><a href='./-.html'>-</a></td><td>-</td><td>上阵，撤回</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TOTLE_ONLINE_REWARD_REQ.html'>TOTLE_ONLINE_REWARD_REQ</a></td><td>0x00FFF129</td><td><a href='./TOTLE_ONLINE_REWARD_RES.html'>TOTLE_ONLINE_REWARD_RES</a></td><td>0x70FFF129</td><td>累计在线</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./GET_TOTLE_ONLINE_REWARD_REQ.html'>GET_TOTLE_ONLINE_REWARD_REQ</a></td><td>0x00FFF130</td><td><a href='./-.html'>-</a></td><td>-</td><td>领取累计在线</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PLAYER_PET_HOUSE_REQ.html'>PLAYER_PET_HOUSE_REQ</a></td><td>0x00FFF131</td><td><a href='./PLAYER_PET_HOUSE_RES.html'>PLAYER_PET_HOUSE_RES</a></td><td>0x70FFF131</td><td>我的仙兽房</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./JIAZU_PET_HOUSE_REQ.html'>JIAZU_PET_HOUSE_REQ</a></td><td>0x00FFF132</td><td><a href='./JIAZU_PET_HOUSE_RES.html'>JIAZU_PET_HOUSE_RES</a></td><td>0x70FFF132</td><td>家族仙兽房</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./PET_STORE_REQ.html'>PET_STORE_REQ</a></td><td>0x00FFF133</td><td><a href='./-.html'>-</a></td><td>-</td><td>宠物挂机</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PET_CALL_BACK_REQ.html'>PET_CALL_BACK_REQ</a></td><td>0x00FFF134</td><td><a href='./-.html'>-</a></td><td>-</td><td>收回宠物</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BLESS_PET_REQ.html'>BLESS_PET_REQ</a></td><td>0x00FFF135</td><td><a href='./BLESS_PET_RES.html'>BLESS_PET_RES</a></td><td>0x70FFF135</td><td>家族仙兽房</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CITY_SINGLE_REQ.html'>CITY_SINGLE_REQ</a></td><td>0x00FFF136</td><td><a href='./CITY_SINGLE_RES.html'>CITY_SINGLE_RES</a></td><td>0x70FFF136</td><td>单人副本</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ENTER_CITY_SINGLE_REQ.html'>ENTER_CITY_SINGLE_REQ</a></td><td>0x00FFF137</td><td><a href='./-.html'>-</a></td><td>-</td><td>进入单人副本</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./ACTIVITY_IS_SHOW_REQ.html'>ACTIVITY_IS_SHOW_REQ</a></td><td>0x00FFF138</td><td><a href='./ACTIVITY_IS_SHOW_RES.html'>ACTIVITY_IS_SHOW_RES</a></td><td>0x70FFF138</td><td>是否显示活动</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ACTIVITY_QUERY_REQ.html'>ACTIVITY_QUERY_REQ</a></td><td>0x00FFF139</td><td><a href='./ACTIVITY_QUERY_RES.html'>ACTIVITY_QUERY_RES</a></td><td>0x70FFF139</td><td>活动请求</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_HOOK_PET_REQ.html'>PLAYER_HOOK_PET_REQ</a></td><td>0x00FFF140</td><td><a href='./PLAYER_HOOK_PET_RES.html'>PLAYER_HOOK_PET_RES</a></td><td>0x70FFF140</td><td>获取玩家正在挂机的宠物id集合</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./ACTIVITY_DO_REQ.html'>ACTIVITY_DO_REQ</a></td><td>0x00FFF141</td><td><a href='./-.html'>-</a></td><td>-</td><td>活动执行</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./DAY_PACKAGE_OF_RMB_REQ.html'>DAY_PACKAGE_OF_RMB_REQ</a></td><td>0x00FFF142</td><td><a href='./DAY_PACKAGE_OF_RMB_RES.html'>DAY_PACKAGE_OF_RMB_RES</a></td><td>0x70FFF142</td><td>每日礼包</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./-.html'>-</a></td><td>-</td><td><a href='./NOTICE_BUY_SUCCESS_RES.html'>NOTICE_BUY_SUCCESS_RES</a></td><td>0x70FFF143</td><td>通知月卡,每日礼包,购买成功</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./CHARGE_LIST_REQ.html'>CHARGE_LIST_REQ</a></td><td>0x00FFF144</td><td><a href='./CHARGE_LIST_RES.html'>CHARGE_LIST_RES</a></td><td>0x70FFF144</td><td>查询充值信息新版</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./STAR_HELP_REQ.html'>STAR_HELP_REQ</a></td><td>0x00FFF145</td><td><a href='./STAR_HELP_RES.html'>STAR_HELP_RES</a></td><td>0x70FFF145</td><td>摘星帮助信息</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./STAR_MONEY_REQ.html'>STAR_MONEY_REQ</a></td><td>0x00FFF146</td><td><a href='./STAR_MONEY_RES.html'>STAR_MONEY_RES</a></td><td>0x70FFF146</td><td>摘星钱数</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./STAR_GO_REQ.html'>STAR_GO_REQ</a></td><td>0x00FFF147</td><td><a href='./STAR_GO_RES.html'>STAR_GO_RES</a></td><td>0x70FFF147</td><td>摘星确认</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./PLAYER_IN_SPESCENE2_REQ.html'>PLAYER_IN_SPESCENE2_REQ</a></td><td>0x00FFF148</td><td><a href='./PLAYER_IN_SPESCENE2_RES.html'>PLAYER_IN_SPESCENE2_RES</a></td><td>0x70FFF148</td><td>玩家是否在某个特殊场景2</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./BATTLE_GUWU_INFO_REQ.html'>BATTLE_GUWU_INFO_REQ</a></td><td>0x00FFF149</td><td><a href='./BATTLE_GUWU_INFO_RES.html'>BATTLE_GUWU_INFO_RES</a></td><td>0x70FFF149</td><td>家族鼓舞</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./FUNCTION_SHOW_REQ.html'>FUNCTION_SHOW_REQ</a></td><td>0x00FFF150</td><td><a href='./FUNCTION_SHOW_RES.html'>FUNCTION_SHOW_RES</a></td><td>0x70FFF150</td><td>功能状态</td></tr>
 * <tr bgcolor="#FFFFFF" align="center"><td><a href='./TOTLE_COST_ACTIVITY_REQ.html'>TOTLE_COST_ACTIVITY_REQ</a></td><td>0x00FFF151</td><td><a href='./TOTLE_COST_ACTIVITY_RES.html'>TOTLE_COST_ACTIVITY_RES</a></td><td>0x70FFF151</td><td>累计消费</td></tr>
 * <tr bgcolor="#FAFAFA" align="center"><td><a href='./END_DEAD_GAME_REQ.html'>END_DEAD_GAME_REQ</a></td><td>0x00FFF152</td><td><a href='./-.html'>-</a></td><td>-</td><td>脱离卡死</td></tr>
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
			}else if(type == 0x00FFFF01L){
					return new TIME_SYNC_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFFF01L){
					return new TIME_SYNC_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFFF02L){
					return new TIME_SETTING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F100L){
					return new GET_PLAYER_BY_ID_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F100L){
					return new GET_PLAYER_BY_ID_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0010F012L){
					return new USER_ENTER_SERVER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F012L){
					return new USER_ENTER_SERVER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0010F016L){
					return new USER_ENTER_SERVER2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x001AF016L){
					return new NEW_USER_ENTER_SERVER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EAF016L){
					return new NEW_USER_ENTER_SERVER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0001F013L){
					return new LINEUP_STATUS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700FF013L){
					return new LINEUP_STATUS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000FF016L){
					return new LEAVE_LINEUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700FF016L){
					return new LEAVE_LINEUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F004L){
					return new GET_USER_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F004L){
					return new GET_USER_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F005L){
					return new USER_BIND_MOBILE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F005L){
					return new USER_BIND_MOBILE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F007L){
					return new GET_PLAYER_YUANBAO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F007L){
					return new GET_PLAYER_YUANBAO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F008L){
					return new PLAYER_YUANBAO_CHANGED_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F008L){
					return new PLAYER_YUANBAO_CHANGED_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000212L){
					return new CREATE_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000212L){
					return new CREATE_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000013L){
					return new QUERY_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000013L){
					return new QUERY_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00001000L){
					return new QUERY_SIMPLE_PLAYER_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70001000L){
					return new QUERY_SIMPLE_PLAYER_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000014L){
					return new REMOVE_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000014L){
					return new REMOVE_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000016L){
					return new PLAYER_ENTER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000016L){
					return new PLAYER_ENTER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000019L){
					return new FUNCTION_NPC_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000019L){
					return new FUNCTION_NPC_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000020L){
					return new NONTARGET_SKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000021L){
					return new TARGET_SKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000022L){
					return new NONTARGET_SKILL_BROADCAST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000023L){
					return new TARGET_SKILL_BROADCAST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000123L){
					return new LASTING_SKILL_BROADCAST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000024L){
					return new PLAYER_UPDATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000024L){
					return new PLAYER_UPDATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000026L){
					return new USE_AURASKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0xA0000026L){
					return new EQUIPMENT_SKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000090L){
					return new ENTER_GAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000090L){
					return new ENTER_GAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000091L){
					return new LEAVE_CURRENT_GAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000092L){
					return new CHANGE_GAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000A0L){
					return new PLAYER_RECONN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000A0L){
					return new PLAYER_RECONN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000C0L){
					return new PLAYER_MOVE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000C1L){
					return new PLAYER_MOVETRACE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000CAL){
					return new SPECIAL_SKILL_MOVETRACE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000C2L){
					return new SET_POSITION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000C3L){
					return new TOUCH_TRANSPORT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000C4L){
					return new NOTICE_CLIENT_UPDATE_MAPRES_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000111D0L){
					return new SPRITES_IN_GAME(sn,messageContent,offset,end - offset);
			}else if(type == 0x001100D0L){
					return new AROUND_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF08L){
					return new CHANGE_PKMODE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000D1L){
					return new NOTIFY_EVENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000DFL){
					return new NOTIFY_EVENTS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000D2L){
					return new NOTIFY_BUFF_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000D3L){
					return new NOTIFY_SELFCHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000D4L){
					return new NOTIFY_BUFF_REMOVED_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000D5L){
					return new NOTIFY_FLOPAVAILABLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000D6L){
					return new QUERY_FLOPAVAILABLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000D6L){
					return new QUERY_FLOPAVAILABLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000D7L){
					return new QUERY_FLOP_SIMPLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000D7L){
					return new QUERY_FLOP_SIMPLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000D8L){
					return new PICKUP_FLOP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000DAL){
					return new PICKUP_ALLFLOP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000D9L){
					return new PREPARE_PLAY_DICE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000DCL){
					return new CPATAIN_ASSIGN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000DDL){
					return new PICKUP_MONEY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000DEL){
					return new PICKUP_CAIJINPC_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000E0L){
					return new OTHER_USER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000E1L){
					return new QUERY_SKILLINFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000E1L){
					return new QUERY_SKILLINFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000F1L){
					return new QUERY_SKILLINFO_PET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000F1L){
					return new QUERY_SKILLINFO_PET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000EEL){
					return new PARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000E2L){
					return new QUERY_ACTIVESKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000E3L){
					return new SEND_ACTIVESKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000E5L){
					return new ALLOCATE_SKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000EAL){
					return new CLEAN_SKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700100E8L){
					return new NEW_XINGFA_SKILL_MSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000E6L){
					return new QUERY_CAREER_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000E6L){
					return new QUERY_CAREER_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000ECL){
					return new NEW_QUERY_CAREER_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000ECL){
					return new NEW_QUERY_CAREER_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000001E7L){
					return new NEW_QUERY_CAREER_INFO_BY_ID_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700001E7L){
					return new NEW_QUERY_CAREER_INFO_BY_ID_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000EAL){
					return new QUERY_CAREER_XINFA_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000EDL){
					return new QUERY_CAREER_JINJIE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000EBL){
					return new QUERY_DUJIE_SKILL_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000EBL){
					return new QUERY_DUJIE_SKILL_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000001E6L){
					return new QUERY_CAREER_INFO_BY_ID_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700001E6L){
					return new QUERY_CAREER_INFO_BY_ID_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000001E8L){
					return new QUERY_XINFA_SKILL_IDLIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700001E8L){
					return new QUERY_XINFA_SKILL_IDLIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000F3L){
					return new QUERY_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000F3L){
					return new QUERY_ARTICLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000FBL){
					return new NOTIFY_DURABILITY_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000FCL){
					return new NOTIFY_EQUIPMENT_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000FDL){
					return new QUERY_ARTICLE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000FDL){
					return new QUERY_ARTICLE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000001FDL){
					return new QUERY_SUIT_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700001FDL){
					return new QUERY_SUIT_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000FEL){
					return new EQUIPMENT_RESET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000FEL){
					return new EQUIPMENT_RESET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F00L){
					return new TEAM_QUERY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000F00L){
					return new TEAM_QUERY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F01L){
					return new TEAM_INVITE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F02L){
					return new TEAM_INVITE_NOTIFY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F03L){
					return new TEAM_INVITE_RESULT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F04L){
					return new TEAM_DISSOLVE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F05L){
					return new TEAM_APPLY_JION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F06L){
					return new TEAM_APPLY_NOTIFY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F07L){
					return new TEAM_APPLY_RESULT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F08L){
					return new TEAM_APPLY_LEAVE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F09L){
					return new TEAM_SET_CAPTAIN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F0AL){
					return new TEAM_CHANGE_CAPTAIN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F0BL){
					return new TEAM_SET_ASSIGN_RULE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F0CL){
					return new TEAM_MEMBER_JOIN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F0DL){
					return new TEAM_MEMBER_LEAVE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F0EL){
					return new TEAM_QUERY_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000F0EL){
					return new TEAM_QUERY_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000E7L){
					return new QUERY_OTHER_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000E7L){
					return new QUERY_OTHER_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000E8L){
					return new QUERY_PERSON_TEAM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000E8L){
					return new QUERY_PERSON_TEAM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000E9L){
					return new SET_INTEAM_RULE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F30L){
					return new SAVE_SHORTCUT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F31L){
					return new QUERY_SHORTCUT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000F31L){
					return new QUERY_SHORTCUT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000E02L){
					return new CHAT_MESSAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000E02L){
					return new CHAT_MESSAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000E03L){
					return new FEE_CHAT_ALLOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000E04L){
					return new FEE_CHAT_CONFIRM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000E06L){
					return new CHAT_FAILED_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000E07L){
					return new CHAT_CHANNEL_STATUS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000E07L){
					return new CHAT_CHANNEL_STATUS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000E08L){
					return new CHAT_CHANNEL_SET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000E08L){
					return new CHAT_CHANNEL_SET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000E09L){
					return new CHAT_VOICE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000E09L){
					return new CHAT_VOICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000E10L){
					return new CHAT_VOICE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000E10L){
					return new CHAT_VOICE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000E0AL){
					return new CHAT_VOICE_SUCC_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FA0L){
					return new TASK_QUERY_TASK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000FA0L){
					return new TASK_QUERY_TASK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FA1L){
					return new TASK_QUERY_ENTITIES_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000FA1L){
					return new TASK_QUERY_ENTITIES_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FA2L){
					return new TASK_SEND_ENTITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FA3L){
					return new TASK_SEND_ACTION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000FA3L){
					return new TASK_SEND_ACTION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FA4L){
					return new OPEN_ACCEPT_TASK_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000A8L){
					return new TAKE_TASK_BY_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FA5L){
					return new FUNCTION_NPC_MODIFY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FA6L){
					return new CAN_ACCEPT_TASK_MODIFY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FA7L){
					return new QUERY_CAN_ACCEPT_TASK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000FA7L){
					return new QUERY_CAN_ACCEPT_TASK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FA8L){
					return new QUERY_AUTO_HOOK_USE_PROP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000FA8L){
					return new QUERY_AUTO_HOOK_USE_PROP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FA9L){
					return new COLLECTION_NPC_MODIFY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000FA9L){
					return new COLLECTION_NPC_MODIFY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FAAL){
					return new WANNA_COLLECTION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000FAAL){
					return new WANNA_COLLECTION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FABL){
					return new NOTICE_CLIENT_READ_TIMEBAR_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FAEL){
					return new NOTICE_CLIENT_READ_TIMEBAR_INTERRUPT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FFBL){
					return new NOTICE_CLIENT_COUNTDOWN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FACL){
					return new NOTICE_CLIENT_FOLLOWNPC_FARAWAY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000FADL){
					return new NOTICE_CLIENT_FOLLOWNPC_NEAR_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E000L){
					return new PASSPORT_RANDOM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000E000L){
					return new PASSPORT_RANDOM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000E003L){
					return new PASSPORT_UPDATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000E003L){
					return new PASSPORT_UPDATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A001L){
					return new CREATE_DEAL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A002L){
					return new DEAL_CREATED_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A003L){
					return new DEAL_ADD_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000A003L){
					return new DEAL_ADD_ARTICLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A004L){
					return new DEAL_DELETE_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A005L){
					return new DEAL_MOD_COINS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A006L){
					return new DEAL_UPDATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A007L){
					return new DEAL_AGREE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A008L){
					return new DEAL_DISAGREE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A009L){
					return new DEAL_CANCEL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A00AL){
					return new DEAL_MAKED_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A00BL){
					return new DEAL_LOCK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000A00BL){
					return new DEAL_LOCK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000B001L){
					return new WAREHOUSE_GET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000B001L){
					return new WAREHOUSE_GET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000B011L){
					return new WAREHOUSE_GET_CARRY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000B011L){
					return new WAREHOUSE_GET_CARRY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000B002L){
					return new WAREHOUSE_MOVE_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000B005L){
					return new WAREHOUSE_ARRANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000B005L){
					return new WAREHOUSE_ARRANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000B006L){
					return new WAREHOUSE_SET_PASSWORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000B006L){
					return new WAREHOUSE_SET_PASSWORD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000B007L){
					return new WAREHOUSE_MODIFY_PASSWORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000B007L){
					return new WAREHOUSE_MODIFY_PASSWORD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000B008L){
					return new WAREHOUSE_INPUT_PASSWORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000B008L){
					return new WAREHOUSE_INPUT_PASSWORD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000C001L){
					return new MAIL_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000C001L){
					return new MAIL_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000C011L){
					return new MAIL_LIST_CARRY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000C011L){
					return new MAIL_LIST_CARRY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000C006L){
					return new MAIL_LIST_BY_STATUS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000C006L){
					return new MAIL_LIST_BY_STATUS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000C021L){
					return new MAIL_LIST_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000C021L){
					return new MAIL_LIST_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000C022L){
					return new MAIL_LIST_CARRY_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000C022L){
					return new MAIL_LIST_CARRY_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000C023L){
					return new MAIL_LIST_BY_STATUS_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000C023L){
					return new MAIL_LIST_BY_STATUS_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000C002L){
					return new MAIL_GET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000C002L){
					return new MAIL_GET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000C003L){
					return new MAIL_GETOUT_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000C003L){
					return new MAIL_GETOUT_ARTICLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000C004L){
					return new MAIL_DELETE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000C004L){
					return new MAIL_DELETE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000C012L){
					return new MAIL_DELETE_ALL_READED_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000C012L){
					return new MAIL_DELETE_ALL_READED_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000C005L){
					return new MAIL_CREATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000C015L){
					return new MAIL_MSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000D001L){
					return new AUCTION_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000D001L){
					return new AUCTION_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000D002L){
					return new AUCTION_CREATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000D004L){
					return new AUCTION_BUY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000D005L){
					return new AUCTION_TYPE_GET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000D005L){
					return new AUCTION_TYPE_GET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000D006L){
					return new AUCTION_CANCEL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EA11L){
					return new SHOPS_NAME_GET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EA11L){
					return new SHOPS_NAME_GET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EA01L){
					return new SHOP_GET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EA01L){
					return new SHOP_GET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EA05L){
					return new SHOP_OTHER_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EA12L){
					return new SHOP_GET_BY_QUERY_CONDITION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EA12L){
					return new SHOP_GET_BY_QUERY_CONDITION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EA02L){
					return new SHOP_BUY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EA02L){
					return new SHOP_BUY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EA03L){
					return new SHOP_SELL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EA04L){
					return new SHOP_BUYBACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EAF0L){
					return new PLAYER_DEAD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EAF1L){
					return new PLAYER_REVIVED_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EAF1L){
					return new PLAYER_REVIVED_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EAF2L){
					return new PLAYER_REVIVED_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EAF2L){
					return new PLAYER_REVIVED_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EEEDL){
					return new QUERY_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0xF0F0EEEDL){
					return new QUERY_WINDOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EEECL){
					return new OPTION_SELECT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EEEEL){
					return new CONFIRM_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EEEFL){
					return new INPUT_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EEE1L){
					return new INPUT_WINDOW_PNG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EEF0L){
					return new OPTION_INPUT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EEF2L){
					return new NPC_REPAIR_QUERY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EEF2L){
					return new NPC_REPAIR_QUERY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EEF3L){
					return new NPC_REPAIR_REPAIR_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EEF3L){
					return new NPC_REPAIR_REPAIR_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EE13L){
					return new REPAIR_CARRY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EE13L){
					return new REPAIR_CARRY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F1EEF0L){
					return new EQUIPMENT_COMPOUND_PREPARE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F1EEF1L){
					return new EQUIPMENT_COMPOUND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F1EEF1L){
					return new EQUIPMENT_COMPOUND_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F1EEF2L){
					return new EQUIPMENT_DRILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F1EEF2L){
					return new EQUIPMENT_DRILL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EEFFL){
					return new TRANSIENTENEMY_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF00L){
					return new HINT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F1EEEEL){
					return new QUERY_TOPO_DIAGRAM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0xF0F1EEEEL){
					return new QUERY_TOPO_DIAGRAM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EB04L){
					return new CONNECT_FAILED_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE01L){
					return new JIAZU_CREATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE01L){
					return new JIAZU_CREATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE02L){
					return new JIAZU_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE02L){
					return new JIAZU_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE03L){
					return new JIAZU_APPLY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE03L){
					return new JIAZU_APPLY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE04L){
					return new JIAZU_QUERY_APPLY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE04L){
					return new JIAZU_QUERY_APPLY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE05L){
					return new JIAZU_APPROVE_APPLY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE05L){
					return new JIAZU_APPROVE_APPLY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE06L){
					return new JIAZU_EXPEL_MEMBER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE06L){
					return new JIAZU_EXPEL_MEMBER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE07L){
					return new JIAZU_LEAVE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE07L){
					return new JIAZU_LEAVE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE08L){
					return new JIAZU_QURRY_BASE_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE08L){
					return new JIAZU_QURRY_BASE_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE09L){
					return new JIAZU_BASE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE09L){
					return new JIAZU_BASE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE10L){
					return new JIAZU_MASTER_RESIGN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE10L){
					return new JIAZU_MASTER_RESIGN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE11L){
					return new JIAZU_APPLY_MASTER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE11L){
					return new JIAZU_APPLY_MASTER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE12L){
					return new JIAZU_DISMISS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE12L){
					return new JIAZU_DISMISS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE13L){
					return new JIAZU_MODIFY_SLOGAN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE13L){
					return new JIAZU_MODIFY_SLOGAN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE14L){
					return new JIAZU_APPOINT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE14L){
					return new JIAZU_APPOINT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE15L){
					return new JIAZU_SET_RANK_NAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE15L){
					return new JIAZU_SET_RANK_NAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE16L){
					return new JIAZU_SET_ICON_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE16L){
					return new JIAZU_SET_ICON_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE17L){
					return new JIAZU_LIST_SALARY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE17L){
					return new JIAZU_LIST_SALARY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE18L){
					return new JIAZU_SALARY_CEREMONY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE18L){
					return new JIAZU_SALARY_CEREMONY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE19L){
					return new JIAZU_QUERY_MEMBER_SALARY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE19L){
					return new JIAZU_QUERY_MEMBER_SALARY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE20L){
					return new JIAZU_P_SALARY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE20L){
					return new JIAZU_P_SALARY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE21L){
					return new JIAZU_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE21L){
					return new JIAZU_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE23L){
					return new JIAZU_BUY_BEDGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE23L){
					return new JIAZU_BUY_BEDGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE24L){
					return new JIAZU_REPLACE_BEDGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE24L){
					return new JIAZU_REPLACE_BEDGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE25L){
					return new JIAZU_QUERY_BY_ID_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE25L){
					return new JIAZU_QUERY_BY_ID_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE26L){
					return new JIAZU_INVITE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE26L){
					return new JIAZU_INVITE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE27L){
					return new JIAZU_SHOW_JIAZU_FUNCTION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE27L){
					return new JIAZU_SHOW_JIAZU_FUNCTION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE28L){
					return new JIAZU_QUERY_STATION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE28L){
					return new JIAZU_QUERY_STATION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE29L){
					return new JIAZU_TITLE_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE29L){
					return new JIAZU_TITLE_CHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE2AL){
					return new JIAZU_BATCH_SEND_SALARY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE2AL){
					return new JIAZU_BATCH_SEND_SALARY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE2BL){
					return new JIAZU_CONTRIBUTE_MONEY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE2BL){
					return new JIAZU_CONTRIBUTE_MONEY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE2CL){
					return new JIAZU_WAREHOUSE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE2DL){
					return new JIAZU_WAREHOUSE_TOPLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE2DL){
					return new JIAZU_WAREHOUSE_TOPLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000AEE2EL){
					return new JIAZU_INFO_ADD_TIME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700AEE2EL){
					return new JIAZU_INFO_ADD_TIME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EEE01L){
					return new SEPTBUILDING_CREATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EEE01L){
					return new SEPTBUILDING_CREATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EEE02L){
					return new SEPTBUILDING_LVUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EEE02L){
					return new SEPTBUILDING_LVUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EEE03L){
					return new SEPTBUILDING_LVDOWN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EEE03L){
					return new SEPTBUILDING_LVDOWN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EEE04L){
					return new SEPTBUILDING_QUERY_CANUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EEE04L){
					return new SEPTBUILDING_QUERY_CANUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EEE05L){
					return new SEPTBUILDING_QUERY_BUILDING_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EEE05L){
					return new SEPTBUILDING_QUERY_BUILDING_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EEE06L){
					return new SEPTBUILDING_CREAT_SEPT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EEE06L){
					return new SEPTBUILDING_CREAT_SEPT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EEE08L){
					return new SEPTBUILDING_DESTROY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EEE08L){
					return new SEPTBUILDING_DESTROY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EEE09L){
					return new SEPTBUILDING_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EEE09L){
					return new SEPTBUILDING_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EEE0AL){
					return new JIAZU_CALL_IN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EEE0BL){
					return new NOTICE_CLIENT_SURFACENPC_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EEE0CL){
					return new NOTICE_CLIENT_JIAZUBOSS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EEE0DL){
					return new QUERY_JIAZUBOSS_DAMAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EEE0DL){
					return new QUERY_JIAZUBOSS_DAMAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00001L){
					return new BOOTHSALE_LOOK_OVER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00001L){
					return new BOOTHSALE_LOOK_OVER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00002L){
					return new BOOTHSALE_SELECT_FOR_SALE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00002L){
					return new BOOTHSALE_SELECT_FOR_SALE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00003L){
					return new BOOTHSALE_CANCEL_FOR_SALE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00003L){
					return new BOOTHSALE_CANCEL_FOR_SALE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00004L){
					return new BOOTHSALE_ADVERTISING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00004L){
					return new BOOTHSALE_ADVERTISING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00005L){
					return new BOOTHSALE_BUY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00005L){
					return new BOOTHSALE_BUY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00006L){
					return new BOOTHSALE_LEAVE_WORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00006L){
					return new BOOTHSALE_LEAVE_WORD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00007L){
					return new BOOTHSALE_CANCEL_BOOTHSALE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00007L){
					return new BOOTHSALE_CANCEL_BOOTHSALE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00008L){
					return new BOOTHSALE_BOOTHSALE_REQUEST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00008L){
					return new BOOTHSALE_BOOTHSALE_REQUEST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00010L){
					return new BOOTHSALE_BOOTHCHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00010L){
					return new BOOTHSALE_BOOTHCHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00011L){
					return new BOOTHSALE_LEAVE_BOOTH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00011L){
					return new BOOTHSALE_LEAVE_BOOTH_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00012L){
					return new BOOTHSALE_QUIT_BOOTHSALE_REQUEST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00012L){
					return new BOOTHSALE_QUIT_BOOTHSALE_REQUEST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00014L){
					return new BOOTHSALE_CHANAGE_ADVERTISING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00014L){
					return new BOOTHSALE_CHANAGE_ADVERTISING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00015L){
					return new BOOTHSALE_NEW_BUY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00102L){
					return new REQUESTBUY_RELEASE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00102L){
					return new REQUESTBUY_RELEASE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00103L){
					return new REQUESTBUY_QUERY_SELF_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00103L){
					return new REQUESTBUY_QUERY_SELF_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00104L){
					return new REQUESTBUY_CANCEL_SELF_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00104L){
					return new REQUESTBUY_CANCEL_SELF_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00105L){
					return new REQUESTBUY_QRY_HIGH_PRICE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00105L){
					return new REQUESTBUY_QRY_HIGH_PRICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00106L){
					return new REQUESTBUY_SALE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00106L){
					return new REQUESTBUY_SALE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00107L){
					return new REQUESTBUY_QRY_BY_TERM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00107L){
					return new REQUESTBUY_QRY_BY_TERM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F00108L){
					return new REQUESTBUY_ASK_KNAP_REMAI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00108L){
					return new REQUESTBUY_ASK_KNAP_REMAI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00f00109L){
					return new REQUESTBUY_CONTIDION_TYPE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00109L){
					return new REQUESTBUY_CONTIDION_TYPE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00f0010AL){
					return new REQUESTBUY_GET_PROPNAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0010AL){
					return new REQUESTBUY_GET_PROPNAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00f0010BL){
					return new REQUESTBUY_GET_ENTITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0010BL){
					return new REQUESTBUY_GET_ENTITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00f0010CL){
					return new REQUESTBUY_FASTBUY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0010CL){
					return new REQUESTBUY_FASTBUY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00f0010DL){
					return new REQUESTBUY_SUISHEN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70f0010DL){
					return new REQUESTBUY_SUISHEN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FA0100L){
					return new PLAYER_ACTIVATION_SOUL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FA0100L){
					return new PLAYER_ACTIVATION_SOUL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FA0101L){
					return new PLAYER_SWITCH_SOUL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FA0101L){
					return new PLAYER_SWITCH_SOUL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FA0102L){
					return new PLAYER_SOUL_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FA0102L){
					return new PLAYER_SOUL_CHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF00L){
					return new CARD_SAVING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EF00L){
					return new CARD_SAVING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF01L){
					return new SAVING_HISTORY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EF01L){
					return new SAVING_HISTORY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F00000L){
					return new SET_QUEUE_READYNUM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF02L){
					return new GANGWAREHOUSE_GET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EF02L){
					return new GANGWAREHOUSE_GET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF03L){
					return new GANGWAREHOUSE_PUT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF04L){
					return new GANGWAREHOUSE_TAKE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF05L){
					return new GANGWAREHOUSE_ARRANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF06L){
					return new CONTRIBUTION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF07L){
					return new GANG_WARE_HOUSE_JOURNAL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EF07L){
					return new GANG_WARE_HOUSE_JOURNAL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF08L){
					return new MAP_POLYGON_MODIFY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF09L){
					return new SKILL_CD_MODIFY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0200EF09L){
					return new PROPS_CD_MODIFY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF10L){
					return new GET_BILLBOARD_MENU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EF10L){
					return new GET_BILLBOARD_MENU_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF11L){
					return new GET_BILLBOARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EF11L){
					return new GET_BILLBOARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF12L){
					return new EQUIPMENT_JOIN_BILLBOARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF13L){
					return new GET_CARD_CHARGE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EF13L){
					return new GET_CARD_CHARGE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF14L){
					return new GET_SMS_CHARGE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EF14L){
					return new GET_SMS_CHARGE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF15L){
					return new SMS_CHARGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EF17L){
					return new SMS_BIND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EF17L){
					return new SMS_BIND_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF01L){
					return new NEW_RECOMMEND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000AF01L){
					return new NEW_RECOMMEND_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF02L){
					return new QUERY_ACTIVITIES_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000AF02L){
					return new QUERY_ACTIVITIES_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF03L){
					return new QUERY_ACTIVITIES_DETAIL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000AF03L){
					return new QUERY_ACTIVITIES_DETAIL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF05L){
					return new QUERY_NETWORKFLOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF0CL){
					return new QUERY_HONOR_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000AF0CL){
					return new QUERY_HONOR_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF0DL){
					return new QUERY_HONOR_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000AF0DL){
					return new QUERY_HONOR_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF1DL){
					return new QUERY_HONOR_INFO_BY_NAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000AF1DL){
					return new QUERY_HONOR_INFO_BY_NAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF0EL){
					return new HONOR_OPRATION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF0FL){
					return new MASTERS_AND_PRENTICES_OPERATION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF10L){
					return new QUERY_MASTERS_AND_PRENTICES_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000AF10L){
					return new QUERY_MASTERS_AND_PRENTICES_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF11L){
					return new QUERY_MASTERS_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000AF11L){
					return new QUERY_MASTERS_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF12L){
					return new QUERY_PRENTICES_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000AF12L){
					return new QUERY_PRENTICES_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF13L){
					return new NOTICE_DELETE_MASTERPRENTICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF20L){
					return new QUERY_BATTLEFIELDLIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000AF20L){
					return new QUERY_BATTLEFIELDLIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF21L){
					return new BATTLEFIELD_ACTION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF22L){
					return new QUERY_BATTLEFIELD_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000AF22L){
					return new QUERY_BATTLEFIELD_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF23L){
					return new DISPLAY_INFO_ON_SCREEN(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000AF30L){
					return new GANG_MAIL_CREATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF12L){
					return new PLAYER_ENTER_DUELBATTLE_MONI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF12L){
					return new PLAYER_ENTER_DUELBATTLE_MONI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF10L){
					return new SET_PET_ACTIVATIONTYPE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EE10L){
					return new QUERY_PET_STRONG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EE10L){
					return new QUERY_PET_STRONG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EE12L){
					return new PET_STRONG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EE12L){
					return new PET_STRONG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF13L){
					return new PET_SKILL_CAST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF14L){
					return new PET_NONTARGET_SKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF15L){
					return new PET_TARGET_SKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF16L){
					return new PET_QUERY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF16L){
					return new PET_QUERY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF17L){
					return new PET_BREEDING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF17L){
					return new PET_BREEDING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF18L){
					return new PET_MATING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF18L){
					return new PET_MATING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF19L){
					return new PET_MATING_ADDPET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF19L){
					return new PET_MATING_ADDPET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF34L){
					return new PET_MATING_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF35L){
					return new PET_MATING_CANCEL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF35L){
					return new PET_MATING_CANCEL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF20L){
					return new PET_MATING_CONFIRM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF20L){
					return new PET_MATING_CONFIRM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF31L){
					return new PET_MATING_SESSION_OVER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF21L){
					return new PET_IDENTIFY_QUALITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF21L){
					return new PET_IDENTIFY_QUALITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF22L){
					return new PET_GENGUDAN_COMPOUND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF22L){
					return new PET_GENGUDAN_COMPOUND_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF23L){
					return new PET_GENGU_UP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF23L){
					return new PET_GENGU_UP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF37L){
					return new QUERY_PET_MERGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF37L){
					return new QUERY_PET_MERGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF24L){
					return new PET_MERGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF24L){
					return new PET_MERGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF25L){
					return new PET_BECHILD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF25L){
					return new PET_BECHILD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF38L){
					return new QUERY_PET_FEED_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF38L){
					return new QUERY_PET_FEED_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF26L){
					return new PET_FEED_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF26L){
					return new PET_FEED_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF27L){
					return new PET_NONTARGET_SKILL_BROADCAST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF28L){
					return new PET_TARGET_SKILL_BROADCAST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF29L){
					return new PET_CALLBACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF29L){
					return new PET_CALLBACK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF30L){
					return new PET_QUERY_BY_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF32L){
					return new PET_RENAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF32L){
					return new PET_RENAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF33L){
					return new PET_ALLOCATE_POINTS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF33L){
					return new PET_ALLOCATE_POINTS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF89L){
					return new OPRATE_PET_ALLOCATE_POINTS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF36L){
					return new PET_MATING_PERSONAL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF36L){
					return new PET_MATING_PERSONAL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF60L){
					return new QUERY_PET_CHUZHAN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF60L){
					return new QUERY_PET_CHUZHAN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF61L){
					return new PET_SEAL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF62L){
					return new ARTICLE_OPRATE_RESULT(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF63L){
					return new PET_REPAIR_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF63L){
					return new PET_REPAIR_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000FF64L){
					return new PET_REPAIR_REPLACE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000FF64L){
					return new PET_REPAIR_REPLACE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0001FF01L){
					return new QUERY_ALL_ACHIEVEMENT_SERIES_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7001FF01L){
					return new QUERY_ALL_ACHIEVEMENT_SERIES_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0001FF02L){
					return new QUERY_ACHIEVEMENT_SERIES_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7001FF02L){
					return new QUERY_ACHIEVEMENT_SERIES_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0001FF03L){
					return new QUERY_ACHIEVEMENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7001FF03L){
					return new QUERY_ACHIEVEMENT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0001FF04L){
					return new MASTER_AND_PRENTICE_RELATIONSHIP_CHANGED_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A000L){
					return new QUERY_EQUIPMENT_JIANDING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7003A000L){
					return new QUERY_EQUIPMENT_JIANDING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A001L){
					return new EQUIPMENT_JIANDING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7003A0A0L){
					return new NEW_JIANDING_MSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A0A1L){
					return new NEW_JIANDING_OK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7003A0A1L){
					return new NEW_JIANDING_OK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A002L){
					return new QUERY_EQUIPMENT_INSCRIPTION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7003A002L){
					return new QUERY_EQUIPMENT_INSCRIPTION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A003L){
					return new EQUIPMENT_INSCRIPTION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A020L){
					return new EQUIPMENT_INLAY_UUB_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7003A020L){
					return new EQUIPMENT_INLAY_UUB_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A004L){
					return new EQUIPMENT_INLAY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A005L){
					return new EQUIPMENT_OUTLAY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EEF5L){
					return new QUERY_EQUIPMENT_STRONG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EEF5L){
					return new QUERY_EQUIPMENT_STRONG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EEF6L){
					return new EQUIPMENT_STRONG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EEF6L){
					return new EQUIPMENT_STRONG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A006L){
					return new QUERY_EQUIPMENT_PICKSTAR_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7003A006L){
					return new QUERY_EQUIPMENT_PICKSTAR_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A007L){
					return new EQUIPMENT_PICKSTAR_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A015L){
					return new QUERY_EQUIPMENT_INSERTSTAR_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7003A015L){
					return new QUERY_EQUIPMENT_INSERTSTAR_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A008L){
					return new EQUIPMENT_INSERTSTAR_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A009L){
					return new EQUIPMENT_DEVELOP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7003A009L){
					return new EQUIPMENT_DEVELOP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A010L){
					return new QUERY_EQUIPMENT_BIND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7003A010L){
					return new QUERY_EQUIPMENT_BIND_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A012L){
					return new EQUIPMENT_BIND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0003A013L){
					return new EQUIPMENT_DEVELOPUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000FFL){
					return new ARTICLE_COMPOSE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000002FFL){
					return new QUERY_ARTICLE_COMPOSE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700002FFL){
					return new QUERY_ARTICLE_COMPOSE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000003FFL){
					return new PLAY_ANIMATION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000F4L){
					return new QUERY_EQUIPMENT_TABLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000F4L){
					return new QUERY_EQUIPMENT_TABLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000FAL){
					return new NOTIFY_EQUIPMENT_TABLECHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000FAL){
					return new NOTIFY_EQUIPMENT_TABLECHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000F5L){
					return new QUERY_KNAPSACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700000F5L){
					return new QUERY_KNAPSACK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000001F7L){
					return new QUERY_KNAPSACK_FB_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700001F7L){
					return new QUERY_KNAPSACK_FB_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000001F5L){
					return new Fangbao_KNAPSACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700001F5L){
					return new Fangbao_KNAPSACK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000F6L){
					return new ARTICLE_OPRATION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000A0F6L){
					return new HOOK_USE_PROP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000001F6L){
					return new ARTICLE_OPRATION_MOVE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000F7L){
					return new REMOVE_EQUIPMENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000100L){
					return new REMOVE_AVATAPROPS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000F8L){
					return new SWITCH_SUIT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000000F9L){
					return new NOTIFY_KNAPSACKCHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F21L){
					return new HELP_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000F21L){
					return new HELP_WINDOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000F20L){
					return new RESOURCE_DATA_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000F20L){
					return new RESOURCE_DATA_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000018L){
					return new RESOURCE_DATA_REQ_PROCESS(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000127L){
					return new QUERY_PLAYER_HORSE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000127L){
					return new QUERY_PLAYER_HORSE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000126L){
					return new SET_DEFAULT_HORSE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000126L){
					return new SET_DEFAULT_HORSE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000118L){
					return new QUERY_HORSE_LIST(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000118L){
					return new QUERY_HORSE_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000119L){
					return new NOTIFY_HORSE_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000120L){
					return new HORSE_PUTONOROFF_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000120L){
					return new HORSE_PUTONOROFF_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000121L){
					return new HORSE_RIDE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000121L){
					return new HORSE_RIDE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000125L){
					return new QUERY_FEED_HORSE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000125L){
					return new QUERY_FEED_HORSE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000122L){
					return new FEED_HORSE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000122L){
					return new FEED_HORSE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000123L){
					return new QUERY_HORSE_EQUIPMENTS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000123L){
					return new QUERY_HORSE_EQUIPMENTS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000124L){
					return new USE_HORSE_RESULT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC01L){
					return new GET_TYPEPLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC01L){
					return new GET_TYPEPLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC30L){
					return new GET_TYPEPLAYER_REQ2(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC30L){
					return new GET_TYPEPLAYER_RES2(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC31L){
					return new TX_GAMELEVEL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC32L){
					return new PLAYER_HOOK_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC32L){
					return new PLAYER_HOOK_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC22L){
					return new GET_CHATGROUPS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC22L){
					return new GET_CHATGROUPS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC17L){
					return new QUERY_GROUPMEMBERS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC17L){
					return new QUERY_GROUPMEMBERS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC02L){
					return new SOCIAL_RELATION_MANAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC02L){
					return new SOCIAL_RELATION_MANAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC08L){
					return new SOCIAL_REFUSE_FRIEND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC03L){
					return new SOCIAL_QUERY_PLAYERINFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC03L){
					return new SOCIAL_QUERY_PLAYERINFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC04L){
					return new SEND_SOCIETY_OPRATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC04L){
					return new SEND_SOCIETY_OPRATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC05L){
					return new QUERY_FRIEND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC05L){
					return new QUERY_FRIEND_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC20L){
					return new QUERY_FRIEND2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC20L){
					return new QUERY_FRIEND2_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC07L){
					return new QUERY_PERSONNAL_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC07L){
					return new QUERY_PERSONNAL_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC06L){
					return new SET_PLAYERINFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC06L){
					return new SET_PLAYERINFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC09L){
					return new CREATE_GROUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC09L){
					return new CREATE_GROUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC10L){
					return new DISMISS_GROUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC10L){
					return new DISMISS_GROUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC11L){
					return new DEL_GROUP_MEMBER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC11L){
					return new DEL_GROUP_MEMBER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC12L){
					return new LEAVE_GROUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC12L){
					return new LEAVE_GROUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC13L){
					return new APPLY_GROUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC13L){
					return new APPLY_GROUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC14L){
					return new GROUPMASTER_APPLY_GROUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC14L){
					return new GROUPMASTER_APPLY_GROUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC18L){
					return new QUERY_GROUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC18L){
					return new QUERY_GROUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC15L){
					return new SET_AUTOBACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC15L){
					return new SET_AUTOBACK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000EC16L){
					return new QUERY_AUTOBACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000EC16L){
					return new QUERY_AUTOBACK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F001L){
					return new CREATE_ZONGPAI_APPLY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F001L){
					return new CREATE_ZONGPAI_APPLY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F021L){
					return new CREATE_ZONGPAI_CHECK_NAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F021L){
					return new CREATE_ZONGPAI_CHECK_NAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F022L){
					return new CREATE_ZONGPAI_INPUT_PASSWORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F050L){
					return new CREATE_ZONGPAI_SUCCESS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F113L){
					return new ZONGZHU_CHECK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F113L){
					return new ZONGZHU_CHECK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F112L){
					return new DISMISS_ZONGPAI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F112L){
					return new DISMISS_ZONGPAI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F014L){
					return new UPDATE_ZONGPAI_DECLARATION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F014L){
					return new UPDATE_ZONGPAI_DECLARATION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F015L){
					return new KICK_ZONGPAI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F116L){
					return new DEMISE_ZONGPAI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F116L){
					return new DEMISE_ZONGPAI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F017L){
					return new LEAVE_ZONGPAI_APPLY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F017L){
					return new LEAVE_ZONGPAI_APPLY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F018L){
					return new ADD_ZONGPAI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F019L){
					return new QUERY_ZONGPAI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F019L){
					return new ZONGPAI_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F030L){
					return new UNITE_APPLY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F030L){
					return new UNITE_APPLY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F031L){
					return new UNITE_DISAGREE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F031L){
					return new UNITE_AGREEORNO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F032L){
					return new UNITE_CONFIRM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F034L){
					return new UNITE_EXIT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F034L){
					return new UNITE_EXIT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F035L){
					return new UNITE_DISMISS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F036L){
					return new UNITE_ADD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F037L){
					return new CLOSE_WINDOWS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F101L){
					return new TAKE_MASTER_PRNTICE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F101L){
					return new TAKE_MASTER_PRNTICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F102L){
					return new REBEL_EVICT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F102L){
					return new REBEL_EVICT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F103L){
					return new REGESTER_CANCLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F105L){
					return new QUERY_MASTERPRENTICE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F105L){
					return new QUERY_MASTERPRENTICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F106L){
					return new QUERY_MASTERPRENTICEINFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F106L){
					return new QUERY_MASTERPRENTICEINFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F200L){
					return new ACCEPT_CHUANGONG_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F201L){
					return new APPLY_CHUANGONG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F202L){
					return new AGREE_CHUANGONG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F203L){
					return new FINISH_CHUANGONG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F203L){
					return new FINISH_CHUANGONG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F300L){
					return new QUERY_PLAYER_TITLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F300L){
					return new QUERY_PLAYER_TITLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F301L){
					return new SET_DEFAULT_TITLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F301L){
					return new SET_DEFAULT_TITLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F040L){
					return new QUERY_WORLD_MAP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F040L){
					return new QUERY_WORLD_MAP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F041L){
					return new QUERY_WORLD_MAP_AREAMAP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F041L){
					return new QUERY_WORLD_MAP_AREAMAP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F048L){
					return new QUERY_MAP_SEARCH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F048L){
					return new QUERY_MAP_SEARCH_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F045L){
					return new QUERY_WORLD_MAP_AREAMAP_BY_MAPNAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F045L){
					return new QUERY_WORLD_MAP_AREAMAP_BY_MAPNAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F046L){
					return new QUERY_WORLD_MAP_AREAMAP_BY_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F046L){
					return new QUERY_WORLD_MAP_AREAMAP_BY_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F042L){
					return new QUERY_GAMEMAP_NPCMONSTER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F042L){
					return new QUERY_GAMEMAP_NPCMONSTER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0000F043L){
					return new QUERY_GAMEMAP_MOVE_LIVINGOBJECT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x7000F043L){
					return new QUERY_GAMEMAP_MOVE_LIVINGOBJECT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010001L){
					return new MARRIAGE_TEST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010002L){
					return new MARRIAGE_BEQSTART_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010003L){
					return new MARRIAGE_BEQ_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010004L){
					return new MARRIAGE_MENU(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010005L){
					return new MARRIAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010006L){
					return new MARRIAGE_ASSIGN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010006L){
					return new MARRIAGE_ASSIGN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010007L){
					return new MARRIAGE_ASSIGN_CHOOSE1_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010007L){
					return new MARRIAGE_ASSIGN_CHOOSE1_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010008L){
					return new MARRIAGE_ASSIGN_CHOOSE2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010008L){
					return new MARRIAGE_ASSIGN_CHOOSE2_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010009L){
					return new MARRIAGE_CHOOSE_GUEST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010009L){
					return new MARRIAGE_CHOOSE_GUEST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010010L){
					return new MARRIAGE_CANCEL_GUEST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010010L){
					return new MARRIAGE_CANCEL_GUEST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010011L){
					return new MARRIAGE_GUEST_OVER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010011L){
					return new MARRIAGE_GUEST_OVER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010012L){
					return new MARRIAGE_TIME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010012L){
					return new MARRIAGE_TIME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010013L){
					return new MARRIAGE_TIME_OK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010014L){
					return new MARRIAGE_FINISH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010014L){
					return new MARRIAGE_FINISH_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010015L){
					return new MARRIAGE_BEQ_FLOWER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010016L){
					return new MARRIAGE_JIAOHUAN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010016L){
					return new MARRIAGE_JIAOHUAN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010017L){
					return new MARRIAGE_JIAOHUAN2OTHER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010018L){
					return new MARRIAGE_CANCEL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010018L){
					return new MARRIAGE_CANCEL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010019L){
					return new MARRIAGE_JOIN_HUNLI_SCREEN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010019L){
					return new MARRIAGE_JOIN_HUNLI_SCREEN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010020L){
					return new GET_MARRIAGE_FRIEND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010020L){
					return new GET_MARRIAGE_FRIEND_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010021L){
					return new MARRIAGE_TARGET_MENU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010021L){
					return new MARRIAGE_TARGET_MENU_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010022L){
					return new MARRIAGE_DELAYTIME_BEGIN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70010022L){
					return new MARRIAGE_DELAYTIME_BEGIN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00010023L){
					return new MARRIAGE_TARGET_SEND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000001L){
					return new COUNTRY_COMMISSION_OR_RECALL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000AA2L){
					return new QUERY_COUNTRY_COMMISSION_OR_RECALL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000AA2L){
					return new QUERY_COUNTRY_COMMISSION_OR_RECALL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000AAAL){
					return new QUERY_COUNTRY_QIUJIN_JINYAN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000AAAL){
					return new QUERY_COUNTRY_QIUJIN_JINYAN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000002L){
					return new COUNTRY_HONOURSOR_OR_CANCEL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000003L){
					return new COUNTRY_COMMEND_OR_CANCEL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000AA6L){
					return new QUERY_COUNTRY_VOTE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000AA6L){
					return new QUERY_COUNTRY_VOTE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000006L){
					return new COUNTRY_VOTE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000007L){
					return new COUNTRY_WANGZHEZHIYIN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000007L){
					return new COUNTRY_WANGZHEZHIYIN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0xA0000008L){
					return new COUNTRY_QIUJIN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0xA0000009L){
					return new COUNTRY_JINYAN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0xA000006L){
					return new COUNTRY_JIECHU_QIUJIN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0xA0000010L){
					return new COUNTRY_JIECHU_JINYAN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000AA0L){
					return new QUERY_COUNTRY_MAIN_PAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000AA0L){
					return new QUERY_COUNTRY_MAIN_PAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000AA3L){
					return new COUNTRY_LINGQU_FENGLU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000AA8L){
					return new COUNTRY_FAFANG_FENGLU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000AA4L){
					return new QUERY_COUNTRY_WANGZHEZHIYIN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000AA4L){
					return new QUERY_COUNTRY_WANGZHEZHIYIN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000AA5L){
					return new QUERY_COUNTRY_YULINJUN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000AA5L){
					return new QUERY_COUNTRY_YULINJUN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000AA7L){
					return new QUERY_COUNTRY_JINKU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000AA7L){
					return new QUERY_COUNTRY_JINKU_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00000AB1L){
					return new QUERY_COUNTRY_GONGGAO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70000AB1L){
					return new QUERY_COUNTRY_GONGGAO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000001L){
					return new CAVE_QUERY_FAERYLIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000001L){
					return new CAVE_QUERY_FAERYLIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000002L){
					return new CAVE_OPTION_CAVE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000002L){
					return new CAVE_OPTION_CAVE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000003L){
					return new CAVE_LVUP_BUILDING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000003L){
					return new CAVE_LVUP_BUILDING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000004L){
					return new CAVE_DOOR_OPTION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000004L){
					return new CAVE_DOOR_OPTION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000005L){
					return new CAVE_ASSART_FIELD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000005L){
					return new CAVE_ASSART_FIELD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000006L){
					return new CAVE_QUERY_ALL_PLANT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000006L){
					return new CAVE_QUERY_ALL_PLANT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000007L){
					return new CAVE_PLANT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000007L){
					return new CAVE_PLANT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000008L){
					return new CAVE_HARVEST_PLANT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000008L){
					return new CAVE_HARVEST_PLANT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000009L){
					return new CAVE_CONVERT_PLANT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000009L){
					return new CAVE_CONVERT_PLANT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000010L){
					return new CAVE_STORE_SIZEUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000010L){
					return new CAVE_STORE_SIZEUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000011L){
					return new CAVE_STORE_PET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000011L){
					return new CAVE_STORE_PET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000012L){
					return new CAVE_TAKEOUT_PET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000012L){
					return new CAVE_TAKEOUT_PET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000013L){
					return new CAVE_CHECK_PLANT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000013L){
					return new CAVE_CHECK_PLANT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000014L){
					return new CAVE_CANCEL_SCHEDULE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000014L){
					return new CAVE_CANCEL_SCHEDULE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000015L){
					return new CAVE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000015L){
					return new CAVE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000016L){
					return new CAVE_ELECTRIC_ATTACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000016L){
					return new CAVE_ELECTRIC_ATTACK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000017L){
					return new CAVE_LEVEL_UP_MOTICE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000017L){
					return new CAVE_LEVEL_UP_MOTICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000018L){
					return new CAVE_QUERY_SCHEDULE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000018L){
					return new CAVE_QUERY_SCHEDULE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000019L){
					return new CAVE_QUERY_PETSTORE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000019L){
					return new CAVE_QUERY_PETSTORE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000020L){
					return new CAVE_QUERY_PETHOOK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000020L){
					return new CAVE_QUERY_PETHOOK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000021L){
					return new CAVE_OPEN_COUNTYLIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000021L){
					return new CAVE_OPEN_COUNTYLIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000022L){
					return new CAVE_QUERY_SELFFAERY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000022L){
					return new CAVE_QUERY_SELFFAERY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000023L){
					return new CAVE_NOTICE_CONVERTARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000023L){
					return new CAVE_NOTICE_CONVERTARTICLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000024L){
					return new CAVE_HOOK_PET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000024L){
					return new CAVE_HOOK_PET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000025L){
					return new CAVE_FIND_SELFCAVE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000025L){
					return new CAVE_FIND_SELFCAVE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000026L){
					return new CAVE_LEAVE_CAVE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000026L){
					return new CAVE_LEAVE_CAVE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000027L){
					return new CAVE_SHOW_TOOLBAR_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000027L){
					return new CAVE_SHOW_TOOLBAR_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F00002AL){
					return new CAVE_QUERY_EXCHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F00002AL){
					return new CAVE_QUERY_EXCHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F00002BL){
					return new CAVE_ACCELERATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F00002BL){
					return new CAVE_ACCELERATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F00002CL){
					return new CAVE_SIMPLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F00002CL){
					return new CAVE_SIMPLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F00002DL){
					return new CAVE_READ_DYNAMIC_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F00002DL){
					return new CAVE_READ_DYNAMIC_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F00002EL){
					return new CAVE_DETAILED_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F00002EL){
					return new CAVE_DETAILED_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000050L){
					return new CAVE_DYNAMIC_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000050L){
					return new CAVE_DYNAMIC_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000051L){
					return new CAVE_DYNAMIC_NOTICE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000051L){
					return new CAVE_DYNAMIC_NOTICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000052L){
					return new CAVE_QUERY_PLANT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000052L){
					return new CAVE_QUERY_PLANT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000053L){
					return new CAVE_QUERY_RESOURCECOLLECTIO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000053L){
					return new CAVE_QUERY_RESOURCECOLLECTION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F100001L){
					return new BOURN_LEVELUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F100001L){
					return new BOURN_LEVELUP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F100002L){
					return new BOURN_ZEZEN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F100002L){
					return new BOURN_ZEZEN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F100003L){
					return new BOURN_REFRESH_TASK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F100003L){
					return new BOURN_REFRESH_TASK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F100004L){
					return new BOURN_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F100004L){
					return new BOURN_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F100005L){
					return new BOURN_OF_TRAINING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F100005L){
					return new BOURN_OF_TRAINING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F100006L){
					return new BOURN_OF_PURIFY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F100006L){
					return new BOURN_OF_PURIFY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F100007L){
					return new NOTICE_CLIENT_DELIVER_TASK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F100008L){
					return new NOTICE_DELIVER_BOURN_TASK_NUM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000128L){
					return new QUERY_SEAL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000128L){
					return new QUERY_SEAL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000130L){
					return new TAKE_SEAL_TASK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000129L){
					return new QUERY_FLOPNPC_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F000129L){
					return new QUERY_FLOPNPC_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000126L){
					return new LEVELUP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000028L){
					return new EQUIPMENT_SHOW_TOOLBAR_CHUJIJIANDING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000029L){
					return new EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIJIANDING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000030L){
					return new EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIMINGKE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000031L){
					return new EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIFUXING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000032L){
					return new EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIBANGDING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000033L){
					return new EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEIZHAIXING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000034L){
					return new EQUIPMENT_SHOW_TOOLBAR_ZHUANGBEISHENGJI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000035L){
					return new EQUIPMENT_SHOW_TOOLBAR_BAOSHIHECHENG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000036L){
					return new EQUIPMENT_SHOW_TOOLBAR_BAOSHIXIANGQIAN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000037L){
					return new EQUIPMENT_SHOW_TOOLBAR_BAOSHIWAQU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F000038L){
					return new FUCK_TRANSPORT_GAME(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F200001L){
					return new FIND_WAY2TASK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F200002L){
					return new ENTITY_MSG(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F200003L){
					return new SKILL_MSG(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F200005L){
					return new PROP_MSG(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F200004L){
					return new NEXT_TASK_OPEN(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300003L){
					return new SHOW_MEMBER_FORLUCK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300004L){
					return new HANDOUT_FORLUCK_TREE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F300004L){
					return new HANDOUT_FORLUCK_TREE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300006L){
					return new CHECK_STORAGE_FORLUCK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300007L){
					return new HANDOUT_FORLUCK_STORAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F300007L){
					return new HANDOUT_FORLUCK_STORAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300008L){
					return new NOTICE_CLIENT_EXCHANGE_FORLUCK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300009L){
					return new ACTIVITY_FORLUCK_FRUIT_EXCHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300010L){
					return new BOTTLE_OPEN_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300012L){
					return new BOTTLE_OPEN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300013L){
					return new BOTTLE_PICK_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300016L){
					return new CLIFFORD_START_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F300016L){
					return new CLIFFORD_START_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300017L){
					return new CLIFFORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300018L){
					return new CLIFFORD_LOOKOVER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300019L){
					return new TOURNAMENT_TIME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300020L){
					return new TOURNAMENT_START_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300021L){
					return new TOURNAMENT_END_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300025L){
					return new TOURNAMENT_QUERY_SIDE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300026L){
					return new TOURNAMENT_RANK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300027L){
					return new TOURNAMENT_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F300027L){
					return new TOURNAMENT_REWARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300128L){
					return new TOURNAMENT_REWARD_SELF_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F300128L){
					return new TOURNAMENT_REWARD_SELF_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F30002AL){
					return new TOURNAMENT_TAKE_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F30002BL){
					return new CONTINUE_KILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300022L){
					return new SEND_VILLAGE_CIRCLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300023L){
					return new SEND_VILLAGE_CIRCLE_DIS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F100025L){
					return new SEND_VILLAGE_STATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F100026L){
					return new SEND_CITYFIGHT_STATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300014L){
					return new ACTIVITY_FARMING_PLATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300015L){
					return new ACTIVITY_FARMING_PLATE_RESPONSE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0000L){
					return new ENTER_GETACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0001L){
					return new GET_FATEACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0001L){
					return new GET_FATEACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0002L){
					return new CHOOSE_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0002L){
					return new CHOOSE_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0003L){
					return new REAL_CHOOSE_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0003L){
					return new REAL_CHOOSE_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0004L){
					return new AGREE_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0005L){
					return new DISAGREE_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0006L){
					return new GIVEUP_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0006L){
					return new GIVEUP_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0007L){
					return new BEGIN_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0008L){
					return new FINISH_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700FF000L){
					return new SEEM_HINT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0105L){
					return new POP_WAIT_TIME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0100L){
					return new POP_QUIZ_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0101L){
					return new ANSWER_QUIZ_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0101L){
					return new ANSWER_QUIZ_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0104L){
					return new ANSWER_USE_PROPS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0102L){
					return new ANSWER_QUIZ_FINISH_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0103L){
					return new ANSWER_QUIZ_CANCEL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0103L){
					return new ANSWER_QUIZ_CANCEL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0200L){
					return new QUERY_SPECIAL_EQUIPMENT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0201L){
					return new QUERY_ONE_SPECIAL_EQUIPMENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0201L){
					return new QUERY_ONE_SPECIAL_EQUIPMENT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0300L){
					return new USE_EXPLORE_RESOURCEMAP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0301L){
					return new USE_EXPLOREPROPS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0302L){
					return new SPECIAL_DEAL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0303L){
					return new SPECIAL_DEAL_AGREE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0304L){
					return new SPECIAL_DEAL_ADD_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0304L){
					return new SPECIAL_DEAL_ADD_ARTICLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0305L){
					return new SPECIAL_DEAL_DEL_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0305L){
					return new SPECIAL_DEAL_DEL_ARTICLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0306L){
					return new SPECIAL_DEAL_FINISH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0306L){
					return new SPECIAL_DEAL_FINISH_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000F0307L){
					return new CLOSE_DEAL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700F0307L){
					return new CLOSE_DEAL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300028L){
					return new SEND_MAP_SOUND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300029L){
					return new NOTICE_CLIENT_NEWPLAYER_LEAD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F400001L){
					return new NOTIFY_HOTSPOT_CHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F400002L){
					return new HOTSPOT_SEE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F400002L){
					return new HOTSPOT_SEE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F400003L){
					return new HOTSPOT_OVER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F400004L){
					return new HOTSPOT_OPEN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300030L){
					return new SEQUENCE_NUM_PARAMETERS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300031L){
					return new QUERY_INVALID_TIME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F300031L){
					return new QUERY_INVALID_TIME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300033L){
					return new PLAY_SOUND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F300035L){
					return new PLAYER_PROPERTY_MAX_VALUE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F300032L){
					return new NOTICE_NPC_FLASH_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F3000A5L){
					return new PUSH_CONFIRMACTION_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F3000A6L){
					return new EXCHANGE_CONFIRMACTION_CODE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F3000A6L){
					return new EXCHANGE_CONFIRMACTION_CODE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F400030L){
					return new CREATE_FEEDBACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F400031L){
					return new QUERY_FEEDBACK_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F400031L){
					return new QUERY_FEEDBACK_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F400032L){
					return new QUERY_SPECIAL_FEEDBACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F400032L){
					return new QUERY_SPECIAL_FEEDBACK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F400033L){
					return new REPLY_SPECIAL_FEEDBACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F400034L){
					return new REPLY_SPECIAL_FEEDBACK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F400035L){
					return new JUDGE_SPECIAL_FEEDBACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F400035L){
					return new JUDGE_SPECIAL_FEEDBACK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F400135L){
					return new FEEDBACK_NOTICE_CLIENT_ALLJUDGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F400136L){
					return new FEEDBACK_NOTICE_CLIENT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F300033L){
					return new NOTICE_CLIENT_PLAY_PARTICLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00100001L){
					return new SIFANG_TEST_NPCMENU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70100002L){
					return new SIFANG_ENLIST_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00100003L){
					return new SIFANG_OVER_MSG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70100003L){
					return new SIFANG_OVER_MSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00100004L){
					return new SIFANG_CHOOSE_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70100004L){
					return new SIFANG_CHOOSE_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70100005L){
					return new SIFANG_SHOW_INFO_BUTTON_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70100006L){
					return new SIFANG_SHOW_START_TIME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F600036L){
					return new RECOMEND_COUNTRY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F400036L){
					return new RANDOM_NAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F400036L){
					return new RANDOM_NAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F600068L){
					return new EXPEND_KNAPSACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F600056L){
					return new GET_DESCRIPTION_BY_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F600056L){
					return new GET_DESCRIPTION_BY_ARTICLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F600057L){
					return new CHANGE_GAME_DISPLAYNAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F600057L){
					return new NOTICE_TEAM_CHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F700001L){
					return new QIANCENGTA_OPEN_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F700002L){
					return new QIANCENGTA_QUERY_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F700003L){
					return new QIANCENGTA_FLUSH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F700004L){
					return new QIANCENGTA_AUTO_PATA_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F700004L){
					return new QIANCENGTA_AUTO_PATA_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F700005L){
					return new QIANCENGTA_MANUAL_PATA_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F700006L){
					return new QIANCENGTA_GET_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F700007L){
					return new QIANCENGTA_REWARD_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F700007L){
					return new QIANCENGTA_REWARD_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F700010L){
					return new QIANCHENGTA_MANUAL_OVER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F700011L){
					return new QIANCHENGTA_MANUAL_MSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F700012L){
					return new QIANCHENGTA_MANUAL_START_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F700012L){
					return new QIANCHENGTA_MANUAL_START_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F700013L){
					return new QIANCHENGTA_MANUAL_FAIL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F700014L){
					return new QIANCHENGTA_MANUAL_BACKHOME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F710001L){
					return new NEWQIANCENGTA_OPEN_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F710002L){
					return new NEWQIANCENGTA_QUERY_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F710003L){
					return new NEWQIANCENGTA_FLUSH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F710004L){
					return new NEWQIANCENGTA_AUTO_PATA_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F710004L){
					return new NEWQIANCENGTA_AUTO_PATA_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F710005L){
					return new NEWQIANCENGTA_MANUAL_PATA_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F710006L){
					return new NEWQIANCENGTA_GET_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F710007L){
					return new NEWQIANCENGTA_REWARD_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F710007L){
					return new NEWQIANCENGTA_REWARD_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F710010L){
					return new NEWQIANCHENGTA_MANUAL_OVER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F710011L){
					return new NEWQIANCHENGTA_MANUAL_MSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F710013L){
					return new NEWQIANCHENGTA_MANUAL_FAIL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F710014L){
					return new NEWQIANCENGTA_GET_FIRST_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F710015L){
					return new NEWQIANCENGTA_GET_FIRST_REWARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFA001L){
					return new TRY_CLIENT_MSG_A_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFA002L){
					return new TRY_CLIENT_MSG_I_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFA005L){
					return new TRY_CLIENT_MSG_W_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFA003L){
					return new CLIENT_MSG_A_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFA003L){
					return new CLIENT_MSG_A_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFA004L){
					return new CLIENT_MSG_I_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFA004L){
					return new CLIENT_MSG_I_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFA006L){
					return new CLIENT_MSG_W_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFA006L){
					return new CLIENT_MSG_W_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F700008L){
					return new GET_EFFECT_NOTICE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F700008L){
					return new EFFECT_NOTICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F700009L){
					return new OBTAIN_NOTICE_SIVLER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8F700009L){
					return new OBTAIN_NOTICE_SIVLER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F70000AL){
					return new ACTIVITY_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0070001AL){
					return new GET_BILLBOARD_MENUS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F70001AL){
					return new GET_BILLBOARD_MENUS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0070002AL){
					return new GET_ONE_BILLBOARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F70002AL){
					return new GET_ONE_BILLBOARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0070002BL){
					return new REQUEST_ACHIEVEMENT_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F70002BL){
					return new REQUEST_ACHIEVEMENT_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0070002CL){
					return new REQUEST_ACHIEVEMENT_DONE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F70002CL){
					return new REQUEST_ACHIEVEMENT_DONE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0070002DL){
					return new REQUEST_ONE_DONE_ACHIEVEMENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0F70002DL){
					return new REQUEST_ONE_DONE_ACHIEVEMENT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0070002EL){
					return new NOTICE_CLIENT_DONE_ACHIEVEMENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00800001L){
					return new VIP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70800001L){
					return new VIP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF69L){
					return new VIP_2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF69L){
					return new VIP_2_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00800002L){
					return new GET_VIP_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00800003L){
					return new VIP_PULL_FRIEND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0001L){
					return new GUOZHAN_ORDERS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE0001L){
					return new GUOZHAN_ORDERS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0002L){
					return new GUOZHAN_ADD_ORDER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE0002L){
					return new GUOZHAN_ADD_ORDER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0003L){
					return new GUOZHAN_REMOVE_ORDER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE0003L){
					return new GUOZHAN_REMOVE_ORDER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0004L){
					return new GUOZHAN_CONTROL_PANNEL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE0004L){
					return new GUOZHAN_CONTROL_PANNEL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0005L){
					return new GUOZHAN_MAKE_ORDER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFB005L){
					return new IOS_CLIENT_MSG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0006L){
					return new GUOZHAN_CURE_BOSS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE0006L){
					return new GUOZHAN_CURE_BOSS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0007L){
					return new GUOZHAN_DEALY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE0007L){
					return new GUOZHAN_DEALY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0008L){
					return new GUOZHAN_PULL_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0009L){
					return new GUOZHAN_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0010L){
					return new GUOZHAN_HISTORY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE0010L){
					return new GUOZHAN_HISTORY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0011L){
					return new GUOZHAN_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE0011L){
					return new GUOZHAN_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0012L){
					return new GUOZHAN_STAT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE0012L){
					return new GUOZHAN_STAT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0013L){
					return new GUOZHAN_RESULT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0014L){
					return new GUOZHAN_DETAIL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE0014L){
					return new GUOZHAN_DETAIL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0015L){
					return new GUOZHAN_MAP_POINT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE0015L){
					return new GUOZHAN_MAP_POINT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0016L){
					return new GUOZHAN_MAP_ORDER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE0016L){
					return new GUOZHAN_MAP_ORDER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0017L){
					return new GUOZHAN_MAP_ORDER_MAKED_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0018L){
					return new GUOZHAN_RETURN_BACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0019L){
					return new GUOZHAN_RESULT_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE0020L){
					return new GUOZHAN_CONTROL_PANNEL_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE0020L){
					return new GUOZHAN_CONTROL_PANNEL_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEE001L){
					return new ACTIVITY_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEE001L){
					return new ACTIVITY_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEE002L){
					return new GET_PLAYERTITLES_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEE002L){
					return new GET_PLAYERTITLES_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEE003L){
					return new SETDEFAULT_PLAYERTITLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEE004L){
					return new CREATE_SPECIALEQUIPMENT_BROADCAST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE01L){
					return new QUERY_PWD_PROTECT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE01L){
					return new QUERY_PWD_PROTECT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE02L){
					return new SET_PWD_PROTECT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE02L){
					return new SET_PWD_PROTECT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE03L){
					return new QUERY_CHARGE_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE03L){
					return new QUERY_CHARGE_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE04L){
					return new QUERY_CHARGE_MONEY_CONFIGURE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE04L){
					return new QUERY_CHARGE_MONEY_CONFIGURE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE05L){
					return new CHARGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE05L){
					return new CHARGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE07L){
					return new QUERY_ORDER_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE07L){
					return new QUERY_ORDER_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE08L){
					return new AY_ARGS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE08L){
					return new A_ARGS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE09L){
					return new A_GET_ORDERID_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE09L){
					return new A_GET_ORDERID_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEEAAL){
					return new NOTICE_CLIENT_APP_CHARGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE0AL){
					return new SET_VIEW_PORT(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE0BL){
					return new SET_CLIENT_DISPLAY_FLAG(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE0CL){
					return new JIU1_GET_CHARGE_ORDER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE0CL){
					return new JIU1_GET_CHARGE_ORDER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0xA0000036L){
					return new NOTIFY_OPEN_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE0DL){
					return new YINGYONGHUI_GET_CHARGE_ORDER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE0DL){
					return new YINGYONGHUI_GET_CHARGE_ORDER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE0EL){
					return new ORDER_STATUS_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE0FL){
					return new LENOVO_GET_CHARGE_ORDER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE0FL){
					return new LENOVO_GET_CHARGE_ORDER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE1EL){
					return new UCSDK_NOTICE_CHARGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE1EL){
					return new UCSDK_NOTICE_CHARGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE9901L){
					return new APPSTORE_SAVING_VERIFY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE9901L){
					return new APPSTORE_SAVING_VERIFY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEF12L){
					return new NOTICE_OPEN_URL_ARGS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EE9902L){
					return new IAPPP_GET_CHARGE_ORDER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EE9902L){
					return new IAPPP_GET_CHARGE_ORDER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEEE0L){
					return new DEFAULT_GET_CHARGEORDER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEEE0L){
					return new DEFAULT_GET_CHARGEORDER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEEE1L){
					return new GET_CHARGE_ORDER_MULTIIO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEEE1L){
					return new GET_CHARGE_ORDER_MULTIIO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE13L){
					return new NOTICE_ACTIVITY_STATUS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE14L){
					return new QUERY_EXCHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE14L){
					return new QUERY_EXCHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE15L){
					return new EXCHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE15L){
					return new EXCHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEAB0L){
					return new DOWNCITY_PREPARE_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEAB1L){
					return new DOWNCITY_PLAYER_STATUS_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEAB2L){
					return new DOWNCITY_CREATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEABAL){
					return new QUERY_TUNSHI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEABAL){
					return new QUERY_TUNSHI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEABBL){
					return new TUNSHI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEABBL){
					return new TUNSHI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEABCL){
					return new QUERY_XILIAN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEABCL){
					return new QUERY_XILIAN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEABDL){
					return new XILIAN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEABEL){
					return new QILING_INLAY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEABFL){
					return new QILING_OUTLAY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEAABAL){
					return new QUERY_LIANQI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEAABAL){
					return new QUERY_LIANQI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEAABBL){
					return new LIANQI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEAABBL){
					return new LIANQI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEAABCL){
					return new QUERY_EQUIPMENT_QILING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEAABCL){
					return new QUERY_EQUIPMENT_QILING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEAABDL){
					return new QUERY_KNAPSACK_QILING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEAABDL){
					return new QUERY_KNAPSACK_QILING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEAABFL){
					return new OPEN_QILING_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEAABEL){
					return new KNAPSACK_QILING_MOVE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x01EE9902L){
					return new QUERY_VIP_DISPLAY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x81EE9902L){
					return new QUERY_VIP_DISPLAY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE10L){
					return new SUPER_GM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE10L){
					return new SUPER_GM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE11L){
					return new SUPER_GM_SELECT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE12L){
					return new NOTICE_OPEN_URL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE21L){
					return new FEEDBACK_HOME_PAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE21L){
					return new FEEDBACK_HOME_PAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE22L){
					return new FEEDBACK_DELETE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE22L){
					return new FEEDBACK_DELETE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE23L){
					return new FEEDBACK_SCORE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE23L){
					return new FEEDBACK_SCORE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE24L){
					return new FEEDBACK_PLAYER_TALK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE24L){
					return new FEEDBACK_PLAYER_TALK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE25L){
					return new FEEDBACK_GM_TALK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE26L){
					return new FEEDBACK_LOOK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE26L){
					return new FEEDBACK_LOOK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE27L){
					return new FEEDBACK_LOOK_SCORE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE27L){
					return new FEEDBACK_LOOK_SCORE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00EEEE28L){
					return new FEEDBACK_COMMIT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70EEEE28L){
					return new FEEDBACK_COMMIT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EFE01L){
					return new ARTICLEPROTECT_MSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EFE02L){
					return new ARTICLEPROTECT_BLOCK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EFE03L){
					return new ARTICLEPROTECT_UNBLOCK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EFE04L){
					return new ARTICLEPROTECT_OTHERMSG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EFE04L){
					return new ARTICLEPROTECT_OTHERMSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE01L){
					return new ANDROID_PROCESS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE01L){
					return new ANDROID_PROCESS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE02L){
					return new RSA_DATA_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE03L){
					return new GET_RSA_DATA_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE03L){
					return new GET_RSA_DATA_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE04L){
					return new OPEN_PHONENUM_SHOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE05L){
					return new TRY_BING_PHONENUM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE05L){
					return new TRY_BING_PHONENUM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE06L){
					return new TRY_SEND_PHONTNUM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE06L){
					return new TRY_SEND_PHONTNUM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE07L){
					return new BING_PHONENUM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE07L){
					return new BING_PHONENUM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE08L){
					return new TRY_UNBING_PHONENUM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE08L){
					return new TRY_UNBING_PHONENUM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE09L){
					return new TRY_SEND_UNBIND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE09L){
					return new TRY_SEND_UNBIND_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE10L){
					return new UNBIND_PHONENUM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE10L){
					return new UNBIND_PHONENUM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE11L){
					return new PHONENUM_ASK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE11L){
					return new PHONENUM_ASK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE12L){
					return new GET_PHONE_INFO_1_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE12L){
					return new GET_PHONE_INFO_1_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE13L){
					return new GET_PHONE_INFO_2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE13L){
					return new GET_PHONE_INFO_2_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE14L){
					return new GET_PHONE_INFO_3_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE14L){
					return new GET_PHONE_INFO_3_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE15L){
					return new GET_PHONE_INFO_4_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE15L){
					return new GET_PHONE_INFO_4_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE16L){
					return new GET_PHONE_INFO_5_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE16L){
					return new GET_PHONE_INFO_5_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE17L){
					return new GET_PHONE_INFO_6_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE17L){
					return new GET_PHONE_INFO_6_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE18L){
					return new GET_PHONE_INFO_7_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE18L){
					return new GET_PHONE_INFO_7_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE19L){
					return new GET_PHONE_INFO_8_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE19L){
					return new GET_PHONE_INFO_8_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE20L){
					return new GET_PHONE_INFO_9_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE20L){
					return new GET_PHONE_INFO_9_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE21L){
					return new GET_PHONE_INFO_10_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE21L){
					return new GET_PHONE_INFO_10_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE22L){
					return new GET_PHONE_INFO_11_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE22L){
					return new GET_PHONE_INFO_11_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE23L){
					return new GET_CLIENT_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE23L){
					return new GET_CLIENT_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE24L){
					return new GET_CLIENT_INFO_1_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE24L){
					return new GET_CLIENT_INFO_1_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE25L){
					return new GET_CLIENT_INFO_2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE25L){
					return new GET_CLIENT_INFO_2_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE26L){
					return new GET_CLIENT_INFO_3_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE26L){
					return new GET_CLIENT_INFO_3_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE27L){
					return new GET_CLIENT_INFO_4_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE27L){
					return new GET_CLIENT_INFO_4_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE28L){
					return new GET_CLIENT_INFO_5_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE28L){
					return new GET_CLIENT_INFO_5_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE29L){
					return new GET_CLIENT_INFO_6_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE29L){
					return new GET_CLIENT_INFO_6_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE30L){
					return new GET_CLIENT_INFO_7_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE30L){
					return new GET_CLIENT_INFO_7_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE31L){
					return new GET_CLIENT_INFO_8_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE31L){
					return new GET_CLIENT_INFO_8_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE32L){
					return new GET_CLIENT_INFO_9_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE32L){
					return new GET_CLIENT_INFO_9_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EAE33L){
					return new VALIDATE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EAE33L){
					return new VALIDATE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EB101L){
					return new GET_RMB_REWARDMSG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EB101L){
					return new GET_RMB_REWARDMSG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EB102L){
					return new GET_RMBREWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EB102L){
					return new GET_RMBREWARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EB103L){
					return new GET_WEEKANDMONTH_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EB103L){
					return new GET_WEEKANDMONTH_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EB104L){
					return new GET_WEEKMONTH_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EB104L){
					return new GET_WEEKMONTH_REWARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EB105L){
					return new GET_WEEKACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EB105L){
					return new GET_WEEKACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x000EB106L){
					return new GET_WEEK_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EB106L){
					return new GET_WEEK_REWARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x700EB107L){
					return new WEEKACTIVITY_STATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAE01L){
					return new CURRENT_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAE02L){
					return new SIGNUP_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAE01L){
					return new SIGNUP_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAE03L){
					return new SOUL_MESSAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAE03L){
					return new SOUL_MESSAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAE04L){
					return new SOUL_UPGRADE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAE04L){
					return new LOGIN_ACTIVITY_MESS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAE05L){
					return new LOGIN_ACTIVITY_GET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAE05L){
					return new CONTINUE_ACTIVITY_MESS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAE06L){
					return new CONTINUE_ACTIVITY_GET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA03L){
					return new NEED_CHANGE_NAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA04L){
					return new CHANGE_PLAYER_NAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA04L){
					return new CHANGE_PLAYER_NAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA05L){
					return new CALL_FRIEND_DRINKING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA05L){
					return new CALL_FRIEND_DRINKING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA06L){
					return new DRINKING_FRIEND_DO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA06L){
					return new PET_DAO_DATA_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA24L){
					return new ENTER_PET_DAO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA54L){
					return new PET_JIN_JIE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA54L){
					return new PET_JIN_JIE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA55L){
					return new PET_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA55L){
					return new PET_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA5AL){
					return new PET_FIND_CUR_INDEX_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA5AL){
					return new PET_FIND_CUR_INDEX_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA56L){
					return new PET_DETAIL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA56L){
					return new PET_DETAIL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA57L){
					return new PET_CHONG_BAI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA58L){
					return new PET_CHONG_BAI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA59L){
					return new PET_BOOK_UI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA59L){
					return new PET_BOOK_UI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA60L){
					return new PET_SKILL_TAKE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA60L){
					return new PET_SKILL_TAKE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA61L){
					return new PET_TALENT_UP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA61L){
					return new PET_TALENT_UP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA62L){
					return new PET_SKILL_UP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA62L){
					return new PET_SKILL_UP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA63L){
					return new PET2_QUERY_BY_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA7EL){
					return new PET3_QUERY_BY_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA7FL){
					return new PET2_LiZi_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA7FL){
					return new PET2_LiZi_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA64L){
					return new PET2_QUERY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA64L){
					return new PET2_QUERY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA65L){
					return new QUERY_SKILLINFO_PET2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA65L){
					return new QUERY_SKILLINFO_PET2_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA66L){
					return new PET2_LIAN_HUN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA66L){
					return new PET2_LIAN_HUN_REs(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA6BL){
					return new PET2_HunArticle_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA6BL){
					return new PET2_HunArticle_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA67L){
					return new PET2_LIAN_DAN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA67L){
					return new PET2_LIAN_DAN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA69L){
					return new PET2_UP_LV_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA69L){
					return new PET2_UP_LV_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA68L){
					return new PET2_UI_DESC_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA68L){
					return new PET2_UI_DESC_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA6AL){
					return new PET2_XueMai_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA6AL){
					return new PET2_XueMai_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA25L){
					return new ACTIVENESS_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA07L){
					return new ACTIVENESS_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA26L){
					return new LOTTERY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA08L){
					return new LOTTERY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA27L){
					return new LOTTERY_FINISH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA10L){
					return new LOTTERY_FINISH_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA28L){
					return new ACTIVENESS_DES_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA11L){
					return new ACTIVENESS_DES_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA29L){
					return new GET_PRIZE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA12L){
					return new GET_PRIZE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA30L){
					return new INITDATA_MINIGAME_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA30L){
					return new INITDATA_MINIGAME_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA31L){
					return new HANDLE_MINIGAME_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA31L){
					return new HANDLE_MINIGAME_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA32L){
					return new BUY_LIFE_MINIGAME_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA32L){
					return new BUY_LIFE_MINIGAME_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA39L){
					return new TIPS_FOR_MINIGAME_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA41L){
					return new TIPS_FOR_MINIGAME_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA36L){
					return new START_MINIGAME_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA36L){
					return new START_MINIGAME_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA33L){
					return new PET_DAO_NUM_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA34L){
					return new CAN_GET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA37L){
					return new CAN_GET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA35L){
					return new GET_SHOP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA38L){
					return new GET_SHOP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA39L){
					return new NPC_ACTION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA6FL){
					return new SkEnh_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA6FL){
					return new SkEnh_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA6EL){
					return new SkEnh_exINFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA6EL){
					return new SkEnh_exINFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA70L){
					return new SkEnh_Exp2point_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA70L){
					return new SkEnh_Exp2point_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA71L){
					return new SkEnh_Add_point_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA71L){
					return new SkEnh_Add_point_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA7AL){
					return new SkEnh_Detail_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA7AL){
					return new SkEnh_Detail_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA81L){
					return new ENTER_ROBBERT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA89L){
					return new ENTER_ROBBERT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA91L){
					return new START_ROBBERT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA95L){
					return new FEISHENG_END_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA84L){
					return new ROBBERY_TIPS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA82L){
					return new ROBBERY_TIPS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA85L){
					return new NOTIFY_RAYROBBERY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA94L){
					return new BACE_TOWN_IN_ROBBERT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA92L){
					return new NOTIFY_CLIENT_VICTORY_TIPS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA93L){
					return new NOTIFY_CLIENT_ROBBERY_CLIENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA86L){
					return new NOTIFY_ROBBERY_COUNTDOWN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA87L){
					return new NOTIFY_FEISHENG_DONGHUA_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA88L){
					return new CHANAGE_PLAYER_AVATA_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA88L){
					return new CHANAGE_PLAYER_AVATA_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA82L){
					return new SIGN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA72L){
					return new SIGN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA83L){
					return new GET_SIGNAWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA85L){
					return new QUERY_WORLD__XJ_MAP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA73L){
					return new QUERY_WORLD__XJ_MAP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA86L){
					return new IS_XJ_MAP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA74L){
					return new IS_XJ_MAP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA87L){
					return new QUERY_NEW_EQUIPMENT_STRONG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA76L){
					return new QUERY_NEW_EQUIPMENT_STRONG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EEF8L){
					return new NEW_EQUIPMENT_STRONG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EEF8L){
					return new NEW_EQUIPMENT_STRONG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF00L){
					return new FIND_WAY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF01L){
					return new LOTTERY_FINISH_NEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF01L){
					return new LOTTERY_FINISH_NEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF0FL){
					return new FUNCTION_OPEN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF0FL){
					return new FUNCTION_OPEN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF02L){
					return new SYNC_MAGICWEAPON_FOR_KNAPSACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF02L){
					return new SYNC_MAGICWEAPON_FOR_KNAPSACK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF03L){
					return new QUERY_SHENSHI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF03L){
					return new QUERY_SHENSHI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF04L){
					return new CONFIRM_SHENSHI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF04L){
					return new CONFIRM_SHENSHI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x8E0EAA99L){
					return new NOTIFY_CLIENT_SUMMON_MAGICWEAPON_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF05L){
					return new QUERY_MAGICWEAPON_STRONG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF05L){
					return new QUERY_MAGICWEAPON_STRONG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF06L){
					return new MAGICWEAPON_STRONG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF06L){
					return new MAGICWEAPON_STRONG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF07L){
					return new QUERY_MAGICWEAPON_EAT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF07L){
					return new QUERY_MAGICWEAPON_EAT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF09L){
					return new CONFIRM_MAGICWEAPON_EAT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF17L){
					return new CONFIRM_MAGICWEAPON_EAT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF18L){
					return new QUERY_MAGICWEAPON_HIDDLE_PROP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF18L){
					return new QUERY_MAGICWEAPON_HIDDLE_PROP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF19L){
					return new JIHUO_MAGICWEAPON_HIDDLE_PROP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF19L){
					return new JIHUO_MAGICWEAPON_HIDDLE_PROP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF20L){
					return new REFRESH_MAGICWEAPON_HIDDLE_PROP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF20L){
					return new REFRESH_MAGICWEAPON_HIDDLE_PROP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF21L){
					return new OPEN_MAGICWEAPON_OPTION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA99L){
					return new OPEN_MAGICWEAPON_ZHULING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF26L){
					return new QUERY_MAGICWEAPON_NAIJIU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF26L){
					return new QUERY_MAGICWEAPON_NAIJIU_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF27L){
					return new COMFIRM_MAGICWEAPON_NAIJIU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF27L){
					return new COMFIRM_MAGICWEAPON_NAIJIU_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA98L){
					return new NOTIFY_CLIENT_MAGICWEAPON_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA96L){
					return new ENTER_DEVILSQUARE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA89L){
					return new DEVILSQUARE_TIPS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA90L){
					return new DEVILSQUARE_COUNTDOWNTIME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA93L){
					return new DEVILSQUARE_COMPOSE_TIPS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF23L){
					return new ENTER_COMPOSE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF23L){
					return new ENTER_COMPOSE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF16L){
					return new SKILL_POINT_SHOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF24L){
					return new GOD_EQUIPMENT_UPGRADE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF24L){
					return new GOD_EQUIPMENT_UPGRADE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF25L){
					return new GOD_EQUIPMENT_UPGRADE_SURE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF25L){
					return new GOD_EQUIPMENT_UPGRADE_SURE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF28L){
					return new FAIRY_SHOW_ELECTORS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF28L){
					return new FAIRY_VOTE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF29L){
					return new FAIRY_VOTE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF29L){
					return new FAIRY_DECLARE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF30L){
					return new FAIRY_DECLARE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF30L){
					return new FAIRY_THANK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF31L){
					return new FAIRY_THANK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF31L){
					return new FAIRY_DOTHANK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF32L){
					return new FAIRY_DOTHANK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF33L){
					return new FAIRY_VOTERECORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF33L){
					return new FAIRY_VOTERECORD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF34L){
					return new FAIRY_AWARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF32L){
					return new FAIRY_SET_AWARDLEVEL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF35L){
					return new FAIRY_SET_AWARDLEVEL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF34L){
					return new FAIRY_REFRESH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF40L){
					return new QUERY_ALL_AIMS_CHAPTER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF40L){
					return new QUERY_ALL_AIMS_CHAPTER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF41L){
					return new QUERY_ONE_CHAPTER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF41L){
					return new QUERY_ONE_CHAPTER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF42L){
					return new QUERY_ONE_AIMS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF42L){
					return new QUERY_ONE_AIMS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF43L){
					return new RECEIVE_AIM_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF43L){
					return new RECEIVE_AIM_REWARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x0E0EAA97L){
					return new NOTICE_PARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF44L){
					return new QUERY_COMPLETE_AIM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF44L){
					return new QUERY_COMPLETE_AIM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF89L){
					return new NOTICE_PLAYER_MUBIAO_CHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF47L){
					return new QUERY_HORSE_LIST2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF47L){
					return new QUERY_HORSE_LIST2_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF48L){
					return new NOTIFY_HORSE_SELFCHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF49L){
					return new QUERY_HORSE_DATA_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF49L){
					return new QUERY_HORSE_DATA_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF50L){
					return new QUERY_SKILLS_MAP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF50L){
					return new QUERY_SKILLS_MAP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF51L){
					return new HORSE_BLOODSTAR_STRONG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF51L){
					return new HORSE_BLOODSTAR_STRONG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF55L){
					return new HORSE_RANKSTAR_STRONG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF55L){
					return new HORSE_RANKSTAR_STRONG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF56L){
					return new REFRESH_HORSE_SKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF56L){
					return new REFRESH_HORSE_SKILL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF57L){
					return new LEARN_HORSE_SKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF57L){
					return new LEARN_HORSE_SKILL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF58L){
					return new HORSE_COLOR_STRONG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF58L){
					return new HORSE_COLOR_STRONG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF59L){
					return new UPGRADE_HORSE_SKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF59L){
					return new UPGRADE_HORSE_SKILL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF60L){
					return new GET_HORSE_SKILL_INFO_SHOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF60L){
					return new GET_HORSE_SKILL_INFO_SHOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF61L){
					return new GET_HORSE_SKILL_WINDOW_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF61L){
					return new GET_HORSE_SKILL_WINDOW_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF62L){
					return new GET_HORSE_RANK_WINDOW_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF62L){
					return new GET_HORSE_RANK_WINDOW_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF63L){
					return new GET_HORSE_BLOOD_WINDOW_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF63L){
					return new GET_HORSE_BLOOD_WINDOW_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF64L){
					return new GET_HORSE_NEED_ARTICLE_WIND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF64L){
					return new GET_HORSE_NEED_ARTICLE_WIND_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF65L){
					return new GET_HORSE_SKILL_OPEN_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF65L){
					return new GET_HORSE_SKILL_OPEN_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF75L){
					return new GET_HORSE_HELP_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF75L){
					return new GET_HORSE_HELP_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF76L){
					return new GET_HORSE_COLOR_STRONG_WIND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF76L){
					return new GET_HORSE_COLOR_STRONG_WIND_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF52L){
					return new QUERY_ARTICLE_EXCHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF52L){
					return new QUERY_ARTICLE_EXCHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF53L){
					return new CONFIRM_ARTICLE_EXCHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF53L){
					return new CONFIRM_ARTICLE_EXCHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF54L){
					return new SEAL_TASK_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF54L){
					return new SEAL_TASK_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF66L){
					return new COLLECT_MATERIAL_FOR_BOSS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF66L){
					return new COLLECT_MATERIAL_FOR_BOSS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF67L){
					return new CONFIRM_COLLENT_MATERIAL_FOR_BOSS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF67L){
					return new CONFIRM_COLLENT_MATERIAL_FOR_BOSS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF68L){
					return new ACTIVITY_FIRST_PAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF68L){
					return new ACTIVITY_FIRST_PAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF99L){
					return new GET_ACTIVITY_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF99L){
					return new GET_ACTIVITY_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF70L){
					return new SEAL_TASK_STAT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF70L){
					return new SEAL_TASK_STAT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF71L){
					return new NOTIFY_SHOW_DOWNCITY_TUN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF72L){
					return new PLAY_DOWNCITY_TUN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF72L){
					return new PLAY_DOWNCITY_TUN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF73L){
					return new GET_ACTIVITY_MESS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF74L){
					return new NOTICE_ACTIVITY_STAT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF78L){
					return new OPEN_WAFEN_ACTIVITY_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF78L){
					return new OPEN_WAFEN_ACTIVITY_WINDOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF79L){
					return new GET_ONE_FENMU_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF79L){
					return new GET_ONE_FENMU_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF80L){
					return new DIG_FENMU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF80L){
					return new DIG_FENMU_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF81L){
					return new RECEIVE_FENMU_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF81L){
					return new RECEIVE_FENMU_REWARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF82L){
					return new EXCHANGE_CHANZI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF82L){
					return new EXCHANGE_CHANZI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF84L){
					return new DIG_FENMU_TEN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF84L){
					return new DIG_FENMU_TEN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF85L){
					return new BICHAN_ARTICLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF85L){
					return new BICHAN_ARTICLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF86L){
					return new PLAYER_TITLES_List_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF86L){
					return new PLAYER_TITLES_List_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF88L){
					return new QUERY_TITLE_PRODUCE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF88L){
					return new QUERY_TITLE_PRODUCE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00F0EF83L){
					return new NOTICE_ACTIVITY_BUTTON_STAT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70F0EF83L){
					return new NOTICE_ACTIVITY_BUTTON_STAT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0000L){
					return new ARTICLE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0000L){
					return new ARTICLE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0001L){
					return new GET_VIP_NOTICE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0001L){
					return new GET_VIP_NOTICE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0002L){
					return new ARTICLE_REMOVE_SUCC_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0009L){
					return new REFRESH_SHOP_GOODS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0009L){
					return new REFRESH_SHOP_GOODS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0010L){
					return new CAN_REFRESH_SHOP_NAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0010L){
					return new CAN_REFRESH_SHOP_NAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0015L){
					return new JIAZU_INFO_REQ2(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0015L){
					return new JIAZU_INFO_RES2(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0016L){
					return new JIAZU_INFO_ADD_TIME_REQ2(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0016L){
					return new JIAZU_INFO_ADD_TIME_RES2(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0017L){
					return new JIAZU_BIAOCHE_QIANGHUA_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0017L){
					return new JIAZU_BIAOCHE_QIANGHUA_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0018L){
					return new JIAZU_BIAOCHE_QIANGHUA_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0018L){
					return new JIAZU_BIAOCHE_QIANGHUA_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0034L){
					return new BRIGHTINLAY_EXCAVATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0034L){
					return new BRIGHTINLAY_EXCAVATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0035L){
					return new JIAZU_XIULIAN_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0035L){
					return new JIAZU_XIULIAN_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0036L){
					return new JIAZU_XIULIAN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0036L){
					return new JIAZU_XIULIAN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0037L){
					return new JIAZU_JINENG_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0037L){
					return new JIAZU_JINENG_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0038L){
					return new JIAZU_JINENG_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0038L){
					return new JIAZU_JINENG_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0039L){
					return new JIAZU_TASK_WINDOW_OPEN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0039L){
					return new JIAZU_TASK_WINDOW_OPEN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0040L){
					return new JIAZU_TASK_JIEQU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0040L){
					return new JIAZU_TASK_JIEQU_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0041L){
					return new JIAZU_TASK_REFRESH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0041L){
					return new JIAZU_TASK_REFRESH_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0043L){
					return new GET_JIAZU_SKILL_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0043L){
					return new GET_JIAZU_SKILL_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0046L){
					return new ACTIVITY_FIRST_PAGE2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0046L){
					return new ACTIVITY_FIRST_PAGE2_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0049L){
					return new GET_PINGTAI_PARAM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0049L){
					return new GET_PINGTAI_PARAM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0058L){
					return new HIDDEN_POPWINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0059L){
					return new NOTIFY_AUTOFEED_HORSE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0063L){
					return new JIAZU_FENGYING_STATUS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0063L){
					return new JIAZU_FENGYING_STATUS__RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0064L){
					return new COMMON_RELEVANT_DES_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0064L){
					return new COMMON_RELEVANT_DES_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0065L){
					return new GET_NEW_SHOP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0065L){
					return new GET_NEW_SHOP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0067L){
					return new ACTIVITY_EXTEND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0068L){
					return new MORE_ARGS_ORDER_STATUS_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0114L){
					return new MONTH_CARD_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0114L){
					return new MONTH_CARD_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0069L){
					return new MONTH_CARD_ACTIVITY_GET_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0069L){
					return new MONTH_CARD_ACTIVITY_GET_REWARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0070L){
					return new MONTH_CARD_ACTIVITY_BUY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0070L){
					return new MONTH_CARD_ACTIVITY_BUY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0071L){
					return new OPEN_DAILY_TURN_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0071L){
					return new OPEN_DAILY_TURN_WINDOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0072L){
					return new PLAY_DAILY_TURN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0072L){
					return new PLAY_DAILY_TURN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0073L){
					return new GET_ONE_DAILY_TURN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0073L){
					return new GET_ONE_DAILY_TURN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0075L){
					return new BUY_EXTRA_TIMES4TURN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0075L){
					return new BUY_EXTRA_TIMES4TURN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0074L){
					return new CHARGE_AGRS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0076L){
					return new OPEN_TREASUREACTIVITY_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0076L){
					return new OPEN_TREASUREACTIVITY_WINDOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0077L){
					return new GET_ONE_TREASUREACTIVITY_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0077L){
					return new GET_ONE_TREASUREACTIVITY_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0078L){
					return new PLAY_TREASUREACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0078L){
					return new PLAY_TREASUREACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0079L){
					return new OPEN_LEVELUPREWARD_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0079L){
					return new OPEN_LEVELUPREWARD_WINDOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0080L){
					return new BUY_LEVELUPREWARD_GOOD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0081L){
					return new CHARGE_GET_PACKAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0082L){
					return new CHARGE_GET_PACKAGE_SURE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0083L){
					return new GET_NEWPLAYERCAREERS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0083L){
					return new GET_NEWPLAYERCAREERS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0088L){
					return new NOTICE_CLIENT_BIANSHEN_CD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0087L){
					return new NOTICE_CLIENT_PLAYE_CARTOON_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0089L){
					return new GET_SHOUKUI_COMMENSKILLS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0089L){
					return new GET_SHOUKUI_COMMENSKILLS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0084L){
					return new SWITCH_BIANSHENJI_SKILLS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0084L){
					return new SWITCH_BIANSHENJI_SKILLS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0085L){
					return new QUERY_CAREER_BIANSHEN_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0086L){
					return new QUERY_NEW_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0086L){
					return new QUERY_NEW_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0087L){
					return new RESET_BIANSHEN_SKILLS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0088L){
					return new TOURNAMENT_QUERY_PLAYER_ICON_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0091L){
					return new GET_ENCHANT_DESCRIPTION_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0091L){
					return new SGET_ENCHANT_DESCRIPTION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0092L){
					return new GET_ARTICLE_DES_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0092L){
					return new GET_ARTICLE_DES_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0093L){
					return new ENCHANT_EQUIPMENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0093L){
					return new ENCHANT_EQUIPMENT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0094L){
					return new OPEN_ENCHANT_LOCK_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0094L){
					return new OPEN_ENCHANT_LOCK_WINDOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0095L){
					return new ENCHANT_LOCK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0095L){
					return new ENCHANT_LOCK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0096L){
					return new ENCHANT_UNLOCK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0096L){
					return new ENCHANT_UNLOCK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0100L){
					return new ENCHANT_ALL_EQUIPT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0100L){
					return new ENCHANT_ALL_EQUIPT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0097L){
					return new JUBAO_PLAYER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0097L){
					return new JUBAO_PLAYER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0098L){
					return new XILIAN_PAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0098L){
					return new XILIAN_PAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0099L){
					return new XILIAN_CONFIRM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0099L){
					return new XILIAN_CONFIRM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0101L){
					return new OPEN_SHOUHUN_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0101L){
					return new OPEN_SHOUHUN_WINDOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0102L){
					return new REPLACE_ALL_SHOUHUN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0102L){
					return new REPLACE_ALL_SHOUHUN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0103L){
					return new REPLACE_ONE_SHOUHUN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0103L){
					return new REPLACE_ONE_SHOUHUN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0104L){
					return new SHOUHUN_TUNSHI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF014L){
					return new SHOUHUN_TUNSHI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0107L){
					return new OPEN_SHOUHUN_LUCK_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0107L){
					return new OPEN_SHOUHUN_LUCK_WINDOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0108L){
					return new TAKE_SHOUHUN_LUCK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0108L){
					return new TAKE_SHOUHUN_LUCK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0105L){
					return new XILIAN_PUT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF015L){
					return new XILIAN_PUT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0106L){
					return new XILIAN_REMOVE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF016L){
					return new XILIAN_REMOVE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0109L){
					return new TAKEOUT_SHOUHUN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0109L){
					return new TAKEOUT_SHOUHUN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0110L){
					return new GET_SHOUHUN_ADDEXPS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0110L){
					return new GET_SHOUHUN_ADDEXPS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0111L){
					return new GET_SHOUHUN_KNAP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0111L){
					return new GET_SHOUHUN_KNAP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0112L){
					return new ZHENLI_SHOUHUN_KNAP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0112L){
					return new ZHENLI_SHOUHUN_KNAP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0113L){
					return new NOTIFY_SHOUHUN_KNAP_CHANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0113L){
					return new NOTIFY_SHOUHUN_KNAP_CHANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0115L){
					return new PET_FLY_STATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0115L){
					return new PET_FLY_STATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0116L){
					return new PET_FLY_BUTTON_ONCLICK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0116L){
					return new PET_FLY_BUTTON_ONCLICK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0119L){
					return new PET_EAT_FLY_PROP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0119L){
					return new PET_EAT_FLY_PROP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0117L){
					return new NOTICE_CLIENT_ALCHEMY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0118L){
					return new ALCHEMY_END_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0118L){
					return new ALCHEMY_END_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0120L){
					return new PET_CLEAR_PROP_CD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0120L){
					return new PET_CLEAR_PROP_CD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0121L){
					return new START_DRAW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0121L){
					return new START_DRAW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0122L){
					return new END_DRAW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0122L){
					return new END_DRAW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0123L){
					return new QUERY_PET_FLY_SKILLS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0123L){
					return new QUERY_PET_FLY_SKILLS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0124L){
					return new NEW_PET_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0124L){
					return new NEW_PET_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0125L){
					return new NEW_PET_DETAIL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0125L){
					return new NEW_PET_DETAIL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0126L){
					return new NOTICE_PLAYER_OUT_OF_MAP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0126L){
					return new NOTICE_PLAYER_OUT_OF_MAP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0127L){
					return new TALENT_FIRST_PAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0127L){
					return new TALENT_FIRST_PAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0128L){
					return new QUERY_TALENT_EXP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0128L){
					return new QUERY_TALENT_EXP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0129L){
					return new CONFIRM_TALENT_EXP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0129L){
					return new CONFIRM_TALENT_EXP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0130L){
					return new XIULIAN_TALENT_EXP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0130L){
					return new XIULIAN_TALENT_EXP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0131L){
					return new PET_FLY_ANIMATION_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0132L){
					return new QUERY_TALENT_SKILLS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0132L){
					return new QUERY_TALENT_SKILLS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0133L){
					return new GET_FAIRY_ROBBERY_STATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0133L){
					return new GET_FAIRY_ROBBERY_STATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0134L){
					return new FLY_TALENT_ADD_POINTS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0134L){
					return new FLY_TALENT_ADD_POINTS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0135L){
					return new CONFIRM_FLY_TALENT_ADD_POINTS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0135L){
					return new CONFIRM_FLY_TALENT_ADD_POINTS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0136L){
					return new NOTICE_TALENT_BUTTON_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0136L){
					return new ONCLICK_TALENT_BUTTON_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0137L){
					return new WILL_CHANGE_NAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0137L){
					return new WILL_CHANGE_NAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0138L){
					return new CHANGE_NAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0138L){
					return new CHANGE_NAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0139L){
					return new QUERY_USED_NAME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0139L){
					return new QUERY_USED_NAME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0140L){
					return new QUERY_CHEST_TYPE_TIME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0140L){
					return new QUERY_CHEST_TYPE_TIME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0141L){
					return new NOTICE_CLIENT_PLAYE_CARTOON2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0142L){
					return new QUERY_NEW_EQUIPMENT_STRONG2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0142L){
					return new QUERY_NEW_EQUIPMENT_STRONG2_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0143L){
					return new XILIAN_EQUIPMENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0143L){
					return new XILIAN_EQUIPMENT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0144L){
					return new XILIAN_PUTEQUIPMENT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0144L){
					return new XILIAN_PUTEQUIPMENT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0145L){
					return new XILIAN_EQUIPMENT_SURE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0145L){
					return new XILIAN_EQUIPMENT_SURE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0146L){
					return new OPEN_MAGICWEAPON_BREAK_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0146L){
					return new OPEN_MAGICWEAPON_BREAK_WINDOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0147L){
					return new QUERY_MAGICWEAPON_BREAK_NEEDPROP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0147L){
					return new QUERY_MAGICWEAPON_BREAK_NEEDPROP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0148L){
					return new MAGICWEAPON_BREAK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0148L){
					return new MAGICWEAPON_BREAK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0149L){
					return new DISASTER_RANK_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0149L){
					return new DISASTER_RANK_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0151L){
					return new DISASTER_END_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0152L){
					return new DISASTER_START_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0153L){
					return new DISASTER_MATCH_SUCCESS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0154L){
					return new DISASTER_SIGN_SUCCESS_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0155L){
					return new DISASTER_OPT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0155L){
					return new DISASTER_OPT_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0156L){
					return new PLAYER_IN_SPESCENE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0156L){
					return new PLAYER_IN_SPESCENE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0157L){
					return new SHOOK_DICE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0157L){
					return new SHOOK_DICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0158L){
					return new SHOOK_DICE_RESULT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0159L){
					return new SHOOK_DICE2_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0160L){
					return new DICE_BILLBOARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0161L){
					return new DICE_END_NOTICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0162L){
					return new QUERY_COUNTRY_OFFICER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0162L){
					return new QUERY_COUNTRY_OFFICER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0163L){
					return new DISASTER_SKILL_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0163L){
					return new DISASTER_SKILL_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0164L){
					return new DICE_TRANSFER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0165L){
					return new SOULPITH_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0165L){
					return new SOULPITH_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0166L){
					return new INLAY_SOULPITH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0166L){
					return new INLAY_SOULPITH_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0167L){
					return new TAKEOUT_SOULPITH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0167L){
					return new TAKEOUT_SOULPITH_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0168L){
					return new POURIN_SOULPITH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0168L){
					return new POURIN_SOULPITH_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0169L){
					return new DEVOUR_SOULPITH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0169L){
					return new DEVOUR_SOULPITH_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0170L){
					return new ARTIFICE_SOULPITH_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0170L){
					return new ARTIFICE_SOULPITH_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0171L){
					return new SOULPITH_EXTRA_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0171L){
					return new SOULPITH_EXTRA_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FF0172L){
					return new GOURD_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FF0172L){
					return new GOURD_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF001L){
					return new VIP_LOTTERY_OPEN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF002L){
					return new VIP_LOTTERY_CLICK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF002L){
					return new VIP_LOTTERY_CLICK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF003L){
					return new HUNSHI_JIANDING_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF003L){
					return new HUNSHI_JIANDING_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF004L){
					return new HUNSHI_JIANDING_SURE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF004L){
					return new HUNSHI_JIANDING_SURE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF005L){
					return new WOLF_SIGN_UP_QUERY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF005L){
					return new WOLF_SIGN_UP_QUERY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF006L){
					return new WOLF_SIGN_UP_SURE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF006L){
					return new WOLF_SIGN_UP_SURE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF007L){
					return new WOLF_FIGHT_QUERY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF007L){
					return new WOLF_FIGHT_QUERY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF008L){
					return new WOLF_STATE_NOTICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF009L){
					return new WOLF_NOTICE_ENTER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF009L){
					return new WOLF_NOTICE_ENTER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF010L){
					return new WOLF_ENTER_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF010L){
					return new WOLF_ENTER_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF011L){
					return new WOLF_UPDATE_FIGHT_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF012L){
					return new WOLF_START_FIGHT_NOTICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF015L){
					return new WOLF_END_NOTICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF016L){
					return new HUNSHI_OPEN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF017L){
					return new HUNSHI_MERGE_AIM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF017L){
					return new HUNSHI_MERGE_AIM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF018L){
					return new HUNSHI_SPECIAL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF018L){
					return new HUNSHI_SPECIAL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF019L){
					return new HUNSHI_MERGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF019L){
					return new HUNSHI_MERGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF020L){
					return new HUNSHI2_OPEN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF020L){
					return new HUNSHI2_OPEN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF021L){
					return new HUNSHI2_KIND_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF021L){
					return new HUNSHI2_KIND_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF022L){
					return new HUNSHI_PUTON_OPEN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF022L){
					return new HUNSHI_PUTON_OPEN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF023L){
					return new SOUL_ARTICLE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF023L){
					return new SOUL_ARTICLE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF024L){
					return new SOULPITH_LEVEL_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF024L){
					return new SOULPITH_LEVEL_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF025L){
					return new SOUL_LEVELUP_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF025L){
					return new SOUL_LEVELUP_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF026L){
					return new HUNSHI_PUTON_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF026L){
					return new HUNSHI_PUTON_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF027L){
					return new HUNSHI_PROP_VIEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF027L){
					return new HUNSHI_PROP_VIEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF028L){
					return new HUNSHI2_PUTON_OPEN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF028L){
					return new HUNSHI2_PUTON_OPEN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF029L){
					return new HUNSHI2_PROP_VIEW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF029L){
					return new HUNSHI2_PROP_VIEW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF030L){
					return new HUNSHI2_CELL_BUY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF030L){
					return new HUNSHI2_CELL_BUY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF031L){
					return new WOLF_SIGNUP_NUM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF031L){
					return new WOLF_SIGNUP_NUM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF032L){
					return new WOLF_SKILL_ID_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF033L){
					return new WOLF_USE_SKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF033L){
					return new WOLF_USE_SKILL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF034L){
					return new DICE_SIGN_UP_QUERY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF034L){
					return new DICE_SIGN_UP_QUERY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF035L){
					return new DICE_SIGN_UP_SURE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF035L){
					return new DICE_SIGN_UP_SURE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF036L){
					return new NOTICE_DISASTER_COUNTDOWN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF037L){
					return new HUNSHI_JIANDING_QINGQIU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF037L){
					return new HUNSHI_JIANDING_QINGQIU_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF038L){
					return new NEW_WAREHOUSE_GET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF038L){
					return new NEW_WAREHOUSE_GET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF039L){
					return new NEW_WAREHOUSE_ARRANGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF039L){
					return new NEW_WAREHOUSE_ARRANGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF040L){
					return new NEW_ACVITITY_SHOW_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF040L){
					return new NEW_ACVITITY_SHOW_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF041L){
					return new NEW_WAREHOUSE_INPUT_PASSWORD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF041L){
					return new NEW_WAREHOUSE_INPUT_PASSWORD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF042L){
					return new HUNSHI_JIANDING2_QINGQIU_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF042L){
					return new HUNSHI_JIANDING2_QINGQIU_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF043L){
					return new HUNSHI_JIANDING2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF043L){
					return new HUNSHI_JIANDING2_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF044L){
					return new HUNSHI_JIANDING2_SURE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF044L){
					return new HUNSHI_JIANDING2_SURE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF045L){
					return new NEW_QUERY_ARTICLE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF045L){
					return new NEW_QUERY_ARTICLE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF046L){
					return new HUNSHI2_INFOSHOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF046L){
					return new HUNSHI2_INFOSHOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF059L){
					return new OPEN_MAGICWEAPON_EXTRAATTR_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF059L){
					return new OPEN_MAGICWEAPON_EXTRAATTR_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF060L){
					return new MAGICWEAPON_EXTRAATTR_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF060L){
					return new MAGICWEAPON_EXTRAATTR_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF061L){
					return new RESET_MAGICWEAPON_EXTRAATTR_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF061L){
					return new RESET_MAGICWEAPON_EXTRAATTR_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF047L){
					return new XIANLING_OPEN_MAIN_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF067L){
					return new XIANLING_OPEN_MAIN_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF069L){
					return new HELP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF069L){
					return new HELP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF070L){
					return new XL_CHALLENGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF070L){
					return new XL_CHALLENGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF071L){
					return new XL_CHALLENGE_SURE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF072L){
					return new XL_OPEN_MEETMONSTER_BUFF_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF072L){
					return new XL_OPEN_MEETMONSTER_BUFF_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF073L){
					return new XL_ACT_MEETMONSTER_BUFF_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF073L){
					return new XL_ACT_MEETMONSTER_BUFF_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF074L){
					return new XL_OPEN_SCORE_BUFF_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF074L){
					return new XL_OPEN_SCORE_BUFF_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF075L){
					return new XL_USE_SCORE_CARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF075L){
					return new XL_USE_SCORE_CARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF076L){
					return new XL_TIMEDTASK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF076L){
					return new XL_TIMEDTASK_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF077L){
					return new XL_TIMEDTASK_PRIZE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF077L){
					return new XL_TIMEDTASK_PRIZE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF079L){
					return new XL_GET_SCORE_PRIZE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF079L){
					return new XL_GET_SCORE_PRIZE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF0782L){
					return new REFRESH_CROSS_SHOP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF083L){
					return new XL_BUTENERGY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF083L){
					return new XL_BUTENERGY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF0784L){
					return new XL_BUTENERGY_SURE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF084L){
					return new XL_BUTENERGY_SURE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF084L){
					return new QUERY_NEW_EQUIPMENT_STRONG3_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF087L){
					return new QUERY_NEW_EQUIPMENT_STRONG3_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF085L){
					return new XL_USE_SKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF085L){
					return new XL_USE_SKILL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF086L){
					return new XL_QUERY_SKILL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF086L){
					return new XL_QUERY_SKILL_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF088L){
					return new QUERY_PET_ORNAID_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF088L){
					return new QUERY_PET_ORNAID_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF089L){
					return new PUTON_PET_ORNA_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF089L){
					return new PUTON_PET_ORNA_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF090L){
					return new XL_EXIT_CHALLENGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF091L){
					return new XL_SHOW_SKILL_HELP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF092L){
					return new XL_CATCH_SUCC_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF093L){
					return new XL_REFRESH_ENERGY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF093L){
					return new XL_REFRESH_ENERGY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF094L){
					return new XL_NOTICE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF094L){
					return new XL_NOTICE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF095L){
					return new PET3_QUERY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF095L){
					return new PET3_QUERY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF096L){
					return new QUERY_PLAYER_CREATE_TIME_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF096L){
					return new QUERY_PLAYER_CREATE_TIME_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF099L){
					return new QUERY_PALYER_PROP_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF099L){
					return new QUERY_PALYER_PROP_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF100L){
					return new QUERY_HORSE_EQUIPMENT_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF100L){
					return new QUERY_HORSE_EQUIPMENT_INFO__RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF101L){
					return new ACTIVITY_HORSEEQU_INLAY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF101L){
					return new ACTIVITY_HORSEEQU_INLAY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF102L){
					return new QUERY_INLAY_WINDOW_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF102L){
					return new QUERY_INLAY_WINDOW_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF103L){
					return new INLAYTAKE_HORSEBAOSHI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF103L){
					return new INLAYTAKE_HORSEBAOSHI_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF104L){
					return new INLAYTAKE_WINDOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF104L){
					return new INLAYTAKE_WINDOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF105L){
					return new NEW_GET_ONE_TREASUREACTIVITY_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF105L){
					return new NEW_GET_ONE_TREASUREACTIVITY_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF111L){
					return new BOOTH_OFFLINE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF111L){
					return new BOOTH_OFFLINE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF112L){
					return new BOOTH_OFFLINE_SURE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF112L){
					return new BOOTH_OFFLINE_SURE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF113L){
					return new BOOTH_FIRST_PAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF113L){
					return new BOOTH_FIRST_PAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF114L){
					return new BOOTH_HELP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF114L){
					return new BOOTH_HELP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF115L){
					return new BOOTH_START_FIGHT_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF116L){
					return new BOOTH_BATTLE_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF116L){
					return new BOOTH_BATTLE_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF117L){
					return new BOOTH_GULI_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF118L){
					return new PET_CELL_HELP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF118L){
					return new PET_CELL_HELP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF119L){
					return new PET_CELL_UPGRADE_QUERY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF119L){
					return new PET_CELL_UPGRADE_QUERY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF120L){
					return new PET_CELL_UPGRADE_CONFIRM_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF120L){
					return new PET_CELL_UPGRADE_CONFIRM_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF121L){
					return new PET_CELL_FIRST_PAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF121L){
					return new QUERY_PET_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF122L){
					return new MONTH_CARD_FIRST_PAGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF122L){
					return new MONTH_CARD_FIRST_PAGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF123L){
					return new MONTH_CARD_BUY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF124L){
					return new LOGIN_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF124L){
					return new LOGIN_REWARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF125L){
					return new GET_LOGIN_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF127L){
					return new FIRST_CHARGE_STATE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF127L){
					return new FIRST_CHARGE_STATE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF126L){
					return new FIRST_CHARGE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF126L){
					return new FIRST_CHARGE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF128L){
					return new PET_JOIN_CELL_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF129L){
					return new TOTLE_ONLINE_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF129L){
					return new TOTLE_ONLINE_REWARD_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF130L){
					return new GET_TOTLE_ONLINE_REWARD_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF131L){
					return new PLAYER_PET_HOUSE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF131L){
					return new PLAYER_PET_HOUSE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF132L){
					return new JIAZU_PET_HOUSE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF132L){
					return new JIAZU_PET_HOUSE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF133L){
					return new PET_STORE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF134L){
					return new PET_CALL_BACK_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF135L){
					return new BLESS_PET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF135L){
					return new BLESS_PET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF136L){
					return new CITY_SINGLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF136L){
					return new CITY_SINGLE_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF137L){
					return new ENTER_CITY_SINGLE_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF138L){
					return new ACTIVITY_IS_SHOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF138L){
					return new ACTIVITY_IS_SHOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF139L){
					return new ACTIVITY_QUERY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF139L){
					return new ACTIVITY_QUERY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF140L){
					return new PLAYER_HOOK_PET_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF140L){
					return new PLAYER_HOOK_PET_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF141L){
					return new ACTIVITY_DO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF142L){
					return new DAY_PACKAGE_OF_RMB_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF142L){
					return new DAY_PACKAGE_OF_RMB_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF143L){
					return new NOTICE_BUY_SUCCESS_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF144L){
					return new CHARGE_LIST_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF144L){
					return new CHARGE_LIST_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF145L){
					return new STAR_HELP_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF145L){
					return new STAR_HELP_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF146L){
					return new STAR_MONEY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF146L){
					return new STAR_MONEY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF147L){
					return new STAR_GO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF147L){
					return new STAR_GO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF148L){
					return new PLAYER_IN_SPESCENE2_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF148L){
					return new PLAYER_IN_SPESCENE2_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF149L){
					return new BATTLE_GUWU_INFO_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF149L){
					return new BATTLE_GUWU_INFO_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF150L){
					return new FUNCTION_SHOW_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF150L){
					return new FUNCTION_SHOW_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF151L){
					return new TOTLE_COST_ACTIVITY_REQ(sn,messageContent,offset,end - offset);
			}else if(type == 0x70FFF151L){
					return new TOTLE_COST_ACTIVITY_RES(sn,messageContent,offset,end - offset);
			}else if(type == 0x00FFF152L){
					return new END_DEAD_GAME_REQ(sn,messageContent,offset,end - offset);
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
