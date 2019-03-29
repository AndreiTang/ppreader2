package org.andrei.ppreader.ui.adapter.helper;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.IPPReaderTaskNotification;

public class PPReaderTextFragmentViewManager {

    public PPReaderTextFragmentViewManager(@NonNull PagerAdapter adapter){
        m_adapter = adapter;
    }

    public void addListener(@NonNull IPPReaderTaskNotification notification){
        m_notify = notification;
    }

    public PPReaderTextFragmentViews addView(final View view, final PPReaderTextPage page, int pos){
        return null;
    }

    public void removeView(final View view){

    }

    public PPReaderTextFragmentViews getItem(int index){
        return null;
    }

    public int getCount(){
        return 0;
    }

    public void updateView(final PPReaderTextPage page){

    }

    private PagerAdapter m_adapter;
    private IPPReaderTaskNotification m_notify;

}
