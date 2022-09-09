package com.rhinoactive.imageutility.awsdirectimagedownloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class AWSImageDownloaderWithAuthentication {

    private String accessKeyId;
    private String secretAccessKey;
    private String regionName;
    private String serviceName;
    private String httpMethodName;

    public AWSImageDownloaderWithAuthentication(String accessKeyId, String secretAccessKey, String regionName, String serviceName, String httpMethodName) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.regionName = regionName;
        this.serviceName = serviceName;
        this.httpMethodName = httpMethodName;
    }

    /**
     * @param activity used for Glide. Only one of activity or context needs to be NonNull
     * @param context  used for Glide. Only one of activity or context needs to be NonNull
     * @param view     to load the url into
     * @param url      of the image to load
     */
    public void download(String S3BaseUrl, Activity activity, Context context, final View view, String url) {
        TreeMap<String, String> awsHeaders = new TreeMap<>();
        awsHeaders.put("host", S3BaseUrl.substring(8));

        String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        String urlToEncode = "/" + url;
        String urlEncoded = Uri.encode(urlToEncode, ALLOWED_URI_CHARS);

        AWSV4Auth awsv4Auth = new AWSV4Auth.Builder(accessKeyId, secretAccessKey)
                .regionName(regionName)
                .serviceName(serviceName)
                .httpMethodName(httpMethodName)
                .canonicalURI(urlEncoded)
                .queryParametes(null)
                .awsHeaders(awsHeaders)
                .payload(null)
                .build();


        Map<String, String> header = awsv4Auth.getHeaders();

        GlideUrl glideUrl = new GlideUrl(S3BaseUrl + urlToEncode, new LazyHeaders.Builder()
                .addHeader("Authorization", header.get("Authorization"))
                .addHeader("host", S3BaseUrl.substring(8))
                .addHeader("X-Amz-Content-Sha256", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")
                .addHeader("X-Amz-Date", header.get("x-amz-date"))
                .build());

        loadImage(view, context, activity, glideUrl);
    }

    public GlideUrl generateGlideUrlWithSignedUrl(String S3BaseUrl, String url){
        TreeMap<String, String> awsHeaders = new TreeMap<>();
        awsHeaders.put("host", S3BaseUrl.substring(8));

        String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        String urlToEncode = "/" + url;
        String urlEncoded = Uri.encode(urlToEncode, ALLOWED_URI_CHARS);

        AWSV4Auth awsv4Auth = new AWSV4Auth.Builder(accessKeyId, secretAccessKey)
                .regionName(regionName)
                .serviceName(serviceName)
                .httpMethodName(httpMethodName)
                .canonicalURI(urlEncoded)
                .queryParametes(null)
                .awsHeaders(awsHeaders)
                .payload(null)
                .build();


        Map<String, String> header = awsv4Auth.getHeaders();

        GlideUrl glideUrl = new GlideUrl(S3BaseUrl + urlToEncode, new LazyHeaders.Builder()
                .addHeader("Authorization", header.get("Authorization"))
                .addHeader("host", S3BaseUrl.substring(8))
                .addHeader("X-Amz-Content-Sha256", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")
                .addHeader("X-Amz-Date", header.get("x-amz-date"))
                .build());

        return glideUrl;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void playVideo(String S3BaseUrl, VideoView videoView, String url) {
        TreeMap<String, String> awsHeaders = new TreeMap<>();
        awsHeaders.put("host", S3BaseUrl.substring(8));

        String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        String urlToEncode = "/" + url;
        String urlEncoded = Uri.encode(urlToEncode, ALLOWED_URI_CHARS);

        AWSV4Auth awsv4Auth = new AWSV4Auth.Builder(accessKeyId, secretAccessKey)
                .regionName(regionName)
                .serviceName(serviceName)
                .httpMethodName(httpMethodName)
                .canonicalURI(urlEncoded)
                .queryParametes(null)
                .awsHeaders(awsHeaders)
                .payload(null)
                .build();


        Map<String, String> header = awsv4Auth.getHeaders();

        Map<String, String> videoHeaders = new HashMap<>();
        videoHeaders.put("Authorization", header.get("Authorization"));
        videoHeaders.put("host", S3BaseUrl.substring(8));
        videoHeaders.put("X-Amz-Content-Sha256", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        videoHeaders.put("X-Amz-Date", header.get("x-amz-date"));

        videoView.setVideoURI(Uri.parse(S3BaseUrl + urlToEncode), videoHeaders);
        videoView.requestFocus();
        videoView.start();
    }

    private void loadImage(final View view, Context context, Activity activity, GlideUrl glideUrl) {
        if (activity == null) {
            if (view instanceof ImageView) {
                Glide.with(context).load(glideUrl).into((ImageView) view);
            } else {
                Glide.with(context).load(glideUrl).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }
                });
            }
        } else {
            if (view instanceof ImageView) {
                Glide.with(activity).load(glideUrl).into((ImageView) view);
            } else {
                Glide.with(activity).load(glideUrl).into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        view.setBackground(resource);
                    }
                });
            }
        }
    }
}
