package org.andrei.ppreader;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderDataManager;
import org.andrei.ppreader.data.PPReaderEngineInfo;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.IPPReaderService;
import org.andrei.ppreader.service.IPPReaderServiceFactory;
import org.andrei.ppreader.service.IPPReaderTaskNotification;
import org.andrei.ppreader.service.IPPReaderTaskRet;
import org.andrei.ppreader.service.PPReaderServiceFactory;
import org.andrei.ppreader.service.PPReaderUpdateNovelRet;
import org.andrei.ppreader.service.message.IPPReaderMessage;
import org.andrei.ppreader.service.message.PPReaderAddNovelMessage;
import org.andrei.ppreader.service.message.PPReaderMessageCenter;
import org.andrei.ppreader.service.message.PPReaderMessageType;
import org.andrei.ppreader.service.message.PPReaderMessageTypeDefine;
import org.andrei.ppreader.ui.adapter.PPReaderBaseAdapter;
import org.andrei.ppreader.ui.fragment.PPReaderBaseFragment;
import org.andrei.ppreader.ui.fragment.PPReaderMainFragment;
import org.andrei.ppreader.ui.fragment.PPReaderStartFragment;
import org.andrei.ppreader.ui.fragment.PPReaderTextFragment;
import org.andrei.ppreader.ui.fragment.helper.PPReaderAddNovelRet;
import org.andrei.ppreader.ui.fragment.helper.PPReaderSelectNovelRet;
import org.andrei.ppreader.ui.fragment.helper.PPReaderCommonRet;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends FragmentActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context appContext = getApplicationContext();
        String path = appContext.getExternalFilesDir(null).getPath();
        m_dataManager = new PPReaderDataManager(path);
        PPReaderBaseAdapter.setDataManager(m_dataManager);
        PPReaderBaseFragment.setDataManager(m_dataManager);
        PPReaderBaseAdapter.setMessageCenter(PPReaderMessageCenter.instance());
        PPReaderBaseFragment.setMessageCenter(PPReaderMessageCenter.instance());
        PPReaderBaseFragment.setServiceFactory(new PPReaderServiceFactory(m_dataManager));

        if(savedInstanceState == null){
            firstRun();
        }
        else{

            int frag = savedInstanceState.getInt(KEY_FRAGMENTS,-1);
            if(frag == -1){
                firstRun();
                return;
            }
            ArrayList<PPReaderNovel> novels = (ArrayList<PPReaderNovel>) savedInstanceState.getSerializable(KEY_NOVELS);
            for(PPReaderNovel novel : novels){
                m_dataManager.addNovel(novel);
            }

            ArrayList<PPReaderEngineInfo> infos = (ArrayList<PPReaderEngineInfo>)savedInstanceState.getSerializable(KEY_INFOS);
            m_dataManager.setEngineInfos(infos);

            IPPReaderServiceFactory factory = new PPReaderServiceFactory(m_dataManager);

            PPReaderMainFragment main = (PPReaderMainFragment)getSupportFragmentManager().findFragmentByTag(PPReaderMainFragment.class.getName());

            PPReaderTextFragment text = (PPReaderTextFragment)getSupportFragmentManager().findFragmentByTag(PPReaderTextFragment.class.getName());
            text.init(factory.createServiceInstance());

            if(frag == 1){
                getSupportFragmentManager().beginTransaction().hide(main).show(text).commit();
            }
            else{
                getSupportFragmentManager().beginTransaction().hide(text).show(main).commit();
            }

        }

        changeStatusBarColor();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        m_dataManager.save();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        PPReaderTextFragment text = (PPReaderTextFragment)getSupportFragmentManager().findFragmentByTag(PPReaderTextFragment.class.getName());

        int frag = 0;
        if(text.isVisible()){
            frag = 1;
        }
        savedInstanceState.putInt(KEY_FRAGMENTS,frag);

        ArrayList<PPReaderNovel> novels = new ArrayList<>();
        for(int i = 0 ; i < m_dataManager.getNovelCount(); i++){
            novels.add(m_dataManager.getNovel(i));
        }
        savedInstanceState.putSerializable(KEY_NOVELS,novels);

        ArrayList<PPReaderEngineInfo> infos = new ArrayList<>();
        for(int i = 0; i < m_dataManager.getEngineInfoCount(); i++){
            infos.add(m_dataManager.getEngineInfo(i));
        }
        savedInstanceState.putSerializable(KEY_INFOS,infos);
    }

    @Override
    public void onBackPressed(){
        PPReaderMainFragment main = (PPReaderMainFragment)getSupportFragmentManager().findFragmentByTag(PPReaderMainFragment.class.getName());
        PPReaderTextFragment text = (PPReaderTextFragment)getSupportFragmentManager().findFragmentByTag(PPReaderTextFragment.class.getName());

        if(main == null || main.isVisible()){
            super.onBackPressed();
            return;
        }
        else{
            //main.switchFragment(0);
            getSupportFragmentManager().beginTransaction().show(main).hide(text).commit();
        }
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_TO_LIST_PAGE)
    protected void switchToListFragment(IPPReaderMessage msg){
        Fragment main = getSupportFragmentManager().findFragmentByTag(PPReaderMainFragment.class.getName());
        Fragment text = getSupportFragmentManager().findFragmentByTag(PPReaderTextFragment.class.getName());
        getSupportFragmentManager().beginTransaction().hide(text).show(main).commit();
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_SELECT_NOVEL)
    protected  void switchToTextFragment(IPPReaderMessage msg){
        Fragment main = getSupportFragmentManager().findFragmentByTag(PPReaderMainFragment.class.getName());
        Fragment text = getSupportFragmentManager().findFragmentByTag(PPReaderTextFragment.class.getName());
        getSupportFragmentManager().beginTransaction().hide(main).show(text).commit();
    }

    @PPReaderMessageType(type = PPReaderMessageTypeDefine.TYPE_ADD_NOVEL)
    protected void addNovel(IPPReaderMessage msg){
        PPReaderAddNovelMessage message = (PPReaderAddNovelMessage)msg;
        m_dataManager.addNovel(message.getNovel());
    }



    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.setStatusBarColor(Color.parseColor("#DBC49B"));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void firstRun(){
        Fragment start = new PPReaderStartFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.ppreader_root,start).commit();


        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                int ret = m_dataManager.load();
                e.onNext(ret);
                Thread.sleep(3000);
                e.onNext(1);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                IPPReaderServiceFactory serviceFactory = new PPReaderServiceFactory(m_dataManager);

                PPReaderMainFragment main = new PPReaderMainFragment();
                PPReaderTextFragment text = new PPReaderTextFragment();

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.ppreader_root,main,PPReaderMainFragment.class.getName()).
                        add(R.id.ppreader_root,text,PPReaderTextFragment.class.getName()).
                        hide(text).
                        commit();
            }
        });
    }

    private final static String KEY_FRAGMENTS = "key_fragments";
    private final static String KEY_NOVELS = "key_novels";
    private final static String KEY_INFOS = "key_infos";
    private IPPReaderDataManager m_dataManager;
}
