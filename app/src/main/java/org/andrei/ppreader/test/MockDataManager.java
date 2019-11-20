package org.andrei.ppreader.test;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderDataManager;
import org.andrei.ppreader.data.PPReaderEngineInfo;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.engine.EngineNames;

import java.util.ArrayList;

public class MockDataManager extends PPReaderDataManager {

    public MockDataManager(){




        //m_novels.add(novel);

    }

    @Override
    public int load(final String folder) {
        return 0;
    }


}
