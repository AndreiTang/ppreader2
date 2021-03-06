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
import org.andrei.ppreader.service.IPPReaderServiceFactory;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderCommonMessage;
import org.andrei.ppreader.service.message.PPReaderMessageType;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;
import org.andrei.ppreader.ui.adapter.PPReaderMainAdapter;

public class PPReaderMainFragment extends PPReaderBaseFragment implements IPPReaderMainFragmentNotification  {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ppreader_main, container, false);
    }

    public void addOnNotification(IPPReaderMainFragmentNotification notification){
        m_notification = notification;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Fragment[] fragments = new Fragment[3];
        if(savedInstanceState == null) {
            firstRun(fragments);
        }
        else{
            resume(savedInstanceState,fragments);
        }

        init(fragments);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        ViewPager vp = (ViewPager)getView().findViewById(R.id.main_viewpager);
        int index = vp.getCurrentItem();
        savedInstanceState.putInt(KEY_INDEX,index);
    }


    @Override
    public void onHiddenChanged(boolean hidden){
        super.onHiddenChanged(hidden);
        if(!hidden){
            //m_listFragment.invalidate();
            getActivity().findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    //@PPReaderMessageType(type= PPReaderMessageTypeDefine.TYPE_TO_LIST_PAGE)
    public void switchFragment(int index){
        ViewPager vp = (ViewPager)getView().findViewById(R.id.main_viewpager);
        vp.setCurrentItem(index);
    }

    @Override
    public void onOpenNovel(PPReaderNovel novel) {
        if(m_notification != null){
            m_notification.onOpenNovel(novel);
        }
    }

    public void invalidate(){
        m_listFragment.invalidate();
    }

    @Override
    public void onSwitchPage(int index) {
        switchFragment(index);
    }


    private void firstRun(Fragment[] fragments){
        PPReaderListFragment listFragment = new PPReaderListFragment();
        fragments[0] = listFragment;

        PPReaderSearchFragment searchFragment = new PPReaderSearchFragment();
        fragments[1] = searchFragment;

        PPReaderSettingFragment settingFragment = new PPReaderSettingFragment();
        fragments[2] = settingFragment;
    }

    private void resume(Bundle savedInstanceState,final Fragment[] fragments){

        int index = savedInstanceState.getInt(KEY_INDEX);
        ViewPager vp = (ViewPager)getView().findViewById(R.id.main_viewpager);
        vp.setCurrentItem(index);

        getChildFragmentManager().getFragments().toArray(fragments);
    }

    private void init(final Fragment[] fragments){
        getActivity().findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ViewPager vp = getView().findViewById(R.id.main_viewpager);
        vp.setOffscreenPageLimit(2);

        PPReaderMainAdapter adapter = new PPReaderMainAdapter(this.getChildFragmentManager(), fragments);
        vp.setAdapter(adapter);

        PPReaderListFragment listFragment = (PPReaderListFragment)fragments[0];
        listFragment.addOnNotification(this);
        m_listFragment = listFragment;

        PPReaderSearchFragment searchFragment = (PPReaderSearchFragment)fragments[1];
        searchFragment.addOnNotification(this);

        PPReaderSettingFragment settingFragment = (PPReaderSettingFragment)fragments[2];
        settingFragment.addOnNotification(this);
    }

    private final static String KEY_INDEX = "key_index";
    private IPPReaderMainFragmentNotification m_notification;
    private PPReaderListFragment m_listFragment;
}
