package org.andrei.ppreader.service.engine;

import java.util.ArrayList;

public class PPReaderNovelEngineManager implements IPPReaderNovelEngineManager {

    public PPReaderNovelEngineManager(){
        m_engines.add(new PPReader88dushuEngine());
    }

    @Override
    public IPPReaderNovelEngine get(String name) {
        for(IPPReaderNovelEngine engine :m_engines){
            if(engine.getName().compareTo(name) == 0){
                return engine;
            }
        }
        return null;
    }

    @Override
    public IPPReaderNovelEngine get(int index) {
        return m_engines.get(index);
    }

    @Override
    public int count() {
        return m_engines.size();
    }

    private ArrayList<IPPReaderNovelEngine> m_engines = new ArrayList<>();
}
