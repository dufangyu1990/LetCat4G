package com.example.dufangyu.letcat4g.present;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;

import com.example.dufangyu.letcat4g.CallBack.NetCallBackImp;
import com.example.dufangyu.letcat4g.helper.GenericHelper;
import com.example.dufangyu.letcat4g.socketUtils.TcpConnectUtil;
import com.example.dufangyu.letcat4g.utils.ActivityControl;
import com.example.dufangyu.letcat4g.utils.LogUtil;
import com.example.dufangyu.letcat4g.view.IView;

import static com.example.dufangyu.letcat4g.utils.Constant.TCPDISLINK;
import static com.example.dufangyu.letcat4g.utils.Constant.TCPLINK;
import static com.example.dufangyu.letcat4g.utils.Constant.TCPNONET;


/**
 * Created by dufangyu on 2017/6/13.
 */

public  class ActivityPresentImpl<T extends IView>extends Activity implements IPresent<T> {

    protected T mView;



    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        beforeViewCreate(savedInstanceState);
        ActivityControl.addActivity(this);
        try {
            mView = getViewClass().newInstance();
            setContentView(mView.createView(getLayoutInflater(),null));
            mView.bindPresenter(this);
            mView.bindEvent();
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
            TcpConnectUtil.getTcpInstance().setNetCallBack(netCallBackImp);
            TcpConnectUtil.getTcpInstance().startThreads();
            afterViewCreate(savedInstanceState);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<T> getViewClass() {
        return GenericHelper.getViewClass(getClass());
    }

    @Override
    public void beforeViewCreate(Bundle savedInstanceState) {

    }

    @Override
    public void afterViewCreate(Bundle savedInstanceState) {

    }

    @Override
    public void presentCallBack(String param1, String param2, String params3) {

    }





    @Override
    protected void onResume() {
        super.onResume();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityControl.removeActivity(this);
        //销毁后结束请求
        LogUtil.d("dfy", getClass().getSimpleName() + "  onDestroy");
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            pressAgainExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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


    public void pressAgainExit(){
        finish();
    }

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
