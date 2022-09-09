## Utility library to handle logic associated with animating activity or fragment transitions. ##

## Also provides logic that simplifies the creation of external thrid party activities. ##

# To integrate this library into your project: #

1) In the terminal, navigate to your project's root level directory and execute the command:

git submodule add https://bitbucket.org/rhinoactive/activityanimatorutility.git ./Libraries/ActivityAnimatorUtility

This adds the ActivityAnimatorUtility submodule to you project.

2) In you app's settings.gradle file add the following lines:

```
#!java


include ':activityanimatorutility'
project(':activityanimatorutility').projectDir = new File('Libraries/ActivityAnimatorUtility/app')
```


3) In your app's build.gradle file add the following dependency:


```
#!java

dependencies {
/*
other dependencies
*/
compile project(':activityanimatorutility')
}
```

## To animate activity transitions ##

Use the ActivityAndAnimateManager class to build and execute a transition to a new activity.

Example:

```
#!java

ActivityAndAnimateManager.Builder builder = new ActivityAndAnimateManager.Builder(activity, ExampleActivity.class, AnimationType.SLIDE_LEFT);
builder.stringExtra(ImageViewerActivity.S3_PATH_JSON_KEY, s3Path);
builder.buildActivityAndTransition();
```

The above example will start the ExampleActivity by sliding left along the screen. The s3Path will also be passed to the intent data of the ExampleActivity.

Note: builder.stringExtra(String, String) is just one of the available build options. There are a number of other build options available, please read the javadocs for a complete list.

### To animate the transition back to a previous activity on the stack ###

In order to animation the transition between activities when the user presses the back button, call:

```
#!java

ActivityAnimationUtils.animateActivityTransition(Activity, AnimationType);
```

just after:

```
#!java

super.onBackPressed();
```

In your overriding activity class.

Example:

```
#!java

@Override
public void onBackPressed() {
    super.onBackPressed();
    ActivityAnimationUtils.animateActivityTransition(ExampleActivity.this, AnimationType.SLIDE_RIGHT);
}

```

In the above example, when the user presses the back button while on the ExampleActivity screen, they will navigate back to the previous activity by sliding right along the screen.

## How to start external activities ##

There are a number of external activities that this library simplifies the creation thereof. Look into the externalactivities folder for a complete list of external activities.

Example of creating an external link activity:

```
#!java

ExternalActivity externalActivity = new ExternalLinkActivity(activity, "www.google.com");
externalActivity.start();

```
### How to start an external activity that retruns a result ###

Some external activities, such as the camera or gallery activity, return a result to the calling activity or fragment.

These classes have three constructors that accept either an activity, a fragment, or a support fragment. Depending on the constructor you use, the external activity will return the results to the onActivityResult method of either the activity, frgament, or support fragment respectively.

Example of creating an external gallery activity that will return its result data to a fragment:

```
#!java

private void startGalleryActivity() {
    ExternalActivity externalGalleryActivity = new ExternalGalleryActivity(fragment);
    externalGalleryActivity.start();
}

```

Then intercept the results in the fragment's onActivityResult(int, int, Intent) method.

```
#!java

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode==RESULT_OK && requestCode == com.rhinodesktop.activityanimatorutility.Constants.PICK_IMG_RQST) {
    
    //Logic ...
    
    }
}

```

## How to animate fragment transitions ##

There are two classes available for animating fragment transitions, the FragmentAndAnimateManager and the SupportFragmentAnimateManager. The difference, as the naming would suggest, is that the FragmentAndAnimateManager animates native fragment transitions while the SupportFragmentAnimateManager animates support fragment transitions.

See https://stackoverflow.com/questions/15109017/difference-between-android-app-fragment-and-android-support-v4-app-fragment for more information on fragments vs. support fragments.

This distinction exists because native fragment animation transitions are done using animators,

https://developer.android.com/reference/android/animation/Animator.html

while support fragment animations are done using animations. 

https://developer.android.com/reference/android/view/animation/Animation.html

Unless you need to support devices with an API level of 10 or below, it is recommended that you use native fragments in your app instead of support fragments. 

### Example flow for animating transitions between native fragments ###

First add the fragment to the containing resource view in your Activity.

```
#!java

private void addFragment() {
    FragmentTransactionInfo fragmentTransactionInfo = new FragmentTransactionInfo(Constants.EXAMPLE_FRAGMENT_TAG, null, false);
    FragmentInfo fragmentInfo = new FragmentInfo(getFragmentManager(), exampleFragment, R.id.main_fragment_container, fragmentTransactionInfo);
    FragmentAndAnimateManager.getInstance().addFragment(fragmentInfo);
}

```

Note: The FragmentTransactionInfo constructor accepts the following arguments:

tag: The optional tag name for the fragment, can be used later to retrieve the fragment with FragmentManager.findFragmentByTag(String).

transName: The optional name of this transaction, can be used as the string parameter for FragmentManager.popBackStack(String, int) to pop that transaction from the back stack.

addToBackStack: True if this fragment transaction is to be added to the back stack.



Then change the support fragment contained in the resource view by doing: 

```
#!java

private void swapFragment() {
    FragmentTransactionInfo fragmentTransactionInfo = new FragmentTransactionInfo(Constants.OTHER_FRAGMENT_TAG, null, true);
    FragmentInfo fragmentInfo = new FragmentInfo(getFragmentManager(), otherFragment, Constants.main_fragment_container, fragmentTransactionInfo);
    FragmentAndAnimateManager.getInstance().changeFragment(fragmentInfo, AnimatorType.SLIDE_LEFT, AnimatorType.SLIDE_RIGHT);
}

```

Specifying the animation that will occur when the new fragment is added as well as the animation that will occur when the fragment is popped from the back stack.

Note: In order to animate the transition of a Fragment by sliding it horizontally, you must contain your fragment in the HorizontallySlidableFragmentFrameLayout. See https://stackoverflow.com/a/4936159 for more detail.


### Example flow for animating transitions between support fragments ###

First add the support fragment to the containing resource view in your Activity.

```
#!java

private void addFragment() {
    Fragment fragment = new ExampleFragment();
    SupportFragmentAnimateManager.getInstance().addSupportFragment(getSupportFragmentManager(), fragment, R.id.fragment_container);
}

```

Then change the support fragment contained in the resource view by doing: 

```
#!java

private void swapFragment() {
    Fragment fragment = new otherFragment();
    SupportFragmentAnimateManager.getInstance().changeSupportFragment(getSupportFragmentManager(), fragment, AnimationType.SLIDE_LEFT, R.id.fragment_container);
}

```



