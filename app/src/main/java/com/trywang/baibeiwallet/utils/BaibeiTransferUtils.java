package com.trywang.baibeiwallet.utils;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.trywang.baibeiwallet.BaibeiWallet;
import com.trywang.baibeiwallet.BaseApplication;
import com.trywang.baibeiwallet.solidity.CloudTreeToken;
import com.trywang.baibeiwallet.solidity.Token;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.math.BigInteger;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO 写清楚此类的作用
 *
 * @author Try
 * @date 2018/9/14 17:21
 */
public class BaibeiTransferUtils {
    public final static String TAG = "BaibeiTransferUtils";

    public static Single<CloudTreeToken> loadContract2(BaibeiWallet wallet, String addr, BigInteger gasPrice, BigInteger gasLimit) {
        Credentials credentials = Credentials.create(wallet.getKeyPair());
        Log.i(TAG, "onClickDeploy: gasPrice = " + gasPrice);
        Log.i(TAG, "onClickDeploy: GAS_LIMIT = " + gasLimit);
        Web3j web3j = Web3jUtils.initWeb3j(null);
        return Single.fromCallable(() -> CloudTreeToken.load(addr, web3j, credentials, gasPrice, gasLimit))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    public static Single<Token> loadContract(BaibeiWallet wallet, String addr, BigInteger gasPrice, BigInteger gasLimit) {
        Credentials credentials = Credentials.create(wallet.getKeyPair());
        Log.i(TAG, "onClickDeploy: gasPrice = " + gasPrice);
        Log.i(TAG, "onClickDeploy: GAS_LIMIT = " + gasLimit);
        Web3j web3j = Web3jUtils.initWeb3j(null);
        return Single.fromCallable(() -> Token.load(addr, web3j, credentials, gasPrice, gasLimit))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Single<BigInteger> balanceOf(Token token, String addr) {
        if (token == null) {
            Toast.makeText(BaseApplication.sInstance, "合约为空！", Toast.LENGTH_SHORT).show();
            return Single.error(new NullPointerException("合约为空！"));
        }

        if (TextUtils.isEmpty(addr)) {
            Toast.makeText(BaseApplication.sInstance, "转账地址为空！", Toast.LENGTH_SHORT).show();
            return Single.error(new NullPointerException("转账地址为空！"));
        }

        return Single.fromCallable(() -> token.balanceOf(Keys.toChecksumAddress(addr))
                .send())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<BigInteger> getBalanceForETH(String address) {
        if (TextUtils.isEmpty(address)) {
            return Single.error(new NullPointerException("转账地址为空！"));
        }

        String addr = address.startsWith("0x") ? address : Keys.toChecksumAddress(address);
        Web3j web3j = Web3jUtils.initWeb3j(null);
        return Single.fromCallable(() -> web3j
                .ethGetBalance(addr, DefaultBlockParameterName.LATEST)
                .send()
                .getBalance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
