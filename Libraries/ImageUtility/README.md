## Project that saves/loads images from amazon web services as well as displays images in image views. ##



# How to integrate this library into your project: #


1) Ensure that you have created a mobile project for your app on Amazon's mobile hub web interface.

https://console.aws.amazon.com/mobilehub/home?region=us-east-1#/

From there you should also ensure that you have configured your project to use the 'User Data Storage' service.

If not then under the 'Add More Backend Features' section click on the 'User Data Storage' option and click on 'Integrate with your app' and follow the outlined steps in "Option 2" of the "Getting Started" guide. You can ingnore the "User Sign-in" and "User Data Storage" sections.
    

2) In the terminal, navigate to your project's root level directory and execute the command:

git submodule add https://bitbucket.org/rhinoactive/imageutility.git ./Libraries/ImageUtility

This adds the ImageUtility submodule to you project.


3) In you app's settings.gradle file add the following lines:

```
#!java


include ":imageutility"
project(":imageutility").projectDir = new File("Libraries/ImageUtility/app")
```


4) In your app's build.gradle file add the following dependency:


```
#!java

dependencies {

    /*
    
    other dependencies
    
    */
    
    compile project(":imageutility")
    
}

```


5) In your app's custom Application class add the following line to the onCreate() method:


```
#!java

AwsFileManager.initAwsMobileClient(getApplicationContext(), AWSConfiguration.AWS_MOBILEHUB_USER_AGENT, AWSConfiguration.AMAZON_COGNITO_REGION, AWSConfiguration.AMAZON_COGNITO_IDENTITY_POOL_ID, AWSConfiguration.AMAZON_S3_USER_FILES_BUCKET, AWSConfiguration.AMAZON_S3_USER_FILES_BUCKET_REGION);
```


This will initialize the Amazon file storage service.

Note: The "AWSConfiguration" class refers to AWSConfiguration file that you created in step 1 which is located in your project and NOT the AWSConfiguration file that is located in the ImageUtility submodule.

- If your project uses other amazon services you will need to include the following line in the onCreate method of your app's custom application class:


```
#!java

AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());
```


In this case there will be two AWSMobileClients, one for the ImageUtility and one for your project. If your app does not use other amazon services then the above line is not needed.



Notes:

You can view the buckets for all your apps here: https://console.aws.amazon.com/s3/home

To view the location of the local directory on your device where amazon will download files, set a breakpoint in your app and in the debugger determine the value of "AwsFileManager.getInstance().fileManager.getLocalContentPath()".

-An example of this directory would be: /data/data/com.rhinoactive.zimity/files/s3_zimity-userfiles-mobilehub-404979933/content



Usage Notes 4/26/20:


**AT THE TIME OF THIS WRITING IT IS IMPERATIVE THAT AN awsconfiguration.json FILE LOCATED AT:
/app/src/main/res/raw/awsconfiguration.json"**

Example awsconfiguration.json file:

```
#!json

{
  "Version": "1.0",
  "CredentialsProvider": {
    "CognitoIdentity": {
      "Default": {
        "PoolId": "POOL_ID",
        "Region": "us-east-1"
      }
    }
  },
  "IdentityManager" : {
    "Default" : {
    }
  },
  "S3TransferUtility": {
    "Default": {
      "Bucket": "BUCKET_NAME",
      "Region": "us-east-1"
    }
  }
}

```




```
#!java

//initialization example
DeveloperAuthenticationProvider provider = new DeveloperAuthenticationProvider(Constants.DEELOPER_ACCOUNT_ID, Constants.AWS_POOL_ID, Regions.US_EAST_1, token.getIdentityId(), token.getToken());
CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(ApplicationClass.getAppContext(), provider, Regions.US_EAST_1);
AwsMobileClientUtils.initAwsMobileClient(this, ECoverApp.getAppContext());
AWSMobileClient.getInstance().setCredentialsProvider(credentialsProvider);


// download file example
String path = "PATH/TO/FILE.jpg" // the bucket key
AmazonFileDownloader downloader = new AmazonFileDownloader(new FileCallbackInterface() {
    @Override
    public void callbackHandler(File file) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(mContext).load(file).into(mMainLogo);
            }
        });
    }
}, null);
downloader.downloadFile(path);

```
