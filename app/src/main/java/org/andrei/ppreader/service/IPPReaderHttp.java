package org.andrei.ppreader.service;

import org.jsoup.nodes.Document;

public interface IPPReaderHttp {
    Document get(String url);
}
