package com.ey.pwbc.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "tokenData")
public class TokenData {

    @DatabaseField(id = true)
    private String tokenID;

    @DatabaseField(columnName = "privateKey")
    private String privateKey;

    @DatabaseField(columnName = "publicKey")
    private String publicKey;

    public TokenData() {
    }

    public TokenData(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public TokenData(String tokenID, String privateKey, String publicKey) {
        this.tokenID = tokenID;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "TokenData{" +
                "privateKey=" + privateKey +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
