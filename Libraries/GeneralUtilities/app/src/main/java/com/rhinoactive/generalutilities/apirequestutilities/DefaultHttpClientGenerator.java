package com.rhinoactive.generalutilities.apirequestutilities;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by rhinodesktop on 2017-01-31.
 */

/**
 * Creates a default singleton instance of an {@link OkHttpClient}
 */
public class DefaultHttpClientGenerator {

    private static OkHttpClient okHttpClient;

    public static OkHttpClient getInstance() throws GeneralSecurityException {
        if (okHttpClient == null) {
            okHttpClient = generateHTTPClient();
        }
        return okHttpClient;
    }

    private static OkHttpClient generateHTTPClient() throws GeneralSecurityException {
        X509TrustManager trustManager = defaultTrustManager();
        SSLSocketFactory sslSocketFactory = defaultSslSocketFactory(trustManager);
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory, trustManager)
                .build();
    }

    /**
     * Function to build a new client that has all the configurations of the original, but allows for different timeout durations to be added.
     * If any of the parameters are set to REQUEST_DEFAULT_TIMEOUT_DURATION (-1), the previously assigned timeout duration will be used.
     * If any timeouts were not previously assigned, timeouts will use their defaults.
     * If any parameters are set to 0, that indicates it won't timeout for that part of the request.
     *
     * @param callTimeoutDuration   The total amount of time the request has to complete.
     * @param connectionTimeoutDuration The amount of time the client has to make a connection with the host.
     * @param writeTimeoutDuration  The maximum time of inactivity between two data packets when sending the request to the server.
     * @param readTimeoutDuration the maximum time of inactivity between two data packets when waiting for the server's response.
     *
     * @return a new OkHttpClient that has the configurations of the original, with new timeouts.
     * @throws GeneralSecurityException
     */
    public static OkHttpClient getClientWithCustomTimeouts(int callTimeoutDuration, int connectionTimeoutDuration, int writeTimeoutDuration, int readTimeoutDuration) throws GeneralSecurityException {
        if (okHttpClient == null) {
            okHttpClient = generateHTTPClient();
        }
        OkHttpClient.Builder clientBuilder = okHttpClient.newBuilder();
        if (callTimeoutDuration > -1) {
            clientBuilder.callTimeout(callTimeoutDuration, TimeUnit.SECONDS);
        }
        if (connectionTimeoutDuration > -1) {
            clientBuilder.connectTimeout(connectionTimeoutDuration, TimeUnit.SECONDS);
        }
        if (writeTimeoutDuration > -1) {
            clientBuilder.writeTimeout(writeTimeoutDuration, TimeUnit.SECONDS);
        }
        if (readTimeoutDuration > -1) {
            clientBuilder.readTimeout(readTimeoutDuration, TimeUnit.SECONDS);
        }
        return clientBuilder.build();
    }

    /**
     * Returns a trust manager that trusts the VM's default certificate authorities.
     */
    private static X509TrustManager defaultTrustManager() throws GeneralSecurityException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
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
