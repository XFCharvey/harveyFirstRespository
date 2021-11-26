package com.cbox.mock;

import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;
import org.w3c.dom.Element;

import com.cbox.base.utils.sign.Md5Utils;

public class MockTest {

    // 登陆 Url
    // private String loginUrl = "http://my.sunac.com.cn:8060/Default_Login.aspx?rdnum=0.6852456351827736";
    private String loginUrl = "http://my.sunac.com.cn:8060/Default_Login.aspx";
    private String user = "wangxl46";
    private String passwd = "Sunac1918";

    public static void main(String[] args) {
        MockTest test = new MockTest();
        test.test();
    }

    // 生成16位唯一性的订单号
    public String getUUID() {
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

    public void test() {

        HttpClient httpClient = new HttpClient();

        String uid = this.getUUID();
        loginUrl = loginUrl + "?rdnum=" + uid;
        System.out.println(loginUrl);

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
            System.out.println("token=" + token);

            if (statusCode == 302 || statusCode == 200) {// 重定向到新的URL,或200是成功

                System.out.println("模拟登录成功");

                String dataUrl = "";

                // String dataUrl = "http://my.sunac.com.cn:8060/Frame/Index.aspx?ssotoken=" + token;
                // this.getUrl(dataUrl, postMethod, httpClient);

                // postMethod.setRequestHeader("Referer", "http://my.sunac.com.cn:8060/KFXT/RCFW/RcRwcl.aspx");
                // String dataUrl =
                // "http://my.sunac.com.cn:8060/Kfxt/Rcfw/RcRwcl_XMLHTTP.aspx?ProjGUID=8fdac8b0-498d-432c-abdc-f4213819d8ad&ywtype=GetPilotProject&rdnum=0.023521964946984885";
                // this.getUrl(dataUrl, postMethod, httpClient);

                // postMethod.setRequestHeader("Referer", "http://my.sunac.com.cn:8060/KFXT/RCFW/RcRwcl.aspx");
                // dataUrl =
                // "http://my.sunac.com.cn:8060/Kfxt/rcfw/RcRwcl_Grid.aspx?BuGUID=8fdac8b0-498d-432c-abdc-f4213819d8ad&xml=%2FKfxt%2Frcfw%2FRcRwcl_Grid.xml&filter=%3Cfilter%20type%3D%22and%22%3E%3Cfilter%20type%3D%22and%22%3E%3Ccondition%20operator%3D%22eq%22%20attribute%3D%22TaskAscription%22%20value%3D%22%E5%9C%B0%E4%BA%A7%22%2F%3E%3C%2Ffilter%3E%3Cfilter%20type%3D%22and%22%3E%3Cfilter%20type%3D%22and%22%3E%3Ccondition%20operator%3D%22api%22%20attribute%3D%22ProjGUID%22%20value%3D%228fdac8b0-498d-432c-abdc-f4213819d8ad%22%20datatype%3D%22buprojectfilter%22%20application%3D%220102%22%2F%3E%3C%2Ffilter%3E%0D%0A%09%09%3Cfilter%20type%3D%22and%22%2F%3E%3C%2Ffilter%3E%0D%0A%3C%2Ffilter%3E%0D%0A&mySt=2";
                // this.getUrl(dataUrl, postMethod, httpClient);

                // postMethod.setRequestHeader("Referer",
                // "http://my.sunac.com.cn:8060/_grid/gridcontainer.aspx?data=/_grid/griddata.aspx&vscrollmode=0&xml=%2fKfxt%2frcfw%2fRcRwcl_Grid.xml&filter=i8KtABmW%2b%2bpmpEk4%2b3S3RPD6%2f7P54ZE70B%2bArfqc%2f2rE2RXlcF0%2faI55kCPA%2by60Xwx9YitKJJaZtonlFM8hcoPCYKvFjvxXCxBaA8s1xPxD7Nv5NSVlUNKSFNaz1z3hau4O7ggB7R%2fYfZl2sbToYImGsu2%2bTyUe6PAcSORUMTn%2bQa%2fIJKt%2b4E3XsEBziCsclWjh%2flkrF%2b3MLmIh5debFsNUKVB3FewZCAiuwZB9QOunhg2HCyUFKU0E%2bmmT3WTflOoZJyRK%2fn%2foi3bGVuS6yyjNBvtZWI4JiyKtm1ktI0L91Zgaf2P%2ftoq2gz5zjxCfp1Y35FOk6jzGldKanU4%2bVyCIJyoScU%2fiQP9KiPUbKFWHF7sB%2bUKcLKGWN7PSU%2bbkgE46SIC0S6%2fslxfX8JNM1rufR9F7oDDlexhY8hIq0F2kdIR8acFP0%2bl0JnK51PIi3m0XqaSPYSNT8J5P3OiaiQ%3d%3d&processNullFilter=1&customFilter=7Mf93klSAzW8XFLqdrGSiLl9j5X4xODJQpo9xBq8EWx0lI33ay7InUruGMlYEmU6qW9vLIZZGK43srzwEnFJzHLo3sSsXz0gaWWAjE4Vc6ovv%2ffaUVxdxEsCa1U%2fnzAYrkKcUHyelGeYlwk9sG8i%2f1QvNvu24G8Ji7D39mP6AK8USeYc5%2bMDdqWUV344LyE6wIsnkQuzGKUgj6AlHK7UEJxzF%2fBe9TIgxQwX1owJkpOAfJIUE4QkQVc%2bcYOiNpRNNVpIG1MkQ6w8qn%2fb3YRiOh1g0SoNQI77tjx3qJQp5soAbRYzW2WQKHw1xLix1eeM99nDVOWg3brTA14JfLgFIO9zrtuLQnN6Bjqtf3riHvg%3d&customFilter2=TTm5zl9l7AYRhO85y0fRIQ%3d%3d&dependencySQLFilter=&location=&allowResize=1&allowPage=1&pageSize=20&showPageCount=1&appName=Default&multiselect=1&selectbycheckbox=1&defaultselected=1&gridId=appGrid&application=&cp=");
                // 任务工单
                // dataUrl =
                // "http://my.sunac.com.cn:8060/_grid/griddata.aspx?xml=%2fKfxt%2frcfw%2fRcRwcl_Grid.xml&gridId=appGrid&sortCol=&sortDir=&vscrollmode=0&multiSelect=1&selectByCheckBox=1&filter=i8KtABmW%2b%2bpmpEk4%2b3S3RPD6%2f7P54ZE70B%2bArfqc%2f2rE2RXlcF0%2faI55kCPA%2by60Xwx9YitKJJaZtonlFM8hcoPCYKvFjvxXCxBaA8s1xPxD7Nv5NSVlUNKSFNaz1z3hau4O7ggB7R%2fYfZl2sbToYImGsu2%2bTyUe6PAcSORUMTn%2bQa%2fIJKt%2b4E3XsEBziCsclWjh%2flkrF%2b3MLmIh5debFsNUKVB3FewZCAiuwZB9QOunhg2HCyUFKU0E%2bmmT3WTflOoZJyRK%2fn%2foi3bGVuS6yyjNBvtZWI4JiyKtm1ktI0L91Zgaf2P%2ftoq2gz5zjxCfp1Y35FOk6jzGldKanU4%2bVyCIJyoScU%2fiQP9KiPUbKFWHF7sB%2bUKcLKGWN7PSU%2bbkgE46SIC0S6%2fslxfX8JNM1rufR9F7oDDlexhY8hIq0F2kdIR8acFP0%2bl0JnK51PIi3m0XqaSPYSNT8J5P3OiaiQ%3d%3d&processNullFilter=1&customFilter=7Mf93klSAzW8XFLqdrGSiLl9j5X4xODJQpo9xBq8EWx0lI33ay7InUruGMlYEmU6qW9vLIZZGK43srzwEnFJzHLo3sSsXz0gaWWAjE4Vc6ovv%2ffaUVxdxEsCa1U%2fnzAYrkKcUHyelGeYlwk9sG8i%2f1QvNvu24G8Ji7D39mP6AK8USeYc5%2bMDdqWUV344LyE6wIsnkQuzGKUgj6AlHK7UEJxzF%2fBe9TIgxQwX1owJkpOAfJIUE4QkQVc%2bcYOiNpRNNVpIG1MkQ6w8qn%2fb3YRiOh1g0SoNQI77tjx3qJQp5soAbRYzW2WQKHw1xLix1eeM99nDVOWg3brTA14JfLgFIO9zrtuLQnN6Bjqtf3riHvg%3d&customFilter2=TTm5zl9l7AYRhO85y0fRIQ%3d%3d&dependencySQLFilter=&location=&pageNum=1&pageSize=200&showPageCount=1&appName=Default&application=&cp=";
                // dataUrl =
                // "http://my.sunac.com.cn:8060/_grid/griddata.aspx?xml=%2fKfxt%2frcfw%2fRcRwcl_Grid.xml&gridId=appGrid&sortCol=&sortDir=&vscrollmode=0&multiSelect=1&selectByCheckBox=1&filter=i8KtABmW%2b%2bpmpEk4%2b3S3RPD6%2f7P54ZE70B%2bArfqc%2f2rE2RXlcF0%2faI55kCPA%2by60EUJEsZBMQRirN7rTrL4sW2FIBtLAWYmLB0ItuIJczkFexTlsQPIUa6YAgcLomf7Ca7OfG5MMCc5TYVPS3ZWO2zhsGXv6oNISm%2bsTlFdxRJg5e9ONagAFvMYFSYDBQ%2fEjzcZ8V5MUwRlMq5VMEy%2f%2fLcTqqNX2Cn9Ifn19%2bAgmUdZYnL1dL3M3%2bCsUtA0CyUSzLq5XauTXf3hYxxE3g17ADMaf%2biJTiTnZg0AEOueziA4%3d&processNullFilter=1&customFilter=7Mf93klSAzW8XFLqdrGSiLl9j5X4xODJQpo9xBq8EWx0lI33ay7InUruGMlYEmU6qW9vLIZZGK43srzwEnFJzHLo3sSsXz0gaWWAjE4Vc6ovv%2ffaUVxdxEsCa1U%2fnzAYrkKcUHyelGeYlwk9sG8i%2f1QvNvu24G8Ji7D39mP6AK8USeYc5%2bMDdqWUV344LyE6wIsnkQuzGKUgj6AlHK7UEJxzF%2fBe9TIgxQwX1owJkpOAfJIUE4QkQVc%2bcYOiNpRNNVpIG1MkQ6w8qn%2fb3YRiOh1g0SoNQI77tjx3qJQp5soAbRYzW2WQKHw1xLix1eeM99nDVOWg3brTA14JfLgFIO9zrtuLQnN6Bjqtf3riHvg%3d&customFilter2=TTm5zl9l7AYRhO85y0fRIQ%3d%3d&dependencySQLFilter=&location=&pageNum=1&pageSize=100&showPageCount=1&appName=Default&application=&cp=";
                // this.getUrl(dataUrl, postMethod, httpClient);

                // 项目信息

                // 房源
                dataUrl = "http://my.sunac.com.cn:8060/_grid/griddata.aspx?xml=%2fSlxt%2fXMZB%2fRoomList_Grid_Kf.xml&funcid=01020204&gridId=appGrid&sortCol=&sortDir=&vscrollmode=0&multiSelect=1&selectByCheckBox=0&filter=7yNlwDHqjXW%2b7aWP%2fE23cE1YS4sSJTGLE%2bgYT0mJkaIhPyqgA%2fmTQpZ7Ray17z073VUmwnNtLxH1rSFCf0CcRGHBowO%2b0Yg7%2flt3Yu%2bv1cX0vnvEiWlaqqAWSeCaBnhMXBkK0d8bVjeD%2fotw1Vf0rNHXgQW6SS10mZc1XH4W63N8HiliLVHZyGXUfAQcH5bcqe%2bGnx5jYVhGxvb4tmX0lk%2fDTq8Fs5pzoTQBxyCvsYNYLwLfUFcuGE79OgTvsc4GjPpmTQBQUA6Wz7i%2fz8KznU5bp9rmNKxi65UHMycQBtlVUdlm4Krq3BOY%2b92WNTde&processNullFilter=1&customFilter=&customFilter2=&dependencySQLFilter=&location=&pageNum=1&pageSize=20&showPageCount=1&appName=Default&application=&cp=";
                this.getUrl(dataUrl, postMethod, httpClient);

                // postMethod.setRequestHeader("Referer", "");
                // dataUrl = "http://my.sunac.com.cn:10080/WebReport/ReportServer?formlet=rc.frm";
                // this.getUrl(dataUrl, postMethod, httpClient);

                // postMethod.setRequestHeader("Referer", "http://my.sunac.com.cn:10080/WebReport/ReportServer?formlet=rc.frm");
                // dataUrl =
                // "http://my.sunac.com.cn:10080/WebReport/ReportServer?op=chart&cmd=writer_out_html&sessionID=82006&chartID=Cells__A2__A2__78f817c1-ace6-4613-b900-d3a570a607f1__index__0&__time=1629104889291";
                // this.getUrl(dataUrl, postMethod, httpClient);

            } else {
                System.out.println("登录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getUrl(String dataUrl, PostMethod postMethod, HttpClient httpClient) {

        // 获得登陆后的 Cookie
        Cookie[] cookies = httpClient.getState().getCookies();
        StringBuffer tmpcookies = new StringBuffer();
        for (Cookie c : cookies) {
            tmpcookies.append(c.toString() + ";");
            // System.out.println("cookies = " + c.toString());
        }

        // 进行登陆后的操作
        GetMethod getMethod = new GetMethod(dataUrl);
        // 每次访问需授权的网址时需带上前面的 cookie 作为通行证
        // （httpClient客户端 会自动带上 如不是特殊要求一般不进行设置）
        getMethod.setRequestHeader("cookie", tmpcookies.toString());
        // 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
        // 例如，referer 从哪里来的，UA 像搜索引擎都会表名自己是谁，无良搜索引擎除外
        postMethod.setRequestHeader("Referer", "http://my.sunac.com.cn:8060/Default.aspx?Page=%2fFrame%2fIndex.aspx&redirect=1");
        postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
        try {
            System.out.println("*************************************************************************");
            System.out.println("visit url:" + dataUrl);
            System.out.println();

            httpClient.executeMethod(getMethod);
            // 打印出返回数据，检验一下是否成功
            String text = getMethod.getResponseBodyAsString();
            // System.out.println(text);

            this.deal_resource(text);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * html源码处理
       * @param response
        * @throws ParserException
        */
    public void deal_resource(String response) throws ParserException {

        Parser parser = Parser.createParser(response, "gbk");
        HtmlPage page = new HtmlPage(parser);

        parser.visitAllNodesWith(page);

        NodeList nodeList = page.getBody();

        NodeFilter filter = new TagNameFilter("table");

        nodeList = nodeList.extractAllNodesThatMatch(filter, true);

        TableTag tag = (TableTag) nodeList.elementAt(0);

        int iIndex = 1;
        for (int i = 0; i < tag.getChildCount(); i++) {
            NodeList nodelist1 = tag.getChild(i).getChildren();
            if (nodelist1 != null) {
                // String[] nodeMsg = new String[nodelist1.size()];
                // for (int j = 0; j < nodelist1.size(); j++) {
                // nodeMsg[j] = nodelist1.elementAt(j).toPlainTextString();
                // }
                //
                // String houseName = String.valueOf(nodeMsg[1]);
                // String[] houseNameArr = houseName.split("-");
                // for (int j = 0; j < houseNameArr.length; j++) {
                // String project = houseNameArr[0];
                // System.out.println(project);
                // }
                System.out.print("Row" + (iIndex++) + "：");
                for (int j = 0; j < nodelist1.size(); j++) {
                    System.out.print(nodelist1.elementAt(j).toPlainTextString() + ",");
                }
                System.out.println();
            }
        }
    }

}
