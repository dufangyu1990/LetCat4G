package com.example.dufangyu.letcat4g.utils;

import android.os.Environment;


public class Constant {
	 public static final int TOKENTIME=60;
	 public static final String DATAPATHROOT= Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";
	 public static final String TXT_OF_SET = "/set.txt";//

	//socket连接的服务器地
	public final static String TCPSERVERIP = "115.28.75.240";//219.236.247.110(测试服务器)//123.57.217.93(主网)//192.168.1.187
	public final static  int TCPSERVERPORT = 7222;


	public static final String DEVICEIDTYPE="1001";
	public static final String DEVICEIDTD="WG12345678901234";

	public final static  String APKURL = "http://123.57.217.93:8080/Android/NewGpsTest.apk";

	public static final int REQ_TIMEOUT = 35000;
	public static final int TCPNONET = 100;//app一进来就没网，100
	public static final int TCPDISLINK = 101;//中途突然没网 101
	public static final int TCPLINK = 102;//连接服务器成功 102


}