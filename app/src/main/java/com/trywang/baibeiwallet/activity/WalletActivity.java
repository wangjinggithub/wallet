package com.trywang.baibeiwallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trywang.baibeiwallet.BaibeiWallet;
import com.trywang.baibeiwallet.ClipboardUtils;
import com.trywang.baibeiwallet.R;
import com.trywang.baibeiwallet.WalletManager;
import com.trywang.baibeiwallet.WalletManagerActivity;
import com.trywang.baibeiwallet.event.SelWalletEvent;
import com.trywang.baibeiwallet.model.TokenModel;
import com.trywang.baibeiwallet.solidity.Token;
import com.trywang.baibeiwallet.utils.BaibeiTransferUtils;
import com.trywang.baibeiwallet.utils.Web3jUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WalletActivity extends AppCompatActivity {

    @BindView(R.id.et_web3j_addr)
    public EditText mEtWeb3jAddr;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_balance_eth)
    TextView mTvBalanceEth;
    @BindView(R.id.tv_balance_token)
    TextView mTvBalanceToken;
    BaibeiWallet mWallet;
    int mIndex = -1;
    String mAddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void init() {
        if (mWallet == null) {
            return;
        }
        mAddr = Keys.toChecksumAddress(mWallet.getWalletFile().getAddress());
        mTvAddress.setText(mAddr);
    }

    Web3j mWeb3j;

    private void initWeb3j(String url) {
        mWeb3j = Web3jUtils.initWeb3j(this, url);
    }

    @OnClick(R.id.btn_init_web3j)
    public void onClickInitWeb3j() {
        initWeb3j(mEtWeb3jAddr.getText().toString());
    }

    @OnClick(R.id.btn_all_wallet)
    public void onClickAllWallet() {
        Intent i = new Intent(WalletActivity.this, WalletManagerActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_copy_address)
    public void OnClickCopyAddress() {
        ClipboardUtils.copy(mTvAddress.getText().toString());
    }

    @OnClick(R.id.btn_refresh_balance_eth)
    public void OnClickRefreshBalanceEth() {
        if (mWallet == null) {
            Toast.makeText(this, "钱包为空", Toast.LENGTH_SHORT).show();
            return;
        }

        getBalanceForETH(mAddr);
    }

    @OnClick(R.id.btn_refresh_balance_token)
    public void OnClickRefreshBalanceToken() {
        loadContract("0x8cc96dfe394def64659e47df7e048c5ee285bd57", "CTT");
    }

    @OnClick(R.id.ll_eth)
    public void OnClickETH() {
        TokenModel token = new TokenModel("", "ETH", true);
        jumpToTokenDetail(token);
    }

    @OnClick(R.id.ll_token)
    public void OnClickTOKEN() {
        TokenModel token = new TokenModel("0x8cc96dfe394def64659e47df7e048c5ee285bd57", "CTT", false);
        jumpToTokenDetail(token);
    }


    @OnClick(R.id.btn_setting)
    public void OnClickSetting() {
        if (mWallet == null) {
            Toast.makeText(this, "钱包为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(WalletActivity.this, WalletSettingActivity.class);
        i.putExtra("index", mIndex);
        startActivity(i);
    }

    public void jumpToTokenDetail(TokenModel token) {
        Intent i = new Intent(WalletActivity.this, TokenDetailActivity.class);
        i.putExtra("index", mIndex);
        i.putExtra("token", token);
        startActivity(i);
    }


    public Single<BigInteger> balanceInWei(String address) {
        return Single.fromCallable(() -> mWeb3j
                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .send()
                .getBalance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelWalletEvent(SelWalletEvent sel) {
        mWallet = WalletManager.getInstance().getWallet(sel.index);
        mIndex = sel.index;
        init();
    }


    Token mContract;

    private void loadContract(String contractAddr, String tokenName) {
        TokenModel mTokenModel = new TokenModel(contractAddr, tokenName, false);
        if (mTokenModel != null && mTokenModel.isEth) {
            //以太坊不需要加载合约
            return;
        }
        BaibeiTransferUtils.loadContract(mWallet, mTokenModel.getAddr(), getGasPrice(), getGasLimit())
                .subscribe(new SingleObserver<Token>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(Token token) {
                        Toast.makeText(WalletActivity.this, "合约加载完成！", Toast.LENGTH_SHORT).show();
                        mContract = token;
                        getBalance(tokenName);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(WalletActivity.this, "合约加载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getBalance(String tokenName) {
        if (mContract == null) {
            Toast.makeText(this, "合约不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        BaibeiTransferUtils.balanceOf(mContract, mWallet.getWalletFile().getAddress())
                .subscribe(mBalanceObserver);

    }

    private void getBalanceForETH(String addr) {
        BaibeiTransferUtils.getBalanceForETH(addr)
                .subscribe(new SingleObserver<BigInteger>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(BigInteger bigInteger) {
                        mTvBalanceEth.setText(bigInteger.toString() + "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTvBalanceEth.setText("余额查询有误！" + e.getMessage());
                    }
                });
    }

    private SingleObserver<BigInteger> mBalanceObserver = new SingleObserver<BigInteger>() {
        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onSuccess(BigInteger bigInteger) {
            mTvBalanceToken.setText(bigInteger.toString());
        }

        @Override
        public void onError(Throwable e) {
            mTvBalanceToken.setText("余额查询有误！" + e.getMessage());
        }
    };


    private BigInteger getGasPrice() {
        return BigInteger.valueOf(1000);
    }

    private BigInteger getGasLimit() {
        return BigInteger.valueOf(600000);
    }

}
