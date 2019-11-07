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
                    String href = getContentUrl() + item.attr("href");
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
            chapter.id = c.attr("href");;
            delta.add(chapter);
        }
        return ServiceError.ERR_OK;
    }

    @Override
    public int fetchNovelDetail(Document doc, PPReaderNovel novel) {

        try{
            Element root = doc.getElementsByClass("jieshao").get(0);
            Elements items = root.getElementsByClass("lf");
            novel.img = items.get(0).getElementsByTag("img").get(0).attr("src");
            if(novel.img.indexOf(getImageUrl()) ==1){
                novel.img = "";
            }
            else{
                novel.img = novel.img.substring(getImageUrl().length());
            }

            items = root.getElementsByClass("rt");
            novel.name = items.get(0).getElementsByTag("h1").get(0).text();

            items = root.getElementsByClass("msg");
            novel.author = items.get(0).getElementsByTag("em").get(0).text().trim();
            novel.author = novel.author.substring(3);

            items = root.getElementsByClass("intro");
            novel.desc = items.get(0).text().trim();

        }
        catch (Exception ex){
            return ServiceError.ERR_NOT_FOUND;
        }

        return ServiceError.ERR_OK;
    }

    @Override
    public int fetchChapterText(Document doc, StringBuilder ret){
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
        return "https://www.88dushu.com";
    }

    @Override
    public String getImageUrl() {
        return "https://fm.88dushu.com";
    }

    @Override
    public String getSearchUrl() {
        return "https://www.88dushu.com/modules/article/search.php?searchtype=articlename&searchkey=";
    }

    @Override
    public EncodeType getEncodeType() {
        return EncodeType.GB2312;
    }

}
