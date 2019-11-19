package org.andrei.ppreader.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderCommonMessage;
import org.andrei.ppreader.service.message.PPReaderFetchTextMessage;
import org.andrei.ppreader.service.message.PPReaderMessageType;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;
import org.andrei.ppreader.service.message.PPReaderSelectNovelMessage;
import org.andrei.ppreader.service.message.PPReaderUpdateNovelMessage;
import org.andrei.ppreader.service.task.PPReaderFetchTextTask;
import org.andrei.ppreader.ui.adapter.PPReaderCatalogAdapter;
import org.andrei.ppreader.ui.adapter.PPReaderTextAdapter;
import org.andrei.ppreader.ui.adapter.helper.IPPReaderPageManager;
import org.andrei.ppreader.ui.adapter.helper.PPReaderPageManager;
import org.andrei.ppreader.ui.view.PPReaderControlPanel;
import org.andrei.ppreader.ui.view.PPReaderNovelTextCatalog;
import org.andrei.ppreader.ui.view.PPReaderNovelTextTitleBar;
import org.andrei.ppreader.ui.view.helper.PPReaderRxBinding;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class PPReaderNovelTextFragment extends PPReaderBaseFragment {

    public PPReaderNovelTextFragment(){
        //m_notify = notification;
        m_pageManager = new PPReaderPageManager();
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

        m_service.start();
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
            m_service.stop();
        }
        else{
            m_service.start();
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
    public void onDestroy(){
        super.onDestroy();
        m_service.stop();
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
        m_pageManager.load(novel);
        loadNovel();
    }


    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_SELECT_NOVEL)
    protected void selectNovel(IPPReaderMessage msg){
        PPReaderNovel novel = ((PPReaderSelectNovelMessage)msg).getNovel();
        setNovel(novel);
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_SHOW_CATALOG)
    protected void showCatalog(IPPReaderMessage message){
        ViewPager vp = getView().findViewById(R.id.novel_text_pager);
        int pos = vp.getCurrentItem();
        PPReaderTextPage page = m_pageManager.getItem(pos);
        int index = m_novel.getChapterIndex(page.chapterId);
        long duration = (m_novel.duration + System.currentTimeMillis() - m_beginTime)/1000;
        PPReaderNovelTextCatalog catalog = getView().findViewById(R.id.novel_text_catalog);
        catalog.show(index,duration);
    }


    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_TEXT)
    protected void updateText(IPPReaderMessage msg){
        PPReaderFetchTextMessage message = (PPReaderFetchTextMessage) msg;
        if (message.getNovelId().compareTo(m_novel.id) != 0) {
            return;
        }
        if (message.getRetCode() == ServiceError.ERR_OK) {
            m_pageManager.updateText(message.getChapterId(), message.getText());
            updateTextSuccess(message.getChapterId());
        } else {
            updateTextFail(message.getChapterId());
        }
        m_adapter.notifyDataSetChanged();
    }

    private void updateTextSuccess(String chapterId){

        int index = m_pageManager.getChapterFirstPageIndex(chapterId);
        PPReaderTextPage page = m_pageManager.getItem(index);
        if(page.chapterIndex == m_novel.currIndex){
            page.status = PPReaderTextPage.STATUS_TEXT_NO_SLICE;
        }
        else{
            page.status = PPReaderTextPage.STATUS_LOADED;
        }
    }

    private void updateTextFail(String chapterId){
        int index = m_pageManager.getChapterFirstPageIndex(chapterId);
        PPReaderTextPage page = m_pageManager.getItem(index);
        page.status = PPReaderTextPage.STATUS_FAIL;
    }

    private void loadNovel(){
        if(m_novel == null || !m_isActive){
            return;
        }

        setChapterDetail(m_novel.currIndex);

        PPReaderNovelTextCatalog catalog = getView().findViewById(R.id.novel_text_catalog);
        catalog.loadNovel(m_novel);

        switchToCurrentPage();

    }

    private void switchToCurrentPage(){
        if(m_novel.currIndex + m_novel.currOffset == 0){
            //setCurrent is unavailable if the index is 0,so directly set detail
            setCurrentPage(0);
        }
        else{
            ViewPager vp = getView().findViewById(R.id.novel_text_pager);
            PPReaderChapter chapter = m_novel.chapters.get(m_novel.currIndex);
            int index = m_pageManager.getChapterFirstPageIndex(chapter.id);
            if(index > -1){
                index += m_novel.currOffset;
                vp.setCurrentItem(index);
            }
        }
    }

    private void setChapterDetail(int index){
        PPReaderTextPage page = m_pageManager.getItem(index);
        if(page == null){
            return;
        }

        PPReaderNovelTextTitleBar bar = getView().findViewById(R.id.novel_action_bar);
        PPReaderChapter chapter = m_novel.chapters.get(page.chapterIndex);
        bar.setTitle(chapter.title);

        String pageNo = Integer.toString(page.chapterIndex+1) + "/" + Integer.toString(m_novel.chapters.size());
        TextView pageNoView =  getView().findViewById(R.id.novel_bottom_bar);
        pageNoView.setText(pageNo);
    }

    private void init(){
        PPReaderNovelTextTitleBar bar = getView().findViewById(R.id.novel_action_bar);
        bar.registerBatteryReceiver(getActivity());
        initViewPager();
        initPageAdapter();
        initNovelTextCatalog();
        m_isActive = true;
    }

    private void initPageAdapter(){
        m_adapter = new PPReaderTextAdapter(this,m_pageManager, new PPReaderTextAdapter.IPPReaderTextAdapterNotify() {
            @Override
            public void sendFetchTextRequest(PPReaderTextPage page) {
                fetchText(page);
            }
        });

        ViewPager vp = getView().findViewById(R.id.novel_text_pager);
        vp.setAdapter(m_adapter);

    }

    private void initViewPager(){
        ViewPager vp = getView().findViewById(R.id.novel_text_pager);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               setCurrentPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        PPReaderRxBinding.dbClicks(vp).subscribe(new Consumer<MotionEvent>() {
            @Override
            public void accept(MotionEvent motionEvent) throws Exception {
                dbClicks(motionEvent);
            }
        });
    }

    private void initNovelTextCatalog(){
        PPReaderNovelTextCatalog catalog = getView().findViewById(R.id.novel_text_catalog);
        catalog.addOnClickItemNotify(new PPReaderCatalogAdapter.IPPReaderCatalogAdapterNotify() {
            @Override
            public void onClickItem(int index) {
                PPReaderChapter chapter = m_novel.getChapter(index);
                int pos = m_pageManager.getChapterFirstPageIndex(chapter.id);
                ViewPager vp = getView().findViewById(R.id.novel_text_pager);
                vp.setCurrentItem(pos);
                getView().findViewById(R.id.novel_text_catalog).setVisibility(View.GONE);
            }
        });
    }

    private void setCurrentPage(int position){
        PPReaderTextPage page = m_pageManager.getItem(position);
        m_novel.currIndex = page.chapterIndex;
        m_novel.currOffset = page.offset;
        if(page.status == PPReaderTextPage.STATUS_LOADED){
            page.status = PPReaderTextPage.STATUS_TEXT_NO_SLICE;
        }
        else if(page.status == PPReaderTextPage.STATUS_INIT){
            fetchText(page);
        }
        setChapterDetail(position);
        m_adapter.notifyDataSetChanged();
    }

    private void fetchText(PPReaderTextPage page){
        PPReaderFetchTextTask task = new PPReaderFetchTextTask(m_novel,m_novel.getChapter(page.chapterIndex));
        m_service.addTask(task);
        page.status = PPReaderTextPage.STATUS_LOADING;
    }

    private void dbClicks(MotionEvent motionEvent){
        PPReaderControlPanel panel = getView().findViewById(R.id.novel_text_panel);
        panel.show((int)motionEvent.getRawX(),(int)motionEvent.getRawY());
    }

    private final static String NOVEL = "novel";
    private PPReaderNovel m_novel;
    private boolean m_isActive = false;
    private long m_beginTime;
    private IPPReaderPageManager m_pageManager;
    private PPReaderTextAdapter m_adapter;
}
