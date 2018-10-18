package com.trywang.baibeiwallet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.trywang.baibeiwallet.utils.MnemonicUtils;

import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.web3j.crypto.Hash.sha256;

public class SecondActivity extends AppCompatActivity {

    public static final String TAG = SecondActivity.class.getSimpleName();
    @BindView(R.id.tv)
    TextView mTv0;
    Web3ClientVersion web3ClientVersion = null;

    String mnemonic = "gaze heavy accident trash myself report carpet end domain climb ankle high";

    Handler mHandler = new SecondActivity.XHandler();

    class XHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String clientVersion = web3ClientVersion.getWeb3ClientVersion();
                    Log.i(TAG, "init: clientVersion = " + clientVersion);
                    mTv0.setText("clientVersion = " + clientVersion);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);

    }

    public void init(){
//        String url = "http://localhost:7545/";
        String url = "http://127.0.0.1:7545/";
        HttpService service = new HttpService(url);// defaults to http://localhost:8545/
        final Web3j web3 = Web3jFactory.build((service));



        Web3ClientVersion web3ClientVersion = null;
        try {
            Log.i(TAG, "init: 111" + web3.web3ClientVersion());
            web3ClientVersion = web3.web3ClientVersion().sendAsync().get();
            Log.i(TAG, "init: web3ClientVersion = " + web3ClientVersion);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        web3.web3ClientVersion().observable().subscribe(new Action1<Web3ClientVersion>() {
//            @Override
//            public void call(Web3ClientVersion web3ClientVersion) {
//                String clientVersion = web3ClientVersion.getWeb3ClientVersion();
//                Log.i(TAG, "init: clientVersion = " + clientVersion);
//                mTv0.setText("clientVersion = " + clientVersion);
//            }
//        });


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    web3ClientVersion = web3.web3ClientVersion().send();
//                    mHandler.sendEmptyMessage(1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();


    }


    @OnClick(R.id.btn0)
    public void onClickBtn0(){
//        init();

        Log.i(TAG, "onClickBtn0: " + getPwd());
    }

    @OnClick(R.id.btn1)
    public void onClickBtn1(){
//        File file = getExternalCacheDir();
//        try {
//            Credentials c = WalletUtils.loadCredentials("1234",file.getAbsolutePath()+ "/"+ wallet.getFilename());
//            Log.i(TAG, "onClickBtn1: 第二部点击钱包地址 = " + c.getAddress());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (CipherException e) {
//            e.printStackTrace();
//        }

//        byte[] b = new byte[16];
//        SecureRandom sr = new SecureRandom();
//        sr.nextBytes(b);
//        mnemonic = MnemonicUtils.generateMnemonic(b);

        Log.i(TAG, "onClickBtn1: 生成的助记符 = " + mnemonic);

        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));

        try {
            WalletFile wf1 = Wallet.createLight("123", privateKey);
            Log.i(TAG, "onClickBtn1: wf1 = " + wf1.getId());
            Credentials c = Credentials.create(Wallet.decrypt("123", wf1));
            Log.i(TAG, "onClickBtn1: 地址 c = " + c.getAddress());
        } catch (CipherException e) {
            e.printStackTrace();
        }


    }

    @OnClick(R.id.btn2)
    public void onClickBtn3(){
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));

        try {
            WalletFile wf1 = Wallet.createLight("1234", privateKey);
            Log.i(TAG, "第二次: wf1 = " + wf1.getId());
            Credentials c = Credentials.create(Wallet.decrypt("1234", wf1));
            Log.i(TAG, "第二次: 地址 c = " + c.getAddress());
        } catch (CipherException e) {
            e.printStackTrace();
        }

