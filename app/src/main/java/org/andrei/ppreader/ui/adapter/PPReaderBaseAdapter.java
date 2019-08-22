package org.andrei.ppreader.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.IPPReaderMessageCenter;

public class PPReaderBaseAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public static void setDataManager(IPPReaderDataManager dataManager){
        m_dataManager = dataManager;
    }

    public static void setMessageCenter(IPPReaderMessageCenter msgCenter){
        m_msgCenter = msgCenter;
    }

    public void sendMessage(IPPReaderMessage message){
        if(m_msgCenter != null){
            m_msgCenter.sendMessage(message);
        }
    }

    protected static IPPReaderDataManager m_dataManager = null;
    private static IPPReaderMessageCenter m_msgCenter = null;

}
