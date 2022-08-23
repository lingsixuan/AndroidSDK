package ling.android.安全;

import android.content.Context;
import ling.android.操作.*;

import java.util.ArrayList;

public class hook检测 {

    public static boolean 检测(Context context) {
        String 路径 = 应用操作.取APK路径(context);
        int 结尾 = 文本操作.倒找文本(路径, "/", 路径.length());
        String 合法路径 = 文本操作.取文本左边(路径, 结尾);

        String 内存分配表 = 文件操作.读入文本文件("/proc/self/maps");
        ArrayList<String> 集合 = 正则表达式.正则匹配2(内存分配表, "/data/app[^ \\[\\]\\n]*");
        for (int i = 0; i < 集合.size(); i++) {
            if (集合.get(i).indexOf(合法路径) != 0) {
                return true;
            }
        }
        集合 = 正则表达式.正则匹配2(内存分配表, "/data/dalvik-cache/arm/data[\\[\\]\\n]*");
        for (int i = 0; i < 集合.size(); i++) {
            if (集合.get(i).contains(context.getPackageName()) == false) {
                return true;
            }
        }
        return false;
    }
}
