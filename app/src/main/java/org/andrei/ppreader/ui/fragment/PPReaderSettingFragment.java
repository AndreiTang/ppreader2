package org.andrei.ppreader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.message.PPReaderCommonMessage;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;
import org.andrei.ppreader.ui.fragment.helper.PPReaderBaseFragment;
import org.andrei.ppreader.ui.fragment.helper.PPReaderCommonRet;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class PPReaderSettingFragment extends PPReaderBaseFragment {

    public void init(final IPPReaderDataManager dataManager){
        m_dataManager = dataManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ppreader_setting, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initUI(savedInstanceState);

    }

    private void initUI(Bundle savedInstanceState){
        TextView tv = getView().findViewById(R.id.main_item_title);
        tv.setText(R.string.novel_setting_title);

        tv = getView().findViewById(R.id.main_item_left_btn);
        tv.setText(R.string.novel_setting_search);
        RxView.clicks(tv).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception{
                PPReaderCommonMessage msg = new PPReaderCommonMessage(PPReaderMessageTypeDefine.TYPE_TO_LIST_PAGE,1);
                sendMessage(msg);
            }
        });

        tv = getView().findViewById(R.id.main_item_right_btn);
        tv.setText(R.string.novel_setting_list);
        RxView.clicks(tv).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception{
                PPReaderCommonMessage msg = new PPReaderCommonMessage(PPReaderMessageTypeDefine.TYPE_TO_LIST_PAGE,0);
                sendMessage(msg);
            }
        });

        if(savedInstanceState != null){
            PPReaderPreferenceFragment fragment = (PPReaderPreferenceFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.setting_container);
            fragment.init(m_dataManager);
        }
        else{
            PPReaderPreferenceFragment fragment = new PPReaderPreferenceFragment();
            fragment.init(m_dataManager);
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.setting_container,fragment).commit();
        }
    }


    private IPPReaderDataManager m_dataManager;
}


