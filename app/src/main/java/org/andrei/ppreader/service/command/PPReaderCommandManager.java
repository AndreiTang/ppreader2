package org.andrei.ppreader.service.command;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.IPPReaderServiceCommand;
import org.andrei.ppreader.service.IPPReaderTask;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderHttp;
import org.andrei.ppreader.service.PPReaderNovelEngineManager;
import org.andrei.ppreader.util.TaskNames;

import java.util.HashMap;

public class PPReaderCommandManager implements IPPReaderServiceCommand {

    public PPReaderCommandManager(IPPReaderDataManager dataManager){
        PPReaderNovelEngineManager engineManager = new PPReaderNovelEngineManager();
        PPReaderHttp http = new PPReaderHttp();
        m_cmds.put(TaskNames.SEARCH_URLS,new PPReaderSearchUrlsCommand(engineManager,http,dataManager) );
        m_cmds.put(TaskNames.UPDATE_NOVEL,new PPReaderUpdateNovelCommand(engineManager,http));


    }

    @Override
    public IPPReaderTaskRet run(IPPReaderTask task) {
        IPPReaderServiceCommand cmd = m_cmds.get(task.type());
        return cmd.run(task);
    }

    private HashMap<String,IPPReaderServiceCommand> m_cmds = new HashMap<>();
}
