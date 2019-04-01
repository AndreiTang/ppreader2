package org.andrei.ppreader.test;

import android.support.annotation.NonNull;

import org.andrei.ppreader.service.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderTask;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderUpdateNovelsTask;

import java.util.Timer;
import java.util.TimerTask;

public class MockService implements IPPReaderService{
    @Override
    public void start(@NonNull IPPReaderTaskNotification notify) {
        m_notify = notify;
    }

    @Override
    public void stop() {

    }

    @Override
    public void waitForExit() {

    }

    @Override
    public void addTask(@NonNull IPPReaderTask task) {

        final PPReaderUpdateNovelsTask t = (PPReaderUpdateNovelsTask)task;
        TimerTask ts = new TimerTask() {
            @Override
            public void run() {

                IPPReaderTaskRet ret = t.run(m_mgr,null);
                m_notify.onNotify(ret);
            }
        };

        new Timer().schedule(ts,2000);

    }

    @Override
    public void clearTasks() {

    }

    @Override
    public boolean isIdle() {
        return false;
    }

    IPPReaderTaskNotification m_notify;
    IPPReaderNovelEngineManager m_mgr = new MockEngineManager();
}
