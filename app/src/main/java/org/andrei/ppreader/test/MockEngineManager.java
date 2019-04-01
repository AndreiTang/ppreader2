package org.andrei.ppreader.test;

import org.andrei.ppreader.service.IPPReaderNovelEngine;
import org.andrei.ppreader.service.IPPReaderNovelEngineManager;

public class MockEngineManager implements IPPReaderNovelEngineManager {
    @Override
    public IPPReaderNovelEngine get(String name) {
        return new MockEngine();
    }

    @Override
    public IPPReaderNovelEngine get(int index) {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }
}
