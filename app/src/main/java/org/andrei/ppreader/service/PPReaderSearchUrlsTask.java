package org.andrei.ppreader.service;

public class PPReaderSearchUrlsTask implements IPPReaderTask {

    public PPReaderSearchUrlsTask(final String name){
       m_name = name;
    }
    @Override
    public IPPReaderTaskRet run(IPPReaderNovelEngineManager manager, IPPReaderHttp http) {
        return null;
    }

    private String m_name;
}
