package ling.android.操作;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class 编码操作 {

    //将普通文本转换成URL文本,默认utf-8编码
    public static String URL编码(String 值) {
        return URLEncoder.encode(值);
    }

    //将URL文本转换成普通文本,默认utf-8编码
    public static String URL解码(String 值) {
        return URLDecoder.decode(值);
    }

    //将普通文本转换成URL编码，参数二 编码类型 为转换后的编码类型，例如："UTF-8"或"GBK"等
    public static String URL编码(String 值, String 编码) {
        try {
            return URLEncoder.encode(值, 编码);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    //将URL文本转换成普通文本，参数二 编码类型 为转换后的编码类型，例如："UTF-8"或"GBK"等
    public static String URL解码(String 值, String 编码) {
        try {
            return URLDecoder.decode(值, 编码);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    //将文本转换成新的编码格式
    public static String 转换编码(String 待转换文本, String 原编码, String 新编码) {
        if (待转换文本 == null) {
            return "";
        }
        try {
            return new String(待转换文本.getBytes(原编码), 新编码);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    //将ANSI文本转换成UCS2编码的文本，例如：编码操作.UCS2编码("结绳，你好！")
    public static String UCS2编码(String 值) {
        String str = "";
        for (int i = 0; i < 值.length(); i++) {
            String temp = Integer.toHexString(值.charAt(i) & 65535);
            if (temp.length() == 2) {
                temp = "00" + temp;
            }
            str = str + "\\u" + temp;
        }
        return str;
    }

    //将UCS2编码的文本转换成ANSI文本，例如：编码操作.UCS2解码("\u7ed3\u7ef3\uff0c\u4f60\u597d\uff01")
    public static String UCS2解码(String 值) {
        int len = 值.length();
        StringBuffer outBuffer = new StringBuffer(len);
        int x = 0;
        while (x < len) {
            int x2 = x + 1;
            char aChar = 值.charAt(x);
            if (aChar == '\\') {
                x = x2 + 1;
                aChar = 值.charAt(x2);
                if (aChar == 'u') {
                    int value = 0;
                    int i = 0;
                    while (i < 4) {
                        x2 = x + 1;
                        aChar = 值.charAt(x);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = ((value << 4) + aChar) - 48;
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (((value << 4) + 10) + aChar) - 65;
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (((value << 4) + 10) + aChar) - 97;
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                        i++;
                        x = x2;
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
                x = x2;
            }
        }
        return outBuffer.toString();
    }

}