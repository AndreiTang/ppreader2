package org.andrei.ppreader.service.task;

import org.andrei.ppreader.service.command.CommandNames;

public class PPReaderSearchUrlsTask implements IPPReaderTask {

    public PPReaderSearchUrlsTask(final String name){
       this.name = name;
    }

    @Override
    public String type() {
        return CommandNames.SEARCH_URLS;
    }

    public String name;


}
