package com.example.dufangyu.letcat4g.activity;

import android.os.Bundle;
import android.view.View;

import com.example.dufangyu.letcat4g.biz.IMain;
import com.example.dufangyu.letcat4g.biz.MainBiz;
import com.example.dufangyu.letcat4g.biz.MainListener;
import com.example.dufangyu.letcat4g.present.ActivityPresentImpl;
import com.example.dufangyu.letcat4g.utils.LightManager;
import com.example.dufangyu.letcat4g.utils.LogUtil;
import com.example.dufangyu.letcat4g.utils.MyToast;
import com.example.dufangyu.letcat4g.view.MainView;

import static com.example.dufangyu.letcat4g.utils.Constant.DEVICEIDTD;
import static com.example.dufangyu.letcat4g.utils.Constant.DEVICEIDTYPE;


public class MainActivity extends ActivityPresentImpl<MainView> implements MainListener,View.OnClickListener {

    private long exitTime=0;
    private IMain mainBiz;
    private String deviceId;




    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        mainBiz = new MainBiz(this);
        MyApplication.getInstance().setStringPerference("lightState","0");
        MyApplication.getInstance().setStringPerference("lockState","1");
        MyApplication.getInstance().setStringPerference("doorState","1");
        MyApplication.getInstance().setStringPerference("batteryState","70,0,90");

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
        LightManager.getInstance().setListener(this);
//        TraceServiceImpl.sShouldStopService = false;
//        TraceServiceImpl.setModelBiz(mainBiz);
//        startService(new Intent(this, TraceServiceImpl.class));



    }

    @Override
    public void loginFailed() {

    }

    //收到灯控指令
    @Override
    public void openLight(String type) {

        LightManager.getInstance().openLight(type);
    }
    //收到巡检指令
    @Override
    public void getCheckOrder(String deviceId) {
        String lightState=MyApplication.getInstance().getStringPerference("lightState");
        LogUtil.d("dfy","lightState = "+lightState);
        this.deviceId = deviceId;
        String lockState="1";
        String doorState="1";
        String batteryState="70,80,90";
        mainBiz.sendDeviceData(deviceId,lightState,lockState,doorState,batteryState);
    }

    @Override
    public void openLightSuccess() {
        String lightState=MyApplication.getInstance().getStringPerference("lightState");
        LogUtil.d("dfy","lightState = "+lightState);
        String lockState="1";
        String doorState="1";
        String batteryState="70,80,90";
        mainBiz.sendDeviceData(deviceId,lightState,lockState,doorState,batteryState);
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
