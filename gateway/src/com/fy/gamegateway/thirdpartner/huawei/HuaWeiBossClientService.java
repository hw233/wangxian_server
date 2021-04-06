package com.fy.gamegateway.thirdpartner.huawei;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fy.gamegateway.thirdpartner.huawei.sdk.GameServiceCallDemo;
import com.fy.gamegateway.thirdpartner.huawei.sdk.RequestHandler;
import com.fy.gamegateway.thirdpartner.huawei.sdk.SignHelper;



public class HuaWeiBossClientService {
	
	static Logger logger = Logger.getLogger(HuaWeiBossClientService.class);


//	最新SDK地址：http://developer.huawei.com/consumer/cn/service/hms/catalog/huaweiiap.html?page=hmssdk_huaweiiap_sdkdownload
//	开发文档：http://developer.huawei.com/consumer/cn/service/hms/catalog/huaweiiap.html?page=hmssdk_huaweiiap_devguide	
	
	
	public static final String LOGIN_RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmKLBMs2vXosqSR2rojMzioTRVt8oc1ox2uKjyZt6bHUK0u+OpantyFYwF3w1d0U3mCF6rGUnEADzXiX/2/RgLQDEXRD22er31ep3yevtL/r0qcO8GMDzy3RJexdLB6z20voNM551yhKhB18qyFesiPhcPKBQM5dnAOdZLSaLYHzQkQKANy9fYFJlLDo11I3AxefCBuoG+g7ilti5qgpbkm6rK2lLGWOeJMrF+Hu+cxd9H2y3cXWXxkwWM1OZZTgTq3Frlsv1fgkrByJotDpRe8SwkiVuRycR0AHsFfIsuZCFwZML16EGnHqm2jLJXMKIBgkZTzL8Z+201RmOheV4AQIDAQAB";
	 // 游戏服务器提供给CP签名接口URL
	public static final  String requestUri = "https://gss-cn.game.hicloud.com/gameservice/api/gbClientApi";
	public static final  String appId = "100227637";
	public static final  String cpId = "30086000000225375";
	 // CP签名私钥
//	public static final  String CP_AUTH_SIGN_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDLrG2kgZvQIFtwau1jL4fKAz8BS6OFMKdqADHhryRaYrDs26QOFD4vaYLQx8M6tdbVC25wcx3DlMhMBmpLHW2ogoIKVFyIzWSbxEui3nPtoCMqCtJkC5sVQjeMYUdL5ecRlHDQlC5FHwTLCfCKuI4ELIG3d9kbJOfnP2GLpEbHZCLlQY5RkkXXuiUHymSGDjF1TE11FWIRDdV054TP5beU9zj9roX389nxqzvoTeTGas5gRMmFdomycliVrWvXXcXJRjvOfjTb5QDflGtXnTyGZbGr8kLWzhHtKd6vShQmjnbcG60F2WYUyWQ+YfcSO+jWF/eL4VRTkyhvgd/fTJ0ZAgMBAAECggEANnSvSg03TkHggOGt/9d8/Ge6yEtjeKCGGoLb02YjF6GMgX/GUNdbuwyAY/cNPVzSCFtKl3lH63uTxDPw5o2DPMtMHpvos9EKxdUJjg3hPBcVX8/N6d7qg5gVRwzZZbMeAWbJdRMxfdypyOmq7UQkAHyv4BCEtUu8T2l0WMQ/Al9KUf7iUUMPK6NF4TeY8uQp5pot1p89pMr+QXqbbofghc1HbVGZ/MIubqhvsrouJUG1kUAA9DgcbRP0k2vbCY99D9uPGoYVfcKt62JTqMUSVRXx/XfymwmmPgjBtsViQZXjN/lSOyIimtwcW44/8HlnOVwTEqyMOF9eehpAIGEJGwKBgQD/Q55yUtt7tYigQBQRbtxyC/kOXW4TCG2lh5itthaSsfhHEwl7cjGnzsqWj/YY/0VYexnxZ3DJls5Qp564p1OWvusjKxVqregicBVD0NsN54dSnMHKgfAEsCpCFiTefcAhC2FxaKqLojYwIZQTe0lfrbNeJVVufzFdNYmn2OUH2wKBgQDMQrx9Qeu8h2sNQBNFBYPMn0GMLTHMXt2EtT6ynj/qj9NEA81la1lS8eKxf3+71Q6VjEIqbtBKhLG0F+w6kCv7URZddN37YWANVt5JD88/w0tJQ0Yt2d0mLKwMIuCwdmDSt2h7Fj3eYo1e/0L+bK7/xzJuIUtYkZlv+z8YSmkrGwKBgEXuHmAvdtWDO/S62USVXGsaAJcA3YD1VCR+60LbmX+CpgPCoVxiQlw4eKOauM7MUBEXWFob/ngbilXqNb9ch7fKgdxWlz1seyXEEu9LLle20Am/zA9QXZLLOCh2rI8PYC0tUvqgs+gg/jtdV5WmtskxauwlyivFSJ7CZf3Hm6b1AoGAUVMc0s4lOnm8IJlrUqbAc/7QTSxNUG5Qyh6TaPBiyFQdLE5LP97+wGx38m2uAK3yUY53ZYebPKAaVHbV4fc226fMPMdj/kslwAnfFL3LqTprRi8XyPed+i39DhPw27E2GY9WdPNFDL62RKVjGgNRpZq8tGZjJk9KBPDGjThLKRMCgYB5/ShnEEiTfFYYiuAl5zf5TNQArhVlMEX7bCwgs0matwn5rkZSdjQiu7loHGsSw/d0o0JBI7+Pqk23PuTmTseAwpWh2denaxCciQAvEJU8VQLhiLJ6GBPxTWyD83R4vTj5vBpiQIEeaVcTGf8C+pmnZfl0kTcKp/Wf1uu/SRSw8g==";
	public static final  String CP_AUTH_SIGN_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCDo7sfLaAE1XnHZZFoFHQQwW47CrXTCwG4SnpVRENKHZYNnR+SEoBwDAAIh5mijHMmJflxdBkfy6HJxGgaJnv+MkB1ZwgOFiHz1k9sHUSE32KvC0EMgs1osmsEfFfWyRpqZKwcTu8j5Y/iFCQp48kuKgAJUC4tPN4rhoh/wZSVrXzzuk421TRhhczmnwAi4ZWmE75qbyyfQn/q4MgSi8x/hSNkMIi4Xfb2Hmzc7UnMNNYiDaKGKBoro7YXDVHVK6I/+ToQpEhuvkQyu6WNAiJLSddrfPUOt2wIUySbKza3NG+jQgSNCWYRPnnjcRudhssp55kXOxg9NoiKs4Kw6NvzAgMBAAECggEAUhUpG+LWMc1vFv9lMLH4QW5rrpMaF1N4qhJ+v2ZGUxOpuilsQygYQEWrS6ZKDUpojrQFG1JaTDIsWuSGSTAQBswM12f8ZNQ2LZZHhdGmJPgpz55D3Go/bPY7tttV5k8VkpvdmfU6NnbxRfZrKlen3YpA5Y3CjWCz5A4KVSc+4Ckbqbxq0FPW20/TDdC+vpiudOYxSktqdD17wIdmkSqAbb3L7irxpGJUoytALuh7Pg633LzsvkgjFJsxk34dFqawivIxHsG2weeV10dRLbYSAwUE/gDB5/ZvRIZbjohV1josxqAMmopQVujB5O5ynf0nl/WyRAMXhnzgKEwa1sm38QKBgQDRLrTpJTA41yYkhqZvNvlI64L9agArmxmBECkD6Iyiso8G+qHI9+teYOAp5bwc1mKvvM0VyIqXGl0bv81YGCWGt9Ui8cacVeo4pFZrLWpQORGeQgyyDRlE/a4OoMMS94ObxAUgmQ+EYdCqunSb5y6ZIfZqVSYvv6Q9lfAyTmFyGQKBgQChGiMA+DgKj9nR7GqtYQ7GsHNPN0s/Y1gCyDrx5dgwG4RknhYjpNVxo7ijD/7W1QUjMp+hOXJttQD6TyINcRlh3a9OeWoZ2ptaTV5dkT6Aroz+rROfWOSghG+ZSiYPH1gAujDF/gsw9InjWKt2uE6Gjr8osiYTbIki4xhnfTf36wKBgQCkwjgGBp+DtZo6XMAMiu0wDSfAN9RohCJDcJoNlQXoQ+G5qoDxTKxbSsxk06klwYo7Rub1yF24hDBf4eN4BpTQFk1O18YKbvUrpezXQA8jnr+MuhNLzltf5rtIlEDx2HgVoQA027nIGE8FmhSskxs1y/VNbUHFjBlt1II67Vd9SQKBgDTe4wX6cEnnE9E1CX2FENhcKDujJerTq5qeI9onWF/RaA2tZA6mlfYJiRrasQPIPTtj7cd3lvoyIPQE7GQE7hOUMWhLqGp7t+hW49VJSX3pyxP5sSrmfYHAdO+Vyg/EdZkngcI4sWuLlYUrIHqB2iOGzW2Jqb1OLWNprCSqj6epAoGBAJh1p/I5GhOYzXltAaBl9mpydG9zSZndN4mqlKhO2LHB/f2M3cf1ef82wcexintnY5DXKDxCbFIMZA1wzS7gwzPWuuxqonUUNEt/AqOj0sc553Bdzn/WhLWp9wsdFCelO8bfYWo56WcKb7B9VcDcbGhF3G1rm02o1NX5iJOy9aj3";

	
	
