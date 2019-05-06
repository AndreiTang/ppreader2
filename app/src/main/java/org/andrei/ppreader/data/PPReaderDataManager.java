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
    public PPReaderNovel getNovel(String id) {
        for(PPReaderNovel novel: m_novels){
            if(novel.id.compareTo(id) == 0){
                return novel;
            }
        }
        return null;
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

    @Override
    public PPReaderEngineInfo getEngineInfo(int index) {
        return m_infos.get(index);
    }

    @Override
    public int getEngineInfoCount(){
        if(m_infos == null){
            return -1;
        }
        return m_infos.size();
    }

    @Override
    public void setEngineInfos(ArrayList<PPReaderEngineInfo> infos) {
        m_infos = infos;
    }

    protected ArrayList<PPReaderNovel> m_novels = new ArrayList<>();
    protected ArrayList<PPReaderEngineInfo> m_infos;
}
