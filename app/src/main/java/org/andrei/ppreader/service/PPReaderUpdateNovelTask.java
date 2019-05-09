package org.andrei.ppreader.service;

import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.util.TaskNames;

public class PPReaderUpdateNovelTask implements IPPReaderTask {

    public PPReaderUpdateNovelTask(PPReaderNovel novel) {
        this.url = novel.chapterUrl;
        this.engineName = novel.engineName;
        this.id = novel.id;
    }

    @Override
    public String type() {
        return TaskNames.UPDATE_NOVEL;
    }

    public String url;
    public String engineName;
    public String id;

}
