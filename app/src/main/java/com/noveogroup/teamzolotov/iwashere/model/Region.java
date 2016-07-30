package com.noveogroup.teamzolotov.iwashere.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.noveogroup.teamzolotov.iwashere.database.ContentDescriptor;

/**
 * Created by dserov on 28/07/16.
 */

@DatabaseTable(tableName = ContentDescriptor.Regions.TABLE_NAME)
public class Region {

    @DatabaseField(generatedId = true, columnName = ContentDescriptor.Regions.Cols.ID)
    private int id;

    @DatabaseField(columnName = ContentDescriptor.Regions.Cols.OSM_ID)
    private int osmId;

    @DatabaseField(columnName = ContentDescriptor.Regions.Cols.VISITED)
    private boolean visited;

    public Region() {}

    public Region(int osmId) {
        this.osmId = osmId;
    }

    public int getOsmId() {
        return osmId;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