	private static HuaWeiBossClientService self;
	public static HuaWeiBossClientService getInstance(){
		if(self != null){
			return self;
		}
		self = new HuaWeiBossClientService();
		return self;
	}
	
	public static void main(String[] args) {
		 // CP签名私钥        
        final String CP_AUTH_SIGN_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIvK5NdUneYrz96HmvY5waScy8GKZokkTuhy7J3ptWMH81BpIvUR0GCZ09W3hMQ1JiTL7C3dbwLmcginmsHgX946Cz5XiPQCaBXOm+uZ0BVdWG5Ut+EC0UJaA+9ngqwMoU7DVVtZRYzpN61bnsqlD7FvWUsMVIjw9kXG8/UqkM/1AgMBAAECgYBafM1ArV9APJ/jlbezJpO4MF6cOzBAGaaSRMHxPfTR07WmGUgthohx32EF+DMSpqy4KOAKmElhgTXTcFp2oPysa+WrdobHYDRfSof3ffFSS0jhLPmU3ZrQrbi+1Nx4dsm1aQQxmeYlrwHJO3N0YhqTNG40FxX4V+7R/O/D8+NHCQJBAOuZanE6FgVwr1VHljJVYrTJ0c00DUFvvG6xurhDWwaqYE28dZKWdLw3x+XOcnBAmiRalZeB1sf/AIbTJ0uFgEcCQQCX5bVETTbZCbNDzpUePQjs7Axpa/3PNtUmfOQpQlmVuEqE19KcEbC5PzvturewPz4EWGqF3Ui5V8OqNLaIEOfjAkEA6uMyfx51NAlLlQ9hDF+aoDoSRK7fhFzdFdN2IB1vFC8rAnWhFnuQeNJ4JoJ0D7wosZPqTnnxY8W1hbXhGGdKIwJAJtq/b2VA7muoy2OAiWebbN9EnYLePa8M/vdbdaJfQAkQJsL+QKXVysz+C2WDrzW+K2ZieTONcrucdWFef5ezeQJBAMC8wR+Moa3D98DtmN0BmnPDIJSpaYjisSCLkyVVU/mX9p0ExJA+5Msv4QEnW9CjoZSc2ZqGB8Pn1FDwErMkvII=";        
        // 游戏服务器提供给CP签名接口URL        
        final String requestUri = "https://gss-cn.game.hicloud.com/gameservice/api/gbClientApi";        
        // CP请求参数        

        Map<String,String> mockRequestParams = new HashMap<String,String>();        
        mockRequestParams.put("method","external.hms.gs.checkPlayerSign");        
        mockRequestParams.put("appId","300011255");        

        mockRequestParams.put("cpId","900086000000000375");        

        mockRequestParams.put("ts","1500552495471");        

        mockRequestParams.put("playerId","1646420000000006");        

        mockRequestParams.put("playerLevel","2");        
        mockRequestParams.put("playerSSign","KdbNLoC23SOMlkW2fm9q+dTgQEASqZJP1QzMkNwGCFEMnNinY1CTg3Z7hIAb2GPCgw+39vvEZ2Ruvj5Fts0xAzV4qfRXUCC6bHVE1l/aFJUnHl8JAcpY8k3ZqPqng1suwzdlZor9eyPe52uRDLvxrDTqUAFH4s9vVR8PbpAEBFGhrD/nyZ+W0ORQ16ondxZQCTnkGDt6lrp7mX4vTQYJ8C8vv1fZgIKJPR4GAL8Hu0CEkJd0NCoktzL59WNzLOTL3D0iZl4mV8UcGx3aiKN2UZiuKf1J3ePG8wf6g7nXgM8EGRfZlItdqgPRP+898m73Z2KDntW2YGPMluH60ckjMQ==");        

        GameServiceCallDemo.callGameService(requestUri,mockRequestParams,CP_AUTH_SIGN_PRIVATE_KEY);        
	}
	
	
	public boolean loginCheck(String userName,String pwd){
		long now = System.currentTimeMillis();
		try {
			
			String playerId = userName.split("HUAWEISDKUSER_")[1];
			String playerSSign = pwd.split("###@@@")[0];
			String playerLevel = pwd.split("###@@@")[1];
			String ts = pwd.split("###@@@")[2];
			
	        // CP请求参数
	        Map<String,String> mockRequestParams = new HashMap<String,String>();
	        mockRequestParams.put("method","external.hms.gs.checkPlayerSign");
	        mockRequestParams.put("appId",appId);
	        mockRequestParams.put("cpId",cpId);
	        mockRequestParams.put("ts",ts);
	        mockRequestParams.put("playerId",playerId);
	        mockRequestParams.put("playerLevel",playerLevel);
	        mockRequestParams.put("playerSSign",playerSSign);

	        //排序编码
	        String baseStr = SignHelper.format(mockRequestParams);
	        //签名
	        String cpSign = SignHelper.sign(baseStr.getBytes(Charset.forName("UTF-8")), CP_AUTH_SIGN_PRIVATE_KEY);
	        
	        mockRequestParams.put("cpSign",cpSign);

	        // 响应消息中返回参数
	        Map<String,Object> responseParamPairs = RequestHandler.doPost(requestUri,mockRequestParams);

	        if(responseParamPairs.isEmpty())
	        {
	        	logger.warn("[验证华为登录接口] [失败:None response parameter.] [userName:"+userName+"] ["+pwd+"] [cost:"+(System.currentTimeMillis() - now)+"]");
	        	return false;
	        }
	        else
	        {
	        	String code = GameServiceCallDemo.getString("rtnCode",responseParamPairs);
	            if(code != null && code.equals("0"))
	            {
	            	logger.warn("[验证华为登录接口] [成功] [userName:"+userName+"] ["+pwd+"] [cost:"+(System.currentTimeMillis() - now)+"]");
	            	return true;
	            }
	            else
	            {
	            	logger.warn("[验证华为登录接口] [失败] [userName:"+userName+"] [请求串:"+baseStr+"] [自签名:"+cpSign+"] [返回数据:"+responseParamPairs+"] [pwd:"+pwd+"] [cost:"+(System.currentTimeMillis() - now)+"]");
	            	return false;
	            }
	        }
		} catch (Exception e) {
			logger.warn("[验证华为登录接口] [异常] [userName:"+userName+"] ["+pwd+"] [cost:"+(System.currentTimeMillis() - now)+"]",e);
		}
		
		return false;
	}

	
	
}
