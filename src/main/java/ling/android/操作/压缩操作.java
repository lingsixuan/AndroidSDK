package ling.android.操作;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class 压缩操作 {

    //压缩文件到输出路径，参数一为要压缩的文件(或目录)路径
    public static boolean zip压缩(String 路径, String 输出路径) {
        boolean flag = false;
        ZipOutputStream out = null;
        try {
            File outFile = new File(输出路径);
            File fileOrDirectory = new File(路径);
            out = new ZipOutputStream(new FileOutputStream(outFile));
            if (fileOrDirectory.isFile()) {
                zipFileOrDirectory(out, fileOrDirectory, "");
            } else {
                File[] entries = fileOrDirectory.listFiles();
                for (int i = 0; i < entries.length; i++) {
                    zipFileOrDirectory(out, entries[i], "");
                }
            }
            flag = true;
        } catch (IOException ex) {
            ex.printStackTrace();
            flag = false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    flag = false;
                }
            }
        }
        return flag;
    }

    //解压指定路径的压缩包到输出路径(必须为目录)
    public static boolean zip解压(String 路径, String 输出路径) {
        boolean flag = false;
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(路径);
            Enumeration e = zipFile.entries();
            ZipEntry zipEntry = null;
            File dest = new File(输出路径);
            dest.mkdirs();
            while (e.hasMoreElements()) {
                zipEntry = (ZipEntry) e.nextElement();
                String entryName = zipEntry.getName();
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    if (zipEntry.isDirectory()) {
                        String name = zipEntry.getName();
                        name = name.substring(0, name.length() - 1);
                        File f = new File(输出路径 + File.separator + name);
                        f.mkdirs();
                        flag = true;
                    } else {
                        int index = entryName.lastIndexOf("\\");
                        if (index != -1) {
                            File df = new File(输出路径 + File.separator + entryName.substring(0, index));
                            df.mkdirs();
                        }
                        index = entryName.lastIndexOf("/");
                        if (index != -1) {
                            File df = new File(输出路径 + File.separator + entryName.substring(0, index));
                            df.mkdirs();
                        }
                        File f = new File(输出路径 + File.separator + zipEntry.getName());

                        in = zipFile.getInputStream(zipEntry);
                        out = new FileOutputStream(f);

                        byte[] by = new byte[1024];
                        int c;
                        while ((c = in.read(by)) != -1) {
                            out.write(by, 0, c);
                        }
                        out.flush();
                        flag = true;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    flag = false;
                } finally {
                }

            }

        } catch (IOException ex) {
            ex.printStackTrace();
            flag = false;
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException ex) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    private static void zipFileOrDirectory(ZipOutputStream out, File fileOrDirectory, String curPath) throws IOException {
        FileInputStream in = null;
        try {
            if (!fileOrDirectory.isDirectory()) {
                byte[] buffer = new byte[4096];

                in = new FileInputStream(fileOrDirectory);

                ZipEntry entry = new ZipEntry(curPath + fileOrDirectory.getName());

                out.putNextEntry(entry);
                int bytes_read;
                while ((bytes_read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                }
                out.closeEntry();
            } else {
                File[] entries = fileOrDirectory.listFiles();

                if (entries.length <= 0) {
                    ZipEntry zipEntry = new ZipEntry(curPath + fileOrDirectory.getName() + "/");
                    out.putNextEntry(zipEntry);
                    out.closeEntry();
                } else {
                    for (int i = 0; i < entries.length; i++) {
                        zipFileOrDirectory(out, entries[i], curPath + fileOrDirectory.getName() + "/");
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }
    }

}
