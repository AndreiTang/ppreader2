package org.andrei.ppreader.service;

import java.util.ArrayList;

public class PPReaderSearchUrlsRet implements IPPReaderTaskRet {
    @Override
    public String type() {
        return PPReaderSearchUrlsRet.class.getName();
    }

    @Override
    public int getRetCode() {
        return retCode;
    }

    public ArrayList<String> urls = new ArrayList<>();
    public String engineName;
    public int retCode;
}
