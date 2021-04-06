package com.fy.gamegateway.thirdpartner.huawei.sdk;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;

public class SignHelper
{
    public static String sign(byte[] data, String privateKey)
    {
        try
        {
            byte[] e = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(privateK);
            signature.update(data);
            return Base64.encodeBase64String(signature.sign());
        }
        catch (Exception var)
        {
            System.out.println("SignUtil.sign error." + var);
            return "";
        }
    }

    /**
     * 根据参数Map构造排序好的参数串
     *
     * @param params
     * @return
     */
    public static String format(Map<String, String> params)
    {
        StringBuffer base = new StringBuffer();
        Map<String, String> tempMap = new TreeMap<String, String>(params);

        // 获取计算nsp_key的基础串
        try
        {
            for (Map.Entry<String, String> entry : tempMap.entrySet())
            {
                String k = entry.getKey();
                String v = entry.getValue();
                base.append(k).append("=").append(URLEncoder.encode(v, "UTF-8")).append("&");
            }
        }
        catch (UnsupportedEncodingException e)
        {
            System.out.println("Encode parameters failed.");
            e.printStackTrace();
        }

        String body = base.toString().substring(0, base.toString().length() - 1);
        // 空格和星号转义
        body = body.replaceAll("\\+", "%20").replaceAll("\\*", "%2A");

        return body;
    }

    public static String generateCPSign(Map<String,String> requestParams,final String cpAuthKey)
    {
        // 对消息体中查询字符串按字典序排序并且进行URLCode编码
        String baseStr = format(requestParams);

        // 用CP侧签名私钥对上述编码后的请求字符串进行签名
        String cpSign = sign(baseStr.getBytes(Charset.forName("UTF-8")), cpAuthKey);

        return cpSign;
    }
}
