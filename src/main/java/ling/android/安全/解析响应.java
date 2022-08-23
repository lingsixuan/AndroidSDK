package ling.android.安全;

import ling.android.异常类.签名损坏;
import ling.android.操作.加解密操作;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class 解析响应 {
    public static int 允许误差 = 15;

    public static JSONObject 解析(String 盐值, String 原始数据) throws 签名损坏 {
        try {
            JSONObject JSON对象 = new JSONObject(原始数据);
            String 数据签名 = JSON对象.getString("MD5");
            String 实际签名 = 加解密操作.MD5加密(JSON对象.getString("data") + 盐值);
            if (实际签名 == null)
                throw new 签名损坏("计算签名失败");
            if (!实际签名.equals(数据签名))
                throw new 签名损坏("签名损坏");
            JSON对象 = new JSONObject(JSON对象.getString("data"));
            long 响应时间 = JSON对象.getLong("时间");
            long 当前时间 = new Date().getTime() / 1000;
            if (当前时间 - 响应时间 < -允许误差 || 当前时间 - 响应时间 > 允许误差)
                throw new 签名损坏("数据包过期");
            return JSON对象;
        } catch (JSONException e) {
            throw new 签名损坏("回执数据无法解析");
        }
    }
}
