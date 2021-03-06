package org.andrei.ppreader.service.command;

import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngine;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.message.PPReaderUpdateNovelMessage;
import org.andrei.ppreader.service.task.IPPReaderTask;
import org.andrei.ppreader.service.task.PPReaderSearchNovelsTask;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderSearchNovelsMessage;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class PPReaderSearchNovelsCommand implements IPPReaderServiceCommand {

    public PPReaderSearchNovelsCommand(IPPReaderNovelEngineManager manager, IPPReaderHttp http){
        m_engineManager = manager;
        m_http = http;
    }

    @Override
    public IPPReaderMessage run(IPPReaderTask task) {
        PPReaderSearchNovelsTask t = (PPReaderSearchNovelsTask)task;
        IPPReaderNovelEngine engine = m_engineManager.get(t.engineName);
        if(engine == null)
        {
            return new PPReaderSearchNovelsMessage(ServiceError.ERR_NOT_ENGINE,null);
        }

        ArrayList<PPReaderNovel> novels = new ArrayList<>();
        int retCode = ServiceError.ERR_NOT_NETWORK;
        Document doc = m_http.get(t.url);
        if(doc != null){
            retCode= engine.searchNovels(doc,novels);
            for(PPReaderNovel n: novels){
                n.engineName = t.engineName;
            }
        }
        PPReaderSearchNovelsMessage ret = new PPReaderSearchNovelsMessage(retCode,novels);
        return ret;
    }

    private IPPReaderNovelEngineManager m_engineManager;
    private IPPReaderHttp m_http;
}
