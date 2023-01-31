package ling.android.操作;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.*;

public class okhttp {

    private Request.Builder builder;
    private OkHttpClient client;
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    public okhttp() {
        this.builder = new Request.Builder().addHeader("Content-Type", "text/plain");
        this.client = setSaveLoadCookie(new OkHttpClient.Builder()).build();
    }

    public okhttp(String host, String sha256) {
        this(new 证书固定数据().add(host, sha256));
    }

    public okhttp(证书固定数据 证书链) {
        this.builder = new Request.Builder().addHeader("Content-Type", "text/plain");
        CertificatePinner.Builder temp = new CertificatePinner.Builder();
        for (int i = 0; i < 证书链.getSize(); i++) {
            temp.add(证书链.getHost(i), 证书链.getSha256(i));
        }
        CertificatePinner certificatePinner = temp.build();
        this.client = setSaveLoadCookie(new OkHttpClient.Builder()).certificatePinner(certificatePinner).build();
    }

    /**
     * 使用自签名证书访问url
     *
     * @param 证书集 自签名证书
     * @param 证书链 证书信任链
     * @throws IOException              IO错误时抛出
     * @throws GeneralSecurityException 证书错误时抛出
     */
    public okhttp(自签名证书集 证书集, 证书固定数据 证书链) throws IOException, GeneralSecurityException {
        this.builder = new Request.Builder().addHeader("Content-Type", "text/plain");
        OkHttpClient.Builder build = new OkHttpClient.Builder();
        //处理证书固定
        CertificatePinner.Builder temp = new CertificatePinner.Builder();
        for (int i = 0; i < 证书链.getSize(); i++) {
            temp.add(证书链.getHost(i), 证书链.getSha256(i));
        }
        CertificatePinner certificatePinner = temp.build();
        build.certificatePinner(certificatePinner);
        //处理自签名证书
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        for (int i = 0; i < 证书集.list.size(); i++) {
            InputStream inputStream = new ByteArrayInputStream(证书集.list.get(i).证书.getBytes());
            keyStore.setCertificateEntry(证书集.list.get(i).域名, certificateFactory
                    .generateCertificate(inputStream));
            inputStream.close();
        }
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
        build.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
        //域名校验程序
        build.hostnameVerifier((hostname, session) -> {
            for (int i = 0; i < 证书集.list.size(); i++) {
                if (证书集.list.get(i).域名.equals(hostname))
                    return true;
            }
            return false;
        });
        this.client = setSaveLoadCookie(build).build();
    }

    /*public static void main(String[] str) throws GeneralSecurityException, IOException {
        自签名证书集 s = new 自签名证书集();
        s.add("api.lsxapi.top", "-----BEGIN CERTIFICATE-----\n" +
                "MIIELzCCAxcCFHjE2A9LIMCj+QbHUNvVu+GFz0OEMA0GCSqGSIb3DQEBCwUAMIHT\n" +
                "MQswCQYDVQQGEwJjbjEbMBkGA1UECAwSw6XCh8KMw6bCgMKdw6jCkMKxMRswGQYD\n" +
                "VQQHDBLDp8Klwp3DpMK7wqXDqcKdwpkxJzAlBgNVBAoMHsOlwpzCo8Okwr3Cv8Ok\n" +
                "wrrCkcOkwrrCksOowoHClDEnMCUGA1UECwwew6XCnMKjw6TCvcK/w6TCusKRw6TC\n" +
                "usKSw6jCgcKUMRcwFQYDVQQDDA5hcGkubHN4YXBpLnRvcDEfMB0GCSqGSIb3DQEJ\n" +
                "ARYQYWRtaW5AbHN4Lnp5ai5weTAeFw0yMzAxMzEwMjA1MTVaFw0zMzAxMjgwMjA1\n" +
                "MTVaMIHTMQswCQYDVQQGEwJjbjEbMBkGA1UECAwSw6XCh8KMw6bCgMKdw6jCkMKx\n" +
                "MRswGQYDVQQHDBLDp8Klwp3DpMK7wqXDqcKdwpkxJzAlBgNVBAoMHsOlwpzCo8Ok\n" +
                "wr3Cv8OkwrrCkcOkwrrCksOowoHClDEnMCUGA1UECwwew6XCnMKjw6TCvcK/w6TC\n" +
                "usKRw6TCusKSw6jCgcKUMRcwFQYDVQQDDA5hcGkubHN4YXBpLnRvcDEfMB0GCSqG\n" +
                "SIb3DQEJARYQYWRtaW5AbHN4Lnp5ai5weTCCASIwDQYJKoZIhvcNAQEBBQADggEP\n" +
                "ADCCAQoCggEBANH24E2Jx/GzneK2F0IBBwdbQnovwCMsMdIqx/QKZIYAPFDH28zc\n" +
                "RWbkDDLBr19vbZJ6DFPasd/5w1iHnorWwuIfStmbwC45vISN+FpdQT/aawxuNBgF\n" +
                "GFZ8vCdGqmfu1zhe5DNA5cVm5tkiH3rQq5lM6/HC+GcA48IMUEUqfY3C56LLysHA\n" +
                "0CrDBNVOrFCVuGTC3ZapeOiDgEXdH0ZgoFt1eeEkRTtB7JQpk+Dix+ZAN5qDUPU2\n" +
                "tg8rmGeWP4Bny540ajjQOJmYVKKZ3luZrlO+7VS66qnsJAhWnYVhdvADPQ2zGZPs\n" +
                "vaE+jcjmmoqUxv/f2imnxI5rOU35+KIWmdMCAwEAATANBgkqhkiG9w0BAQsFAAOC\n" +
                "AQEAJkvH/F/Il8IU9kmd0Wn5/njTV8NRUCRE7ICiKH1WIWD1xZWGi5VgyZCaMDC2\n" +
                "TtNkUGC68WPvhZa3ZyiJwwD0Ajr6Gjf7f7aixe+5uMTrH4yy0s18qPaHey1oi1wk\n" +
                "HycT5j02otNX4X06WJjcf3N7N5Lp9FcduuX7GkCA13lpwvJU/0Tw+9D7+sElkIdX\n" +
                "Wpow/UAWOISp+wSo7z6d/J5n7X5araxA5y242Yz11AbnCVDQw6D6h9VuO0D5i9ai\n" +
                "jphXUOu18XnII4UbSKqyy1Sd/eKjvByoinBgWW4dd/bZD9kfPyTULLpfwUW+YyGI\n" +
                "CaGKx2uWhFtzNzRmENY2uRdkLg==\n" +
                "-----END CERTIFICATE-----\n");
        okhttp o = new okhttp(s,new 证书固定数据().add("api.lsxapi.top","sha256/kFxpKa86rnQNpfln2TnjIw6U6vOSDnEKz5C5Q6/6xQA="));
        Response res = o.取同步对象().get("https://api.lsxapi.top");
        System.out.printf("响应码：%s\n响应内容：%s\n", res.code(), res.body().string());
    }*/

