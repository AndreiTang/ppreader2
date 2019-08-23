package org.andrei.ppreader.service.task;

import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.command.CommandNames;

public class PPReaderFetchTextTask implements IPPReaderTask {

    public PPReaderFetchTextTask(PPReaderNovel novel, PPReaderChapter chapter){
        this.chapterId = chapter.id;
        this.engineName = novel.engineName;
        this.novelId = novel.id;
        this.url = chapter.url;
    }

    @Override
    public String type() {
        return CommandNames.FETCH_TEXT;
    }

    public String url;
    public String engineName;
    public String novelId;
    public String chapterId;


}
