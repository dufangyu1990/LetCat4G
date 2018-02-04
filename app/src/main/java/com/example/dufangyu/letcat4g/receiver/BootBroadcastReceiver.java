package com.example.dufangyu.letcat4g.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.dufangyu.letcat4g.service.MyService;
import com.example.dufangyu.letcat4g.utils.LogUtil;

import static com.example.dufangyu.letcat4g.utils.Constant.ACTION_BOOT;

/**
 * Created by dufangyu on 2018/1/21.
 */

public class BootBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT)){

//            Intent mBootIntent = new Intent(context, MainActivity.class);
//            // 下面这句话必须加上才能开机自动运行app的界面
//            mBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(mBootIntent);

            LogUtil.d("dfy","接受开机广播");
            Intent mBootIntent = new Intent(context,MyService.class);
            context.startService(mBootIntent);

        }

    }
}
