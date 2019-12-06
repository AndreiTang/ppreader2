package org.andrei.ppreader.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.andrei.ppreader.R;

import java.util.zip.Inflater;

public class PPReaderMainTitleBar extends LinearLayout {

    

    public PPReaderMainTitleBar(Context context) {
        super(context);
        init(context);
    }

    public PPReaderMainTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PPReaderMainTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public PPReaderMainTitleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_main_item_title_bar,this,true);
    }


    public void setLeftButtonTitle(String title){
        TextView tx = findViewById(R.id.main_item_left_btn);
        tx.setText(title);
    }

    public void setRightButtonTitle(String title){
        TextView tx = findViewById(R.id.main_item_right_btn);
        tx.setText(title);
    }

    public void setTitle(String title){
        TextView tx = findViewById(R.id.main_item_title);
        tx.setText(title);
    }




}
