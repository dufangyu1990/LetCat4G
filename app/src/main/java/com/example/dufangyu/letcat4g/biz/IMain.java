package com.example.dufangyu.letcat4g.biz;

/**
 * Created by dufangyu on 2017/9/5.
 */

public interface IMain {

    void sendLoginCommad(String deviceType, String deviceId);
    void sendData(String deviceType, String deviceId,String alarmState,String deviceType1,String vale1,String value2);

    //对巡检指令进行回应
    void sendDeviceData(String deviceId,String lightState,String lockState,String doorState,String batteryState);
}
