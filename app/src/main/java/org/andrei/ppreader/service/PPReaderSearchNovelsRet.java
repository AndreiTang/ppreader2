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
        return m_retCode;
    }

    public ArrayList<PPReaderNovel> novels = new ArrayList<>();

    public int m_retCode;
}
