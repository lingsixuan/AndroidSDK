package ling.android.操作;

public class 文本操作 {
    public static String 取文本左边(String 文本, int 长度) {
        if ("".equals(文本) || 长度 <= 0) {
            return "";
        } else {
            return 长度 <= 文本.length() ? 文本.substring(0, 长度) : 文本;
        }
    }

    public static String 取文本右边(String 文本, int 长度) {
        if ("".equals(文本) || 长度 <= 0) {
            return "";
        } else {
            return 长度 <= 文本.length() ? 文本.substring(文本.length() - 长度, 文本.length()) : 文本;
        }
    }

    public static int 倒找文本(String 文本, String 寻找内容, int 开始位置) {
        if (开始位置 < 0 || 开始位置 > 文本.length() || "".equals(文本) || "".equals(寻找内容))
            return -1;
        return 文本.lastIndexOf(寻找内容, 开始位置);
    }

    public static int 寻找文本(String 文本, String 寻找内容, int 开始位置) {
        if (开始位置 < 0 || 开始位置 > 文本.length() || "".equals(文本) || "".equals(寻找内容))
            return -1;
        return 文本.indexOf(寻找内容, 开始位置);
    }


    //将英文字母全部转化为大写，参数为待转换文本
    public static String 到大写(String 文本) {
        return 文本.toUpperCase();
    }

    //将英文字母全部转化为小写，参数为待转换文本
    public static String 到小写(String 文本) {
        return 文本.toLowerCase();
    }

    //取某一位置的字符，参数一为从中获取的文本，参数二为要获取的字符位置
    public static char 取指定字符(String 文本, int 位置) {
        return 文本.charAt(位置);
    }


    //取文本中间一段内容，参数一为待取文本，参数二为开始位置，参数三为要截取的长度
    public static String 取文本中间(String 文本, int 开始位置, int 长度) {
        if ("".equals(文本) || 开始位置 < 0 || 长度 <= 0 || 开始位置 > 文本.length()) {
            return "";
        }
        int end = 开始位置 + 长度;
        if (end > 文本.length()) {
            end = 文本.length();
        }
        return 文本.substring(开始位置, end);
    }

    //取一段文本的长度，参数为要取长度的文本
    public static int 取文本长度(String 文本) {
        return 文本.length();
    }

    //取一段文本的字节长度，参数为要取长度的文本
    public static int 取文本长度2(String 文本) {
        return 文本.getBytes().length;
    }

    //删除文本首尾处空字符，参数为待处理的文本
    public static String 删首尾空(String 文本) {
        return 文本.trim();
    }

    //删除文本前面空字符，参数为待处理的文本
    public static String 删首空(String 文本) {
        char[] chars = 文本.toCharArray();
        int count = 0;
        int i = 0;
        while (i < chars.length && chars[i] == ' ') {
            i++;
            count++;
        }
        String s = 文本;
        if (count > 0) {
            return new String(chars, count, chars.length - count);
        }
        return s;
    }

    //删除文本后面空字符，参数为待处理的文本
    public static String 删尾空(String 文本) {
        char[] chars = 文本.toCharArray();
        int count = 0;
        int i = chars.length - 1;
        while (i > 0 && chars[i] == ' ') {
            i--;
            count++;
        }
        String s = 文本;
        if (count > 0) {
            return new String(chars, 0, chars.length - count);
        }
        return s;
    }

    //替换一段文本，参数一为文本，参数二为将要被替换文本，参数三为来替换文本
    public static String 子文本替换(String 文本, String 需替文本, String 替换文本) {
        if ("".equals(需替文本) || "".equals(文本)) {
            return "";
        }
        return 文本.replaceAll("\\Q" + 需替文本 + "\\E", 替换文本);
    }

    //替换一段文本，参数一为要操作的文本，参数二为开始位置，参数三为结束位置，参数四为替换文本
    public static String 子文本替换(String 文本, int 开始位置, int 结束位置, String 替换文本) {
        if ("".equals(文本) || 开始位置 < 0 || 开始位置 > 文本.length() || 结束位置 < 开始位置 || 结束位置 > 文本.length()) {
            return "";
        }
        return 文本.substring(0, 开始位置) + 替换文本 + 文本.substring(结束位置 + 1);
    }

    //比较两段文本有多少不同，参数一为一段文本，参数二为另一段文本
    public static int 文本比较(String 文本一, String 文本二) {
        return 文本一.compareTo(文本二);
    }

    //将文本翻转排序(倒置)，参数为将要改变顺序文本
    public static String 翻转文本(String 文本) {
        return new StringBuffer(文本).reverse().toString();
    }

    //分割一段文本，参数一为要分割的文本，参数二为作为分割符号的文本
    public static String[] 分割文本(String 文本, String 分割符) {
        if ("".equals(分割符) || "".equals(文本)) {
            return new String[0];
        }
        if (分割符.equals("\n")) {
            文本 = 子文本替换(文本, "\r", "");
        }
        if (取文本右边(文本, 取文本长度(分割符)).equals(分割符)) {
            return 取指定文本(分割符 + 文本, 分割符, 分割符);
        }
        return 取指定文本(分割符 + 文本 + 分割符, 分割符, 分割符);
    }

    //取一段文本，参数一为被取文本，参数二为开始文本，参数三为结束文本
    public static String[] 取指定文本(String 文本, String 开始文本, String 结束文本) {
        if ("".equals(文本) || "".equals(开始文本) || "".equals(结束文本)) {
            return new String[0];
        }
        return 正则表达式.正则匹配(文本, "(?<=\\Q" + 开始文本 + "\\E).*?(?=\\Q" + 结束文本 + "\\E)");
    }

    //截取指定文本，参数一为将被截取文本，参数二为开始文本，参数三为结束文本
    public static String 截取文本(String 文本, String 开始文本, String 结束文本) {
        String[] temp = 取指定文本(文本, 开始文本, 结束文本);
        if (temp.length > 0) {
            return temp[0];
        }
        return "";
    }
}
