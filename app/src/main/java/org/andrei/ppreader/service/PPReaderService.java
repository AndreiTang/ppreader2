package org.andrei.ppreader.service;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.ArrayList;


public class PPReaderService {
    public static PPReaderService createInstance(@NonNull IPPReaderTaskNotification notification){
        return new PPReaderService(notification);
    }


    public void addTask(@NonNull IPPReaderTask task ){
        synchronized (this){
            m_tasks.add(task);
        }
    }

    public void waitForEnd(){
        try {
            m_thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void exit(){
        m_isRunning = false;
        synchronized(this){
            m_notification = null;
            m_tasks.clear();
        }
    }

    private void  init(){
        m_handler = new Handler(Looper.getMainLooper());
        m_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                process();
            }
        });
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

            IPPReaderTaskRet ret = task.run();
            postRet(ret);
        }while (m_isRunning);
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


    private PPReaderService(IPPReaderTaskNotification notification){
        m_notification = notification;
        init();
    }

    private Thread m_thread;
    private Handler m_handler;
    private volatile IPPReaderTaskNotification m_notification;
    private volatile ArrayList<IPPReaderTask> m_tasks = new ArrayList<IPPReaderTask>();
    private volatile boolean m_isRunning = true;
}
