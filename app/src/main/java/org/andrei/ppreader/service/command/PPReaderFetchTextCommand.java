package org.andrei.ppreader.service.command;

import org.andrei.ppreader.service.task.IPPReaderTask;
import org.andrei.ppreader.service.task.PPReaderFetchTextTask;
import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderFetchTextMessage;

public class PPReaderFetchTextCommand implements IPPReaderServiceCommand {

    public PPReaderFetchTextCommand(IPPReaderNovelEngineManager manager, IPPReaderHttp http){
        m_manager = manager;
        m_http = http;
    }

    @Override
    public IPPReaderMessage run(IPPReaderTask task) {
        PPReaderFetchTextTask t = (PPReaderFetchTextTask)task;
        StringBuilder text = new StringBuilder();
        int retCode = m_manager.get(t.engineName).fetchChapterText(t.url, m_http, text);
        PPReaderFetchTextMessage ret = new PPReaderFetchTextMessage(retCode,t.novelId,t.chapterId,text.toString());
        return ret;
    }

    private IPPReaderNovelEngineManager m_manager;
    private IPPReaderHttp m_http;
}
