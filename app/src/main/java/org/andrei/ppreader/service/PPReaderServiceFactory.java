package org.andrei.ppreader.service;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.command.PPReaderCommandManager;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;

public class PPReaderServiceFactory implements IPPReaderServiceFactory {

    public PPReaderServiceFactory(IPPReaderNovelEngineManager engineManager, IPPReaderDataManager dataManager){
        m_mgr = new PPReaderCommandManager(engineManager,dataManager);
    }

    @Override
    public IPPReaderService createServiceInstance() {
        return PPReaderService.createInstance(m_mgr);
    }

    private PPReaderCommandManager m_mgr ;
}
