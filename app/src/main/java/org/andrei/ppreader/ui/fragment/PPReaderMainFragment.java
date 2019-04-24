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
import org.andrei.ppreader.ui.adapter.PPReaderMainAdapter;
import org.andrei.ppreader.ui.fragment.helper.PPReaderSelectNovelRet;
import org.andrei.ppreader.ui.fragment.helper.PPReaderCommonRet;

public class PPReaderMainFragment extends Fragment implements IPPReaderTaskNotification {

    public void init(final IPPReaderDataManager dataManager, final IPPReaderTaskNotification notification, final IPPReaderServiceFactory serviceFactory){
        m_dataManager = dataManager;
        m_notification = notification;
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

        getActivity().findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ViewPager vp = getView().findViewById(R.id.main_viewpager);
        vp.setOffscreenPageLimit(2);
        Fragment[] fragments = new Fragment[3];

        if(savedInstanceState == null) {

            PPReaderListFragment listFragment = new PPReaderListFragment();
            fragments[0] = listFragment;
            listFragment.init(m_dataManager, this, m_serviceFactory.createServiceInstance());
            m_listFragment = listFragment;

            PPReaderSearchFragment searchFragment = new PPReaderSearchFragment();
            fragments[1] = searchFragment;
            searchFragment.init(this, m_serviceFactory.createServiceInstance());

            PPReaderSettingFragment settingFragment = new PPReaderSettingFragment();
            fragments[2] = settingFragment;
            settingFragment.init(m_dataManager, this);

        }
        else{
            int index = savedInstanceState.getInt(KEY_INDEX);
            vp.setCurrentItem(index);

            getChildFragmentManager().getFragments().toArray(fragments);

            PPReaderListFragment listFragment = (PPReaderListFragment)fragments[0];
            listFragment.init(m_dataManager, this, m_serviceFactory.createServiceInstance());
            m_listFragment = listFragment;

            PPReaderSearchFragment searchFragment = (PPReaderSearchFragment)fragments[1];
            searchFragment.init(this, m_serviceFactory.createServiceInstance());

            PPReaderSettingFragment settingFragment = (PPReaderSettingFragment)fragments[2];
            settingFragment.init(m_dataManager, this);
        }

        PPReaderMainAdapter adapter = new PPReaderMainAdapter(this.getChildFragmentManager(), fragments);
        vp.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        ViewPager vp = (ViewPager)getView().findViewById(R.id.main_viewpager);
        int index = vp.getCurrentItem();
        savedInstanceState.putInt(KEY_INDEX,index);
    }

    @Override
    public void onNotify(IPPReaderTaskRet ret) {
        if(ret.type().compareTo(PPReaderSelectNovelRet.class.getName()) == 0 && m_notification != null){
            m_notification.onNotify(ret);
        }
        else if(ret.type().compareTo(PPReaderCommonRet.TYPE_TO_LIST_PAGE) == 0 && m_notification != null){
            PPReaderCommonRet tr = (PPReaderCommonRet)ret;
            switchFragment(tr.index);
        }
    }

    public void switchFragment(int index){
        ViewPager vp = (ViewPager)getView().findViewById(R.id.main_viewpager);
        vp.setCurrentItem(index);
    }

    public void addNovel(final PPReaderNovel novel){
        m_listFragment.addNovel(novel);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private PPReaderListFragment m_listFragment;
    private IPPReaderDataManager m_dataManager;
    private IPPReaderTaskNotification m_notification;
    private IPPReaderServiceFactory m_serviceFactory;
    private final static String KEY_INDEX = "key_index";



}
