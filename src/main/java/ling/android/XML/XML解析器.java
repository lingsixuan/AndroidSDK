package ling.android.XML;

import android.widget.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.webkit.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.app.*;
import android.content.*;
import android.content.res.*;

import java.util.*;
//import jiesheng.*;
import java.io.*;

import org.xmlpull.v1.*;

public class XML解析器 {

    public XML解析器(String XML文本) throws XmlPullParserException {
        if (!载入XML(XML文本))
            throw new XmlPullParserException("载入XML失败");
    }

    //调用本方法初始化XML，返回是否载入XML成功
    protected boolean 载入XML(String XML文本) throws XmlPullParserException {
        parser = XmlPullParserFactory.newInstance().newPullParser();
        StringReader reader = new StringReader(XML文本);
        parser.setInput(reader);
        if (parser.getEventType() == 1) {
            reader.close();
            return false;
        }
        return true;
    }

    //获取当前解析到的位置，0为文档开始位置，1为文档结束位置，2为节点开始位置，3为节点结束位置
    public int 当前解析位置() throws XmlPullParserException {
        return parser.getEventType();
    }

    //获取当前已载入XML文档当前节点位置对应的节点名称，如果当前解析位置不在节点开始位置或节点结束位置，将返回空字符串
    public String 当前节点名() {
        String name = parser.getName();
        return name;
        //return name != null ? name : "";
    }

    //获取当前已载入XML文档当前节点位置对应的节点内容，如果当前解析位置不在节点开始位置或节点结束位置，将返回空字符串
    public String 当前节点内容() {
        String name = parser.getText();
        return name;
        //return name != null ? name : "";
    }

    //获取当前节点位置的属性数量，如果失败，则返回-1
    public int 属性数量() {
        return parser.getAttributeCount();
    }

    //获取当前节点位置指定索引位置处的属性名称，索引不得大于属性数量，如果失败，则返回空字符串
    public String 取属性名(int 索引) {
        String name = parser.getAttributeName(索引);
        return name;
        //return name != null ? name : "";
    }

    //获取当前节点位置指定索引位置处的属性内容，索引不得大于属性数量，如果失败，则返回空对象
    public String 取属性内容(int 索引) {
        String value = parser.getAttributeValue(索引);
        return value;
        //return value != null ? value : "";
    }

    //解析一次XML，返回解析状态，-1为解析出现异常，0为文档开始位置，1为文档结束位置，2为节点开始位置，3为节点结束位置
    public int 解析() throws XmlPullParserException, IOException {
        return parser.next();
    }

    private XmlPullParser parser;


}