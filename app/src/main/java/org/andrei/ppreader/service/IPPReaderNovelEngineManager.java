package org.andrei.ppreader.service;

public interface IPPReaderNovelEngineManager {
    public IPPReaderNovelEngine get(final String name);
    public IPPReaderNovelEngine get(int index);
    public int count();
}
