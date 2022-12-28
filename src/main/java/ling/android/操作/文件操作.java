package ling.android.操作;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import ling.android.异常类.打开文件错误;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;

public class 文件操作 {
    private String 路径 = "";
    private String 编码 = "utf-8";
    private FileInputStream fin;
    private InputStreamReader isr;
    private BufferedReader br;
    private String line;
    private FileOutputStream fout;
    private OutputStreamWriter osw;
    private BufferedWriter bw;

    public static final int 文件排序_时间排序 = 0;
    public static final int 文件排序_名称排序 = 1;
    public static final int 文件排序_大小排序 = 2;

    public 文件操作(String 路径) {
        this.路径 = 路径;
    }

    public 文件操作(String 路径, String 编码) {
        this.路径 = 路径;
        this.编码 = 编码;
    }


    public static String 取文件编码(String 路径) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(路径)));
            in.mark(4);
            byte[] first3bytes = new byte[3];
            in.read(first3bytes);
            in.reset();
            if (first3bytes[0] == (byte) -17 && first3bytes[1] == (byte) -69 && first3bytes[2] == (byte) -65) {
                return "utf-8";
            }
            if (first3bytes[0] == (byte) -1 && first3bytes[1] == (byte) -2) {
                return "unicode";
            }
            if (first3bytes[0] == (byte) -2 && first3bytes[1] == (byte) -1) {
                return "utf-16be";
            }
            if (first3bytes[0] == (byte) -1 && first3bytes[1] == (byte) -1) {
                return "utf-16le";
            }
            return "GBK";
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException("取文件编码( 未找到文件:" + 路径);
        }
    }


    public static byte[] 读入字节(String 路径) {
        byte[] buffer = null;
        if (!new File(路径).exists()) {
            return null;
        }
        try {
            FileInputStream fin = new FileInputStream(路径);
            buffer = new byte[fin.available()];
            fin.read(buffer);
            fin.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读入字节文件( 错误");
        }
    }

    public static boolean 写出字节文件(String 路径, byte[] 欲写出字节集) {
        try {
            FileOutputStream fout = new FileOutputStream(路径);
            fout.write(欲写出字节集);
            fout.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("写出字节文件( 错误");
        }
    }

    public static long 取文件大小(String 路径) {
        File file = new File(路径);
        try {
            if (file.isDirectory()) {
                return getFileSizes(file);
            }
            return getFileSize(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("取文件大小( 错误");
        }
    }


    public static String 读入资源文件(Context 窗口环境, String 文件名称) {
        return 读入资源文件(窗口环境, 文件名称, "utf-8");
    }

    public static String 读入资源文件(Context 窗口环境, String 文件名称, String 编码) {
        try {
            InputStream inputstream = 窗口环境.getAssets().open(文件名称);
            if (inputstream == null) {
                return "";
            }
            int length = inputstream.available();
            byte[] buffer = new byte[length];
            inputstream.read(buffer);
            String res2 = new String(buffer, 0, length, 编码);
            inputstream.close();
            return res2;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("读入资源文件( 未找到文件: " + 文件名称);
        }
    }

    public static String 寻找文件关键词(String 路径, String 关键词) {
        String result = "";
        for (File f : new File(路径).listFiles()) {
            if (f.getName().indexOf(关键词) >= 0) {
                result = f.getPath() + "\n" + result;
            }
        }
        return result;
    }

    public static String 寻找文件后缀名(String 路径, String 后缀名) {
        String result = "";
        for (File f : new File(路径).listFiles()) {
            if (f.getPath().substring(f.getPath().length() - 后缀名.length()).equals(后缀名) && !f.isDirectory()) {
                result = f.getPath() + "\n" + result;
            }
        }
        return result;
    }


    public static String[] 取子目录(String 路径) {
        File[] ff = new File(路径).listFiles();
        String[] paths = new String[ff.length];
        for (int i = 0; i < ff.length; i++) {
            if (ff[i].isDirectory()) {
                paths[i] = ff[i].getAbsolutePath();
            }
        }
        return paths;
    }

    public static String[] 取子文件列表(String 路径) {
        List<String> list = new ArrayList<>();
        File[] ff = new File(路径).listFiles();
        for (int i = 0; i < ff.length; i++) {
            list.add(ff[i].getAbsolutePath());
        }
        return list.toArray(new String[list.size()]);
    }

    public static String[] 取子文件列表(String 路径, int 排序方式, boolean 是否正序) {
        List<String> list = new ArrayList<>();
        File[] files = new File(路径).listFiles();
        if (排序方式 == 文件排序_名称排序) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile()) {
                        if (是否正序)
                            return -1;
                        else
                            return 1;
                    }
                    if (o1.isFile() && o2.isDirectory()) {
                        if (是否正序)
                            return 1;
                        else
                            return -1;
                    }
                    if (o1.getName().compareTo(o2.getName()) == 1) {
                        if (是否正序) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        if (是否正序) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }
            });
        } else if (排序方式 == 文件排序_时间排序) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0) {
                        if (是否正序) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else if (diff == 0) {
                        return 0;
                    } else {
                        if (是否正序) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }

                public boolean equals(Object obj) {
                    return true;
                }
            });
        } else if (排序方式 == 文件排序_大小排序) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    long diff = f1.length() - f2.length();
                    if (diff > 0) {
                        if (是否正序) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else if (diff == 0) {
                        return 0;
                    } else {
                        if (是否正序) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }

                public boolean equals(Object obj) {
                    return true;
                }
            });
        }
        for (int i = 0; i < files.length; i++) {
            list.add(files[i].getAbsolutePath());
        }
        return list.toArray(new String[list.size()]);
    }

    public static List<String> 取子文件集合(String 路径) {
        List<String> list = new ArrayList<>();
        File[] ff = new File(路径).listFiles();
        for (int i = 0; i < ff.length; i++) {
            list.add(ff[i].getAbsolutePath());
        }
        return list;
    }

    public List<String> 取子文件集合(String 路径, int 排序方式, boolean 是否正序) {
        List<String> list = new ArrayList<>();
        File[] files = new File(路径).listFiles();
        if (排序方式 == 文件排序_名称排序) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile()) {
                        if (是否正序)
                            return -1;
                        else
                            return 1;
                    }
                    if (o1.isFile() && o2.isDirectory()) {
                        if (是否正序)
                            return 1;
                        else
                            return -1;
                    }
                    if (o1.getName().compareTo(o2.getName()) == 1) {
                        if (是否正序) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        if (是否正序) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }
            });
        } else if (排序方式 == 文件排序_时间排序) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0) {
                        if (是否正序) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else if (diff == 0) {
                        return 0;
                    } else {
                        if (是否正序) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }

                public boolean equals(Object obj) {
                    return true;
                }
            });
        } else if (排序方式 == 文件排序_大小排序) {
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    long diff = f1.length() - f2.length();
                    if (diff > 0) {
                        if (是否正序) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else if (diff == 0) {
                        return 0;
                    } else {
                        if (是否正序) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                }

                public boolean equals(Object obj) {
                    return true;
                }
            });
        }
        for (int i = 0; i < files.length; i++) {
            list.add(files[i].getAbsolutePath());
        }
        return list;
    }

    @SuppressLint("SimpleDateFormat")
    public static String 取文件修改时间(String 路径) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(new File(路径).lastModified()));
        }
        return "0";
    }


    private static long getFileSize(File file) throws Exception {
        if (file.exists()) {
            return (long) new FileInputStream(file).available();
        }
        file.createNewFile();
        return 0;
    }

    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File[] flist = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size += getFileSizes(flist[i]);
            } else {
                size += getFileSize(flist[i]);
            }
        }
        return size;
    }


    public static boolean 重命名文件(String 原路径, String 新路径) {
        if (新路径.equals(原路径)) {
            return true;
        }
        File oldfile = new File(原路径);
        if (!oldfile.exists()) {
            return false;
        }
        File newfile = new File(新路径);
        if (newfile.exists()) {
            return false;
        }
        return oldfile.renameTo(newfile);
    }

    public static boolean 复制文件(String 文件路径, String 欲复制到路径) {
        try {
            FileUtils.copyTo(new File(文件路径), new File(欲复制到路径));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static boolean 移动文件(String 文件路径, String 欲移动到路径) {
        try {
            FileUtils.moveTo(new File(文件路径), new File(欲移动到路径));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static String 取文件前缀名(String 文件路径) {
        return FileUtils.getFilePrefix(new File(文件路径));
    }

    public static String 取文件后缀名(String 文件路径) {
        return FileUtils.getFileSuffix(new File(文件路径));
    }

    public static String 取文件名(String 文件路径) {
        return FileUtils.getFileName(文件路径);
    }

    public static String 取文件MD5(String 文件路径) {
        try {
            return FileUtils.getMD5(new File(文件路径));
        } catch (IOException e) {
            return "";
        }
    }

    public static String 取文件SHA1(String 文件路径) {
        try {
            return FileUtils.getSHA1(new File(文件路径));
        } catch (IOException e) {
            return "";
        }
    }

    public static String 取文件CRC32(String 文件路径) {
        try {
            return FileUtils.getCRC32(new File(文件路径));
        } catch (IOException e) {
            return "";
        }
    }

    public static boolean 删除文件(String 文件路径) {
        return FileUtils.deleteFile(new File(文件路径));
    }

    public static boolean 创建目录(String 路径) {
        return FileUtils.createDirectory(new File(路径));
    }

    public static boolean 创建文件(String 路径) {
        return FileUtils.createFile(new File(路径));
    }

    public static boolean 是否为目录(String 路径) {
        File file = new File(路径);
        return file.exists() && file.isDirectory();
    }

    public static boolean 是否为隐藏文件(String 路径) {
        File file = new File(路径);
        if (file.exists())
            return file.isHidden();
        else
            return false;
    }

    public static boolean 文件是否存在(String 路径) {
        return new File(路径).exists();
    }

    public void 打开文本文件_读() throws 打开文件错误 {
        if (!new File(路径).exists())
            throw new 打开文件错误("文件无法打开");
        else {
            try {
                fin = new FileInputStream(路径);
                isr = new InputStreamReader(fin, 编码);
                br = new BufferedReader(isr);
            } catch (Exception e) {
                throw new 打开文件错误("文件无法打开");
            }
        }
    }

    public void 打开文本文件_写() throws 打开文件错误 {
        if (!new File(路径).exists())
            throw new 打开文件错误("文件无法打开");
        else {
            try {
                fout = new FileOutputStream(路径);
                osw = new OutputStreamWriter(fout, 编码);
                bw = new BufferedWriter(osw);
            } catch (Exception e) {
                throw new 打开文件错误("文件无法打开");
            }
        }
    }

    public boolean 写一行(String 内容) {
        try {
            bw.newLine();
            bw.write(内容);
            bw.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String 读一行() throws IOException {
        String readLine = br.readLine();
        line = readLine;
        return line;
    }

    public void 关闭文件() throws IOException {
        br.close();
        fin.close();
        bw.close();
        fout.close();
    }

    public static boolean 写出资源文件(Context context, String 文件名, String 欲写出路径) {
        try {
            InputStream stream = context.getAssets().open(文件名);
            File file = new File(欲写出路径);
            if (!Objects.requireNonNull(file.getParentFile()).exists()) {
                file.getParentFile().mkdirs();
            }
            if (stream != null && writeStreamToFile(stream, file)) {
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void 写出文本文件(String 路径, String 欲写出内容) {
        try {
            write(new File(路径), 欲写出内容);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String 读入文本文件(String 路径) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(路径)));
            boolean first = true;
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (first) {
                    first = false;
                    content.append(line);
                } else {
                    content.append('\n').append(line);
                }
            }
            br.close();
            return content.toString();
        } catch (IOException e) {
            return "";
        }
    }


    public static void 打开文件(Context context, String 路径) {
        context.startActivity(openFile(路径));
    }

    private static Intent openFile(String filePath) {
        File file = new File(filePath);
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String end = file.getName()
                .substring(file.getName().lastIndexOf(".") + 1, file.getName().length())
                .toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals(
                "ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        }
        if (end.equals("3gp") || end.equals("mp4")) {
            return getVideoFileIntent(filePath);
        }
        if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals(
                "bmp")) {
            return getImageFileIntent(filePath);
        }
        if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        }
        if (end.equals("ppt")) {
            return getPptFileIntent(filePath);
        }
        if (end.equals("xls")) {
            return getExcelFileIntent(filePath);
        }
        if (end.equals("doc")) {
            return getWordFileIntent(filePath);
        }
        if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        }
        if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        }
        if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        }
        return getAllIntent(filePath);
    }

    private static Intent getAllIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(param)), "*/*");
        return intent;
    }

    private static Intent getApkFileIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/vnd.android.package-archive");
        return intent;
    }

    private static Intent getVideoFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(Uri.fromFile(new File(param)), "video/*");
        return intent;
    }

    private static Intent getAudioFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(Uri.fromFile(new File(param)), "audio/*");
        return intent;
    }

    private static Intent getImageFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(param)), "image/*");
        return intent;
    }

    private static Intent getPptFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/vnd.ms-powerpoint");
        return intent;
    }

    private static Intent getExcelFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/vnd.ms-excel");
        return intent;
    }

    private static Intent getWordFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/msword");
        return intent;
    }

    private static Intent getChmFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/x-chm");
        return intent;
    }

    private static Intent getTextFileIntent(String param, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            intent.setDataAndType(Uri.parse(param), "text/plain");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(param)), "text/plain");
        }
        return intent;
    }

    private static Intent getPdfFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/pdf");
        return intent;
    }


    public static void write(File file, String 内容) throws IOException {
        if (!file.exists()) {
            createFile(file);
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        writer.write(内容);
        writer.flush();
        writer.close();
    }

    public static boolean createFile(File file) {
        try {
            if (!Objects.requireNonNull(file.getParentFile()).exists()) {
                file.getParentFile().mkdirs();
            }
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String 取私有目录路径(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    private static boolean writeStreamToFile(InputStream stream, File file) throws
            FileNotFoundException, IOException {
        OutputStream output = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        while (true) {
            int read = stream.read(buffer);
            if (read != -1) {
                output.write(buffer, 0, read);
            } else {
                output.flush();
                output.close();
                stream.close();
                return true;
            }
        }
    }
}
