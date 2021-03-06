package com.example.dufangyu.letcat4g.present;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.example.dufangyu.letcat4g.CallBack.NetCallBackImp;
import com.example.dufangyu.letcat4g.helper.GenericHelper;
import com.example.dufangyu.letcat4g.socketUtils.TcpConnectUtil;
import com.example.dufangyu.letcat4g.utils.ActivityControl;
import com.example.dufangyu.letcat4g.utils.LogUtil;
import com.example.dufangyu.letcat4g.utils.MyToast;
import com.example.dufangyu.letcat4g.view.IView;

import static com.example.dufangyu.letcat4g.utils.Constant.TCPDISLINK;
import static com.example.dufangyu.letcat4g.utils.Constant.TCPLINK;
import static com.example.dufangyu.letcat4g.utils.Constant.TCPNONET;


/**
 * Created by dufangyu on 2017/6/13.
 */

public class FragmentActivityPresentImpl<T extends IView>extends FragmentActivity implements IPresent<T> {

    protected T mView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeViewCreate(savedInstanceState);
        ActivityControl.addActivity(this);
        try {
            mView = getViewClass().newInstance();
            mView.bindPresenter(this);
            setContentView(mView.createView(getLayoutInflater(),null));
            mView.bindEvent();
            TcpConnectUtil.getTcpInstance().setNetCallBack(netCallBackImp);
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
    protected void onDestroy() {
        super.onDestroy();
        ActivityControl.removeActivity(this);
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
                    MyToast.showTextToast(getApplicationContext(),"网络异常");
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

    }

    public void doNetDisConnect(){

    }

    public void doNetConnect(){

    }
}
