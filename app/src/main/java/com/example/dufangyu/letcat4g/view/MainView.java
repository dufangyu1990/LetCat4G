package com.example.dufangyu.letcat4g.view;

import android.widget.Button;

import com.example.dufangyu.letcat4g.R;


/**
 * Created by dufangyu on 2017/8/31.
 */

public class MainView extends ViewImpl{


    private Button button;
    @Override
    public void initView() {
        button = findViewById(R.id.button);
    }








    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void bindEvent() {

    }
}
