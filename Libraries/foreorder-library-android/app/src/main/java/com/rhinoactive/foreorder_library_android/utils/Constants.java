package com.rhinoactive.foreorder_library_android.utils;

/**
 * Created by Hunter Andrin on 2017-04-28.
 */

public class Constants {

    public final static int CONFLICT = 409;
    public final static int UNAUTHORIZED = 401;
    public final static int NO_CONTENT = 204;

    public static final int ANIM_DURATION = 200;
    public static final String APP_NAME_CONSUMER = "ForeOrder - Consumer";
    public static final String APP_NAME_EMPLOYEE = "ForeOrder - Employee";

    public final static String SERVER_URL = "http://18.143.85.82:8088/api/";
//    public final static String SERVER_URL = "https://api.foreorder.com/";
    public final static String DEV_SERVER_URL = "https://api.dev.foreorder.com/";

    public final static String PRIVACY_POLICY_URL = "public/legal/ForeOrderPrivacy.pdf";
    public final static String TERMS_OF_USE_URL = "public/legal/ForeOrderTerms.pdf";

    public final static String LOGIN_FAILED = "Login failed. Please check your credentials and try again.";
    public final static String NO_PERMISSIONS = "You do not have permission to use this app.";

    public final static String JPG = "jpg";

    public final static String ACCOUNT_CREATION_FAILED = "An error occurred. Account could not be created.";

    public final static String ERROR_OCCURRED = "An error occurred. Please try again later.";
    public final static String ERROR_OCCURRED_SENDING_PIN = "An error occurred. PIN could not be sent.";
    public final static String ERROR_OCCURRED_GETTING_CLUBS = "An error occurred while getting the courses. Please try again later.";
    public final static String ERROR_OCCURRED_GETTING_MENUS = "An error occurred while getting the menus. Please try again later.";
    public final static String ERROR_OCCURRED_PLACING_ORDER = "An error occurred while placing your order. Please try again later.";
    public final static String ERROR_OCCURRED_ADDING_MEMBERSHIP = "An error occurred while adding the membership. Please try again later.";
    public final static String ERROR_OCCURRED_REMOVING_MEMBERSHIP = "An error occurred while removing the membership. Please try again later.";
    public static final String ERROR_OCCURRED_CLUB_NOT_SELECTED = "A club needs to be selected from the list.";
    public static final String ERROR_OCCURRED_EMPTY_MEMBER_ID = "Member ID / Access code cannot be empty.";
    public final static String ERROR_OCCURRED_COMPLETING_ORDER = "An error occurred while completing the order. Please try again later.";
    public final static String ERROR_OCCURRED_RECEIVING_ORDER = "An error occurred while receiving the order. Please try again later.";
    public final static String ERROR_OCCURRED_GETTING_ORDERS = "An error occurred while getting the orders. Please try again later.";
    public final static String SESSION_EXPIRED = "Your session has expired. Redirecting to login.";
    public static final String ORDERING = "Ordering";
    public static final String COMPLETING = "Completing ";
    public static final String LOGGING_IN = "Logging in";
    public static final String CREATING_ACCOUNT = "Creating Account";
    public final static String MISSING_ITEMS = "Please select the required items.";
    public final static String RESENDING_PIN = "Re-sending PIN...";
    public final static String INCORRECT_PIN = "Incorrect PIN";

    public final static String ORDERS = "Orders";

    public final static String ORDER_TABLE = "Order";
    public final static String ORDER_LIST = "Order List";
    public final static String ORDER_MAP = "Order Map";
    public final static String ORDER_DETAILS = "Order Details";
    public final static String CHANGE_CLUB = "Change Club";
    public final static String MULTIPLE_MENUS = "Multiple Menus";

    public final static String KEY_ALREADY_IN_USE = "That email or phone number is already in use.";
    public final static String PHONE_NUMBER_ALREADY_IN_USE = "That phone number is already in use.";

    public final static String CART_EMPTY = "Your cart is empty.";

    public final static String ERROR_UPLOADING_PHOTO = "An error occurred. Failed to upload image.";

    //Shared Preference keys
    public static final String SHARED_PREF_KEY = "ForeOrderPreferences";
    public static final String CURRENT_USER_KEY = "currentUserKey";
    public static final String CURRENT_SESSION_KEY = "currentSessionKey";
    public static final String CURRENT_CLUB_MENU_KEY = "currentClubMenuKey";

    //Endpoints

    public static final String FB_LOGIN = "fb_login";
    public static final String VALIDATE = "validate";
    public static final String LOGIN = "login";
    public static final String USER = "user";
    public static final String MEMBERSHIP = "membership";
    public static final String LOGOUT = "logout";
    public static final String LOCATION = "location";
    public static final String CLUB = "club";
    public static final String MENU = "menu";
    public static final String MENU_IDS = "menu_ids";
    public static final String FULL_TRUE = "full=true";
    public static final String OPEN_ONLY = "open_only";
    public static final String PRIVATE = "private";
    public static final String TRUE = "true";
    public static final String SMS_VALIDATE = "sms_validate";
    public static final String PASSWORD_ENDPOINT = "password";
    public static final String PIN = "pin";
    public static final String VALID = "valid";

