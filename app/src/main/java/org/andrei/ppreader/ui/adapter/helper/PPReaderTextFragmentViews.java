package org.andrei.ppreader.ui.adapter.helper;

import android.view.View;

import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.ui.view.PPReaderTextView;

public class PPReaderTextFragmentViews {
    public View maskView;
    public PPReaderTextView textView;
    public View errView;
    public View root;
    public int pos = -1;
    public PPReaderTextPage page = null;
    public int status = PPReaderTextPage.STATUS_INIT;
}
