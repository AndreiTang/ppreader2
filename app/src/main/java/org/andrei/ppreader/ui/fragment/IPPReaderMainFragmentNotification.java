package org.andrei.ppreader.ui.fragment;
import org.andrei.ppreader.data.PPReaderNovel;

public interface IPPReaderMainFragmentNotification {
    void onOpenNovel(PPReaderNovel novel);
    void onSwitchPage(int index);
}
