package org.andrei.ppreader.service;

public class PPReaderFetchTextRet implements IPPReaderTaskRet {
    @Override
    public String type() {
        return this.getClass().getName();
    }

    @Override
    public int getRetCode() {
        return retCode;
    }

    public int retCode;
    public String text;
    public String novelId;
    public String chapterId;
}
