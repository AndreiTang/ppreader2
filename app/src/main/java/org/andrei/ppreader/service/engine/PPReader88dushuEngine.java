package org.andrei.ppreader.service.engine;

import android.app.Service;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.Utils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.UUID;

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
    public int searchNovels(String url, String engineName,IPPReaderHttp http, ArrayList<PPReaderNovel> novels) {

        Document doc = http.get(url);
        if(doc == null){
            return ServiceError.ERR_NOT_NETWORK;
        }

        Elements els = doc.getElementsByClass("ops_cover");
        if (els.size() == 0) {
            return ServiceError.ERR_NOT_FOUND;
        }

        Elements elements = els.get(0).getElementsByClass("block");
        if (elements.size() == 0) {
            return  ServiceError.ERR_NOT_FOUND;
        }

        for(Element element : elements){
            PPReaderNovel novel = new PPReaderNovel();
            Element item = element.getElementsByTag("a").get(0);
            novel.chapterUrl = item.attr("href");
            item = element.getElementsByTag("img").get(0);
            novel.img = item.attr("src");
            novel.name = item.attr("alt");
            Elements pps = element.getElementsByTag("p");
            item = pps.get(2);
            novel.author = item.text();
            novel.author = novel.author.substring(3);
            item = pps.get(4);
            novel.desc = item.text();
            novel.desc = novel.desc.trim();
            novel.desc = novel.desc.replaceAll("\n", "");
            novel.engineName = engineName;
            novel.id = UUID.randomUUID().toString();
            novels.add(novel);
        }

        return ServiceError.ERR_OK;
    }

    @Override
    public int updateNovel(String url, IPPReaderHttp http, ArrayList<PPReaderChapter> delta, PPReaderNovelType type) {

        Document doc = http.get(url);

        Elements mulus = doc.getElementsByClass("mulu");
        if(mulus == null || mulus.size() == 0){
            return ServiceError.ERR_NOT_FOUND;
        }
        Element root = mulus.get(0);
        Elements cs = root.getElementsByTag("a");
        for(Element c :cs){
            PPReaderChapter chapter = new PPReaderChapter();
            chapter.url = url + c.attr("href");
            chapter.title = c.text();
            chapter.id = UUID.randomUUID().toString();
            delta.add(chapter);
        }

        Element item = doc.getElementsByTag("em").get(1);
        if(item.text().indexOf("连载") != -1){
            type.val = PPReaderNovel.TYPE_ING;
        }
        else{
            type.val = PPReaderNovel.TYPE_OVER;
        }

        return ServiceError.ERR_OK;
    }

    @Override
    public int fetchChapterText(final String url, final IPPReaderHttp http, StringBuilder ret){
        Document doc = http.get(url);
        if(doc == null){
            return ServiceError.ERR_NOT_NETWORK;
        }

        Element item = doc.getElementsByClass("yd_text2").get(0);
        String text = item.html();
        if(text.isEmpty()){
            return ServiceError.ERR_NOT_FOUND;
        }
        text = Utils.adjustText(text);
        ret.append(text);

        return ServiceError.ERR_OK;
    }

    @Override
    public String getName() {
        return EngineNames.ENGINE_88dushu;
    }

    static final String SEARCH_URL = "https://so.88dush.com";


}
