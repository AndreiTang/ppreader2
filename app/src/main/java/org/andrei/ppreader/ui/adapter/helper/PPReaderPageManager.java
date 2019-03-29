package org.andrei.ppreader.ui.adapter.helper;

import android.text.Layout;
import android.widget.TextView;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;

public class PPReaderPageManager implements IPPReaderPageManager {

    public int getCount(){
        return 0;
    }

    public PPReaderTextPage getItem(int index){
        return null;
    }

    public int getIndex(final PPReaderTextPage page){
        return 0;
    }

    public void injectText(int index, Layout txtLayout){

    }

    @Override
    public void updateText(String chapterId, String text) {

    }

    @Override
    public void load(PPReaderNovel novel) {

    }

    @Override
    public void addItem(PPReaderChapter chapter) {

    }

    @Override
    public int getChapterFirstPageIndex(String chapterId) {
        return 0;
    }
}
