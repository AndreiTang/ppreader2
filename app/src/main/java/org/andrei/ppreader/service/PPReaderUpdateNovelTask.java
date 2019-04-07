package org.andrei.ppreader.service;

import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.util.TaskNames;

public class PPReaderUpdateNovelTask implements IPPReaderTask {

    public PPReaderUpdateNovelTask(PPReaderNovel novel) {
        this.novel = novel;
    }

    @Override
    public String type() {
        return TaskNames.UPDATE_NOVEL;
    }

    public PPReaderNovel novel;

}
