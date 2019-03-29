package org.andrei.ppreader.data;

import java.util.ArrayList;

public class PPReaderDataManager implements IPPReaderDataManager {
    @Override
    public int load() {
        return 0;
    }

    @Override
    public void save() {

    }

    @Override
    public void addNovel(PPReaderNovel novel) {

    }

    @Override
    public PPReaderNovel getNovel(int index) {
        return m_novels.get(index);
    }

    @Override
    public int getNovelCount() {
        return m_novels.size();
    }

    @Override
    public void removeNovel(String id) {
        for (PPReaderNovel novel: m_novels) {
            if(id.compareTo(novel.id) == 0){
                m_novels.remove(novel);
                break;
            }
        }
    }

    protected ArrayList<PPReaderNovel> m_novels = new ArrayList<>();
}
