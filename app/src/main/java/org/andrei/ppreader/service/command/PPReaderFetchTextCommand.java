package org.andrei.ppreader.service.command;

import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngine;
import org.andrei.ppreader.service.task.IPPReaderTask;
import org.andrei.ppreader.service.task.PPReaderFetchTextTask;
import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderFetchTextMessage;
import org.jsoup.nodes.Document;

public class PPReaderFetchTextCommand implements IPPReaderServiceCommand {

    public PPReaderFetchTextCommand(IPPReaderNovelEngineManager manager, IPPReaderHttp http){
        m_manager = manager;
        m_http = http;
    }

    @Override
    public IPPReaderMessage run(IPPReaderTask task) {
        PPReaderFetchTextTask t = (PPReaderFetchTextTask)task;
        StringBuilder text = new StringBuilder();
        IPPReaderNovelEngine engine = m_manager.get(t.engineName);
        if(engine == null){
            return  new PPReaderFetchTextMessage(ServiceError.ERR_NOT_FOUND,null,null,null);
        }

        String url = engine.getContentUrl() + t.url;
        int retCode = ServiceError.ERR_NOT_NETWORK;
        Document doc = m_http.get(url);
        if(doc != null){
            retCode = m_manager.get(t.engineName).fetchChapterText(doc, text);
        }
        PPReaderFetchTextMessage ret = new PPReaderFetchTextMessage(retCode,t.novelId,t.chapterId,text.toString());
        return ret;
    }

    private IPPReaderNovelEngineManager m_manager;
    private IPPReaderHttp m_http;
}
