# A utility library that contains redundant logic for both the Android consumer and employee apps #

## To integrate this library into your project: ##

1) In the terminal, navigate to your project's root directory execute the command:

git submodule add https://bitbucket.org/rhinoactive/foreorder-library-android.git ./Libraries/foreorder-library-android

This adds the foreorder-library-android submodule to you project.

2) In you app's settings.gradle file add the following lines:


```
#!java

include ':foreorder-library-android'
project(':foreorder-library-android').projectDir = new File('Libraries/foreorder-library-android/app')
```


3) In your app's build.gradle file add the following dependency:


```
#!java

dependencies {
/*
other dependencies
*/
compile project(':foreorder-library-android')
}
```