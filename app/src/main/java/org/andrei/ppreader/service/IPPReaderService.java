package org.andrei.ppreader.service;

import android.support.annotation.NonNull;

public interface IPPReaderService {
    void start();
    void stop();
    void waitForExit();
    void addTask(@NonNull final IPPReaderTask task );
    void clearTasks();
    boolean isIdle();
}
