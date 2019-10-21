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
    public int searchNovels(String url,String nsmr, IPPReaderHttp http, ArrayList<PPReaderNovel> novels) {
        return 0;
    }

    @Override
    public int updateNovel(String novelUrl, IPPReaderHttp http, ArrayList<PPReaderChapter> delta, PPReaderNovelType type) {
        delta.add(new PPReaderChapter());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int fetchChapterText(String url, IPPReaderHttp http, StringBuilder ret) {
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
