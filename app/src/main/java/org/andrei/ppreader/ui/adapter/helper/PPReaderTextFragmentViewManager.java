package org.andrei.ppreader.ui.adapter.helper;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewTreeObserver;

import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.ui.fragment.helper.PPReaderAllocateTextRet;

import java.util.ArrayList;

public class PPReaderTextFragmentViewManager {

    public PPReaderTextFragmentViewManager(IPPReaderTaskNotification notification){
        m_notify = notification;
    }

    public PPReaderTextFragmentViews addView(final View view, final PPReaderTextPage page, final int pos){

        final PPReaderTextFragmentViews vs = new PPReaderTextFragmentViews();
        if(page.status == PPReaderTextPage.STATUS_TEXT_NO_SLICE){

            final StringBuilder text = new StringBuilder();
            text.append("J\n");
            //using dummy title to occupy title place which is just one line.
            // If the real title is length than the width of textview. it will occupy more than 1 line which will cause error.
            text.append("This is dummy\n");
            text.append("J\n");
            text.append(page.text);
            vs.textView.setText(text);
            vs.textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    vs.textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    PPReaderAllocateTextRet ret = new PPReaderAllocateTextRet();
                }
            });
        }
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

    public void updateView(int index,final PPReaderTextPage page){

    }

    private IPPReaderTaskNotification m_notify;
    private ArrayList<PPReaderTextFragmentViews> m_views = new ArrayList<>();

}
