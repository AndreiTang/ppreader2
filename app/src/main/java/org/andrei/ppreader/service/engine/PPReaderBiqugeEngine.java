package org.andrei.ppreader.service.engine;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.Utils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class PPReaderBiqugeEngine implements IPPReaderNovelEngine {
    @Override
    public int searchUrls(Document doc, ArrayList<String> novels) {
        Elements elements = doc.getElementsByClass("pagelink");
        if(elements.size() == 1){
            Element pages = elements.first();
            Elements hrefs = pages.getElementsByTag("a");
            if(hrefs.size() >0){
                for(Element item : hrefs){
                    String href = getContentUrl() + item.attr("href");
                    boolean isExist = Helper.containNovelUrl(href,novels);
                    if(!isExist) {
                        novels.add(href);
                    }
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
        Elements els = doc.getElementsByClass("grid");
        if (els.size() == 0) {
            return ServiceError.ERR_NOT_FOUND;
        }

        Elements elements = els.get(0).getElementsByTag("tr");
        if (elements.size() == 0) {
            return  ServiceError.ERR_NOT_FOUND;
        }

        for(Element element : elements){
            if(element.attr("align").compareTo("center") == 0){
                continue;
            }
            Element item =  element.getElementsByClass("odd").get(0).getElementsByTag("a").get(0);
            PPReaderNovel novel = new PPReaderNovel();
            novel.chapterUrl = item.attr("href").substring(getContentUrl().length());
            novel.detailUrl = novel.chapterUrl;
            novels.add(novel);
        }

        return ServiceError.ERR_OK;
    }

    @Override
    public int updateNovel(Document doc, ArrayList<PPReaderChapter> delta) {
        Elements cs = doc.getElementsByTag("dd");
        if(cs == null || cs.size() == 0){
            return ServiceError.ERR_NOT_FOUND;
        }
        for(Element c :cs){
            if(c.getElementsByTag("a").size() == 0){
               continue;
            }
            Element a = c.getElementsByTag("a").get(0);
            PPReaderChapter chapter = new PPReaderChapter();
            chapter.url = a.attr("href");
            int index = chapter.url.lastIndexOf("/");
            chapter.url = chapter.url.substring(index+1);
            chapter.title = a.text();
            chapter.id = a.attr("href");
            delta.add(chapter);
        }
        return ServiceError.ERR_OK;
    }

    @Override
    public int fetchNovelDetail(Document doc, PPReaderNovel novel) {
        try{
            novel.author = doc.select("meta[name=author]").get(0).attr("content");
            novel.name = doc.select("meta[property=og:title]").get(0).attr("content");
            novel.img = doc.select("meta[property=og:image]").get(0).attr("content").substring(getImageUrl().length());
            novel.desc = doc.select("meta[property=og:description]").get(0).attr("content").substring(getImageUrl().length());
        }
        catch (Exception ex){
            return ServiceError.ERR_NOT_FOUND;
        }
        return ServiceError.ERR_OK;
    }

    @Override
    public int fetchChapterText(Document doc, StringBuilder ret) {
        Element el = doc.getElementById("booktext");
        if(el == null){
            return ServiceError.ERR_NOT_FOUND;
        }
        String text = Utils.adjustText(el.text());
        ret.append(text);
        return ServiceError.ERR_OK;
    }

    @Override
    public String getName() {
        return EngineNames.ENGINE_biquge;
    }

    @Override
    public String getContentUrl() {
        return "https://www.biquger.com";
    }

    @Override
    public String getImageUrl() {
        return "https://www.biquger.com";
    }

    @Override
    public String getSearchUrl() {
        return "https://www.biquger.com/modules/article/search.php?searchkey=";
    }

    @Override
    public EncodeType getEncodeType() {
        return EncodeType.GB2312;
    }
}
