# Library that handles the logic associated with tracking the user's location #

## To integrate this library into your project: ##

1) In the terminal, navigate to your project's root level directory and execute the command:

git submodule add https://bitbucket.org/rhinoactive/locationutilities.git ./Libraries/LocationUtilities

This adds the LocationUtilities submodule to you project.

2) In you app's settings.gradle file add the following lines:


```
#!java

include ':locationutilities'
project(':locationutilities').projectDir = new File('Libraries/LocationUtilities/app')
```


3) In your app's build.gradle file add the following dependency:

```
#!java

dependencies {
/*
other dependencies
*/
compile project(':locationutilities')
}
```

## Example of how to send user location updates to a server ##

1) Create a class that implements the google location listener. Example:

```
#!java

public class UserLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        CurrentUserManager.getInstance().setLocation(location);
        final User currentUser = CurrentUserManager.getInstance().getCurrentUser();
        //This method sends the location data to the server
        UserLocationNetworkRequestManager.updateUserLocationOnServer(currentUser.getUserId(), location);
    }
}
```

2) Create a class that extends the GoogleLocationTracker class.
   Note, this class should be the only place that you need to use the GoogleLocationTrackerUtils. Example:

```
#!java

public class CurrentUserLocationTracker extends GoogleLocationTracker{

    private static CurrentUserLocationTracker currentUserLocationTracker = null;

    private static int updateInterval;

    private CurrentUserLocationTracker(Context context) {
        super(context);
    }

    public static synchronized void connectInstanceIfNotAlreadyStarted(Context context, TimeBetweenLocationUpdates timeBetweenLocationUpdates) {
        if (GoogleLocationTrackerUtils.isInstanceNull(currentUserLocationTracker)) {
            updateInterval = timeBetweenLocationUpdates;
            currentUserLocationTracker = new CurrentUserLocationTracker(context);
        }
        GoogleLocationTrackerUtils.connectIfNotAlreadyStarted(currentUserLocationTracker);
    }

    public static synchronized void disconnectUserLocationUpdateCallback() {
        GoogleLocationTrackerUtils.disconnectGoogleApiClient(currentUserLocationTracker);
        currentUserLocationTracker = null;
    }
    
    public static void resetUserTracking(Context context, TimeBetweenLocationUpdates timeBetweenLocationUpdates) {
        disconnectUserLocationUpdateCallback();
        connectInstanceIfNotAlreadyStarted(context, timeBetweenLocationUpdates);
    }

    // This method gets called once after the google location service is created.
    @Override
    protected void updateUserLocations(Location location) {
        CurrentUserManager.getInstance().setLocation(location);
        EventBus.getDefault().post(new UserLocationUpdatesStartedEvent());
    }

    // Return the LocationListener object that you created in step 1.
    @Override
    protected LocationListener createLocationListener() {
        return new UserLocationListener();
    }

    @Override
    protected void userMissingLocationPermission(SecurityException ex) {
        Timber.e("User has not yet allowed the app to track the their location.");
    }

    @Override
    protected long getTimeBetweenUpdatesInMilliseconds() {
        return updateInterval;
    }
}
```

3) To start tracking the user's location execute the following command:


```
#!java

CurrentUserLocationTracker.connectInstanceIfNotAlreadyStarted(TrackUserLocationsActivity.this, updateInterval);
```

This should be done in your activity/fragment class.

In order for location updates to work, Google Play Services should be up to date. Also, GPS needs to be enabled and the location permission needs to be granted by the user.

Here is an example of how to properly check Google Play Services availability, request the location permission, enable gps, and connect the instance of your user tracking service.

In the example, we are starting the user tracking in the onStart method to handle the case where the gps is disabled. In this case, the request gps dialog will appear. Once the user selects whether to enable the gps or not, the activity/fragment will resume and the user tracking will immediately start if the user enabled their gps. 

This example uses the PermissionUtilities library to request the location permission. https://bitbucket.org/rhinoactive/permissionutilities


