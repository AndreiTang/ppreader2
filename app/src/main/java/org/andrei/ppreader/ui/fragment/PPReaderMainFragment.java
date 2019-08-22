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
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderCommonMessage;
import org.andrei.ppreader.service.message.PPReaderMessageType;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;
import org.andrei.ppreader.ui.adapter.PPReaderMainAdapter;
import org.andrei.ppreader.ui.fragment.helper.PPReaderBaseFragment;
import org.andrei.ppreader.ui.fragment.helper.PPReaderSelectNovelRet;
import org.andrei.ppreader.ui.fragment.helper.PPReaderCommonRet;

public class PPReaderMainFragment extends PPReaderBaseFragment {

    public void init(final IPPReaderDataManager dataManager,final IPPReaderServiceFactory serviceFactory){
        m_dataManager = dataManager;
        m_serviceFactory = serviceFactory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ppreader_main, container, false);
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

//    @Override
//    public void onNotify(IPPReaderTaskRet ret) {
//        if(ret.type().compareTo(PPReaderSelectNovelRet.class.getName()) == 0 && m_notification != null){
//            m_notification.onNotify(ret);
//        }
//    }

    @Override
    public void onHiddenChanged(boolean hidden){
        super.onHiddenChanged(hidden);
        if(!hidden){
            getActivity().findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @PPReaderMessageType(type= PPReaderMessageTypeDefine.TYPE_TO_LIST_PAGE)
    public void switchFragment(final IPPReaderMessage msg){
        PPReaderCommonMessage swMsg = (PPReaderCommonMessage)msg;
        ViewPager vp = (ViewPager)getView().findViewById(R.id.main_viewpager);
        vp.setCurrentItem(swMsg.getValue());
    }

    public void addNovel(final PPReaderNovel novel){
        m_listFragment.addNovel(novel);
    }

    private void firstRun(Fragment[] fragments){
        PPReaderListFragment listFragment = new PPReaderListFragment();
        fragments[0] = listFragment;
        listFragment.init(m_dataManager, m_serviceFactory.createServiceInstance());
        m_listFragment = listFragment;

        PPReaderSearchFragment searchFragment = new PPReaderSearchFragment();
        fragments[1] = searchFragment;
        searchFragment.init(m_serviceFactory.createServiceInstance());

        PPReaderSettingFragment settingFragment = new PPReaderSettingFragment();
        fragments[2] = settingFragment;
        settingFragment.init(m_dataManager);
    }

    private void resume(Bundle savedInstanceState,final Fragment[] fragments){

        int index = savedInstanceState.getInt(KEY_INDEX);
        ViewPager vp = (ViewPager)getView().findViewById(R.id.main_viewpager);
        vp.setCurrentItem(index);

        getChildFragmentManager().getFragments().toArray(fragments);

        PPReaderListFragment listFragment = (PPReaderListFragment)fragments[0];
        listFragment.init(m_dataManager, m_serviceFactory.createServiceInstance());
        m_listFragment = listFragment;

        PPReaderSearchFragment searchFragment = (PPReaderSearchFragment)fragments[1];
        searchFragment.init(m_serviceFactory.createServiceInstance());

        PPReaderSettingFragment settingFragment = (PPReaderSettingFragment)fragments[2];
        settingFragment.init(m_dataManager);
    }

    private void init(final Fragment[] fragments){
        getActivity().findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ViewPager vp = getView().findViewById(R.id.main_viewpager);
        vp.setOffscreenPageLimit(2);

        PPReaderMainAdapter adapter = new PPReaderMainAdapter(this.getChildFragmentManager(), fragments);
        vp.setAdapter(adapter);
    }


    private PPReaderListFragment m_listFragment;
    private IPPReaderDataManager m_dataManager;
   // private IPPReaderTaskNotification m_notification;
    private IPPReaderServiceFactory m_serviceFactory;
    private final static String KEY_INDEX = "key_index";



}
