package org.andrei.ppreader.ui.fragment.helper;

import android.view.MotionEvent;

import org.andrei.ppreader.service.IPPReaderTaskRet;

public class PPReaderDBClicksRet implements IPPReaderTaskRet {
    @Override
    public String type() {
        return this.getClass().getName();
    }

    @Override
    public int getRetCode() {
        return 0;
    }

    public MotionEvent event;
}
