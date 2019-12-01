package org.andrei.ppreader;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.andrei.ppreader.data.IPPReaderDataManager;
import org.andrei.ppreader.data.PPReaderDataManager;
import org.andrei.ppreader.data.PPReaderEngineInfo;
import org.andrei.ppreader.data.PPReaderNovel;
import org.andrei.ppreader.service.PPReaderServiceFactory;
import org.andrei.ppreader.service.engine.IPPReaderNovelEngineManager;
import org.andrei.ppreader.service.engine.PPReaderNovelEngineManager;

import org.andrei.ppreader.ui.fragment.IPPReaderMainFragmentNotification;
import org.andrei.ppreader.ui.fragment.IPPReaderNovelTextFragmentNotification;
import org.andrei.ppreader.ui.fragment.PPReaderMainFragment;
import org.andrei.ppreader.ui.fragment.PPReaderStartFragment;
import org.andrei.ppreader.ui.fragment.PPReaderNovelTextFragment;

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

        m_engineManager = new PPReaderNovelEngineManager();
        PPReader.setDataManager(new PPReaderDataManager());
        m_dataManager = PPReader.getDataManager();
        PPReader.setServiceFactory(new PPReaderServiceFactory(m_engineManager,m_dataManager) );

        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            firstRun();
        }
        else{
            restoreFromSaveInstance(savedInstanceState);
        }
    }

    @Override
    public void onStop(){
        Context appContext = getApplicationContext();
        String path = appContext.getExternalFilesDir(null).getPath();
        m_dataManager.save(path);
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);

        PPReaderNovelTextFragment text = (PPReaderNovelTextFragment)getSupportFragmentManager().findFragmentByTag(PPReaderNovelTextFragment.class.getName());

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
        PPReaderNovelTextFragment text = (PPReaderNovelTextFragment)getSupportFragmentManager().findFragmentByTag(PPReaderNovelTextFragment.class.getName());

        if(main == null || main.isVisible()){
            super.onBackPressed();
            return;
        }
        else{
            //main.switchFragment(0);
            text.backPress();

        }
    }

    private void restoreFromSaveInstance(Bundle savedInstanceState){
        Log.i("Andrei","restart");
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

        PPReaderMainFragment main = (PPReaderMainFragment)getSupportFragmentManager().findFragmentByTag(PPReaderMainFragment.class.getName());
        PPReaderNovelTextFragment text = (PPReaderNovelTextFragment)getSupportFragmentManager().findFragmentByTag(PPReaderNovelTextFragment.class.getName());

        if(frag == 1){
            getSupportFragmentManager().beginTransaction().hide(main).show(text).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().hide(text).show(main).commit();
        }

        initTextFragment(main,text);
        initMainFragment(main,text);
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
                int ret = loadData();
                e.onNext(ret);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
               buildFragments();
            }
        });
    }

    private void buildFragments(){
        PPReaderMainFragment main = new PPReaderMainFragment();
        PPReaderNovelTextFragment text = new PPReaderNovelTextFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.ppreader_root,main,PPReaderMainFragment.class.getName()).
                add(R.id.ppreader_root,text, PPReaderNovelTextFragment.class.getName()).
                hide(text).
                commit();
        initTextFragment(main,text);
        initMainFragment(main,text);
    }

    private int loadData(){
        Context appContext = getApplicationContext();
        String path = appContext.getExternalFilesDir(null).getPath();
        int ret = m_dataManager.load(path);
        if(m_dataManager.getEngineInfoCount() != m_engineManager.count()  ){
            ArrayList<PPReaderEngineInfo> infos = new ArrayList<>();
            for(int i = 0; i < m_engineManager.count(); i++){
                PPReaderEngineInfo info = new PPReaderEngineInfo();
                info.name = m_engineManager.get(i).getName();
                if(m_dataManager.getEngineInfo(info.name)!=null){
                    info.isUsed = m_dataManager.getEngineInfo(info.name).isUsed;
                }
                info.contentUrl = m_engineManager.get(i).getContentUrl();
                info.imageUrl = m_engineManager.get(i).getImageUrl();
                infos.add(info);
            }
            m_dataManager.setEngineInfos(infos);
        }
        return ret;
    }

    private void initTextFragment(final PPReaderMainFragment main, final PPReaderNovelTextFragment text){
        text.addOnNotification(new IPPReaderNovelTextFragmentNotification() {
            @Override
            public void onSwitchFragment(int index) {
                main.switchFragment(index);
                getSupportFragmentManager().beginTransaction().show(main).hide(text).commit();
            }

            @Override
            public void onRefresh(boolean isValidate) {
                if(isValidate){
                    Context appContext = getApplicationContext();
                    String path = appContext.getExternalFilesDir(null).getPath();
                    m_dataManager.save(path);
                    main.invalidate();
                }
                getSupportFragmentManager().beginTransaction().show(main).commit();
                main.switchFragment(0);
                getSupportFragmentManager().beginTransaction().hide(text).commit();

            }
        });
    }

    private void initMainFragment(final PPReaderMainFragment main, final PPReaderNovelTextFragment text){
        main.addOnNotification(new IPPReaderMainFragmentNotification() {
            @Override
            public void onOpenNovel(PPReaderNovel novel) {
                getSupportFragmentManager().beginTransaction().hide(main).show(text).commit();
                PPReaderNovel item = m_dataManager.getNovel(novel.id);
                if(item != null){
                    text.setNovel(item);
                }
                else{
                    text.setNovel(novel);
                }

            }

            @Override
            public void onSwitchPage(int index) {

            }

            @Override
            public void onAddChapters(PPReaderNovel novel) {
                text.refresh(novel);
            }
        });

    }

    private final static String KEY_FRAGMENTS = "key_fragments";
    private final static String KEY_NOVELS = "key_novels";
    private final static String KEY_INFOS = "key_infos";
    private IPPReaderDataManager m_dataManager;
    private IPPReaderNovelEngineManager m_engineManager;
}
