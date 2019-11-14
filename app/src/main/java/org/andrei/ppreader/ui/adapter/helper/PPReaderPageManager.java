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

            if (pageTextHeight < tv.getHeight()) {
                item.texts.add(lineText);
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
                    item.title = page.title;
                    item.chapterId = page.chapterId;
                    item.status = PPReaderTextPage.STATUS_OK;
                    item.texts.add(lineText);
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
        for (PPReaderTextPage page : m_pages) {
            if (page.chapterId.compareTo(chapterId) == 0) {
                page.text = cleanText(text);
                break;
            }
        }
    }

    @Override
    public void load(PPReaderNovel novel) {
        for (int i = 0; i < novel.chapters.size(); i++) {
            PPReaderChapter chapter = novel.chapters.get(i);
            addItem(chapter);
        }
    }

    @Override
    public void addItem(PPReaderChapter chapter) {
        PPReaderTextPage page = new PPReaderTextPage();
        page.chapterId = chapter.id;
        page.title = chapter.title;
        if (page.text.length() > 0) {
            page.text = cleanText(chapter.text);
            page.status = PPReaderTextPage.STATUS_LOADED;
        }
        page.chapterIndex = m_pages.size();
        m_pages.add(page);
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

    private ArrayList<PPReaderTextPage> m_pages = new ArrayList<>();
}
