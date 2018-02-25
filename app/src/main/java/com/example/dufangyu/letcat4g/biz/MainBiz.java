package com.example.dufangyu.letcat4g.biz;


import com.example.dufangyu.letcat4g.CallBack.DataCallBackImp;
import com.example.dufangyu.letcat4g.socketUtils.TcpConnectUtil;
import com.example.dufangyu.letcat4g.utils.LogUtil;

/**
 * Created by dufangyu on 2017/9/5.
 */

public class MainBiz implements IMain{
    private MainListener listener;


    public MainBiz(MainListener listener)
    {
        TcpConnectUtil.getTcpInstance().setDataCallBack(serverCallBack);
        this.listener = listener;
    }
    @Override
    public void sendLoginCommad(String deviceType, String deviceId) {

        TcpConnectUtil.getTcpInstance().IntiTemp();

        TcpConnectUtil.getTcpInstance().ClintSendBcCommData (2105, "0001", deviceType, deviceId, "", "", "", "", "", "", "", "","" , "", "", "", "", "", "");
    }


    @Override
    public void sendData(String deviceType, String deviceId,String alarmState, String deviceType1, String value1, String value2) {
        LogUtil.d("dfy","发送温湿度数据");
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData (2105, "0002", deviceType, deviceId, "", alarmState, "", "", "", "", deviceType1, value1,value2 , "", "", "", "", "", "");

    }

    @Override
    public void sendDeviceData(String deviceId, String lightState, String lockState, String doorState, String batteryState) {
        LogUtil.d("dfy","发送设备数据");
        LogUtil.d("dfy","deviceId = "+deviceId);
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData (2160, "1001", "101", deviceId, "", "", "", "", "", "", "", lightState, lockState , doorState, batteryState, "", "", "", "");
    }



    private DataCallBackImp serverCallBack = new DataCallBackImp()
    {

        @Override
        public void onReceiveServerResult(int intDataType, String strDataType, String strSetType, String strSetSN, String strSetSN1, String strAlmComType, String strHisType, String strPosType, String strFadeType, String strRecogType, String strRecogType1, String strParam1, String strParam2, String strParam3, String strParam4, String strParam5, String strParam6, String strParam7, String strParam8, String[] strArr) {
            if(intDataType==2105)
            {
                if(strDataType.equals("1001"))
                {
                    if(strParam1.equals("1"))
                    {
                        if(listener!=null)
                            listener.loginSuccess();
                    }
                }
            }else if(intDataType==2160)
            {
                if(strDataType.equals("0002"))//灯控指令
                {
                    if(listener!=null)
                        listener.openLight(strParam1);
                }else if(strDataType.equals("0001"))//收到巡检指令
                {
                    LogUtil.d("dfy","收到巡检指令设备号="+strSetSN);
                    if(listener!=null)
                        listener.getCheckOrder(strSetSN);
                }else if(strDataType.equals("0003"))
                {
                    LogUtil.d("dfy","电话号码="+strParam1);
                    if(listener!=null)
                        listener.callUser(strParam1);
                }
            }
        }


    };

}
