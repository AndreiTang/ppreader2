package org.andrei.ppreader.service.engine;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.IPPReaderHttp;
import org.andrei.ppreader.service.IPPReaderNovelEngine;
import org.andrei.ppreader.service.ServiceError;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class PPReader88dushuEngine implements IPPReaderNovelEngine {
    @Override
    public int searchUrls(String name, IPPReaderHttp http, ArrayList<String> novels) {
        String url = SEARCH_URL + "/search/so.php?search_field=0&q=" + name;
        Document doc = http.get(url);
        if(doc == null){
            return ServiceError.ERR_NOT_NETWORK;
        }

        Elements elements = doc.getElementsByClass("ops_page");
        if(elements.size() == 1){
            Element pages = elements.first();
            Elements hrefs = pages.getElementsByTag("a");
            if(hrefs.size() >0){
                for(Element item : hrefs){
                    if(item.className().compareTo("btn_page") == 0){
                        continue;
                    }
                    String href = SEARCH_URL + item.attr("href");
                    novels.add(href);
                }
            }
        }

        if(novels.size() == 0){
            return ServiceError.ERR_NOT_FOUND;
        }
        novels.add(0,url);
        return ServiceError.ERR_OK;
    }

    @Override
    public int searchNovels(String url, IPPReaderHttp http, ArrayList<PPReaderNovel> novels) {

        return 0;
    }

    @Override
    public int update(PPReaderNovel novel, IPPReaderHttp http, ArrayList<PPReaderChapter> delta, Integer type) {
        return 0;
    }

    @Override
    public String getName() {
        return EngineNames.ENGINE_88dushu;
    }

    static final String SEARCH_URL = "https://so.88dush.com";


}
