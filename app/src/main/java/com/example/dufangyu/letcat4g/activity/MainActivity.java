package com.example.dufangyu.letcat4g.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dufangyu.letcat4g.present.ActivityPresentImpl;
import com.example.dufangyu.letcat4g.service.MyService;
import com.example.dufangyu.letcat4g.utils.MyToast;
import com.example.dufangyu.letcat4g.utils.SerialPortUtil;
import com.example.dufangyu.letcat4g.view.MainView;


public class MainActivity extends ActivityPresentImpl<MainView> implements View.OnClickListener {

    private long exitTime=0;

    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        Intent intent3 = new Intent(this, MyService.class);
        startService(intent3);

    }

    public void pressAgainExit(){
        if((System.currentTimeMillis()-exitTime) > 2000){
            MyToast.showTextToast(getApplicationContext(), "再按一次退出");
            exitTime = System.currentTimeMillis();
        } else {
            SerialPortUtil.getInstance().closeSerialPort();
            finish();
            System.exit(0);
        }
    }


    @Override
    public void doNetConnect() {
        super.doNetConnect();

    }





    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
//            case R.id.button:
//                IntentWrapper.whiteListMatters(this, "推送数据服务的持续运行");
//                break;
        }
    }
}
