package org.andrei.ppreader.service.message;

public class PPReaderFetchTextMessage implements IPPReaderMessage {

    public PPReaderFetchTextMessage(int retCode,String novelId,String chapterId,String text){

    }

    @Override
    public String type() {
        return PPReaderMessageTypeDefine.TYPE_TEXT;
    }

    public int getRetCode(){
        return m_retCode;
    }

    public String getNovelId(){
        return m_novelId;
    }

    public String getChapterId(){
        return m_chapterId;
    }

    public String getText(){
        return m_text;
    }

    private int m_retCode;
    private String m_novelId;
    private String m_chapterId;
    private String m_text;
}
