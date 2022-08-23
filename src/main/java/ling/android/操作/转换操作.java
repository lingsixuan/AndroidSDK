package ling.android.操作;

import android.graphics.Color;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 转换操作 {

    //将毫秒时长转换为时分秒，一般用于转换音乐时长或视频时长
    public static String 转换时长(int 时长) {
        if (时长 / 1000 % 60 < 10)
            return 时长 / 1000 / 60 + ":0" + 时长 / 1000 % 60;
        else
            return 时长 / 1000 / 60 + ":" + 时长 / 1000 % 60;
    }

    //将文件字节长度转换为MB（从B到MB）
    public static String 转换文件大小(int 大小) {
        String 大小2 = 到文本(大小) + "B";
        if (大小 > 1024 * 1024)
            大小2 = String.format("%.2f MB", Long.valueOf(大小).doubleValue() / (1024 * 1024));
        return (大小2);
    }

    public static String 取小数点后两位(double 数据) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String money = df.format(数据);
        return money;
    }

    public static String 到文本(Object 值) {
        if (值 instanceof String)
            return (String) 值;
        else if (值 instanceof Throwable) {
            Throwable th = (Throwable) 值;
            java.io.StringWriter write = new java.io.StringWriter();
            java.io.PrintWriter printWriter = new java.io.PrintWriter(write);
            th.printStackTrace(printWriter);
            return write.toString();
        } else if (值.getClass().isArray()) {
            return java.util.Arrays.deepToString((Object[]) 值);
        } else {
            return String.valueOf(值);
        }
    }

    public static String 到文本(char... 值) {
        return new String(值);
    }


    //将文本型颜色值转换为整数型颜色值
    public static int 转换颜色(String 颜色值) {
        return Color.parseColor(颜色值);
    }

    //将字符转为文本型数据
    public static int 字符转代码(String 字符) {
        try {
            return 字符.charAt(0);
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("字符转代码( 字符长度应该大于等于1");
        }
    }

    //将代码转为字符
    public static String 代码转字符(int 代码) {
        return Character.toString((char) 代码);
    }


    public static int 到整数(Object 值) {
        if (值 instanceof String) {
            return Integer.parseInt((String) 值);
        } else if (值 instanceof Double) {
            return (int) (double) 值;
        } else if (值 instanceof Float) {
            return (int) (float) 值;
        } else {
            throw new RuntimeException("到整数错误，无法将对应值转换为整数");
        }
    }

    public static double 到数值(String 值) {
        try {
            return Double.parseDouble(值);
        } catch (Exception e) {
            throw new RuntimeException("到数值错误，无法将对应值转换为小数");
        }
    }

    public static long 到长整数(Object 值) {
        if (值 instanceof String) {
            return Long.parseLong((String) 值);
        } else if (值 instanceof Double) {
            return (long) (double) 值;
        } else if (值 instanceof Float) {
            return (long) (float) 值;
        } else {
            throw new RuntimeException("到长整数错误，无法将对应值转换为长整数");
        }
    }

    //将普通中文转为unicode编码
    public static String 中文转unicode(String 值) {
        char[] utfBytes = 值.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    //将unicode编码转为普通中文
    public static String unicode转中文(String 值) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(值);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            值 = 值.replace(matcher.group(1), ch + "");
        }
        return 值;
    }

    //将整数转为十六进制文本
    public static String 到十六进制(String 值) {
        String 文本 = Integer.toHexString(到整数(值));
        if (文本.length() < 2)
            文本 = "0" + 文本;
        return 文本;
    }

    //将16进制文本转换成10进制数值
    public static int 到十进制(String 值) {
        if (!"".equals(值))
            return Integer.valueOf(值, 16).intValue();
        return 0;
    }

    //将10进制数值转换成2进制文本
    public static String 到二进制(int 值) {
        return Integer.toBinaryString(值);
    }

    //将文本型字符转换成2进制文本
    public static String 文本到二进制(String 值) {
        char[] strChar = 值.toCharArray();
        String result = "";
        for (int i = 0; i < strChar.length; i++) {
            result = result + Integer.toBinaryString(strChar[i]) + " ";
        }
        return result;
    }

    //将字节型数组转为文本
    public static String 字节到文本(byte[] 值) {
        return new String(值);
    }

    //将字节型数组转为文本
    public static String 字节到文本(byte[] 值, String 编码) {
        try {
            return new String(值, 编码);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //将文本转为字节型数组
    public static byte[] 文本到字节(String 值) {
        return 值.getBytes();
    }

    //将文本转为字节型数组
    public static byte[] 文本到字节(String 值, String 编码) {
        try {
            return 值.getBytes(编码);
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("文本到字节( 解码错误");
        }
    }

    //将字节型数组转换成整数
    public static int 字节到整数(byte[] 值) {
        int targets = 值[0] & 0xFF | 值[1] << 8 & 0xFF00 | 值[2] << 24 >>> 8 | 值[3] << 24;
        return targets;
    }

    //将整数转换成字节型数组
    public static byte[] 整数到字节(int 值) {
        byte[] targets = new byte[4];
        targets[0] = ((byte) (值 & 0xFF));
        targets[1] = ((byte) (值 >> 8 & 0xFF));
        targets[2] = ((byte) (值 >> 16 & 0xFF));
        targets[3] = ((byte) (值 >>> 24));
        return targets;
    }

    //将字节型数组转换成长整数
    public static long 字节到长整数(byte[] 值) {
        return (值[0] & 0xFF) << 56 | (值[1] & 0xFF) << 48 | (值[2] & 0xFF) << 40 | (值[3] & 0xFF) << 32 | (值[4] & 0xFF) << 24 | (值[5] & 0xFF) << 16 | (值[6] & 0xFF) << 8 | (值[7] & 0xFF) << 0;
    }

    //将长整数转换成字节型数组
    public static byte[] 长整数到字节(long 值) {
        byte[] bb = new byte[8];
        bb[0] = ((byte) (int) (值 >> 56));
        bb[1] = ((byte) (int) (值 >> 48));
        bb[2] = ((byte) (int) (值 >> 40));
        bb[3] = ((byte) (int) (值 >> 32));
        bb[4] = ((byte) (int) (值 >> 24));
        bb[5] = ((byte) (int) (值 >> 16));
        bb[6] = ((byte) (int) (值 >> 8));
        bb[7] = ((byte) (int) 值);
        return bb;
    }

    //将10进制数值转换成大写汉字的人民币金额文本
    public static String 数值到金额(double 值) {
        if ((值 > 1.0E+018D) || (值 < -1.0E+018D)) {
            return "";
        }
        String[] chineseDigits = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        boolean negative = false;
        if (值 < 0.0D) {
            negative = true;
            值 *= -1.0D;
        }
        long temp = Math.round(值 * 100.0D);
        int numFen = (int) (temp % 10L);
        temp /= 10L;
        int numJiao = (int) (temp % 10L);
        temp /= 10L;
        int[] parts = new int[20];
        int numParts = 0;
        for (
                int i = 0;
                temp != 0L; i++) {
            int part = (int) (temp % 10000L);
            parts[i] = part;
            numParts++;
            temp /= 10000L;
        }
        boolean beforeWanIsZero = true;
        String chineseStr = "";
        for (int i = 0; i < numParts; i++) {
            String partChinese = partTranslate(parts[i]);
            if (i % 2 == 0) {
                if ("".equals(partChinese))
                    beforeWanIsZero = true;
                else {
                    beforeWanIsZero = false;
                }
            }
            if (i != 0) {
                if (i % 2 == 0) {
                    chineseStr = "亿" + chineseStr;
                } else if (("".equals(partChinese)) && (!beforeWanIsZero)) {
                    chineseStr = "零" + chineseStr;
                } else {
                    if ((parts[(i - 1)] < 1000) && (parts[(i - 1)] > 0)) {
                        chineseStr = "零" + chineseStr;
                    }
                    chineseStr = "万" + chineseStr;
                }
            }

            chineseStr = partChinese + chineseStr;
        }
        if ("".equals(chineseStr))
            chineseStr = chineseDigits[0];
        else if (negative) {
            chineseStr = "负" + chineseStr;
        }
        chineseStr = chineseStr + "元";
        if ((numFen == 0) && (numJiao == 0))
            chineseStr = chineseStr + "整";
        else if (numFen == 0)
            chineseStr = chineseStr + chineseDigits[numJiao] + "角";
        else if (numJiao == 0)
            chineseStr = chineseStr + "零" + chineseDigits[numFen] + "分";
        else {
            chineseStr = chineseStr + chineseDigits[numJiao] + "角" + chineseDigits[numFen] + "分";
        }

        return chineseStr;
    }

    //将字节集(字节型数组)转换成16进制文本
    public static String 字节集到十六进制(byte[] 值) {
        byte[] hex = "0123456789ABCDEF".getBytes();
        byte[] buff = new byte[2 * 值.length];
        for (int i = 0; i < 值.length; i++) {
            buff[(2 * i)] = hex[(值[i] >> 4 & 0xF)];
            buff[(2 * i + 1)] = hex[(值[i] & 0xF)];
        }
        return new String(buff);
    }

    //将16进制文本转换成字节集(字节型数组)
    public static byte[] 十六进制到字节集(String 值) {
        byte[] b = new byte[值.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = 值.charAt(j++);
            char c1 = 值.charAt(j++);
            b[i] = ((byte) (parse(c0) << 4 | parse(c1)));
        }
        return b;
    }

    private static String partTranslate(int amountPart) {
        if ((amountPart < 0) || (amountPart > 10000)) {
            return "";
        }
        String[] chineseDigits = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String[] units = {"", "拾", "佰", "仟"};
        int temp = amountPart;
        String amountStr = new Integer(amountPart).toString();
        int amountStrLength = amountStr.length();
        boolean lastIsZero = true;
        String chineseStr = "";
        for (
                int i = 0;
                (i < amountStrLength) && (temp != 0); i++) {
            int digit = temp % 10;
            if (digit == 0) {
                if (!lastIsZero) {
                    chineseStr = "零" + chineseStr;
                }
                lastIsZero = true;
            } else {
                chineseStr = chineseDigits[digit] + units[i] + chineseStr;
                lastIsZero = false;
            }
            temp /= 10;
        }
        return chineseStr;
    }

    private static int parse(char c) {
        if (c >= 'a') {
            return c - 'a' + 10 & 0xF;
        }
        if (c >= 'A') {
            return c - 'A' + 10 & 0xF;
        }
        return c - '0' & 0xF;
    }
}