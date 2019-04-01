package org.andrei.ppreader.service;

import org.andrei.ppreader.data.IPPReaderDataManager;

public interface IPPReaderTask {
    public IPPReaderTaskRet run(final IPPReaderNovelEngineManager manager,final IPPReaderHttp http);
}
