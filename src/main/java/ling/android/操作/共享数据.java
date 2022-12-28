package ling.android.操作;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class 共享数据 {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;


    //初始化共享数据，在使用时必须先初始化，否则会报错，参数为储存名称，可随意
    @SuppressLint("CommitPrefEdits")
    public 共享数据(Context 窗口, String 名称) {
        this.sp = 窗口.getSharedPreferences(名称, Context.MODE_PRIVATE);
        editor = this.sp.edit();
    }

    //判断共享数据是否包含某个数据
    public boolean 是否包含数据(String 键) {
        return this.sp.contains(键);
    }

    //移除共享数据中的某个数据
    public boolean 移除数据(String 键) {
        this.editor.remove(键);
        return (this.editor.commit());
    }

    //清除共享数据中的所有数据
    public boolean 清除数据() {
        this.editor.clear();
        return (this.editor.commit());
    }

    //取出之前设置的值，参数为值的名称，获取失败则返回默认值
    public String 取文本(String 键, String 默认值) {
        return this.sp.getString(键, 默认值);
    }

    //取出之前设置的值，参数为值的名称，获取失败则返回""
    public String 取文本(String 键) {
        return this.sp.getString(键, "");
    }

    //将指定名称和值的数据写入私有目录
    public boolean 置文本(String 键, String 值) {
        this.editor.putString(键, 值);
        return this.editor.commit();
    }

    //将指定名称和值的数据写入私有目录
    public boolean 置整数(String 键, int 值) {
        this.editor.putInt(键, 值);
        return this.editor.commit();
    }

    //取出之前设置的值，参数为值的名称，获取失败则返回默认值
    public int 取整数(String 键, int 默认值) {
        return this.sp.getInt(键, 默认值);
    }

    //取出之前设置的值，参数为值的名称，获取失败则返回0
    public int 取整数(String 键) {
        return this.sp.getInt(键, 0);
    }

    //将指定名称和值的数据写入私有目录
    public boolean 置长整数(String 键, long 值) {
        this.editor.putLong(键, 值);
        return this.editor.commit();
    }

    //取出之前设置的值，参数为值的名称，获取失败则返回默认值
    public long 取长整数(String 键, long 默认值) {
        return this.sp.getLong(键, 默认值);
    }

    //取出之前设置的值，参数为值的名称，获取失败则返回0
    public long 取长整数(String 键) {
        return this.sp.getLong(键, 0);
    }

    //将指定名称和值的数据写入私有目录
    public boolean 置浮点数(String 键, float 值) {
        this.editor.putFloat(键, 值);
        return this.editor.commit();
    }

    //取出之前设置的值，参数为值的名称，获取失败则返回默认值
    public float 取浮点数(String 键, float 默认值) {
        return this.sp.getFloat(键, 默认值);
    }

    //取出之前设置的值，参数为值的名称，获取失败则返回0f
    public float 取浮点数(String 键) {
        return this.sp.getFloat(键, 0f);
    }

    //将指定名称和值的数据写入私有目录
    public boolean 置逻辑(String 键, boolean 值) {
        this.editor.putBoolean(键, 值);
        return this.editor.commit();
    }

    //取出之前设置的值，参数为值的名称，获取失败则返回默认值
    public boolean 取逻辑(String 键, boolean 默认值) {
        return this.sp.getBoolean(键, 默认值);
    }

    //取出之前设置的值，参数为值的名称，获取失败则返回假
    public boolean 取逻辑(String 键) {
        return this.sp.getBoolean(键, false);
    }

}
