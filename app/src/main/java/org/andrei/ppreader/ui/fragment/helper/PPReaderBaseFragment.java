package org.andrei.ppreader.ui.fragment.helper;

import android.support.v4.app.Fragment;

import org.andrei.ppreader.service.IPPReaderNotification;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderNotifyManager;

public class PPReaderBaseFragment extends Fragment implements IPPReaderNotification {

    @Override
    public void onNotify(IPPReaderTaskRet ret) {
        this.getClass().getAnnotations();
    }

    @Override
    public boolean isInteresting(String type) {
        return false;
    }

    protected  void show(){

    }

    protected void hide(){

    }

    protected void register(){
        PPReaderNotifyManager.instance().register(this);
    }

    protected void sendMessage(IPPReaderTaskRet ret){
        PPReaderNotifyManager.instance().sendNotification(ret);
    }




}
