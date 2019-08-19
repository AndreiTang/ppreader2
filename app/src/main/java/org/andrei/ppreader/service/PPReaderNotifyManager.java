package org.andrei.ppreader.service;

import java.util.HashSet;

public class PPReaderNotifyManager {

    public static PPReaderNotifyManager instance(){
        return m_mgr;
    }

    public void register(IPPReaderNotification item){
        m_items.add(item);
    }

    public void sendNotification(IPPReaderTaskRet ret){
        for(IPPReaderNotification item :m_items){
            if(item.isInteresting(ret.type())){
                item.onNotify(ret);
            }
        }
    }

    private HashSet<IPPReaderNotification> m_items = new HashSet<>();
    private static PPReaderNotifyManager m_mgr = new PPReaderNotifyManager();
}
