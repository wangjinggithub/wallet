package com.trywang.baibeiwallet.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trywang.baibeiwallet.BaibeiWallet;
import com.trywang.baibeiwallet.R;
import com.trywang.baibeiwallet.WalletManager;
import com.trywang.baibeiwallet.model.TokenModel;
import com.trywang.baibeiwallet.solidity.Token;
import com.trywang.baibeiwallet.utils.BaibeiTransferUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.web3j.crypto.Keys;

import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TokenDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_address)
    TextView mTvAddr;
    @BindView(R.id.tv_balance)
    TextView mTvBala;
    @BindView(R.id.iv_qrcode)
    ImageView mIvQrCode;

    @BindView(R.id.et_addr_get_balance)
    EditText mEtAddrGetBalance;
    @BindView(R.id.tv_get_balance)
    TextView mTvGetBalance;

    BaibeiWallet mWallet;
    String mAddr;
    int mIndex;
    Token mContract;
    TokenModel mTokenModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_detail);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mTokenModel = getIntent().getParcelableExtra("token");
        mIndex = getIntent().getIntExtra("index", 0);
        mWallet = WalletManager.getInstance().getWallet(mIndex);
        mAddr = Keys.toChecksumAddress(mWallet.getWalletFile().getAddress());
        mTvTitle.setText(mTokenModel.getTitle() + "");
        mTvAddr.setText("地址：" + mAddr);
        if (mTokenModel == null) {
            return;
        }

        if (mTokenModel.isEth) {
            getBalanceForETH(mAddr);
        } else {
            loadContract();
        }
    }

    private void loadContract() {
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
                        Toast.makeText(TokenDetailActivity.this, "合约加载完成！", Toast.LENGTH_SHORT).show();
                        mContract = token;
                        getBalance();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TokenDetailActivity.this, "合约加载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private BigInteger getGasPrice() {
        return BigInteger.valueOf(1000);
    }

    private BigInteger getGasLimit() {
        return BigInteger.valueOf(600000);
    }

    private void getBalance() {
        if (mContract == null ) {
            Toast.makeText(this, "合约不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        BaibeiTransferUtils.balanceOf(mContract, mWallet.getWalletFile().getAddress())
                .subscribe(mBalanceObserver);
    }

    private void getBalanceForETH(String addr) {
        BaibeiTransferUtils.getBalanceForETH(addr)
                .subscribe(mBalanceObserver);
    }

    private SingleObserver<BigInteger> mBalanceObserver = new SingleObserver<BigInteger>() {
        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onSuccess(BigInteger bigInteger) {
            mTvBala.setText("余额：" + bigInteger.toString());
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            mTvBala.setText("余额查询有误！" + e.getMessage());
        }
    };

    private SingleObserver<BigInteger> mBalanceObserver2 = new SingleObserver<BigInteger>() {
        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onSuccess(BigInteger bigInteger) {
            mTvGetBalance.setText("余额：" + bigInteger.toString());
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            mTvGetBalance.setText("获取余额出错！" + e.getMessage());
        }
    };


    @OnClick(R.id.btn_transfer_into)
    public void OnClickCTransferInto() {
        showQrcode();
    }

    @OnClick(R.id.btn_transfer_out)
    public void OnClickTransferOut() {
        Intent i = new Intent(TokenDetailActivity.this, TransferActivity.class);
        i.putExtra("index", mIndex);
        i.putExtra("token", mTokenModel);
        startActivity(i);
    }

    @OnClick(R.id.btn_get_balance)
    public void onClickGetBalance() {
        String addr = mEtAddrGetBalance.getText().toString();
        if (mTokenModel.isEth) {
            BaibeiTransferUtils.getBalanceForETH(addr)
                    .subscribe(mBalanceObserver2);
        } else {

        BaibeiTransferUtils.loadContract(mWallet, mTokenModel.getAddr(), getGasPrice(), getGasLimit())
                .flatMap((Function<Token, SingleSource<BigInteger>>) token -> {
                    mContract = token;
                    return Single.fromCallable(() -> mContract.balanceOf(Keys.toChecksumAddress(addr))
                            .sendAsync().get());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mBalanceObserver2);
        }
    }

    private void showQrcode() {
        if (mWallet == null) {
            Toast.makeText(this, "钱包为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = Keys.toChecksumAddress(mWallet.getWalletFile().getAddress());
        Bitmap qrcode = CodeUtils.createImage(address, 600, 600, null);
        mIvQrCode.setImageBitmap(qrcode);
    }
}