```
#!java

public class TrackUserLocationsActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback  {

    private PermissionManager permissionManager;

    @Override
    public void onStart() {
        super.onCreate();
        
        ...
        
        startTrackingLocation();
    }
    
    private void startTrackingLocation() {
        GoogleApiAvailabilityUtils.displayErrorIfGooglePlayServicesUnavailable(this);
        
        permissionManager = new PermissionManager(Permission.LOCATION_PERMISSION, this);
        if (!permissionManager.isPermissionGranted()) {
            permissionManager.requestPermissionWithRationalDialog(Constants.LOCATION_PERMISSION_RATIONALE, Constants.LOCATION_RATIONALE_TITLE);
        }

        boolean isGpsEnabled = GpsUtils.isGpsEnabled(this);
        if (isGpsEnabled) {
            resetLocationUpdates();
        } else {
            GpsUtils.buildDialogForNoGps(this, ZimityDialogUtils.getInstance(), Constants.GPS_NOT_ENABLED);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.handlePermissionResult(requestCode, permissions, grantResults, new PermissionRequestResultCallback() {
            @Override
            public void permissionGranted() {
                resetLocationUpdates();
            }

            @Override
            public void permissionDenied() {
                permissionManager.displayPermissionDeniedDialog(true, Constants.LOCATION_PERMISSION_DENIED, Constants.LOCATION_PERMISSION_REQUIRED);
            }
        });
    }
    
    public void resetLocationUpdates() {
        CurrentUserLocationTracker.resetUserTracking(this, TimeBetweenLocationUpdates.AT_EVENT);
    }
}
```

4) To stop your user tracking service (for example when a user signs out) execute the command:

```
#!java

CurrentUserLocationTracker.disconnectUserLocationUpdateCallback();
```

This can be done in your Activity's onDestory method or in a logout function.

```
#!java

    @Override
    public void onDestroy() {
        CurrentUserLocationTracker.disconnectUserLocationUpdateCallback();
        super.onDestroy();
    }
```

Note: If you don't call this method then location updates will continue until the app is terminated. 

## How to send user location updates in the background (ie. When the app isn't open) ##

This method will start location updates when the user turns on or unlocks their phone.

1) Create a class that extends the LocationUpdateService. Example:


```
#!java

public class UserLocationUpdateService extends LocationUpdateService {

    // User needs to be signed in before we can send location updates for that user.
    @Override
    protected boolean isUserSignedIn() {
        CurrentUserManager currentUserManager = CurrentUserManager.getInstance();
        currentUserManager.setCurrentUserSessionFromSavedPrefs();
        return currentUserManager.getCurrentUser() != null && currentUserManager.getCurrentSession() != null;
    }

    // Connect the instance of your user tracking service here.
    @Override
    protected void startTracking(Context context) {
        CurrentUserLocationTracker.connectInstanceIfNotAlreadyStarted(context, TimeBetweenLocationUpdates);
    }
}
```

2) Create a class that extends the LocationUpdateReceiver. Example:

```
#!java

public class UserLocationUpdateReceiver extends LocationUpdateReceiver {

    // Return this class.
    @Override
    protected Class<? extends LocationUpdateService> getServiceImplementationClass() {
        return UserLocationUpdateService.class;
    }
}
```

3) Add your receiver and service to the AndroidManifest.xml file. Example:

```
#!xml

<receiver
    android:name=".receivers.UserLocationUpdateReceiver"
    android:enabled="true"
    android:label="UserLocationUpdateReceiver">
    <intent-filter>
        <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
        <action android:name="android.intent.action.USER_PRESENT" />
        <action android:name="android.intent.action.BOOT_COMPLETED" />
        <action android:name="android.intent.action.QUICKBOOT_POWERON" />
    </intent-filter>
</receiver>

<service
    android:name=".services.UserLocationUpdateService"
    android:enabled="true" />
```

And that's it! The user's location will be tracked even when the app isn't open provided that the user's session that is stored in the saved preferences is still valid.