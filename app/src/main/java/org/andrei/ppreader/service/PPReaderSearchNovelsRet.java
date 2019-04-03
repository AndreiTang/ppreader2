package org.andrei.ppreader.service;

import org.andrei.ppreader.data.PPReaderNovel;

import java.util.ArrayList;

public class PPReaderSearchNovelsRet implements IPPReaderTaskRet {
    @Override
    public String type() {
        return this.getClass().getName();
    }

    @Override
    public int getRetCode() {
        return 0;
    }

    public ArrayList<PPReaderNovel> m_novels;
}
