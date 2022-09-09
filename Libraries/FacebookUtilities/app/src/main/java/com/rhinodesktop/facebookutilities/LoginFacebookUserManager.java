package com.rhinodesktop.facebookutilities;

import android.util.Log;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.rhinodesktop.facebookutilities.callbacks.LoginCallback;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import timber.log.Timber;

/**
 * Created by rhinodesktop on 2017-01-18.
 */

public class LoginFacebookUserManager {

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    public static void loginFacebookUser(String loginUserUrl, final LoginCallback loginCallback) {
        try {
            createLoginRequest(loginUserUrl).enqueue(loginCallback);
        } catch (Exception ex) {
            Timber.e("Error logging user in: %s", ex.toString());
        }
    }

    private static Call createLoginRequest(String loginUserUrl) throws GeneralSecurityException {
        Gson gson = new Gson();
        String currentAccessToken = AccessToken.getCurrentAccessToken().getToken();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("app_token", currentAccessToken);

        Request request = new Request.Builder()
                .url(loginUserUrl)
                .post(RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(parameters)))
                .build();

        OkHttpClient client = configureHTTPClient();
        return client.newCall(request);
    }

    private static OkHttpClient configureHTTPClient() throws GeneralSecurityException {
        X509TrustManager trustManager = defaultTrustManager();
        SSLSocketFactory sslSocketFactory = defaultSslSocketFactory(trustManager);
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory, trustManager)
                .build();
    }

    /** Returns a trust manager that trusts the VM's default certificate authorities. */
    private static X509TrustManager defaultTrustManager() throws GeneralSecurityException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new GeneralSecurityException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    /**
     * Returns the VM's default SSL socket factory, using {@code trustManager} for trusted root
     * certificates.
     */
    private static SSLSocketFactory defaultSslSocketFactory(X509TrustManager trustManager) throws GeneralSecurityException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        //If parameters are null then the default implementation will be used
        sslContext.init(null, new TrustManager[] { trustManager }, null);
        return sslContext.getSocketFactory();
    }
}