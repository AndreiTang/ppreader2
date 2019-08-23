package org.andrei.ppreader.service.command;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.task.IPPReaderTask;
import org.andrei.ppreader.service.engine.PPReaderHttp;
import org.andrei.ppreader.service.engine.PPReaderNovelEngineManager;
import org.andrei.ppreader.service.message.IPPReaderMessage;

import java.util.HashMap;

public class PPReaderCommandManager implements IPPReaderServiceCommand {

    public PPReaderCommandManager(IPPReaderDataManager dataManager){
        PPReaderNovelEngineManager engineManager = new PPReaderNovelEngineManager();
        PPReaderHttp http = new PPReaderHttp();
        m_cmds.put(CommandNames.SEARCH_URLS,new PPReaderSearchUrlsCommand(engineManager,http,dataManager) );
        m_cmds.put(CommandNames.UPDATE_NOVEL,new PPReaderUpdateNovelCommand(engineManager,http));
        m_cmds.put(CommandNames.SEARCH_NOVELS,new PPReaderSearchNovelsCommand(engineManager,http));
        m_cmds.put(CommandNames.FETCH_TEXT,new PPReaderFetchTextCommand(engineManager,http));
    }

    @Override
    public IPPReaderMessage run(IPPReaderTask task) {
        IPPReaderServiceCommand cmd = m_cmds.get(task.type());
        return cmd.run(task);
    }

    private HashMap<String,IPPReaderServiceCommand> m_cmds = new HashMap<>();
}
