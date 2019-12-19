package org.andrei.ppreader.test;

import org.andrei.ppreader.data.PPReaderDataManager;

public class MockDataManager extends PPReaderDataManager {

    public MockDataManager(){




        //m_novels.add(novel);

    }

    @Override
    public int load(final String folder) {
        return 0;
    }


}
