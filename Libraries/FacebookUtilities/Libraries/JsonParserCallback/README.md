## Library that handles the parsing of json data into object classes ##

### To integrate this library into your project ###

Note: If you are using the FacebookUtilities library, then you can ignore integration steps 1-3 as this library is included in the FacebookUtilities as a submodule. https://bitbucket.org/rhinoactive/facebookutilities

1) In the terminal, navigate to your project's root level directory and execute the command:

git submodule add https://bitbucket.org/rhinoactive/jsonparsercallback.git ./Libraries/JsonParserCallback

This adds the JsonParserCallback submodule to you project.

2) In you app's settings.gradle file add the following lines:


```
#!java

include ':jsonparsercallback'
project(':jsonparsercallback').projectDir = new File('Libraries/JsonParserCallback/app')
```


3) In your app's build.gradle file add the following dependency:


```
#!java

dependencies {
/*
other dependencies
*/
compile project(':jsonparsercallback')
}
```


### Example Usage ###

1) There are two types of default parsers available: the JsonObjectParser and the JsonArrayParser. The JsonObjectParser is used when you need to parse a single json object whereas the JsonArrayParser is used when you need to parse an array of json objects.

Depending on your case, create an class that extends one of these classes:

Example:

```
#!java

public class CreateChatParser extends JsonObjectParser<Chat> {

    @Override
    public void handleError(Exception ex) {
        String errorMessage = String.format(Locale.ENGLISH, "Error creating the chat: %s", ex.getMessage());
        Timber.e(errorMessage);
        SeatSwapToastUtils.getInstance().displayToastFromMainThreadLong(Constants.ERROR_OCCURRED_CREATING_CHAT );
    }

    //Return the root level json key
    @Override
    protected String getJsonKey() {
        return Constants.CHAT_TABLE;
    }

    @Override
    protected Chat handleSuccessfulParse(JsonObject jsonElement, GsonBuilder builder) {
        return builder.create().fromJson(jsonElement, Chat.class);
    }

    //You should put logic here that should occur after parsing the object such as saving the object to a local database or broadcasting an event.
    @Override
    protected void postSuccessfulParsingLogic(Chat chat) {
        RealmUtils.saveRealmObject(chat);
        EventBus.getDefault().post(new ChatCreatedEvent(chat));
    }
}
```


```
#!java

public class GetVenueParser extends JsonArrayParser<Venue> {

    @Override
    public void handleError(Exception ex) {
        String errorMessage = String.format(Locale.ENGLISH,"Error getting venues: %s", ex.getMessage());
        Timber.e(errorMessage);
        SeatSwapToastUtils.getInstance().displayToastFromMainThreadLong(Constants.ERROR_OCCURRED_GETTING_VENUES);
    }

    @Override
    protected String getJsonKey() {
        return Constants.VENUE_TABLE;
    }

    @Override
    protected Venue parseSingleElement(JsonElement singleJsonElement, GsonBuilder builder) {
        return builder.create().fromJson(singleJsonElement, Venue.class);
    }

    @Override
    protected void postSuccessfulParsingLogic(List<Venue> parsedObject) {
        RealmUtils.saveList(parsedObject);
        EventBus.getDefault().post(new VenuesUpdatedEvent());
    }

    @Override
    protected void handleNoJsonKeyInResponse(RuntimeException ex) {
        Timber.e("No Venues Found");
    }
}

```


2) Create a method to execute the network request.

Note: The Call class is from the okhttp3 library. https://github.com/square/okhttp

```
#!java

public static void LoginUser(Seat seat) {
    CreateChatParser createChatParser = new CreateChatParser();
    try {
        StandardCallback callback = new HttpStatusCallback(createChatParser);
        Call call = ApiRequests.getInstance().createChat(seat);
        call.enqueue(callback);
    } catch (Exception ex) {
        createChatParser.handleError(ex);
    }
}

//In ApiRequests
public Call createChat(Seat seat) throws Exception {
    String createChatUrl = Constants.CHAT_SEAT + "/" + seat.getSeatId() + "?" + Constants.FULL_TRUE;
    Map<String, Object> params = new HashMap<>();
    params.put(Constants.USER1_ID, CurrentUserManager.getInstance().getCurrentUser().getUserId());
    params.put(Constants.USER2_ID, seat.getSellerId());
    return postRequest(createChatUrl, params);
}
```

Note: If the server you are interacting with returns dynamic response codes, like in the above example, then you can create a class that extends the StandardCallback class to handle the different response codes.

ex:
```
#!java

public class HttpStatusCallback extends StandardCallback {

    public HttpStatusCallback(StandardParser parser) {
        super(parser);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.code() == Constants.UNAUTHORIZED) {
            logoutAndRedirectToLogin();
        } else if (response.code() == Constants.NO_CONTENT) {
            Timber.d("No Content available from the server.");
            return;
        } else {
            super.onResponse(call, response);
        }
        response.body().close();
    }

    // A 401 error code will be returned if the user's session ID is invalid
    private void logoutAndRedirectToLogin() {
        SeatSwapToastUtils.getInstance().displayToastFromMainThreadLong(Constants.SESSION_EXPIRED);
        FacebookLoginManager.getInstance().logoutOfFacebook(new RedirectToLoginAndClearUserLogoutHandler());
    }
}

```