package org.andrei.ppreader.service.command;

import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngine;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.IPPReaderServiceCommand;
import org.andrei.ppreader.service.IPPReaderTask;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderSearchNovelsRet;
import org.andrei.ppreader.service.PPReaderSearchNovelsTask;

public class PPReaderSearchNovelsCommand implements IPPReaderServiceCommand {

    public PPReaderSearchNovelsCommand(IPPReaderNovelEngineManager manager, IPPReaderHttp http){
        m_engineManager = manager;
        m_http = http;
    }

    @Override
    public IPPReaderTaskRet run(IPPReaderTask task) {
        PPReaderSearchNovelsTask t = (PPReaderSearchNovelsTask)task;
        PPReaderSearchNovelsRet ret = new PPReaderSearchNovelsRet();
        IPPReaderNovelEngine engine = m_engineManager.get(t.engineName);
        ret.m_retCode = engine.searchNovels(t.url,t.engineName,m_http,ret.novels);
        return ret;
    }

    private IPPReaderNovelEngineManager m_engineManager;
    private IPPReaderHttp m_http;
}
