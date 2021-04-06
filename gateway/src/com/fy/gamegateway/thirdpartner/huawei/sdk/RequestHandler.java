package com.fy.gamegateway.thirdpartner.huawei.sdk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;

public class RequestHandler
{

    private static final int HTTP_RESPONSE_STATUS_CODE_OK = 200;

    public static Map<String,Object> doPost(String url, Map<String, String> paramaters)
    {
        HttpPost httpReq = new HttpPost(url);

        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try
        {
            if (paramaters != null)
            {
                List<NameValuePair> paramPairs = new ArrayList<NameValuePair>();

                BasicNameValuePair bnv;

                for (Map.Entry<String, String> entry : paramaters.entrySet())
                {
                    bnv = new BasicNameValuePair(entry.getKey(), entry.getValue());
                    paramPairs.add(bnv);
                }

                httpReq.setEntity(new UrlEncodedFormEntity(paramPairs, "UTF-8"));

            }

            Map<String,Object> responseParamPairs = new HashMap<String,Object>();

            HttpResponse resp = httpclient.execute(httpReq);

            if(null != resp && HTTP_RESPONSE_STATUS_CODE_OK == resp.getStatusLine().getStatusCode())
            {
                responseParamPairs = JSON.parseObject(EntityUtils.toString(resp.getEntity()));
            }

            return responseParamPairs;
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
        finally
        {
            try
            {
                httpclient.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
