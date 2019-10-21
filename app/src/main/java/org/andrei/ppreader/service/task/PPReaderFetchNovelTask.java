package org.andrei.ppreader.service.task;

import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.command.CommandNames;

public class PPReaderFetchNovelTask extends PPReaderUpdateNovelTask {
    public PPReaderFetchNovelTask(PPReaderNovel novel) {
        super(novel);
        this.url = novel.detailUrl;
    }

    @Override
    public String type() {
        return CommandNames.FETCH_NOVEL;
    }
}
