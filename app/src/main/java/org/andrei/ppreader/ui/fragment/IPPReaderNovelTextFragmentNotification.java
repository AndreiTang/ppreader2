package org.andrei.ppreader.ui.fragment;

import org.andrei.ppreader.data.PPReaderNovel;

public interface IPPReaderNovelTextFragmentNotification {
    public void onSwitchFragment(int index);
    public void onRefresh(boolean needInvalidate);
}
