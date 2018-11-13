package com.springframework.utils;

import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;

import java.io.CharArrayReader;
import java.util.Map;

/**
 * @author summer
 */
@Slf4j
public class NSXmlUtils {
    private DocumentFactory factory;

    public NSXmlUtils(Map<String, String> nameSpaceMap) {
        factory = new DocumentFactory();
        factory.setXPathNamespaceURIs(nameSpaceMap);
    }

    /**
     * Map<String, String> nameSpaceMap = new HashMap<String, String>();
     * example: nameSpaceMap.put("gm", "http://earth.google.com/kml/2.0");
     * Xpath like: /gm:root/gm:sub
     *
     * @param xml
     * @param
     * @return
     */
    public Document getDocument(String xml) {
        SAXReader reader = new SAXReader(factory);
        Document document = null;
        try {
            document = reader.read(new CharArrayReader(xml.toCharArray()));
        } catch (DocumentException e) {
            log.error("", e);
        }// 读取XML文件
        return document;


    }

}
