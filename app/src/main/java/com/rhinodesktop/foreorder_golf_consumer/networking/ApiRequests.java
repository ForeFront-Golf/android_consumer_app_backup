package com.rhinodesktop.foreorder_golf_consumer.networking;

import android.location.Location;
import android.os.Build;

import com.rhinoactive.foreorder_library_android.utils.Constants;
import com.rhinoactive.generalutilities.apirequestutilities.ApiRequestsUtility;
import com.rhinodesktop.foreorder_golf_consumer.BuildConfig;
import com.rhinodesktop.foreorder_golf_consumer.managers.CurrentOrder;
import com.rhinodesktop.foreorder_golf_consumer.models.Order;
import com.rhinodesktop.foreorder_golf_consumer.models.Session;
import com.rhinodesktop.foreorder_golf_consumer.models.UserLocation;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by rhinodesktop on 2017-03-07.
 */

public class ApiRequests extends ApiRequestsUtility {

    private static ApiRequests apiRequests = null;

    private ApiRequests() {
    }

    public static ApiRequests getInstance() {
        if (apiRequests == null) {
            apiRequests = new ApiRequests();
        }
        return apiRequests;
    }

    public String getFacebookLoginUrl() {
        return getServerUrl() + Constants.FB_LOGIN;
    }

    public Call validateSession() throws GeneralSecurityException {
        String validateUrl = Constants.VALIDATE;
        return getRequest(validateUrl);
    }

