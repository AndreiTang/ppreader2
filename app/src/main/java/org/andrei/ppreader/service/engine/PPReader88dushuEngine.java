package org.andrei.ppreader.service.engine;

import android.app.Service;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.Utils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

public class PPReader88dushuEngine implements IPPReaderNovelEngine {
    @Override
    public int searchUrls(Document doc, ArrayList<String> novels) {

        Elements elements = doc.getElementsByClass("pagelink");
        if(elements.size() == 1){
            Element pages = elements.first();
            Elements hrefs = pages.getElementsByTag("a");
            if(hrefs.size() >0){
                for(Element item : hrefs){
                    if(item.className().compareTo("first") == 0){
                        continue;
                    }
                    else if(item.className().compareTo("next") == 0){
                        break;
                    }
                    String href = "https://www.88dush.com" + item.attr("href");
                    novels.add(href);
                }
            }
        }

        if(novels.size() == 0){
            return ServiceError.ERR_NOT_FOUND;
        }
        return ServiceError.ERR_OK;
    }

    @Override
    public int searchNovels(Document doc, ArrayList<PPReaderNovel> novels) {

        Elements els = doc.getElementsByClass("booklist");
        if (els.size() == 0) {
            return ServiceError.ERR_NOT_FOUND;
        }

        Elements elements = els.get(0).getElementsByTag("li");
        if (elements.size() == 0) {
            return  ServiceError.ERR_NOT_FOUND;
        }

        for(Element element : elements){
            if(element.className().compareTo("t") == 0){
                continue;
            }
            PPReaderNovel novel = new PPReaderNovel();
            Element item = element.getElementsByTag("a").get(0);
            novel.chapterUrl = item.attr("href");
            novel.detailUrl = novel.chapterUrl;
            novels.add(novel);
        }

        return ServiceError.ERR_OK;
    }

    @Override
    public int updateNovel(Document doc, ArrayList<PPReaderChapter> delta) {


        Elements mulus = doc.getElementsByClass("mulu");
        if(mulus == null || mulus.size() == 0){
            return ServiceError.ERR_NOT_FOUND;
        }
        Element root = mulus.get(0);
        Elements cs = root.getElementsByTag("a");
        for(Element c :cs){
            PPReaderChapter chapter = new PPReaderChapter();
            chapter.url = c.attr("href");
            chapter.title = c.text();
            chapter.id = UUID.randomUUID().toString();
            delta.add(chapter);
        }
        return ServiceError.ERR_OK;
    }

    @Override
    public int fetchNovelDetail(Document doc, PPReaderNovel novel) {
        Elements items = doc.getElementsByClass("lf");
        if(items == null || items.size() == 0){
            return ServiceError.ERR_NOT_FOUND;
        }
        novel.img = items.get(0).getElementsByTag("img").get(0).attr("src");
        if(novel.img.indexOf(getImageUrl()) ==1){
            novel.img = "";
        }
        else{
            novel.img = novel.img.substring(getImageUrl().length());
        }
        items = doc.getElementsByAttribute("rt");
        if(items == null || items.size() == 0){
            return ServiceError.ERR_NOT_FOUND;
        }
        novel.name = items.get(0).getElementsByTag("h1").get(0).text();

        return 0;
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

    @Override
    public String getContentUrl() {
        return "https://www.88dush.com";
    }

    @Override
    public String getImageUrl() {
        return "https://fm.88dush.com";
    }

    @Override
    public String getSearchUrl() {
        return "https://www.88dush.com/modules/article/search.php?searchtype=articlename&searchkey=";
    }

    @Override
    public EncodeType getEncodeType() {
        return EncodeType.GB2312;
    }

}
