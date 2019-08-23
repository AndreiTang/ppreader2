package org.andrei.ppreader.service.command;

import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.task.IPPReaderTask;

public interface IPPReaderServiceCommand {
    public IPPReaderMessage run(IPPReaderTask task);
}
