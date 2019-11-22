package org.andrei.ppreader.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.IPPReaderServiceNotification;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderFetchTextMessage;
import org.andrei.ppreader.service.message.PPReaderMessageType;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;
import org.andrei.ppreader.service.task.PPReaderFetchTextTask;
import org.andrei.ppreader.ui.adapter.PPReaderCatalogAdapter;
import org.andrei.ppreader.ui.adapter.PPReaderTextAdapter;
import org.andrei.ppreader.ui.adapter.helper.IPPReaderPageManager;
import org.andrei.ppreader.ui.adapter.helper.PPReaderPageManager;
import org.andrei.ppreader.ui.view.PPReaderControlPanel;
import org.andrei.ppreader.ui.view.PPReaderNovelTextCatalog;
import org.andrei.ppreader.ui.view.PPReaderNovelTextTitleBar;
import org.andrei.ppreader.ui.view.helper.PPReaderRxBinding;

import io.reactivex.functions.Consumer;

public class PPReaderNovelTextFragment extends PPReaderBaseFragment implements IPPReaderServiceNotification {

    public PPReaderNovelTextFragment(){
        //m_notify = notification;
        m_pageManager = new PPReaderPageManager();
    }

    public void addOnNotification(IPPReaderNovelTextFragmentNotification notification){
        m_notification = notification;
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

        m_service.start(this);
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
            m_service.start(this);
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

    @Override
    public void onNotify(IPPReaderMessage msg) {
        if(msg.type().compareTo(PPReaderMessageTypeDefine.TYPE_TEXT) == 0){
            updateText(msg);
        }
    }

    public void setNovel(final PPReaderNovel novel){
        m_novel = novel;
        m_pageManager.load(novel);
        loadNovel();
    }

    public void backPress(){
        if(m_dataManager.getNovel(m_novel.id) == null){
            popUpSaveDlg();
        }
        else{
            getActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();
        }
    }

    private void  popUpSaveDlg(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.novel_text_save_dlg);

        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_novel.needValidate = true;
                if(m_notification != null){
                    m_notification.onAddNovel(m_novel);
                }
                getActivity().getSupportFragmentManager().beginTransaction().hide(PPReaderNovelTextFragment.this).commit();
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().getSupportFragmentManager().beginTransaction().hide(PPReaderNovelTextFragment.this).commit();
            }
        });
        builder.show();
    }

    private void updateText(IPPReaderMessage msg){
        PPReaderFetchTextMessage message = (PPReaderFetchTextMessage) msg;
        if (message.getNovelId().compareTo(m_novel.id) != 0) {
            return;
        }
        String text = "";
        boolean bRet = false;
        if (message.getRetCode() == ServiceError.ERR_OK) {
            bRet = true;
            text = message.getText();
        }
        m_novel.needValidate = true;
        m_pageManager.updateText(message.getChapterId(),bRet,text);
        m_adapter.notifyDataSetChanged();
    }

    private void loadNovel(){
        if(m_novel == null || !m_isActive){
            return;
        }

        setChapterDetail(m_novel.currIndex);

        PPReaderNovelTextCatalog catalog = getView().findViewById(R.id.novel_text_catalog);
        String imgRootUrl = m_dataManager.getEngineInfo(m_novel.engineName).imageUrl;
        catalog.loadNovel(m_novel,imgRootUrl);

        switchToCurrentPage();
    }

    private void switchToCurrentPage(){
        int index = m_pageManager.getCurrentIndex();
        if(index == 0){
            //setCurrent is unavailable if the index is 0,so directly set detail
            setCurrentPage(0);
        }
        else if(index > 0){
            ViewPager vp = getView().findViewById(R.id.novel_text_pager);
            vp.setCurrentItem(index);
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
        initControlPanel();
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

    private void initControlPanel(){
        PPReaderControlPanel panel = getView().findViewById(R.id.novel_text_panel);
        panel.addOnAction(new PPReaderControlPanel.IPPReaderControlPanelAction() {
            @Override
            public void doAction(Action action) {
                if(action == Action.Catalog){
                    showCatalog();
                }
                else{
                    int index = 0;
                    if(action == Action.Search){
                        index = 1;
                    }
                    if(m_notification != null){
                        m_notification.onSwitchFragment(index);
                    }
                }
            }
        });
    }

    //@PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_SHOW_CATALOG)
    private void showCatalog(){
        ViewPager vp = getView().findViewById(R.id.novel_text_pager);
        int pos = vp.getCurrentItem();
        PPReaderTextPage page = m_pageManager.getItem(pos);
        int index = m_novel.getChapterIndex(page.chapterId);
        long duration = (m_novel.duration + System.currentTimeMillis() - m_beginTime)/1000;
        PPReaderNovelTextCatalog catalog = getView().findViewById(R.id.novel_text_catalog);
        catalog.show(index,duration);
    }

    private void setCurrentPage(int position){
        PPReaderTextPage page = m_pageManager.getItem(position);
        if(page.status == PPReaderTextPage.STATUS_INVALID){
            return;
        }
        else if(page.status == PPReaderTextPage.STATUS_INIT){
            fetchText(page);
        }
        m_pageManager.setCurrentIndex(position);
        setChapterDetail(position);
        m_adapter.notifyDataSetChanged();
    }

    private void fetchText(PPReaderTextPage page){
        PPReaderFetchTextTask task = new PPReaderFetchTextTask(m_novel,m_novel.getChapter(page.chapterIndex));
        m_service.addTask(task);
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
    private IPPReaderNovelTextFragmentNotification m_notification;


}
