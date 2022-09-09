package com.rhinoactive.generalutilities.apirequestutilities;

import com.google.gson.Gson;
import com.rhinoactive.generalutilities.Constants;
import com.rhinoactive.generalutilities.models.HTTPMethod;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by rhinodesktop on 2017-01-31.
 */

/**
 * Class that does a lot of the scaffolding needed for creating network requests.
 */
public abstract class ApiRequestsUtility {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
    protected static final String APPLICATION_JSON = "application/json";
    private static final MediaType JSON_CHARSET = MediaType.parse(APPLICATION_JSON + "; charset=utf-8");
    private static Gson gson = new Gson();
    public static final int REQUEST_DEFAULT_TIMEOUT_DURATION = -1;

    protected abstract String getServerUrl();

    protected abstract String getAuthorizationValue();

    protected abstract String getAppVersionNumber();

    protected Call postRequestWithFormData(String url, Map<String, String> params, Map<String, List<String>> files) throws GeneralSecurityException {
        url = getServerUrl() + url;
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        multipartBodyBuilder = addParamsToMultipartBuilder(multipartBodyBuilder, params);
        multipartBodyBuilder = addFilesToMultipartBuilder(multipartBodyBuilder, files);
        RequestBody requestBody = multipartBodyBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader(Constants.AUTHORIZATION_HEADER, getAuthorizationValue())
                .addHeader(Constants.USER_AGENT, getAppVersionNumber())
                .post(requestBody)
                .build();
        OkHttpClient client = configureHTTPClient();
        return client.newCall(request);
    }

    protected Call postRequest(String url, Map<String, Object> params) throws GeneralSecurityException {
        return createNetworkCall(url, params, HTTPMethod.POST);
    }

    protected Call postRequest(String url, Map<String, Object> params, int callTimeout, int connectionTimeout, int writeTimeout, int readTimeout) throws GeneralSecurityException {
        return createNetworkCall(url, params, HTTPMethod.POST, callTimeout, connectionTimeout, writeTimeout, readTimeout);
    }

    protected Call putRequest(String url, Map<String, Object> params) throws GeneralSecurityException {
        return createNetworkCall(url, params, HTTPMethod.PUT);
    }

    protected Call putRequest(String url, Map<String, Object> params,int callTimeout, int connectionTimeout, int writeTimeout, int readTimeout) throws GeneralSecurityException {
        return createNetworkCall(url, params, HTTPMethod.PUT, callTimeout, connectionTimeout, writeTimeout, readTimeout);
    }

    protected Call deleteRequest(String url, Map<String, Object> params) throws GeneralSecurityException {
        return createNetworkCall(url, params, HTTPMethod.DELETE);
    }

    protected Call deleteRequest(String url, Map<String, Object> params, int callTimeout, int connectionTimeout, int writeTimeout, int readTimeout) throws GeneralSecurityException {
        return createNetworkCall(url, params, HTTPMethod.DELETE, callTimeout, connectionTimeout, writeTimeout, readTimeout);
    }

    protected Call getRequest(String url) throws GeneralSecurityException {
        return createNetworkCall(url, null, HTTPMethod.GET);
    }

    protected Call getRequest(String url, int callTimeout, int connectionTimeout, int writeTimeout, int readTimeout) throws GeneralSecurityException {
        return createNetworkCall(url, null, HTTPMethod.GET, callTimeout, connectionTimeout, writeTimeout, readTimeout);
    }

    protected OkHttpClient configureHTTPClient() throws GeneralSecurityException {
        return DefaultHttpClientGenerator.getInstance();
    }

    protected OkHttpClient configureHTTPClient(int callTimeout, int connectionTimeout, int writeTimeout, int readTimeout) throws GeneralSecurityException {
        return DefaultHttpClientGenerator.getClientWithCustomTimeouts(callTimeout, connectionTimeout, writeTimeout, readTimeout);
    }
//club
    private Call createNetworkCall(String url, Map<String, Object> params, HTTPMethod method) throws GeneralSecurityException {
        url = getServerUrl() + url;
        Request.Builder builder = buildRequest(url);
        builder = setMethod(builder, params, method);
        Request request = builder.build();
        OkHttpClient client = configureHTTPClient();
        return client.newCall(request);
    }

    private Call createNetworkCall(String url, Map<String, Object> params, HTTPMethod method, int callTimeout, int connectionTimeout, int writeTimeout, int readTimeout) throws GeneralSecurityException {
        url = getServerUrl() + url;
        Request.Builder builder = buildRequest(url);
        builder = setMethod(builder, params, method);
        Request request = builder.build();
        OkHttpClient client = configureHTTPClient(callTimeout, connectionTimeout, writeTimeout, readTimeout);
        return client.newCall(request);
    }

    protected Request.Builder buildRequest(String url) {
        return new Request.Builder()
                .url(url)
                .addHeader(Constants.AUTHORIZATION_HEADER, getAuthorizationValue())
                .addHeader(Constants.USER_AGENT, getAppVersionNumber());
    }

    private Request.Builder setMethod(Request.Builder builder, Map<String, Object> params, HTTPMethod method) {
        if (method == HTTPMethod.GET)
            return builder.get();
        else if (method == HTTPMethod.POST)
            return builder.post(RequestBody.create(JSON_CHARSET, gson.toJson(params)));
        else if (method == HTTPMethod.PUT)
            return builder.put(RequestBody.create(JSON_CHARSET, gson.toJson(params)));
        else
            return params != null ? builder.delete(RequestBody.create(JSON_CHARSET, gson.toJson(params))) : builder.delete();
    }

    private MultipartBody.Builder addParamsToMultipartBuilder(MultipartBody.Builder multipartBodyBuilder, Map<String, String> params) {
        for (Map.Entry<String, String> param : params.entrySet()) {
            String key = param.getKey();
            String value = param.getValue();
            multipartBodyBuilder.addFormDataPart(key, value);
        }
        return multipartBodyBuilder;
    }

    private MultipartBody.Builder addFilesToMultipartBuilder(MultipartBody.Builder multipartBodyBuilder, Map<String, List<String>> files) {
        for (Map.Entry<String, List<String>> file : files.entrySet()) {
            String key = file.getKey();
            List<String> absoluteFileNames = file.getValue();
            for (String absoluteFileName : absoluteFileNames) {
                File sourceFile = new File(absoluteFileName);
                RequestBody fileRequestBody = RequestBody.create(MEDIA_TYPE_PNG, sourceFile);
                String localFileName = absoluteFileName.substring(absoluteFileName.lastIndexOf("/") + 1);
                multipartBodyBuilder.addFormDataPart(key, localFileName, fileRequestBody);
            }
        }
        return multipartBodyBuilder;
    }
}
