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

    public PPReaderTextAdapter(final Fragment parent,  final IPPReaderPageManager pageMgr, IPPReaderTaskNotification notification){
        m_viewMgr = new PPReaderTextFragmentViewManager(notification);
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
        PPReaderTextFragmentViews views = m_viewMgr.addView(v,page,position);
        return v;
    }

    @Override
    public int getItemPosition(Object object) {
        for(int i = 0; i < m_viewMgr.getCount(); i++){
            PPReaderTextFragmentViews vs = m_viewMgr.getItem(i);
            PPReaderTextPage page = m_pageMgr.getItem(vs.pos);
            vs.page = page;
            if(!vs.page.equals(page)){
                m_viewMgr.updateView(vs);
            }
            else if(vs.page.equals(page) && page.status != vs.status){
                m_viewMgr.updateView(vs);
            }
        }
        return super.getItemPosition(object);
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
