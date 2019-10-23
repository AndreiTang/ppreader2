package org.andrei.ppreader.data;

import java.util.ArrayList;

public interface IPPReaderDataManager {
    public int load(final String folder);
    public void save(final String folder);
    public void addNovel(final PPReaderNovel novel);
    public PPReaderNovel getNovel(int index);
    public PPReaderNovel getNovel(String id);
    public int getNovelCount();
    public void removeNovel(final String folder,final String id);
    public PPReaderEngineInfo getEngineInfo(int index);
    public PPReaderEngineInfo getEngineInfo(String engineName);
    public int getEngineInfoCount();
    public void setEngineInfos(ArrayList<PPReaderEngineInfo> infos);
}
