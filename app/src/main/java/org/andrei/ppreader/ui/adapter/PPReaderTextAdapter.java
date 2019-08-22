package org.andrei.ppreader.ui.adapter;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.ui.adapter.helper.IPPReaderPageManager;
import org.andrei.ppreader.ui.adapter.helper.PPReaderTextFragmentViewManager;
import org.andrei.ppreader.ui.adapter.helper.PPReaderTextFragmentViews;

public class PPReaderTextAdapter extends PagerAdapter {

    public PPReaderTextAdapter(final Fragment parent,  final IPPReaderPageManager pageMgr){
        m_viewMgr = new PPReaderTextFragmentViewManager();
        m_pageMgr = pageMgr;
        m_parent = parent;
    }

    @Override
    public int getCount() {
        return m_pageMgr.getCount();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){
        final View v = m_parent.getLayoutInflater().inflate(R.layout.view_ppreader_text,null);
        container.addView(v);
        PPReaderTextPage page = m_pageMgr.getItem(position);
        m_viewMgr.addView(v,page,position);
        m_viewMgr.updateAllViews(m_pageMgr);
        return v;
    }

    @Override
    public int getItemPosition(Object object) {
        m_viewMgr.updateView(object,m_pageMgr);
        return PagerAdapter.POSITION_UNCHANGED;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        m_viewMgr.removeView((View) object);
    }

    private PPReaderTextFragmentViewManager m_viewMgr;
    private IPPReaderPageManager m_pageMgr;
    private Fragment m_parent;
}
