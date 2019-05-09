package org.andrei.ppreader.service.command;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderEngineInfo;
import org.andrei.ppreader.service.engine.IPPReaderHttp;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngine;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.IPPReaderServiceCommand;
import org.andrei.ppreader.service.IPPReaderTask;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderSearchUrlsRet;
import org.andrei.ppreader.service.PPReaderSearchUrlsTask;
import org.andrei.ppreader.service.ServiceError;

public class PPReaderSearchUrlsCommand implements IPPReaderServiceCommand {


    public PPReaderSearchUrlsCommand(IPPReaderNovelEngineManager engineManager, IPPReaderHttp http, IPPReaderDataManager dataManager){
        m_engineManager = engineManager;
        m_dataManager = dataManager;
        m_http = http;
    }

    @Override
    public IPPReaderTaskRet run(IPPReaderTask task) {
        PPReaderSearchUrlsRet ret = new PPReaderSearchUrlsRet();
        ret.retCode = ServiceError.ERR_NOT_ENGINE;

        PPReaderSearchUrlsTask t = (PPReaderSearchUrlsTask)task;

        for(int i = 0 ; i < m_dataManager.getEngineInfoCount(); i++){
            PPReaderEngineInfo item = m_dataManager.getEngineInfo(i);
            if(item == null || item.isUsed == false){
                continue;
            }
            IPPReaderNovelEngine engine = m_engineManager.get(item.name);
            if(engine == null){
                continue;
            }
            ret.retCode = engine.searchUrls(t.name,m_http,ret.urls);
            if(ret.retCode == ServiceError.ERR_OK){
                ret.engineName = item.name;
                break;
            }

        }
        return ret;
    }

    private IPPReaderNovelEngineManager m_engineManager;
    private IPPReaderHttp m_http;
    private IPPReaderDataManager m_dataManager;
}
