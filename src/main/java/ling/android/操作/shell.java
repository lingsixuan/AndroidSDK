package ling.android.操作;

import android.content.Context;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;

import ling.android.操作.共享数据;

import java.io.*;

public class shell {

    public static String 执行shell(String 命令) {
        //String result = "";
        StringBuilder result = new StringBuilder();
        //DataOutputStream dos = null;
        //DataInputStream dis = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        try {
            Process p = Runtime.getRuntime().exec(命令, null, null);
            /*dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());
            dos.writeBytes(命令 + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();*/
            //String line = null;
            p.waitFor();
            /*while ((line = dis.readLine()) != null) {
                result += line + "\n";
            }*/
            bufrIn = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));

            // 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufrIn != null) {
                try {
                    bufrIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufrError != null) {
                try {
                    bufrError.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            /*if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
        }
        return result.toString();

    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }

    public static String 执行命令(Context context, String 命令) {
        String result = "";
        DataOutputStream dos = null;
        DataInputStream dis = null;
        try {
            共享数据 data = new 共享数据(context, "配置");
            Process p = Runtime.getRuntime().exec(data.取文本("超级用户命令", "su"));// 经过Root处理的android系统即有su命令
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());
            dos.writeBytes(命令 + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;
            while ((line = dis.readLine()) != null) {
                result += line + "\n";
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
