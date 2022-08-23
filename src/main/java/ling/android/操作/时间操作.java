package ling.android.操作;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class 时间操作 {
    public static final int DATE_APRIL = 3;
    public static final int DATE_AUGUST = 7;
    public static final int DATE_DAY = 5;
    public static final int DATE_DECEMBER = 11;
    public static final int DATE_FEBRUARY = 1;
    public static final int DATE_FRIDAY = 6;
    public static final int DATE_HOUR = 11;
    public static final int DATE_JANUARY = 0;
    public static final int DATE_JULY = 6;
    public static final int DATE_JUNE = 5;
    public static final int DATE_MARCH = 2;
    public static final int DATE_MAY = 4;
    public static final int DATE_MINUTE = 12;
    public static final int DATE_MONDAY = 2;
    public static final int DATE_MONTH = 2;
    public static final int DATE_NOVEMBER = 10;
    public static final int DATE_OCTOBER = 9;
    public static final int DATE_SATURDAY = 7;
    public static final int DATE_SECOND = 13;
    public static final int DATE_SEPTEMBER = 8;
    public static final int DATE_SUNDAY = 1;
    public static final int DATE_THURSDAY = 5;
    public static final int DATE_TUESDAY = 3;
    public static final int DATE_WEDNESDAY = 4;
    public static final int DATE_WEEK = 3;
    public static final int DATE_YEAR = 1;

    //根据格式获取时间文本，年为y，月为M，日为d，时为H，分为m，秒为s，如：取格式时间("yyyy-MM-dd")，返回xxxx-xx-xx，对应年份-月份-日
    public String 取格式时间(String 格式) {
        return (new SimpleDateFormat(格式).format(new Date()));
    }

    //将时间戳转换为指定时间文本格式，年为y，月为M，日为d，时为H，分为m，秒为s，如：时间戳到文本(1239552759,"yyyy-MM-dd")，返回xxxx-xx-xx，对应年份-月份-日
    public static String 时间戳到文本(long 时间戳, String 时间格式文本) {
        return (new SimpleDateFormat(时间格式文本).format(时间戳));
    }

    //返回现行时间戳文本,单位为毫秒，也就是从1970年1月1日到现在的毫秒数
    public static long 取时间戳() {
        return new Date().getTime();
    }

    //返回现行时间戳文本，即1970年1月1日到现在的毫秒数，类型： 1、13位毫秒数  2、10位秒数。
    public static String 取时间戳文本() {
        return new SimpleDateFormat().format(new Date());
    }

    //将时间文本转为时间戳
    public static long 时间文本到时间戳(String 时间格式, String 时间文本) {
        SimpleDateFormat format = new SimpleDateFormat(时间格式);
        try {
            return format.parse(时间文本).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //将给定的时间戳转化为时间对象
    public static Calendar 到时间(String 时间戳) {
        Calendar date = new GregorianCalendar();
        try {
            date.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(时间戳));
        } catch (ParseException e) {
            try {
                date.setTime(new SimpleDateFormat("yyyy/MM/dd").parse(时间戳));
            } catch (ParseException e2) {
                throw new RuntimeException("到时间( 参数错误");
            }
        }
        return date;
    }

    //返回一个时间对象，这一时间被加上或减去了一段间隔。参数二 被增减部分 指定增加或减少时间的哪一部分 1、年份 2、月份 3、日 4、星期 5、小时 6、分钟 7、秒参数三 增加值 要增减的时间值，正数为增加，负数为减少。
    public static Calendar 增减时间(Calendar 时间对象, int 增减部分, int 时间值) {
        int kind = 0;
        switch (增减部分) {
            case 1:
                kind = 1;
                break;
            case 2:
                kind = 2;
                break;
            case 3:
                kind = 5;
                break;
            case 4:
                kind = 3;
                break;
            case 5:
                kind = 11;
                break;
            case 6:
                kind = 12;
                break;
            case 7:
                kind = 13;
                break;
        }
        ((Calendar) 时间对象).add(kind, 时间值);
        return 时间对象;
    }

    //返回一个值为 1 到 31 之间的整数，表示一个月中的某一日。
    public static int 取日(Calendar 时间对象) {
        return ((Calendar) 时间对象).get(5);
    }

    //将指定时间转换为文本。
    public static String 时间到文本(Calendar 时间对象) {
        return DateFormat.getDateTimeInstance(1, 1).format(((Calendar) 时间对象).getTime());
    }

    //返回一个值为 0 到 23 之间的整数，表示一天中的某一小时。
    public static int 取小时(Calendar 时间对象) {
        return ((Calendar) 时间对象).get(11);
    }

    //返回一个值为 0 到 59 之间的整数，表示一小时中的某一分钟。
    public static int 取分钟(Calendar 时间对象) {
        return ((Calendar) 时间对象).get(12);
    }

    //返回一个值为 1 到 12 之间的整数，表示指定时间中的月份。
    public static int 取月份(Calendar 时间对象) {
        return ((Calendar) 时间对象).get(2) + 1;
    }

    //返回当前月份的名称
    public static String 取月份名称(Calendar 时间对象) {
        return String.format("%1$tB", ((Calendar) 时间对象));
    }

    //返回当前系统日期及时间，返回一个时间对象。
    public static Calendar 取现行时间对象() {
        return new GregorianCalendar();
    }

    //返回一个值为 0 到 59 之间的整数，表示一分钟中的某一秒。
    public static int 取秒(Calendar 时间对象) {
        return ((Calendar) 时间对象).get(13);
    }

    //获取系统从启动开始运行到现在的总时长,单位为毫秒，也就是从1970年1月1日到现在的毫秒数
    public static long 取启动时间() {
        return System.currentTimeMillis();
    }

    //返回一个值为 1 到 7 之间的整数，表示指定时间中的星期几，注意：星期日为第一天。
    public static int 取星期几(Calendar 时间对象) {
        return ((Calendar) 时间对象).get(7);
    }

    //返回今天是星期几的名称
    public static String 取星期几名称(Calendar 时间对象) {
        return String.format("%1$tA", ((Calendar) 时间对象));
    }

    //返回一个值为 100 到 9999 之间的整数，表示指定时间中的年份。
    public static int 取年份(Calendar 时间对象) {
        return ((Calendar) 时间对象).get(1);
    }

    //获取当前系统时间，并以文本型返回。
    public static String 取现行时间文本(String 内容) {
        return new SimpleDateFormat("HH" + 内容 + "mm" + 内容 + "ss").format(new Date(System.currentTimeMillis()));
    }

    //获取当前系统日期，并以文本型返回。
    public static String 取现行日期文本(String 内容) {
        return new SimpleDateFormat("yyyy" + 内容 + "MM" + 内容 + "dd").format(new Date(System.currentTimeMillis()));
    }

    //获取两个时间对象之间的间隔,单位为毫秒。
    public static long 取时间间隔(Calendar 时间对象1, Calendar 时间对象2) {
        return ((Calendar) 时间对象1).getTimeInMillis() - ((Calendar) 时间对象2).getTimeInMillis();
    }

    //返回指定时间戳文本，即1970年1月1日到指定时间的毫秒数或秒数，类型： 1、13位毫秒数  2、10位秒数。
    public static String 取指定时间戳(Calendar 时间对象, int 类型) {
        String s = String.valueOf(((Calendar) 时间对象).getTimeInMillis());
        if (类型 == 2) {
            return s.substring(0, 10);
        }
        return s;
    }

    //将时间戳(1970年1月1日到指定时间的毫秒数)转换为格式化时间文本。
    public static String 时间戳到时间文本(String 时间戳) {
        if (时间戳 == null || 时间戳.equals("")) {
            return "";
        }
        Long stamp;
        if (时间戳.length() == 10) {
            stamp = Long.valueOf(Long.parseLong(时间戳) * 1000);
        } else if (时间戳.length() != 13) {
            return "";
        } else {
            stamp = Long.valueOf(Long.parseLong(时间戳));
        }
        String strs = "";
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(stamp.longValue()));
        } catch (Exception e) {
            e.printStackTrace();
            return strs;
        }
    }

}