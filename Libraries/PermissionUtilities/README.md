Library that manages the logic associated with requesting permissions with Android API levels 23 and above as permissions are not granted during installation. This library provides functionality for checking whether a user has granted permissions and displays appropriate dialogs that ask the user to grant those permissions

# To integrate this library into your project: #

1) In the terminal, navigate to your project's root level directory and execute the command:

git submodule add https://bitbucket.org/rhinoactive/permissionutilities.git ./Libraries/PermissionUtilities

This adds the ActivityAnimatorUtility submodule to you project.

2) In you app's settings.gradle file add the following lines:

```
#!java


include ':permissionutilities'
project(':permissionutilities').projectDir = new File('Libraries/PermissionUtilities/app')
```


3) In your app's build.gradle file add the following dependency:


```
#!java

dependencies {
/*
other dependencies
*/
compile project(':permissionutilities')
}
```

## To request a permission ##

### There are two types of Permission Managers, the ActivityPermissionManager and the FragmentPermissionManager. They are functionality equivalent. The difference, as the naming would suggest, is that the ActivityPermissionManager is used to request permissions from an activity whereas the FragmentPermissionManager is used to request permissions from a fragment. ###

### This distinction exists so that we can intercept the OnRequestPermissionsResultCallback in the respective Activity or Fragment. ###

In this example, we will write logic that requests a permission from an activity.

1) Create a PermissionManager class variable in your Activity:
The constructor requires that you pass in the requested permission as well as a reference to the activity.

```
#!java

private PermissionManager permissionManager = new ActivityPermissionManager(Permission.WRITE_EXTERNAL_STORAGE_PERMISSION, this);
```

2) Check if the permission is granted before performing logic that requires that permission. If the permission has not been granted then request it:

```
#!java

	public void createPhotoDialogIfPermissionGranted() {
        if (permissionManager.isPermissionGranted()) {
			//Perform logic that requires the WRITE_EXTERNAL_STORAGE_PERMISSION
        } else {
            permissionManager.requestPermissionWithRationalDialog(Constants.PERMISSION_RATIONALE, Constants.PERMISSION_REQUIRED_TITLE);
        }
    }
```

Note: we have two options for requesting a permission. We can also request a permission without a RationaleDialog by calling:

```
#!java
	permissionManager.requestPermission();
```

The reason we would use a rationale dialog is to inform the user as to why we require that permission.


3) Handle the result of requesting the permission. In your activity, override the onRequestPermissionsResult method and call the PermissionManager.handlePermissionResult() method:

```
#!java
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.handlePermissionResult(requestCode, permissions, grantResults, new PermissionRequestResultCallback() {
            @Override
            public void permissionGranted() {
				//Do something if the permission was granted.
			}

            @Override
            public void permissionDenied() {
                permissionManager.displayPermissionDeniedDialog(false, Constants.EXTERNAL_PERMISSION_DENIED, Constants.PERMISSION_REQUIRED_TITLE);
            }
        });
    }
```

Note: You can perform custom logic based on whether the permission was granted or denied. In this case we're displaying a permission denied dialog in the case that the permission was not granted.





