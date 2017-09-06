package com.example.dufangyu.letcat4g.activity;

import android.os.Bundle;

import com.example.dufangyu.letcat4g.biz.IMain;
import com.example.dufangyu.letcat4g.biz.MainBiz;
import com.example.dufangyu.letcat4g.biz.MainListener;
import com.example.dufangyu.letcat4g.present.ActivityPresentImpl;
import com.example.dufangyu.letcat4g.utils.LogUtil;
import com.example.dufangyu.letcat4g.utils.MyToast;
import com.example.dufangyu.letcat4g.utils.Util;
import com.example.dufangyu.letcat4g.view.MainView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.example.dufangyu.letcat4g.utils.Constant.DEVICEIDTD;
import static com.example.dufangyu.letcat4g.utils.Constant.DEVICEIDTYPE;


public class MainActivity extends ActivityPresentImpl<MainView> implements MainListener {

    private long exitTime=0;
    private static SendDataTask  task;
    private IMain mainBiz;
    private static ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor;
    @Override
    public void afterViewCreate(Bundle savedInstanceState) {
        super.afterViewCreate(savedInstanceState);
        mainBiz = new MainBiz(this);
        task = new SendDataTask();

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
        initExecutor();
    }

    @Override
    public void loginFailed() {

    }

    private void initExecutor()
    {
        if(mScheduledThreadPoolExecutor ==null)
            mScheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
        if (mScheduledThreadPoolExecutor != null && task != null) {
            mScheduledThreadPoolExecutor.scheduleAtFixedRate(task, 5, 10, TimeUnit.SECONDS);
        }
    }

    class SendDataTask implements Runnable{

        @Override
        public void run() {
            LogUtil.d("dfy","进行数据请求");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    int tempvalue  = Util.getRandomValue(20,30);
                    LogUtil.d("dfy","tempvalue = "+tempvalue);
                    mainBiz.sendData(DEVICEIDTYPE,DEVICEIDTD,"101",String.valueOf(tempvalue),String.valueOf(tempvalue));

                }
            });

        }

    }
}
