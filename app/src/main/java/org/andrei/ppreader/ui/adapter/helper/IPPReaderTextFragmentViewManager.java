package org.andrei.ppreader.ui.adapter.helper;

import android.view.View;

public interface IPPReaderTextFragmentViewManager {
    void addView(final PPReaderTextFragmentViews views,final int pos);
    void removeView(final View rootView);
    //void addNotify(final IPPReaderTaskRet notify);
    PPReaderTextFragmentViews getViewsByPosition(final int pos);

}
