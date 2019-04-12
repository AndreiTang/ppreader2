package org.andrei.ppreader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import org.andrei.ppreader.R;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.ui.fragment.helper.PPReaderTextRet;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class PPReaderSettingFragment extends Fragment {

    public void init(final IPPReaderTaskNotification notification){
        m_notification = notification;
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
        initUI();

    }



    private void initUI(){
        TextView tv = getView().findViewById(R.id.main_item_title);
        tv.setText(R.string.novel_setting_title);

        tv = getView().findViewById(R.id.main_item_left_btn);
        tv.setText(R.string.novel_setting_search);
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

        tv = getView().findViewById(R.id.main_item_right_btn);
        tv.setText(R.string.novel_setting_list);
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

        Fragment fragment = new PPReaderPreferenceFragment();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.setting_container,fragment).commit();



    }

    private IPPReaderTaskNotification m_notification;
}


