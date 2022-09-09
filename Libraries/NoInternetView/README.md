View that appears which informs users that the device has lost connection to the internet.

# To integrate this library into your project: #

1) In the terminal, navigate to your project's root level directory and execute the command:

git submodule add https://bitbucket.org/rhinoactive/nointernetview.git ./Libraries/NoInternetView

This adds the NoInternetToolbar submodule to you project.

2) In you app's settings.gradle file add the following lines:

```
#!java


include ':nointernetview'
project(':nointernetview').projectDir = new File('Libraries/NoInternetView/app')
```


3) In your app's build.gradle file add the following dependency:


```
#!java

dependencies {
/*
other dependencies
*/
compile project(':nointernetview')
}
```

### Using this view is very easy, simply add the NoInternetView to your layout. ###

```
#!xml

<com.rhinoactive.nointernetview.NoInternetView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>
```

Note: It is recommended that you put the NoInternetView in a custom toolbar view so that it can be resued without too much duplication.

Complete Example:

standard_toolbar_layout.xml
```
#!xml

<?xml version="1.0" encoding="utf-8"?>
<android.support.v7.widget.Toolbar xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    tools:ignore="MissingPrefix"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:contentInsetStart="0dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <com.rhinoactive.nointernetview.NoInternetView
            android:id="@+id/no_internet_view_toolbar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"/>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:orientation="horizontal">


            ...


        </LinearLayout>

    </LinearLayout>

</android.support.v7.widget.Toolbar>
```
