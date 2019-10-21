package org.andrei.ppreader.service.command;

import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngine;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderFetchNovelMessage;
import org.andrei.ppreader.service.message.PPReaderSearchNovelsMessage;
import org.andrei.ppreader.service.task.IPPReaderTask;
import org.andrei.ppreader.service.task.PPReaderFetchNovelTask;
import org.andrei.ppreader.service.task.PPReaderSearchNovelsTask;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class PPReaderFetchNovelCommand implements IPPReaderServiceCommand {

    public PPReaderFetchNovelCommand(IPPReaderNovelEngineManager manager, IPPReaderHttp http){
        m_engineManager = manager;
        m_http = http;
    }

    @Override
    public IPPReaderMessage run(IPPReaderTask task) {

        PPReaderFetchNovelTask t = (PPReaderFetchNovelTask)task;
        IPPReaderNovelEngine engine = m_engineManager.get(t.engineName);
        if(engine == null)
        {
            return new PPReaderFetchNovelMessage(ServiceError.ERR_NOT_ENGINE,null);
        }

        PPReaderNovel novel = new PPReaderNovel();
        int retCode = ServiceError.ERR_NOT_NETWORK;
        Document doc = m_http.get(t.url);
        if(doc != null){
            retCode= engine.fetchNovelDetail(doc,novel);
        }
        novel.detailUrl = t.url;
        PPReaderFetchNovelMessage ret = new PPReaderFetchNovelMessage(retCode,novel);


        return null;
    }

    private IPPReaderNovelEngineManager m_engineManager;
    private IPPReaderHttp m_http;
}
