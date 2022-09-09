package com.rhinoactive.generalutilities;

import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rhinodesktop on 2017-03-28.
 */


/**
 * Provides utility functions for adjusting the parameters of a view.
 */
public class ViewUtils {

    /**
     * All int values are in terms of px
     *
     * @param view A reference to the view
     * @param left The size of the left margin
     * @param top The size of the top margin
     * @param right The size of the right margin
     * @param bottom The size of the bottom margin
     */
    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    /**
     * All int values are in terms of px
     *
     * @param view A reference to the view
     * @param left The size to add to the left margin
     * @param top The size to add to the top margin
     * @param right The size to add to the right margin
     * @param bottom The size to add to the bottom margin
     */
    public static void addMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            int currentLeftMargin = params.leftMargin;
            int currentTopMargin = params.topMargin;
            int currentRightMargin = params.rightMargin;
            int currentBottomMargin = params.bottomMargin;
            params.setMargins(currentLeftMargin + left, currentTopMargin + top, currentRightMargin + right, currentBottomMargin + bottom);
            view.requestLayout();
        }
    }

    /**
     * All int values are in terms of px
     *
     * @param view A reference to the view
     * @param left The size to subtract from the left margin
     * @param top The size to subtract from the top margin
     * @param right The size to subtract from the right margin
     * @param bottom The size to subtract from the bottom margin
     */
    public static void subtractMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            int currentLeftMargin = params.leftMargin;
            int currentTopMargin = params.topMargin;
            int currentRightMargin = params.rightMargin;
            int currentBottomMargin = params.bottomMargin;
            params.setMargins(currentLeftMargin - left, currentTopMargin - top, currentRightMargin - right, currentBottomMargin - bottom);
            view.requestLayout();
        }
    }

    /**
     *
     * @param view A reference to the view
     * @return The center Point of the view
     */
    public static Point getCenterPointOfView(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = location[0] + view.getWidth() / 2;
        int y = location[1] + view.getHeight() / 2;
        return new Point(x, y);
    }
}
