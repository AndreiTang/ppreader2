package org.andrei.ppreader.service.command;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngine;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.IPPReaderServiceCommand;
import org.andrei.ppreader.service.IPPReaderTask;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderUpdateNovelRet;
import org.andrei.ppreader.service.PPReaderUpdateNovelTask;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.engine.PPReaderNovelType;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderUpdateNovelMessage;

import java.util.ArrayList;

public class PPReaderUpdateNovelCommand implements IPPReaderServiceCommand {


    public PPReaderUpdateNovelCommand(IPPReaderNovelEngineManager manager, IPPReaderHttp http){
        m_manager = manager;
        m_http = http;
    }

    @Override
    public IPPReaderMessage run(IPPReaderTask task) {

        String url = ((PPReaderUpdateNovelTask)task).url;
        String engineName = ((PPReaderUpdateNovelTask)task).engineName;
        String id = ((PPReaderUpdateNovelTask)task).id;
        IPPReaderNovelEngine engine = m_manager.get(engineName);

        int retCode = ServiceError.ERR_OK;
        ArrayList<PPReaderChapter> delta = null;
        PPReaderNovelType type = new PPReaderNovelType();
        if(engine == null){
            retCode = ServiceError.ERR_NOT_ENGINE;
        }
        else{
            delta = new ArrayList<>();
            retCode = engine.updateNovel(url,m_http,delta,type);
        }
        PPReaderUpdateNovelMessage ret = new PPReaderUpdateNovelMessage(retCode,id,delta,type.val);
        return ret;
    }


    private IPPReaderNovelEngineManager m_manager;
    private IPPReaderHttp m_http;
}
