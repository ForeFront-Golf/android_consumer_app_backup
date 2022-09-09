Utility Library to handle logic associated with the Facebook API such as logging in/out and pulling data (albums, tagged user photos, uploaded user photos, and work experience) from Facebook.


# **To integrate this library into your project:** #

Note: This project includes the JsonParserCallback library as a nested submodule. https://bitbucket.org/rhinoactive/jsonparsercallback/overview

1) In the terminal, navigate to your project's root level directory and execute the command:

git submodule add https://bitbucket.org/rhinoactive/facebookutilities.git ./Libraries/FacebookUtilities

This adds the FacebookUtilities submodule to you project.

2) The JsonParserCallback library is required by FacebookUtilities. To add this library to your project, navigate to the FacebookUtilities directory you just created and execute the following commands:

git rm --cached Libraries/JsonParserCallback/

rm -rf Libraries/JsonParserCallback/

git submodule add https://bitbucket.org/rhinoactive/jsonparsercallback.git ./Libraries/JsonParserCallback

This adds the JsonParserCallback submodule to FacebookUtilities

3) In you app's settings.gradle file add the following lines:

```
#!java

include ':facebookutilities'
project(':facebookutilities').projectDir = new File('Libraries/FacebookUtilities/app')
include ':jsonparsercallback'
project(':jsonparsercallback').projectDir = new File('Libraries/FacebookUtilities/Libraries/JsonParserCallback/app')
```

4) In your app's build.gradle file add the following dependency:

```
#!java

dependencies {
/*
other dependencies
*/
compile project(':facebookutilities')
}
```

5) If you haven't done so, add your app to your Facebook developer page in order to get a Facebook App Id. Go here: https://developers.facebook.com/apps/ add a new app and follow the outlined steps in the Android QuickStart guide to get the Facebook api set up with your project.



# To Integrate Facebook Login: #

1) Create a class that extends the FacebookLoginButtonCallback class. All this class does is return a custom message. This message will be displayed to the user if they did not enable Facebook permissions that were required by the app during login.

Ex.

```
#!java

public class SeatSwapLoginButtonCallback extends FacebookLoginButtonCallback {
    public SeatSwapLoginButtonCallback(Activity activity, String loginUrl, View loginView, List<FacebookPermission> requiredPermissions, LoginCallback loginCallback, LogoutHandler logoutHandler) {
        super(activity, loginUrl, loginView, requiredPermissions, loginCallback, logoutHandler);
    }

    @Override
    protected String requiredPermissionNotGrantedToastMessage() {
        return "You did not grant the required permissions.";
    }
}

```

2) Create a class that extends the LoginParser. Write logic to determine if the login was successful and logic to handle successful and unsuccessful logins.

Ex.

```
#!java

public class FacebookLoginParser extends LoginParser {

    private User user;
    private Activity previousActivity;

    public FacebookLoginParser(Activity previousActivity) {
        this.previousActivity = previousActivity;
    }

    @Override
    protected boolean wasLoginSuccessful(JsonObject obj, GsonBuilder builder) {
        JsonObject usrObj = obj.getAsJsonObject(Constants.USER_TABLE);
        user = builder.create().fromJson(usrObj, User.class);
        return user != null;
    }

    @Override
    protected void handleSuccessfulLogin(JsonObject obj, GsonBuilder builder) {
        JsonObject facebookUserObj = obj.getAsJsonObject(Constants.FACEBOOK_USER_TABLE);
        final FacebookUser facebookUser = builder.create().fromJson(facebookUserObj, FacebookUser.class);
        uploadDefaultProfilePhotoIfNeeded(facebookUser);
        JsonObject sessionObj = obj.getAsJsonObject(Constants.SESSION_TABLE);
        final Session session = builder.create().fromJson(sessionObj, Session.class);
        CurrentUserManager.getInstance().setUpCurrentUserSession(user, facebookUser, session);
        proceedToHomeScreen();
    }

    // These functions upload the user's current facebook profile image to amazon and uses it as the user's
    // default profile image for the app if the user doesn't currently have a profile image.
    // These methods can be removed if the app doesn't require a default profile image.
    private void uploadDefaultProfilePhotoIfNeeded(FacebookUser facebookUser) {
        String profileS3Path = user.createProfilePhotoS3Path();
        if (user.getProfilePhotoUrl() == null) {
            uploadFacebookProfile(facebookUser, profileS3Path);
        }
    }

    private void uploadFacebookProfile(FacebookUser facebookUser, String profileS3Path) {
        try {
            String imgLink = facebookUser.getProfileImageUrl();
            CallbackInterface callback = new AmazonProfilePhotoUploadCallback(profileS3Path);
            AmazonImageUploader amazonImageUploader = new AmazonImageLinkUploader(imgLink);
            amazonImageUploader.uploadUserImageToAmazon(profileS3Path, callback);
        } catch (Exception ex) {
            String errorMsg = String.format("Error uploading profile photo from facebook: %s", ex.getMessage());
            Timber.e(errorMsg);
        }
    }

    @Override
    protected void handleFailedLogin(JsonObject obj, GsonBuilder builder) {
        Timber.e(obj.toString());
        logoutAndReturnToLogin();
    }

    @Override
    protected void requestFailed(Exception ex) {
        Timber.e(ex.getMessage());
        logoutAndReturnToLogin();
    }

    @Override
    protected void errorParsingResponse(Exception ex) {
        Timber.e(ex.getMessage());
        logoutAndReturnToLogin();
    }

    private void logoutAndReturnToLogin() {
        Timber.e("Failed to log the user in.");
        SeatSwapToastUtils.getInstance().displayToastFromMainThreadLong("Login Failed");
        ActivityAndAnimateManager.proceedToActivityAndClearStack(previousActivity, LoginActivity.class, ActivityAnimationType.NONE);
        FacebookLoginManager.getInstance().logoutOfFacebook(new ClearCurrentUserManagerLogoutHandler());
    }

    private void proceedToHomeScreen() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ActivityAndAnimateManager.proceedToActivityAndClearStack(previousActivity, VenueListingActivity.class, ActivityAnimationType.FADE_IN);
            }
        });
    }
}
```


