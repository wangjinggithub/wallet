package com.trywang.baibeiwallet;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trywang.baibeiwallet.solidity.DeployContract;
import com.trywang.baibeiwallet.utils.BaiBeiWalletUtils;

import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    List<Bip39Wallet> mListWallet = new ArrayList<>();
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.et_pwd)
    EditText mEtPwd;
    @BindView(R.id.tv_result)
    TextView mTvResult;
    @BindView(R.id.et_web3j_addr)
    EditText mEtWeb3jAddr;
    private long mLastPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_contract)
    public void onClickJumpToContract(){
        Intent i = new Intent(MainActivity.this, DeployContract.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_init_web3j)
    public void onClickInitWeb3j(){
        initWeb3j(mEtWeb3jAddr.getText().toString());
    }

    @OnClick(R.id.btn_wallet_all)
    public void onClickWalletAll(){
        Intent i = new Intent(MainActivity.this,WalletManagerActivity.class);
        startActivity(i);
    }

    /**
     * 生成钱包
     */
    @OnClick(R.id.btn_create_wallet)
    public void onClickCreateWallet() {
        String pwd = mEtPwd.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        mTvAddress.setText("正在创建地址，请稍等。。。");

        try {

            BaibeiWallet wallet = WalletUtils.generateBip39WalletV2(pwd);
            WalletManager.getInstance().addWallet(wallet);
            mListWallet.add(wallet);
            String address = wallet.getWalletFile().getAddress();
            Log.i(TAG, "onClickCreateWallet: 钱包地址 = " + address);
            mTvAddress.setText(Keys.toChecksumAddress(address));
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @OnClick(R.id.btn_copy_address)
    public void onClickCopyAddress() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(null, mTvAddress.getText().toString()));
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 导出助记符
     */
    @OnClick(R.id.btn_mnemonic)
    public void onClickExportMnemonic() {
        mTvResult.setText("开始导出，请稍等。。。");
        long startTime = System.currentTimeMillis();
        if (mListWallet.size() <= 0) {
            Toast.makeText(this, "暂时没有导入钱包", Toast.LENGTH_SHORT).show();
            return;
        }

        String pwd = mEtPwd.getText().toString();
        if (!checkPwd(pwd)) {
            return;
        }

        BaibeiWallet wallet = (BaibeiWallet) mListWallet.get(0);
        Log.i(TAG, "onClickExportMnemonic: 助记词 = " + wallet.getMnemonic());

        long timeSpace = System.currentTimeMillis() - startTime;
        Log.i(TAG, "onClickExportMnemonic: 校验密码之前 = " + timeSpace);

        if (!WalletUtils.checkPwd(pwd, wallet.getWalletFile())) {
            Toast.makeText(this, "密码错误！", Toast.LENGTH_SHORT).show();
            return;
        }

        timeSpace = System.currentTimeMillis() - startTime;

        mTvResult.setText("总耗时 = " + timeSpace + ";助记词 = " + wallet.getMnemonic());
    }

    /**
     * 导出keystore
     */
    @OnClick(R.id.btn_keystore)
    public void onClickExportKeystore() {
        if (!checkPwdAll()) {
            return;
        }

        BaibeiWallet wallet = (BaibeiWallet) mListWallet.get(0);
        Log.i(TAG, "onClickExportKeystore: " + wallet.getKeyStoreJsonString());

        mTvResult.setText("keyStore = " + wallet.getKeyStoreJsonString());
    }

    /**
     * 导出私钥
     */
    @OnClick(R.id.btn_private_key)
    public void onClickExportPrivateKey() {
        long startTime = System.currentTimeMillis();
        if (!checkPwdAll()) {
            return;
        }

        BaibeiWallet wallet = (BaibeiWallet) mListWallet.get(0);

        try {
            ECKeyPair ecKeyPair = Wallet.decrypt(mEtPwd.getText().toString(), wallet.getWalletFile());
            String privateKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey());
            long timeSpace = System.currentTimeMillis() - startTime;

            Log.i(TAG, "总消耗时间 = " + timeSpace + ";私钥 = " + privateKey);
            mTvResult.setText("总消耗时间 = " + timeSpace + ";私钥 = " + privateKey);
        } catch (CipherException e) {
            e.printStackTrace();
        }
    }

    private boolean checkPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkPwdAll() {
        mTvResult.setText("开始导出，请稍等。。。");
        long startTime = System.currentTimeMillis();
        if (mListWallet.size() <= 0) {
            Toast.makeText(this, "暂时没有导入钱包", Toast.LENGTH_SHORT).show();
            return false;
        }

        String pwd = mEtPwd.getText().toString();
        if (!checkPwd(pwd)) {
            return false;
        }

        BaibeiWallet wallet = (BaibeiWallet) mListWallet.get(0);

        long timeSpace = System.currentTimeMillis() - startTime;
        Log.i(TAG, "onClickExportMnemonic: 校验密码之前 = " + timeSpace);

        if (!WalletUtils.checkPwd(pwd, wallet.getWalletFile())) {
            Toast.makeText(this, "密码错误！", Toast.LENGTH_SHORT).show();
            return false;
        }
        timeSpace = System.currentTimeMillis() - startTime;
        Log.i(TAG, "checkPwdAll: 总耗时 = " + timeSpace);
        return true;
    }


    //转账相关

    @BindView(R.id.et_amount_transfer)
    EditText mEtAmountTransfer;
    @BindView(R.id.et_address_other)
    EditText mEtAddressTransferTo;
    @BindView(R.id.tv_transfer_address_one)
    TextView mTvAddressTransferFrom;
    @BindView(R.id.tv_balance_one)
    TextView mTvBalanceFrom;
    @BindView(R.id.tv_transfer_address_two)
    TextView mTvAddressTransferTo;
    @BindView(R.id.tv_balance_two)
    TextView mTvBalanceTo;
    @BindView(R.id.et_address_balance)
    EditText mEtAddressBalance;


    Web3j mWeb3j;

    private void initWeb3j(String url) {
        url = TextUtils.isEmpty(url) ? "http://192.168.100.73:8545" : url;
        mWeb3j = Web3jFactory.build(new HttpService(url, createOkHttpClient(), false));  // defaults to http://localhost:8545/
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


    @OnClick(R.id.btn_balance)
    public void onClickGetBalance() {
        //获取余额
        String address = mEtAddressBalance.getText().toString();
        String addr = TextUtils.isEmpty(address) ? "0x5189f235b0747802afaac563933bd4c065acdad5" : address;

        balanceInWei(addr)
                .subscribe(new SingleObserver<BigInteger>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(BigInteger bigInteger) {
                        Log.i(TAG, "onClickGetBalance: " + bigInteger);
                        mTvAddressTransferTo.setText(addr);
                        mTvBalanceTo.setText("" + bigInteger.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onClickGetBalance: 报错了 ");
                        e.printStackTrace();
                    }
                });
    }

    private void getBalance(String add,TextView tvAddr,TextView tvBal){
        balanceInWei(add)
                .subscribe(new SingleObserver<BigInteger>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(BigInteger bigInteger) {
                        Log.i(TAG, "onClickGetBalance: " + bigInteger);
                        tvAddr.setText(add);
                        tvBal.setText("" + bigInteger.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onClickGetBalance: 报错了 ");
                        tvAddr.setText("报错了 " + e.getMessage());
                        tvBal.setText("");
                        e.printStackTrace();
                    }
                });
    }

    public Single<BigInteger> balanceInWei(String address) {
         return Single.fromCallable(() -> mWeb3j
                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .send()
                .getBalance())
                .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread());

    }

    public void hide(View view){

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);

    }

    AlertDialog alert ;
    public void enterPwd(IEnterPwdListener successListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_pwd,null,false);
        EditText etPwd = view.findViewById(R.id.et_pwd);
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "取消输入", Toast.LENGTH_SHORT).show();
                hide(etPwd);
                alert.dismiss();

            }
        });
