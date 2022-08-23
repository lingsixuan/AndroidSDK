package ling.android.异常类;

import android.content.Context;
import android.widget.Toast;

public class 内存访问错误 extends Exception {
    public String 异常 = "";

    public 内存访问错误(String 异常) {
        super(异常);
        this.异常 = 异常;
    }

    public void print(Context 上下文对象) {
        Toast.makeText(上下文对象, this.异常, Toast.LENGTH_SHORT).show();
    }
}
