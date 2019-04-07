package org.andrei.ppreader.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderUpdateNovelRet;
import org.andrei.ppreader.service.PPReaderUpdateNovelTask;
import org.andrei.ppreader.ui.adapter.PPReaderListAdapter;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextRet;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class PPReaderListFragment extends Fragment {


    public PPReaderListFragment() {
        // Required empty public constructor
    }

    public void init(final IPPReaderDataManager dataManager, final IPPReaderTaskNotification notification, final IPPReaderService service){
        m_dataManager = dataManager;
        m_notification = notification;
        m_service = service;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ppreader_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initUI();
        initAdapter();
        initService();

        for( int i = 0; i < m_dataManager.getNovelCount(); i++ ){
            PPReaderUpdateNovelTask task = new PPReaderUpdateNovelTask(m_dataManager.getNovel(i));
            m_service.addTask(task);
        }
    }

    public void addNovel(final PPReaderNovel novel){
        m_dataManager.addNovel(novel);
    }


    private void initUI(){
        TextView tv = getView().findViewById(R.id.main_item_title);
        tv.setText(R.string.novel_list_title);

        tv = getView().findViewById(R.id.main_item_left_btn);
        tv.setText(R.string.novel_list_edit);
        RxView.clicks(tv).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception{
                GridView gv = getView().findViewById(R.id.novel_list);
                PPReaderListAdapter adapter = (PPReaderListAdapter)gv.getAdapter();
                boolean isEditMode = adapter.isEditMode();
                adapter.setEditMode(!isEditMode);
                TextView tv = getView().findViewById(R.id.main_item_left_btn);
                if(isEditMode){
                    tv.setText(R.string.novel_list_edit);
                }
                else{
                    tv.setText(R.string.novel_list_remove);
                }
            }
        });

        tv = getView().findViewById(R.id.main_item_right_btn);
        tv.setText(R.string.novel_list_search);
        RxView.clicks(tv).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception{
                PPReaderTextRet ret = new PPReaderTextRet(PPReaderTextRet.TYPE_TO_LIST_PAGE);
                ret.index = 1;
                if(m_notification != null){
                    m_notification.onNotify(ret);
                }
            }
        });

    }

    private void initAdapter(){
        GridView gv = getView().findViewById(R.id.novel_list);
        PPReaderListAdapter adapter = new PPReaderListAdapter(this,m_dataManager, new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {
                if(m_notification != null){
                    m_notification.onNotify(ret);
                }
            }
        });
        gv.setAdapter(adapter);
    }

    private void initService(){
        m_service.start(new IPPReaderTaskNotification() {
            @Override
            public void onNotify(IPPReaderTaskRet ret) {
                PPReaderUpdateNovelRet r = (PPReaderUpdateNovelRet)ret;
                PPReaderNovel novel = m_dataManager.getNovel(r.novel.id);
                if(novel == null){
                    return;
                }
                if(r.delta.size() > 0){
                    novel.isUpdated = true;
                    novel.type = r.type;
                    novel.chapters.addAll(r.delta);
                    GridView gv = getView().findViewById(R.id.novel_list);
                    PPReaderListAdapter adapter = (PPReaderListAdapter)gv.getAdapter();
                    if(adapter != null){
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private IPPReaderDataManager m_dataManager;
    private IPPReaderTaskNotification m_notification;
    private IPPReaderService m_service;

}
