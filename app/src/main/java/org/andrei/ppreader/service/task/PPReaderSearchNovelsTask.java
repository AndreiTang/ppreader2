package org.andrei.ppreader.service.task;

import org.andrei.ppreader.service.command.CommandNames;

public class PPReaderSearchNovelsTask implements IPPReaderTask {

    public PPReaderSearchNovelsTask(String url, String engineName){
        this.url = url;
        this.engineName = engineName;
    }

    @Override
    public String type() {
        return CommandNames.SEARCH_NOVELS;
    }

    public String url;
    public String engineName;
}
