package com.rhinodesktop.facebookutilities;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.rhinodesktop.facebookutilities.graphrequests.FacebookAlbumGraphRequest;
import com.rhinodesktop.facebookutilities.graphrequests.FacebookGraphRequest;
import com.rhinodesktop.facebookutilities.graphrequests.FacebookPhotosForAlbumRequest;
import com.rhinodesktop.facebookutilities.graphrequests.TaggedUserPhotosGraphRequest;
import com.rhinodesktop.facebookutilities.graphrequests.UploadedUserPhotosGraphRequest;
import com.rhinodesktop.facebookutilities.models.FbPager;
import com.rhinodesktop.facebookutilities.models.FbPhotosAndPagers;
import com.rhinodesktop.facebookutilities.utils.Constants;

import lombok.Getter;
import lombok.Setter;

import static com.facebook.AccessToken.getCurrentAccessToken;

/**
 * Created by rhinodesktop on 2017-01-20.
 */

public class FacebookPhotoManager {

    private static FacebookPhotoManager facebookPhotoManager = new FacebookPhotoManager();

    @Getter
    @Setter
    private FbPhotosAndPagers currentUserFbPhotosAndPagers = new FbPhotosAndPagers();

    private FacebookPhotoManager() {}

    public static FacebookPhotoManager getInstance() {
        return facebookPhotoManager;
    }

    // Pass in null for the albumPager if you're accessing the first page of photos
    public void getFbAlbum(FacebookAlbumGraphRequest albumRequest) {
        String graphPath = getCurrentAccessToken().getUserId() + "/albums";
        if (albumRequest.getAlbumPager() != null && albumRequest.getAlbumPager().getNext() != null) {
            graphPath = graphPath + "?after=" + albumRequest.getAlbumPager().getCursors().getAfter();
        }
        new GraphRequest(getCurrentAccessToken(), graphPath, null, HttpMethod.GET, albumRequest).executeAsync();
    }

    public void getAlbumPictures(FacebookPhotosForAlbumRequest request, String albumId) {
        String graphPath = albumId + "/photos";
        Bundle parameters = new Bundle();
        parameters.putString("fields", "source");
        new GraphRequest(getCurrentAccessToken(), graphPath, parameters, HttpMethod.GET, request).executeAsync();
    }

    // Gets all of the user's photos. This includes all of the photos that the user has uploaded as well as
    // all the photos the user has been tagged in that have been made public.
    public void getFbPhotos(Context context) {
        getFbPhotosForType(context, Constants.TYPE_TAGGED, currentUserFbPhotosAndPagers.getFbTaggedPager(), currentUserFbPhotosAndPagers.isHasMoreTaggedImages(), new TaggedUserPhotosGraphRequest());
        getFbPhotosForType(context, Constants.TYPE_UPLOADED, currentUserFbPhotosAndPagers.getFbUploadedPager(), currentUserFbPhotosAndPagers.isHasMoreUploadedImages(), new UploadedUserPhotosGraphRequest());
    }

    private static void getFbPhotosForType(Context context, String photoType, FbPager fbPager, boolean hasMoreImages, FacebookGraphRequest facebookGraphRequest) {
        if (!hasMoreImages) {
            return;
        }
        String graphPath = getCurrentAccessToken().getUserId() + "/photos?limit=30&type=" + photoType;
        boolean getMorePhotos = true;
        // If the pager is null then it is the first time loading the images
        if (fbPager != null) {
            if (fbPager.getNext() == null) {
                //There are no more pages so don't get more photos
                getMorePhotos = false;
            } else {
                graphPath = graphPath + "&after=" + fbPager.getCursors().getAfter();
            }
        }
        if (getMorePhotos) {
            requestPhotosFromFb(context, graphPath, facebookGraphRequest);
        }
    }

    private static void requestPhotosFromFb(Context context, String graphPath, FacebookGraphRequest facebookGraphRequest) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "source");
        new GraphRequest(getCurrentAccessToken(), graphPath, parameters, HttpMethod.GET, facebookGraphRequest).executeAsync();
        Toast.makeText(context, "Loading photos...", Toast.LENGTH_SHORT).show();
    }
}
