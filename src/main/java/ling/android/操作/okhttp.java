package ling.android.操作;

import android.os.Handler;
import android.os.Looper;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class okhttp {

    private Request.Builder builder;
    private OkHttpClient client;
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    public okhttp() {
        this.builder = new Request.Builder().addHeader("Content-Type", "text/plain");
        this.client = setSaveLoadCookie(new OkHttpClient.Builder()).build();
    }

    public okhttp(String host, String sha256) {
        this(new 证书().add(host, sha256));
    }

    public okhttp(证书 证书链) {
        this.builder = new Request.Builder().addHeader("Content-Type", "text/plain");
        CertificatePinner.Builder temp = new CertificatePinner.Builder();
        for (int i = 0; i < 证书链.getSize(); i++) {
            temp.add(证书链.getHost(i), 证书链.getSha256(i));
        }
        CertificatePinner certificatePinner = temp.build();
        this.client = setSaveLoadCookie(new OkHttpClient.Builder()).certificatePinner(certificatePinner).build();
    }

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

    public static class 证书 {
        private final List<String> host = new ArrayList<>();
        private final List<String> sha256 = new ArrayList<>();

        public 证书() {

        }

        public 证书 add(String host, String sha256) {
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
