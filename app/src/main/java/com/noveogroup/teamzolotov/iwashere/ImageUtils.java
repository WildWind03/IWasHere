package com.noveogroup.teamzolotov.iwashere;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

public class ImageUtils {
    private ImageUtils() {

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
