package com.ey.pwbc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class TokenDBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "TokensDB.db";
    private static final int DATABASE_VERSION = 1;

    // database access objects
    private Dao<TokenData, Integer> tokenDataDao;

    public TokenDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, TokenData.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, TokenData.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<TokenData, Integer> getTokenDataDao() {
        if (tokenDataDao == null) {
            try {
                tokenDataDao = getDao(TokenData.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tokenDataDao;
    }
}