//        view.findViewById(R.id.btn_confirm).setOnClickListener(successListener);
        
        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (successListener != null) {
                    successListener.onSuccess(etPwd.getText().toString());
                }
                hide(etPwd);
                Toast.makeText(MainActivity.this, "密码是 " + etPwd.getText().toString(), Toast.LENGTH_SHORT).show();
                alert.dismiss();
            }
        });
        alert = builder.setView(view)
                .create();
        alert.show();
    }

    @OnClick(R.id.btn_transfer)
    public void onClickTransfer() {
        Log.i(TAG, "onClickTransfer: 转账开始");
        BaibeiWallet wallet = WalletManager.getInstance().getWallet(0);
        String toAddress = mEtAddressTransferTo.getText().toString();
//        Keys.toChecksumAddress(address)
        double value = Double.valueOf(mEtAmountTransfer.getText().toString());


        enterPwd((pwd) ->{
            try {
                Credentials c = WalletUtils.loadCredentials(pwd,wallet.getWalletFile());
                sendTransaction(c,toAddress,value);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "密码错误！IOException", Toast.LENGTH_SHORT).show();
            } catch (CipherException e) {
                e.printStackTrace();
                Toast.makeText(this, "密码错误！CipherException", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendTransaction(Credentials c,String toAddress,double value){
        Log.i(TAG, "onClickTransfer: value = " + BigDecimal.valueOf(value));
        Single.fromCallable(()-> Transfer.sendFunds(
                mWeb3j, c, toAddress,
                BigDecimal.valueOf(value), Convert.Unit.ETHER)
                .send())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<TransactionReceipt>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "onSubscribe: Disposable" );
                    }

                    @Override
                    public void onSuccess(TransactionReceipt transactionReceipt) {
                        Log.i(TAG, "onSuccess:  " + transactionReceipt.toString());
                        BaibeiWallet wallet = WalletManager.getInstance().getWallet(0);
                        String fromAddr = wallet.getWalletFile().getAddress();
                        getBalance(Keys.toChecksumAddress(fromAddr),mTvAddressTransferFrom,mTvBalanceFrom);
                        getBalance(Keys.toChecksumAddress(toAddress),mTvAddressTransferTo,mTvBalanceTo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: "  );
                        e.printStackTrace();
                    }
                });
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

    interface IEnterPwdListener{
        void onSuccess(String pwd);
    }


    //导出相关===============begin==============
    @OnClick(R.id.btn_inprot_mnemonic)
    public void onClickInport(){
//        String mnemonic = "again fossil all tired ticket hurry gown modify submit minimum plate mountain";
//        Credentials c = WalletUtils.loadBip39Credentials("123",mnemonic);
//        Credentials c = WalletUtils.loadBip39Credentials("1",mnemonic);
        Log.i(TAG, "onClickInportBuyKeystore: 轻钱包");
        try {
            BaiBeiWalletUtils.generateBip44Wallet("1");
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Log.i(TAG, "onClickInport1111没加盐: c = " + c.getAddress());

//        BaibeiWallet wallet = null;
//        try {
//            wallet = WalletUtils.generateBip39WalletV2("1",mnemonic);
//            WalletManager.getInstance().addWallet(wallet);
//            mListWallet.add(wallet);
//            mTvAddress.setText(wallet.getWalletFile().getAddress());
//        } catch (CipherException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    @OnClick(R.id.btn_inprot_keystore)
    public void onClickInportBuyKeystore(){

        try {
            Bip39Wallet wallet39 = WalletUtils.generateBip39Wallet("1",getCacheDir());
            Bip39Wallet wallet = WalletUtils.generateBip44Wallet("1",getCacheDir());
            Log.i(TAG, "onClickInportBuyKeystore: 完成");
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        String mnemonic = "again fossil all tired ticket hurry gown modify submit minimum plate mountain";
//        Credentials c = WalletUtils.loadBip39Credentials2("123",mnemonic);
//        Log.i(TAG, "onClickInport1111加盐: c = " + c.getAddress());




//        String keystore = "{\"address\":\"00302f9f4c6a8ceafebd7cea272b31c39ad278f9\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"5a67faf24c3a5ab96c8e5c73c2d2a504a09d2d318ac6dba89a59727fa460a64f\",\"cipherparams\":{\"iv\":\"d5131fdb3b341c4aecb5b07dc2aae8fc\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"7e6bfe55c272036c6c13efcf1519c611bf32c1b9c1bd183d043ecf2cb3aaa9f9\"},\"mac\":\"28589b223be5df9f120d480519ad95b2dffbe20c906ade706a2a7641a028aa58\"},\"id\":\"cfc419d7-1d0d-4d08-b4a3-90430dddca31\",\"version\":3}";
//        ObjectMapper om = new ObjectMapper();
//        WalletFile  walletFile = om.readValue(WalletFile.class,keystore);



//        try {
////            JSONObject j = new JSONObject(keystore);
////            WalletFile wf = new WalletFile();
////            wf.setAddress(j.optString(""));
////            wf.setId(j.optString(""));
////            wf.setVersion(j.optString(""));
////            wf.setAddress(j.optString(""));
//
//            WalletFile  walletFile = om.readValue(keystore,WalletFile.class);
////            WalletFile walletFile = new Gson().fromJson(keystore,WalletFile.class);
//            Log.i(TAG, "onClickInportBuyKeystore:getId = " + walletFile.getId()
//                    + "getAddress = " + walletFile.getAddress()
//                    + "getVersion = " + walletFile.getVersion()
//                    + "getCrypto = " + walletFile.getCrypto());
//            Credentials c = WalletUtils.loadCredentials("123456abc",walletFile);
//            Log.i(TAG, "onClickInportBuyKeystore: " + c.getAddress()
//                   +"getPrivateKey = " + c.getEcKeyPair().getPrivateKey());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (CipherException e) {
//            e.printStackTrace();
//        }


//        BaibeiWallet wallet = null;
//        try {
//            wallet = WalletUtils.generateBip39WalletV2("1",mnemonic);
//            WalletManager.getInstance().addWallet(wallet);
//            mListWallet.add(wallet);
//            mTvAddress.setText(wallet.getWalletFile().getAddress());
//        } catch (CipherException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }




    //导出相关===============end==============

//
//
//
//    @OnClick(R.id.btn0)
//    public void onClickBtn0(){
//        Log.i(TAG, "onClickBtn0: " + getPwd());
//    }
//
//    @OnClick(R.id.btn1)
//    public void onClickBtn1(){
////        File file = getExternalCacheDir();
////        try {
////            Credentials c = WalletUtils.loadCredentials("1234",file.getAbsolutePath()+ "/"+ wallet.getFilename());
////            Log.i(TAG, "onClickBtn1: 第二部点击钱包地址 = " + c.getAddress());
////        } catch (IOException e) {
////            e.printStackTrace();
////        } catch (CipherException e) {
////            e.printStackTrace();
////        }
//
////        byte[] b = new byte[16];
////        SecureRandom sr = new SecureRandom();
////        sr.nextBytes(b);
////        mnemonic = MnemonicUtils.generateMnemonic(b);
//
//        Log.i(TAG, "onClickBtn1: 生成的助记符 = " + mnemonic);
//
//        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
//        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
//
//        try {
//            WalletFile wf1 = Wallet.createLight("123", privateKey);
//            Log.i(TAG, "onClickBtn1: wf1 = " + wf1.getId());
//            Credentials c = Credentials.create(Wallet.decrypt("123", wf1));
//            Log.i(TAG, "onClickBtn1: 地址 c = " + c.getAddress());
//        } catch (CipherException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    @OnClick(R.id.btn2)
//    public void onClickBtn3(){
//        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
//        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
//
//        try {
//            WalletFile wf1 = Wallet.createLight("1234", privateKey);
//            Log.i(TAG, "第二次: wf1 = " + wf1.getId());
//            Credentials c = Credentials.create(Wallet.decrypt("1234", wf1));
//            Log.i(TAG, "第二次: 地址 c = " + c.getAddress());
//        } catch (CipherException e) {
//            e.printStackTrace();
//        }
//
////        Credentials c = WalletUtils.loadBip39Credentials(null,mnemonic);
////        Log.i(TAG, "恢复第2次: 地址 c = " + c.getAddress());
//    }
//    @OnClick(R.id.btn3)
//    public void onClickBtn4(){
//
//
//
////        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "1234");
////        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
//
////        try {
////            WalletFile wf1 = Wallet.createLight("1234", privateKey);
////            Log.i(TAG, "第3次: wf1 = " + wf1.getId());
//            Credentials c = WalletUtils.loadBip39Credentials("1234",mnemonic);
//            String privateKey = "7253e452716857e806e3e20ec04976e141c78f34a22408f0b7a29dd5c61b85fc";
//            String privateKey0 = "70591658037768891181623601018978678524879057393889454295878762611354316593108";
//
//            String pk = "9c117669cf24cb5f85e59adbdc7fa9eb357edad11dbd87d66471c2db8d1057d4";
//        StringBuilder sb = new StringBuilder();
//        byte[] buf = pk.getBytes();
//        for (int i = 0; i < buf.length; i++) {
//            sb.append(buf[i]);
//        }
//        Log.i(TAG, "onClickBtn4: 反推出 " + sb.toString());
//
//        buf = Numeric.hexStringToByteArray(pk);
//        sb = new StringBuilder();
//        for (int i = 0; i < buf.length; i++) {
//            sb.append(buf[i]);
//        }
//
//
//        Log.i(TAG, "onClickBtn4: 2反推出 " + sb.toString());
//
//            byte[] b = privateKey.getBytes();
//        Log.i(TAG, "onClickBtn4:私钥 " + c.getEcKeyPair().getPrivateKey());
//        Log.i(TAG, "onClickBtn4:16进制私钥 " + Numeric.toHexStringNoPrefix(c.getEcKeyPair().getPrivateKey()));
//        Log.i(TAG, "onClickBtn4:16进制私钥 " + Numeric.toHexString(c.getEcKeyPair().getPrivateKey().toByteArray()));
//        Log.i(TAG, "onClickBtn4:gong钥 " + c.getEcKeyPair().getPublicKey());
//            Log.i(TAG, "恢复第3次: 地址 c = " + c.getAddress());
////        } catch (CipherException e) {
////            e.printStackTrace();
////        }
//    }
//
//    Bip39Wallet wallet = null;
//
//    private String getPwd(){
////        Wallet w = Wallet.createLight()
////        byte bytes[] = new byte[32];
////        SecureRandom sr = new SecureRandom();
////        SecureRandom sr = SecureRandomUtils.secureRandom();
////        SecureRandomUtils.secureRandom();
////        random.
////        sr.nextBytes(bytes);
////        Log.i(TAG, "getPwd: length = " + bytes.length);
////        StringBuilder sb = new StringBuilder();
////        for (int i = 0; i < bytes.length; i++) {
////            Log.i(TAG, "getPwd: i = " + i + "; bytes[i] = " + bytes[i]);
////            sb.append(bytes[i]);
////        }
////        Log.i(TAG, "getPwd: sb.toString() = " + sb.toString());
////        mTv0.setText(sb.toString());
//
////        LinuxSecureRandom lsr = new LinuxSecureRandom();
////        String mnemonic = MnemonicUtils.generateMnemonic(bytes);
////        byte[] masterKey = MnemonicUtils.generateSeed(mnemonic,"123");//marster key 第一组
//
////        Credentials.create()
////        KeyStore k ;
////        ECKeyPair.create()
//
////        MnemonicUtils.generateSeed()
////        Log.i(TAG, "getPwd: 助记词 = " + mnemonic);
////        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "123");
//
//        File file = getExternalCacheDir();
//        Log.i(TAG, "keystroe地址: file = " + file);
//
//        try {
//
//            wallet = WalletUtils.generateBip39Wallet("123",file);
//            Credentials c = WalletUtils.loadCredentials("123",file.getAbsolutePath()+ "/"+ wallet.getFilename());
//            Log.i(TAG, "钱包: 地址 = " + c.getAddress());
////            ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
//            ECKeyPair privateKey = c.getEcKeyPair();
//
//            Log.i("Bip39Wallet222222", "generateBip39Wallet: getPrivateKey = " + privateKey.getPrivateKey()
//                    + "\ngetPublicKey = " + privateKey.getPublicKey());
//
//            Log.i(TAG, "钱包 Bip39Wallet = " + wallet.toString());
//
//        } catch (CipherException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        return "";
//
//    }


}
