package org.andrei.ppreader.service.message;

public interface IPPReaderMessageHandler {
    void onMessageHandler(final IPPReaderMessage msg);
    boolean isInteresting(final String type);
}
