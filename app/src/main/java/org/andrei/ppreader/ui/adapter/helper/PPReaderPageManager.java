package org.andrei.ppreader.ui.adapter.helper;

import android.text.Layout;
import android.widget.TextView;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.ui.fragment.helper.PPReaderText;

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

    public void injectText(int index, TextView tv){
        if(index == -1 || index >= m_pages.size()){
            return;
        }
        PPReaderTextPage page = m_pages.get(index);
        String text = tv.getText().toString();

        int lineHeight = tv.getLineHeight();
        int lineCount = tv.getLineCount();
        int pageTextHeight = 0;
        int offset = 0;

        PPReaderTextPage item = page;

        for (int i = 0; i < lineCount; i++) {
            int begin = tv.getLayout().getLineStart(i);
            int end = tv.getLayout().getLineEnd(i);
            String lineText = text.substring(begin, end);
            pageTextHeight += lineHeight;

            if (pageTextHeight < tv.getHeight()) {
                item.texts.add(lineText);
            } else {
                i--;
                //the the font size of title is bigger than lines in body. So the line size in body decrease 1
                if (offset == 0) {
                    for(int t = 0 ; t < 4 ; t++){
                        item.texts.remove(0);
                    }

                }

                if (i != lineCount - 1) {
                    offset++;
                    item = new PPReaderTextPage();
                    item.offset = offset;
                    item.status = PPReaderTextPage.STATUS_OK;
                    m_pages.add(index+offset,item);
                    pageTextHeight = 0;
                }
            }
        }
        page.status = PPReaderTextPage.STATUS_OK;
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
        if(page.text.length() > 0){
            page.status = PPReaderTextPage.STATUS_TEXT_NO_SLICE;
        }
        page.chapterIndex = m_pages.size();
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
