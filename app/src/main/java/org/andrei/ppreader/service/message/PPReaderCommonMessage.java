package org.andrei.ppreader.service.message;

public class PPReaderCommonMessage implements IPPReaderMessage {

    public PPReaderCommonMessage(final String type,int val){
        m_type = type;
        m_val = val;
    }

    @Override
    public String type() {
        return m_type;
    }

    public int getValue(){
        return m_val;
    }


    private String m_type;
    private int m_val;
}
