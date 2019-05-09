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

import java.util.ArrayList;

public class PPReaderUpdateNovelCommand implements IPPReaderServiceCommand {


    public PPReaderUpdateNovelCommand(IPPReaderNovelEngineManager manager, IPPReaderHttp http){
        m_manager = manager;
        m_http = http;
    }

    @Override
    public IPPReaderTaskRet run(IPPReaderTask task) {

        String url = ((PPReaderUpdateNovelTask)task).url;
        String engineName = ((PPReaderUpdateNovelTask)task).engineName;
        String id = ((PPReaderUpdateNovelTask)task).id;
        IPPReaderNovelEngine engine = m_manager.get(engineName);
        PPReaderUpdateNovelRet ret = new PPReaderUpdateNovelRet();
        ret.id = id;
        if(engine == null){
            ret.retCode = ServiceError.ERR_NOT_ENGINE;
        }
        ArrayList<PPReaderChapter> delta = new ArrayList<>();
        PPReaderNovelType type = new PPReaderNovelType();
        ret.retCode = engine.updateNovel(url,m_http,delta,type);
        ret.delta = delta;
        ret.type = type.val;
        return ret;
    }


    private IPPReaderNovelEngineManager m_manager;
    private IPPReaderHttp m_http;
}
