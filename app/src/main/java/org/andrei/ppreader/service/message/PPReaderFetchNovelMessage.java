package org.andrei.ppreader.service.message;

import org.andrei.ppreader.data.PPReaderNovel;

public class PPReaderFetchNovelMessage implements IPPReaderMessage {

    public PPReaderFetchNovelMessage(int retCode, PPReaderNovel novel){
        this.m_retCode = retCode;
        this.m_novel = novel;
    }

    @Override
    public String type() {
        return PPReaderMessageTypeDefine.TYPE_FETCH_NOVEL;
    }

    public PPReaderNovel getNovel(){
        return m_novel;
    }

    public int getRetCode(){
        return m_retCode;
    }

    private int m_retCode;
    private PPReaderNovel m_novel;


}
