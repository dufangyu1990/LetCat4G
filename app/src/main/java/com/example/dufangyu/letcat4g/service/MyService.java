package com.example.dufangyu.letcat4g.service;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.example.dufangyu.letcat4g.activity.MyApplication;
import com.example.dufangyu.letcat4g.biz.IMain;
import com.example.dufangyu.letcat4g.biz.MainBiz;
import com.example.dufangyu.letcat4g.biz.MainListener;
import com.example.dufangyu.letcat4g.utils.Constant;
import com.example.dufangyu.letcat4g.utils.LightManager;
import com.example.dufangyu.letcat4g.utils.LogUtil;
import com.example.dufangyu.letcat4g.utils.MyToast;
import com.example.dufangyu.letcat4g.utils.SerialPortUtil;
import com.example.dufangyu.letcat4g.utils.Util;

import static com.example.dufangyu.letcat4g.utils.Constant.DEVICEIDTD;
import static com.example.dufangyu.letcat4g.utils.Constant.DEVICEIDTYPE;

/**
 * Created by dufangyu on 2018/1/21.
 */

public class MyService extends BaseService implements MainListener,SerialPortUtil.OnDataReceiveListener {

    private IMain mainBiz;
    public static String mydeviceId;
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("dfy","MyService  onCreate");
        mainBiz = new MainBiz(this);
        MyApplication.getInstance().setStringPerference("lightState","0");
        MyApplication.getInstance().setStringPerference("lockState","1");
        MyApplication.getInstance().setStringPerference("doorState","1");
        MyApplication.getInstance().setStringPerference("batteryState","70,0,90");
    }

    @Override
    public void doNetConnect() {
        super.doNetConnect();
        LogUtil.d("dfy","MyService  doNetConnect");
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
        LightManager.getInstance().setListener(this);
        SerialPortUtil.getInstance().setOnDataReceiveListener(this);
        TraceServiceImpl.sShouldStopService = false;
        TraceServiceImpl.setModelBiz(mainBiz);
        startService(new Intent(this, TraceServiceImpl.class));
    }

    @Override
    public void loginFailed() {

    }

    /**
     * 开关彩灯指令
     * type “0”关灯。
     *type “1”开灯红
     *type “2”开灯绿
     *type “3”开灯蓝
     * @param type
     */
    @Override
    public void openLight(String type) {
        LightManager.getInstance().openLight(type);

    }

    @Override
    public void getCheckOrder(String deviceId) {
        String lightState=MyApplication.getInstance().getStringPerference("lightState");
        LogUtil.d("dfy","lightState = "+lightState);
        mydeviceId = deviceId;
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
        mainBiz.sendDeviceData(mydeviceId,lightState,lockState,doorState,batteryState);
    }


    //收到监听指令，打电话给用户
    @Override
    public void callUser(String phoneNumber) {

        // 使用系统的电话拨号服务，必须去声明权限，在AndroidManifest.xml中进行声明
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                + phoneNumber));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /**
     * 开关ZigeBe灯
     * @param lightflag
     */
    @Override
    public void openNdclose_ZBLight(String lightflag) {

        LogUtil.d("dfy","接收到开灯指令"+lightflag);
        if(!TextUtils.isEmpty(lightflag))
        {
            if(lightflag.equals("1"))//开灯
            {
                SerialPortUtil.getInstance().sendCmds(Constant.OPENZBLIGHTCMD);
            }else if(lightflag.equals("0"))//关灯
            {
                SerialPortUtil.getInstance().sendCmds(Constant.CLOSEZBLIGHTCMD);
            }
        }

    }


    /**
     * 开关zb门锁
     * @param doorlockflag
     */
    @Override
    public void openNdclose_ZBDoorLock(String doorlockflag) {

        LogUtil.d("dfy","接收到开门指令"+doorlockflag);
        if(!TextUtils.isEmpty(doorlockflag))
        {
            if(doorlockflag.equals("1"))//开锁
            {
                byte key[] = {0x46,0x45,0x49,0x42,0x49,0x47};
                byte[] tailBuffer = new byte[6];
                byte pass[] = {0x08,0x08,0x08,0x08,0x08,0x08};
                for(int i =0;i<6;i++)
                {
                    tailBuffer[i] = (byte)(key[i]^pass[i]);
                }
                byte[] mBuffer= Util.hexStringToBytes(Constant.OPENZBDOORCMD);
                byte[] mBufferTemp = new byte[mBuffer.length+tailBuffer.length];
                System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
                System.arraycopy(tailBuffer, 0, mBufferTemp, mBuffer.length, tailBuffer.length);

                for(int i =0;i<mBufferTemp.length;i++)
                {
                    int data = mBufferTemp[i]&0xFF;
                    LogUtil.d("dfy","mBufferTemp["+i+"] = "+Integer.toHexString(data));
                }
                SerialPortUtil.getInstance().sendBuffer(mBufferTemp);
            }else if(doorlockflag.equals("0"))//上锁
            {
            }
        }
    }

    @Override
    public void onDataReceive(byte[] buffer, int size) {
        LogUtil.d("dfy","接收数据线程:"+Thread.currentThread().getName());
    }
}
