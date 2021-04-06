package com.fy.boss.finance.service.platform;

public class NonBankcardPaymentResult {
	private String r0_Cmd;			// 业务类型
	private String r1_Code;			// 消费请求结果(该结果代表请求是否成功，不代表实际扣款结果)
	private String r6_Order;	//得到商户订单号
	private String rq_ReturnMsg;	// 错误信息
	private String hmac;			// 签名数据
	
	/**
	 * 获取业务类型
	 * @return
	 */
	public String getR0_Cmd() {
		return r0_Cmd;
	}
	
	/**
	 * 设置业务类型
	 * @param cmd
	 */
	public void setR0_Cmd(String cmd) {
		r0_Cmd = cmd;
	}
	
	/**
	 * 获得消费请求结果
	 * @return
	 */
	public String getR1_Code() {
		return r1_Code;
	}
	
	/**
	 * 设置消费请求结果
	 * @param code
	 */
	public void setR1_Code(String code) {
		r1_Code = code;
	}
	
	/**
	 * 得到商户订单号
	 * @return
	 */
	public String getR6_Order() {
		return r6_Order;
	}
	
	/**
	 * 设置商户订单号
	 * @param order
	 */
	public void setR6_Order(String order) {
		r6_Order = order;
	}
	
	/**
	 * 获取错误信息
	 * @return
	 */
	public String getRq_ReturnMsg() {
		return rq_ReturnMsg;
	}
	
	/**
	 * 设置错误信息
	 * @param rq_ReturnMsg
	 */
	public void setRq_ReturnMsg(String rq_ReturnMsg) {
		this.rq_ReturnMsg = rq_ReturnMsg;
	}
	
	/**
	 * 获取错误信息
	 * @return
	 */
	public String getHmac() {
		return hmac;
	}
	
	/**
	 * 设置错误信息
	 * @param hmac
	 */
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
}
