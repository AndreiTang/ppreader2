package org.andrei.ppreader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
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
import org.andrei.ppreader.service.PPReaderTextTaskRet;
import org.andrei.ppreader.ui.adapter.PPReaderTextAdapter;
import org.andrei.ppreader.ui.adapter.helper.IPPReaderPageManager;
import org.andrei.ppreader.ui.adapter.helper.PPReaderPageManager;
import org.andrei.ppreader.ui.fragment.helper.PPReaderText;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextBars;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextCatalog;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextPanel;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextRet;

import java.util.ArrayList;

public class PPReaderTextFragment extends Fragment {

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
        m_bars = new PPReaderTextBars();
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
    }

    private void init(Bundle savedInstanceState){
        m_pageMgr = new PPReaderPageManager();
        m_service.start(new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {

                if(ret.type().compareTo(PPReaderTextTaskRet.class.getName()) != 0){
                    return;
                }

                if(ret.getRetCode() != 0 ){
                    return;
                }

                PPReaderTextTaskRet textTaskRet = (PPReaderTextTaskRet)ret;
                if(textTaskRet.novelId.compareTo(m_novel.id) != 0){
                    return;
                }

                m_pageMgr.updateText(textTaskRet.chapterId,textTaskRet.text);
                m_text.notifyDataSetChanged();
            }
        });

        initViewPager();
        initPanel();
        initCatalog();
        m_isActive = true;
    }

    private void initViewPager(){
        ViewPager vp = getView().findViewById(R.id.novel_text_pager);
        m_text = new PPReaderText(vp, new PPReaderTextAdapter(getActivity(), m_pageMgr));
        m_text.addListener(new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {
                if(ret.type().compareTo(PPReaderTextRet.TYPE_CURR) == 0){
                    int pos = ((PPReaderTextRet)ret).index;
                    setBarInfo(pos);
                }
                else if(ret.type().compareTo(PPReaderTextRet.TYPE_FETCH_TEXT) == 0){
                    int pos = ((PPReaderTextRet)ret).index;
                    PPReaderTextPage page = m_pageMgr.getItem(pos);
                    m_service.addTask(null);
                }
                else if(ret.type().compareTo(PPReaderTextRet.TYPE_DB_CLICK) == 0){
                    m_panel.show();
                }
            }
        });
    }



    private void initPanel(){
        View v = null;
        m_panel = new PPReaderTextPanel(v);
        m_panel.addListener(new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {
                if(ret.type().compareTo(PPReaderTextRet.TYPE_SHOW_CATALOG) == 0){
                    int pos = m_text.getCurrentIndex();
                    PPReaderTextPage page = m_pageMgr.getItem(pos);
                    int index = m_novel.getChapterIndex(page.chapterId);
                    m_catalog.show(index);
                }
                else if(ret.type().compareTo(PPReaderTextRet.TYPE_TO_LIST_PAGE) == 0){
                    //directly tell activity to switch to list page.
                    m_notify.onNotify(ret);
                }
            }
        });
    }

    private void initCatalog(){
        m_catalog = new PPReaderTextCatalog();
        m_catalog.addListener(new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {
                if(ret.type().compareTo(PPReaderTextRet.TYPE_SET_CURR) == 0){
                    int index = ((PPReaderTextRet)ret).index;
                    PPReaderChapter chapter = m_novel.getChapter(index);
                    int pos = m_pageMgr.getChapterFirstPageIndex(chapter.id);
                    m_text.setCurrentItem(pos);
                }
            }
        });
    }

    private void setBarInfo(int position){
        PPReaderTextPage page = m_pageMgr.getItem(position);
        if(page == null){
            return;
        }
        String pageNo = Integer.toString(position+1) + "/" + Integer.toString(m_pageMgr.getCount());
        m_bars.updateInfo(page.title,pageNo);
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
