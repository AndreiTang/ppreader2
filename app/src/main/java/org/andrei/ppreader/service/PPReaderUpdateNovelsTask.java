package org.andrei.ppreader.service;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;

import java.util.ArrayList;

public class PPReaderUpdateNovelsTask implements IPPReaderTask {

    public PPReaderUpdateNovelsTask(PPReaderNovel novel){
        m_novel = novel;
    }

    @Override
    public IPPReaderTaskRet run(IPPReaderNovelEngineManager manager,final IPPReaderHttp http) {
        IPPReaderNovelEngine engine = manager.get(m_novel.engineName);
        PPReaderUpdateNovelRet ret = new PPReaderUpdateNovelRet();
        ret.novel = m_novel.id;
        if(engine == null){
            ret.retCode = ServiceError.ERR_NOT_ENGINE;
        }
        ArrayList<PPReaderChapter> delta = new ArrayList<>();
        ret.retCode = engine.update(m_novel,http,delta);
        ret.delta = delta;
        return ret;
    }

    private PPReaderNovel m_novel;
}
