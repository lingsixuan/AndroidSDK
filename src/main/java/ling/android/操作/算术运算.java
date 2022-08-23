package ling.android.操作;

import java.math.BigDecimal;
import java.util.Stack;

public class 算术运算 {

    private static double E = 2.718281828459045d;
    private static double PI = 3.141592653589793d;

    //求一个数的反正切值
    public static double 求反正切(double 值) {
        return Math.atan(值);
    }

    //求一个数的余弦值
    public static double 求余弦(double 值) {
        return Math.cos(值);
    }

    //求一个数的反对数
    public static double 求反对数(double 值) {
        return Math.exp(值);
    }

    //求一个数的自然对数
    public static double 求自然对数(double 值) {
        return Math.log(值);
    }

    //获取一个随机数字，参数指定一个范围，参数一为随机数字最小值，参数二为随机数字最大值
    public static int 取随机数(int 最小值, int 最大值) {
        return (int) (Math.random() * (最大值 + 1 - 最小值) + 最小值);
    }

    //求一个数的正切值
    public static double 求正弦(double 值) {
        return Math.sin(值);
    }

    //获取数的符号，如果参数小于0，则返回-1.0。 如果参数大于零，则返回1.0；如果参数为正零或负零，则将其作为结果返回。
    public static int 取符号(double 值) {
        return (int) Math.signum(值);
    }

    //求一个数的平方根
    public static double 求平方根(double 值) {
        return Math.sqrt(值);
    }

    //求一个数的正切值
    public static double 求正切(double 值) {
        return Math.tan(值);
    }

    //将角度值转化为弧度值
    public static double 角度转弧度(double 值) {
        return Math.toRadians(值);
    }

    //将弧度值转化为角度值
    public static double 弧度转角度(double 值) {
        return Math.toDegrees(值);
    }

    //将一个数四舍五入，参数一为将要四舍五人的数字，参数二为小数点后几位
    public static double 四舍五入(double 数字, int 精确度) {
        return new BigDecimal(String.valueOf(数字)).setScale(精确度, 4).doubleValue();
    }

    //求一个数的反正弦值
    public static double 求反正弦(double 值) {
        return Math.asin(值);
    }

    //求一个数的反余弦值
    public static double 求反余弦(double 值) {
        return Math.acos(值);
    }

    //求计算表达式计算结果
    public static double 表达式计算(String 表达式) {
        double num[] = new double[20];
        int flag = 0, begin = 0, end = 0, now;
        now = -1;
        Stack<Character> st = new Stack<Character>();
        for (int i = 0; i < 表达式.length(); i++) {
            char s = 表达式.charAt(i);
            if (s == ' ') {

            } else if (s == '+' || s == '-' || s == '*' || s == '/' || s == '(' || s == ')' || s == '%') {
                if (flag == 1) {
                    now += 1;
                    if (end < begin) {
                        num[now] = Integer.valueOf(表达式.substring(begin, begin + 1));
                    } else {
                        num[now] = Integer.valueOf(表达式.substring(begin, end + 1));
                    }
                    flag = 0;
                }
                if (s == '-') {
                    if (i == 0) {
                        flag = 1;
                        begin = 0;
                    } else if (表达式.charAt(i - 1) == '(' || 表达式.charAt(i - 1) == '*'
                            || 表达式.charAt(i - 1) == '/') {
                        flag = 1;
                        begin = i;
                    } else {
                        if (st.empty()) {
                            st.push(s);
                        } else if (s == ')') {
                            num[now - 1] = compute(num[now - 1], num[now], st.pop());
                            now -= 1;
                            st.pop();
                        } else if (s == '(') {
                            st.push(s);
                        } else if (priority(s) <= priority(st.peek())) {
                            num[now - 1] = compute(num[now - 1], num[now], st.pop());
                            now -= 1;
                            st.push(s);
                        } else {
                            st.push(s);
                        }
                    }
                } else if (st.empty()) {
                    st.push(s);
                } else if (s == ')') {
                    num[now - 1] = compute(num[now - 1], num[now], st.pop());
                    now -= 1;
                    st.pop();
                } else if (s == '(') {
                    st.push(s);
                } else if (priority(s) <= priority(st.peek())) {
                    num[now - 1] = compute(num[now - 1], num[now], st.pop());
                    now -= 1;
                    st.push(s);
                } else {
                    st.push(s);
                }

            } else if (flag == 0) {
                flag = 1;
                begin = i;
            } else {
                end = i;
            }

        }
        if (flag == 1) {
            now += 1;
            if (end < begin) {
                num[now] = Integer.valueOf(表达式.substring(begin, begin + 1));
            } else {
                num[now] = Integer.valueOf(表达式.substring(begin, end + 1));
            }
        }
        while (now > 0) {
            num[now - 1] = compute(num[now - 1], num[now], st.pop());
            now -= 1;
        }
        return num[0];
    }

    private static int priority(char s) {
        switch (s) {
            case '(':
            case ')':
                return 0;
            case '-':
            case '+':
                return 1;
            case '*':
            case '%':
            case '/':
                return 2;
            default:
                return -1;

        }
    }

    private static double compute(double num1, double num2, char s) {
        switch (s) {
            case '(':
            case ')':
                return 0;
            case '-':
                return num1 - num2;
            case '+':
                return num1 + num2;
            case '%':
                return num1 % num2;
            case '*':
                return num1 * num2;
            case '/':
                return num1 / num2;
            default:
                return 0;

        }
    }
}