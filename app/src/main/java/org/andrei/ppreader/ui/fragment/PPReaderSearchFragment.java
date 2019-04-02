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
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.ui.adapter.PPReaderListAdapter;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextRet;

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

    private IPPReaderDataManager m_dataManager;
    private IPPReaderTaskNotification m_notification;
    private IPPReaderService m_service;


}
