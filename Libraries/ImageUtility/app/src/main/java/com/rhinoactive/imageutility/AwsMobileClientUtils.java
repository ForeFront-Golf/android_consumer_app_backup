package com.rhinoactive.imageutility;

import android.app.Activity;
import android.content.Context;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Created by hunter on 2018-02-21.
 */

/**
 * Singleton class that initializes the {@link AmazonS3Client} instance for your app.
 */
public class AwsMobileClientUtils {

    private static AwsMobileClientUtils awsMobileClientUtils = null;
    private TransferUtility transferUtility;
    private AmazonS3Client amazonS3Client;
    private String localS3FilePath;


    /**
     * Private constructor, call {@link #initAwsMobileClient(Activity, Context)} to create the
     * {@link #awsMobileClientUtils} instance
     *
     * @param activity
     * @param applicationContext
     */
    private AwsMobileClientUtils(Activity activity, Context applicationContext) {
        AWSMobileClient.getInstance().initialize(activity).execute();
        localS3FilePath = createLocalS3FilePath(applicationContext);
        amazonS3Client = new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider(), Region.getRegion(Regions.US_EAST_1));
        transferUtility = TransferUtility.builder()
            .context(applicationContext)
            .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
            .s3Client(amazonS3Client)
            .build();
    }

    // Call this in your startup activity's onCreate method
    public static void initAwsMobileClient(Activity activity, Context applicationContext) {
        try{
            awsMobileClientUtils = new AwsMobileClientUtils(activity, applicationContext);

        }
        catch(Exception e)
        {e.printStackTrace();}
    }

    public static AwsMobileClientUtils getInstance() {
        return awsMobileClientUtils;
    }

    public String getLocalS3FilePath() {
        return localS3FilePath;
    }

    public TransferUtility getTransferUtility() {
        return transferUtility;
    }

    private String createLocalS3FilePath(Context applicationContext) {
        return applicationContext.getFilesDir() + Constants.AMAZON_S3_LOCAL_FILE_DIRECTORY;
    }
}
