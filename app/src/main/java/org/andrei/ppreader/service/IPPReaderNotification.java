package org.andrei.ppreader.service;

public interface IPPReaderNotification {
    void onNotify(IPPReaderTaskRet ret);
    boolean isInteresting(String type);
}
