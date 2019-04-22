package org.andrei.ppreader.ui.fragment.helper;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.ui.adapter.PPReaderTextAdapter;

public class PPReaderText {
    public PPReaderText(final ViewPager vp, final PPReaderTextAdapter adapter){
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(m_notification == null){
                    return;
                }
                PPReaderTextRet ret = new PPReaderTextRet(PPReaderTextRet.TYPE_CURR);
                ret.index = position;
                m_notification.onNotify(ret);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        m_vp = vp;
        m_adapter = adapter;
        vp.setAdapter(m_adapter);


    }

    public void addListener(IPPReaderTaskNotification notification){
        m_notification = notification;
    }

    public void notifyDataSetChanged(){
        m_adapter.notifyDataSetChanged();
    }

    public void setCurrentItem(int index){
        m_vp.setCurrentItem(index);
        if(index == 0){
            m_adapter.notifyDataSetChanged();
        }
    }

    public int getCurrentIndex(){
        return m_vp.getCurrentItem();
    }



    private IPPReaderTaskNotification m_notification;
    private PPReaderTextAdapter m_adapter;
    private ViewPager m_vp;


}
