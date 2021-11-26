package com.cbox.business.timetask.mock.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.http.client.params.ClientPNames;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;
import org.w3c.dom.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cbox.base.utils.StrUtil;
import com.cbox.base.utils.sign.Md5Utils;
import com.cbox.mock.XMLParse;

/**
 * @ClassName: MockUtil 
 * @Function: http模拟访问工具类，这里用到的包：org.apache.commons.httpclient.HttpClient
 * 参考链接：https://blog.csdn.net/ganlijianstyle/article/details/7569494  
 * 
 * @author qiuzq 
 * @date 2021年9月18日 下午2:37:22 
 * @version 1.0
 */
public class MockUtil {

    // 生成16位唯一性的订单号
    public static String getUUID() {
        // 随机生成一位整数
        int random = (int) (Math.random() * 9 + 1);
        String valueOf = String.valueOf(random);
        // 生成uuid的hashCode值
        int hashCode = UUID.randomUUID().toString().hashCode();
        // 可能为负数
        if (hashCode < 0) {
            hashCode = -hashCode;
        }
        String value = valueOf + String.format("%015d", hashCode);
        // System.out.println(value);
        return "0." + value;
    }

    public static HttpClient mockLogin() {

        String loginUrl = "http://my.sunac.com.cn:8060/Default_Login.aspx";
        // String user = "wangxl46";
        // String passwd = "Sunac1918";
        String user = "panj11";
        String passwd = "Panjin1987";

        HttpClient httpClient = new HttpClient();

        String uid = getUUID();
        loginUrl = loginUrl + "?rdnum=" + uid;

        // 模拟登陆，按实际服务器端要求选用 Post 或 Get 请求方式
        PostMethod postMethod = new PostMethod(loginUrl);

        // 设置登陆时要求的信息，用户名和密码
        NameValuePair[] data = { new NameValuePair("usercode", user), new NameValuePair("psw", Md5Utils.hash(passwd)) };

        postMethod.setRequestBody(data);
        try {

            // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);

            int statusCode = httpClient.executeMethod(postMethod);
            String result = new String(postMethod.getResponseBody());
            // System.out.println(result);

            // 1-获取XML-IO流
            InputStream xmlInputStream = XMLParse.getXmlInputStreamForStr(result);
            // 2-解析XML-IO流 ，获取Document 对象，以及Document对象 的根节点
            Element rootElement = XMLParse.getRootElementFromIs(xmlInputStream);
            String token = rootElement.getAttribute("token");
            // System.out.println("token=" + token);

            if (statusCode == 302 || statusCode == 200) {// 重定向到新的URL,或200是成功
                // 登录成功

            } else {
                httpClient = null;
                System.out.println("登录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpClient = null;
        }

        return httpClient;

    }

    public static HttpClient mockLoginYl() {

        String loginUrl = "https://passport-hb.myyscm.com/api/login-v2";
        String user = "wangxl46";
        String passwd = "sunac123";
        String tenantCode = "sunac";
        String captchaCode = "";

        // 声明
        ProtocolSocketFactory fcty = new CboxSecureProtocolSocketFactory();
        // 加入相关的https请求方式
        Protocol.registerProtocol("https", new Protocol("https", fcty, 443));

        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(loginUrl);

        // 设置登陆时要求的信息，用户名和密码
        NameValuePair[] data = { new NameValuePair("tenantCode", tenantCode), new NameValuePair("username", user),
                new NameValuePair("password", passwd),
                new NameValuePair("captchaCode", captchaCode) };

        postMethod.setRequestBody(data);
        try {

            // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            httpClient.getParams().setParameter("http.protocol.handle-redirects", false);

            int statusCode = httpClient.executeMethod(postMethod);
            String result = new String(postMethod.getResponseBody());
            System.out.println(result);

            // Header headers = postMethod.getResponseHeader("Set-Cookie");
            // HeaderElement[] elements = headers.getValues();
            // postMethod.getresponse

            Header locationHeader = postMethod.getResponseHeader("Location");

            // PASSPORTSID2

            JSONObject jObj = JSON.parseObject(result);
            String errCode = jObj.getString("errcode");
            if ("0".equals(errCode)) {
                // 登录成功，返回连接串

                String callbackURL = jObj.getString("callbackURL");
                MockUtil.visitUrlGet(callbackURL, httpClient);

            } else {
                httpClient = null;
                System.out.println("登录失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            httpClient = null;
        }

        return httpClient;

    }





    /**
     * visitUrlGet: 通过get方式访问url，返回结果数据
     *
     * @date: 2021年9月18日 上午9:46:05 
     * @author qiuzq 
     * @param dataUrl
     * @param httpClient
     * @return
     */
    public static String visitUrlGet(String dataUrl, HttpClient httpClient) {
        String resContent = "";

        // 获得登陆后的 Cookie
        Cookie[] cookies = httpClient.getState().getCookies();
        StringBuffer tmpcookies = new StringBuffer();
        for (Cookie c : cookies) {
            tmpcookies.append(c.toString() + ";");
        }

        // 进行登陆后的操作
        GetMethod getMethod = new GetMethod(dataUrl);
        // 每次访问需授权的网址时需带上前面的 cookie 作为通行证
        // （httpClient客户端 会自动带上 如不是特殊要求一般不进行设置）
        getMethod.setRequestHeader("cookie", tmpcookies.toString());
        // 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据

        try {
            System.out.println("*************************************************************************");
            System.out.println("Url:" + dataUrl);
            // System.out.println();

            httpClient.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);

            httpClient.executeMethod(getMethod);

            // 打印出返回数据，检验一下是否成功
            resContent = getMethod.getResponseBodyAsString();

            Header locationHeader = getMethod.getResponseHeader("Location");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resContent;
    }
    

    public static String visitUrlPost(String dataUrl, Map<String, String> params, HttpClient httpClient) {
        String resContent = "";

        // 获得登陆后的 Cookie
        Cookie[] cookies = httpClient.getState().getCookies();
        StringBuffer tmpcookies = new StringBuffer();
        for (Cookie c : cookies) {
            tmpcookies.append(c.toString() + ";");
        }
        // 进行登陆后的操作
        PostMethod postMethod = new PostMethod(dataUrl);
        // 每次访问需授权的网址时需带上前面的 cookie 作为通行证
        // （httpClient客户端 会自动带上 如不是特殊要求一般不进行设置）
        postMethod.setRequestHeader("cookie", tmpcookies.toString());
        // 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
        // 设置请求参数
        if(params != null) {
            params.forEach((key, value) -> {
                postMethod.addParameter(key, value);
            });
        }

        postMethod.setRequestHeader("Referer", "http://my.sunac.com.cn:8060/Frame/Index.aspx");

        try {
            // System.out.println("*************************************************************************");
            // System.out.println("Url:" + dataUrl);
            // System.out.println();

            httpClient.executeMethod(postMethod);

            // 打印出返回数据，检验一下是否成功
            resContent = postMethod.getResponseBodyAsString();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resContent;
    }

    /**
     * getTableData:
     *
     * @date: 2021年9月18日 上午11:42:49 
     * @author qiuzq 
     * @param resContent 返回的文本信息
     * @param attrKey 需要从tr中提取的属性值
     * @param iElementIndex 取得param数量，默认为0
     * @return
     * @throws ParserException
     */
    public static List<String[]> getTableData(String resContent, String attrKey, int iElementIndex) throws ParserException {
        if (StrUtil.isNull(resContent)) {
            return new ArrayList();
        }

        Parser parser = Parser.createParser(resContent, "gbk");
        HtmlPage page = new HtmlPage(parser);
        parser.visitAllNodesWith(page);
        TableTag[] nodeList = page.getTables();

        TableTag tag = (TableTag) nodeList[iElementIndex];

        List<String[]> listData = new ArrayList<String[]>();

        for (int i = 0; i < tag.getChildCount(); i++) {
            Node node = tag.getChild(i);

            NodeList nodelist1 = node.getChildren();
            if (nodelist1 != null) {
                /* 获取行数据 */

                int iKeyLen = 0;
                int iIndex = 0;

                if (StrUtil.isNotNull(attrKey)) {
                    String[] attrKeys = attrKey.split(",");
                    iKeyLen = attrKeys.length;
                }

                String[] nodeMsg = new String[nodelist1.size() + iKeyLen];

                // 增加提取的
                if (StrUtil.isNotNull(attrKey)) {
                    String[] attrKeys = attrKey.split(",");

                    TagNode tagNode = (TagNode) node;
                    for (int h = 0; h < iKeyLen; h++) {
                        nodeMsg[iIndex++] = tagNode.getAttribute(attrKeys[h]);
                    }
                }

                for (int j = 0; j < nodelist1.size(); j++) {
                    nodeMsg[iIndex++] = nodelist1.elementAt(j).toPlainTextString();
                }

                listData.add(nodeMsg);
            }
        }

        return listData;
    }

    public static Map<String, Object> getFormData(String resContent, String spanClass) throws ParserException {
        if (StrUtil.isNull(resContent)) {
            return new HashMap();
        }

        Parser parser = Parser.createParser(resContent, "gbk");
        HtmlPage page = new HtmlPage(parser);

        NodeFilter inputFilter = new NodeClassFilter(InputTag.class);
        NodeList listNodeFilter = parser.extractAllNodesThatMatch(inputFilter);

        Map<String, Object> mapValue = new HashMap<String, Object>();
        for (int i = 0; i < listNodeFilter.size(); i++) {
            Node node = listNodeFilter.elementAt(i);
            InputTag tag = (InputTag) node;
            mapValue.put(tag.getAttribute("name"), tag.getAttribute("value"));
        }

        if (StrUtil.isNotNull(spanClass)) {
            Parser parser2 = Parser.createParser(resContent, "gbk");
            AndFilter andFilter = new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("class", spanClass));
            NodeList listNodeFilter2 = parser2.extractAllNodesThatMatch(andFilter);
            for (int i = 0; i < listNodeFilter2.size(); i++) {
                Node node = listNodeFilter2.elementAt(i);
                Span tag = (Span) node;
                mapValue.put(tag.getAttribute("name"), tag.getAttribute("value"));
            }
        }


        return mapValue;
    }

    public static List<String[]> getTableData(String resContent) throws ParserException {
        return getTableData(resContent, "", 0);
    }

    public static Node getNode(String resContent, String filterKey, int iElementIndex) throws ParserException {
        if (StrUtil.isNull(resContent)) {
            return null;
        }

        Parser parser = Parser.createParser(resContent, "gbk");
        HtmlPage page = new HtmlPage(parser);
        parser.visitAllNodesWith(page);
        NodeList nodeList = page.getBody();
        NodeFilter filter = new TagNameFilter(filterKey);

        nodeList = nodeList.extractAllNodesThatMatch(filter, true);

        return nodeList.elementAt(iElementIndex);
    }

    public static void printList(List<String[]> listData) {
        for (int i = 0; i < listData.size(); i++) {
            String[] nodemsg = listData.get(i);

            System.out.print("Line" + (i + 1) + ": ");
            for (int j = 0; j < nodemsg.length; j++) {
                String str = StrUtil.getNotNullStrValue(nodemsg[j], "x");
                System.out.print(str);
                if (j < nodemsg.length - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
        }
    }

    public static void printMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
        }
    }

}
