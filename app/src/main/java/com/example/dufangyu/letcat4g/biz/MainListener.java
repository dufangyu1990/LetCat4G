package com.example.dufangyu.letcat4g.biz;

/**
 * Created by dufangyu on 2017/9/5.
 */

public interface MainListener {
    void loginSuccess();
    void loginFailed();
    void openLight(String type);
    void getCheckOrder(String deviceId);
}
