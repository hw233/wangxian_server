package com.fy.gamegateway.thirdpartner.qq.mime.qweibo;

public class OauthKey {
	public String customKey;		
	public String customSecrect;	
	public String tokenKey;			
	public String tokenSecrect;		
	public String verify;			
	public String callbackUrl;		
	public String termialType;		
	public String appId;			
	public String platformId;		
	public String clientIp;			
	public String format;			
	
	//������ƽ̨�������
	public String oidKey;//�û������ʶ��Ӧ�õı�������url������ϣ����ڽ���Ӧ�õ�uv��pv���������ͳ�ơ�
	public String open_id;			//�û�Ψһ��ʶ
	
	//�����ֶ���Ϊqzone����ƽ̨��Ҫ�ġ�
	public String openKey;
	public String appKey;
	public String serviceName;		//�ռ�api�������ip,�ռ�������api url�Ƿֿ���.
	
	//
	public OauthKey() {
		customKey       = null;
		customSecrect   = null;
		tokenKey 		= null;
		tokenSecrect	= null;
		verify 			= null;
		callbackUrl		= null;
		oidKey		    = null;
		open_id		    = null;	
		termialType     = null;
		appId			= null;
		platformId	    = null;
		clientIp        = null;
		openKey         = null;
		appKey          = null;
		serviceName     = null;
		format		    = "json";
	}
}
