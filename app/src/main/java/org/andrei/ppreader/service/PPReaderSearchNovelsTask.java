package org.andrei.ppreader.service;

public class PPReaderSearchNovelsTask implements IPPReaderTask {

    public PPReaderSearchNovelsTask(final String url,final String engineName){
        m_url = url;
        m_engineName = engineName;
    }

    @Override
    public IPPReaderTaskRet run(IPPReaderNovelEngineManager manager, IPPReaderHttp http) {
        return null;
    }

    private String m_url;
    private String m_engineName;
}
