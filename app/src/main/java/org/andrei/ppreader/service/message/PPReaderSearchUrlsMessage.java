package org.andrei.ppreader.service.message;

import java.util.ArrayList;

public class PPReaderSearchUrlsMessage implements IPPReaderMessage {

    public PPReaderSearchUrlsMessage(int retCode,String engineName, ArrayList<String> urls){
        m_retCode = retCode;
        m_engineName = engineName;
        m_urls = urls;
    }

    @Override
    public String type() {
        return PPReaderMessageTypeDefine.TYPE_SEARCH_URLS;
    }

    public int getRetCode(){
        return m_retCode;
    }

    public String getEngineName(){
        return m_engineName;
    }

    public ArrayList<String> getUrls(){
        return m_urls;
    }

    private ArrayList<String> m_urls;
    private int m_retCode;
    private String m_engineName;
}
