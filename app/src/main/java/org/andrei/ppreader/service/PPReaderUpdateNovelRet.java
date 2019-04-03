package org.andrei.ppreader.service;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;

import java.util.ArrayList;

public class PPReaderUpdateNovelRet implements IPPReaderTaskRet {
    @Override
    public String type() {
        return this.getClass().getName();
    }

    @Override
    public int getRetCode() {
        return retCode;
    }



    public PPReaderNovel novel;
    public ArrayList<PPReaderChapter> delta ;
    public int type = 0;
    public int retCode = 0;
}
