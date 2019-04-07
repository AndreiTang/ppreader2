package org.andrei.ppreader.service;

import org.andrei.ppreader.util.TaskNames;

public class PPReaderSearchNovelsTask implements IPPReaderTask {

    public PPReaderSearchNovelsTask(String url, String engineName){
        this.url = url;
        this.engineName = engineName;
    }

    @Override
    public String type() {
        return TaskNames.SEARCH_NOVELS;
    }

    public String url;
    public String engineName;
}
