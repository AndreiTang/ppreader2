package org.andrei.ppreader.ui.fragment.helper;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.andrei.ppreader.service.IPPReaderNotification;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderNotifyManager;
import org.andrei.ppreader.service.PPReaderTaskType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

public class PPReaderBaseFragment extends Fragment implements IPPReaderNotification {


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        collectInteresting();
    }

    @Override
    public void onNotify(IPPReaderTaskRet ret) {
        Class<?> cl = this.getClass();
        for(Method m : cl.getDeclaredMethods()){
            PPReaderTaskType ct = m.getAnnotation(PPReaderTaskType.class);
            if(ct != null && ct.type().compareTo(ret.type()) == 0){
                try {
                    m.invoke(this,ret);
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



    protected void sendNotification(IPPReaderTaskRet ret){
        PPReaderNotifyManager.instance().sendNotification(ret);
    }

    private void collectInteresting(){
        Class<?> cl = this.getClass();
        for(Method m : cl.getDeclaredMethods()){
            PPReaderTaskType ct = m.getAnnotation(PPReaderTaskType.class);
            if(ct != null){
                m_methods.add(ct.type());
            }
        }
    }

    private HashSet<String> m_methods = new HashSet<>();

}
