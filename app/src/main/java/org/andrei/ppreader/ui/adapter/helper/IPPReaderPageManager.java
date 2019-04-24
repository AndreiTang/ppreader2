package org.andrei.ppreader.ui.adapter.helper;

import android.text.Layout;
import android.widget.TextView;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;

public interface IPPReaderPageManager {
    public int getCount();

    public PPReaderTextPage getItem(int index);

    public int getIndex(final PPReaderTextPage page);

    public void injectText(int index, TextView tv);

    public void updateText(final String chapterId, final String text);

    public void load(final PPReaderNovel novel);

    public void addItem(final PPReaderChapter chapter);

    public int getChapterFirstPageIndex(final String chapterId);

}
