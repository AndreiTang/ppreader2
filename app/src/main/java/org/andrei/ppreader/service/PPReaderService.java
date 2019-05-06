package org.andrei.ppreader.service;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.ArrayList;


public class PPReaderService implements IPPReaderService {
    public static PPReaderService createInstance(IPPReaderServiceCommand cmd){
        return new PPReaderService(cmd);
    }

    @Override
    public void start(@NonNull IPPReaderTaskNotification notify) {
        m_notification = notify;
        init();
    }

    @Override
    public void stop() {
        m_isRunning = false;
        synchronized(this){
            m_notification = null;
            m_tasks.clear();
        }
    }

    @Override
    public void waitForExit() {
        try {
            m_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addTask(@NonNull IPPReaderTask task ){
        synchronized (this){
            m_tasks.add(task);
        }
    }


    @Override
    public void clearTasks() {
        synchronized(this){
            m_tasks.clear();
        }
    }

    @Override
    public boolean isIdle() {
        return m_tasks.size() == 0;
    }

    private void  init(){
        m_handler = new Handler(Looper.getMainLooper());
        m_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                process();
            }
        });
        m_isRunning = true;
        m_thread.start();
    }

    private void process(){
        do{
            IPPReaderTask task = null;

            synchronized(this){
                if(m_tasks.size() > 0){
                    task = m_tasks.remove(0);
                    assert (task != null);
                }
                else{
                    continue;
                }
            }

            IPPReaderTaskRet ret =  run(task);
            postRet(ret);
        }while (m_isRunning);
    }

    private IPPReaderTaskRet run(IPPReaderTask task){
        return m_cmd.run(task);
    }

    private void postRet(final IPPReaderTaskRet ret){
        m_handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    if(m_notification != null){
                        m_notification.onNotify(ret);
                    }
                }
            }
        });
    }

    private PPReaderService(IPPReaderServiceCommand cmd){
        m_cmd = cmd;
    }

    private Thread m_thread;
    private Handler m_handler;
    private volatile IPPReaderTaskNotification m_notification;
    private volatile ArrayList<IPPReaderTask> m_tasks = new ArrayList<IPPReaderTask>();
    private volatile boolean m_isRunning = true;
    private IPPReaderServiceCommand m_cmd;

}