//        Credentials c = WalletUtils.loadBip39Credentials(null,mnemonic);
//        Log.i(TAG, "恢复第2次: 地址 c = " + c.getAddress());
    }
    @OnClick(R.id.btn3)
    public void onClickBtn4(){



//        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "1234");
//        ECKeyPair privateKey = ECKeyPair.create(sha256(seed));

//        try {
//            WalletFile wf1 = Wallet.createLight("1234", privateKey);
//            Log.i(TAG, "第3次: wf1 = " + wf1.getId());
        Credentials c = WalletUtils.loadBip39Credentials("1234",mnemonic);
        String privateKey = "7253e452716857e806e3e20ec04976e141c78f34a22408f0b7a29dd5c61b85fc";
        String privateKey0 = "70591658037768891181623601018978678524879057393889454295878762611354316593108";

        String pk = "9c117669cf24cb5f85e59adbdc7fa9eb357edad11dbd87d66471c2db8d1057d4";
        StringBuilder sb = new StringBuilder();
        byte[] buf = pk.getBytes();
        for (int i = 0; i < buf.length; i++) {
            sb.append(buf[i]);
        }
        Log.i(TAG, "onClickBtn4: 反推出 " + sb.toString());

        buf = Numeric.hexStringToByteArray(pk);
        sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            sb.append(buf[i]);
        }


        Log.i(TAG, "onClickBtn4: 2反推出 " + sb.toString());

        byte[] b = privateKey.getBytes();
        Log.i(TAG, "onClickBtn4:私钥 " + c.getEcKeyPair().getPrivateKey());
        Log.i(TAG, "onClickBtn4:16进制私钥 " + Numeric.toHexStringNoPrefix(c.getEcKeyPair().getPrivateKey()));
        Log.i(TAG, "onClickBtn4:16进制私钥 " + Numeric.toHexString(c.getEcKeyPair().getPrivateKey().toByteArray()));
        Log.i(TAG, "onClickBtn4:gong钥 " + c.getEcKeyPair().getPublicKey());
        Log.i(TAG, "恢复第3次: 地址 c = " + c.getAddress());
//        } catch (CipherException e) {
//            e.printStackTrace();
//        }
    }

    Bip39Wallet wallet = null;

    private String getPwd(){
//        Wallet w = Wallet.createLight()
//        byte bytes[] = new byte[32];
//        SecureRandom sr = new SecureRandom();
//        SecureRandom sr = SecureRandomUtils.secureRandom();
//        SecureRandomUtils.secureRandom();
//        random.
//        sr.nextBytes(bytes);
//        Log.i(TAG, "getPwd: length = " + bytes.length);
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < bytes.length; i++) {
//            Log.i(TAG, "getPwd: i = " + i + "; bytes[i] = " + bytes[i]);
//            sb.append(bytes[i]);
//        }
//        Log.i(TAG, "getPwd: sb.toString() = " + sb.toString());
//        mTv0.setText(sb.toString());

//        LinuxSecureRandom lsr = new LinuxSecureRandom();
//        String mnemonic = MnemonicUtils.generateMnemonic(bytes);
//        byte[] masterKey = MnemonicUtils.generateSeed(mnemonic,"123");//marster key 第一组

//        Credentials.create()
//        KeyStore k ;
//        ECKeyPair.create()

//        MnemonicUtils.generateSeed()
//        Log.i(TAG, "getPwd: 助记词 = " + mnemonic);
//        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "123");

        File file = getExternalCacheDir();
        Log.i(TAG, "keystroe地址: file = " + file);

        try {

            wallet = WalletUtils.generateBip39Wallet("123",file);
            Credentials c = WalletUtils.loadCredentials("123",file.getAbsolutePath()+ "/"+ wallet.getFilename());
            Log.i(TAG, "钱包: 地址 = " + c.getAddress());
//            ECKeyPair privateKey = ECKeyPair.create(sha256(seed));
            ECKeyPair privateKey = c.getEcKeyPair();

            Log.i("Bip39Wallet222222", "generateBip39Wallet: getPrivateKey = " + privateKey.getPrivateKey()
                    + "\ngetPublicKey = " + privateKey.getPublicKey());

            Log.i(TAG, "钱包 Bip39Wallet = " + wallet.toString());

        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";

    }


}
