package org.andrei.ppreader.test;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngine;
import org.andrei.ppreader.service.engine.PPReaderNovelType;

import java.util.ArrayList;

public class MockEngine implements IPPReaderNovelEngine {

    @Override
    public int searchUrls(String name, IPPReaderHttp http, ArrayList<String> novels) {
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
}
