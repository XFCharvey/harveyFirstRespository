package com.cbox.business.timetask.mock.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cbox.base.utils.StrUtil;

/**
 * @ClassName: MockHttpsUtil 
 * @Function: https模拟访问的工具类。注意这里用到的包是： org.apache.http.client.HttpClient
 * 
 * @author qiuzq 
 * @date 2021年11月9日 上午11:06:03 
 * @version 1.0
 */
public class MockHttpsUtil {
    // 用来存储cookies信息的变量
    private static CookieStore cookieStore;
    private static String charset = "utf-8";

    public static String doHttpsPost(HttpClient httpClient, String url, Map<String, String> map) {

        System.out.println("Post Url:" + url);

        String result = null;

        HttpPost httpPost = null;
        HttpResponse response = null;
        try {

            httpPost = new HttpPost(url);
            // 设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> elem = (Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                httpPost.setEntity(entity);
            }
            response = httpClient.execute(httpPost);

            result = MockHttpsUtil.getResult(response);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String doHttpsGet(HttpClient httpClient, String url) {

        System.out.println("Get Url:" + url);

        String result = null;
        HttpResponse response = null;
        HttpGet request = new HttpGet(url);
        try {
            response = httpClient.execute(request);

            result = MockHttpsUtil.getResult(response);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /** 得到返回的body数据 */
    private static String getResult(HttpResponse response) {
        String result = null;
        try {
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /** 打印cookie数据 */
    private static void printCookies() {
        if (cookieStore != null) {
            List<Cookie> cookielist = cookieStore.getCookies();
            for (Cookie cookie : cookielist) {
                String name = cookie.getName();
                String value = cookie.getValue();
                System.out.println("cookie name =" + name + ", value=" + value);
            }
        }

    }

    /** 得到Get请求的Location数据 */
    public static String getLocationUrl(String url, HttpClient httpClient) {
        String location = null;
        int responseCode = 0;

        // System.out.println("Url:" + url);
        // printCookies();


        HttpResponse response;
        try {

            response = httpClient.execute(new HttpGet(url));
            responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 302) {
                Header locationHeader = response.getFirstHeader("Location");
                location = locationHeader.getValue();
            }

            // 这段很奇怪，如果注释掉就会报错
            String result = MockHttpsUtil.getResult(response);
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

        }

        return location;
    }

    /** 模拟登陆明源云链系统 */
    public static HttpClient mockLoginYl() {

        System.out.println("自动模拟登录...");

        String url = "https://passport-hb.myyscm.com/api/login-v2";
        Map<String, String> map = new HashMap<String, String>();
        map.put("tenantCode", "sunac");
        map.put("username", "wangxl46");
        map.put("password", "sunac123");
        map.put("captchaCode", "");

        ProtocolSocketFactory fcty = new CboxSecureProtocolSocketFactory();
        // 加入相关的https请求方式
        Protocol.registerProtocol("https", new Protocol("https", fcty, 443));

        cookieStore = new BasicCookieStore();
        RequestConfig config = RequestConfig.custom().setConnectTimeout(50000).setConnectionRequestTimeout(10000).setSocketTimeout(50000).setRedirectsEnabled(false).build();// 不允许重定向
        HttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).setDefaultRequestConfig(config).build();

        String charset = "utf-8";
        // HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            // if (httpClient == null) {
            // httpClient = new SSLClient();
            // }

            httpPost = new HttpPost(url);
            // 设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> elem = (Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                httpPost.setEntity(entity);
            }
            
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // System.out.println(result);
        JSONObject jObj = JSON.parseObject(result);
        String errCode = jObj.getString("errcode");
        if ("0".equals(errCode)) {
            // 登录成功，继续完成权限验证，总共需要执行下面三次请求操作

            url = "https://yf-hb.myyscm.com/?_smp=MobileCheckRoom";
            String resContent = MockHttpsUtil.getLocationUrl(url, httpClient);
            // System.out.println(resContent);

            url = resContent;
            resContent = MockHttpsUtil.getLocationUrl(url, httpClient);


            url = resContent;
            resContent = MockHttpsUtil.getLocationUrl(url, httpClient);

            System.out.println("登录成功！");

            return httpClient;
        } else {
            return null;
        }
    }

    public static int parseProblemCount(String result) {
        int iCount = 0;

        if (StrUtil.isNotNull(result)) {
            JSONObject obj = JSON.parseObject(result);
            String count = obj.getJSONObject("data").getString("所有问题");
            if (StrUtil.isNotNull(count)) {
                iCount = StrUtil.getNotNullIntValue(count);
            }
        }

        return iCount;
    }

}
