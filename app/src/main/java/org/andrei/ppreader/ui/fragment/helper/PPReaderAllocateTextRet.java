package org.andrei.ppreader.ui.fragment.helper;

import android.text.Layout;
import android.widget.TextView;

import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.IPPReaderTaskRet;

public class PPReaderAllocateTextRet implements IPPReaderTaskRet {
    @Override
    public String type() {
        return getClass().getName();
    }

    @Override
    public int getRetCode() {
        return 0;
    }

    public int index;
    public TextView tv;
    public PPReaderTextPage page;
}
