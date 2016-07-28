package com.noveogroup.teamzolotov.iwashere.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.noveogroup.teamzolotov.iwashere.model.Region;
import com.noveogroup.teamzolotov.iwashere.util.RegionUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by dserov on 28/07/16.
 */
public class RegionOrmLiteOpenHelper extends OrmLiteSqliteOpenHelper {

    public static final String DATABASE_NAME = "regions.db";
    public static final int DATABASE_VERSION = 1;

    private Dao<Region, Integer> dao;
    private RuntimeExceptionDao<Region, Integer> runtimeDao;


    public RegionOrmLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Region.class);
            populateDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Region.class, false);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Region, Integer> getDao() throws SQLException {
        if (dao == null) {
            dao = getDao(Region.class);
        }
        return dao;
    }

    public RuntimeExceptionDao<Region, Integer> getRuntimeExceptionDao() {
        if (runtimeDao == null) {
            runtimeDao = getRuntimeExceptionDao(Region.class);
        }
        return runtimeDao;
    }

    @Override
    public void close() {
        super.close();
        dao = null;
        runtimeDao = null;
    }

    private void populateDatabase() throws SQLException {
        Dao<Region, Integer> dao = getDao();
        List<Region> regions = RegionUtil.initRegions();
        for (Region r : regions) {
            dao.create(r);
        }
    }
}
