package org.andrei.ppreader.service.engine;

import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class PPReaderHttp implements IPPReaderHttp {
    @Override
    public Document get(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(60000).get();
        } catch (IOException e1) {
            return null;
        }
        return doc;
    }
}
