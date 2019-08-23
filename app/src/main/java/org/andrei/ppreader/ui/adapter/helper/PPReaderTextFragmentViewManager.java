package org.andrei.ppreader.ui.adapter.helper;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.message.PPReaderAllocateTextMessage;
import org.andrei.ppreader.service.message.PPReaderCommonMessage;
import org.andrei.ppreader.service.message.PPReaderMessageCenter;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class PPReaderTextFragmentViewManager {


    public void addView(final View view, final PPReaderTextPage page, final int pos) {

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
                        PPReaderCommonMessage msg = new PPReaderCommonMessage(PPReaderMessageTypeDefine.TYPE_FETCH_TEXT,pos);
                        PPReaderMessageCenter.instance().sendMessage(msg);
                    }
                }
        );
        m_views.add(vs);
        updateItem(vs);
    }

    public void removeView(final View view) {
        for (PPReaderTextFragmentViews vs : m_views) {
            if (vs.root.equals(view)) {
                m_views.remove(vs);
                return;
            }
        }
    }

    public PPReaderTextFragmentViews getItem(int index) {
        return m_views.get(index);
    }

    public int getCount() {
        return m_views.size();
    }

    public void updateView(Object object, IPPReaderPageManager pageMgr) {
        for (PPReaderTextFragmentViews vs : m_views) {
            if (vs.root.equals(object)) {
                PPReaderTextPage page = pageMgr.getItem(vs.pos);
                updateView(vs,page);
                return;
            }
        }
    }

    public void updateAllViews(IPPReaderPageManager pageMgr) {
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
            vs.textView.setVisibility(View.VISIBLE);
            vs.errView.setVisibility(View.GONE);
            vs.maskView.setVisibility(View.GONE);
            final StringBuilder text = new StringBuilder();
            text.append("J\n");
            //using dummy title to occupy title place which is just one line.
            // If the real title is length than the width of textview. it will occupy more than 1 line which will cause error.
            text.append("This is dummy\n");
            text.append("J\n");
            text.append("J\n");
            text.append("J\n");
            text.append(page.text);
            final TextView textView = vs.textView;
            textView.setText(text);
            textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if (page.status != PPReaderTextPage.STATUS_TEXT_NO_SLICE) {
                        return;
                    }

                    PPReaderAllocateTextMessage msg = new PPReaderAllocateTextMessage(textView,page);
                    PPReaderMessageCenter.instance().sendMessage(msg);
                }
            });

        } else if (page.status == PPReaderTextPage.STATUS_OK) {
            vs.textView.setVisibility(View.VISIBLE);
            vs.errView.setVisibility(View.GONE);
            vs.maskView.setVisibility(View.GONE);
            vs.textView.setText(page);
        } else if (page.status == PPReaderTextPage.STATUS_FAIL) {
            vs.textView.setVisibility(View.GONE);
            vs.errView.setVisibility(View.VISIBLE);
            vs.maskView.setVisibility(View.GONE);
        } else {
            vs.textView.setVisibility(View.GONE);
            vs.errView.setVisibility(View.GONE);
            vs.maskView.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<PPReaderTextFragmentViews> m_views = new ArrayList<>();

}
