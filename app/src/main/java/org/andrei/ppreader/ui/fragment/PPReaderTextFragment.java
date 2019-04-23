package org.andrei.ppreader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderTextRet;
import org.andrei.ppreader.service.PPReaderTextTask;
import org.andrei.ppreader.ui.adapter.PPReaderTextAdapter;
import org.andrei.ppreader.ui.adapter.helper.IPPReaderPageManager;
import org.andrei.ppreader.ui.adapter.helper.PPReaderPageManager;
import org.andrei.ppreader.ui.fragment.helper.PPReaderAllocateTextRet;
import org.andrei.ppreader.ui.fragment.helper.PPReaderText;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextBars;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextCatalog;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextPanel;

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
    }

    @Override
    public void onHiddenChanged(boolean hidden){
        super.onHiddenChanged(hidden);
        if(hidden){

        }
        else{

        }
    }

    @Override
    public void onNotify(IPPReaderTaskRet ret) {
        if(ret.type().compareTo(org.andrei.ppreader.ui.fragment.helper.PPReaderTextRet.TYPE_CURR) == 0){
            int pos = ((org.andrei.ppreader.ui.fragment.helper.PPReaderTextRet)ret).index;
            setBarInfo(pos);
            fetchText(pos);
        }
        else if(ret.type().compareTo(org.andrei.ppreader.ui.fragment.helper.PPReaderTextRet.TYPE_DB_CLICK) == 0){
            m_panel.show();
        }
        else if(ret.type().compareTo(org.andrei.ppreader.ui.fragment.helper.PPReaderTextRet.TYPE_SHOW_CATALOG) == 0){
            int pos = m_text.getCurrentIndex();
            PPReaderTextPage page = m_pageMgr.getItem(pos);
            int index = m_novel.getChapterIndex(page.chapterId);
            m_catalog.show(index);
        }
        else if(ret.type().compareTo(org.andrei.ppreader.ui.fragment.helper.PPReaderTextRet.TYPE_TO_LIST_PAGE) == 0){
            //directly tell activity to switch to list page.
            m_notify.onNotify(ret);
        }
        else if(ret.type().compareTo(org.andrei.ppreader.ui.fragment.helper.PPReaderTextRet.TYPE_SET_CURR) == 0){
            int index = ((org.andrei.ppreader.ui.fragment.helper.PPReaderTextRet)ret).index;
            PPReaderChapter chapter = m_novel.getChapter(index);
            int pos = m_pageMgr.getChapterFirstPageIndex(chapter.id);
            m_text.setCurrentItem(pos);
        }
        else if(ret.type().compareTo(PPReaderTextRet.class.getName()) == 0){
            if(ret.getRetCode() != 0 ){
                return;
            }

            PPReaderTextRet textTaskRet = (PPReaderTextRet)ret;
            if(textTaskRet.novelId.compareTo(m_novel.id) != 0){
                return;
            }

            m_pageMgr.updateText(textTaskRet.chapterId,textTaskRet.text);
            m_text.notifyDataSetChanged();
        }
        else if(ret.type().compareTo(PPReaderAllocateTextRet.class.getName())==0){
            PPReaderAllocateTextRet r = (PPReaderAllocateTextRet)ret;

            m_pageMgr.injectText(r.index,r.layout);
            m_text.notifyDataSetChanged();
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
    }

    private void init(Bundle savedInstanceState){
        View v = null;
        m_panel = new PPReaderTextPanel(v,this);
        m_bars = new PPReaderTextBars();
        m_catalog = new PPReaderTextCatalog(this);
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
        String pageNo = Integer.toString(position+1) + "/" + Integer.toString(m_pageMgr.getCount());
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
    private PPReaderTextPanel m_panel;
    private PPReaderTextCatalog m_catalog;
    private IPPReaderTaskNotification m_notify;
    private boolean m_isActive = false;


}
