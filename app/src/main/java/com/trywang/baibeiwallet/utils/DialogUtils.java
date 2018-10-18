package com.trywang.baibeiwallet.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.trywang.baibeiwallet.R;

/**
 * TODO 写清楚此类的作用
 *
 * @author Try
 * @date 2018/9/13 14:18
 */
public class DialogUtils {

    public interface IEnterPwdListener{
        void onSuccess(String pwd);
    }

    public static void hide(Context context,View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }


    public static void enterPwd(Activity activity,IEnterPwdListener successListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_pwd,null,false);
        AlertDialog alert = builder.setView(view)
                .create();
        EditText etPwd = view.findViewById(R.id.et_pwd);
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "取消输入", Toast.LENGTH_SHORT).show();
                hide(activity,etPwd);
                alert.dismiss();

            }
        });
        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (successListener != null) {
                    successListener.onSuccess(etPwd.getText().toString());
                }
                hide(activity,etPwd);
                Toast.makeText(activity, "密码是 " + etPwd.getText().toString(), Toast.LENGTH_SHORT).show();
                alert.dismiss();
            }
        });
        alert.show();
    }
}
