package com.dktlh.ktl.xposedtest.utils.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtils {

    private static OkHttpClient client;
    private static OkHttpUtils okHttpUtils;
    private OkHttpCallback callback;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://异常
                    IOException e = (IOException) msg.obj;
                    Log.i("ruin", "e--> " + e.toString());
                    callback.onError(e);
                    break;
                case 2://成功
                    String result = (String) msg.obj;
                    callback.onResponse(result);
                    break;
            }
        }
    };

    /**
     * http请求
     */
    public static OkHttpUtils build() {
        client = new OkHttpClient();
        okHttpUtils = new OkHttpUtils();
        return okHttpUtils;
    }

    /**
     * https请求添加证书
     */
//    public static OkHttpUtils buildS() {
//        X509TrustManager trustManager;
//        SSLSocketFactory sslSocketFactory;
//        final InputStream inputStream;
//        try {
//            inputStream = MyApp.getApp().getBaseContext().getAssets().open("证书名"); // 得到证书的输入流
//            try {
//
//                trustManager = trustManagerForCertificates(inputStream);//以流的方式读入证书
//                SSLContext sslContext = SSLContext.getInstance("TLS");
//                sslContext.init(null, new TrustManager[]{trustManager}, null);
//                sslSocketFactory = sslContext.getSocketFactory();
//
//            } catch (GeneralSecurityException e) {
//                throw new RuntimeException(e);
//            }
//
//            client = new OkHttpClient.Builder()
//                    .sslSocketFactory(sslSocketFactory, trustManager)
//                    .build();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new OkHttpUtils();
//    }

    //设置回调方法
    public OkHttpUtils setCallback(OkHttpCallback callback) {
        this.callback = callback;
        return okHttpUtils;
    }

    //get请求
    public OkHttpUtils getokHttp(String url) {

        Request.Builder requestBuilder = new Request.Builder().url(url);
        //可以省略，默认是GET请求
        requestBuilder.method("GET", null);

        Request request = requestBuilder.build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = e;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = Message.obtain();
                msg.what = 2;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
        return okHttpUtils;
    }

    //post请求
    public OkHttpUtils postOkHttp(String url, Map<String, String> params) {

        FormBody.Builder builder = new FormBody.Builder();

        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        FormBody formBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = e;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = Message.obtain();
                msg.what = 2;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
        return okHttpUtils;
    }


    //post上传文件
    public OkHttpUtils upLoadFile(String url, Map<String, String> params, List<File> fileList) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);

        //加参数
        for (String key : params.keySet()) {
            builder.addFormDataPart(key, params.get(key));
        }

        //加文件
        for (File file : fileList) {
            builder.addFormDataPart("img", file.getName(), RequestBody.create(null, file));
        }

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = e;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Message msg = Message.obtain();
                msg.what = 2;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        });
        return okHttpUtils;
    }

    //请求回调接口
    public interface OkHttpCallback {
        void onError(Exception e);

        void onResponse(String result);
    }

    /**
     * 以流的方式添加信任证书
     */
    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     * <p>
     * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
     * set. This is useful in development where certificate authority-trusted certificates aren't
     * available. Or in production, to avoid reliance on third-party certificate authorities.
     * <p>
     * <p>
     * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
     * <p>
     * <p>Relying on your own trusted certificates limits your server team's ability to update their
     * TLS certificates. By installing a specific set of trusted certificates, you take on additional
     * operational complexity and limit your ability to migrate between certificate authorities. Do
     * not use custom trusted certificates in production without the blessing of your server's TLS
     * administrator.
     */
    private static X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    /**
     * 添加password
     *
     * @param password
     * @return
     * @throws GeneralSecurityException
     */
    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); // 这里添加自定义的密码，默认
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
    /**
     * 下载文件
     *
     * @param fileUrl     文件url
     * @param destFileDir 存储目标目录
     * @param progress    回调
     */
    public void downLoadFile(String fileUrl, final String destFileDir, String fileName, final OkhttpFreshProgress progress) {
        if (TextUtils.isEmpty(fileUrl)) {
            return;
        }

        final File file = new File(destFileDir + fileName);

        final Request request = new Request.Builder().url(fileUrl).build();
        final Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ruin", e.toString());
                if (progress != null) {
                    progress.failed(e);
                }
                Log.i("ruin", "下载失败!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    Log.e("ruin", "total------>" + total);
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        Log.e("ruin", "current------>" + current);
                        if (progress!=null){
                            progress.freshProgress(total,current);
                        }
                    }
                    fos.flush();
                    if (progress != null) {
                        progress.success();
                        Log.i("ruin", "下载成功!");
                    }
                } catch (IOException e) {
                    Log.e("ruin", e.toString());
                    Log.i("ruin", "下载失败!");
                    if (progress!=null){
                        progress.failed(e);
                    }
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        Log.e("ruin", e.toString());
                    }
                }
            }
        });
    }
    //进度条回调
    public interface OkhttpFreshProgress {
        void start();
        void freshProgress(long total, long current);
        void success();
        void failed(IOException e);
    }
}
