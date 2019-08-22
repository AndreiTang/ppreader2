package org.andrei.ppreader.service.message;

import org.andrei.ppreader.service.IPPReaderNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;

import java.util.HashSet;

public class PPReaderMessageCenter implements IPPReaderMessageCenter {

    public static IPPReaderMessageCenter instance(){
        return m_mgr;
    }

    public void register(IPPReaderMessageHandler item){
        m_items.add(item);
    }

    public void Unregister(IPPReaderMessageHandler item){
        m_items.remove(item);
    }

    public void sendMessage(IPPReaderMessage msg){
        for(IPPReaderMessageHandler item :m_items){
            if(item.isInteresting(msg.type())){
                item.onMessageHandler(msg);
            }
        }
    }

    private HashSet<IPPReaderMessageHandler> m_items = new HashSet<>();
    private static PPReaderMessageCenter m_mgr = new PPReaderMessageCenter();
}
