package org.andrei.ppreader.data;

import java.util.ArrayList;

public interface IPPReaderDataManager {
    public int load();
    public void save();
    public void addNovel(final PPReaderNovel novel);
    public PPReaderNovel getNovel(int index);
    public PPReaderNovel getNovel(String id);
    public int getNovelCount();
    public void removeNovel(String id);


}
