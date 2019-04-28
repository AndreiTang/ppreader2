package org.andrei.ppreader.ui.fragment.helper;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import org.andrei.ppreader.R;

public class PPReaderTextBars {

    public PPReaderTextBars(View actionBar, TextView bottomBar, Activity activity){
        m_actionBar = actionBar;
        m_bottomBar = bottomBar;
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        activity.registerReceiver(m_batteryReceiver, intentFilter);

        TextClock tmView = m_actionBar.findViewById(R.id.novel_text_time);
        tmView.setFormat24Hour("HH:mm");
        tmView.setFormat12Hour("hh:mm a");
    }
    public void updateInfo(final String title, final String pageNo){
        TextView tv = m_actionBar.findViewById(R.id.novel_text_title);
        tv.setText(title);
        m_bottomBar.setText(pageNo);
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

    private BroadcastReceiver m_batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                int power = level * 100 / scale;
                int id = getBatteryId(power);
                ImageView battery =  m_actionBar.findViewById(R.id.novel_text_battery);
                battery.setImageResource(id);
            }
        }
    };

    private View m_actionBar;
    private TextView m_bottomBar;
}
