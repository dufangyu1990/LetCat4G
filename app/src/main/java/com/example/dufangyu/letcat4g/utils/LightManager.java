package com.example.dufangyu.letcat4g.utils;

import android.os.Handler;
import android.text.TextUtils;

import com.example.dufangyu.letcat4g.activity.MyApplication;
import com.example.dufangyu.letcat4g.biz.MainListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by dufangyu on 2018/1/28.
 */

public class LightManager {
    private  boolean gpiostatus=true;
    public  OutputStream Power_out = null;//by zhu
    private File file;//当前文件对象
    private File formalFile;//上次文件对象

    private MainListener listener;
    private Handler myHandler = new Handler();
    private static class  SingleHolder{
        public static LightManager INSTANCE = new LightManager();
    }


    public static LightManager getInstance() {
        return SingleHolder.INSTANCE;
    }



    public void setListener(MainListener listener)
    {
        this.listener = listener;
    }

    public void openLight(String type)
    {
        byte[] power_on_data = new byte[]{'1', 0, 0};
        if (gpiostatus) {
            power_on_data = new byte[]{'1', 0, 0};//拉高电平
        } else {
            power_on_data = new byte[]{'0', 0, 0};//拉低
        }
        gpiostatus = !gpiostatus;
        LedPowerControl(power_on_data,type);
    }


    public  void LedPowerControl(byte[] data,String type) {
        OpenPowerControl(data,type);
        try {
            if (Power_out != null) {
                Power_out.write(data);
                Power_out.flush();
                if(!gpiostatus || type.equals("0"))
                {
                    MyApplication.getInstance().setStringPerference("lightState",type);
                    if(listener!=null)
                    {
                        listener.openLightSuccess();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ///获取驱动设备节点
    public  void OpenPowerControl(byte[] data,final String type) {
        ClosePowerControl();
        if(formalFile!=null)
        {
            try {
                Power_out = new BufferedOutputStream(new FileOutputStream(formalFile));
                if (Power_out != null) {
                    Power_out.write(data);
                    Power_out.flush();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            formalFile = null;
            if(!type.equals("0"))
            {
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openLight(type);
                    }
                },5);
            }


        }else{
            if(!TextUtils.isEmpty(type))
            {
                if(!type.equals("0"))
                {
                    if(type.equals("1"))
                    {
                        file= new File("/sys/devices/aptt/driver/red");
                    }else if(type.equals("2"))
                    {
                        file= new File( "/sys/devices/aptt/driver/green");
                    }else if(type.equals("3"))
                    {
                        file= new File( "/sys/devices/aptt/driver/blue");
                    }
                }
                formalFile = file;
            }

            if(file!=null)
            {
                if (file.exists()) {
                    try {
                        Power_out = new BufferedOutputStream(new FileOutputStream(file));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }





    }
    ///关闭驱动设备节点
    public   void ClosePowerControl() {
        if (Power_out != null) {
            try {
                Power_out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Power_out = null;
        }
    }

}
