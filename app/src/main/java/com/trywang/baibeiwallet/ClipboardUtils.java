package com.trywang.baibeiwallet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * TODO 写清楚此类的作用
 *
 * @author Try
 * @date 2018/9/7 15:05
 */
public class ClipboardUtils {

    public static void copy(String value){
        ClipboardManager cm = (ClipboardManager) BaseApplication.sInstance.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(null, value));
        Toast.makeText(BaseApplication.sInstance, "复制成功", Toast.LENGTH_SHORT).show();
    }
}
