package com.fy.boss.platform.baoruan;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.fy.boss.finance.model.OrderForm;
import com.fy.boss.finance.service.OrderFormManager;
import com.fy.boss.finance.service.PlatformSavingCenter;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.text.ParamUtils;
import com.xuanzhi.tools.text.StringUtil;

public class BaoRuanCallBackServlet extends HttpServlet {
	
	private static final String PUBKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDAWcja+1vTuFg/PqVzbRoZeHps9RW+mp4fexjYGQ85MVxWJi5SXYpArzKZILaa6dXuQLCN8JgS877eShwJAi14+vWBLr70EKC96zTf3n97u3dAr17vuo2EKleJskXJTiMp2FFWQtMiVUDoGk4CdZef+gzEnKQXnRmX6gUaCi9ZRQIDAQAB";
	private String appid = "135833496863804037";
	//private String cid = "135830493976591001";
	private String cid = "801";
//	private String uniquekey = "D9iB5BsD8KuYbtuxornbQQuDzkRQDgnh";
	//private String financekey = "hXyRXqLR2IoRFtBYFpq4wnQVuFTs6U8m";
	private String financekey = "839433b48f56cc5441f60cf46f0c7d07";
	
	private String callBackUrl = "http://112.25.14.13:9110/mieshi_game_boss/baoruanResult?my_order=";
	

