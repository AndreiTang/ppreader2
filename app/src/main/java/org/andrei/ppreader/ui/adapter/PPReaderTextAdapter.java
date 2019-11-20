package org.andrei.ppreader.ui.adapter;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.ui.adapter.helper.IPPReaderPageManager;
import org.andrei.ppreader.ui.adapter.helper.PPReaderTextFragmentViews;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class PPReaderTextAdapter extends PagerAdapter {

    public interface IPPReaderTextAdapterNotify{
        void sendFetchTextRequest(PPReaderTextPage page);
    }

    public PPReaderTextAdapter(final Fragment parent, final IPPReaderPageManager pageMgr, final IPPReaderTextAdapterNotify notify){
        m_pageMgr = pageMgr;
        m_parent = parent;
        m_notify = notify;
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
        addView(v,page,position);
        updateAllViews(m_pageMgr);
        return v;
    }

    @Override
    public int getItemPosition(Object object) {
        updateView(object,m_pageMgr);
        return PagerAdapter.POSITION_UNCHANGED;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        removeView((View) object);
    }

    private void addView(final View view, final PPReaderTextPage page, final int pos) {

        final PPReaderTextFragmentViews vs = new PPReaderTextFragmentViews();
        vs.root = view;
        vs.pos = pos;
        vs.page = page;
        vs.status = page.status;
        vs.textView = view.findViewById(R.id.ppreader_text_text);
        vs.maskView = view.findViewById(R.id.ppreader_text_loading);
        vs.errView = view.findViewById(R.id.ppreader_text_err);
        RxView.clicks(vs.errView).throttleFirst(1, TimeUnit.SECONDS).subscribe(
                new Consumer<Object>() {
                    public void accept(Object t) throws Exception {
                       if(m_notify != null){
                           m_notify.sendFetchTextRequest(vs.page);
                       }
                    }
                }
        );
        m_views.add(vs);
        updateItem(vs);
    }

    private void removeView(final View view) {
        for (PPReaderTextFragmentViews vs : m_views) {
            if (vs.root.equals(view)) {
                m_views.remove(vs);
                return;
            }
        }
    }


    private void updateView(Object object, IPPReaderPageManager pageMgr) {
        for (PPReaderTextFragmentViews vs : m_views) {
            if (vs.root.equals(object)) {
                PPReaderTextPage page = pageMgr.getItem(vs.pos);
                updateView(vs,page);
                return;
            }
        }
    }

    private void updateAllViews(IPPReaderPageManager pageMgr) {
        for (PPReaderTextFragmentViews vs : m_views) {
            PPReaderTextPage page = pageMgr.getItem(vs.pos);
            updateView(vs, page);
        }
    }

    private void updateView(PPReaderTextFragmentViews vs, PPReaderTextPage page) {
        if (!vs.page.equals(page)) {
            vs.page = page;
            vs.status = vs.page.status;
            updateItem(vs);
        } else if (vs.page.equals(page) && page.status != vs.status) {
            vs.status = vs.page.status;
            updateItem(vs);
        }
    }

    private void updateItem(final PPReaderTextFragmentViews vs) {
        final PPReaderTextPage page = vs.page;
        if (page.status == PPReaderTextPage.STATUS_TEXT_NO_SLICE) {
            setNoSliceText(vs,page);
        } else if (page.status == PPReaderTextPage.STATUS_OK) {
            String title = m_pageMgr.getPageTitle(page);
            vs.textView.setText(page,title);
        }
        else if(page.status == PPReaderTextPage.STATUS_INIT){
            if(m_notify != null){
                m_notify.sendFetchTextRequest(vs.page);
                page.status = PPReaderTextPage.STATUS_LOADING;
            }
        }
        setViewByState(vs,page.status);
    }

    private void setNoSliceText(final PPReaderTextFragmentViews vs,final PPReaderTextPage page){
        final StringBuilder text = new StringBuilder();
        text.append("J\n");
        //using dummy title to occupy title place which is just one line.
        // If the real title is length than the width of textview. it will occupy more than 1 line which will cause error.
        text.append("This is dummy\n");
        text.append("J\n");
        text.append("J\n");
        String tx = m_pageMgr.getPageText(page);
        text.append(tx);
        final TextView textView = vs.textView;
        textView.setText(text);
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (page.status != PPReaderTextPage.STATUS_TEXT_NO_SLICE) {
                    return;
                }
                //PPReaderAllocateTextMessage msg = new PPReaderAllocateTextMessage(textView,page);
                //PPReaderMessageCenter.instance().sendMessage(msg);
                int index = m_pageMgr.getIndex(page);
                m_pageMgr.injectText(index,textView);
                notifyDataSetChanged();
            }
        });
    }

    private void setViewByState(final PPReaderTextFragmentViews vs,int status){
        if (status == PPReaderTextPage.STATUS_TEXT_NO_SLICE || status == PPReaderTextPage.STATUS_OK) {
            vs.textView.setVisibility(View.VISIBLE);
            vs.errView.setVisibility(View.GONE);
            vs.maskView.setVisibility(View.GONE);

        } else if (status == PPReaderTextPage.STATUS_FAIL) {
            vs.textView.setVisibility(View.GONE);
            vs.errView.setVisibility(View.VISIBLE);
            vs.maskView.setVisibility(View.GONE);
        } else {
            vs.textView.setVisibility(View.GONE);
            vs.errView.setVisibility(View.GONE);
            vs.maskView.setVisibility(View.VISIBLE);
        }
    }

    private IPPReaderPageManager m_pageMgr;
    private Fragment m_parent;
    private ArrayList<PPReaderTextFragmentViews> m_views = new ArrayList<>();
    private IPPReaderTextAdapterNotify m_notify;

}
