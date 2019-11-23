package org.andrei.ppreader;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.IPPReaderServiceFactory;

public class PPReader {

    public static void setServiceFactory(IPPReaderServiceFactory serviceFactory){
        m_serviceFactory = serviceFactory;
    }

    public static void setDataManager(IPPReaderDataManager dataManager){
        m_dataManager = dataManager;
    }

    public static IPPReaderDataManager getDataManager(){
        return m_dataManager;
    }

    public static IPPReaderServiceFactory getServiceFactory(){
        return m_serviceFactory;
    }

    private static IPPReaderServiceFactory m_serviceFactory;
    private static IPPReaderDataManager m_dataManager;
}
