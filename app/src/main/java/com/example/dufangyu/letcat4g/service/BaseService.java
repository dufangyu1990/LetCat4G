package com.example.dufangyu.letcat4g.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.dufangyu.letcat4g.CallBack.NetCallBackImp;
import com.example.dufangyu.letcat4g.socketUtils.TcpConnectUtil;
import com.example.dufangyu.letcat4g.utils.LogUtil;

import static com.example.dufangyu.letcat4g.utils.Constant.TCPDISLINK;
import static com.example.dufangyu.letcat4g.utils.Constant.TCPLINK;
import static com.example.dufangyu.letcat4g.utils.Constant.TCPNONET;

/**
 * Created by dufangyu on 2018/1/21.
 */

public class BaseService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        TcpConnectUtil.getTcpInstance().setNetCallBack(netCallBackImp);
        TcpConnectUtil.getTcpInstance().startThreads();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    private NetCallBackImp netCallBackImp =new NetCallBackImp() {
        @Override
        public void runOnUI(int stateCode) {
            switch (stateCode)
            {
                case TCPNONET:
                    LogUtil.d("dfy", "无网络");
                    doNoNetWork();
                    break;
                case TCPDISLINK:
                    LogUtil.d("dfy", "与服务器连接断开");
                    doNetDisConnect();
                    break;
                case TCPLINK:
                    LogUtil.d("dfy","与服务器连接成功");
                    doNetConnect();
                    break;

            }



        }
    };
    //app连接不上服务器(断网或者服务器出问题)
    public void doNetDisConnect(){

    }

    //app连接服务器成功
    public void doNetConnect(){

    }

    //app没网
    public void doNoNetWork(){

    }

}
