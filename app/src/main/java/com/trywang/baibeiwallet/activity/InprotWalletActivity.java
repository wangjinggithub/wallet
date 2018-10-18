package com.trywang.baibeiwallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.trywang.baibeiwallet.BaibeiWallet;
import com.trywang.baibeiwallet.R;
import com.trywang.baibeiwallet.WalletManager;
import com.trywang.baibeiwallet.event.CreateWalletEvent;
import com.trywang.baibeiwallet.utils.BaiBeiWalletUtils;

import org.greenrobot.eventbus.EventBus;
import org.web3j.crypto.CipherException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InprotWalletActivity extends AppCompatActivity {
    @BindView(R.id.et_data_inprot)
    public EditText mEtData;
    @BindView(R.id.et_pwd)
    public EditText mEtPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inprot_wallet);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_inprot_mnemonic)
    public void onClickInportMnemonic(){
        String pwd = mEtPwd.getText().toString();
        String mnemonic = mEtData.getText().toString();
        if (!checkPwd(pwd) ) {
            return;
        }
        if (TextUtils.isEmpty(mnemonic)) {
            Toast.makeText(this, "助记词不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            BaibeiWallet baibeiWallet = BaiBeiWalletUtils.generateBip44Wallet(mnemonic,pwd);
            WalletManager.getInstance().addWallet(baibeiWallet);
            Toast.makeText(this, "导入成功", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new CreateWalletEvent());
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @OnClick(R.id.btn_inprot_keystore)
    public void onClickInportKeystore(){
        String pwd = mEtPwd.getText().toString();
        String keystore = mEtData.getText().toString();
        if (!checkPwd(pwd) ) {
            return;
        }
        if (TextUtils.isEmpty(keystore)) {
            Toast.makeText(this, "Keystore不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            BaibeiWallet baibeiWallet = BaiBeiWalletUtils.loadByKeyStore(pwd,keystore);
            WalletManager.getInstance().addWallet(baibeiWallet);
            Toast.makeText(this, "导入成功", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new CreateWalletEvent());
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_inprot_key)
    public void onClickInportKey(){
        String pwd = mEtPwd.getText().toString();
        String privateKey = mEtData.getText().toString();
        if (!checkPwd(pwd) ) {
            return;
        }
        if (TextUtils.isEmpty(privateKey)) {
            Toast.makeText(this, "私钥不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            BaibeiWallet baibeiWallet = BaiBeiWalletUtils.generateBip44WalletByPrivateKey(privateKey,pwd);
            WalletManager.getInstance().addWallet(baibeiWallet);
            Toast.makeText(this, "导入成功", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new CreateWalletEvent());
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

}
