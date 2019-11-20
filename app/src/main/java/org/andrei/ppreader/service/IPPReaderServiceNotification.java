package org.andrei.ppreader.service;

import org.andrei.ppreader.service.message.IPPReaderMessage;

public interface IPPReaderServiceNotification {
    public void onNotify(IPPReaderMessage msg);
}
