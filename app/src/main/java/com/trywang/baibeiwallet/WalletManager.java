package com.trywang.baibeiwallet;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 写清楚此类的作用
 *
 * @author Try
 * @date 2018/9/7 14:47
 */
public class WalletManager {
    private static volatile WalletManager sInstance;

    private List<BaibeiWallet> mListWallets = new ArrayList<>();

    private WalletManager(){
    }

    public static WalletManager getInstance(){
        if (sInstance == null) {
            synchronized (WalletManager.class){
                if (sInstance == null) {
                    sInstance = new WalletManager();
                }
            }
        }
        return sInstance;
    }

    public void addWallet(BaibeiWallet wallet ){
        mListWallets.add(wallet);
    }

    public List<BaibeiWallet> getWalletAll(){
        return mListWallets;
    }

    public BaibeiWallet getWallet(int position) {
        if (position < 0 || position > mListWallets.size()) {
            return null;
        }
        return mListWallets.get(position);
    }
}
