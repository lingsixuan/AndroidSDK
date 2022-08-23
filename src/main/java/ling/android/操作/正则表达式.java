package ling.android.操作;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 正则表达式 {
    private Matcher matcher = null;
    private Pattern pattern = null;

    //创建一个正则表达式，参数一为正则表达式，参数二为字母大小写是否敏感，参数三为是否匹配多行
    public 正则表达式(String 表达式, boolean 敏感, boolean 多行) {
        if (敏感 && 多行) {
            pattern = Pattern.compile(表达式, 10);
        } else if (!敏感 && !多行) {
            pattern = Pattern.compile(表达式);
        } else if (敏感 && !多行) {
            pattern = Pattern.compile(表达式, 2);
        } else if (!敏感 && 多行) {
            pattern = Pattern.compile(表达式, 8);
        }
    }

    public 正则表达式(String 表达式) {
        pattern = Pattern.compile(表达式);
    }

    //进行正则匹配，参数一为原文本，参数二为正则表达式
    public static String[] 正则匹配(String 文本, String 表达式) {
        Matcher mr = Pattern.compile(表达式, 40).matcher(文本);
        List<String> list = new ArrayList<>();
        while (mr.find()) {
            list.add(mr.group());
        }
        return list.toArray(new String[list.size()]);
    }

    //进行正则匹配，参数一为原文本，参数二为正则表达式
    public static ArrayList<String> 正则匹配2(String 文本, String 表达式) {
        Matcher mr = Pattern.compile(表达式, 40).matcher(文本);
        ArrayList<String> list = new ArrayList<>();
        while (mr.find()) {
            list.add(mr.group());
        }
        return list;
    }


    //用正则表达式分割文本，参数为待分割的文本
    public String[] 全部分割(String 文本) {
        if (pattern != null) {
            return pattern.split(文本);
        }
        return new String[0];
    }

    //初始化要匹配文本，参数为待匹配的文本
    public void 开始匹配(String 文本) {
        if (pattern != null) {
            matcher = pattern.matcher(文本);
        }
    }

    //用正则匹配替换文本，参数为用来替换的文本
    public String 全部替换(String 文本) {
        if (matcher != null) {
            return matcher.replaceAll(文本);
        }
        return ("");
    }

    //匹配下一个文本
    public boolean 匹配下一个() {
        if (matcher != null) {
            return matcher.find();
        }
        return false;
    }

    //获取匹配到的文本
    public String 取匹配文本() {
        if (matcher != null) {
            return matcher.group();
        }
        return ("");
    }

    //获取匹配到的文本开始的位置
    public int 取匹配开始位置() {
        if (matcher != null) {
            return matcher.start();
        }
        return 0;
    }

    //获取匹配到的文本结束的位置
    public int 取匹配结束位置() {
        if (matcher != null) {
            return matcher.end();
        }
        return 0;
    }

    //获取匹配到的文本的数量
    public int 取子匹配数量() {
        if (matcher != null) {
            return matcher.groupCount();
        }
        return 0;
    }

    //获取匹配到的某一个文本，参数为匹配到的文本的索引
    public String 取子匹配文本(int 索引) {
        if (matcher != null) {
            return matcher.group(索引);
        }
        return "";
    }


}
