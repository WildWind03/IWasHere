package com.noveogroup.teamzolotov.iwashere.database;

import android.provider.BaseColumns;

/**
 * Created by dserov on 28/07/16.
 */
public final class ContentDescriptor {
    private ContentDescriptor() {
        throw new UnsupportedOperationException();
    }

    public static class Regions {

        public static final String TABLE_NAME = "regions";

        public static class Cols {

            public static final String ID = BaseColumns._ID;
            public static final String OSM_ID = "osm_id";
            public static final String VISITED = "visited";
        }
    }
}
