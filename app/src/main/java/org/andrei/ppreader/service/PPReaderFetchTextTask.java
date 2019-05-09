package org.andrei.ppreader.service;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.util.TaskNames;

public class PPReaderFetchTextTask implements IPPReaderTask {

    public PPReaderFetchTextTask(PPReaderNovel novel, PPReaderChapter chapter){
        this.chapterId = chapter.id;
        this.engineName = novel.engineName;
        this.novelId = novel.id;
        this.url = chapter.url;
    }

    @Override
    public String type() {
        return TaskNames.FETCH_TEXT;
    }

    public String url;
    public String engineName;
    public String novelId;
    public String chapterId;


}
