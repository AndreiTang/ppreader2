package org.andrei.ppreader.service;

public class PPReaderTextTask implements IPPReaderTask{
    @Override
    public String type() {
        return this.getClass().getName();
    }

    public String novelId;
    public String chapterId;
    public String chapterUrl;
}
