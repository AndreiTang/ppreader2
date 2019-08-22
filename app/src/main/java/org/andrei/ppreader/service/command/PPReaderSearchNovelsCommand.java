package org.andrei.ppreader.service.command;

import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngine;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.IPPReaderServiceCommand;
import org.andrei.ppreader.service.IPPReaderTask;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderSearchNovelsRet;
import org.andrei.ppreader.service.PPReaderSearchNovelsTask;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderSearchNovelsMessage;

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
        ArrayList<PPReaderNovel> novels = new ArrayList<>();
        int retCode= engine.searchNovels(t.url,t.engineName,m_http,novels);
        PPReaderSearchNovelsMessage ret = new PPReaderSearchNovelsMessage(retCode,novels);
        return ret;
    }

    private IPPReaderNovelEngineManager m_engineManager;
    private IPPReaderHttp m_http;
}
