package org.andrei.ppreader.service.command;

import org.andrei.ppreader.service.IPPReaderServiceCommand;
import org.andrei.ppreader.service.IPPReaderTask;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderFetchTextRet;
import org.andrei.ppreader.service.PPReaderFetchTextTask;
import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;

public class PPReaderFetchTextCommand implements IPPReaderServiceCommand {

    public PPReaderFetchTextCommand(IPPReaderNovelEngineManager manager, IPPReaderHttp http){
        m_manager = manager;
        m_http = http;
    }

    @Override
    public IPPReaderTaskRet run(IPPReaderTask task) {
        PPReaderFetchTextTask t = (PPReaderFetchTextTask)task;
        PPReaderFetchTextRet ret = new PPReaderFetchTextRet();
        StringBuilder text = new StringBuilder();
        ret.retCode = m_manager.get(t.engineName).fetchChapterText(t.url, m_http, text);
        ret.chapterId = t.chapterId;
        ret.novelId = t.novelId;
        ret.text = text.toString();
        return ret;
    }

    private IPPReaderNovelEngineManager m_manager;
    private IPPReaderHttp m_http;
}
