package com.trywang.baibeiwallet.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.trywang.baibeiwallet.Constant;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * TODO 写清楚此类的作用
 *
 * @author Try
 * @date 2018/9/13 14:53
 */
public class Web3jUtils {

    public static Web3j initWeb3j(String url) {
        if (!TextUtils.isEmpty(url)) {
            Constant.sWeb3jAddress = url;
        }
        url = TextUtils.isEmpty(url) ? Constant.sWeb3jAddress : url;
        return Web3jFactory.build(new HttpService(url, createOkHttpClient(), false));
    }

    public static Web3j initWeb3j(Context context,String url) {
        Web3j mWeb3j;

        if (!TextUtils.isEmpty(url)) {
            Constant.sWeb3jAddress = url;
        }
        url = TextUtils.isEmpty(url) ? Constant.sWeb3jAddress : url;
        mWeb3j = Web3jFactory.build(new HttpService(url, createOkHttpClient(), false));  // defaults to http://localhost:8545/
//        Web3j web3 = Web3jFactory.build(new HttpService(url));  // defaults to http://localhost:8545/
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = mWeb3j.web3ClientVersion().sendAsync().get();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            Toast.makeText(context, "连接成功 " + clientVersion, Toast.LENGTH_SHORT).show();
            Log.i("TAG", "onClickTransfer: clientVersion = " + clientVersion);
            NetVersion nv = mWeb3j.netVersion().sendAsync().get();
            Log.i("TAG", "onClickTransfer: 网络的版本 nv.getNetVersion() = " + nv.getNetVersion());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mWeb3j;
    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.writeTimeout(30 * 1000, TimeUnit.SECONDS)
                .connectTimeout(30 * 1000, TimeUnit.SECONDS);
        configureLogging(builder);
        return builder.build();
    }

    private static void configureLogging(OkHttpClient.Builder builder) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String msg) {
                        Log.i("TAG", "log: " + msg);
                    }
                });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
    }
}
