package org.andrei.ppreader.service.message;

import org.andrei.ppreader.data.PPReaderNovel;

import java.util.ArrayList;

public class PPReaderSearchNovelsMessage implements IPPReaderMessage {

    public PPReaderSearchNovelsMessage(int retCode, ArrayList<PPReaderNovel> novels){
        m_novels = novels;
        m_retCode = retCode;
    }

    @Override
    public String type() {
        return PPReaderMessageTypeDefine.TYPE_SEARCH_NOVELS;
    }

    public int getRetCode(){
        return m_retCode;
    }

    public ArrayList<PPReaderNovel> getNovels(){
        return m_novels;
    }


    private ArrayList<PPReaderNovel> m_novels;
    private int m_retCode;
}
