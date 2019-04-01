package org.andrei.ppreader.test;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.IPPReaderHttp;
import org.andrei.ppreader.service.IPPReaderNovelEngine;

import java.util.ArrayList;

public class MockEngine implements IPPReaderNovelEngine {
    @Override
    public int search(String url, IPPReaderHttp http, ArrayList<PPReaderNovel> novels) {
        return 0;
    }

    @Override
    public int update(PPReaderNovel novel, IPPReaderHttp http, ArrayList<PPReaderChapter> delta) {
        delta.add(new PPReaderChapter());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
