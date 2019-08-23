package org.andrei.ppreader.service.task;

import org.andrei.ppreader.service.task.IPPReaderTask;

public class PPReaderTextTask implements IPPReaderTask {
    @Override
    public String type() {
        return this.getClass().getName();
    }

    public String novelId;
    public String chapterId;
    public String chapterUrl;
}
