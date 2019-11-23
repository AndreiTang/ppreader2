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
import org.andrei.ppreader.data.PPReaderChapter;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.data.PPReaderTextPage;
import org.andrei.ppreader.service.IPPReaderServiceNotification;
import org.andrei.ppreader.service.task.PPReaderUpdateNovelTask;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderCommonMessage;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;
import org.andrei.ppreader.service.message.PPReaderUpdateNovelMessage;
import org.andrei.ppreader.ui.adapter.PPReaderListAdapter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class PPReaderListFragment extends PPReaderBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ppreader_list, container, false);
    }

    public void addOnNotification(IPPReaderMainFragmentNotification notification){
        m_notification = notification;
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

    private void initService(){
        m_service.start(new IPPReaderServiceNotification() {
            @Override
            public void onNotify(IPPReaderMessage msg) {
                if(msg.type().compareTo(PPReaderMessageTypeDefine.TYPE_UPDATE_NOVEL) == 0){
                    updateNovel(msg);
                }
            }
        });
    }

    //@PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_UPDATE_NOVEL)
    private void updateNovel(IPPReaderMessage msg){
        PPReaderUpdateNovelMessage message = (PPReaderUpdateNovelMessage) msg;
        PPReaderNovel novel = m_dataManager.getNovel(message.getId());
        if(novel == null){
            return;
        }
        int delta = message.getDelta().size() - novel.chapters.size();
        if(delta > 0){
            novel.isUpdated = true;
            novel.needValidate = true;
            novel.type = message.getNovelType();
            int index = novel.chapters.size();
            novel.chapters.addAll(message.getDelta().subList( index,message.getDelta().size()));
            for(PPReaderChapter chapter : message.getDelta()){
                PPReaderTextPage page = new PPReaderTextPage();
                page.chapterId = chapter.id;
                page.chapterIndex = index;
                index++;
                novel.textPages.add(page);
            }
            GridView gv = getView().findViewById(R.id.novel_list);
            PPReaderListAdapter adapter = (PPReaderListAdapter)gv.getAdapter();
            if(adapter != null){
                adapter.notifyDataSetChanged();
            }
        }
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
                if(m_notification != null){
                    m_notification.onSwitchPage(1);
                }
            }
        });

    }

    private void initAdapter(){
        GridView gv = getView().findViewById(R.id.novel_list);
        PPReaderListAdapter adapter = new PPReaderListAdapter(this, new PPReaderListAdapter.IPPReaderListAdapterAction() {
            @Override
            public void openNovel(PPReaderNovel novel) {
                if(m_notification != null){
                    m_notification.onOpenNovel(novel);
                }
            }
        });
        gv.setAdapter(adapter);
    }

    private IPPReaderMainFragmentNotification m_notification;

}
