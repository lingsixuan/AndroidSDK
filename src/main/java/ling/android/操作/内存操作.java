package ling.android.操作;

import android.widget.Switch;
import ling.android.异常类.内存访问错误;
import ling.android.异常类.打开文件错误;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Array;
import java.util.ArrayList;

public class 内存操作 {
    /**
     * 数据类型
     */
    public static final int AUTO = 127;
    public static final int BYTE = 1;
    public static final int WORD = 2;
    public static final int DWORD = 4;
    //public static final int XOR = 8;
    public static final int FLOAT = 16;
    public static final int QWORD = 32;
    public static final int DOUBLE = 64;

    private long xa;
    private String so;

    RandomAccessFile mem;

    public 内存操作(String so名称) throws 内存访问错误 {
        xa = 取xa首地址(so名称);
        so = so名称;
        try {
            mem = new RandomAccessFile(new File("/proc/self/mem"), "rw");
        } catch (FileNotFoundException e) {
            throw new 内存访问错误("无法访问内存映射表");
        }
    }

    public void 写入数据(long 指针, double 数据, int 类型) throws 内存访问错误 {
        switch (类型) {
            case BYTE:
                置BYTE(指针, (byte) 数据);
                break;
            case WORD:
                置WORD(指针, (short) 数据);
                break;
            case DWORD:
                置DWORD(指针, (int) 数据);
                break;
            case FLOAT:
                置FLOAT(指针, (float) 数据);
                break;
            case QWORD:
                置QWORD(指针, (long) 数据);
                break;
            case DOUBLE:
                置DOUBLE(指针, 数据);
                break;
            default:
                throw new 内存访问错误("未知的数据类型");
        }
    }

    public double 读取数据(long 指针, int 类型) throws 内存访问错误 {
        switch (类型) {
            case BYTE:
                return 取BYTE(指针);
            case WORD:
                return 取WORD(指针);
            case DWORD:
                return 取DWORD(指针);
            case FLOAT:
                return 取FLOAT(指针);
            case QWORD:
                return 取QWORD(指针);
            case DOUBLE:
                return 取DOUBLE(指针);
            default:
                throw new 内存访问错误("未知的数据类型");
        }
    }

    private double 取DOUBLE(long 指针) throws 内存访问错误 {
        return Double.longBitsToDouble(取QWORD(指针));
    }

    private float 取FLOAT(long 指针) throws 内存访问错误 {
        return Float.intBitsToFloat(取DWORD(指针));
    }

    private short 取WORD(long 指针) throws 内存访问错误 {
        try {
            mem.seek(指针);
            int ch1 = mem.read();
            int ch2 = mem.read();
            return (short) ((ch2 << 8) + (ch1 << 0));
        } catch (IOException e) {
            throw new 内存访问错误("读取数据失败");
        }

    }

    private byte 取BYTE(long 指针) throws 内存访问错误 {
        try {
            mem.seek(指针);
            return mem.readByte();
        } catch (IOException e) {
            throw new 内存访问错误("读取数据失败");
        }
    }

    private long 取QWORD(long 指针) throws 内存访问错误 {
        try {
            mem.seek(指针);
            long ch1 = mem.read();
            long ch2 = mem.read();
            long ch3 = mem.read();
            long ch4 = mem.read();
            long ch5 = mem.read();
            long ch6 = mem.read();
            long ch7 = mem.read();
            long ch8 = mem.read();
            if ((ch1 | ch2 | ch3 | ch4 | ch5 | ch6 | ch7 | ch8) < 0)
                throw new 内存访问错误("指针越界");
            return ((ch8 << 56) + (ch7 << 48) + (ch6 << 40) + (ch5 << 32) + (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
        } catch (IOException e) {
            throw new 内存访问错误("读取数据失败");
        }
    }

    private int 取DWORD(long 指针) throws 内存访问错误 {
        try {
            mem.seek(指针);
            int ch1 = mem.read();
            int ch2 = mem.read();
            int ch3 = mem.read();
            int ch4 = mem.read();
            if ((ch1 | ch2 | ch3 | ch4) < 0)
                throw new 内存访问错误("指针越界");
            return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
        } catch (IOException e) {
            throw new 内存访问错误("读取数据失败");
        }
    }

    private void 置DWORD(long 指针, int 数值) throws 内存访问错误 {
        try {
            mem.seek(指针);
            mem.write((数值 >>> 0) & 0xFF);
            mem.write((数值 >>> 8) & 0xFF);
            mem.write((数值 >>> 16) & 0xFF);
            mem.write((数值 >>> 24) & 0xFF);
        } catch (IOException e) {
            throw new 内存访问错误("写入数据失败");
        }
    }

    private void 置WORD(long 指针, int 数值) throws 内存访问错误 {
        try {
            mem.seek(指针);
            mem.write((数值 >>> 0) & 0xFF);
            mem.write((数值 >>> 8) & 0xFF);
        } catch (IOException e) {
            throw new 内存访问错误("写入数据失败");
        }
    }

    private void 置FLOAT(long 指针, float 数值) throws 内存访问错误 {
        置DWORD(指针, Float.floatToIntBits(数值));
    }

    private void 置QWORD(long 指针, long 数值) throws 内存访问错误 {
        try {
            mem.seek(指针);
            mem.write((byte) (数值 >>> 0) & 0xFF);
            mem.write((byte) (数值 >>> 8) & 0xFF);
            mem.write((byte) (数值 >>> 16) & 0xFF);
            mem.write((byte) (数值 >>> 24) & 0xFF);
            mem.write((byte) (数值 >>> 32) & 0xFF);
            mem.write((byte) (数值 >>> 40) & 0xFF);
            mem.write((byte) (数值 >>> 48) & 0xFF);
            mem.write((byte) (数值 >>> 56) & 0xFF);
        } catch (IOException e) {
            throw new 内存访问错误("写入数据失败");
        }
    }

    private void 置DOUBLE(long 指针, double 数值) throws 内存访问错误 {
        置QWORD(指针, Double.doubleToLongBits(数值));
    }

    private void 置BYTE(long 指针, byte 数值) throws 内存访问错误 {
        try {
            mem.seek(指针);
            mem.write(数值);
        } catch (IOException e) {
            throw new 内存访问错误("写入数据失败");
        }
    }

    public long 取xa首地址() {
        return xa;
    }

    public static long 取xa首地址(String so名称) throws 内存访问错误 {
        try {
            文件操作 内存分配表 = new 文件操作("/proc/self/maps");
            内存分配表.打开文本文件_读();
            String line;
            while ((line = 内存分配表.读一行()) != null) {
                if (line.indexOf(so名称, 0) >= 0) {
                    int i = line.indexOf("-", 0);
                    String hix = i <= line.length() ? line.substring(0, i) : line;
                    return Long.valueOf(hix, 16);
                }
            }
            return 0x0;
        } catch (IOException e) {
            throw new 内存访问错误("寻找xa首地址失败");
        } catch (打开文件错误 e) {
            throw new 内存访问错误("无法访问内存分配表");
        }
    }
}
