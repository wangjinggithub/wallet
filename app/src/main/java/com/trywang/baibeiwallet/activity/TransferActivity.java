package com.trywang.baibeiwallet.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trywang.baibeiwallet.BaibeiWallet;
import com.trywang.baibeiwallet.R;
import com.trywang.baibeiwallet.WalletManager;
import com.trywang.baibeiwallet.WalletUtils;
import com.trywang.baibeiwallet.model.TokenModel;
import com.trywang.baibeiwallet.solidity.Token;
import com.trywang.baibeiwallet.utils.BaibeiTransferUtils;
import com.trywang.baibeiwallet.utils.DialogUtils;
import com.trywang.baibeiwallet.utils.Web3jUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TransferActivity extends AppCompatActivity {
    public static int REQ_SCAN_CODE = 100;
    public static String TAG = TransferActivity.class.getSimpleName();

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_balance_token)
    TextView mTvBalanceToken;
    @BindView(R.id.et_amount_transfer)
    EditText mEtAmount;
    @BindView(R.id.et_address_other)
    EditText mEtAddress;
    @BindView(R.id.tv_transfer_address_one)
    TextView mTvAddressTransferFrom;
    @BindView(R.id.tv_balance_one)
    TextView mTvBalanceFrom;
    @BindView(R.id.tv_transfer_address_two)
    TextView mTvAddressTransferTo;
    @BindView(R.id.tv_balance_two)
    TextView mTvBalanceTo;
    @BindView(R.id.tv_transfer_data)
    TextView mTvTransferData;


    BaibeiWallet mWallet;
    Token mContract;
    TokenModel mTokenModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);
        int index = getIntent().getIntExtra("index",0);
        mTokenModel = getIntent().getParcelableExtra("token");
        mWallet = WalletManager.getInstance().getWallet(index);

        if (mTokenModel == null) {
            return;
        }

        mTvTitle.setText(mTokenModel.getTitle()+"转账");

        if (mTokenModel.isEth) {
            getBalanceForETH(Keys.toChecksumAddress(mWallet.getWalletFile().getAddress()));
        } else {
            loadContract();
        }

    }

    @OnClick(R.id.btn_qrcode_address)
    public void onClickQrcodeAddress(){
        //扫码
        RxPermissions rxPermissions = new RxPermissions(TransferActivity.this);
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean granted) {
                            if (granted) {
                                ZXingLibrary.initDisplayOpinion(getApplicationContext());
                                Intent intent = new Intent(TransferActivity.this, CaptureActivity.class);
                                startActivityForResult(intent, REQ_SCAN_CODE);
                            } else {
                                Toast.makeText(TransferActivity.this, "相机授权失败", Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_SCAN_CODE && data != null && data.getExtras() != null) {
            Bundle bundle = data.getExtras();
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                String result = bundle.getString(CodeUtils.RESULT_STRING);
                Log.i("TAG","扫描结果:" + result);
                if (!TextUtils.isEmpty(result)) {
                    mEtAddress.setText(result);
                    mEtAddress.setSelection(mEtAddress.getText().length());
                } else {
                    Toast.makeText(this, "结果为空！", Toast.LENGTH_SHORT).show();
                }
            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                Toast.makeText(this, "解析二维码失败", Toast.LENGTH_SHORT).show();
//                AppUI.success(getContext(), "解析二维码失败");
            }
        }
    }

    @OnClick(R.id.btn_transfer)
    public void onClickTransfer(){
        if (mWallet == null) {
            Toast.makeText(this, "钱包为null", Toast.LENGTH_SHORT).show();
            return;
        }

        String amount = mEtAmount.getText().toString();
        String addr = mEtAddress.getText().toString();
        if (TextUtils.isEmpty(amount)) {
            Toast.makeText(this, "金额不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(addr)) {
            Toast.makeText(this, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        DialogUtils.enterPwd(this,(pwd)->{
            Credentials c = null;
            try {
                c = WalletUtils.loadCredentials(pwd,mWallet.getWalletFile());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CipherException e) {
                Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            sendTransaction(c,addr,Double.valueOf(amount));
        });
    }

    private void sendTransaction(Credentials c, String toAddress, double value){
        if (mTokenModel.isEth) {
            sendTransactionForETH(c,toAddress,value);
        } else {
            sendTransactionForOther(c,toAddress,value);
        }
    }

    private void sendTransactionForETH(Credentials c, String toAddress, double value){
        Log.i("TAG", "onClickTransfer: value = " + BigDecimal.valueOf(value));
        Web3j web3j = Web3jUtils.initWeb3j(null);
       Single.fromCallable(()-> Transfer.sendFunds(
                web3j, c, toAddress,
                BigDecimal.valueOf(value), Convert.Unit.ETHER)
                .send())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TransactionReceipt>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("TAG", "onSubscribe: Disposable" );
                    }

                    @Override
                    public void onSuccess(TransactionReceipt transactionReceipt) {
                        Log.i("TAG", "onSuccess:  " + transactionReceipt.toString());
                        mTvTransferData.setText(transactionReceipt.toString());
                        String fromAddr = mWallet.getWalletFile().getAddress();
                        getBalance(Keys.toChecksumAddress(fromAddr),mTvAddressTransferFrom,mTvBalanceFrom);
                        getBalance(Keys.toChecksumAddress(toAddress),mTvAddressTransferTo,mTvBalanceTo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onError: "  );
                        e.printStackTrace();
                    }
                });
    }


    private void sendTransactionForOther(Credentials c, String toAddr, double value){
        Log.i(TAG, "transferByContract:转账金额为 " + value);

        Single.fromCallable(()->mContract.transfer(toAddr,BigDecimal.valueOf(value).toBigInteger())
                .send())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TransactionReceipt>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(TransactionReceipt transactionReceipt) {
                        Log.i(TAG, "onSuccess: 挖矿成功！" + transactionReceipt.toString());
                        Toast.makeText(TransferActivity.this, "转账成功！", Toast.LENGTH_SHORT).show();
                        mTvTransferData.setText(transactionReceipt.toString());
                        String fromAddr = mWallet.getWalletFile().getAddress();
                        getBalance(Keys.toChecksumAddress(fromAddr),mTvAddressTransferFrom,mTvBalanceFrom);
                        getBalance(Keys.toChecksumAddress(toAddr),mTvAddressTransferTo,mTvBalanceTo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: 挖矿失败 " );
                        Toast.makeText(TransferActivity.this, "转账失败！", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
    }





    public Single<BigInteger> getBalance(String address) {
        if (mTokenModel.isEth) {
            return BaibeiTransferUtils.getBalanceForETH(address);
        } else {
            return BaibeiTransferUtils.balanceOf(mContract,address);
        }
    }

    private void getBalanceForETH(String addr) {
        BaibeiTransferUtils.getBalanceForETH(addr)
                .subscribe(mBalanceObserver);
    }

    private void getBalance(String add, TextView tvAddr, TextView tvBal){
        getBalance(add)
                .subscribe(new SingleObserver<BigInteger>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(BigInteger bigInteger) {
                        Log.i("TAG", "onClickGetBalance: " + bigInteger);
                        tvAddr.setText(add);
                        tvBal.setText("" + bigInteger.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", "onClickGetBalance: 报错了 ");
                        tvAddr.setText("报错了 " + e.getMessage());
                        tvBal.setText("");
                        e.printStackTrace();
                    }
                });
    }


    private SingleObserver<BigInteger> mBalanceObserver = new SingleObserver<BigInteger>() {
        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onSuccess(BigInteger bigInteger) {
            mTvBalanceToken.setText("余额：" + bigInteger.toString());
        }

        @Override
        public void onError(Throwable e) {
            mTvBalanceToken.setText("余额查询有误！" + e.getMessage());
        }
    };

    private void getBalanceForToken() {
        if (mContract == null) {
            Toast.makeText(this, "合约未加载完成！", Toast.LENGTH_SHORT).show();
            return;
        }

        BaibeiTransferUtils.balanceOf(mContract, mWallet.getWalletFile().getAddress())
                .subscribe(mBalanceObserver);
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
                        Toast.makeText(TransferActivity.this, "合约加载完成！", Toast.LENGTH_SHORT).show();
                        mContract = token;
                        getBalanceForToken();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(TransferActivity.this, "合约加载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private BigInteger getGasPrice() {
        return new BigInteger("100000000000");
    }

    private BigInteger getGasLimit() {
        return BigInteger.valueOf(600000);
    }

}
