package org.andrei.ppreader.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.message.PPReaderFetchNovelMessage;
import org.andrei.ppreader.service.task.PPReaderFetchNovelTask;
import org.andrei.ppreader.service.task.PPReaderSearchNovelsTask;
import org.andrei.ppreader.service.task.PPReaderSearchUrlsTask;
import org.andrei.ppreader.service.task.PPReaderUpdateNovelTask;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderCommonMessage;
import org.andrei.ppreader.service.message.PPReaderMessageType;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;
import org.andrei.ppreader.service.message.PPReaderSearchNovelsMessage;
import org.andrei.ppreader.service.message.PPReaderSearchUrlsMessage;
import org.andrei.ppreader.service.message.PPReaderUpdateNovelMessage;
import org.andrei.ppreader.ui.adapter.PPReaderSearchAdapter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class PPReaderSearchFragment extends PPReaderBaseFragment {

    public void init(final IPPReaderService service){
        //m_notification = notification;
        m_service = service;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ppreader_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initUI();
        m_service.start();
        if(savedInstanceState != null){
            m_urls = (ArrayList<String>)savedInstanceState.getSerializable(KEY_URLS);
            ArrayList<PPReaderNovel> novels = (ArrayList<PPReaderNovel>)savedInstanceState.getSerializable(KEY_NOVELS);

            ListView lv = getView().findViewById(R.id.novel_search_ret_list);
            PPReaderSearchAdapter adapter = (PPReaderSearchAdapter)lv.getAdapter();
            if(novels != null && novels.size() > 0){
                for(PPReaderNovel novel : novels){
                    adapter.addNovel(novel);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        if(m_urls != null && m_urls.size() > 0){
            savedInstanceState.putSerializable(KEY_URLS,m_urls);
        }

        PPReaderSearchAdapter adapter = getAdapter();
        if(adapter != null){
            adapter.saveInstanceState(savedInstanceState,KEY_NOVELS);
        }
    }

    private void initUI(){
        m_footView = getLayoutInflater().inflate(R.layout.view_ppreader_search_foot, null);
        initButtons();
        initListView();
        initSearchView();
    }

    private void initButtons(){
        TextView tv = getView().findViewById(R.id.main_item_title);
        tv.setText(R.string.novel_search_title);

        tv = getView().findViewById(R.id.main_item_left_btn);
        tv.setText(R.string.novel_search_list);
        RxView.clicks(tv).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception{
                PPReaderCommonMessage msg = new PPReaderCommonMessage(PPReaderMessageTypeDefine.TYPE_TO_LIST_PAGE,0);
                sendMessage(msg);

            }
        });

        tv = getView().findViewById(R.id.main_item_right_btn);
        tv.setText(R.string.novel_search_setting);
        RxView.clicks(tv).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception{
                PPReaderCommonMessage msg = new PPReaderCommonMessage(PPReaderMessageTypeDefine.TYPE_TO_LIST_PAGE,2);
                sendMessage(msg);
            }
        });
    }

    private void initSearchView(){
        SearchView sv = (SearchView) getView().findViewById(R.id.novel_search);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                m_service.stop();
                m_service.waitForExit();
                m_service.start();
                removeFootView();
                PPReaderSearchAdapter adapter = getAdapter();
                adapter.clear();
                getView().findViewById(R.id.novel_search_ret_list).setVisibility(View.GONE);
                getView().findViewById(R.id.novel_search_error_mask).setVisibility(View.GONE);
                getView().findViewById(R.id.novel_search_loading_mask).setVisibility(View.VISIBLE);

                PPReaderSearchUrlsTask task = new PPReaderSearchUrlsTask("大明");
                m_service.addTask(task);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initListView(){
        final ListView lv = getView().findViewById(R.id.novel_search_ret_list);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem + visibleItemCount >= totalItemCount && m_service.isIdle() && m_urls != null && m_urls.size() > 0){
                    String url = m_urls.remove(0);
                    PPReaderSearchNovelsTask task = new PPReaderSearchNovelsTask(url,m_engineName);
                    m_service.addTask(task);
                }
            }
        });
        PPReaderSearchAdapter adapter = new PPReaderSearchAdapter(this);
        lv.setAdapter(adapter);
    }

    @PPReaderMessageType(type=PPReaderMessageTypeDefine.TYPE_SEARCH_URLS)
    protected void searchUrls(IPPReaderMessage msg){

        PPReaderSearchUrlsMessage message = (PPReaderSearchUrlsMessage)msg;

        if(message.getRetCode() == ServiceError.ERR_OK){
            m_urls = message.getUrls();
            String url= m_urls.remove(0);
            m_engineName = message.getEngineName();
            PPReaderSearchNovelsTask task = new PPReaderSearchNovelsTask(url,m_engineName);
            m_service.addTask(task);
        }
        else{
            getView().findViewById(R.id.novel_search_ret_list).setVisibility(View.GONE);
            getView().findViewById(R.id.novel_search_error_mask).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.novel_search_loading_mask).setVisibility(View.GONE);
            TextView tv = getView().findViewById(R .id.novel_search_err_msg);
            if(message.getRetCode() == ServiceError.ERR_NOT_FOUND){
                tv.setText(R.string.err_not_found);
            }
            else if(message.getRetCode() == ServiceError.ERR_NOT_NETWORK){
                tv.setText(R.string.err_network);
            }
        }
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_SEARCH_NOVELS)
    protected void searchNovels(IPPReaderMessage msg){
        PPReaderSearchNovelsMessage message = (PPReaderSearchNovelsMessage)msg;
        if(message.getRetCode() == ServiceError.ERR_OK){
            ArrayList<PPReaderNovel> novels = message.getNovels();
            for(PPReaderNovel novel : novels){
                m_novels.add(novel);
                PPReaderFetchNovelTask task = new PPReaderFetchNovelTask(novel);
                m_service.addTask(task);
            }
        }
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_FETCH_NOVEL)
    protected void fetchNovel(IPPReaderMessage msg){
        PPReaderFetchNovelMessage message = (PPReaderFetchNovelMessage)msg;
        if(message.getRetCode() == ServiceError.ERR_OK){
            PPReaderNovel novel = message.getNovel();
            if(!rmNovelTask(novel)){
                return;
            }
            PPReaderSearchAdapter adapter = getAdapter();
            adapter.addNovel(novel);
            getView().findViewById(R.id.novel_search_ret_list).setVisibility(View.VISIBLE);
            if(m_service.isIdle() && (m_urls == null || (m_urls != null && m_urls.size() == 0)) ){
                removeFootView();
            }
            else{
                insertFootView();
            }
            getView().findViewById(R.id.novel_search_loading_mask).setVisibility(View.GONE);
        }
    }


    private void insertFootView() {
        ListView lv = (ListView) getView().findViewById(R.id.novel_search_ret_list);
        if (lv == null || lv.getChildCount() == 0 || m_service.isIdle() || lv.getFooterViewsCount() == 1) {
            return;
        }
        int pos = lv.getLastVisiblePosition();
        View v = lv.getChildAt(pos);
        if (v != null && v.getBottom() >= lv.getBottom() && lv.getFooterViewsCount() == 0) {
            lv.addFooterView(m_footView);
        }
    }

    private void removeFootView(){
        ListView lv = (ListView) getView().findViewById(R.id.novel_search_ret_list);
        if (lv.getFooterViewsCount() == 1) {
            lv.removeFooterView(m_footView);
        }
    }

    private PPReaderSearchAdapter getAdapter(){
        ListView lv = getView().findViewById(R.id.novel_search_ret_list);
        PPReaderSearchAdapter adapter = null;
        if(lv.getAdapter() == null){
            return null;
        }
        if(lv.getAdapter() instanceof PPReaderSearchAdapter){
            adapter = (PPReaderSearchAdapter)lv.getAdapter();
        }
        else{
            HeaderViewListAdapter ha = (HeaderViewListAdapter)lv.getAdapter();
            adapter = (PPReaderSearchAdapter)ha.getWrappedAdapter();
        }
        return  adapter;
    }

    private boolean rmNovelTask(PPReaderNovel novel){
        for(PPReaderNovel item: m_novels){
            if(item.detailUrl.compareTo(novel.detailUrl) == 0){
                m_novels.remove(item);
                return true;
            }
        }
        return false;
    }

    private final static String KEY_URLS = "urls";
    private final static String KEY_NOVELS = "novels";

    //private IPPReaderTaskNotification m_notification;
    //private IPPReaderService m_service;
    private ArrayList<String> m_urls;
    private String m_engineName;
    private View m_footView;
    private ArrayList<PPReaderNovel> m_novels = new ArrayList<>();
}
