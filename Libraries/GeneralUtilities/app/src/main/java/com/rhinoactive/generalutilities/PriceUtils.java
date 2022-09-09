package com.rhinoactive.generalutilities;

/**
 * Created by rhinodesktop on 2017-03-28.
 */

public class PriceUtils {

    /**
     *
     * @param price The price that you want to calculate hst for
     * @return The hst for the given price
     */
    public static float calculateHst(float price) {
        return price * .13f;
    }
}
