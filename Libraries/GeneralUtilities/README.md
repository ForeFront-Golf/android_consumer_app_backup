## A general utility library. ##

Provides Utility functions for:

-Making API requests

-Animating views

-Creating dialogs

-Making toast messages

And many other functions. Please read the javadoc for each class to learn more.


# To integrate this library into your project: #

1) In the terminal, navigate to your project's root level directory and execute the command:

git submodule add https://bitbucket.org/rhinoactive/generalutilities.git ./Libraries/GeneralUtilities

This adds the GeneralUtilities submodule to you project.

2) In you app's settings.gradle file add the following lines:


```
#!java

include ':generalutilities'
project(':generalutilities').projectDir = new File('Libraries/GeneralUtilities/app')
```


3) In your app's build.gradle file add the following dependency:


```
#!java

dependencies {
/*
other dependencies
*/
compile project(':generalutilities')
}
```

## Below, we will provide examples on how to integrate the most commonly used utilities into your project. ##

# Example usage of the ApiRequestsUtility #

1) Create a class that extends the ApiRequestsUtility class. It is recommended that you make this class into a singleton.


```
#!java

public class ApiRequests extends ApiRequestsUtility {

    private static ApiRequests apiRequests= null;

    private ApiRequests() {}

    public static ApiRequests getInstance() {
        if (apiRequests == null) {
            apiRequests = new ApiRequests();
        }
        return apiRequests;
    }
    
    //Get request
    public Call getSeat(int seatId) throws Exception {
        String ticketUrl = Constants.SEAT_ID + "/" + seatId;
        return getRequest(ticketUrl);
    }
    
    //Put request
    public Call modifyChat(Chat chat, SeenCompleteElement seenCompleteElement) throws Exception {
        String modifyChatUrl = Constants.CHAT + "/" + chat.getChatId();
        Map<String, Object> params = new HashMap<>();
        params.put(seenCompleteElement.getElement(), true);
        return putRequest(modifyChatUrl, params);
    }
    
    //Post request
    public Call createChat(Seat seat) throws Exception {
        String createChatUrl = Constants.CHAT_SEAT + "/" + seat.getSeatId() + "?" + Constants.FULL_TRUE;
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.USER1_ID, CurrentUserManager.getInstance().getCurrentUser().getUserId());
        params.put(Constants.USER2_ID, seat.getSellerId());
        return postRequest(createChatUrl, params);
    }
    
    
//    ... more methods

    @Override
    protected String getServerUrl() {
        return Constants.SERVER_URL;
    }

    @Override
    protected String getAuthorizationValue() {
        String authorizationValue = "";
        Session currentSession = CurrentUserManager.getInstance().getCurrentSession();
        if (currentSession != null && currentSession.getSessionId() != null) {
            authorizationValue = currentSession.getSessionId();
        }
        return  authorizationValue;
    }

    @Override
    protected String getAppVersionNumber() {
        return BuildConfig.VERSION_NAME;
    }
}
```

Note: This library provides a default OkHttpClient instance that is generated using the DefaultHttpClientGenerator.getInstance() method. You can use your own OkHttpClient if you need to customize the defaul logic, for example if you need to modify the timeout values. Just override the configureHTTPClient() method in your concrete ApiRequestsUtility class and return your OkHttpClient instance.

Ex:

```
#!java

@Override
protected OkHttpClient configureHTTPClient() throws GeneralSecurityException {
    return YourOkHttpClient.getInstance();
}
```


# Example of how to animate views as they appear/disappear from the screen #

To animate a view as it becomes visible on the screen, call the AnimationUtils.showAnimation(View, ShowViewAnimation, int) method. Choose the desired animation from the ShowViewAnimation class.

```
#!java

AnimationUtils.getInstance().showAnimation(overlayContentLayout, ShowViewAnimation.SLIDE_UP, ANIM_DURATION);
```

To animate a view as it disappears from the screen, call the AnimationUtils.hideAnimation(View, HideViewAnimation, int) method. Choose the desired animation from the HideViewAnimation class.

```
#!java

AnimationUtils.getInstance().hideAnimation(overlayContentLayout, HideViewAnimation.SLIDE_DOWN, ANIM_DURATION);
```

# Example of how to create custom dialogs #

You can use the DialogUtils right out of the box to create dialogs but it is recommended that you create an additional class that handles the creation of dialogs. This is so all of the dialogs in your app look and feel consistent.

Ex.

