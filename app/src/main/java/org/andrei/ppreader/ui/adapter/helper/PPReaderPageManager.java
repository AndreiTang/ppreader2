package org.andrei.ppreader.ui.adapter.helper;

import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import org.andrei.ppreader.PPReader;
import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.task.PPReaderFetchTextTask;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

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

    public String getPageText(final PPReaderTextPage page){
        PPReaderChapter chapter = m_novel.chapters.get(page.chapterIndex);
        if(chapter == null){
            return "";
        }
        return cleanText(chapter.text);
    }

    public String getPageTitle(final PPReaderTextPage page){
        PPReaderChapter chapter = m_novel.chapters.get(page.chapterIndex);
        if(chapter == null){
            return "";
        }
        return chapter.title;
    }

    @Override
    public int getCurrentIndex() {
        int index = -1;
        if(m_novel.currIndex + m_novel.currOffset == 0){
            //setCurrent is unavailable if the index is 0,so directly set detail
            index = 0;
        }
        else{
            PPReaderChapter chapter = m_novel.chapters.get(m_novel.currIndex);
            index = getChapterFirstPageIndex(chapter.id);
            if(index > -1){
                index += m_novel.currOffset;
            }
        }
        return index;
    }

    @Override
    public void setCurrentIndex(int pos){
        PPReaderTextPage page = m_pages.get(pos);
        if(page == null){
            return;
        }
        m_novel.currIndex = page.chapterIndex;
        m_novel.currOffset = page.offset;
        m_novel.needValidate = true;
        if(page.status == PPReaderTextPage.STATUS_LOADED){
            page.status = PPReaderTextPage.STATUS_TEXT_NO_SLICE;
        }
        else if(page.status == PPReaderTextPage.STATUS_INIT){
            page.status = PPReaderTextPage.STATUS_LOADING;
        }
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
                     while(item.texts.size() != 0){
                         String str = item.texts.remove(0);
                         item.posArr.remove(0);
                         if(str.indexOf("##end##") != -1){
                             break;
                         }
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
    public void updateText(String chapterId,boolean isSuccessful ,String text) {

        int index  = getChapterFirstPageIndex(chapterId);
        PPReaderTextPage page = m_pages.get(index);
        if(index == -1){
            return;
        }
        if(isSuccessful){
            PPReaderChapter chapter = m_novel.getChapter(chapterId);
            chapter.text = text;
            updateTextSuccess(page);
        }
        else{
            page.status = PPReaderTextPage.STATUS_FAIL;
        }

    }

    private void updateTextSuccess(PPReaderTextPage page){
        if(page.chapterIndex == m_novel.currIndex){
            page.status = PPReaderTextPage.STATUS_TEXT_NO_SLICE;
        }
        else{
            page.status = PPReaderTextPage.STATUS_LOADED;
        }
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
        if(index == -1){
            return;
        }

        //it mean this text isn't downloaded.
        if(chapter.text.length() == 0){
            m_pages.get(index).status = PPReaderTextPage.STATUS_INIT;
            return;
        }
        else if(chapter.text.length() > 0 && m_pages.get(index).posArr.size() == 0){
            //it mean text is fetched, but it isn't allocated.
            m_pages.get(index).status = PPReaderTextPage.STATUS_LOADED;
            return;
        }

        StringBuilder text = new StringBuilder();
        text.append("\n");
        //using dummy title to occupy title place which is just one line.
        // If the real title is length than the width of textview. it will occupy more than 1 line which will cause error.
        text.append(chapter.title);
        text.append("\n");
        text.append("##end##\n");
        text.append(cleanText(chapter.text));


        for (int i=  index ; i< m_pages.size() ; i++){
            PPReaderTextPage page = m_pages.get(i);
            if(page.chapterId.compareTo(chapter.id) != 0){
                break;
            }
            allocateText(page,text.toString());
        }
    }

    private void allocateText(PPReaderTextPage page, String text){
        //has been allocated
        if(page.texts.size() > 0){
            page.status = PPReaderTextPage.STATUS_OK;
            return;
        }
        for(PPReaderTextPage.TextPosition pos : page.posArr){
            String tx = text.substring(pos.begin,pos.end);
            if(pos.end == text.length() -1 ){

            }
            page.texts.add(tx);
        }
        page.status = PPReaderTextPage.STATUS_OK;
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
        String tx = sb.toString();
        char end = tx.charAt(tx.length() - 1);
        if(end != '\n'){
            tx += "\n";
        }
        return tx;
    }

    private ArrayList<PPReaderTextPage> m_pages = new ArrayList<>();
    private PPReaderNovel m_novel;
}
