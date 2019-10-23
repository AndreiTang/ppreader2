package org.andrei.ppreader.test;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngine;
import org.andrei.ppreader.service.engine.PPReaderNovelType;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class MockEngine implements IPPReaderNovelEngine {



    @Override
    public int searchUrls(Document doc, ArrayList<String> novels) {
        return 0;
    }

    @Override
    public int searchNovels(Document doc, ArrayList<PPReaderNovel> novels) {
        return 0;
    }

    @Override
    public int updateNovel(Document doc, ArrayList<PPReaderChapter> delta) {
        return 0;
    }

    @Override
    public int fetchNovelDetail(Document doc, PPReaderNovel novel) {
        return 0;
    }



    @Override
    public int fetchChapterText(Document doc, StringBuilder ret) {
        return 0;
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getContentUrl() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public String getSearchUrl() {
        return null;
    }

    @Override
    public EncodeType getEncodeType() {
        return null;
    }
}
