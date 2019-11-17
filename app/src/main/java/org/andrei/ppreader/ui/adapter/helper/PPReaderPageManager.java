package org.andrei.ppreader.ui.adapter.helper;

import android.view.Gravity;
import android.widget.TextView;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PPReaderPageManager implements IPPReaderPageManager {

    public int getCount() {
        return m_pages.size();
    }

    public PPReaderTextPage getItem(int index) {
        if (index >= m_pages.size()) {
            PPReaderTextPage page = new PPReaderTextPage();
            page.status = PPReaderTextPage.STATUS_INVALID;
            return page;
        }
        return m_pages.get(index);
    }

    public int getIndex(final PPReaderTextPage page) {
        for (int i = 0; i < m_pages.size(); i++) {
            PPReaderTextPage item = m_pages.get(i);
            if (item.equals(page)) {
                return i;
            }
        }
        return -1;
    }

    public void injectText(int index, TextView tv) {
        if (index == -1 || index >= m_pages.size()) {
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
            PPReaderTextPage.TextPosition pos = null;
            if (pageTextHeight < tv.getHeight()) {
                item.texts.add(lineText);
                pos = new PPReaderTextPage.TextPosition();
                pos.begin = begin;
                pos.end = end;
                item.posArr.add(pos);
            } else {

                //the the font size of title is bigger than lines in body. So the line size in body decrease 1
                if (offset == 0) {
                    for (int t = 0; t < 4; t++) {
                        item.texts.remove(0);
                    }
                    item.gravity = Gravity.BOTTOM;
                }

                if (i != lineCount) {
                    offset++;
                    item = new PPReaderTextPage();
                    item.offset = offset;
                    item.chapterIndex= page.chapterIndex;
                    item.chapterId = page.chapterId;
                    item.status = PPReaderTextPage.STATUS_OK;
                    item.texts.add(lineText);
                    pos = new PPReaderTextPage.TextPosition();
                    pos.begin = begin;
                    pos.end = end;
                    item.posArr.add(pos);
                    m_pages.add(index + offset, item);
                    pageTextHeight = lineHeight;
                    item.gravity = Gravity.CENTER_VERTICAL;
                }
            }
        }

        //the last part will be top , if parts is just 1.
        if (item.offset != 0) {
            item.gravity = Gravity.TOP;
        }
        page.status = PPReaderTextPage.STATUS_OK;
    }

    @Override
    public void updateText(String chapterId, String text) {
        PPReaderChapter chapter = m_novel.getChapter(chapterId);
        chapter.text = text;
        int index  = getChapterFirstPageIndex(chapterId);
        if(index == -1){
            return;
        }
        PPReaderTextPage page = m_pages.get(index);
        page.status = PPReaderTextPage.STATUS_LOADED;
    }



    @Override
    public void load(PPReaderNovel novel) {
        m_novel = novel;
        m_pages = novel.textPages;
        for(PPReaderChapter chapter : m_novel.chapters){
            allocateTexts(chapter);
        }
    }

    @Override
    public void addItem(PPReaderChapter chapter) {
//        PPReaderTextPage page = new PPReaderTextPage();
//        page.chapterId = chapter.id;
//        PPReaderChapter cp = new PPReaderChapter();
//        cp.title = chapter.title;
//        cp.id = chapter.id;
//        //page.title = chapter.title;
//        if (chapter.pages.size() > 0) {
//
//        }
//
//        m_pages.add(page);
    }

    private void allocateTexts(PPReaderChapter chapter) {
        int index = getChapterFirstPageIndex(chapter.id);
        String text = cleanText(chapter.text);
        for (int i=  index ; ; i++){
            PPReaderTextPage page = m_pages.get(i);
            allocateText(page,text);
        }
    }

    private void allocateText(PPReaderTextPage page, String text){
        if(page.texts.size() >0){
            return;
        }
        for(PPReaderTextPage.TextPosition pos : page.posArr){
            String tx = text.substring(pos.begin,pos.end);
            page.texts.add(tx);
        }
    }
    @Override
    public int getChapterFirstPageIndex(String chapterId) {
        for (int i = 0; i < m_pages.size(); i++) {
            PPReaderTextPage item = m_pages.get(i);
            if (item.chapterId.compareTo(chapterId) == 0) {
                return i;
            }
        }
        return -1;
    }

    private String cleanText(String text) {
        StringBuilder sb = new StringBuilder();
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(text.getBytes());
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String line = br.readLine();
            while (line != null){
                if(line.length() > 0){
                    sb.append(line);
                    sb.append("\n");
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private ArrayList<PPReaderTextPage> m_pages;
    private PPReaderNovel m_novel;
}
