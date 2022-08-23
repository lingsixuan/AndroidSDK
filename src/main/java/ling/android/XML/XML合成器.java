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

public class XML合成器 {

    public XML合成器() {
        try {
            serializer = XmlPullParserFactory.newInstance().newSerializer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //开始定义一个XML文档，返回开始文档的结果，如果成功返回真，反之则假，默认UTF-8编码
    public boolean 开始XML文档() {
        return 开始XML文档("utf-8");
    }

    //开始定义一个XML文档，返回开始文档的结果，如果成功返回真，反之则假，需要传入参数，为该XML文档的编码类型
    public boolean 开始XML文档(String 文档编码) {
        if (serializer == null) {
            return false;
        }
        try {
            writer = new StringWriter();
            serializer.setOutput(writer);
            serializer.startDocument(文档编码, true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //该方法与开始XML文档成对使用，返回结束XML的结果
    public boolean 结束XML文档() {
        if (serializer == null) {
            return false;
        }
        try {
            serializer.endDocument();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //开始定义一个XML节点，返回开始节点的结果，如果成功返回真，反之则假
    public boolean 开始节点(String 节点名称) {
        if (serializer == null) {
            return false;
        }
        try {
            serializer.startTag(null, 节点名称);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //向当前节点添加属性，返回添加结果，如果成功返回真，反之则假
    public boolean 添加节点属性(String 属性名, String 属性内容) {
        if (serializer == null) {
            return false;
        }
        try {
            serializer.attribute(null, 属性名, 属性内容);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //向当前节点设置内容，返回设置结果，如果成功返回真，反之则假
    public boolean 置节点内容(String 内容) {
        if (serializer == null) {
            return false;
        }
        try {
            serializer.text(内容);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //向当前节点位置添加注释，返回添加结果，如果成功返回真，反之则假
    public boolean 添加注释(String 内容) {
        if (serializer == null) {
            return false;
        }
        try {
            serializer.comment(内容);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //本方法与开始节点成对使用，如果成功返回真，反之则假
    public boolean 结束节点(String 节点名称) {
        if (serializer == null) {
            return false;
        }
        try {
            serializer.endTag(null, 节点名称);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //将XML文档数据导出为文本型数据，如果失败，则返回空字符串
    public String 导出文本() {
        if (writer != null) {
            String xml = writer.toString();
            return xml;
        }
        return "";
    }


    private XmlSerializer serializer;
    private StringWriter writer;


}