    public HashMap<String, List<Cookie>> getCookieStore() {
        return cookieStore;
    }

    /**
     * 设置Cookie处理器
     *
     * @param builder Okhttp对象
     * @return Okhttp对象
     */
    private OkHttpClient.Builder setSaveLoadCookie(OkHttpClient.Builder builder) {
        return builder.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                cookieStore.put(httpUrl.host(), list);
            }

            @NotNull
            @Override
            public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                List<Cookie> cookies = cookieStore.get(httpUrl.host());
                return cookies != null ? cookies : new ArrayList<>();
            }
        });
    }


    public 同步请求 取同步对象() {
        return new 同步请求(builder, client);
    }

    public 异步请求 取异步对象() {
        return new 异步请求(builder, client);
    }

    public static class 证书固定数据 {
        private final List<String> host = new ArrayList<>();
        private final List<String> sha256 = new ArrayList<>();

        public 证书固定数据() {

        }

        public 证书固定数据 add(String host, String sha256) {
            this.host.add(host);
            this.sha256.add(sha256);
            return this;
        }

        public String getHost(int i) {
            return host.get(i);
        }

        public String getSha256(int i) {
            return sha256.get(i);
        }

        public int getSize() {
            return host.size();
        }
    }

    public static class 自签名证书集 {
        protected List<自签名证书> list = new ArrayList<>();

        public void add(String 域名, String 证书) {
            list.add(new 自签名证书(域名, 证书));
        }
    }

    protected static class 自签名证书 {
        protected String 证书;
        protected String 域名;

        public 自签名证书(String 域名, String 证书) {
            this.证书 = 证书;
            this.域名 = 域名;
        }
    }


    public static class 异步请求 {
        protected Request.Builder builder;
        protected OkHttpClient client;
        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        protected 异步请求(Request.Builder builder, OkHttpClient client) {
            this.builder = builder;
            this.client = client;
        }


        public void get(String url, @NotNull 响应 回调) {
            Request okhttp = builder.get().url(url).build();
            Call call = client.newCall(okhttp);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    mainHandler.post(() -> {
                        回调.请求出错(call, e);
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    回复 temp = new 回复(response);
                    mainHandler.post(() -> {
                        try {
                            回调.请求完成(call, temp);
                        } catch (IOException e) {
                            回调.请求出错(call, e);
                        }
                    });
                }
            });
        }

        public void post(String url, String data, @NotNull 响应 回调) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), data);
            post(url, requestBody, 回调);
        }

        public void post(String url, JSONObject json, @NotNull 响应 回调) {
            post(url, json.toString(), 回调);
        }

        public void post(String url, RequestBody body, @NotNull 响应 回调) {
            Request okhttp = builder.post(body).url(url).build();
            Call call = client.newCall(okhttp);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    mainHandler.post(() -> {
                        回调.请求出错(call, e);
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    回复 temp = new 回复(response);
                    mainHandler.post(() -> {
                        try {
                            回调.请求完成(call, temp);
                        } catch (IOException e) {
                            回调.请求出错(call, e);
                        }
                    });
                }
            });
        }

        public interface 响应 {
            void 请求出错(@NotNull Call call, @NotNull IOException e);

            void 请求完成(@NotNull Call call, @NotNull 回复 response) throws IOException;
        }
    }

    public static class 同步请求 {
        protected Request.Builder builder;
        protected OkHttpClient client;

        protected 同步请求(Request.Builder builder, OkHttpClient client) {
            this.builder = builder;
            this.client = client;
        }

        public Response get(String url) throws IOException {
            Request okhttp = builder.get().url(url).build();
            Call call = client.newCall(okhttp);
            return call.execute();
        }

        public Response post(String url, String data) throws IOException {
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), data);
            return post(url, requestBody);
        }

        public Response post(String url, JSONObject json) throws IOException {
            return post(url, json.toString());
        }

        public Response post(String url, RequestBody body) throws IOException {
            Request okhttp = builder.post(body).url(url).build();
            Call call = client.newCall(okhttp);
            return call.execute();
        }

    }

    public static class 回复 {
        protected Response response;

        public 回复(Response response) throws IOException {
            this.response = response;
        }

        public Response getResponse() {
            return response;
        }

        public String getString() {
            String[] temp = new String[1];
            Thread thread = new Thread(() -> {
                try {
                    temp[0] = Objects.requireNonNull(response.body()).string();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
            try {
                thread.join();
                return temp[0];
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
