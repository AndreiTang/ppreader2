package org.andrei.ppreader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.andrei.ppreader.R;
import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.ui.adapter.PPReaderMainAdapter;
import org.andrei.ppreader.ui.fragment.helper.PPReaderSelectNovelRet;

import io.reactivex.functions.Consumer;

public class PPReaderMainFragment extends Fragment implements IPPReaderTaskNotification {

    public void init(final IPPReaderDataManager dataManager,final IPPReaderTaskNotification notification){
        m_dataManager = dataManager;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ppreader_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        PPReaderListFragment lineFragment = (PPReaderListFragment)m_fragments[0];
        lineFragment.init(m_dataManager,this);

        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        PPReaderMainAdapter adapter = new PPReaderMainAdapter(this.getChildFragmentManager(),m_fragments);
        ViewPager vp = (ViewPager)getView().findViewById(R.id.main_viewpager);
        vp.setAdapter(adapter);
    }

    @Override
    public void onNotify(IPPReaderTaskRet ret) {
        if(ret.type().compareTo(PPReaderSelectNovelRet.class.getName()) == 0 && m_notification != null){
            m_notification.onNotify(ret);
        }
    }

    public void switchFragment(int index){
        ViewPager vp = (ViewPager)getView().findViewById(R.id.main_viewpager);
        vp.setCurrentItem(index);
    }

    public void addNovel(final PPReaderNovel novel){
        PPReaderListFragment fragment = (PPReaderListFragment)m_fragments[0];
        fragment.addNovel(novel);
    }

    private Fragment[] m_fragments = {new PPReaderListFragment(),new PPReaderSearchFragment(),new PPReaderSettingFragment()};
    private IPPReaderDataManager m_dataManager;
    private IPPReaderTaskNotification m_notification;


}
