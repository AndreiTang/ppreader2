package org.andrei.ppreader.ui.fragment.helper;

import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.ui.adapter.PPReaderTextAdapter;
import org.andrei.ppreader.ui.view.helper.PPReaderRxBinding;

import io.reactivex.functions.Consumer;

public class PPReaderText {
    public PPReaderText(final ViewPager vp, final PPReaderTextAdapter adapter,final IPPReaderTaskNotification notification){

        m_notification = notification;
        m_vp = vp;
        m_adapter = adapter;
        vp.setAdapter(m_adapter);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(m_notification == null){
                    return;
                }
                PPReaderCommonRet ret = new PPReaderCommonRet(PPReaderCommonRet.TYPE_CURR);
                ret.index = position;
                m_notification.onNotify(ret);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        PPReaderRxBinding.dbClicks(vp).subscribe(new Consumer<MotionEvent>() {
            @Override
            public void accept(MotionEvent motionEvent) throws Exception {
                PPReaderDBClicksRet ret = new PPReaderDBClicksRet();
                ret.event = motionEvent;
                m_notification.onNotify(ret);
            }
        });
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
