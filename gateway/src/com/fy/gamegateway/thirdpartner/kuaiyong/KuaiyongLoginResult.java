package com.fy.gamegateway.thirdpartner.kuaiyong;

public class KuaiyongLoginResult {
	public String code;
	public String message;
	public String uid;
	public String username;
	public String passwd = "";
	public String accessToken = "";
	public String clientSecret = "";
	public int refer_type;
	public String refer_name;
	@Override
	public String toString() {
		return "[accessToken=" + accessToken
				+ ", clientSecret=" + clientSecret + ", code=" + code
				+ ", message=" + message + ", passwd=" + passwd
				+ ", refer_name=" + refer_name + ", refer_type=" + refer_type
				+ ", uid=" + uid + ", username=" + username + "]";
	}
	
}
