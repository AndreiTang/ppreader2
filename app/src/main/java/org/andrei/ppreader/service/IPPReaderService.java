package org.andrei.ppreader.service;

import android.support.annotation.NonNull;

public interface IPPReaderService {
    void start(@NonNull IPPReaderTaskNotification notify);
    void stop();
    void waitForExit();
    void addTask(@NonNull IPPReaderTask task );
    void addNotification(@NonNull IPPReaderTaskNotification notification);
    void clearTasks();
}
