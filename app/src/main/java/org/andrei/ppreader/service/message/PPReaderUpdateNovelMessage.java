package org.andrei.ppreader.service.message;

import org.andrei.ppreader.data.PPReaderChapter;

import java.util.ArrayList;

public class PPReaderUpdateNovelMessage implements IPPReaderMessage {

    public PPReaderUpdateNovelMessage(int retCode,String id, ArrayList<PPReaderChapter> delta){
        m_retCode = retCode;
        m_id = id;
        m_delta = delta;
    }

    @Override
    public String type() {
        return PPReaderMessageTypeDefine.TYPE_UPDATE_NOVEL;
    }

    public String getId(){
        return m_id;
    }

    public ArrayList<PPReaderChapter> getDelta(){
        return m_delta;
    }

    public int getRetCode(){
        return m_retCode;
    }

    public int getNovelType(){
        return m_novelType;
    }

    public String  m_id;
    public ArrayList<PPReaderChapter> m_delta ;
    public int m_novelType = 0;
    public int m_retCode = 0;
}
