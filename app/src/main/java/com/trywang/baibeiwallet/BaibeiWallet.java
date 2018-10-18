package com.trywang.baibeiwallet;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletFile;

/**
 * TODO 写清楚此类的作用
 *
 * @author Try
 * @date 2018/9/4 16:24
 */
public class BaibeiWallet extends Bip39Wallet {
    WalletFile walletFile;
    ECKeyPair mKeyPair;

    public BaibeiWallet(WalletFile walletFile,String mnemonic) {
        super(null,mnemonic);
        this.walletFile = walletFile;
    }

    public BaibeiWallet( WalletFile walletFile,String mnemonic, ECKeyPair mKeyPair) {
        super(null, mnemonic);
        this.walletFile = walletFile;
        this.mKeyPair = mKeyPair;
    }

    public BaibeiWallet(String filename, String mnemonic) {
        super(filename, mnemonic);
    }

    public ECKeyPair getKeyPair() {
        return mKeyPair;
    }

    public void setKeyPair(ECKeyPair keyPair) {
        this.mKeyPair = keyPair;
    }

    public WalletFile getWalletFile() {
        return walletFile;
    }

    public void setWalletFile(WalletFile walletFile) {
        this.walletFile = walletFile;
    }

    public String getKeyStoreJsonString(){
        Gson gson = new Gson();
        JSONObject j = null;
        try {
            j = new JSONObject(gson.toJson(walletFile));
            j.remove("address");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return j.toString();
    }
}
