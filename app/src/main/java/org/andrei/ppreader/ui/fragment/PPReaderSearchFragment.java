package org.andrei.ppreader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderSearchNovelsRet;
import org.andrei.ppreader.service.PPReaderSearchUrlsRet;
import org.andrei.ppreader.service.PPReaderUpdateNovelRet;
import org.andrei.ppreader.service.PPReaderUpdateNovelsTask;
import org.andrei.ppreader.service.ServiceError;
import org.andrei.ppreader.ui.adapter.PPReaderListAdapter;
import org.andrei.ppreader.ui.adapter.PPReaderSearchAdapter;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextRet;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class PPReaderSearchFragment extends Fragment {

    public void init(final IPPReaderDataManager dataManager, final IPPReaderTaskNotification notification, final IPPReaderService service){
        m_dataManager = dataManager;
        m_notification = notification;
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
        initAdapter();
        initService();
    }

    private void initUI(){
        TextView tv = getView().findViewById(R.id.main_item_title);
        tv.setText(R.string.novel_search_title);

        tv = getView().findViewById(R.id.main_item_left_btn);
        tv.setText(R.string.novel_search_list);
        RxView.clicks(tv).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception{
                PPReaderTextRet ret = new PPReaderTextRet(PPReaderTextRet.TYPE_TO_LIST_PAGE);
                ret.index = 0;
                if(m_notification != null){
                    m_notification.onNotify(ret);
                }
            }
        });

        tv = getView().findViewById(R.id.main_item_right_btn);
        tv.setText(R.string.novel_list_search);
        RxView.clicks(tv).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception{
                PPReaderTextRet ret = new PPReaderTextRet(PPReaderTextRet.TYPE_TO_LIST_PAGE);
                ret.index = 2;
                if(m_notification != null){
                    m_notification.onNotify(ret);
                }
            }
        });

    }

    private void initAdapter(){
        ListView lv = getView().findViewById(R.id.novel_search_ret_list);
        PPReaderSearchAdapter adapter = new PPReaderSearchAdapter(this, new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {
                m_notification.onNotify(ret);
            }
        });
        lv.setAdapter(adapter);
    }

    private void initService(){
        m_service.start(new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {
                if(ret.type().compareTo(PPReaderSearchUrlsRet.class.getName()) == 0){
                    if(ret.getRetCode() == ServiceError.ERR_OK){
                        m_urls = ((PPReaderSearchUrlsRet)ret).m_urls;
                        String url= m_urls.remove(0);
                        m_service.addTask(null);
                    }
                    else{
                        getView().findViewById(R.id.novel_search_ret_list).setVisibility(View.GONE);
                        getView().findViewById(R.id.novel_search_error_mask).setVisibility(View.VISIBLE);
                        TextView tv = getView().findViewById(R .id.novel_search_err_msg);
                        if(ret.getRetCode() == ServiceError.ERR_NOT_FOUND){
                            tv.setText(R.string.err_not_found);
                        }
                        else if(ret.getRetCode() == ServiceError.ERR_NOT_NETWORK){
                            tv.setText(R.string.err_network);
                        }
                    }
                }
                else if(ret.type().compareTo(PPReaderSearchNovelsRet.class.getName()) == 0 && ret.getRetCode() == ServiceError.ERR_OK){
                    ArrayList<PPReaderNovel> novels = ((PPReaderSearchNovelsRet)ret).m_novels;
                    for(PPReaderNovel novel : novels){
                        PPReaderUpdateNovelsTask task = new PPReaderUpdateNovelsTask(novel);
                        m_service.addTask(task);
                    }
                }
                else if(ret.type().compareTo(PPReaderUpdateNovelRet.class.getName()) == 0 && ret.getRetCode() == ServiceError.ERR_OK){
                    PPReaderNovel novel = ((PPReaderUpdateNovelRet)ret).novel;
                    ListView lv = getView().findViewById(R.id.novel_search_ret_list);
                    PPReaderSearchAdapter adapter = (PPReaderSearchAdapter)lv.getAdapter();
                    adapter.addNovel(novel);
                }


            }
        });
    }

    private IPPReaderDataManager m_dataManager;
    private IPPReaderTaskNotification m_notification;
    private IPPReaderService m_service;
    private ArrayList<String> m_urls;


}
