package org.andrei.ppreader.service.engine;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;

import java.util.ArrayList;

public interface IPPReaderNovelEngine {
    int searchUrls(String name, IPPReaderHttp http, ArrayList<String> novels);
    int searchNovels(String url, String engineName ,IPPReaderHttp http, ArrayList<PPReaderNovel> novels);
    int updateNovel(final String novelUrl, final IPPReaderHttp http, ArrayList<PPReaderChapter> delta, PPReaderNovelType type);
    int fetchChapterText(final String url, final IPPReaderHttp http, StringBuilder ret);
    String getName();
}
