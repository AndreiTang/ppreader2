package org.andrei.ppreader.ui.adapter;


import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.andrei.ppreader.ui.adapter.helper.IPPReaderAdapterViewFactory;
import org.andrei.ppreader.ui.adapter.helper.PPReaderTextHelper;

public class PPReadeTextAdapter extends PagerAdapter {

    public PPReadeTextAdapter(@NonNull  IPPReaderAdapterViewFactory viewFactory){
        m_viewFactory = viewFactory;
    }

    @Override
    public int getCount() {
        return m_help.getCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){
        final View v = m_viewFactory.createView(m_viewID);
        m_help.addView(v,position);
        return v;
    }

    @Override
    public int getItemPosition(Object object) {
        if (m_needUpdate) {
            m_needUpdate = false;
            return POSITION_NONE;
        } else {
            return super.getItemPosition(object);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        m_help.removeView((View) object);
    }

    private final static int m_viewID = 0;



    private PPReaderTextHelper m_help;
    private IPPReaderAdapterViewFactory m_viewFactory;
    private boolean m_needUpdate = false;
}
