package com.rhinoactive.generalutilities;

/**
 * Created by Hunter Andrin on 2017-05-19.
 */

public class TextUtils {

    /**
     *
     * @param strings The String values to check.
     * @return Whether or not all of the strings contain text (ie. are non null and have a length or 1 or greater).
     */
    public static boolean stringsContainText(String... strings) {
        boolean stringsContainText = true;
        for (String text : strings) {
            if (text == null || text.length() == 0) {
                stringsContainText = false;
                break;
            }
        }
        return stringsContainText;
    }
}
