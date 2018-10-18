package com.trywang.baibeiwallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.trywang.baibeiwallet.BaibeiWallet;
import com.trywang.baibeiwallet.ClipboardUtils;
import com.trywang.baibeiwallet.R;
import com.trywang.baibeiwallet.WalletManager;
import com.trywang.baibeiwallet.WalletUtils;
import com.trywang.baibeiwallet.utils.DialogUtils;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.utils.Numeric;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletSettingActivity extends AppCompatActivity {
    @BindView(R.id.tv_export_data)
    TextView mTvExprotData;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    BaibeiWallet mWallet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_setting);
        ButterKnife.bind(this);
        int index = getIntent().getIntExtra("index",0);
        mWallet = WalletManager.getInstance().getWallet(index);
        mTvAddress.setText(Keys.toChecksumAddress(mWallet.getWalletFile().getAddress()));
    }

    @OnClick(R.id.btn_export_mnemonic)
    public void onClickExportMnemonic(){
        Toast.makeText(this, "开始导出，请稍等。。。", Toast.LENGTH_SHORT).show();
        long startTime = System.currentTimeMillis();
        if (mWallet == null) {
            Toast.makeText(this, "暂时没有导入钱包", Toast.LENGTH_SHORT).show();
            return;
        }

        DialogUtils.enterPwd(WalletSettingActivity.this,(pwd)->{
            if (!checkPwd(pwd)) {
                return;
            }

            Log.i("TAG", "onClickExportMnemonic: 助记词 = " + mWallet.getMnemonic());

            long timeSpace = System.currentTimeMillis() - startTime;
            Log.i("TAG", "onClickExportMnemonic: 校验密码之前 = " + timeSpace);

            if (!WalletUtils.checkPwd(pwd, mWallet.getWalletFile())) {
                Toast.makeText(this, "密码错误！", Toast.LENGTH_SHORT).show();
                return;
            }
            timeSpace = System.currentTimeMillis() - startTime;
            Log.i("TAG", "onClickExportMnemonic: " + "总耗时 = " + timeSpace + ";助记词 = " + mWallet.getMnemonic());
            mTvExprotData.setText(mWallet.getMnemonic());
        });
    }
    @OnClick(R.id.btn_export_keystore)
    public void onClickExportKeystore(){
        long startTime = System.currentTimeMillis();
        if (mWallet == null) {
            Toast.makeText(this, "暂时没有导入钱包", Toast.LENGTH_SHORT).show();
            return ;
        }

        DialogUtils.enterPwd(WalletSettingActivity.this,(pwd)->{
            if (!checkPwd(pwd)) {
                return ;
            }
            long timeSpace = System.currentTimeMillis() - startTime;
            Log.i("TAG", "onClickExportMnemonic: 校验密码之前 = " + timeSpace);

            if (!WalletUtils.checkPwd(pwd, mWallet.getWalletFile())) {
                Toast.makeText(this, "密码错误！", Toast.LENGTH_SHORT).show();
                return ;
            }
            timeSpace = System.currentTimeMillis() - startTime;
            Log.i("TAG", "总消耗时间 = " + timeSpace + "keyStore = " + mWallet.getKeyStoreJsonString());
            mTvExprotData.setText( mWallet.getKeyStoreJsonString());
        });


    }
    @OnClick(R.id.btn_export_key)
    public void onClickExportKey(){
        long startTime = System.currentTimeMillis();
        if (mWallet == null) {
            Toast.makeText(this, "暂时没有导入钱包", Toast.LENGTH_SHORT).show();
            return ;
        }

        DialogUtils.enterPwd(WalletSettingActivity.this,(pwd)->{
            if (!checkPwd(pwd)) {
                return ;
            }
            long timeSpace = System.currentTimeMillis() - startTime;
            Log.i("TAG", "onClickExportMnemonic: 校验密码之前 = " + timeSpace);

            if (!WalletUtils.checkPwd(pwd, mWallet.getWalletFile())) {
                Toast.makeText(this, "密码错误！", Toast.LENGTH_SHORT).show();
                return ;
            }
            timeSpace = System.currentTimeMillis() - startTime;
            Log.i("TAG", "checkPwdAll: 总耗时 = " + timeSpace);

            try {
                ECKeyPair ecKeyPair = Wallet.decrypt(pwd, mWallet.getWalletFile());
                String privateKey = Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey());
                timeSpace = System.currentTimeMillis() - startTime;

                Log.i("TAG", "总消耗时间 = " + timeSpace + ";私钥 = " + privateKey);
                mTvExprotData.setText( privateKey);
            } catch (CipherException e) {
                e.printStackTrace();
            }
        });



    }
    @OnClick(R.id.btn_copy)
    public void onClickCopy(){
        String result = mTvExprotData.getText().toString();
        if (TextUtils.isEmpty(result)) {
            Toast.makeText(this, "数据不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        ClipboardUtils.copy(result);
    }

    private boolean checkPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
