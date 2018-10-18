package com.trywang.baibeiwallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.trywang.baibeiwallet.activity.CreateWalletActivity;
import com.trywang.baibeiwallet.activity.InprotWalletActivity;
import com.trywang.baibeiwallet.event.CreateWalletEvent;
import com.trywang.baibeiwallet.event.SelWalletEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.web3j.crypto.Keys;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletManagerActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRecyclerView;
    WalletAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_manager);
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
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.btn_create_wallet)
    public void onClickCreateWallet(){
        Intent i = new Intent(WalletManagerActivity.this, CreateWalletActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_inport_wallet)
    public void onClickInportWallet(){
        Intent i = new Intent(WalletManagerActivity.this, InprotWalletActivity.class);
        startActivity(i);
    }


    private void init(){
        mAdapter = new WalletAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public class WalletHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_title)
        public TextView mTvTitle;
        @BindView(R.id.tv_address)
        public TextView mTvAddress;
        @BindView(R.id.btn_copy_address)
        public Button mBtnCopy;
        @BindView(R.id.btn_sel)
        public Button mBtnSel;

        public WalletHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class WalletAdapter extends RecyclerView.Adapter<WalletHolder>{

        List<BaibeiWallet> mDatas ;
        public WalletAdapter(){
            mDatas = WalletManager.getInstance().getWalletAll();
        }

        public void setData(){
            mDatas = WalletManager.getInstance().getWalletAll();;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public WalletHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(),R.layout.item_wallet,null);
            return new WalletHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WalletHolder holder, int position) {
            holder.mTvTitle.setText("第" + position + "钱包地址：");
            holder.mTvAddress.setText(Keys.toChecksumAddress(mDatas.get(position).walletFile.getAddress()));
            holder.mBtnCopy.setOnClickListener((view) ->{
                ClipboardUtils.copy(holder.mTvAddress.getText().toString());
                Log.i("TAG", "onBindViewHolder: 复制地址 " + holder.mTvAddress.getText().toString());
            });
            holder.mBtnSel.setOnClickListener((view) ->{
                EventBus.getDefault().post(new SelWalletEvent(position));
            });
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateWalletEvent(CreateWalletEvent cwe){
        mAdapter.setData();
    }


}
