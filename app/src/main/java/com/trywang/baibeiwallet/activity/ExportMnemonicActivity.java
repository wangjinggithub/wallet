package com.trywang.baibeiwallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.trywang.baibeiwallet.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExportMnemonicActivity extends AppCompatActivity {

    @BindView(R.id.id_flowlayout_sel)
    TagFlowLayout mFlowLayoutSel;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;
    @BindView(R.id.tv)
    TextView mTv;
    List<MnemonicModel> mList = new ArrayList<>();
    List<MnemonicModel> mListSel = new ArrayList<>();
    TagAdapter mAdapterSel;
    TagAdapter mAdapter;

    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_mnemonic);
        ButterKnife.bind(this);
        initData();
        initFlowLayoutSel();
        init();
    }

    private void initData() {
        List<MnemonicModel> tempList0 = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            tempList0.add(new MnemonicModel("string" + i, i));
            sb.append(tempList0.get(i).mnemonic + " ");
        }
        result = sb.toString();
        sb.delete(0,sb.length());

        for (int i = 0; i < 10; i++) {
            int sel = (int) (Math.random() * 100 % tempList0.size());
            mList.add(tempList0.remove(sel));
        }
        sb = new StringBuilder();
        for (int i = 0; i < mList.size(); i++) {
            sb.append(mList.get(i).mnemonic + " ");
        }
        mTv.setText(sb.toString());
    }

    private void init() {
        mAdapter = new TagAdapter<MnemonicModel>(mList) {
            @Override
            public View getView(FlowLayout parent, int position, MnemonicModel s) {
                View view = LayoutInflater.from(ExportMnemonicActivity.this).inflate(R.layout.item_tag_mnemonic,
                        mFlowLayout, false);
                TextView tv = view.findViewById(R.id.tv);
                s.index = position;
                tv.setText(s.mnemonic);
                return view;
            }
        };

        mFlowLayout.setAdapter(mAdapter);
        mFlowLayout.setOnTagClickListener((view, position, parent) -> {
            boolean has = mListSel.contains(mList.get(position));
            if (has) {
                mListSel.remove(mList.get(position));
            } else {
                mListSel.add(mList.get(position));
            }
            mAdapterSel.notifyDataChanged();
            return false;
        });
    }

    private void initFlowLayoutSel() {
        if (mAdapterSel == null) {
            mAdapterSel = new TagAdapter<MnemonicModel>(mListSel) {
                @Override
                public View getView(FlowLayout parent, int position, MnemonicModel s) {
                    View view = LayoutInflater.from(ExportMnemonicActivity.this).inflate(R.layout.item_tag_mnemonic_sel,
                            mFlowLayout, false);
                    TextView tv = view.findViewById(R.id.tv);
                    tv.setText(s.mnemonic);
                    return view;
                }
            };
        }
        mFlowLayoutSel.setAdapter(mAdapterSel);
        mFlowLayoutSel.setOnTagClickListener((view, position, parent) -> {
            MnemonicModel model = mListSel.remove(position);
            Set<Integer> sel = mFlowLayout.getSelectedList();
            sel.remove(model.index);
            mAdapter.setSelectedList(sel);
            mAdapterSel.notifyDataChanged();
            return false;
        });
    }

    @OnClick(R.id.tv)
    public void onClick() {
        initData();
    }

    @OnClick(R.id.tv_check)
    public void onClickCheck(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mListSel.size(); i++) {
            sb.append(mListSel.get(i).mnemonic + " ");
        }
        if (result.equalsIgnoreCase(sb.toString())) {
            Toast.makeText(this, "校验通过！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "校验未通过！", Toast.LENGTH_SHORT).show();
        }
    }

    class MnemonicModel {
        String mnemonic;
        int index;

        public MnemonicModel(String mnemonic, int index) {
            this.mnemonic = mnemonic;
            this.index = index;
        }
    }
}
