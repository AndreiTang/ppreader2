package org.andrei.ppreader.service;

public class PPReaderTextRet implements IPPReaderTaskRet {

    @Override
    public String type() {
        return PPReaderTextRet.class.getName();
    }

    @Override
    public int getRetCode() {
        return 0;
    }

    public String novelId;

    public String chapterId;

    public String text;
}
