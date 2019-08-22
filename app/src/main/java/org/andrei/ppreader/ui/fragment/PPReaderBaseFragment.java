package org.andrei.ppreader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;


import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderServiceFactory;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.IPPReaderMessageCenter;
import org.andrei.ppreader.service.message.IPPReaderMessageHandler;
import org.andrei.ppreader.service.message.PPReaderMessageType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

public class PPReaderBaseFragment extends Fragment implements IPPReaderMessageHandler {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        collectInteresting();
        if(m_serviceFactory != null){
            m_service = m_serviceFactory.createServiceInstance();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(m_msgCenter == null){
            return;
        }
        m_msgCenter.register(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(m_msgCenter == null){
            return;
        }
        m_msgCenter.Unregister(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(m_msgCenter == null){
            return;
        }
        if(hidden){
            m_msgCenter.Unregister(this);
        }
        else{
            m_msgCenter.register(this);
        }
    }

    @Override
    public void onMessageHandler(IPPReaderMessage msg) {
        Class<?> cl = this.getClass();
        for(Method m : cl.getDeclaredMethods()){
            PPReaderMessageType ct = m.getAnnotation(PPReaderMessageType.class);
            if(ct != null && ct.type().compareTo(msg.type()) == 0){
                try {
                    m.invoke(this,msg);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean isInteresting(String type) {
        return m_methods.contains(type);
    }

    protected void sendMessage(IPPReaderMessage msg){
        if(m_msgCenter != null){
            m_msgCenter.sendMessage(msg);
        }
    }

    private void collectInteresting(){
        Class<?> cl = this.getClass();
        for(Method m : cl.getDeclaredMethods()){
            PPReaderMessageType ct = m.getAnnotation(PPReaderMessageType.class);
            if(ct != null){
                m_methods.add(ct.type());
            }
        }
    }

    public static void setMessageCenter(IPPReaderMessageCenter msgCenter){
        m_msgCenter = msgCenter;
    }

    public static void setDataManager(IPPReaderDataManager dataManager){
        m_dataManager = dataManager;
    }

    public static void setServiceFactory(IPPReaderServiceFactory factory){
        m_serviceFactory = factory;
    }

    protected IPPReaderService m_service;
    protected static IPPReaderDataManager m_dataManager = null;
    private HashSet<String> m_methods = new HashSet<>();
    private static IPPReaderMessageCenter m_msgCenter;
    private static IPPReaderServiceFactory m_serviceFactory;

}
