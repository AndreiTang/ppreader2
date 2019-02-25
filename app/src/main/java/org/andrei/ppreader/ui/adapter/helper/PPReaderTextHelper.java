package org.andrei.ppreader.ui.adapter.helper;

import android.view.View;

public class PPReaderTextHelper {

    public PPReaderTextHelper(IPPReaderAdapterNotify notify){
        m_notify = notify;
    }

    public int getCount(){
        return 0;
    }

    public void addView(final View view, int pos){

    }

    public void removeView(final View view){

    }

    private IPPReaderAdapterNotify m_notify;
}
