package ling.android.操作;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import ling.android.异常类.网络操作出错;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class 网络操作 {
    public static HashMap<String, String> sHeader = new HashMap<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private int 连接超时时间 = 10 * 1000;
    private int 读取超时时间 = 10 * 1000;
    private Context context;

    @Deprecated
    public 网络操作() {

    }

    public 网络操作(Context context) {
        this.context = context;
    }

    @Deprecated
    public 网络操作(int 连接超时时间, int 读取超时时间) {
        this.连接超时时间 = 连接超时时间;
        this.读取超时时间 = 读取超时时间;
    }

    public 网络操作(Context context, int 连接超时时间, int 读取超时时间) {
        this.连接超时时间 = 连接超时时间;
        this.读取超时时间 = 读取超时时间;
        this.context = context;
    }

    public void 发送数据(String 网址, String 数据, 响应 object) {
        post(网址, 数据, new OnRequestListener() {
            @Override
            public void onCompleted(String code, String text, byte[] content, String cookie) {
                mainHandler.post(() -> {
                    if (object != null)
                        object.网络操作结束(text, 200);
                });
            }

            @Override
            public void onFailed(String code, String text, byte[] content) {
                mainHandler.post(() -> {
                    object.网络操作结束(text, -1);
                });
            }

            @Override
            public void onProgressChanged(int value) {

            }
        });

    }

    public void 取网页源码(String URL, 响应 object) throws 网络操作出错 {
        if (this.context == null)
            throw new 网络操作出错("无效的上下文环境");
        取网页源码(this.context, URL, object);
    }

    @SuppressLint("MissingPermission")
    @Deprecated
    public void 取网页源码(Context context, String URL, 响应 object) throws 网络操作出错 {
        ConnectivityManager 网络连接管理器 = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (网络连接管理器.getActiveNetworkInfo().getState() != NetworkInfo.State.CONNECTED)
            throw new 网络操作出错("无网络连接");
        new Thread(() -> {
            try {
                java.net.URL url = new URL(URL);
                HttpURLConnection 网络 = (HttpURLConnection) url.openConnection();
                网络.setRequestMethod("GET");
                网络.setConnectTimeout(连接超时时间);
                网络.setReadTimeout(读取超时时间);
                //网络.setDoOutput(true);   //是否允许发送数据
                //PrintStream 输出流 = new PrintStream(网络.getOutputStream());
                BufferedReader 输入流 = new BufferedReader(new InputStreamReader(网络.getInputStream()));
                网络.connect();   //建立连接
                //输出流.println(JSON对象.toString());

                int 响应码 = 网络.getResponseCode();
                网络.disconnect();
                String 输入 = 输入流.readLine();
                mainHandler.post(() -> {
                    if (object != null)
                        object.网络操作结束(输入, 响应码);
                });
            } catch (IOException e) {
                mainHandler.post(() -> {
                    if (object != null)
                        object.网络操作结束(null, -1);
                });
            }
        }).start();
    }

    public void 取EWB响应码(String URL, 响应 object) throws 网络操作出错 {
        if (this.context == null)
            throw new 网络操作出错("无效的上下文环境");
        取WEB响应码(this.context, URL, object);
    }

    @SuppressLint("MissingPermission")
    @Deprecated
    public void 取WEB响应码(Context context, String URL, 响应 object) throws 网络操作出错 {
        ConnectivityManager 网络连接管理器 = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (网络连接管理器.getActiveNetworkInfo().getState() != NetworkInfo.State.CONNECTED)
            throw new 网络操作出错("无网络连接");
        new Thread(() -> {
            try {
                java.net.URL url = new URL(URL);
                HttpURLConnection 网络 = (HttpURLConnection) url.openConnection();
                网络.setRequestMethod("GET");
                网络.setConnectTimeout(连接超时时间);
                网络.setReadTimeout(读取超时时间);
                //网络.setDoOutput(true);   //是否允许发送数据
                //PrintStream 输出流 = new PrintStream(网络.getOutputStream());
                网络.connect();   //建立连接
                //输出流.println(JSON对象.toString());
                int 响应码 = 网络.getResponseCode();
                网络.disconnect();
                mainHandler.post(() -> {
                    if (object != null)
                        object.网络操作结束(null, 响应码);
                });
            } catch (IOException e) {
                mainHandler.post(() -> {
                    if (object != null)
                        object.网络操作结束(null, -1);
                });
            }
        }).start();
    }

    public void 下载(String 网址, String 路径, 下载回调 回调) {
        download(网址, 路径, new OnRequestListener() {
            @Override
            public void onCompleted(String code, String text, byte[] content, String cookie) {
                回调.下载结束(cookie);
            }

            @Override
            public void onFailed(String code, String text, byte[] content) {
                回调.下载失败(code);
            }

            @Override
            public void onProgressChanged(int value) {
                回调.正在下载(value);
            }
        });
    }

    public static HttpTask1 post(String url, String data, OnRequestListener callback) {
        HttpTask1 task = new HttpTask1(url, "POST", null, null, callback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
        }
        return task;
    }


    public interface 下载回调 {
        void 下载结束(String cookie);

        void 下载失败(String 响应码);

        void 正在下载(int 进度);
    }

    public static HttpTask1 download(String url, String path, OnRequestListener callback) {
        HttpTask1 task = new HttpTask1(url, "GET", null, null, callback);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, path);
        }
        return task;
    }


    public interface 响应 {
        void 网络操作结束(String 数据, int 响应);
    }

    public static void setSsl() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    static class HttpTask1 extends AsyncTask {
        private String mUrl;
        private OnRequestListener mCallback;
        private byte[] mData;
        private String mCharset;
        private String mCookie;
        private String mMethod;
        private String returnCode = null;
        private String text = null;
        private byte[] returnContent = null;
        private String returnCookie = null;
        private int resultCode = 0;
        private long mFileLength;
        private long mTotalLength;

        public HttpTask1(String url, String method, String cookie, String charset, OnRequestListener callback) {
            mUrl = url;
            mMethod = method;
            mCookie = cookie;
            mCharset = charset;
            mCallback = callback;
        }

        @Override
        protected Object doInBackground(Object[] p1) {
            try {
                URL url = new URL(mUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (mUrl.startsWith("https://")) {
                    conn = (HttpsURLConnection) url.openConnection();
                    网络操作.setSsl();
                }
                conn.setConnectTimeout(120000);
                conn.setFollowRedirects(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Accept-Language", "zh-cn,zh;q=0.5");
                if (mCharset == null)
                    mCharset = "UTF-8";
                conn.setRequestProperty("Accept-Charset", mCharset);

                if (mCookie != null)
                    conn.setRequestProperty("Cookie", mCookie);

                if (mMethod != null)
                    conn.setRequestMethod(mMethod);

                if (!"GET".equals(mMethod) && p1.length != 0) {
                    mData = formatData(p1);
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-length", "" + mData.length);
                }
                if (sHeader != null) {
                    Set<Map.Entry<String, String>> entries = sHeader.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        conn.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                conn.connect();

                //download
                if ("GET".equals(mMethod) && p1.length != 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mFileLength = conn.getContentLengthLong();
                    }
                    File f = new File((String) p1[0]);
                    if (!f.getParentFile().exists())
                        f.getParentFile().mkdirs();
                    FileOutputStream os = new FileOutputStream(f);
                    InputStream is = conn.getInputStream();
                    copyFile(is, os);
                    Map<String, List<String>> hs = conn.getHeaderFields();
                    List<String> cs = hs.get("Set-Cookie");
                    StringBuffer cok = new StringBuffer();
                    if (cs != null)
                        for (String s : cs) {
                            cok.append(s + ";");
                        }
                    returnCookie = cok.toString();
                    returnCode = conn.getResponseCode() + "";
                    resultCode = 1;
                    return new Object[]{returnCode};
                }

                //post upload
                if (p1.length != 0) {
                    OutputStream os = conn.getOutputStream();
                    os.write(mData);
                }

                int code = conn.getResponseCode();
                Map<String, List<String>> hs = conn.getHeaderFields();
                if (code >= 200 && code < 400) {
                    //String encoding=conn.getContentEncoding();
                    List<String> cs = hs.get("Set-Cookie");
                    StringBuffer cok = new StringBuffer();
                    if (cs != null) {
                        for (String s : cs) {
                            cok.append(s + ";");
                        }
                    }
                    ByteArrayOutputStream boas = new ByteArrayOutputStream();
                    byte[] data = new byte[1024];
                    int len = 0;
                    byte[] result = null;
                    InputStream is = conn.getInputStream();
                    while ((len = is.read(data)) != -1) {
                        boas.write(data, 0, len);
                    }
                    returnContent = boas.toByteArray();
                    text = boas.toString();
                    boas.close();
                    is.close();
                    returnCode = code + "";
                    returnCookie = cok.toString();
                    resultCode = 1;
                    return new Object[]{returnCode, returnContent, returnCookie};
                } else {
                    returnCode = code + "";
                    text = conn.getResponseMessage();
                    returnContent = text.getBytes();
                    //returnCookie=cok.toString();
                    resultCode = 1;
                    return new Object[]{returnCode, text, returnContent, null};
                }
            } catch (Exception e) {
                e.printStackTrace();
                returnCode = "-1";
                text = e.getMessage();
                returnContent = text.getBytes();
                resultCode = 0;
                System.out.println(e);
                return new Object[]{returnCode, text, returnContent};
            }

        }

        private byte[] formatData(Object[] p1) throws UnsupportedEncodingException, IOException {
            // TODO: Implement this method
            byte[] bs = null;
            if (p1.length == 1) {
                Object obj = p1[0];
                if (obj instanceof String)
                    bs = ((String) obj).getBytes(mCharset);
                else if (obj.getClass().getComponentType() == byte.class)
                    bs = (byte[]) obj;
                else if (obj instanceof File)
                    bs = readAll(new FileInputStream((File) obj));
                else if (obj instanceof File)
                    bs = formatData((Map) obj);
            }
            return bs;
        }

        private byte[] formatData(Map obj) throws UnsupportedEncodingException {
            // TODO: Implement this method
            StringBuilder buf = new StringBuilder();
            return buf.toString().getBytes(mCharset);
        }

        public boolean cancel() {
            // TODO: Implement this method
            return super.cancel(true);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            // TODO: Implement this method
            super.onProgressUpdate(values);
            mCallback.onProgressChanged((int) values[0]);
        }

        @Override
        protected void onPostExecute(Object result) {
            // TODO: Implement this method
            super.onPostExecute(result);
            if (resultCode == 1)
                mCallback.onCompleted(returnCode, text, returnContent, returnCookie);
            else
                mCallback.onFailed(returnCode, text, returnContent);
        }

        private byte[] readAll(InputStream input) throws IOException {
            ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
            byte[] buffer = new byte[2 ^ 32];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            byte[] ret = output.toByteArray();
            output.close();
            return ret;
        }

        private boolean copyFile(InputStream in, OutputStream out) {
            try {
                int byteread = 0;
                byte[] buffer = new byte[1024 * 1024];
                while ((byteread = in.read(buffer)) != -1) {
                    mTotalLength += byteread;
                    int value = (int) ((mTotalLength / (float) mFileLength) * 100);
                    publishProgress(value);
                    out.write(buffer, 0, byteread);
                }
                //in.close
                //out.close
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    }

    public interface OnRequestListener {
        void onCompleted(String code, String text, byte[] content, String cookie);

        void onFailed(String code, String text, byte[] content);

        void onProgressChanged(int value);
    }


}