	//更新相关的订单状态
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		try
		{
			//只有成功才会回调
			long startTime = System.currentTimeMillis(); 
			req.setCharacterEncoding("utf-8");
			
			//接参数
			String cidParam = ParamUtils.getParameter(req,"cid","");
			String appidParam = ParamUtils.getParameter(req,"appid","");
			String liushuihaoParam = ParamUtils.getParameter(req,"order_id","");
			String zhanghaoParam =  ParamUtils.getParameter(req,"uid","");
			String moneyParam =  ParamUtils.getParameter(req,"amount","");
			String myOrderId = ParamUtils.getParameter(req,"my_order","");
			String callBackUrlParam =  callBackUrl + myOrderId;
			String verifystringParam =  ParamUtils.getParameter(req,"verifystring","");
			
			
			String my_sign = sign(zhanghaoParam, liushuihaoParam,moneyParam);
			//验证签名
			boolean isSign = false;
			
			isSign = my_sign.equalsIgnoreCase(verifystringParam);
			
			
			if(isSign)
			{
				OrderFormManager orderFormManager = OrderFormManager.getInstance();
				OrderForm orderForm = orderFormManager.getOrderForm(myOrderId);
				if(orderForm!= null)
				{
					synchronized(orderForm) {
						if(orderForm.getResponseResult() != OrderForm.RESPONSE_NOBACK) {
							if((!StringUtils.isEmpty(orderForm.getChannelOrderId())) &&(myOrderId.equals(orderForm.getOrderId()))&& (!orderForm.getChannelOrderId().equals(liushuihaoParam+"")) )
							{
								//生成订单
								OrderForm order = new OrderForm();
								order.setCreateTime(new Date().getTime());
								//设置充值平台
								order.setSavingPlatform(orderForm.getSavingPlatform());
								//设置充值介质
								order.setSavingMedium(orderForm.getSavingMedium());
								//设置充值人
								order.setPassportId(orderForm.getPassportId());
								//设置商品数量
								order.setGoodsCount(0);
								//设置消费金额
								order.setPayMoney((long)(Double.valueOf(moneyParam)*100));
								order.setServerName(orderForm.getServerName());
								order.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
								order.setHandleResultDesp("新生成订单");
								//设置订单responseResult
								order.setResponseResult(OrderForm.RESPONSE_SUCC);
								order.setResponseTime(System.currentTimeMillis());
								//设置是否通知游戏服状态 设为false
								order.setNotified(false);
								//设置通知游戏服是否成功 设为false
								order.setNotifySucc(false);
								order.setUserChannel(orderForm.getUserChannel());
								orderForm.setChannelOrderId(liushuihaoParam);
								//先存入到数据库中
								order = orderFormManager.createOrderForm(order);
								order.setOrderId("br-"+DateUtil.formatDate(new Date(), "yyyyMMdd") + "-"+ order.getId());
								try {
									orderFormManager.update(order);
								} catch (Exception e) {
									PlatformSavingCenter.logger.error("[充值回调] [充值平台：宝软（乐玩）] [失败:拷贝订单时 更新订单出现异常] [宝软（乐玩） orderId:"+liushuihaoParam+"] [my order id:"+orderForm.getId()+"] [my orderid:"+order.getOrderId()+"] [充值类型:"+order.getSavingMedium()+"] [账号:"+zhanghaoParam+"] [充值金额:"+moneyParam+"] [cid:"+cid+"] [cidParam:"+cidParam+"] [appId:"+appid+"] [appidParam:"+appidParam+"] [money:"+moneyParam+"] [myOrderid:"+myOrderId+"] [callBackUrlParam:"+callBackUrlParam+"] [订单中充值金额:"+order.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
								}
								if(PlatformSavingCenter.logger.isInfoEnabled())
									PlatformSavingCenter.logger.info("[充值回调] [充值平台：宝软（乐玩）] [充值成功:此为拷贝一份订单 因为客户进行了连续充值] [宝软（乐玩） orderId:"+liushuihaoParam+"] [my order id:"+order.getId()+"] [my orderid:"+order.getOrderId()+"] [充值类型:"+order.getSavingMedium()+"] [账号:"+zhanghaoParam+"] [订单中充值金额:"+order.getPayMoney()+"] [cid:"+cid+"] [cidParam:"+cidParam+"] [appId:"+appid+"] [appidParam:"+appidParam+"] [money:"+moneyParam+"] [myOrderid:"+myOrderId+"] [callBackUrlParam:"+callBackUrlParam+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
								 res.getWriter().write("1");
								 return;
							}
							else
							{
								PlatformSavingCenter.logger.error("[充值回调] [充值平台：宝软（乐玩）] [失败:此订单已经回调过了] [宝软（乐玩） orderId:"+liushuihaoParam+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+zhanghaoParam+"] [充值金额:"+moneyParam+"] [cid:"+cid+"] [cidParam:"+cidParam+"] [appId:"+appid+"] [appidParam:"+appidParam+"] [money:"+moneyParam+"] [myOrderid:"+myOrderId+"] [callBackUrlParam:"+callBackUrlParam+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
								res.getWriter().write("1");
								return;
							}
						}
					}
							//更新订单
						try {
							orderForm.setResponseTime(System.currentTimeMillis());
							orderForm.setHandleResult(OrderForm.HANDLE_RESULT_SUCC);
							orderForm.setResponseResult(OrderForm.RESPONSE_SUCC);
							
							//以返回的充值额度为准
							//宝币确保是1宝币等于1元人民币 并保证不会出现打折的问题 如果是出现了打折问题 则需要找宝币的人
							orderForm.setPayMoney((long)(Double.valueOf(moneyParam)*100));
							orderForm.setChannelOrderId(liushuihaoParam);
							if(PlatformSavingCenter.logger.isInfoEnabled())
								PlatformSavingCenter.logger.info("[充值回调] [充值平台：宝软（乐玩）] [充值成功] [宝软（乐玩） orderId:"+liushuihaoParam+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+zhanghaoParam+"] [订单中充值金额:"+orderForm.getPayMoney()+"] [cid:"+cid+"] [cidParam:"+cidParam+"] [appId:"+appid+"] [appidParam:"+appidParam+"] [money:"+moneyParam+"] [myOrderid:"+myOrderId+"] [callBackUrlParam:"+callBackUrlParam+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
							orderFormManager.update(orderForm);
						} catch (Exception e) {
							PlatformSavingCenter.logger.error("[充值回调] [充值平台：宝软（乐玩）] [失败:更新订单信息出错] [宝软（乐玩） orderId:"+liushuihaoParam+"] [my order id:"+orderForm.getId()+"] [my orderid:"+orderForm.getOrderId()+"] [充值类型:"+orderForm.getSavingMedium()+"] [账号:"+zhanghaoParam+"] [充值金额:"+moneyParam+"] [cid:"+cid+"] [cidParam:"+cidParam+"] [appId:"+appid+"] [appidParam:"+appidParam+"] [money:"+moneyParam+"] [myOrderid:"+myOrderId+"] [callBackUrlParam:"+callBackUrlParam+"] [ [订单中充值金额:"+orderForm.getPayMoney()+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]",e);
							res.getWriter().write("2002");
							return;
						}
						
						res.getWriter().write("1");
						return;
					}
					else
					{
						PlatformSavingCenter.logger.error("[充值回调] [充值平台：宝软（乐玩）] [失败:数据库中无此订单] [宝软（乐玩） orderId:"+liushuihaoParam+"] [orderId:"+myOrderId+"] [充值金额:"+moneyParam+"] [账号:"+zhanghaoParam+"] [cid:"+cid+"] [cidParam:"+cidParam+"] [appId:"+appid+"] [appidParam:"+appidParam+"] [money:"+moneyParam+"] [myOrderid:"+myOrderId+"] [callBackUrlParam:"+callBackUrlParam+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
						res.getWriter().write("2002");
						return;
					}
			}
			else
			{
				PlatformSavingCenter.logger.error("[充值回调] [充值平台：宝软（乐玩）] [失败:签名验证失败] [宝软（乐玩） orderId:"+liushuihaoParam+"] [cid:"+cid+"] [cidParam:"+cidParam+"] [appId:"+appid+"] [appidParam:"+appidParam+"] [money:"+moneyParam+"] [myOrderid:"+myOrderId+"] [callBackUrlParam:"+callBackUrlParam+"] [orderId:"+myOrderId+"] [充值金额:"+moneyParam+"] [账号:"+zhanghaoParam+"] [待解密字符串:"+ParamUtils.getParameter(req, "verifystring", "")+"] [签名后字符串:"+my_sign+"] [costs:"+(System.currentTimeMillis()-startTime)+"ms]");
				res.getWriter().write("2002");
				return;
			}
		}
		catch(Exception e)
		{
			PlatformSavingCenter.logger.error("[充值回调] [充值平台：宝软（乐玩）] [失败:出现异常] [宝软（乐玩） orderId:--] [cid:--] [cidParam:--] [appId:--] [appidParam:--] [money:--] [myOrderid:--] [callBackUrlParam:--] [orderId:--] [充值金额:--] [账号:--]",e);
			res.getWriter().write("2002");
			return;
		}
	}
	
	private  String sign(String uid,String orderid,String amount)
	{
		String md5Str = StringUtil.hash(cid+uid+orderid+amount+financekey);
		return md5Str;
	}
	
}
