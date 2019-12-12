package com.ey.pwbc.database;

import java.sql.SQLException;
import java.util.List;


public class TokenRepo implements Crud {

    private TokenDBHelper helper;

    public TokenRepo() {
        this.helper = TokenDBManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {

        int index = -1;
        TokenData object = (TokenData) item;

        try {
            index = helper.getTokenDataDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int update(Object item) {

        int index = -1;
        TokenData object = (TokenData) item;

        try {
            index = helper.getTokenDataDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int delete(Object item) {

        int index = -1;
        TokenData object = (TokenData) item;

        try {
            index = helper.getTokenDataDao().delete(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public Object findById(int id) {

        TokenData object = null;

        try {
            object = helper.getTokenDataDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public List<?> findAll() {

        List<TokenData> items = null;

        try {
            items = helper.getTokenDataDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }
}