3) (Optional step) If you need to perform additional logic on logout (such as invalidating the current user session like in the example above) then create a class that extends the LogoutHandler.

Ex.

```
#!java

public class ClearCurrentUserManagerLogoutHandler extends LogoutHandler {

    @Override
    public void logout() {
        super.logout();
        CurrentUserLocationTracker.disconnectUserLocationUpdateCallback();
        OneSignalUtils.sendLogoutUserToOneSignal();
        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();
        if (currentUserManager.getCurrentUser() != null) {
            AccountApiManager.invalidateCurrentUserSession();
            currentUserManager.clearCurrentUser();
        }
    }
}
```


4) Create a Login Activity.


```
#!java

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
```



5) Put a login button view into your login activity's layout. 

Ex. 

```
#!xml

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_login"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    ...

    <com.facebook.login.widget.LoginButton
        android:id="@+id/fb_login_btn"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
        
    ...

</RelativeLayout>

```


6) Add logic to your Login Activity to allow users to login. Use the classes you created in the previous steps.

Ex.

```
#!java

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookLoginManager facebookLoginManager = FacebookLoginManager.getInstance();
        String loginUrl = "https://kardiem.ddmappdesign.com:8080/login?full=true";
        //FacebookLoginParser created in step 2
        LoginCallback loginCallback = new LoginCallback(new FacebookLoginParser(LoginActivity.this));
        final LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_btn);
        initLoginButton(loginButton, loginCallback);

        if (facebookLoginManager.isLoggedIn()) {
            facebookLoginManager.logUserIn(loginUrl, loginCallback);
            loginButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookLoginManager.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    private void initLoginButton(LoginButton loginButton, LoginCallback loginCallback) {
        loginButton.setReadPermissions(Arrays.asList(FacebookPermission.PUBLIC_PROFILE.getName(), FacebookPermission.EMAIL_PERMISSION.getName()));
        List<FacebookPermission> requiredPermissions = new ArrayList<>();
        String loginUrl = ApiRequests.getInstance().getFacebookLoginUrl();
        //ClearCurrentUserManagerLogoutHandler created in step 3
        LogoutHandler logoutHandler = new ClearCurrentUserManagerLogoutHandler();
        //SeatSwapLoginButtonCallback created in step 1
        FacebookLoginButtonCallback loginButtonCallback = new SeatSwapLoginButtonCallback(this, loginUrl, loginButton, requiredPermissions, loginCallback, logoutHandler);
        FacebookLoginManager.getInstance().registerLoginButton(loginButton, loginButtonCallback);
    }
}

```

Note: You can use a custom button for facebook login if needed. Any view can act as the login button. To do this call FacebookLoginManager.registerCustomLoginButton() instead of FacebookLoginManager.registerLoginButton() and pass in the required parameters.

ex.

