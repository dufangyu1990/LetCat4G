package com.example.dufangyu.letcat4g.service;

import android.content.Intent;

import com.example.dufangyu.letcat4g.biz.IMain;
import com.example.dufangyu.letcat4g.biz.MainBiz;
import com.example.dufangyu.letcat4g.biz.MainListener;
import com.example.dufangyu.letcat4g.utils.LogUtil;
import com.example.dufangyu.letcat4g.utils.MyToast;

import static com.example.dufangyu.letcat4g.utils.Constant.DEVICEIDTD;
import static com.example.dufangyu.letcat4g.utils.Constant.DEVICEIDTYPE;

/**
 * Created by dufangyu on 2018/1/21.
 */

public class MyService extends BaseService implements MainListener {

    private IMain mainBiz;
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("dfy","MyService  onCreate");
        mainBiz = new MainBiz(this);
    }

    @Override
    public void doNetConnect() {
        super.doNetConnect();
        mainBiz.sendLoginCommad(DEVICEIDTYPE,DEVICEIDTD);
    }

    @Override
    public void doNetDisConnect() {
        super.doNetDisConnect();
        MyToast.showTextToast(getApplicationContext(),"与服务器连接断开");
    }


    @Override
    public void doNoNetWork() {
        super.doNoNetWork();
        MyToast.showTextToast(getApplicationContext(),"无网络");
    }

    @Override
    public void loginSuccess() {
        LogUtil.d("dfy","登录成功！！");
        TraceServiceImpl.sShouldStopService = false;
        TraceServiceImpl.setModelBiz(mainBiz);
        startService(new Intent(this, TraceServiceImpl.class));
    }

    @Override
    public void loginFailed() {

    }
}
