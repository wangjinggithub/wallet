package com.trywang.baibeiwallet.solidity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.trywang.baibeiwallet.R;
import com.trywang.baibeiwallet.WalletManager;
import com.trywang.baibeiwallet.WalletUtils;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static org.web3j.tx.Transfer.GAS_LIMIT;

public class DeployContract extends AppCompatActivity {
    public static final String TAG = DeployContract.class.getSimpleName();
    Coin contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy_contract);
        ButterKnife.bind(this);
    }



    @OnClick(R.id.btn_init_web3j)
    public void initWeb3j(){
        initWeb3j(null);
    }
    @OnClick(R.id.btn_load)
    public void onClickLoad(){
        try {
            String add = "0xb01c131da9fd305e49217d1219e726c36f137f85";
            Credentials credentials = WalletUtils.loadCredentials("1",
                    WalletManager.getInstance().getWallet(0).getWalletFile());
//            BigInteger gasPrice = requestCurrentGasPrice();
            BigInteger gasPrice = BigInteger.valueOf(1);
            BigInteger gasLimit = BigInteger.valueOf(6111000);
            Log.i(TAG, "onClickDeploy: gasPrice = " + gasPrice);
            Log.i(TAG, "onClickDeploy: GAS_LIMIT = " + GAS_LIMIT);
            Single.fromCallable(()->
                    Coin.load(add, mWeb3j, credentials, gasPrice, gasLimit)
//                    Coin.deploy(
//                    mWeb3j,credentials, gasPrice,GAS_LIMIT)
//                    .send()
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Coin>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(Coin coin) {
                            contract = coin;
                            Log.i(TAG, "onSuccess: 加载成功" + coin);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: 部署失败" );
                            e.printStackTrace();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }

    }

//0x63d33999f8361f7cddf22fc351db901973f38348f629e9b97d131bb4ecdd4ac8
    @OnClick(R.id.btn_deploy)
    public void onClickDeploy(){
        try {
            Credentials credentials = WalletUtils.loadCredentials("1",
                    WalletManager.getInstance().getWallet(0).getWalletFile());
//            BigInteger gasPrice = requestCurrentGasPrice();
            BigInteger gasPrice = BigInteger.valueOf(1);
            BigInteger gasLimit = BigInteger.valueOf(200000);
            Log.i(TAG, "onClickDeploy: gasPrice = " + gasPrice);
            Log.i(TAG, "onClickDeploy: GAS_LIMIT = " + GAS_LIMIT);
            Single.fromCallable(()->Token.deploy(
                        mWeb3j,credentials, gasPrice,gasLimit)
                        .send())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<Token>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(Token coin) {
                    Log.i(TAG, "onSuccess: 部署成功");
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "onError: 部署失败" );
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }

    }
    @OnClick(R.id.btn_balance)
    public void onClickGetBalance(){
        Single.fromCallable(()->contract.balances("0xf1fce8e7e47f5fa7ddf41ccd55d75810e41a14f4")
                .send())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new SingleObserver<BigInteger>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(BigInteger bigInteger) {
                Log.i(TAG, "onSuccess: 查询余额成功 = " + bigInteger.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onSuccess: 查询余额失败 " );
                e.printStackTrace();
            }
        });
    }

    @OnClick(R.id.btn_mint)
    public void onClickMint(){
//        0xf1fce8e7e47f5fa7ddf41ccd55d75810e41a14f4
        Single.fromCallable(()->contract.mint("0xf1fce8e7e47f5fa7ddf41ccd55d75810e41a14f4",BigInteger.valueOf(123))
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
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: 挖矿失败 " );
                e.printStackTrace();
            }
        });
    }

    public BigInteger requestCurrentGasPrice() throws IOException {
        EthGasPrice ethGasPrice = mWeb3j.ethGasPrice().send();
        return ethGasPrice.getGasPrice();
    }


    Web3j mWeb3j;

    private void initWeb3j(String url) {
        url = TextUtils.isEmpty(url) ? "http://192.168.100.73:8545" : url;
        mWeb3j = Web3jFactory.build(new HttpService(url,createOkHttpClient(),true));  // defaults to http://localhost:8545/
//        Web3j web3 = Web3jFactory.build(new HttpService(url));  // defaults to http://localhost:8545/
        Web3ClientVersion web3ClientVersion = null;
        try {
            web3ClientVersion = mWeb3j.web3ClientVersion().sendAsync().get();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            Toast.makeText(this, "连接成功 " + clientVersion, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onClickTransfer: clientVersion = " + clientVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        Log.i(TAG, "log: " + msg);
                    }
                });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
    }

}
