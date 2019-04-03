package org.andrei.ppreader.service;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;

import java.util.ArrayList;

public interface IPPReaderNovelEngine {
    int searchUrls(String name, IPPReaderHttp http, ArrayList<String> novels);
    int searchNovels(String url, IPPReaderHttp http, ArrayList<PPReaderNovel> novels);
    int update(final PPReaderNovel novel, final IPPReaderHttp http, ArrayList<PPReaderChapter> delta, Integer type);
}
