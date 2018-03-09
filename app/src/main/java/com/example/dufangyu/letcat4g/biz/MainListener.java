package com.example.dufangyu.letcat4g.biz;

/**
 * Created by dufangyu on 2017/9/5.
 */

public interface MainListener {
    void loginSuccess();
    void loginFailed();
    //控制彩灯
    void openLight(String type);
    //接收到巡检指令
    void getCheckOrder(String deviceId);
    void openLightSuccess();
    //打电话
    void callUser(String phoneNumber);
    //开关zigeB灯
    void openNdclose_ZBLight(String lightflag);
    //开关zigeB门锁
    void openNdclose_ZBDoorLock(String doorlockflag);
}
