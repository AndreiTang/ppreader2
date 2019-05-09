package org.andrei.ppreader.service.engine;

import org.jsoup.nodes.Document;

public interface IPPReaderHttp {
    Document get(String url);
}
