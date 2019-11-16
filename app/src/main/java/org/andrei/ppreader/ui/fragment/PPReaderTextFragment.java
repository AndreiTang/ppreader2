package org.andrei.ppreader.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderAllocateTextMessage;
import org.andrei.ppreader.service.message.PPReaderCommonMessage;
import org.andrei.ppreader.service.message.PPReaderDBClicksMessage;
import org.andrei.ppreader.service.message.PPReaderFetchTextMessage;
import org.andrei.ppreader.service.message.PPReaderMessageType;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;
import org.andrei.ppreader.service.message.PPReaderSelectNovelMessage;
import org.andrei.ppreader.service.message.PPReaderUpdateNovelMessage;
import org.andrei.ppreader.ui.adapter.helper.IPPReaderPageManager;
import org.andrei.ppreader.ui.adapter.helper.PPReaderPageManager;
import org.andrei.ppreader.ui.fragment.helper.PPReaderText;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextCatalog;
import org.andrei.ppreader.ui.view.PPReaderControlPanel;
import org.andrei.ppreader.ui.view.PPReaderTextTitleBar;

import java.util.ArrayList;

public class PPReaderTextFragment extends PPReaderBaseFragment {

    public PPReaderTextFragment(){
        //m_notify = notification;
        m_pageMgr = new PPReaderPageManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ppreader_text, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        m_text = new PPReaderText(m_pageMgr,m_service);
        if(savedInstanceState != null){
            m_novel = (PPReaderNovel) savedInstanceState.getSerializable(NOVEL);
        }

        init();
        loadNovel();
        m_catalog.setNovel(m_novel);

        //final View root = this.getActivity().findViewById(android.R.id.content);
        //root.findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getActivity().findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onHiddenChanged(boolean hidden){
        super.onHiddenChanged(hidden);
        if(hidden){
            if(m_novel != null){
                m_novel.duration += System.currentTimeMillis() - m_beginTime;
            }
            m_beginTime  = 0;
        }
        else{
            m_beginTime = System.currentTimeMillis();
            getActivity().findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
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
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        if(m_novel != null){
            savedInstanceState.putSerializable(NOVEL,m_novel);
        }
    }

    public void setNovel(final PPReaderNovel novel){
        m_novel = novel;
        loadNovel();
    }

   @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_FETCH_TEXT)
    protected void fetchText(IPPReaderMessage msg) {
        PPReaderCommonMessage message = (PPReaderCommonMessage) msg;
        m_text.fetchText(message.getValue());
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_ALLOCATE_TEXT)
    protected void allocateText(IPPReaderMessage msg){
        PPReaderAllocateTextMessage message = (PPReaderAllocateTextMessage) msg;
        int index = m_pageMgr.getIndex(message.getPage());
        m_pageMgr.injectText(index,message.getTv());
        m_text.notifyDataSetChanged();
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_SELECT_NOVEL)
    protected void selectNovel(IPPReaderMessage msg){
        m_novel = ((PPReaderSelectNovelMessage)msg).getNovel();
        loadNovel();
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_UPDATE_NOVEL)
    protected void onAddChapters(IPPReaderMessage msg){
        String novelId = ((PPReaderUpdateNovelMessage)msg).getId();
        if(novelId.compareTo(m_novel.id) != 0){
            return;
        }
        ArrayList<PPReaderChapter> chapters = ((PPReaderUpdateNovelMessage) msg).getDelta();
        for(int i = 0; i < chapters.size(); i++){
            m_pageMgr.addItem(chapters.get(i));
        }
        m_text.notifyDataSetChanged();
    }


    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_CURR)
    protected void setBarInfo(IPPReaderMessage msg){
        PPReaderCommonMessage message = (PPReaderCommonMessage)msg;
        setChapterText(message.getValue());
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_DB_CLICKS)
    protected void dbClicks(IPPReaderMessage msg){
        PPReaderDBClicksMessage message = (PPReaderDBClicksMessage) msg;
        PPReaderControlPanel panel = getView().findViewById(R.id.novel_text_panel);
        panel.show((int)message.getEvent().getRawX(),(int)message.getEvent().getRawY());
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_SET_CURR)
    protected void setCurrentText(IPPReaderMessage msg){
        int index = ((PPReaderCommonMessage)msg).getValue();
        PPReaderChapter chapter = m_novel.getChapter(index);
        int pos = m_pageMgr.getChapterFirstPageIndex(chapter.id);
        m_text.setCurrentItem(pos);
        getView().findViewById(R.id.novel_text_catalog).setVisibility(View.GONE);
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_SHOW_CATALOG)
    protected void showCatalog(IPPReaderMessage message){
        int pos = m_text.getCurrentIndex();
        PPReaderTextPage page = m_pageMgr.getItem(pos);
        int index = m_novel.getChapterIndex(page.chapterId);
        long duration = (m_novel.duration + System.currentTimeMillis() - m_beginTime)/1000;
        m_catalog.show(index,duration);
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_SET_RANGE)
    protected void setCatalogRange(IPPReaderMessage msg){
        int index = ((PPReaderCommonMessage)msg).getValue();
        m_catalog.setRange(index);
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_TEXT)
    protected void updateText(IPPReaderMessage msg){
        PPReaderFetchTextMessage message = (PPReaderFetchTextMessage) msg;

        if (message.getRetCode() != 0) {
            return;
        }

        if (message.getNovelId().compareTo(m_novel.id) != 0) {
            return;
        }

        if (message.getRetCode() == ServiceError.ERR_OK) {
            m_pageMgr.updateText(message.getChapterId(), message.getText());
            int index = m_pageMgr.getChapterFirstPageIndex(message.getChapterId());
            PPReaderTextPage page = m_pageMgr.getItem(index);
            if(page.chapterIndex == m_novel.currIndex){
                page.status = PPReaderTextPage.STATUS_TEXT_NO_SLICE;
            }
            else{
                page.status = PPReaderTextPage.STATUS_LOADED;
            }

        } else {
            int index = m_pageMgr.getChapterFirstPageIndex(message.getChapterId());
            PPReaderTextPage page = m_pageMgr.getItem(index);
            page.status = PPReaderTextPage.STATUS_FAIL;
        }
        m_text.notifyDataSetChanged();
    }

    private void loadNovel(){
        if(m_novel == null || !m_isActive){
            return;
        }
        m_text.loadNovel(m_novel);
        setChapterText(m_novel.currIndex);
        m_catalog.setNovel(m_novel);
    }

    protected void setChapterText(int index){
        PPReaderTextPage page = m_pageMgr.getItem(index);
        if(page == null){
            return;
        }

        PPReaderTextTitleBar bar = getView().findViewById(R.id.novel_action_bar);
        bar.setTitle(page.title);

        String pageNo = Integer.toString(page.chapterIndex+1) + "/" + Integer.toString(m_novel.chapters.size());
        TextView pageNoView =  getView().findViewById(R.id.novel_bottom_bar);
        pageNoView.setText(pageNo);
    }

    private void init(){
        PPReaderControlPanel panel = getView().findViewById(R.id.novel_text_panel);


        PPReaderTextTitleBar bar = getView().findViewById(R.id.novel_action_bar);
        bar.registerBatteryReceiver(getActivity());


        View catalogView = getView().findViewById(R.id.novel_text_catalog);
        m_catalog = new PPReaderTextCatalog(catalogView,this);

        ViewPager vp = getView().findViewById(R.id.novel_text_pager);
        m_text.init(vp,this);

        m_isActive = true;
    }



    private final static String NOVEL = "novel";
    private IPPReaderPageManager m_pageMgr;
    private PPReaderText m_text;
    private PPReaderNovel m_novel;
    private PPReaderTextCatalog m_catalog;
    private boolean m_isActive = false;
    private long m_beginTime;

}
