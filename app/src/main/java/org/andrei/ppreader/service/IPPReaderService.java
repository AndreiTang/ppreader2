package org.andrei.ppreader.service;

import android.support.annotation.NonNull;

import org.andrei.ppreader.service.task.IPPReaderTask;

public interface IPPReaderService {
    void start();
    void stop();
    void waitForExit();
    void addTask(@NonNull final IPPReaderTask task );
    void clearTasks();
    boolean isIdle();
}
