package org.andrei.ppreader.ui.adapter.helper;

import android.text.Layout;
import android.widget.TextView;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;

import java.util.ArrayList;

public class PPReaderPageManager implements IPPReaderPageManager {

    public int getCount(){
        return m_pages.size();
    }

    public PPReaderTextPage getItem(int index){
        if(index >= m_pages.size()){
            PPReaderTextPage page = new PPReaderTextPage();
            page.status = PPReaderTextPage.STATUS_INVALID;
            return page;
        }
        return m_pages.get(index);
    }

    public int getIndex(final PPReaderTextPage page){
        for(int i = 0; i < m_pages.size(); i++){
            PPReaderTextPage item = m_pages.get(i);
            if(item.equals(page)){
                return i;
            }
        }
        return -1;
    }

    public void injectText(int index, Layout txtLayout){

    }

    @Override
    public void updateText(String chapterId, String text) {
        for(PPReaderTextPage page: m_pages){
            if(page.chapterId.compareTo(chapterId) == 0){
                page.text = text;
                break;
            }
        }
    }

    @Override
    public void load(PPReaderNovel novel) {
        for(int i = 0; i <  novel.chapters.size() ; i++){
            PPReaderChapter chapter = novel.chapters.get(i);
            addItem(chapter);
        }
    }

    @Override
    public void addItem(PPReaderChapter chapter) {
        PPReaderTextPage page = new PPReaderTextPage();
        page.text = chapter.text;
        page.chapterId = chapter.id;
        page.title = chapter.title;
        m_pages.add(page);
    }

    @Override
    public int getChapterFirstPageIndex(String chapterId) {
        for(int i = 0; i < m_pages.size(); i++){
            PPReaderTextPage item = m_pages.get(i);
            if(item.chapterId.compareTo(chapterId) == 0){
                return i;
            }
        }
        return -1;
    }

    private ArrayList<PPReaderTextPage> m_pages = new ArrayList<>();
}
