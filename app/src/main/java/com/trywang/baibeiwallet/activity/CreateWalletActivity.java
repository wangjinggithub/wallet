package com.trywang.baibeiwallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trywang.baibeiwallet.BaibeiWallet;
import com.trywang.baibeiwallet.ClipboardUtils;
import com.trywang.baibeiwallet.R;
import com.trywang.baibeiwallet.WalletManager;
import com.trywang.baibeiwallet.event.CreateWalletEvent;
import com.trywang.baibeiwallet.utils.BaiBeiWalletUtils;

import org.greenrobot.eventbus.EventBus;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Keys;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateWalletActivity extends AppCompatActivity {
    @BindView(R.id.tv_address)
    public TextView mTvAddress;

    @BindView(R.id.et_pwd)
    public EditText mEtPwd;

    BaibeiWallet mWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_copy_address)
    public void onClickCopyAddress(){
        String address = mTvAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "地址为空", Toast.LENGTH_SHORT).show();
            return;
        }

        ClipboardUtils.copy(address);
    }

    @OnClick(R.id.btn_create_wallet)
    public void onClickCreate_wallet(){
        String pwd = mEtPwd.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        mTvAddress.setText("正在创建地址，请稍等。。。");
        try {
            mWallet = BaiBeiWalletUtils.generateBip44Wallet(pwd);
            WalletManager.getInstance().addWallet(mWallet);
            String address = mWallet.getWalletFile().getAddress();
            Log.i("TAG", "onClickCreateWallet: 钱包地址 = " + address);
            mTvAddress.setText(Keys.toChecksumAddress(address));
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(mTvAddress.getText().toString())) {
            EventBus.getDefault().post(new CreateWalletEvent());
        }
        super.onBackPressed();
    }
}
