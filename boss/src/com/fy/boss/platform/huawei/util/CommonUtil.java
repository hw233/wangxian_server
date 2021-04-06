
package com.fy.boss.platform.huawei.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fy.boss.platform.huawei.util.RSA;

public abstract class CommonUtil
{
	
    public static boolean rsaDoCheck(Map<String, Object> params, String sign, String publicKey)
    {
        //获取待签名字符串
        String content = RSA.getSignData(params);
        //验签
        return RSA.doCheck(content, sign, publicKey);
    }
    
}
