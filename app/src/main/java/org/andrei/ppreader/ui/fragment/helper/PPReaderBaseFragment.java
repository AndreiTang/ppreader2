package org.andrei.ppreader.ui.fragment.helper;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.andrei.ppreader.service.IPPReaderNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.IPPReaderMessageHandler;
import org.andrei.ppreader.service.message.PPReaderMessageCenter;
import org.andrei.ppreader.service.message.PPReaderMessageType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

public class PPReaderBaseFragment extends Fragment implements IPPReaderMessageHandler {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        collectInteresting();
    }

    @Override
    public void onResume(){
        super.onResume();
        PPReaderMessageCenter.instance().register(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        PPReaderMessageCenter.instance().Unregister(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            PPReaderMessageCenter.instance().Unregister(this);
        }
        else{
            PPReaderMessageCenter.instance().register(this);
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
        PPReaderMessageCenter.instance().sendMessage(msg);
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

    private HashSet<String> m_methods = new HashSet<>();

}
