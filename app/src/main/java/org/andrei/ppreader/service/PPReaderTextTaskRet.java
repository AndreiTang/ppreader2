package org.andrei.ppreader.service;

public class PPReaderTextTaskRet implements IPPReaderTaskRet {

    @Override
    public String type() {
        return PPReaderTextTaskRet.class.getName();
    }

    @Override
    public int getRetCode() {
        return 0;
    }

    public String novelId;

    public String chapterId;

    public String text;
}
