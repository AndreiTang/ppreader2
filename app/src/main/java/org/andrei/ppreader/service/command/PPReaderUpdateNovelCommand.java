package org.andrei.ppreader.service.command;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.IPPReaderHttp;
import org.andrei.ppreader.service.IPPReaderNovelEngine;
import org.andrei.ppreader.service.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.IPPReaderServiceCommand;
import org.andrei.ppreader.service.IPPReaderTask;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderUpdateNovelRet;
import org.andrei.ppreader.service.PPReaderUpdateNovelTask;
import org.andrei.ppreader.service.ServiceError;

import java.util.ArrayList;

public class PPReaderUpdateNovelCommand implements IPPReaderServiceCommand {


    public PPReaderUpdateNovelCommand(IPPReaderNovelEngineManager manager, IPPReaderHttp http){
        m_manager = manager;
        m_http = http;
    }

    @Override
    public IPPReaderTaskRet run(IPPReaderTask task) {

        PPReaderNovel novel = ((PPReaderUpdateNovelTask)task).novel;
        IPPReaderNovelEngine engine = m_manager.get(novel.engineName);
        PPReaderUpdateNovelRet ret = new PPReaderUpdateNovelRet();
        ret.novel = novel;
        if(engine == null){
            ret.retCode = ServiceError.ERR_NOT_ENGINE;
        }
        ArrayList<PPReaderChapter> delta = new ArrayList<>();
        Integer type = new Integer(0);
        ret.retCode = engine.update(novel,m_http,delta,type);
        ret.delta = delta;
        ret.type = type;
        return ret;
    }


    private IPPReaderNovelEngineManager m_manager;
    private IPPReaderHttp m_http;
}
