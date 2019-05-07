package org.andrei.ppreader.service;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.command.PPReaderCommandManager;

public class PPReaderServiceFactory implements IPPReaderServiceFactory {

    public PPReaderServiceFactory(IPPReaderDataManager dataManager){
        m_mgr = new PPReaderCommandManager(dataManager);
    }

    @Override
    public IPPReaderService createServiceInstance() {
        return PPReaderService.createInstance(m_mgr);
    }

    private PPReaderCommandManager m_mgr ;
}
