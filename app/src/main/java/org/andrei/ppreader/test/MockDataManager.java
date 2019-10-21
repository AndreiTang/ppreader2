package org.andrei.ppreader.test;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderDataManager;
import org.andrei.ppreader.data.PPReaderEngineInfo;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.engine.EngineNames;

import java.util.ArrayList;

public class MockDataManager extends PPReaderDataManager {

    public MockDataManager(){
        PPReaderNovel novel = new PPReaderNovel();
        novel.img = "https://fm.88dush.com/70/70997/70997s.jpg";
        novel.name = "放开那个女巫";
        novel.id = "1";
        novel.url = "https://www.88dush.com/xiaoshuo/70/70997";
        novel.chapterUrl = "https://www.88dush.com/xiaoshuo/70/70997/";
        novel.engineName = EngineNames.ENGINE_88dushu;
        //m_novels.add(novel);

    }

    @Override
    public int load(final String folder) {
        return 0;
    }


}
