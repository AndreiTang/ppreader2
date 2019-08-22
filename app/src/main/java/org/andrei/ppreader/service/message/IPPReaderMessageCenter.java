package org.andrei.ppreader.service.message;

public interface IPPReaderMessageCenter {

    void register(IPPReaderMessageHandler item);

    void Unregister(IPPReaderMessageHandler item);

    void sendMessage(IPPReaderMessage msg);
}
