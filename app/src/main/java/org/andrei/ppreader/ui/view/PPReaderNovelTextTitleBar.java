package org.andrei.ppreader.ui.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import org.andrei.ppreader.R;

public class PPReaderNovelTextTitleBar extends LinearLayout {

    public PPReaderNovelTextTitleBar(Context context) {
        super(context);
        init(context);
    }

    public PPReaderNovelTextTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PPReaderNovelTextTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PPReaderNovelTextTitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setTitle(String title){
        if(m_titleView!=null){
            m_titleView.setText(title);
        }
    }

    public void registerBatteryReceiver(Activity activity){
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        activity.registerReceiver(m_batteryReceiver, intentFilter);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_ppreader_text_title_bar,this,true);
        initTimeClock();
        initBattery();
        m_titleView = findViewById(R.id.novel_text_title);
    }

    private void initTimeClock(){
        TextClock tmView = findViewById(R.id.novel_text_time);
        tmView.setFormat24Hour("HH:mm");
        tmView.setFormat12Hour("hh:mm a");
    }

    private void initBattery(){
        m_batteryView = findViewById(R.id.novel_text_battery);

        m_batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                    int level = intent.getIntExtra("level", 0);
                    int scale = intent.getIntExtra("scale", 100);
                    int power = level * 100 / scale;
                    setBattery(power);
                }
            }
        };
    }

    private void setBattery(int per){
        int id = getBatteryId(per);
        if(m_batteryView != null){
            m_batteryView.setImageResource(id);
        }
    }

    private int getBatteryId(int per) {
        if (per > 90)
            return R.drawable.battery_100_90;
        else if (per > 80)
            return R.drawable.battery_90_80;
        else if (per > 70)
            return R.drawable.battery_80_70;
        else if (per > 60)
            return R.drawable.battery_70_60;
        else if (per > 50)
            return R.drawable.battery_60_50;
        else if (per > 40)
            return R.drawable.battery_50_40;
        else if (per > 30)
            return R.drawable.battery_40_30;
        else if (per > 20)
            return R.drawable.battery_30_20;
        else if (per > 10)
            return R.drawable.battery_20_10;
        else
            return R.drawable.battery_10_0;
    }

    private ImageView m_batteryView;
    private TextView m_titleView;
    private BroadcastReceiver m_batteryReceiver = null;

}
