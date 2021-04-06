package com.fy.gamegateway.thirdpartner.huawei.sdk;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.Asserts;

public class GameServiceCallDemo
{
    private static final String RETURN_CODE_SUCCEED = "0";

    /**
     *
     * @param requestUri 请求URL https://gss-cn.game.hicloud.com/GameService-HMS/api/gbClientApi
     * @param requestParams 请求参数对
     * @param cpAuthKey CP侧签名私钥
     */

    public static void callGameService(String requestUri, Map<String,String> requestParams, final String cpAuthKey)
    {
        requestParams.put("cpSign",SignHelper.generateCPSign(requestParams,cpAuthKey));

        // 响应消息中返回参数
        Map<String,Object> responseParamPairs = RequestHandler.doPost(requestUri,requestParams);

        if(responseParamPairs.isEmpty())
        {
            System.out.println("None response parameter.");
        }
        else
        {
            if(RETURN_CODE_SUCCEED.equalsIgnoreCase(getString("rtnCode",responseParamPairs)))
            {
                System.out.println("rtnCode: " + getString("rtnCode",responseParamPairs));
                System.out.println("ts: " + getString("ts",responseParamPairs));
                System.out.println("rtnSign: " + getString("rtnSign",responseParamPairs));
            }
            else
            {
                System.out.println("rtnCode: " + getString("rtnCode",responseParamPairs));
                System.out.println("errMsg: " + getString("errMsg",responseParamPairs));
            }
        }
    }

    public static String getString(String key,Map<String,Object> responseParamPairs)
    {
        Asserts.notNull(responseParamPairs, "responseParamPairs");

        Object value = responseParamPairs.get(key);

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    public static void main(String[] args){
    	
//        // CP签名私钥
//        final String CP_AUTH_SIGN_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCHJJ1kxhOXDh4M9QgXj2sXahxG7clGnWDp5YW9f/+Xyf2RzZma1JB76KqXh2ZNyJfpG/4tsUqm1KBQ3w1glvsvUsCcuiAhVmT3CkN7+M/S+ttcXIlNfT+x2UH4h50d1IPLr7qSjgiBkPcW2WFFXxRfaUqSg7xhLp+9ydXJJFQbvTBKCpr2HFRl4DEuF5SxganG8SQau/swCf2l3lHnrIm4ER5B+O4RNqYr/AMo6tge1LXaYp9ss8TM3VmTTGNHGcIaPvSIeBsT4XolhkZ0RyyT++m1ABSKn1kA8Rv5vjzj5WbHDi8LefreQ3gcBax26h/6tbvgTgMwIDzokRrUOM39AgMBAAECggEAcpm7GtTZkfP3ybcUKJ6HCvEBj6hfUZFtuIrZccwUW4x/id/WzTRKXbj8yMiaGYXsRFJnpim9C2ItnMa5mloOIaBEE+PGEV8o+VDrzzo8SkZONLGIAX0fwVpiFjYyJzSqmtSnG1Z0oiLjVa37TY+GQC6SfVJXMfYOoiuBLjOvW2FB8AJQX6G8dU6S4WZc1RdJ9ZyyQgcfKtt/kvja/JroMkOx2SQ13Yd7BU19xFVJCPiEPF9K+CtTIYO/hkHdUwh8+p74QaEHemJ1HfE2bFtZMEplBoSa06zwzUVx0WV1Y3j/qg7rxrDNl6ITmWTyQEn3bdJn0XPrvcRmv4sgce1K4QKBgQDWNyEUlg3yMKJyNmnK8oLUggXNkBC7ayRkN67MsUdS9gsFBhlOZeocrzb+T9GZUpVfDGmLO5708P8eZbRDDNRSVMyr0eV89sPXx579kyV9/nMzA65LasldlNC15gyr61/ldp6Uleb3bUEeYe7AkRnzSrJfLOTLUoTi7huVY59vKQKBgQChgP/FoWD9U/Ahz72IGS3CFFeHZMjFQfryrVBu5aL8iXeTj9ansKeUbIecYW3UqS+HbO6vbx+VvHeG5wlSEbernMNWrny5FQzeichKjbOkXmp50dQVvuebyvPYaIAkB7BFLEXqk0rBn5TPPQFS4bLcXe/xeCkF0Gd+uBrI4IpGtQKBgG1uFjkU+qTZUXL09xBU2J7EmUBMsy966UlE5MfuXBg2VqTHW9Af4furSnWZwuIHPQUkKxqUZ3yLTFhz7iU+fYxdg3zWqdwvlxY5BLBXJhT6ElFiNPyT3bAvoHr7vUdp40AuW45eEXIeXuCteLDorxAI/Zv/LBXt3rKqnm6vSLgZAoGASa7dAoGaCnndOM/anNk/8yfstyzYHIb5wvYnmDDUp3rgP0aEnIUQL7tEM6iPv1JhCNw+GXQNaPdPYRDPQ84pifY/eLCq3pYoBO+/naQAraEV2vZMWI98g6uYjMdAjy+i0CxeyaLhnGz+K36dt/6Y58lDy1sS/EAUt8+vCK7I53ECgYEAu/Sup6U7CPpNarNbfgNJGpNtijMWSwuM5cwB/7T1uhiA9pR9pxkjOA2RxopokqmjfhKSNi6MLSfkxayK/x/33VqJotqMylQns2G9ZlAJ+zUkB/Ihe1eSkP14e3jiFDaYuXwdW8JUUHVXv+dagCdu/aTZdrJg9UmrnYY6qx9F7gc=";
//        // 游戏服务器提供给CP签名接口URL
//        final String requestUri = "https://gss-cn.game.hicloud.com/gameservice/api/gbClientApi";
//        // CP请求参数
//        Map<String,String> mockRequestParams = new HashMap<String,String>();
//        mockRequestParams.put("method","external.hms.gs.checkPlayerSign");
//        mockRequestParams.put("appId","10000");
//        mockRequestParams.put("cpId","1000");
//        mockRequestParams.put("ts","1350539672156");
//        mockRequestParams.put("playerId","100");
//        mockRequestParams.put("playerLevel","10");
//        mockRequestParams.put("playerSSign","VUOoWexHeQC98OFHyWapgKSACDwBgEHWb6IvPutKO0Z/wSVU3SDoK7/vnaLsYte6cYJu/RVWxoGh8lJfHuMoMucKutoNEXnAnPgTG5cfXf79DCtTnhMJ3lHBjaYFD03RWb2XBRKlnF7m455DeU2bvPZOsi7BhTDNPD0bTxY7PWlASLCSX7C7WqHN4/AWxDiU+ki2pPBstuSDecoUQQATBU35bQE2V7DtOsoGAhseuKXZe7yExMqszyZHLKaaqsbqq1rCua6FvJtwlwO82eY7N5kyW29r3MQ/uW1XGh4aPDods9UfD90BSLoPPmLjV9tREX/HFIdxkZ3FVWbkcWR4YQ==");
//        String cpSign = SignHelper.generateCPSign(mockRequestParams,CP_AUTH_SIGN_PRIVATE_KEY);
//        System.out.println("cpSign="+cpSign);
//        GameServiceCallDemo.callGameService(requestUri,mockRequestParams,CP_AUTH_SIGN_PRIVATE_KEY);
    	
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

}
