package org.andrei.ppreader.service.command;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngine;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.task.IPPReaderTask;
import org.andrei.ppreader.service.task.PPReaderUpdateNovelTask;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.engine.PPReaderNovelType;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderUpdateNovelMessage;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class PPReaderUpdateNovelCommand implements IPPReaderServiceCommand {


    public PPReaderUpdateNovelCommand(IPPReaderNovelEngineManager manager, IPPReaderHttp http){
        m_manager = manager;
        m_http = http;
    }

    @Override
    public IPPReaderMessage run(IPPReaderTask task) {
        String engineName = ((PPReaderUpdateNovelTask)task).engineName;
        String id = ((PPReaderUpdateNovelTask)task).id;
        IPPReaderNovelEngine engine = m_manager.get(engineName);
        if(engine == null)
        {
            return new PPReaderUpdateNovelMessage(ServiceError.ERR_NOT_ENGINE,null,null);
        }

        String url = engine.getContentUrl() + ((PPReaderUpdateNovelTask)task).url;
        int retCode = ServiceError.ERR_NOT_NETWORK;
        ArrayList<PPReaderChapter> delta = new ArrayList<>();
        Document doc = m_http.get(url);
        if(doc != null){
            retCode = engine.updateNovel(doc,delta);
        }


        PPReaderUpdateNovelMessage ret = new PPReaderUpdateNovelMessage(retCode,id,delta);
        return ret;
    }


    private IPPReaderNovelEngineManager m_manager;
    private IPPReaderHttp m_http;
}