    //Serialized class names

    public static final String CURRENT_USER_MANAGER_ID = "currentUserManagerId";

    public static final String MODIFIED_AT = "modified_at";
    public static final String CREATED_AT = "created_at";

    //Session
    public static final String SESSION_TABLE = "Session";
    public static final String SESSION_ID = "session_id";

    //User
    public static final String USER_TABLE = "User";
    public static final String USER_ID = "user_id";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PROFILE_PHOTO_URL = "profile_photo_url";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String DATE_OF_BIRTH = "dob";
    public static final String AGE = "age";
    public static final String PHOTO_URL = "photo_url";
    public static final String PHOTO_URL_THUMB = "photo_url_thumb";
    public static final String PERM_LVL = "perm_lvl";
    public static final String RATING_COUNT = "rating_count";
    public static final String SIGNUP_COMPLETE = "signup_complete";
    public static final String PHONE_VALID = "phone_valid";
    public static final String FACEBOOK_USER = "Facebook_User";
    public static final String PERMISSIONS = "permissions";
    public static final String EMPLOYEE = "employee";
    public static final String CONSUMER = "consumer";

    //FacebookUser
    public static final String PROFILE_IMAGE_URL = "profile_image_url";

    //Club
    public static final String CLUB_TABLE = "Club";
    public static final String CLUB_ID = "club_id";
    public static final String TAX_RATE = "tax_rate";
    public static final String SHOW_TAX = "show_tax";

    //User Location
    public final static String USER_LOCATION_TABLE = "User_Location";
    public static final String LOCATION_ID = "location_id";

    //Location
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String H_ACCURACY = "h_accuracy";

    //Membership
    public static final String MEMBERSHIP_TITLE = "Membership";
    public static final String MEMBER_CODE = "member_code";
    public static final String MEMBERSHIP_ID = "membership_id";
    public static final String SELECT_A_CLUB = "Select a club";
    public static final String NO_CLUBS_AVAILABLE = "No clubs available";
    public static final String MEMBERSHIP_ADDED_SUCCESSFULLY = "Membership is successfully added.";
    public static final String MEMBERSHIP_REMOVED_SUCCESSFULLY = "Membership is successfully removed.";

    //Menu
    public static final String MENU_TABLE = "Menu";
    public static final String MENU_ID = "menu_id";
    public static final String ADDED_TAX_RATE = "added_tax_rate";

    //Menu Item
    public static final String MENU_ITEMS = "menu_items";
    public static final String MENU_ITEM_ID = "menu_item_id";

    //Menu Item Type
    public static final String ITEM_TYPES = "item_types";
    public static final String ITEM_TYPE_ID = "item_type_id";

    //Order
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_NUM = "order_num";
    public static final String PRICE_TOTAL = "price_total";
    public static final String PRICE_TOTAL_WITH_TAX = "price_total_with_tax";
    public static final String TAX_AMOUNT = "tax_amount";
    public static final String FULFILLED = "fulfilled";
    public static final String CURRENT_STATE = "current_state";
    public static final String PLACED = "placed";
    public static final String RECEIVED = "received";
    public static final String COMPLETED = "completed";

    //Option Group
    public static final String OPTION_GROUPS = "option_groups";
    public static final String OPTION_GROUP_ID = "option_group_id";
    public static final String ORDER_OPTION_IDS = "order_option_ids";
    public static final String SINGLE_CHOICE = "single_choice";

    //Order Item
    public static final String ITEMS = "items";
    public static final String ORDER_ITEM_ID = "order_item_id";
    public static final String ORDER_OPTIONS = "order_options";
    public static final String OPTION_ITEM_ID = "option_item_id";
    public static final String ORDER = "order";
    public static final String QUANTITY = "quantity";
    public static final String SPECIAL_REQUEST = "special_request";

    //Shared prefs
    public static final String KEY_CHECK_PREVIOUS_SCREEN = "previous_screen_check";
    public static final String SHOULD_DEFAULT_MAP_TO_CLUB_LOCATION = "should_default_map_to_current_location";
    public static final String GET_PREVIOUS_LATITUDE = "get_previous_latitude";
    public static final String GET_PREVIOUS_LONGITUDE = "get_previous_longitude";
    public static final String GET_PREVIOUS_ZOOM_LEVEL = "get_previous_zoom_level";
    public static final String GET_VIEW_CART_LAYOUT_HEIGHT = "get_view_cart_layout_height";
    public static final String GET_NOTIFICATION_CLUB_ID = "get_notification_club_id";
    public static final String GET_PREVIOUS_CLUB_ID = "get_previous_club_id";
    public static final String KEY_CURRENT_CLUB_ID = "key_current_club_id";
    public static final String KEY_DID_USER_MANUALLY_SELECT_COURSE = "key_did_user_manually_select_course";
    public static final String KEY_CURRENT_USER_ID = "key_current_user_id";

    public static final String NO_GPS_FRAGMENT_TAG = "NoGpsFragment";
    public static final String LOCATION_STILL_DISABLED = "Location services is still disabled. Please check your settings.";
    public static final String CHECKING_LOCATION_SERVICES = "Checking Location Services";

}
