package org.andrei.ppreader.service;

import java.util.ArrayList;

public class PPReaderSearchUrlsRet implements IPPReaderTaskRet {
    @Override
    public String type() {
        return PPReaderSearchUrlsRet.class.getName();
    }

    @Override
    public int getRetCode() {
        return 0;
    }

    public ArrayList<String> m_urls = new ArrayList<>();
}
