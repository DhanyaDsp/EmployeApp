package com.ey.pwbc.Utils;

import android.content.Context;
import android.util.Log;

import com.conio.postequorum.SDK;
import com.conio.postequorum.implementation.Configuration;
import com.conio.postequorum.implementation.SDKFactory;
import com.ey.pwbc.database.TokenDBManager;
import com.ey.pwbc.database.TokenData;
import com.ey.pwbc.database.TokenRepo;

import java.lang.Byte;

import org.web3j.tuples.generated.Tuple2;

import java.util.List;

public class Utils {
    public static Tuple2<String, String> getKeyFromDB(Context context) {
        TokenRepo tokenRepo = new TokenRepo();
        TokenDBManager.init(context);
        List<TokenData> tokenDataList = (List<TokenData>) tokenRepo.findAll();
        if (tokenDataList.size() > 0) {
            return new Tuple2(tokenDataList.get(0).getPrivateKey(), tokenDataList.get(0).getPublicKey());
        }
        return new Tuple2("", "");
    }

    // old contract address: 0x20fB247Ced04CdA18eA78EE8D9Aa79948e516a8B
    // new contract address: 0x4D4773F975cA0E8113a9f4171326964DAD42fe83
    public static Configuration getConf() {
        return new Configuration.ConfigurationBuilder("0x4D4773F975cA0E8113a9f4171326964DAD42fe83",
                "http://51.105.229.99:22100/"
        ).build();
    }

    public static String getEmployeeAddress(byte[] privateKey) {

        SDK sdkEmployee = SDKFactory.getInstance().createSDK(privateKey, Utils.getConf());
        Log.d("sos", "employeeAddress: " + sdkEmployee.getKeyPair().getNoPrefixAddress());
        return sdkEmployee.getKeyPair().getNoPrefixAddress();
    }

    public static String bytesToHex(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for (int b :
                hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