```
#!java

private void initLoginButton(View loginView, LoginCallback loginCallback) {
    List<String> requestedPermissions = new ArrayList<>();
    requestedPermissions.addAll(Arrays.asList(FacebookPermission.PUBLIC_PROFILE.getName(), FacebookPermission.EMAIL_PERMISSION.getName()));
    List<FacebookPermission> requiredPermissions = new ArrayList<>();
    String loginUrl = ApiRequests.getInstance().getFacebookLoginUrl();
    LogoutHandler logoutHandler = new ClearCurrentUserManagerLogoutHandler();
    FacebookLoginButtonCallback loginButtonCallback = new SeatSwapLoginButtonCallback(this, loginUrl, loginView, requiredPermissions, loginCallback, logoutHandler);
    FacebookLoginManager.getInstance().registerCustomLoginButton(this, loginView, loginButtonCallback, requestedPermissions);
}
```

## About Facebook Permissions ##

There are a number of permissions you can request on facebook login. 

Please see the official documentation for more information about permission and what data they return. https://developers.facebook.com/docs/facebook-login/permissions/


# To Integrate Facebook Logout: #

Logging out is quite simple, simply add a click listener to your logout button and call FacebookLoginManager.logoutOfFacebook(LogoutHandler)

```
#!java

private void initLogoutButton() {
    RelativeLayout logoutButtonLayout = findViewById(R.id.rlayout_logout);
    if (logoutButtonLayout != null) {
        logoutButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookLoginManager.getInstance().logoutOfFacebook(new RedirectToLoginAndClearUserLogoutHandler());
            }
        });
    }
}
```

In the above example we are using a class that extends the ClearCurrentUserManagerLogoutHandler class we created early in this ReadMe.

This class does all the things the ClearCurrentUserManagerLogoutHandler does in addition to redirecting the user to the login screen.

```
#!java

public class RedirectToLoginAndClearUserLogoutHandler extends ClearCurrentUserManagerLogoutHandler {

    @Override
    public void logout() {
        super.logout();
        ActivityAndAnimateManager.proceedToActivityAndClearStackWithoutAnimation(SeatSwapApp.getAppContext(), LoginActivity.class);
    }
}
```

# To get user work experience from Facebook: #

1) Create a class that implements RetrieveWorkExperiencesCallback.

Ex.

```
#!java

public class RetrieveCurrentUserWorkExperienceCallback implements RetrieveWorkExperiencesCallback {

    @Override
    public void handleWorkExperiencesRetrieved(List<WorkExperience> jobs) {
        FacebookUser currentFbUser = CurrentUserManager.getInstance().getCurrentUser().getFbUser();
        currentFbUser.setWork(jobs);
        RealmUtils.saveList(WorkExperience);
    }
}
```

This functionality will be called when the request to get the list of work experiences returns.

2) Call FacebookUserInfoManager.getUserWorkInformation(RetrieveWorkExperiencesCallback) to get the list of work experiences.

Note: A good place to call this method is during a sucecssful login, ie in the FacebookLoginParser class that we created eariler.

Ex.

```
#!java

public class FacebookLoginParser extends LoginParser {

    ...

    @Override
    protected void handleSuccessfulLogin(JsonObject obj, GsonBuilder builder) {
    
        ...
        
        FacebookUserInfoManager.getInstance().getUserWorkInformation(new RetrieveCurrentUserWorkExperienceCallback());
        
        ...
        
    }
    
    ...
    
}

```

# Get user photos from Facebook #

### To get all of the users images ###

1) Subcribe to the FacebookImagesDownloadEvent in your Activity/Fragment class using the event bus.

ex.

```
#!java

@Subscribe
public void onFacebookImagesDownloadedEvent(FacebookImagesDownloadEvent event) {
    photoAdapter.notifyItemRangeChanged(0, FacebookPhotoManager.getInstance().getCurrentUserFbPhotosAndPagers().getFbPhotos().size());
}

```

In the above example, we are updating an adapter that displays the photos when the request to get the Facebook images returns.

2) Call FacebookPhotoManager.getFbPhotos(Context)

ex.

```
#!java

private void loadFbPhotos() {
    FacebookPhotoManager.getInstance().getFbPhotos(activity);
}

```

Note: The first time you call this method, it will get the first 30 photos that the user has uploaded as well as the first 30 photos that the user has been tagged in. To get the next 30 images simply call this method again once the initial request finishes.

A good place to get the subsequent Facebook photos would in be in the onFacebookImagesDownloadedEvent method that we defined above.

ex.

```
#!java

@Subscribe
public void onFacebookImagesDownloadedEvent(FacebookImagesDownloadEvent event) {
    photoAdapter.notifyItemRangeChanged(0, FacebookPhotoManager.getInstance().getCurrentUserFbPhotosAndPagers().getFbPhotos().size());
    FacebookPhotoManager.getInstance().getFbPhotos(activity);
}

```

### To get a specific Facebook photo album ###

TODO