```
#!java

public class SeatSwapDialogUtils {

    private static DialogUtils dialogUtils;

    public static DialogUtils getInstance() {
        if (dialogUtils == null) {
            SeatSwapResourceUtils seatSwapResourceUtils = SeatSwapResourceUtils.getInstance();
            DialogUtils.DialogUtilsBuilder dialogUtilsBuilder = new DialogUtils.DialogUtilsBuilder();
            dialogUtilsBuilder.titleColor(seatSwapResourceUtils.colorRes(R.color.text_color));
            dialogUtilsBuilder.positiveTextColor(seatSwapResourceUtils.colorRes(R.color.seat_sea_blue));
            dialogUtilsBuilder.negativeTextColor(seatSwapResourceUtils.colorRes(R.color.seat_sea_blue));
            dialogUtilsBuilder.backgroundColor(seatSwapResourceUtils.colorRes(R.color.white));
            dialogUtilsBuilder.contentColor(seatSwapResourceUtils.colorRes(R.color.text_color));
            dialogUtils = dialogUtilsBuilder.build();
        }
        return dialogUtils;
    }
}
```

Use this class to get a global instance of the DialogUtils and create a dialog by calling the desired function on the DialogUtils instance.

Ex:

```
#!java

SeatSwapDialogUtils.getInstance().showYesNoDialog(context, resourceUtils.strRes(R.string.confirm_complete_ticket), createCompleteSeatClickListener());
```

# Example of how to make toasts from anywhere in the app #

Extend the ToastUtils class and and override the getAppContext() method to return the app context. It is recommended that you make this class a singleton.

Ex:

```
#!java

public class SeatSwapToastUtils extends ToastUtils {

    private static SeatSwapToastUtils seatSwapToastUtils = null;

    private SeatSwapToastUtils() {}

    public static SeatSwapToastUtils getInstance() {
        if (seatSwapToastUtils == null) {
            seatSwapToastUtils = new SeatSwapToastUtils();
        }
        return seatSwapToastUtils;
    }

    @Override
    protected Context getAppContext() {
        return SeatSwapApp.getAppContext();
    }
}

```

Use this class to create toasts with ease. Example:

```
#!java

SeatSwapToastUtils.getInstance().displayToastFromMainThreadLong(Constants.ERROR_OCCURRED_COMPLETING_TICKET);
```

# Example of how to retrieve resources from various resource files #

This utility class simplifies the retrieval of various resources such as strings, arrays, colors, and drawables. Extend the ResourceUtils class and and override the getAppContext() method to return the app context. It is recommended that you make this class a singleton.

```
#!java

public class SeatSwapResourceUtils extends ResourceUtils {

    private static SeatSwapResourceUtils seatSwapResourceUtils;

    public static SeatSwapResourceUtils getInstance() {
        if (seatSwapResourceUtils == null) {
            seatSwapResourceUtils = new SeatSwapResourceUtils();
        }
        return seatSwapResourceUtils;
    }

    @Override
    protected Context getAppContext() {
        return SeatSwapApp.getAppContext();
    }
}
```

Use this class the retrieve various resources with ease. Example:

```
#!java

SeatSwapResourceUtils resourceUtils = SeatSwapResourceUtils.getInstance();
int colorPink = resourceUtils.colorRes(R.color.seat_sea_dark_pink);
```

# Example of how to use the AdvancedRadioGroup #

With Android's default RadioGroup, RadioButtons need to be direct children in order to work properly. This does not allow for much visual customization. The AdvancedRadioGroup remedy's this shortcoming. With the AdvancedRadioGroup, RadioButtons can be nested anywhere in the AdvancedRadioGroup while still allowing only one radio button to be checked.

Ex:

```
#!xml

<com.rhinoactive.generalutilities.views.AdvancedRadioGroup
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:layout_marginBottom="1dp"
        android:background="@color/white">

        <com.rhinoactive.foreorder_library_android.views.BlueRadioButton
            android:id="@+id/radiobtn_single_option"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginLeft="21dp"
            android:layout_marginRight="19dp"
            android:layout_centerVertical="true"
            android:clickable="false"/>

        <TextView
            android:id="@+id/textview_club_item_name"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Club name"
            android:layout_toRightOf="@id/radiobtn_single_option"
            android:textColor="@color/fore_order_blue"
            android:textSize="17dp"
            android:layout_marginRight="21dp"
            android:scrollHorizontally="true"
            android:ellipsize="end"
            android:maxLines="1"
            android:layout_centerVertical="true"/>

    </RelativeLayout>
    
    <!-- More Radio Buttons -->

</com.rhinoactive.generalutilities.views.AdvancedRadioGroup>
```

You also need to make sure you set clickable to false on the radio buttons.

Ex:

```
#!xml

<RadioButton
       ...
        android:clickable="false"/>
```

