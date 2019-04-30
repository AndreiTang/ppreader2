package org.andrei.ppreader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListView;
import android.widget.TextView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderTextRet;
import org.andrei.ppreader.service.PPReaderTextTask;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.ui.adapter.PPReaderTextAdapter;
import org.andrei.ppreader.ui.adapter.helper.IPPReaderPageManager;
import org.andrei.ppreader.ui.adapter.helper.PPReaderPageManager;
import org.andrei.ppreader.ui.fragment.helper.PPReaderAllocateTextRet;
import org.andrei.ppreader.ui.fragment.helper.PPReaderCommonRet;
import org.andrei.ppreader.ui.fragment.helper.PPReaderDBClicksRet;
import org.andrei.ppreader.ui.fragment.helper.PPReaderText;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextBars;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextCatalog;
import org.andrei.ppreader.ui.view.PPReaderControlPanel;

import java.util.ArrayList;

public class PPReaderTextFragment extends Fragment implements IPPReaderTaskNotification {

    public void init(IPPReaderService service,IPPReaderTaskNotification notification){
        m_service = service;
        m_notify = notification;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ppreader_text, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init(savedInstanceState);
        loadNovel();

        final View root = this.getActivity().findViewById(android.R.id.content);
        root.findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onHiddenChanged(boolean hidden){
        super.onHiddenChanged(hidden);
        if(hidden){
            m_novel.duration += System.currentTimeMillis() - m_beginTime;
            m_beginTime  = 0;
        }
        else{
            m_beginTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        m_beginTime = System.currentTimeMillis();
    }

    @Override
    public void onStop(){
        super.onStop();
        if(m_beginTime != 0){
            m_novel.duration += System.currentTimeMillis() - m_beginTime;
            m_beginTime  = 0;
        }
    }

    @Override
    public void onNotify(IPPReaderTaskRet ret) {
        if(ret.type().compareTo(PPReaderCommonRet.TYPE_CURR) == 0){
            int pos = ((PPReaderCommonRet)ret).index;
            PPReaderTextPage page = m_pageMgr.getItem(pos);
            if(page.status == PPReaderTextPage.STATUS_INVALID){
                return;
            }
            setBarInfo(pos);
            fetchText(pos);
            m_novel.currIndex = page.chapterIndex;
            PPReaderChapter chapter = m_novel.chapters.get(page.chapterIndex);
            chapter.offset = page.offset;
        }
        else if(ret.type().compareTo(PPReaderCommonRet.TYPE_FETCH_TEXT) == 0){
            PPReaderCommonRet r = (PPReaderCommonRet)ret;
            fetchText(r.index);
        }
        else if(ret.type().compareTo(PPReaderDBClicksRet.class.getName()) == 0){
            PPReaderDBClicksRet r = (PPReaderDBClicksRet)ret;
            PPReaderControlPanel panel = getView().findViewById(R.id.novel_text_panel);
            panel.show((int)r.event.getRawX(),(int)r.event.getRawY());
        }
        else if(ret.type().compareTo(PPReaderCommonRet.TYPE_SHOW_CATALOG) == 0){
            int pos = m_text.getCurrentIndex();
            PPReaderTextPage page = m_pageMgr.getItem(pos);
            int index = m_novel.getChapterIndex(page.chapterId);
            long duration = (m_novel.duration + System.currentTimeMillis() - m_beginTime)/1000;
            m_catalog.show(index,duration);
        }
        else if(ret.type().compareTo(PPReaderCommonRet.TYPE_TO_LIST_PAGE) == 0){
            //directly tell activity to switch to list page.
            m_notify.onNotify(ret);
        }
        else if(ret.type().compareTo(PPReaderCommonRet.TYPE_SET_CURR) == 0){
            int index = ((PPReaderCommonRet)ret).index;
            PPReaderChapter chapter = m_novel.getChapter(index);
            int pos = m_pageMgr.getChapterFirstPageIndex(chapter.id);
            m_text.setCurrentItem(pos);
            getView().findViewById(R.id.novel_text_catalog).setVisibility(View.GONE);
        }
        else if(ret.type().compareTo(PPReaderTextRet.class.getName()) == 0){
            if(ret.getRetCode() != 0 ){
                return;
            }

            PPReaderTextRet textTaskRet = (PPReaderTextRet)ret;
            if(textTaskRet.novelId.compareTo(m_novel.id) != 0){
                return;
            }

            if(textTaskRet.getRetCode() == ServiceError.ERR_OK){
                m_pageMgr.updateText(textTaskRet.chapterId,textTaskRet.text);
            }
            else {
                int index = m_pageMgr.getChapterFirstPageIndex(textTaskRet.chapterId);
                PPReaderTextPage page = m_pageMgr.getItem(index);
                page.status  = PPReaderTextPage.STATUS_FAIL;
            }
            m_text.notifyDataSetChanged();
        }
        else if(ret.type().compareTo(PPReaderAllocateTextRet.class.getName())==0){
            PPReaderAllocateTextRet r = (PPReaderAllocateTextRet)ret;

            PPReaderTextPage item = m_pageMgr.getItem(r.index);
            if(item.equals(r.page)){
                m_pageMgr.injectText(r.index,r.tv);
                m_text.notifyDataSetChanged();
            }
        }
    }

    public void setNovel(final PPReaderNovel novel){
        m_novel = novel;
        if(m_isActive){
            loadNovel();
        }
    }

    public void onAddChapters(final String novelId, final ArrayList<PPReaderChapter> chapters){
        if(novelId.compareTo(m_novel.id) != 0){
            return;
        }
        for(int i = 0; i < chapters.size(); i++){
            m_pageMgr.addItem(chapters.get(i));
        }
        m_text.notifyDataSetChanged();
    }

    private void loadNovel(){
        if(m_novel == null){
            return;
        }
        m_service.clearTasks();
        m_pageMgr.load(m_novel);
        m_text.setCurrentItem(m_novel.currIndex);
        setBarInfo(m_novel.currIndex);
        if(m_novel.currIndex == 0){
            fetchText(0);
        }

        final PPReaderChapter chapter = m_novel.chapters.get(m_novel.currIndex);
        if(chapter.offset > 0){
            final ListView lv = getView().findViewById(R.id.novel_text_pager);
            lv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    lv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int index = m_pageMgr.getChapterFirstPageIndex(chapter.id) + chapter.offset;
                    lv.setSelection(index);
                }
            });
        }
    }

    private void init(Bundle savedInstanceState){
        PPReaderControlPanel panel = getView().findViewById(R.id.novel_text_panel);
        panel.addListener(this);
        View actionBar = getView().findViewById(R.id.novel_action_bar);
        TextView bottomBar = getView().findViewById(R.id.novel_bottom_bar);
        m_bars = new PPReaderTextBars(actionBar,bottomBar,getActivity());
        View catalogView = getView().findViewById(R.id.novel_text_catalog);
        m_catalog = new PPReaderTextCatalog(catalogView,m_novel,this,this);
        m_pageMgr = new PPReaderPageManager();
        m_service.start(this);
        ViewPager vp = getView().findViewById(R.id.novel_text_pager);
        m_text = new PPReaderText(vp, new PPReaderTextAdapter(getActivity(), m_pageMgr,this),this);

        m_isActive = true;
    }

    private void setBarInfo(int position){
        PPReaderTextPage page = m_pageMgr.getItem(position);
        if(page == null){
            return;
        }
        String pageNo = Integer.toString(page.chapterIndex+1) + "/" + Integer.toString(m_novel.chapters.size());
        m_bars.updateInfo(page.title,pageNo);
    }

    private void fetchText(int pos){
        PPReaderTextPage page = m_pageMgr.getItem(pos);
        if(page.status == PPReaderTextPage.STATUS_INIT){
            page.status = PPReaderTextPage.STATUS_LOADING;
            m_text.notifyDataSetChanged();

            PPReaderTextTask task = new PPReaderTextTask();
            task.chapterId = page.chapterId;
            task.novelId = m_novel.id;
            task.chapterUrl = m_novel.getChapter(page.chapterIndex).url;
            m_service.addTask(task);
        }
    }


    private IPPReaderPageManager m_pageMgr;
    private IPPReaderService m_service;
    private PPReaderText m_text;
    private PPReaderTextBars m_bars;
    private PPReaderNovel m_novel;
    private PPReaderTextCatalog m_catalog;
    private IPPReaderTaskNotification m_notify;
    private boolean m_isActive = false;
    private long m_beginTime;


}
