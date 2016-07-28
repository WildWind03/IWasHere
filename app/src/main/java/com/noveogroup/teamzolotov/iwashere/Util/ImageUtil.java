package com.noveogroup.teamzolotov.iwashere.util;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

public class ImageUtil {
    private ImageUtil() {

    }

    public static int getAppropriateHeight(Activity activity) {
        if (null == activity) {
            throw new IllegalArgumentException("Context mustn't be null." +
                    " It's impossible to get appropriate height without knowledge" +
                    " about height of the display");
        }

        Display currentDisplay = activity.getWindowManager().getDefaultDisplay();
        Point sizes = new Point();
        currentDisplay.getSize(sizes);
        return sizes.y / 4;
    }
}
