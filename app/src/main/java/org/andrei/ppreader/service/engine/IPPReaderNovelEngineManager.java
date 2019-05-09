package org.andrei.ppreader.service.engine;

import org.andrei.ppreader.service.engine.IPPReaderNovelEngine;

public interface IPPReaderNovelEngineManager {
    public IPPReaderNovelEngine get(final String name);
    public IPPReaderNovelEngine get(int index);
    public int count();
}
