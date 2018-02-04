package com.example.dufangyu.letcat4g.service;

import android.content.Intent;
import android.os.IBinder;

import com.example.dufangyu.letcat4g.R;
import com.example.dufangyu.letcat4g.biz.IMain;
import com.example.dufangyu.letcat4g.socketUtils.TcpConnectUtil;
import com.example.dufangyu.letcat4g.utils.LogUtil;
import com.example.dufangyu.letcat4g.utils.MyToast;
import com.example.dufangyu.letcat4g.utils.Util;
import com.xdandroid.hellodaemon.AbsWorkService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.example.dufangyu.letcat4g.utils.Constant.ALARMSTATE;
import static com.example.dufangyu.letcat4g.utils.Constant.AROUNDDEVICE;
import static com.example.dufangyu.letcat4g.utils.Constant.DEVICEIDTD;
import static com.example.dufangyu.letcat4g.utils.Constant.DEVICEIDTYPE;

public class TraceServiceImpl extends AbsWorkService {

    // 3`1`V`72`7`V`V`V`V
    //是否 任务完成, 不再需要服务运行?
    public static boolean sShouldStopService;
    public static Disposable sDisposable;
    private static Intent myintent;
    private static IMain thisMainBiz;





    public static void setModelBiz(IMain mainBiz)
    {
        thisMainBiz = mainBiz;
    }

    public static void stopService() {
        //我们现在不再需要服务运行了, 将标志位置为 true
        sShouldStopService = true;
        //取消对任务的订阅
        if (sDisposable != null) sDisposable.dispose();
        //取消 Job / Alarm / Subscription
        cancelJobAlarmSub();
    }

    /**
     * 是否 任务完成, 不再需要服务运行?
     * @return 应当停止服务, true; 应当启动服务, false; 无法判断, 什么也不做, null.
     */
    @Override
    public Boolean shouldStopService(Intent intent, int flags, int startId) {
        return sShouldStopService;
    }

    @Override
    public void startWork(final Intent intent, int flags, int startId) {
        sDisposable = Flowable
                .interval(10, TimeUnit.SECONDS)
                //取消任务时取消定时唤醒
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("保存数据到磁盘。");
                        cancelJobAlarmSub();
                    }
                }).observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long count) throws Exception {

                            sendData();
//                        LogUtil.d("dfy","Thread = "+Thread.currentThread().getName());
                    }
                });
    }

    @Override
    public void stopWork(Intent intent, int flags, int startId) {
        stopService();
    }

    /**
     * 任务是否正在运行?
     * @return 任务正在运行, true; 任务当前不在运行, false; 无法判断, 什么也不做, null.
     */
    @Override
    public Boolean isWorkRunning(Intent intent, int flags, int startId) {
        //若还没有取消订阅, 就说明任务仍在运行.
        return sDisposable != null && !sDisposable.isDisposed();
    }

    @Override
    public IBinder onBind(Intent intent, Void v) {
        return null;
    }

    @Override
    public void onServiceKilled(Intent rootIntent) {
        System.out.println("保存数据到磁盘。");
        LogUtil.d("dfy","保存数据到磁盘。");
    }


    private void sendData()
    {

        if(!TcpConnectUtil.p_bLinkCenterON)
        {
            LogUtil.d("dfy","FUCK U");
            MyToast.showTextToast(getApplicationContext(),getResources().getString(R.string.badnetwork));
            return;
        }
        int tempvalue  = Util.getRandomValue(20,30);
        int tempvalue2  = Util.getRandomValue(20,80);
        LogUtil.d("dfy","温度 = "+tempvalue);
        LogUtil.d("dfy","湿度 = "+tempvalue2);
        if(thisMainBiz!=null)
            thisMainBiz.sendData(DEVICEIDTYPE,DEVICEIDTD,ALARMSTATE,AROUNDDEVICE,String.valueOf(tempvalue),String.valueOf(tempvalue2));
    }

}
