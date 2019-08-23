package org.andrei.ppreader.service.command;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderEngineInfo;
import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngine;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.task.IPPReaderTask;
import org.andrei.ppreader.service.task.PPReaderSearchUrlsTask;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderSearchUrlsMessage;

import java.util.ArrayList;

public class PPReaderSearchUrlsCommand implements IPPReaderServiceCommand {


    public PPReaderSearchUrlsCommand(IPPReaderNovelEngineManager engineManager, IPPReaderHttp http, IPPReaderDataManager dataManager){
        m_engineManager = engineManager;
        m_dataManager = dataManager;
        m_http = http;
    }

    @Override
    public IPPReaderMessage run(IPPReaderTask task) {
        int retCode = ServiceError.ERR_NOT_ENGINE;

        PPReaderSearchUrlsTask t = (PPReaderSearchUrlsTask)task;
        String engineName = "";
        ArrayList<String> urls = new ArrayList<>();
        for(int i = 0 ; i < m_dataManager.getEngineInfoCount(); i++){
            PPReaderEngineInfo item = m_dataManager.getEngineInfo(i);
            if(item == null || item.isUsed == false){
                continue;
            }
            IPPReaderNovelEngine engine = m_engineManager.get(item.name);
            if(engine == null){
                continue;
            }
            retCode = engine.searchUrls(t.name,m_http,urls);
            if(retCode == ServiceError.ERR_OK){
                engineName = item.name;
                break;
            }

        }
        PPReaderSearchUrlsMessage ret = new PPReaderSearchUrlsMessage(retCode,engineName,urls);
        return ret;
    }

    private IPPReaderNovelEngineManager m_engineManager;
    private IPPReaderHttp m_http;
    private IPPReaderDataManager m_dataManager;
}
