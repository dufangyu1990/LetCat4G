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

        LogUtil.d("dfy","发送数据");
        TcpConnectUtil.getTcpInstance().IntiTemp();
        TcpConnectUtil.getTcpInstance().ClintSendBcCommData (2105, "0002", deviceType, deviceId, "", alarmState, "", "", "", "", deviceType1, value1,value2 , "", "", "", "", "", "");

    }





    private DataCallBackImp serverCallBack = new DataCallBackImp()
    {

        @Override
        public void onReceiveServerResult(int intDataType, String strDataType, String strSetSN, String strSetSN1, String strAlmComType, String strParam1, String strParam2, String strParam3) {

            if(intDataType==2105)
            {
                if(strDataType.equals("1001"))
                {
                    if(strParam1.equals("1"))
                    {
                        listener.loginSuccess();
                    }
                }
            }
        }
    };

}
