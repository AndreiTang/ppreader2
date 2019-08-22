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
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderCommonMessage;
import org.andrei.ppreader.service.message.PPReaderDBClicksMessage;
import org.andrei.ppreader.service.message.PPReaderMessageType;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;
import org.andrei.ppreader.service.message.PPReaderSelectNovelMessage;
import org.andrei.ppreader.service.message.PPReaderUpdateNovelMessage;
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

public class PPReaderTextFragment extends Fragment {

    public void init(IPPReaderService service){
        //m_notify = notification;
        m_pageMgr = new PPReaderPageManager();
        m_text = new PPReaderText(m_pageMgr,service);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ppreader_text, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            m_novel = (PPReaderNovel) savedInstanceState.getSerializable(NOVEL);
        }

        init();
        loadNovel();

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

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_SELECT_NOVEL)
    protected void selectNovel(IPPReaderMessage msg){
        m_novel = ((PPReaderSelectNovelMessage)msg).getNovel();
        m_catalog.setNovel(m_novel);
        if(m_isActive){
            loadNovel();
        }
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
        setBarInfo(message.getValue());
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

    private void loadNovel(){
        if(m_novel == null){
            return;
        }
        m_text.loadNovel(m_novel);
        setBarInfo(m_novel.currIndex);
    }

    protected void setBarInfo(int index){
        PPReaderTextPage page = m_pageMgr.getItem(index);
        if(page == null){
            return;
        }
        String pageNo = Integer.toString(page.chapterIndex+1) + "/" + Integer.toString(m_novel.chapters.size());
        m_bars.updateInfo(page.title,pageNo);
    }

    private void init(){
        PPReaderControlPanel panel = getView().findViewById(R.id.novel_text_panel);

        View actionBar = getView().findViewById(R.id.novel_action_bar);
        TextView bottomBar = getView().findViewById(R.id.novel_bottom_bar);
        m_bars = new PPReaderTextBars(actionBar,bottomBar,getActivity());

        View catalogView = getView().findViewById(R.id.novel_text_catalog);
        m_catalog = new PPReaderTextCatalog(catalogView,this);

        ViewPager vp = getView().findViewById(R.id.novel_text_pager);
        m_text.init(vp,this);


        m_isActive = true;
    }



    private final static String NOVEL = "novel";
    private IPPReaderPageManager m_pageMgr;
    private PPReaderText m_text;
    private PPReaderTextBars m_bars;
    private PPReaderNovel m_novel;
    private PPReaderTextCatalog m_catalog;
    //private IPPReaderTaskNotification m_notify;
    private boolean m_isActive = false;
    private long m_beginTime;

}
