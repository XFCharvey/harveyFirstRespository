package com.cbox.mock;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParse {

    public static void main(String[] args) throws Exception {
        //1-获取XML-IO流
        InputStream xmlInputStream = getXmlInputStream("xml/tinyioc.xml");
        //2-解析XML-IO流 ，获取Document 对象，以及Document对象 的根节点
        Element rootElement = getRootElementFromIs(xmlInputStream);
        //3~5-从根元素解析得到元素
        parseElementFromRoot(rootElement);
        //控制台输出：　　　　//name == HelloWorld　　　　//className == com.huishe.HelloWord　　　　//propertyEle: name == textone　　　　//propertyEle: value == Hello World!　　　　//propertyEle: name == texttwo　　　  //propertyEle: value == Hello SUN!
    }

    // 1-获取XML-IO流
    private static InputStream getXmlInputStream(String xmlPath) {
        InputStream inputStream = null;
        try {
            // 1-把要解析的 XML 文档转化为输入流，以便 DOM 解析器解析它
            inputStream = new FileInputStream(xmlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }
    
    // 1-获取XML字符串-IO流
    public static InputStream getXmlInputStreamForStr(String xmlStr) {
        InputStream inputStream = null;
        try {
            // 1-把要解析的 XML字符串转化为输入流，以便 DOM 解析器解析它
            inputStream = new ByteArrayInputStream(xmlStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    // 2-解析XML-IO流 ，获取Document 对象，以及Document对象 的根节点
    public static Element getRootElementFromIs(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            return null;
        }
        /*
         * javax.xml.parsers 包中的DocumentBuilderFactory用于创建DOM模式的解析器对象 ， 
         * DocumentBuilderFactory是一个抽象工厂类，它不能直接实例化，但该类提供了一个newInstance方法 ，
                 * 这个方法会根据本地平台默认安装的解析器，自动创建一个工厂的对象并返回。
        */
        // 2-调用 DocumentBuilderFactory.newInstance() 方法得到创建 DOM 解析器的工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // 3-调用工厂对象的 newDocumentBuilder方法得到 DOM 解析器对象。
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        // 4-调用 DOM 解析器对象的 parse() 方法解析 XML 文档，得到代表整个文档的 Document 对象，进行可以利用DOM特性对整个XML文档进行操作了。
        Document doc = docBuilder.parse(inputStream);
        // 5-得到 XML 文档的根节点
        Element root = doc.getDocumentElement();
        // 6-关闭流
        if (inputStream != null) {
            inputStream.close();
        }
        return root;
    }

    // 3-从根元素解析得到元素
    private static void parseElementFromRoot(Element root) {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                // 4-从元素解析得到属性值
                getDataFromElement(ele);
                // 5-从元素解析特定子元素并解析(以property为例)
                getCertainElementFromParentElement(ele);
            }
        }
    }

    // 4-从元素解析得到属性值
    private static void getDataFromElement(Element ele) {
        String name = ele.getAttribute("name");// 根据属性名称读取属性值
        System.out.println("name == " + name);
        String className = ele.getAttribute("class");
        System.out.println("className == " + className);
    }

    // 5-从元素解析特定子元素并解析(以property为例)
    private static void getCertainElementFromParentElement(Element ele) {
        NodeList propertyEleList = ele.getElementsByTagName("property");// 根据标签名称获取标签元素列表
        for (int i = 0; i < propertyEleList.getLength(); i++) {
            Node node = propertyEleList.item(i);
            if (node instanceof Element) {
                Element propertyEle = (Element) node;
                String name = propertyEle.getAttribute("name");
                System.out.println("propertyEle: name == " + name);
                String value = propertyEle.getAttribute("value");
                System.out.println("propertyEle: value == " + value);
            }
        }
    }

}