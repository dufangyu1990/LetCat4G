package com.example.dufangyu.letcat4g.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;

import com.example.dufangyu.letcat4g.R;
import com.example.dufangyu.letcat4g.biz.IMain;
import com.example.dufangyu.letcat4g.biz.MainBiz;
import com.example.dufangyu.letcat4g.biz.MainListener;
import com.example.dufangyu.letcat4g.present.ActivityPresentImpl;
import com.example.dufangyu.letcat4g.service.PushDataService;
import com.example.dufangyu.letcat4g.service.TraceServiceImpl;
import com.example.dufangyu.letcat4g.utils.Constant;
import com.example.dufangyu.letcat4g.utils.LogUtil;
import com.example.dufangyu.letcat4g.utils.MyToast;
import com.example.dufangyu.letcat4g.utils.Util;
import com.example.dufangyu.letcat4g.view.MainView;
import com.xdandroid.hellodaemon.IntentWrapper;

import static com.example.dufangyu.letcat4g.utils.Constant.ALARMSTATE;
import static com.example.dufangyu.letcat4g.utils.Constant.AROUNDDEVICE;
import static com.example.dufangyu.letcat4g.utils.Constant.DEVICEIDTD;
import static com.example.dufangyu.letcat4g.utils.Constant.DEVICEIDTYPE;


public class MainActivity extends ActivityPresentImpl<MainView> implements MainListener,View.OnClickListener {

    private long exitTime=0;
    private IMain mainBiz;

    private Handler mHandler = new Handler() {
        // 接收结果，刷新界面
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MSG_SENDDATA:
                    sendData();
                    break;
            }
        };
    };


    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        mainBiz = new MainBiz(this);


    }

    public void pressAgainExit(){
        if((System.currentTimeMillis()-exitTime) > 2000){
            MyToast.showTextToast(getApplicationContext(), "再按一次退出");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }


    @Override
    public void doNetConnect() {
        super.doNetConnect();
        mainBiz.sendLoginCommad(DEVICEIDTYPE,DEVICEIDTD);

    }

    @Override
    public void loginSuccess() {
        LogUtil.d("dfy","登录成功！！");
        Intent intent = new Intent(this, PushDataService.class);
        intent.putExtra("messenger", new Messenger(mHandler));
        startService(intent);
        TraceServiceImpl.sShouldStopService = false;
        startService(new Intent(this, TraceServiceImpl.class));



    }

    @Override
    public void loginFailed() {

    }


    private void sendData()
    {
        int tempvalue  = Util.getRandomValue(20,30);
        int tempvalue2  = Util.getRandomValue(20,80);
//        LogUtil.d("dfy","温度 = "+tempvalue);
//        LogUtil.d("dfy","湿度 = "+tempvalue2);
        mainBiz.sendData(DEVICEIDTYPE,DEVICEIDTD,ALARMSTATE,AROUNDDEVICE,String.valueOf(tempvalue),String.valueOf(tempvalue2));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button:
                IntentWrapper.whiteListMatters(this, "推送数据服务的持续运行");
                break;
        }
    }
}
