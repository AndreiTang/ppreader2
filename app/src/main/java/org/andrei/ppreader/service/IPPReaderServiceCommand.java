package org.andrei.ppreader.service;

import org.andrei.ppreader.service.message.IPPReaderMessage;

public interface IPPReaderServiceCommand {
    public IPPReaderMessage run(IPPReaderTask task);
}
