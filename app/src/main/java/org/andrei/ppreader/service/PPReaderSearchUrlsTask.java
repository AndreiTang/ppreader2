package org.andrei.ppreader.service;

import org.andrei.ppreader.util.TaskNames;

public class PPReaderSearchUrlsTask implements IPPReaderTask {

    public PPReaderSearchUrlsTask(final String name){
       this.name = name;
    }

    @Override
    public String type() {
        return TaskNames.SEARCH_URLS;
    }

    public String name;


}
