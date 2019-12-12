package com.ey.pwbc.database;


import android.content.Context;


public class TokenDBManager {

    static private TokenDBManager instance;
    private TokenDBHelper helper;

    private TokenDBManager(Context ctx) {
        helper = new TokenDBHelper(ctx);
    }

    static public void init(Context ctx) {
        if (null == instance) {
            instance = new TokenDBManager(ctx);
        }
    }

    static public TokenDBManager getInstance() {
        return instance;
    }

    public TokenDBHelper getHelper() {
        return helper;
    }

}
