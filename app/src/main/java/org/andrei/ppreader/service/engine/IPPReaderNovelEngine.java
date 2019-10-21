package org.andrei.ppreader.service.engine;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public interface IPPReaderNovelEngine {

    enum EncodeType{
        UTF8,
        GB2312;
    }

    //int searchUrls(String name, IPPReaderHttp http, ArrayList<String> novels);
    int searchUrls(Document doc, ArrayList<String> novels);
    int searchNovels(Document doc, ArrayList<PPReaderNovel> novels);
    int updateNovel(Document doc, ArrayList<PPReaderChapter> delta);
    int fetchNovelDetail(Document doc,PPReaderNovel novel);
    int fetchChapterText(final String url, final IPPReaderHttp http, StringBuilder ret);
    String getName();
    String getContentUrl();
    String getImageUrl();
    String getSearchUrl();
    EncodeType getEncodeType();
}
