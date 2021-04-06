/**
 * @(#)RsaTest.java, Mar 21, 2013. Copyright (c) 2011, Wandou Labs and/or its
 *                   affiliates. All rights reserved. WANDOU LABS
 *                   PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.fy.boss.platform.wandoujia;

import com.fy.boss.platform.wandoujia.WandouRsa;

public class RsaTest {
 
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println("WandouLabs PaySDK 2013 - aidi@wandoujia.com");
        String content = "{\"timeStamp\":1363848203377,\"orderId\":100001472,\"money\":4000,\"chargeType\":\"BALANCEPAY\",\"appKeyId\":100000000,\"buyerId\":1,\"cardNo\":null}";
        String sign = "VwnhaP9gAbDD2Msl3bFnvsJfgz3NOAqM/JVexl1myHfsrHX3cRrFXz86cNO+oNYWBBM7m/5ZdtHRpSArZWFuZHysKfirO3BynUaIYSAiD2J1Xio5q9+Yr83cI/ESyemVAt7lK4lMW3ReSwmAcOs0kDZLAxVIb++EPy0y2NpH4kI=";
        System.out.println("content:" + content);
        System.out.println("sign:" + sign);
        boolean check = false;
        check = WandouRsa.doCheck(content, sign);
        System.out.println("RsaCheck:" + check);

    }

}