    public Call resetPassword(String email) throws GeneralSecurityException {
        String resetUrl = Constants.USER + "/" + Constants.PASSWORD_ENDPOINT;
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.EMAIL, email);
        return putRequest(resetUrl, params);
    }

    public Call logUserInWithEmail(String email, String password) throws GeneralSecurityException {
        String loginUrl = Constants.LOGIN;
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.EMAIL, email);
        params.put(Constants.PASSWORD, password);
        return postRequest(loginUrl, params);
    }

    public Call getMembershipList(int userId) throws GeneralSecurityException {
        String membershipUrl = Constants.USER + "/" + userId + "/" + Constants.MEMBERSHIP;
        return getRequest(membershipUrl);
    }

    public Call addCourseMembership(Integer clubId, String memberCode, int userId) throws GeneralSecurityException {
        String membershipUrl = Constants.USER + "/" + userId + "/" + Constants.MEMBERSHIP;
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.CLUB_ID, clubId);
        params.put(Constants.MEMBER_CODE, memberCode);
        return postRequest(membershipUrl, params);
    }

    public Call getPrivateClubsList() throws GeneralSecurityException {
        String privateClubsListUrl = Constants.CLUB + "?" + Constants.PRIVATE + "=" + Constants.TRUE;
        return getRequest(privateClubsListUrl);
    }

    public Call removeCourseMembership(int membershipId, int userId) throws GeneralSecurityException {
        String membershipUrl = Constants.USER + "/" + userId + "/" + Constants.MEMBERSHIP + "/" + membershipId;
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.VALID, false);
        return putRequest(membershipUrl, params);
    }

    public Call updateCurrentUserImagePath(int userId, String imageS3Path) throws GeneralSecurityException {
        String userUrl = Constants.USER + "/" + userId;
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.PROFILE_PHOTO_URL, imageS3Path);
        return putRequest(userUrl, params);
    }

    public Call createAccount(String firstName, String lastName, String email, String phoneNumber, String dateOfBirth, String password)
            throws GeneralSecurityException {
        String createAccountUrl = Constants.USER;
        Map<String, Object> params = addUserInfoParams(firstName, lastName, email, phoneNumber, dateOfBirth);
        params.put(Constants.PASSWORD, password);
        return postRequest(createAccountUrl, params);
    }

    public Call verifyCurrentUserInfo(int userId, String firstName, String lastName, String email, String phoneNumber, String dateOfBirth) throws GeneralSecurityException {
        String updateUserUrl = Constants.USER + "/" + userId;
        Map<String, Object> params = addUserInfoParams(firstName, lastName, email, phoneNumber, dateOfBirth);
        return putRequest(updateUserUrl, params);
    }

    public Call invalidateSession(int userId) throws GeneralSecurityException {
        String logoutUrl = Constants.USER + "/" + userId + "/" + Constants.LOGOUT;
        Map<String, Object> params = new HashMap<>();
        return putRequest(logoutUrl, params);
    }

    public Call sendPin(int userId) throws GeneralSecurityException {
        String pinValidateUrl = Constants.USER + "/" + userId + "/" + Constants.SMS_VALIDATE;
        return getRequest(pinValidateUrl);
    }

    public Call validatePin(int userId, String pin) throws GeneralSecurityException {
        String pinValidateUrl = Constants.USER + "/" + userId + "/" + Constants.SMS_VALIDATE;
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.PIN, pin);
        return postRequest(pinValidateUrl, params);
    }

    public Call updateCurrentUserLocation(int userId, Location location) throws GeneralSecurityException {
        String locationUrl = Constants.USER + "/" + userId + "/" + Constants.LOCATION;
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.LAT, location.getLatitude());
        params.put(Constants.LON, location.getLongitude());
        params.put(Constants.H_ACCURACY, location.getAccuracy());
        return postRequest(locationUrl, params);
    }

    public Call getMenuForCurrentClub(int clubId) throws GeneralSecurityException {
        String locationUrl = Constants.CLUB + "/" + clubId + "/" + Constants.MENU
                + "?" + Constants.FULL_TRUE;
        return getRequest(locationUrl);
    }

    public Call getClubsNearCurrentUser(Location location) throws GeneralSecurityException {
        String clubUrl = Constants.CLUB + "?" + Constants.LAT + "=" + location.getLatitude() + "&"
                + Constants.LON + "=" + location.getLongitude();
        return getRequest(clubUrl);
    }

    public Call placeOrder(int userId, int clubId, UserLocation location) throws GeneralSecurityException {
        Order order = CurrentOrder.getInstance().getOrder();
        int menuId = order.getOrderItems().get(0).getMenuItems().get(0).getMenuId();
        String orderUrl = Constants.USER + "/" + userId + "/" + Constants.CLUB + "/" + clubId + "/"
                + Constants.MENU + "/" + menuId + "/" + Constants.ORDER;
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.LAT, location.getLat());
        params.put(Constants.LON, location.getLon());
        params.put(Constants.ORDER, order.createOrderMapJson());
        return postRequest(orderUrl, params);
    }

    public Call checkOrderStatus(int userId) throws GeneralSecurityException {
        String orderStatusUrl = Constants.USER + "/" + userId + "/" + Constants.ORDER + "?"
                + Constants.OPEN_ONLY + "=" + Constants.TRUE;
        return getRequest(orderStatusUrl);
    }

    @Override
    protected Request.Builder buildRequest(String url) {
        Request.Builder builder = super.buildRequest(url);
        builder.addHeader(com.rhinoactive.generalutilities.Constants.ACCEPT, APPLICATION_JSON);
        return builder;
    }

    @Override
    protected String getServerUrl() {
        return Constants.SERVER_URL;
//        return Constants.DEV_SERVER_URL;
    }

    @Override
    protected String getAuthorizationValue() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Session> sessionResults = realm.where(Session.class).sort("createdAt").findAll();
        if (sessionResults.isEmpty()) {
            realm.close();
            return "";
        } else {
            String sessionId = sessionResults.last().getSessionId();
            realm.close();
            return sessionId;
        }
    }

    @Override
    protected String getAppVersionNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.APP_NAME_CONSUMER);
        sb.append(String.format("/%s", BuildConfig.VERSION_NAME));
        sb.append(String.format(" (Android; %s)", Build.VERSION.RELEASE));
        return sb.toString();
    }

    private Map<String, Object> addUserInfoParams(String firstName, String lastName, String email, String phoneNumber, String dateOfBirth) {
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.FIRST_NAME, firstName);
        params.put(Constants.LAST_NAME, lastName);
        params.put(Constants.EMAIL, email);
        params.put(Constants.PHONE_NUMBER, phoneNumber);
        params.put(Constants.DATE_OF_BIRTH, dateOfBirth);
        return params;
    }
}
